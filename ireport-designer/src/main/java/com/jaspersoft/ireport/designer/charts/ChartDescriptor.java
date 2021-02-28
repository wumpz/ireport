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
package com.jaspersoft.ireport.designer.charts;

import net.sf.jasperreports.engine.JRChart;

/**
 *
 * @author Administrator
 */
public class ChartDescriptor {
    
    private javax.swing.ImageIcon icon;
    private String name = "";
    private String displayName = "";
    private byte chartType = JRChart.CHART_TYPE_PIE;

    public byte getChartType() {
        return chartType;
    }

    public void setChartType(byte chartType) {
        this.chartType = chartType;
    }
    
    /** Creates a new instance of ChartDescriptor */
    public ChartDescriptor(String iconName, String name, byte chartType) {
        
        setIcon(new javax.swing.ImageIcon( getClass().getResource(iconName) ));
        this.name = name;
        this.displayName = name;
        this.chartType = chartType;
    }

    public javax.swing.ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(javax.swing.ImageIcon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
