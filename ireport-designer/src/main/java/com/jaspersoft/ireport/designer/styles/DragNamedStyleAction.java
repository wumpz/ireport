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
package com.jaspersoft.ireport.designer.styles;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.palette.PaletteItemAction;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import java.util.List;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class DragNamedStyleAction extends PaletteItemAction {

    private static JRDesignElement findElementAt(AbstractReportObjectScene theScene, Point p) {

         LayerWidget layer = theScene.getElementsLayer();
         List<Widget> widgets = layer.getChildren();

         // Use revers order to find the top most...
         for (int i= widgets.size()-1; i>=0; --i)
         {
             Widget w = widgets.get(i);
             Point p2 = w.convertSceneToLocal(p);
             if (w.isHitAt(p2))
             {
                 if (w instanceof JRDesignElementWidget)
                 {
                     JRDesignElement de =((JRDesignElementWidget)w).getElement();
                     return de;
                 }
             }
         }
         return null;
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {

        // Let's see what to do with this style....

        Point p = getScene().convertViewToScene(dtde.getLocation());

        final JRDesignElement elem = findElementAt((AbstractReportObjectScene)getScene(), p);

        JRDesignStyle s = (JRDesignStyle) getPaletteItem().getData();
        Object toActivate = s;
        if (elem != null)

        {
            elem.setStyleNameReference(s.getName());
            toActivate = elem;
        }

        final Object obj = toActivate;

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                IReportManager.getInstance().setSelectedObject(obj);
                IReportManager.getInstance().getActiveVisualView().requestActive();
                IReportManager.getInstance().getActiveVisualView().requestAttention(true);
            }
        });
    }

}
