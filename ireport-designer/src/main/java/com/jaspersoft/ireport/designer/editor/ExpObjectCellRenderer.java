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
package com.jaspersoft.ireport.designer.editor;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.DefaultStyledDocument;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;

/**
 * @author gtoffoli
 */
public class ExpObjectCellRenderer extends JTextPane implements ListCellRenderer, TableCellRenderer {
        private Color selectionBackground;
        private Color background;
        
        private boolean showObjectType = true;
        private boolean showObjectClass = true;

        // Create a style object and then set the style attributes
        Style typeStyle = null;
        Style classTypeStyle = null;
        
        Style parameterStyle = null;
        Style variableStyle = null;
        Style fieldStyle = null;
        Style whiteStyle = null;
        Style normalStyle = null;

        public ExpObjectCellRenderer(JList list) {
            super();
            
            initColors();
            selectionBackground = list.getSelectionBackground();
            background = list.getBackground();
            
            
        }

    protected Component getRendererComponent(Object object, boolean isSelected, boolean cellHasFocus) {
       
            this.setText("");
            StyledDocument doc = (StyledDocument)this.getDocument();
                        
            if (object instanceof JRVariable ||
                object instanceof JRField ||
                object instanceof JRParameter)
            {
                object = new ExpObject(object);
            }
            
            
            setBackground(isSelected || cellHasFocus ? selectionBackground : background);
            StyleConstants.setBackground(typeStyle, isSelected || cellHasFocus ? selectionBackground : background);
            StyleConstants.setBackground(classTypeStyle, isSelected || cellHasFocus ? selectionBackground : background);
            StyleConstants.setBackground(parameterStyle, isSelected || cellHasFocus ? selectionBackground : background);
            StyleConstants.setBackground(variableStyle, isSelected || cellHasFocus ? selectionBackground : background);
            StyleConstants.setBackground(fieldStyle, isSelected || cellHasFocus ? selectionBackground : background);
            StyleConstants.setBackground(whiteStyle, isSelected || cellHasFocus ? selectionBackground : background);
            
            if (object instanceof ExpObject)
            {
                ExpObject eo = (ExpObject)object;
                 
                 try {
                     
                     doc.insertString(doc.getLength(), eo.getName() + "   ", (isSelected || cellHasFocus) ? whiteStyle : null);
                     
                     if (isShowObjectType())
                     {
                         Style s = parameterStyle;
                         String type = "Parameter";

                         if (eo.getType() == eo.TYPE_FIELD) 
                         {
                             s = fieldStyle;
                             type = "Field";
                         }
                         else if (eo.getType() == eo.TYPE_VARIABLE)
                         {
                             s = variableStyle;
                             type = "Variable";
                         }
                         if (isSelected || cellHasFocus) s = whiteStyle;
                         
                         doc.insertString(doc.getLength(), type + " ", s);
                     }

                     if (isShowObjectClass())
                     {
                         String tp = eo.getClassType() + "";
                         if (tp.lastIndexOf(".") > 0) tp = tp.substring(tp.lastIndexOf(".")+1);

                         doc.insertString(doc.getLength(), tp, classTypeStyle);
                         doc.setLogicalStyle(0, normalStyle);
                     }
                } catch (Exception ex){}
            }
            else
            {
                 try {
                     if (object != null)
                     {
                         doc.insertString(doc.getLength(), "" + object, null);
                     }
                 } catch (Exception ex){}
            }
            setOpaque(true);
            
            this.revalidate();
            return this;
    }
        
        private void initColors()
        {
            selectionBackground = UIManager.getColor("List.selectionBackground");
            background = UIManager.getColor("List.background");
            
            StyledDocument doc = new DefaultStyledDocument();
            this.setDocument( doc );
            
            normalStyle = doc.addStyle("normalStyle", null);
            
            java.awt.Font font = UIManager.getFont("List.font");
            StyleConstants.setFontFamily(normalStyle, font.getFamily() );
            StyleConstants.setFontSize(normalStyle, font.getSize() );
            StyleConstants.setForeground(normalStyle, UIManager.getColor("List.foreground") );
            
            
            typeStyle = doc.addStyle("typeStyle", null);
            StyleConstants.setItalic(typeStyle, true);
            StyleConstants.setForeground(typeStyle, Color.gray);
            
            classTypeStyle = doc.addStyle("classTypeStyle", null);
            StyleConstants.setForeground(classTypeStyle, Color.gray);
            
            parameterStyle = doc.addStyle("parameterStyle", null);
            StyleConstants.setForeground(parameterStyle, Color.red.darker());
            
            variableStyle = doc.addStyle("variableStyle", null);
            StyleConstants.setForeground(variableStyle, Color.blue);
            
            fieldStyle = doc.addStyle("fieldStyle", null);
            StyleConstants.setForeground(fieldStyle, Color.green.darker().darker());
            
            whiteStyle = doc.addStyle("whiteStyle", null);
            StyleConstants.setForeground(whiteStyle, Color.white);
        }
        
        public ExpObjectCellRenderer() {
            super();
            initColors();
            
        }
        
        public Component getListCellRendererComponent(JList list, Object object,
                int index, boolean isSelected, boolean cellHasFocus) {
                
            return getRendererComponent(object, isSelected, cellHasFocus);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return getRendererComponent(value, isSelected, hasFocus);
        }

    public boolean isShowObjectType() {
        return showObjectType;
    }

    public void setShowObjectType(boolean showObjectType) {
        this.showObjectType = showObjectType;
    }

    /**
     * @return the showObjectClass
     */
    public boolean isShowObjectClass() {
        return showObjectClass;
    }

    /**
     * @param showObjectClass the showObjectClass to set
     */
    public void setShowObjectClass(boolean showObjectClass) {
        this.showObjectClass = showObjectClass;
    }
    
    
    }


