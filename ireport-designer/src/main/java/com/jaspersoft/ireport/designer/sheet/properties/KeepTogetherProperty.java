package com.jaspersoft.ireport.designer.sheet.properties;

import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.design.JRDesignGroup;

/**
 *
 * @author gtoffoli
 */


public class KeepTogetherProperty extends BooleanProperty
    {

        private final JRDesignGroup group;

        @SuppressWarnings("unchecked")
        public KeepTogetherProperty(JRDesignGroup group)
        {
            super(group);
            this.group = group;
        }

         @Override
        public String getName()
        {
            return JRDesignGroup.PROPERTY_KEEP_TOGETHER;
        }

        @Override
        public String getDisplayName()
        {
            return I18n.getString("Global.Property.KeepTogether");
        }

        @Override
        public String getShortDescription()
        {
            return I18n.getString("Global.Property.KeepTogether.description");
        }

        @Override
        public Boolean getBoolean() {
            return getOwnBoolean();
        }

        @Override
        public Boolean getOwnBoolean() {
            return group.isKeepTogether();
        }

        @Override
        public Boolean getDefaultBoolean() {
            return false;
        }

        @Override
        public void setBoolean(Boolean value) {
            if (value == null)
            {
                value = false;
            }
            group.setKeepTogether(value);
        }

    }
