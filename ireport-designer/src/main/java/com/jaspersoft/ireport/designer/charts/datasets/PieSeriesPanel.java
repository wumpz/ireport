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

import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.utils.Misc;
import net.sf.jasperreports.charts.design.JRDesignPieSeries;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;

/**
 *
 * @author gtoffoli
 */
public class PieSeriesPanel extends javax.swing.JPanel {

    private JRDesignPieSeries pieSeries = new JRDesignPieSeries();
    private ExpressionContext expressionContext = null;

    /** Creates new form PieSeriesPanel */
    public PieSeriesPanel() {
        initComponents();

         this.jRTextExpressionKey.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionKeyTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionKeyTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionKeyTextChanged();
            }
        });

        this.jRTextExpressionValue.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionValueTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionValueTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionValueTextChanged();
            }
        });

        this.jRTextExpressionLabel.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionLabelTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionLabelTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionLabelTextChanged();
            }
        });
    }

    /**
     * this method is used to pass the correct subdataset to the expression editor
     */
    public void setExpressionContext(ExpressionContext ec)
    {
        this.expressionContext = ec;
        jRTextExpressionKey.setExpressionContext(ec);
        jRTextExpressionLabel.setExpressionContext(ec);
        jRTextExpressionValue.setExpressionContext(ec);

        sectionItemHyperlinkPanel1.setExpressionContext(ec);
    }

    public void jRTextExpressionKeyTextChanged()
    {
        JRDesignExpression exp = null;
        if (jRTextExpressionKey.getText().trim().length() > 0)
        {
            exp = new JRDesignExpression();
            exp.setValueClassName("java.lang.Object");//NOI18N
            exp.setText(jRTextExpressionKey.getText());
        }
        getPieSeries().setKeyExpression( exp );
    }

    public void jRTextExpressionLabelTextChanged()
    {
        JRDesignExpression exp = null;
        if (jRTextExpressionLabel.getText().trim().length() > 0)
        {
            exp = new JRDesignExpression();
            exp.setValueClassName("java.lang.String");//NOI18N
            exp.setText(jRTextExpressionLabel.getText());
        }
        getPieSeries().setLabelExpression( exp );
    }

    public void jRTextExpressionValueTextChanged()
    {
        JRDesignExpression exp = null;
        if (jRTextExpressionValue.getText().trim().length() > 0)
        {
            exp = new JRDesignExpression();
            exp.setValueClassName("java.lang.Number");//NOI18N
            exp.setText(jRTextExpressionValue.getText());
        }
        getPieSeries().setValueExpression( exp );
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabelKeyExpression = new javax.swing.JLabel();
        jRTextExpressionKey = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jLabelValueExpression = new javax.swing.JLabel();
        jRTextExpressionValue = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jLabelLabelExpression = new javax.swing.JLabel();
        jRTextExpressionLabel = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanel2 = new javax.swing.JPanel();
        sectionItemHyperlinkPanel1 = new com.jaspersoft.ireport.designer.tools.HyperlinkPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelKeyExpression.setText(org.openide.util.NbBundle.getMessage(PieSeriesPanel.class, "PieSeriesPanel.jLabelKeyExpression.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jLabelKeyExpression, gridBagConstraints);

        jRTextExpressionKey.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionKey.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionKey.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jRTextExpressionKey, gridBagConstraints);

        jLabelValueExpression.setText(org.openide.util.NbBundle.getMessage(PieSeriesPanel.class, "PieSeriesPanel.jLabelValueExpression.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel1.add(jLabelValueExpression, gridBagConstraints);

        jRTextExpressionValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionValue.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionValue.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jRTextExpressionValue, gridBagConstraints);

        jLabelLabelExpression.setText(org.openide.util.NbBundle.getMessage(PieSeriesPanel.class, "PieSeriesPanel.jLabelLabelExpression.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel1.add(jLabelLabelExpression, gridBagConstraints);

        jRTextExpressionLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionLabel.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionLabel.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jRTextExpressionLabel, gridBagConstraints);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(PieSeriesPanel.class, "PieSeriesPanel.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(sectionItemHyperlinkPanel1, gridBagConstraints);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(PieSeriesPanel.class, "PieSeriesPanel.jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelKeyExpression;
    private javax.swing.JLabel jLabelLabelExpression;
    private javax.swing.JLabel jLabelValueExpression;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionKey;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionLabel;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionValue;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.jaspersoft.ireport.designer.tools.HyperlinkPanel sectionItemHyperlinkPanel1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the pieSeries
     */
    public JRDesignPieSeries getPieSeries() {
        return pieSeries;
    }

    /**
     * @param pieDataset the pieDataset to set
     */
    public void setPieSeries(JRDesignPieSeries originalPieSeries) {
        this.pieSeries = originalPieSeries;

        jRTextExpressionKey.setText(  Misc.getExpressionText(pieSeries.getKeyExpression()) );
        jRTextExpressionValue.setText(  Misc.getExpressionText(pieSeries.getValueExpression()) );
        jRTextExpressionLabel.setText(  Misc.getExpressionText(pieSeries.getLabelExpression()) );


        if (pieSeries.getSectionHyperlink() == null)
        {
            JRDesignHyperlink hl = new JRDesignHyperlink();
            hl.setHyperlinkType( HyperlinkTypeEnum.NONE);
            pieSeries.setSectionHyperlink(hl);
        }
        sectionItemHyperlinkPanel1.setHyperlink( pieSeries.getSectionHyperlink() );

    }

}
