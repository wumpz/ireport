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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import org.openide.util.Exceptions;

/**
 *
 * @author gtoffoli
 */
public class AddGroupUndoableEdit extends AggregatedUndoableEdit {

    private JRDesignGroup group = null;
    private JRDesignDataset dataset = null;
    
    public AddGroupUndoableEdit(JRDesignGroup group, JRDesignDataset dataset)
    {
        this.group = group;
        this.dataset = dataset;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        
        super.undo();
        getDataset().removeGroup(getGroup());
        
    }

    @Override
    public void redo() throws CannotRedoException {
        
        super.redo();
        try {

            getDataset().addGroup(getGroup());
        } catch (JRException ex) {
            Exceptions.printStackTrace(ex);
            throw new CannotRedoException();
        }
    }
    
    @Override
    public String getPresentationName() {
        
        String groupName = "";
        if (getGroup() != null && getGroup().getName() != null)
        {
            groupName = getGroup().getName();
        }
        
        return "Add group " + groupName;
    }

    public

    JRDesignGroup getGroup() {
        return group;
    }

    public void setGroup(JRDesignGroup group) {
        this.group = group;
    }

    public JRDesignDataset getDataset() {
        return dataset;
    }

    public void setDataset(JRDesignDataset dataset) {
        this.dataset = dataset;
    }
}
