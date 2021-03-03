/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table.undo;

import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.designer.undo.AggregatedUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: AddTableCellUndoableEdit.java 0 2010-03-26 10:36:49 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class DeleteTableColumnUndoableEdit extends AggregatedUndoableEdit {

    private Object parent = null;
    private BaseColumn column = null;
    private int index = 0;
    private StandardTable table = null;
    private JasperDesign jasperDesign = null;

    public DeleteTableColumnUndoableEdit(StandardTable table, JasperDesign jasperDesign, BaseColumn column, Object parent,  int index)
    {
        this.parent = parent;
        this.column = column;
        this.index = index;
        this.table = table;
        this.jasperDesign = jasperDesign;
        setPresentationName("Delete column");
    }

    @Override
    public void undo() throws CannotUndoException {

        super.undo();

        TableModelUtils.addColumn(parent, column, index);
        TableModelUtils.fixTableLayout(table, jasperDesign);
    }

    @Override
    public void redo() throws CannotRedoException {

        super.redo();
        TableModelUtils.removeColumn(parent, column, index);
        TableModelUtils.fixTableLayout(table, jasperDesign);
    }

}
