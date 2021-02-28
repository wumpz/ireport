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


package com.jaspersoft.ireport.designer.connection.gui;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author gtoffoli
 */
public class ExcelColumnEditor extends AbstractCellEditor implements TableCellEditor {
        protected JTextField textField;
        protected Object oldValue;

        static final String digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

            /** Creates new IntegerCellEditor */
            public ExcelColumnEditor() {
                textField = new JTextField();
                //textField.addActionListener(this);
                textField.setEditable(true);
                textField.setHorizontalAlignment(SwingConstants.RIGHT);
            }

            public Component getTableCellEditorComponent(JTable table,Object value,boolean isSelected,int row,int column) {
                textField.setText(value.toString());
                oldValue = value;
                return textField;
            }

            /** Returns the value contained in the editor */
            public Object getCellEditorValue() {
                String name = oldValue.getClass().getName();
                Object returnValue=null;

                String val = textField.getText();

                if (val.matches("[A-Z]*"))
                {
                    int theReturnValue = 0;
                    int esp = 0;
                    for (int i=val.length()-1; i>=0; --i)
                    {
                        int digitVal = digits.indexOf(val.charAt(i));
                        if (esp > 0) digitVal++;
                        theReturnValue += digitVal * (int)Math.pow(26, esp);
                        esp++;
                    }

                    return new Integer(theReturnValue);
                }

                try {
                    if (name.equals("java.lang.Integer"))
                        returnValue = new java.lang.Integer(textField.getText());
                    else if (name.equals("java.lang.Double"))
                        returnValue = new java.lang.Double(textField.getText());
                    else if (name.equals("java.lang.Float"))
                        returnValue = new java.lang.Float(textField.getText());
                    else if (name.equals("java.lang.Long"))
                        returnValue = new java.lang.Long(textField.getText());
                    else if (name.equals("java.lang.Short"))
                        returnValue = new java.lang.Short(textField.getText());
                    else if (name.equals("java.lang.Byte"))
                        returnValue = new java.lang.Byte(textField.getText());
                    else if (name.equals("java.math.BigDecimal"))
                        returnValue = new java.math.BigDecimal(textField.getText());
                    else if (name.equals("java.math.BigInteger"))
                        returnValue = new java.math.BigInteger(textField.getText());
                } catch (NumberFormatException e) {}
                return returnValue;
            }

    }