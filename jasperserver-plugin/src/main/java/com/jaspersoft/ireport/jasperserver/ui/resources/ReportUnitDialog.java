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
package com.jaspersoft.ireport.jasperserver.ui.resources;

import com.jaspersoft.ireport.JrxmlDataObject;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.ConfigurablePlainDocument;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.ireport.jasperserver.ui.*;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.io.File;
import java.util.List;
import java.awt.GridBagConstraints;
import javax.swing.JOptionPane;

/**
 *
 * @author  gtoffoli
 */
public class ReportUnitDialog extends javax.swing.JDialog {
    
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    
    private JServer server = null;
    private String parentFolder = null;
    
    private ResourceDescriptor newResourceDescriptor = null;
    private ResourceDescriptor dataSourceDescriptor = null;

    private ResourceDescriptor currentDataSourceDescriptor = null; // used to remove the datasource just in case...
    private RepositoryReportUnit resource = null;
    
    private boolean controlsSupportActive = false;
    private boolean resourcesSupportActive = false;
    
    
    
    /**
     * Creates new form ReportUnitDialog
     */
    public ReportUnitDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        jComboBoxControlsLayout.addItem(new Tag( ""+ResourceDescriptor.RU_CONTROLS_LAYOUT_POPUP_SCREEN,
                                                  JasperServerManager.getString("reportUnitDialog.controlLayout.popupScreen", "Popup screen")));
        jComboBoxControlsLayout.addItem(new Tag( ""+ResourceDescriptor.RU_CONTROLS_LAYOUT_SEPARATE_PAGE,
                                                  JasperServerManager.getString("reportUnitDialog.controlLayout.separatePage", "Separate page")));
        jComboBoxControlsLayout.addItem(new Tag( ""+ResourceDescriptor.RU_CONTROLS_LAYOUT_TOP_OF_PAGE,
                                                  JasperServerManager.getString("reportUnitDialog.controlLayout.topOfPage", "Top of page")));
        jComboBoxControlsLayout.addItem(new Tag( ""+4, // there is no constant for this one.
                                                  JasperServerManager.getString("reportUnitDialog.controlLayout.inPage", "In page")));

        
        this.jTextFieldName.setDocument(new ConfigurablePlainDocument(JasperServerManager.MAX_ID_LENGHT));
        this.jTextFieldLabel.setDocument(new ConfigurablePlainDocument(JasperServerManager.MAX_NAME_LENGHT));
        this.jEditorPaneDescription.setDocument(new ConfigurablePlainDocument(250));
        
        setLocationRelativeTo(null);
        this.jTextFieldLabel.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
        });
        
        this.jTextFieldFile.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
        });
        
        this.jTextFieldName.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
        });

        jTextFieldName.requestFocusInWindow();
        jRadioButtonRepo.setSelected(true);
        jRadioButtonLocalDataSource.setSelected(false);
        
        
        
        this.setControlsSupportActive(false);
        this.setResourcesSupportActive(false);
        
        updateResourceFromType();
        
        
        if (!JasperServerManager.getMainInstance().getBrandingProperties().getProperty("ireport.manage.datasources.enabled", "true").equals("true"))
        {
            jPanel3.removeAll();
            GridBagConstraints  gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
            QualityLabel jlabel = new QualityLabel();
            jlabel.setText("<html><b>" +  JasperServerManager.getString("reportUnitDialog.useDefaultDatasource","This server uses a default data source to run the reports and execute queries") +  "</b></html>");
            jPanel3.add(jlabel, gridBagConstraints);
        }
        
        JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
        jButtonGetCurrentReport.setEnabled(view != null); //MainFrame.getMainInstance().getActiveReportFrame() != null);
        applyI18n();
        
    }
    
    public void applyI18n()
    {
        jButtonClose.setText( JasperServerManager.getString("reportUnitDialog.buttonCancel","Cancel"));
        jButtonSave.setText( JasperServerManager.getString("reportUnitDialog.buttonSave","Save"));
        jLabel1.setText( JasperServerManager.getString("reportUnitDialog.title","Report unit"));
        jLabelDescription.setText( JasperServerManager.getString("reportUnitDialog.labelDescription","Description"));
        jLabelLabel.setText( JasperServerManager.getString("reportUnitDialog.labelLabel","Label"));
        jLabelName.setText( JasperServerManager.getString("reportUnitDialog.labelName","Name"));
        jLabelUriString.setText( JasperServerManager.getString("reportUnitDialog.labelParentFolder","Parent folder"));
        
        jButtonBrowse.setText( JasperServerManager.getString("reportUnitDialog.buttonBrowse","Browse"));
        jButtonEditLocalDataSource.setText( JasperServerManager.getString("reportUnitDialog.buttonEditLocalResource","Edit local datasource"));
        jButtonGetCurrentReport.setText( JasperServerManager.getString("reportUnitDialog.buttonGetCurrentReport","Get source from current opened report"));
        jButtonPickJrxml.setText(  JasperServerManager.getString("reportUnitDialog.buttonBrowse","Browse"));
        jButtonPickResource.setText( JasperServerManager.getString("reportUnitDialog.buttonBrowse","Browse"));
        jLabelResourceFile.setText( JasperServerManager.getString("reportUnitDialog.labelMainJRXML","Main JRXML"));
        jLabelResourceType1.setText( JasperServerManager.getString("reportUnitDialog.labelDatasource","Datasource"));
        jRadioButtonLocalDataSource.setText( JasperServerManager.getString("reportUnitDialog.radioLocal","Locally Defined"));
        jRadioButtonLocal1.setText( JasperServerManager.getString("reportUnitDialog.radioLocal","Locally Defined"));
        jRadioButtonRepo.setText( JasperServerManager.getString("reportUnitDialog.radioRepo","From the repository"));
        jRadioButtonRepo1.setText( JasperServerManager.getString("reportUnitDialog.radioRepo","From the repository"));
        
        jLabelInputControlRenderingView.setText( JasperServerManager.getString("reportUnitDialog.labelInputControlRenderingView","JSP for running Input Controls (blank for default)"));
        jLabelReportRenderingView.setText( JasperServerManager.getString("reportUnitDialog.labelReportRenderingView","JSP for Report View (blank for default)"));
        
        jTabbedPane1.setTitleAt(0, JasperServerManager.getString("reportUnitDialog.tabGeneral","General") );
        jTabbedPane1.setTitleAt(1, JasperServerManager.getString("reportUnitDialog.tabDetails","Main report and Data Source") );
        jTabbedPane1.setTitleAt(2, JasperServerManager.getString("reportUnitDialog.tabOther","Other") );
        
    }
    
    private void updateSaveButton()
    {
        if (jTextFieldLabel.getText().length() > 0 &&
            jTextFieldName.getText().length() > 0)
        {
            jButtonSave.setEnabled(true);
        }
        else
        {
            jButtonSave.setEnabled(false);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelCommon = new javax.swing.JPanel();
        jLabelUriString = new javax.swing.JLabel();
        jTextFieldUriString = new javax.swing.JTextField();
        jLabelName = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelLabel = new javax.swing.JLabel();
        jTextFieldLabel = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPaneDescription = new javax.swing.JEditorPane();
        jLabelDescription = new javax.swing.JLabel();
        jPanelMainReport = new javax.swing.JPanel();
        jLabelResourceFile = new javax.swing.JLabel();
        jRadioButtonRepo1 = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jTextFieldFileRepo = new javax.swing.JTextField();
        jButtonPickJrxml = new javax.swing.JButton();
        jRadioButtonLocal1 = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jTextFieldFile = new javax.swing.JTextField();
        jButtonBrowse = new javax.swing.JButton();
        jButtonGetCurrentReport = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jLabelResourceType1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jRadioButtonRepo = new javax.swing.JRadioButton();
        jComboBoxDatasources = new javax.swing.JComboBox();
        jButtonPickResource = new javax.swing.JButton();
        jRadioButtonLocalDataSource = new javax.swing.JRadioButton();
        jButtonEditLocalDataSource = new javax.swing.JButton();
        jRadioButtonNoDataSource = new javax.swing.JRadioButton();
        jPanel9 = new javax.swing.JPanel();
        jPanelOther = new javax.swing.JPanel();
        jLabelInputControlRenderingView = new javax.swing.JLabel();
        jTextFieldInputControlRenderingView = new javax.swing.JTextField();
        jLabelReportRenderingView = new javax.swing.JLabel();
        jTextFieldReportRenderingView = new javax.swing.JTextField();
        jLabelLayout = new javax.swing.JLabel();
        jComboBoxControlsLayout = new javax.swing.JComboBox();
        jCheckBoxAlwaysPrompt = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanelButtons = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Report unit");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/documents_label.png"))); // NOI18N
        jLabel1.setText("Report unit");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jPanel1, gridBagConstraints);

        jSeparator1.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator1.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jSeparator1, gridBagConstraints);

        jPanelCommon.setPreferredSize(new java.awt.Dimension(400, 185));
        jPanelCommon.setLayout(new java.awt.GridBagLayout());

        jLabelUriString.setText("Parent folder");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelCommon.add(jLabelUriString, gridBagConstraints);

        jTextFieldUriString.setEditable(false);
        jTextFieldUriString.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextFieldUriString.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelCommon.add(jTextFieldUriString, gridBagConstraints);

        jLabelName.setText("ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelCommon.add(jLabelName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelCommon.add(jTextFieldName, gridBagConstraints);

        jSeparator2.setMinimumSize(new java.awt.Dimension(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        jPanelCommon.add(jSeparator2, gridBagConstraints);

        jLabelLabel.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelCommon.add(jLabelLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelCommon.add(jTextFieldLabel, gridBagConstraints);

        jScrollPane1.setViewportView(jEditorPaneDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelCommon.add(jScrollPane1, gridBagConstraints);

        jLabelDescription.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelCommon.add(jLabelDescription, gridBagConstraints);

        jTabbedPane1.addTab("Common", jPanelCommon);

        jPanelMainReport.setLayout(new java.awt.GridBagLayout());

        jLabelResourceFile.setText("Main JRXML");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 4);
        jPanelMainReport.add(jLabelResourceFile, gridBagConstraints);

        buttonGroup2.add(jRadioButtonRepo1);
        jRadioButtonRepo1.setText("From the repository");
        jRadioButtonRepo1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonRepo1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonRepo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonRepoActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 4);
        jPanelMainReport.add(jRadioButtonRepo1, gridBagConstraints);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jTextFieldFileRepo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel6.add(jTextFieldFileRepo, gridBagConstraints);

        jButtonPickJrxml.setText("Browse");
        jButtonPickJrxml.setEnabled(false);
        jButtonPickJrxml.setMinimumSize(new java.awt.Dimension(73, 21));
        jButtonPickJrxml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed12(evt);
            }
        });
        jPanel6.add(jButtonPickJrxml, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 4);
        jPanelMainReport.add(jPanel6, gridBagConstraints);

        buttonGroup2.add(jRadioButtonLocal1);
        jRadioButtonLocal1.setText("Locally Defined");
        jRadioButtonLocal1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonLocal1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonLocal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonLocalActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 4);
        jPanelMainReport.add(jRadioButtonLocal1, gridBagConstraints);

        jPanel8.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel8.add(jTextFieldFile, gridBagConstraints);

        jButtonBrowse.setText("Browse");
        jButtonBrowse.setMinimumSize(new java.awt.Dimension(73, 21));
        jButtonBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseActionPerformed1(evt);
            }
        });
        jPanel8.add(jButtonBrowse, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 4);
        jPanelMainReport.add(jPanel8, gridBagConstraints);

        jButtonGetCurrentReport.setText("Get source from current opened report");
        jButtonGetCurrentReport.setMinimumSize(null);
        jButtonGetCurrentReport.setPreferredSize(null);
        jButtonGetCurrentReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed11(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 30, 2, 4);
        jPanelMainReport.add(jButtonGetCurrentReport, gridBagConstraints);

        jSeparator5.setMinimumSize(new java.awt.Dimension(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 6, 4);
        jPanelMainReport.add(jSeparator5, gridBagConstraints);

        jLabelResourceType1.setText("Datasource");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelMainReport.add(jLabelResourceType1, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(jRadioButtonRepo);
        jRadioButtonRepo.setText("From the repository");
        jRadioButtonRepo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonRepo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonRepo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonRepoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 4);
        jPanel3.add(jRadioButtonRepo, gridBagConstraints);

        jComboBoxDatasources.setEditable(true);
        jComboBoxDatasources.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 2);
        jPanel3.add(jComboBoxDatasources, gridBagConstraints);

        jButtonPickResource.setText("Browse");
        jButtonPickResource.setEnabled(false);
        jButtonPickResource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPickResourceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 8);
        jPanel3.add(jButtonPickResource, gridBagConstraints);

        buttonGroup1.add(jRadioButtonLocalDataSource);
        jRadioButtonLocalDataSource.setSelected(true);
        jRadioButtonLocalDataSource.setText("Locally Defined");
        jRadioButtonLocalDataSource.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonLocalDataSource.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonLocalDataSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonLocalDataSourceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 4);
        jPanel3.add(jRadioButtonLocalDataSource, gridBagConstraints);

        jButtonEditLocalDataSource.setText("Edit local datasource");
        jButtonEditLocalDataSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditLocalDataSourceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 0);
        jPanel3.add(jButtonEditLocalDataSource, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelMainReport.add(jPanel3, gridBagConstraints);

        buttonGroup1.add(jRadioButtonNoDataSource);
        jRadioButtonNoDataSource.setText("Don't use any data source");
        jRadioButtonNoDataSource.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonNoDataSource.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonNoDataSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonNoDataSourcejRadioButtonLocalActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 4);
        jPanelMainReport.add(jRadioButtonNoDataSource, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanelMainReport.add(jPanel9, gridBagConstraints);

        jTabbedPane1.addTab("Main report and Data Source", jPanelMainReport);

        jPanelOther.setLayout(new java.awt.GridBagLayout());

        jLabelInputControlRenderingView.setText("JSP for running Input Controls (blank for default)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 4);
        jPanelOther.add(jLabelInputControlRenderingView, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelOther.add(jTextFieldInputControlRenderingView, gridBagConstraints);

        jLabelReportRenderingView.setText("JSP for Report View (blank for default)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 4);
        jPanelOther.add(jLabelReportRenderingView, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelOther.add(jTextFieldReportRenderingView, gridBagConstraints);

        jLabelLayout.setText("Controls Layout");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 0, 4);
        jPanelOther.add(jLabelLayout, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanelOther.add(jComboBoxControlsLayout, gridBagConstraints);

        jCheckBoxAlwaysPrompt.setSelected(true);
        jCheckBoxAlwaysPrompt.setText("Always prompt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 0, 4);
        jPanelOther.add(jCheckBoxAlwaysPrompt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanelOther.add(jPanel10, gridBagConstraints);

        jTabbedPane1.addTab("Other", jPanelOther);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        jSeparator4.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator4.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jSeparator4, gridBagConstraints);

        jPanelButtons.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanelButtons.setPreferredSize(new java.awt.Dimension(10, 30));
        jPanelButtons.setLayout(new java.awt.GridBagLayout());

        jButtonSave.setText("Create report unit");
        jButtonSave.setEnabled(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanelButtons.add(jButtonSave, gridBagConstraints);

        jButtonClose.setText("Cancel");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonClose, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        getContentPane().add(jPanelButtons, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed12(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed12
        ResourceChooser rc = new ResourceChooser();
        rc.setServer( getServer() );
        if (rc.showDialog(this, null) == JOptionPane.OK_OPTION) {
            ResourceDescriptor rd = rc.getSelectedDescriptor();
            
            if (rd == null || rd.getUriString() == null) {
                jTextFieldFileRepo.setText("");
            } else {
                if (!rd.getWsType().equals(rd.TYPE_JRXML) )
                {
                    JOptionPane.showMessageDialog(Misc.getMainFrame(),
                            JasperServerManager.getString("reportUnitDialog.message.chooseJrxml","Please choose a JRXML resource"),
                            "",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                else
                {
                    jTextFieldFileRepo.setText( rd.getUriString() );
                }
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed12

    private void jRadioButtonLocalActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonLocalActionPerformed1
        updateJrxmlFromType();
    }//GEN-LAST:event_jRadioButtonLocalActionPerformed1

    private void jRadioButtonRepoActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonRepoActionPerformed1
        updateJrxmlFromType();
    }//GEN-LAST:event_jRadioButtonRepoActionPerformed1

    private void jButton1ActionPerformed11(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed11

        JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
        if (view != null && view.getLookup() != null)
        {
            JrxmlDataObject dobject = view.getLookup().lookup(JrxmlDataObject.class);
            if (dobject != null)
            {
                jTextFieldFile.setText( dobject.getPrimaryFile().getPath() );
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed11

    private void jButtonEditLocalDataSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditLocalDataSourceActionPerformed

        
        DataSourceDialog dtd = new DataSourceDialog(Misc.getMainFrame(),true);
        dtd.setServer(this.getServer());
        dtd.setParentFolder( this.getParentFolder() + "/<dataSource>");
        dtd.setDoNotStore(true);
        if (dataSourceDescriptor != null)
        {
                dtd.setResource(dataSourceDescriptor);
        }
            
        dtd.setVisible(true);
            
        if (dtd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            dataSourceDescriptor = dtd.getNewResourceDescriptor();
        }
        
    }//GEN-LAST:event_jButtonEditLocalDataSourceActionPerformed

    private void jRadioButtonLocalDataSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonLocalDataSourceActionPerformed
        updateResourceFromType();
}//GEN-LAST:event_jRadioButtonLocalDataSourceActionPerformed

    private void jButtonPickResourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPickResourceActionPerformed
        ResourceChooser rc = new ResourceChooser();
        rc.setServer( getServer() );
        if (rc.showDialog(this, null) == JOptionPane.OK_OPTION) {
            ResourceDescriptor rd = rc.getSelectedDescriptor();
            if (rd == null || rd.getUriString() == null) {
                jComboBoxDatasources.setSelectedItem("");
            } else {
                jComboBoxDatasources.setSelectedItem( rd.getUriString() );
            }
            updateSaveButton();
        }
    }//GEN-LAST:event_jButtonPickResourceActionPerformed

    private void jRadioButtonRepoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonRepoActionPerformed
        updateResourceFromType();
    }//GEN-LAST:event_jRadioButtonRepoActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed

        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        
        
        ResourceDescriptor rd = new ResourceDescriptor();
        
        File resourceFile = null;
        
        rd.setWsType( ResourceDescriptor.TYPE_REPORTUNIT );
        rd.setDescription( jEditorPaneDescription.getText().trim() ); //getResource().getDescriptor().getDescription()
        rd.setName( jTextFieldName.getText()  );
        String uri = getParentFolder();
        if (!uri.endsWith("/")) uri = uri + "/";
        uri += jTextFieldName.getText();
        rd.setUriString( uri );
        rd.setLabel(jTextFieldLabel.getText().trim() ); //getResource().getDescriptor().getLabel()  );
        rd.setParentFolder( getParentFolder() );
        rd.setIsNew( resource == null );
        rd.setResourceProperty(ResourceDescriptor.PROP_RU_ALWAYS_PROPMT_CONTROLS, ""+jCheckBoxAlwaysPrompt.isSelected());

        if (jTextFieldInputControlRenderingView.getText().trim().length() > 0)
        {
            rd.setResourceProperty(rd.PROP_RU_INPUTCONTROL_RENDERING_VIEW, jTextFieldInputControlRenderingView.getText().trim() );
        }
        
        if (jTextFieldReportRenderingView.getText().trim().length() > 0)
        {
            rd.setResourceProperty(rd.PROP_RU_REPORT_RENDERING_VIEW, jTextFieldReportRenderingView.getText().trim() );
        }


        rd.setResourceProperty(ResourceDescriptor.PROP_RU_CONTROLS_LAYOUT, ""+((Tag)jComboBoxControlsLayout.getSelectedItem()).getValue() );
        
        // Add the datasource resource...
        if (JasperServerManager.getMainInstance().getBrandingProperties().getProperty("ireport.manage.datasources.enabled", "true").equals("true"))
        {
            ResourceDescriptor tmpDataSourceDescriptor = null;
            if (jRadioButtonRepo.isSelected())
            {
                tmpDataSourceDescriptor = new ResourceDescriptor();
                tmpDataSourceDescriptor.setWsType( ResourceDescriptor.TYPE_DATASOURCE );
                tmpDataSourceDescriptor.setReferenceUri( jComboBoxDatasources.getSelectedItem()+"");
                tmpDataSourceDescriptor.setIsReference(true);
            }
            else if (jRadioButtonLocalDataSource.isSelected())
            {
                if (dataSourceDescriptor == null)
                {
                    JOptionPane.showMessageDialog(this,
                            JasperServerManager.getString("reportUnitDialog.message.localDatasourceNotDefined","The local datasource is not correctly defined.\nPress the button \"Edit local datasource\" to fix the problem."));
                    return;
                }
                tmpDataSourceDescriptor = dataSourceDescriptor;
                tmpDataSourceDescriptor.setIsReference(false);
            }
            if (tmpDataSourceDescriptor != null)
            {
                tmpDataSourceDescriptor.setName("report_datasource");
                rd.getChildren().add( tmpDataSourceDescriptor );
            }
        }
        
        // Add the jrxml resource...
        ResourceDescriptor jrxmlDescriptor = new ResourceDescriptor();
        jrxmlDescriptor.setWsType( ResourceDescriptor.TYPE_JRXML );
        
        if (jRadioButtonRepo1.isSelected())
        {
            jrxmlDescriptor.setIsNew(true);
            jrxmlDescriptor.setMainReport(true);
            jrxmlDescriptor.setIsReference(true);
            jrxmlDescriptor.setReferenceUri( jTextFieldFileRepo.getText() );
            rd.getChildren().add( jrxmlDescriptor );
        }
        else
        {
            if (resource == null || jTextFieldFile.getText().trim().length() > 0)
            {
                jrxmlDescriptor.setName( jTextFieldName.getText() + "_jrxml");
                jrxmlDescriptor.setLabel("Main jrxml"); //getResource().getDescriptor().getLabel()  );
                jrxmlDescriptor.setDescription("Main jrxml"); //getResource().getDescriptor().getDescription()
                jrxmlDescriptor.setIsNew(true);
                jrxmlDescriptor.setHasData(true);
                jrxmlDescriptor.setMainReport(true);
                resourceFile = new File( jTextFieldFile.getText());

                if (!resourceFile.exists()) {
                    
                    JOptionPane.showMessageDialog(this, 
                            JasperServerManager.getFormattedString("reportUnitDialog.message.fileNotFound","{0}\n\nFile not found!",new Object[]{jTextFieldFile.getText()}));
                    return;
                }
                rd.getChildren().add( jrxmlDescriptor );
            }
        }
            
        try {
            
            validate(rd);
            newResourceDescriptor = getServer().getWSClient().addOrModifyResource(rd, resourceFile);
            setDialogResult(JOptionPane.OK_OPTION);

            // Data source deletion not yet supported...

//            if (JasperServerManager.getMainInstance().getBrandingProperties().getProperty("ireport.manage.datasources.enabled", "true").equals("true"))
//            {
//                // Look if we have a datasource...
//                if (jRadioButtonNoDataSource.isSelected() && currentDataSourceDescriptor != null)
//                {
//                    System.out.println("Deleting resource: " + currentDataSourceDescriptor.getUriString());
//                    System.out.flush();
//                    getServer().getWSClient().delete(currentDataSourceDescriptor, newResourceDescriptor.getUriString());
//                }
//            }

            if (resource != null && newResourceDescriptor != null)
            {
                resource.setDescriptor(newResourceDescriptor);
            }





            this.setVisible(false);
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {ex.getMessage()}));
            ex.printStackTrace();
            return;
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonBrowseActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseActionPerformed1
        String fileName = "";
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser(IReportManager.getInstance().getCurrentDirectory());
        
        jfc.setDialogTitle("Pick a file....");
        
        jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".xml") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "JasperReports XML *.xml, *.jrxml";
                }
            });
        
        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
            
            jTextFieldFile.setText(  jfc.getSelectedFile()+"");
            updateSaveButton();
        }
    }//GEN-LAST:event_jButtonBrowseActionPerformed1

    private void jRadioButtonNoDataSourcejRadioButtonLocalActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonNoDataSourcejRadioButtonLocalActionPerformed1
        updateResourceFromType();
}//GEN-LAST:event_jRadioButtonNoDataSourcejRadioButtonLocalActionPerformed1
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReportUnitDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
    
    /**
     * Accept a list of Strings or ResourceDescriptor
     */
    public void setDatasources(List datasources)
    {
        jComboBoxDatasources.removeAllItems();
        for (int i=0; i<datasources.size(); ++i)
        {
            Object datasource = datasources.get(i);
            if (datasource instanceof java.lang.String)
            {
                jComboBoxDatasources.addItem(datasource);
            }
            else if (datasource instanceof ResourceDescriptor)
            {
                jComboBoxDatasources.addItem(((ResourceDescriptor)datasource).getUriString());
            }
        }
        
        if (jComboBoxDatasources.getItemCount() > 0)
        {
            jComboBoxDatasources.setSelectedIndex(0);
        }
    }

    public JServer getServer() {
        return server;
    }

    public void setServer(JServer server) {
        this.server = server;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
        this.jTextFieldUriString.setText(parentFolder);
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButtonBrowse;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonEditLocalDataSource;
    private javax.swing.JButton jButtonGetCurrentReport;
    private javax.swing.JButton jButtonPickJrxml;
    private javax.swing.JButton jButtonPickResource;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBoxAlwaysPrompt;
    private javax.swing.JComboBox jComboBoxControlsLayout;
    private javax.swing.JComboBox jComboBoxDatasources;
    private javax.swing.JEditorPane jEditorPaneDescription;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelInputControlRenderingView;
    private javax.swing.JLabel jLabelLabel;
    private javax.swing.JLabel jLabelLayout;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelReportRenderingView;
    private javax.swing.JLabel jLabelResourceFile;
    private javax.swing.JLabel jLabelResourceType1;
    private javax.swing.JLabel jLabelUriString;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelCommon;
    private javax.swing.JPanel jPanelMainReport;
    private javax.swing.JPanel jPanelOther;
    private javax.swing.JRadioButton jRadioButtonLocal1;
    private javax.swing.JRadioButton jRadioButtonLocalDataSource;
    private javax.swing.JRadioButton jRadioButtonNoDataSource;
    private javax.swing.JRadioButton jRadioButtonRepo;
    private javax.swing.JRadioButton jRadioButtonRepo1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextFieldFile;
    private javax.swing.JTextField jTextFieldFileRepo;
    private javax.swing.JTextField jTextFieldInputControlRenderingView;
    private javax.swing.JTextField jTextFieldLabel;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldReportRenderingView;
    private javax.swing.JTextField jTextFieldUriString;
    // End of variables declaration//GEN-END:variables
    
    
    public void updateResourceFromType()
    {
        jComboBoxDatasources.setEnabled( jRadioButtonRepo.isSelected() );
        jButtonPickResource.setEnabled( jRadioButtonRepo.isSelected() );
        
        jButtonEditLocalDataSource.setEnabled( jRadioButtonLocalDataSource.isSelected() );
    }
    
    public void updateJrxmlFromType()
    {
        jTextFieldFileRepo.setEnabled( jRadioButtonRepo1.isSelected() );
        jButtonPickJrxml.setEnabled( jRadioButtonRepo1.isSelected() );
        jTextFieldFile.setEnabled( jRadioButtonLocal1.isSelected() );
        jButtonBrowse.setEnabled( jRadioButtonLocal1.isSelected() );
        jButtonGetCurrentReport.setEnabled( jRadioButtonLocal1.isSelected() );
    }
    
    public ResourceDescriptor getNewResourceDescriptor() {
        return newResourceDescriptor;
    }
    
    /**
     * Call this method to modify the specified resource...
     */
    public void setResource(RepositoryReportUnit resource)
    {
        this.resource = resource;
        
        if (resource != null)
        {
            setResource(resource.getDescriptor());
        }
        jTextFieldName.setEditable(false);
        jTextFieldName.setOpaque(false);
    }
    
    /**
     * Call this method to modify the specified resource...
     */
    public void setResource(ResourceDescriptor descriptor)
    {
        if (descriptor == null) return;
        
        setTitle( JasperServerManager.getFormattedString("properties.title", "{0} - Properties", new Object[]{descriptor.getName()}));
        
        jTextFieldName.setText( descriptor.getName());
        jTextFieldLabel.setText( descriptor.getLabel());
        jEditorPaneDescription.setText( descriptor.getDescription());
        
        if (descriptor.getResourcePropertyValue(descriptor.PROP_RU_INPUTCONTROL_RENDERING_VIEW) != null)
            jTextFieldInputControlRenderingView.setText(descriptor.getResourcePropertyValue(descriptor.PROP_RU_INPUTCONTROL_RENDERING_VIEW));

        if (descriptor.getResourcePropertyValue(descriptor.PROP_RU_REPORT_RENDERING_VIEW) != null)
            jTextFieldReportRenderingView.setText(descriptor.getResourcePropertyValue(descriptor.PROP_RU_REPORT_RENDERING_VIEW));


        Boolean b = descriptor.getResourcePropertyValueAsBoolean(descriptor.PROP_RU_ALWAYS_PROPMT_CONTROLS);
        if (b != null)
        {
            jCheckBoxAlwaysPrompt.setSelected(b);
        }

        String controlsLayout = descriptor.getResourcePropertyValue(ResourceDescriptor.PROP_RU_CONTROLS_LAYOUT);
        if (controlsLayout == null)
        {
            controlsLayout = ""+ResourceDescriptor.RU_CONTROLS_LAYOUT_POPUP_SCREEN;
        }

        Misc.setComboboxSelectedTagValue(jComboBoxControlsLayout, controlsLayout);

        // Update child descriptors...
        //if (descriptor.getChildren() == null ||
        //    descriptor.getChildren().size() == 0)
        //{
            // Update the content all the times...
            try {
                java.util.List children = getServer().getWSClient().list(descriptor);
                if (children != null)
                {
                    descriptor.setChildren(children);
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        //}

        jRadioButtonNoDataSource.setSelected(true);
        
        for (int i=0; i<descriptor.getChildren().size(); ++i)
        {
            
            ResourceDescriptor rd = (ResourceDescriptor)descriptor.getChildren().get(i);
            
            System.out.println("Resource " + i + " "+rd.getWsType());
            System.out.flush();
            
            if (rd.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE))
            {
                jComboBoxDatasources.setSelectedItem( rd.getReferenceUri() );
                jRadioButtonRepo.setSelected(true);
                jRadioButtonLocalDataSource.setSelected(false);
                currentDataSourceDescriptor = rd;
            }
            else if ( RepositoryFolder.isDataSource( rd))
            {
                System.out.println("Setting URI: "  + rd.getReferenceUri());
                dataSourceDescriptor = rd;
                jRadioButtonLocalDataSource.setSelected(true);
                jRadioButtonRepo.setSelected(false);
                currentDataSourceDescriptor = rd;
            }
            else if ( rd.getWsType().equals(ResourceDescriptor.TYPE_JRXML) &&
                      rd.isMainReport())
            {
                if (!rd.getUriString().startsWith( descriptor.getUriString() ))
                {
                    jRadioButtonLocal1.setSelected(false);
                    jRadioButtonRepo1.setSelected(true);
                    jTextFieldFileRepo.setText(rd.getUriString());
                }
                else
                {
                    jRadioButtonLocal1.setSelected(true);
                    jRadioButtonRepo1.setSelected(false);
                }
            }
            else
            {
                 // Resources and controls....
               
            }
            updateResourceFromType();
            updateJrxmlFromType();
        }

        if (!jRadioButtonNoDataSource.isSelected())
        {
            jRadioButtonNoDataSource.setEnabled(false);
        }
    }

    public RepositoryReportUnit getResource() {
        return resource;
    }

    public boolean isControlsSupportActive() {
        return controlsSupportActive;
    }

    public void setControlsSupportActive(boolean controlsSupportActive) {
        this.controlsSupportActive = controlsSupportActive;
        
    }

    public boolean isResourcesSupportActive() {
        return resourcesSupportActive;
    }

    public void setResourcesSupportActive(boolean resourcesSupportActive) {
        this.resourcesSupportActive = resourcesSupportActive;
    }
    
    /**
     * This method valitates data. If something is wrong, an exception si thrown.
     * The validation code was arranged by the web UI
     */
    public void validate(ResourceDescriptor dataType) throws Exception 
    {
		ValidationUtils.validateName( dataType.getName() );
                ValidationUtils.validateLabel( dataType.getLabel() );
                ValidationUtils.validateDesc( dataType.getDescription() );
    }
}
