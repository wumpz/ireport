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
import com.jaspersoft.ireport.designer.dnd.DnDUtilities;
import com.jaspersoft.ireport.designer.undo.DeleteElementGroupUndoableEdit;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.MoveDownAction;
import org.openide.actions.MoveUpAction;
import org.openide.actions.PasteAction;
import org.openide.actions.ReorderAction;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class ElementGroupNode extends IRIndexedNode implements PropertyChangeListener {

    JasperDesign jd = null;
    JRDesignElementGroup elementGroup = null;

    public JRDesignElementGroup getElementGroup() {
        return elementGroup;
    }

    public ElementGroupNode(JasperDesign jd, JRDesignElementGroup elementGroup, Lookup doLkp) {
        this(new ElementContainerChildren(jd, elementGroup, doLkp), jd, elementGroup, doLkp);
    }
    
    public ElementGroupNode(ElementContainerChildren pc, JasperDesign jd, JRDesignElementGroup elementGroup, Lookup doLkp)
    {
        super (pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.fixed(jd, elementGroup)));
        this.jd = jd;
        this.elementGroup = elementGroup;
        setDisplayName ( "Group" );
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/elementgroup-16.png");
    
        elementGroup.getEventSupport().addPropertyChangeListener(this);
        
        this.addNodeListener(new NodeListener() {

            public void childrenAdded(NodeMemberEvent ev) {}
            public void childrenRemoved(NodeMemberEvent ev) {}
            public void nodeDestroyed(NodeEvent ev) {}
            public void propertyChange(PropertyChangeEvent evt) {}

            @SuppressWarnings("unchecked")
            public void childrenReordered(NodeReorderEvent ev) {
                // Fire an event now...
                
                List elements = getElementGroup().getChildren();
                int[] permutations = ev.getPermutation();
                
                boolean permFound = false;
                
                Object[] elementsArray = new Object[elements.size()];
                for (int i=0; i<elementsArray.length; ++i)
                {
                    //System.out.println("["+i+"]=" + permutations[i]);
                    if (i >= permutations.length || permutations[i] >= elements.size())
                    {
                        permFound = true;
                    }
                    else
                    {
                        elementsArray[permutations[i]] = elements.get(i);
                        if (permutations[i] != i)
                        {
                            permFound = true;
                        }
                    }
                }

                if (!permFound) return;
                
                elements.clear();
                for (int i=0; i<elementsArray.length; ++i)
                {
                    if (elementsArray[i] != null)
                    {
                        elements.add(elementsArray[i]);
                    }
                }
                
                getElementGroup().getEventSupport().firePropertyChange( JRDesignBand.PROPERTY_CHILDREN, null, getElementGroup().getChildren());
            }
        });
    
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        return sheet;
    }
    
    @Override
    public Action[] getActions(boolean popup) {
        return new Action[] {
            SystemAction.get( CopyAction.class ),
            SystemAction.get( CutAction.class ),
            SystemAction.get( PasteAction.class ),
            SystemAction.get( ReorderAction.class ),
            SystemAction.get( MoveUpAction.class),
            SystemAction.get( MoveDownAction.class),
            null,
            SystemAction.get( DeleteAction.class ) };
    }
    
    /**
     * Elements can be cut, so canCut returns always true.
     */
    @Override
    public boolean canCut() {
        return true;
    }
    
    /**
     * Elements can not be renamed, so canRename returns always false.
     */
    @Override
    public boolean canRename() {
        return false;
    }
    
    /**
     * Elements can not be destroied , so canDestroy returns always true.
     */
    @Override
    public boolean canDestroy() {
        return true;
    }
    
    /**
     * When this node is destroied, we have to remove this element from his parent container.
     */
    @Override
    public void destroy() throws IOException {
       
       JRDesignElementGroup container = (JRDesignElementGroup)getElementGroup().getElementGroup();
       int index = container.getChildren().indexOf(getElementGroup());
       container.removeElementGroup( getElementGroup() );
       // TODO: add Unduable edit here
       DeleteElementGroupUndoableEdit undo = new DeleteElementGroupUndoableEdit(getElementGroup(), container, index);
       IReportManager.getInstance().addUndoableEdit(undo);
       super.destroy();
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
    public Transferable drag() throws IOException {
        return clipboardCut();
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
            
            if (null != element)
            {
                if (element instanceof JRElementGroup &&
                    ModelUtils.isAncestorElemenetGroup((JRElementGroup)element, (JRElementGroup)getElementGroup()) )
                {
                    return null;
                }
                
                return new ElementPasteType( element.getElementGroup(),
                                             getElementGroup(),
                                             element,dropAction,this);
            }
            
            if (dropNode instanceof ElementGroupNode)
            {
                JRDesignElementGroup g = ((ElementGroupNode)dropNode).getElementGroup();
                
                if (ModelUtils.isAncestorElemenetGroup(g,getElementGroup()))
                {
                    return null;
                }
                
                return new ElementPasteType( g.getElementGroup(),
                                             getElementGroup(),
                                             g,dropAction,this);
            }
        }
        return null;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
    }

}
