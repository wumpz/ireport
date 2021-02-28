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
package com.jaspersoft.ireport.designer.charts.datasets;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.tools.JNumberComboBox;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.charts.design.JRDesignPieDataset;
import net.sf.jasperreports.charts.design.JRDesignPieSeries;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;

/**
 *
 * @author  Administrator
 */
public class PieDatasetPanel extends javax.swing.JPanel  implements ChartDatasetPanel {

    private ExpressionContext expressionContext = null;
    private JRDesignPieDataset pieDataset = null;
    private PieSeriesPanel pieSeriesPanel = null;

    public static final int SINGLE_SERIES_MODE = 0;
    public static final int MULTIPLE_SERIES_MODE = 1;

    private int currentMode = -1;


    /** Creates new form PieDatasetPanel */
    public PieDatasetPanel() {
        initComponents();
        
        //applyI18n();
        
        this.jRTextExpressionOtherKey.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionOtherKeyTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionOtherKeyTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionOtherKeyTextChanged();
            }
        });
        
        
        this.jRTextExpressionOtherLabel.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionOtherLabelTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionOtherLabelTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionOtherLabelTextChanged();
            }
        });

        pieSeriesPanel = new PieSeriesPanel();

        jList1.setModel( new javax.swing.DefaultListModel());
        jList1.setCellRenderer(new DatasetListsCellRenderer());

        SpinnerNumberModel maxCountSpinner = new SpinnerNumberModel(0,0,Integer.MAX_VALUE,1);
        maxCountSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {

                SpinnerNumberModel model = (SpinnerNumberModel)jSpinnerMaxCount.getModel();
                if (model.getNumber().intValue() == 0)
                {
                    getPieDataset().setMaxCount(null);
                }
                else
                {
                    getPieDataset().setMaxCount(model.getNumber().intValue());
                }
            }
        });


        jSpinnerMaxCount.setModel( maxCountSpinner);

        SpinnerNumberModel minPercentagetSpinner = new SpinnerNumberModel(0d,0d,100d,1d);
        minPercentagetSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {

                SpinnerNumberModel model = (SpinnerNumberModel)jSpinnerMinPercentage.getModel();
                if (model.getNumber().floatValue() == 0)
                {
                    getPieDataset().setMinPercentage(null);
                }
                else
                {
                    getPieDataset().setMinPercentage(model.getNumber().floatValue());
                }
            }
        });
        jSpinnerMinPercentage.setModel( minPercentagetSpinner );
    }

    public JRDesignPieDataset getPieDataset() {
        return pieDataset;
    }
    
    /**
     * this method is used to pass the correct subdataset to the expression editor
     */
    public void setExpressionContext(ExpressionContext ec)
    {
        this.expressionContext = ec;
        jRTextExpressionOtherKey.setExpressionContext(ec);
        jRTextExpressionOtherLabel.setExpressionContext(ec);
        sectionItemHyperlinkPanel2.setExpressionContext(ec);
        pieSeriesPanel.setExpressionContext(ec);
    }

    public void setPieDataset(JRDesignPieDataset pieDataset)
    {
        this.pieDataset = pieDataset;
        jRTextExpressionOtherKey.setText( Misc.getExpressionText( pieDataset.getOtherKeyExpression()) );
        jRTextExpressionOtherLabel.setText( Misc.getExpressionText(pieDataset.getOtherLabelExpression()) );
        if (pieDataset.getOtherSectionHyperlink() == null)
        {
            JRDesignHyperlink hl = new JRDesignHyperlink();
            hl.setHyperlinkType( HyperlinkTypeEnum.NONE );
            pieDataset.setOtherSectionHyperlink(hl);
        }
        sectionItemHyperlinkPanel2.setHyperlink( pieDataset.getOtherSectionHyperlink() );

        List series = pieDataset.getSeriesList();
        if (series.size() == 0)
        {
            pieDataset.addPieSeries(new JRDesignPieSeries());
        }

        if (series.size() == 1)
        {
            setMode( SINGLE_SERIES_MODE);
        }
        else
        {
            setMode( MULTIPLE_SERIES_MODE);
        }

        try {
            SpinnerNumberModel model = (SpinnerNumberModel)jSpinnerMaxCount.getModel();
            if (pieDataset.getMaxCount() == null)
            {
                model.setValue(0);
            }
            else
            {
                model.setValue(pieDataset.getMaxCount());
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        try {
            SpinnerNumberModel model = (SpinnerNumberModel)jSpinnerMinPercentage.getModel();
            if (pieDataset.getMinPercentage() == null)
            {
                model.setValue(new Double(0));
            }
            else
            {
                model.setValue(pieDataset.getMinPercentage().doubleValue());
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setMode(int mode)
    {
        if (currentMode == mode) return;

        List series = pieDataset.getSeriesList();
        if (mode == SINGLE_SERIES_MODE && getPieDataset().getSeriesList().size() > 1)
        {
            if (JOptionPane.showConfirmDialog(pieSeriesPanel, "You have defined {0} series, only one is allowed in single series mode, do you want to discard the other ones?", "" , JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION)
            {
                return;
            }

            for (int i=1; i<series.size(); ++i)
            {
                series.remove(i);
            }
        }

        jPanelData.removeAll();
        if (mode == SINGLE_SERIES_MODE)
        {
            pieSeriesPanel.setPieSeries( (JRDesignPieSeries)series.get(0));
            jPanelData.add(pieSeriesPanel, BorderLayout.CENTER);
            jButtonMode.setText("Use more series");
        }
        else
        {
            ((DefaultListModel)jList1.getModel()).removeAllElements();
            for (int i=0; i<series.size(); ++i)
            {
                JRDesignPieSeries pieSeries = (JRDesignPieSeries)series.get(i);
                ((DefaultListModel)jList1.getModel()).addElement(pieSeries);
            }
            jPanelData.add(jPanelSeries, BorderLayout.CENTER);
            jButtonMode.setText("Use one series");
        }

        currentMode = mode;
        jPanelData.updateUI();

    }
    
    public void jRTextExpressionOtherKeyTextChanged()
    {
        JRDesignExpression exp = null;
        if (jRTextExpressionOtherKey.getText().trim().length() > 0)
        {
            exp = new JRDesignExpression();
            exp.setValueClassName("java.lang.Object");//NOI18N
            exp.setText(jRTextExpressionOtherKey.getText());
        }
        pieDataset.setOtherKeyExpression( exp );
    }
    
    public void jRTextExpressionOtherLabelTextChanged()
    {
        JRDesignExpression exp = null;
        if (jRTextExpressionOtherLabel.getText().trim().length() > 0)
        {
            exp = new JRDesignExpression();
            exp.setValueClassName("java.lang.String");//NOI18N
            exp.setText(jRTextExpressionOtherLabel.getText());
        }
        pieDataset.setOtherLabelExpression( exp );
    }  
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPopupMenuPieSeries = new javax.swing.JPopupMenu();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemPaste = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanelData = new javax.swing.JPanel();
        jPanelSeries = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jButtonAdd = new javax.swing.JButton();
        jButtonModify = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButtonMode = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabelKeyExpression1 = new javax.swing.JLabel();
        jRTextExpressionOtherKey = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jLabelLabelExpression1 = new javax.swing.JLabel();
        jRTextExpressionOtherLabel = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        sectionItemHyperlinkPanel2 = new com.jaspersoft.ireport.designer.tools.HyperlinkPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabelMaxCount = new javax.swing.JLabel();
        jSpinnerMaxCount = new javax.swing.JSpinner();
        jLabelMinPercentage = new javax.swing.JLabel();
        jSpinnerMinPercentage = new javax.swing.JSpinner();

        jMenuItemCopy.setText("Copy series");
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyActionPerformed(evt);
            }
        });
        jPopupMenuPieSeries.add(jMenuItemCopy);

        jMenuItemPaste.setText("Paste series");
        jMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteActionPerformed(evt);
            }
        });
        jPopupMenuPieSeries.add(jMenuItemPaste);

        setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanelData.setLayout(new java.awt.BorderLayout());

        jPanelSeries.setLayout(new java.awt.GridBagLayout());

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelSeries.add(jScrollPane1, gridBagConstraints);

        jPanel4.setMinimumSize(new java.awt.Dimension(100, 0));
        jPanel4.setPreferredSize(new java.awt.Dimension(100, 0));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jButtonAdd.setText("Add");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 4);
        jPanel4.add(jButtonAdd, gridBagConstraints);

        jButtonModify.setText("Modify");
        jButtonModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        jPanel4.add(jButtonModify, gridBagConstraints);

        jButtonRemove.setText("Remove");
        jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        jPanel4.add(jButtonRemove, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 99;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jPanel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelSeries.add(jPanel4, gridBagConstraints);

        jPanelData.add(jPanelSeries, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanelData, gridBagConstraints);

        jButtonMode.setText("Use more series...");
        jButtonMode.setPreferredSize(new java.awt.Dimension(150, 23));
        jButtonMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel3.add(jButtonMode, gridBagConstraints);

        jTabbedPane1.addTab("Pie series", jPanel3);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabelKeyExpression1.setText("Other Key expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel7.add(jLabelKeyExpression1, gridBagConstraints);

        jRTextExpressionOtherKey.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionOtherKey.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionOtherKey.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel7.add(jRTextExpressionOtherKey, gridBagConstraints);

        jLabelLabelExpression1.setText("Other Label expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel7.add(jLabelLabelExpression1, gridBagConstraints);

        jRTextExpressionOtherLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionOtherLabel.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionOtherLabel.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel7.add(jRTextExpressionOtherLabel, gridBagConstraints);

        jTabbedPane1.addTab("Other section value", jPanel7);
        jTabbedPane1.addTab("Other section hyperlink", sectionItemHyperlinkPanel2);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        jTabbedPane1.getAccessibleContext().setAccessibleName("");

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelMaxCount.setText("Max number of slices to show");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        jPanel1.add(jLabelMaxCount, gridBagConstraints);

        jSpinnerMaxCount.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jSpinnerMaxCount, gridBagConstraints);

        jLabelMinPercentage.setText("Min slice percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        jPanel1.add(jLabelMinPercentage, gridBagConstraints);

        jSpinnerMinPercentage.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel1.add(jSpinnerMinPercentage, gridBagConstraints);

        add(jPanel1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (evt.getClickCount() == 1 && evt.getButton() == evt.BUTTON3) {
            jMenuItemCopy.setEnabled(jList1.getSelectedIndex() >= 0);
            jMenuItemPaste.setEnabled( IReportManager.getInstance().getChartSeriesClipBoard() != null &&
                    IReportManager.getInstance().getChartSeriesClipBoard().size() > 0);

            jPopupMenuPieSeries.show(jList1, evt.getPoint().x, evt.getPoint().y);
        } else if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1) {
            jButtonModifyActionPerformed(null);
        }
}//GEN-LAST:event_jList1MouseClicked

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged

        if (jList1.getSelectedIndex() >= 0) {
            jButtonModify.setEnabled( true );
            jButtonRemove.setEnabled( true );
        } else {
            jButtonModify.setEnabled( false );
            jButtonRemove.setEnabled( false );
        }
}//GEN-LAST:event_jList1ValueChanged

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed

        PieSeriesDialog csd = new PieSeriesDialog(Misc.getMainFrame() ,true);
        csd.setExpressionContext( this.getExpressionContext() );
        csd.setPieSeries(new JRDesignPieSeries());
        csd.setVisible(true);

        if (csd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {

            JRDesignPieSeries series = csd.getPieSeries();
            getPieDataset().getSeriesList().add(series);
            ((javax.swing.DefaultListModel)jList1.getModel()).addElement(series);
        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyActionPerformed

        if (jList1.getSelectedIndex() >= 0) {
            JRDesignPieSeries cs = (JRDesignPieSeries)jList1.getSelectedValue();
            PieSeriesDialog csd = new PieSeriesDialog(Misc.getMainFrame() ,true);
            csd.setExpressionContext( this.getExpressionContext() );
            csd.setPieSeries(cs);
            csd.setVisible(true);

            if (csd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
                JRDesignPieSeries modifiedSeries = csd.getPieSeries();
                cs.setKeyExpression( modifiedSeries.getKeyExpression() );
                cs.setValueExpression( modifiedSeries.getValueExpression() );
                cs.setLabelExpression( modifiedSeries.getLabelExpression() );
                cs.setSectionHyperlink( modifiedSeries.getSectionHyperlink() );

                jList1.updateUI();
            }

        }
}//GEN-LAST:event_jButtonModifyActionPerformed

    private void jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveActionPerformed

        while (jList1.getSelectedIndex() >= 0) {
            getPieDataset().getSeriesList().remove( jList1.getSelectedValue() );
            ((javax.swing.DefaultListModel)jList1.getModel()).removeElementAt(jList1.getSelectedIndex());
        }
    }//GEN-LAST:event_jButtonRemoveActionPerformed

    private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopyActionPerformed
        Object[] values = jList1.getSelectedValues();
        java.util.List copy_c = new ArrayList();
        try {
            for (int i=0; i<values.length; ++i) copy_c.add( ((JRDesignPieSeries)values[i]).clone() );
            IReportManager.getInstance().setChartSeriesClipBoard(copy_c);
        } catch (Exception ex) { }
}//GEN-LAST:event_jMenuItemCopyActionPerformed

    private void jMenuItemPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteActionPerformed
        java.util.List series = IReportManager.getInstance().getChartSeriesClipBoard();
        //getChartSeriesClipBoard()

        if (series != null && series.size() > 0) {
            for (int i=0; i<series.size(); ++i) {
                if (series.get(i) instanceof JRDesignPieSeries) {
                    JRDesignPieSeries cs = (JRDesignPieSeries)series.get(i);
                    try {
                        cs = (JRDesignPieSeries)cs.clone();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        continue;
                    }
                    getPieDataset().addPieSeries(cs);
                    ((javax.swing.DefaultListModel)jList1.getModel()).addElement(cs);
                }
            }
            jList1.updateUI();
        }
}//GEN-LAST:event_jMenuItemPasteActionPerformed

    private void jButtonModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModeActionPerformed

        setMode( currentMode == SINGLE_SERIES_MODE ? MULTIPLE_SERIES_MODE : SINGLE_SERIES_MODE );
    }//GEN-LAST:event_jButtonModeActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonMode;
    private javax.swing.JButton jButtonModify;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JLabel jLabelKeyExpression1;
    private javax.swing.JLabel jLabelLabelExpression1;
    private javax.swing.JLabel jLabelMaxCount;
    private javax.swing.JLabel jLabelMinPercentage;
    private javax.swing.JList jList1;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemPaste;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanelData;
    private javax.swing.JPanel jPanelSeries;
    private javax.swing.JPopupMenu jPopupMenuPieSeries;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionOtherKey;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionOtherLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerMaxCount;
    private javax.swing.JSpinner jSpinnerMinPercentage;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.jaspersoft.ireport.designer.tools.HyperlinkPanel sectionItemHyperlinkPanel2;
    // End of variables declaration//GEN-END:variables

    public void setFocusedExpression(Object[] expressionInfo) {
        
    }

    public void containerWindowOpened() {

    }

    /**
     * @return the expressionContext
     */
    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }
    

}
