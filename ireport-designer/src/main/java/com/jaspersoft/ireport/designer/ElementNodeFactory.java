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

import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public interface ElementNodeFactory {

    /**
     * Creates a special node implementation that extends ElementNode
     * to provide custom properties, actions, icon, and so on.
     * If return null, the default ElementNode is used instead.
     * @param jd
     * @param element
     * @param lkp
     * @return
     */
    public ElementNode createElementNode(JasperDesign jd, JRDesignComponentElement element, Lookup lkp);

    /**
     * Creates a custom element Widget to control the element rendering.
     * If null, the default JRDesignElementWidget is used.
     * @param scene
     * @param element
     * @return
     */
    public JRDesignElementWidget createElementWidget(AbstractReportObjectScene scene, JRDesignElement element);
}
