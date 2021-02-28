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
package com.jaspersoft.ireport.designer.charts;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.charts.datasets.CategoryDatasetPanel;
import com.jaspersoft.ireport.designer.charts.datasets.ChartDatasetPanel;
import com.jaspersoft.ireport.designer.charts.datasets.GanttDatasetPanel;
import com.jaspersoft.ireport.designer.charts.datasets.HighLowDatasetPanel;
import com.jaspersoft.ireport.designer.charts.datasets.PieDatasetPanel;
import com.jaspersoft.ireport.designer.charts.datasets.TimePeriodDatasetPanel;
import com.jaspersoft.ireport.designer.charts.datasets.TimeSeriesDatasetPanel;
import com.jaspersoft.ireport.designer.charts.datasets.ValueDatasetPanel;
import com.jaspersoft.ireport.designer.charts.datasets.XYDatasetPanel;
import com.jaspersoft.ireport.designer.charts.datasets.XYZDatasetPanel;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.tools.DatasetParametersTableCellRenderer;
import com.jaspersoft.ireport.designer.tools.JRDatasetParameterDialog;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableColumnModel;
import net.sf.jasperreports.charts.design.JRDesignCategoryDataset;
import net.sf.jasperreports.charts.design.JRDesignGanttDataset;
import net.sf.jasperreports.charts.design.JRDesignHighLowDataset;
import net.sf.jasperreports.charts.design.JRDesignPieDataset;
import net.sf.jasperreports.charts.design.JRDesignTimePeriodDataset;
import net.sf.jasperreports.charts.design.JRDesignTimeSeriesDataset;
import net.sf.jasperreports.charts.design.JRDesignValueDataset;
import net.sf.jasperreports.charts.design.JRDesignXyDataset;
import net.sf.jasperreports.charts.design.JRDesignXyzDataset;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;


/**
 *
 * @author  Administrator
 */
public class ChartPropertiesDialog extends javax.swing.JDialog {
    
    private JRDesignChart currentSelectedChartElement = null;
    private int dialogResult = javax.swing.JOptionPane.OK_OPTION;
    private JasperDesign jasperDesign = null;
    private ExpressionContext expressionContext = null;

    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }
    
    private boolean init = false;
       
    @SuppressWarnings("unchecked")
    public void setChartElement(JRDesignChart chartReportElement, JasperDesign jd)
    {
        setInit(true);
        this.jasperDesign = jd;
        
        try {
            
            currentSelectedChartElement = chartReportElement;

            jButtonPaste.setEnabled( IReportManager.getInstance().getChartDatasetClipBoard() != null ) ;

            if (currentSelectedChartElement == null)
            {
                // We have to clean all the gui....
                jComboBoxTypeOfData.removeAllItems();
                jPanelDataDefinition.removeAll();
            }
            else
            {
                updateGroups();
                updateSubDatasets();
              
                
                // Set general dataset data...
                
                Misc.setComboboxSelectedTagValue(jComboBoxIncrementType, currentSelectedChartElement.getDataset().getIncrementTypeValue() );
                jComboBoxIncrementGroup.setEnabled(currentSelectedChartElement.getDataset().getIncrementTypeValue() == IncrementTypeEnum.GROUP);
                if (currentSelectedChartElement.getDataset().getIncrementTypeValue() == IncrementTypeEnum.GROUP)
                {
                    jComboBoxIncrementGroup.setSelectedItem( currentSelectedChartElement.getDataset().getIncrementGroup().getName() );
                }
                
                Misc.setComboboxSelectedTagValue(jComboBoxResetType, currentSelectedChartElement.getDataset().getResetTypeValue() );
                jComboBoxResetGroup.setEnabled(currentSelectedChartElement.getDataset().getResetTypeValue() == ResetTypeEnum.GROUP);
                if (currentSelectedChartElement.getDataset().getResetTypeValue() == ResetTypeEnum.GROUP)
                {
                    jComboBoxResetGroup.setSelectedItem( currentSelectedChartElement.getDataset().getResetGroup().getName() );
                }
                
                jRTextExpressionAreaFilterExpression.setText( Misc.getExpressionText( currentSelectedChartElement.getDataset().getIncrementWhenExpression() ) );

                JRDesignDataset dataset = getChartDataset();
                        
                if (currentSelectedChartElement.getDataset().getDatasetRun() != null)
                {
                    JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)currentSelectedChartElement.getDataset().getDatasetRun();
                    
                    jComboBoxSubDataset.setSelectedItem(datasetRun.getDatasetName());
                    //jPanel7.remove(jTabbedPaneSubDataset);
                    //jTabbedPaneSubDataset.setVisible(true);
                    if (jPanel7.getComponentCount() == 0)
                    {
                        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridy = 1;
                        gridBagConstraints.gridwidth = 2;
                        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
                        jPanel7.add(jTabbedPaneSubDataset, gridBagConstraints);
                        jPanel7.updateUI();
                    }

                    jRTextExpressionAreaMapExpression.setText( Misc.getExpressionText( datasetRun.getParametersMapExpression() ) );

                    int connectionType = 0;
                    
                    if ( datasetRun.getConnectionExpression() != null)
                    {
                        connectionType = 1;
                    }
                    if ( datasetRun.getDataSourceExpression() != null)
                    {
                        connectionType = 2;
                    }
                    
                    
                    if (connectionType == 0) {
                        this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                        this.jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
                    }
                    else if (connectionType == 1) {
                        this.jComboBoxDatasetConnectionType.setSelectedIndex(1);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
                        this.jRTextExpressionAreaTextConnectionExpression.setText( Misc.getExpressionText( datasetRun.getConnectionExpression() ));
                    }
                    else {
                        this.jComboBoxDatasetConnectionType.setSelectedIndex(2);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
                        this.jRTextExpressionAreaTextConnectionExpression.setText( Misc.getExpressionText( datasetRun.getDataSourceExpression()) );
                    }

                    //Add parameters...
                    javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                    dtm.setRowCount(0);

                    JRDatasetParameter[] params = datasetRun.getParameters();
                    for (int i=0; i<params.length; ++i) {
                        JRDatasetParameter parameter = params[i];
                        Vector row = new Vector();
                        row.addElement(parameter);
                        row.addElement( Misc.getExpressionText( parameter.getExpression() ) );
                        dtm.addRow(row);
                    }            

                    dataset = (JRDesignDataset)getJasperDesign().getDatasetMap().get( datasetRun.getDatasetName() );
                }
                else
                {
                    javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                    dtm.setRowCount(0);
                    this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                    this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                    this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                    this.jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
                    jRTextExpressionAreaMapExpression.setText("");//NOI18N

                    jComboBoxSubDataset.setSelectedIndex(0);
                    //jTabbedPaneSubDataset.setVisible(false);
                    jPanel7.remove(jTabbedPaneSubDataset);
                }

                java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 4;
                gridBagConstraints.gridy = 2;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.fill = GridBagConstraints.BOTH;
                gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);

                int chartType = currentSelectedChartElement.getChartType();
                
                jComboBoxTypeOfData.removeAllItems();
                
                if (chartType == JRChart.CHART_TYPE_PIE ||
                    chartType == JRChart.CHART_TYPE_PIE3D)
                {
                    jComboBoxTypeOfData.addItem(new Tag("net.sf.jasperreports.charts.design.JRDesignPieDataset", I18n.getString("ChartPropertiesDialog.ComboBox.pie_dataset")));
                }
                else if (chartType == JRChart.CHART_TYPE_BAR ||
                         chartType == JRChart.CHART_TYPE_BAR3D ||
                         chartType == JRChart.CHART_TYPE_STACKEDBAR ||
                         chartType == JRChart.CHART_TYPE_STACKEDBAR3D ||
                         chartType == JRChart.CHART_TYPE_LINE ||
                         chartType == JRChart.CHART_TYPE_AREA ||
                         chartType == JRChart.CHART_TYPE_STACKEDAREA )
                {
                    jComboBoxTypeOfData.addItem(new Tag("net.sf.jasperreports.charts.design.JRDesignCategoryDataset", I18n.getString("ChartPropertiesDialog.ComboBox.category_dataset")));
                }
                else if (chartType == JRChart.CHART_TYPE_XYBAR)
                {
                    jComboBoxTypeOfData.addItem(new Tag("net.sf.jasperreports.charts.design.JRDesignTimePeriodDataset", I18n.getString("ChartPropertiesDialog.ComboBox.time_period_dataset")));
                    jComboBoxTypeOfData.addItem(new Tag("net.sf.jasperreports.charts.design.JRDesignTimeSeriesDataset", I18n.getString("ChartPropertiesDialog.ComboBox.time_series_dataset")));
                    jComboBoxTypeOfData.addItem(new Tag("net.sf.jasperreports.charts.design.JRDesignXyDataset", I18n.getString("ChartPropertiesDialog.ComboBox.XY_dataset")));
                }
                else if ( chartType == JRChart.CHART_TYPE_XYLINE ||
                          chartType == JRChart.CHART_TYPE_XYAREA || 
                          chartType == JRChart.CHART_TYPE_SCATTER )
                {   
                    jComboBoxTypeOfData.addItem(new Tag("net.sf.jasperreports.charts.design.JRDesignXyDataset", I18n.getString("ChartPropertiesDialog.ComboBox.XY_dataset")));
                }
                else if ( chartType == JRChart.CHART_TYPE_BUBBLE )
                {
                    jComboBoxTypeOfData.addItem(new Tag("net.sf.jasperreports.charts.design.JRDesignXyzDataset", I18n.getString("ChartPropertiesDialog.ComboBox.XYZ_dataset")));
                }
                else if ( chartType == JRChart.CHART_TYPE_TIMESERIES )
                {
                    jComboBoxTypeOfData.addItem(new Tag("net.sf.jasperreports.charts.design.JRDesignTimeSeriesDataset", I18n.getString("ChartPropertiesDialog.ComboBox.time_series_dataset")));
                }
                else if ( chartType == JRChart.CHART_TYPE_HIGHLOW ||
                          chartType == JRChart.CHART_TYPE_CANDLESTICK)
                {
                    
                    jComboBoxTypeOfData.addItem(new Tag("net.sf.jasperreports.charts.design.JRDesignHighLowDataset", I18n.getString("ChartPropertiesDialog.ComboBox.high_low_dataset")));
                }
                else if ( chartType == JRChart.CHART_TYPE_METER ||
                          chartType == JRChart.CHART_TYPE_THERMOMETER)
                {
                    jComboBoxTypeOfData.addItem(new Tag("net.sf.jasperreports.charts.design.JRDesignValueDataset", I18n.getString("ChartPropertiesDialog.ComboBox.value_dataset")));
                }
                else if ( chartType == JRChart.CHART_TYPE_GANTT)
                {
                    jComboBoxTypeOfData.addItem(new Tag("net.sf.jasperreports.charts.design.JRDesignGanttDataset", I18n.getString("ChartPropertiesDialog.ComboBox.gantt_dataset")));
                }

                setDatasetPanel( currentSelectedChartElement.getDataset(), dataset  );
                setExpressionContext( new ExpressionContext( ModelUtils.getDatasetFromChartDataset((JRDesignChartDataset)currentSelectedChartElement.getDataset(), jd) ) );
            }
            
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        ExpressionContext docEc = new ExpressionContext( jd.getMainDesignDataset() );
        jRTextExpressionAreaMapExpression.setExpressionContext(docEc);
        jRTextExpressionAreaTextConnectionExpression.setExpressionContext(docEc);
        setInit(false);
    }
         
    public ChartPropertiesDialog(Dialog parent, boolean modal) 
    {
         super(parent,modal);
         initAll();
    }

    /** Creates new form ReportQueryFrame */
    public ChartPropertiesDialog(Frame parent, boolean modal) 
    {
         super(parent,modal);
         initAll();
    }


    public void initAll()
    {
        initComponents();
        
        
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, -1, -1, -1);
        
        //applyI18n();
        
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.NONE,I18n.getString("ChartPropertiesDialog.ComboBoxReset.none")));
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.REPORT,I18n.getString("ChartPropertiesDialog.ComboBoxReset.report")));
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.PAGE,I18n.getString("ChartPropertiesDialog.ComboBoxReset.page")));
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.COLUMN,I18n.getString("ChartPropertiesDialog.ComboBoxReset.column")));
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.GROUP,I18n.getString("ChartPropertiesDialog.ComboBoxReset.group")));
        
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.NONE,I18n.getString("ChartPropertiesDialog.ComboBoxIncrementType.none")));
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.REPORT,I18n.getString("ChartPropertiesDialog.ComboBoxIncrementType.report")));
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.PAGE,I18n.getString("ChartPropertiesDialog.ComboBoxIncrementType.page")));
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.COLUMN,I18n.getString("ChartPropertiesDialog.ComboBoxIncrementType.column")));
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.GROUP,I18n.getString("ChartPropertiesDialog.ComboBoxIncrementType.group")));
         
        jComboBoxDatasetConnectionType.addItem(new Tag(I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.noConnectionNoDatasource"),I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.noConnectionNoDatasource")));
        jComboBoxDatasetConnectionType.addItem(new Tag(I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.connExpression"),I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.connExpression")));
        jComboBoxDatasetConnectionType.addItem(new Tag(I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.datasourceExpr"),I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.datasourceExpr")));
        
        this.jRTextExpressionAreaMapExpression.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
        });
        
        this.jRTextExpressionAreaFilterExpression.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaFilterExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaFilterExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaFilterExpressionTextChanged();
            }
        });
        
        this.jRTextExpressionAreaTextConnectionExpression.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
        });
        
        javax.swing.DefaultListSelectionModel dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableDatasetParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableDatasetParametersListSelectionValueChanged(e);
            }
        });
        
        
        
        
        this.pack();
        this.setLocationRelativeTo(null);
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCloseActionPerformed(e);
            }
        };
       
        DatasetParametersTableCellRenderer dpcr = new DatasetParametersTableCellRenderer();
        ((DefaultTableColumnModel)jTableDatasetParameters.getColumnModel()).getColumn(0).setCellRenderer(dpcr);
        ((DefaultTableColumnModel)jTableDatasetParameters.getColumnModel()).getColumn(1).setCellRenderer(dpcr);
        
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);

        
        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButtonClose);
    }
    
    public void jRTextExpressionAreaMapExpressionTextChanged() {
        if (this.isInit()) return; 
        if (currentSelectedChartElement != null)
        {
            JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)currentSelectedChartElement.getDataset().getDatasetRun();
            if (datasetRun != null)
            {
                JRDesignExpression exp = null;
                if (jRTextExpressionAreaMapExpression.getText().trim().length() > 0)
                {
                    exp = new JRDesignExpression();
                    exp.setValueClassName("java.util.Map");//NOI18N
                    exp.setText(jRTextExpressionAreaMapExpression.getText());
                }
                
                datasetRun.setParametersMapExpression(exp);
                notifyChange();
            }
        }
    }
    
    public void jRTextExpressionAreaFilterExpressionTextChanged() {
        if (this.isInit()) return; 
        if (currentSelectedChartElement != null)
        {
            JRDesignExpression exp = null;
            if (jRTextExpressionAreaFilterExpression.getText().trim().length() > 0)
            {
                exp = new JRDesignExpression();
                exp.setValueClassName("java.lang.Boolean");//NOI18N
                exp.setText(jRTextExpressionAreaFilterExpression.getText());
            }

            ((JRDesignChartDataset) currentSelectedChartElement.getDataset()).setIncrementWhenExpression(exp);
            notifyChange();
        }
    }

    public void jRTextExpressionAreaTextConnectionExpressionTextChanged() {
        if (this.isInit()) return; 
        if (currentSelectedChartElement != null)
        {
            JRDesignExpression exp = null;
            if (jRTextExpressionAreaTextConnectionExpression.getText().trim().length() > 0)
            {
                exp = new JRDesignExpression();
                exp.setText(jRTextExpressionAreaTextConnectionExpression.getText());
            }
            
            int index = jComboBoxDatasetConnectionType.getSelectedIndex();
            
            if (index == 1)
            {
                if (exp != null) exp.setValueClassName("java.sql.Connection");//NOI18N
                ((JRDesignDatasetRun) currentSelectedChartElement.getDataset().getDatasetRun()).setConnectionExpression(exp);
            }
            else if (index == 2)
            {
                if (exp != null) exp.setValueClassName("net.sf.jasperreports.engine.JRDataSource");//NOI18N
                ((JRDesignDatasetRun) currentSelectedChartElement.getDataset().getDatasetRun()).setDataSourceExpression(exp);
            }
            notifyChange();
         }
    }
    
    public void jTableDatasetParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableDatasetParameters.getSelectedRowCount() > 0) {
            this.jButtonModParameter.setEnabled(true);
            this.jButtonRemParameter.setEnabled(true);
        }
        else {
            this.jButtonModParameter.setEnabled(false);
            this.jButtonRemParameter.setEnabled(false);
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

        jPanelData = new javax.swing.JPanel();
        jLabelTypeOfData = new javax.swing.JLabel();
        jComboBoxTypeOfData = new javax.swing.JComboBox();
        jTabbedPaneData = new javax.swing.JTabbedPane();
        jPanelDataset = new javax.swing.JPanel();
        jLabelResetType = new javax.swing.JLabel();
        jComboBoxResetType = new javax.swing.JComboBox();
        jLabelResetGroup = new javax.swing.JLabel();
        jComboBoxResetGroup = new javax.swing.JComboBox();
        jLabelIncrementType = new javax.swing.JLabel();
        jComboBoxIncrementType = new javax.swing.JComboBox();
        jLabelIncrementGroup = new javax.swing.JLabel();
        jComboBoxIncrementGroup = new javax.swing.JComboBox();
        jLabelIncrementType2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelIncrementType1 = new javax.swing.JLabel();
        jComboBoxSubDataset = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPaneSubDataset = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jComboBoxDatasetConnectionType = new javax.swing.JComboBox();
        jRTextExpressionAreaTextConnectionExpression = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanel4 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDatasetParameters = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jButtonAddParameter = new javax.swing.JButton();
        jButtonModParameter = new javax.swing.JButton();
        jButtonRemParameter = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jRTextExpressionAreaMapExpression = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanel2 = new javax.swing.JPanel();
        jButtonCopy = new javax.swing.JButton();
        jButtonPaste = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jRTextExpressionAreaFilterExpression = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanelDataDefinition = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();

        setTitle("Chart details");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanelData.setLayout(new java.awt.GridBagLayout());

        jLabelTypeOfData.setText("Type of dataset");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelData.add(jLabelTypeOfData, gridBagConstraints);

        jComboBoxTypeOfData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeOfDataActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanelData.add(jComboBoxTypeOfData, gridBagConstraints);

        jPanelDataset.setLayout(new java.awt.GridBagLayout());

        jLabelResetType.setText("Reset type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelResetType, gridBagConstraints);

        jComboBoxResetType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxResetTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxResetType, gridBagConstraints);

        jLabelResetGroup.setText("Reset group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelResetGroup, gridBagConstraints);

        jComboBoxResetGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxResetGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxResetGroup, gridBagConstraints);

        jLabelIncrementType.setText("Increment type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelIncrementType, gridBagConstraints);

        jComboBoxIncrementType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxIncrementTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxIncrementType, gridBagConstraints);

        jLabelIncrementGroup.setText("Increment group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelIncrementGroup, gridBagConstraints);

        jComboBoxIncrementGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxIncrementGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxIncrementGroup, gridBagConstraints);

        jLabelIncrementType2.setText("Filter expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelIncrementType2, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Dataset run"));
        jPanel1.setPreferredSize(new java.awt.Dimension(329, 192));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelIncrementType1.setText("Sub dataset");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel1.add(jLabelIncrementType1, gridBagConstraints);

        jComboBoxSubDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSubDatasetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jComboBoxSubDataset, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel41.setText("Connection / Datasource Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel6.add(jLabel41, gridBagConstraints);

        jComboBoxDatasetConnectionType.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxDatasetConnectionType.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxDatasetConnectionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDatasetConnectionTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanel6.add(jComboBoxDatasetConnectionType, gridBagConstraints);

        jRTextExpressionAreaTextConnectionExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
        jRTextExpressionAreaTextConnectionExpression.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaTextConnectionExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanel6.add(jRTextExpressionAreaTextConnectionExpression, gridBagConstraints);

        jTabbedPaneSubDataset.addTab("Connection/Datasource exp", jPanel6);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel16.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setMinimumSize(new java.awt.Dimension(300, 50));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 50));

        jTableDatasetParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parameter", "Expression"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDatasetParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDatasetParametersMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableDatasetParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel16.add(jScrollPane2, gridBagConstraints);

        jPanel10.setMinimumSize(new java.awt.Dimension(100, 10));
        jPanel10.setPreferredSize(new java.awt.Dimension(100, 69));
        jPanel10.setLayout(new java.awt.GridBagLayout());

        jButtonAddParameter.setText("Add");
        jButtonAddParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel10.add(jButtonAddParameter, gridBagConstraints);

        jButtonModParameter.setText("Modify");
        jButtonModParameter.setEnabled(false);
        jButtonModParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel10.add(jButtonModParameter, gridBagConstraints);

        jButtonRemParameter.setText("Remove");
        jButtonRemParameter.setEnabled(false);
        jButtonRemParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel10.add(jButtonRemParameter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel16.add(jPanel10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jPanel16, gridBagConstraints);

        jTabbedPaneSubDataset.addTab("Parameters", jPanel4);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("Parameters Map Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 0, 0);
        jPanel5.add(jLabel26, gridBagConstraints);

        jRTextExpressionAreaMapExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaMapExpression.setMinimumSize(new java.awt.Dimension(0, 0));
        jRTextExpressionAreaMapExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanel5.add(jRTextExpressionAreaMapExpression, gridBagConstraints);

        jTabbedPaneSubDataset.addTab("Parameters map exp", jPanel5);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel7.add(jTabbedPaneSubDataset, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 100;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelDataset.add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButtonCopy.setText("Copy dataset");
        jButtonCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCopyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel2.add(jButtonCopy, gridBagConstraints);

        jButtonPaste.setText("Paste dataset");
        jButtonPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPasteActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonPaste, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 101;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        jPanelDataset.add(jPanel2, gridBagConstraints);

        jRTextExpressionAreaFilterExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaFilterExpression.setMinimumSize(new java.awt.Dimension(400, 50));
        jRTextExpressionAreaFilterExpression.setPreferredSize(new java.awt.Dimension(400, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        jPanelDataset.add(jRTextExpressionAreaFilterExpression, gridBagConstraints);

        jTabbedPaneData.addTab("Dataset", jPanelDataset);

        jPanelDataDefinition.setLayout(new java.awt.GridBagLayout());
        jTabbedPaneData.addTab("Details", jPanelDataDefinition);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanelData.add(jTabbedPaneData, gridBagConstraints);

        getContentPane().add(jPanelData, java.awt.BorderLayout.CENTER);

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        jPanel8.add(jButtonClose, gridBagConstraints);

        getContentPane().add(jPanel8, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        try {
        ((ChartDatasetPanel)jPanelDataDefinition.getComponent(0)).containerWindowOpened();
        } catch (Exception ex) {}
        
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.notifyChange();
        
    }//GEN-LAST:event_formWindowClosing

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        
        this.notifyChange();
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jTableDatasetParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDatasetParametersMouseClicked

        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1)
        {
            if (jTableDatasetParameters.getSelectedRowCount() > 0)
            {
                jButtonModParameterActionPerformed(null);
            }
        }
        
    }//GEN-LAST:event_jTableDatasetParametersMouseClicked

    private void jButtonRemParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemParameterActionPerformed
        if (this.isInit()) return;
        
        if (currentSelectedChartElement == null) return;
        
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
        
        JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)currentSelectedChartElement.getDataset().getDatasetRun();
        
        while (jTableDatasetParameters.getSelectedRowCount() > 0) {
            int i=jTableDatasetParameters.getSelectedRow();
            datasetRun.removeParameter( ((JRDatasetParameter)jTableDatasetParameters.getValueAt( i, 0)).getName() );
            dtm.removeRow(i);
        }
        notifyChange();
        
    }//GEN-LAST:event_jButtonRemParameterActionPerformed

    @SuppressWarnings("unchecked")
    private void jButtonModParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModParameterActionPerformed
        
        if (this.isInit()) return;
        
        if (currentSelectedChartElement == null) return;
                int rowNumber = jTableDatasetParameters.getSelectedRow();
        JRDesignDatasetParameter parameter = (JRDesignDatasetParameter)jTableDatasetParameters.getValueAt( jTableDatasetParameters.getSelectedRow(), 0);
        
        java.util.HashMap map = new java.util.HashMap();
        java.util.List<JRDatasetParameter> params = Arrays.asList(currentSelectedChartElement.getDataset().getDatasetRun().getParameters());
        for (JRDatasetParameter p : params)
        {
            map.put(p.getName(), p);
        }
        
        Object pWin = SwingUtilities.windowForComponent(this);
        JRDatasetParameterDialog jrpd = null;
        if (pWin instanceof Dialog) jrpd = new JRDatasetParameterDialog((Dialog)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(currentSelectedChartElement.getDataset().getDatasetRun().getDatasetName()) );
        else jrpd = new JRDatasetParameterDialog((Frame)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(currentSelectedChartElement.getDataset().getDatasetRun().getDatasetName()));

        ExpressionContext docEc = new ExpressionContext( getJasperDesign().getMainDesignDataset() );
        jrpd.setExpressionContext(docEc);
        
        jrpd.setParameter( parameter );
        
        /*
        if (subdatasetParameterHighlightExpression != null)
        {
            jrpd.setFocusedExpression( ((Integer)subdatasetParameterHighlightExpression[0]).intValue() );
        }
        */
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            parameter.setName( jrpd.getParameter().getName() );
            parameter.setExpression( jrpd.getParameter().getExpression());
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
            dtm.setValueAt(parameter, rowNumber, 0);
            dtm.setValueAt(parameter.getExpression(), rowNumber, 1);
            jTableDatasetParameters.updateUI();
            notifyChange();
        }
        
    }//GEN-LAST:event_jButtonModParameterActionPerformed

    @SuppressWarnings("unchecked")
    private void jButtonAddParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddParameterActionPerformed
        if (this.isInit()) return;
        
        if (currentSelectedChartElement == null) return;
        // Set the new value for all selected elements...
                
        java.util.HashMap map = new java.util.HashMap();
        java.util.List<JRDatasetParameter> params = Arrays.asList(currentSelectedChartElement.getDataset().getDatasetRun().getParameters());
        for (JRDatasetParameter p : params)
        {
            map.put(p.getName(), p);
        }
        
        Object pWin = SwingUtilities.windowForComponent(this);
        JRDatasetParameterDialog jrpd = null;
        if (pWin instanceof Dialog) jrpd = new JRDatasetParameterDialog((Dialog)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(currentSelectedChartElement.getDataset().getDatasetRun().getDatasetName()));
        else jrpd = new JRDatasetParameterDialog((Frame)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(currentSelectedChartElement.getDataset().getDatasetRun().getDatasetName()));

        ExpressionContext docEc = new ExpressionContext( getJasperDesign().getMainDesignDataset() );
        jrpd.setExpressionContext(docEc);
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            JRDesignDatasetParameter parameter = jrpd.getParameter();
            try {
                ((JRDesignDatasetRun)currentSelectedChartElement.getDataset().getDatasetRun()).addParameter( parameter );
                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                dtm.addRow(new Object[]{parameter, parameter.getExpression()});
                notifyChange();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButtonAddParameterActionPerformed

    private void jComboBoxSubDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSubDatasetActionPerformed

        if (this.isInit()) return; 

        if (currentSelectedChartElement != null)
        {
            // If we selected a goof dataset....
            if (this.jComboBoxSubDataset.getSelectedIndex() > 0)
            {
                    // Check subdataset parameters....
                    JRDesignDataset dds = (JRDesignDataset)getJasperDesign().getDatasetMap().get(""+jComboBoxSubDataset.getSelectedItem());
                    ExpressionContext ec = new ExpressionContext(dds);
                    setExpressionContext( ec );

                    if (currentSelectedChartElement.getDataset().getDatasetRun() == null ||
                        !("" + jComboBoxSubDataset.getSelectedItem()).equals(currentSelectedChartElement.getDataset().getDatasetRun().getDatasetName()) )//NOI18N
                    {

                        JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)currentSelectedChartElement.getDataset().getDatasetRun();
                        if (datasetRun == null)
                        {
                            datasetRun = new JRDesignDatasetRun();
                            ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setDatasetRun(datasetRun);
                            
                            setInit(true);
                            this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                            this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                            this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                            this.jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
                            jRTextExpressionAreaMapExpression.setText("");//NOI18N
                            setInit(false);
                            
                            java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                            gridBagConstraints.gridy = 1;
                            gridBagConstraints.gridwidth = 2;
                            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                            gridBagConstraints.weightx = 1.0;
                            gridBagConstraints.weighty = 1.0;
                            gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
                            jPanel7.add(jTabbedPaneSubDataset, gridBagConstraints);
        
                            //jTabbedPaneSubDataset.setVisible(true);
                            jPanel7.updateUI();
                        }
                        datasetRun.setDatasetName("" + jComboBoxSubDataset.getSelectedItem());//NOI18N
                        // we need to fix the groups...
                        updateGroups();
                    }
            }
            else
            {
                ExpressionContext ec = new ExpressionContext(getJasperDesign().getMainDesignDataset());
                ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setDatasetRun(null);

                setExpressionContext( ec );

                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                dtm.setRowCount(0);
                setInit(true);
                this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                this.jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
                jRTextExpressionAreaMapExpression.setText("");//NOI18N
                setInit(false);
                
                jPanel7.remove(jTabbedPaneSubDataset);
                //jTabbedPaneSubDataset.setVisible(false);
                //jTabbedPaneSubDataset.updateUI();
                jPanel7.updateUI();
            }

            updateGroups();
            List groups = getChartDataset().getGroupsList();

            if (groups.size() == 0)
            {
                IncrementTypeEnum val = (IncrementTypeEnum)((Tag)jComboBoxIncrementType.getSelectedItem()).getValue();
                if (val == IncrementTypeEnum.GROUP)
                {
                    setInit(true);
                    //((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setIncrementType(JRVariable.RESET_TYPE_REPORT);
                    //((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setIncrementGroup(null);
                    Misc.setComboboxSelectedTagValue(jComboBoxIncrementType, IncrementTypeEnum.NONE);
                    setInit(false);
                }
                ResetTypeEnum val2 = (ResetTypeEnum)((Tag)jComboBoxResetType.getSelectedItem()).getValue();
                if (val2 == ResetTypeEnum.GROUP)
                {
                    setInit(true);
                    //((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setResetType(JRVariable.RESET_TYPE_REPORT);
                    //((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setResetGroup(null);
                    Misc.setComboboxSelectedTagValue(jComboBoxResetType, ResetTypeEnum.REPORT);
                    setInit(false);
                }
            }
            jComboBoxIncrementTypeActionPerformed(null);
            jComboBoxResetTypeActionPerformed(null);
            
            currentSelectedChartElement.getEventSupport().firePropertyChange(JRDesignChartDataset.PROPERTY_DATASET_RUN, null, null);
        }
        notifyChange();
    }//GEN-LAST:event_jComboBoxSubDatasetActionPerformed

    private void jComboBoxDatasetConnectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDatasetConnectionTypeActionPerformed
        if (isInit() || currentSelectedChartElement == null) return;
                
        JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)currentSelectedChartElement.getDataset().getDatasetRun();
        if (jComboBoxDatasetConnectionType.getSelectedIndex() == 0) {
            jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
            jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
            datasetRun.setConnectionExpression(null);
            datasetRun.setDataSourceExpression(null);
        }
        else if (jComboBoxDatasetConnectionType.getSelectedIndex() == 1) {
            
            jRTextExpressionAreaTextConnectionExpression.setText("$P{REPORT_CONNECTION}");//NOI18N
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
            
            datasetRun.setDataSourceExpression(null);

            JRDesignExpression exp = new JRDesignExpression();
            exp.setValueClassName("java.sql.Connection");//NOI18N
            exp.setText("$P{REPORT_CONNECTION}");//NOI18N
            datasetRun.setConnectionExpression(exp);

        }
        else if (jComboBoxDatasetConnectionType.getSelectedIndex() == 2) {
            
            jRTextExpressionAreaTextConnectionExpression.setText("new net.sf.jasperreports.engine.JREmptyDataSource(1)");//NOI18N
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
            datasetRun.setConnectionExpression(null);

            JRDesignExpression exp = new JRDesignExpression();
            exp.setValueClassName("net.sf.jasperreports.engine.JRDataSource");//NOI18N
            exp.setText("new net.sf.jasperreports.engine.JREmptyDataSource(1)");//NOI18N
            datasetRun.setDataSourceExpression(exp);
        }
        
        notifyChange();
    }//GEN-LAST:event_jComboBoxDatasetConnectionTypeActionPerformed

    private void jButtonPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPasteActionPerformed
        
        if (currentSelectedChartElement == null) return;
        if (IReportManager.getInstance().getChartDatasetClipBoard() == null) return;
        
        JRDesignChartDataset theDataset = IReportManager.getInstance().getChartDatasetClipBoard();
        try {
            currentSelectedChartElement.setDataset( (JRDesignChartDataset)theDataset.clone() );
            setChartElement(currentSelectedChartElement, getJasperDesign());
        } catch (Exception ex) { }
        
    }//GEN-LAST:event_jButtonPasteActionPerformed

    private void jButtonCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyActionPerformed

        if (currentSelectedChartElement == null) return;
        try {
            IReportManager.getInstance().setChartDatasetClipBoard((JRDesignChartDataset)currentSelectedChartElement.getDataset().clone() );
            jButtonPaste.setEnabled(true);
        } catch (Exception ex) { }
    }//GEN-LAST:event_jButtonCopyActionPerformed

    private void jComboBoxTypeOfDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTypeOfDataActionPerformed

        if (isInit() || currentSelectedChartElement == null) return;
        
        if (jComboBoxTypeOfData.getSelectedItem() != null)
        {
            String typeOfDataset = ((Tag)jComboBoxTypeOfData.getSelectedItem()).getValue()+"";//NOI18N
            if (currentSelectedChartElement.getDataset().getClass().getName().equals(typeOfDataset)) return;
            
            try {
                jPanelDataDefinition.removeAll();
                
                JRChartDataset currentDataset =  currentSelectedChartElement.getDataset();
                
                Constructor constructor = this.getClass().forName( typeOfDataset ).getConstructor(new Class[]{JRChartDataset.class});
                
                JRDesignChartDataset dataset = (JRDesignChartDataset)constructor.newInstance(currentDataset);
                currentSelectedChartElement.setDataset( dataset) ;
                setDatasetPanel(currentSelectedChartElement.getDataset());
            
            } catch (Exception ex)
            {
              ex.printStackTrace();;   
            }
            
            
            this.notifyChange();
        }
        
    }//GEN-LAST:event_jComboBoxTypeOfDataActionPerformed

    
    
    
    
    private void jComboBoxIncrementGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIncrementGroupActionPerformed
        if (isInit() || currentSelectedChartElement == null) return;
        String name = (String)jComboBoxIncrementGroup.getSelectedItem();
        
        if (name != null && name.trim().length() != 0)
        {
            ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setIncrementGroup( (JRGroup)getChartDataset().getGroupsMap().get(name));
        }
        else
        {
            ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setIncrementGroup(null);
        }

        System.out.println("Increment group set to: " + ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).getIncrementGroup());
        System.out.flush();

    }//GEN-LAST:event_jComboBoxIncrementGroupActionPerformed

    private void jComboBoxIncrementTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIncrementTypeActionPerformed
        
        
        if (isInit() || currentSelectedChartElement == null) return;
        
        IncrementTypeEnum val = (IncrementTypeEnum)((Tag)jComboBoxIncrementType.getSelectedItem()).getValue();
        
        if (val == IncrementTypeEnum.GROUP)
        {
            // Currently selected dataset...
            List groups = getChartDataset().getGroupsList();

            if (groups.isEmpty())
            {
                setInit(true);
                Misc.setComboboxSelectedTagValue(jComboBoxIncrementType, currentSelectedChartElement.getDataset().getIncrementTypeValue());
                SwingUtilities.invokeLater(new Runnable(){
                    public void run()
                    {
                        JOptionPane.showMessageDialog(jComboBoxIncrementGroup, I18n.getString("ChartPropertiesDialog.MessageDialog.NoGroupsAvail"));
                    }
                });
                setInit(false);
                return;
            }
            else
            {
                ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setIncrementType(val);
                jComboBoxIncrementGroup.setEnabled(true);
                jComboBoxIncrementGroup.setSelectedIndex(0);
                jComboBoxIncrementGroupActionPerformed(null);
            }
        }
        else
        {
           ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setIncrementType(val);
           ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setIncrementGroup(null);
           jComboBoxIncrementGroup.setEnabled(false);
           jComboBoxIncrementGroup.setSelectedItem(null);
        }
        
    }//GEN-LAST:event_jComboBoxIncrementTypeActionPerformed

    private void jComboBoxResetGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxResetGroupActionPerformed

        if (isInit() || currentSelectedChartElement == null) return;
        String name = (String)jComboBoxResetGroup.getSelectedItem();
        
        if (name != null && name.trim().length() != 0)
        {
            ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setResetGroup( (JRGroup)getChartDataset().getGroupsMap().get(name));
        }
        else
        {
            ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setResetGroup(null);
        }
    }//GEN-LAST:event_jComboBoxResetGroupActionPerformed

    private void jComboBoxResetTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxResetTypeActionPerformed

        
        if (isInit() || currentSelectedChartElement == null) return;
        
        ResetTypeEnum val = (ResetTypeEnum)((Tag)jComboBoxResetType.getSelectedItem()).getValue();
        
        if (val == ResetTypeEnum.GROUP)
        {
            List groups = getChartDataset().getGroupsList();
            
            if (groups.isEmpty())
            {
                setInit(true);
                Misc.setComboboxSelectedTagValue(jComboBoxResetType, currentSelectedChartElement.getDataset().getResetTypeValue());
                SwingUtilities.invokeLater(new Runnable(){
                    public void run()
                    {
                        JOptionPane.showMessageDialog(jComboBoxResetGroup, I18n.getString("ChartPropertiesDialog.MessageDialog.NoGroupsAvail"));
                    }
                });
                setInit(false);
                return;
            }
            else
            {
                ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setResetType(val);
                jComboBoxResetGroup.setEnabled(true);
                jComboBoxResetGroup.setSelectedIndex(0);
            }
        }
        else
        {
           ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setResetType(val);
           ((JRDesignChartDataset)currentSelectedChartElement.getDataset()).setResetGroup(null);
           jComboBoxResetGroup.setEnabled(false);
           jComboBoxResetGroup.setSelectedItem(null);
        }
    }//GEN-LAST:event_jComboBoxResetTypeActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddParameter;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonCopy;
    private javax.swing.JButton jButtonModParameter;
    private javax.swing.JButton jButtonPaste;
    private javax.swing.JButton jButtonRemParameter;
    private javax.swing.JComboBox jComboBoxDatasetConnectionType;
    private javax.swing.JComboBox jComboBoxIncrementGroup;
    private javax.swing.JComboBox jComboBoxIncrementType;
    private javax.swing.JComboBox jComboBoxResetGroup;
    private javax.swing.JComboBox jComboBoxResetType;
    private javax.swing.JComboBox jComboBoxSubDataset;
    private javax.swing.JComboBox jComboBoxTypeOfData;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabelIncrementGroup;
    private javax.swing.JLabel jLabelIncrementType;
    private javax.swing.JLabel jLabelIncrementType1;
    private javax.swing.JLabel jLabelIncrementType2;
    private javax.swing.JLabel jLabelResetGroup;
    private javax.swing.JLabel jLabelResetType;
    private javax.swing.JLabel jLabelTypeOfData;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelData;
    private javax.swing.JPanel jPanelDataDefinition;
    private javax.swing.JPanel jPanelDataset;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaFilterExpression;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaMapExpression;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaTextConnectionExpression;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPaneData;
    private javax.swing.JTabbedPane jTabbedPaneSubDataset;
    private javax.swing.JTable jTableDatasetParameters;
    // End of variables declaration//GEN-END:variables
    
    

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

     /**
     * This method update the comboboxes where is present the goup list.
     */
    public void updateGroups()
    {

        if (getJasperDesign() == null)
	{
            jComboBoxResetGroup.removeAllItems();
        }	
        else
        {
            List<String> groupNames = new ArrayList<String>();

            List groups = getChartDataset().getGroupsList();

            for (int i=0; i<groups.size(); ++i)
            {
                groupNames.add( ((JRGroup)groups.get(i)).getName());
            }
            
            Misc.updateComboBox(jComboBoxResetGroup, groupNames);
            Misc.updateComboBox(jComboBoxIncrementGroup, groupNames);
        }
        
    }
    
    
    public void updateSubDatasets()
    {

        if (getJasperDesign() == null)
	{
            jComboBoxSubDataset.removeAllItems();
            jComboBoxSubDataset.addItem("");//NOI18N
        }	
        else
        {
            List<String> datasetNames = new ArrayList<String>();
            for (int i=0; i<getJasperDesign().getDatasetsList().size(); ++i)
            {
                datasetNames.add( ((JRDataset)getJasperDesign().getDatasetsList().get(i)).getName());
            }
            
            Misc.updateComboBox(jComboBoxSubDataset, datasetNames, true);
        }
    }
    
    /*
    public void applyI18n()
        {
                // Start autogenerated code ----------------------
                jButtonAdd.setText(I18n.getString("chartPropertiesDialog.buttonAdd","Add"));
                jButtonAddParameter.setText(I18n.getString("chartPropertiesDialog.buttonAddParameter","Add"));
                jButtonClose.setText(I18n.getString("chartPropertiesDialog.buttonClose","Close"));
                jButtonDelete.setText(I18n.getString("chartPropertiesDialog.buttonDelete","Delete"));
                jButtonModParameter.setText(I18n.getString("chartPropertiesDialog.buttonModParameter","Modify"));
                jButtonModify.setText(I18n.getString("chartPropertiesDialog.buttonModify","Edit chart"));
                jButtonMoveDown.setText(I18n.getString("chartPropertiesDialog.buttonMoveDown","Move down"));
                jButtonMoveUp.setText(I18n.getString("chartPropertiesDialog.buttonMoveUp","Move up"));
                jButtonRemParameter.setText(I18n.getString("chartPropertiesDialog.buttonRemParameter","Remove"));
                jLabel26.setText(I18n.getString("chartPropertiesDialog.label26","Parameters Map Expression"));
                jLabel41.setText(I18n.getString("chartPropertiesDialog.label41","Connection / Datasource Expression"));
                jLabelIncrementType1.setText(I18n.getString("chartPropertiesDialog.labelIncrementType1","Sub dataset"));
                jLabelIncrementType2.setText(I18n.getString("chartPropertiesDialog.labelIncrementType2","Increment When expression"));
                // End autogenerated code ----------------------
            jTabbedPane1.setTitleAt(0, it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.TabChartProperties","Chart properties"));
            jTabbedPane1.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.TabChartData","Chart data"));
            jLabelTypeOfData.setText( it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.LabelTypeOfDataset","Type of dataset"));
            jTabbedPaneData.setTitleAt(0, it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.TabDataset","Dataset"));
            jTabbedPaneData.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.TabDatasetDetails","Details"));
            
            jLabelResetType.setText( it.businesslogic.ireport.util.I18n.getString("resetType","Reset type"));
            jLabelResetGroup.setText( it.businesslogic.ireport.util.I18n.getString("resetGroup","Reset group"));
            jLabelIncrementType.setText( it.businesslogic.ireport.util.I18n.getString("incrementType","Increment type"));
            jLabelIncrementGroup.setText( it.businesslogic.ireport.util.I18n.getString("incrementGroup","Increment group"));
            
            jButtonCopy.setText( it.businesslogic.ireport.util.I18n.getString("charts.copyDataset","Copy dataset"));
            jButtonPaste.setText( it.businesslogic.ireport.util.I18n.getString("charts.pasteDataset","Paste dataset"));
            
            this.setTitle(it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.title","Chart properties"));
            
            jTableDatasetParameters.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("chartPropertiesDialog.tablecolumn.parameter","Parameter") );
            jTableDatasetParameters.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("chartPropertiesDialog.tablecolumn.expression","Expression") );
            
            jTable1.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("chartPropertiesDialog.tablecolumn.chart","Chart") );
            jTable1.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("chartPropertiesDialog.tablecolumn.axisPosition","Axis position") );
                   
            jTabbedPaneSubDataset.setTitleAt(0, it.businesslogic.ireport.util.I18n.getString("datasetRun.tab.Parameters","Parameters"));
            jTabbedPaneSubDataset.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("datasetRun.tab.ParametersMapExp","Parameters map exp."));
            jTabbedPaneSubDataset.setTitleAt(2, it.businesslogic.ireport.util.I18n.getString("datasetRun.tab.ConnectionDatasourceExp","Connection/Datasource exp."));
            
            ((javax.swing.border.TitledBorder)jPanel1.getBorder()).setTitle( I18n.getString("datasetRun.panelBorder.DatasetRun","Dataset run") );
            
            this.getRootPane().updateUI();
        }
    */
    
    private void setDatasetPanel( JRChartDataset dataset )
    {
        setDatasetPanel( dataset, getChartDataset());
        
    }
    
    private void setDatasetPanel( JRChartDataset dataset, JRDesignDataset ds)
    {
        jPanelDataDefinition.removeAll();
        
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
          
        if (dataset instanceof JRDesignTimePeriodDataset)
        {
             TimePeriodDatasetPanel pdp = new TimePeriodDatasetPanel();
             pdp.setTimePeriodDataset((JRDesignTimePeriodDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
             
        }
        else if (dataset instanceof JRDesignCategoryDataset)
        {
             CategoryDatasetPanel pdp = new CategoryDatasetPanel();
             pdp.setCategoryDataset((JRDesignCategoryDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof JRDesignPieDataset)
        {
             PieDatasetPanel pdp = new PieDatasetPanel();
             pdp.setPieDataset((JRDesignPieDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof JRDesignTimeSeriesDataset)
        {
             TimeSeriesDatasetPanel pdp = new TimeSeriesDatasetPanel();
             pdp.setTimeSeriesDataset((JRDesignTimeSeriesDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof JRDesignXyDataset)
        {
             XYDatasetPanel pdp = new XYDatasetPanel();
             pdp.setXYDataset((JRDesignXyDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        
        else if (dataset instanceof JRDesignXyzDataset)
        {
             XYZDatasetPanel pdp = new XYZDatasetPanel();
             pdp.setXYZDataset((JRDesignXyzDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof JRDesignHighLowDataset)
        {
            HighLowDatasetPanel pdp = new HighLowDatasetPanel();
             pdp.setHighLowDataset((JRDesignHighLowDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof JRDesignValueDataset)
        {
            ValueDatasetPanel pdp = new ValueDatasetPanel();
             pdp.setValueDataset((JRDesignValueDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof JRDesignGanttDataset)
        {
             GanttDatasetPanel pdp = new GanttDatasetPanel();
             pdp.setGanttDataset((JRDesignGanttDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        
        Misc.setComboboxSelectedTagValue(jComboBoxTypeOfData, dataset.getClass().getName());
        
        if (ds != null)
        {
            setExpressionContext( new ExpressionContext(ds) );
        }
        
        jPanelDataDefinition.updateUI();
    }
    
    
    /** 
     * this method notifies a chart dataset panel the subdataset used to fill it 
     */
    public void setExpressionContext(ExpressionContext ec)
    {
        expressionContext = ec;
        if (jPanelDataDefinition.getComponentCount() > 0)
        {
            Component c = this.jPanelDataDefinition.getComponent(0);
            ((ChartDatasetPanel)c).setExpressionContext( ec );
        }
        jRTextExpressionAreaFilterExpression.setExpressionContext(ec);
    }
    
    public boolean isInit() {
        return init || (currentSelectedChartElement == null);
    }

    public void setInit(boolean init) {
        this.init = init;
    }
    
   public void notifyChange()
   {
        IReportManager.getInstance().notifyReportChange();
   }

   /* 
    public static final int COMPONENT_NONE=0;
    public static final int COMPONENT_DATASET_SPECIFIC_EXPRESSION=1;
    public static final int COMPONENT_METER_INTERVALS=2;
    public static final int COMPONENT_INCREMENT_WHEN_EXPRESSION=70;
    public static final int COMPONENT_DATASETRUN_PARAMETERS=71;
    public static final int COMPONENT_DATASETRUN_MAP_EXPRESSION=72;
    public static final int COMPONENT_DATASETRUN_DS_CONN_EXPRESSION=73;
    
    private Object[] meterIntervalsHilightExpression = null; 
    private Object[] subdatasetParameterHighlightExpression = null; 
    
    **
     * This method set the focus on a specific component.
     * 
     * expressionInfo[0] can be something like:
     * COMPONENT_DATASET_SPECIFIC_EXPRESSION, ...
     *
     * If it is COMPONENT_DATASET_SPECIFIC_EXPRESSION, other parameters are expected in the array...
     
    public void setFocusedExpression(Object[] expressionInfo)
    {
        int expID = ((Integer)expressionInfo[0]).intValue();
        
        switch (expID)
        {
            case COMPONENT_INCREMENT_WHEN_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelData );
                jTabbedPaneData.setSelectedComponent( jPanelDataset );
                Misc.selectTextAndFocusArea(jRTextExpressionAreaFilterExpression);
                break;
            case COMPONENT_DATASETRUN_PARAMETERS:
                jTabbedPane1.setSelectedComponent( jPanelData );
                jTabbedPaneData.setSelectedComponent( jPanelDataset );
                jTabbedPaneSubDataset.setSelectedComponent(jPanel4);
                
                int index = ((Integer)expressionInfo[1]).intValue();
                
                if (index >=0 && jTableDatasetParameters.getRowCount() > index )
                {
                    jTableDatasetParameters.setRowSelectionInterval(index,index);
                    subdatasetParameterHighlightExpression = new Object[expressionInfo.length-2];
                    for (int i=2; i< expressionInfo.length; ++i) subdatasetParameterHighlightExpression[i-2] = expressionInfo[i];
                    break;
                }
                
                break;
            case COMPONENT_DATASETRUN_MAP_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelData );
                jTabbedPaneData.setSelectedComponent( jPanelDataset );
                jTabbedPaneSubDataset.setSelectedComponent(jPanel5);
                Misc.selectTextAndFocusArea(jRTextExpressionAreaMapExpression);
                break;
            case COMPONENT_DATASETRUN_DS_CONN_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelData );
                jTabbedPaneData.setSelectedComponent( jPanelDataset );
                jTabbedPaneSubDataset.setSelectedComponent(jPanel6);
                Misc.selectTextAndFocusArea(jRTextExpressionAreaTextConnectionExpression);
                break;
            case COMPONENT_DATASET_SPECIFIC_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelData );
                jTabbedPaneData.setSelectedComponent( jPanelDataDefinition );
                if (jPanelDataDefinition.getComponentCount() > 0 )
                {
                    Object newInfo[] = new Object[expressionInfo.length -1 ];
                    for (int i=1; i< expressionInfo.length; ++i) newInfo[i-1] = expressionInfo[i];
                    
                    ((ChartDatasetPanel)jPanelDataDefinition.getComponent(0)).setFocusedExpression( newInfo );
                }
                break;
            case COMPONENT_METER_INTERVALS:
                this.setPropertyLabelError("meterIntervalsMeterPlot",(String)expressionInfo[1]);
                meterIntervalsHilightExpression = new Object[expressionInfo.length-2];
                for (int i=2; i< expressionInfo.length; ++i) meterIntervalsHilightExpression[i-2] = expressionInfo[i];
                break;
        }
    }
    */

   public JRDesignDataset getChartDataset()
   {
       if (currentSelectedChartElement != null &&
           currentSelectedChartElement.getDataset() != null &&
           currentSelectedChartElement.getDataset().getDatasetRun() != null &&
           currentSelectedChartElement.getDataset().getDatasetRun().getDatasetName() != null)
       {
           String dn = currentSelectedChartElement.getDataset().getDatasetRun().getDatasetName();
           JRDesignDataset ds = (JRDesignDataset) getJasperDesign().getDatasetMap().get(dn);
           if (ds != null) return ds;
       }
       return getJasperDesign().getMainDesignDataset();
   }
}
