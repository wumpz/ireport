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

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.utils.Misc;
import net.sf.jasperreports.charts.design.JRDesignHighLowDataset;



/**
 *
 * @author  Administrator
 */
public class HighLowDatasetPanel extends javax.swing.JPanel implements ChartDatasetPanel {
    
    private JRDesignHighLowDataset highLowDataset = null;
    private ExpressionContext expressionContext = null;
    
    /** Creates new form PieDatasetPanel */
    public HighLowDatasetPanel() {
        initComponents();
        
        //applyI18n();
        
        this.jRTextExpressionSeries.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionSeriesTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionSeriesTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionSeriesTextChanged();
            }
        });
        
        
        this.jRTextExpressionDate.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionDateTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionDateTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionDateTextChanged();
            }
        });
        
        this.jRTextExpressionVolume.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionVolumeTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionVolumeTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionVolumeTextChanged();
            }
        });
        
        this.jRTextExpressionHigh.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionHighTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionHighTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionHighTextChanged();
            }
        });
        
        this.jRTextExpressionLow.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionLowTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionLowTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionLowTextChanged();
            }
        });
        
        this.jRTextExpressionOpen.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionOpenTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionOpenTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionOpenTextChanged();
            }
        });
        
        this.jRTextExpressionClose.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionCloseTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionCloseTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionCloseTextChanged();
            }
        });
    }

    public JRDesignHighLowDataset getHighLowDataset() {
        return highLowDataset;
    }
    
    /**
     * this method is used to pass the correct subdataset to the expression editor
     */
    public void setExpressionContext( ExpressionContext ec )
    {
        jRTextExpressionSeries.setExpressionContext(ec);
        jRTextExpressionDate.setExpressionContext(ec);
        jRTextExpressionHigh.setExpressionContext(ec);
        jRTextExpressionLow.setExpressionContext(ec);
        jRTextExpressionOpen.setExpressionContext(ec);
        jRTextExpressionClose.setExpressionContext(ec);
        jRTextExpressionVolume.setExpressionContext(ec);
        sectionItemHyperlinkPanel1.setExpressionContext(ec);
    }

    public void setHighLowDataset(JRDesignHighLowDataset highLowDataset) {
        this.highLowDataset = highLowDataset;
        jRTextExpressionSeries.setText( Misc.getExpressionText( highLowDataset.getSeriesExpression()) );
        jRTextExpressionDate.setText( Misc.getExpressionText(highLowDataset.getDateExpression()) );
        jRTextExpressionHigh.setText( Misc.getExpressionText(highLowDataset.getHighExpression()) );
        jRTextExpressionLow.setText( Misc.getExpressionText(highLowDataset.getLowExpression()) );
        jRTextExpressionOpen.setText( Misc.getExpressionText(highLowDataset.getOpenExpression()) );
        jRTextExpressionClose.setText( Misc.getExpressionText(highLowDataset.getCloseExpression()) );
        jRTextExpressionVolume.setText( Misc.getExpressionText(highLowDataset.getVolumeExpression()) );
        sectionItemHyperlinkPanel1.setHyperlink(highLowDataset.getItemHyperlink());
    }
    
    public void jRTextExpressionSeriesTextChanged()
    {
        highLowDataset.setSeriesExpression( 
                Misc.createExpression(null,  jRTextExpressionSeries.getText()) );

    }
    
    public void jRTextExpressionDateTextChanged()
    {
        highLowDataset.setDateExpression( 
                Misc.createExpression("java.util.Date",  jRTextExpressionDate.getText()) );//NOI18N
    }
    
    public void jRTextExpressionHighTextChanged()
    {
        highLowDataset.setHighExpression( 
                Misc.createExpression("java.lang.Number",  jRTextExpressionHigh.getText()) );//NOI18N
    }
    
    public void jRTextExpressionLowTextChanged()
    {
        highLowDataset.setLowExpression( 
                Misc.createExpression("java.lang.Number",  jRTextExpressionLow.getText()) );//NOI18N
    }
    
    public void jRTextExpressionOpenTextChanged()
    {
        highLowDataset.setOpenExpression(
                Misc.createExpression("java.lang.Number",  jRTextExpressionOpen.getText()) );//NOI18N
    }
    
    public void jRTextExpressionCloseTextChanged()
    {
        highLowDataset.setCloseExpression( 
                Misc.createExpression("java.lang.Number",  jRTextExpressionClose.getText()) );//NOI18N
    }
    
    public void jRTextExpressionVolumeTextChanged()
    {
        highLowDataset.setVolumeExpression( 
                Misc.createExpression("java.lang.Number",  jRTextExpressionVolume.getText()) );//NOI18N
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabelSeriesExpression = new javax.swing.JLabel();
        jLabelDateExpression = new javax.swing.JLabel();
        jRTextExpressionSeries = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jRTextExpressionDate = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelHL = new javax.swing.JPanel();
        jLabelHighExpression = new javax.swing.JLabel();
        jRTextExpressionHigh = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jLabelLowExpression = new javax.swing.JLabel();
        jRTextExpressionLow = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanelOC = new javax.swing.JPanel();
        jLabelOpenExpression = new javax.swing.JLabel();
        jRTextExpressionOpen = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jLabelCloseExpression = new javax.swing.JLabel();
        jRTextExpressionClose = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanel1 = new javax.swing.JPanel();
        jLabelVolumeExpression = new javax.swing.JLabel();
        jRTextExpressionVolume = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        sectionItemHyperlinkPanel1 = new com.jaspersoft.ireport.designer.tools.HyperlinkPanel();

        setLayout(new java.awt.GridBagLayout());

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabelSeriesExpression.setText(I18n.getString("HighLowDatasetPanel.Label.SeriesExpression"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jLabelSeriesExpression, gridBagConstraints);

        jLabelDateExpression.setText(I18n.getString("HighLowDatasetPanel.Label.DateExpression"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jLabelDateExpression, gridBagConstraints);

        jRTextExpressionSeries.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionSeries.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionSeries.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        jPanel2.add(jRTextExpressionSeries, gridBagConstraints);

        jRTextExpressionDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionDate.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionDate.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        jPanel2.add(jRTextExpressionDate, gridBagConstraints);

        jPanelHL.setLayout(new java.awt.GridBagLayout());

        jLabelHighExpression.setText(I18n.getString("HighLowDatasetPanel.Label.HighExpression")); 
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelHL.add(jLabelHighExpression, gridBagConstraints);

        jRTextExpressionHigh.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionHigh.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionHigh.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelHL.add(jRTextExpressionHigh, gridBagConstraints);

        jLabelLowExpression.setText(I18n.getString("HighLowDatasetPanel.Label.LowExpression")); 
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanelHL.add(jLabelLowExpression, gridBagConstraints);

        jRTextExpressionLow.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionLow.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionLow.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        jPanelHL.add(jRTextExpressionLow, gridBagConstraints);

        jTabbedPane1.addTab(I18n.getString("HighLowDatasetPanel.Panel.HighLow"), jPanelHL);

        jPanelOC.setLayout(new java.awt.GridBagLayout());

        jLabelOpenExpression.setText(I18n.getString("HighLowDatasetPanel.Label.OpenExpression")); 
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelOC.add(jLabelOpenExpression, gridBagConstraints);

        jRTextExpressionOpen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionOpen.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionOpen.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelOC.add(jRTextExpressionOpen, gridBagConstraints);

        jLabelCloseExpression.setText(I18n.getString("HighLowDatasetPanel.Label.CloseExpression")); 
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelOC.add(jLabelCloseExpression, gridBagConstraints);

        jRTextExpressionClose.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionClose.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionClose.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelOC.add(jRTextExpressionClose, gridBagConstraints);

        jTabbedPane1.addTab(I18n.getString("HighLowDatasetPanel.Panel.OpenClose"), jPanelOC);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelVolumeExpression.setText(I18n.getString("HighLowDatasetPanel.Label.VolumeExpression"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jLabelVolumeExpression, gridBagConstraints);

        jRTextExpressionVolume.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionVolume.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionVolume.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jRTextExpressionVolume, gridBagConstraints);

        jTabbedPane1.addTab(I18n.getString("HighLowDatasetPanel.Panel.Volume"), jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jTabbedPane1, gridBagConstraints);

        jTabbedPane2.addTab(I18n.getString("HighLowDatasetPanel.Pane.Data"), jPanel2);
        jTabbedPane2.addTab(I18n.getString("HighLowDatasetPanel.Pane.ItemHyperlink"), sectionItemHyperlinkPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jTabbedPane2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelCloseExpression;
    private javax.swing.JLabel jLabelDateExpression;
    private javax.swing.JLabel jLabelHighExpression;
    private javax.swing.JLabel jLabelLowExpression;
    private javax.swing.JLabel jLabelOpenExpression;
    private javax.swing.JLabel jLabelSeriesExpression;
    private javax.swing.JLabel jLabelVolumeExpression;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelHL;
    private javax.swing.JPanel jPanelOC;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionClose;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionDate;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionHigh;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionLow;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionOpen;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionSeries;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionVolume;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private com.jaspersoft.ireport.designer.tools.HyperlinkPanel sectionItemHyperlinkPanel1;
    // End of variables declaration//GEN-END:variables
    
    /*
    public void applyI18n()
    {
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
        jLabelSeriesExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.seriesExpression", "Series expression"));
        jLabelDateExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.dateExpression", "Date expression"));
        jLabelHighExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.highExpression", "High expression"));
        jLabelLowExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.lowExpression", "Low expression"));
        jLabelOpenExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.openExpression", "Open expression"));
        jLabelCloseExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.closeExpression", "Close expression"));
        jLabelVolumeExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.volumeExpression", "Volume expression"));
        
        jTabbedPane1.setTitleAt(0,I18n.getString("chartSeries.tab.HighLow","High/Low"));
        jTabbedPane1.setTitleAt(1,I18n.getString("chartSeries.tab.OpenClose","Open/Close"));
        jTabbedPane1.setTitleAt(2,I18n.getString("chartSeries.tab.Volume","Volume"));
        
        jTabbedPane2.setTitleAt(0,I18n.getString("chartSeries.tab.Data","Data"));
        jTabbedPane2.setTitleAt(1,I18n.getString("chartSeries.tab.ItemHyperlink","Item hyperlink"));
        
        this.updateUI();
        
    }
     */
    
    public static final int COMPONENT_NONE=0;
    public static final int COMPONENT_SERIES_EXPRESSION=1;
    public static final int COMPONENT_DATA_EXPRESSION=2;
    public static final int COMPONENT_HIGH_EXPRESSION=3;
    public static final int COMPONENT_LOW_EXPRESSION=4;
    public static final int COMPONENT_OPEN_EXPRESSION=5;
    public static final int COMPONENT_CLOSE_EXPRESSION=6;
    public static final int COMPONENT_VOLUME_EXPRESSION=7;
    public static final int COMPONENT_HYPERLINK=100;
    
    /**
     * This method set the focus on a specific component.
     * Valid constants are something like:
     * COMPONENT_KEY_EXPRESSION, COMPONENT_VALUE_EXPRESSION, ...
     * otherInfo is used here only for COMPONENT_HYPERLINK
     * otherInfo[0] = expression ID
     * otherInfo[1] = parameter #
     * otherInfo[2] = parameter expression ID
     */
    public void setFocusedExpression(Object[] expressionInfo)
    {
        int expID = ((Integer)expressionInfo[0]).intValue();
        switch (expID)
        {
            case COMPONENT_SERIES_EXPRESSION:
                Misc.selectTextAndFocusArea(jRTextExpressionSeries);
                break;
            case COMPONENT_DATA_EXPRESSION:
                Misc.selectTextAndFocusArea(jRTextExpressionDate);
                break;
            case COMPONENT_HIGH_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelHL );
                Misc.selectTextAndFocusArea(jRTextExpressionHigh);
                break;
            case COMPONENT_LOW_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelHL );
                Misc.selectTextAndFocusArea(jRTextExpressionLow);
                break;
            case COMPONENT_OPEN_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelOC );
                Misc.selectTextAndFocusArea(jRTextExpressionOpen);
                break;
            case COMPONENT_CLOSE_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelOC );
                Misc.selectTextAndFocusArea(jRTextExpressionClose);
                break;
            case COMPONENT_VOLUME_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanel1 );
                Misc.selectTextAndFocusArea(jRTextExpressionVolume);
                break;
            case COMPONENT_HYPERLINK:
                jTabbedPane2.setSelectedComponent( sectionItemHyperlinkPanel1 );
                Object newInfo[] = new Object[expressionInfo.length -1 ];
                for (int i=1; i< expressionInfo.length; ++i) newInfo[i-1] = expressionInfo[i];
                sectionItemHyperlinkPanel1.setFocusedExpression(newInfo);
                break;   
        }
    }
    
    public void containerWindowOpened() {
        sectionItemHyperlinkPanel1.openExtraWindows();
    }
         
}
