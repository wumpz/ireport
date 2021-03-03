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
import com.jaspersoft.ireport.components.table.TableMatrix;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnEndAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnGroupAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnStartAction;
import com.jaspersoft.ireport.components.table.actions.DeleteTableColumnAction;
import com.jaspersoft.ireport.components.table.nodes.properties.ColumnPrintWhenExpressionProperty;
import com.jaspersoft.ireport.components.table.nodes.properties.ColumnPropertyExpressionsProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.dnd.DnDUtilities;
import com.jaspersoft.ireport.designer.outline.nodes.IRAbstractNode;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.Action;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class TableColumnGroupNode extends IRAbstractNode implements PropertyChangeListener {

    JasperDesign jd = null;
    private JRDesignComponentElement tableElement = null;
    
    private StandardColumnGroup columnGroup = null;
    private Object parent = null;
    private byte section = 0;
    private JRDesignGroup group = null;

    public TableColumnGroupNode(JasperDesign jd, JRDesignComponentElement tableElement, StandardColumnGroup columnGroup, Object parent, byte section, JRDesignGroup group, Lookup doLkp)
    {
        super (new TableColumnGroupChildren(jd, tableElement, columnGroup, section, group, doLkp), new ProxyLookup(doLkp, Lookups.fixed(jd, tableElement, columnGroup, parent)));
        this.jd = jd;
        this.columnGroup = columnGroup;
        this.tableElement = tableElement;
        this.parent = parent;
        this.section = section;
        this.group = group;
        setIconBaseWithExtension("com/jaspersoft/ireport/components/table/columngroup.png");
        //setDisplayName(I18n.getString("ColumnGroupNode.name"));
        setName("columnGroup");
        columnGroup.getEventSupport().addPropertyChangeListener(this);

//        this.addNodeListener(new NodeListener() {
//
//            public void childrenAdded(NodeMemberEvent ev) {}
//            public void childrenRemoved(NodeMemberEvent ev) {}
//            public void nodeDestroyed(NodeEvent ev) {}
//            public void propertyChange(PropertyChangeEvent evt) {}
//
//            @SuppressWarnings("unchecked")
//            public void childrenReordered(NodeReorderEvent ev) {
//                // Fire an event now...
//
//                List columns = getColumnGroup().getColumns();
//                int[] permutations = ev.getPermutation();
//
//                Object[] elementsArray = new Object[columns.size()];
//                for (int i=0; i<elementsArray.length; ++i)
//                {
//                    elementsArray[permutations[i]] = columns.get(i);
//                }
//                columns.clear();
//                for (int i=0; i<elementsArray.length; ++i)
//                {
//                    columns.add(elementsArray[i]);
//                }
//
//                getColumnGroup().getEventSupport().firePropertyChange( StandardColumnGroup.PROPERTY_COLUMNS, null, getColumnGroup().getColumns());
//            }
//        });
    }

    @Override
    public String getDisplayName()
    {
        int index = TableModelUtils.getColumnIndex(getTable(),getColumnGroup());
        return NbBundle.getMessage(TableElementNode.class, "table.columnGroup.name", index+1,  index + TableModelUtils.getColSpan(columnGroup) );

    }
    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        
        Sheet.Set set = Sheet.createPropertiesSet();

        JRDesignDataset dataset = getJasperDesign().getMainDesignDataset();
        if (getTable().getDatasetRun() != null && getTable().getDatasetRun().getDatasetName() != null)
        {
            dataset = (JRDesignDataset) getJasperDesign().getDatasetMap().get(getTable().getDatasetRun().getDatasetName());
        }

        set.put(new ColumnPrintWhenExpressionProperty((StandardBaseColumn) getColumnGroup(), dataset));
        set.put(new ColumnPropertyExpressionsProperty((StandardBaseColumn) getColumnGroup(), dataset));
        
        sheet.put(set);
        
         
         return sheet;
    }
    
    @Override
    public boolean canCut() {
        return true;
    }
    
    
    /**
     * @return false (always)
     */
    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public void destroy() throws IOException {

        if (parent instanceof StandardTable)
        {
            ((StandardTable)parent).removeColumn(getColumnGroup());
        }

        if (parent instanceof StandardColumnGroup)
        {
            ((StandardColumnGroup)parent).removeColumn(getColumnGroup());
        }

        super.destroy();
    }
    
    
    
    @Override
    public boolean canDestroy() {
        if (parent instanceof StandardTable)
        {
            return ((StandardTable)parent).getColumns().size() > 1;
        }

        if (parent instanceof StandardColumnGroup)
        {
            return ((StandardColumnGroup)parent).getColumns().size() > 1;
        }
        return false;
    }
        
        
    @Override
    public Transferable clipboardCut() throws IOException {
        return NodeTransfer.transferable(this, NodeTransfer.CLIPBOARD_CUT);
    }
    
    @Override
    public Transferable clipboardCopy() throws IOException {
        return NodeTransfer.transferable(this, NodeTransfer.CLIPBOARD_COPY);
    }
            
    @Override
    public Action[] getActions(boolean popup) {
        return new Action[] {
            SystemAction.get( AddTableColumnStartAction.class ),
            SystemAction.get(AddTableColumnEndAction.class),
            SystemAction.get(AddTableColumnGroupAction.class),
            SystemAction.get( DeleteTableColumnAction.class )
        };
    }

    @Override
    public Transferable drag() throws IOException {
        
        ExTransferable tras = ExTransferable.create(clipboardCut());
        return tras;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) return;
        
        
        // Update the sheet
        if (ModelUtils.containsProperty(  this.getPropertySets(), evt.getPropertyName()))
        {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
        }
        
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

    @Override
    public boolean hasCustomizer()
    {
        return true;
    }

    /**
     * @return the columnGroup
     */
    public StandardColumnGroup getColumnGroup() {
        return columnGroup;
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
     
     
  
}
