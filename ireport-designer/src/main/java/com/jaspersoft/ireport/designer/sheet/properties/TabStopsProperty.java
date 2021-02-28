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
package com.jaspersoft.ireport.designer.sheet.properties;

import com.jaspersoft.ireport.designer.sheet.editors.JRTabStopsPropertyEditor;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import org.openide.nodes.PropertySupport;

/**
 * This class should be able to handle both common properties
 * and expression based properties.
 * 
 * @author gtoffoli
 */
public class TabStopsProperty  extends PropertySupport {

    PropertyEditor editor = null;
    JRDesignTextElement element = null;
    
    @SuppressWarnings("unchecked")
    public TabStopsProperty(JRDesignTextElement element)
    {
       super( JRBaseParagraph.PROPERTY_TAB_STOPS, List.class, I18n.getString("Global.Property.TabStops"),I18n.getString("Global.Property.TabStops.desc"), true,true);
       setValue("canEditAsText", Boolean.FALSE);
       setValue("useList", Boolean.TRUE);
       this.element = element;
       
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        
        TabStop[] tabStops = element.getParagraph().getTabStops();
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
        if (element.getParagraph().getTabStops() != null)
        {
            for (int k=element.getParagraph().getTabStops().length-1; k>= 0; --k)
            {
                element.getParagraph().removeTabStop(0);
            }
        }

        for (int i=0; i <values.size(); ++i)
        {
            TabStop prop = (TabStop)values.get(i);
            element.getParagraph().addTabStop(prop);
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

//    @Override
//    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
//        if (element.getParagraph().getTabStops() != null)
//        {
//            for (int k=element.getParagraph().getTabStops().length-1; k>= 0; --k)
//            {
//                element.getParagraph().removeTabStop(0);
//            }
//        }
//    }

    @Override
    public boolean supportsDefaultValue() {
        return false;
    }


    
}





