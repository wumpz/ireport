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
package com.jaspersoft.ireport.components.table.nodes;

import com.jaspersoft.ireport.components.table.TableElementNode;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.nodes.properties.CellStyleProperty;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnAfterAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnBeforeAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnEndAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnGroupAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnStartAction;
import com.jaspersoft.ireport.components.table.actions.DeleteTableCellAction;
import com.jaspersoft.ireport.components.table.actions.DeleteTableColumnAction;
import com.jaspersoft.ireport.components.table.actions.GroupColumnsAction;
import com.jaspersoft.ireport.components.table.actions.TableCellDoHorizontalLayoutAction;
import com.jaspersoft.ireport.components.table.actions.TableCellDoLayoutAction;
import com.jaspersoft.ireport.components.table.actions.TableCellDoVerticalLayoutAction;
import com.jaspersoft.ireport.components.table.nodes.properties.CellHeightProperty;
import com.jaspersoft.ireport.components.table.nodes.properties.CellPaddingAndBordersProperty;
import com.jaspersoft.ireport.components.table.nodes.properties.ColumnPrintWhenExpressionProperty;
import com.jaspersoft.ireport.components.table.nodes.properties.ColumnPropertyExpressionsProperty;
import com.jaspersoft.ireport.components.table.nodes.properties.ColumnWidthProperty;
import com.jaspersoft.ireport.components.table.nodes.properties.RowSpanProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.dnd.DnDUtilities;
import com.jaspersoft.ireport.designer.outline.nodes.ElementContainerChildren;
import com.jaspersoft.ireport.designer.outline.nodes.ElementGroupNode;
import com.jaspersoft.ireport.designer.outline.nodes.ElementPasteType;
import com.jaspersoft.ireport.designer.outline.nodes.IRIndexedNode;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.actions.PasteAction;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class TableCellNode extends IRIndexedNode implements PropertyChangeListener {

    JasperDesign jd = null;
    private JRDesignComponentElement tableElement = null;
    private BaseColumn column = null;
    private DesignCell cell = null;
    private byte section = 0;
    private JRGroup group = null;

     public TableCellNode(JasperDesign jd,  JRDesignComponentElement tableElement, BaseColumn column, DesignCell cell, byte section, JRGroup group, Lookup doLkp) {
        this(new ElementContainerChildren(jd, cell, doLkp), jd, tableElement, column, cell,section,group,doLkp);
    }

    public TableCellNode(ElementContainerChildren pc, JasperDesign jd, JRDesignComponentElement tableElement, BaseColumn column, DesignCell cell, byte section, JRGroup group,Lookup doLkp) {
        super(pc, pc.getIndex(), new ProxyLookup( Lookups.fixed(jd, tableElement, column, cell), doLkp) );
        this.jd = jd;
        this.column = column;
        this.tableElement = tableElement;
        this.section = section;
        this.group = group;
        this.cell = cell;

        super.setName("Table cell"); // NOI18N

        if (column instanceof StandardColumnGroup)
        {
            setIconBaseWithExtension("com/jaspersoft/ireport/components/table/header-16.png");
        }
        else
        {
            setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/cell-16.png");
        }
        cell.getEventSupport().addPropertyChangeListener(this);
        
        this.addNodeListener(new NodeListener() {

            public void childrenAdded(NodeMemberEvent ev) {}
            public void childrenRemoved(NodeMemberEvent ev) {}
            public void nodeDestroyed(NodeEvent ev) {}
            public void propertyChange(PropertyChangeEvent evt) {}

            @SuppressWarnings("unchecked")
            public void childrenReordered(NodeReorderEvent ev) {
                // Fire an event now...

                List elements = getCell().getChildren();
                int[] permutations = ev.getPermutation();

                Object[] elementsArray = new Object[elements.size()];
                for (int i=0; i<elementsArray.length; ++i)
                {
                    elementsArray[permutations[i]] = elements.get(i);
                }
                elements.clear();
                for (int i=0; i<elementsArray.length; ++i)
                {
                    elements.add(elementsArray[i]);
                }

                getCell().getEventSupport().firePropertyChange( JRDesignBand.PROPERTY_CHILDREN, null, getCell().getChildren());
            }
        });
    }

    public String getDisplayName()
    {
        if (column instanceof StandardColumnGroup)
        {
            return NbBundle.getMessage(TableElementNode.class, "table.groupCell.name", (TableModelUtils.getColumnIndex(getTable(),getColumn())+1));
        }
        else
        {
            return NbBundle.getMessage(TableElementNode.class, "table.cell.name", (TableModelUtils.getColumnIndex(getTable(),getColumn())+1));
        }
    }

    @Override
    public Action[] getActions(boolean popup) {

        java.util.List<Action> list = new ArrayList<Action>();
        list.add( SystemAction.get(DeleteTableCellAction.class));
        list.add(null);
        list.add( SystemAction.get(AddTableColumnAfterAction.class));
        list.add( SystemAction.get(AddTableColumnBeforeAction.class));
        list.add( SystemAction.get(AddTableColumnStartAction.class));
        list.add( SystemAction.get(AddTableColumnEndAction.class));
        list.add( SystemAction.get(AddTableColumnGroupAction.class));
        list.add( null);
        list.add( SystemAction.get(DeleteTableColumnAction.class));
        list.add( null);
        list.add( SystemAction.get(GroupColumnsAction.class));
        list.add( null );
        list.add( SystemAction.get(PasteAction.class));
        list.add( SystemAction.get(TableCellDoVerticalLayoutAction.class));
        list.add( SystemAction.get(TableCellDoHorizontalLayoutAction.class));

        return list.toArray(new Action[list.size()]);
    }

    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();

        Sheet.Set set = Sheet.createPropertiesSet();

        set.put(new CellStyleProperty(getCell(), jd));
        set.put(new CellHeightProperty(getCell(), getTable(), getJasperDesign()));
        set.put(new RowSpanProperty(getCell(), getTable(), getJasperDesign()));
        set.put(new ColumnWidthProperty((StandardBaseColumn) getColumn(), getTable(), getJasperDesign()));
        set.put(new CellPaddingAndBordersProperty(getCell()));

        JRDesignDataset dataset = getJasperDesign().getMainDesignDataset();
        if (getTable().getDatasetRun() != null && getTable().getDatasetRun().getDatasetName() != null)
        {
            dataset = (JRDesignDataset) getJasperDesign().getDatasetMap().get(getTable().getDatasetRun().getDatasetName());
        }

        set.put(new ColumnPrintWhenExpressionProperty((StandardBaseColumn) getColumn(), dataset));
        set.put(new ColumnPropertyExpressionsProperty((StandardBaseColumn) getColumn(), dataset));

        sheet.put(set);

        return sheet;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    public void propertyChange(PropertyChangeEvent evt) {

        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) {
            return;
        }


        // Update the sheet
        if (ModelUtils.containsProperty(this.getPropertySets(), evt.getPropertyName())) {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }

    }

    public BaseColumn getColumn() {
        return column;
    }

    public JRDesignComponentElement getComponentElement() {
        return tableElement;
    }

    public StandardTable getTable() {
        return (StandardTable) getComponentElement().getComponent();
    }

    public DesignCell getCell() {
        return cell;
    }

    public JasperDesign getJasperDesign() {
        return jd;
    }

    /**
     * @return the section
     */
    public byte getSection() {
        return section;
    }

    /**
     * @return the group
     */
    public JRGroup getGroup() {
        return group;
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

    @Override
    public boolean hasCustomizer() {
        return true;
    }


    
    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        Node[] dropNodes = NodeTransfer.nodes(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        int dropAction = DnDUtilities.getTransferAction(t);

        if (dropNode == null)
        {
            ElementPasteType.setLastPastedNodes(dropNodes);
        }

        if (null != dropNode) {
            JRDesignElement element = dropNode.getLookup().lookup(JRDesignElement.class);

            if (element instanceof JRDesignComponentElement && ((JRDesignComponentElement)element).getComponent() == getTable()) return null;

            if (null != element ) {

                return new ElementPasteType( element.getElementGroup(),
                                             (JRElementGroup)getCell(),
                                             element,dropAction,this);
            }

            if (dropNode instanceof ElementGroupNode)
            {
                JRDesignElementGroup g = ((ElementGroupNode)dropNode).getElementGroup();
                return new ElementPasteType( g.getElementGroup(),
                                             (JRElementGroup)getCell(),
                                             g,dropAction,this);
            }
            else
            {

            }
        }
        return null;
    }
     
}
