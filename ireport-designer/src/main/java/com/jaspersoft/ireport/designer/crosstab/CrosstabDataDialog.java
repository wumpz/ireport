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
package com.jaspersoft.ireport.designer.crosstab;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.tools.DatasetParametersTableCellRenderer;
import com.jaspersoft.ireport.designer.tools.JRDatasetParameterDialog;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableColumnModel;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;


/**
 *
 * @author  Administrator
 */
public class CrosstabDataDialog extends javax.swing.JDialog {
    
    private JRDesignCrosstab currentSelectedCrosstabElement = null;
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
    public void setCrosstabElement(JRDesignCrosstab crosstabReportElement, JasperDesign jd)
    {
        setInit(true);
        this.jasperDesign = jd;
        
        try {
            
            currentSelectedCrosstabElement = crosstabReportElement;

            if (currentSelectedCrosstabElement != null)
            {
                updateGroups();
                updateSubDatasets();
              
                jCheckBoxPreSorted.setSelected( currentSelectedCrosstabElement.getDataset().isDataPreSorted() );
                // Set general dataset data...
                
                Misc.setComboboxSelectedTagValue(jComboBoxIncrementType, currentSelectedCrosstabElement.getDataset().getIncrementTypeValue() );
                jComboBoxIncrementGroup.setEnabled(currentSelectedCrosstabElement.getDataset().getIncrementTypeValue() == IncrementTypeEnum.GROUP);
                if (currentSelectedCrosstabElement.getDataset().getIncrementTypeValue() == IncrementTypeEnum.GROUP)
                {
                    jComboBoxIncrementGroup.setSelectedItem( currentSelectedCrosstabElement.getDataset().getIncrementGroup().getName() );
                }
                
                Misc.setComboboxSelectedTagValue(jComboBoxResetType, currentSelectedCrosstabElement.getDataset().getResetTypeValue() );
                jComboBoxResetGroup.setEnabled(currentSelectedCrosstabElement.getDataset().getResetTypeValue() == ResetTypeEnum.GROUP);
                if (currentSelectedCrosstabElement.getDataset().getResetTypeValue() == ResetTypeEnum.GROUP)
                {
                    jComboBoxResetGroup.setSelectedItem( currentSelectedCrosstabElement.getDataset().getResetGroup().getName() );
                }
                
                jRTextExpressionAreaFilterExpression.setText( Misc.getExpressionText( currentSelectedCrosstabElement.getDataset().getIncrementWhenExpression() ) );

                JRDesignDataset dataset = (JRDesignDataset)getJasperDesign().getMainDataset();
                        
                if (currentSelectedCrosstabElement.getDataset().getDatasetRun() != null)
                {
                    JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)currentSelectedCrosstabElement.getDataset().getDatasetRun();
                    
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
                        this.jRTextExpressionAreaTextConnectionExpression.setText("");
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
                    this.jRTextExpressionAreaTextConnectionExpression.setText("");
                    jRTextExpressionAreaMapExpression.setText("");

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

                
                setExpressionContext( new ExpressionContext( ModelUtils.getDatasetFromCrosstabDataset((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset(), jd) ) );
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
         
    public CrosstabDataDialog(Dialog parent, boolean modal) 
    {
         super(parent,modal);
         initAll();
    }

    /** Creates new form ReportQueryFrame */
    public CrosstabDataDialog(Frame parent, boolean modal) 
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
        
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.NONE,I18n.getString("CrosstabDataDialog.ComboBox.None")));
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.REPORT,I18n.getString("CrosstabDataDialog.ComboBox.Report")));
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.PAGE,I18n.getString("CrosstabDataDialog.ComboBox.Page")));
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.COLUMN,I18n.getString("CrosstabDataDialog.ComboBox.Column")));
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.GROUP,I18n.getString("CrosstabDataDialog.ComboBox.Group")));
        
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.NONE,I18n.getString("CrosstabDataDialog.ComboBox.None")));
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.REPORT,I18n.getString("CrosstabDataDialog.ComboBox.Report")));
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.PAGE,I18n.getString("CrosstabDataDialog.ComboBox.Page")));
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.COLUMN,I18n.getString("CrosstabDataDialog.ComboBox.Column")));
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.GROUP,I18n.getString("CrosstabDataDialog.ComboBox.Group")));
         
        jComboBoxDatasetConnectionType.addItem(new Tag(I18n.getString("CrosstabDataDialog.ComboBox.Warning1"),I18n.getString("CrosstabDataDialog.ComboBox.Warning1")));
        jComboBoxDatasetConnectionType.addItem(new Tag(I18n.getString("CrosstabDataDialog.ComboBox.Warning2"),I18n.getString("CrosstabDataDialog.ComboBox.Warning2")));
        jComboBoxDatasetConnectionType.addItem(new Tag(I18n.getString("CrosstabDataDialog.ComboBox.Warning3"),I18n.getString("CrosstabDataDialog.ComboBox.Warning3")));
        
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
        if (currentSelectedCrosstabElement != null)
        {
            JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)currentSelectedCrosstabElement.getDataset().getDatasetRun();
            if (datasetRun != null)
            {
                JRDesignExpression exp = null;
                if (jRTextExpressionAreaMapExpression.getText().trim().length() > 0)
                {
                    exp = new JRDesignExpression();
                    exp.setValueClassName("java.util.Map");
                    exp.setText(jRTextExpressionAreaMapExpression.getText());
                }
                
                datasetRun.setParametersMapExpression(exp);
                notifyChange();
            }
        }
    }
    
    public void jRTextExpressionAreaFilterExpressionTextChanged() {
        if (this.isInit()) return; 
        if (currentSelectedCrosstabElement != null)
        {
            JRDesignExpression exp = null;
            if (jRTextExpressionAreaFilterExpression.getText().trim().length() > 0)
            {
                exp = new JRDesignExpression();
                exp.setValueClassName("java.lang.Boolean");
                exp.setText(jRTextExpressionAreaFilterExpression.getText());
            }

            ((JRDesignCrosstabDataset) currentSelectedCrosstabElement.getDataset()).setIncrementWhenExpression(exp);
            notifyChange();
        }
    }

    public void jRTextExpressionAreaTextConnectionExpressionTextChanged() {
        if (this.isInit()) return; 
        if (currentSelectedCrosstabElement != null)
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
                if (exp != null) exp.setValueClassName("java.sql.Connection");
                ((JRDesignDatasetRun) currentSelectedCrosstabElement.getDataset().getDatasetRun()).setConnectionExpression(exp);
            }
            else if (index == 2)
            {
                if (exp != null) exp.setValueClassName("net.sf.jasperreports.engine.JRDataSource");
                ((JRDesignDatasetRun) currentSelectedCrosstabElement.getDataset().getDatasetRun()).setDataSourceExpression(exp);
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
        jPanelDataset = new javax.swing.JPanel();
        jCheckBoxPreSorted = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabelResetType = new javax.swing.JLabel();
        jComboBoxResetType = new javax.swing.JComboBox();
        jLabelResetGroup = new javax.swing.JLabel();
        jComboBoxResetGroup = new javax.swing.JComboBox();
        jLabelIncrementType = new javax.swing.JLabel();
        jComboBoxIncrementType = new javax.swing.JComboBox();
        jLabelIncrementGroup = new javax.swing.JLabel();
        jComboBoxIncrementGroup = new javax.swing.JComboBox();
        jLabelIncrementType2 = new javax.swing.JLabel();
        jRTextExpressionAreaFilterExpression = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
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
        jPanel8 = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();

        setTitle("Crosstab data");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanelData.setLayout(new java.awt.GridBagLayout());

        jPanelDataset.setLayout(new java.awt.GridBagLayout());

        jCheckBoxPreSorted.setText("Data is pre-sorted");
        jCheckBoxPreSorted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPreSortedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelDataset.add(jCheckBoxPreSorted, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Dataset"));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabelResetType.setText("Reset type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel2.add(jLabelResetType, gridBagConstraints);

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
        jPanel2.add(jComboBoxResetType, gridBagConstraints);

        jLabelResetGroup.setText("Reset group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel2.add(jLabelResetGroup, gridBagConstraints);

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
        jPanel2.add(jComboBoxResetGroup, gridBagConstraints);

        jLabelIncrementType.setText("Increment type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel2.add(jLabelIncrementType, gridBagConstraints);

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
        jPanel2.add(jComboBoxIncrementType, gridBagConstraints);

        jLabelIncrementGroup.setText("Increment group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel2.add(jLabelIncrementGroup, gridBagConstraints);

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
        jPanel2.add(jComboBoxIncrementGroup, gridBagConstraints);

        jLabelIncrementType2.setText("Filter expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel2.add(jLabelIncrementType2, gridBagConstraints);

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
        jPanel2.add(jRTextExpressionAreaFilterExpression, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelDataset.add(jPanel2, gridBagConstraints);

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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelDataset.add(jPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelData.add(jPanelDataset, gridBagConstraints);

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
        
        if (currentSelectedCrosstabElement == null) return;
        
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
        
        JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)currentSelectedCrosstabElement.getDataset().getDatasetRun();
        
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
        
        if (currentSelectedCrosstabElement == null) return;
                int rowNumber = jTableDatasetParameters.getSelectedRow();
        JRDesignDatasetParameter parameter = (JRDesignDatasetParameter)jTableDatasetParameters.getValueAt( jTableDatasetParameters.getSelectedRow(), 0);
        
        java.util.HashMap map = new java.util.HashMap();
        java.util.List<JRDatasetParameter> params = Arrays.asList(currentSelectedCrosstabElement.getDataset().getDatasetRun().getParameters());
        for (JRDatasetParameter p : params)
        {
            map.put(p.getName(), p);
        }
        
        Object pWin = SwingUtilities.windowForComponent(this);
        JRDatasetParameterDialog jrpd = null;
        if (pWin instanceof Dialog) jrpd = new JRDatasetParameterDialog((Dialog)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(currentSelectedCrosstabElement.getDataset().getDatasetRun().getDatasetName()));
        else jrpd = new JRDatasetParameterDialog((Frame)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(currentSelectedCrosstabElement.getDataset().getDatasetRun().getDatasetName()));

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
        
        if (currentSelectedCrosstabElement == null) return;
        // Set the new value for all selected elements...
                
        java.util.HashMap map = new java.util.HashMap();
        java.util.List<JRDatasetParameter> params = Arrays.asList(currentSelectedCrosstabElement.getDataset().getDatasetRun().getParameters());
        for (JRDatasetParameter p : params)
        {
            map.put(p.getName(), p);
        }
        
        Object pWin = SwingUtilities.windowForComponent(this);
        JRDatasetParameterDialog jrpd = null;
        if (pWin instanceof Dialog) jrpd = new JRDatasetParameterDialog((Dialog)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(currentSelectedCrosstabElement.getDataset().getDatasetRun().getDatasetName()));
        else jrpd = new JRDatasetParameterDialog((Frame)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(currentSelectedCrosstabElement.getDataset().getDatasetRun().getDatasetName()));

        ExpressionContext docEc = new ExpressionContext( getJasperDesign().getMainDesignDataset() );
        jrpd.setExpressionContext(docEc);
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            JRDesignDatasetParameter parameter = jrpd.getParameter();
            try {
                ((JRDesignDatasetRun)currentSelectedCrosstabElement.getDataset().getDatasetRun()).addParameter( parameter );
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

        if (currentSelectedCrosstabElement != null)
        {
            // If we selected a goof dataset....
            if (this.jComboBoxSubDataset.getSelectedIndex() > 0)
            {
                    if (currentSelectedCrosstabElement.getDataset().getDatasetRun() == null ||
                        !("" + jComboBoxSubDataset.getSelectedItem()).equals(currentSelectedCrosstabElement.getDataset().getDatasetRun().getDatasetName()) )
                    {

                        JRDesignDataset dds = (JRDesignDataset)getJasperDesign().getDatasetMap().get(""+jComboBoxSubDataset.getSelectedItem());
                        ExpressionContext ec = new ExpressionContext(dds);
                        setExpressionContext( ec );
                    
                        JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)currentSelectedCrosstabElement.getDataset().getDatasetRun();
                        if (datasetRun == null)
                        {
                            datasetRun = new JRDesignDatasetRun();
                            ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setDatasetRun(datasetRun);
                            
                            setInit(true);
                            this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                            this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                            this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                            this.jRTextExpressionAreaTextConnectionExpression.setText("");
                            jRTextExpressionAreaMapExpression.setText("");
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
                        datasetRun.setDatasetName("" + jComboBoxSubDataset.getSelectedItem());
                        
                        // Check subdataset parameters.... (TODO)
                        updateGroups();
                    }
            }
            else
            {
                ExpressionContext ec = new ExpressionContext(getJasperDesign().getMainDesignDataset());
                ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setDatasetRun(null);

                setExpressionContext( ec );

                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                dtm.setRowCount(0);
                setInit(true);
                this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                this.jRTextExpressionAreaTextConnectionExpression.setText("");
                jRTextExpressionAreaMapExpression.setText("");
                setInit(false);
                
                jPanel7.remove(jTabbedPaneSubDataset);
                //jTabbedPaneSubDataset.setVisible(false);
                //jTabbedPaneSubDataset.updateUI();
                jPanel7.updateUI();
            }

            updateGroups();
            List groups = getCrosstabDataset().getGroupsList();

            

            if (groups.isEmpty())
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

        }
        notifyChange();
    }//GEN-LAST:event_jComboBoxSubDatasetActionPerformed

    private void jComboBoxDatasetConnectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDatasetConnectionTypeActionPerformed
        if (isInit() || currentSelectedCrosstabElement == null) return;
                
        JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)currentSelectedCrosstabElement.getDataset().getDatasetRun();
        if (jComboBoxDatasetConnectionType.getSelectedIndex() == 0) {
            jRTextExpressionAreaTextConnectionExpression.setText("");
            jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
            datasetRun.setConnectionExpression(null);
            datasetRun.setDataSourceExpression(null);
        }
        else if (jComboBoxDatasetConnectionType.getSelectedIndex() == 1) {
            
            jRTextExpressionAreaTextConnectionExpression.setText("$P{REPORT_CONNECTION}");
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
            
            datasetRun.setDataSourceExpression(null);

            JRDesignExpression exp = new JRDesignExpression();
            exp.setValueClassName("java.sql.Connection");
            exp.setText("$P{REPORT_CONNECTION}");
            datasetRun.setConnectionExpression(exp);

        }
        else if (jComboBoxDatasetConnectionType.getSelectedIndex() == 2) {
            
            jRTextExpressionAreaTextConnectionExpression.setText("new net.sf.jasperreports.engine.JREmptyDataSource(1)");
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
            
            datasetRun.setConnectionExpression(null);

            JRDesignExpression exp = new JRDesignExpression();
            exp.setValueClassName("net.sf.jasperreports.engine.JRDataSource");
            exp.setText("new net.sf.jasperreports.engine.JREmptyDataSource(1)");
            datasetRun.setDataSourceExpression(exp);
        }
        
        notifyChange();
    }//GEN-LAST:event_jComboBoxDatasetConnectionTypeActionPerformed

    
    
    
    
    private void jComboBoxIncrementGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIncrementGroupActionPerformed
        if (isInit() || currentSelectedCrosstabElement == null) return;
        String name = (String)jComboBoxIncrementGroup.getSelectedItem();
        
        if (name != null && name.trim().length() != 0)
        {
            ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setIncrementGroup( (JRGroup)getCrosstabDataset().getGroupsMap().get(name));
        }
        else
        {
            ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setIncrementGroup(null);
        }
    }//GEN-LAST:event_jComboBoxIncrementGroupActionPerformed

    private void jComboBoxIncrementTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIncrementTypeActionPerformed
        
        
        if (isInit() || currentSelectedCrosstabElement == null) return;
        
        IncrementTypeEnum val = (IncrementTypeEnum)((Tag)jComboBoxIncrementType.getSelectedItem()).getValue();
        
        if (val == IncrementTypeEnum.GROUP)
        {
            if (getCrosstabDataset().getGroupsList().size() == 0)
            {
                setInit(true);
                Misc.setComboboxSelectedTagValue(jComboBoxIncrementType, currentSelectedCrosstabElement.getDataset().getIncrementTypeValue());
                SwingUtilities.invokeLater(new Runnable(){
                    public void run()
                    {
                        JOptionPane.showMessageDialog(jComboBoxIncrementGroup, I18n.getString("CrosstabDataDialog.Message.NoGroupsAvailable"));
                    }
                });
                setInit(false);
                return;
            }
            else
            {
                ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setIncrementType(val);
                jComboBoxIncrementGroup.setEnabled(true);
                jComboBoxIncrementGroup.setSelectedIndex(0);
            }
        }
        else
        {
           ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setIncrementType(val);
           ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setIncrementGroup(null);
           jComboBoxIncrementGroup.setEnabled(false);
           jComboBoxIncrementGroup.setSelectedItem(null);
        }
        
    }//GEN-LAST:event_jComboBoxIncrementTypeActionPerformed

    private void jComboBoxResetGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxResetGroupActionPerformed

        if (isInit() || currentSelectedCrosstabElement == null) return;
        String name = (String)jComboBoxResetGroup.getSelectedItem();
        
        if (name != null && name.trim().length() != 0)
        {
            ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setResetGroup( (JRGroup)getCrosstabDataset().getGroupsMap().get(name));
        }
        else
        {
            ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setResetGroup(null);
        }
    }//GEN-LAST:event_jComboBoxResetGroupActionPerformed

    private void jComboBoxResetTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxResetTypeActionPerformed

        
        if (isInit() || currentSelectedCrosstabElement == null) return;
        
        ResetTypeEnum val = (ResetTypeEnum)((Tag)jComboBoxResetType.getSelectedItem()).getValue();
        
        if (val == ResetTypeEnum.GROUP)
        {
            if (getCrosstabDataset().getGroupsList().size() == 0)
            {
                setInit(true);
                Misc.setComboboxSelectedTagValue(jComboBoxResetType, currentSelectedCrosstabElement.getDataset().getResetTypeValue());
                SwingUtilities.invokeLater(new Runnable(){
                    public void run()
                    {
                        JOptionPane.showMessageDialog(jComboBoxResetGroup, I18n.getString("CrosstabDataDialog.Message.NoGroupsAvailable"));
                    }
                });
                setInit(false);
                return;
            }
            else
            {
                ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setResetType(val);
                jComboBoxResetGroup.setEnabled(true);
                jComboBoxResetGroup.setSelectedIndex(0);
            }
        }
        else
        {
           ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setResetType(val);
           ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setResetGroup(null);
           jComboBoxResetGroup.setEnabled(false);
           jComboBoxResetGroup.setSelectedItem(null);
        }
    }//GEN-LAST:event_jComboBoxResetTypeActionPerformed

    private void jCheckBoxPreSortedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPreSortedActionPerformed
        if (isInit() || currentSelectedCrosstabElement == null) return;
        ((JRDesignCrosstabDataset)currentSelectedCrosstabElement.getDataset()).setDataPreSorted( jCheckBoxPreSorted.isSelected() );
}//GEN-LAST:event_jCheckBoxPreSortedActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddParameter;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonModParameter;
    private javax.swing.JButton jButtonRemParameter;
    private javax.swing.JCheckBox jCheckBoxPreSorted;
    private javax.swing.JComboBox jComboBoxDatasetConnectionType;
    private javax.swing.JComboBox jComboBoxIncrementGroup;
    private javax.swing.JComboBox jComboBoxIncrementType;
    private javax.swing.JComboBox jComboBoxResetGroup;
    private javax.swing.JComboBox jComboBoxResetType;
    private javax.swing.JComboBox jComboBoxSubDataset;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabelIncrementGroup;
    private javax.swing.JLabel jLabelIncrementType;
    private javax.swing.JLabel jLabelIncrementType1;
    private javax.swing.JLabel jLabelIncrementType2;
    private javax.swing.JLabel jLabelResetGroup;
    private javax.swing.JLabel jLabelResetType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelData;
    private javax.swing.JPanel jPanelDataset;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaFilterExpression;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaMapExpression;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaTextConnectionExpression;
    private javax.swing.JScrollPane jScrollPane2;
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

            List groups = getCrosstabDataset().getGroupsList();

            
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
            jComboBoxSubDataset.addItem("");
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
    
    /** 
     * this method notifies a chart dataset panel the subdataset used to fill it 
     */
    public void setExpressionContext(ExpressionContext ec)
    {
        expressionContext = ec;
        jRTextExpressionAreaFilterExpression.setExpressionContext(ec);
    }
    
    public boolean isInit() {
        return init || (currentSelectedCrosstabElement == null);
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

   public JRDesignDataset getCrosstabDataset()
   {
       if (currentSelectedCrosstabElement != null &&
           currentSelectedCrosstabElement.getDataset() != null &&
           currentSelectedCrosstabElement.getDataset().getDatasetRun() != null &&
           currentSelectedCrosstabElement.getDataset().getDatasetRun().getDatasetName() != null)
       {
           String dn = currentSelectedCrosstabElement.getDataset().getDatasetRun().getDatasetName();
           JRDesignDataset ds = (JRDesignDataset) getJasperDesign().getDatasetMap().get(dn);
           if (ds != null) return ds;
       }
       return getJasperDesign().getMainDesignDataset();
   }
}
