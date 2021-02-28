/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.designer.outline.nodes;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.actions.DeleteGroupAction;
import com.jaspersoft.ireport.designer.actions.MoveGroupDownAction;
import com.jaspersoft.ireport.designer.actions.MoveGroupUpAction;
import com.jaspersoft.ireport.designer.outline.nodes.BandNode.RefreshNodes;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.PasteAction;
import org.openide.actions.RenameAction;
import org.openide.actions.ReorderAction;
import org.openide.nodes.Children;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class GroupNodeImpl extends IRAbstractNode implements PropertyChangeListener, GroupNode {

    JasperDesign jd = null;
    private JRDesignGroup group = null;

    public GroupNodeImpl(JasperDesign jd, JRDesignGroup group, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup(doLkp, Lookups.fixed(jd, group)));
        this.jd = jd;
        this.group = group;
        setDisplayName ( group.getName());
        super.setName( group.getName() );
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/groups-16.png");
        
        group.getEventSupport().addPropertyChangeListener(this);
    }

    @Override
    public String getDisplayName() {
        return group.getName();
    }
    
    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();

        if (group != null)
        {
            Sheet.Set groupPropertiesSet = Sheet.createPropertiesSet();
            groupPropertiesSet.setName("GROUP_PROPERTIES");
            groupPropertiesSet.setDisplayName(I18n.getString("BandNode.Property.Groupproperties"));

            if (getDataset() != null)
            {
                groupPropertiesSet = BandNode.fillGroupPropertySet(groupPropertiesSet, getDataset(), group);
            }
            
            sheet.put(groupPropertiesSet);
        }
        return sheet;
    }
    
    @Override
    public boolean canCut() {
        return true;
    }
    
    @Override
    public boolean canRename() {
        return true;
    }
    
    @Override
    public boolean canDestroy() {
        return true;
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
    public void destroy() throws IOException {
       
       JRDesignDataset dataset = getParentNode().getLookup().lookup(JRDesignDataset.class);
       getGroup().getEventSupport().removePropertyChangeListener(this);
       dataset.removeGroup(getGroup());
       super.destroy();
    }


        
    @Override
    public Action[] getActions(boolean popup) {

        java.util.List<Action> list = new ArrayList<Action>();

        list.add( SystemAction.get( CopyAction.class ));
        list.add( SystemAction.get( CutAction.class ));
        list.add( SystemAction.get( RenameAction.class ));
        list.add( SystemAction.get( ReorderAction.class ));
        list.add( SystemAction.get( PasteAction.class));
        list.add( SystemAction.get( RefreshNodes.class));

        if (group != null)
        {
            list.add( null );
            list.add( SystemAction.get(MoveGroupUpAction.class));
            list.add( SystemAction.get(MoveGroupDownAction.class));
            list.add( DeleteGroupAction.getInstance() );
        }


        return list.toArray(new Action[list.size()]);
    }
    
    @Override
    public Transferable drag() throws IOException {
        
        ExTransferable tras = ExTransferable.create(clipboardCut());
        return tras;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setName(String s) {
        
        if (s.equals(""))
        {
            throw new IllegalArgumentException(I18n.getString("GroupNode.Property.GroupInvalid"));
        }
        
        List<JRGroup> currentGroups = null;
        JRDesignDataset dataset = getParentNode().getLookup().lookup(JRDesignDataset.class);
        currentGroups = (List<JRGroup>)dataset.getGroupsList();
        for (JRGroup pg : currentGroups)
        {
            JRDesignGroup p = (JRDesignGroup)pg;
            if (p != getGroup() && p.getName().equals(s))
            {
                throw new IllegalArgumentException(I18n.getString("GroupNode.Property.GroupInUse"));
            }
        }
        
        String oldName = getGroup().getName();
        getGroup().setName(s);
        dataset.getGroupsMap().remove(oldName);
        dataset.getGroupsMap().put(s,getGroup());

        JRDesignVariable var = (JRDesignVariable) dataset.getVariablesMap().get(oldName + "_COUNT");
        var.setName(s + "_COUNT");
        dataset.getVariablesMap().remove(oldName + "_COUNT");
        dataset.getVariablesMap().put(s + "_COUNT", var);

        dataset.getEventSupport().firePropertyChange(JRDesignDataset.PROPERTY_VARIABLES, null, null);

        
        
        ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getGroup(), "Name", String.class, oldName, s);

        IReportManager.getInstance().addUndoableEdit(opue);
    }

    public JRDesignGroup getGroup() {
        return group;
    }

    public void setGroup(JRDesignGroup group) {
        this.group = group;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignGroup.PROPERTY_NAME ))
        {
            super.setName(getGroup().getName());
            this.setDisplayName(getGroup().getName());
        }
        
        // Update the sheet
        if (ModelUtils.containsProperty(this.getPropertySets(), evt.getPropertyName())) {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }

    public JRDesignDataset getDataset() {
        if (getParentNode() == null) return null;
        return getParentNode().getLookup().lookup(JRDesignDataset.class);
    }
    
    
    
 
}
