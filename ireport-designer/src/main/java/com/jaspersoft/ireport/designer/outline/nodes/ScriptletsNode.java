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

import com.jaspersoft.ireport.designer.dnd.DnDUtilities;

import com.jaspersoft.ireport.designer.outline.NewTypesUtils;
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
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignScriptlet;
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
 * This node is used just as folder for the report scriptlets
 *
 * @author gtoffoli
 */
public class ScriptletsNode extends IRIndexedNode implements PropertyChangeListener {

    private JasperDesign jd = null;
    private JRDesignDataset dataset = null;
    
    public ScriptletsNode(JasperDesign jd, Lookup doLkp) {
        this(jd, (JRDesignDataset)jd.getMainDataset(),doLkp);
    }
    
    public ScriptletsNode(JasperDesign jd, JRDesignDataset dataset, Lookup doLkp) {
        this(new ScriptletsChildren(jd, dataset,doLkp), jd, dataset,doLkp);
    }

    public ScriptletsNode(ScriptletsChildren pc, JasperDesign jd, JRDesignDataset dataset, Lookup doLkp) {
        super(pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.fixed(jd, dataset)));
    
        addNodeListener(new NodeListener() {

            public void childrenAdded(NodeMemberEvent ev) {
                //System.out.println("childrenAdded");
            }

            public void childrenRemoved(NodeMemberEvent ev) {
                //System.out.println("childrenRemoved");
            }

            @SuppressWarnings("unchecked")
            public void childrenReordered(NodeReorderEvent ev) {

                List list = getDataset().getScriptletsList();
                ArrayList newList = new ArrayList();

                Node[] nodes = getChildren().getNodes();
                for (int i = 0; i < nodes.length; ++i) {
                    JRDesignScriptlet p = ((ScriptletNode) nodes[i]).getScriptlet();
                    if (p.getName().equals("REPORT")) {
                        continue;
                    }
                    newList.add(p);
                }

                list.clear();
                list.addAll(newList);
                
                getDataset().getEventSupport().firePropertyChange(
                        new PropertyChangeEvent(getDataset(), JRDesignDataset.PROPERTY_SCRIPTLETS , null, null ) );

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
        if (dataset == null) dataset = jd.getMainDesignDataset();
        this.dataset = dataset;
        // Add things to the node lookup...
        dataset.getEventSupport().addPropertyChangeListener(this);
        
        
        setDisplayName(I18n.getString("ScriptletNode.Property.Scriptlets"));
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/scriptlets-16.png");
    }

    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        final Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        final int dropAction = DnDUtilities.getTransferAction(t);
        
        if (null != dropNode) {
            final JRDesignScriptlet scriptlet = dropNode.getLookup().lookup(JRDesignScriptlet.class);
            if (null != scriptlet) {
                return new PasteType() {

                    @SuppressWarnings("unchecked")
                    public Transferable paste() throws IOException {
                        
                        if (scriptlet.getName().equals("REPORT")) {
                            return null;
                        }
                        
                        List list = getDataset().getScriptletsList();
                        int currentIndex = -1; //Current position in the list
                        int lastSystemDefinedScriptletIndex = -1; //First valid position
                        
                        for (int i = 0; i < list.size(); ++i) {
                            JRDesignScriptlet p = (JRDesignScriptlet) list.get(i);
                            if (p == scriptlet) {
                                currentIndex = i;
                            }
                            if (p.getName().equals("REPORT")) {
                                lastSystemDefinedScriptletIndex = i;
                            }
                        }
                        // At this point lastSystemDefinedScriptletIndex contains the first valid index
                        // to add a scriptlet and currentIndex contains the index of the scriptlet in the list
                        // if present
                        
                        if( (dropAction & NodeTransfer.MOVE) != 0 ) // Moving scriptlet...
                        {
                            int newIndex = -1;
                            if (currentIndex != -1) { // Case 1: Moving in the list...
                                // Put the field in a valid position...
                                // Find the position of the node...
                                Node[] nodes = getChildren().getNodes();
                                for (int i = 0; i < nodes.length; ++i) {
                                    if (((ScriptletNode) nodes[i]).getScriptlet() == scriptlet) {
                                        newIndex = i;
                                        break;
                                    }
                                }
                                
                                list.remove(scriptlet);
                                if (newIndex == -1) 
                                {
                                    list.add(scriptlet);
                                }
                                else
                                {
                                    list.add(Math.max(newIndex, lastSystemDefinedScriptletIndex+1),scriptlet );
                                }
                            } 
                            else // Adding a copy to the list 
                            {
                                try {
                                    JRDesignScriptlet newParam = (JRDesignScriptlet)scriptlet.clone();
                                    Map map = getDataset().getScriptletsMap();
                                    int k = 1;
                                    while (map.containsKey(newParam.getName())) {
                                        newParam.setName(scriptlet.getName() + "_" + k);
                                        k++;
                                    }
                                    (getDataset()).addScriptlet(newParam);
                                    // Remove the scriptlet from the old list...
                                    if (dropNode.getParentNode() instanceof ScriptletsNode) {
                                        ScriptletsNode pn = (ScriptletsNode) dropNode.getParentNode();
                                        pn.getDataset().removeScriptlet(scriptlet);
                                    }
                                } catch (JRException ex) {
                                    Exceptions.printStackTrace(ex);
                                }
                            }
                        }
                        else // Duplicating
                        {
                            try {
                                JRDesignScriptlet newParam = (JRDesignScriptlet)scriptlet.clone();
                                Map map = getDataset().getScriptletsMap();
                                int k = 1;
                                while (map.containsKey(newParam.getName())) {
                                    newParam.setName(scriptlet.getName() + "_" + k);
                                    k++;
                                }
                                (getDataset()).addScriptlet(newParam);
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

    public

    JasperDesign getJasperDesign() {
        return jd;
    }

    public JRDesignDataset getDataset() {
        return dataset;
    }

    public void setDataset(JRDesignDataset dataset) {
        this.dataset = dataset;
    }

    
    /**
     *  We can create a new Scriptlet here...
     */
    @Override
    public NewType[] getNewTypes()
    {
        return NewTypesUtils.getNewType(this, NewTypesUtils.SCRIPTLET);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName().equals( JRDesignDataset.PROPERTY_SCRIPTLETS))
        {
            // Refrersh scriptlets list...
            ((ScriptletsChildren)this.getChildren()).recalculateKeys();
        }
    }
}