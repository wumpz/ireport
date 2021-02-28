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

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;

/**
 *
 * @author gtoffoli
 */
public class AddElementUndoableEdit extends AggregatedUndoableEdit {

    private JRDesignElement element = null;
    private Object container = null;
    private int index = -1;
    
    public AddElementUndoableEdit(JRDesignElement element, Object container)
    {
        this.element = element;
        this.container = container;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        
        super.undo();
        
        if (container instanceof JRDesignElementGroup)
        {
            index = ((JRDesignElementGroup)container).getChildren().indexOf(element);
            ((JRDesignElementGroup)container).removeElement(element);
        }
        else if (container instanceof JRDesignFrame)
        {
            index = ((JRDesignFrame)container).getChildren().indexOf(element);
            ((JRDesignFrame)container).removeElement(element);
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        
        super.redo();
        if (container instanceof JRDesignElementGroup)
        {
            // To add the element at the correct index we need to work a bit
            // since the API is limited here...
            JRDesignElementGroup cg = ((JRDesignElementGroup)container);
            element.setElementGroup(cg);
            cg.getChildren().add(index, element);
            cg.getEventSupport().fireCollectionElementAddedEvent(JRDesignElementGroup.PROPERTY_CHILDREN, element, cg.getChildren().size() - 1);
        }
        else if (container instanceof JRDesignFrame)
        {
            // To add the element at the correct index we need to work a bit
            // since the API is limited here...
            JRDesignFrame cg = ((JRDesignFrame)container);
            element.setElementGroup(cg);
            cg.getChildren().add(index, element);
            cg.getEventSupport().fireCollectionElementAddedEvent(JRDesignElementGroup.PROPERTY_CHILDREN, element, cg.getChildren().size() - 1);
        }
        
    }
    
    @Override
    public String getPresentationName() {
        
        return "Add element";
    }

    public JRDesignElement getElement() {
        return element;
    }

    public void setElement(JRDesignElement element) {
        this.element = element;
    }
}
