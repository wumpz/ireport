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
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.locale.I18n;
import java.util.prefs.Preferences;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.lowagie.text.pdf.PdfWriter;
import javax.swing.JFileChooser;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

/**
 *
 * @author gtoffoli
 */
public class PdfExportParametersPanel extends AbstractExportParametersPanel {

    /** Creates new form PdfExportParametersPanel */
    public PdfExportParametersPanel() {
        initComponents();
        applyI18n();
        
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

         jTextFieldMetadataAuthor.getDocument().addDocumentListener(textfieldListener);
         jTextFieldMetadataCreator.getDocument().addDocumentListener(textfieldListener);
         jTextFieldMetadataKeywords.getDocument().addDocumentListener(textfieldListener);
         jTextFieldMetadataSubject.getDocument().addDocumentListener(textfieldListener);
         jTextFieldMetadataTitle.getDocument().addDocumentListener(textfieldListener);
         jTextFieldOwnerPassword.getDocument().addDocumentListener(textfieldListener);
         jTextFieldUserPassword.getDocument().addDocumentListener(textfieldListener);
         jTextFieldTagLanguage.getDocument().addDocumentListener(textfieldListener);
         jTextAreaPDFJavascript.getDocument().addDocumentListener(textfieldListener);

         jComboBoxEncryption.addItem(I18n.getString("export.pdf.encryption.None"));
         jComboBoxEncryption.addItem(I18n.getString("export.pdf.encryption.Standard"));
         jComboBoxEncryption.addItem(I18n.getString("export.pdf.encryption.128bitKey"));



         jComboBoxPDFVersion.addItem(new Tag(null, I18n.getString("Global.Button.Default"))); //NOI18N
         jComboBoxPDFVersion.addItem(new Tag(""+JRPdfExporterParameter.PDF_VERSION_1_2, "1.2")); //NOI18N
         jComboBoxPDFVersion.addItem(new Tag(""+JRPdfExporterParameter.PDF_VERSION_1_3, "1.3")); //NOI18N
         jComboBoxPDFVersion.addItem(new Tag(""+JRPdfExporterParameter.PDF_VERSION_1_4, "1.4")); //NOI18N
         jComboBoxPDFVersion.addItem(new Tag(""+JRPdfExporterParameter.PDF_VERSION_1_5, "1.5")); //NOI18N
         jComboBoxPDFVersion.addItem(new Tag(""+JRPdfExporterParameter.PDF_VERSION_1_6, "1.6")); //NOI18N

         jComboBoxPdfA.addItem(new Tag(""+JRPdfExporterParameter.PDFA_CONFORMANCE_NONE, "None")); //NOI18N
         jComboBoxPdfA.addItem(new Tag(""+JRPdfExporterParameter.PDFA_CONFORMANCE_1A, "PDF/A-1A")); //NOI18N
         jComboBoxPdfA.addItem(new Tag(""+JRPdfExporterParameter.PDFA_CONFORMANCE_1B, "PDF/A-1B")); //NOI18N
         
    }

    public void load() {
        setInit(true);
        Preferences pref = IReportManager.getPreferences();
        

        JRPropertiesUtil jrPropUtils = IRLocalJasperReportsContext.getUtilities();
        
        Misc.setComboboxSelectedTagValue(jComboBoxPDFVersion, pref.get(JRPdfExporterParameter.PROPERTY_PDF_VERSION, jrPropUtils.getProperty(JRPdfExporterParameter.PROPERTY_PDF_VERSION)));
        Misc.setComboboxSelectedTagValue(jComboBoxPdfA, pref.get(JRPdfExporterParameter.PROPERTY_PDFA_CONFORMANCE, jrPropUtils.getProperty(JRPdfExporterParameter.PROPERTY_PDFA_CONFORMANCE)));

        jTextFieldICC.setText(pref.get(JRPdfExporterParameter.PROPERTY_PDFA_ICC_PROFILE_PATH, Misc.nvl(jrPropUtils.getProperty(JRPdfExporterParameter.PROPERTY_PDFA_ICC_PROFILE_PATH),"")));

        jCheckBoxCreatingBatchModeBookmarks.setSelected( pref.getBoolean(JRPdfExporterParameter.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS, jrPropUtils.getBooleanProperty(JRPdfExporterParameter.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS)));
        jCheckBoxCompressed.setSelected( pref.getBoolean(JRPdfExporterParameter.PROPERTY_COMPRESSED, jrPropUtils.getBooleanProperty(JRPdfExporterParameter.PROPERTY_COMPRESSED)));
        jCheckBoxForceLinebreakPolicy.setSelected( pref.getBoolean(JRPdfExporterParameter.PROPERTY_FORCE_LINEBREAK_POLICY, jrPropUtils.getBooleanProperty(JRPdfExporterParameter.PROPERTY_FORCE_LINEBREAK_POLICY)));
        jCheckBoxForceSVGShapes.setSelected( pref.getBoolean(JRPdfExporterParameter.PROPERTY_FORCE_SVG_SHAPES, jrPropUtils.getBooleanProperty(JRPdfExporterParameter.PROPERTY_FORCE_SVG_SHAPES)));
        jCheckBoxIsTagged.setSelected( pref.getBoolean(JRPdfExporterParameter.PROPERTY_TAGGED, jrPropUtils.getBooleanProperty(JRPdfExporterParameter.PROPERTY_TAGGED)));

        jComboBoxEncryption.setSelectedIndex(0);
        if (pref.getBoolean(JRPdfExporterParameter.PROPERTY_ENCRYPTED, jrPropUtils.getBooleanProperty(JRPdfExporterParameter.PROPERTY_ENCRYPTED)))
        {
            jComboBoxEncryption.setSelectedIndex(1);
        }
        if (pref.getBoolean(JRPdfExporterParameter.PROPERTY_128_BIT_KEY, jrPropUtils.getBooleanProperty(JRPdfExporterParameter.PROPERTY_128_BIT_KEY)))
        {
            jComboBoxEncryption.setSelectedIndex(2);
        }
        jTextFieldMetadataAuthor.setText(pref.get("export.pdf.METADATA_AUTHOR", ""));
        jTextFieldMetadataCreator.setText(pref.get("export.pdf.METADATA_CREATOR", ""));
        jTextFieldMetadataKeywords.setText(pref.get("export.pdf.METADATA_KEYWORDS", ""));
        jTextFieldMetadataSubject.setText(pref.get("export.pdf.METADATA_SUBJECT", ""));
        jTextFieldMetadataTitle.setText(pref.get("export.pdf.METADATA_TITLE", ""));
        jTextFieldOwnerPassword.setText(pref.get("export.pdf.OWNER_PASSWORD", ""));
        jTextFieldUserPassword.setText(pref.get("export.pdf.USER_PASSWORD", ""));
        jTextFieldTagLanguage.setText(pref.get("export.pdf.TAG_LANGUAGE", jrPropUtils.getProperty(JRPdfExporterParameter.PROPERTY_TAG_LANGUAGE)));
        jTextAreaPDFJavascript.setText(pref.get("export.pdf.PDF_JAVASCRIPT", jrPropUtils.getProperty(JRPdfExporterParameter.PROPERTY_PDF_JAVASCRIPT)));

        int documentPermissions = pref.getInt("export.pdf.PERMISSIONS",0);
        jCheckBoxAllawDegradedPrinting.setSelected( (documentPermissions & PdfWriter.ALLOW_DEGRADED_PRINTING) > 0);
        jCheckBoxAllowAssembly.setSelected( (documentPermissions & PdfWriter.ALLOW_ASSEMBLY) > 0);
        jCheckBoxAllowCopy.setSelected( (documentPermissions & PdfWriter.ALLOW_COPY) > 0);
        jCheckBoxAllowFillIn.setSelected( (documentPermissions & PdfWriter.ALLOW_FILL_IN) > 0);
        jCheckBoxAllowModifyAnnotations.setSelected( (documentPermissions & PdfWriter.ALLOW_MODIFY_ANNOTATIONS) > 0);
        jCheckBoxAllowModifyContents.setSelected( (documentPermissions & PdfWriter.ALLOW_MODIFY_CONTENTS) > 0);
        jCheckBoxAllowPrinting.setSelected( (documentPermissions & PdfWriter.ALLOW_PRINTING) == PdfWriter.ALLOW_PRINTING);
        jCheckBoxAllowScreenReaders.setSelected( (documentPermissions & PdfWriter.ALLOW_SCREENREADERS) > 0);
        setInit(false);
    }

    public void store() {

        Preferences pref = IReportManager.getPreferences();

        Tag t = (Tag) jComboBoxPDFVersion.getSelectedItem();
        if (t.getValue() == null) {
            pref.remove(JRPdfExporterParameter.PROPERTY_PDF_VERSION);
        } else {
            pref.put(JRPdfExporterParameter.PROPERTY_PDF_VERSION, ""+t.getValue());
        }

        pref.putBoolean(JRPdfExporterParameter.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS , jCheckBoxCreatingBatchModeBookmarks.isSelected());
        pref.putBoolean(JRPdfExporterParameter.PROPERTY_COMPRESSED , jCheckBoxCompressed.isSelected());
        pref.putBoolean(JRPdfExporterParameter.PROPERTY_FORCE_LINEBREAK_POLICY , jCheckBoxForceLinebreakPolicy.isSelected());
        pref.putBoolean(JRPdfExporterParameter.PROPERTY_FORCE_SVG_SHAPES , jCheckBoxForceSVGShapes.isSelected());
        pref.putBoolean(JRPdfExporterParameter.PROPERTY_TAGGED , jCheckBoxIsTagged.isSelected());

        int index = jComboBoxEncryption.getSelectedIndex();
        pref.putBoolean(JRPdfExporterParameter.PROPERTY_ENCRYPTED , (index != 0));
        pref.putBoolean(JRPdfExporterParameter.PROPERTY_128_BIT_KEY , (index == 2));

        pref.put("export.pdf.METADATA_AUTHOR" , jTextFieldMetadataAuthor.getText());
        pref.put("export.pdf.METADATA_CREATOR" , jTextFieldMetadataCreator.getText());
        pref.put("export.pdf.METADATA_KEYWORDS" , jTextFieldMetadataKeywords.getText());
        pref.put("export.pdf.METADATA_SUBJECT" , jTextFieldMetadataSubject.getText());

        pref.put("export.pdf.METADATA_TITLE" , jTextFieldMetadataTitle.getText());
        pref.put("export.pdf.OWNER_PASSWORD" , jTextFieldOwnerPassword.getText());
        pref.put("export.pdf.USER_PASSWORD" , jTextFieldUserPassword.getText());
        pref.put("export.pdf.TAG_LANGUAGE" , jTextFieldTagLanguage.getText());
        pref.put("export.pdf.PDF_JAVASCRIPT" , jTextAreaPDFJavascript.getText());
        
        t = (Tag) jComboBoxPdfA.getSelectedItem();
        if (t.getValue() == null) {
            pref.remove(JRPdfExporterParameter.PROPERTY_PDFA_CONFORMANCE);
        } else {
            pref.put(JRPdfExporterParameter.PROPERTY_PDFA_CONFORMANCE,""+t.getValue());
        }

        pref.put(JRPdfExporterParameter.PROPERTY_PDFA_ICC_PROFILE_PATH , jTextFieldICC.getText());


        int documentPermissions = 0;
        if (jCheckBoxAllawDegradedPrinting.isSelected()) documentPermissions |= PdfWriter.ALLOW_DEGRADED_PRINTING;
        if (jCheckBoxAllowAssembly.isSelected()) documentPermissions |= PdfWriter.ALLOW_ASSEMBLY;
        if (jCheckBoxAllowCopy.isSelected()) documentPermissions |= PdfWriter.ALLOW_COPY;
        if (jCheckBoxAllowFillIn.isSelected()) documentPermissions |= PdfWriter.ALLOW_FILL_IN;
        if (jCheckBoxAllowModifyAnnotations.isSelected()) documentPermissions |= PdfWriter.ALLOW_MODIFY_ANNOTATIONS;
        if (jCheckBoxAllowModifyContents.isSelected()) documentPermissions |= PdfWriter.ALLOW_MODIFY_CONTENTS;
        if (jCheckBoxAllowPrinting.isSelected()) documentPermissions |= PdfWriter.ALLOW_PRINTING;
        if (jCheckBoxAllowScreenReaders.isSelected()) documentPermissions |= PdfWriter.ALLOW_SCREENREADERS;

        pref.putInt("export.pdf.PERMISSIONS" , documentPermissions);
    }

    public boolean valid() {
        return true;
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
        jLabelPDFVersion = new javax.swing.JLabel();
        jComboBoxPDFVersion = new javax.swing.JComboBox();
        jCheckBoxCreatingBatchModeBookmarks = new javax.swing.JCheckBox();
        jCheckBoxCompressed = new javax.swing.JCheckBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabelEncryption = new javax.swing.JLabel();
        jLabelOwnerPassword = new javax.swing.JLabel();
        jTextFieldOwnerPassword = new javax.swing.JTextField();
        jLabelUserPassword = new javax.swing.JLabel();
        jComboBoxEncryption = new javax.swing.JComboBox();
        jTextFieldUserPassword = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxAllowPrinting = new javax.swing.JCheckBox();
        jCheckBoxAllowModifyContents = new javax.swing.JCheckBox();
        jCheckBoxAllowCopy = new javax.swing.JCheckBox();
        jCheckBoxAllowModifyAnnotations = new javax.swing.JCheckBox();
        jCheckBoxAllowFillIn = new javax.swing.JCheckBox();
        jCheckBoxAllowScreenReaders = new javax.swing.JCheckBox();
        jCheckBoxAllowAssembly = new javax.swing.JCheckBox();
        jCheckBoxAllawDegradedPrinting = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLabelMetadataTitle = new javax.swing.JLabel();
        jTextFieldMetadataTitle = new javax.swing.JTextField();
        jLabelMetadataAuthor = new javax.swing.JLabel();
        jTextFieldMetadataAuthor = new javax.swing.JTextField();
        jLabelMetadataSubject = new javax.swing.JLabel();
        jTextFieldMetadataSubject = new javax.swing.JTextField();
        jLabelMetadataKeywords = new javax.swing.JLabel();
        jTextFieldMetadataKeywords = new javax.swing.JTextField();
        jLabelMetadataCreator = new javax.swing.JLabel();
        jTextFieldMetadataCreator = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jCheckBoxForceLinebreakPolicy = new javax.swing.JCheckBox();
        jCheckBoxForceSVGShapes = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaPDFJavascript = new javax.swing.JTextArea();
        jLabelPDFJavascript = new javax.swing.JLabel();
        jCheckBoxIsTagged = new javax.swing.JCheckBox();
        jLabelTagLanguage = new javax.swing.JLabel();
        jTextFieldTagLanguage = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxPdfA = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldICC = new javax.swing.JTextField();
        jButtonPDFViewer1 = new javax.swing.JButton();

        jLabelTitle.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabelTitle.setText("PDF Export parameters");

        jLabelPDFVersion.setText("PDF Version");

        jComboBoxPDFVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPDFVersionActionPerformed(evt);
            }
        });

        jCheckBoxCreatingBatchModeBookmarks.setText("Creating batch mode bookmarks");
        jCheckBoxCreatingBatchModeBookmarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCreatingBatchModeBookmarksActionPerformed(evt);
            }
        });

        jCheckBoxCompressed.setText("Compressed");
        jCheckBoxCompressed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCompressedActionPerformed(evt);
            }
        });

        jLabelEncryption.setText("Encryption");

        jLabelOwnerPassword.setText("Owner password");

        jLabelUserPassword.setText("User password");

        jComboBoxEncryption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxEncryptionActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Document permissions"));

        jCheckBoxAllowPrinting.setText("Allow printing");
        jCheckBoxAllowPrinting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAllowPrintingActionPerformed(evt);
            }
        });

        jCheckBoxAllowModifyContents.setText("Allow Modify Contents");
        jCheckBoxAllowModifyContents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAllowModifyContentsActionPerformed(evt);
            }
        });

        jCheckBoxAllowCopy.setText("Allow Copy");
        jCheckBoxAllowCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAllowCopyActionPerformed(evt);
            }
        });

        jCheckBoxAllowModifyAnnotations.setText("Allow Modify Annotations");
        jCheckBoxAllowModifyAnnotations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAllowModifyAnnotationsActionPerformed(evt);
            }
        });

        jCheckBoxAllowFillIn.setText("Allow Fill In");
        jCheckBoxAllowFillIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAllowFillInActionPerformed(evt);
            }
        });

        jCheckBoxAllowScreenReaders.setText("Allow Screen Readers");
        jCheckBoxAllowScreenReaders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAllowScreenReadersActionPerformed(evt);
            }
        });

        jCheckBoxAllowAssembly.setText("Allow Assembly");
        jCheckBoxAllowAssembly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAllowAssemblyActionPerformed(evt);
            }
        });

        jCheckBoxAllawDegradedPrinting.setText("Allow Degraded Printing");
        jCheckBoxAllawDegradedPrinting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAllawDegradedPrintingActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxAllowModifyAnnotations)
                    .add(jCheckBoxAllowPrinting)
                    .add(jCheckBoxAllowAssembly))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCheckBoxAllowModifyContents)
                            .add(jCheckBoxAllowFillIn))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCheckBoxAllowScreenReaders)
                            .add(jCheckBoxAllowCopy)))
                    .add(jCheckBoxAllawDegradedPrinting)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBoxAllowPrinting)
                    .add(jCheckBoxAllowCopy)
                    .add(jCheckBoxAllowModifyContents))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBoxAllowModifyAnnotations)
                    .add(jCheckBoxAllowFillIn)
                    .add(jCheckBoxAllowScreenReaders))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBoxAllowAssembly)
                    .add(jCheckBoxAllawDegradedPrinting)))
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabelEncryption)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBoxEncryption, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelOwnerPassword)
                            .add(jLabelUserPassword))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTextFieldUserPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTextFieldOwnerPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelEncryption)
                    .add(jComboBoxEncryption, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelUserPassword)
                    .add(jTextFieldUserPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelOwnerPassword)
                    .add(jTextFieldOwnerPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(160, 160, 160))
        );

        jTabbedPane1.addTab("Security", jPanel1);

        jLabelMetadataTitle.setText("Title");

        jLabelMetadataAuthor.setText("Author");

        jLabelMetadataSubject.setText("Subject");

        jLabelMetadataKeywords.setText("Keywords");

        jLabelMetadataCreator.setText("Creator");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelMetadataKeywords)
                    .add(jLabelMetadataCreator)
                    .add(jLabelMetadataTitle)
                    .add(jLabelMetadataAuthor)
                    .add(jLabelMetadataSubject))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextFieldMetadataAuthor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .add(jTextFieldMetadataTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jTextFieldMetadataSubject, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .add(jTextFieldMetadataCreator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .add(jTextFieldMetadataKeywords, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelMetadataTitle)
                    .add(jTextFieldMetadataTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelMetadataAuthor)
                    .add(jTextFieldMetadataAuthor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelMetadataSubject)
                    .add(jTextFieldMetadataSubject, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelMetadataKeywords)
                    .add(jTextFieldMetadataKeywords, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelMetadataCreator)
                    .add(jTextFieldMetadataCreator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(112, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Metadata", jPanel3);

        jCheckBoxForceLinebreakPolicy.setText("Force linebreak policy");
        jCheckBoxForceLinebreakPolicy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxForceLinebreakPolicyActionPerformed(evt);
            }
        });

        jCheckBoxForceSVGShapes.setText("Force SVG shapes");
        jCheckBoxForceSVGShapes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxForceSVGShapesActionPerformed(evt);
            }
        });

        jTextAreaPDFJavascript.setColumns(20);
        jTextAreaPDFJavascript.setRows(5);
        jScrollPane1.setViewportView(jTextAreaPDFJavascript);

        jLabelPDFJavascript.setText("PDF Javascript");

        jCheckBoxIsTagged.setText("Is tagged");
        jCheckBoxIsTagged.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxIsTaggedActionPerformed(evt);
            }
        });

        jLabelTagLanguage.setText("Tag language");

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                    .add(jCheckBoxForceLinebreakPolicy)
                    .add(jCheckBoxForceSVGShapes)
                    .add(jCheckBoxIsTagged)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabelTagLanguage)
                        .add(8, 8, 8)
                        .add(jTextFieldTagLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 114, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabelPDFJavascript))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBoxForceLinebreakPolicy)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxForceSVGShapes)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxIsTagged)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelTagLanguage)
                    .add(jTextFieldTagLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabelPDFJavascript)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Other options", jPanel4);

        jLabel1.setText("PDF/A conformance");

        jLabel3.setText("PDF/A sRGB profile");

        jButtonPDFViewer1.setText(org.openide.util.NbBundle.getMessage(PdfExportParametersPanel.class, "ReportViewer.jButtonPDFViewer.text")); // NOI18N
        jButtonPDFViewer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPDFViewer1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBoxPdfA, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextFieldICC, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonPDFViewer1)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jComboBoxPdfA, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jTextFieldICC, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonPDFViewer1))
                .addContainerGap(175, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("PDF/A", jPanel5);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabelPDFVersion)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBoxPDFVersion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(jCheckBoxCreatingBatchModeBookmarks)
                        .add(18, 18, 18)
                        .add(jCheckBoxCompressed)))
                .addContainerGap(168, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(jLabelTitle)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                .addContainerGap())
            .add(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabelTitle)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelPDFVersion)
                    .add(jComboBoxPDFVersion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBoxCreatingBatchModeBookmarks)
                    .add(jCheckBoxCompressed))
                .add(5, 5, 5)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 275, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxCreatingBatchModeBookmarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCreatingBatchModeBookmarksActionPerformed
        notifyChange();
}//GEN-LAST:event_jCheckBoxCreatingBatchModeBookmarksActionPerformed

    private void jCheckBoxForceLinebreakPolicyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxForceLinebreakPolicyActionPerformed
        notifyChange();
}//GEN-LAST:event_jCheckBoxForceLinebreakPolicyActionPerformed

    private void jCheckBoxForceSVGShapesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxForceSVGShapesActionPerformed
        notifyChange();
}//GEN-LAST:event_jCheckBoxForceSVGShapesActionPerformed

    private void jCheckBoxIsTaggedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxIsTaggedActionPerformed
       notifyChange();
}//GEN-LAST:event_jCheckBoxIsTaggedActionPerformed

    private void jComboBoxPDFVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPDFVersionActionPerformed
       notifyChange();
    }//GEN-LAST:event_jComboBoxPDFVersionActionPerformed

    private void jCheckBoxCompressedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCompressedActionPerformed
       notifyChange();
    }//GEN-LAST:event_jCheckBoxCompressedActionPerformed

    private void jComboBoxEncryptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxEncryptionActionPerformed
        notifyChange();
    }//GEN-LAST:event_jComboBoxEncryptionActionPerformed

    private void jCheckBoxAllowPrintingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAllowPrintingActionPerformed
        notifyChange();
        if (jCheckBoxAllowPrinting.isSelected()) jCheckBoxAllawDegradedPrinting.setSelected(true);
    }//GEN-LAST:event_jCheckBoxAllowPrintingActionPerformed

    private void jCheckBoxAllowModifyContentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAllowModifyContentsActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxAllowModifyContentsActionPerformed

    private void jCheckBoxAllowCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAllowCopyActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxAllowCopyActionPerformed

    private void jCheckBoxAllowModifyAnnotationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAllowModifyAnnotationsActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxAllowModifyAnnotationsActionPerformed

    private void jCheckBoxAllowFillInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAllowFillInActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxAllowFillInActionPerformed

    private void jCheckBoxAllowScreenReadersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAllowScreenReadersActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxAllowScreenReadersActionPerformed

    private void jCheckBoxAllowAssemblyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAllowAssemblyActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxAllowAssemblyActionPerformed

    private void jCheckBoxAllawDegradedPrintingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAllawDegradedPrintingActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxAllawDegradedPrintingActionPerformed

    private void jButtonPDFViewer1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPDFViewer1ActionPerformed

        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

        jfc.setDialogTitle("Choose an ICC profile");
        jfc.setMultiSelectionEnabled(false);
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            jTextFieldICC.setText( jfc.getSelectedFile().getPath());
        }
}//GEN-LAST:event_jButtonPDFViewer1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonPDFViewer;
    private javax.swing.JButton jButtonPDFViewer1;
    private javax.swing.JCheckBox jCheckBoxAllawDegradedPrinting;
    private javax.swing.JCheckBox jCheckBoxAllowAssembly;
    private javax.swing.JCheckBox jCheckBoxAllowCopy;
    private javax.swing.JCheckBox jCheckBoxAllowFillIn;
    private javax.swing.JCheckBox jCheckBoxAllowModifyAnnotations;
    private javax.swing.JCheckBox jCheckBoxAllowModifyContents;
    private javax.swing.JCheckBox jCheckBoxAllowPrinting;
    private javax.swing.JCheckBox jCheckBoxAllowScreenReaders;
    private javax.swing.JCheckBox jCheckBoxCompressed;
    private javax.swing.JCheckBox jCheckBoxCreatingBatchModeBookmarks;
    private javax.swing.JCheckBox jCheckBoxForceLinebreakPolicy;
    private javax.swing.JCheckBox jCheckBoxForceSVGShapes;
    private javax.swing.JCheckBox jCheckBoxIsTagged;
    private javax.swing.JComboBox jComboBoxEncryption;
    private javax.swing.JComboBox jComboBoxPDFVersion;
    private javax.swing.JComboBox jComboBoxPdfA;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelEncryption;
    private javax.swing.JLabel jLabelMetadataAuthor;
    private javax.swing.JLabel jLabelMetadataCreator;
    private javax.swing.JLabel jLabelMetadataKeywords;
    private javax.swing.JLabel jLabelMetadataSubject;
    private javax.swing.JLabel jLabelMetadataTitle;
    private javax.swing.JLabel jLabelOwnerPassword;
    private javax.swing.JLabel jLabelPDFJavascript;
    private javax.swing.JLabel jLabelPDFVersion;
    private javax.swing.JLabel jLabelTagLanguage;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelUserPassword;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextAreaPDFJavascript;
    private javax.swing.JTextField jTextFieldICC;
    private javax.swing.JTextField jTextFieldMetadataAuthor;
    private javax.swing.JTextField jTextFieldMetadataCreator;
    private javax.swing.JTextField jTextFieldMetadataKeywords;
    private javax.swing.JTextField jTextFieldMetadataSubject;
    private javax.swing.JTextField jTextFieldMetadataTitle;
    private javax.swing.JTextField jTextFieldOwnerPassword;
    private javax.swing.JTextField jTextFieldTagLanguage;
    private javax.swing.JTextField jTextFieldUserPassword;
    // End of variables declaration//GEN-END:variables

    private void applyI18n()
    {
        jLabelTitle.setText(I18n.getString( "PdfExportParametersPanel.jLabelTitle.text")); // NOI18N
        jLabelPDFVersion.setText(I18n.getString( "PdfExportParametersPanel.jLabelPDFVersion.text")); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(I18n.getString( "PdfExportParametersPanel.jPanel2.border.title"))); // NOI18N
        jCheckBoxCreatingBatchModeBookmarks.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxCreatingBatchModeBookmarks.text")); // NOI18N

        jCheckBoxCompressed.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxCompressed.text")); // NOI18N
        jCheckBoxAllowPrinting.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxAllowPrinting.text")); // NOI18N
        jCheckBoxAllowModifyContents.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxAllowModifyContents.text")); // NOI18N
        jCheckBoxAllowCopy.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxAllowCopy.text")); // NOI18N
        jCheckBoxAllowModifyAnnotations.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxAllowModifyAnnotations.text")); // NOI18N
        jCheckBoxAllowFillIn.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxAllowFillIn.text")); // NOI18N
        jCheckBoxAllowScreenReaders.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxAllowScreenReaders.text")); // NOI18N
        jCheckBoxAllowAssembly.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxAllowAssembly.text")); // NOI18N
        jCheckBoxAllawDegradedPrinting.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxAllawDegradedPrinting.text")); // NOI18N

        jLabelMetadataTitle.setText(I18n.getString( "PdfExportParametersPanel.jLabelMetadataTitle.text")); // NOI18N
        jLabelMetadataAuthor.setText(I18n.getString( "PdfExportParametersPanel.jLabelMetadataAuthor.text")); // NOI18N
        jLabelMetadataSubject.setText(I18n.getString( "PdfExportParametersPanel.jLabelMetadataSubject.text")); // NOI18N
        jLabelMetadataKeywords.setText(I18n.getString( "PdfExportParametersPanel.jLabelMetadataKeywords.text")); // NOI18N
        jLabelMetadataCreator.setText(I18n.getString( "PdfExportParametersPanel.jLabelMetadataCreator.text")); // NOI18N

        jCheckBoxForceLinebreakPolicy.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxForceLinebreakPolicy.text")); // NOI18N
        jCheckBoxForceSVGShapes.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxForceSVGShapes.text")); // NOI18N
        jLabelPDFJavascript.setText(I18n.getString( "PdfExportParametersPanel.jLabelPDFJavascript.text")); // NOI18N
        jCheckBoxIsTagged.setText(I18n.getString( "PdfExportParametersPanel.jCheckBoxIsTagged.text")); // NOI18N
        jLabelTagLanguage.setText(I18n.getString( "PdfExportParametersPanel.jLabelTagLanguage.text")); // NOI18N

        jLabelEncryption.setText(I18n.getString( "PdfExportParametersPanel.jLabelEncryption.text")); // NOI18N
        jLabelOwnerPassword.setText(I18n.getString( "PdfExportParametersPanel.jLabelOwnerPassword.text")); // NOI18N
        jLabelUserPassword.setText(I18n.getString( "PdfExportParametersPanel.jLabelUserPassword.text")); // NOI18N
    }

    @Override
    public String getDisplayName() {
        return I18n.getString("PdfExportParametersPanel.title");
    }
}
