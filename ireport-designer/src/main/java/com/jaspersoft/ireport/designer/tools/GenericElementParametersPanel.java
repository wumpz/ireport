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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignGenericElementParameter;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignSubreportParameter;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author  gtoffoli
 */
public class GenericElementParametersPanel extends javax.swing.JPanel {
    
    private List parameters = new ArrayList();
    
    private ExpressionContext expressionContext = null;

    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    public void setExpressionContext(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
    }
    

    public List getParameters() {
        return parameters;
    }

    /**
     * This method will duplicate the map. The panel will work on a copy of the map.
     **/
    @SuppressWarnings("unchecked")
    public void setParameters(List oldParameters) {
        
        this.parameters.clear();
        DefaultTableModel model = (DefaultTableModel)this.jTable.getModel();
        model.setRowCount(0);
        // Create a copy of the map content...
        Iterator iterator = oldParameters.iterator();
        while (iterator.hasNext())
        {
            JRDesignGenericElementParameter oldParameter = (JRDesignGenericElementParameter)iterator.next();
            
            JRDesignGenericElementParameter parameter = new JRDesignGenericElementParameter();
            parameter.setName(oldParameter.getName() );
            if (oldParameter.getValueExpression() != null)
            {
                JRDesignExpression exp = new JRDesignExpression();
                exp.setText(oldParameter.getValueExpression().getText());
                exp.setValueClassName(oldParameter.getValueExpression().getValueClassName());
                parameter.setValueExpression(exp);
            }
            parameters.add(parameter);
                
            model.addRow(new Object[]{parameter,Misc.getExpressionText(parameter.getValueExpression()) });
        }
   }
    
    
    /** Creates new form SubreportParametersPanel */
    public GenericElementParametersPanel() {
        initComponents();

        jTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null && value instanceof JRGenericElementParameter)
                {
                    label.setText( ((JRGenericElementParameter)value).getName() );
                }
                return label;
            }
        });
        
        javax.swing.DefaultListSelectionModel dlsm =  (javax.swing.DefaultListSelectionModel)this.jTable.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableSelectionValueChanged(e);
            }
        });
    }
    
    public void jTableSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTable.getSelectedRowCount() > 0) {
            this.jButtonModify.setEnabled(true);
            this.jButtonDelete.setEnabled(true);
        } else {
            this.jButtonModify.setEnabled(false);
            this.jButtonDelete.setEnabled(false);
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

        jLabelTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new org.jdesktop.swingx.JXTable();
        jPanel1 = new javax.swing.JPanel();
        jButtonAdd = new javax.swing.JButton();
        jButtonModify = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonCopyFromMaster = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jLabelTitle.setText(org.openide.util.NbBundle.getMessage(GenericElementParametersPanel.class, "GenericElementParametersPanel.jLabelTitle.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        add(jLabelTitle, gridBagConstraints);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(375, 275));

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Expression"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable.setOpaque(false);
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jScrollPane1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButtonAdd.setText(org.openide.util.NbBundle.getMessage(GenericElementParametersPanel.class, "GenericElementParametersPanel.jButtonAdd.text")); // NOI18N
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel1.add(jButtonAdd, gridBagConstraints);

        jButtonModify.setText(org.openide.util.NbBundle.getMessage(GenericElementParametersPanel.class, "GenericElementParametersPanel.jButtonModify.text")); // NOI18N
        jButtonModify.setEnabled(false);
        jButtonModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel1.add(jButtonModify, gridBagConstraints);

        jButtonDelete.setText(org.openide.util.NbBundle.getMessage(GenericElementParametersPanel.class, "GenericElementParametersPanel.jButtonDelete.text")); // NOI18N
        jButtonDelete.setEnabled(false);
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel1.add(jButtonDelete, gridBagConstraints);

        jButtonCopyFromMaster.setText(org.openide.util.NbBundle.getMessage(GenericElementParametersPanel.class, "GenericElementParametersPanel.jButtonCopyFromMaster.text")); // NOI18N
        jButtonCopyFromMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCopyFromMasterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jButtonCopyFromMaster, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings("unchecked")
    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed

        Object pWin = SwingUtilities.getWindowAncestor(this);
        GenericElementParameterDialog jrpd = null;
        if (pWin instanceof Dialog) jrpd = new GenericElementParameterDialog((Dialog)pWin, getParameters());
        else jrpd = new GenericElementParameterDialog((Frame)pWin, getParameters());
        
        jrpd.setExpressionContext( getExpressionContext() );
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            
            JRDesignGenericElementParameter parameter = jrpd.getParameter();
            parameters.add(parameter);
            
            DefaultTableModel model = (DefaultTableModel)jTable.getModel();
            model.addRow(new Object[]{parameter, Misc.getExpressionText(parameter.getValueExpression())});

        }
        
    }//GEN-LAST:event_jButtonAddActionPerformed

    @SuppressWarnings("unchecked")
    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        
        DefaultTableModel model = (DefaultTableModel)jTable.getModel();
        // Remove selected parameters...
        while (jTable.getSelectedRow() >= 0)
        {
            int row = jTable.getSelectedRow();
            row = ((JXTable)jTable).convertRowIndexToModel(row);
            parameters.remove( model.getValueAt(row, 0) );
            model.removeRow(row);
        }
        
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    @SuppressWarnings("unchecked")
    private void jButtonModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyActionPerformed
        
        DefaultTableModel model = (DefaultTableModel)jTable.getModel();
        
        int row = jTable.getSelectedRow();
        if (row < 0) return;
        
        row = ((JXTable)jTable).convertRowIndexToModel(row);

        JRDesignGenericElementParameter parameter = (JRDesignGenericElementParameter) model.getValueAt(row,0);
        int paramIndex = parameters.indexOf(parameter);
        
        GenericElementParameterDialog jrpd = null;
        Window pWin = SwingUtilities.getWindowAncestor(this);
        if (pWin instanceof Dialog) jrpd = new GenericElementParameterDialog((Dialog)pWin, getParameters());
        else if (pWin instanceof Frame) jrpd = new GenericElementParameterDialog((Frame)pWin, getParameters());
        else jrpd = new GenericElementParameterDialog((Dialog)null, getParameters());

        
        jrpd.setExpressionContext( getExpressionContext() );
        jrpd.setParameter(parameter);
        jrpd.setVisible(true);
            
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            parameter = jrpd.getParameter();
            parameters.remove(paramIndex);
            parameters.add(paramIndex, parameter);
            model.setValueAt(parameter, row, 0);
            model.setValueAt(Misc.getExpressionText( parameter.getValueExpression()), row, 1);
            
            jTable.updateUI();
        }
    }//GEN-LAST:event_jButtonModifyActionPerformed

    @SuppressWarnings("unchecked")
    private void jButtonCopyFromMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyFromMasterActionPerformed
        
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel)jTable.getModel();
        
        List reportParameters = IReportManager.getInstance().getActiveReport().getParametersList();
        
        for (int i=0; i<reportParameters.size(); ++i) {
            JRDesignParameter jrParameter = (JRDesignParameter)reportParameters.get(i);
            if (jrParameter.isSystemDefined()) continue;
            
            // Check if a similar parameter already exists...
            if (!findParameter(parameters, jrParameter.getName())) {
                JRDesignGenericElementParameter parameter = new JRDesignGenericElementParameter();
                parameter.setName(jrParameter.getName() );
                JRDesignExpression exp = new JRDesignExpression();
                exp.setText("$P{" + jrParameter.getName() + "}");
                exp.setValueClassName( jrParameter.getValueClassName() );
                parameter.setValueExpression(exp);
                parameters.add(parameter);
                model.addRow(new Object[]{parameter, Misc.getExpressionText(parameter.getValueExpression())});
            }
        }
        jTable.updateUI();
        
    }//GEN-LAST:event_jButtonCopyFromMasterActionPerformed

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked
        
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt))
        {
            if (jTable.getSelectedRowCount() > 0)
            {
                jButtonModifyActionPerformed(null);
            }
        }
        
    }//GEN-LAST:event_jTableMouseClicked
    
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonCopyFromMaster;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonModify;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    // End of variables declaration//GEN-END:variables

    private boolean findParameter(List parameters, String name) {
        for (int i=0; i<parameters.size(); ++i)
        {
            JRGenericElementParameter param = (JRGenericElementParameter)parameters.get(i);
            if (param.getName().equals(name)) return true;
        }
        return false;
    }
    
}
