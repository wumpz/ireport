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
package com.jaspersoft.ireport.designer.outline.nodes.properties.charts;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ReportClassLoader;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.StringListProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import net.sf.jasperreports.charts.ChartThemeBundle;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;

    
/**
 *  Class to manage the JRDesignChart.PROPERTY_EVALUATION_TIME property
 */
public final class ThemeProperty extends StringListProperty implements PreferenceChangeListener
{
        
    private List<Tag> tags = null;
    private final JRDesignChart chart;

    @SuppressWarnings("unchecked")
    public ThemeProperty(JRDesignChart chart)
    {
        super(chart);
        this.chart = chart;
        IReportManager.getPreferences().addPreferenceChangeListener(this);
    }

    @Override
    public String getName()
    {
        return JRChart.PROPERTY_CHART_THEME;
    }

    @Override
    public String getDisplayName()
    {
        return "Theme";
    }

    @Override
    public String getShortDescription()
    {
        return "The theme to use to render the chart.";
    }

    @Override
    public List getTagList()
    {
        if (tags == null)
        {
            updateTags();
        }

        return tags;
    }

    private void updateTags()
    {
        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(IReportManager.getJRExtensionsClassLoader());

        Set<String> themeNamesSet = new HashSet<String>();
        List themeBundles = ExtensionsEnvironment.getExtensionsRegistry().getExtensions(ChartThemeBundle.class);
        for (Iterator it = themeBundles.iterator(); it.hasNext();)
        {
           ChartThemeBundle bundle = (ChartThemeBundle) it.next();
           String[] themeNames = bundle.getChartThemeNames();
           themeNamesSet.addAll(Arrays.asList(themeNames));
        }

        String[] allThemeNames  = themeNamesSet.toArray(new String[themeNamesSet.size()]);
        Arrays.sort(allThemeNames);

        Thread.currentThread().setContextClassLoader(oldCL);

        tags = new ArrayList<Tag>(allThemeNames.length);
        //tags.add(new Tag(null, "Default"));
        for (int i=0; i<allThemeNames.length; ++i)
        {
            tags.add(new Tag(allThemeNames[i]));
        }
    }

    @Override
    public String getString()
    {
        return chart.getTheme();
    }

    @Override
    public String getOwnString()
    {
        return chart.getTheme();
    }

    @Override
    public String getDefaultString()
    {
        return null;
    }

    @Override
    public void setString(String renderType)
    {
        chart.setTheme(renderType);
    }


    public void preferenceChange(PreferenceChangeEvent evt) {

        if (evt == null || evt.getKey() == null || evt.getKey().equals( IReportManager.IREPORT_CLASSPATH))
        {
            // Refresh the array...
            updateTags();
            ((ComboBoxPropertyEditor)getPropertyEditor()).setTagValues(getTagList());
        }
    }
}
