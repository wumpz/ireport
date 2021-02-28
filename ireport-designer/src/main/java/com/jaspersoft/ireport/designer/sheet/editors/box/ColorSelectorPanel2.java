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
package com.jaspersoft.ireport.designer.sheet.editors.box;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.tools.HexColorChooserPanel;
import com.jaspersoft.ireport.designer.tools.MaskedPlainDocument;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
/**
 *
 * @author  Administrator
 */
public class ColorSelectorPanel2 extends javax.swing.JPanel {
    
    private Color color = Color.BLACK;
    private String value = "#000000";
    private ImageIcon noColorIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/nocolor.png"));
    
    private boolean init = false;
    
    
    /** Creates new form ColorSelectorPanel */
    public ColorSelectorPanel2() {
        initComponents();
        applyI18n();
        setColor(null);
        
        txtBorderSupplier.setVisible(false);
    }

    public String getValue() {
        return value;
    }

    public void setValue(Object newValue) {
        
        if (newValue == null){
            this.setColor(null);
            return;
        }
        
        Color newColor = null;
        if (newValue instanceof Color) newColor = (Color)newValue;
        else newColor = parseColorString(""+newValue);
        
        if (newColor == null) return;
        this.setColor(newColor);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        
        lblNoColor.setIcon(color == null ? noColorIcon : null);
        
        if (color == null){
            this.value=null;
            txtColor.setText("");
            this.pnlColor.setBackground(Color.WHITE);
            this.pnlColor.setBorder(new LineBorder(Color.LIGHT_GRAY));
            this.pnlColor.invalidate();
            this.pnlColor.updateUI();
        }
        else
        {
            this.pnlColor.setBorder(new LineBorder(Color.BLACK));
            this.value = HexColorChooserPanel.getEncodedColor( color );
            this.pnlColor.setBackground(color );
            this.pnlColor.invalidate();
            this.pnlColor.updateUI();
            txtColor.setText( HexColorChooserPanel.getEncodedColor( color ) );
        }
        
        
        fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,""));
    }
    
    public static Color parseColorString(String newValue)
    {
        if (newValue == null) return null;
        
        
        newValue = newValue.trim();
        //System.out.println("Evaluating: " + newValue);
        
        if (!newValue.startsWith("[") || !newValue.endsWith("]"))
        {
            // Try to create the color from a string...
            java.awt.Color c = java.awt.Color.getColor(newValue);
            if (c != null) return c;
            if (c == null && newValue.matches( MaskedPlainDocument.COLOR_MASK))
            {
                if (newValue.startsWith("#"))
                {
                    newValue = newValue.substring(1);
                }
                if (newValue.length() >=6)
                {
                    try {
                        int hr = Integer.parseInt( newValue.substring(0,2), 16);
                        int hg = Integer.parseInt( newValue.substring(2,4), 16);
                        int hb = Integer.parseInt( newValue.substring(4,6), 16);
                        c = new Color(hr,hg,hb);
                    } catch (Exception ex)
                    {
                    }
                }
            }
            return c;
        }
        
        int r = 0;
        int g = 0;
        int b = 0;
        String rgbValues = newValue.substring(1,newValue.length()-1);  
        try {
        
            StringTokenizer st = new StringTokenizer(rgbValues, ",",false);
            r = Integer.parseInt( st.nextToken() );
            g = Integer.parseInt( st.nextToken() );
            b = Integer.parseInt( st.nextToken() );
        } catch (Exception ex) { return null; }
        
        Color c = new Color(r,g,b);
        return c;
        
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlColor = new javax.swing.JPanel();
        lblNoColor = new javax.swing.JLabel();
        txtBorderSupplier = new javax.swing.JTextField();
        txtColor = new javax.swing.JTextField();
        jButtonSelect = new javax.swing.JButton();

        setBackground(java.awt.SystemColor.text);
        setBorder(txtBorderSupplier.getBorder());
        setMaximumSize(new java.awt.Dimension(2147483647, 22));
        setMinimumSize(new java.awt.Dimension(120, 22));
        setPreferredSize(new java.awt.Dimension(120, 22));
        setLayout(new java.awt.GridBagLayout());

        pnlColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlColor.setMaximumSize(new java.awt.Dimension(11, 11));
        pnlColor.setMinimumSize(new java.awt.Dimension(11, 11));
        pnlColor.setPreferredSize(new java.awt.Dimension(11, 11));
        pnlColor.setLayout(new java.awt.BorderLayout());
        pnlColor.add(lblNoColor, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(pnlColor, gridBagConstraints);

        txtBorderSupplier.setEditable(false);
        txtBorderSupplier.setEnabled(false);
        txtBorderSupplier.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        add(txtBorderSupplier, gridBagConstraints);

        txtColor.setText("[0,0,0]");
        txtColor.setBorder(null);
        txtColor.setMinimumSize(new java.awt.Dimension(20, 20));
        txtColor.setPreferredSize(new java.awt.Dimension(20, 20));
        txtColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColorActionPerformed(evt);
            }
        });
        txtColor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtColorFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(txtColor, gridBagConstraints);

        jButtonSelect.setText("...");
        jButtonSelect.setFocusable(false);
        jButtonSelect.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonSelect.setMaximumSize(new java.awt.Dimension(18, 18));
        jButtonSelect.setMinimumSize(new java.awt.Dimension(18, 18));
        jButtonSelect.setPreferredSize(new java.awt.Dimension(18, 18));
        jButtonSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        add(jButtonSelect, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void txtColorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtColorFocusLost
        txtColorActionPerformed(null);
}//GEN-LAST:event_txtColorFocusLost

    private void jButtonSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectActionPerformed
        Color c = HexColorChooserPanel.showDialog(this,I18n.getString("ColorSelectorPanel.Dialog.PickColor"),this.getColor());
        if (c != null) setColor(c);
        txtColor.requestFocus();
    }//GEN-LAST:event_jButtonSelectActionPerformed

    private void txtColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColorActionPerformed
       
        Color newColor = parseColorString(txtColor.getText());
        if (newColor == null)
        {
            this.txtColor.setText(getValue());
        }
        if (newColor != null) setColor(newColor);
        
}//GEN-LAST:event_txtColorActionPerformed

    /**
     * Registers ActionListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addActionListener(java.awt.event.ActionListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (java.awt.event.ActionListener.class, listener);
    }

    /**
     * Removes ActionListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeActionListener(java.awt.event.ActionListener listener) {

        listenerList.remove (java.awt.event.ActionListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    private void fireActionListenerActionPerformed(java.awt.event.ActionEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==java.awt.event.ActionListener.class) {
                ((java.awt.event.ActionListener)listeners[i+1]).actionPerformed (event);
            }
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSelect;
    private javax.swing.JLabel lblNoColor;
    private javax.swing.JPanel pnlColor;
    private javax.swing.JTextField txtBorderSupplier;
    private javax.swing.JTextField txtColor;
    // End of variables declaration//GEN-END:variables
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                //jButtonSelect.setText(I18n.getString("colorSelectorPanel.buttonSelect","..."));
                // End autogenerated code ----------------------
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
}
