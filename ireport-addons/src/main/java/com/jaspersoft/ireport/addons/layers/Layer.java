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

package com.jaspersoft.ireport.addons.layers;

import java.beans.PropertyChangeSupport;

/**
 *
 * @version $Id: Layer.java 0 2010-02-25 15:31:45 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class Layer {

    public static final String PROPERTY_NAME = "PROPERTY_NAME";
    public static final String PROPERTY_VISIBLE = "PROPERTY_VISIBLE";
    public static final String PROPERTY_PRINT_WHEN_EXPRESSION = "PROPERTY_PRINT_WHEN_EXPRESSION";

    private PropertyChangeSupport eventSupport = null;

    private int id = 0;
    private String name = "Layer";
    private boolean visible = true;
    private String printWhenExpression = null;

    private boolean backgroundLayer = false;

    public Layer()
    {
        eventSupport = new PropertyChangeSupport(this);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        getEventSupport().firePropertyChange(PROPERTY_NAME, oldName, this.name );
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        
        boolean oldVisible = this.visible;
        this.visible = visible;
        getEventSupport().firePropertyChange(PROPERTY_VISIBLE, oldVisible, this.visible );
    }

    /**
     * @return the backgroundLayer
     */
    public boolean isBackgroundLayer() {
        return backgroundLayer;
    }

    /**
     * @param backgroundLayer the backgroundLayer to set
     */
    public void setBackgroundLayer(boolean backgroundLayer) {
        this.backgroundLayer = backgroundLayer;
    }

    /**
     * @return the eventSupport
     */
    public PropertyChangeSupport getEventSupport() {
        return eventSupport;
    }

    /**
     * @return the printWhenExpression
     */
    public String getPrintWhenExpression() {
        return printWhenExpression;
    }

    /**
     * @param printWhenExpression the printWhenExpression to set
     */
    public void setPrintWhenExpression(String newPrintWhenExpression) {
        String oldPrintWhenExpression = this.printWhenExpression;
        this.printWhenExpression = newPrintWhenExpression;
        getEventSupport().firePropertyChange(PROPERTY_PRINT_WHEN_EXPRESSION, oldPrintWhenExpression, this.printWhenExpression );
    }


}
