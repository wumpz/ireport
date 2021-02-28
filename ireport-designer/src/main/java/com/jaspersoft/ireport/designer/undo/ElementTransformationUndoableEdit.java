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

import java.awt.Rectangle;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class ElementTransformationUndoableEdit extends AggregatedUndoableEdit {

    
    private JRDesignElement element = null;
    protected Rectangle oldBounds = null;
    protected Rectangle newBounds = null;
    
    public ElementTransformationUndoableEdit(JRDesignElement element, Rectangle oldBounds)
    {
        this.element = element;
        this.oldBounds = oldBounds;
        this.newBounds = new Rectangle( element.getX(), element.getY(), element.getWidth(), element.getHeight());
    }
    
    @Override
    public void redo() throws CannotRedoException 
    {
        super.redo();
        this.element.setX(  newBounds.x );
        this.element.setY(  newBounds.y );
        this.element.setWidth(  newBounds.width );
        this.element.setHeight(  newBounds.height );
    }

    @Override
    public void undo() throws CannotUndoException
    {
        super.undo();
        this.element.setX(  oldBounds.x );
        this.element.setY(  oldBounds.y );
        this.element.setWidth(  oldBounds.width );
        this.element.setHeight(  oldBounds.height );
    }

    public JRDesignElement getElement() {
        return element;
    }

    public void setElement(JRDesignElement element) {
        this.element = element;
    }
    
    
}
