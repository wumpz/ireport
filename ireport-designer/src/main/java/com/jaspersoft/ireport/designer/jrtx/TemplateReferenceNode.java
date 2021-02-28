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
package com.jaspersoft.ireport.designer.jrtx;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.Action;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import org.openide.ErrorManager;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.RenameAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
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

    private JRSimpleTemplate template;
    private JRTemplateReference reference = null;

    public TemplateReferenceNode(JRSimpleTemplate template, JRTemplateReference reference, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup(doLkp, Lookups.fixed(template, reference)));
        this.template = template;
        this.reference = reference;

        init();
    }

    @Override
    public String getName() {
        return getReference().getLocation();
    }

    @Override
    public String getDisplayName() {
        return "Reference (" + getReference().getLocation() + ")";
    }

    private void init()
    {
        super.setName(getReference().getLocation());
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/jasperreports_jrtx.png");
    }



    @Override
    public void destroy() throws IOException {

          getTemplate().removeIncludedTemplate( getReference() );

          // we need to notify the change...
          JRTXEditorSupport ed = getLookup().lookup(JRTXEditorSupport.class);
          if (ed != null) ed.notifyModelChangeToTheView();

          if (getParentNode() != null && getParentNode() instanceof TemplateNode)
          {
              ((StylesChildren)getParentNode().getChildren()).recalculateKeys();
          }


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

        set.put(new LocationProperty(getReference(),getTemplate(), this));

        sheet.put(set);
        return sheet;
    }

    @Override
    public boolean canCut() {
        return false;
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
    public Action[] getActions(boolean popup) {
        return new Action[] {
            SystemAction.get( RenameAction.class ),
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


    @Override
    @SuppressWarnings("unchecked")
    public void setName(String s) {

        if (s.equals(""))
        {
            throw new IllegalArgumentException("Invalid location");
        }
        
        String oldName = getReference().getLocation();
        getReference().setLocation(s);

        ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                getReference(), "Location", String.class, oldName, getReference().getLocation());

        propertyChange(new PropertyChangeEvent(this,Node.PROP_NAME, null, getReference().getLocation()));

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

        JRTXEditorSupport ed = getLookup().lookup(JRTXEditorSupport.class);
        if (ed != null) ed.notifyModelChangeToTheView();

        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( Node.PROP_NAME ))
        {
            super.setName(getReference().getLocation());
            super.fireNameChange(null, super.getName());
            super.fireDisplayNameChange(null, getDisplayName());
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
        }

        // Update the sheet
        
    }

    /**
     * @return the reference
     */
    public JRTemplateReference getReference() {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(JRTemplateReference reference) {
        this.reference = reference;
    }

    /**
     * @return the template
     */
    public JRSimpleTemplate getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(JRSimpleTemplate template) {
        this.template = template;
    }



     /***************  SHEET PROPERTIES DEFINITIONS **********************/


    /**
     *  Class to manage the JRDesignParameter.PROPERTY_NAME property
     */
    public static final class LocationProperty extends PropertySupport.ReadWrite {

        private JRTemplateReference reference = null;
        JRSimpleTemplate template = null;
        TemplateReferenceNode node = null;

        @SuppressWarnings("unchecked")
        public LocationProperty(JRTemplateReference reference, JRSimpleTemplate template, TemplateReferenceNode node)
        {
            super( Node.PROP_NAME, String.class,
                  "Location",
                  "Location");
            this.setReference(reference);
            this.template = template;
            this.node = node;
            this.setValue("oneline", Boolean.TRUE);
        }

        @Override
        public boolean canWrite()
        {
            return true;
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return getReference().getLocation();
        }

        @SuppressWarnings("unchecked")
        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {


            if (val == null || val.equals(""))
            {
                IllegalArgumentException iae = annotateException(I18n.getString("StyleNode.Exception.NameNotValid"));
                throw iae;
            }

            String s = val+"";

            String oldName = getReference().getLocation();
            getReference().setLocation(s);

            ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                getReference(), "Location", String.class, oldName, getReference().getLocation());

            IReportManager.getInstance().addUndoableEdit(opue);

            if (node != null)
            {
                node.propertyChange(new PropertyChangeEvent(this,Node.PROP_NAME, null, getReference().getLocation()));
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

        /**
         * @return the reference
         */
        public

        JRTemplateReference getReference() {
            return reference;
        }

        /**
         * @param reference the reference to set
         */
        public void setReference(JRTemplateReference reference) {
            this.reference = reference;
        }
    }


}
