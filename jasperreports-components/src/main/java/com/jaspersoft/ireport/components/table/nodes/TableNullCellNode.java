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
package com.jaspersoft.ireport.components.table.nodes;

import com.jaspersoft.ireport.components.table.TableElementNode;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.actions.AddTableCellAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnAfterAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnBeforeAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnEndAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnGroupAction;
import com.jaspersoft.ireport.components.table.actions.AddTableColumnStartAction;
import com.jaspersoft.ireport.components.table.actions.DeleteTableColumnAction;
import com.jaspersoft.ireport.components.table.actions.GroupColumnsAction;
import com.jaspersoft.ireport.designer.outline.nodes.IRAbstractNode;
import java.util.ArrayList;
import javax.swing.Action;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class TableNullCellNode extends IRAbstractNode {

    private JasperDesign jd = null;
    private byte section = 0;
    private JRGroup group = null;
    private BaseColumn column = null;
    private JRDesignComponentElement tableElement = null;


    public TableNullCellNode(JasperDesign jd, JRDesignComponentElement tableElement, BaseColumn column, byte section, JRGroup group, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup( doLkp, Lookups.fixed(jd, column, tableElement)));
        this.jd = jd;
        this.column = column;
        this.section = section;
        this.group = group;
        this.tableElement = tableElement;
        setDisplayName ("Empty cell");

        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/cell-16.png");
    }

    public String getDisplayName()
    {
        if (column instanceof StandardColumnGroup)
        {
            return NbBundle.getMessage(TableElementNode.class, "table.emptyGroupCell.name", (TableModelUtils.getColumnIndex(getTable(), getColumn())+1));
        }
        else
        {
            return NbBundle.getMessage(TableElementNode.class, "table.emptyCell.name", (TableModelUtils.getColumnIndex(getTable(),getColumn())+1));
        }
    }

    @Override
    public String getHtmlDisplayName()
    {
        return "<font color=#999999>" + getDisplayName();
    }

    @Override
    public Action[] getActions(boolean popup) {

        java.util.List<Action> list = new ArrayList<Action>();
        list.add( SystemAction.get(AddTableCellAction.class));
        list.add(null);
        list.add( SystemAction.get(AddTableColumnAfterAction.class));
        list.add( SystemAction.get(AddTableColumnBeforeAction.class));
        list.add( SystemAction.get(AddTableColumnStartAction.class));
        list.add( SystemAction.get(AddTableColumnEndAction.class));
        list.add( SystemAction.get(AddTableColumnGroupAction.class));
        list.add( null);
        list.add( SystemAction.get(DeleteTableColumnAction.class));
        list.add( null);
        list.add( SystemAction.get(GroupColumnsAction.class));


        return list.toArray(new Action[list.size()]);
    }

    /**
     * @return the section
     */
    public byte getSection() {
        return section;
    }

    /**
     * @param section the section to set
     */
    public void setSection(byte section) {
        this.section = section;
    }

    /**
     * @return the group
     */
    public JRGroup getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(JRGroup group) {
        this.group = group;
    }

    /**
     * @return the column
     */
    public BaseColumn getColumn() {
        return column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(BaseColumn column) {
        this.column = column;
    }

    public JRDesignComponentElement getComponentElement() {
        return tableElement;
    }

    public StandardTable getTable() {
        return (StandardTable) getComponentElement().getComponent();
    }

}
