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
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;

/**
 *
 * @author gtoffoli
 */
public class AddElementGroupUndoableEdit extends AggregatedUndoableEdit {

    private JRDesignElementGroup group = null;
    private Object container = null;
    
    public AddElementGroupUndoableEdit(JRDesignElementGroup group, Object container)
    {
        this.group = group;
        this.container = container;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        
        super.undo();
        
        if (container instanceof JRDesignElementGroup)
        {
            ((JRDesignElementGroup)container).removeElementGroup(group);
        }
        else if (container instanceof JRDesignFrame)
        {
            ((JRDesignFrame)container).removeElementGroup(group);
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        
        super.redo();
        if (container instanceof JRDesignElementGroup)
        {
            ((JRDesignElementGroup)container).addElementGroup(group);
        }
        else if (container instanceof JRDesignFrame)
        {
            ((JRDesignFrame)container).addElementGroup(group);
        }
        
    }
    
    @Override
    public String getPresentationName() {
        
        return "Add element group";
    }

    public JRDesignElementGroup getGroup() {
        return group;
    }

    public void setGroup(JRDesignElementGroup group) {
        this.group = group;
    }
}
