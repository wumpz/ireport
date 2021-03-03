/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table;

import com.jaspersoft.ireport.components.table.undo.AddTableColumnUndoableEdit;
import com.jaspersoft.ireport.components.table.undo.DeleteTableColumnUndoableEdit;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.ImageUtilities;

/**
 *
 * @version $Id: TableMatrix.java 0 2010-03-25 13:27:48 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class TableMatrix {

    private List<TableCell> cells = new ArrayList<TableCell>();
    private List<Integer> verticalSeparators = new ArrayList<Integer>();
    private List<Integer> horizontalSeparators = new ArrayList<Integer>();

    private StandardTable table = null;
    private JasperDesign jasperDesign = null;

    public TableMatrix(JasperDesign jd, StandardTable table)
    {
        this.jasperDesign = jd;
        this.table = table;
        processMatrix();
    }

    public void processMatrix()
    {
        if (getTable() == null || getJasperDesign() == null) return;

        JRDesignDataset dataset = getJasperDesign().getMainDesignDataset();
        if (table.getDatasetRun() != null && table.getDatasetRun().getDatasetName() != null)
        {
            dataset = (JRDesignDataset) getJasperDesign().getDatasetMap().get(table.getDatasetRun().getDatasetName());
        }
        processMatrix(dataset);
    }
    /**
     * This method will update all the strucures....
     */
    public synchronized void processMatrix(JRDesignDataset dataset)
    {
        cells.clear();
        getVerticalSeparators().clear();
        getHorizontalSeparators().clear();

        createVerticalSeparators();

        // Paint the table...cell by cell...
        List<BaseColumn> columns = getTable().getColumns();
        int x = 0;
        int y = 0;
        int col = 0;
        int row = 0;

        byte[] sections = new byte[]{ TableCell.TABLE_HEADER,
                                  TableCell.COLUMN_HEADER,
                                  TableCell.GROUP_HEADER,
                                  TableCell.DETAIL,
                                  TableCell.GROUP_FOOTER,
                                  TableCell.COLUMN_FOOTER,
                                  TableCell.TABLE_FOOTER };

        horizontalSeparators.add(0);
        for (byte sectionType :  sections)
        {
            int iterations = 1;
            if ( (sectionType == TableCell.GROUP_HEADER || sectionType == TableCell.GROUP_FOOTER) )
            {
                iterations = (dataset != null) ? dataset.getGroupsList().size() : 0;
            }
            for (int k=0; k<iterations; ++k)
            {
                String groupName = null;
                if (dataset != null && (sectionType == TableCell.GROUP_HEADER || sectionType == TableCell.GROUP_FOOTER))
                {
                    groupName = ((JRGroup)dataset.getGroupsList().get(k)).getName();
                }

                int sectionHeight = getSectionHeight(sectionType, groupName);
                int sectionRows = getSectionRows(sectionType, groupName);
                // Add the h lines for this sections...
                for (int i=0; i<sectionRows-1; ++i) getHorizontalSeparators().add(0);
                for (BaseColumn c : columns)
                {
                    Point pos = processSection(c, x, y, row, col, sectionHeight, sectionRows, sectionType, groupName);
                    x += pos.x;
                    col += pos.y;
                }
                x=0;
                y += sectionHeight;
                row += sectionRows;
                col = 0;
                getHorizontalSeparators().add(y);
            }
        }

        // Recalculate horizontal separators... yes, we have to recalculate them, since some column heights maybe wrong.
        // Too hard explain the case...ok let's explain it:
        // A group has header 15 and children with header 15, total group height: 30. Then I find a group with header 30 and null child headers. Fucked up,
        // since I screwd up the bottom line Y of the children of the previous column group. Got it?
        for (int i=1; i<getHorizontalSeparators().size(); ++i) // Starting from 1 since the first horizontal lineis always 0
        {
            // Find the max height of line i
            int maxRowHeight = 0;
            boolean hasCells = false;

            // Find the max height of this row.
            // The max height comes from the max height of a cell on this row.
            for (TableCell cell : cells)
            {
                if (cell.getRow() + cell.getRowSpan() == i)
                {
                    hasCells = true;
                    break;
                }
            }

            for (TableCell cell : cells)
            {
                
                // Find a cell with bottom row this row...
                // New fix: if the row does not contain any cell which this row as top border, the height of this line must be set to 0!
                if (cell.getRow() + cell.getRowSpan() == i)
                {
                    int cellHeight = (cell.getCell() != null && cell.getCell().getHeight() != null) ? cell.getCell().getHeight().intValue() : 0;
                    if (cellHeight == 0) continue; // not useful...

                    // If the row span is > 1, remove the caluclated previous row heights
                    if (cell.getRowSpan() > 1) // easy, get it!
                    {
                        // THIS IS THE MAIN TRICK WHICH WILL FIX THE PROBLEM REPORTED ON TOP OF THIS SCOPE!!!
                        int previousSpannedRowsHeight = getHorizontalSeparators().get(cell.getRow()) - getHorizontalSeparators().get(cell.getRow()+cell.getRowSpan()-1);
                        cellHeight -= -previousSpannedRowsHeight;

                        // BUT! if this row has not cell... remove the additional height...
                        if (hasCells) cellHeight = 0;
                    }

                    if (cellHeight <= 0) continue; // again, not only this is not useful, but if cellHeight < 0 there is a design problem we may want to fix in iReport....
                    maxRowHeight = (cellHeight > maxRowHeight) ? cellHeight  : maxRowHeight;
                }
            }
            getHorizontalSeparators().set(i, maxRowHeight + getHorizontalSeparators().get(i-1));
        }

        
    }

    /**
     * Analize the specified section for the specified column...
     * Returns the total column width.
     */
    private Point processSection(BaseColumn column, int x, int y, int row, int col, int availableHeight, int availableRows, byte sectionType, String groupName)
    {
        Point p = new Point(column.getWidth(),1);
        DesignCell cell = (DesignCell)getCellOfType(column, sectionType, groupName);
        if (column instanceof StandardColumn)
        {
            addCell(x, y, row, col, column, cell , availableRows, 1, sectionType, groupName);
            return p;
        }
        else if (column instanceof StandardColumnGroup)
        {
            int exceedingVerticalCells = 0; // <- Row span -1
            int rowHeight = 0;
            if (cell != null)
            {
                rowHeight = cell.getHeight();
                if (cell.getRowSpan() != null)
                {
                    exceedingVerticalCells = cell.getRowSpan()-1;
                    if (exceedingVerticalCells <0) exceedingVerticalCells = 0;
                    int nestedRows = getSectionRows((StandardColumnGroup)column, sectionType, groupName);
                    if (exceedingVerticalCells+1 > availableRows-nestedRows) exceedingVerticalCells = availableRows-nestedRows-1; // Fix the span if not consistent...
                }
            }

            int nextRow = row;
            int nextAvailableRows = availableRows;
            if (sectionType != TableCell.DETAIL)
            {
                int horiz = getHorizontalSeparators().get(row+exceedingVerticalCells+1);
                if (horiz < y+rowHeight) getHorizontalSeparators().set(row+exceedingVerticalCells+1, y+rowHeight);
                nextRow = row+1+exceedingVerticalCells;
                nextAvailableRows = availableRows-1-exceedingVerticalCells;
            }
            
            // analyze the subcolumns
            List<BaseColumn> subColumns = ((StandardColumnGroup)column).getColumns();
            // Check sub columns...
            int subCol = 0;
            int subX = 0;
            for (BaseColumn subColumn : subColumns)
            {
                Point coords = processSection(subColumn, x+subX, y+rowHeight, nextRow, col+subCol, availableHeight-rowHeight,nextAvailableRows, sectionType, groupName);
                subX += coords.x;
                subCol += coords.y;
            }

            // Now we know all about this group header...
            if (sectionType != TableCell.DETAIL)
            {
                addCell(x, y, row, col, column, cell , 1+exceedingVerticalCells, subCol, sectionType, groupName );
            }
            p.x = x + subX;
            p.y = subCol;

        }

        return p;

    }

    private void addCell( int x, int y, int row, int col, BaseColumn baseColumn, DesignCell cell, int rowSpan, int colSpan, byte cellType, String groupName)
    {
        TableCell tableCell = new TableCell();
        tableCell.setCol(col);
        tableCell.setRow(row);
        tableCell.setType(cellType);
        tableCell.setGroupName(groupName);
        tableCell.setCell(cell);
        tableCell.setColSpan(colSpan);
        tableCell.setRowSpan(rowSpan);
        tableCell.setColumn(baseColumn);

        cells.add(tableCell);
    }


    private int getSectionHeight(byte sectionType)
    {
        return getSectionHeight(sectionType, null);
    }


    // To check the section height, we must ge 
    private int getSectionHeight(byte sectionType, String groupName)
    {
        int height = 0;
        List<BaseColumn> columns = getTable().getColumns();
        for (BaseColumn c : columns)
        {
            if (c instanceof StandardColumn)
            {
                Cell cell = getCellOfType(c, sectionType, groupName);
                if (cell != null && cell.getHeight().intValue() > height) height = cell.getHeight().intValue();
            }
            else if (c instanceof StandardColumnGroup)
            {
                int gh = getSectionHeight((StandardColumnGroup)c, sectionType, groupName );
                height = (gh > height) ? gh : height;
            }
        }
        return height;

    }
    
    private int getSectionHeight(StandardColumnGroup standardColumnGroup, byte sectionType, String groupName)
    {
        int headerHeight = 0;
        Cell cell = getCellOfType(standardColumnGroup, sectionType, groupName);
        if (cell != null)
        {
            headerHeight = cell.getHeight();
        }
        List<BaseColumn> columns = standardColumnGroup.getColumns();
        // Check sub columns...
        int maxChildrenHeaderHeight = 0;
        for (BaseColumn c : columns)
        {
            if (c instanceof StandardColumn)
            {
                cell = getCellOfType(c, sectionType, groupName);
                if (cell != null && cell.getHeight().intValue() > maxChildrenHeaderHeight) maxChildrenHeaderHeight = cell.getHeight().intValue();
            }
            else if (c instanceof StandardColumnGroup)
            {
                int gh = getSectionHeight((StandardColumnGroup)c, sectionType, groupName);
                maxChildrenHeaderHeight = (gh > maxChildrenHeaderHeight) ? gh : maxChildrenHeaderHeight;
            }
        }

        return headerHeight + maxChildrenHeaderHeight;

    }

    private int getSectionRows(byte sectionType)
    {
        return getSectionRows(sectionType, null);
    }

    private int getSectionRows(byte sectionType, String groupName)
    {
        return getSectionRows(null, sectionType, groupName);
    }


    private int getSectionRows(Object parent, byte sectionType, String groupName)
    {
        if (sectionType == TableCell.DETAIL) return 1;
        List<BaseColumn> columns = (parent == null) ? getTable().getColumns() : TableModelUtils.getColumns(parent);
        int maxChildRows = 0;
        for (BaseColumn c : columns)
        {
            if (c instanceof StandardColumnGroup)
            {
                int rows = getSectionRows((StandardColumnGroup)c, sectionType, groupName );
                maxChildRows = (rows > maxChildRows) ? rows : maxChildRows;
            }
        }
        return maxChildRows+1;
    }

    

    private Cell getCellOfType(BaseColumn col, byte sectionType, String groupName)
    {
        if (col == null) return null;
        switch (sectionType)
        {
            case TableCell.TABLE_HEADER: return col.getTableHeader();
            case TableCell.COLUMN_HEADER: return col.getColumnHeader();
            case TableCell.GROUP_HEADER: return col.getGroupHeader(groupName);
            case TableCell.TABLE_FOOTER: return col.getTableFooter();
            case TableCell.COLUMN_FOOTER: return col.getColumnFooter();
            case TableCell.GROUP_FOOTER: return col.getGroupFooter(groupName);
            case TableCell.DETAIL:
            {
                if (col instanceof StandardColumn) return ((StandardColumn)col).getDetailCell();
            }
        }
        return null;
    }

    /**
     * @return the cells
     */
    public List<TableCell> getCells() {
        return cells;
    }

    private void createVerticalSeparators() {

        verticalSeparators = new ArrayList<Integer>();
        int pos = 0;
        verticalSeparators.add(pos);
        List<StandardColumn> cols = getStandardColumns();
        for (StandardColumn c : cols)
        {
            pos += c.getWidth();
            verticalSeparators.add(pos);
        }
    }

  
    /**
     * @return the verticalSeparators
     */
    public List<Integer> getVerticalSeparators() {
        return verticalSeparators;
    }

    /**
     * @return the horizontalSeparators
     */
    public List<Integer> getHorizontalSeparators() {
        return horizontalSeparators;
    }

    public Point getCellLocation(DesignCell designCell) {

        for (TableCell tc : cells)
        {
            if (tc.getCell() == designCell)
            {
                return  getCellLocation(tc);
            }
        }

        return new Point(0,0);
    }

    public Point getCellLocation(TableCell tc) {
        return new Point( getVerticalSeparators().get(tc.getCol()), getHorizontalSeparators().get(tc.getRow()));
    }

    public Rectangle getCellBounds(DesignCell designCell) {

        for (TableCell tc : cells)
        {
            if (tc.getCell() == designCell)
            {
                return getCellBounds(tc);
            }
        }

        return null;
    }

    public Rectangle getCellBounds(TableCell tc) {

        int x = getVerticalSeparators().get(tc.getCol());
        int y = getHorizontalSeparators().get(tc.getRow());
        int w = getVerticalSeparators().get(tc.getCol() + tc.getColSpan()) - x;
        int h = getHorizontalSeparators().get(tc.getRow() + tc.getRowSpan()) - y;
        return new Rectangle(x,y,w,h);
    }

    /**
     * @return the table
     */
    public StandardTable getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(StandardTable table) {
        this.table = table;
    }

    public StandardColumn getColumn(int i) {

        return getStandardColumns().get(i);
    }

    public List<StandardColumn> getStandardColumns() {

        List<StandardColumn> standardColumns = new ArrayList<StandardColumn>();
        List<BaseColumn> columns = getTable().getColumns();
        for (BaseColumn c : columns)
        {
            standardColumns.addAll(getStandardColumns(c));
        }
        return standardColumns;
    }

    /**
     * REturns the position of each line.
     * startingWidth is the starting point from left.
     * @param col
     * @return
     */
    private List<StandardColumn> getStandardColumns(BaseColumn col) {

        List<StandardColumn> standardColumns = new ArrayList<StandardColumn>();

        if (col instanceof StandardColumn)
        {
            standardColumns.add((StandardColumn) col);
        }
        else if (col instanceof StandardColumnGroup)
        {
            StandardColumnGroup columnGroup = (StandardColumnGroup)col;
            List<BaseColumn> columns = columnGroup.getColumns();
            for (BaseColumn c : columns)
            {
                standardColumns.addAll(getStandardColumns(c));
            }
        }
        return standardColumns;
    }

    /**
     * Check if this is the bottom line of at least one not null cell...
     * @param i
     * @return
     */
    boolean isValidBase(int i) {

        for (TableCell cell : getCells())
        {
            if (cell.getRow()+1 == i && cell.getRowSpan() == 1 && cell.getCell() != null) return true;
        }

        return false;

    }

    public TableCell getCellAt(Point point) {

        for (TableCell cell : getCells())
        {
            if ( getCellBounds(cell).contains(point))
            {
                return cell;
            }
        }

        return null;
    }

    public BaseColumn getColumnAt(Point point)
    {
        for (TableCell cell : getCells())
        {
            Rectangle r = getCellBounds(cell);
            if (point.x >= r.x && point.x <= r.x + r.width) return cell.getColumn();
        }
        return null;
    }

    public TableCell findTableCell(BaseColumn column, byte section, String grpName) {

        for (TableCell cell : getCells())
        {
            if (cell.getColumn() == column &&
                cell.getType() == section)
            {
                if ( (cell.getType() == TableCell.GROUP_FOOTER || cell.getType() == TableCell.GROUP_HEADER) &&
                      !cell.getGroupName().equals(grpName))
                {
                    continue;
                }

                // TableCell found!
                return cell;
            }
        }

        return null;

    }

    /**
     * Returns the bounds of the header cell...
     * @param column
     * @return
     */
    public Rectangle getColumnBounds(BaseColumn column) {

        for (TableCell cell : cells)
        {
            if (cell.getColumn() == column && cell.getType() == TableCell.TABLE_HEADER)
            {
                Rectangle r = getCellBounds(cell);
                r.height = getHorizontalSeparators().get( getHorizontalSeparators().size()-1) - r.y;
                return r;
            }
        }

        return null;
    }


    /**
     * Returns the bounds of the header cell...
     * @param column
     * @return
     */
    public Rectangle getSectionBounds(byte section, String grpName) {

        Rectangle rect = null;
        for (TableCell cell : cells)
        {
            if (cell.getType() == section)
            {
                if (cell.getGroupName() != null && !cell.getGroupName().equals(grpName)) continue;
                
                Rectangle r = getCellBounds(cell);
                if (rect == null)
                {
                    rect = r;
                }
                else
                {
                    if (r.x < rect.x) rect.x = r.x;
                    if (r.y < rect.y) rect.y = r.y;
                    if ((r.y+r.height) > (rect.y + rect.height)) rect.height = r.y+r.height - rect.y;
                    if ((r.x+r.width) > (rect.x + rect.width)) rect.width = r.x+r.width - rect.x;
                }
            }
        }

        return rect;
    }

    public int getColumnIndex(BaseColumn column) {


        // Find the first cell with this column index..
        for (TableCell cell : cells)
        {
            if (cell.getColumn() == column)
            {
                return cell.getCol();
            }
        }
        return -1;

    }

    public Object getColumnParent(BaseColumn column) {

        List<BaseColumn> columns = getTable().getColumns();
        for (BaseColumn c : columns)
        {
            if (c == column) return getTable();
            if (c instanceof StandardColumnGroup)
            {
                StandardColumnGroup parent = getColumnParent(column, (StandardColumnGroup)c);
                if (parent != null) return parent;
            }
        }
        return null;
    }

    private StandardColumnGroup getColumnParent(BaseColumn column, StandardColumnGroup standardColumnGroup) {
        List<BaseColumn> columns = standardColumnGroup.getColumns();
        for (BaseColumn c : columns)
        {
            if (c == column) return standardColumnGroup;
            if (c instanceof StandardColumnGroup)
            {
                StandardColumnGroup parent = getColumnParent(column, (StandardColumnGroup)c);
                if (parent != null) return parent;
            }
        }
        return null;
    }


    /**
     * Check if the second column (parent) is really an ancestor of the first column argument
     *
     * @param child - the column to test
     * @param parent - the column to traver looking for child
     * @return the method returns true is child==parent or if child is contaied in one of the recursive children of parent
     */
    public static boolean isAncestorColumnOf(BaseColumn child, BaseColumn parent) {

        if (parent == child) return true;
        if (parent instanceof StandardColumnGroup)
        {
            for (BaseColumn c : ((StandardColumnGroup)parent).getColumns() )
            {
                if (isAncestorColumnOf(child, c)) return true;
            }
        }
        return false;
    }


    /**
     * This method allows to move a column from its current parent to another parent.
     * If the old parent remains without cells, the user is asked to continue and delete it or not.
     * Notification events are sent if required.
     * Undo edits are added to the current undoable stack (via IReportManager)
     *
     * Additional checks are performed like check if the column and the new parent belog to the same table.
     * In negative case, nothing is done.
     *
     * @param column - the column to move
     * @param newParent - the new parent (StantadardTable or StandardColumnGroup)
     * @param newPosition - the position in the new parent, or -1 to add the column at the end
     */
    public void moveColumn(BaseColumn column, Object newParent, int newPosition)
    {
        if (column == null) return;
        if (newParent == null) return;
        if (!(newParent instanceof StandardColumnGroup || newParent instanceof StandardTable)) return;

        // Check both the columns belong to this table...
        Object aParent = newParent;
        while (!(aParent instanceof StandardTable))
        {
            aParent = getColumnParent((BaseColumn)aParent);
            if (aParent == null) return;
        }
        if (aParent != getTable()) return;

        aParent = column;
        while (!(aParent instanceof StandardTable))
        {
            aParent = getColumnParent((BaseColumn)aParent);
            if (aParent == null) return;
        }
        if (aParent != getTable()) return;


        // Check that we are not trying to add a parent to a child or nephew...
        if (newParent instanceof StandardColumnGroup)
        {
            if (TableMatrix.isAncestorColumnOf((StandardColumnGroup)newParent, column)) return;
        }

        // ----------
        // Collect all the requires informations...
        Object oldParent = getColumnParent(column);

        List<BaseColumn> oldColumns = (oldParent instanceof StandardTable) ? ((StandardTable)oldParent).getColumns() : ((StandardColumnGroup)oldParent).getColumns();
        List<BaseColumn> newColumns = (newParent instanceof StandardTable) ? ((StandardTable)newParent).getColumns() : ((StandardColumnGroup)newParent).getColumns();

        if (newParent == oldParent)
        {
            // If we are in the same parent, that's easy!!!
            if (oldColumns.size() == 1) return; // Done ;-) Nothing to do here!
            else
            {
                int oldIndex = oldColumns.indexOf(column);
                if (newPosition > oldIndex) newPosition--;
                oldColumns.remove(column);

                if (newPosition < 0 || newPosition > oldColumns.size()) newPosition = oldColumns.size();

                oldColumns.add(newPosition, column);

                if (newParent instanceof StandardTable)
                {
                    ((StandardTable)newParent).getEventSupport().firePropertyChange( StandardColumnGroup.PROPERTY_COLUMNS, null, oldColumns);
                }
                else
                {
                    ((StandardColumnGroup)newParent).getEventSupport().firePropertyChange( StandardColumnGroup.PROPERTY_COLUMNS, null, oldColumns);
                }

                // Create the undo edit..
                DeleteTableColumnUndoableEdit edit = new DeleteTableColumnUndoableEdit(getTable(),getJasperDesign(),  column, oldParent, oldIndex);
                AddTableColumnUndoableEdit edit2 = new AddTableColumnUndoableEdit(getTable(),getJasperDesign(),column, newParent, newPosition);
                edit.concatenate(edit2);
                edit.setPresentationName("Move Column");
                IReportManager.getInstance().addUndoableEdit(edit);
                TableModelUtils.fixTableLayout(getTable(), getJasperDesign());
            }
        }
        else
        {
            // things are a bit more hard in this case.
            // 1. we have to remove the column from the oldParent first...
            if (oldColumns.size() == 1)
            {
                if (JOptionPane.showConfirmDialog(
                        Misc.getMainFrame(),
                        "You are moving a column from a group which contains only this column.\nA column group must have at least a column.\n"+
                        "If you continue, the column group will be removed.\n\nContinue anyway?",
                        "Deleting Column Group",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        ImageUtilities.image2Icon(ImageUtilities.loadImage("com/jaspersoft/ireport/components/table/deleting_group.png"))) != JOptionPane.OK_OPTION) return;
            }

            int oldIndex = oldColumns.indexOf(column);
            oldColumns.remove(column);

            DeleteTableColumnUndoableEdit edit = new DeleteTableColumnUndoableEdit(getTable(),getJasperDesign(),column, oldParent, oldIndex);

            // Remove the column groups with 0 child columns from their parent recursively...
            while (oldColumns.size() == 0 && oldParent instanceof StandardColumnGroup)
            {
                Object oldParentParent = getColumnParent((StandardColumnGroup)oldParent);
                if (oldParentParent != null) // This shoule be ALWAYS true....
                {
                    if (oldParentParent instanceof StandardTable)
                    {
                        oldIndex = ((StandardTable)oldParentParent).getColumns().indexOf((StandardColumnGroup)oldParent);
                        ((StandardTable)oldParentParent).removeColumn((StandardColumnGroup)oldParent);
                        edit.concatenate(new DeleteTableColumnUndoableEdit(getTable(),getJasperDesign(),(StandardColumnGroup)oldParent, oldParentParent, oldIndex));
                        oldColumns = ((StandardTable)oldParentParent).getColumns();
                        oldParent = oldParentParent;
                    }
                    else
                    {
                        oldIndex = ((StandardColumnGroup)oldParentParent).getColumns().indexOf((StandardColumnGroup)oldParent);
                        ((StandardColumnGroup)oldParentParent).removeColumn((StandardColumnGroup)oldParent);
                        edit.concatenate(new DeleteTableColumnUndoableEdit(getTable(),getJasperDesign(),(StandardColumnGroup)oldParent, oldParentParent, oldIndex));
                        oldColumns = ((StandardColumnGroup)oldParentParent).getColumns();
                        oldParent = oldParentParent;
                    }
                }
            }
            // Notify the change in the old parent (or its ancestor...) if required...
            if (oldParent != newParent)
            {
                if (oldParent instanceof StandardTable)
                {
                    ((StandardTable)oldParent).getEventSupport().firePropertyChange( StandardColumnGroup.PROPERTY_COLUMNS, null, oldColumns);
                }
                else
                {
                    ((StandardColumnGroup)oldParent).getEventSupport().firePropertyChange( StandardColumnGroup.PROPERTY_COLUMNS, null, oldColumns);
                }
            }

            // Add the column to the new parent
            if (newPosition < 0 || newPosition > newColumns.size()) newPosition = newColumns.size();
            newColumns.add(newPosition, column );

            // Notify the new column owner...
            if (newParent instanceof StandardTable)
            {
                ((StandardTable)newParent).getEventSupport().firePropertyChange( StandardColumnGroup.PROPERTY_COLUMNS, null, newColumns);
            }
            else
            {
                ((StandardColumnGroup)newParent).getEventSupport().firePropertyChange( StandardColumnGroup.PROPERTY_COLUMNS, null, newColumns);
            }

            AddTableColumnUndoableEdit edit2 = new AddTableColumnUndoableEdit(getTable(),getJasperDesign(),column, newParent, newPosition);
            edit.concatenate(edit2);
            edit.setPresentationName("Move Column");
            
            IReportManager.getInstance().addUndoableEdit(edit);
            TableModelUtils.fixTableLayout(getTable(), getJasperDesign());
        }
    }


    /**
     * The design height is the position of the last bottom line.
     * @return
     */
    public int getTableDesignHeight()
    {
        return getHorizontalSeparators().get( getHorizontalSeparators().size()-1);
    }

    /**
     * The design width is the position of the last vertical line
     * @return
     */
    public int getTableDesignWidth()
    {
        return getVerticalSeparators().get( getVerticalSeparators().size()-1);
    }

    public TableCell getTableCell(DesignCell designCell)
    {
        for (TableCell cell : cells)
        {
            if (cell.getCell() == designCell) return cell;
        }
        return null;
    }


    /**
     * Returns the max rowSpan for the specified cell.
     * @param designCell
     * @return 1 or more.
     */
    public int getMaxRowSpan(DesignCell designCell)
    {
        TableCell cell = getTableCell(designCell);

        if (cell.getColumn() instanceof StandardColumn) return 1; // StandardColumn cells cannot span..

        int totalSectionRows = getSectionRows(getColumnParent(cell.getColumn()), (byte)cell.getType(), cell.getGroupName());
        int otherRows = getSectionRows((StandardColumnGroup) cell.getColumn(), (byte)cell.getType(), cell.getGroupName());

        return totalSectionRows-otherRows;
    }

    /**
     * @return the jasperDesign
     */
    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    /**
     * @param jasperDesign the jasperDesign to set
     */
    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }

}
