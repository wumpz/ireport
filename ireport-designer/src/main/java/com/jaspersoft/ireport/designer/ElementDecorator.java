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
package com.jaspersoft.ireport.designer;

import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author gtoffoli
 */
public interface ElementDecorator {
    
    /**
     * Allow to paint extra stuff over the element.
     * Used only for elements.
     * @param w
     */
    public void paintWidget(Widget w);
    
    /**
     * Allow to specify extra menu items in the context of
     * the element node...
     * @return
     */
    public SystemAction[] getActions(Node node);

    /**
     * Return true if this decorator can be used with the given
     * design element...
     * @return
     */
    public boolean appliesTo(Object designElement);
}
