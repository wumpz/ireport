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

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.connection.JRXMLADataSourceConnection;
import com.jaspersoft.ireport.designer.utils.ConfigurablePlainDocument;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.ui.ValidationUtils;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.List;
import javax.swing.JOptionPane;


/**
 *
 * @author  gtoffoli
 */
public class XMLAConnectionDialog extends javax.swing.JDialog {
    
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    
    private JServer server = null;
    private String parentFolder = null;
    private RepositoryFolder datasourceResource = null;
    
    private ResourceDescriptor newResourceDescriptor = null;
    
    private boolean doNotStore = false;
    
    /**
     * Creates new form DataSourceDialog
     */
    public XMLAConnectionDialog(java.awt.Frame parent, boolean modal) {
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
        this.jTextFieldURL.getDocument().addDocumentListener(changesListener);
        this.jTextFieldCatalog.getDocument().addDocumentListener(changesListener);
        this.jTextFieldDatasource.getDocument().addDocumentListener(changesListener);
        
        applyI18n();
        
        jTextFieldName.requestFocusInWindow();
        
        jButtonImportConnection.setEnabled( true );   
        
    }
    
    public void applyI18n()
    {
        jButtonClose.setText( JasperServerManager.getString("xmlaConnectionDialog.buttonCancel","Cancel"));
        jButtonImportConnection.setText( JasperServerManager.getString("xmlaConnectionDialog.buttonImportConnection","Import from iReport"));
        jButtonSave.setText( JasperServerManager.getString("xmlaConnectionDialog.buttonSave","Save"));
        jLabel1.setText( JasperServerManager.getString("xmlaConnectionDialog.title","Data Source"));
        jLabelDescription.setText( JasperServerManager.getString("xmlaConnectionDialog.labelDescription","Description"));
        jLabelLabel.setText( JasperServerManager.getString("xmlaConnectionDialog.labelLabel","Label"));
        jLabelName.setText( JasperServerManager.getString("xmlaConnectionDialog.labelName","Name"));
        
        jLabelUriString.setText( JasperServerManager.getString("xmlaConnectionDialog.labelParentFolder","Parent folder"));
        
        jLabelPassword.setText( JasperServerManager.getString("xmlaConnectionDialog.labelPassword","Password"));
        jLabelURL.setText( JasperServerManager.getString("xmlaConnectionDialog.labelURI","URI"));
        jLabelCatalog.setText( JasperServerManager.getString("xmlaConnectionDialog.labelCatalog","Catalog"));
        jLabelDatasource.setText( JasperServerManager.getString("xmlaConnectionDialog.labelDatasource","Data Source"));
        jLabelUsername.setText( JasperServerManager.getString("xmlaConnectionDialog.labelUsername","Username"));
        jTabbedPane1.setTitleAt(0, JasperServerManager.getString("xmlaConnectionDialog.tabGeneral","General") );
        jTabbedPane1.setTitleAt(1, JasperServerManager.getString("xmlaConnectionDialog.tabDetails","XMLA/A connection details") );
    }
    
    private void updateSaveButton()
    {
        if (jTextFieldLabel.getText().length() > 0 &&
            jTextFieldName.getText().length() > 0)
        {
            boolean ok = false;
            if (jTextFieldURL.getText().length() > 0 &&
                jTextFieldCatalog.getText().length() > 0 &&
                jTextFieldDatasource.getText().length() > 0) 
            {
                ok = true;
            }
            
            jButtonSave.setEnabled(ok);
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
        jLabelURL = new javax.swing.JLabel();
        jTextFieldURL = new javax.swing.JTextField();
        jLabelCatalog = new javax.swing.JLabel();
        jTextFieldCatalog = new javax.swing.JTextField();
        jLabelDatasource = new javax.swing.JLabel();
        jTextFieldDatasource = new javax.swing.JTextField();
        jLabelUsername = new javax.swing.JLabel();
        jTextFieldUsername = new javax.swing.JTextField();
        jLabelPassword = new javax.swing.JLabel();
        jPasswordField = new javax.swing.JPasswordField();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButtonImportConnection = new javax.swing.JButton();
        jButtonImportConnection1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DataSource");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_new.png"))); // NOI18N
        jLabel1.setText("XML/A Connection");
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

        jScrollPane1.setMinimumSize(new java.awt.Dimension(368, 219));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(368, 219));
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

        jLabelURL.setText("URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 4, 0);
        jPanel3.add(jLabelURL, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 8);
        jPanel3.add(jTextFieldURL, gridBagConstraints);

        jLabelCatalog.setText("Catalog");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 4, 0);
        jPanel3.add(jLabelCatalog, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 8);
        jPanel3.add(jTextFieldCatalog, gridBagConstraints);

        jLabelDatasource.setText("Datasource");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 0);
        jPanel3.add(jLabelDatasource, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanel3.add(jTextFieldDatasource, gridBagConstraints);

        jLabelUsername.setText("Username");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 4, 0);
        jPanel3.add(jLabelUsername, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 8);
        jPanel3.add(jTextFieldUsername, gridBagConstraints);

        jLabelPassword.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 0);
        jPanel3.add(jLabelPassword, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanel3.add(jPasswordField, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel5, gridBagConstraints);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jButtonImportConnection.setText("Import from iReport");
        jButtonImportConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportConnectionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel6.add(jButtonImportConnection, gridBagConstraints);

        jButtonImportConnection1.setText("Import to iReport");
        jButtonImportConnection1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportConnection1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 8);
        jPanel6.add(jButtonImportConnection1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel6, gridBagConstraints);

        jTabbedPane1.addTab("Connection details", jPanel3);

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

    private void jButtonImportConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportConnectionActionPerformed

        List<IReportConnection> iReportConnections = IReportManager.getInstance().getConnections();
        
        
        
        java.util.Vector validConnections = new java.util.Vector();
        for (IReportConnection conn : iReportConnections)
        {
            if (conn instanceof JRXMLADataSourceConnection)
            {
                validConnections.add(conn);
            }
        }
        if (validConnections.size() == 0)
        {
            JOptionPane.showMessageDialog(this,
                        JasperServerManager.getString("xmlaConnectionDialog.message.noXMLAconfigured","No XML/A datasources currently configured in iReport.")
                    );
            return;
        }
        JRXMLADataSourceConnection[] connections = new JRXMLADataSourceConnection[validConnections.size()];
        for (int i=0; i<connections.length; ++i)
        {
            connections[i] = (JRXMLADataSourceConnection)(validConnections.elementAt(i));
        }
        
        JRXMLADataSourceConnection selectedCon = (JRXMLADataSourceConnection)JOptionPane.showInputDialog(this,
                JasperServerManager.getString("xmlaConnectionDialog.message.selectXMLA","Select a XML/A datasource:"),
                JasperServerManager.getString("xmlaConnectionDialog.message.import","Import..."),
                0,null,connections, connections[0]);
        if (selectedCon != null)
        {
            jTextFieldCatalog.setText( selectedCon.getCatalog() );
            jTextFieldDatasource.setText( selectedCon.getDatasource() );
            jTextFieldURL.setText( selectedCon.getUrl() );
            //jTextFieldUsername.setText( descriptor.getResourcePropertyValue( descriptor.PROP_XMLA_USERNAME ));
            //jPasswordField.setText( descriptor.getResourcePropertyValue( descriptor.PROP_XMLA_PASSWORD 
        }
        
    }//GEN-LAST:event_jButtonImportConnectionActionPerformed

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
        rd.setIsNew(datasourceResource == null);
        
        
        rd.setWsType( ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION );
        
        rd.setResourceProperty( ResourceDescriptor.PROP_XMLA_URI, jTextFieldURL.getText());
        rd.setResourceProperty( ResourceDescriptor.PROP_XMLA_CATALOG, jTextFieldCatalog.getText());
        rd.setResourceProperty( ResourceDescriptor.PROP_XMLA_DATASOURCE, jTextFieldDatasource.getText());
        rd.setResourceProperty( ResourceDescriptor.PROP_XMLA_USERNAME, jTextFieldUsername.getText());
        rd.setResourceProperty( ResourceDescriptor.PROP_XMLA_PASSWORD, new String(jPasswordField.getPassword()));
       
        try {
            
            validate(rd);
            
            if (!doNotStore)
            {
                newResourceDescriptor = getServer().getWSClient().addOrModifyResource(rd, null);
            }
            else newResourceDescriptor = rd;
            
            setDialogResult(JOptionPane.OK_OPTION);
            
            if (datasourceResource != null)
            {
                datasourceResource.setDescriptor(newResourceDescriptor);
            }
            
            this.setVisible(false);
            this.dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {ex.getMessage()}));
            ex.printStackTrace();
            return;
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonImportConnection1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportConnection1ActionPerformed

        JRXMLADataSourceConnection conn = new JRXMLADataSourceConnection();

        String name = jTextFieldLabel.getText();
        if (name.trim().length() == 0)
        {
            name = "XMLA Connection";
        }

        List<IReportConnection> connections = IReportManager.getInstance().getConnections();
        for (int i=0; ; i++)
        {
            String tmpName = name + ((i>0) ? " (" + i + ")" : "");
            boolean found = false;
            for (IReportConnection c : connections)
            {
                if (c.getName().equals(tmpName))
                {
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                name = tmpName;
                break;
            }
        }

        conn.setName(name);
        conn.setUsername( jTextFieldUsername.getText() );
        conn.setPassword( new String(jPasswordField.getPassword()) );
        conn.setUrl( jTextFieldURL.getText() );
        conn.setCatalog(jTextFieldCatalog.getText() );
        conn.setDatasource( jTextFieldDatasource.getText() );
        conn.setSavePassword(true);

        IReportManager.getInstance().addConnection(conn);
        IReportManager.getInstance().setDefaultConnection(conn);
        JOptionPane.showMessageDialog(this, "XML/A Connection successfully imported (" + name + ")" );
    }//GEN-LAST:event_jButtonImportConnection1ActionPerformed
    

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
        this.jTextFieldUriString.setText(parentFolder);
    }

    public ResourceDescriptor getNewResourceDescriptor() {
        return newResourceDescriptor;
    }
    
    /**
     * Call this method to modify the specified resource...
     */
    public void setResource(RepositoryFolder resource)
    {
        this.datasourceResource = resource;
        if (resource != null)
        {
           setResource( resource.getDescriptor());
           jTextFieldName.setEditable(false);
           jTextFieldName.setOpaque(false);
        }
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
                
        if (descriptor.getWsType().equals( ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION))
        {
            jTextFieldCatalog.setText( descriptor.getResourcePropertyValue( descriptor.PROP_XMLA_CATALOG ));
            jTextFieldDatasource.setText( descriptor.getResourcePropertyValue( descriptor.PROP_XMLA_DATASOURCE ));
            jTextFieldURL.setText( descriptor.getResourcePropertyValue( descriptor.PROP_XMLA_URI ));
            jTextFieldUsername.setText( descriptor.getResourcePropertyValue( descriptor.PROP_XMLA_USERNAME ));
            jPasswordField.setText( descriptor.getResourcePropertyValue( descriptor.PROP_XMLA_PASSWORD ));
        }
               
        jButtonSave.setText("Save");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonImportConnection;
    private javax.swing.JButton jButtonImportConnection1;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JEditorPane jEditorPaneDescription;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelCatalog;
    private javax.swing.JLabel jLabelDatasource;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelLabel;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelURL;
    private javax.swing.JLabel jLabelUriString;
    private javax.swing.JLabel jLabelUsername;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextFieldCatalog;
    private javax.swing.JTextField jTextFieldDatasource;
    private javax.swing.JTextField jTextFieldLabel;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldURL;
    private javax.swing.JTextField jTextFieldUriString;
    private javax.swing.JTextField jTextFieldUsername;
    // End of variables declaration//GEN-END:variables
    
    
    
    public boolean isDoNotStore() {
        return doNotStore;
    }

    public void setDoNotStore(boolean doNotStore) {
        this.doNotStore = doNotStore;
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
