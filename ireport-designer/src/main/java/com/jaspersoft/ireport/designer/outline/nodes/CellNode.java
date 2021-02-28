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

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.actions.PaddingAndBordersAction;
import com.jaspersoft.ireport.designer.crosstab.CellInfo;
import com.jaspersoft.ireport.designer.dnd.DnDUtilities;
import com.jaspersoft.ireport.designer.outline.nodes.properties.CrosstabCellPropertiesFactory;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.actions.PasteAction;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class CellNode extends IRIndexedNode implements PropertyChangeListener {

    private final JasperDesign jd;
    private final JRDesignCellContents cellContents;
    private final JRDesignCrosstab crosstab;
    
     public CellNode(JasperDesign jd, JRDesignCrosstab crosstab, JRDesignCellContents cellContents, Lookup doLkp) {
        this(new ElementContainerChildren(jd, cellContents, doLkp), jd, crosstab, cellContents,doLkp);
    }

    public CellNode(ElementContainerChildren pc, JasperDesign jd, JRDesignCrosstab crosstab, JRDesignCellContents cellContents, Lookup doLkp) {
        super(pc, pc.getIndex(), new ProxyLookup( Lookups.fixed(jd, crosstab, cellContents), doLkp) );
        this.jd = jd;
        this.crosstab = crosstab;
        this.cellContents = cellContents;
        
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/cell-16.png");
        
        this.cellContents.getEventSupport().addPropertyChangeListener(this);
        
        
        
        setDisplayName ( ModelUtils.nameOf(cellContents));
        
        this.addNodeListener(new NodeListener() {

            public void childrenAdded(NodeMemberEvent ev) {}
            public void childrenRemoved(NodeMemberEvent ev) {}
            public void nodeDestroyed(NodeEvent ev) {}
            public void propertyChange(PropertyChangeEvent evt) {}

            @SuppressWarnings("unchecked")
            public void childrenReordered(NodeReorderEvent ev) {
                // Fire an event now...
                
                List elements = getCellContents().getChildren();
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
                
                getCellContents().getEventSupport().firePropertyChange( JRDesignBand.PROPERTY_CHILDREN, null, getCellContents().getChildren());
            }
        });
    }

    @Override
    public String getDisplayName() {
        if (getCellContents() == null)
        {
            return super.getDisplayName();
        }
        String cellInfo = "";

        List<CellInfo> infos = ModelUtils.getCellInfos(crosstab);

        for (CellInfo ci : infos)
        {
            if (ci.getCellContents() == this.cellContents)
            {
                cellInfo = "["+ ci.getTop() + "," + ci.getLeft() + "][" + ci.getX() + "," + ci.getY() + "," + ci.getColSpan() + ","+ci.getRowSpan() + "]";
                break;
            }

        }

        return ModelUtils.nameOf(cellContents) + " " + cellInfo;
    }

    
    
    
    @Override
    public String getHtmlDisplayName()
    {
        return getDisplayName();
    }

    /*
    @Override
    @SuppressWarnings("unchecked")
    public Cookie getCookie(Class clazz) {
        Children ch = getChildren();

        if (clazz.isInstance(ch)) {
            return (Cookie) ch;
        }
        
        if (clazz.isAssignableFrom( GenericCookie.JRDesignBandCookie.class ))
        {
            return new GenericCookie.JRDesignBandCookie(this.getBand());
        }
        
        return super.getCookie(clazz);
    }
    */
    
    
    
    

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        
        List<Set> cellPropertiesSets = CrosstabCellPropertiesFactory.getCrosstabCellPropertySets(cellContents, jd);
        
        for (Set s : cellPropertiesSets)
        {
            sheet.put(s);
        }
        
        return sheet;
    }
    
    
    public JasperDesign getJasperDesign() {
        return this.jd;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        fireNameChange(null, "");
        if (evt.getPropertyName() == null) return;
        
        if (ModelUtils.containsProperty(  this.getPropertySets(), evt.getPropertyName()))
        {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
        }
        
        if (evt.getPropertyName().equals(  JRDesignGroup.PROPERTY_NAME) ||
            evt.getPropertyName().equals(  JRDesignCrosstabCell.PROPERTY_COLUMN_TOTAL_GROUP) ||
            evt.getPropertyName().equals(  JRDesignCrosstabCell.PROPERTY_ROW_TOTAL_GROUP))
        {
            this.fireDisplayNameChange(null, getDisplayName());
        }
        
        if (evt.getPropertyName().equals(  JRDesignCellContents.PROPERTY_CHILDREN))
        {
            ((ElementContainerChildren)getChildren()).recalculateKeys();
        }
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
    public Action[] getActions(boolean popup) {
        java.util.List<Action> list = new ArrayList<Action>();

        list.add( SystemAction.get(PaddingAndBordersAction.class));
        list.add( SystemAction.get(PasteAction.class));

        return list.toArray(new Action[list.size()]);
    }

    public JRDesignCellContents getCellContents() {
        return cellContents;
    }

    public JRDesignCrosstab getCrosstab() {
        return crosstab;
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
    
    /**
     *  We can add element groups and new elements here.
     */
    //@Override
    //public NewType[] getNewTypes()
    //{
    //  return NewTypesUtils.getNewType( NewTypesUtils.FIELD, this);
    //}
    
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
            
            if (element instanceof JRCrosstab) return null;
            
            if (null != element ) {
                
                return new ElementPasteType( element.getElementGroup(),
                                             (JRElementGroup)getCellContents(),
                                             element,dropAction,this);
            }
            
            if (dropNode instanceof ElementGroupNode)
            {
                JRDesignElementGroup g = ((ElementGroupNode)dropNode).getElementGroup();
                return new ElementPasteType( g.getElementGroup(),
                                             (JRElementGroup)getCellContents(),
                                             g,dropAction,this);
            }
            else
            {
                
            }
        }
        return null;
    }
    
    
    
}
