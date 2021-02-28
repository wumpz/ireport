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
package com.jaspersoft.ireport.designer.outline.nodes;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.properties.ByteProperty;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabGroup;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.crosstabs.type.CrosstabColumnPositionEnum;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class CrosstabColumnGroupNode extends CrosstabGroupNode implements PropertyChangeListener {

    private JRDesignCrosstabColumnGroup group = null;

    public CrosstabColumnGroupNode(JasperDesign jd, JRDesignCrosstab crosstab, JRDesignCrosstabColumnGroup group, Lookup doLkp)
    {
        super (jd, crosstab, group, doLkp);
        this.group = group;
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/crosstabcolumns-16.png");
    }

    /**
     *  This is the function to create the sheet...
     * 
     */
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();

        Sheet.Set set = sheet.get(Sheet.PROPERTIES);
        set.put(new PositionProperty(group,getCrosstab()));
        sheet.put(set);
        
        return sheet;
    }
    
    
    @Override
    public List<JRDesignCrosstabGroup> getGroups() {
        List list = Arrays.asList(getCrosstab().getColumnGroups());
        return (List<JRDesignCrosstabGroup>)list;
    }

    @Override
    public int getType() {
        return COLUMN_GROUP;
    }


    /**
     *  Class to manage the JRDesignElement.PROPERTY_POSITION_TYPE property
     */
    public static final class PositionProperty extends ByteProperty
    {
        private final JRDesignCrosstabColumnGroup group;
        private final JRDesignCrosstab crosstab;

        @SuppressWarnings("unchecked")
        public PositionProperty(JRDesignCrosstabColumnGroup group, JRDesignCrosstab crosstab)
        {
            super(group);
            setName( JRDesignCrosstabColumnGroup.PROPERTY_POSITION );
            setDisplayName("Header Position");
            setShortDescription("This property set the position of the content of the group header cell");
            this.crosstab = crosstab;
            this.group = group;
        }

        @Override
        public List getTagList()
        {
            List tags = new java.util.ArrayList();
            tags.add(new Tag(CrosstabColumnPositionEnum.LEFT.getValueByte(), "Left"));
            tags.add(new Tag(CrosstabColumnPositionEnum.CENTER.getValueByte(), "Center"));
            tags.add(new Tag(CrosstabColumnPositionEnum.RIGHT.getValueByte(), "Right"));
            tags.add(new Tag(CrosstabColumnPositionEnum.STRETCH.getValueByte(), "Stretch"));
            return tags;
        }

        @Override
        public Byte getByte()
        {
            return (group.getPositionValue()) == null ? null : group.getPositionValue().getValueByte();
        }

        @Override
        public Byte getOwnByte()
        {
            return getByte();
        }

        @Override
        public Byte getDefaultByte()
        {
            return null;
        }

        @Override
        public void setByte(Byte positionType)
        {
            group.setPosition(positionType != null ? CrosstabColumnPositionEnum.getByValue(positionType) : null );
        }

    }
}
