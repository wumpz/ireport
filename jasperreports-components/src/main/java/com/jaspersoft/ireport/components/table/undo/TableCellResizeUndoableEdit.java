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
package com.jaspersoft.ireport.components.table.undo;

import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class TableCellResizeUndoableEdit extends ObjectPropertyUndoableEdit {

    private boolean main = false;
    private StandardTable table = null;
    private JasperDesign jasperDesign = null;
    
    @Override
    public String getPresentationName() {
        return "Table Row/Column resize";
    }
    
    public TableCellResizeUndoableEdit(StandardTable table, JasperDesign jasperDesign, Object object, String propertyName, Class propertyClass, Object oldValue,  Object newValue)
    {
        super(object, propertyName, propertyClass, oldValue,  newValue);
        this.table = table;
        this.jasperDesign = jasperDesign;
    }

    public

    boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        if (isMain() && table != null)
        {
             // This trick is used to force a document rebuild...
           table.getEventSupport().firePropertyChange( StandardTable.PROPERTY_COLUMNS , null, null);
           TableModelUtils.fixTableLayout(table, jasperDesign);
        }
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        if (isMain() && table != null)
        {
           // This trick is used to force a document rebuild...
           table.getEventSupport().firePropertyChange( StandardTable.PROPERTY_COLUMNS , null, null);
           TableModelUtils.fixTableLayout(table, jasperDesign);
        }
    }

}
