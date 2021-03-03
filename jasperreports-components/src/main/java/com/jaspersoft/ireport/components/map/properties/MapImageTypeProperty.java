/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.components.map.properties;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.EnumProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.util.List;
import net.sf.jasperreports.components.map.StandardMapComponent;
import net.sf.jasperreports.components.map.type.MapImageTypeEnum;

/**
 *
 * @author gtoffoli
 */
public class MapImageTypeProperty extends EnumProperty {

    StandardMapComponent mapComponent = null;
    
    public MapImageTypeProperty(StandardMapComponent mapComponent)
    {
        super(MapImageTypeEnum.class, mapComponent);
        this.mapComponent = mapComponent;
        
        this.setName(  StandardMapComponent.PROPERTY_MAP_TYPE );
        this.setDisplayName(I18n.getString("MapImageTypeEnum.name"));
        this.setShortDescription(I18n.getString("MapImageTypeEnum.description"));
    }
    
    @Override
    public List getTagList() 
    {
        List tags = new java.util.ArrayList();
        tags.add(new Tag(MapImageTypeEnum.PNG, I18n.getString("MapImageTypeProperty.enum.png")));
        tags.add(new Tag(MapImageTypeEnum.PNG_8, I18n.getString("MapImageTypeProperty.enum.png8")));
        tags.add(new Tag(MapImageTypeEnum.PNG_32, I18n.getString("MapImageTypeProperty.enum.png32")));
        tags.add(new Tag(MapImageTypeEnum.GIF, I18n.getString("MapImageTypeProperty.enum.gif")));
        tags.add(new Tag(MapImageTypeEnum.JPG, I18n.getString("MapImageTypeProperty.enum.jpg")));
        tags.add(new Tag(MapImageTypeEnum.JPG_BASELINE, I18n.getString("MapImageTypeProperty.enum.jpg-baseline")));
        return tags;
    }

    @Override
    public Object getPropertyValue() {
        return mapComponent.getImageType() == null ? getDefaultValue() : mapComponent.getImageType();
    }

    @Override
    public Object getOwnPropertyValue() {
        return getPropertyValue();
    }

    @Override
    public Object getDefaultValue() {
        return MapImageTypeEnum.PNG;
    }

    @Override
    public void setPropertyValue(Object value) {
        
        mapComponent.setImageType( (MapImageTypeEnum)value  );
    }
    
}
