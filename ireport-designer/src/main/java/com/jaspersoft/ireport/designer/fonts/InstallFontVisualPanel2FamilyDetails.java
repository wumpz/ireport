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

package com.jaspersoft.ireport.designer.fonts;

import com.jaspersoft.ireport.designer.sheet.Tag;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public final class InstallFontVisualPanel2FamilyDetails extends JPanel {

    InstallFontWizardPanel2FamilyDetails wizardPanel = null;

    /** Creates new form InstallFontVisualPanel2 */
    public InstallFontVisualPanel2FamilyDetails(InstallFontWizardPanel2FamilyDetails wizardPanel) {
        initComponents();
        this.wizardPanel = wizardPanel;

        java.util.List<Tag> encodings = new ArrayList<Tag>();

        // Add regular PDF fonts...
        encodings.add(new Tag(null,"<default>"));
        encodings.add(new Tag("Cp1250","CP1250 (Central European)"));
        encodings.add(new Tag("Cp1251","CP1251 (Cyrillic)"));
        encodings.add(new Tag("Cp1252","CP1252 (Western European ANSI aka WinAnsi)"));
        encodings.add(new Tag("Cp1253","CP1253 (Greek)"));
        encodings.add(new Tag("Cp1254","CP1254 (Turkish)"));
        encodings.add(new Tag("Cp1255","CP1255 (Hebrew)"));
        encodings.add(new Tag("Cp1256","CP1256 (Arabic)"));
        encodings.add(new Tag("Cp1257","CP1257 (Baltic)"));
        encodings.add(new Tag("Cp1258","CP1258 (Vietnamese)"));
        encodings.add(new Tag("UniGB-UCS2-H","UniGB-UCS2-H (Chinese Simplified)"));
        encodings.add(new Tag("UniGB-UCS2-V","UniGB-UCS2-V (Chinese Simplified)"));
        encodings.add(new Tag("UniCNS-UCS2-H","UniCNS-UCS2-H (Chinese traditional)"));
        encodings.add(new Tag("UniCNS-UCS2-V","UniCNS-UCS2-V (Chinese traditional)"));
        encodings.add(new Tag("UniJIS-UCS2-H","UniJIS-UCS2-H (Japanese)"));
        encodings.add(new Tag("UniJIS-UCS2-V","UniJIS-UCS2-V (Japanese)"));
        encodings.add(new Tag("UniJIS-UCS2-HW-H","UniJIS-UCS2-HW-H (Japanese)"));
        encodings.add(new Tag("UniJIS-UCS2-HW-V","UniJIS-UCS2-HW-V (Japanese)"));
        encodings.add(new Tag("UniKS-UCS2-H","UniKS-UCS2-H (Korean)"));
        encodings.add(new Tag("UniKS-UCS2-V","UniKS-UCS2-V (Korean)"));
        encodings.add(new Tag("Identity-H","Identity-H (Unicode with horizontal writing)"));
        encodings.add(new Tag("Identity-V","Identity-V (Unicode with vertical writing)"));

        jComboBox1.setModel(new DefaultComboBoxModel( encodings.toArray()));
        jComboBox1.setSelectedIndex(0);

        DocumentListener dl = new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                notifyChange();
            }

            public void removeUpdate(DocumentEvent e) {
                notifyChange();
            }

            public void changedUpdate(DocumentEvent e) {
                notifyChange();
            }
        };

        jTextFieldFamilyName.getDocument().addDocumentListener(dl);
        jTextFieldTTFFontBold.getDocument().addDocumentListener(dl);
        jTextFieldTTFFontItalic.getDocument().addDocumentListener(dl);
        jTextFieldTTFFontBoldItalic.getDocument().addDocumentListener(dl);
    }

    public void notifyChange()
    {
        if (wizardPanel != null)
        {
            wizardPanel.fireChangeEvent();
        }
    }

    @Override
    public String getName() {
        return "Family Details";
    }

    public void validateForm() throws IllegalArgumentException
    {
        
        if (jTextFieldFamilyName.getText().trim().length() == 0) throw new IllegalArgumentException("Please specify a valid Family Name");

        validateFileName(jTextFieldTTFFontBold);
        validateFileName(jTextFieldTTFFontItalic);
        validateFileName(jTextFieldTTFFontBoldItalic);
    }

    public void validateFileName(JTextField textfield) throws IllegalArgumentException
    {
        if (textfield.getText().trim().length() > 0)
        {
            File f = new File(textfield.getText().trim());
            if (!f.exists())
            {
                throw new IllegalArgumentException("The file '" + textfield.getText() + "' does not exist.");
            }
        }
    }

    public void readSettings(Object settings) {
        jTextFieldFamilyName.setText( (String)((InstallFontWizardDescriptor)settings).getProperty("family_name"));
    }

    public void storeSettings(Object settings) {

        ((InstallFontWizardDescriptor)settings).putProperty("family_name", jTextFieldFamilyName.getText());
        ((InstallFontWizardDescriptor)settings).putProperty("bold_ttf_file", jTextFieldTTFFontBold.getText());
        ((InstallFontWizardDescriptor)settings).putProperty("italic_ttf_file", jTextFieldTTFFontItalic.getText());
        ((InstallFontWizardDescriptor)settings).putProperty("bolditalic_ttf_file", jTextFieldTTFFontBoldItalic.getText());

        if (jComboBox1.getSelectedItem() instanceof Tag)
        {
            ((InstallFontWizardDescriptor)settings).putProperty("pdf_encoding",  ((Tag)jComboBox1.getSelectedItem()).getValue());
        }
        else
        {
            ((InstallFontWizardDescriptor)settings).putProperty("pdf_encoding",  jComboBox1.getSelectedItem());
        }

        
        ((InstallFontWizardDescriptor)settings).putProperty("pdf_embedded", jCheckBoxEmbedded.isSelected()+"");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabelFamilyName = new javax.swing.JLabel();
        jTextFieldFamilyName = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabelTTFFontBold = new javax.swing.JLabel();
        jTextFieldTTFFontBold = new javax.swing.JTextField();
        jButtonBoldBrowse = new javax.swing.JButton();
        jLabelTTFFontItalic = new javax.swing.JLabel();
        jTextFieldTTFFontItalic = new javax.swing.JTextField();
        jButtonItalicBrowse = new javax.swing.JButton();
        jLabelTTFFontBoldItalic = new javax.swing.JLabel();
        jTextFieldTTFFontBoldItalic = new javax.swing.JTextField();
        jButtonBoldItalicBrowse = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabelPDFEncoding = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jCheckBoxEmbedded = new javax.swing.JCheckBox();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jLabel1.text")); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        org.openide.awt.Mnemonics.setLocalizedText(jLabelFamilyName, org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jLabelFamilyName.text")); // NOI18N

        jTextFieldFamilyName.setText(org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jTextFieldFamilyName.text")); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jPanel1.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jLabel2.text")); // NOI18N
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        org.openide.awt.Mnemonics.setLocalizedText(jLabelTTFFontBold, org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jLabelTTFFontBold.text")); // NOI18N

        jTextFieldTTFFontBold.setText(org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jTextFieldTTFFontBold.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonBoldBrowse, org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jButtonBoldBrowse.text")); // NOI18N
        jButtonBoldBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBoldBrowseActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabelTTFFontItalic, org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jLabelTTFFontItalic.text")); // NOI18N

        jTextFieldTTFFontItalic.setText(org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jTextFieldTTFFontItalic.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonItalicBrowse, org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jButtonItalicBrowse.text")); // NOI18N
        jButtonItalicBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonItalicBrowseActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabelTTFFontBoldItalic, org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jLabelTTFFontBoldItalic.text")); // NOI18N

        jTextFieldTTFFontBoldItalic.setText(org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jTextFieldTTFFontBoldItalic.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonBoldItalicBrowse, org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jButtonBoldItalicBrowse.text")); // NOI18N
        jButtonBoldItalicBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBoldItalicBrowseActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelTTFFontBold)
                            .add(jLabelTTFFontItalic)
                            .add(jLabelTTFFontBoldItalic))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jTextFieldTTFFontBoldItalic, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButtonBoldItalicBrowse))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(jTextFieldTTFFontBold, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextFieldTTFFontItalic, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jButtonBoldBrowse))
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jButtonItalicBrowse)))))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonBoldBrowse)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jTextFieldTTFFontBold, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabelTTFFontBold)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelTTFFontItalic)
                    .add(jTextFieldTTFFontItalic, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonItalicBrowse))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelTTFFontBoldItalic)
                    .add(jTextFieldTTFFontBoldItalic, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonBoldItalicBrowse))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jPanel2.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabelPDFEncoding, org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jLabelPDFEncoding.text")); // NOI18N

        jComboBox1.setEditable(true);

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxEmbedded, org.openide.util.NbBundle.getMessage(InstallFontVisualPanel2FamilyDetails.class, "InstallFontVisualPanel2FamilyDetails.jCheckBoxEmbedded.text")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxEmbedded)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabelPDFEncoding)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBox1, 0, 311, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelPDFEncoding)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBoxEmbedded)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jLabelFamilyName)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jTextFieldFamilyName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelFamilyName)
                    .add(jTextFieldFamilyName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBoldBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBoldBrowseActionPerformed
        InstallFontWizardDescriptor.browseForTTFFile(jTextFieldTTFFontBold);
}//GEN-LAST:event_jButtonBoldBrowseActionPerformed

    private void jButtonItalicBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonItalicBrowseActionPerformed
        InstallFontWizardDescriptor.browseForTTFFile(jTextFieldTTFFontItalic);
}//GEN-LAST:event_jButtonItalicBrowseActionPerformed

    private void jButtonBoldItalicBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBoldItalicBrowseActionPerformed
        InstallFontWizardDescriptor.browseForTTFFile(jTextFieldTTFFontBoldItalic);
}//GEN-LAST:event_jButtonBoldItalicBrowseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBoldBrowse;
    private javax.swing.JButton jButtonBoldItalicBrowse;
    private javax.swing.JButton jButtonItalicBrowse;
    private javax.swing.JCheckBox jCheckBoxEmbedded;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelFamilyName;
    private javax.swing.JLabel jLabelPDFEncoding;
    private javax.swing.JLabel jLabelTTFFontBold;
    private javax.swing.JLabel jLabelTTFFontBoldItalic;
    private javax.swing.JLabel jLabelTTFFontItalic;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextFieldFamilyName;
    private javax.swing.JTextField jTextFieldTTFFontBold;
    private javax.swing.JTextField jTextFieldTTFFontBoldItalic;
    private javax.swing.JTextField jTextFieldTTFFontItalic;
    // End of variables declaration//GEN-END:variables
}

