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
package com.jaspersoft.ireport.components.table;

import com.jaspersoft.ireport.components.table.wizard.TableWizardIterator;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.palette.actions.*;
import com.jaspersoft.ireport.designer.undo.AddDatasetUndoableEdit;
import com.jaspersoft.ireport.designer.undo.AddStyleUndoableEdit;
import com.jaspersoft.ireport.designer.undo.AggregatedUndoableEdit;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Dialog;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;

/**
 *
 * @author gtoffoli
 */
public class CreateTableAction extends CreateReportElementAction {

    @Override
    public JRDesignElement createReportElement(JasperDesign jd) {

        TableWizardIterator iterator = new TableWizardIterator();

        WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);

        iterator.initialize(wizardDescriptor, jd);

        // {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
        // {1} will be replaced by WizardDescriptor.Iterator.name()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
        wizardDescriptor.setTitle("Table wizard");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {

            AggregatedUndoableEdit edit = null;
            JRDesignComponentElement component = new JRDesignComponentElement();
            StandardTable componentImpl = new StandardTable();

            int type = getIntProperty(wizardDescriptor, "table_type",1);
            int columns = getIntProperty(wizardDescriptor, "columns",4);
            int height = 20;

            List<JRDesignStyle> styles = getStylesProperty(wizardDescriptor);

            height += getBooleanProperty(wizardDescriptor, "th", true) ? 30 : 0;
            height += getBooleanProperty(wizardDescriptor, "ch", true) ? 30 : 0;
            height += getBooleanProperty(wizardDescriptor, "cf", true) ? 30 : 0;
            height += getBooleanProperty(wizardDescriptor, "tf", true) ? 30 : 0;

            if (type == 1)
            {
                for (int i=0; i< columns; ++i)
                {
                    BaseColumn column = createColumn(wizardDescriptor, null, null);
                    componentImpl.addColumn(column);
                }

                JRDesignDataset newDataset = new JRDesignDataset(false);
                String name = "Table Dataset ";
                for (int i = 1;; i++) {
                    if (!jd.getDatasetMap().containsKey(name + i)) {
                        newDataset.setName(name + i);
                        break;
                    }
                }
                try {
                    jd.addDataset(newDataset);
                    AddDatasetUndoableEdit edit2 = new AddDatasetUndoableEdit(newDataset, jd);
                    if (edit == null) edit = edit2;
                    else edit.concatenate(edit2);

                } catch (JRException ex) {
                    //Exceptions.printStackTrace(ex);
                }
                
                JRDesignDatasetRun datasetRun = new JRDesignDatasetRun();

                datasetRun.setDatasetName(newDataset.getName());

                JRDesignExpression exp = new JRDesignExpression();
                exp.setValueClassName("net.sf.jasperreports.engine.JRDataSource");//NOI18N
                exp.setText("new net.sf.jasperreports.engine.JREmptyDataSource(1)");//NOI18N

                datasetRun.setDataSourceExpression(exp);
                componentImpl.setDatasetRun(datasetRun);
                
            }
            else if (type == 0)
            {
                List<JRDesignField> fields = (List<JRDesignField>)wizardDescriptor.getProperty("selectedFields");

                JRDesignDatasetRun datasetRun = new JRDesignDatasetRun();

                String con_exp = (String)wizardDescriptor.getProperty("table_connection_expression");
                String ds_exp = (String)wizardDescriptor.getProperty("table_datasource_expression");
                JRDesignDataset dataset = (JRDesignDataset)wizardDescriptor.getProperty("dataset");

                List groups = dataset.getGroupsList();

                for (int i=0; i< groups.size(); ++i)
                {
                    height += getBooleanProperty(wizardDescriptor, "gh", true) ? 30 : 0;
                    height += getBooleanProperty(wizardDescriptor, "gf", true) ? 30 : 0;
                }
                if (con_exp != null)
                {
                    datasetRun.setConnectionExpression( Misc.createExpression("java.sql.Connection", con_exp) );
                }
                else if (ds_exp != null)
                {
                    datasetRun.setDataSourceExpression( Misc.createExpression("net.sf.jasperreports.engine.JRDataSource", ds_exp) );
                }
                else
                {
                    datasetRun.setDataSourceExpression( Misc.createExpression("net.sf.jasperreports.engine.JRDataSource", "new net.sf.jasperreports.engine.JREmptyDataSource(1)") );
                }

                datasetRun.setDatasetName(dataset.getName());

                if (fields == null || fields.size() == 0)
                {
                    for (int i=0; i<4; ++i)
                    {
                        BaseColumn column = createColumn(wizardDescriptor, null, null);
                        componentImpl.addColumn(column);
                    }
                }
                else
                {
                    for (JRDesignField field : fields)
                    {
                        BaseColumn column = createColumn(wizardDescriptor, field, dataset);
                        componentImpl.addColumn(column);
                    }
                }

                componentImpl.setDatasetRun(datasetRun);

            }

            // Add the styles...

            for (JRDesignStyle style : styles)
            {
                try {
                    jd.addStyle(style);
                    AddStyleUndoableEdit edit2 = new AddStyleUndoableEdit(style, jd);
                    if (edit == null) edit = edit2;
                    else edit.concatenate(edit2);
                } catch (JRException ex) {
                    ex.printStackTrace();
                }
            }

            if (styles.size() > 0)
            {
                component.setStyle(styles.get(0));
            }
            
            component.setKey((String) wizardDescriptor.getProperty("basename"));
            component.setComponent(componentImpl);
            component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "table"));

            component.setWidth(columns*90);
            component.setHeight(height);

            if (edit != null)
            {
                IReportManager.getInstance().addUndoableEdit(edit);
            }

            return component;
        }


        
        return null;
        
    }


    public BaseColumn createColumn(WizardDescriptor wizardDescriptor, JRDesignField field, JRDesignDataset dataset)
    {
        StandardColumn column = new StandardColumn();
        column.setWidth(90);

        List<JRDesignStyle> styles = getStylesProperty(wizardDescriptor);
        if (getBooleanProperty(wizardDescriptor, "th", true))
        {
            DesignCell cell = new DesignCell();
            cell.setHeight(30);
            if (styles.size() > 1)
            {
                cell.setStyle(styles.get(1));
            }
            column.setTableHeader(cell);
        }

        if (getBooleanProperty(wizardDescriptor, "tf", true))
        {
            DesignCell cell = new DesignCell();
            cell.setHeight(30);
            if (styles.size() > 1)
            {
                cell.setStyle(styles.get(1));
            }
            column.setTableFooter(cell);
        }

        if (getBooleanProperty(wizardDescriptor, "ch", true))
        {
            DesignCell cell = new DesignCell();
            cell.setHeight(30);
            if (styles.size() > 2)
            {
                cell.setStyle(styles.get(2));
            }
            column.setColumnHeader(cell);

            if (field != null)
            {
                JRDesignStaticText text=  new JRDesignStaticText(getJasperDesign());
                text.setText(field.getName());
                text.setWidth(90);
                text.setHeight(30);
                cell.addElement(text);
            }
        }

        if (getBooleanProperty(wizardDescriptor, "cf", true))
        {
            DesignCell cell = new DesignCell();
            cell.setHeight(30);
            if (styles.size() > 2)
            {
                cell.setStyle(styles.get(2));
            }
            column.setColumnFooter(cell);
        }

        // Details cell....
        {
            DesignCell cell = new DesignCell();
            cell.setHeight(20);
            if (styles.size() > 3)
            {
                cell.setStyle(styles.get(3));
            }
            column.setDetailCell(cell);

            if (field != null)
            {
                JRDesignTextField text=  new JRDesignTextField(getJasperDesign());
                JRDesignExpression exp = Misc.createExpression(field.getValueClassName(), "$F{"+ field.getName() + "}");

                CreateTextFieldAction.setMatchingClassExpression(exp, field.getValueClassName(), true);
                text.setExpression(exp);
                text.setWidth(90);
                text.setHeight(20);
                cell.addElement(text);
            }
        }

        if (dataset != null)
        {
            List groups = dataset.getGroupsList();

            for (int i=0; i< groups.size(); ++i)
            {
                if (getBooleanProperty(wizardDescriptor, "gh", true))
                {

                        JRDesignGroup group = (JRDesignGroup)groups.get(i);

                        DesignCell cell = new DesignCell();
                        cell.setHeight(30);
                        if (styles.size() > 1)
                        {
                            cell.setStyle(styles.get(1));
                        }
                        column.setGroupHeader(group.getName(), cell);

                        JRDesignTextField text=  new JRDesignTextField(getJasperDesign());
                        if (group.getExpression() != null)
                        {
                            JRDesignExpression exp = (JRDesignExpression) group.getExpression().clone();
                            CreateTextFieldAction.setMatchingClassExpression(exp, exp.getValueClassName(), true);
                            text.setExpression(exp);
                            text.setWidth(90);
                            text.setHeight(20);
                            cell.addElement(text);
                        }
                        
                }
                if (getBooleanProperty(wizardDescriptor, "gf", true))
                {

                        JRDesignGroup group = (JRDesignGroup)groups.get(i);

                        DesignCell cell = new DesignCell();
                        cell.setHeight(30);
                        if (styles.size() > 1)
                        {
                            cell.setStyle(styles.get(1));
                        }
                        column.setGroupFooter(group.getName(), cell);
                }
            }

        }

        return column;
    }

    public int getIntProperty(WizardDescriptor wizard, String name, int defValue)
    {
        if (wizard.getProperty(name) != null &&
            wizard.getProperty(name) instanceof Number)
        {
            return ((Number)wizard.getProperty(name)).intValue();
        }
        return defValue;
    }

    public boolean getBooleanProperty(WizardDescriptor wizard, String name, boolean defValue)
    {
        if (wizard.getProperty(name) != null &&
            wizard.getProperty(name) instanceof Boolean)
        {
            return ((Boolean)wizard.getProperty(name)).booleanValue();
        }
        return defValue;
    }

    public List<JRDesignStyle> getStylesProperty(WizardDescriptor wizard)
    {
        List<JRDesignStyle> styles = new ArrayList<JRDesignStyle>();
        if (getBooleanProperty(wizard, "create_styles", true) == false)
        {
            return styles;
        }

        if (wizard.getProperty("styles") != null &&
            wizard.getProperty("styles") instanceof List)
        {
            return (List<JRDesignStyle>)wizard.getProperty("styles");
        }
        return styles;
    }

}
