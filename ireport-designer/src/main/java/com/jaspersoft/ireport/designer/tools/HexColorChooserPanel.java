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
package com.jaspersoft.ireport.designer.tools;

import com.jaspersoft.ireport.locale.I18n;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author  gtoffoli
 */
public class HexColorChooserPanel extends javax.swing.colorchooser.AbstractColorChooserPanel {
    
    private JDialog dialog = null;
    private Color selectedColor = null;
    private boolean init = false;
    
    /** Creates new form HexColorChooserPanel */
    public HexColorChooserPanel() {
        initComponents();
        
        jTextField1.setDocument(new MaskedPlainDocument(MaskedPlainDocument.COLOR_MASK));
    
        /*
        ((AbstractDocument)jTextField1.getDocument()).setDocumentFilter(new DocumentFilter(){
            
            // This method is called when characters are inserted into the document
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String str,
                    AttributeSet attr) throws BadLocationException {
                replace(fb, offset, 0, str, attr);
            }

            // This method is called when characters in the document are replace with other characters
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
                    String str, AttributeSet attrs) throws BadLocationException {
                int newLength = fb.getDocument().getLength()-length+str.length();
                if (newLength <= 6 || ( newLength ==7 && fb.getDocument().getText(0,1).startsWith("#"))) {
                    fb.replace(offset, length, str, attrs);
                } else {
                    throw new BadLocationException("New characters exceeds max size of document", offset);
                }
            }
        } );
         */

        /*
        jTextField1.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                
                if (e.getKeyCode() == e.VK_ENTER)
                {
                   if (jTextField1.getText().length() >= 6)
                   {
                        this.updateColor(); 
                   }
                }
            }
            public void keyReleased(KeyEvent e) {
            }
            public void keyTyped(KeyEvent e) {
            }
        });
         */
        
        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                if (jTextField1.getText().length() >= 6)
                   {
                        updateColor(); 
                   }
            }
            public void insertUpdate(DocumentEvent e) {
                if (jTextField1.getText().length() >= 6)
                   {
                        updateColor(); 
                   }
            }
            public void removeUpdate(DocumentEvent e) {
            }
        });
        
        jTextField1.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
            }
            public void focusLost(FocusEvent e) {
                updateColor();
            }
        });
    }
    
    public void updateColor()
    {
        if (isInit()) return;
        Color c = parseColorString( jTextField1.getText());
        if (c != null)
        {
            setInit(true);
            getColorSelectionModel().setSelectedColor(c);
            
            setInit(false);
        }
    }
    
    public static Color parseColorString(String newValue)
    {
        if (newValue == null) return null;
        
        newValue = newValue.trim();
        // Try to create the color from a string...
        java.awt.Color c = null;
        if (newValue.startsWith("#"))
        {
            newValue = newValue.substring(1);
        }
        if (newValue.length() == 6)
        {
            try {
                int hr = Integer.parseInt( newValue.substring(0,2), 16);
                int hg = Integer.parseInt( newValue.substring(2,4), 16);
                int hb = Integer.parseInt( newValue.substring(4,6), 16);
                c = new Color(hr,hg,hb);
                return c;
            } catch (Exception ex)
            {
                
            }
        }    

        return null;
               
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Hex 0x");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        add(jLabel1, gridBagConstraints);

        jTextField1.setText("000000");
        jTextField1.setMinimumSize(new java.awt.Dimension(100, 19));
        jTextField1.setPreferredSize(new java.awt.Dimension(100, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        add(jTextField1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    public static String getEncodedColor(java.awt.Color c) {
        String nums = "0123456789ABCDEF";
        String s = "#";
        s += nums.charAt( c.getRed()/16 );
        s += nums.charAt( c.getRed()%16 );
        s += nums.charAt( c.getGreen()/16 );
        s += nums.charAt( c.getGreen()%16 );
        s += nums.charAt( c.getBlue()/16 );
        s += nums.charAt( c.getBlue()%16 );
        return s;
    }

    
    public void updateChooser() {
        
        if (isInit()) return;
        Color c = this.getColorFromModel();
        if (c != null)
        {
            boolean oldValue = isInit();
            setInit(true);
            try {
                
                jTextField1.setText(getEncodedColor(c));
            } finally {
                setInit(oldValue);
            }
        }
    }

    protected void buildChooser() {
    }

    public String getDisplayName() {
        return I18n.getString("HexColorChooserPanel.Display.Name"); 
    }

    public Icon getSmallDisplayIcon() {
        return null;
    }

    public Icon getLargeDisplayIcon() {
        return null;
    }
    
    
    public static Color showDialog(Component c, String title, Color defColor)
    {
        final JColorChooser jcc = new JColorChooser();
        final HexColorChooserPanel hcp = new HexColorChooserPanel();
        AbstractColorChooserPanel[] current_panels = jcc.getChooserPanels();
        AbstractColorChooserPanel[]  panels = new AbstractColorChooserPanel[current_panels.length + 1];
        int i=0;
        for (; i<current_panels.length; ++i)
        {
            panels[i] = current_panels[i];
        }
        panels[i] = hcp;
        jcc.setChooserPanels( panels );
        if (defColor != null)
        {
            jcc.setColor(defColor);
        }
        
        JDialog theDialog = jcc.createDialog(c,title,true,jcc,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        hcp.setSelectedColor( jcc.getColor() );
                        hcp.getDialog().setVisible(false);
                        hcp.getDialog().dispose();
                    }
                },new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        hcp.setSelectedColor( null );
                        hcp.getDialog().setVisible(false);
                        hcp.getDialog().dispose();
                    }
                });
       hcp.setDialog( theDialog );
       
       theDialog.setVisible(true);
       
       return hcp.getSelectedColor();
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    private JDialog getDialog() {
        return dialog;
    }

    private void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    
    
}

