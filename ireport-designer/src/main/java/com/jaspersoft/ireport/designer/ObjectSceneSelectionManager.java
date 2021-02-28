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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.jasperreports.engine.design.JRDesignElement;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class ObjectSceneSelectionManager implements ObjectSceneListener{

    private AbstractReportObjectScene scene;

    private List<JRDesignElement> selectedElements = null;

    public ObjectSceneSelectionManager(AbstractReportObjectScene scene)
    {
        this.scene = scene;
        selectedElements = new ArrayList<JRDesignElement>();
        scene.addObjectSceneListener(this, ObjectSceneEventType.OBJECT_SELECTION_CHANGED);
    }

    public void objectAdded(ObjectSceneEvent arg0, Object arg1) {

    }

    public void objectRemoved(ObjectSceneEvent arg0, Object arg1) {
    }

    public void objectStateChanged(ObjectSceneEvent arg0, Object arg1, ObjectState arg2, ObjectState arg3) {
    }

    public void selectionChanged(ObjectSceneEvent event,
                      Set<Object> previousSelection,
                      Set<Object> newSelection)
    {
        // remove from selection what is no longer there...
        for (int i=0; i<selectedElements.size(); ++i)
        {
            if (!newSelection.contains(selectedElements.get(i)))
            {
                selectedElements.remove(i);
                i--;
            }
        }

        // queue the others...
        for (Iterator iter = newSelection.iterator(); iter.hasNext(); )
        {
            Object elem = iter.next();
            if (elem instanceof JRDesignElement)
            {
                JRDesignElement delem = (JRDesignElement)elem;
                selectedElements.add(delem);
            }
        }

        // update nodes...
        if (selectedElements.size() > 0)
        {
            for (JRDesignElement element : selectedElements)
            {
                Widget w = getScene().findWidget(element);
                if (w instanceof JRDesignElementWidget)
                {
                    JRDesignElementWidget dew = (JRDesignElementWidget)w;
                    dew.getSelectionWidget().updateBounds();
                    dew.getSelectionWidget().revalidate(true);
                }
            }
            getScene().validate();
        }
        
    }

    public void highlightingChanged(ObjectSceneEvent arg0, Set<Object> arg1, Set<Object> arg2) {
    }

    public void hoverChanged(ObjectSceneEvent arg0, Object arg1, Object arg2) {
    }

    public void focusChanged(ObjectSceneEvent arg0, Object arg1, Object arg2) {
    }

    /**
     * @return the scene
     */
    public AbstractReportObjectScene getScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    public void setScene(AbstractReportObjectScene scene) {
        this.scene = scene;
    }

    /**
     * @return the selectedElements
     */
    public List<JRDesignElement> getSelectedElements() {
        return selectedElements;
    }
}
