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
package com.jaspersoft.ireport.designer.options.export;

import com.jaspersoft.ireport.designer.IRLocalJasperReportsContext;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import java.util.prefs.Preferences;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;

/**
 *
 * @author gtoffoli
 */
public class XlsExportParametersPanel extends AbstractExportParametersPanel {

    /** Creates new form XlsExportParametersPanel */
    public XlsExportParametersPanel() {
        initComponents();
        SpinnerNumberModel snm = new SpinnerNumberModel(0,0, Integer.MAX_VALUE,1);
        jSpinnerMaximumRowsPerSheet.setModel(snm);

        jSpinnerFreezeRow.setModel(new SpinnerNumberModel(0,0, Integer.MAX_VALUE,1));

        //jComboBoxColumnEdge.setModel(new DefaultComboBoxModel(new Tag[]{new Tag(null,""),new Tag("Left","Left"), new Tag("Right","Right")  }));
        //jComboBoxRowEdge.setModel(new DefaultComboBoxModel(new Tag[]{new Tag(null,""),new Tag("Top","Top"), new Tag("Bottom","Bottom")  }));
        snm.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                notifyChange();
            }
        });

        applyI18n();
    }

    public void applyI18n()
    {
        jLabelTitle.setText(I18n.getString("XlsExportParametersPanel.jLabelTitle.text")); // NOI18N
        jCheckBoxOnePagePerSheet.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxOnePagePerSheet.text")); // NOI18N
        jCheckBoxRemoveEmptySpaceBetweenRows.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxRemoveEmptySpaceBetweenRows.text")); // NOI18N
        jCheckBoxRemoveEmptySpaceBetweenColumns.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxRemoveEmptySpaceBetweenColumns.text")); // NOI18N
        jCheckBoxWhitePageBackground.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxWhitePageBackground.text")); // NOI18N
        jCheckBoxAutoDetectCellType.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxAutoDetectCellType.text")); // NOI18N
        jCheckBoxFontSizeFixEnabled.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxFontSizeFixEnabled.text")); // NOI18N
        jCheckBoxImageBorderFixEnabled.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxImageBorderFixEnabled.text")); // NOI18N
        jLabelMaximumRowsPerSheet.setText(I18n.getString("XlsExportParametersPanel.jLabelMaximumRowsPerSheet.text")); // NOI18N
        jCheckBoxIgnoreGraphics.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxIgnoreGraphics.text")); // NOI18N
        jCheckBoxCollapseRowSpan.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxCollapseRowSpan.text")); // NOI18N
        jCheckBoxIgnoreCellBorder.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxIgnoreCellBorder.text")); // NOI18N
        jTabbedPane1.setTitleAt(0, I18n.getString("XlsExportParametersPanel.jPanel1.TabConstraints.tabTitle")); // NOI18N
        jCheckBoxUseSheetNames.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxUseSheetNames.text")); // NOI18N
        jLabelList.setText(I18n.getString("XlsExportParametersPanel.jLabelList.text")); // NOI18N
        jTabbedPane1.setTitleAt(1, I18n.getString("XlsExportParametersPanel.jPanel2.TabConstraints.tabTitle")); // NOI18N
        jCheckBoxCreateCustomPalette.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxCreateCustomPalette.text")); // NOI18N
        jLabelPassword.setText(I18n.getString("XlsExportParametersPanel.jLabelPassword.text")); // NOI18N
        jTabbedPane1.setTitleAt(2, I18n.getString("XlsExportParametersPanel.jPanel3.TabConstraints.tabTitle")); // NOI18N
        jCheckBoxIgnoreCellBackground.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxIgnoreCellBackground.text")); // NOI18N
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTitle = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jCheckBoxOnePagePerSheet = new javax.swing.JCheckBox();
        jCheckBoxRemoveEmptySpaceBetweenRows = new javax.swing.JCheckBox();
        jCheckBoxRemoveEmptySpaceBetweenColumns = new javax.swing.JCheckBox();
        jCheckBoxWhitePageBackground = new javax.swing.JCheckBox();
        jCheckBoxAutoDetectCellType = new javax.swing.JCheckBox();
        jCheckBoxFontSizeFixEnabled = new javax.swing.JCheckBox();
        jCheckBoxImageBorderFixEnabled = new javax.swing.JCheckBox();
        jLabelMaximumRowsPerSheet = new javax.swing.JLabel();
        jSpinnerMaximumRowsPerSheet = new javax.swing.JSpinner();
        jCheckBoxIgnoreGraphics = new javax.swing.JCheckBox();
        jCheckBoxCollapseRowSpan = new javax.swing.JCheckBox();
        jCheckBoxIgnoreCellBorder = new javax.swing.JCheckBox();
        jCheckBoxIgnoreCellBackground = new javax.swing.JCheckBox();
        jSpinnerFreezeRow = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldColumn = new javax.swing.JTextField();
        jCheckBoxFixFontSize = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxUseSheetNames = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabelList = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jCheckBoxCreateCustomPalette = new javax.swing.JCheckBox();
        jLabelPassword = new javax.swing.JLabel();
        jTextFieldPassword = new javax.swing.JTextField();

        jLabelTitle.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabelTitle.setText("CSV Export parameters");

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jCheckBoxOnePagePerSheet.setText("One Page per Sheet");
        jCheckBoxOnePagePerSheet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxOnePagePerSheetActionPerformed(evt);
            }
        });

        jCheckBoxRemoveEmptySpaceBetweenRows.setText("Remove Empty Space Between Rows");
        jCheckBoxRemoveEmptySpaceBetweenRows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxRemoveEmptySpaceBetweenRowsActionPerformed(evt);
            }
        });

        jCheckBoxRemoveEmptySpaceBetweenColumns.setText("Remove Empty Space Between Columns");
        jCheckBoxRemoveEmptySpaceBetweenColumns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxRemoveEmptySpaceBetweenColumnsActionPerformed(evt);
            }
        });

        jCheckBoxWhitePageBackground.setText("White Page Background");
        jCheckBoxWhitePageBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxWhitePageBackgroundActionPerformed(evt);
            }
        });

        jCheckBoxAutoDetectCellType.setText("Detect Cell Type");
        jCheckBoxAutoDetectCellType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAutoDetectCellTypeActionPerformed(evt);
            }
        });

        jCheckBoxFontSizeFixEnabled.setText("Font Size Fix Enabled");
        jCheckBoxFontSizeFixEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFontSizeFixEnabledActionPerformed(evt);
            }
        });

        jCheckBoxImageBorderFixEnabled.setText("Image Border Fix Enabled");
        jCheckBoxImageBorderFixEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxImageBorderFixEnabledActionPerformed(evt);
            }
        });

        jLabelMaximumRowsPerSheet.setText("Maximum Rows Per Sheet (0 means no maximum)");

        jCheckBoxIgnoreGraphics.setText("Ignore Graphics");
        jCheckBoxIgnoreGraphics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxIgnoreGraphicsActionPerformed(evt);
            }
        });

        jCheckBoxCollapseRowSpan.setText("Collapse Row Span");
        jCheckBoxCollapseRowSpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCollapseRowSpanActionPerformed(evt);
            }
        });

        jCheckBoxIgnoreCellBorder.setText("Ignore Cell Border");
        jCheckBoxIgnoreCellBorder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxIgnoreCellBorderActionPerformed(evt);
            }
        });

        jCheckBoxIgnoreCellBackground.setText("Ignore Cell Background");
        jCheckBoxIgnoreCellBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxIgnoreCellBackgroundActionPerformed(evt);
            }
        });

        jSpinnerFreezeRow.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerFreezeRowStateChanged(evt);
            }
        });

        jLabel1.setText("Freeze pane on column (i.e. A or AB)");

        jLabel2.setText("Freeze pane on row");

        jCheckBoxFixFontSize.setText("Decrease font size so that texts fit into the specified cell height");
        jCheckBoxFixFontSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFixFontSizeActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxIgnoreCellBackground)
                    .add(jCheckBoxOnePagePerSheet)
                    .add(jCheckBoxRemoveEmptySpaceBetweenRows)
                    .add(jCheckBoxRemoveEmptySpaceBetweenColumns)
                    .add(jCheckBoxWhitePageBackground)
                    .add(jCheckBoxAutoDetectCellType)
                    .add(jCheckBoxFontSizeFixEnabled)
                    .add(jCheckBoxImageBorderFixEnabled)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabelMaximumRowsPerSheet)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinnerMaximumRowsPerSheet, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jCheckBoxIgnoreGraphics)
                    .add(jCheckBoxCollapseRowSpan)
                    .add(jCheckBoxIgnoreCellBorder)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(jPanel1Layout.createSequentialGroup()
                            .add(jLabel1)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jTextFieldColumn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(jPanel1Layout.createSequentialGroup()
                            .add(jLabel2)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jSpinnerFreezeRow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jCheckBoxFixFontSize))
                .add(0, 4, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBoxOnePagePerSheet)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxRemoveEmptySpaceBetweenRows)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxRemoveEmptySpaceBetweenColumns)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxWhitePageBackground)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxAutoDetectCellType)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxFontSizeFixEnabled)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxImageBorderFixEnabled)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelMaximumRowsPerSheet)
                    .add(jSpinnerMaximumRowsPerSheet, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxIgnoreGraphics)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxCollapseRowSpan)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxIgnoreCellBorder)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxIgnoreCellBackground)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jTextFieldColumn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jSpinnerFreezeRow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(9, 9, 9)
                .add(jCheckBoxFixFontSize)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel1);

        jTabbedPane1.addTab("Common", jScrollPane2);

        jCheckBoxUseSheetNames.setText("Use Sheet Names");
        jCheckBoxUseSheetNames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUseSheetNamesActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabelList.setText("Sheet names. Each row corresponds to one sheet name.");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxUseSheetNames)
                    .add(jLabelList)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBoxUseSheetNames)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabelList)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                .add(73, 73, 73))
        );

        jTabbedPane1.addTab("Sheet Names", jPanel2);

        jCheckBoxCreateCustomPalette.setText("Create Custom Palette");
        jCheckBoxCreateCustomPalette.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCreateCustomPaletteActionPerformed(evt);
            }
        });

        jLabelPassword.setText("Password");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxCreateCustomPalette)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabelPassword)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextFieldPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 141, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(257, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBoxCreateCustomPalette)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelPassword)
                    .add(jTextFieldPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(261, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("JExcelAPI options", jPanel3);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabelTitle)
                .addContainerGap(369, Short.MAX_VALUE))
            .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabelTitle)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxOnePagePerSheetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxOnePagePerSheetActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxOnePagePerSheetActionPerformed

    private void jCheckBoxRemoveEmptySpaceBetweenRowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxRemoveEmptySpaceBetweenRowsActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxRemoveEmptySpaceBetweenRowsActionPerformed

    private void jCheckBoxRemoveEmptySpaceBetweenColumnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxRemoveEmptySpaceBetweenColumnsActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxRemoveEmptySpaceBetweenColumnsActionPerformed

    private void jCheckBoxWhitePageBackgroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxWhitePageBackgroundActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxWhitePageBackgroundActionPerformed

    private void jCheckBoxAutoDetectCellTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAutoDetectCellTypeActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxAutoDetectCellTypeActionPerformed

    private void jCheckBoxFontSizeFixEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFontSizeFixEnabledActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxFontSizeFixEnabledActionPerformed

    private void jCheckBoxImageBorderFixEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxImageBorderFixEnabledActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxImageBorderFixEnabledActionPerformed

    private void jCheckBoxIgnoreGraphicsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxIgnoreGraphicsActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxIgnoreGraphicsActionPerformed

    private void jCheckBoxCollapseRowSpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCollapseRowSpanActionPerformed
       notifyChange();
    }//GEN-LAST:event_jCheckBoxCollapseRowSpanActionPerformed

    private void jCheckBoxIgnoreCellBorderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxIgnoreCellBorderActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxIgnoreCellBorderActionPerformed

    private void jCheckBoxUseSheetNamesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxUseSheetNamesActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxUseSheetNamesActionPerformed

    private void jCheckBoxCreateCustomPaletteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCreateCustomPaletteActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxCreateCustomPaletteActionPerformed

    private void jCheckBoxIgnoreCellBackgroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxIgnoreCellBackgroundActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxIgnoreCellBackgroundActionPerformed

    private void jSpinnerFreezeRowStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerFreezeRowStateChanged
        notifyChange();
    }//GEN-LAST:event_jSpinnerFreezeRowStateChanged

    private void jCheckBoxFixFontSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFixFontSizeActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxFixFontSizeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBoxAutoDetectCellType;
    private javax.swing.JCheckBox jCheckBoxCollapseRowSpan;
    private javax.swing.JCheckBox jCheckBoxCreateCustomPalette;
    private javax.swing.JCheckBox jCheckBoxFixFontSize;
    private javax.swing.JCheckBox jCheckBoxFontSizeFixEnabled;
    private javax.swing.JCheckBox jCheckBoxIgnoreCellBackground;
    private javax.swing.JCheckBox jCheckBoxIgnoreCellBorder;
    private javax.swing.JCheckBox jCheckBoxIgnoreGraphics;
    private javax.swing.JCheckBox jCheckBoxImageBorderFixEnabled;
    private javax.swing.JCheckBox jCheckBoxOnePagePerSheet;
    private javax.swing.JCheckBox jCheckBoxRemoveEmptySpaceBetweenColumns;
    private javax.swing.JCheckBox jCheckBoxRemoveEmptySpaceBetweenRows;
    private javax.swing.JCheckBox jCheckBoxUseSheetNames;
    private javax.swing.JCheckBox jCheckBoxWhitePageBackground;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelList;
    private javax.swing.JLabel jLabelMaximumRowsPerSheet;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner jSpinnerFreezeRow;
    private javax.swing.JSpinner jSpinnerMaximumRowsPerSheet;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextFieldColumn;
    private javax.swing.JTextField jTextFieldPassword;
    // End of variables declaration//GEN-END:variables


    public void load() {
        setInit(true);
        Preferences pref = IReportManager.getPreferences();
        
        JRPropertiesUtil jrPropUtils = IRLocalJasperReportsContext.getUtilities();
        
        jCheckBoxCreateCustomPalette.setSelected( pref.getBoolean(JExcelApiExporterParameter.PROPERTY_CREATE_CUSTOM_PALETTE, jrPropUtils.getBooleanProperty(JExcelApiExporterParameter.PROPERTY_CREATE_CUSTOM_PALETTE)));
        jTextFieldPassword.setText(  pref.get(JExcelApiExporterParameter.PROPERTY_PASSWORD, jrPropUtils.getProperty(JExcelApiExporterParameter.PROPERTY_PASSWORD)));

        jCheckBoxCollapseRowSpan.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN, jrPropUtils.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));
        
        jCheckBoxAutoDetectCellType.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_DETECT_CELL_TYPE, jrPropUtils.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));
        jCheckBoxFontSizeFixEnabled.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_FONT_SIZE_FIX_ENABLED, jrPropUtils.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));
        jCheckBoxIgnoreCellBorder.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BORDER, jrPropUtils.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));
        jCheckBoxIgnoreCellBackground.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BACKGROUND, jrPropUtils.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BACKGROUND)));

        jCheckBoxIgnoreGraphics.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_GRAPHICS, jrPropUtils.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_GRAPHICS)));
        jCheckBoxImageBorderFixEnabled.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_IMAGE_BORDER_FIX_ENABLED, jrPropUtils.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_IMAGE_BORDER_FIX_ENABLED)));
        jCheckBoxOnePagePerSheet.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_ONE_PAGE_PER_SHEET, jrPropUtils.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_ONE_PAGE_PER_SHEET)));
        jCheckBoxRemoveEmptySpaceBetweenColumns.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, jrPropUtils.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS)));
        jCheckBoxRemoveEmptySpaceBetweenRows.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, jrPropUtils.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS)));
        jCheckBoxWhitePageBackground.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND, jrPropUtils.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND)));

        SpinnerNumberModel model = (SpinnerNumberModel)jSpinnerMaximumRowsPerSheet.getModel();
        model.setValue( pref.getInt(JRXlsAbstractExporterParameter.PROPERTY_MAXIMUM_ROWS_PER_SHEET, jrPropUtils.getIntegerProperty(JRXlsAbstractExporterParameter.PROPERTY_MAXIMUM_ROWS_PER_SHEET)));

        jCheckBoxUseSheetNames.setSelected( pref.getBoolean(JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.useSheetNames", false));

        // freeze pane...
        String columnIndex = jrPropUtils.getProperty(JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN);
        if (columnIndex == null) columnIndex = "";
        jTextFieldColumn.setText( pref.get(JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN, columnIndex));
        ((SpinnerNumberModel)jSpinnerFreezeRow.getModel()).setValue( pref.getInt(JRXlsAbstractExporter.PROPERTY_FREEZE_ROW, jrPropUtils.getIntegerProperty(JRXlsAbstractExporter.PROPERTY_FREEZE_ROW,0)));

        //com.jaspersoft.ireport.designer.utils.Misc.setComboBoxTag(true, pref.get(JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN_EDGE, jrPropUtils.getProperty(JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN_EDGE)), jComboBoxColumnEdge);
        //com.jaspersoft.ireport.designer.utils.Misc.setComboBoxTag(true, pref.get(JRXlsAbstractExporter.PROPERTY_FREEZE_ROW_EDGE, jrPropUtils.getProperty(JRXlsAbstractExporter.PROPERTY_FREEZE_ROW_EDGE)), jComboBoxRowEdge);

        
        jTextArea1.setText(pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.sheetNames", jrPropUtils.getProperty(JRXlsAbstractExporterParameter.PROPERTY_MAXIMUM_ROWS_PER_SHEET)));

        
        jCheckBoxFixFontSize.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_FONT_SIZE_FIX_ENABLED, jrPropUtils.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_FONT_SIZE_FIX_ENABLED)));

        
        setInit(false);
    }

    public void store() {

        Preferences pref = IReportManager.getPreferences();

        pref.putBoolean(JExcelApiExporterParameter.PROPERTY_CREATE_CUSTOM_PALETTE,  jCheckBoxCreateCustomPalette.isSelected() );
        pref.put(JExcelApiExporterParameter.PROPERTY_PASSWORD, jTextFieldPassword.getText());
        
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN,  jCheckBoxCollapseRowSpan.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_DETECT_CELL_TYPE,  jCheckBoxAutoDetectCellType.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_FONT_SIZE_FIX_ENABLED,  jCheckBoxFontSizeFixEnabled.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BORDER,  jCheckBoxIgnoreCellBorder.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BACKGROUND,  jCheckBoxIgnoreCellBackground.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_GRAPHICS,  jCheckBoxIgnoreGraphics.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_IMAGE_BORDER_FIX_ENABLED,  jCheckBoxImageBorderFixEnabled.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_ONE_PAGE_PER_SHEET,  jCheckBoxOnePagePerSheet.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,  jCheckBoxRemoveEmptySpaceBetweenColumns.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,  jCheckBoxRemoveEmptySpaceBetweenRows.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND,  jCheckBoxWhitePageBackground.isSelected() );

        SpinnerNumberModel model = (SpinnerNumberModel)jSpinnerMaximumRowsPerSheet.getModel();
        pref.putInt(JRXlsAbstractExporterParameter.PROPERTY_MAXIMUM_ROWS_PER_SHEET, model.getNumber().intValue());

        pref.putBoolean(JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.useSheetNames",  jCheckBoxUseSheetNames.isSelected() );
        pref.put(JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.sheetNames", jTextArea1.getText().trim());


        pref.put(JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN,  jTextFieldColumn.getText() );
        pref.putInt(JRXlsAbstractExporter.PROPERTY_FREEZE_ROW,  ((SpinnerNumberModel)jSpinnerFreezeRow.getModel()).getNumber().intValue() );
        
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_FONT_SIZE_FIX_ENABLED, jCheckBoxFixFontSize.isSelected());


//        if (jComboBoxColumnEdge.getSelectedIndex() > 0)
//        {
//            pref.put(JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN_EDGE, (String) ((Tag)jComboBoxColumnEdge.getSelectedItem()).getValue());
//        }
//        else
//        {
//            pref.remove(JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN_EDGE);
//        }

//        if (jComboBoxRowEdge.getSelectedIndex() > 0)
//        {
//            pref.put(JRXlsAbstractExporter.PROPERTY_FREEZE_ROW_EDGE, (String) ((Tag)jComboBoxRowEdge.getSelectedItem()).getValue());
//            System.out.println("Saved " + (String) ((Tag)jComboBoxRowEdge.getSelectedItem()).getValue());
//        }
//        else
//        {
//            pref.remove(JRXlsAbstractExporter.PROPERTY_FREEZE_ROW_EDGE);
//        }
    }

    public boolean valid() {
        return true;
    }

    @Override
    public String getDisplayName() {
        return I18n.getString("XlsExportParametersPanel.title");
    }
}
