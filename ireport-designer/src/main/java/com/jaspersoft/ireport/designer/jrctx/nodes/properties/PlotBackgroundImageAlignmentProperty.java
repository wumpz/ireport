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
package com.jaspersoft.ireport.designer.jrctx.nodes.properties;

import com.jaspersoft.ireport.designer.sheet.properties.*;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.util.List;
import net.sf.jasperreports.chartthemes.simple.PlotSettings;
import org.jfree.ui.Align;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
 */
public final class PlotBackgroundImageAlignmentProperty extends IntegerProperty
{
    private final PlotSettings settings;
    private ComboBoxPropertyEditor editor;

    @SuppressWarnings("unchecked")
    public PlotBackgroundImageAlignmentProperty(PlotSettings settings)
    {
        super(settings);
        this.settings = settings;
    }

    @Override
    public String getName()
    {
        return PlotSettings.PROPERTY_backgroundImageAlignment;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property." + getName());
    }

    @Override
    public String getShortDescription()
    {
        return getDisplayName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public PropertyEditor getPropertyEditor()
    {
        if (editor == null)
        {
            editor = new ComboBoxPropertyEditor(false, getTagList());
        }
        return editor;
    }

    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(new Integer(Align.BOTTOM), I18n.getString("Global.Property.Bottom")));
        tags.add(new Tag(new Integer(Align.BOTTOM_LEFT), I18n.getString("Global.Property.BottomLeft")));
        tags.add(new Tag(new Integer(Align.BOTTOM_RIGHT), I18n.getString("Global.Property.BottomRight")));
        tags.add(new Tag(new Integer(Align.CENTER), I18n.getString("Global.Property.Center")));
        tags.add(new Tag(new Integer(Align.FIT), I18n.getString("Global.Property.Fit")));
        tags.add(new Tag(new Integer(Align.FIT_HORIZONTAL), I18n.getString("Global.Property.FitHorizontal")));
        tags.add(new Tag(new Integer(Align.FIT_VERTICAL), I18n.getString("Global.Property.FitVertical")));
        tags.add(new Tag(new Integer(Align.LEFT), I18n.getString("Global.Property.Left")));
        tags.add(new Tag(new Integer(Align.RIGHT), I18n.getString("Global.Property.Right")));
        tags.add(new Tag(new Integer(Align.TOP), I18n.getString("Global.Property.Top")));
        tags.add(new Tag(new Integer(Align.TOP_LEFT), I18n.getString("Global.Property.TopLeft")));
        tags.add(new Tag(new Integer(Align.TOP_RIGHT), I18n.getString("Global.Property.TopRight")));
        return tags;
    }

    @Override
    public Integer getInteger()
    {
        return settings.getBackgroundImageAlignment();
    }

    @Override
    public Integer getOwnInteger()
    {
        return settings.getBackgroundImageAlignment();
    }

    @Override
    public Integer getDefaultInteger()
    {
        return null;
    }

    @Override
    public void setInteger(Integer value)
    {
        settings.setBackgroundImageAlignment(value);
    }


    @Override
    public void validateInteger(Integer value)
    {
    }

}
