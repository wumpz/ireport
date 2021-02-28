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

import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.designer.widgets.SelectionWidget;
import java.awt.Point;
import java.util.Collections;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class ReportObjectSelectProvider implements SelectProvider {

        private AbstractReportObjectScene scene = null;

        public ReportObjectSelectProvider(AbstractReportObjectScene scene)
        {
            this.scene = scene;
        }

        public boolean isAimingAllowed (Widget widget, Point localLocation, boolean invertSelection) {
            return false;
        }

        public boolean isSelectionAllowed (Widget widget, Point localLocation, boolean invertSelection) {
            if (widget instanceof SelectionWidget)
            {
                widget = ((SelectionWidget)widget).getRealWidget();
            }

            return scene.findObject (widget) != null;
        }

        public void select (Widget widget, Point localLocation, boolean invertSelection) {

            if (widget instanceof SelectionWidget)
            {
                widget = ((SelectionWidget)widget).getRealWidget();
            }

            Object object = scene.findObject(widget);

            scene.setFocusedObject (object);
            
            if (object != null) {

                if (!invertSelection  &&  scene.getSelectedObjects ().contains (object))
                   return;

                scene.userSelectionSuggested (Collections.singleton (object), invertSelection);
            } else
            {
                if (!invertSelection)
                {
                    scene.userSelectionSuggested (Collections.emptySet (), invertSelection);
                }
            }
        }
}
