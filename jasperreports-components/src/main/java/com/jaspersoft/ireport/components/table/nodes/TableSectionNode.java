/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table.nodes;

import com.jaspersoft.ireport.components.table.TableCell;
import com.jaspersoft.ireport.components.table.TableMatrix;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnEndAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnGroupAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnStartAction;
import com.jaspersoft.ireport.designer.dnd.DnDUtilities;
import com.jaspersoft.ireport.designer.outline.nodes.IRIndexedNode;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @version $Id: TableSectionNode.java 0 2010-03-26 00:07:19 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class TableSectionNode  extends IRIndexedNode {

    JasperDesign jd = null;
    private JRDesignComponentElement tableElement = null;
    private byte sectionType = 0;
    private JRDesignGroup group = null;

    private PropertyChangeListener groupNameListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                updateDisplayName();
            }
    };

     public TableSectionNode(JasperDesign jd,  JRDesignComponentElement tableElement, byte sectionType, JRDesignGroup group, Lookup doLkp) {
        this(new TableSectionChildren(jd, tableElement, sectionType, group, doLkp), jd, tableElement, sectionType, group,doLkp);
    }

    public TableSectionNode(TableSectionChildren pc, JasperDesign jd, JRDesignComponentElement tableElement, byte sectionType, JRDesignGroup group, Lookup doLkp)
    {
        super(pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.fixed(jd, tableElement)));
        this.jd = jd;
        this.tableElement = tableElement;
        this.sectionType = sectionType;
        this.group = group;

        updateDisplayName();

        if (group != null)
        {
            group.getEventSupport().addPropertyChangeListener(JRDesignGroup.PROPERTY_NAME, groupNameListener);
        }
        
        setName("column");

        /*
        this.addNodeListener(new NodeListener() {

            public void childrenAdded(NodeMemberEvent ev) {}
            public void childrenRemoved(NodeMemberEvent ev) {}
            public void nodeDestroyed(NodeEvent ev) {}
            public void propertyChange(PropertyChangeEvent evt) {}

            @SuppressWarnings("unchecked")
            public void childrenReordered(NodeReorderEvent ev) {
                // Fire an event now...

                System.out.println("Elements reordered!!!");
                System.out.flush();

                List columns = getTable().getColumns();
                int[] permutations = ev.getPermutation();

                Object[] elementsArray = new Object[columns.size()];
                for (int i=0; i<elementsArray.length; ++i)
                {
                    elementsArray[permutations[i]] = columns.get(i);
                }
                columns.clear();
                for (int i=0; i<elementsArray.length; ++i)
                {
                    columns.add(elementsArray[i]);
                }

                
                //getTable().getEventSupport().removePropertyChangeListener((PropertyChangeListener)getChildren());
                // Force just a redraw...
                getTable().getEventSupport().firePropertyChange( StandardColumnGroup.PROPERTY_WIDTH, null, getTable().getColumns());
                //getTable().getEventSupport().addPropertyChangeListener((PropertyChangeListener)getChildren());

            }
        });

         */

    }


    public void updateDisplayName()
    {
        switch (sectionType)
        {
            case TableCell.TABLE_HEADER:
            {
                setIconBaseWithExtension("com/jaspersoft/ireport/components/table/table-headers.png");
                setDisplayName(I18n.getString("TableHeaderNode.name"));
                break;
            }
            case TableCell.COLUMN_HEADER:
            {
                setIconBaseWithExtension("com/jaspersoft/ireport/components/table/column-headers.png");
                setDisplayName(I18n.getString("ColumnHeaderNode.name"));
                break;
            }
            case TableCell.GROUP_HEADER:
            {
                setIconBaseWithExtension("com/jaspersoft/ireport/components/table/group-headers.png");
                setDisplayName(I18n.getString("GroupHeaderNode.name", group.getName()));
                break;
            }
            case TableCell.DETAIL:
            {
                setIconBaseWithExtension("com/jaspersoft/ireport/components/table/table-detail.png");
                setDisplayName(I18n.getString("DetailNode.name"));
                break;
            }
            case TableCell.GROUP_FOOTER:
            {
                setIconBaseWithExtension("com/jaspersoft/ireport/components/table/group-footers.png");
                setDisplayName(I18n.getString("GroupFooterNode.name", group.getName()));
                break;
            }
            case TableCell.COLUMN_FOOTER:
            {
                setIconBaseWithExtension("com/jaspersoft/ireport/components/table/column-footers.png");
                setDisplayName(I18n.getString("ColumnFooterNode.name"));
                break;
            }
            case TableCell.TABLE_FOOTER:
            {
                setIconBaseWithExtension("com/jaspersoft/ireport/components/table/table-footers.png");
                setDisplayName(I18n.getString("TableFooterNode.name"));
                break;
            }

        }
    }
    /**
     * @return false (always)
     */
    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    public JRDesignComponentElement getComponentElement() {
        return tableElement;
    }

    public StandardTable getTable() {
        return (StandardTable)getComponentElement().getComponent();
    }

    public JasperDesign getJasperDesign()
    {
        return jd;
    }

    /**
     * @return the sectionType
     */
    public byte getSectionType() {
        return sectionType;
    }

    /**
     * @param sectionType the sectionType to set
     */
    public void setSectionType(byte sectionType) {
        this.sectionType = sectionType;
    }


        @Override
    public PasteType getDropType(Transferable t, final int action, final int index) {

        final Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        final int dropAction = DnDUtilities.getTransferAction(t);

        if (null != dropNode && index >= 0) {

            final BaseColumn column = dropNode.getLookup().lookup(BaseColumn.class);
            
            if (null != column) {

                // check if the column belongs to this table...
                final TableMatrix matrix = new TableMatrix(getJasperDesign(), getTable());

                if (matrix.getColumnIndex(column) < 0) return null;

                return new PasteType() {

                    @SuppressWarnings("unchecked")
                    public Transferable paste() throws IOException {

                        matrix.moveColumn(column, getTable(), index);
                        return null;
                    }
                };
            }
        }
        return null;
    }


         @Override
    public Action[] getActions(boolean popup) {

        java.util.List<Action> list = new ArrayList<Action>();

        list.add( SystemAction.get(AddTableColumnStartAction.class));
        list.add( SystemAction.get(AddTableColumnEndAction.class));
        list.add( SystemAction.get(AddTableColumnGroupAction.class));
        
        return list.toArray(new Action[list.size()]);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void createPasteTypes(Transferable t, List s) {
        super.createPasteTypes(t, s);
        PasteType paste = getDropType(t, DnDConstants.ACTION_MOVE, -1);
        if (null != paste) {
            s.add(paste);
        }
    }

    /**
     * @return the group
     */
    public JRDesignGroup getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(JRDesignGroup group) {
        this.group = group;
    }
}
