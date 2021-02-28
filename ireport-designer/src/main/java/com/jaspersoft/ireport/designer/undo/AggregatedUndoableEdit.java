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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 *
 * @author gtoffoli
 */
public class AggregatedUndoableEdit extends AbstractUndoableEdit {

    private String presentationName = "";
    private List<UndoableEdit> otherEdits = new ArrayList<UndoableEdit>();

    public AggregatedUndoableEdit()
    {
        this("");
    }
    
    public AggregatedUndoableEdit(String name)
    {
        super();
        this.presentationName = name;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        
        for (int i=otherEdits.size()-1; i>=0; --i)
        {
            UndoableEdit undoOp = otherEdits.get(i);
            undoOp.undo();
        }
        
        super.undo();
    }

    @Override
    public void redo() throws CannotRedoException {
        
        super.redo();
        for (UndoableEdit undoOp : otherEdits)
        {
            undoOp.redo();
        }
        
    }

    public String getPresentationName() {
        return presentationName;
    }

    /**
     *  concatenate add an ObjectPropertyUndoableEdit edit.
     *  The concatenated edits are undo in the inverse order they were added, and
     *  redo in the same order.
     * 
     */
    public boolean concatenate(UndoableEdit anEdit) {
        otherEdits.add( anEdit  );
        return true;
    }

    public void setPresentationName(String presentationName) {
        this.presentationName = presentationName;
    }
    
    public int getAggregatedEditCount()
    {
        return otherEdits.size();
    }

    

}
