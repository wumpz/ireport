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

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IRFont;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ReportClassLoader;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.tools.JNumberComboBox;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRFontUtil;


/**
 *
 * @author  gtoffoli
 */
public class JRFontPanel extends javax.swing.JPanel implements PreferenceChangeListener {
    
    private JDialog dialog = null;
    
    private JRFont jRFont = new JRBaseFont();
    private boolean init = false;
    private boolean reportFontMode = false;
    
    
    public void fillReportFonts(JasperDesign jasperDesign)
    {
        if (jasperDesign != null)
        {
            // TODO: fill the list of JRReportFont...
            @SuppressWarnings("deprecation")
            List reportFonts = new ArrayList(); //jasperDesign.getFontsList();
            for (int i=0;i<reportFonts.size(); ++i)
            {
                 Misc.updateComboBox( jComboBoxReportFonts, reportFonts, true);
            }
        }
    }
    /** Creates new form JRFontPanel */
    public JRFontPanel() {
        initComponents();
        
        jPanel6.setVisible(false);
        
        ((JNumberComboBox)jNumberComboBoxSize).addEntry(I18n.getString("Global.ComboBox.3"),3);
        ((JNumberComboBox)jNumberComboBoxSize).addEntry(I18n.getString("Global.ComboBox.5"),5);
        ((JNumberComboBox)jNumberComboBoxSize).addEntry(I18n.getString("Global.ComboBox.8"),8);
        ((JNumberComboBox)jNumberComboBoxSize).addEntry(I18n.getString("Global.ComboBox.10"),10);
        ((JNumberComboBox)jNumberComboBoxSize).addEntry(I18n.getString("Global.ComboBox.12"),12);
        ((JNumberComboBox)jNumberComboBoxSize).addEntry(I18n.getString("Global.ComboBox.14"),14);
        ((JNumberComboBox)jNumberComboBoxSize).addEntry(I18n.getString("Global.ComboBox.18"),18);
        ((JNumberComboBox)jNumberComboBoxSize).addEntry(I18n.getString("Global.ComboBox.24"),24);
        ((JNumberComboBox)jNumberComboBoxSize).addEntry(I18n.getString("Global.ComboBox.36"),36);
        ((JNumberComboBox)jNumberComboBoxSize).addEntry(I18n.getString("Global.ComboBox.48"),48);
        
        jNumberComboBoxSize.setSelectedIndex(3);
                
        jComboBoxPdfEncoding.addItem(new Tag("Cp1250","CP1250 (Central European)"));
        jComboBoxPdfEncoding.addItem(new Tag("Cp1251","CP1251 (Cyrillic)"));
        jComboBoxPdfEncoding.addItem(new Tag("Cp1252","CP1252 (Western European ANSI aka WinAnsi)"));
        jComboBoxPdfEncoding.addItem(new Tag("Cp1253","CP1253 (Greek)"));
        jComboBoxPdfEncoding.addItem(new Tag("Cp1254","CP1254 (Turkish)"));
        jComboBoxPdfEncoding.addItem(new Tag("Cp1255","CP1255 (Hebrew)"));
        jComboBoxPdfEncoding.addItem(new Tag("Cp1256","CP1256 (Arabic)"));
        jComboBoxPdfEncoding.addItem(new Tag("Cp1257","CP1257 (Baltic)"));
        jComboBoxPdfEncoding.addItem(new Tag("Cp1258","CP1258 (Vietnamese)"));
        jComboBoxPdfEncoding.addItem(new Tag("UniGB-UCS2-H","UniGB-UCS2-H (Chinese Simplified)"));
        jComboBoxPdfEncoding.addItem(new Tag("UniGB-UCS2-V","UniGB-UCS2-V (Chinese Simplified)"));
        jComboBoxPdfEncoding.addItem(new Tag("UniCNS-UCS2-H","UniCNS-UCS2-H (Chinese traditional)"));
        jComboBoxPdfEncoding.addItem(new Tag("UniCNS-UCS2-V","UniCNS-UCS2-V (Chinese traditional)"));
        jComboBoxPdfEncoding.addItem(new Tag("UniJIS-UCS2-H","UniJIS-UCS2-H (Japanese)"));
        jComboBoxPdfEncoding.addItem(new Tag("UniJIS-UCS2-V","UniJIS-UCS2-V (Japanese)"));
        jComboBoxPdfEncoding.addItem(new Tag("UniJIS-UCS2-HW-H","UniJIS-UCS2-HW-H (Japanese)"));
        jComboBoxPdfEncoding.addItem(new Tag("UniJIS-UCS2-HW-V","UniJIS-UCS2-HW-V (Japanese)"));
        jComboBoxPdfEncoding.addItem(new Tag("UniKS-UCS2-H","UniKS-UCS2-H (Korean)"));
        jComboBoxPdfEncoding.addItem(new Tag("UniKS-UCS2-V","UniKS-UCS2-V (Korean)"));
        jComboBoxPdfEncoding.addItem(new Tag("Identity-H","Identity-H (Unicode with horizontal writing)"));
        jComboBoxPdfEncoding.addItem(new Tag("Identity-V","Identity-V (Unicode with vertical writing)"));
        
        // Load Fonts...
        updateStandardFonts();
        updateFonts();

        jComboBoxReportFonts.setVisible(false);
        setReportFontMode(reportFontMode);

        IReportManager.getPreferences().addPreferenceChangeListener(this);
    }

    public void updateStandardFonts()
    {
        jComboBoxFontName.removeAllItems();

        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(new ReportClassLoader(IReportManager.getReportClassLoader()));

        Collection extensionFonts = JRFontUtil.getFontFamilyNames();
        for(Iterator it = extensionFonts.iterator(); it.hasNext();)
        {
            String fname = (String)it.next();
            System.out.println("JR specific family: " + fname);
            jComboBoxFontName.addItem(fname);
        }

        Thread.currentThread().setContextClassLoader(oldCL);

        String[] fontFamilies = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (int i=0; i<fontFamilies.length; ++i)
        {
            jComboBoxFontName.addItem(fontFamilies[i]);
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

        jPanelFont = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jComboBoxFontName = new javax.swing.JComboBox();
        jNumberComboBoxSize = new JNumberComboBox();
        jLabel25 = new javax.swing.JLabel();
        jComboBoxPDFFontName = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxBold = new javax.swing.JCheckBox();
        jCheckBoxUnderline = new javax.swing.JCheckBox();
        jCheckBoxItalic = new javax.swing.JCheckBox();
        jCheckBoxStrikeThrough = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jCheckBoxPDFEmbedded = new javax.swing.JCheckBox();
        jCheckBoxDefaultFont = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jComboBoxPdfEncoding = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        jTextFieldReportFont = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonResetAllToDefault = new javax.swing.JButton();
        jComboBoxReportFonts = new javax.swing.JComboBox();
        jSeparator4 = new javax.swing.JSeparator();

        setLayout(new java.awt.GridBagLayout());

        jPanelFont.setLayout(new java.awt.GridBagLayout());

        jLabel24.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jLabel24.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanelFont.add(jLabel24, gridBagConstraints);

        jLabel27.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jLabel27.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanelFont.add(jLabel27, gridBagConstraints);

        jComboBoxFontName.setEditable(true);
        jComboBoxFontName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFontNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelFont.add(jComboBoxFontName, gridBagConstraints);

        jNumberComboBoxSize.setEditable(true);
        jNumberComboBoxSize.setMinimumSize(new java.awt.Dimension(70, 22));
        jNumberComboBoxSize.setPreferredSize(new java.awt.Dimension(70, 22));
        jNumberComboBoxSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberComboBoxSizeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanelFont.add(jNumberComboBoxSize, gridBagConstraints);

        jLabel25.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jLabel25.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanelFont.add(jLabel25, gridBagConstraints);

        jComboBoxPDFFontName.setEditable(true);
        jComboBoxPDFFontName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPDFFontNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelFont.add(jComboBoxPDFFontName, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jCheckBoxBold.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jCheckBoxBold.text")); // NOI18N
        jCheckBoxBold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxBoldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jCheckBoxBold, gridBagConstraints);

        jCheckBoxUnderline.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jCheckBoxUnderline.text")); // NOI18N
        jCheckBoxUnderline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUnderlineActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jCheckBoxUnderline, gridBagConstraints);

        jCheckBoxItalic.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jCheckBoxItalic.text")); // NOI18N
        jCheckBoxItalic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxItalicActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jCheckBoxItalic, gridBagConstraints);

        jCheckBoxStrikeThrough.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jCheckBoxStrikeThrough.text")); // NOI18N
        jCheckBoxStrikeThrough.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxStrikeThroughActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jCheckBoxStrikeThrough, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelFont.add(jPanel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelFont.add(jSeparator2, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jCheckBoxPDFEmbedded.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jCheckBoxPDFEmbedded.text")); // NOI18N
        jCheckBoxPDFEmbedded.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPDFEmbeddedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(jCheckBoxPDFEmbedded, gridBagConstraints);

        jCheckBoxDefaultFont.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jCheckBoxDefaultFont.text")); // NOI18N
        jCheckBoxDefaultFont.setEnabled(false);
        jCheckBoxDefaultFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDefaultFontActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(jCheckBoxDefaultFont, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel4, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel32.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jLabel32.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jLabel32, gridBagConstraints);

        jComboBoxPdfEncoding.setEditable(true);
        jComboBoxPdfEncoding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPdfEncodingActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jComboBoxPdfEncoding, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        jPanel3.add(jPanel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanelFont.add(jPanel3, gridBagConstraints);

        jLabel23.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jLabel23.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelFont.add(jLabel23, gridBagConstraints);

        jTextFieldReportFont.setPreferredSize(new java.awt.Dimension(360, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelFont.add(jTextFieldReportFont, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        jPanelFont.add(jSeparator3, gridBagConstraints);

        jPanel6.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel6.add(jPanel7, gridBagConstraints);

        jButtonOK.setMnemonic('o');
        jButtonOK.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jButtonOK.text")); // NOI18N
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(jButtonOK, gridBagConstraints);

        jButtonCancel.setMnemonic('c');
        jButtonCancel.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jButtonCancel.text")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        jPanel6.add(jButtonCancel, gridBagConstraints);

        jButtonResetAllToDefault.setMnemonic('d');
        jButtonResetAllToDefault.setText(org.openide.util.NbBundle.getMessage(JRFontPanel.class, "JRFontPanel.jButtonResetAllToDefault.text")); // NOI18N
        jButtonResetAllToDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetAllToDefaultjButtonOKActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(jButtonResetAllToDefault, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelFont.add(jPanel6, gridBagConstraints);

        jComboBoxReportFonts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxReportFontsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelFont.add(jComboBoxReportFonts, gridBagConstraints);

        jSeparator4.setPreferredSize(new java.awt.Dimension(360, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 4, 0);
        jPanelFont.add(jSeparator4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jPanelFont, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    private void jComboBoxFontNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFontNameActionPerformed
        if (init) return;
        getJRFont().setFontName( jComboBoxFontName.getSelectedItem()+"");
    }//GEN-LAST:event_jComboBoxFontNameActionPerformed

    private void jNumberComboBoxSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberComboBoxSizeActionPerformed
        if (init) return;
        jRFont.setFontSize( (int)((JNumberComboBox)jNumberComboBoxSize).getValue());
    }//GEN-LAST:event_jNumberComboBoxSizeActionPerformed

    private void jComboBoxPDFFontNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPDFFontNameActionPerformed
        if (init) return;
        // Set band to all....
        if (jComboBoxPDFFontName.getSelectedItem() == null || (jComboBoxPDFFontName.getSelectedItem()+"").equals("")) return;
        // Set the new value for all selected elements...
        Object obj = jComboBoxPDFFontName.getSelectedItem();
        String fontName = ""+ obj;
        if (obj instanceof Tag) {
            fontName = ""+((Tag)obj).getValue();
        } else {
            fontName = ""+obj;
        }
        
        jRFont.setPdfFontName( (fontName.length() > 0) ? fontName : null);
    }//GEN-LAST:event_jComboBoxPDFFontNameActionPerformed

    private void jCheckBoxBoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxBoldActionPerformed
        if (init) return;
        jRFont.setBold( jCheckBoxBold.isSelected() );
    }//GEN-LAST:event_jCheckBoxBoldActionPerformed

    private void jCheckBoxUnderlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxUnderlineActionPerformed
        if (init) return;
        jRFont.setUnderline( jCheckBoxUnderline.isSelected() );
    }//GEN-LAST:event_jCheckBoxUnderlineActionPerformed

    private void jCheckBoxItalicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxItalicActionPerformed
        if (init) return;
        jRFont.setItalic( jCheckBoxItalic.isSelected() );
    }//GEN-LAST:event_jCheckBoxItalicActionPerformed

    private void jCheckBoxStrikeThroughActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxStrikeThroughActionPerformed
        if (init) return;
        jRFont.setStrikeThrough( jCheckBoxStrikeThrough.isSelected()  );
    }//GEN-LAST:event_jCheckBoxStrikeThroughActionPerformed

    private void jCheckBoxPDFEmbeddedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPDFEmbeddedActionPerformed
        if (init) return;
        jRFont.setPdfEmbedded( jCheckBoxPDFEmbedded.isSelected() );
    }//GEN-LAST:event_jCheckBoxPDFEmbeddedActionPerformed

    private void jCheckBoxDefaultFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDefaultFontActionPerformed
        if (init) return;
        //jRFont.setDefaultFont( jCheckBoxDefaultFont.isSelected() );
    }//GEN-LAST:event_jCheckBoxDefaultFontActionPerformed

    private void jComboBoxPdfEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPdfEncodingActionPerformed
        if (init) return;
        if (this.jComboBoxPdfEncoding.getSelectedItem() instanceof Tag) {
            jRFont.setPdfEncoding( ""+((Tag)this.jComboBoxPdfEncoding.getSelectedItem()).getValue());
        } else {
            String s = ""+this.jComboBoxPdfEncoding.getSelectedItem();
            if (s != null && s.length() == 0) {
                jRFont.setPdfEncoding(s);
            } else {
                this.jComboBoxPdfEncoding.setSelectedItem(jRFont.getPdfEncoding());
            }
            
        }
        // else
        //     jRFont.setPdfEncoding( Misc.nvl(this.jComboBoxPdfEncoding.getSelectedItem(),"CP1251"));
    }//GEN-LAST:event_jComboBoxPdfEncodingActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        if (this.jTextFieldReportFont.getText().trim().length() <= 0 && isReportFontMode()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getString("JRFontPanel.Message.Warning"),
                    I18n.getString("JRFontPanel.Message.Error"),
                    javax.swing.JOptionPane.WARNING_MESSAGE );
            return;
        }
        
        /*
        if (isReportFontMode())
        {
            jRFont.setReportFont( this.jTextFieldReportFont.getText() );
        }
        else if (this.jComboBoxReportFonts.isVisible() && this.jComboBoxReportFonts.getSelectedItem() != null)
        {
            jRFont.setReportFont( this.jComboBoxReportFonts.getSelectedItem() +"");
        }
         */
        
        if (dialog != null)
        {
            this.setDialogResult( javax.swing.JOptionPane.OK_OPTION);
            dialog.setVisible(false);
            dialog.dispose();
        }
    }//GEN-LAST:event_jButtonOKActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        
        if (dialog != null)
        {
            dialog.setVisible(false);
            setDialogResult( javax.swing.JOptionPane.CANCEL_OPTION);
            dialog.dispose();
        }
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonResetAllToDefaultjButtonOKActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetAllToDefaultjButtonOKActionPerformed1
        JRFont ifont = new JRBaseFont();
        setJRFont(ifont, jTextFieldReportFont.isVisible());
    }//GEN-LAST:event_jButtonResetAllToDefaultjButtonOKActionPerformed1

    private void jComboBoxReportFontsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxReportFontsActionPerformed
        if (init) return;
        /*
        if (jComboBoxReportFonts.getSelectedItem() != null && jComboBoxReportFonts.getSelectedItem() instanceof IReportFont)
        {
            // Set all fields to the value of the report font...
            //IReportFont ifont = (IReportFont)((IReportFont)jComboBoxReportFonts.getSelectedItem()).clone();
         
            //setJRFont(ifont, false);
            // Remove all values...
            //ifont.getBeanProperties().clear();
            //ifont.setReportFont(  jComboBoxReportFonts.getSelectedItem()+"" );
        }
         */
    }//GEN-LAST:event_jComboBoxReportFontsActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JButton jButtonResetAllToDefault;
    private javax.swing.JCheckBox jCheckBoxBold;
    private javax.swing.JCheckBox jCheckBoxDefaultFont;
    private javax.swing.JCheckBox jCheckBoxItalic;
    private javax.swing.JCheckBox jCheckBoxPDFEmbedded;
    private javax.swing.JCheckBox jCheckBoxStrikeThrough;
    private javax.swing.JCheckBox jCheckBoxUnderline;
    private javax.swing.JComboBox jComboBoxFontName;
    private javax.swing.JComboBox jComboBoxPDFFontName;
    private javax.swing.JComboBox jComboBoxPdfEncoding;
    private javax.swing.JComboBox jComboBoxReportFonts;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JComboBox jNumberComboBoxSize;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanelFont;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField jTextFieldReportFont;
    // End of variables declaration//GEN-END:variables
    
    
    private int dialogResult;  
    
    /** Getter for property dialogResult.
     * @return Value of property dialogResult.
     *
     */
    public int getDialogResult() {
        return dialogResult;
    }
    
    /** Setter for property dialogResult.
     * @param dialogResult New value of property dialogResult.
     *
     */
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
    
    /** Getter for property jRFont.
     * @return Value of property jRFont.
     *
     */
    public JRFont getJRFont() {
        return jRFont;
    }
    
    
    public void setJRFont(JRFont jRFont)
    {
       setJRFont(jRFont, true);
    }
    /** Setter for property jRFont.
     * @param jRFont New value of property jRFont.
     *
     */
    public void setJRFont(JRFont jRFont, boolean reportFontToo) {
       
        init = true;
        
        if (jRFont == null) jRFont = new JRBaseFont();
        this.jRFont = jRFont;
        
        /*
        if (reportFontToo)
        {
            this.jTextFieldReportFont.setText( new String(jRFont.getReportFont()) );
            if (jRFont.getReportFont() != null && jRFont.getReportFont().length()>0)
            {
                for (int i=0; i<jComboBoxReportFonts.getItemCount(); ++i)
                {
                    if ((jComboBoxReportFonts.getItemAt(i) + "").equals(jRFont.getReportFont()))
                    {
                        jComboBoxReportFonts.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
        */
        
        this.jCheckBoxBold.setSelected( jRFont.isBold());
        this.jCheckBoxItalic.setSelected( jRFont.isItalic());
        this.jCheckBoxStrikeThrough.setSelected( jRFont.isStrikeThrough());
        this.jCheckBoxPDFEmbedded.setSelected( jRFont.isPdfEmbedded() );
        this.jCheckBoxUnderline.setSelected( jRFont.isUnderline());
        
        //this.jCheckBoxDefaultFont.setSelected( jRFont.isDefaultFont());

        setComboBoxText(true, jRFont.getFontName() , jComboBoxFontName);
        this.setMixedTagComboBox(true, jRFont.getPdfFontName() , jComboBoxPDFFontName );
        this.setElementComboNumber(true, (double)jRFont.getFontSize(), (JNumberComboBox)jNumberComboBoxSize);
        this.setPdfEncodingComboBox(true, jRFont.getPdfEncoding() , jComboBoxPdfEncoding );
        
        init = false;
    }
    
    @SuppressWarnings("unchecked")
    public void updateFonts()
    {
        List fontsVec = new ArrayList();
        
        // Add regular PDF fonts...
        fontsVec.add(new Tag("Helvetica"));
        fontsVec.add(new Tag("Helvetica-Bold"));
        fontsVec.add(new Tag("Helvetica-BoldOblique"));
        fontsVec.add(new Tag("Helvetica-Oblique"));
        fontsVec.add(new Tag("Courier"));
        fontsVec.add(new Tag("Courier-Bold"));
        fontsVec.add(new Tag("Courier-BoldOblique"));
        fontsVec.add(new Tag("Courier-Oblique"));
        fontsVec.add(new Tag("Symbol"));
        fontsVec.add(new Tag("Times-Roman"));
        fontsVec.add(new Tag("Times-Bold"));
        fontsVec.add(new Tag("Times-BoldItalic"));
        fontsVec.add(new Tag("Times-Italic"));
        fontsVec.add(new Tag("ZapfDingbats"));
        fontsVec.add(new Tag("STSong-Light"));
        fontsVec.add(new Tag("MHei-Medium"));
        fontsVec.add(new Tag("MSung-Light"));
        fontsVec.add(new Tag("HeiseiKakuGo-W5"));
        fontsVec.add(new Tag("HeiseiMin-W3"));
        fontsVec.add(new Tag("HYGoThic-Medium"));
        fontsVec.add(new Tag("HYSMyeongJo-Medium"));

        List<IRFont> fonts = IReportManager.getInstance().getIRFonts();

        for (IRFont f : fonts)
        {
            fontsVec.add(new Tag(f.getFile(), f.toString()));
        }
        
        Misc.updateComboBox(jComboBoxPDFFontName,fontsVec);
    } 
    
    protected boolean setComboBoxText( boolean firstTime, String value, javax.swing.JComboBox comboField )
    {       
      if (( ! firstTime ) && (!( Misc.nvl(comboField.getSelectedItem(),"").equalsIgnoreCase(value))))
      {
        comboField.setSelectedIndex(0);
        return false;
      }
      else
      {
          try {
            comboField.setSelectedItem( value );		 
          } catch (Exception ex){
            ex.printStackTrace();
          }
      }
      return true;
    }
    
    
     protected boolean setMixedTagComboBox( boolean firstTime, Object value, javax.swing.JComboBox comboField ) {
        if (firstTime)
        {
            try {
                for (int i=0; i<comboField.getItemCount(); ++i) {
                    if (comboField.getItemAt(i) instanceof Tag && ((Tag)comboField.getItemAt(i)).getValue().equals(value) ) {
                        comboField.setSelectedIndex(i);
                        return true;
                    }
                }
                // No tag found...
                comboField.setSelectedItem(value);
                
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return true;
        }
        else
        {
            Object selectedValue = comboField.getSelectedItem();
            if (selectedValue == null && value == null) return true;
            if (selectedValue == null) return false;
            if (selectedValue instanceof Tag)
            {
                selectedValue = ((Tag)selectedValue).getValue();
            }
            
            if (selectedValue.equals(value)) return true;
        }
        return false;
    }
    
    protected boolean setPdfEncodingComboBox( boolean firstTime, String value, javax.swing.JComboBox comboField )
    {       
      if (( ! firstTime ) && 
          ( !(comboField.getSelectedItem()!=null &&
              ( (comboField.getSelectedItem() instanceof Tag &&
                 (((Tag)comboField.getSelectedItem()).getValue()+"").equalsIgnoreCase(value)) || 
                (comboField.getSelectedItem()+"").equals(value) )
               )))
      {
        if (comboField.getItemCount() > 0)
            comboField.setSelectedIndex(0);
        return false;
      }
      else
      {
          try {
              for (int i=0; i<comboField.getItemCount(); ++i)
              {
                  if (comboField.getItemAt(i) instanceof Tag && ((Tag)comboField.getItemAt(i)).getValue().equals(value) )
                  {
                      comboField.setSelectedIndex(i);
                      return true;
                   }
              }	 
              comboField.setSelectedItem(value);   
          } catch (Exception ex){
            ex.printStackTrace();
          }
      }
      return true;
    }
    
    protected boolean setElementComboNumber( boolean firstTime, double value, JNumberComboBox numberField )
    {
      if (( ! firstTime ) && (!(numberField.getValue() == value)))
      {
        numberField.setSelectedItem("");
        return false;
      }
      else
      {
          try {
            numberField.setValue( value );		 
          } catch (Exception ex){
            ex.printStackTrace();
          }
      }
      return true;
    }

    public boolean isReportFontMode() {
        return reportFontMode;
    }

    public void setReportFontMode(boolean reportFontMode) {
        setReportFontMode( reportFontMode ? 1 : 0);
    }
    
    public void setReportFontMode(int reportFontMode) {
        this.reportFontMode = reportFontMode == 1;
        
        jComboBoxReportFonts.setVisible( reportFontMode == 0 );
        jTextFieldReportFont.setVisible( reportFontMode == 1 );
        jCheckBoxDefaultFont.setEnabled( reportFontMode == 1 );
    
        jLabel23.setVisible(reportFontMode != 3);
        
        if (jComboBoxReportFonts.getItemCount() == 0)
        {
            // We have to populate the combobox...
            
            // TODO: fill the list of JRReportFont...
            //if ( MainFrame.getMainInstance().getActiveReportFrame() != null)
            // {
            //     Misc.updateComboBox( jComboBoxReportFonts, MainFrame.getMainInstance().getActiveReportFrame().getReport().getFonts(), true);
            // }
        }
    }
    /*
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jCheckBoxBold.setText(I18n.getString("jRFontDialog.checkBoxBold","Bold"));
                jCheckBoxItalic.setText(I18n.getString("jRFontDialog.checkBoxItalic","Italic"));
                jCheckBoxPDFEmbedded.setText(I18n.getString("jRFontDialog.checkBoxPDFEmbedded","PDF Embedded"));
                jCheckBoxStrokeTrough.setText(I18n.getString("jRFontDialog.checkBoxStrokeTrough","Strike Trough"));
                jCheckBoxUnderline.setText(I18n.getString("jRFontDialog.checkBoxUnderline","Underline"));
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                jButtonCancel.setText(I18n.getString("jRFontDialog.buttonCancel","Cancel"));
                jButtonOK.setText(I18n.getString("jRFontDialog.buttonOK","OK"));
                jButtonResetAllToDefault.setText(I18n.getString("jRFontDialog.buttonResetAllToDefault","Set all to default"));
                jLabel23.setText(I18n.getString("jRFontDialog.label23","Report font"));
                jLabel24.setText(I18n.getString("jRFontDialog.label24","Font name"));
                jLabel25.setText(I18n.getString("jRFontDialog.label25","PDF font name"));
                jLabel27.setText(I18n.getString("jRFontDialog.label27","Size"));
                jLabel32.setText(I18n.getString("jRFontDialog.label32","PDF Encoding"));
                // End autogenerated code ----------------------
                
                this.setTitle(I18n.getString("jRFontDialog.title","Add/modify report font"));
                jButtonCancel.setMnemonic(I18n.getString("jRFontDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOK.setMnemonic(I18n.getString("jRFontDialog.buttonOKMnemonic","o").charAt(0));
                jCheckBoxDefaultFont.setText(I18n.getString("jRFontDialog.checkBoxDefaultFont","Default"));
    }
     */
    
    public int showDialog(Component parent)
    {
        Object pWin = SwingUtilities.windowForComponent(parent);
        if (pWin instanceof Frame) dialog = new JDialog((Frame)pWin);
        else if (pWin instanceof Dialog) dialog = new JDialog((Dialog)pWin);
        else dialog = new JDialog();

        dialog.setModal(true);
        
        jPanel6.setVisible(true);
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };
       
        dialog.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        dialog.getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);


        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        dialog.getContentPane().add(this, gridBagConstraints);
        
        //to make the default button ...
        dialog.getRootPane().setDefaultButton(this.jButtonOK);
        
        dialog.setVisible(true);
        
        return getDialogResult();
    }

    public void preferenceChange(PreferenceChangeEvent evt) {
        if (evt == null || evt.getKey() == null || evt.getKey().equals( IReportManager.IREPORT_CLASSPATH))
        {
            // Refresh the array...
            updateStandardFonts();
        }
    }
}
