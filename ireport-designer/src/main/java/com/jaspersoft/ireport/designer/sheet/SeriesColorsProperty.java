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
package com.jaspersoft.ireport.designer.sheet;

import com.jaspersoft.ireport.designer.sheet.editors.SeriesColorsPropertyEditor;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import net.sf.jasperreports.engine.base.JRBaseChartPlot.JRBaseSeriesColor;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author gtoffoli
 */
public class SeriesColorsProperty extends PropertySupport {

    PropertyEditor editor = null;
    
    @SuppressWarnings("unchecked")
    public SeriesColorsProperty(String name,
                       String displayName,
                       String shortDescription)
    {
       super( name, SortedSet.class, displayName,shortDescription, true,true);
       setValue( "canEditAsText", Boolean.FALSE );
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return "";
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new SeriesColorsPropertyEditor();
        }
        return editor;
    }

    public static List sortedSetAsList(SortedSet set)
    {
         List list = new ArrayList();
        list.addAll( set );
        return list;
    }

    public static SortedSet listAsSortedSet(List list)
    {
         SortedSet set = new TreeSet();
         set.addAll( list );
         return set;
    }

    public static boolean isListChanged(List values, SortedSet originalValues)
    {
            if (values == null && originalValues == null)
            {
                return false;
            }

            if (values != null && originalValues != null)
            {
                // Check if the colors are the same...
                List l1 = values;
                List l2 = sortedSetAsList(originalValues);

                boolean areEq = true;
                if (l1.size() != l2.size())
                {
                    areEq = false;
                }
                if (areEq)
                {
                    for (int i=0; areEq && i<l1.size(); ++i)
                    {
                        if (l1.get(i) instanceof JRBaseSeriesColor && l2.get(i) instanceof JRBaseSeriesColor)
                        {
                            JRBaseSeriesColor c1 = (JRBaseSeriesColor)l1.get(i);
                            JRBaseSeriesColor c2 = (JRBaseSeriesColor)l2.get(i);

                            if (c1 == null || c2 == null)
                            {
                                if (c1 != c2)
                                {
                                    areEq = false;
                                }
                            }
                            else
                            {
                                if (c1.getSeriesOrder() != c2.getSeriesOrder())
                                {
                                    areEq = false;
                                }
                                else if (c1.getColor() != c2.getColor() || (c1.getColor() != null && !c1.getColor().equals(c2.getColor()))  )
                                {
                                    areEq = false;
                                }
                            }


                        }
                        else if (!(l1.get(i).equals(l2.get(i))))
                        {
                            areEq = false;
                        }
                    }
                }

                return !areEq;
            }
            return true;
    }
    
}
