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
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.sheet.JRPenProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StringProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StylePatternProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.BackcolorProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.BlankWhenNullProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.BoldProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.FillProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.FirstLineIndentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.FontNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.FontSizeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.ForecolorProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.HorizontalAlignmentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.ItalicProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.LeftIndentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.LineSpacingProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.LineSpacingSizeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.ModeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.PaddingAndBordersProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.PdfEmbeddedProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.PdfEncodingProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.PdfFontNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.RadiusProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.RightIndentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.RotationProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.ScaleImageProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.SpacingAfterProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.SpacingBeforeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.StrikeThroughProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.TabStopWidthProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.TabStopsProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.UnderlineProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.VerticalAlignmentProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import org.openide.ErrorManager;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.RenameAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
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
public class StyleNode extends AbstractNode implements PropertyChangeListener
{

    private JRSimpleTemplate template;
    private JRBaseStyle style = null;

    public StyleNode(JRSimpleTemplate template, JRBaseStyle style, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup(doLkp, Lookups.fixed(template, style)));
        this.template = template;
        this.style = style;

        init();
    }

    @Override
    public String getDisplayName() {
        return getStyle().getName();
    }

    private void init()
    {
        setDisplayName ( style.getName());
        super.setName( style.getName() );
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/style-16.png");
        
        style.getEventSupport().addPropertyChangeListener(this);
        ((JRBasePen)style.getLinePen()).getEventSupport().addPropertyChangeListener(this);

        JRBaseLineBox baseBox = (JRBaseLineBox)style.getLineBox();
        baseBox.getEventSupport().addPropertyChangeListener(this);
        ((JRBasePen)baseBox.getPen()).getEventSupport().addPropertyChangeListener(this);
        ((JRBasePen)baseBox.getTopPen()).getEventSupport().addPropertyChangeListener(this);
        ((JRBasePen)baseBox.getBottomPen()).getEventSupport().addPropertyChangeListener(this);
        ((JRBasePen)baseBox.getLeftPen()).getEventSupport().addPropertyChangeListener(this);
        ((JRBasePen)baseBox.getRightPen()).getEventSupport().addPropertyChangeListener(this);

        ((JRBaseParagraph)style.getParagraph()).getEventSupport().addPropertyChangeListener(this);
    }



    @Override
    public void destroy() throws IOException {

          getTemplate().removeStyle(getStyle());

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

        set.put(new NameProperty((JRDesignStyle)getStyle(),getTemplate()));
        set.put(new DefaultStyleProperty((JRDesignStyle)getStyle(),getTemplate()));
        set.put(new ParentStyleNameProperty( (JRDesignStyle)getStyle()));
        //set.put(new ParentStyleProperty( getStyle(), jd));
        set.put(new ModeProperty( getStyle()));
        set.put(new ForecolorProperty( getStyle()));
        set.put(new BackcolorProperty( getStyle()));
        // Pen....

        set.put(new PaddingAndBordersProperty( getStyle()));
        JRPenProperty penProp = new JRPenProperty(getStyle().getLinePen(), getStyle());

        set.put(penProp);

        // Width...
        set.put(new FillProperty( getStyle()));
        set.put(new RadiusProperty( getStyle()));
        set.put(new ScaleImageProperty( getStyle()));
        

        set.put(new FontNameProperty( getStyle() ));
        set.put(new FontSizeProperty( getStyle() ));

        set.put(new BoldProperty( getStyle() ));
        set.put(new ItalicProperty( getStyle() ));
        set.put(new UnderlineProperty( getStyle() ));
        set.put(new StrikeThroughProperty( getStyle() ));

        set.put(new HorizontalAlignmentProperty( getStyle()));
        set.put(new VerticalAlignmentProperty( getStyle()));
        set.put(new RotationProperty( getStyle() ));

        set.put(new LineSpacingProperty( getStyle() ));
        set.put(new LineSpacingSizeProperty( getStyle() ));
        //set.put(new StyledTextProperty( getStyle() ));


        set.put(new FirstLineIndentProperty( getStyle()  ));
        set.put(new LeftIndentProperty( getStyle()  ));
        set.put(new RightIndentProperty( getStyle() ));
        set.put(new SpacingBeforeProperty( getStyle()  ));
        set.put(new SpacingAfterProperty( getStyle()  ));

        set.put(new TabStopWidthProperty( getStyle()  ));
        set.put(new TabStopsProperty( getStyle()  ));



        set.put(new PdfFontNameProperty( getStyle() ));
        set.put(new PdfEmbeddedProperty( getStyle() ));
        set.put(new PdfEncodingProperty( getStyle() ));

        set.put(new StylePatternProperty( getStyle() ));
        set.put(new BlankWhenNullProperty( getStyle() ));


        sheet.put(set);
        return sheet;
    }

    @Override
    public boolean canCut() {
        return true;
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
            SystemAction.get( CopyAction.class ),
            SystemAction.get( CutAction.class ),
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
            throw new IllegalArgumentException(I18n.getString("StyleNode.Exception.NameNotValid"));
        }

        List<JRDesignStyle> currentStyles = new ArrayList<JRDesignStyle>();
        JRStyle[] ss = getTemplate().getStyles();
        for (int i=0; i<ss.length; ++i)
        {
            currentStyles.add((JRDesignStyle)ss[i]);
        }

        for (JRDesignStyle st : currentStyles)
        {
            if (st != getStyle() && st.getName().equals(s))
            {
                IllegalArgumentException iae = annotateException(I18n.getString("StyleNode.Exception.NameInUse"));
                throw iae;
            }
        }
        String oldName = getStyle().getName();
        ((JRDesignStyle)getStyle()).setName(s);

        ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                getStyle(), "Name", String.class, oldName, getStyle().getName());



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
        if (evt.getPropertyName().equals( JRDesignStyle.PROPERTY_NAME ))
        {
            super.setName(getStyle().getName());
            this.setDisplayName(getStyle().getName());
        }
        else if (evt.getPropertyName().equals(JRBasePen.PROPERTY_LINE_COLOR) ||
                 evt.getPropertyName().equals(JRBasePen.PROPERTY_LINE_STYLE) ||
                 evt.getPropertyName().equals(JRBasePen.PROPERTY_LINE_WIDTH))
        {

            if (ModelUtils.containsProperty(this.getPropertySets(),"pen"))
            {
                this.firePropertyChange("pen", evt.getOldValue(), evt.getNewValue() );
            }

            if (ModelUtils.containsProperty(this.getPropertySets(),"linebox"))
            {
                this.firePropertyChange("linebox", evt.getOldValue(), evt.getNewValue() );
            }
        }
        else if (evt.getPropertyName().equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING) ||
                 evt.getPropertyName().equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING) ||
                 evt.getPropertyName().equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING) ||
                 evt.getPropertyName().equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING))
        {
            if (ModelUtils.containsProperty(this.getPropertySets(),"linebox"))
            {
                this.firePropertyChange("linebox", evt.getOldValue(), evt.getNewValue() );
            }
        }

        // Update the sheet
        this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
    }

    public JRBaseStyle getStyle() {
        return style;
    }

    public void setStyle(JRBaseStyle style) {
        this.style = style;
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
    public static final class NameProperty extends PropertySupport.ReadWrite {

        JRDesignStyle style = null;
        JRSimpleTemplate template = null;

        @SuppressWarnings("unchecked")
        public NameProperty(JRDesignStyle style, JRSimpleTemplate template)
        {
            super(JRDesignStyle.PROPERTY_NAME, String.class,
                  I18n.getString("StyleNode.Property.Name"),
                  I18n.getString("StyleNode.Property.NameStyle"));
            this.style = style;
            this.template = template;
            this.setValue("oneline", Boolean.TRUE);
        }

        @Override
        public boolean canWrite()
        {
            return true;
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return getStyle().getName();
        }

        @SuppressWarnings("unchecked")
        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {


            if (val == null || val.equals(""))
            {
                IllegalArgumentException iae = annotateException(I18n.getString("StyleNode.Exception.NameNotValid"));
                throw iae;
            }

            String s = val+"";

            List<JRDesignStyle> currentStyles = new ArrayList<JRDesignStyle>();
            JRStyle[] ss = template.getStyles();
            for (int i=0; i<ss.length; ++i)
            {
                currentStyles.add((JRDesignStyle)ss[i]);
            }

            for (JRDesignStyle st : currentStyles)
            {
                if (st != getStyle() && st.getName().equals(s))
                {
                    IllegalArgumentException iae = annotateException(I18n.getString("StyleNode.Exception.NameInUse"));
                    throw iae;
                }
            }
            String oldName = getStyle().getName();
            getStyle().setName(s);

            ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    getStyle(), "Name", String.class, oldName, getStyle().getName());

            IReportManager.getInstance().addUndoableEdit(opue);

        }

        public JRDesignStyle getStyle() {
            return style;
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
     *  Class to manage the JRDesignParameter.PROPERTY_NAME property
     */
    public static final class ParentStyleNameProperty extends StringProperty {

        JRDesignStyle style = null;

        @SuppressWarnings("unchecked")
        public ParentStyleNameProperty(JRDesignStyle style)
        {
            super(style);

            setName(JRDesignStyle.PROPERTY_PARENT_STYLE_NAME_REFERENCE);
            setDisplayName("Parent Style Name");
            setShortDescription("Parent Style Name");
            this.style = style;
            this.setValue("oneline", Boolean.TRUE);
        }

        @Override
        public String getString() {
            return style.getStyleNameReference();
        }

        @Override
        public String getOwnString() {
            return style.getStyleNameReference();
        }

        @Override
        public String getDefaultString() {
            return null;
        }

        @Override
        public void setString(String value) {
            style.setParentStyleNameReference(value);
        }


    }

    /**
     *  Class to manage the JRDesignParameter.PROPERTY_FOR_PROMPTING property
     */
    public static final class DefaultStyleProperty extends PropertySupport.ReadWrite {

        JRDesignStyle style = null;
        JRSimpleTemplate template = null;

        @SuppressWarnings("unchecked")
        public DefaultStyleProperty(JRDesignStyle style, JRSimpleTemplate template)
        {
            super(JRDesignStyle.PROPERTY_DEFAULT, Boolean.class,
                  "Default Style",
                  I18n.getString("StyleNode.Property.DefaultStyle"));
            this.template = template;
            this.style = style;
        }

        @Override
        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return new Boolean( getStyle().isDefault() );
        }

        @Override
        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

            if (val != null && val instanceof Boolean)
            {
                Boolean oldValue = getStyle().isDefault();
                Boolean newValue = (Boolean)val;
                ObjectPropertyUndoableEdit urob2 = null;


                // Find if there is another default style...
                if (newValue.booleanValue())
                {
                    List<JRDesignStyle> list = new ArrayList<JRDesignStyle>();
                    JRStyle[] ss = template.getStyles();
                    for (int i=0; i<ss.length; ++i)
                    {
                        list.add((JRDesignStyle)ss[i]);
                    }

                    for (JRDesignStyle st : list)
                    {
                        if (st.isDefault())
                        {
                            st.setDefault(false);
                            urob2 = new ObjectPropertyUndoableEdit(
                                st,
                                "Default",
                                Boolean.TYPE,
                                Boolean.TRUE,Boolean.FALSE);
                            break;
                        }
                    }
                }

                getStyle().setDefault(newValue);

                ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            getStyle(),
                            "Default",
                            Boolean.TYPE,
                            oldValue,newValue);
                if (urob2 != null)
                {
                    urob.concatenate(urob2);
                }
                // Find the undoRedo manager...
            
            }
        }

        @Override
        public boolean isDefaultValue() {
            return !getStyle().isDefault();
        }

        @Override
        public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
            super.restoreDefaultValue();
            setValue(Boolean.FALSE);
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }

        public JRDesignStyle getStyle() {
            return style;
        }


    }
}
