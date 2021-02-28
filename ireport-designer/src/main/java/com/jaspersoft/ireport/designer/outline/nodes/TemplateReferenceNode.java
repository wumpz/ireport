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
import com.jaspersoft.ireport.designer.actions.RefreshStylesAction;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.ExpressionProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javax.swing.Action;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.ErrorManager;
import org.openide.actions.DeleteAction;
import org.openide.actions.RenameAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.PropertySupport;
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
public class TemplateReferenceNode extends AbstractNode implements PropertyChangeListener
{

    private JasperDesign jd;
    private JRDesignReportTemplate template = null;

    public TemplateReferenceNode(JasperDesign jd, JRDesignReportTemplate template, Lookup doLkp)
    {
        super (new TemplateReferenceChildren(jd, template, doLkp), new ProxyLookup(doLkp, Lookups.fixed(jd, template)));
        this.jd = jd;
        this.template = template;
        init();
        template.getEventSupport().addPropertyChangeListener(this);
    }

    @Override
    public String getName() {
        return Misc.getExpressionText(template.getSourceExpression());
    }

    @Override
    public String getDisplayName() {
        String fname = "";
        try {
            fname = Misc.getExpressionText(template.getSourceExpression());
            File f = new File(fname);
            fname = f.getName();
            if (fname.startsWith("\""))
            {
                fname = fname.substring(1);
            }
            if (fname.endsWith("\""))
            {
                fname = fname.substring(0, fname.length()-1);
            }
        } catch (Exception ex)
        {
            fname = "" + Misc.getExpressionText(template.getSourceExpression());
            if (fname.length() > 20)
            {
                fname = fname.substring(0, 20) + "...";
            }
        }
        return "Template (" + fname + ")";
    }

    private void init()
    {
        super.setName(Misc.getExpressionText(template.getSourceExpression()));
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/jasperreports_jrtx.png");
    }



    @Override
    public void destroy() throws IOException {

          jd.removeTemplate(template);

          IReportManager.getInstance().notifyReportChange();
          super.destroy();
    }

    /**
     *  This is the function to create the sheet...
     *
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();

        Sheet.Set set = Sheet.createPropertiesSet();

        set.put(new SourceExpressionProperty(template,jd));
        set.put(new SourceExpressionClassNameProperty(template));


        sheet.put(set);
        return sheet;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canRename() {
        return false;
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
    public Action[] getActions(boolean popup) {
        return new Action[] {
            SystemAction.get( RenameAction.class ),
            SystemAction.get( RefreshStylesAction.class ),
            null,
            SystemAction.get( DeleteAction.class ) };
    }

    @Override
    public Transferable drag() throws IOException {
        ExTransferable tras = ExTransferable.create(clipboardCut());
        //tras.put(new ReportObjectPaletteTransferable(
        //            "com.jaspersoft.ireport.designer.palette.actions.CreateTextFieldFromParameterAction",
        //            getParameter()));

        return tras;
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

    public void propertyChange(PropertyChangeEvent evt) {

        // Notify the change in the template...
        IReportManager.getInstance().notifyReportChange();

        if (evt.getPropertyName() == null) return;
        
        if (evt.getPropertyName().equals( JRDesignReportTemplate.PROPERTY_SOURCE_EXPRESSION ))
        {
            super.setName(Misc.getExpressionText(template.getSourceExpression()));
            super.fireNameChange(null, super.getName());
            super.fireDisplayNameChange(null, getDisplayName());
            
        }

        // Update the sheet
        this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
    }

    /**
     * @return the template
     */
    public JRReportTemplate getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(JRDesignReportTemplate template) {
        this.template = template;
    }

    


     /***************  SHEET PROPERTIES DEFINITIONS **********************/


    /**
     *  Class to manage the JRDesignParameter.PROPERTY_NAME property
     */
    public static final class SourceExpressionProperty extends ExpressionProperty {

        private JRDesignReportTemplate template = null;
        private JasperDesign jd=null;

        @SuppressWarnings("unchecked")
        public SourceExpressionProperty(JRDesignReportTemplate template, JasperDesign jd)
        {
            super(template, jd.getMainDesignDataset());

            setName(JRDesignReportTemplate.PROPERTY_SOURCE_EXPRESSION);
            setDisplayName("Source Expression");
            this.template = template;
            this.jd = jd;
        }

        @Override
        public String getDefaultExpressionClassName() {
            return "java.lang.String";
        }

        @Override
        public JRDesignExpression getExpression() {
            return (JRDesignExpression) template.getSourceExpression();
        }

        @Override
        public void setExpression(JRDesignExpression expression) {
            template.setSourceExpression(expression);
        }

        
    }

    /**
     *  Class to manage the JRDesignParameter.PROPERTY_NAME property
     */
    public static final class SourceExpressionClassNameProperty  extends PropertySupport.ReadWrite {


        private final JRDesignReportTemplate template;
        PropertyEditor editor = null;


        public SourceExpressionClassNameProperty(JRDesignReportTemplate template)
        {
            super(JRDesignExpression.PROPERTY_VALUE_CLASS_NAME, String.class,
                  I18n.getString("Expression_Class"),
                  I18n.getString("Expression_Class"));
            this.template = template;

            setValue("canEditAsText", true);
            setValue("oneline", true);
            setValue("suppressCustomEditor", false);
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {

            if (template.getSourceExpression() == null) return "java.lang.String";
            if (template.getSourceExpression().getValueClassName() == null) return "java.lang.String";
            return template.getSourceExpression().getValueClassName();
        }


        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

            JRDesignExpression oldExp =  (JRDesignExpression)template.getSourceExpression();
            JRDesignExpression newExp = null;
            //System.out.println("Setting as value: " + val);

            String newVal = (val != null) ? val+"" : "";
            newVal = newVal.trim();

            if ( newVal.equals("") )
            {
                newVal = null;
            }

            newExp = new JRDesignExpression();
            newExp.setText( (oldExp != null) ? oldExp.getText() : null );
            newExp.setValueClassName( newVal );
            template.setSourceExpression(newExp);

            ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            template,
                            "SourceExpression",
                            JRExpression.class,
                            oldExp,newExp);
                // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);

            //System.out.println("Done: " + val);
        }

        @Override
        public boolean isDefaultValue() {
            return template.getSourceExpression() == null ||
                   template.getSourceExpression().getValueClassName() == null ||
                   template.getSourceExpression().getValueClassName().equals("java.lang.String");
        }

        @Override
        public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
            setValue(null);
            editor.setValue("java.lang.String");
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }

        @Override
        @SuppressWarnings("unchecked")
        public PropertyEditor getPropertyEditor() {

            if (editor == null)
            {
                java.util.List classes = new ArrayList();
                classes.add(new Tag("java.lang.String"));
                classes.add(new Tag("java.io.File"));
                classes.add(new Tag("java.net.URL"));
                classes.add(new Tag("java.io.InputStream"));
                classes.add(new Tag("net.sf.jasperreports.engine.JRTemplate"));

                editor = new ComboBoxPropertyEditor(false, classes);
            }
            return editor;
        }

    }

}
