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

package com.jaspersoft.ireport.designer.widgets;

import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @version $Id: ErrorWidget.java 0 2009-12-02 18:18:15 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ErrorWidget extends Widget {

    private Widget parentWidget = null;

    public ErrorWidget(Scene scene, Widget parentWidget)
    {
        super(scene);
        this.parentWidget = parentWidget;
        createDependency();
    }

    /**
     * @return the parentWidget
     */
    public Widget getReferringWidget() {
        return parentWidget;
    }

    /**
     * @param parentWidget the parentWidget to set
     */
    public void setReferringWidget(Widget parentWidget) {
        this.parentWidget = parentWidget;
    }

    private void createDependency()
    {
        parentWidget.addDependency(new Dependency() {

            public void revalidateDependency() {
                if (parentWidget.getParentWidget() == null)
                {
                    ErrorWidget.this.removeFromParent();
                }
                else
                {
                    ErrorWidget.this.setPreferredLocation(parentWidget.getLocation());
                    ErrorWidget.this.setPreferredBounds( parentWidget.getBounds() );
                }
            }
        });


    }


}
