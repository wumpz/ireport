package com.jaspersoft.ireport.designer.sheet.properties;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.type.FooterPositionEnum;

/**
 *
 * @author gtoffoli
 */


public class FooterPositionProperty extends AbstractProperty
    {
        private final JRDesignGroup group;
        private ComboBoxPropertyEditor editor;

        @SuppressWarnings("unchecked")
        public FooterPositionProperty(JRDesignGroup group)
        {
            super(FooterPositionEnum.class, group);
            this.group = group;
            setValue("suppressCustomEditor", Boolean.TRUE);
        }

        @Override
        @SuppressWarnings("unchecked")
        public PropertyEditor getPropertyEditor()
        {
            if (editor == null)
            {
                editor = new ComboBoxPropertyEditor(false, getTagList());
            }
            return editor;
        }


        @Override
        public String getName()
        {
            return JRDesignGroup.PROPERTY_FOOTER_POSITION;
        }

        @Override
        public String getDisplayName()
        {
            return I18n.getString("Global.Property.FooterPosition");
        }

        @Override
        public String getShortDescription()
        {
            return I18n.getString("Global.Property.FooterPosition.description");
        }

    
        public List getTagList()
        {
            List tags = new java.util.ArrayList();
            tags.add(new Tag(FooterPositionEnum.NORMAL, I18n.getString("Global.Property.FooterPosition.normal")));
            tags.add(new Tag(FooterPositionEnum.STACK_AT_BOTTOM, I18n.getString("Global.Property.FooterPosition.stackAtBottom")));
            tags.add(new Tag(FooterPositionEnum.FORCE_AT_BOTTOM, I18n.getString("Global.Property.FooterPosition.forceAtBottom")));
            tags.add(new Tag(FooterPositionEnum.COLLATE_AT_BOTTOM, I18n.getString("Global.Property.FooterPosition.collateAtBottom")));
            
            return tags;
        }


        @Override
        public Object getPropertyValue() {
            return group.getFooterPositionValue();
        }

        @Override
        public Object getOwnPropertyValue() {
            return getPropertyValue();
        }

        @Override
        public Object getDefaultValue() {
            return FooterPositionEnum.NORMAL;
        }

        @Override
        public void validate(Object value) {
        }

        @Override
        public void setPropertyValue(Object value) {

            group.setFooterPosition((FooterPositionEnum)value);

        }

    }


    