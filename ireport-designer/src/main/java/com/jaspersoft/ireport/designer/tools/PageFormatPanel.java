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
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.utils.PageSize;
import com.jaspersoft.ireport.designer.utils.Unit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.OrientationEnum;

/**
 *
 * @author gtoffoli
 */
public class PageFormatPanel extends javax.swing.JPanel {

    JDialog dialog = null;
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    private boolean updating = false;
    private JasperDesign jasperDesign;

    /** Creates new form PageFormatPanel */
    public PageFormatPanel() {
        initComponents();

        jSpinnerColumns.setModel(new SpinnerNumberModel(1,1,1000,1));

        ChangeListener changeListener = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updatePreview();
            }
        };

        ChangeListener changeListener2 = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                recalculateColumnWidth();
            }
        };

        ChangeListener changeListenerUpdateFormat = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {

                String format = PageSize.deductPageFormat(
                        multiUnitNumberEditorWidth.getValue(),
                        multiUnitNumberEditorHeight.getValue());

                boolean oldUp = setUpdating(true);
                Misc.setComboboxSelectedTagValue(jComboBoxFormat, format);
                setUpdating(oldUp);
            }
        };

        multiUnitNumberEditorWidth.addChangeListener(changeListener);
        multiUnitNumberEditorWidth.addChangeListener(changeListener2);
        multiUnitNumberEditorWidth.addChangeListener(changeListenerUpdateFormat);

        multiUnitNumberEditorHeight.addChangeListener(changeListener);
        multiUnitNumberEditorHeight.addChangeListener(changeListenerUpdateFormat);

        multiUnitNumberEditorMarginaTop.addChangeListener(changeListener);
        multiUnitNumberEditorMarginBottom.addChangeListener(changeListener);

        multiUnitNumberEditorMarginLeft.addChangeListener(changeListener);
        multiUnitNumberEditorMarginLeft.addChangeListener(changeListener2);
        multiUnitNumberEditorMarginRight.addChangeListener(changeListener);
        multiUnitNumberEditorMarginRight.addChangeListener(changeListener2);

        multiUnitNumberEditorColumnWidth.addChangeListener(changeListener);
        multiUnitNumberEditorColumnWidth.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                recalculateColumnSpace();
            }
        });

        multiUnitNumberEditorcolumnSpace.addChangeListener(changeListener);
        multiUnitNumberEditorcolumnSpace.addChangeListener(changeListener2);

        jComboBox1.setModel(new DefaultComboBoxModel(Unit.getStandardUnits()));
        String unit = IReportManager.getPreferences().get("Unit","inches");
        int index = Unit.getUnitIndex(unit);
        jComboBox1.setSelectedIndex(index);

        this.jComboBoxFormat.addItem(new Tag("Custom",     I18n.getString("pageformat.Custom")));
        this.jComboBoxFormat.addItem(new Tag("LETTER",     I18n.getString("pageformat.LETTER")));
        this.jComboBoxFormat.addItem(new Tag("NOTE",       I18n.getString("pageformat.NOTE")));
        this.jComboBoxFormat.addItem(new Tag("LEGAL",      I18n.getString("pageformat.LEGAL")));
        this.jComboBoxFormat.addItem(new Tag("A0",         I18n.getString("pageformat.A0")));
        this.jComboBoxFormat.addItem(new Tag("A1",         I18n.getString("pageformat.A1")));
        this.jComboBoxFormat.addItem(new Tag("A2",         I18n.getString("pageformat.A2")));
        this.jComboBoxFormat.addItem(new Tag("A3",         I18n.getString("pageformat.A3")));
        this.jComboBoxFormat.addItem(new Tag("A4",         I18n.getString("pageformat.A4")));
        this.jComboBoxFormat.addItem(new Tag("A5",         I18n.getString("pageformat.A5")));
        this.jComboBoxFormat.addItem(new Tag("A6",         I18n.getString("pageformat.A6")));
        this.jComboBoxFormat.addItem(new Tag("A7",         I18n.getString("pageformat.A7")));
        this.jComboBoxFormat.addItem(new Tag("A8",         I18n.getString("pageformat.A8")));
        this.jComboBoxFormat.addItem(new Tag("A9",         I18n.getString("pageformat.A9")));
        this.jComboBoxFormat.addItem(new Tag("A10",        I18n.getString("pageformat.A10")));
        this.jComboBoxFormat.addItem(new Tag("B0",         I18n.getString("pageformat.B0")));
        this.jComboBoxFormat.addItem(new Tag("B1",         I18n.getString("pageformat.B1")));
        this.jComboBoxFormat.addItem(new Tag("B2",         I18n.getString("pageformat.B2")));
        this.jComboBoxFormat.addItem(new Tag("B3",         I18n.getString("pageformat.B3")));
        this.jComboBoxFormat.addItem(new Tag("B4",         I18n.getString("pageformat.B4")));
        this.jComboBoxFormat.addItem(new Tag("B5",         I18n.getString("pageformat.B5")));
        this.jComboBoxFormat.addItem(new Tag("ARCH_E",     I18n.getString("pageformat.ARCH_E")));
        this.jComboBoxFormat.addItem(new Tag("ARCH_D",     I18n.getString("pageformat.ARCH_D")));
        this.jComboBoxFormat.addItem(new Tag("ARCH_C",     I18n.getString("pageformat.ARCH_C")));
        this.jComboBoxFormat.addItem(new Tag("ARCH_B",     I18n.getString("pageformat.ARCH_B")));
        this.jComboBoxFormat.addItem(new Tag("ARCH_A",     I18n.getString("pageformat.ARCH_A")));
        this.jComboBoxFormat.addItem(new Tag("FLSA",       I18n.getString("pageformat.FLSA")));
        this.jComboBoxFormat.addItem(new Tag("FLSE",       I18n.getString("pageformat.FLSE")));
        this.jComboBoxFormat.addItem(new Tag("HALFLETTER", I18n.getString("pageformat.HALFLETTER")));
        this.jComboBoxFormat.addItem(new Tag("11x17",      I18n.getString("pageformat.11x17")));
        this.jComboBoxFormat.addItem(new Tag("LEDGER",     I18n.getString("pageformat.LEDGER")));


        
        jLabelFormat.setText(I18n.getString("PageFormatPanel.jLabelFormat.text")); // NOI18N
        jLabelWidth.setText(I18n.getString("PageFormatPanel.jLabelWidth.text")); // NOI18N
        jLabelHeight.setText(I18n.getString("PageFormatPanel.jLabelHeight.text")); // NOI18N
        jLabelMarginTop.setText(I18n.getString("PageFormatPanel.jLabelMarginTop.text")); // NOI18N
        jLabelMarginBottom.setText(I18n.getString("PageFormatPanel.jLabelMarginBottom.text")); // NOI18N
        jLabelLeft.setText(I18n.getString("PageFormatPanel.jLabelLeft.text")); // NOI18N
        jLabelMarginRight.setText(I18n.getString("PageFormatPanel.jLabelMarginRight.text")); // NOI18N
        jLabelMargins.setText(I18n.getString("PageFormatPanel.jLabelMargins.text")); // NOI18N
        jRadioPortrait.setText(I18n.getString("PageFormatPanel.jRadioPortrait.text")); // NOI18N
        jRadioLandscape.setText(I18n.getString("PageFormatPanel.jRadioLandscape.text")); // NOI18N
        jLabelOrientation.setText(I18n.getString("PageFormatPanel.jLabelOrientation.text")); // NOI18N
        jButtonCancel.setText(I18n.getString("PageFormatPanel.jButtonCancel.text")); // NOI18N
        jLabelUnit.setText(I18n.getString("PageFormatPanel.jLabelUnit.text")); // NOI18N
        jLabelColumnstitle.setText(I18n.getString("PageFormatPanel.jLabelColumnstitle.text")); // NOI18N
        jLabelColumns.setText(I18n.getString("PageFormatPanel.jLabelColumns.text")); // NOI18N
        jLabelColumnWidth.setText(I18n.getString("PageFormatPanel.jLabelColumnWidth.text")); // NOI18N
        jLabelSpace.setText(I18n.getString("PageFormatPanel.jLabelSpace.text")); // NOI18N
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
        dialog.setTitle("Page format...");
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setMinimumSize( dialog.getSize() );
        dialog.setMaximumSize( dialog.getSize());
        dialog.setResizable(false);
        dialog.setVisible(modal);


        return getDialogResult();
    }

    public void setJasperDesign(JasperDesign jd)
    {
        this.jasperDesign = jd;
        boolean old = setUpdating(true);
        jSpinnerColumns.setValue(jd.getColumnCount());
        multiUnitNumberEditorWidth.setValue( jd.getPageWidth() );
        multiUnitNumberEditorHeight.setValue(jd.getPageHeight());
        multiUnitNumberEditorMarginaTop.setValue(jd.getTopMargin());
        multiUnitNumberEditorMarginBottom.setValue(jd.getBottomMargin());
        multiUnitNumberEditorMarginLeft.setValue(jd.getLeftMargin());
        multiUnitNumberEditorMarginRight.setValue(jd.getRightMargin());
        multiUnitNumberEditorColumnWidth.setValue( jd.getColumnWidth() );
        multiUnitNumberEditorcolumnSpace.setValue(jd.getColumnSpacing());

        jRadioLandscape.setSelected(  jd.getOrientationValue() == OrientationEnum.LANDSCAPE );
        jRadioPortrait.setSelected(  jd.getOrientationValue() == OrientationEnum.PORTRAIT );

        Misc.setComboboxSelectedTagValue(jComboBoxFormat, PageSize.deductPageFormat(jd.getPageWidth(), jd.getPageHeight()));
        setUpdating(old);
        updatePreview();

    }

    public void updatePreview()
    {
        int cols =  ((Number)jSpinnerColumns.getValue()).intValue();

        pageFormatPreviewPanel1.setPageWidth(multiUnitNumberEditorWidth.getValue());
        pageFormatPreviewPanel1.setPageHeight(multiUnitNumberEditorHeight.getValue());
        pageFormatPreviewPanel1.setMarginBottom(multiUnitNumberEditorMarginBottom.getValue());
        pageFormatPreviewPanel1.setMarginLeft(multiUnitNumberEditorMarginLeft.getValue());
        pageFormatPreviewPanel1.setMarginRight(multiUnitNumberEditorMarginRight.getValue());
        pageFormatPreviewPanel1.setMarginTop(multiUnitNumberEditorMarginaTop.getValue());
        pageFormatPreviewPanel1.setColumns(cols);
        pageFormatPreviewPanel1.setColumnSpace(multiUnitNumberEditorcolumnSpace.getValue());
        pageFormatPreviewPanel1.setColumnWidth(multiUnitNumberEditorColumnWidth.getValue());

        pageFormatPreviewPanel1.repaint();
    }

    public void applyChanges(JasperDesign jd) {

        int cols =  ((Number)jSpinnerColumns.getValue()).intValue();

        jd.setPageWidth(multiUnitNumberEditorWidth.getValue());
        jd.setPageHeight(multiUnitNumberEditorHeight.getValue());
        jd.setBottomMargin(multiUnitNumberEditorMarginBottom.getValue());
        jd.setLeftMargin(multiUnitNumberEditorMarginLeft.getValue());
        jd.setRightMargin(multiUnitNumberEditorMarginRight.getValue());
        jd.setTopMargin(multiUnitNumberEditorMarginaTop.getValue());
        jd.setColumnCount(cols);
        jd.setColumnSpacing(multiUnitNumberEditorcolumnSpace.getValue());
        jd.setColumnWidth(multiUnitNumberEditorColumnWidth.getValue());
        jd.setOrientation( jRadioLandscape.isSelected() ? OrientationEnum.LANDSCAPE : OrientationEnum.PORTRAIT  );
    }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabelFormat = new javax.swing.JLabel();
        jComboBoxFormat = new javax.swing.JComboBox();
        jLabelWidth = new javax.swing.JLabel();
        jLabelHeight = new javax.swing.JLabel();
        jLabelMarginTop = new javax.swing.JLabel();
        jLabelMarginBottom = new javax.swing.JLabel();
        jLabelLeft = new javax.swing.JLabel();
        jLabelMarginRight = new javax.swing.JLabel();
        jLabelMargins = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jRadioPortrait = new javax.swing.JRadioButton();
        jRadioLandscape = new javax.swing.JRadioButton();
        jLabelOrientation = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabelUnit = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        multiUnitNumberEditorWidth = new com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor();
        multiUnitNumberEditorHeight = new com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor();
        multiUnitNumberEditorMarginaTop = new com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor();
        multiUnitNumberEditorMarginBottom = new com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor();
        multiUnitNumberEditorMarginLeft = new com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor();
        multiUnitNumberEditorMarginRight = new com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor();
        jLabelColumnstitle = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabelColumns = new javax.swing.JLabel();
        jSpinnerColumns = new javax.swing.JSpinner();
        multiUnitNumberEditorColumnWidth = new com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor();
        jLabelColumnWidth = new javax.swing.JLabel();
        multiUnitNumberEditorcolumnSpace = new com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor();
        jLabelSpace = new javax.swing.JLabel();
        pageFormatPreviewPanel1 = new com.jaspersoft.ireport.designer.tools.PageFormatPreviewPanel();

        jLabelFormat.setText("Format");

        jComboBoxFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFormatActionPerformed(evt);
            }
        });

        jLabelWidth.setText("Width");

        jLabelHeight.setText("Height");

        jLabelMarginTop.setText("Top");

        jLabelMarginBottom.setText("Bottom");

        jLabelLeft.setText("Left");

        jLabelMarginRight.setText("Right");

        jLabelMargins.setText("Margins");

        buttonGroup1.add(jRadioPortrait);
        jRadioPortrait.setSelected(true);
        jRadioPortrait.setText("Portrait");
        jRadioPortrait.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioPortraitActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioLandscape);
        jRadioLandscape.setText("Landscape");
        jRadioLandscape.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioLandscapeActionPerformed(evt);
            }
        });

        jLabelOrientation.setText("Page orientation");

        jButtonOk.setText("Ok");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jLabelUnit.setText("Unit");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jLabelUnit)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 263, Short.MAX_VALUE)
                .add(jButtonOk)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonCancel))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(0, 0, 0)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jButtonCancel)
                            .add(jButtonOk)))
                    .add(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabelUnit)
                            .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jLabelColumnstitle.setText("Columns");

        jLabelColumns.setText("Columns");

        jSpinnerColumns.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerColumnsStateChanged(evt);
            }
        });

        multiUnitNumberEditorColumnWidth.setEnabled(false);

        jLabelColumnWidth.setText("Column width");

        multiUnitNumberEditorcolumnSpace.setEnabled(false);

        jLabelSpace.setText("Space");

        org.jdesktop.layout.GroupLayout pageFormatPreviewPanel1Layout = new org.jdesktop.layout.GroupLayout(pageFormatPreviewPanel1);
        pageFormatPreviewPanel1.setLayout(pageFormatPreviewPanel1Layout);
        pageFormatPreviewPanel1Layout.setHorizontalGroup(
            pageFormatPreviewPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 235, Short.MAX_VALUE)
        );
        pageFormatPreviewPanel1Layout.setVerticalGroup(
            pageFormatPreviewPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 186, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelMarginBottom)
                            .add(jLabelLeft)
                            .add(jLabelMarginTop)
                            .add(jLabelMarginRight))
                        .add(6, 6, 6)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, multiUnitNumberEditorMarginRight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 165, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(multiUnitNumberEditorMarginaTop, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                .add(multiUnitNumberEditorMarginBottom, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(multiUnitNumberEditorMarginLeft, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelColumnWidth)
                            .add(jLabelColumns)
                            .add(jLabelSpace))
                        .add(4, 4, 4)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jSpinnerColumns, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(multiUnitNumberEditorColumnWidth, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                            .add(multiUnitNumberEditorcolumnSpace, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelMargins)
                            .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 205, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelColumnstitle)
                            .add(jSeparator4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 235, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jRadioLandscape)
                            .add(jRadioPortrait)
                            .add(jSeparator3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 205, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabelFormat)
                                    .add(jLabelWidth)
                                    .add(jLabelHeight))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(multiUnitNumberEditorWidth, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(multiUnitNumberEditorHeight, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(jComboBoxFormat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                            .add(jLabelOrientation))
                        .add(18, 18, 18)
                        .add(pageFormatPreviewPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabelFormat)
                            .add(jComboBoxFormat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(multiUnitNumberEditorWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabelWidth))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabelHeight)
                            .add(multiUnitNumberEditorHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(24, 24, 24)
                        .add(jLabelOrientation)
                        .add(2, 2, 2)
                        .add(jSeparator3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jRadioPortrait)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jRadioLandscape)
                        .add(27, 27, 27))
                    .add(layout.createSequentialGroup()
                        .add(pageFormatPreviewPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelMargins)
                    .add(jLabelColumnstitle))
                .add(2, 2, 2)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabelMarginTop)
                            .add(multiUnitNumberEditorMarginaTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(layout.createSequentialGroup()
                        .add(jSeparator4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabelColumns)
                            .add(jSpinnerColumns, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabelMarginBottom)
                    .add(multiUnitNumberEditorMarginBottom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelColumnWidth)
                    .add(multiUnitNumberEditorColumnWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabelLeft)
                    .add(multiUnitNumberEditorMarginLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelSpace)
                    .add(multiUnitNumberEditorcolumnSpace, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabelMarginRight)
                    .add(multiUnitNumberEditorMarginRight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setDialogResult(JOptionPane.CANCEL_OPTION);
        if (dialog != null)
        {
            dialog.setVisible(false);
            dialog.dispose();
        }
}//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        setDialogResult(JOptionPane.OK_OPTION);
        applyChanges(this.jasperDesign);
        if (dialog != null)
        {
            dialog.setVisible(false);
            dialog.dispose();
        }

    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jRadioLandscapeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioLandscapeActionPerformed
        updateOrientation();
    }//GEN-LAST:event_jRadioLandscapeActionPerformed

    private void jRadioPortraitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioPortraitActionPerformed
        updateOrientation();
    }//GEN-LAST:event_jRadioPortraitActionPerformed

    private void jSpinnerColumnsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerColumnsStateChanged

        int cols =  ((Number)jSpinnerColumns.getValue()).intValue();
        if (cols > 1)
        {
            multiUnitNumberEditorcolumnSpace.setEnabled(true);
            multiUnitNumberEditorColumnWidth.setEnabled(true);
        }
        else
        {
            multiUnitNumberEditorcolumnSpace.setEnabled(false);
            multiUnitNumberEditorColumnWidth.setEnabled(false);
        }
        recalculateColumnWidth();
        updatePreview();
    }//GEN-LAST:event_jSpinnerColumnsStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

        Unit unit = (Unit)jComboBox1.getSelectedItem();
        multiUnitNumberEditorWidth.setSelectedUnit(unit);
        multiUnitNumberEditorHeight.setSelectedUnit(unit);
        multiUnitNumberEditorMarginaTop.setSelectedUnit(unit);
        multiUnitNumberEditorMarginBottom.setSelectedUnit(unit);
        multiUnitNumberEditorMarginLeft.setSelectedUnit(unit);
        multiUnitNumberEditorMarginRight.setSelectedUnit(unit);
        multiUnitNumberEditorColumnWidth.setSelectedUnit(unit);
        multiUnitNumberEditorcolumnSpace.setSelectedUnit(unit);

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBoxFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFormatActionPerformed

        if (isUpdating()) return;
        Tag t = (Tag) jComboBoxFormat.getSelectedItem();

        if (!t.getValue().equals("Custom"))
        {
            Point p = PageSize.getFormatSize(""+t.getValue());
            if (p != null)
            {
                if (jRadioLandscape.isSelected())
                {
                    multiUnitNumberEditorWidth.setValue(p.y);
                    multiUnitNumberEditorHeight.setValue(p.x);
                }
                else
                {
                    multiUnitNumberEditorWidth.setValue(p.x);
                    multiUnitNumberEditorHeight.setValue(p.y);
                }
            }
        }
        recalculateColumnWidth();
        updatePreview();
    }//GEN-LAST:event_jComboBoxFormatActionPerformed

    public void recalculateColumnWidth()
    {
        int available = multiUnitNumberEditorWidth.getValue() -
                        multiUnitNumberEditorMarginLeft.getValue() -
                        multiUnitNumberEditorMarginRight.getValue();
        
        int cols =  ((Number)jSpinnerColumns.getValue()).intValue();
        if (cols <= 1)
        {
            multiUnitNumberEditorcolumnSpace.setValue(0);
            multiUnitNumberEditorColumnWidth.setValue(available);
            return;
        }

        int maxColSpace = available/(cols-1);
        available -= multiUnitNumberEditorcolumnSpace.getValue() * (cols-1);

        if (available < 0)
        {
            // restore min avail...
            multiUnitNumberEditorcolumnSpace.setValue(maxColSpace);
            multiUnitNumberEditorColumnWidth.setValue(0);
        }
        else
        {
            multiUnitNumberEditorColumnWidth.setValue(available/cols);
        }
                        
    }

    public void recalculateColumnSpace()
    {
        int available = multiUnitNumberEditorWidth.getValue() -
                        multiUnitNumberEditorMarginLeft.getValue() -
                        multiUnitNumberEditorMarginRight.getValue();

        int cols =  ((Number)jSpinnerColumns.getValue()).intValue();
        if (cols <= 1)
        {
            return;
        }

        int maxColWidth = available/cols;

        available -= multiUnitNumberEditorColumnWidth.getValue() * (cols);

        if (available < 0)
        {
            // restore min avail...
            multiUnitNumberEditorcolumnSpace.setValue(0);
            multiUnitNumberEditorColumnWidth.setValue(maxColWidth);
        }
        else
        {
            multiUnitNumberEditorcolumnSpace.setValue(available/(cols-1));
        }
        updatePreview();

    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBoxFormat;
    private javax.swing.JLabel jLabelColumnWidth;
    private javax.swing.JLabel jLabelColumns;
    private javax.swing.JLabel jLabelColumnstitle;
    private javax.swing.JLabel jLabelFormat;
    private javax.swing.JLabel jLabelHeight;
    private javax.swing.JLabel jLabelLeft;
    private javax.swing.JLabel jLabelMarginBottom;
    private javax.swing.JLabel jLabelMarginRight;
    private javax.swing.JLabel jLabelMarginTop;
    private javax.swing.JLabel jLabelMargins;
    private javax.swing.JLabel jLabelOrientation;
    private javax.swing.JLabel jLabelSpace;
    private javax.swing.JLabel jLabelUnit;
    private javax.swing.JLabel jLabelWidth;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioLandscape;
    private javax.swing.JRadioButton jRadioPortrait;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSpinner jSpinnerColumns;
    private com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor multiUnitNumberEditorColumnWidth;
    private com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor multiUnitNumberEditorHeight;
    private com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor multiUnitNumberEditorMarginBottom;
    private com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor multiUnitNumberEditorMarginLeft;
    private com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor multiUnitNumberEditorMarginRight;
    private com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor multiUnitNumberEditorMarginaTop;
    private com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor multiUnitNumberEditorWidth;
    private com.jaspersoft.ireport.designer.tools.MultiUnitNumberEditor multiUnitNumberEditorcolumnSpace;
    private com.jaspersoft.ireport.designer.tools.PageFormatPreviewPanel pageFormatPreviewPanel1;
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

    private void updateOrientation() {

        int w = multiUnitNumberEditorWidth.getValue();
        int h = multiUnitNumberEditorHeight.getValue();

        if (jRadioLandscape.isSelected())
        {
            multiUnitNumberEditorWidth.setValue(Math.max(w, h));
            multiUnitNumberEditorHeight.setValue(Math.min(w, h));
        }
        else
        {
            multiUnitNumberEditorWidth.setValue(Math.min(w, h));
            multiUnitNumberEditorHeight.setValue(Math.max(w, h));
        }

        recalculateColumnWidth();
        updatePreview();

    }

    /**
     * @return the updating
     */
    public boolean isUpdating() {
        return updating;
    }

    /**
     * @param updating the updating to set
     */
    public boolean setUpdating(boolean updating) {
        boolean b = this.updating;
        this.updating = updating;
        return b;
    }

    /**
     * @return the jasperDesign
     */
    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

}
