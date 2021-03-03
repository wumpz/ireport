/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of iReport.
 *
 * iReport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * iReport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with iReport. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.ireport.components.table;

import com.jaspersoft.ireport.components.table.actions.AddTableColumnEndAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnGroupAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnStartAction;
import com.jaspersoft.ireport.components.table.nodes.TableCellNode;
import com.jaspersoft.ireport.components.table.nodes.TableChildren;
import com.jaspersoft.ireport.components.table.nodes.TableColumnGroupNode;
import com.jaspersoft.ireport.components.table.nodes.TableNullCellNode;
import com.jaspersoft.ireport.components.table.nodes.TableSectionNode;
import com.jaspersoft.ireport.components.table.nodes.properties.WhenNoDataTypeProperty;
import com.jaspersoft.ireport.components.table.undo.AddTableCellUndoableEdit;
import com.jaspersoft.ireport.components.table.undo.DeleteTableCellUndoableEdit;
import com.jaspersoft.ireport.components.table.undo.DeleteTableColumnUndoableEdit;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.undo.AggregatedUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.GroupCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardGroupCell;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;



/**
 *
 * @author gtoffoli
 */
public class TableElementNode extends ElementNode {

    JRDesignDataset currentDataset = null;
    PropertyChangeListener datasetRunChangeListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                 datasetPropertyChanged(evt);
            }
    };

    StandardTable table = null;
    public TableElementNode(JasperDesign jd, JRDesignElement element,Children children, Index index, Lookup doLkp)
    {
        super(jd, element, children, index, doLkp);
        setIconBaseWithExtension("com/jaspersoft/ireport/components/table/table-16.png");

        table = (StandardTable)((JRDesignComponentElement)element).getComponent();


        updateDatasetRunListeners();

    }





    @Override
    public String getDisplayName() {
        return I18n.getString("TableElementNode.name");
    }

    /**
     *  This is used internally to understand if the element can accept other elements...
     */
    @Override
    public boolean canPaste() {
        return true;
    }



    
    

    @Override
    public Action[] getActions(boolean popup) {

        List<Action> actions = new ArrayList<Action>();
        Action[] originalActions = super.getActions(popup);

        actions.add(SystemAction.get(EditDatasetRunAction.class));
        actions.add(SystemAction.get(AddTableColumnStartAction.class));
        actions.add(SystemAction.get(AddTableColumnEndAction.class));
        actions.add( SystemAction.get(AddTableColumnGroupAction.class));

        for (int i=0; i<originalActions.length; ++i)
        {
            actions.add(originalActions[i]);
        }
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    protected Sheet createSheet() {
        
        Sheet sheet = super.createSheet();
        
        // adding common properties...
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Table");
        set.setDisplayName(I18n.getString("TableElementNode.name"));
        //DesignListContents contents = (DesignListContents) ((StandardListComponent)((JRDesignComponentElement)this.getElement()).getComponent()).getContents();
        //set.put(new PrintOrderProperty((StandardListComponent)((JRDesignComponentElement)this.getElement()).getComponent()));

        set.put(new WhenNoDataTypeProperty((StandardTable)((JRDesignComponentElement)this.getElement()).getComponent()));
        sheet.put( set);
        
        return sheet;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
       
        super.propertyChange(evt);
    }


    public StandardTable getTable()
    {
        return (StandardTable)((JRDesignComponentElement)getElement()).getComponent();
    }


    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);

        if (dropNode == null ||
            dropNode instanceof TableCellNode ||
            dropNode instanceof TableNullCellNode ||
            dropNode instanceof TableColumnGroupNode ||
            dropNode instanceof TableSectionNode) return null;

        return super.getDropType(t, action, index);
    }

    private void updateDatasetRunListeners() {

        table.getEventSupport().removePropertyChangeListener(StandardTable.PROPERTY_DATASET_RUN,datasetRunChangeListener);
        table.getEventSupport().addPropertyChangeListener(StandardTable.PROPERTY_DATASET_RUN, datasetRunChangeListener);

        // Add listeners to the dataset poitned by this dataset run...
        JRDesignDataset dataset = null;
        if (table.getDatasetRun() != null && table.getDatasetRun().getDatasetName() != null)
        {
            dataset = (JRDesignDataset) getJasperDesign().getDatasetMap().get(table.getDatasetRun().getDatasetName());
        }

        if (dataset == currentDataset) return;
        if (dataset != currentDataset)
        {
            if (currentDataset != null)
            {
                currentDataset.getEventSupport().removePropertyChangeListener(datasetRunChangeListener);
                
                // remove all the old group listeners...
                List groups = currentDataset.getGroupsList();
                for (int i=0; i<groups.size(); ++i)
                {
                    JRDesignGroup group = (JRDesignGroup) groups.get(i);
                    group.getEventSupport().removePropertyChangeListener(JRDesignGroup.PROPERTY_NAME, datasetRunChangeListener);
                }
            }

            currentDataset = dataset;
            dataset.getEventSupport().addPropertyChangeListener(datasetRunChangeListener);

            // remove all the old group listeners...
            List groups = currentDataset.getGroupsList();
            for (int i=0; i<groups.size(); ++i)
            {
                JRDesignGroup group = (JRDesignGroup) groups.get(i);
                group.getEventSupport().removePropertyChangeListener(JRDesignGroup.PROPERTY_NAME, datasetRunChangeListener);
                group.getEventSupport().addPropertyChangeListener(JRDesignGroup.PROPERTY_NAME, datasetRunChangeListener);
            }
        }
    }

    protected void datasetPropertyChanged(PropertyChangeEvent evt) {

        boolean refreshTableStructure = false;

        if (evt.getPropertyName().equals(JRDesignDataset.PROPERTY_NAME))
        {
            if (evt.getSource() instanceof JRDesignDataset)
            {
                ((JRDesignDatasetRun)table.getDatasetRun()).setDatasetName(currentDataset.getName() );
            }
            else if (evt.getSource() instanceof JRDesignGroup)
            {
                // For each column in the table we need to update the group name reference...
                updateColumnGroupName(table.getColumns(), (String)evt.getOldValue(),(String)evt.getNewValue());
                System.out.println("updating group:  " + evt.getOldValue() + " " + evt.getNewValue());
                System.out.flush();
                table.getEventSupport().firePropertyChange(StandardTable.PROPERTY_COLUMNS, null, table.getColumns());
                refreshTableStructure = true;
            }
        }

        if (evt.getPropertyName().equals(JRDesignDataset.PROPERTY_GROUPS))
        {
            AggregatedUndoableEdit edit = removeLostColumnSections();
            // Remove no longer referenced table sections...
            if (edit != null)
            {
                IReportManager.getInstance().addUndoableEdit(edit);
            }
            refreshTableStructure = true;
        }

        if (evt.getPropertyName().equals(StandardTable.PROPERTY_DATASET_RUN))
        {
            refreshTableStructure = true;


        }

        if (refreshTableStructure)
        {
            updateDatasetRunListeners();
            ((TableChildren)getChildren()).recalculateKeys();
        }
    }

    private void updateColumnGroupName(List<BaseColumn> columns, String oldValue, String newValue)
    {
        for (BaseColumn col : columns)
        {
                for (GroupCell header : col.getGroupHeaders())
                {
                    if (header.getGroupName().equals(oldValue))
                    {
                        ((StandardGroupCell)header).setGroupName(newValue);
                    }
                }
                for (GroupCell footer : col.getGroupFooters())
                {
                    if (footer.getGroupName().equals(oldValue))
                    {
                        ((StandardGroupCell)footer).setGroupName(newValue);
                    }
                }

                if (col instanceof StandardColumnGroup)
                {
                    updateColumnGroupName(((StandardColumnGroup)col).getColumns(), oldValue, newValue);
                }
        }
    }
    
    
    /**
     * Update the table model removing groups no longer in the dataset.
     * This operation cannot be undo.
     * @param columns
     * @param oldValue
     * @param newValue
     */
    private AggregatedUndoableEdit removeLostColumnSections() {
        
        // for each column, check the header and footer cells...
        // if the name is not in the dataset, remove the cell (undo delete cell...)
        List<String> validGroupNames = new ArrayList<String>();
        List groups = currentDataset.getGroupsList();
        for (int i=0; i<groups.size(); ++i)
        {
            JRGroup group = (JRGroup)groups.get(i);
            validGroupNames.add(group.getName());
        }

        return removeGroupCellFrom( table.getColumns(), validGroupNames);
    }



    private AggregatedUndoableEdit removeGroupCellFrom(List<BaseColumn> columns,  List<String> validGroupNames) {

        AggregatedUndoableEdit edit = null;
        for (int i = columns.size()-1; i>=0; --i)
        {
            BaseColumn col = columns.get(i);

            for (GroupCell header : col.getGroupHeaders())
            {
                if (header.getCell() != null && !validGroupNames.contains(header.getGroupName()))
                {
                    AggregatedUndoableEdit ed = deleteCell(col, (DesignCell) header.getCell(), TableCell.GROUP_HEADER, header.getGroupName() );
                    if (edit == null) edit = ed;
                    else if (ed != null) edit.concatenate(ed);
                    break;
                }
            }
            for (GroupCell footer : col.getGroupFooters())
            {
                if (footer.getCell() != null && !validGroupNames.contains(footer.getGroupName()))
                {
                    AggregatedUndoableEdit ed = deleteCell(col, (DesignCell) footer.getCell(), TableCell.GROUP_FOOTER, footer.getGroupName() ) ;
                    if (edit == null) edit = ed;
                    else if (ed != null) edit.concatenate(ed);
                    break;
                }
            }

            if (col instanceof StandardColumnGroup)
            {
                AggregatedUndoableEdit ed = removeGroupCellFrom(((StandardColumnGroup)col).getColumns(), validGroupNames);
                if (edit == null) edit = ed;
                else if (ed != null) edit.concatenate(ed);
            }

        }

        return edit;

    }

    private AggregatedUndoableEdit deleteCell(BaseColumn column, DesignCell cell, byte section, String groupName)
    {
        AggregatedUndoableEdit edit = null;

        Object parentGroup = TableModelUtils.getColumnParent(table, column);

        if (column instanceof StandardColumn)
        {
            // If this is the only column, check if this is the only cell...
            int cells = 0;
            if ( ((StandardColumn)column).getTableHeader() != null) cells++;
            if ( ((StandardColumn)column).getTableFooter() != null) cells++;
            if ( ((StandardColumn)column).getColumnHeader() != null) cells++;
            if ( ((StandardColumn)column).getColumnFooter() != null) cells++;
            if ( ((StandardColumn)column).getDetailCell() != null) cells++;

            for (GroupCell groupCell : ((StandardColumn)column).getGroupHeaders())
            {
                if (groupCell.getCell() != null) cells++;
            }

            for (GroupCell groupCell : ((StandardColumn)column).getGroupFooters())
            {
                if (groupCell.getCell() != null) cells++;
            }

            if (cells == 1)
            {

                if (TableModelUtils.getStandardColumnsCount(table.getColumns()) == 1)
                {
                    //This is the only cell of a detail column in the table, and it cannot be removed.
                    // just add a detail cell first...
                    DesignCell detailCell = new DesignCell();
                    detailCell.setHeight(30);
                    TableModelUtils.addCell(column, detailCell, TableCell.DETAIL, null);

                    AddTableCellUndoableEdit aUndo = new AddTableCellUndoableEdit(table,getJasperDesign(),detailCell,column,TableCell.DETAIL, null);
                    if (edit == null)
                    {
                        edit = aUndo;
                    }
                    else
                    {
                        edit.concatenate(aUndo);
                    }
                    cells++;

                }

                if (cells == 2)
                {
                    // Let's proceed... remove the cell from the column (this is actually not necessary...
                    TableModelUtils.removeCell(column, section, groupName);

                    DeleteTableCellUndoableEdit edit2 = new DeleteTableCellUndoableEdit(table,getJasperDesign(),cell, column, section, groupName);

                    if (edit == null) edit = edit2;
                    else
                    {
                        edit.concatenate(edit2);
                    }

                }
                else
                {
                    // Remove the column from the parent...
                    int position = TableModelUtils.getColumns(parentGroup).indexOf(column);
                    TableModelUtils.removeColumn(parentGroup, column, position);


                    DeleteTableColumnUndoableEdit edit2 = new DeleteTableColumnUndoableEdit(table,getJasperDesign(),column, parentGroup, position);
                    if (edit == null) edit = edit2;
                    else
                    {
                        edit.concatenate(edit2);
                    }
                    List<BaseColumn> oldColumns = TableModelUtils.getColumns(parentGroup);

                    // Remove all the column groups with no longer children recursively....
                    while (oldColumns.size() == 0 && parentGroup instanceof StandardColumnGroup)
                    {
                        Object oldParentParent = TableModelUtils.getColumnParent(table, (StandardColumnGroup)parentGroup);
                        if (oldParentParent != null) // This shoule be ALWAYS true....
                        {
                            if (oldParentParent instanceof StandardTable)
                            {
                                position = ((StandardTable)oldParentParent).getColumns().indexOf((StandardColumnGroup)parentGroup);
                                ((StandardTable)oldParentParent).removeColumn((StandardColumnGroup)parentGroup);
                                edit.concatenate(new DeleteTableColumnUndoableEdit(table,getJasperDesign(),(StandardColumnGroup)parentGroup, oldParentParent, position));
                                oldColumns = ((StandardTable)oldParentParent).getColumns();
                                parentGroup = oldParentParent;
                            }
                            else
                            {
                                position = ((StandardColumnGroup)oldParentParent).getColumns().indexOf((StandardColumnGroup)parentGroup);
                                ((StandardColumnGroup)oldParentParent).removeColumn((StandardColumnGroup)parentGroup);
                                edit.concatenate(new DeleteTableColumnUndoableEdit(table,getJasperDesign(),(StandardColumnGroup)parentGroup, oldParentParent, position));
                                oldColumns = ((StandardColumnGroup)oldParentParent).getColumns();
                                parentGroup = oldParentParent;
                            }
                        }
                    }
                }
                //edit.setPresentationName("Remove Cell");
                return edit;
            }
        }

        // Green light to proceed! This is the easy case!

        TableModelUtils.removeCell(column, section, groupName);

        // Now we should check if this column has at least a cell. If not, the column must be recursively removed. In that case prompt for

        DeleteTableCellUndoableEdit edit2 = new DeleteTableCellUndoableEdit(table,getJasperDesign(),cell, column, section, groupName);

        if (edit == null) edit = edit2;
        else
        {
            edit.concatenate(edit2);
        }

        return edit;
    }
}

