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

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.ConfigurablePlainDocument;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.ui.*;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author  gtoffoli
 */
public class QueryDialog extends javax.swing.JDialog {
    
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    
    private JServer server = null;
    private String parentFolder = null;
    private String reportUnitUri = null;
    private RepositoryFolder resource = null;
    private ResourceDescriptor dataSourceDescriptor = null;
    
    private ResourceDescriptor newResourceDescriptor = null;
    
    private boolean doNotStore = false;
    
    /**
     * Creates new form DataSourceDialog
     */
    public QueryDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        setLocationRelativeTo(null);
        javax.swing.event.DocumentListener changesListener = new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
        };
        
        this.jTextFieldName.setDocument(new ConfigurablePlainDocument(JasperServerManager.MAX_ID_LENGHT));
        this.jTextFieldLabel.setDocument(new ConfigurablePlainDocument(JasperServerManager.MAX_NAME_LENGHT));
        this.jEditorPaneDescription.setDocument(new ConfigurablePlainDocument(250));
        
        this.jTextFieldLabel.getDocument().addDocumentListener(changesListener);
        this.jTextFieldName.getDocument().addDocumentListener(changesListener);
        jTextFieldName.requestFocusInWindow();

        List list = JasperServerManager.getMainInstance().getSupportedQueryLanguages();
        
        for (int i=0; i<list.size(); ++i)
        {
            jComboBoxQueryLanguage.addItem( list.get(i) );
        }
        
        //jComboBoxQueryLanguage.addItem( new Tag("sql","SQL"));
        //jComboBoxQueryLanguage.addItem( new Tag("hql","Hibernate Query Language (HQL)"));
        //jComboBoxQueryLanguage.addItem( new Tag("xPath","XPath"));
        //jComboBoxQueryLanguage.addItem( new Tag("ejbql","EJBQL"));
        //jComboBoxQueryLanguage.addItem( new Tag("mdx","MDX"));
        
        
        jTextAreaSql.getDocument().addDocumentListener(changesListener);    
        
        if (!JasperServerManager.getMainInstance().getBrandingProperties().getProperty("ireport.manage.datasources.enabled", "true").equals("true"))
        {
                jTabbedPane1.remove(jPanelDatasource);
        }
        
        applyI18n();
        
        
        
    }
    
    public void applyI18n()
    {
        jButtonClose.setText( JasperServerManager.getString("queryDialog.buttonCancel","Cancel"));
        jButtonSave.setText( JasperServerManager.getString("queryDialog.buttonSave","Save"));
        jLabel1.setText( JasperServerManager.getString("queryDialog.title","Query"));
        jLabelDescription.setText( JasperServerManager.getString("queryDialog.labelDescription","Description"));
        jLabelLabel.setText( JasperServerManager.getString("queryDialog.labelLabel","Label"));
        jLabelName.setText( JasperServerManager.getString("queryDialog.labelName","Name"));
        jLabelUriString.setText( JasperServerManager.getString("queryDialog.labelParentFolder","Parent folder"));
        jLabel2.setText( JasperServerManager.getString("queryDialog.labelQuery","Query"));
        jLabel3.setText( JasperServerManager.getString("queryDialog.labelQueryLanguage","Query language"));
        jTabbedPane1.setTitleAt(0, JasperServerManager.getString("queryDialog.tabGeneral","General") );
        jTabbedPane1.setTitleAt(1, JasperServerManager.getString("queryDialog.tabDetails","Query") );
    }
    
    
    private void updateSaveButton()
    {
        if (jTextFieldLabel.getText().length() > 0 &&
            jTextFieldName.getText().length() > 0 &&
            jTextAreaSql.getText().length() > 0)
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
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
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxQueryLanguage = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaSql = new javax.swing.JTextArea();
        jPanelDatasource = new javax.swing.JPanel();
        jLabelResourceType1 = new javax.swing.JLabel();
        jRadioButtonRepo = new javax.swing.JRadioButton();
        jComboBoxDatasources = new javax.swing.JComboBox();
        jButtonPickResource = new javax.swing.JButton();
        jRadioButtonLocal = new javax.swing.JRadioButton();
        jButtonEditLocalDataSource = new javax.swing.JButton();
        jRadioButtonNone = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Data Type");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_new.png"))); // NOI18N
        jLabel1.setText("Query");
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

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 185));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabelUriString.setText("Parent folder");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(jLabelUriString, gridBagConstraints);

        jTextFieldUriString.setEditable(false);
        jTextFieldUriString.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextFieldUriString.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(jTextFieldUriString, gridBagConstraints);

        jLabelName.setText("ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jLabelName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jTextFieldName, gridBagConstraints);

        jSeparator2.setMinimumSize(new java.awt.Dimension(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        jPanel2.add(jSeparator2, gridBagConstraints);

        jLabelLabel.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jLabelLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(jTextFieldLabel, gridBagConstraints);

        jScrollPane1.setViewportView(jEditorPaneDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jScrollPane1, gridBagConstraints);

        jLabelDescription.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel2.add(jLabelDescription, gridBagConstraints);

        jTabbedPane1.addTab("General", jPanel2);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Query language");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        jPanel3.add(jLabel3, gridBagConstraints);

        jComboBoxQueryLanguage.setEditable(true);
        jComboBoxQueryLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxQueryLanguageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel3.add(jComboBoxQueryLanguage, gridBagConstraints);

        jLabel2.setText("Query");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        jPanel3.add(jLabel2, gridBagConstraints);

        jTextAreaSql.setColumns(20);
        jTextAreaSql.setRows(5);
        jScrollPane2.setViewportView(jTextAreaSql);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel3.add(jScrollPane2, gridBagConstraints);

        jTabbedPane1.addTab("Query", jPanel3);

        jPanelDatasource.setLayout(new java.awt.GridBagLayout());

        jLabelResourceType1.setText("Locate Data Source");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelDatasource.add(jLabelResourceType1, gridBagConstraints);

        buttonGroup1.add(jRadioButtonRepo);
        jRadioButtonRepo.setSelected(true);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 4);
        jPanelDatasource.add(jRadioButtonRepo, gridBagConstraints);

        jComboBoxDatasources.setEditable(true);
        jComboBoxDatasources.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 2);
        jPanelDatasource.add(jComboBoxDatasources, gridBagConstraints);

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
        jPanelDatasource.add(jButtonPickResource, gridBagConstraints);

        buttonGroup1.add(jRadioButtonLocal);
        jRadioButtonLocal.setText("Locally Defined");
        jRadioButtonLocal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonLocal.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonLocalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 4);
        jPanelDatasource.add(jRadioButtonLocal, gridBagConstraints);

        jButtonEditLocalDataSource.setText("Edit local datasource");
        jButtonEditLocalDataSource.setEnabled(false);
        jButtonEditLocalDataSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditLocalDataSourceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        jPanelDatasource.add(jButtonEditLocalDataSource, gridBagConstraints);

        buttonGroup1.add(jRadioButtonNone);
        jRadioButtonNone.setText("None");
        jRadioButtonNone.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonNone.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonNone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonLocalActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 8, 4, 4);
        jPanelDatasource.add(jRadioButtonNone, gridBagConstraints);

        jTabbedPane1.addTab("Data Source", jPanelDatasource);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        jPanel4.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel4.setPreferredSize(new java.awt.Dimension(10, 30));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jButtonSave.setText("Save");
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
        jPanel4.add(jButtonSave, gridBagConstraints);

        jButtonClose.setText("Cancel");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonClose, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        getContentPane().add(jPanel4, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonLocalActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonLocalActionPerformed1
        updateDataSourceFromType();
    }//GEN-LAST:event_jRadioButtonLocalActionPerformed1

    private void jButtonEditLocalDataSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditLocalDataSourceActionPerformed
        DataSourceDialog dtd = new DataSourceDialog(Misc.getMainFrame(),true);
        dtd.setServer(this.getServer());
        dtd.setParentFolder( this.getParentFolder() + "/<dataSource>");
        dtd.setDoNotStore(true);
        if (dataSourceDescriptor != null) {
            dtd.setResource(dataSourceDescriptor);
        }
        
        dtd.setVisible(true);
        
        if (dtd.getDialogResult() == JOptionPane.OK_OPTION) {
            dataSourceDescriptor = dtd.getNewResourceDescriptor();
        }
    }//GEN-LAST:event_jButtonEditLocalDataSourceActionPerformed

    private void jRadioButtonLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonLocalActionPerformed
        updateDataSourceFromType();
    }//GEN-LAST:event_jRadioButtonLocalActionPerformed

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
        updateDataSourceFromType();
    }//GEN-LAST:event_jRadioButtonRepoActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed

        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        
        
        ResourceDescriptor rd = new ResourceDescriptor();
        
        rd.setDescription( jEditorPaneDescription.getText().trim() ); //getResource().getDescriptor().getDescription()
        rd.setName( jTextFieldName.getText()  );
        String uri = getParentFolder();
        if (!uri.endsWith("/")) uri = uri + "/";
        uri += jTextFieldName.getText();
        rd.setUriString( uri );
        rd.setLabel(jTextFieldLabel.getText().trim() ); //getResource().getDescriptor().getLabel()  );
        rd.setParentFolder( getParentFolder() );
        rd.setIsNew(resource == null);
        
        rd.setWsType( ResourceDescriptor.TYPE_QUERY);

        rd.setSql( jTextAreaSql.getText() );
        
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
            else if (jRadioButtonLocal.isSelected())
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
            else
            {
                // No datasource set.
            }

            if (tmpDataSourceDescriptor != null)
            {
                rd.getChildren().add(tmpDataSourceDescriptor);
            }
        }
        
        String queryLanguage = "sql";
        
        Object obj = jComboBoxQueryLanguage.getSelectedItem();
        if (obj != null && obj instanceof Tag) 
        {
            queryLanguage = ""+((Tag)obj).getValue();
        }
        else
        {
            queryLanguage = ""+obj;
        }     

        rd.setResourceProperty("PROP_QUERY_LANGUAGE", queryLanguage);
        
        try {
            
            validate(rd);
            if (!doNotStore)
            {
                newResourceDescriptor = getServer().getWSClient().addOrModifyResource(rd, null);
            }
            else newResourceDescriptor = rd;
            
            setDialogResult(JOptionPane.OK_OPTION);
            
            if (resource != null)
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

    private void jComboBoxQueryLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxQueryLanguageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxQueryLanguageActionPerformed
    

    public int getDialogResult() {
        return dialogResult; 
    }     
                
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
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
        
        if (reportUnitUri != null && reportUnitUri.length() > 0)
        this.parentFolder = reportUnitUri + "_files";
        
        this.jTextFieldUriString.setText(parentFolder);
    }
    
    public String getReportUnitUri() {
        return reportUnitUri;
    }

    public void setReportUnitUri(String reportUnitUri) {
        this.reportUnitUri = reportUnitUri;
        
        if (reportUnitUri != null && reportUnitUri.length() > 0)
            this.setParentFolder( reportUnitUri + "_files" );
    }

    public ResourceDescriptor getNewResourceDescriptor() {
        return newResourceDescriptor;
    }
    
    /**
     * Call this method to modify the specified resource...
     */
    public void setResource(RepositoryFolder resource)
    {
        
        this.resource = resource;
        if (resource != null)
        {
            setResource(resource.getDescriptor());
            jTextFieldName.setEditable(false);
            jTextFieldName.setOpaque(false);
        }
    }
    
    
    /**
     * Call this method to modify the specified descriptor...
     */
    public void setResource(ResourceDescriptor descriptor)
    {
        
        this.resource = resource;
        jTextFieldName.setText( descriptor.getName());
        setTitle( JasperServerManager.getFormattedString("properties.title", "{0} - Properties", new Object[]{descriptor.getName()}));
        
        jTextFieldLabel.setText( descriptor.getLabel());
        jEditorPaneDescription.setText( descriptor.getDescription());
        
        jTextAreaSql.setText( (descriptor.getSql() != null) ? descriptor.getSql() : "" ); 
        
        String ql = descriptor.getResourcePropertyValue( ResourceDescriptor.PROP_QUERY_LANGUAGE);
        if (ql == null) ql = "sql";
        
        boolean found = false;
        for (int ix=0; ix<jComboBoxQueryLanguage.getItemCount(); ++ix)
        {
           Object value = jComboBoxQueryLanguage.getItemAt(ix);
           if (jComboBoxQueryLanguage.getItemAt(ix) instanceof Tag)
           {
               value = ((Tag)jComboBoxQueryLanguage.getItemAt(ix)).getValue();
           }
           if (value.equals(ql))
           {
               found = true;
               jComboBoxQueryLanguage.setSelectedIndex(ix);
               break;
           }
        }
        if (!found) // Default is sql...
        {
            jComboBoxQueryLanguage.setSelectedItem(ql);
        }
        
        jRadioButtonLocal.setSelected(false);
        jRadioButtonRepo.setSelected(false);
        jRadioButtonNone.setSelected(true);
                
        for (int i=0; i<descriptor.getChildren().size(); ++i)
        {
            
            ResourceDescriptor rd = (ResourceDescriptor)descriptor.getChildren().get(i);
            if (rd.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE))
            {
                jComboBoxDatasources.setSelectedItem( rd.getReferenceUri() );
                jRadioButtonRepo.setSelected(true);
                jRadioButtonLocal.setSelected(false);
                jRadioButtonNone.setSelected(false);
            }
            else if ( RepositoryFolder.isDataSource( rd))
            {
                dataSourceDescriptor = rd;
                jRadioButtonLocal.setSelected(true);
                jRadioButtonRepo.setSelected(false);
                jRadioButtonNone.setSelected(false);
            }
            else
            {
                 // Unknown children...
            }
        }
        updateDataSourceFromType();
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonEditLocalDataSource;
    private javax.swing.JButton jButtonPickResource;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JComboBox jComboBoxDatasources;
    private javax.swing.JComboBox jComboBoxQueryLanguage;
    private javax.swing.JEditorPane jEditorPaneDescription;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelLabel;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelResourceType1;
    private javax.swing.JLabel jLabelUriString;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelDatasource;
    private javax.swing.JRadioButton jRadioButtonLocal;
    private javax.swing.JRadioButton jRadioButtonNone;
    private javax.swing.JRadioButton jRadioButtonRepo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextAreaSql;
    private javax.swing.JTextField jTextFieldLabel;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldUriString;
    // End of variables declaration//GEN-END:variables
    
    
   
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
    
     public boolean isDoNotStore() {
        return doNotStore;
    }

    public void setDoNotStore(boolean doNotStore) {
        this.doNotStore = doNotStore;
    }
    
    public void updateDataSourceFromType()
    {
        jComboBoxDatasources.setEnabled( jRadioButtonRepo.isSelected() );
        jButtonPickResource.setEnabled( jRadioButtonRepo.isSelected() );
        
        jButtonEditLocalDataSource.setEnabled( jRadioButtonLocal.isSelected() );
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

         updateDataSourceFromType();
    }
}
