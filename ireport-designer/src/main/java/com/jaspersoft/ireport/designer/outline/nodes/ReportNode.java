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

import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.properties.LanguageProperty;
import com.jaspersoft.ireport.designer.sheet.properties.WhenNoDataTypeProperty;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.actions.AddDatasetAction;
import com.jaspersoft.ireport.designer.actions.CompileReportAction;
import com.jaspersoft.ireport.designer.actions.RemoveMarginsAction;
import com.jaspersoft.ireport.designer.dnd.DnDUtilities;
import com.jaspersoft.ireport.designer.menu.EditPageFormatAction;
import com.jaspersoft.ireport.designer.menu.EditQueryAction;
import com.jaspersoft.ireport.designer.menu.OpenReportDirectoryInFavoritesAction;
import com.jaspersoft.ireport.designer.sheet.JRImportsProperty;
import com.jaspersoft.ireport.designer.sheet.JRPropertiesMapProperty;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;
import com.jaspersoft.ireport.designer.sheet.properties.EnumProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.designer.wizards.ReportGroupWizardAction;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import org.openide.ErrorManager;
import org.openide.actions.PasteAction;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.actions.NodeAction;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class ReportNode extends IRAbstractNode implements PropertyChangeListener, ExpressionHolder {

    JasperDesign jd = null;
    
    public static final Action testPropertiesAction = new NodeAction() {

        protected void performAction(Node[] activatedNodes) {
                JasperDesign jd = activatedNodes[0].getLookup().lookup(JasperDesign.class);
                jd.setName("test name");
                jd.setPageWidth(700);
                jd.setPageHeight(400);
                jd.setOrientation( OrientationEnum.LANDSCAPE);
                jd.setTopMargin(10);
                jd.setBottomMargin(20);
                jd.setLeftMargin(35);
                jd.setRightMargin(5);
        }

        protected boolean enable(Node[] activatedNodes) {
            return (activatedNodes.length > 0);
        }

        public String getName() {
            return "Test document properties changes";
        }

        public HelpCtx getHelpCtx() {
            return null;
        }
    };
    
    public ReportNode(JasperDesign jd, Lookup doLkp)
    {
        super (new ReportChildren(jd,doLkp), new ProxyLookup(Lookups.singleton(jd), doLkp) );
        this.jd = jd;
        jd.getEventSupport().addPropertyChangeListener(this);
        
        updateSectionListeners();
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/report-16.png");
    }

    public void updateSectionListeners()
    {
        ((JRDesignSection)jd.getDetailSection()).getEventSupport().removePropertyChangeListener(this);
        ((JRDesignSection)jd.getDetailSection()).getEventSupport().addPropertyChangeListener(this);
        
        for (int i=0; i<this.jd.getGroupsList().size(); ++i)
        {
            JRDesignGroup grp = (JRDesignGroup)this.jd.getGroupsList().get(i);
            grp.getEventSupport().removePropertyChangeListener(this);
            grp.getEventSupport().addPropertyChangeListener(this);
            if (((JRDesignSection)grp.getGroupHeaderSection() != null))
            {
                ((JRDesignSection)grp.getGroupHeaderSection()).getEventSupport().removePropertyChangeListener(this);
                ((JRDesignSection)grp.getGroupHeaderSection()).getEventSupport().addPropertyChangeListener(this);
            }
            if (((JRDesignSection)grp.getGroupFooterSection() != null))
            {
                ((JRDesignSection)grp.getGroupFooterSection()).getEventSupport().removePropertyChangeListener(this);
                ((JRDesignSection)grp.getGroupFooterSection()).getEventSupport().addPropertyChangeListener(this);
            }
        }
    }

    public JasperDesign getJasperDesign() {
        return jd;
    }

    
    @Override
    public String getDisplayName() {
        //return "Report " + jd.getName();
        return "" + jd.getName();
    }

    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        final Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        final int dropAction = DnDUtilities.getTransferAction(t);

        if (null != dropNode && dropNode instanceof DatasetNode) {
            final JRDesignDataset dataset = ((DatasetNode)dropNode).getDataset();
            if (null != dataset) {
                return new PasteType() {

                    @SuppressWarnings("unchecked")
                    public Transferable paste() throws IOException {
                        try {

                            JRDesignDataset newDataset = (JRDesignDataset) dataset.clone();

                            String name = newDataset.getName();
                            for (int i = 1;; i++) {
                                if (!getJasperDesign().getDatasetMap().containsKey(name + "_" + i)) {
                                newDataset.setName(name + "_" + i);
                                break;
                                }
                            }

                            getJasperDesign().addDataset(newDataset);

                        } catch (JRException ex) {
                            ex.printStackTrace();
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
    protected Sheet createSheet() {
        //System.out.println("Requested createSheet!!!");
        //.out.flush();
        
        Sheet sheet = super.createSheet();
        Sheet.Set reportPropertiesSet = Sheet.createPropertiesSet();
        reportPropertiesSet.setName("REPORT_PROPERTIES");
        reportPropertiesSet.setDisplayName(I18n.getString("ReportNode.Display.Report_properties"));//NOI18N
        reportPropertiesSet.put(new NameProperty( jd ));
        sheet.put(reportPropertiesSet);
        
        Sheet.Set pageSizeSet = Sheet.createPropertiesSet();
        pageSizeSet.setName("PAGE_SIZE");
        pageSizeSet.setDisplayName(I18n.getString("ReportNode.Display.Pagesize"));//NOI18N
        pageSizeSet.put(new PageWidthProperty( jd ));
        pageSizeSet.put(new PageHeightProperty( jd ));
        pageSizeSet.put(new OrientationProperty( jd ));
        sheet.put(pageSizeSet);
        
        Sheet.Set marginsSet = Sheet.createPropertiesSet();
        marginsSet.setName("PAGE_MARGINS");
        marginsSet.setDisplayName(I18n.getString("ReportNode.Display.Margins"));
        marginsSet.put(new LeftMarginProperty( jd ));
        marginsSet.put(new RightMarginProperty( jd ));
        marginsSet.put(new TopMarginProperty( jd ));
        marginsSet.put(new BottomMarginProperty( jd ));
        sheet.put(marginsSet);
        
        Sheet.Set columnsSet = Sheet.createPropertiesSet();
        columnsSet.setName("PAGE_COLUMNS");
        columnsSet.setDisplayName(I18n.getString("ReportNode.Display.Columns"));
        columnsSet.put(new ColumnCountProperty( jd ));
        columnsSet.put(new ColumnWidthProperty( jd ));
        columnsSet.put(new ColumnSpacingProperty( jd ));
        columnsSet.put(new PrintOrderProperty( jd ));
        sheet.put(columnsSet);
        
        Sheet.Set moreSet = Sheet.createPropertiesSet();
        moreSet.setName("PAGE_MORE");
        moreSet.setDisplayName(I18n.getString("ReportNode.Display.More"));
        DatasetNode.fillDatasetPropertySet(moreSet, jd.getMainDesignDataset(), jd );
        moreSet.put(new JRPropertiesMapProperty( jd ));
        moreSet.put(new TitleNewPageProperty( jd ));
        moreSet.put(new SummaryNewPageProperty( jd ));
        moreSet.put(new SummaryWithPageHeaderAndFooterProperty( jd ));
        moreSet.put(new FloatColumnFooterProperty( jd ));
        moreSet.put(new IgnorePaginationProperty( jd ));
        moreSet.put(new ColumnDirectionProperty(jd));
        moreSet.put(new WhenNoDataTypeProperty( jd ));
        moreSet.put(new LanguageProperty( jd ));
        moreSet.put(new FormatFactoryClassProperty( jd ));
        moreSet.put(new JRImportsProperty( jd ));
        sheet.put(moreSet);
        
        return sheet;
    }
    
    @Override
    public Action[] getActions(boolean context) {
        
        Action[] actions = super.getActions(context);
        java.util.ArrayList<Action> myactions = new java.util.ArrayList<Action>();


        myactions.add(SystemAction.get(EditPageFormatAction.class));
        myactions.add(SystemAction.get(RemoveMarginsAction.class));
        myactions.add(null);
        myactions.add(SystemAction.get(CompileReportAction.class));
        myactions.add(null);
        for (int i=0; i<actions.length; ++i)
        {
            myactions.add(actions[i]);
        }
        myactions.add(null);
        
        myactions.add(SystemAction.get(EditQueryAction.class));
        myactions.add(null);
        myactions.add(SystemAction.get(ReportGroupWizardAction.class));
        myactions.add(SystemAction.get(AddDatasetAction.class));
        myactions.add(SystemAction.get(PasteAction.class));
        myactions.add(null);
        myactions.add(SystemAction.get(OpenReportDirectoryInFavoritesAction.class));
        //testPropertiesAction);



        return myactions.toArray(new Action[myactions.size()]);
    }
    
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_PAGE_WIDTH property
     */
    private static final class NameProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public NameProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_NAME,String.class, I18n.getString("ReportNode.Property.ReportName"), I18n.getString("ReportNode.Property.DefaultName"), true, true);
                this.jasperDesign = jd;
                this.setValue("oneline", Boolean.TRUE);
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.getName();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof String)
                {
                    String oldValue = jasperDesign.getName();
                    String newValue = (String)val;
                    jasperDesign.setName(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "Name", 
                                String.class,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            
                }
            }
    }
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_PAGE_WIDTH property
     */
    private static final class PageWidthProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public PageWidthProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_PAGE_WIDTH,Integer.class, I18n.getString("ReportNode.Property.Pagewidth"), I18n.getString("ReportNode.Property.Pagewidthdetails"), true, true);
                this.jasperDesign = jd;
            }
            
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.getPageWidth();
            }

            // TODO: check page width consistency
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Integer)
                {
                    Integer oldValue = jasperDesign.getPageWidth();
                    Integer newValue = (Integer)val;
                    jasperDesign.setPageWidth(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "PageWidth", 
                                Integer.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                    adjustColumns(jasperDesign);
                }
            }
    }
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_PAGE_HEIGHT property
     */
    private static final class PageHeightProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public PageHeightProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_PAGE_HEIGHT,Integer.class, I18n.getString("ReportNode.Property.Pageheight"), I18n.getString("ReportNode.Property.PageHeightdetails"), true, true);
                this.jasperDesign = jd;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.getPageHeight();
            }

            // TODO: check page height consistency
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Integer)
                {
                    Integer oldValue = jasperDesign.getPageHeight();
                    Integer newValue = (Integer)val;
                    jasperDesign.setPageHeight(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "PageHeight", 
                                Integer.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                }
            }
    }
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_LEFT_MARGIN property
     */
    private static final class LeftMarginProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public LeftMarginProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_LEFT_MARGIN,Integer.class, I18n.getString("ReportNode.Property.Leftmargin"), I18n.getString("ReportNode.Property.Leftmargindetails"), true, true);
                this.jasperDesign = jd;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.getLeftMargin();
            }

            // TODO: check page width with this margin consistency
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Integer)
                {
                    Integer oldValue = jasperDesign.getLeftMargin();
                    Integer newValue = (Integer)val;
                    jasperDesign.setLeftMargin(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "LeftMargin", 
                                Integer.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                    adjustColumns(jasperDesign);
                }
            }
    }
    
     /**
     *  Class to manage the JasperDesign.PROPERTY_RIGHT_MARGIN property
     */
    private static final class RightMarginProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public RightMarginProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_RIGHT_MARGIN,Integer.class, I18n.getString("ReportNode.Property.Rightmargin"), I18n.getString("ReportNode.Property.Rightmargindetails"), true, true);
                this.jasperDesign = jd;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.getRightMargin();
            }

            // TODO: check page width with this margin consistency
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Integer)
                {
                    Integer oldValue = jasperDesign.getRightMargin();
                    Integer newValue = (Integer)val;
                    jasperDesign.setRightMargin(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "RightMargin", 
                                Integer.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                    
                    adjustColumns(jasperDesign);
                }
            }
    }
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_TOP_MARGIN property
     */
    private static final class TopMarginProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public TopMarginProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_TOP_MARGIN,Integer.class,  I18n.getString("ReportNode.Property.Topmargin"),  I18n.getString("ReportNode.Property.Topmargindetail"), true, true);
                this.jasperDesign = jd;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.getTopMargin();
            }

            // TODO: check page height with this margin consistency
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Integer)
                {
                    Integer oldValue = jasperDesign.getTopMargin();
                    Integer newValue = (Integer)val;
                    jasperDesign.setTopMargin(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "TopMargin", 
                                Integer.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                }
            }
            
    }
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_BOTTOM_MARGIN property
     */
    private static final class BottomMarginProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public BottomMarginProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_BOTTOM_MARGIN,Integer.class, I18n.getString("ReportNode.Property.Bottomargin"), I18n.getString("ReportNode.Property.Bottomargindetails"), true, true);
                this.jasperDesign = jd;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.getBottomMargin();
            }

            // TODO: check page height with this margin consistency
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Integer)
                {
                    Integer oldValue = jasperDesign.getBottomMargin();
                    Integer newValue = (Integer)val;
                    jasperDesign.setBottomMargin(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "BottomMargin", 
                                Integer.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                }
            }
    }
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_BOTTOM_MARGIN property
     */
    private static final class ColumnCountProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public ColumnCountProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_COLUMN_COUNT,Integer.class, I18n.getString("ReportNode.Property.Columns"), I18n.getString("ReportNode.Property.Columnsdetails"), true, true);
                this.jasperDesign = jd;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.getColumnCount();
            }

            // TODO: check page height with this margin consistency
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Integer)
                {
                    Integer oldValue = jasperDesign.getColumnCount();
                    Integer newValue = (Integer)val;
                    
                    if (newValue <= 0)
                    {
                        IllegalArgumentException iae = annotateException(I18n.getString("ReportNode.Warning.Columns"));
                        throw iae; 
                    }
                    
                    jasperDesign.setColumnCount(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "ColumnCount", 
                                Integer.TYPE,
                                oldValue,newValue);
                    
                    adjustColumns(jasperDesign);
                    
                    
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                }
            }
            
            public IllegalArgumentException annotateException(String msg)
            {
                IllegalArgumentException iae = new IllegalArgumentException(msg); 
                ErrorManager.getDefault().annotate(iae, 
                                        ErrorManager.EXCEPTION,
                                        msg,
                                        msg, null, null); 
                return iae;
            }
    }
    
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_BOTTOM_MARGIN property
     */
    private static final class ColumnWidthProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public ColumnWidthProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_COLUMN_WIDTH,Integer.class, I18n.getString("ReportNode.Property.ColumnWidth"),I18n.getString("ReportNode.Property.ColumnWidthdetails"), true, true);
                this.jasperDesign = jd;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.getColumnWidth();
            }

            // TODO: check page height with this margin consistency
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Integer)
                {
                    Integer oldValue = jasperDesign.getColumnWidth();
                    Integer newValue = (Integer)val;
                    

                    int available = jasperDesign.getPageWidth();
                    available -= jasperDesign.getLeftMargin();
                    available -= jasperDesign.getRightMargin();
                    available /= jasperDesign.getColumnCount();
                    //available -= jasperDesign.getColumnSpacing()*jasperDesign.getColumnCount()-1;
                    
                    if (newValue > available)
                    {
                        IllegalArgumentException iae = annotateException(I18n.getString("ReportNode.Exception.ColumnWidth",available));
                        throw iae; 
                    }
                    
                    jasperDesign.setColumnWidth(newValue);
                    
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "ColumnWidth", 
                                Integer.TYPE,
                                oldValue,newValue);
                    
                    if (jasperDesign.getColumnCount() > 1)
                    {
                        available = jasperDesign.getPageWidth();
                        available -= jasperDesign.getLeftMargin();
                        available -= jasperDesign.getRightMargin();
                        available -= jasperDesign.getColumnCount()*newValue;
                        available /= jasperDesign.getColumnCount()-1;
                        // Recalculate the column spacing...
                        ObjectPropertyUndoableEdit urob2 =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "ColumnWidth", 
                                Integer.TYPE,
                                jasperDesign.getColumnSpacing(),available);
                        
                        jasperDesign.setColumnSpacing(available);
                        urob.concatenate(urob2);
                    }
                    
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                }
            }
            
            public IllegalArgumentException annotateException(String msg)
            {
                IllegalArgumentException iae = new IllegalArgumentException(msg); 
                ErrorManager.getDefault().annotate(iae, 
                                        ErrorManager.EXCEPTION,
                                        msg,
                                        msg, null, null); 
                return iae;
            }
    }
    
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_BOTTOM_MARGIN property
     */
    private static final class ColumnSpacingProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public ColumnSpacingProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_COLUMN_SPACING,Integer.class, I18n.getString("ReportNode.Property.ColumnSpacing"), I18n.getString("ReportNode.Property.ColumnSpacingdetails"), true, true);
                this.jasperDesign = jd;
            }

            @Override
            public boolean canWrite() {
                return jasperDesign.getColumnCount() > 1;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.getColumnSpacing();
            }

            // TODO: check page height with this margin consistency
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Integer)
                {
                    Integer oldValue = jasperDesign.getColumnSpacing();
                    Integer newValue = (Integer)val;
                    

                    int available = jasperDesign.getPageWidth();
                    available -= jasperDesign.getLeftMargin();
                    available -= jasperDesign.getRightMargin();
                    //available -= jasperDesign.getColumnCount()*jasperDesign.getColumnWidth();
                    available /= jasperDesign.getColumnCount()-1;
                    
                    if (newValue > available)
                    {
                        IllegalArgumentException iae = annotateException(I18n.getString("ReportNode.Exception.ColumnSpace",available));
                        throw iae; 
                    }
                    
                    jasperDesign.setColumnSpacing(newValue);
                    
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "ColumnSpacing", 
                                Integer.TYPE,
                                oldValue,newValue);
                    
                    // Adjust column width...
                    available = jasperDesign.getPageWidth();
                    available -= jasperDesign.getLeftMargin();
                    available -= jasperDesign.getRightMargin();
                    available -= ((jasperDesign.getColumnCount()-1) * newValue);
                    available /= jasperDesign.getColumnCount();

                    // Recalculate the column spacing...
                    ObjectPropertyUndoableEdit urob2 =
                        new ObjectPropertyUndoableEdit(
                            jasperDesign,
                            "ColumnWidth", 
                            Integer.TYPE,
                            jasperDesign.getColumnWidth(),available);

                    jasperDesign.setColumnWidth(available);
                    urob.concatenate(urob2);

                    
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                }
            }
            
            public IllegalArgumentException annotateException(String msg)
            {
                IllegalArgumentException iae = new IllegalArgumentException(msg); 
                ErrorManager.getDefault().annotate(iae, 
                                        ErrorManager.EXCEPTION,
                                        msg,
                                        msg, null, null); 
                return iae;
            }
    }
    
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_ORIENTATION property
     */
    private static final class OrientationProperty extends EnumProperty
    {
            private final JasperDesign jasperDesign;
            private ComboBoxPropertyEditor editor;
            
            @SuppressWarnings("unchecked")
            public OrientationProperty(JasperDesign jd)
            {
                super(OrientationEnum.class, jd);
                this.jasperDesign = jd;
                setValue("suppressCustomEditor", Boolean.TRUE);
            }

    @Override
    public String getName()
    {
        return JasperDesign.PROPERTY_ORIENTATION;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("ReportNode.Property.Orientation");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("ReportNode.Property.Orientationdetails");
    }

    @Override
    public List getTagList()
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(OrientationEnum.PORTRAIT, I18n.getString("ReportNode.Orientation.Portrait")));
        tags.add(new Tag(OrientationEnum.LANDSCAPE, I18n.getString("ReportNode.Orientation.Landscape")));
        return tags;
    }

    @Override
    public Object getPropertyValue()
    {
        return jasperDesign.getOrientationValue();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getPropertyValue();
    }

    @Override
    public Object getDefaultValue()
    {
        return OrientationEnum.PORTRAIT;
    }

    @Override
    public void setPropertyValue(Object alignment)
    {
        jasperDesign.setOrientation((OrientationEnum)alignment);

        int pWidth = jasperDesign.getPageWidth();
                    int pHeight = jasperDesign.getPageHeight();

                    if ((jasperDesign.getOrientationValue() == OrientationEnum.LANDSCAPE && pWidth < pHeight) ||
                        (jasperDesign.getOrientationValue() == OrientationEnum.PORTRAIT && pWidth > pHeight))
                    {
                        jasperDesign.setPageWidth(pHeight);
                        jasperDesign.setPageHeight(pWidth);

                        // switch height and width...
                        ObjectPropertyUndoableEdit urob1 =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "PageWidth",
                                Integer.TYPE,
                                pWidth,pHeight);
                        IReportManager.getInstance().addUndoableEdit(urob1, true);
                        ObjectPropertyUndoableEdit urob2 =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "PageHeight",
                                Integer.TYPE,
                                pHeight,pWidth);
                        IReportManager.getInstance().addUndoableEdit(urob2, true);

                        // Adjust the columns width...
                        if (jasperDesign.getColumnCount() > 0) // Do it always...
                        {
                            int availableSpace = jasperDesign.getPageWidth() - jasperDesign.getLeftMargin() - jasperDesign.getRightMargin();
                            availableSpace -= (jasperDesign.getColumnCount()-1) * jasperDesign.getColumnSpacing();
                            int columnWidth = availableSpace / jasperDesign.getColumnCount();
                            int oldColumnWidth = jasperDesign.getColumnWidth();

                            jasperDesign.setColumnWidth(columnWidth);
                            ObjectPropertyUndoableEdit urob3 =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "ColumnWidth",
                                Integer.TYPE,
                                oldColumnWidth,columnWidth);
                            IReportManager.getInstance().addUndoableEdit(urob3, true);
                        }

                }
        }
    }
    
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_TITLE_NEW_PAGE property
     */
    private static final class TitleNewPageProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public TitleNewPageProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_TITLE_NEW_PAGE,Boolean.class, I18n.getString("ReportNode.Property.Title"), "ReportNode.Property.Titledetail", true, true);
                this.jasperDesign = jd;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.isTitleNewPage();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Boolean)
                {
                    Boolean oldValue = jasperDesign.isTitleNewPage();
                    Boolean newValue = (Boolean)val;
                    jasperDesign.setTitleNewPage(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "TitleNewPage", 
                                Boolean.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            
                }
            }
    }
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_SUMMARY_NEW_PAGE property
     */
    private static final class SummaryNewPageProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public SummaryNewPageProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_SUMMARY_NEW_PAGE,Boolean.class, I18n.getString("ReportNode.Property.Summary"), I18n.getString("ReportNode.Property.Summarydetails"), true, true);
                this.jasperDesign = jd;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.isSummaryNewPage();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Boolean)
                {
                    Boolean oldValue = jasperDesign.isSummaryNewPage();
                    Boolean newValue = (Boolean)val;
                    jasperDesign.setSummaryNewPage(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "SummaryNewPage", 
                                Boolean.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            
                }
            }
    }


    /**
     *  Class to manage the JasperDesign.PROPERTY_SUMMARY_NEW_PAGE property
     */
    private static final class  SummaryWithPageHeaderAndFooterProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;

            @SuppressWarnings("unchecked")
            public SummaryWithPageHeaderAndFooterProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_SUMMARY_WITH_PAGE_HEADER_AND_FOOTER,Boolean.class, I18n.getString("ReportNode.Property.SummaryWithPageHeaderAndFooter"), I18n.getString("ReportNode.Property.SummaryWithPageHeaderAndFooterdetails"), true, true);
                this.jasperDesign = jd;
            }

            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.isSummaryWithPageHeaderAndFooter();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Boolean)
                {
                    Boolean oldValue = jasperDesign.isSummaryWithPageHeaderAndFooter();
                    Boolean newValue = (Boolean)val;
                    jasperDesign.setSummaryWithPageHeaderAndFooter(newValue);

                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "SummaryWithPageHeaderAndFooter",
                                Boolean.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);

                }
            }
    }
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_FLOAT_COLUMN_FOOTER property
     */
    private static final class FloatColumnFooterProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public FloatColumnFooterProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_FLOAT_COLUMN_FOOTER,Boolean.class,I18n.getString("ReportNode.Property.FloatColumnFooter"), I18n.getString("ReportNode.Property.FloatColumnFooterdetail"), true, true);
                this.jasperDesign = jd;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.isFloatColumnFooter();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Boolean)
                {
                    Boolean oldValue = jasperDesign.isFloatColumnFooter();
                    Boolean newValue = (Boolean)val;
                    jasperDesign.setFloatColumnFooter(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "FloatColumnFooter", 
                                Boolean.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            
                }
            }
    }
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_IGNORE_PAGINATION property
     */
    private static final class IgnorePaginationProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public IgnorePaginationProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_IGNORE_PAGINATION,Boolean.class, I18n.getString("ReportNode.property.Pagination"),  I18n.getString("ReportNode.property.Paginationdetail"), true, true);
                this.jasperDesign = jd;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return jasperDesign.isIgnorePagination();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Boolean)
                {
                    Boolean oldValue = jasperDesign.isIgnorePagination();
                    Boolean newValue = (Boolean)val;
                    jasperDesign.setIgnorePagination(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "IgnorePagination", 
                                Boolean.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            
                }
            }
    }
    
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_ORIENTATION property
     */
    private static final class PrintOrderProperty extends EnumProperty
    {
            private final JasperDesign jasperDesign;
            private ComboBoxPropertyEditor editor;
            
            @SuppressWarnings("unchecked")
            public PrintOrderProperty(JasperDesign jd)
            {
                super(PrintOrderEnum.class, jd);
                this.jasperDesign = jd;
                setValue("suppressCustomEditor", Boolean.TRUE);
            }

            @Override
            public String getName()
            {
                return JasperDesign.PROPERTY_PRINT_ORDER;
            }

            @Override
            public String getDisplayName()
            {
                return I18n.getString("ReportNode.Property.Print");
            }

            @Override
            public String getShortDescription()
            {
                return I18n.getString("ReportNode.Property.Printdetail");
            }

            @Override
            public List getTagList()
            {
                List tags = new java.util.ArrayList();
                tags.add(new Tag(PrintOrderEnum.VERTICAL, I18n.getString("ReportNode.Property.Vertical")));
                tags.add(new Tag(PrintOrderEnum.HORIZONTAL, I18n.getString("ReportNode.Property.Horizontal")));
                return tags;
            }

            @Override
            public Object getPropertyValue()
            {
                return jasperDesign.getPrintOrderValue();
            }

            @Override
            public Object getOwnPropertyValue()
            {
                return getPropertyValue();
            }

            @Override
            public Object getDefaultValue()
            {
                return PrintOrderEnum.VERTICAL;
            }

            @Override
            public void setPropertyValue(Object val)
            {
                jasperDesign.setPrintOrder((PrintOrderEnum)val);
            }
    }
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_PAGE_WIDTH property
     */
    private static final class FormatFactoryClassProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
        
            @SuppressWarnings("unchecked")
            public FormatFactoryClassProperty(JasperDesign jd)
            {
                super(JasperDesign.PROPERTY_FORMAT_FACTORY_CLASS,String.class, I18n.getString("ReportNode.Property.FactoryClass"), I18n.getString("ReportNode.Property.FactoryClassdetail"), true, true);
                this.jasperDesign = jd;
                this.setValue("oneline", Boolean.TRUE);
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return (jasperDesign.getFormatFactoryClass() == null) ? "" : jasperDesign.getFormatFactoryClass();
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof String)
                {
                    String oldValue = jasperDesign.getFormatFactoryClass();
                    String newValue = (val == null || ((String)val).trim().length() == 0) ? null : ((String)val).trim();
                    jasperDesign.setFormatFactoryClass(newValue);
                
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                jasperDesign,
                                "FormatFactoryClass", 
                                String.class,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            
                }
            }
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) return;
        
        if (evt.getPropertyName().equals(JasperDesign.PROPERTY_BACKGROUND) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_TITLE) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_PAGE_HEADER) ||    
            evt.getPropertyName().equals(JasperDesign.PROPERTY_COLUMN_HEADER) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_DETAIL) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_COLUMN_FOOTER) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_PAGE_FOOTER) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_LAST_PAGE_FOOTER) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_SUMMARY) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_NO_DATA) ||
            evt.getPropertyName().equals(JRDesignDataset.PROPERTY_GROUPS) ||
            evt.getPropertyName().equals(JRDesignGroup.PROPERTY_GROUP_HEADER) ||
            evt.getPropertyName().equals(JRDesignGroup.PROPERTY_GROUP_FOOTER) ||
            evt.getPropertyName().equals(JasperDesign.PROPERTY_DATASETS) ||
            evt.getPropertyName().equals(JRDesignSection.PROPERTY_BANDS)
            )
        {
            updateSectionListeners();
            ((ReportChildren)getChildren()).updateChildren();
        }
        
        if (evt.getPropertyName().equals( JasperDesign.PROPERTY_NAME))
        {
            this.fireDisplayNameChange(null, jd.getName());
        }
        
        if (evt.getPropertyName().equals( JasperDesign.PROPERTY_COLUMN_COUNT) ||
            evt.getPropertyName().equals( JasperDesign.PROPERTY_COLUMN_SPACING) ||
            evt.getPropertyName().equals( JasperDesign.PROPERTY_COLUMN_WIDTH) ||
            evt.getPropertyName().equals( JasperDesign.PROPERTY_ORIENTATION) ||
            evt.getPropertyName().equals( JasperDesign.PROPERTY_LEFT_MARGIN) ||
            evt.getPropertyName().equals( JasperDesign.PROPERTY_RIGHT_MARGIN) ||
            evt.getPropertyName().equals( JasperDesign.PROPERTY_PAGE_WIDTH))
        {
            this.firePropertyChange(JasperDesign.PROPERTY_COLUMN_COUNT, null, jd.getColumnCount() );
            this.firePropertyChange(JasperDesign.PROPERTY_COLUMN_SPACING, null, jd.getColumnSpacing() );
            this.firePropertyChange(JasperDesign.PROPERTY_COLUMN_WIDTH, null, jd.getColumnWidth() );
        }
        
        if (ModelUtils.containsProperty(  this.getPropertySets(), evt.getPropertyName()))
        {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
        }
        
        if (evt.getPropertyName().equals(JRDesignDataset.PROPERTY_GROUPS))
        {
            // refresh group listening...
            for (int i=0; i<this.jd.getGroupsList().size(); ++i)
            {
                JRDesignGroup grp = (JRDesignGroup)this.jd.getGroupsList().get(i);
                grp.getEventSupport().removePropertyChangeListener(this);
                grp.getEventSupport().addPropertyChangeListener(this);
            }
        }
        
        /*
        else if (evt.getPropertyName().equals( JasperDesign.PROPERTY_PAGE_HEIGHT) ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_PAGE_WIDTH) ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_ORIENTATION) ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_RIGHT_MARGIN) ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_LEFT_MARGIN) ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_TOP_MARGIN) ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_BOTTOM_MARGIN) ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_TITLE_NEW_PAGE) ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_SUMMARY_NEW_PAGE) ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_FLOAT_COLUMN_FOOTER) ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_PRINT_ORDER) ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_IGNORE_PAGINATION) ||
                 evt.getPropertyName().equals(  "WhenNoDataType") ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_FORMAT_FACTORY_CLASS) ||
                 evt.getPropertyName().equals( JasperDesign.PROPERTY_LANGUAGE) ||
                 DatasetNode.acceptProperty(evt))
        {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
        }
        */
    }

    public static void adjustColumns(JasperDesign jasperDesign)
    {
        // Recalculate the column width...
        int total = jasperDesign.getPageWidth();
        total -= jasperDesign.getLeftMargin();
        total -= jasperDesign.getRightMargin();
        if (jasperDesign.getColumnCount() > 1)
        {
            total -= jasperDesign.getColumnSpacing()*(jasperDesign.getColumnCount()-1);
        }

        total /= jasperDesign.getColumnCount();

        ObjectPropertyUndoableEdit urob2 =
                new ObjectPropertyUndoableEdit(
                    jasperDesign,
                    "ColumnWidth", 
                    Integer.TYPE,
                    jasperDesign.getColumnWidth(),total);

        jasperDesign.setColumnWidth(total);
        IReportManager.getInstance().addUndoableEdit(urob2, true);

        if (jasperDesign.getColumnCount() == 1 &&
            jasperDesign.getColumnSpacing() > 0)
        {
            ObjectPropertyUndoableEdit urob3 =
                new ObjectPropertyUndoableEdit(
                    jasperDesign,
                    "ColumnSpacing", 
                    Integer.TYPE,
                    jasperDesign.getColumnSpacing(),0);

            jasperDesign.setColumnSpacing(0);
            IReportManager.getInstance().addUndoableEdit(urob3, true);
        }
    }
    
    
    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> type) {

        Object o = getLookup().lookup(type);
        if (o == null && SaveCookie.class.isAssignableFrom(type))
        {
            o = IReportManager.getInstance().getActiveVisualView().getEditorSupport().getDataObject().getLookup().lookup(SaveCookie.class);
        }
        
        if (o == null && Node.Cookie.class.isAssignableFrom(type)) // try to look in the super cookie...
        {
           o = super.getCookie(type); 
        }
        
        return o instanceof Node.Cookie ? (T)o : null;
    }

    public boolean hasExpression(JRDesignExpression ex) {
        if (jd.getFilterExpression() == ex) return true;
        return false;
    }

    public ExpressionContext getExpressionContext(JRDesignExpression ex) {
        return new ExpressionContext( jd.getMainDesignDataset() );
    }
    
}


/**
 *  Class to manage the JasperDesign.PROPERTY_ORIENTATION property
 */
class ColumnDirectionProperty extends AbstractProperty
{
        private final JasperDesign jd;
        private ComboBoxPropertyEditor editor = null;

        @SuppressWarnings("unchecked")
        public ColumnDirectionProperty(JasperDesign jd)
        {
            super(RunDirectionEnum.class,jd);
            this.jd = jd;
            setValue("suppressCustomEditor", Boolean.TRUE);
        }

        @Override
        @SuppressWarnings("unchecked")
        public PropertyEditor getPropertyEditor()
        {
            if (editor == null)
            {
                editor = new ComboBoxPropertyEditor(false, getTagList());
            }
            return editor;
        }

        @Override
        public String getName()
        {
            return JasperDesign.PROPERTY_COLUMN_DIRECTION;
        }

        @Override
        public String getDisplayName()
        {
            return I18n.getString("Global.Property.ColumnDirection");
        }

        @Override
        public String getShortDescription()
        {
            return I18n.getString("Global.Property.ColumnDirection.desc");
        }

        public List getTagList()
        {
            List tags = new java.util.ArrayList();
            tags.add(new Tag(RunDirectionEnum.LTR, I18n.getString("Global.Property.ColumnDirection.LTR")));
            tags.add(new Tag(RunDirectionEnum.RTL, I18n.getString("Global.Property.ColumnDirection.RTL")));

            return tags;
        }

        @Override
        public Object getPropertyValue() {
            return jd.getColumnDirection();
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
        public void validate(Object value) {

        }

        @Override
        public void setPropertyValue(Object value) {
            if (value != null && value instanceof RunDirectionEnum)
            {
                jd.setColumnDirection( (RunDirectionEnum)value);
            }
        }

}
