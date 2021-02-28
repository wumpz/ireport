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
import com.jaspersoft.ireport.designer.dnd.DnDUtilities;

import com.jaspersoft.ireport.designer.outline.NewTypesUtils;
import com.jaspersoft.ireport.designer.undo.AddStyleUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.actions.NewAction;
import org.openide.actions.PasteAction;
import org.openide.actions.ReorderAction;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.NewType;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class StylesNode extends IRIndexedNode implements PropertyChangeListener {

    private JasperDesign jd = null;
    
    public StylesNode(JasperDesign jd, Lookup doLkp) {
        this(new StylesChildren(jd,doLkp), jd, doLkp);
    }

    public StylesNode(StylesChildren pc, JasperDesign jd, Lookup doLkp) {
        super(pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.singleton(jd)));
    
        addNodeListener(new NodeListener() {

            public void childrenAdded(NodeMemberEvent ev) {
                //System.out.println("childrenAdded");
            }

            public void childrenRemoved(NodeMemberEvent ev) {
                //System.out.println("childrenRemoved");
            }

            @SuppressWarnings("unchecked")
            public void childrenReordered(NodeReorderEvent ev) {

                List list = getJasperDesign().getStylesList();

                ArrayList newList = new ArrayList();
                ArrayList<JRReportTemplate> newList2 = new ArrayList<JRReportTemplate>();

                Node[] nodes = getChildren().getNodes();
                for (int i = 0; i < nodes.length; ++i) {

                    if (nodes[i] instanceof StyleNode)
                    {
                        JRDesignStyle s = ((StyleNode) nodes[i]).getDesignStyle();
                        newList.add(s);
                    }
                    else if (nodes[i] instanceof TemplateReferenceNode)
                    {
                        JRReportTemplate s = ((TemplateReferenceNode) nodes[i]).getTemplate();
                        newList2.add(s);
                    }
                }

                list.clear();
                list.addAll(newList);

                // remove all the old templates...
                JRReportTemplate[] oldTemplates = getJasperDesign().getTemplates();
                for (int i=0; i<oldTemplates.length; ++i)
                {
                    getJasperDesign().removeTemplate(oldTemplates[i]);
                }

                // add all the new templates...
                for (JRReportTemplate t : newList2)
                {
                    getJasperDesign().addTemplate(t);
                }

                getJasperDesign().getEventSupport().firePropertyChange(
                        new PropertyChangeEvent(getJasperDesign(), JasperDesign.PROPERTY_STYLES, null, null ) );
                com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
            }

            public void nodeDestroyed(NodeEvent ev) {
                //System.out.println("nodeDestroyed");
            }

            public void propertyChange(PropertyChangeEvent evt) {
                //System.out.println("propertyChange " + evt.getPropertyName());
            }
        });
        //(dataset != null) ? Lookups.fixed(jd,dataset) : Lookups.singleton(jd)
        
        this.jd = jd;
        
        // Add things to the node lookup...
        jd.getEventSupport().addPropertyChangeListener(this);
        
        
        setDisplayName(I18n.getString("StylesNode.Display.Name"));
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/styles-16.png");
    }

    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        final Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        final int dropAction = DnDUtilities.getTransferAction(t);
        
        if (null != dropNode) {
            final JRDesignStyle style = dropNode.getLookup().lookup(JRDesignStyle.class);
            if (null != style) {
                return new PasteType() {

                    @SuppressWarnings("unchecked")
                    public Transferable paste() throws IOException {
                        
                        List list = getJasperDesign().getStylesList();
                        int currentIndex = -1; //Current position in the list
                        
                        for (int i = 0; i < list.size(); ++i) {
                            JRDesignStyle s = (JRDesignStyle) list.get(i);
                            if (s == style) {
                                currentIndex = i;
                            }
                        }
                        
                        // At this point lastSystemDefinedParameterIndex contains the first valid index
                        // to add a parameter and currentIndex contains the index of the parameter in the list
                        // if present
                        
                        if( (dropAction & NodeTransfer.MOVE) != 0 ) // Moving parameter...
                        {
                            int newIndex = -1;
                            
                            if (currentIndex != -1) { // Case 1: Moving in the list...
                                // Put the field in a valid position...
                                // Find the position of the node...
                                Node[] nodes = getChildren().getNodes();
                                for (int i = 0; i < nodes.length; ++i) {
                                    if (((StyleNode) nodes[i]).getStyle() == style) {
                                        newIndex = i;
                                        break;
                                    }
                                }
                                
                                list.remove(style);
                                if (newIndex == -1) 
                                {
                                    list.add(style);
                                    newIndex = list.indexOf(style);
                                }
                                else
                                {
                                    list.add(newIndex,style );
                                }
                                
                                AddStyleUndoableEdit undo = new AddStyleUndoableEdit(style, getJasperDesign()); //newIndex
                                IReportManager.getInstance().addUndoableEdit(undo);
                            } 
                            else // Adding a copy to the list 
                            {
                                try {
                                    JRDesignStyle newStyle = (JRDesignStyle)style.clone();
                                    Map map = getJasperDesign().getStylesMap();
                                    int k = 1;
                                    while (map.containsKey(newStyle.getName())) {
                                        newStyle.setName(style.getName() + "_" + k);
                                        k++;
                                    }
                                    (getJasperDesign()).addStyle(newStyle);
                                    
                                } catch (JRException ex) {
                                    Exceptions.printStackTrace(ex);
                                }
                            }
                        }
                        else // Duplicating
                        {
                            try {
                                JRDesignStyle newStyle = (JRDesignStyle)style.clone();
                                Map map = getJasperDesign().getStylesMap();
                                int k = 1;
                                while (map.containsKey(newStyle.getName())) {
                                    newStyle.setName(style.getName() + "_" + k);
                                    k++;
                                }
                                getJasperDesign().addStyle(newStyle);
                                AddStyleUndoableEdit undo = new AddStyleUndoableEdit(style, getJasperDesign()); //newIndex
                                IReportManager.getInstance().addUndoableEdit(undo);

                            } catch (JRException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }

                        return null;
                    };
                };
            }
        }
        return null;
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
    public Action[] getActions(boolean context) {
        return new Action[]{
            SystemAction.get(NewAction.class),
            SystemAction.get(PasteAction.class),
            SystemAction.get(ReorderAction.class)};
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    /*
     * @return false to signal that the customizer should not be used.
     *  Subclasses can override this method to enable customize action
     *  and use customizer provided by this class.
     */
    @Override
    public boolean hasCustomizer() {
        return true;
    }

    public JasperDesign getJasperDesign() {
        return jd;
    }
    
    /**
     *  We can create a new Parameter here...
     */
    @Override
    public NewType[] getNewTypes()
    {
        return NewTypesUtils.getNewType(this, NewTypesUtils.STYLE, NewTypesUtils.REPORT_TEMPLATE);
    }

    public void propertyChange(PropertyChangeEvent evt) {
    }
}