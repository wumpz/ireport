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
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabParameter;
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
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.NewType;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * This node is used just as folder for the report parameters
 *
 * @author gtoffoli
 */
public class CrosstabMeasuresNode extends IRIndexedNode implements PropertyChangeListener {

    private JasperDesign jd = null;
    private JRDesignCrosstab crosstab = null;
    
    public CrosstabMeasuresNode(JasperDesign jd, JRDesignCrosstab crosstab, Lookup doLkp) {
        this(new CrosstabMeasuresChildren(jd, crosstab,doLkp), jd, crosstab,doLkp);
    }

    public CrosstabMeasuresNode(CrosstabMeasuresChildren pc, JasperDesign jd, JRDesignCrosstab crosstab, Lookup doLkp) {
        super(pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.fixed(jd, crosstab)));
    
        addNodeListener(new NodeListener() {

            public void childrenAdded(NodeMemberEvent ev) {
                //System.out.println("childrenAdded");
            }

            public void childrenRemoved(NodeMemberEvent ev) {
                //System.out.println("childrenRemoved");
            }

            @SuppressWarnings("unchecked")
            public void childrenReordered(NodeReorderEvent ev) {

                /*
                List list = getCrosstab().getParametersList();
                ArrayList newList = new ArrayList();

                for (int i = 0; i < list.size(); ++i) {
                    JRDesignParameter p = (JRDesignParameter) list.get(i);
                    if (p.isSystemDefined()) {
                        newList.add(p);
                    }
                }

                Node[] nodes = getChildren().getNodes();
                for (int i = 0; i < nodes.length; ++i) {
                    JRDesignParameter p = ((ParameterNode) nodes[i]).getParameter();
                    if (p.isSystemDefined()) {
                        continue;
                    }
                    newList.add(p);
                }

                list.clear();
                list.addAll(newList);
                
                getCrosstab().getEventSupport().firePropertyChange(
                        new PropertyChangeEvent(getCrosstab(), JRDesignDataset.PROPERTY_PARAMETERS , null, null ) );
                */
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
        this.crosstab = crosstab;
        // Add things to the node lookup...
        crosstab.getEventSupport().addPropertyChangeListener(this);
        
        
        setDisplayName("Measures");
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/crosstabmeasures-16.png");
    }

    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        final Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        final int dropAction = DnDUtilities.getTransferAction(t);
        
        if (null != dropNode) {
            final JRDesignCrosstabParameter parameter = dropNode.getLookup().lookup(JRDesignCrosstabParameter.class);
            if (null != parameter) {
                return new PasteType() {

                    @SuppressWarnings("unchecked")
                    public Transferable paste() throws IOException {
                        /*
                        if (parameter.isSystemDefined()) {
                            return null;
                        }
                        
                        List list = getCrosstab().getParametersList();
                        int currentIndex = -1; //Current position in the list
                        int lastSystemDefinedParameterIndex = -1; //First valid position
                        
                        for (int i = 0; i < list.size(); ++i) {
                            JRDesignCrosstabParameter p = (JRDesignCrosstabParameter) list.get(i);
                            if (p == parameter) {
                                currentIndex = i;
                            }
                            if (p.isSystemDefined()) {
                                lastSystemDefinedParameterIndex = i;
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
                                    if (((CrosstabParameterNode) nodes[i]).getParameter() == parameter) {
                                        newIndex = i;
                                        break;
                                    }
                                }
                                
                                list.remove(parameter);
                                if (newIndex == -1) 
                                {
                                    list.add(parameter);
                                }
                                else
                                {
                                    list.add(Math.max(newIndex, lastSystemDefinedParameterIndex+1),parameter );
                                }
                            } 
                            else // Adding a copy to the list 
                            {
                                try {
                                    JRDesignCrosstabParameter newParam = ModelUtils.cloneCrosstabParameter(parameter);
                                    Map map = getCrosstab().getParametersMap();
                                    int k = 1;
                                    while (map.containsKey(newParam.getName())) {
                                        newParam.setName(parameter.getName() + "_" + k);
                                        k++;
                                    }
                                    getCrosstab().addParameter(newParam);
                                    // Remove the parameter from the old list...
                                    if (dropNode.getParentNode() instanceof CrosstabMeasuresNode) {
                                        CrosstabMeasuresNode pn = (CrosstabMeasuresNode) dropNode.getParentNode();
                                        pn.getCrosstab().removeParameter(parameter);
                                    }
                                } catch (JRException ex) {
                                    Exceptions.printStackTrace(ex);
                                }
                            }
                        }
                        else // Duplicating
                        {
                            try {
                                JRDesignCrosstabParameter newParam = ModelUtils.cloneCrosstabParameter(parameter);
                                Map map = getCrosstab().getParametersMap();
                                int k = 1;
                                while (map.containsKey(newParam.getName())) {
                                    newParam.setName(parameter.getName() + "_" + k);
                                    k++;
                                }
                                getCrosstab().addParameter(newParam);
                            } catch (JRException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
                         */
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

    /**
     *  We can create a new Parameter here...
     */
    @Override
    public NewType[] getNewTypes()
    {
        return NewTypesUtils.getNewType(this, NewTypesUtils.CROSSTAB_MEASURE);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_MEASURES))
        {
            // Refrersh parameters list...
            ((CrosstabMeasuresChildren)this.getChildren()).recalculateKeys();
        }
    }

    public JRDesignCrosstab getCrosstab() {
        return crosstab;
    }
}