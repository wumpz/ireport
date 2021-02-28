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
package com.jaspersoft.ireport.designer.jrctx.nodes;

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.jrctx.JRCTXEditorSupport;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.ShowTitleProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.TitleBackgroundPaintProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.TitleForegroundPaintProperty;
import com.jaspersoft.ireport.designer.outline.nodes.IRAbstractNode;
import com.jaspersoft.ireport.designer.sheet.properties.BoldProperty;
import com.jaspersoft.ireport.designer.sheet.properties.FontNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.FontSizeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ItalicProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PdfEmbeddedProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PdfEncodingProperty;
import com.jaspersoft.ireport.designer.sheet.properties.PdfFontNameProperty;
import com.jaspersoft.ireport.designer.sheet.properties.StrikeThroughProperty;
import com.jaspersoft.ireport.designer.sheet.properties.UnderlineProperty;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.chartthemes.simple.TitleSettings;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;


/**
 *
 * @author gtoffoli
 */
public class TitleSettingsNode  extends IRAbstractNode implements PropertyChangeListener {

    private static final String IMAGE_ICON_BASE = "com/jaspersoft/ireport/designer/resources/chartsettings.png";

    private TitleSettings titleSettings = null;

    public TitleSettingsNode(TitleSettings titleSettings, Lookup doLkp)
    {
        super(Children.LEAF, doLkp); //
        this.titleSettings = titleSettings;
        setName("Title Settings");
        setIconBaseWithExtension(IMAGE_ICON_BASE);
        titleSettings.getEventSupport().addPropertyChangeListener(this);
        ((JRBaseFont)titleSettings.getFont()).getEventSupport().addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {

        // Model changed...
        JRCTXEditorSupport ed = getLookup().lookup(JRCTXEditorSupport.class);
        ed.notifyModelChangeToTheView();

        if (evt.getPropertyName() == null) return;

        if (ModelUtils.containsProperty(this.getPropertySets(),evt.getPropertyName()))
        {
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
        }
    }


    /**
     *  This is the function to create the sheet...
     *
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();

        Sheet.Set set = Sheet.createPropertiesSet();
        
        set.put(new ShowTitleProperty(getTitleSettings()));
//	public static final String PROPERTY_position = "position";
        set.put(new TitleForegroundPaintProperty(getTitleSettings()));
        set.put(new TitleBackgroundPaintProperty(getTitleSettings()));
        JRFont font = getTitleSettings().getFont();
        set.put(new FontNameProperty(font));
        set.put(new FontSizeProperty(font));
        set.put(new BoldProperty(font));
        set.put(new ItalicProperty(font));
        set.put(new UnderlineProperty(font));
        set.put(new StrikeThroughProperty(font));
        set.put(new PdfFontNameProperty(font));
        set.put(new PdfEmbeddedProperty(font));
        set.put(new PdfEncodingProperty(font));
//	public static final String PROPERTY_horizontalAlignment = "horizontalAlignment";
//	public static final String PROPERTY_verticalAlignment = "verticalAlignment";
//	public static final String PROPERTY_padding = "padding";


        sheet.put(set);
        return sheet;
    }

    /**
     * @return the titleSettings
     */
    public TitleSettings getTitleSettings() {
        return titleSettings;
    }
}