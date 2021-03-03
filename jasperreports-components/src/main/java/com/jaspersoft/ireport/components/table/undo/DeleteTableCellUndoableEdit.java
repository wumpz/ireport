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
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: AddTableCellUndoableEdit.java 0 2010-03-26 10:36:49 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class DeleteTableCellUndoableEdit extends AggregatedUndoableEdit {

    private DesignCell cell = null;
    private BaseColumn column = null;
    private byte section = 0;
    private String groupName = null;
    private StandardTable table = null;
    private JasperDesign jasperDesign = null;


    public DeleteTableCellUndoableEdit(StandardTable table, JasperDesign jasperDesign, DesignCell cell, BaseColumn column, byte section, JRGroup group)
    {
        this(table, jasperDesign, cell, column, section, (group == null) ? null : group.getName());
    }

    public DeleteTableCellUndoableEdit(StandardTable table, JasperDesign jasperDesign, DesignCell cell, BaseColumn column, byte section, String groupName)
    {
        this.cell = cell;
        this.column = column;
        this.section = section;
        this.groupName = groupName;
        this.table = table;
        this.jasperDesign = jasperDesign;
    }

    @Override
    public void undo() throws CannotUndoException {

        super.undo();
        TableModelUtils.addCell(column, cell, section, groupName);
        TableModelUtils.fixTableLayout(table, jasperDesign);
    }

    @Override
    public void redo() throws CannotRedoException {

        super.redo();
        TableModelUtils.removeCell(column, section, groupName);
        TableModelUtils.fixTableLayout(table, jasperDesign);
    }

    @Override
    public String getPresentationName() {
        return "Delete cell";
    }


}
