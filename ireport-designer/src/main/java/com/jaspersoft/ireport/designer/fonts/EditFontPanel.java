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

/*
 * EditFontPanel.java
 *
 * Created on 26-ott-2009, 10.50.35
 */

package com.jaspersoft.ireport.designer.fonts;

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.subreport.TableComboBoxEditor;
import com.jaspersoft.ireport.designer.tools.LocaleSelectorDialog;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author gtoffoli
 */
public class EditFontPanel extends javax.swing.JPanel {

    private JDialog dialog = null;
    private int dialogResult = JOptionPane.OK_OPTION;

    private SimpleFontFamilyEx fontFamily = null;

    /** Creates new form EditFontPanel */
    public EditFontPanel() {
        initComponents();


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

        jList1.setModel(new DefaultListModel());
        jList1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                jButtonRemoveLocale.setEnabled( jList1.getSelectedIndex() >= 0 );
            }
        });


        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                jButtonRemoveMapping.setEnabled(jTable1.getSelectedRow() >= 0);
            }
        });

        TableColumn col = jTable1.getColumnModel().getColumn(0);
        TableComboBoxEditor tcb = new TableComboBoxEditor(new java.util.Vector());
        JComboBox cb = (JComboBox)tcb.getComponent();
        cb.setEditable(true);
        cb.removeAllItems();

        Vector items = new Vector();
        items.add("html");
        items.add("xhtml");
        items.add("rtf");
        cb.setModel(new DefaultComboBoxModel(items));

        col.setCellEditor(tcb);

        jTable1.setRowHeight(20);
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

        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(this, BorderLayout.CENTER);
        setDialogResult(JOptionPane.CANCEL_OPTION);
        dialog.setTitle("Edit font...");
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(modal);

        return getDialogResult();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelFamilyName = new javax.swing.JLabel();
        jTextFieldFamilyName = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
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
        jLabelTTFFont = new javax.swing.JLabel();
        jTextFieldTTFFont = new javax.swing.JTextField();
        jButtonBrowse = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabelPDFEncoding = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jCheckBoxEmbedded = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtonAddMapping = new javax.swing.JButton();
        jButtonRemoveMapping = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButtonRemoveLocale = new javax.swing.JButton();
        jButtonAddLocale = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        jLabelFamilyName.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jLabelFamilyName.text")); // NOI18N

        jTextFieldFamilyName.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jTextFieldFamilyName.text")); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jPanel2.border.title"))); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jLabel2.text")); // NOI18N
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jLabelTTFFontBold.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jLabelTTFFontBold.text")); // NOI18N

        jTextFieldTTFFontBold.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jTextFieldTTFFontBold.text")); // NOI18N

        jButtonBoldBrowse.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jButtonBoldBrowse.text")); // NOI18N
        jButtonBoldBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBoldBrowseActionPerformed(evt);
            }
        });

        jLabelTTFFontItalic.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jLabelTTFFontItalic.text")); // NOI18N

        jTextFieldTTFFontItalic.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jTextFieldTTFFontItalic.text")); // NOI18N

        jButtonItalicBrowse.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jButtonItalicBrowse.text")); // NOI18N
        jButtonItalicBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonItalicBrowseActionPerformed(evt);
            }
        });

        jLabelTTFFontBoldItalic.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jLabelTTFFontBoldItalic.text")); // NOI18N

        jTextFieldTTFFontBoldItalic.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jTextFieldTTFFontBoldItalic.text")); // NOI18N

        jButtonBoldItalicBrowse.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jButtonBoldItalicBrowse.text")); // NOI18N
        jButtonBoldItalicBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBoldItalicBrowseActionPerformed(evt);
            }
        });

        jLabelTTFFont.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jLabelTTFFont.text")); // NOI18N

        jTextFieldTTFFont.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jTextFieldTTFFont.text")); // NOI18N

        jButtonBrowse.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jButtonBrowse.text")); // NOI18N
        jButtonBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabelTTFFont, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(26, 26, 26)
                        .add(jTextFieldTTFFont, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                        .add(1, 1, 1)
                        .add(jButtonBrowse, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelTTFFontItalic)
                            .add(jLabelTTFFontBoldItalic)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                                .add(jLabelTTFFontBold, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(35, 35, 35)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTextFieldTTFFontBold, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .add(jTextFieldTTFFontItalic, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .add(jTextFieldTTFFontBoldItalic, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButtonBoldItalicBrowse))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jButtonBoldBrowse)
                                    .add(jButtonItalicBrowse))
                                .addContainerGap())))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextFieldTTFFont, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelTTFFont)
                    .add(jButtonBrowse))
                .add(10, 10, 10)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextFieldTTFFontBold, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelTTFFontBold)
                    .add(jButtonBoldBrowse))
                .add(10, 10, 10)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelTTFFontItalic)
                    .add(jTextFieldTTFFontItalic, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonItalicBrowse))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelTTFFontBoldItalic)
                    .add(jTextFieldTTFFontBoldItalic, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonBoldItalicBrowse))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jPanel3.border.title"))); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jLabel3.text")); // NOI18N

        jLabelPDFEncoding.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jLabelPDFEncoding.text")); // NOI18N

        jComboBox1.setEditable(true);

        jCheckBoxEmbedded.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jCheckBoxEmbedded.text")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxEmbedded)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabelPDFEncoding)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBox1, 0, 305, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelPDFEncoding)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBoxEmbedded)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 247, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jLabel5.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jLabel5.text")); // NOI18N
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel6.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jLabel6.text")); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Export type", "Mapping"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jButtonAddMapping.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jButtonAddMapping.text")); // NOI18N
        jButtonAddMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddMappingActionPerformed(evt);
            }
        });

        jButtonRemoveMapping.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jButtonRemoveMapping.text")); // NOI18N
        jButtonRemoveMapping.setEnabled(false);
        jButtonRemoveMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveMappingActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel6)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel5Layout.createSequentialGroup()
                        .add(jButtonAddMapping)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonRemoveMapping)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonRemoveMapping)
                    .add(jButtonAddMapping))
                .addContainerGap())
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        jButtonRemoveLocale.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jButtonRemoveLocale.text")); // NOI18N
        jButtonRemoveLocale.setEnabled(false);
        jButtonRemoveLocale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveLocaleActionPerformed(evt);
            }
        });

        jButtonAddLocale.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jButtonAddLocale.text")); // NOI18N
        jButtonAddLocale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddLocaleActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jList1);

        jLabel4.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jLabel4.text")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jLabel1.text")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jButtonAddLocale)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonRemoveLocale)
                        .addContainerGap(294, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                            .add(jLabel4, 0, 448, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonAddLocale)
                    .add(jButtonRemoveLocale))
                .addContainerGap())
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        jButtonOk.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jButtonOk.text")); // NOI18N
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        jButtonCancel.setText(org.openide.util.NbBundle.getMessage(EditFontPanel.class, "EditFontPanel.jButtonCancel.text")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabelFamilyName)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jTextFieldFamilyName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(342, 342, 342)
                        .add(jButtonOk)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonCancel))
                    .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelFamilyName)
                    .add(jTextFieldFamilyName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonCancel)
                    .add(jButtonOk))
                .addContainerGap())
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

    private void jButtonBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseActionPerformed
        InstallFontWizardDescriptor.browseForTTFFile(jTextFieldTTFFont);
}//GEN-LAST:event_jButtonBrowseActionPerformed

    private void jButtonRemoveLocaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveLocaleActionPerformed

        while (jList1.getSelectedIndex() >= 0) {
            ((DefaultListModel)jList1.getModel()).remove(jList1.getSelectedIndex());
        }
    }//GEN-LAST:event_jButtonRemoveLocaleActionPerformed

    private void jButtonAddLocaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddLocaleActionPerformed

        LocaleSelectorDialog lsd = new LocaleSelectorDialog( (Dialog)SwingUtilities.getAncestorOfClass( Dialog.class, this), true);
        lsd.setVisible(true);
        if (lsd.getDialogResult() == JOptionPane.OK_OPTION) {
            ((DefaultListModel)jList1.getModel()).addElement( new Tag(lsd.getSelectedLocaleId(), lsd.getSelectedLocale().getDisplayLanguage() + " (" + lsd.getSelectedLocaleId() + ")" ));
        }
    }//GEN-LAST:event_jButtonAddLocaleActionPerformed

    private void jButtonAddMappingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddMappingActionPerformed

        ((DefaultTableModel)jTable1.getModel()).addRow(new Object[]{"",""});

    }//GEN-LAST:event_jButtonAddMappingActionPerformed

    private void jButtonRemoveMappingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveMappingActionPerformed

        while (jTable1.getSelectedRow() >= 0) {
            ((DefaultTableModel)jTable1.getModel()).removeRow(jTable1.getSelectedRow());
        }
}//GEN-LAST:event_jButtonRemoveMappingActionPerformed


    public void validateFileName(JTextField textfield) throws IllegalArgumentException
    {
        if (textfield.getText().trim().length() > 0)
        {
            File f = new File(textfield.getText().trim());
            if (!f.exists())
            {
                f = new File(Misc.getFontsDirectory(), textfield.getText());
                if (!f.exists())
                {
                    throw new IllegalArgumentException("The file '" + textfield.getText() + "' does not exist.");
                }
            }
        }
    }

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed

        try {
            if (jTextFieldFamilyName.getText().trim().length() == 0) throw new IllegalArgumentException();

            validateFileName(jTextFieldTTFFont);
            validateFileName(jTextFieldTTFFontBold);
            validateFileName(jTextFieldTTFFontItalic);
            validateFileName(jTextFieldTTFFontBoldItalic);

        } catch (Exception ex)
        {
            javax.swing.JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE );
            return;
        }


        this.fontFamily = new SimpleFontFamilyEx();

        getFontFamily().setName(jTextFieldFamilyName.getText());
        getFontFamily().setNormalFont(jTextFieldTTFFont.getText().trim());
        getFontFamily().setBoldFont(jTextFieldTTFFontBold.getText().trim());
        getFontFamily().setItalicFont(jTextFieldTTFFontItalic.getText().trim());
        getFontFamily().setBoldItalicFont(jTextFieldTTFFontBoldItalic.getText().trim());

        getFontFamily().setBoldItalicFont(jTextFieldTTFFontBoldItalic.getText().trim());

        if (jComboBox1.getSelectedItem() instanceof Tag)
        {
           getFontFamily().setPdfEncoding( (String)((Tag)jComboBox1.getSelectedItem()).getValue());
        }
        else
        {
            getFontFamily().setPdfEncoding(jComboBox1.getSelectedItem()+"");
        }

        getFontFamily().setPdfEmbedded( jCheckBoxEmbedded.isSelected());


        List<String> locales = new ArrayList<String>();

        DefaultListModel dlm = (DefaultListModel)jList1.getModel();

        for (int i=0; i<dlm.getSize(); ++i)
        {

            locales.add( (String)((Tag)dlm.get(i)).getValue() );
        }

        getFontFamily().setLocales(new HashSet(locales));


        HashMap<String,String> mappings = new HashMap<String, String>();

        for (int i=0; i<jTable1.getRowCount(); ++i)
        {

            String key = (String)jTable1.getValueAt(i, 0);
            String val = (String)jTable1.getValueAt(i, 1);
            if (key.trim().length() > 0 && val.trim().length() > 0)
            {
                mappings.put( "net.sf.jasperreports."  +key.trim(), val.trim() );
            }
        }

        getFontFamily().setExportFonts(mappings);

        setDialogResult(JOptionPane.OK_OPTION);

        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
        }
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setDialogResult(JOptionPane.CANCEL_OPTION);
        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
        }
}//GEN-LAST:event_jButtonCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddLocale;
    private javax.swing.JButton jButtonAddMapping;
    private javax.swing.JButton jButtonBoldBrowse;
    private javax.swing.JButton jButtonBoldItalicBrowse;
    private javax.swing.JButton jButtonBrowse;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonItalicBrowse;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonRemoveLocale;
    private javax.swing.JButton jButtonRemoveMapping;
    private javax.swing.JCheckBox jCheckBoxEmbedded;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelFamilyName;
    private javax.swing.JLabel jLabelPDFEncoding;
    private javax.swing.JLabel jLabelTTFFont;
    private javax.swing.JLabel jLabelTTFFontBold;
    private javax.swing.JLabel jLabelTTFFontBoldItalic;
    private javax.swing.JLabel jLabelTTFFontItalic;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextFieldFamilyName;
    private javax.swing.JTextField jTextFieldTTFFont;
    private javax.swing.JTextField jTextFieldTTFFontBold;
    private javax.swing.JTextField jTextFieldTTFFontBoldItalic;
    private javax.swing.JTextField jTextFieldTTFFontItalic;
    // End of variables declaration//GEN-END:variables

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

    /**
     * @return the fontFamily
     */
    public SimpleFontFamilyEx getFontFamily() {
        return fontFamily;
    }

    /**
     * @param fontFamily the fontFamily to set
     */
    public void setFontFamily(SimpleFontFamilyEx fontFamily) {
    
        jTextFieldFamilyName.setText( fontFamily.getName());
        jTextFieldTTFFont.setText(  fontFamily.getNormalFont());
        jTextFieldTTFFontBold.setText(  fontFamily.getBoldFont());
        jTextFieldTTFFontItalic.setText(  fontFamily.getItalicFont());
        jTextFieldTTFFontBoldItalic.setText(  fontFamily.getBoldItalicFont());
        if (fontFamily.getPdfEncoding() != null && fontFamily.getPdfEncoding().length() > 0)
        {
            Misc.setComboboxSelectedTagValue(jComboBox1, fontFamily.getPdfEncoding());
        }
        if (fontFamily.isPdfEmbedded() != null)
        {
           jCheckBoxEmbedded.setSelected( fontFamily.isPdfEmbedded().booleanValue() );
        }

        ((DefaultListModel)jList1.getModel()).removeAllElements();
        // Add locales...
        if (fontFamily.getLocales() != null)
        {
            Iterator itLocales = fontFamily.getLocales().iterator();
            while (itLocales.hasNext())
            {
                ((DefaultListModel)jList1.getModel()).addElement( itLocales.next() );
            }
        }

        ((DefaultTableModel)jTable1.getModel()).setRowCount(0);
        // Add locales...
        if (fontFamily.getExportFonts() != null)
        {
            Iterator itKeys = fontFamily.getExportFonts().keySet().iterator();
            while (itKeys.hasNext())
            {
                String key=(String) itKeys.next();
                String value = (String)fontFamily.getExportFonts().get(key);
                if (key.startsWith("net.sf.jasperreports."))
                {
                    key = key.substring("net.sf.jasperreports.".length());
                }
                ((DefaultTableModel)jTable1.getModel()).addRow(new Object[]{key, value});
            }
        }

        jList1.updateUI();
        jTable1.updateUI();
    }

}
