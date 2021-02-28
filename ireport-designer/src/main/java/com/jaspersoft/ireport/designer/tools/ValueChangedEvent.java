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
package com.jaspersoft.ireport.designer.tools;

import javax.swing.*;
/**
 *
 * @author  Administrator
 */
public class ValueChangedEvent {
    
    private JComponent source;    
    
    private double oldValue;    
    private double newValue;  
    /** Creates a new instance of ValueChangedEvent */
    public ValueChangedEvent(JComponent source, double oldValue, double newValue) {
        
        this.source = source;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    /** Getter for property source.
     * @return Value of property source.
     *
     */
    public JComponent getSource() {
        return source;
    }
    
    /** Setter for property source.
     * @param source New value of property source.
     *
     */
    public void setSource(JComponent source) {
        this.source = source;
    }
    
    /** Getter for property newValue.
     * @return Value of property newValue.
     *
     */
    public double getNewValue() {
        return newValue;
    }
    
    /** Setter for property newValue.
     * @param newValue New value of property newValue.
     *
     */
    public void setNewValue(double newValue) {
        this.newValue = newValue;
    }
    
    /** Getter for property oldValue.
     * @return Value of property oldValue.
     *
     */
    public double getOldValue() {
        return oldValue;
    }
    
    /** Setter for property oldValue.
     * @param oldValue New value of property oldValue.
     *
     */
    public void setOldValue(double oldValue) {
        this.oldValue = oldValue;
    }
    
}
