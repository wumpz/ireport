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
import com.jaspersoft.ireport.designer.crosstab.CrosstabDataAction;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;
import com.jaspersoft.ireport.designer.sheet.properties.BooleanProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ByteProperty;
import com.jaspersoft.ireport.designer.sheet.properties.CrosstabParametersMapExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.IntegerProperty;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;

/**
 *
 * @author gtoffoli
 */
public class CrosstabNode extends ElementNode {

    private JRDesignCrosstab crosstab = null;
    
    public JRDesignCrosstab getCrosstab() {
        return crosstab;
    }
    
    public CrosstabNode(JasperDesign jd, JRDesignCrosstab crosstab, Lookup doLkp)
    {
       this(jd, crosstab, new CrosstabChildren(jd, crosstab, doLkp), doLkp); 
    }
    
    public CrosstabNode(JasperDesign jd, JRDesignCrosstab crosstab, CrosstabChildren children, Lookup doLkp)
    {
        super(jd, crosstab, children, children.getIndex(), doLkp);
        this.crosstab = crosstab;
        
        this.crosstab.getEventSupport().addPropertyChangeListener(this);
        
        this.addNodeListener(new NodeListener() {

            public void childrenAdded(NodeMemberEvent ev) {}
            public void childrenRemoved(NodeMemberEvent ev) {}
            public void nodeDestroyed(NodeEvent ev) {}
            public void propertyChange(PropertyChangeEvent evt) {}

            @SuppressWarnings("unchecked")
            public void childrenReordered(NodeReorderEvent ev) {
                // Fire an event now...
                
                /*
                      
                List elements = getCrosstab().getChildren();
                int[] permutations = ev.getPermutation();
                
                boolean permFound = false;
                
                Object[] elementsArray = new Object[elements.size()];
                for (int i=0; i<elementsArray.length; ++i)
                {
                    //System.out.println("["+i+"]=" + permutations[i]);
                    elementsArray[permutations[i]] = elements.get(i);
                    if (permutations[i] != i) 
                    {
                        permFound = true;
                    }
                }

                if (!permFound) return;
                
                elements.clear();
                for (int i=0; i<elementsArray.length; ++i)
                {
                    elements.add(elementsArray[i]);
                }
                
                getFrame().getEventSupport().firePropertyChange( JRDesignBand.PROPERTY_CHILDREN, null, getFrame().getChildren());
                */
           }
        });
        
    }
    
    @Override
    public Action[] getActions(boolean popup) {
        
       Action[] actions = super.getActions(popup);
       java.util.List<Action> list = new ArrayList<Action>( Arrays.asList(actions) );
       
       list.add(0, SystemAction.get(CrosstabDataAction.class));
       list.add(1, null);
    
       return list.toArray(new Action[list.size()]);
    }
    

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        
        // adding common properties...
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("CROSSTAB_PROPERTIES");
        propertySet.setDisplayName("Crosstab properties");
        
        // crosstab properties...
        propertySet.put(new RepeatColumnHeadersProperty(getCrosstab()));
        propertySet.put(new RepeatRowHeadersProperty(getCrosstab()));
        propertySet.put(new ColumnBreakOffsetProperty(getCrosstab()));
        propertySet.put(new IgnoreWidthProperty(getCrosstab()));
        propertySet.put(new RunDirectionProperty(getCrosstab()));
        propertySet.put(new CrosstabParametersMapExpressionProperty(getCrosstab(), ModelUtils.getElementDataset(crosstab, jd)));

        sheet.put( propertySet );
        
        return sheet;
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
    
        if (evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_CELLS) ||
            evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_HEADER_CELL) ||
            evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_ROW_GROUPS) ||
            evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_COLUMN_GROUPS) ||
            evt.getPropertyName().equals( JRDesignCrosstab.PROPERTY_WHEN_NO_DATA_CELL)
           )
       {
            ((CrosstabChildren)getChildren()).recalculateKeys();
       }
    }
    
    /**
     *  This is used internally to understand if the element can accept other elements...
     */
    public boolean canPaste() {
        return false;
    }
    
    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        /*
        Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        int dropAction = DnDUtilities.getTransferAction(t);
               
        if (null != dropNode) {
            JRDesignElement element = dropNode.getLookup().lookup(JRDesignElement.class);
            
            if (null != element) {
                
                return new ElementPasteType( element.getElementGroup(),
                                             getFrame(),
                                             element,dropAction,this);
            }
            
            // Check if we are pasting a group not an element
            if (dropNode instanceof ElementGroupNode)
            {
                JRDesignElementGroup g = ((ElementGroupNode)dropNode).getElementGroup();
                return new ElementPasteType( g.getElementGroup(),
                                             getFrame(),
                                             g,dropAction,this);
            }
        }
        */
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
    
    
    public static final class RepeatColumnHeadersProperty extends BooleanProperty {

        private final JRDesignCrosstab crosstab;

        @SuppressWarnings("unchecked")
        public RepeatColumnHeadersProperty(JRDesignCrosstab crosstab)
        {
            super(crosstab);
            this.crosstab = crosstab;
        }
        @Override
        public String getName()
        {
            return JRDesignCrosstab.PROPERTY_REPEAT_COLUMN_HEADERS;
        }

        @Override
        public String getDisplayName()
        {
            return "Repeat Colunm Headers";
        }

        @Override
        public String getShortDescription()
        {
            return "Repeat Colunm Headers.";
        }

        @Override
        public Boolean getBoolean()
        {
            return crosstab.isRepeatColumnHeaders();
        }

        @Override
        public Boolean getOwnBoolean()
        {
            return crosstab.isRepeatColumnHeaders();
        }

        @Override
        public Boolean getDefaultBoolean()
        {
            return Boolean.TRUE;
        }

        @Override
        public void setBoolean(Boolean b)
        {
            crosstab.setRepeatColumnHeaders(b);
        }

    }
    
        public static final class IgnoreWidthProperty extends BooleanProperty {

        private final JRDesignCrosstab crosstab;

        @SuppressWarnings("unchecked")
        public IgnoreWidthProperty(JRDesignCrosstab crosstab)
        {
            super(crosstab);
            this.crosstab = crosstab;
        }
        @Override
        public String getName()
        {
            return JRDesignCrosstab.PROPERTY_IGNORE_WIDTH;
        }

        @Override
        public String getDisplayName()
        {
            return "Ignore Width Property";
        }

        @Override
        public String getShortDescription()
        {
            return "This attribute determines whether the crosstab will break at the width set for the crosstab element, or whether the crosstab is to expand over this width (and over the page width as well).";
        }

        @Override
        public Boolean getBoolean()
        {
            return crosstab.getIgnoreWidth();
        }

        @Override
        public Boolean getOwnBoolean()
        {
            return crosstab.getIgnoreWidth();
        }

        @Override
        public Boolean getDefaultBoolean()
        {
            return null;
        }

        @Override
        public void setBoolean(Boolean b)
        {
            crosstab.setIgnoreWidth(b);
        }

    }
    
    public static final class RepeatRowHeadersProperty extends BooleanProperty {

        private final JRDesignCrosstab crosstab;

        @SuppressWarnings("unchecked")
        public RepeatRowHeadersProperty(JRDesignCrosstab crosstab)
        {
            super(crosstab);
            this.crosstab = crosstab;
        }
        @Override
        public String getName()
        {
            return JRDesignCrosstab.PROPERTY_REPEAT_ROW_HEADERS;
        }

        @Override
        public String getDisplayName()
        {
            return "Repeat Row Headers";
        }

        @Override
        public String getShortDescription()
        {
            return "Repeat Row Headers.";
        }

        @Override
        public Boolean getBoolean()
        {
            return crosstab.isRepeatRowHeaders();
        }

        @Override
        public Boolean getOwnBoolean()
        {
            return crosstab.isRepeatRowHeaders();
        }

        @Override
        public Boolean getDefaultBoolean()
        {
            return Boolean.TRUE;
        }

        @Override
        public void setBoolean(Boolean b)
        {
            crosstab.setRepeatRowHeaders(b);
        }

    }
    
    
    public static final class ColumnBreakOffsetProperty extends IntegerProperty {

        private final JRDesignCrosstab crosstab;

        @SuppressWarnings("unchecked")
        public ColumnBreakOffsetProperty(JRDesignCrosstab crosstab)
        {
            super(crosstab);
            this.crosstab = crosstab;
        }
        @Override
        public String getName()
        {
            return JRDesignCrosstab.PROPERTY_COLUMN_BREAK_OFFSET;
        }

        @Override
        public String getDisplayName()
        {
            return "Column Break Offset";
        }

        @Override
        public String getShortDescription()
        {
            return "Column Break Offset.";
        }

        
        @Override
        public Integer getInteger() {
            return crosstab.getColumnBreakOffset();
        }

        @Override
        public Integer getOwnInteger() {
            return crosstab.getColumnBreakOffset();
        }

        @Override
        public Integer getDefaultInteger() {
            return 10;
        }

        @Override
        public void validateInteger(Integer value) {
            return;
        }

        @Override
        public void setInteger(Integer value) {
            if (value != null) crosstab.setColumnBreakOffset(value);
        }

    }
    
    
    public static final class RunDirectionProperty extends AbstractProperty {

        private final JRDesignCrosstab crosstab;
        private ComboBoxPropertyEditor editor;

        @SuppressWarnings("unchecked")
        public RunDirectionProperty(JRDesignCrosstab crosstab)
        {
            super(RunDirectionEnum.class, crosstab);
            this.crosstab = crosstab;
            setValue("suppressCustomEditor", Boolean.TRUE);
        }
        
        @Override
        public String getName()
        {
            return JRBaseCrosstab.PROPERTY_RUN_DIRECTION;
        }

        @Override
        public String getDisplayName()
        {
            return "Run Direction";
        }

        @Override
        public String getShortDescription()
        {
            return "Run Direction.";
        }

        
        public List getTagList() {
            List list = new ArrayList();
            list.add(new Tag(RunDirectionEnum.LTR, "Left to Right"));
            list.add(new Tag(RunDirectionEnum.RTL, "Right to Left"));
            return list;

        }

        @Override
        public Object getPropertyValue() {
            return crosstab.getRunDirectionValue() == null ? null : crosstab.getRunDirectionValue().getValueByte();
        }

        @Override
        public Object getOwnPropertyValue() {
            return getPropertyValue();
        }

        @Override
        public Object getDefaultValue() {
            return RunDirectionEnum.LTR;
        }

        @Override
        public void setPropertyValue(Object value) {
            crosstab.setRunDirection((RunDirectionEnum)value);
        }

        @Override
        public void validate(Object value) {
        }

    }
}
