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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.util.prefs.Preferences;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;

/**
 *
 * @author gtoffoli
 */
public class TextExportParametersPanel extends AbstractExportParametersPanel {

    /** Creates new form OpenOfficeExportParametersPanel */
    public TextExportParametersPanel() {
        initComponents();

        jTextAreaBetweenPagesText.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        
        ChangeListener snmcl = new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    notifyChange();
                }
        };

        jSpinnerCharacterWidth.getModel().addChangeListener(snmcl);
        jSpinnerCharacterHeight.getModel().addChangeListener(snmcl);

        SpinnerNumberModel snm = new SpinnerNumberModel(0,0, Integer.MAX_VALUE,1);
        jSpinnerPageWidth.setModel(snm);
        snm.addChangeListener(snmcl);

        snm = new SpinnerNumberModel(0,0, Integer.MAX_VALUE,1);
        jSpinnerPageHeight.setModel(snm);
        snm.addChangeListener(snmcl);

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

         jTextAreaBetweenPagesText.getDocument().addDocumentListener(new javax.swing.event.DocumentListener()
        {
            public void changedUpdate(javax.swing.event.DocumentEvent evt)
            {
                if (jTextAreaBetweenPagesText.getText().length() > 0)
                {
                    jCheckBoxNothingBetweenPages.setSelected(false);
                }
                notifyChange();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt)
            {
                if (jTextAreaBetweenPagesText.getText().length() > 0)
                {
                    jCheckBoxNothingBetweenPages.setSelected(false);
                }
                notifyChange();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt)
            {
                if (jTextAreaBetweenPagesText.getText().length() > 0)
                {
                    jCheckBoxNothingBetweenPages.setSelected(false);
                }
                notifyChange();
            } });
         jTextFieldLineSeparator.getDocument().addDocumentListener(textfieldListener);

         jLabelNote.setText(I18n.getString("TextExportParametersPanel.jLabelNode.text", Misc.addSlashesString(System.getProperty("line.separator"))));
         applyI18n();
    }

    public void applyI18n()
    {
        jLabelTitle.setText(I18n.getString("TextExportParametersPanel.jLabelTitle.text")); // NOI18N
        jLabelCharacterWidth.setText(I18n.getString("TextExportParametersPanel.jLabelCharacterWidth.text")); // NOI18N
        jLabelCharacterHeight.setText(I18n.getString("TextExportParametersPanel.jLabelCharacterHeight.text")); // NOI18N
        jLabelPageWidth.setText(I18n.getString("TextExportParametersPanel.jLabelPageWidth.text")); // NOI18N
        jLabelPageHeight.setText(I18n.getString("TextExportParametersPanel.jLabelPageHeight.text")); // NOI18N
        jLabelBetweenPagesText.setText(I18n.getString("TextExportParametersPanel.jLabelBetweenPagesText.text")); // NOI18N
        jLabelLineSeparator.setText(I18n.getString("TextExportParametersPanel.jLabelLineSeparator.text")); // NOI18N
        jLabelDefault1.setText(I18n.getString("TextExportParametersPanel.jLabelDefault1.text")); // NOI18N
        jLabelDefault2.setText(I18n.getString("TextExportParametersPanel.jLabelDefault2.text")); // NOI18N
        jLabelDefault3.setText(I18n.getString("TextExportParametersPanel.jLabelDefault3.text")); // NOI18N
        jLabelDefault4.setText(I18n.getString("TextExportParametersPanel.jLabelDefault4.text")); // NOI18N
        jLabelDefault5.setText(I18n.getString("TextExportParametersPanel.jLabelDefault5.text")); // NOI18N
        jCheckBoxNothingBetweenPages.setText(I18n.getString("TextExportParametersPanel.jCheckBoxNothingBetweenPages.text")); //NOI18N

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
        jLabelCharacterWidth = new javax.swing.JLabel();
        jSpinnerCharacterWidth = new javax.swing.JSpinner();
        jLabelCharacterHeight = new javax.swing.JLabel();
        jSpinnerCharacterHeight = new javax.swing.JSpinner();
        jLabelPageWidth = new javax.swing.JLabel();
        jSpinnerPageWidth = new javax.swing.JSpinner();
        jLabelPageHeight = new javax.swing.JLabel();
        jSpinnerPageHeight = new javax.swing.JSpinner();
        jLabelBetweenPagesText = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaBetweenPagesText = new javax.swing.JTextArea();
        jLabelLineSeparator = new javax.swing.JLabel();
        jTextFieldLineSeparator = new javax.swing.JTextField();
        jLabelDefault1 = new javax.swing.JLabel();
        jLabelDefault2 = new javax.swing.JLabel();
        jLabelDefault3 = new javax.swing.JLabel();
        jLabelDefault4 = new javax.swing.JLabel();
        jLabelDefault5 = new javax.swing.JLabel();
        jCheckBoxNothingBetweenPages = new javax.swing.JCheckBox();
        jLabelNote = new javax.swing.JLabel();

        jLabelTitle.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabelTitle.setText("Text Export parameters");

        jLabelCharacterWidth.setText("Character Width");

        jSpinnerCharacterWidth.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), null, Float.valueOf(1.0f)));

        jLabelCharacterHeight.setText("Character Height");

        jSpinnerCharacterHeight.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), null, Float.valueOf(1.0f)));

        jLabelPageWidth.setText("Page Width");

        jLabelPageHeight.setText("Page Height");

        jLabelBetweenPagesText.setText("Between Pages Text");

        jTextAreaBetweenPagesText.setColumns(20);
        jTextAreaBetweenPagesText.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTextAreaBetweenPagesText.setRows(5);
        jScrollPane1.setViewportView(jTextAreaBetweenPagesText);

        jLabelLineSeparator.setText("Line Separator");

        jLabelDefault1.setText("(0 to use default)");

        jLabelDefault2.setText("(0 to use default)");

        jLabelDefault3.setText("(0 to use default)");

        jLabelDefault4.setText("(0 to use default)");

        jLabelDefault5.setText("Usually \"\\n\" in Unix and \"\\r\\n\" in Windows");

        jCheckBoxNothingBetweenPages.setText("Don't put any text between pages");
        jCheckBoxNothingBetweenPages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxNothingBetweenPagesActionPerformed(evt);
            }
        });

        jLabelNote.setText("Default new line: {0}. For specific new line characters, use \\n or \\r\\n.");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabelTitle)
                .addContainerGap(275, Short.MAX_VALUE))
            .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelBetweenPagesText)
                .addContainerGap(311, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jLabelLineSeparator)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextFieldLineSeparator))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelCharacterWidth)
                            .add(jLabelCharacterHeight)
                            .add(jLabelPageWidth)
                            .add(jLabelPageHeight))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jSpinnerCharacterHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jSpinnerPageWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jSpinnerPageHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jSpinnerCharacterWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 81, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelDefault1)
                    .add(jLabelDefault2)
                    .add(jLabelDefault3)
                    .add(jLabelDefault4)
                    .add(jLabelDefault5))
                .addContainerGap(42, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBoxNothingBetweenPages)
                .addContainerGap(223, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelNote)
                .addContainerGap(78, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {jSpinnerCharacterHeight, jSpinnerCharacterWidth, jSpinnerPageHeight, jSpinnerPageWidth}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabelTitle)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelCharacterWidth)
                    .add(jSpinnerCharacterWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelDefault1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelCharacterHeight)
                    .add(jSpinnerCharacterHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelDefault2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelPageWidth)
                    .add(jSpinnerPageWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelDefault3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelPageHeight)
                    .add(jSpinnerPageHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelDefault4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelLineSeparator)
                    .add(jTextFieldLineSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelDefault5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabelBetweenPagesText)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabelNote)
                .add(12, 12, 12)
                .add(jCheckBoxNothingBetweenPages)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxNothingBetweenPagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxNothingBetweenPagesActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxNothingBetweenPagesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBoxNothingBetweenPages;
    private javax.swing.JLabel jLabelBetweenPagesText;
    private javax.swing.JLabel jLabelCharacterHeight;
    private javax.swing.JLabel jLabelCharacterWidth;
    private javax.swing.JLabel jLabelDefault1;
    private javax.swing.JLabel jLabelDefault2;
    private javax.swing.JLabel jLabelDefault3;
    private javax.swing.JLabel jLabelDefault4;
    private javax.swing.JLabel jLabelDefault5;
    private javax.swing.JLabel jLabelLineSeparator;
    private javax.swing.JLabel jLabelNote;
    private javax.swing.JLabel jLabelPageHeight;
    private javax.swing.JLabel jLabelPageWidth;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner jSpinnerCharacterHeight;
    private javax.swing.JSpinner jSpinnerCharacterWidth;
    private javax.swing.JSpinner jSpinnerPageHeight;
    private javax.swing.JSpinner jSpinnerPageWidth;
    private javax.swing.JTextArea jTextAreaBetweenPagesText;
    private javax.swing.JTextField jTextFieldLineSeparator;
    // End of variables declaration//GEN-END:variables

    public void load() {
        setInit(true);
        Preferences pref = IReportManager.getPreferences();
        
        SpinnerNumberModel model = (SpinnerNumberModel)jSpinnerCharacterHeight.getModel();
        model.setValue( pref.getFloat(JRTextExporterParameter.PROPERTY_CHARACTER_HEIGHT, 0));

        model = (SpinnerNumberModel)jSpinnerCharacterWidth.getModel();
        model.setValue( pref.getFloat(JRTextExporterParameter.PROPERTY_CHARACTER_WIDTH, 0));

        model = (SpinnerNumberModel)jSpinnerPageHeight.getModel();
        model.setValue( pref.getInt(JRTextExporterParameter.PROPERTY_PAGE_HEIGHT, 0));

        model = (SpinnerNumberModel)jSpinnerPageWidth.getModel();
        model.setValue( pref.getInt(JRTextExporterParameter.PROPERTY_PAGE_WIDTH, 0));

        jTextAreaBetweenPagesText.setText(Misc.addSlashesString(pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.txt.betweenPagesText", "")));
        jCheckBoxNothingBetweenPages.setSelected(pref.getBoolean(JRPropertiesUtil.PROPERTY_PREFIX + "export.txt.nothingBetweenPages", false)); // This is an iReport specific option!

        jTextFieldLineSeparator.setText( Misc.addSlashesString(pref.get(JRPropertiesUtil.PROPERTY_PREFIX + "export.txt.lineSeparator", System.getProperty("line.separator"))));
        setInit(false);
    }

    public void store() {

        Preferences pref = IReportManager.getPreferences();

        SpinnerNumberModel model = (SpinnerNumberModel)jSpinnerCharacterHeight.getModel();
        pref.putFloat(JRTextExporterParameter.PROPERTY_CHARACTER_HEIGHT, model.getNumber().floatValue());

        model = (SpinnerNumberModel)jSpinnerCharacterWidth.getModel();
        pref.putFloat(JRTextExporterParameter.PROPERTY_CHARACTER_WIDTH, model.getNumber().floatValue());

        model = (SpinnerNumberModel)jSpinnerPageHeight.getModel();
        pref.putInt(JRTextExporterParameter.PROPERTY_PAGE_HEIGHT, model.getNumber().intValue());

        model = (SpinnerNumberModel)jSpinnerPageWidth.getModel();
        pref.putInt(JRTextExporterParameter.PROPERTY_PAGE_WIDTH, model.getNumber().intValue());

        
        pref.put(JRPropertiesUtil.PROPERTY_PREFIX + "export.txt.betweenPagesText", Misc.removeSlashesString(jTextAreaBetweenPagesText.getText()));
        pref.put(JRPropertiesUtil.PROPERTY_PREFIX + "export.txt.lineSeparator", Misc.removeSlashesString(jTextFieldLineSeparator.getText()));

        pref.putBoolean(JRPropertiesUtil.PROPERTY_PREFIX + "export.txt.nothingBetweenPages", jCheckBoxNothingBetweenPages.isSelected());
 
    }

    public boolean valid() {
        return true;
    }

    @Override
    public String getDisplayName() {
        return I18n.getString("TextExportParametersPanel.title");
    }
}
