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
package com.jaspersoft.ireport.designer.undo;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.util.List;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class UndoMoveChildrenUndoableEdit  extends AggregatedUndoableEdit {

    private JRDesignElementWidget widget = null;


    public UndoMoveChildrenUndoableEdit(JRDesignElementWidget widget)
    {
        this.widget = widget;
    }

    @Override
    public void undo() throws CannotUndoException {

        super.undo();
        updateChildren();
    }

    @Override
    public void redo() throws CannotRedoException {

        super.redo();
        updateChildren();
    }



    @Override
    public String getPresentationName() {
        return "Element childrens update";
    }

    /**
     * @return the widget
     */
    public JRDesignElementWidget getWidget() {
        return widget;
    }

    /**
     * @param widget the widget to set
     */
    public void setWidget(JRDesignElementWidget widget) {
        this.widget = widget;
    }

    private void updateChildren() {
        updateChildren(widget);
        AbstractReportObjectScene scene = (AbstractReportObjectScene) widget.getScene();
        scene.validate();
    }
    private void updateChildren(JRDesignElementWidget wid) {

          List listOfElements = wid.getChildrenElements();
          AbstractReportObjectScene scene = (AbstractReportObjectScene) widget.getScene();

          for (int i=0; i < listOfElements.size(); ++i)
          {
               if (listOfElements.get(i) instanceof JRDesignElement)
               {
                   JRDesignElement element = (JRDesignElement)listOfElements.get(i);
                   JRDesignElementWidget w = (JRDesignElementWidget)scene.findWidget(element);
                   w.updateBounds();
                   w.getSelectionWidget().updateBounds();

                   if (w.getChildrenElements() != null)
                   {
                       updateChildren(w);
                   }
               }
          }


    }



}
