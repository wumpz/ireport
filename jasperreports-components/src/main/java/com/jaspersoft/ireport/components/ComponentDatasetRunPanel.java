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
package com.jaspersoft.ireport.components;

import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.tools.DatasetParametersTableCellRenderer;
import com.jaspersoft.ireport.designer.tools.JRDatasetParameterDialog;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableColumnModel;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class ComponentDatasetRunPanel extends javax.swing.JPanel {

    private JRDesignDatasetRun datasetRun = null;
    private JasperDesign jasperDesign = null;
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    private JDialog dialog = null;

    private boolean init = false;

    /** Creates new form ListDatasetRunPanel */
    public ComponentDatasetRunPanel() {
        initComponents();

        jComboBoxDatasetConnectionType.addItem(new Tag(I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.noConnectionNoDatasource"),I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.noConnectionNoDatasource")));
        jComboBoxDatasetConnectionType.addItem(new Tag(I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.connExpression"),I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.connExpression")));
        jComboBoxDatasetConnectionType.addItem(new Tag(I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.datasourceExpr"),I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.datasourceExpr")));

        javax.swing.DefaultListSelectionModel dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableDatasetParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableDatasetParametersListSelectionValueChanged(e);
            }
        });

        
        jButtonResetButton.setVisible(false);


        DatasetParametersTableCellRenderer dpcr = new DatasetParametersTableCellRenderer();
        ((DefaultTableColumnModel)jTableDatasetParameters.getColumnModel()).getColumn(0).setCellRenderer(dpcr);
        ((DefaultTableColumnModel)jTableDatasetParameters.getColumnModel()).getColumn(1).setCellRenderer(dpcr);


        this.jRTextExpressionAreaMapExpression.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
        });


        this.jRTextExpressionAreaTextConnectionExpression.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
        });
    }


    public void setShowRemoveDatasetRun(boolean b)
    {
        jButtonResetButton.setVisible(b);
        this.updateUI();
    }
    
    public boolean getShowRemoveDatasetRun()
    {
        return jButtonResetButton.isVisible();
    }

    public void jRTextExpressionAreaMapExpressionTextChanged() {
        if (this.isInit()) return;
        if (datasetRun != null)
        {
            JRDesignExpression exp = null;
            if (jRTextExpressionAreaMapExpression.getText().trim().length() > 0)
            {
                exp = new JRDesignExpression();
                exp.setValueClassName("java.util.Map");//NOI18N
                exp.setText(jRTextExpressionAreaMapExpression.getText());
            }

            datasetRun.setParametersMapExpression(exp);
        }
    }


    public void jRTextExpressionAreaTextConnectionExpressionTextChanged() {
        if (this.isInit()) return;
        if (datasetRun != null)
        {
            JRDesignExpression exp = null;
            if (jRTextExpressionAreaTextConnectionExpression.getText().trim().length() > 0)
            {
                exp = new JRDesignExpression();
                exp.setText(jRTextExpressionAreaTextConnectionExpression.getText());
            }

            int index = jComboBoxDatasetConnectionType.getSelectedIndex();

            if (index == 1)
            {
                if (exp != null) exp.setValueClassName("java.sql.Connection");//NOI18N
                datasetRun.setConnectionExpression(exp);
            }
            else if (index == 2)
            {
                if (exp != null) exp.setValueClassName("net.sf.jasperreports.engine.JRDataSource");//NOI18N
                datasetRun.setDataSourceExpression(exp);
            }
         }
    }

    public void jTableDatasetParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableDatasetParameters.getSelectedRowCount() > 0) {
            this.jButtonModParameter.setEnabled(true);
            this.jButtonRemParameter.setEnabled(true);
        }
        else {
            this.jButtonModParameter.setEnabled(false);
            this.jButtonRemParameter.setEnabled(false);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabelIncrementType1 = new javax.swing.JLabel();
        jComboBoxSubDataset = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPaneSubDataset = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jComboBoxDatasetConnectionType = new javax.swing.JComboBox();
        jRTextExpressionAreaTextConnectionExpression = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanel4 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDatasetParameters = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jButtonAddParameter = new javax.swing.JButton();
        jButtonModParameter = new javax.swing.JButton();
        jButtonRemParameter = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jRTextExpressionAreaMapExpression = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanel2 = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonResetButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jPanel1.border.title"))); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(329, 192));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelIncrementType1.setText(org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jLabelIncrementType1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel1.add(jLabelIncrementType1, gridBagConstraints);

        jComboBoxSubDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSubDatasetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jComboBoxSubDataset, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel41.setText(org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jLabel41.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel6.add(jLabel41, gridBagConstraints);

        jComboBoxDatasetConnectionType.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxDatasetConnectionType.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxDatasetConnectionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDatasetConnectionTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanel6.add(jComboBoxDatasetConnectionType, gridBagConstraints);

        jRTextExpressionAreaTextConnectionExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
        jRTextExpressionAreaTextConnectionExpression.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaTextConnectionExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanel6.add(jRTextExpressionAreaTextConnectionExpression, gridBagConstraints);

        jTabbedPaneSubDataset.addTab(org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel16.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setMinimumSize(new java.awt.Dimension(300, 50));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 50));

        jTableDatasetParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parameter", "Expression"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDatasetParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDatasetParametersMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableDatasetParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel16.add(jScrollPane2, gridBagConstraints);

        jPanel10.setMinimumSize(new java.awt.Dimension(100, 10));
        jPanel10.setPreferredSize(new java.awt.Dimension(100, 69));
        jPanel10.setLayout(new java.awt.GridBagLayout());

        jButtonAddParameter.setText(org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jButtonAddParameter.text")); // NOI18N
        jButtonAddParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel10.add(jButtonAddParameter, gridBagConstraints);

        jButtonModParameter.setText(org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jButtonModParameter.text")); // NOI18N
        jButtonModParameter.setEnabled(false);
        jButtonModParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel10.add(jButtonModParameter, gridBagConstraints);

        jButtonRemParameter.setText(org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jButtonRemParameter.text")); // NOI18N
        jButtonRemParameter.setEnabled(false);
        jButtonRemParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel10.add(jButtonRemParameter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel16.add(jPanel10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jPanel16, gridBagConstraints);

        jTabbedPaneSubDataset.addTab(org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText(org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jLabel26.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 0, 0);
        jPanel5.add(jLabel26, gridBagConstraints);

        jRTextExpressionAreaMapExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaMapExpression.setMinimumSize(new java.awt.Dimension(0, 0));
        jRTextExpressionAreaMapExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanel5.add(jRTextExpressionAreaMapExpression, gridBagConstraints);

        jTabbedPaneSubDataset.addTab(org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel7.add(jTabbedPaneSubDataset, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 61;
        gridBagConstraints.ipady = 46;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jPanel1, gridBagConstraints);

        jButtonOk.setText(org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jButtonOk.text")); // NOI18N
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        jButtonCancel.setText(org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jButtonCancel.text")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonResetButton.setText(org.openide.util.NbBundle.getMessage(ComponentDatasetRunPanel.class, "ComponentDatasetRunPanel.jButtonResetButton.text")); // NOI18N
        jButtonResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButtonResetButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jButtonOk)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonCancel))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(8, 8, 8)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonCancel)
                    .add(jButtonOk)
                    .add(jButtonResetButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    private void jComboBoxSubDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSubDatasetActionPerformed

        if (this.isInit() || datasetRun == null) return;
         // Check subdataset parameters....
        if (datasetRun == null ||
                !("" + jComboBoxSubDataset.getSelectedItem()).equals(datasetRun.getDatasetName()) )//NOI18N
        {
            if (datasetRun == null) {
                datasetRun = new JRDesignDatasetRun();

                setInit(true);
                this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                this.jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
                jRTextExpressionAreaMapExpression.setText("");//NOI18N
                setInit(false);

                java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridy = 1;
                gridBagConstraints.gridwidth = 2;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
                jPanel7.add(jTabbedPaneSubDataset, gridBagConstraints);

                //jTabbedPaneSubDataset.setVisible(true);
                jPanel7.updateUI();
            }
            datasetRun.setDatasetName("" + jComboBoxSubDataset.getSelectedItem());//NOI18N
        }
}//GEN-LAST:event_jComboBoxSubDatasetActionPerformed

    private void jTableDatasetParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDatasetParametersMouseClicked

        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            if (jTableDatasetParameters.getSelectedRowCount() > 0) {
                jButtonModParameterActionPerformed(null);
            }
        }
    }//GEN-LAST:event_jTableDatasetParametersMouseClicked

    private void jButtonAddParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddParameterActionPerformed
        if (this.isInit() || datasetRun == null) return;

        // Set the new value for all selected elements...
        java.util.HashMap map = new java.util.HashMap();
        java.util.List<JRDatasetParameter> params = Arrays.asList(datasetRun.getParameters());
        for (JRDatasetParameter p : params) {
            map.put(p.getName(), p);
        }

        Object pWin = SwingUtilities.windowForComponent(this);
        JRDatasetParameterDialog jrpd = null;
        if (pWin instanceof Dialog) jrpd = new JRDatasetParameterDialog((Dialog)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(datasetRun.getDatasetName()));
        else jrpd = new JRDatasetParameterDialog((Frame)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(datasetRun.getDatasetName()));

        ExpressionContext docEc = new ExpressionContext( getJasperDesign().getMainDesignDataset() );
        jrpd.setExpressionContext(docEc);
        jrpd.setVisible(true);

        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            JRDesignDatasetParameter parameter = jrpd.getParameter();
            try {
                datasetRun.addParameter( parameter );
                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                dtm.addRow(new Object[]{parameter, parameter.getExpression()});
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
}//GEN-LAST:event_jButtonAddParameterActionPerformed

    private void jButtonModParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModParameterActionPerformed

        if (this.isInit() || datasetRun == null) return;

        int rowNumber = jTableDatasetParameters.getSelectedRow();
        JRDesignDatasetParameter parameter = (JRDesignDatasetParameter)jTableDatasetParameters.getValueAt(rowNumber, 0);

        java.util.HashMap map = new java.util.HashMap();
        java.util.List<JRDatasetParameter> params = Arrays.asList(datasetRun.getParameters());
        for (JRDatasetParameter p : params) {
            map.put(p.getName(), p);
        }

        Object pWin = SwingUtilities.windowForComponent(this);
        JRDatasetParameterDialog jrpd = null;
        if (pWin instanceof Dialog) jrpd = new JRDatasetParameterDialog((Dialog)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(datasetRun.getDatasetName()) );
        else jrpd = new JRDatasetParameterDialog((Frame)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(datasetRun.getDatasetName()));

        ExpressionContext docEc = new ExpressionContext( getJasperDesign().getMainDesignDataset() );
        jrpd.setExpressionContext(docEc);

        jrpd.setParameter( parameter );

        /*
        if (subdatasetParameterHighlightExpression != null)
        {
            jrpd.setFocusedExpression( ((Integer)subdatasetParameterHighlightExpression[0]).intValue() );
        }
         */
        jrpd.setVisible(true);

        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            parameter.setName( jrpd.getParameter().getName() );
            parameter.setExpression( jrpd.getParameter().getExpression());
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
            dtm.setValueAt(parameter, rowNumber, 0);
            dtm.setValueAt(parameter.getExpression(), rowNumber, 1);
            jTableDatasetParameters.updateUI();

            // Print all the parameters in the dataset...
            params = Arrays.asList(datasetRun.getParameters());
            for (JRDatasetParameter p : params) {
                System.out.println(p.getName() + " = "  + Misc.getExpressionText( p.getExpression() ) + "  " + parameter + "  " + p + " " + (p==parameter) );
            }
            System.out.flush();
        }
    }//GEN-LAST:event_jButtonModParameterActionPerformed

    private void jButtonRemParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemParameterActionPerformed
        if (this.isInit() || datasetRun == null) return;

        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();

        while (jTableDatasetParameters.getSelectedRowCount() > 0) {
            int i=jTableDatasetParameters.getSelectedRow();
            datasetRun.removeParameter( ((JRDatasetParameter)jTableDatasetParameters.getValueAt( i, 0)).getName() );
            dtm.removeRow(i);
        }
    }//GEN-LAST:event_jButtonRemParameterActionPerformed

    private void jComboBoxDatasetConnectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDatasetConnectionTypeActionPerformed
        if (isInit() || datasetRun == null) return;

        if (jComboBoxDatasetConnectionType.getSelectedIndex() == 0) {
            jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
            jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
            datasetRun.setConnectionExpression(null);
            datasetRun.setDataSourceExpression(null);
        } else if (jComboBoxDatasetConnectionType.getSelectedIndex() == 1) {

            jRTextExpressionAreaTextConnectionExpression.setText("$P{REPORT_CONNECTION}");//NOI18N
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);

            datasetRun.setDataSourceExpression(null);

            JRDesignExpression exp = new JRDesignExpression();
            exp.setValueClassName("java.sql.Connection");//NOI18N
            exp.setText("$P{REPORT_CONNECTION}");//NOI18N
            datasetRun.setConnectionExpression(exp);

        } else if (jComboBoxDatasetConnectionType.getSelectedIndex() == 2) {

            jRTextExpressionAreaTextConnectionExpression.setText("new net.sf.jasperreports.engine.JREmptyDataSource(1)");//NOI18N
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
            datasetRun.setConnectionExpression(null);

            JRDesignExpression exp = new JRDesignExpression();
            exp.setValueClassName("net.sf.jasperreports.engine.JRDataSource");//NOI18N
            exp.setText("new net.sf.jasperreports.engine.JREmptyDataSource(1)");//NOI18N
            datasetRun.setDataSourceExpression(exp);
        }

}//GEN-LAST:event_jComboBoxDatasetConnectionTypeActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed

        this.setDialogResult(JOptionPane.CANCEL_OPTION);
        dialog.setVisible(false);
        dialog.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        this.setDialogResult(JOptionPane.OK_OPTION);
        dialog.setVisible(false);
        dialog.dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetButtonActionPerformed
        this.setDialogResult(JOptionPane.NO_OPTION);
        dialog.setVisible(false);
        dialog.dispose();
    }//GEN-LAST:event_jButtonResetButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddParameter;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonModParameter;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonRemParameter;
    private javax.swing.JButton jButtonResetButton;
    private javax.swing.JComboBox jComboBoxDatasetConnectionType;
    private javax.swing.JComboBox jComboBoxSubDataset;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabelIncrementType1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaMapExpression;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaTextConnectionExpression;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPaneSubDataset;
    private javax.swing.JTable jTableDatasetParameters;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the datasetRun
     */
    public JRDesignDatasetRun getDatasetRun() {
        return datasetRun;
    }

    /**
     * @param datasetRun the datasetRun to set
     */
    public void setDatasetRun(JRDesignDatasetRun datasetRunOriginal) {

        setInit(true);

        this.datasetRun = (JRDesignDatasetRun) datasetRunOriginal.clone();
        jComboBoxSubDataset.setSelectedItem(datasetRun.getDatasetName());
        //jPanel7.remove(jTabbedPaneSubDataset);
        //jTabbedPaneSubDataset.setVisible(true);
        if (jPanel7.getComponentCount() == 0)
        {
            java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
            jPanel7.add(jTabbedPaneSubDataset, gridBagConstraints);
            jPanel7.updateUI();
        }

        jRTextExpressionAreaMapExpression.setText( Misc.getExpressionText( datasetRun.getParametersMapExpression() ) );

        int connectionType = 0;

        if ( datasetRun.getConnectionExpression() != null)
        {
            connectionType = 1;
        }
        if ( datasetRun.getDataSourceExpression() != null)
        {
            connectionType = 2;
        }


        if (connectionType == 0) {
            this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
            this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
            this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
            this.jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
        }
        else if (connectionType == 1) {
            this.jComboBoxDatasetConnectionType.setSelectedIndex(1);
            this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
            this.jRTextExpressionAreaTextConnectionExpression.setText( Misc.getExpressionText( datasetRun.getConnectionExpression() ));
        }
        else {
            this.jComboBoxDatasetConnectionType.setSelectedIndex(2);
            this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
            this.jRTextExpressionAreaTextConnectionExpression.setText( Misc.getExpressionText( datasetRun.getDataSourceExpression()) );
        }

        //Add parameters...
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
        dtm.setRowCount(0);

        JRDatasetParameter[] params = datasetRun.getParameters();
        for (int i=0; i<params.length; ++i) {
            JRDatasetParameter parameter = params[i];
            Vector row = new Vector();
            row.addElement(parameter);
            row.addElement( Misc.getExpressionText( parameter.getExpression() ) );
            dtm.addRow(row);
        }
        // Set expression context...
        ExpressionContext docEc = new ExpressionContext(getJasperDesign().getMainDesignDataset() );
        jRTextExpressionAreaMapExpression.setExpressionContext(docEc);
        jRTextExpressionAreaTextConnectionExpression.setExpressionContext(docEc);

        setInit(false);
    }

    /**
     * @return the init
     */
    public boolean isInit() {
        return init;
    }

    /**
     * @param init the init to set
     */
    public void setInit(boolean init) {
        this.init = init;
    }

    /**
     * @return the jasperDesign
     */
    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    /**
     * @param jasperDesign the jasperDesign to set
     */
    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;

        List<String> datasetNames = new ArrayList<String>();
        for (int i=0; i<getJasperDesign().getDatasetsList().size(); ++i)
        {
            datasetNames.add( ((JRDataset)getJasperDesign().getDatasetsList().get(i)).getName());
        }

        Misc.updateComboBox(jComboBoxSubDataset, datasetNames, false);

    }

    public int showDialog(Frame frame, boolean modal)
    {
         dialog = new JDialog(frame, modal );
         return showDialog();
    }

    public int showDialog(JDialog dialog, boolean modal)
    {
        dialog = new JDialog(dialog, modal );
        return showDialog();
    }

    public int showDialog(JComponent component, boolean modal)
    {
        Object obj = null;
        if (component != null && (obj = SwingUtilities.getWindowAncestor(component)) != null)
        {
            if (obj instanceof Frame) dialog = new JDialog((Frame)obj, modal );
            else if (obj instanceof Dialog) dialog = new JDialog((Dialog)obj, modal );
        }
        if (dialog == null)
        {
            dialog = new JDialog(Misc.getMainFrame(), modal);
        }

        return showDialog();
    }


    private int showDialog()
    {
        if (dialog == null) return JOptionPane.CANCEL_OPTION;
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(this, BorderLayout.CENTER);
        setDialogResult(JOptionPane.CANCEL_OPTION);
        dialog.setTitle("Dataset Run");
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setMinimumSize( dialog.getSize() );
        dialog.setMaximumSize( dialog.getSize());
        dialog.setResizable(true);
        dialog.setVisible(dialog.isModal());

        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };
        dialog.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        dialog.getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);

        //to make the default button ...
        dialog.getRootPane().setDefaultButton(jButtonCancel);

        return getDialogResult();
    }

    /**
     * @return the dialogResult
     */
    public int getDialogResult() {
        return dialogResult;
    }

    /**
     * @param dialogResult the dialogResult to set
     */
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

}
