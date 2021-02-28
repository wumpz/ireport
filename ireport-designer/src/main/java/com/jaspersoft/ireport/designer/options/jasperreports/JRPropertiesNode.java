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
package com.jaspersoft.ireport.designer.options.jasperreports;

import com.jaspersoft.ireport.designer.IRLocalJasperReportsContext;
import java.util.List;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

/**
 *
 * @author gtoffoli
 */
public class JRPropertiesNode extends AbstractNode {

    public JRPropertiesNode()
    {
        super(Children.LEAF);
    }

     @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();

        Sheet.Set set = createSet("JasperReports properties",null);

        List props =  IRLocalJasperReportsContext.getUtilities().getProperties("");
        
        for (int i=0; i<props.size(); ++i)
        {
            PropertySuffix prop = (PropertySuffix)props.get(i);
            set.put(new StringProperty(prop.getKey()));
        }

        sheet.put(set);
        return sheet;
    }


     public Sheet.Set createSet(String name, String displayName)
     {
         Sheet.Set set = Sheet.createPropertiesSet();
         set.setName(name);
         if (displayName == null)
         {
             set.setDisplayName(name);
         }
         else
         {
             set.setDisplayName(displayName);
         }

         return set;
     }

}
