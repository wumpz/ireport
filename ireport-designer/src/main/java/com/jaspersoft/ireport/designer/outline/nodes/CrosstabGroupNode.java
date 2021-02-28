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
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.properties.ExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.EnumProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StringListProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StringProperty;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import net.sf.jasperreports.crosstabs.design.JRCrosstabOrigin;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.util.Pair;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
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
public abstract class CrosstabGroupNode extends IRAbstractNode implements PropertyChangeListener {

    public static final int ROW_GROUP = 1;
    public static final int COLUMN_GROUP = 2;
    
    JasperDesign jd = null;
    private JRDesignCrosstab crosstab = null;
    
    private JRDesignCrosstabGroup group = null;
    private int type = 0;
    
    /**
     * 
     * @return the list of groups (row or column).
     */
    public abstract List<JRDesignCrosstabGroup> getGroups();
    
    /**
     * 
     * @return ROW_GROUP or COLUMN_GROUP
     */
    public abstract int getType();

    public CrosstabGroupNode(JasperDesign jd, JRDesignCrosstab crosstab, JRDesignCrosstabGroup group, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup(doLkp, Lookups.fixed(jd, crosstab, group)));
        this.jd = jd;
        this.group = group;
        this.crosstab = crosstab;
        setDisplayName ( group.getName());
        super.setName( group.getName() );
        
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/crosstabrows-16.png");
        group.getEventSupport().addPropertyChangeListener(this);
    }

    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        
        Sheet.Set set = Sheet.createPropertiesSet();

        set.put(new NameProperty(getGroup(), jd, getCrosstab()));
        set.put(new TotalPositionProperty( getGroup(),getCrosstab()));
        
        set.put(new BucketExpressionProperty((JRDesignCrosstabBucket)getGroup().getBucket(), getCrosstab(), jd));
        set.put(new BucketClassNameProperty((JRDesignCrosstabGroup)getGroup(), getCrosstab(), jd));
        set.put(new BucketComparatorExpressionProperty((JRDesignCrosstabBucket)getGroup().getBucket(), getCrosstab(), jd));
        set.put(new BucketOrderProperty((JRDesignCrosstabBucket)getGroup().getBucket(), getCrosstab()));
        set.put(new BucketOrderByExpressionProperty((JRDesignCrosstabBucket)getGroup().getBucket(), getCrosstab(), jd));

        sheet.put(set);
        
         return sheet;
    }
    
    @Override
    public boolean canCut() {
        return true;
    }
    
    
    /**
     * TODO: make the group name changable.
     * For now we prevent this possibility to avoid to mess around the model
     * since a lot of stuff must be aligned with this name.
     * 
     * @return false (always)
     */
    @Override
    public boolean canRename() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        
        if (getType() == COLUMN_GROUP)
        {
            
            getCrosstab().removeColumnGroup( (JRDesignCrosstabColumnGroup)getGroup());
        }
        else
        {
            getCrosstab().removeRowGroup((JRDesignCrosstabRowGroup)getGroup());
        }   // Remove all the cell having this group as column...
        
        // Remove all the cell having this group as column...
        List cells = getCrosstab().getCellsList();
            
        String name = getGroup().getName();
        
        for (int i=0; i<cells.size(); ++i)
        {
            JRDesignCrosstabCell cell = (JRDesignCrosstabCell)cells.get(i);
            if (cell != null)
            {
                String totalGroup = (getType() == COLUMN_GROUP ) ? cell.getColumnTotalGroup() : cell.getRowTotalGroup();
                if (totalGroup != null && totalGroup.equals(name) )
                {
                    cells.remove(cell);
                    i--;
                }
            }
        }
        
        
        
        super.destroy();
    }
    
    
    
    @Override
    public boolean canDestroy() {
        return getGroups().size() > 1;
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
            SystemAction.get( CopyAction.class ),
            SystemAction.get( CutAction.class ),
            SystemAction.get( RenameAction.class ),
            SystemAction.get( ReorderAction.class ),
            null,
            SystemAction.get( DeleteAction.class ) };
    }
    
    @Override
    public Transferable drag() throws IOException {
        
        ExTransferable tras = ExTransferable.create(clipboardCut());
        return tras;
    }

    
    
    @Override
    @SuppressWarnings("unchecked")
    public void setName(String s) {
        
        PropertySet props = getSheet().get(Sheet.PROPERTIES);
        if (props != null)
        {
            Property[] properties = props.getProperties();
            for (int i=0; i<properties.length; ++i)
            {
                Property p = properties[i];
                if (p.getName() != null &&
                    p.getName().equals(JRDesignCrosstabGroup.PROPERTY_NAME))
                {
                    try {
                        p.setValue(s);
                    } catch (Exception ex)
                    {
                        throw new IllegalArgumentException(ex.getMessage());
                    }
                }
            }
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignCrosstabGroup.PROPERTY_NAME ))
        {
            super.setName(getGroup().getName());
            this.setDisplayName(getGroup().getName());
        }
        
        // Update the sheet
        if (ModelUtils.containsProperty(  this.getPropertySets(), evt.getPropertyName()))
        {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
            
            if (evt.getPropertyName().equals(JRDesignCrosstabBucket.PROPERTY_EXPRESSION))
            {
                this.firePropertyChange(JRDesignExpression.PROPERTY_VALUE_CLASS_NAME, null, "");
            }
        }
        
    }

    public JRDesignCrosstabGroup getGroup() {
        return group;
    }

    public JRDesignCrosstab getCrosstab() {
        return crosstab;
    }
    
    
    /***************  SHEET PROPERTIES DEFINITIONS **********************/
    
    
    /**
     *  Class to manage the JRDesignField.PROPERTY_NAME property
     */
    public static final class NameProperty extends StringProperty {

        private JRDesignCrosstabGroup group = null;
        private JRDesignCrosstab crosstab = null;
        private JasperDesign jd = null;

        @SuppressWarnings("unchecked")
        public NameProperty(JRDesignCrosstabGroup group, JasperDesign jd, JRDesignCrosstab crosstab)
        {
            super(group);
            setName(JRDesignCrosstabGroup.PROPERTY_NAME);
            setDisplayName("Name");
            setShortDescription("Name of the group");
            this.group = group;
            this.crosstab = crosstab;
            this.jd = jd;
            this.setValue("oneline", Boolean.TRUE);
        }

        public JRDesignCrosstab getCrosstab() {
            return crosstab;
        }

        public JRDesignCrosstabGroup getGroup() {
            return group;
        }

        @Override
        public String getString() {
            return getGroup().getName();
        }

        @Override
        public String getOwnString() {
            return getGroup().getName();
        }

        @Override
        public String getDefaultString() {
            return getGroup().getName();
        }

        @Override
        public boolean supportsDefaultValue() {
            return false;
        }

        @Override
        public void validate(Object value) {

            super.validate(value);
            
            if (value == null || value.equals(""))
            {
                IllegalArgumentException iae = annotateException("Group name not valid."); 
                throw iae; 
            }

            String s = value+"";

            if (s.equals(getGroup().getName())) return;
            if (!ModelUtils.isValidNewCrosstabObjectName(crosstab, s))
            {
                IllegalArgumentException iae = annotateException("Group name already in use."); 
                    throw iae; 
            }
            
        }

        
        @Override
        public void setString(String newName) {
            
            final String oldName = getGroup().getName();
            if (oldName.equals(newName)) return;
            
            // We need to update the map...
            final Map map;
            if (crosstab.getColumnGroupIndicesMap().containsKey(oldName))
            {
                map = crosstab.getColumnGroupIndicesMap();
            }
            else if (crosstab.getRowGroupIndicesMap().containsKey(oldName))
            {
                map = crosstab.getRowGroupIndicesMap();
            }
            else
            {
                return; // Inexistent group...
            }
            
            if (map != null) // this should be always true
            {
                Object obj = map.get(oldName);
                if (obj != null) // this should be alwys true (like the map)...
                {
                    map.put(newName,obj);
                    map.remove(oldName);
                
                    // Change all the cells referring this group...
                    List cells = crosstab.getCellsList();
                    for (int i=0; i<cells.size(); ++i)
                    {
                        
                        JRDesignCrosstabCell cell = (JRDesignCrosstabCell)cells.get(i);
                        
                        // update the pair in the map..
                        // the crosstab keep a set of pair (row,group) for each cell in a special
                        // map, we have to rename the group saved by the pair).
                        String rowName_new = cell.getRowTotalGroup();
                        String columnName_new = cell.getColumnTotalGroup();
                        String rowName_old = cell.getRowTotalGroup();
                        String columnName_old = cell.getColumnTotalGroup();
                        boolean updateCell = false;
                                
                        if (cell.getColumnTotalGroup() != null && cell.getColumnTotalGroup().equals(oldName))
                        {
                            cell.setColumnTotalGroup(newName);
                            // change the origin..
                            columnName_new = newName;
                            updateCell = true;
                        }
                        else if (cell.getRowTotalGroup() != null && cell.getRowTotalGroup().equals(oldName))
                        {
                            cell.setRowTotalGroup(newName);
                            rowName_new = newName;
                            updateCell = true;
                        }
                        
                        if (updateCell)
                        {
                            Pair cellKey = new Pair(rowName_old, columnName_old);
                            // Remove the cell mapping
                            getCrosstab().getCellsMap().remove(cellKey);
                            cellKey = new Pair(rowName_new, columnName_new);
                            // add the cell mapping....
                            getCrosstab().getCellsMap().put(cellKey, cell);
                            
                            if (cell.getContents() != null)
                            {
                                JRDesignCellContents contents = (JRDesignCellContents)cell.getContents();
                                contents.setOrigin(new JRCrosstabOrigin(getCrosstab(), JRCrosstabOrigin.TYPE_DATA_CELL,
						rowName_new, columnName_new));
                                contents.getEventSupport().firePropertyChange( JRDesignCrosstabGroup.PROPERTY_NAME, null, null);
                            }
                        }
                    }
                    getGroup().setName(newName);
                    if (getGroup().getTotalHeader() != null)
                    {
                        getGroup().setTotalHeader( (JRDesignCellContents)getGroup().getTotalHeader() );
                        ((JRDesignCellContents)getGroup().getTotalHeader() ).getEventSupport().firePropertyChange(JRDesignCrosstabGroup.PROPERTY_NAME, oldName, newName); 
                    }
                    if (getGroup().getHeader() != null)
                    {
                        getGroup().setHeader( (JRDesignCellContents)getGroup().getHeader() );
                        ((JRDesignCellContents)getGroup().getHeader() ).getEventSupport().firePropertyChange(JRDesignCrosstabGroup.PROPERTY_NAME, oldName, newName); 
                    }
                }
            }
            
            // replace oldName with newName in all the crosstab expressions...
            
            String className = null;
            if (getGroup().getBucket() != null && getGroup().getBucket().getExpression() != null)
            {
                className = getGroup().getBucket().getExpression().getValueClassName();
            }
            ModelUtils.fixElementsExpressions(crosstab, oldName, newName, JRExpressionChunk.TYPE_VARIABLE, className);
            
            List expressions = JRExpressionCollector.collectExpressions(jd, crosstab);
            for (int i=0; i<expressions.size(); ++i)
            {
                JRDesignExpression exp = (JRDesignExpression)expressions.get(i);
                ModelUtils.replaceChunkText(exp, oldName, newName, JRExpressionChunk.TYPE_VARIABLE, className);
            }
        }

    }
    
    
    
    /**
     *  Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
     */
    public static final class TotalPositionProperty extends EnumProperty
    {
        private final JRDesignCrosstabGroup group;
        private final JRDesignCrosstab crosstab;

        @SuppressWarnings("unchecked")
        public TotalPositionProperty(JRDesignCrosstabGroup group, JRDesignCrosstab crosstab)
        {
            super(CrosstabTotalPositionEnum.class,  group);
            this.crosstab = crosstab;
            this.group = group;
        }

        @Override
        public List getTagList() 
        {
            List tags = new java.util.ArrayList();
            tags.add(new Tag(CrosstabTotalPositionEnum.END, "End"));
            tags.add(new Tag(CrosstabTotalPositionEnum.START, "Start"));
            tags.add(new Tag(CrosstabTotalPositionEnum.NONE, "None"));
            return tags;
        }

        @Override
        public String getName()
        {
            return JRDesignCrosstabGroup.PROPERTY_TOTAL_POSITION;
        }

        @Override
        public String getDisplayName()
        {
            return "Total Position";
        }

        @Override
        public String getShortDescription()
        {
            return "This property set the position of the total column or row for this group. If no total is required, set the position type to None";
        }

       

        @Override
        public Object getPropertyValue()
        {
            return group.getTotalPositionValue();
        }

        @Override
        public Object getOwnPropertyValue()
        {
            return getPropertyValue();
        }

        @Override
        public Object getDefaultValue()
        {
            return CrosstabTotalPositionEnum.NONE;
        }

        @Override
        public void setPropertyValue(Object alignment)
        {
            group.setTotalPosition((CrosstabTotalPositionEnum)alignment);
        }
    }
    
    /**
     */
    public static final class BucketExpressionProperty extends ExpressionProperty
    {
        private final JRDesignCrosstabBucket bucket;
        private final JRDesignCrosstab crosstab;
        private final JasperDesign jd;

        
        @SuppressWarnings("unchecked")
        public BucketExpressionProperty(JRDesignCrosstabBucket bucket, JRDesignCrosstab crosstab, JasperDesign jd)
        {
            super(bucket, new ExpressionContext( ModelUtils.getCrosstabDataset(crosstab, jd)));
            setName( JRDesignCrosstabBucket.PROPERTY_EXPRESSION);
            setDisplayName("Bucket expression");
            setShortDescription("The expression used to group the rows/columns.");
            this.crosstab = crosstab;
            this.bucket = bucket;
            this.jd = jd;
            crosstab.getEventSupport().addPropertyChangeListener(JRDesignCrosstab.PROPERTY_DATASET,  new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    setValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT, ModelUtils.getElementDataset(getCrosstab(), getJasperDesign()));
                }
            });

        }

        
        public JasperDesign getJasperDesign()
        {
            return jd;
        }
        
        @Override
        public String getDefaultExpressionClassName() {
            return "java.lang.Object";
        }

        @Override
        public JRDesignExpression getExpression() {
            return (JRDesignExpression)bucket.getExpression();
        }

        @Override
        public void setExpression(JRDesignExpression expression) {
            bucket.setExpression(expression);
        }

        public JRDesignCrosstabBucket getBucket() {
            return bucket;
        }

        public JRDesignCrosstab getCrosstab() {
            return crosstab;
        }

        
    }


    /**
     *  Class to manage the JRDesignParameter.PROPERTY_VALUE_CLASS_NAME property
     */
    public class BucketClassNameProperty extends StringListProperty {

        private final JRDesignCrosstabGroup group;
        private final JRDesignCrosstab crosstab;
        private final JasperDesign jd;

        @SuppressWarnings("unchecked")
        public BucketClassNameProperty(JRDesignCrosstabGroup group, JRDesignCrosstab crosstab, JasperDesign jd)
        {
            super(group);
            this.crosstab = crosstab;
            setName( JRDesignExpression.PROPERTY_VALUE_CLASS_NAME);
            setDisplayName("Bucket Value Class");
            setShortDescription("Bucket Value Class");
            this.group = group;
            this.jd = jd;
            crosstab.getEventSupport().addPropertyChangeListener(JRDesignCrosstab.PROPERTY_DATASET,  new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    setValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT, ModelUtils.getElementDataset(getCrosstab(), getJasperDesign()));
                }
            });
        }

        @Override
        public List getTagList() {
            java.util.List classes = new ArrayList();
            classes.add(new Tag("java.lang.Boolean"));
            classes.add(new Tag("java.lang.Byte"));
            classes.add(new Tag("java.util.Date"));
            classes.add(new Tag("java.sql.Timestamp"));
            classes.add(new Tag("java.sql.Time"));
            classes.add(new Tag("java.lang.Double"));
            classes.add(new Tag("java.lang.Float"));
            classes.add(new Tag("java.lang.Integer"));
            classes.add(new Tag("java.lang.Long"));
            classes.add(new Tag("java.lang.Short"));
            classes.add(new Tag("java.math.BigDecimal"));
            classes.add(new Tag("java.lang.Number"));
            classes.add(new Tag("java.lang.String"));
            classes.add(new Tag("java.lang.Object"));
            return classes;
        }

        @Override
        public String getString() {
            return getBucket().getValueClassName();
        }

        @Override
        public String getOwnString() {
            return getString();
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }

        @Override
        public void setString(String value) {
           if (getBucket().getExpression() != null)
           {
               getBucket().setValueClassName(value);
               ((JRDesignExpression)getBucket().getExpression()).setValueClassName(value);

               // Introspect the crosstab and change the class for all the expressions which use this group name...
               List<JRDesignElement> elements = ModelUtils.getAllElements(crosstab);
               for (JRDesignElement ele : elements)
               {
                   if (ele instanceof JRDesignTextField)
                   {
                       JRDesignTextField dtf = (JRDesignTextField)ele;
                       if (dtf.getExpression() != null &&
                           dtf.getExpression().getText() != null &&
                           dtf.getExpression().getText().trim().equals("$V{" + group.getName() + "}"))
                       {
                           ((JRDesignExpression)dtf.getExpression()).setValueClassName(value);
                       }
                   }
               }
           }
            
        }

        public JasperDesign getJasperDesign()
        {
            return jd;
        }

        public JRDesignCrosstabBucket getBucket() {
            return (JRDesignCrosstabBucket)group.getBucket();
        }

        public JRDesignCrosstab getCrosstab() {
            return crosstab;
        }

        @Override
        public String getDefaultString() {
            return "java.lang.Object";
        }
    }
 
    /**
     */
    public static final class BucketComparatorExpressionProperty extends ExpressionProperty
    {
        private final JRDesignCrosstabBucket bucket;
        private final JRDesignCrosstab crosstab;
        private final JasperDesign jd;

        @SuppressWarnings("unchecked")
        public BucketComparatorExpressionProperty(JRDesignCrosstabBucket bucket, JRDesignCrosstab crosstab, JasperDesign jd)
        {
            super(bucket, new ExpressionContext( ModelUtils.getCrosstabDataset(crosstab, jd)));
            setName( JRDesignCrosstabBucket.PROPERTY_COMPARATOR_EXPRESSION);
            setDisplayName("Comparator exp.");
            setShortDescription("The comparator expression. The expressions's type should be compatible with java.util.Comparator.");
            this.crosstab = crosstab;
            this.bucket = bucket;
            this.jd = jd;
            crosstab.getEventSupport().addPropertyChangeListener(JRDesignCrosstab.PROPERTY_DATASET,  new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    setValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT, ModelUtils.getElementDataset(getCrosstab(), getJasperDesign()));
                }
            });

        }

        
        public JasperDesign getJasperDesign()
        {
            return jd;
        }
        
        @Override
        public String getDefaultExpressionClassName() {
            return "java.util.Comparator";
        }

        @Override
        public JRDesignExpression getExpression() {
            return (JRDesignExpression)bucket.getComparatorExpression();
        }

        @Override
        public void setExpression(JRDesignExpression expression) {
            bucket.setComparatorExpression(expression);
        }

        public JRDesignCrosstabBucket getBucket() {
            return bucket;
        }

        public JRDesignCrosstab getCrosstab() {
            return crosstab;
        }

        @Override
        public Object getDefaultValue() {
            return null;
        }

        @Override
        public boolean isDefaultValue() {
            return getValue() == null;
        }

        @Override
        public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
            setValue(null);
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }

        @Override
        public void setValue(Object newValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (newValue != null && (newValue instanceof String) && ((String)newValue).trim().length() == 0)
            {
             super.setValue(null);    
            }
            super.setValue(newValue);
        }
        
        
        
        
        
    }
    
    /**
     */
    public static final class BucketOrderByExpressionProperty extends ExpressionProperty
    {
        private final JRDesignCrosstabBucket bucket;
        private final JRDesignCrosstab crosstab;
        private final JasperDesign jd;

        @SuppressWarnings("unchecked")
        public BucketOrderByExpressionProperty(JRDesignCrosstabBucket bucket, JRDesignCrosstab crosstab, JasperDesign jd)
        {
            super(bucket, new ExpressionContext( ModelUtils.getCrosstabDataset(crosstab, jd)));
            setName( JRDesignCrosstabBucket.PROPERTY_ORDER_BY_EXPRESSION);
            setDisplayName("Order by exp.");
            setShortDescription("The 'Order by' expression can be used to specify a custom sorting of the data. The expressions's type should be compatible with java.lang.Comparable (otherwise a custom comparator expression would be required)");
            this.crosstab = crosstab;
            this.bucket = bucket;
            this.jd = jd;
            crosstab.getEventSupport().addPropertyChangeListener(JRDesignCrosstab.PROPERTY_DATASET,  new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    setValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT, ModelUtils.getElementDataset(getCrosstab(), getJasperDesign()));
                }
            });

        }


        public JasperDesign getJasperDesign()
        {
            return jd;
        }

        @Override
        public String getDefaultExpressionClassName() {
            return "java.lang.Comparable";
        }

        @Override
        public JRDesignExpression getExpression() {
            return (JRDesignExpression)bucket.getOrderByExpression();
        }

        @Override
        public void setExpression(JRDesignExpression expression) {
            bucket.setOrderByExpression(expression);
        }

        public JRDesignCrosstabBucket getBucket() {
            return bucket;
        }

        public JRDesignCrosstab getCrosstab() {
            return crosstab;
        }

        @Override
        public Object getDefaultValue() {
            return null;
        }

        @Override
        public boolean isDefaultValue() {
            return getValue() == null;
        }

        @Override
        public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
            setValue(null);
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }

        @Override
        public void setValue(Object newValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (newValue != null && (newValue instanceof String) && ((String)newValue).trim().length() == 0)
            {
             super.setValue(null);
            }
            super.setValue(newValue);
        }
    }

    /**
     *  Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
     */
    public static final class BucketOrderProperty extends EnumProperty
    {
        private final JRDesignCrosstabBucket bucket;
        private final JRDesignCrosstab crosstab;

        @SuppressWarnings("unchecked")
        public BucketOrderProperty(JRDesignCrosstabBucket bucket, JRDesignCrosstab crosstab)
        {
            super(SortOrderEnum.class,  bucket);
            this.crosstab = crosstab;
            this.bucket = bucket;
        }

        @Override
        public List getTagList() 
        {
            List tags = new java.util.ArrayList();
            tags.add(new Tag(SortOrderEnum.ASCENDING, "Ascending"));
            tags.add(new Tag(SortOrderEnum.DESCENDING, "Descending"));
            return tags;
        }

        @Override
        public String getName()
        {
            return JRDesignCrosstabBucket.PROPERTY_ORDER;
        }

        @Override
        public String getDisplayName()
        {
            return "Order";
        }

        @Override
        public String getShortDescription()
        {
            return "The sorting type";
        }

        @Override
        public Object getPropertyValue()
        {
            return bucket.getOrderValue();
        }

        @Override
        public Object getOwnPropertyValue()
        {
            return getPropertyValue();
        }

        @Override
        public Object getDefaultValue()
        {
            return SortOrderEnum.ASCENDING;
        }

        @Override
        public void setPropertyValue(Object order)
        {
            bucket.setOrder((SortOrderEnum)order);
        }

    }
}
