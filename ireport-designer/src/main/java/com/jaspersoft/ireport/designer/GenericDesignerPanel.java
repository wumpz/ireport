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

package com.jaspersoft.ireport.designer;

import javax.swing.Icon;
import javax.swing.JComponent;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: GenericDesignerPanel.java 0 2010-03-12 14:38:24 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public interface GenericDesignerPanel {

    public void setJasperDesign(JasperDesign jd);

    /**
     * If this design panel uses a Scene, this method is used to synchronize objects selection.
     * The method should return null otherwise.
     * 
     * @return
     */
    public AbstractReportObjectScene getScene();

    /**
     * In this design panel refers to a specific element container, this method is used
     * to get it. The method should return null otherwise.
     * @return
     */
    public JRDesignElement getElement();


    /**
     * The component of this designer.
     * @return
     */
    public JComponent getComponent();


    /**
     * Get label used in the tab button (i.e.  "My element {0}")
     * {0} is replaced with the element index
     * {1} is replaced with the element key
     * @return
     */
    public String getLabel();

    /**
     * The icon used in the tab button
     */
    public Icon getIcon();

    /**
     * Returns the elements dataset of the specified element.
     * If the element is not an element of the scene, returns null;
     * 
     * @param element
     * @return
     */
    public JRDesignDataset getElementDataset(JRDesignElement element);


}
