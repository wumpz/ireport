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

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.StringListProperty;
import com.jaspersoft.ireport.designer.utils.ExpressionFileResolver;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlTemplateLoader;
import org.openide.util.WeakListeners;

    
/**
 *  Class to manage the JRDesignElement.PROPERTY_PARENT_STYLE_NAME_REFERENCE property
 */
public abstract class AbstractStyleProperty extends StringListProperty implements PropertyChangeListener
{
    private final JasperDesign jasperDesign;

    @SuppressWarnings("unchecked")
    public AbstractStyleProperty(Object object, JasperDesign jasperDesign)
    {
        super(object);
        this.jasperDesign = jasperDesign;

        jasperDesign.getEventSupport().addPropertyChangeListener(
            WeakListeners.propertyChange(this, jasperDesign.getEventSupport())
            );
    }
    @Override
    public String getName()
    {
        return JRDesignElement.PROPERTY_PARENT_STYLE_NAME_REFERENCE;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.Style");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.Styledetail");
    }

    @Override
    public List getTagList() 
    {
        List tags = new ArrayList();
        tags.add(new Tag( null , ""));
        List styles = jasperDesign.getStylesList();
        for (int i=0; i<styles.size(); ++i)
        {
            JRDesignStyle style = (JRDesignStyle)styles.get(i);
            tags.add(new Tag( style , style.getName()));
            style.getEventSupport().addPropertyChangeListener(
                WeakListeners.propertyChange(this, style.getEventSupport())
                );
        }

        // Add all the references too...
        JRReportTemplate[] templates = jasperDesign.getTemplates();
        for (int i=0; i<templates.length; ++i)
        {
            tags.addAll(getStyleInTemplate(templates[i]));
        }

        return tags;
    }

    List getStyleInTemplate(JRReportTemplate template)
    {
        List styleNames = new ArrayList();
        if (template != null)
        {
            String fileNameExp = Misc.getExpressionText(template.getSourceExpression());
            if (fileNameExp != null && fileNameExp.length() > 0)
            {
                ExpressionFileResolver resolver = new ExpressionFileResolver(
                        (JRDesignExpression)template.getSourceExpression(),jasperDesign.getMainDesignDataset(), jasperDesign);

                File f = resolver.resolveFile(null);
                if (f!=null && f.exists())
                {
                    try {
                        // try to load this jrtx template...
                        JRSimpleTemplate template2 = (JRSimpleTemplate) JRXmlTemplateLoader.load(new FileInputStream(f));

                        JRStyle[] styles = template2.getStyles();
                        for (int i=0; i<styles.length; ++i)
                        {
                            styleNames.add(new Tag(styles[i].getName(),I18n.getString("property.styleNameReference",  styles[i].getName())));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return styleNames;
    }

    @Override
    public Object getPropertyValue()
    {
        if (getStyle() == null)
            return getStyleNameReference();
        return getStyle();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getPropertyValue();
    }

    @Override
    public void setPropertyValue(Object value)
    {
        if (value instanceof JRStyle)
        {
            setStyle((JRStyle)value);
            setStyleNameReference(null);
        }
        else
        {
            String styleNameReference = (String)value;
            if (styleNameReference != null && styleNameReference.trim().length() == 0)
            {
                styleNameReference = null;
            }
            setStyleNameReference(styleNameReference);
            setStyle(null);
        }
    }

    @Override
    public String getString()
    {
        //not used
        return null;
    }

    @Override
    public String getOwnString()
    {
        //not used
        return null;
    }

    @Override
    public String getDefaultString()
    {
        return null;
    }

    @Override
    public void setString(String str)
    {
        //not used
    }

    public void propertyChange(PropertyChangeEvent evt) 
    {
        if (editor == null) return;
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JasperDesign.PROPERTY_STYLES) 
            || evt.getPropertyName().equals( JRDesignStyle.PROPERTY_NAME) ||
            evt.getPropertyName().equals( JasperDesign.PROPERTY_TEMPLATES))
        {
            editor.setTagValues(getTagList());
        }
    }
    
    public abstract String getStyleNameReference();

    public abstract void setStyleNameReference(String s);
    
    public abstract JRStyle getStyle();
    
    public abstract void setStyle(JRStyle s);

}
