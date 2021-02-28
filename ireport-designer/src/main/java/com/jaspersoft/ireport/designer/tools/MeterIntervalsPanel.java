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

import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.text.DecimalFormat;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import org.openide.util.Exceptions;

/**
 *
 * @author  gtoffoli
 */
public class MeterIntervalsPanel extends javax.swing.JPanel {
    
    private int dialogResult = javax.swing.JOptionPane.CANCEL_OPTION;
    private java.util.List meterIntervals = null;
    
    public static java.util.List clipboard = new java.util.ArrayList();
    public static java.util.List lastIntervals = new java.util.ArrayList();
    
    private ExpressionContext expressionContext = null;

    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    public void setExpressionContext(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
    }
    
    private JDialog dialog = null;
    
    private boolean init = false;

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
    
    
    
    /** Creates new form MeterIntervalsPanel */
    public MeterIntervalsPanel() {
        initComponents();
        
        meterIntervals = new java.util.ArrayList();
        
        jPanel1.setVisible(false);
        
        jButtonPasteInterval.setEnabled( clipboard.size() > 0 );
        jButtonUseLast.setEnabled( lastIntervals.size() > 0 );
        
        MeterIntervalTableCellRenderer mcr = new MeterIntervalTableCellRenderer();
        ((DefaultTableColumnModel)jTable1.getColumnModel()).getColumn(0).setCellRenderer(mcr);
        ((DefaultTableColumnModel)jTable1.getColumnModel()).getColumn(1).setPreferredWidth(50);
        ((DefaultTableColumnModel)jTable1.getColumnModel()).getColumn(2).setCellRenderer(mcr);
        ((DefaultTableColumnModel)jTable1.getColumnModel()).getColumn(3).setCellRenderer(mcr);
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment( SwingConstants.CENTER  );
        ((DefaultTableColumnModel)jTable1.getColumnModel()).getColumn(1).setCellRenderer(dtcr);
            
        jTable1.getSelectionModel().addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e) 
            {
                jTable1ValueChanged(e);
            }
        });
        
        jTable1.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                updateList();
            }
        });
        
    }
    
    /**
     * Refresh the list if the model changes. If init is true, the methods does nothing.
     */
    @SuppressWarnings("unchecked")
    private void updateList()
    {
        if (isInit()) return;
        
        java.util.List temp_list = new java.util.ArrayList();
        
        try {
            for (int i=0; i<jTable1.getRowCount(); ++i) {
                temp_list.add( ((JRMeterInterval)jTable1.getValueAt(i,0)).clone() );
            }
        } catch (Exception ex) { }
        
        this.meterIntervals = temp_list;
    }
    
    private void jTable1ValueChanged(javax.swing.event.ListSelectionEvent evt) {                                    

        
        if (jTable1.getSelectedRowCount() > 0)
        {
            jButtonDelete.setEnabled(true);
            jButtonModify.setEnabled(true);
            jButtonMoveUp.setEnabled(jTable1.getSelectedRow() > 0);
            jButtonMoveDown.setEnabled(jTable1.getSelectedRow() < jTable1.getRowCount()-1);
        }
        else
        {
            jButtonModify.setEnabled(false);
            jButtonDelete.setEnabled(false);
            jButtonMoveUp.setEnabled(false);
            jButtonMoveDown.setEnabled(false);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButtonAdd = new javax.swing.JButton();
        jButtonModify = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonMoveUp = new javax.swing.JButton();
        jButtonMoveDown = new javax.swing.JButton();
        jButtonCopyInterval = new javax.swing.JButton();
        jButtonPasteInterval = new javax.swing.JButton();
        jButtonUseLast = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Label", "Alpha", "Low exp", "High exp"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jScrollPane1, gridBagConstraints);

        jPanel2.setMinimumSize(new java.awt.Dimension(150, 150));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 283));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButtonAdd.setText(I18n.getString("Global.Button.Add")); // NOI18N
        jButtonAdd.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jButtonAdd, gridBagConstraints);

        jButtonModify.setText(I18n.getString("MeterIntervalsPanel.jButtonModify.text")); // NOI18N
        jButtonModify.setEnabled(false);
        jButtonModify.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jButtonModify, gridBagConstraints);

        jButtonDelete.setText(I18n.getString("Global.Button.Delete")); // NOI18N
        jButtonDelete.setEnabled(false);
        jButtonDelete.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jButtonDelete, gridBagConstraints);

        jButtonMoveUp.setText(I18n.getString("Global.Button.MoveUp")); // NOI18N
        jButtonMoveUp.setEnabled(false);
        jButtonMoveUp.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonMoveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMoveUpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jButtonMoveUp, gridBagConstraints);

        jButtonMoveDown.setText(I18n.getString("Global.Button.MoveDown")); // NOI18N
        jButtonMoveDown.setEnabled(false);
        jButtonMoveDown.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonMoveDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMoveDownActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 14, 4);
        jPanel2.add(jButtonMoveDown, gridBagConstraints);

        jButtonCopyInterval.setText(I18n.getString("MeterIntervalsPanel.jButtonCopyInterval.text")); // NOI18N
        jButtonCopyInterval.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonCopyInterval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCopyIntervalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jButtonCopyInterval, gridBagConstraints);

        jButtonPasteInterval.setText(I18n.getString("MeterIntervalsPanel.jButtonPasteInterval.text")); // NOI18N
        jButtonPasteInterval.setEnabled(false);
        jButtonPasteInterval.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonPasteInterval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPasteIntervalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jButtonPasteInterval, gridBagConstraints);

        jButtonUseLast.setText(I18n.getString("MeterIntervalsPanel.jButtonUseLast.text")); // NOI18N
        jButtonUseLast.setEnabled(false);
        jButtonUseLast.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonUseLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUseLastjButtonPasteIntervalActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jButtonUseLast, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        add(jPanel2, gridBagConstraints);

        jSeparator1.setMinimumSize(new java.awt.Dimension(0, 2));
        jSeparator1.setPreferredSize(new java.awt.Dimension(3, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jSeparator1, gridBagConstraints);

        jPanel1.setPreferredSize(new java.awt.Dimension(320, 23));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButtonOk.setText(I18n.getString("Global.Button.Ok")); // NOI18N
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel1.add(jButtonOk, gridBagConstraints);

        jButtonCancel.setText(I18n.getString("Global.Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonCancel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() == 2) {
            jButtonModifyActionPerformed(null);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        
        Window pWin = SwingUtilities.getWindowAncestor(this);
        MeterIntervalDialog cc = null;
        if (pWin instanceof Dialog) cc = new MeterIntervalDialog((Dialog)pWin, true);
        else if (pWin instanceof Frame) cc = new MeterIntervalDialog((Frame)pWin, true);
        else cc = new MeterIntervalDialog((Dialog)null, true);

        
        //MeterIntervalDialog cc = new MeterIntervalDialog(, true);
        cc.setExpressionContext(expressionContext);
        cc.setVisible(true);
        
        if (cc.getDialogResult() == JOptionPane.OK_OPTION) {
            addRowValues(cc.getMeterInterval());
        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyActionPerformed
        int index = jTable1.getSelectedRow();
        if (index >=0) {
            JRMeterInterval c = (JRMeterInterval)jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            
            
            MeterIntervalDialog cc = null;
            Window pWin = SwingUtilities.getWindowAncestor(this);
            if (pWin instanceof Dialog) cc = new MeterIntervalDialog((Dialog)pWin, true);
            else if (pWin instanceof Frame) cc = new MeterIntervalDialog((Frame)pWin, true);
            else cc = new MeterIntervalDialog((Dialog)null, true);

            
            cc.setExpressionContext(expressionContext);
            cc.setMeterInterval( c );
            
            /*
            if (newInfo != null) {
                cc.setFocusedExpression(newInfo);
            }
            */
            cc.setVisible(true);
            
            if (cc.getDialogResult() == JOptionPane.OK_OPTION) {
                setRowValues(c, index);
            }
        }
    }//GEN-LAST:event_jButtonModifyActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        int[] indexes = jTable1.getSelectedRows();
        
        for (int i=indexes.length-1;  i>=0; --i) {
            ((DefaultTableModel)jTable1.getModel()).removeRow(indexes[i]);
        }
        jTable1.updateUI();
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonMoveUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMoveUpActionPerformed
        if (jTable1.getSelectedRow() > 0) {
            DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
            int[] indices = jTable1.getSelectedRows();
            for (int i=indices.length-1; i>=0; --i) {
                if (indices[i] == 0) continue;
                
                Object val = jTable1.getValueAt( indices[i], 0);
                dtm.removeRow(indices[i]);
                dtm.insertRow(indices[i]-1, new Object[5]  );
                setRowValues( (JRMeterInterval)val, indices[i]-1);
                indices[i]--;
            }
            
            DefaultListSelectionModel dlsm = (DefaultListSelectionModel)jTable1.getSelectionModel();
            dlsm.setValueIsAdjusting(true);
            dlsm.clearSelection();
            for (int i=0; i<indices.length; ++i) {
                dlsm.addSelectionInterval(indices[i],  indices[i]);
            }
            dlsm.setValueIsAdjusting( false );
        }
    }//GEN-LAST:event_jButtonMoveUpActionPerformed

    private void jButtonMoveDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMoveDownActionPerformed
        if (jTable1.getSelectedRowCount() > 0) {
            DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
            int[] indices = jTable1.getSelectedRows();
            for (int i=indices.length-1; i>=0; --i) {
                if (indices[i] >= (jTable1.getRowCount() -1)) continue;
                
                Object val = jTable1.getValueAt( indices[i], 0);
                dtm.removeRow(indices[i]);
                dtm.insertRow(indices[i]+1, new Object[5]  );
                setRowValues( (JRMeterInterval)val, indices[i]+1);
                indices[i]++;
            }
            
            DefaultListSelectionModel dlsm = (DefaultListSelectionModel)jTable1.getSelectionModel();
            dlsm.setValueIsAdjusting(true);
            dlsm.clearSelection();
            for (int i=0; i<indices.length; ++i) {
                dlsm.addSelectionInterval(indices[i],  indices[i]);
            }
            dlsm.setValueIsAdjusting( false );
        }
    }//GEN-LAST:event_jButtonMoveDownActionPerformed

    @SuppressWarnings("unchecked")
    private void jButtonCopyIntervalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyIntervalActionPerformed
        if (jTable1.getRowCount() > 0) {
            clipboard.clear();
            for (int i=0; i<jTable1.getRowCount(); ++i) {
                clipboard.add(((JRMeterInterval) jTable1.getValueAt(i,0)).clone());
            }
        }
        
        jButtonPasteInterval.setEnabled(true);
    }//GEN-LAST:event_jButtonCopyIntervalActionPerformed

    private void jButtonPasteIntervalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPasteIntervalActionPerformed
        if (clipboard.size() > 0) {
            for (int i=0; i<clipboard.size(); ++i) {
                addRowValues((JRMeterInterval) ((JRMeterInterval)clipboard.get(i)).clone());
            }
        }
    }//GEN-LAST:event_jButtonPasteIntervalActionPerformed

    private void jButtonUseLastjButtonPasteIntervalActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUseLastjButtonPasteIntervalActionPerformed1
        if (lastIntervals.size() > 0) {
            for (int i=0; i<lastIntervals.size(); ++i) {
                addRowValues((JRMeterInterval) ((JRMeterInterval)lastIntervals.get(i)).clone());
            }
        }
    }//GEN-LAST:event_jButtonUseLastjButtonPasteIntervalActionPerformed1

    @SuppressWarnings("unchecked")
    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        
        setDialogResult(JOptionPane.OK_OPTION);
        if (dialog != null)
        {
            dialog.setVisible(false);
            dialog.dispose();
        }
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        
        setDialogResult( JOptionPane.CANCEL_OPTION);
        if (dialog != null)
        {
            dialog.setVisible(false);
            dialog.dispose();
        }
    }//GEN-LAST:event_jButtonCancelActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonCopyInterval;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonModify;
    private javax.swing.JButton jButtonMoveDown;
    private javax.swing.JButton jButtonMoveUp;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonPasteInterval;
    private javax.swing.JButton jButtonUseLast;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    
    
    public int showDialog(Component parent)
     {
        Object pWin = SwingUtilities.windowForComponent(parent);
        if (pWin instanceof Frame) dialog = new JDialog((Frame)pWin);
        else if (pWin instanceof Dialog) dialog = new JDialog((Dialog)pWin);
        else dialog = new JDialog();

        dialog.setLocationRelativeTo(null);
        dialog.getRootPane().setLayout(new BorderLayout());
        dialog.getRootPane().add( this, BorderLayout.CENTER );
                
        dialog.pack();
        jPanel1.setVisible(true);
        
        dialog.setVisible(true);
        
        return getDialogResult();
     }
    
    
    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }


    public void setRowValues(JRMeterInterval mi, int row)
    {
        DecimalFormat dnf = new DecimalFormat("0.00");
        jTable1.setValueAt(mi, row, 0);
        
        jTable1.setValueAt(mi.getAlphaDouble() == null ? null : dnf.format(mi.getAlphaDouble()) , row, 1);
        jTable1.setValueAt(mi.getDataRange().getLowExpression(), row, 2);
        jTable1.setValueAt(mi.getDataRange().getHighExpression(), row, 3);
        jTable1.updateUI();
    }
    
    public void addRowValues(JRMeterInterval mi)
    {
        DecimalFormat dnf = new DecimalFormat("0.00");
        DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
        dtm.addRow(new Object[]{mi, mi.getAlphaDouble() == null ? null : dnf.format(mi.getAlphaDouble()), mi.getDataRange().getLowExpression(), mi.getDataRange().getHighExpression()} );
        jTable1.updateUI();
    }

    @SuppressWarnings("unchecked")
    public java.util.List getMeterIntervals() {
        
        // We assume that this method is called when the list is accepted...
        lastIntervals.clear();
        
        try {
            for (int i=0; i<jTable1.getRowCount(); ++i) {
                lastIntervals.add( ((JRMeterInterval)jTable1.getValueAt(i,0)).clone() );
            }
        } catch (Exception ex) { }
        
        
        return meterIntervals;
    }

    public void setMeterIntervals(java.util.List meterIntervals) {
        
        if (meterIntervals == null) return;

        this.meterIntervals.clear();
        
        DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
        dtm.setRowCount(0);
        
        for (int i=0; i<meterIntervals.size(); ++i)
        {
            try {
                addRowValues( (JRMeterInterval)((JRMeterInterval)meterIntervals.get(i)).clone() );
            } catch (Exception ex) { }
        }
        
        jTable1.updateUI();
    }
    
    /*
     public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonAdd.setText(I18n.getString("meterIntervalsDialog.buttonAdd","Add"));
                jButtonCancel.setText(I18n.getString("meterIntervalsDialog.buttonCancel","Cancel"));
                jButtonCopyInterval.setText(I18n.getString("meterIntervalsDialog.buttonCopyInterval","Copy intervals"));
                jButtonDelete.setText(I18n.getString("meterIntervalsDialog.buttonDelete","Delete"));
                jButtonModify.setText(I18n.getString("meterIntervalsDialog.buttonModify","Modify"));
                jButtonMoveDown.setText(I18n.getString("meterIntervalsDialog.buttonMoveDown","Move down"));
                jButtonMoveUp.setText(I18n.getString("meterIntervalsDialog.buttonMoveUp","Move up"));
                jButtonOk.setText(I18n.getString("meterIntervalsDialog.buttonOk","OK"));
                jButtonPasteInterval.setText(I18n.getString("meterIntervalsDialog.buttonPasteInterval","Paste intervals"));
                jButtonUseLast.setText(I18n.getString("meterIntervalsDialog.buttonUseLast","Use last"));
                // End autogenerated code ----------------------
                
                jTable1.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("meterIntervalsDialog.tablecolumn.label","Label") );
                jTable1.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("meterIntervalsDialog.tablecolumn.alpha","Alpha") );
                jTable1.getColumnModel().getColumn(2).setHeaderValue( I18n.getString("meterIntervalsDialog.tablecolumn.lowExp","Low exp") );
                jTable1.getColumnModel().getColumn(3).setHeaderValue( I18n.getString("meterIntervalsDialog.tablecolumn.highExp","High exp") );
                
                jButtonCancel.setMnemonic(I18n.getString("meterIntervalsDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOk.setMnemonic(I18n.getString("meterIntervalsDialog.buttonOkMnemonic","o").charAt(0));
    }
    */
}
