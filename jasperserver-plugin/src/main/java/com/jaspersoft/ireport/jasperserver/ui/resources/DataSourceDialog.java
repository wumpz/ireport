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

import com.jaspersoft.ireport.jasperserver.ui.*;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.connection.JDBCConnection;
import com.jaspersoft.ireport.designer.connection.JDBCNBConnection;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.ConfigurablePlainDocument;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.netbeans.api.db.explorer.DatabaseConnection;

/**
 *
 * @author  gtoffoli
 */
public class DataSourceDialog extends javax.swing.JDialog {

    private int dialogResult = JOptionPane.CANCEL_OPTION;
    private JServer server = null;
    private String parentFolder = null;
    private RepositoryFolder datasourceResource = null;
    private ResourceDescriptor newResourceDescriptor = null;
    private boolean doNotStore = false;

    /**
     * Creates new form DataSourceDialog
     */
    public DataSourceDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocationRelativeTo(null);
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCloseActionPerformed(e);
            }
        };
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);

        //to make the default button ...
        getRootPane().setDefaultButton(jButtonClose);

        
        
        
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
        this.jTextFieldDriver.getDocument().addDocumentListener(changesListener);
        this.jTextFieldURL.getDocument().addDocumentListener(changesListener);
        this.jTextFieldUsername.getDocument().addDocumentListener(changesListener);
        this.jTextFieldServiceName.getDocument().addDocumentListener(changesListener);
        this.jPasswordField.getDocument().addDocumentListener(changesListener);
        this.jTextFieldBeanName.getDocument().addDocumentListener(changesListener);
        
        this.jTextFieldHiveJDBCUrl.getDocument().addDocumentListener(changesListener);
        
        
        this.jTextFieldMongoDBURI.getDocument().addDocumentListener(changesListener);
        this.jTextFieldMongoDBUsername.getDocument().addDocumentListener(changesListener);
        this.jPasswordFieldMongoDB.getDocument().addDocumentListener(changesListener);



        applyI18n();

        jTextFieldName.requestFocusInWindow();




        jComboBox1.setModel(new DefaultComboBoxModel(new Tag[]{
                    new Tag(jPanelJDBC, jPanelJDBC.getName()),
                    new Tag(jPanelJNDI, jPanelJNDI.getName()),
                    new Tag(jPanelBeanDataSource, jPanelBeanDataSource.getName()),
                    new Tag(jPanelVirtualDatasource, jPanelVirtualDatasource.getName()),
                    new Tag(jPanelHadoopHive, jPanelHadoopHive.getName()),
                    new Tag(jPanelMongoDB, jPanelMongoDB.getName())
                }));
        
        
        jComboBox1.setSelectedIndex(0);
        
        javax.swing.DefaultListSelectionModel dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableDatasetParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableDatasetParametersListSelectionValueChanged(e);
            }
        });
        
        jTableDatasetParameters.getColumnModel().getColumn(0).setCellRenderer(new DatasourceTableCellRenderer());
        
        
        ((DefaultTableModel)jTableDatasetParameters.getModel()).addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent tme) {
               updateSaveButton();
            }
        });

    }
    
    
     public void jTableDatasetParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableDatasetParameters.getSelectedRowCount() > 0) {
            this.jButtonModParameter.setEnabled(true);
            this.jButtonRemParameter.setEnabled(jTableDatasetParameters.getRowCount() > 0);
        }
        else {
            this.jButtonModParameter.setEnabled(false);
            this.jButtonRemParameter.setEnabled(false);
        }
    }

    public void applyI18n() {
        jButtonClose.setText(JasperServerManager.getString("dataSourceDialog.buttonCancel", "Cancel"));
        jButtonImportConnection.setText(JasperServerManager.getString("dataSourceDialog.buttonImportConnection", "Import from iReport"));
        jButtonSave.setText(JasperServerManager.getString("dataSourceDialog.buttonSave", "Save"));
        jLabel1.setText(JasperServerManager.getString("dataSourceDialog.title", "Data Source"));
        jLabelBeanMethod.setText(JasperServerManager.getString("dataSourceDialog.labelBeanMethod", "Bean Method"));
        jLabelBeanName.setText(JasperServerManager.getString("dataSourceDialog.labelBeanName", "Bean Name"));
        jLabelDescription.setText(JasperServerManager.getString("dataSourceDialog.labelDescription", "Description"));
        jLabelDriver.setText(JasperServerManager.getString("dataSourceDialog.labelDriver", "Driver"));
        jLabelLabel.setText(JasperServerManager.getString("dataSourceDialog.labelLabel", "Label"));
        jLabelName.setText(JasperServerManager.getString("dataSourceDialog.labelName", "Name"));
        jLabelUriString.setText(JasperServerManager.getString("dataSourceDialog.labelParentFolder", "Parent folder"));
        jLabelPassword.setText(JasperServerManager.getString("dataSourceDialog.labelPassword", "Password"));
        jLabelServiceName.setText(JasperServerManager.getString("dataSourceDialog.labelServiceName", "Service Name"));
        jLabelURL.setText(JasperServerManager.getString("dataSourceDialog.labelURL", "URL"));
        jLabelUsername.setText(JasperServerManager.getString("dataSourceDialog.labelUsername", "Username"));
//        jRadioButtonBean.setText( JasperServerManager.getString("dataSourceDialog.radioBean","Bean Data Source"));
//        jRadioButtonJDBC.setText( JasperServerManager.getString("dataSourceDialog.radioJDBC","JDBC Data Source"));
//        jRadioButtonJNDI.setText( JasperServerManager.getString("dataSourceDialog.radioJNDI","JNDI Data Source"));
        jTabbedPane1.setTitleAt(0, JasperServerManager.getString("dataSourceDialog.tabGeneral", "General"));
        jTabbedPane1.setTitleAt(1, JasperServerManager.getString("dataSourceDialog.tabDetails", "Data Source details"));
    }

    private void updateSaveButton() {
        if (jTextFieldLabel.getText().length() > 0
                && jTextFieldName.getText().length() > 0) {
            boolean ok = false;

            Tag selectedPanel = (Tag) jComboBox1.getSelectedItem();

            if (selectedPanel.getValue() == jPanelJDBC
                    && jTextFieldDriver.getText().length() > 0
                    && jTextFieldURL.getText().length() > 0
                    && jTextFieldUsername.getText().length() > 0) {
                ok = true;
            } else if (selectedPanel.getValue() == jPanelJNDI
                    && jTextFieldServiceName.getText().length() > 0) {
                ok = true;
            } else if (selectedPanel.getValue() == jPanelBeanDataSource
                    && jTextFieldBeanName.getText().length() > 0) {
                ok = true;
            } else if (selectedPanel.getValue() == jPanelVirtualDatasource
                    && jTableDatasetParameters.getRowCount() > 0 ) {
                ok = true;
            } else if (selectedPanel.getValue() == jPanelHadoopHive
                    && jTextFieldHiveJDBCUrl.getText().length() > 0 ) {
                ok = true;
            } else if (selectedPanel.getValue() == jPanelMongoDB
                    && jTextFieldMongoDBURI.getText().length() > 0 ) {
                ok = true;
            }

            jButtonSave.setEnabled(ok);
        } else {
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

        jPanelJDBC = new javax.swing.JPanel();
        jLabelDriver = new javax.swing.JLabel();
        jTextFieldDriver = new javax.swing.JTextField();
        jLabelURL = new javax.swing.JLabel();
        jTextFieldURL = new javax.swing.JTextField();
        jLabelUsername = new javax.swing.JLabel();
        jTextFieldUsername = new javax.swing.JTextField();
        jLabelPassword = new javax.swing.JLabel();
        jPasswordField = new javax.swing.JPasswordField();
        jButtonImportConnection = new javax.swing.JButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jPanelJNDI = new javax.swing.JPanel();
        jLabelServiceName = new javax.swing.JLabel();
        jTextFieldServiceName = new javax.swing.JTextField();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jPanelBeanDataSource = new javax.swing.JPanel();
        jLabelBeanName = new javax.swing.JLabel();
        jTextFieldBeanName = new javax.swing.JTextField();
        jLabelBeanMethod = new javax.swing.JLabel();
        jTextFieldBeanMethod = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        jPanelVirtualDatasource = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDatasetParameters = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jButtonAddParameter = new javax.swing.JButton();
        jButtonModParameter = new javax.swing.JButton();
        jButtonRemParameter = new javax.swing.JButton();
        jPanelHadoopHive = new javax.swing.JPanel();
        jLabelServiceName1 = new javax.swing.JLabel();
        jTextFieldHiveJDBCUrl = new javax.swing.JTextField();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jPanelMongoDB = new javax.swing.JPanel();
        jLabelURL1 = new javax.swing.JLabel();
        jTextFieldMongoDBURI = new javax.swing.JTextField();
        jLabelUsername1 = new javax.swing.JLabel();
        jTextFieldMongoDBUsername = new javax.swing.JTextField();
        jLabelPassword1 = new javax.swing.JLabel();
        jPasswordFieldMongoDB = new javax.swing.JPasswordField();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
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
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanelDatasource = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        jPanelJDBC.setName("JDBC"); // NOI18N
        jPanelJDBC.setLayout(new java.awt.GridBagLayout());

        jLabelDriver.setText("Driver");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        jPanelJDBC.add(jLabelDriver, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 8);
        jPanelJDBC.add(jTextFieldDriver, gridBagConstraints);

        jLabelURL.setText("URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        jPanelJDBC.add(jLabelURL, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanelJDBC.add(jTextFieldURL, gridBagConstraints);

        jLabelUsername.setText("Username");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        jPanelJDBC.add(jLabelUsername, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanelJDBC.add(jTextFieldUsername, gridBagConstraints);

        jLabelPassword.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        jPanelJDBC.add(jLabelPassword, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanelJDBC.add(jPasswordField, gridBagConstraints);

        jButtonImportConnection.setText("Import from iReport");
        jButtonImportConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportConnectionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 8);
        jPanelJDBC.add(jButtonImportConnection, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanelJDBC.add(filler3, gridBagConstraints);

        jPanelJNDI.setName("JNDI"); // NOI18N
        jPanelJNDI.setLayout(new java.awt.GridBagLayout());

        jLabelServiceName.setText("Service Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelJNDI.add(jLabelServiceName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanelJNDI.add(jTextFieldServiceName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanelJNDI.add(filler2, gridBagConstraints);

        jPanelBeanDataSource.setName("Bean Data Source"); // NOI18N
        jPanelBeanDataSource.setLayout(new java.awt.GridBagLayout());

        jLabelBeanName.setText("Bean Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        jPanelBeanDataSource.add(jLabelBeanName, gridBagConstraints);

        jTextFieldBeanName.setToolTipText("Name of configured bean");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanelBeanDataSource.add(jTextFieldBeanName, gridBagConstraints);

        jLabelBeanMethod.setText("Bean Method");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        jPanelBeanDataSource.add(jLabelBeanMethod, gridBagConstraints);

        jTextFieldBeanMethod.setToolTipText("Name of method on configured bean (optional)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanelBeanDataSource.add(jTextFieldBeanMethod, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelBeanDataSource.add(filler1, gridBagConstraints);

        jPanelVirtualDatasource.setName("Virtual Data Source"); // NOI18N
        jPanelVirtualDatasource.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setMinimumSize(new java.awt.Dimension(300, 50));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 50));

        jTableDatasetParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Datasource ID", "URI"
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
        jPanelVirtualDatasource.add(jScrollPane2, gridBagConstraints);

        jPanel10.setMinimumSize(new java.awt.Dimension(100, 10));
        jPanel10.setPreferredSize(new java.awt.Dimension(100, 69));
        jPanel10.setLayout(new java.awt.GridBagLayout());

        jButtonAddParameter.setText(org.openide.util.NbBundle.getMessage(DataSourceDialog.class, "DataSourceDialog.jButtonAddParameter.text")); // NOI18N
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

        jButtonModParameter.setText(org.openide.util.NbBundle.getMessage(DataSourceDialog.class, "DataSourceDialog.jButtonModParameter.text")); // NOI18N
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

        jButtonRemParameter.setText(org.openide.util.NbBundle.getMessage(DataSourceDialog.class, "DataSourceDialog.jButtonRemParameter.text")); // NOI18N
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
        jPanelVirtualDatasource.add(jPanel10, gridBagConstraints);

        jPanelHadoopHive.setName("Hadoop-Hive Data Source"); // NOI18N
        jPanelHadoopHive.setLayout(new java.awt.GridBagLayout());

        jLabelServiceName1.setText("Hive's JDBC URL ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelHadoopHive.add(jLabelServiceName1, gridBagConstraints);

        jTextFieldHiveJDBCUrl.setText("jdbc:hive://hostname:10000/default");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanelHadoopHive.add(jTextFieldHiveJDBCUrl, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanelHadoopHive.add(filler4, gridBagConstraints);

        jPanelMongoDB.setName("MongoDB Data Source"); // NOI18N
        jPanelMongoDB.setLayout(new java.awt.GridBagLayout());

        jLabelURL1.setText("MongoDB URI ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanelMongoDB.add(jLabelURL1, gridBagConstraints);

        jTextFieldMongoDBURI.setText("mongodb://hostname:27017/database");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanelMongoDB.add(jTextFieldMongoDBURI, gridBagConstraints);

        jLabelUsername1.setText("Username (optional)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanelMongoDB.add(jLabelUsername1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanelMongoDB.add(jTextFieldMongoDBUsername, gridBagConstraints);

        jLabelPassword1.setText("Password (optional)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanelMongoDB.add(jLabelPassword1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanelMongoDB.add(jPasswordFieldMongoDB, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanelMongoDB.add(filler5, gridBagConstraints);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DataSource");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_new.png"))); // NOI18N
        jLabel1.setText("Datasource");
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

        jLabel2.setText("Datasource Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel3.add(jLabel2, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 4);
        jPanel3.add(jComboBox1, gridBagConstraints);

        jPanelDatasource.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));
        jPanelDatasource.setMinimumSize(new java.awt.Dimension(12, 300));
        jPanelDatasource.setPreferredSize(new java.awt.Dimension(100, 300));
        jPanelDatasource.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel3.add(jPanelDatasource, gridBagConstraints);

        jTabbedPane1.addTab("Datasource details", jPanel3);

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
        for (IReportConnection conn : iReportConnections) {
            if (conn instanceof JDBCConnection
                    || conn instanceof JDBCNBConnection) {
                validConnections.add(conn);
            }
        }
        if (validConnections.size() == 0) {
            JOptionPane.showMessageDialog(this,
                    JasperServerManager.getString("dataSourceDialog.message.noJDBCconfigured", "No JDBC connections currently configured in iReport."));
            return;
        }
        IReportConnection[] connections = new IReportConnection[validConnections.size()];
        for (int i = 0; i < connections.length; ++i) {
            connections[i] = (IReportConnection) (validConnections.elementAt(i));
        }

        IReportConnection selectedCon = (IReportConnection) JOptionPane.showInputDialog(this,
                JasperServerManager.getString("dataSourceDialog.message.selectJDBC", "Select a JDBC datasource:"),
                JasperServerManager.getString("dataSourceDialog.message.import", "Import..."),
                JOptionPane.QUESTION_MESSAGE, null, connections, connections[0]);
        if (selectedCon != null) {
            if (selectedCon instanceof JDBCConnection) {
                jTextFieldDriver.setText(((JDBCConnection) selectedCon).getJDBCDriver());
                jTextFieldURL.setText(((JDBCConnection) selectedCon).getUrl());
                jTextFieldUsername.setText(((JDBCConnection) selectedCon).getUsername());
                jPasswordField.setText(((JDBCConnection) selectedCon).getPassword());
            } else if (selectedCon instanceof JDBCNBConnection) {
                DatabaseConnection dbconn = ((JDBCNBConnection) selectedCon).getDatabaseConnectionObject();
                if (dbconn != null) {
                    jTextFieldDriver.setText(dbconn.getDriverClass());
                    jTextFieldURL.setText(dbconn.getDatabaseURL());
                    jTextFieldUsername.setText(dbconn.getUser());
                    jPasswordField.setText(dbconn.getPassword());
                }
            }
        }

    }//GEN-LAST:event_jButtonImportConnectionActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed

        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed

        try {
            ResourceDescriptor rd = new ResourceDescriptor();

            ValidationUtils.validateName(jTextFieldName.getText());
            ValidationUtils.validateLabel(jTextFieldLabel.getText());
            ValidationUtils.validateDesc(jEditorPaneDescription.getText());

            rd.setDescription(jEditorPaneDescription.getText().trim()); //getResource().getDescriptor().getDescription()
            rd.setName(jTextFieldName.getText());
            String uri = getParentFolder();
            if (!uri.endsWith("/")) {
                uri = uri + "/";
            }
            uri += jTextFieldName.getText();
            rd.setUriString(uri);
            rd.setLabel(jTextFieldLabel.getText().trim()); //getResource().getDescriptor().getLabel()  );
            rd.setParentFolder(getParentFolder());
            rd.setIsNew(datasourceResource == null);

            Tag selectedPanel = (Tag) jComboBox1.getSelectedItem();

            if (selectedPanel.getValue() == jPanelJDBC) {
                rd.setWsType(ResourceDescriptor.TYPE_DATASOURCE_JDBC);
                rd.setDriverClass(jTextFieldDriver.getText());
                rd.setConnectionUrl(jTextFieldURL.getText());
                rd.setUsername(jTextFieldUsername.getText());
                rd.setPassword(new String(jPasswordField.getPassword()));
            } else if (selectedPanel.getValue() == jPanelJNDI) {
                rd.setWsType(ResourceDescriptor.TYPE_DATASOURCE_JNDI);
                rd.setJndiName(jTextFieldServiceName.getText());
            } else if (selectedPanel.getValue() == jPanelBeanDataSource) {
                rd.setWsType(ResourceDescriptor.TYPE_DATASOURCE_BEAN);
                rd.setBeanName(jTextFieldBeanName.getText());
                rd.setBeanMethod(jTextFieldBeanMethod.getText());
            } else if (selectedPanel.getValue() == jPanelVirtualDatasource) {
                rd.setWsType(ResourceDescriptor.TYPE_DATASOURCE_VIRTUAL);
                rd.setBeanName(jTextFieldBeanName.getText());
                rd.setBeanMethod(jTextFieldBeanMethod.getText());
                
                // Add all the sub resources....
                
                for (int i=0; i<jTableDatasetParameters.getRowCount(); ++i)
                {
                    rd.getChildren().add( (ResourceDescriptor)jTableDatasetParameters.getValueAt(i, 0) );
                }
            } else if (selectedPanel.getValue() == jPanelHadoopHive) {
                rd.setWsType(ResourceDescriptor.TYPE_DATASOURCE_CUSTOM);
                
                rd.setResourceProperty(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS,"com.jaspersoft.hadoop.hive.jasperserver.HiveDataSourceService");
                
                Map<String, String> customPropertyMap = new HashMap<String, String>();
                customPropertyMap.put("jdbcURL", jTextFieldHiveJDBCUrl.getText().trim());
                customPropertyMap.put("_cds_name", "HiveDataSource");
                
                rd.setPropertyMap(customPropertyMap);
            } else if (selectedPanel.getValue() == jPanelMongoDB) {
                rd.setWsType(ResourceDescriptor.TYPE_DATASOURCE_CUSTOM);
                
                rd.setResourceProperty(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS,"com.jaspersoft.mongodb.jasperserver.MongoDbDataSourceService");
                
                Map<String, String> customPropertyMap = new HashMap<String, String>();
                customPropertyMap.put("mongoURI", jTextFieldMongoDBURI.getText().trim());
                customPropertyMap.put("_cds_name", "MongoDbDataSource");
                
                
                if (jTextFieldMongoDBUsername.getText().trim().length() > 0)
                {
                    customPropertyMap.put("username", jTextFieldMongoDBUsername.getText().trim());
                }
                if (jPasswordFieldMongoDB.getPassword().length > 0)
                {
                    customPropertyMap.put("password", new String(jPasswordFieldMongoDB.getPassword()));
                    System.out.println("Storing pass: " + new String(jPasswordFieldMongoDB.getPassword()));
                }
                
                rd.setPropertyMap(customPropertyMap);
            }


            if (!doNotStore) {
                newResourceDescriptor = getServer().getWSClient().addOrModifyResource(rd, null);
            } else {
                newResourceDescriptor = rd;
            }

            setDialogResult(JOptionPane.OK_OPTION);

            if (datasourceResource != null) {
                datasourceResource.setDescriptor(newResourceDescriptor);
            }

            this.setVisible(false);
            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[]{ex.getMessage()}));
            ex.printStackTrace();
            return;
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        updateDatasourceType();

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTableDatasetParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDatasetParametersMouseClicked

        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            if (jTableDatasetParameters.getSelectedRowCount() > 0) {
                jButtonModParameterActionPerformed(null);
            }
        }
     }//GEN-LAST:event_jTableDatasetParametersMouseClicked

    private void jButtonAddParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddParameterActionPerformed

        // Take a note of the current datasource IDs...         
        java.util.Map<String, ResourceDescriptor> map = new java.util.HashMap<String, ResourceDescriptor>();
        
        for (int i = 0; i < jTableDatasetParameters.getRowCount(); ++i) {
            ResourceDescriptor dsRd = (ResourceDescriptor)jTableDatasetParameters.getValueAt(i, 0);
            map.put( dsRd.getResourcePropertyValue("PROP_DATASOURCE_SUB_DS_ID"), dsRd);
        }
        
        
        VirtualDatasourceItemDialog dialog = new VirtualDatasourceItemDialog(this, true);
        
        dialog.setServer( getServer() );
        dialog.setExistingResourceDescriptors(map);
        dialog.setVisible(true);
        
        
        if (dialog.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            
            ResourceDescriptor newDsRd = dialog.getResourceDescriptor();
            try {
                
                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) jTableDatasetParameters.getModel();
                dtm.addRow(new Object[]{
                            newDsRd,
                            newDsRd.getResourcePropertyValue("PROP_REFERENCE_URI") });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
     }//GEN-LAST:event_jButtonAddParameterActionPerformed

        private void jButtonModParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModParameterActionPerformed

            java.util.Map<String, ResourceDescriptor> map = new java.util.HashMap<String, ResourceDescriptor>();
        
            for (int i = 0; i < jTableDatasetParameters.getRowCount(); ++i) {
                ResourceDescriptor dsRd = (ResourceDescriptor)jTableDatasetParameters.getValueAt(i, 0);
                map.put( dsRd.getResourcePropertyValue("PROP_DATASOURCE_SUB_DS_ID"), dsRd);
            }
            
            VirtualDatasourceItemDialog dialog = new VirtualDatasourceItemDialog(this, true);
        
            int rowNumber = jTableDatasetParameters.getSelectedRow();
            ResourceDescriptor oldRd = (ResourceDescriptor) jTableDatasetParameters.getValueAt(rowNumber, 0);
            
            dialog.setServer( getServer() );
            dialog.setResourceDescriptor(oldRd);
            dialog.setExistingResourceDescriptors(map);
            dialog.setVisible(true);
            
            if (dialog.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
                

                ResourceDescriptor newRd = dialog.getResourceDescriptor();
                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) jTableDatasetParameters.getModel();
                dtm.setValueAt(newRd, rowNumber, 0);
                dtm.setValueAt(newRd.getResourcePropertyValue("PROP_REFERENCE_URI"), rowNumber, 1);
                jTableDatasetParameters.updateUI();

            }
     }//GEN-LAST:event_jButtonModParameterActionPerformed

        private void jButtonRemParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemParameterActionPerformed

            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) jTableDatasetParameters.getModel();
            while (jTableDatasetParameters.getSelectedRowCount() > 0 && jTableDatasetParameters.getRowCount() > 0) {
                int i = jTableDatasetParameters.getSelectedRow();
                dtm.removeRow(i);
            }
     }//GEN-LAST:event_jButtonRemParameterActionPerformed

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
    public void setResource(RepositoryFolder resource) {
        this.datasourceResource = resource;
        if (resource != null) {
            setResource(resource.getDescriptor());
            jTextFieldName.setEditable(false);
            jTextFieldName.setOpaque(false);
        }


    }

    /**
     * Call this method to modify the specified resource...
     */
    public void setResource(ResourceDescriptor descriptor) {
        if (descriptor == null) {
            return;
        }

        setTitle(JasperServerManager.getFormattedString("properties.title", "{0} - Properties", new Object[]{descriptor.getName()}));

        jTextFieldName.setText(descriptor.getName());
        jTextFieldLabel.setText(descriptor.getLabel());
        jEditorPaneDescription.setText(descriptor.getDescription());

        if (descriptor.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_JDBC)) {
            Misc.setComboboxSelectedTagValue(jComboBox1, jPanelJDBC);
            jTextFieldDriver.setText(descriptor.getDriverClass());
            jTextFieldURL.setText(descriptor.getConnectionUrl());
            jTextFieldUsername.setText(descriptor.getUsername());
            jPasswordField.setText(descriptor.getPassword());
        } else if (descriptor.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_JNDI)) {
            Misc.setComboboxSelectedTagValue(jComboBox1, jPanelJNDI);
            

            jTextFieldServiceName.setText(descriptor.getJndiName());
        } else if (descriptor.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_BEAN)) {
            Misc.setComboboxSelectedTagValue(jComboBox1, jPanelBeanDataSource);
            

            jTextFieldBeanName.setText(descriptor.getBeanName());
            jTextFieldBeanMethod.setText((descriptor.getBeanMethod() != null && descriptor.getBeanMethod().trim().length() > 0)
                    ? descriptor.getBeanMethod() : "");
        }
        else if (descriptor.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_VIRTUAL)) {
            Misc.setComboboxSelectedTagValue(jComboBox1, jPanelVirtualDatasource);
            

            ((DefaultTableModel)jTableDatasetParameters.getModel()).setRowCount(0); 
            
            for (int i=0; i< descriptor.getChildren().size(); ++i)
            {
                 ResourceDescriptor childRd = (ResourceDescriptor) descriptor.getChildren().get(i);
                 
                 ((DefaultTableModel)jTableDatasetParameters.getModel()).addRow(new Object[] { childRd, childRd.getResourcePropertyValue("PROP_REFERENCE_URI")} );
            }
            jTableDatasetParameters.updateUI();
        } else if (descriptor.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_CUSTOM) &&
                   descriptor.getResourcePropertyValue(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS) != null &&
                   descriptor.getResourcePropertyValue(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS).equals("com.jaspersoft.hadoop.hive.jasperserver.HiveDataSourceService")) 
        {
            Misc.setComboboxSelectedTagValue(jComboBox1, jPanelHadoopHive);
            Map props = descriptor.getPropertyMap();
            jTextFieldHiveJDBCUrl.setText((String)props.get("jdbcURL") );
        } else if (descriptor.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_CUSTOM) &&
                   descriptor.getResourcePropertyValue(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS) != null &&
                   descriptor.getResourcePropertyValue(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS).equals("com.jaspersoft.mongodb.jasperserver.MongoDbDataSourceService")) 
        {
            Misc.setComboboxSelectedTagValue(jComboBox1, jPanelMongoDB);
            Map props = descriptor.getPropertyMap();
            jTextFieldMongoDBURI.setText( Misc.nvl( props.get("mongoURI"), "") );
            jTextFieldMongoDBUsername.setText( Misc.nvl( props.get("username"), "") );
            jPasswordFieldMongoDB.setText(Misc.nvl( props.get("password"), "") );
        }

        updateDatasourceType();

        jComboBox1.setEnabled(false);
        
        updateSaveButton();

        jButtonSave.setText("Save");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.JButton jButtonAddParameter;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonImportConnection;
    private javax.swing.JButton jButtonModParameter;
    private javax.swing.JButton jButtonRemParameter;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JEditorPane jEditorPaneDescription;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelBeanMethod;
    private javax.swing.JLabel jLabelBeanName;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelDriver;
    private javax.swing.JLabel jLabelLabel;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelPassword1;
    private javax.swing.JLabel jLabelServiceName;
    private javax.swing.JLabel jLabelServiceName1;
    private javax.swing.JLabel jLabelURL;
    private javax.swing.JLabel jLabelURL1;
    private javax.swing.JLabel jLabelUriString;
    private javax.swing.JLabel jLabelUsername;
    private javax.swing.JLabel jLabelUsername1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelBeanDataSource;
    private javax.swing.JPanel jPanelDatasource;
    private javax.swing.JPanel jPanelHadoopHive;
    private javax.swing.JPanel jPanelJDBC;
    private javax.swing.JPanel jPanelJNDI;
    private javax.swing.JPanel jPanelMongoDB;
    private javax.swing.JPanel jPanelVirtualDatasource;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JPasswordField jPasswordFieldMongoDB;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableDatasetParameters;
    private javax.swing.JTextField jTextFieldBeanMethod;
    private javax.swing.JTextField jTextFieldBeanName;
    private javax.swing.JTextField jTextFieldDriver;
    private javax.swing.JTextField jTextFieldHiveJDBCUrl;
    private javax.swing.JTextField jTextFieldLabel;
    private javax.swing.JTextField jTextFieldMongoDBURI;
    private javax.swing.JTextField jTextFieldMongoDBUsername;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldServiceName;
    private javax.swing.JTextField jTextFieldURL;
    private javax.swing.JTextField jTextFieldUriString;
    private javax.swing.JTextField jTextFieldUsername;
    // End of variables declaration//GEN-END:variables

    public void updateDatasourceType() {
        Tag selectedPanel = (Tag) jComboBox1.getSelectedItem();

        if (selectedPanel != null) {

            jPanelDatasource.removeAll();
            jPanelDatasource.add((JComponent) selectedPanel.getValue(), BorderLayout.CENTER);
            jPanelDatasource.updateUI();
            updateSaveButton();
        }
    }

    public boolean isDoNotStore() {
        return doNotStore;
    }

    public void setDoNotStore(boolean doNotStore) {
        this.doNotStore = doNotStore;
    }
}
