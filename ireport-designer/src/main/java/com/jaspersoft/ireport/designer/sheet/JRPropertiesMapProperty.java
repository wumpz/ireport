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

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.options.jasperreports.JRPropertyDialog;
import com.jaspersoft.ireport.designer.sheet.editors.JRPropertiesMapPropertyEditor;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author gtoffoli
 */
public class JRPropertiesMapProperty  extends PropertySupport {

    PropertyEditor editor = null;
    JRPropertiesHolder propertiesHolder = null;
    
    @SuppressWarnings("unchecked")
    public JRPropertiesMapProperty(JRPropertiesHolder holder)
    {
       super( "properties", JRPropertiesMap.class, I18n.getString("JRPropertiesMapProperty.Property.Properties"),I18n.getString("JRPropertiesMapProperty.Property.Propertiesdetail"), true,true);
       setValue("canEditAsText", Boolean.FALSE);
       this.propertiesHolder = holder;
       if (holder instanceof JasperDesign)
       {
           setValue("reportProperties", Boolean.TRUE);
           setValue("hintType", com.jaspersoft.ireport.designer.sheet.editors.JRPropertyDialog.SCOPE_REPORT);
       }
       else if(holder instanceof JRTextField)
       {
           setValue("hintType", com.jaspersoft.ireport.designer.sheet.editors.JRPropertyDialog.SCOPE_TEXT_ELEMENT);
       }
       else if(holder instanceof JRElement)
       {
           setValue("hintType", com.jaspersoft.ireport.designer.sheet.editors.JRPropertyDialog.SCOPE_ELEMENT);
       }
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return propertiesHolder.getPropertiesMap().cloneProperties();
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (!(val instanceof JRPropertiesMap)) throw new IllegalArgumentException();
        
        // Fill this map with the content of the map we got here...
        ModelUtils.replacePropertiesMap((JRPropertiesMap)val, propertiesHolder.getPropertiesMap());
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
    }
    
    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new JRPropertiesMapPropertyEditor();
        }
        return editor;
    }
    
    
}

