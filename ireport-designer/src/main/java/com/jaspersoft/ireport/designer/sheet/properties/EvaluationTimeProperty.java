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
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

    
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public final class EvaluationTimeProperty extends EnumProperty
{
    private final JRDesignImage image;

    @SuppressWarnings("unchecked")
    public EvaluationTimeProperty(JRDesignImage image)
    {
        super(EvaluationTimeEnum.class, image);
        this.image = image;
    }

    @Override
    public String getName()
    {
        return JRDesignImage.PROPERTY_EVALUATION_TIME;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("Global.Property.EvaluationTime");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("Global.Property.ETdetail");
    }

    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(EvaluationTimeEnum.NOW, I18n.getString("Global.Property.Now")));
        tags.add(new Tag(EvaluationTimeEnum.REPORT, I18n.getString("Global.Property.Report")));
        tags.add(new Tag(EvaluationTimeEnum.PAGE, I18n.getString("Global.Property.Page")));
        tags.add(new Tag(EvaluationTimeEnum.COLUMN, I18n.getString("Global.Property.Column")));
        tags.add(new Tag(EvaluationTimeEnum.GROUP, I18n.getString("Global.Property.Group")));
        tags.add(new Tag(EvaluationTimeEnum.BAND, I18n.getString("Global.Property.Band")));
        tags.add(new Tag(EvaluationTimeEnum.AUTO, I18n.getString("Global.Property.Auto")));
        return tags;
    }

    @Override
    public Object getPropertyValue()
    {
        return image.getEvaluationTimeValue();
    }

    @Override
    public Object getOwnPropertyValue()
    {
        return getPropertyValue();
    }

    @Override
    public Object getDefaultValue()
    {
        return EvaluationTimeEnum.NOW;
    }

    @Override
    public void setPropertyValue(Object evaluationTime)
    {
        image.setEvaluationTime((EvaluationTimeEnum)evaluationTime);
    }

}
