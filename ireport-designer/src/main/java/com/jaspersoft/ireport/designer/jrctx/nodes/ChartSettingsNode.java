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
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.AntiAliasProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.ChartBackgroundImageAlignmentProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.ChartBorderVisibleProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.ChartBackgroundPaintProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.ChartBorderProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.ChartPaddingProperty;
import com.jaspersoft.ireport.designer.jrctx.nodes.properties.TextAntiAliasProperty;
import com.jaspersoft.ireport.designer.outline.nodes.IRAbstractNode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.chartthemes.simple.ChartSettings;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;


/**
 *
 * @author gtoffoli
 */
public class ChartSettingsNode  extends IRAbstractNode implements PropertyChangeListener {

    private static final String IMAGE_ICON_BASE = "com/jaspersoft/ireport/designer/resources/chartsettings.png";

    private ChartSettings chartSettings = null;

    public ChartSettingsNode(ChartSettings chartSettings, Lookup doLkp)
    {
        super(Children.LEAF, doLkp); //
        this.chartSettings = chartSettings;
        setName("Chart Settings");
        setIconBaseWithExtension(IMAGE_ICON_BASE);
        chartSettings.getEventSupport().addPropertyChangeListener(this);
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
        
        set.put(new ChartBackgroundPaintProperty(getChartSettings()));
//	public static final String PROPERTY_backgroundImage = "backgroundImage";
        set.put(new ChartBackgroundImageAlignmentProperty(getChartSettings()));
//	public static final String PROPERTY_backgroundImageAlpha = "backgroundImageAlpha";
//	public static final String PROPERTY_font = "font";
        set.put(new ChartBorderVisibleProperty(getChartSettings()));
        set.put(new ChartBorderProperty(getChartSettings()));
        set.put(new AntiAliasProperty(getChartSettings()));
        set.put(new TextAntiAliasProperty(getChartSettings()));
        set.put(new ChartPaddingProperty(getChartSettings()));

        sheet.put(set);
        return sheet;
    }

    /**
     * @return the chartSettings
     */
    public ChartSettings getChartSettings() {
        return chartSettings;
    }
}