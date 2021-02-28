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
package com.jaspersoft.ireport.designer.sheet.properties.style;

import com.jaspersoft.ireport.designer.sheet.editors.JRTabStopsPropertyEditor;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import org.openide.nodes.PropertySupport;

/**
 * This class should be able to handle both common properties
 * and expression based properties.
 * 
 * @author gtoffoli
 */
public class TabStopsProperty  extends PropertySupport {

    PropertyEditor editor = null;
    private JRBaseStyle style = null;
    
    @SuppressWarnings("unchecked")
    public TabStopsProperty(JRBaseStyle style)
    {
       super( JRBaseParagraph.PROPERTY_TAB_STOPS, List.class, I18n.getString("Global.Property.TabStops"),I18n.getString("Global.Property.TabStops.desc"), true,true);
       setValue("canEditAsText", Boolean.FALSE);
       setValue("useList", Boolean.TRUE);
       this.style = style;
       
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        
        TabStop[] tabStops = style.getParagraph().getTabStops();
        List<TabStop> tabStopsList = new ArrayList<TabStop>();

        if (tabStops != null)
        {
            tabStopsList.addAll( Arrays.asList(tabStops) );
        }

        return tabStopsList;
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //if (!(val instanceof JRPropertiesMap)) throw new IllegalArgumentException();
        
        if (!(val instanceof List)) throw new IllegalArgumentException();
        
        // Fill this map with the content of the map we got here...
        
        // 1. Create the map...
        List values = (List)val;

        // Remove all the tab stops...
        if (style.getParagraph().getTabStops() != null)
        {
            for (int k=style.getParagraph().getTabStops().length-1; k>= 0; --k)
            {
                style.getParagraph().removeTabStop(0);
            }
        }

        for (int i=0; i <values.size(); ++i)
        {
            TabStop prop = (TabStop)values.get(i);
            style.getParagraph().addTabStop(prop);
        }
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
    }
    
    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new JRTabStopsPropertyEditor();
        }
        return editor;
    }
    
    
}





