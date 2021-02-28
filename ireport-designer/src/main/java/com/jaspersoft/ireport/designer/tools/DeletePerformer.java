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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.undo.DeleteElementUndoableEdit;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;

/**
 *
 * @version $Id: DeletePerformer.java 0 2010-09-02 19:49:02 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class DeletePerformer {

    private List<JRDesignElement> elementsToDelete = new ArrayList<JRDesignElement>();
    private Timer t = null;

    public void deleteElement(JRDesignElement element)
    {
        if (!elementsToDelete.contains(element))
        {
            elementsToDelete.add(element);
        }

        if (t == null)
        {
            t = new Timer();
            t.schedule(new TimerTask() {

                @Override
                public void run() {
                    deleteElements();
                }
            }, 100);
        }
    }

    private void deleteElements() {

            List<JRDesignElement> elements = new ArrayList<JRDesignElement>(elementsToDelete);
            elementsToDelete.clear();
            t = null;

            List containers = new ArrayList();

            boolean first = true;
            for (JRDesignElement element : elements)
            {
               Object container = element.getElementGroup();
               if (!containers.contains(container))
               {
                containers.add(container);
               }

               int index = 0;
               if (container instanceof JRDesignElementGroup)
               {
                   index = ((JRDesignElementGroup)container).getChildren().indexOf(element);
                   ((JRDesignElementGroup)container).getChildren().remove(element);
                   element.setElementGroup(null);
               }
               if (container instanceof JRDesignFrame)
               {
                   index = ((JRDesignFrame)container).getChildren().indexOf(element);
                   ((JRDesignFrame)container).getChildren().remove(element);
                   element.setElementGroup(null);
               }

               DeleteElementUndoableEdit edit = new DeleteElementUndoableEdit(element,container,index);
               IReportManager.getInstance().addUndoableEdit(edit, !first);
               first=false;
            }

            // Notify all the containers...
            for (Object container : containers)
            {
                if (container instanceof JRDesignElementGroup)
                {
                    ((JRDesignElementGroup)container).getEventSupport().firePropertyChange(JRDesignElementGroup.PROPERTY_CHILDREN, null, 0);
                }
                else if (container instanceof JRDesignFrame)
                {
                    ((JRDesignFrame)container).getEventSupport().firePropertyChange(JRDesignElementGroup.PROPERTY_CHILDREN, null, 0);
                }
                else {
                    System.out.println("Unknown container: " + container);
                }

            }
    }
    
}
