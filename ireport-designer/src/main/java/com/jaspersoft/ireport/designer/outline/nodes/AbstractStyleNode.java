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

import com.jaspersoft.ireport.designer.sheet.properties.style.RadiusProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.StrikeThroughProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.ScaleImageProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.PdfEmbeddedProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.PaddingAndBordersProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.HorizontalAlignmentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.FillProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.BoldProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.BlankWhenNullProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.BackcolorProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.UnderlineProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.VerticalAlignmentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.ModeProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.sheet.JRPenProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.FirstLineIndentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.MarkupProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StylePatternProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.FontNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.FontSizeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.ForecolorProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.ItalicProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.LeftIndentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.LineSpacingProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.LineSpacingSizeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.PdfEncodingProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.PdfFontNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.RightIndentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.RotationProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.SpacingAfterProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.SpacingBeforeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.TabStopWidthProperty;
import com.jaspersoft.ireport.designer.sheet.properties.style.TabStopsProperty;
import com.jaspersoft.ireport.designer.styles.ResetStyleAction;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.Action;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.RenameAction;
import org.openide.actions.ReorderAction;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * ParameterNode detects the events fired by the subtended parameter.
 * Implements the support for the property sheet of a parameter.
 * If a parameter is system defined, it can not be cut.
 * Actions of a parameter node include copy, paste, reorder, rename and delete.
 * 
 * @author gtoffoli
 */
public class AbstractStyleNode extends IRIndexedNode implements PropertyChangeListener {

    JasperDesign jd = null;
    private JRBaseStyle style = null;

    public JasperDesign getJasperDesign()
    {
        return jd;
    }

    public AbstractStyleNode(StyleChildren pc, JasperDesign jd, JRDesignStyle style, Lookup doLkp) {
        super(pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.fixed(jd,style)));

        this.jd = jd;
        this.style = style;

        init();

        

    }

    public AbstractStyleNode(JasperDesign jd, JRDesignStyle style, Lookup doLkp) {
        this(new StyleChildren(jd, style, doLkp), jd, style, doLkp);
    }

    public AbstractStyleNode(JasperDesign jd, JRDesignConditionalStyle style, Lookup doLkp) {
        super(new ConditionalStyleChildren(jd, style, doLkp), null, new ProxyLookup(doLkp, Lookups.fixed(jd,style)));
        this.jd = jd;
        this.style = style;

        init();
    }

    /*
    public AbstractStyleNode(JasperDesign jd, JRBaseStyle style, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup(doLkp, Lookups.fixed(jd, style)));
        this.jd = jd;
        this.style = style;
        
        init();
    }
    */
    
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
    public String getDisplayName() {
        return getStyle().getName();
    }
    
    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        
        Sheet.Set set = Sheet.createPropertiesSet();
        
        //set.put(new NameProperty( getStyle(), jd));
        //set.put(new DefaultStyleProperty( getStyle(), jd));
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
        

        set.put(new StylePatternProperty( getStyle() ));
        set.put(new BlankWhenNullProperty( getStyle() ));

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
        set.put(new MarkupProperty( getStyle() ));
        
        
                
        sheet.put(set);
        return sheet;
    }
    
    @Override
    public boolean canCut() {
        return true;
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
            SystemAction.get( CopyAction.class ),
            SystemAction.get( CutAction.class ),
            SystemAction.get( RenameAction.class ),
            SystemAction.get( ResetStyleAction.class ),
            SystemAction.get( ReorderAction.class ),
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

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
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
        if (ModelUtils.containsProperty(this.getPropertySets(),evt.getPropertyName()))
        {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
        }
        
    }

    public JRBaseStyle getStyle() {
        return style;
    }

    public void setStyle(JRBaseStyle style) {
        this.style = style;
    }


}
