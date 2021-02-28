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
import javax.swing.JFileChooser;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

/**
 *
 * @author gtoffoli
 */
public class HtmlExportParametersPanel extends AbstractExportParametersPanel {

    /** Creates new form HtmlExportParametersPanel */
    public HtmlExportParametersPanel() {
        initComponents();

        javax.swing.event.DocumentListener textfieldListener =  new javax.swing.event.DocumentListener()
                {
                    public void changedUpdate(javax.swing.event.DocumentEvent evt)
                    {
                        notifyChange();
                    }
                    public void insertUpdate(javax.swing.event.DocumentEvent evt)
                    {
                        notifyChange();
                    }
                    public void removeUpdate(javax.swing.event.DocumentEvent evt)
                    {
                        notifyChange();
                    }
                };

         jTextFieldImagesDirectory.getDocument().addDocumentListener(textfieldListener);
         jTextFieldImagesDirectory1.getDocument().addDocumentListener(textfieldListener);
         jTextAreaHtmlBetweenPages.getDocument().addDocumentListener(textfieldListener);
         jTextAreaHtmlFooter.getDocument().addDocumentListener(textfieldListener);
         jTextAreaHtmlHeader.getDocument().addDocumentListener(textfieldListener);

         applyI18n();
    }

    public void applyI18n()
    {
        jLabelTitle.setText(I18n.getString("HtmlExportParametersPanel.jLabelTitle.text")); // NOI18N
	jCheckBoxSaveImages.setText(I18n.getString("HtmlExportParametersPanel.jCheckBoxSaveImages.text")); // NOI18N
	jLabelImagesDirectory.setText(I18n.getString("HtmlExportParametersPanel.jLabelImagesDirectory.text")); // NOI18N
	jButtonImagesDirectory.setText(I18n.getString("HtmlExportParametersPanel.jButtonImagesDirectory.text")); // NOI18N
	jLabelImagesDirectory1.setText(I18n.getString("HtmlExportParametersPanel.jLabelImagesDirectory1.text")); // NOI18N
	jTabbedPane1.setTitleAt(0, I18n.getString("HtmlExportParametersPanel.jPanel1.TabConstraints.tabTitle")); // NOI18N
	jLabelHeader.setText(I18n.getString("HtmlExportParametersPanel.jLabelHeader.text")); // NOI18N
	jLabelHTMLFooter.setText(I18n.getString("HtmlExportParametersPanel.jLabelHTMLFooter.text")); // NOI18N
	jTabbedPane1.setTitleAt(1,I18n.getString("HtmlExportParametersPanel.jPanel2.TabConstraints.tabTitle")); // NOI18N
	jLabel1.setText(I18n.getString("HtmlExportParametersPanel.jLabel1.text")); // NOI18N
	jTabbedPane1.setTitleAt(2,I18n.getString("HtmlExportParametersPanel.jPanel3.TabConstraints.tabTitle")); // NOI18N
	jCheckBoxRemoveEmptySpace.setText(I18n.getString("HtmlExportParametersPanel.jCheckBoxRemoveEmptySpace.text")); // NOI18N
	jCheckBoxWhiteBackground.setText(I18n.getString("HtmlExportParametersPanel.jCheckBoxWhiteBackground.text")); // NOI18N
	jCheckBoxUseImagesToAlign.setText(I18n.getString("HtmlExportParametersPanel.jCheckBoxUseImagesToAlign.text")); // NOI18N
	jCheckBoxWrapBreakWord.setText(I18n.getString("HtmlExportParametersPanel.jCheckBoxWrapBreakWord.text")); // NOI18N
	jLabelSizeUnit.setText(I18n.getString("HtmlExportParametersPanel.jLabelSizeUnit.text")); // NOI18N
	jCheckBoxFrameAsNestedTables.setText(I18n.getString("HtmlExportParametersPanel.jCheckBoxFrameAsNestedTables.text")); // NOI18N
	jTabbedPane1.setTitleAt(3,I18n.getString("HtmlExportParametersPanel.jPanel4.TabConstraints.tabTitle")); // NOI18N

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
        jPanel1 = new javax.swing.JPanel();
        jCheckBoxSaveImages = new javax.swing.JCheckBox();
        jLabelImagesDirectory = new javax.swing.JLabel();
        jTextFieldImagesDirectory = new javax.swing.JTextField();
        jButtonImagesDirectory = new javax.swing.JButton();
        jTextFieldImagesDirectory1 = new javax.swing.JTextField();
        jLabelImagesDirectory1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaHtmlHeader = new javax.swing.JTextArea();
        jLabelHeader = new javax.swing.JLabel();
        jLabelHTMLFooter = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaHtmlFooter = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaHtmlBetweenPages = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jCheckBoxWhiteBackground = new javax.swing.JCheckBox();
        jCheckBoxWrapBreakWord = new javax.swing.JCheckBox();
        jLabelSizeUnit = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jCheckBoxRemoveEmptySpace = new javax.swing.JCheckBox();
        jCheckBoxUseImagesToAlign = new javax.swing.JCheckBox();
        jCheckBoxFrameAsNestedTables = new javax.swing.JCheckBox();

        jLabelTitle.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabelTitle.setText("HTML Export Parametes");

        jCheckBoxSaveImages.setText("Save images to disk");
        jCheckBoxSaveImages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxSaveImagesActionPerformed(evt);
            }
        });

        jLabelImagesDirectory.setText("Images directory");

        jButtonImagesDirectory.setText("Browse");
        jButtonImagesDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImagesDirectoryActionPerformed(evt);
            }
        });

        jLabelImagesDirectory1.setText("Images URI");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxSaveImages)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelImagesDirectory)
                            .add(jLabelImagesDirectory1))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTextFieldImagesDirectory1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jTextFieldImagesDirectory, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButtonImagesDirectory)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBoxSaveImages)
                .add(13, 13, 13)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelImagesDirectory)
                    .add(jButtonImagesDirectory)
                    .add(jTextFieldImagesDirectory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelImagesDirectory1)
                    .add(jTextFieldImagesDirectory1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10))
        );

        jTabbedPane1.addTab("Images", jPanel1);

        jTextAreaHtmlHeader.setColumns(20);
        jTextAreaHtmlHeader.setRows(5);
        jScrollPane1.setViewportView(jTextAreaHtmlHeader);

        jLabelHeader.setText("HTML header");

        jLabelHTMLFooter.setText("HTML footer");

        jTextAreaHtmlFooter.setColumns(20);
        jTextAreaHtmlFooter.setRows(5);
        jScrollPane2.setViewportView(jTextAreaHtmlFooter);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                    .add(jLabelHeader)
                    .add(jLabelHTMLFooter)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelHeader)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabelHTMLFooter)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Header and Footer", jPanel2);

        jTextAreaHtmlBetweenPages.setColumns(20);
        jTextAreaHtmlBetweenPages.setRows(5);
        jScrollPane3.setViewportView(jTextAreaHtmlBetweenPages);

        jLabel1.setText("HTML between pages");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                    .add(jLabel1))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .add(3, 3, 3)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Between pages", jPanel3);

        jCheckBoxWhiteBackground.setText("White background");
        jCheckBoxWhiteBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxWhiteBackgroundActionPerformed(evt);
            }
        });

        jCheckBoxWrapBreakWord.setText("Wrap Break Word");
        jCheckBoxWrapBreakWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxWrapBreakWordActionPerformed(evt);
            }
        });

        jLabelSizeUnit.setText("Size Unit");

        jComboBox1.setEditable(true);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "px", "pt" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("HTML  specific options"));

        jCheckBoxRemoveEmptySpace.setText("Remove empty space between rows");
        jCheckBoxRemoveEmptySpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxRemoveEmptySpaceActionPerformed(evt);
            }
        });

        jCheckBoxUseImagesToAlign.setText("Use images to align");
        jCheckBoxUseImagesToAlign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUseImagesToAlignActionPerformed(evt);
            }
        });

        jCheckBoxFrameAsNestedTables.setText("Frames as nested tables");
        jCheckBoxFrameAsNestedTables.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFrameAsNestedTablesActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxRemoveEmptySpace)
                    .add(jCheckBoxUseImagesToAlign)
                    .add(jCheckBoxFrameAsNestedTables))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jCheckBoxRemoveEmptySpace)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxUseImagesToAlign)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxFrameAsNestedTables))
        );

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jCheckBoxWhiteBackground)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jCheckBoxWrapBreakWord)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 132, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabelSizeUnit)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(301, 301, 301))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBoxWhiteBackground)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxWrapBreakWord)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelSizeUnit)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Other", jPanel4);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabelTitle)
                .addContainerGap(412, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabelTitle)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxSaveImagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxSaveImagesActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxSaveImagesActionPerformed

    private void jButtonImagesDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImagesDirectoryActionPerformed

        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            jTextFieldImagesDirectory.setText( jfc.getSelectedFile().getPath());
        }
        notifyChange();

    }//GEN-LAST:event_jButtonImagesDirectoryActionPerformed

    private void jCheckBoxRemoveEmptySpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxRemoveEmptySpaceActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxRemoveEmptySpaceActionPerformed

    private void jCheckBoxWhiteBackgroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxWhiteBackgroundActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxWhiteBackgroundActionPerformed

    private void jCheckBoxUseImagesToAlignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxUseImagesToAlignActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxUseImagesToAlignActionPerformed

    private void jCheckBoxWrapBreakWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxWrapBreakWordActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxWrapBreakWordActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        notifyChange();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jCheckBoxFrameAsNestedTablesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFrameAsNestedTablesActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxFrameAsNestedTablesActionPerformed

    public void load() {
        setInit(true);
        Preferences pref = IReportManager.getPreferences();
        JRPropertiesUtil jrPropUtils = IRLocalJasperReportsContext.getUtilities();
        JasperReportsContext context = IRLocalJasperReportsContext.getInstance();
        
        jCheckBoxFrameAsNestedTables.setSelected( pref.getBoolean(JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES, jrPropUtils.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES)));
        jCheckBoxRemoveEmptySpace.setSelected( pref.getBoolean(JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, jrPropUtils.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS)));
        jCheckBoxSaveImages.setSelected( pref.getBoolean(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.saveImages", true));
        jCheckBoxUseImagesToAlign.setSelected( pref.getBoolean(JRHtmlExporterParameter.PROPERTY_USING_IMAGES_TO_ALIGN, jrPropUtils.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_USING_IMAGES_TO_ALIGN)));
        jCheckBoxWhiteBackground.setSelected( pref.getBoolean(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND, jrPropUtils.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND)));
        jCheckBoxWrapBreakWord.setSelected( pref.getBoolean(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD, jrPropUtils.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD)));

        jTextFieldImagesDirectory.setText(  pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesDirectory",""));
        jTextFieldImagesDirectory1.setText(  pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesUri",""));

        jTextAreaHtmlBetweenPages.setText(  pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlBetweenPages",""));
        jTextAreaHtmlFooter.setText(  pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlFooter",""));
        jTextAreaHtmlHeader.setText(  pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlHeader",""));
        jComboBox1.setSelectedItem(  pref.get(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT, jrPropUtils.getProperty(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT)));
        setInit(false);
    }

    public void store() {

        Preferences pref = IReportManager.getPreferences();

        pref.putBoolean(JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES,  jCheckBoxFrameAsNestedTables.isSelected() );
        pref.putBoolean(JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,  jCheckBoxRemoveEmptySpace.isSelected() );
        pref.putBoolean(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.saveImages",  jCheckBoxSaveImages.isSelected() );
        pref.putBoolean(JRHtmlExporterParameter.PROPERTY_USING_IMAGES_TO_ALIGN,  jCheckBoxUseImagesToAlign.isSelected() );
        pref.putBoolean(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND,  jCheckBoxWhiteBackground.isSelected() );
        pref.putBoolean(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD,  jCheckBoxWrapBreakWord.isSelected() );

        pref.put(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesDirectory", jTextFieldImagesDirectory.getText());
        pref.put(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.imagesUri", jTextFieldImagesDirectory1.getText());
        pref.put(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlBetweenPages", jTextAreaHtmlBetweenPages.getText());
        pref.put(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlFooter", jTextAreaHtmlFooter.getText());
        pref.put(JRPropertiesUtil.PROPERTY_PREFIX + "export.html.htmlHeader", jTextAreaHtmlHeader.getText());
        pref.put(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT, jComboBox1.getSelectedItem()+"");

      }

    public boolean valid() {
        return true;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonImagesDirectory;
    private javax.swing.JCheckBox jCheckBoxFrameAsNestedTables;
    private javax.swing.JCheckBox jCheckBoxRemoveEmptySpace;
    private javax.swing.JCheckBox jCheckBoxSaveImages;
    private javax.swing.JCheckBox jCheckBoxUseImagesToAlign;
    private javax.swing.JCheckBox jCheckBoxWhiteBackground;
    private javax.swing.JCheckBox jCheckBoxWrapBreakWord;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelHTMLFooter;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JLabel jLabelImagesDirectory;
    private javax.swing.JLabel jLabelImagesDirectory1;
    private javax.swing.JLabel jLabelSizeUnit;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextAreaHtmlBetweenPages;
    private javax.swing.JTextArea jTextAreaHtmlFooter;
    private javax.swing.JTextArea jTextAreaHtmlHeader;
    private javax.swing.JTextField jTextFieldImagesDirectory;
    private javax.swing.JTextField jTextFieldImagesDirectory1;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getDisplayName() {
        return I18n.getString("HtmlExportParametersPanel.title");
    }

}
