/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.spiderchart.properties;

import com.jaspersoft.ireport.designer.sheet.properties.StringProperty;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;


/**
 *
 * @version $Id: CustomizerClassProperty.java 0 2010-09-21 13:10:08 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class CustomizerClassProperty extends StringProperty {

    private final StandardChartSettings settings;

    
    public CustomizerClassProperty(StandardChartSettings settings)
    {
        super(settings);
        this.settings = settings;
    }

    @Override
    public String getName()
    {
        return StandardChartSettings.PROPERTY_CUSTOMIZER_CLASS;
    }

    @Override
    public String getDisplayName()
    {
        return I18n.getString("components.customizerClass");
    }

    @Override
    public String getShortDescription()
    {
        return I18n.getString("components.customizerClass.desc");
    }

    @Override
    public String getString() {
        return settings.getCustomizerClass();
    }

    @Override
    public String getOwnString() {
        return getString();
    }

    @Override
    public String getDefaultString() {
        return null;
    }

    @Override
    public void setString(String value) {

        settings.setCustomizerClass(value);

    }

}
