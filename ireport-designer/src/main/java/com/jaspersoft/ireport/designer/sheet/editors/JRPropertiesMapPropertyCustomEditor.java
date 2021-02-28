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
package com.jaspersoft.ireport.designer.sheet.editors;

import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.GenericProperty;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRPropertiesMap;
import org.openide.explorer.propertysheet.PropertyEnv;

/**
 *
 * @author  gtoffoli
 */
public class JRPropertiesMapPropertyCustomEditor extends javax.swing.JPanel implements PropertyChangeListener {
    
    private PropertyEnv env;

    private PropertyEditor editor;
    
    
    public JRPropertiesMapPropertyCustomEditor (Object value, PropertyEditor editor, PropertyEnv env) {
        this.env = env;
        this.editor = editor;
        this.env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        this.env.addPropertyChangeListener(this);

        initComponents();
        
        DefaultListSelectionModel dlsm =  (DefaultListSelectionModel)this.jTableProperties.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)  {
                jTablePropertiesListSelectionValueChanged(e);
            }
        });  
        
        if (value instanceof JRPropertiesMap && value != null)
        {
            setPropertiesMap((JRPropertiesMap)value);
        }
        else if (value instanceof List && value != null)
        {
            setPropertiesList((List)value);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelFields = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableProperties = new javax.swing.JTable();
        jPanelButtons2 = new javax.swing.JPanel();
        jButtonNewProperty = new javax.swing.JButton();
        jButtonModifyProperty = new javax.swing.JButton();
        jButtonDeleteProperty = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        jPanelFields.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(32767, 32767));
        jScrollPane3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane3MouseClicked(evt);
            }
        });

        jTableProperties.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableProperties.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePropertiesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableProperties);

        jPanelFields.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanelButtons2.setMinimumSize(new java.awt.Dimension(100, 10));
        jPanelButtons2.setPreferredSize(new java.awt.Dimension(100, 100));
        jPanelButtons2.setLayout(new java.awt.GridBagLayout());

        jButtonNewProperty.setText(I18n.getString("Global.Button.Add")); // NOI18N
        jButtonNewProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewPropertyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 4);
        jPanelButtons2.add(jButtonNewProperty, gridBagConstraints);

        jButtonModifyProperty.setText(I18n.getString("JRPropertiesMapPropertyCustomEditor.jButtonModifyProperty.text")); // NOI18N
        jButtonModifyProperty.setEnabled(false);
        jButtonModifyProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyPropertyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 4);
        jPanelButtons2.add(jButtonModifyProperty, gridBagConstraints);

        jButtonDeleteProperty.setText(I18n.getString("Global.Button.Delete")); // NOI18N
        jButtonDeleteProperty.setEnabled(false);
        jButtonDeleteProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeletePropertyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelButtons2.add(jButtonDeleteProperty, gridBagConstraints);

        jPanel1.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weighty = 1.0;
        jPanelButtons2.add(jPanel1, gridBagConstraints);

        jPanelFields.add(jPanelButtons2, java.awt.BorderLayout.EAST);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 440, Short.MAX_VALUE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(4, 4, 4)
                    .add(jPanelFields, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                    .add(4, 4, 4)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 344, Short.MAX_VALUE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(4, 4, 4)
                    .add(jPanelFields, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .add(4, 4, 4)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTablePropertiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePropertiesMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 &&  jTableProperties.getSelectedRow() >=0 ) {
            jButtonModifyPropertyActionPerformed(new java.awt.event.ActionEvent( jButtonModifyProperty,0, ""));
        }
    }//GEN-LAST:event_jTablePropertiesMouseClicked

    private void jScrollPane3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane3MouseClicked
        // Add your handling code here:
    }//GEN-LAST:event_jScrollPane3MouseClicked

    private void jButtonNewPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewPropertyActionPerformed
        Window pWin = SwingUtilities.windowForComponent(this);
        
        boolean canUseExp = false;
        if (env.getFeatureDescriptor().getValue("canUseExpression") != null &&
             env.getFeatureDescriptor().getValue("canUseExpression").equals(Boolean.TRUE))
        {
            canUseExp = true;
        }
        JRPropertyDialog jrpd = new JRPropertyDialog(Misc.getMainFrame(), true, canUseExp);
        //JRPropertyDialog jrpd = new JRPropertyDialog(w, true);
        jrpd.setProperties(getProperties());

        if (env.getFeatureDescriptor().getValue("reportProperties") != null &&
             env.getFeatureDescriptor().getValue("reportProperties").equals(Boolean.TRUE))
        {
            jrpd.addExporterHints();
        }

        if (env.getFeatureDescriptor().getValue("hintType") != null)
        {
            int type = Integer.parseInt(""+env.getFeatureDescriptor().getValue("hintType"));
            jrpd.addHints(type);
        }


        if (env.getFeatureDescriptor().getValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT) != null)
        {
            jrpd.setExpressionContext((ExpressionContext)env.getFeatureDescriptor().getValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT));
        }
        
        jrpd.setVisible(true);
        
        DefaultTableModel dtm = (DefaultTableModel)jTableProperties.getModel();
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            
            GenericProperty prop = jrpd.getProperty();
            String val =  (prop.isUseExpression()) ? Misc.getExpressionText(prop.getExpression())  : Misc.nvl(prop.getValue(),"");
            
            dtm.addRow(new Object[]{prop,val});
            jTableProperties.updateUI();
        }
    }//GEN-LAST:event_jButtonNewPropertyActionPerformed

    private void jButtonModifyPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyPropertyActionPerformed
        int index = jTableProperties.getSelectedRow();
        //index = jTableProperties.convertRowIndexToModel(index);
        DefaultTableModel dtm = (DefaultTableModel)jTableProperties.getModel();
        
        Window pWin = SwingUtilities.windowForComponent(this);
        
        boolean canUseExp = false;
        if (env.getFeatureDescriptor().getValue("canUseExpression") != null &&
             env.getFeatureDescriptor().getValue("canUseExpression").equals(Boolean.TRUE))
        {
            canUseExp = true;
        }
        JRPropertyDialog jrpd = new JRPropertyDialog(Misc.getMainFrame(), true, canUseExp);
        //JRPropertyDialog jrpd = new JRPropertyDialog(w, true);
        jrpd.setProperties(getProperties());

        if (env.getFeatureDescriptor().getValue("reportProperties") != null &&
             env.getFeatureDescriptor().getValue("reportProperties").equals(Boolean.TRUE))
        {
            jrpd.addExporterHints();
        }

        if (env.getFeatureDescriptor().getValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT) != null)
        {
            jrpd.setExpressionContext((ExpressionContext)env.getFeatureDescriptor().getValue(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT));
        }
        
        if (env.getFeatureDescriptor().getValue("hintType") != null)
        {
            int type = Integer.parseInt(""+env.getFeatureDescriptor().getValue("hintType"));
            jrpd.addHints(type);
        }
        
        //JRPropertyDialog jrpd = new JRPropertyDialog(w, true);
        jrpd.setProperty( (GenericProperty)dtm.getValueAt( index, 0));
        jrpd.setProperties(getProperties());
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            
            GenericProperty prop = jrpd.getProperty();
            String val =  (prop.isUseExpression()) ? Misc.getExpressionText(prop.getExpression())  : Misc.nvl(prop.getValue(),"");
            
            dtm.setValueAt(prop,  index, 0);
            dtm.setValueAt(val, index, 1);
            jTableProperties.updateUI();
        }
    }//GEN-LAST:event_jButtonModifyPropertyActionPerformed

    private void jButtonDeletePropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeletePropertyActionPerformed
        int[]  rows= jTableProperties.getSelectedRows();
        DefaultTableModel dtm = (DefaultTableModel)jTableProperties.getModel();
        for (int i=rows.length-1; i>=0; --i) {
            dtm.removeRow(rows[i]);  //jTableProperties.convertRowIndexToModel(rows[i])
        }
    }//GEN-LAST:event_jButtonDeletePropertyActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDeleteProperty;
    private javax.swing.JButton jButtonModifyProperty;
    private javax.swing.JButton jButtonNewProperty;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelButtons2;
    private javax.swing.JPanel jPanelFields;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableProperties;
    // End of variables declaration//GEN-END:variables
    
    
     @SuppressWarnings("unchecked")
    public void setPropertiesMap(JRPropertiesMap properties) {
        
        List<GenericProperty> list = new ArrayList<GenericProperty>();
        String[] pNames = properties.getPropertyNames();
        
        for (int i = 0; i < pNames.length; i++) {
            list.add(new GenericProperty(pNames[i], properties.getProperty(pNames[i])));
        }
        
        setPropertiesList(list);
    }

     public void setPropertiesList(List<GenericProperty> properties)
     {
         DefaultTableModel dtm = (DefaultTableModel)jTableProperties.getModel();
         dtm.setRowCount(0);
        
         for (GenericProperty prop : properties)
         {
            String val =  (prop.isUseExpression()) ? Misc.getExpressionText(prop.getExpression())  : Misc.nvl(prop.getValue(),"");
            Vector row = new Vector();
            row.addElement( prop);
            row.addElement( val );
            dtm.addRow(row);  
         }
         
     }
     
    public JRPropertiesMap getPropertiesMap() {
        JRPropertiesMap properties = new JRPropertiesMap();
        DefaultTableModel dtm = (DefaultTableModel)jTableProperties.getModel();
        for (int i=0; i<dtm.getRowCount(); ++i)
        {
            GenericProperty prop = (GenericProperty)dtm.getValueAt(i,0);
            if (!prop.isUseExpression())
            {
                properties.setProperty( prop.getKey(), (String)prop.getValue());
            }
        }
        
        return properties;
    }
    
    public List<GenericProperty> getProperties()
    {
        List<GenericProperty> props = new ArrayList<GenericProperty>();
        DefaultTableModel dtm = (DefaultTableModel)jTableProperties.getModel();
        for (int i=0; i<dtm.getRowCount(); ++i)
        {
            props.add( (GenericProperty)dtm.getValueAt(i,0) );
        }
        
        return props;   
    }
    
    
    /**
    * @return Returns the property value that is result of the CustomPropertyEditor.
    * @exception InvalidStateException when the custom property editor does not represent valid property value
    *            (and thus it should not be set)
    */
    private Object getPropertyValue () throws IllegalStateException {
        if (env.getFeatureDescriptor().getValue("useList") != null &&
            env.getFeatureDescriptor().getValue("useList").equals( Boolean.TRUE ))
        {
            return getProperties();
        }
        return getPropertiesMap();
    }



    public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {
            editor.setValue(getPropertyValue());
        }
    }
    
    public void jTablePropertiesListSelectionValueChanged(javax.swing.event.ListSelectionEvent e)
    {
         if (this.jTableProperties.getSelectedRowCount() > 0) {
            this.jButtonModifyProperty.setEnabled(true);
            this.jButtonDeleteProperty.setEnabled(true);
        }
        else {
            this.jButtonModifyProperty.setEnabled(false);
            this.jButtonDeleteProperty.setEnabled(false);
        }
    }
    
}
