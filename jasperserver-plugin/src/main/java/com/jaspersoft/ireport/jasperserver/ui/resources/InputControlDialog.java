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
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author  gtoffoli
 */
public class InputControlDialog extends javax.swing.JDialog {
    
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    
    private JServer server = null;
    private String parentFolder = null;
    private RepositoryFolder resource = null;
    private String reportUnitUri = null;
    
    private ResourceDescriptor newResourceDescriptor = null;
    
    private ResourceDescriptor lovResourceDescriptor = null;
    private ResourceDescriptor dataTypeResourceDescriptor = null;
    private ResourceDescriptor queryResourceDescriptor = null;
    
    
    /**
     * Creates new form DataSourceDialog
     */
    public InputControlDialog(java.awt.Frame parent, boolean modal) {
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
        
        jComboBoxType.addItem( new Tag("1", JasperServerManager.getString("inputControlDialog.type.1","Boolean"))  );
        jComboBoxType.addItem( new Tag("2", JasperServerManager.getString("inputControlDialog.type.2","Single Value"))  );
        jComboBoxType.addItem( new Tag("3", JasperServerManager.getString("inputControlDialog.type.3","Single Select List of Values"))  );
        jComboBoxType.addItem( new Tag("8", JasperServerManager.getString("inputControlDialog.type.8","Single Select List of Values (Radio)"))  ); 
        jComboBoxType.addItem( new Tag("6", JasperServerManager.getString("inputControlDialog.type.6","Multi Select List of Values"))  );
        jComboBoxType.addItem( new Tag("10",JasperServerManager.getString("inputControlDialog.type.10","Multi Select List of Values (Checkbox)"))  ); 
        jComboBoxType.addItem( new Tag("4", JasperServerManager.getString("inputControlDialog.type.4","Single Select Query"))  );
        jComboBoxType.addItem( new Tag("9", JasperServerManager.getString("inputControlDialog.type.9","Single Select Query (Radio)"))  );
        jComboBoxType.addItem( new Tag("7", JasperServerManager.getString("inputControlDialog.type.7","Multi Select Query"))  );
        jComboBoxType.addItem( new Tag("11",JasperServerManager.getString("inputControlDialog.type.11","Multi Select Query (Checkbox)"))  );
        
        jComboBoxType.setSelectedIndex(0);
        
        this.jTextFieldLabel.getDocument().addDocumentListener(changesListener);
        this.jTextFieldName.getDocument().addDocumentListener(changesListener);
        jTextFieldName.requestFocusInWindow();
        
        jListColumns.addListSelectionListener( new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                boolean b = jListColumns.getSelectedIndex()>=0;
                jButtonModColumn.setEnabled(b);
                jButtonRemColumn.setEnabled(b);
            }
        });
        
        jListColumns.setModel( new DefaultListModel());
        
        applyI18n();

    }
    
    public void applyI18n()
     {
        jButtonClose.setText( JasperServerManager.getString("inputControlDialog.buttonCancel","Cancel"));
        jButtonSave.setText( JasperServerManager.getString("inputControlDialog.buttonSave","Save"));
        
        jButtonAddColumn.setText( JasperServerManager.getString("inputControlDialog.buttonAddColumn","Add"));
        
        jButtonEditLocalResource.setText( JasperServerManager.getString("inputControlDialog.buttonEditLocalResource","Edit local resource"));
        jButtonEditLocalResource1.setText( JasperServerManager.getString("inputControlDialog.buttonEditLocalResource","Edit local resource"));
        jButtonModColumn.setText( JasperServerManager.getString("inputControlDialog.buttonModifyColumn","Modify"));
        jButtonPickResource.setText( JasperServerManager.getString("inputControlDialog.buttonPickResource","Browse"));
        jButtonPickResource1.setText( JasperServerManager.getString("inputControlDialog.buttonPickResource","Browse"));
        jButtonRemColumn.setText( JasperServerManager.getString("inputControlDialog.buttonRemColumn","Remove"));
        
        jLabel1.setText( JasperServerManager.getString("inputControlDialog.title","Input Control"));
        jLabelDescription.setText( JasperServerManager.getString("inputControlDialog.labelDescription","Description"));
        jLabelLabel.setText( JasperServerManager.getString("inputControlDialog.labelLabel","Label"));
        jLabelLocate.setText( JasperServerManager.getString("inputControlDialog.labelLocateLOV","Locate List of Values"));
        jLabelLocate1.setText( JasperServerManager.getString("inputControlDialog.labelLocateQuery","Locate query"));
        jLabelLocate2.setText( JasperServerManager.getString("inputControlDialog.labelValueColumn","Value column"));
        jLabelLocate3.setText( JasperServerManager.getString("inputControlDialog.labelVisibleColumns","Visible query columns"));
        jLabelMandatory.setText( JasperServerManager.getString("inputControlDialog.labelMandatory","Mandatory"));
        jLabelName.setText( JasperServerManager.getString("inputControlDialog.labelName","Name"));
        jLabelUriString.setText( JasperServerManager.getString("inputControlDialog.labelParentFolder","Parent folder"));
        jLabelReadOnly.setText( JasperServerManager.getString("inputControlDialog.labelReadOnly","Read Only"));
        jLabelType.setText( JasperServerManager.getString("inputControlDialog.labelType","Type"));
        jRadioButtonLocal.setText( JasperServerManager.getString("inputControlDialog.radioLocallyDefined","Locally Defined"));
        jRadioButtonLocal1.setText( JasperServerManager.getString("inputControlDialog.radioLocallyDefined","Locally Defined"));
        jRadioButtonRepo.setText( JasperServerManager.getString("inputControlDialog.radioRepo","From the repository"));
        jRadioButtonRepo1.setText( JasperServerManager.getString("inputControlDialog.radioRepo","From the repository"));
        
        jTabbedPane1.setTitleAt(0, JasperServerManager.getString("inputControlDialog.tabGeneral","General") );
        jTabbedPane1.setTitleAt(1, JasperServerManager.getString("inputControlDialog.tabDetails","Input control details") );
        jTabbedPane2.setTitleAt(0, JasperServerManager.getString("inputControlDialog.tabQueryResource","Query resource") );
        jTabbedPane2.setTitleAt(1, JasperServerManager.getString("inputControlDialog.tabQueryColumns","Value and visible columns") );

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
        jLabelType = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox();
        jLabelMandatory = new javax.swing.JLabel();
        jCheckBoxMandatory = new javax.swing.JCheckBox();
        jLabelReadOnly = new javax.swing.JLabel();
        jCheckBoxReadOnly = new javax.swing.JCheckBox();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jPanelLocate = new javax.swing.JPanel();
        jLabelLocate = new javax.swing.JLabel();
        jRadioButtonRepo = new javax.swing.JRadioButton();
        jTextFieldResource = new javax.swing.JTextField();
        jButtonPickResource = new javax.swing.JButton();
        jRadioButtonLocal = new javax.swing.JRadioButton();
        jButtonEditLocalResource = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanelQuery = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanelLocateQuery = new javax.swing.JPanel();
        jLabelLocate1 = new javax.swing.JLabel();
        jRadioButtonRepo1 = new javax.swing.JRadioButton();
        jTextFieldResource1 = new javax.swing.JTextField();
        jButtonPickResource1 = new javax.swing.JButton();
        jRadioButtonLocal1 = new javax.swing.JRadioButton();
        jButtonEditLocalResource1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanelFields = new javax.swing.JPanel();
        jLabelLocate2 = new javax.swing.JLabel();
        jTextFieldValueColumn = new javax.swing.JTextField();
        jPanelVisibleColumns = new javax.swing.JPanel();
        jLabelLocate3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListColumns = new javax.swing.JList();
        jPanel8 = new javax.swing.JPanel();
        jButtonAddColumn = new javax.swing.JButton();
        jButtonModColumn = new javax.swing.JButton();
        jButtonRemColumn = new javax.swing.JButton();
        jLabelVisible = new javax.swing.JLabel();
        jCheckBoxVisible = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Input Control");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_new.png"))); // NOI18N
        jLabel1.setText("Input Control");
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

        jLabelType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 20, 4, 0);
        jPanel3.add(jLabelType, gridBagConstraints);

        jComboBoxType.setMinimumSize(new java.awt.Dimension(23, 22));
        jComboBoxType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 20);
        jPanel3.add(jComboBoxType, gridBagConstraints);

        jLabelMandatory.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelMandatory.setText("Mandatory");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        jPanel3.add(jLabelMandatory, gridBagConstraints);

        jCheckBoxMandatory.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 20);
        jPanel3.add(jCheckBoxMandatory, gridBagConstraints);

        jLabelReadOnly.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelReadOnly.setText("Read Only");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        jPanel3.add(jLabelReadOnly, gridBagConstraints);

        jCheckBoxReadOnly.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 20);
        jPanel3.add(jCheckBoxReadOnly, gridBagConstraints);

        jSeparator4.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator4.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        jPanel3.add(jSeparator4, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanelLocate.setLayout(new java.awt.GridBagLayout());

        jLabelLocate.setText("Locate List of Values");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 8, 8);
        jPanelLocate.add(jLabelLocate, gridBagConstraints);

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
        jPanelLocate.add(jRadioButtonRepo, gridBagConstraints);

        jTextFieldResource.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 0);
        jPanelLocate.add(jTextFieldResource, gridBagConstraints);

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
        jPanelLocate.add(jButtonPickResource, gridBagConstraints);

        buttonGroup1.add(jRadioButtonLocal);
        jRadioButtonLocal.setSelected(true);
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 4);
        jPanelLocate.add(jRadioButtonLocal, gridBagConstraints);

        jButtonEditLocalResource.setText("Edit local resource");
        jButtonEditLocalResource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditLocalResourceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        jPanelLocate.add(jButtonEditLocalResource, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanelLocate.add(jPanel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel5.add(jPanelLocate, gridBagConstraints);

        jPanelQuery.setLayout(new java.awt.GridBagLayout());

        jPanelLocateQuery.setLayout(new java.awt.GridBagLayout());

        jLabelLocate1.setText("Locate query");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 8, 8);
        jPanelLocateQuery.add(jLabelLocate1, gridBagConstraints);

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
        jPanelLocateQuery.add(jRadioButtonRepo1, gridBagConstraints);

        jTextFieldResource1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 0);
        jPanelLocateQuery.add(jTextFieldResource1, gridBagConstraints);

        jButtonPickResource1.setText("Browse");
        jButtonPickResource1.setEnabled(false);
        jButtonPickResource1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPickResourceActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 8);
        jPanelLocateQuery.add(jButtonPickResource1, gridBagConstraints);

        buttonGroup2.add(jRadioButtonLocal1);
        jRadioButtonLocal1.setSelected(true);
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
        jPanelLocateQuery.add(jRadioButtonLocal1, gridBagConstraints);

        jButtonEditLocalResource1.setText("Edit local resource");
        jButtonEditLocalResource1.setActionCommand("Edit local query resource");
        jButtonEditLocalResource1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditLocalResourceActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        jPanelLocateQuery.add(jButtonEditLocalResource1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanelLocateQuery.add(jPanel7, gridBagConstraints);

        jTabbedPane2.addTab("Query resource", jPanelLocateQuery);

        jPanelFields.setLayout(new java.awt.GridBagLayout());

        jLabelLocate2.setText("Value column");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 8, 8);
        jPanelFields.add(jLabelLocate2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 8, 8);
        jPanelFields.add(jTextFieldValueColumn, gridBagConstraints);

        jPanelVisibleColumns.setLayout(new java.awt.GridBagLayout());

        jLabelLocate3.setText("Visible query columns");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelVisibleColumns.add(jLabelLocate3, gridBagConstraints);

        jListColumns.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListColumnsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jListColumns);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelVisibleColumns.add(jScrollPane2, gridBagConstraints);

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jButtonAddColumn.setText("Add");
        jButtonAddColumn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddColumnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel8.add(jButtonAddColumn, gridBagConstraints);

        jButtonModColumn.setText("Modify");
        jButtonModColumn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModColumnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel8.add(jButtonModColumn, gridBagConstraints);

        jButtonRemColumn.setText("Remove");
        jButtonRemColumn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemColumnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel8.add(jButtonRemColumn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanelVisibleColumns.add(jPanel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelFields.add(jPanelVisibleColumns, gridBagConstraints);

        jTabbedPane2.addTab("Value and visible columns", jPanelFields);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelQuery.add(jTabbedPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel5.add(jPanelQuery, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel5, gridBagConstraints);

        jLabelVisible.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelVisible.setText("Visible");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 8, 0);
        jPanel3.add(jLabelVisible, gridBagConstraints);

        jCheckBoxVisible.setSelected(true);
        jCheckBoxVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 8, 20);
        jPanel3.add(jCheckBoxVisible, gridBagConstraints);

        jTabbedPane1.addTab("Input Control details", jPanel3);

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

    private void jButtonRemColumnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemColumnActionPerformed

        int keys[] = jListColumns.getSelectedIndices();
        DefaultListModel dlm = (DefaultListModel)jListColumns.getModel();
        for (int i = keys.length-1; i>=0; --i)
        {
            dlm.remove(keys[i]);
        }
    
        
    }//GEN-LAST:event_jButtonRemColumnActionPerformed

    private void jButtonAddColumnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddColumnActionPerformed

        String s = JOptionPane.showInputDialog(Misc.getMainFrame(),JasperServerManager.getString("inputControlDialog.columnName","Column name"));
        if (s != null  && s.trim().length()>0)
        {
            ((DefaultListModel)jListColumns.getModel()).addElement(s);
        }
        
    }//GEN-LAST:event_jButtonAddColumnActionPerformed

    private void jButtonModColumnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModColumnActionPerformed

        int i = jListColumns.getSelectedIndex();
        if (i < 0) return;
        String s = ""+jListColumns.getSelectedValue();    
        s = JOptionPane.showInputDialog(Misc.getMainFrame(),JasperServerManager.getString("inputControlDialog.columnName","Column name"),s);
        if (s != null && s.trim().length()>0)
        {
            ((DefaultListModel)jListColumns.getModel()).setElementAt(s,i);
        }
        
    }//GEN-LAST:event_jButtonModColumnActionPerformed

    private void jListColumnsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListColumnsMouseClicked

        if (evt.getClickCount() == 2 &&
            evt.getButton() == evt.BUTTON1 &&
            jListColumns.getSelectedIndex() >=0 )
        {
            jButtonModColumnActionPerformed(null);
        }
    }//GEN-LAST:event_jListColumnsMouseClicked

    private void jButtonEditLocalResourceActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditLocalResourceActionPerformed1

        
        jButtonEditLocalResourceActionPerformed(evt);
        
    }//GEN-LAST:event_jButtonEditLocalResourceActionPerformed1

    private void jRadioButtonLocalActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonLocalActionPerformed1
        updateQueryResourceFromType();
    }//GEN-LAST:event_jRadioButtonLocalActionPerformed1

    private void jButtonPickResourceActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPickResourceActionPerformed1
        ResourceChooser rc = new ResourceChooser();
        rc.setServer( getServer() );
        if (rc.showDialog(this, null) == JOptionPane.OK_OPTION)
        {
            ResourceDescriptor rd = rc.getSelectedDescriptor();
            if (rd == null || rd.getUriString() == null) 
            {
                jTextFieldResource1.setText("");
            }
            else
            {
                jTextFieldResource1.setText( rd.getUriString() );
            }
            updateSaveButton();
        }
    }//GEN-LAST:event_jButtonPickResourceActionPerformed1

    private void jRadioButtonRepoActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonRepoActionPerformed1
        updateQueryResourceFromType();
    }//GEN-LAST:event_jRadioButtonRepoActionPerformed1

    private void jButtonEditLocalResourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditLocalResourceActionPerformed

        byte b = Byte.parseByte( ((Tag)jComboBoxType.getSelectedItem()).getValue()+"");
        
        if (b == ResourceDescriptor.IC_TYPE_SINGLE_VALUE)
        {
            DataTypeDialog dtd = new DataTypeDialog(Misc.getMainFrame(),true);
            dtd.setServer(this.getServer());
            dtd.setParentFolder( this.getParentFolder() + "/<inputControl>");
            dtd.setDoNotStore(true);
            if (dataTypeResourceDescriptor != null)
            {
                dtd.setResource(dataTypeResourceDescriptor);
            }
            
            dtd.setVisible(true);
            
            if (dtd.getDialogResult() == JOptionPane.OK_OPTION)
            {
                dataTypeResourceDescriptor = dtd.getNewResourceDescriptor();
            }
        }
        else if (b == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES ||
                 b == ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES ||
                 b == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES_RADIO ||
                 b == ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES_CHECKBOX)
        {
            ListOfValuesDialog dtd = new ListOfValuesDialog(Misc.getMainFrame(),true);
            dtd.setServer(this.getServer());
            dtd.setParentFolder( this.getParentFolder() + "/<inputControl>");
            dtd.setDoNotStore(true);
            if (lovResourceDescriptor != null)
            {
                dtd.setResource(lovResourceDescriptor);
            }
            
            dtd.setVisible(true);
            
            if (dtd.getDialogResult() == JOptionPane.OK_OPTION)
            {
                lovResourceDescriptor = dtd.getNewResourceDescriptor();
            }
        }
        else if (b == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY ||
                 b == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY ||
                 b == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY_RADIO ||
                 b == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY_CHECKBOX)
        {
            QueryDialog dtd = new QueryDialog(Misc.getMainFrame(),true);
            dtd.setServer(this.getServer());
            dtd.setParentFolder( this.getParentFolder() + "/<inputControl>");
            dtd.setDoNotStore(true);
            List datasources = null;
            
            if (JasperServerManager.getMainInstance().getBrandingProperties().getProperty("ireport.manage.datasources.enabled", "true").equals("true"))
            {  
                try {
                       datasources = this.getServer().getWSClient().listDatasources(); 
                       dtd.setDatasources(datasources);
                } catch (Exception ex)
                {

                   JOptionPane.showMessageDialog(Misc.getMainFrame(),
                           JasperServerManager.getFormattedString("repositoryExplorer.message.errorListingDatasources", "Error getting the list of available datasources:\n{0}", new Object[] {ex.getMessage()}));
                   ex.printStackTrace();
                }
            }
            
            if (queryResourceDescriptor != null)
            {
                dtd.setResource(queryResourceDescriptor);
            }
            
            
            dtd.setVisible(true);
            
            if (dtd.getDialogResult() == JOptionPane.OK_OPTION)
            {
                queryResourceDescriptor = dtd.getNewResourceDescriptor();
            }
        }
    }//GEN-LAST:event_jButtonEditLocalResourceActionPerformed

    private void jButtonPickResourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPickResourceActionPerformed

        
        ResourceChooser rc = new ResourceChooser();
        rc.setServer( getServer() );
        if (rc.showDialog(this, null) == JOptionPane.OK_OPTION)
        {
            ResourceDescriptor rd = rc.getSelectedDescriptor();
            if (rd == null || rd.getUriString() == null) 
            {
                jTextFieldResource.setText("");
            }
            else
            {
                jTextFieldResource.setText( rd.getUriString() );
            }
            updateSaveButton();
        }
        
    }//GEN-LAST:event_jButtonPickResourceActionPerformed

    private void jComboBoxTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTypeActionPerformed

        String value = ((Tag)jComboBoxType.getSelectedItem()).getValue()+"";
        
        int ic_type = Integer.parseInt(value);
        
        if (ic_type == ResourceDescriptor.IC_TYPE_BOOLEAN)
        {
            jPanelLocate.setVisible(false);
            jPanelQuery.setVisible(false);
        }
        else if (ic_type == ResourceDescriptor.IC_TYPE_SINGLE_VALUE)
        {
            jPanelLocate.setVisible(true);
            jLabelLocate.setText( JasperServerManager.getString("inputControlDialog.labelLocateDataType","Locate (Data Type)"));
            jButtonEditLocalResource.setText( JasperServerManager.getString("inputControlDialog.buttonEditLocalResourceDT","Edit local resource (Data Type)"));
            
            jPanelQuery.setVisible(false);
        }
        else if (ic_type == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES ||
                 ic_type == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES_RADIO ||
                 ic_type == ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES ||
                 ic_type == ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES_CHECKBOX)
        {
            jPanelLocate.setVisible(true);
            jLabelLocate.setText( JasperServerManager.getString("inputControlDialog.labelLocateLOV","Locate (List of Values)"));
            jButtonEditLocalResource.setText( JasperServerManager.getString("inputControlDialog.buttonEditLocalResourceLOV","Edit local resource (List of Values)"));
            jPanelQuery.setVisible(false);
        }
        else if (ic_type == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY ||
                 ic_type == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY_RADIO ||
                 ic_type == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY ||
                 ic_type == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY_CHECKBOX)
        {
            jPanelLocate.setVisible(false);
            jPanelQuery.setVisible(true);
        }
        
    }//GEN-LAST:event_jComboBoxTypeActionPerformed

    private void jRadioButtonLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonLocalActionPerformed
        updateResourceFromType();
    }//GEN-LAST:event_jRadioButtonLocalActionPerformed

    private void jRadioButtonRepoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonRepoActionPerformed
        updateResourceFromType();
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
        
        rd.setWsType( ResourceDescriptor.TYPE_INPUT_CONTROL);

        rd.setMandatory( jCheckBoxMandatory.isSelected() );
        rd.setReadOnly( jCheckBoxReadOnly.isSelected() );

        // visible to be replaced with a proper constant
        rd.setResourceProperty(ResourceDescriptor.PROP_INPUTCONTROL_IS_VISIBLE, jCheckBoxVisible.isSelected());

        rd.setControlType( Byte.parseByte( ((Tag)jComboBoxType.getSelectedItem()).getValue()+""));
        
        if ( (rd.getControlType() == rd.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES |
              rd.getControlType() == rd.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES_RADIO ||
              rd.getControlType() == rd.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES ||
              rd.getControlType() == rd.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES_CHECKBOX))
        {
            if (jRadioButtonLocal.isSelected())
            {
                if (lovResourceDescriptor == null || !lovResourceDescriptor.getWsType().equals( ResourceDescriptor.TYPE_LOV))
                {
                    JOptionPane.showMessageDialog(this,"The local list of values defined is not valid.\nPress the button \"Edit local resource\" to fix the problem.");
                    return;
                }
            }
            
            if (jRadioButtonRepo1.isSelected() && jTextFieldResource1.getText().trim().length() == 0)
            { 
                JOptionPane.showMessageDialog(this,"Please set a valid reference to a List Of Values in the Repository.");
                jTextFieldResource1.requestFocusInWindow();
                return;
            }
        }
        else if (rd.getControlType() == rd.IC_TYPE_SINGLE_VALUE)
        {
            if (jRadioButtonLocal.isSelected())
            {
                if (dataTypeResourceDescriptor == null || !dataTypeResourceDescriptor.getWsType().equals( ResourceDescriptor.TYPE_DATA_TYPE))
                {
                    JOptionPane.showMessageDialog(this,"The local data type defined is not valid.\nPress the button \"Edit local resource\" to fix the problem.");
                    return;
                }
            }
            
            if (jRadioButtonRepo.isSelected() && jTextFieldResource.getText().trim().length() == 0)
            { 
                JOptionPane.showMessageDialog(this,"Please set a valid reference to a data type in the Repository.");
                jTextFieldResource.requestFocusInWindow();
                return;
            }
        }
        else if (rd.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY ||
                 rd.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY_RADIO ||
                 rd.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY ||
                 rd.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY_CHECKBOX)
        {
            if (jRadioButtonLocal1.isSelected())
            {
                if (queryResourceDescriptor == null || !queryResourceDescriptor.getWsType().equals( ResourceDescriptor.TYPE_QUERY))
                {
                    JOptionPane.showMessageDialog(this,"The local query defined is not valid.\nPress the button \"Edit local resource\" to fix the problem.");
                    return;
                }
            }
            
            if (jRadioButtonRepo1.isSelected() && jTextFieldResource1.getText().trim().length() == 0)
            { 
                JOptionPane.showMessageDialog(this,"Please set a valid reference to a Query object in the Repository.");
                jTextFieldResource1.requestFocusInWindow();
                return;
            }
            
            if (jTextFieldValueColumn.getText().trim().length() == 0)
            {
                JOptionPane.showMessageDialog(this,"Please set the value column.");
                return;
            }
            
            if (jListColumns.getModel().getSize() == 0)
            {
                JOptionPane.showMessageDialog(this,"Please set at least one visible column in the visible columns list.");
                return;
            }
            
            rd.setQueryValueColumn( jTextFieldValueColumn.getText() );
            int count = jListColumns.getModel().getSize();
            String[] visibleColumns = new String[count];
            
            for (int i=0; i<count; ++i)
            {
                visibleColumns[i] = jListColumns.getModel().getElementAt(i) + "";
            }
            rd.setQueryVisibleColumns( visibleColumns );
            
        }
        
        
        
        try {
            
            // Validation and children population
            
            validate(rd);
            
            ResourceDescriptor rdChild = null;
            if (rd.getControlType() == rd.IC_TYPE_BOOLEAN)
            {
                // Ni children
            }
            else if (rd.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY ||
                    rd.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY_RADIO ||
                    rd.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY ||
                    rd.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY_CHECKBOX)
            {
                if (jRadioButtonRepo1.isSelected())
                {
                    // Query as reference resource
                    rdChild = new ResourceDescriptor();
                    rdChild.setWsType( ResourceDescriptor.TYPE_REFERENCE);
                    rdChild.setReferenceUri( jTextFieldResource1.getText().trim() );
                }
                else
                {
                    rdChild = queryResourceDescriptor;
                }
            }
            else if (rd.getControlType() == rd.IC_TYPE_SINGLE_VALUE)
            {
                if (jRadioButtonRepo.isSelected())
                {
                    rdChild = new ResourceDescriptor();
                    rdChild.setWsType( ResourceDescriptor.TYPE_REFERENCE);
                    rdChild.setReferenceUri( jTextFieldResource.getText().trim() );
                }
                else
                {
                    rdChild = dataTypeResourceDescriptor;
                }
            }
            else if (rd.getControlType() == rd.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES |
                     rd.getControlType() == rd.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES_RADIO ||
                     rd.getControlType() == rd.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES ||
                     rd.getControlType() == rd.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES_CHECKBOX)
            {
              if (jRadioButtonRepo.isSelected() )
                {
                    // LOV or DataType as reference resource
                    rdChild = new ResourceDescriptor();
                    rdChild.setWsType( ResourceDescriptor.TYPE_REFERENCE);
                    rdChild.setReferenceUri( jTextFieldResource.getText().trim() );
                }
                else // Local resource
                {
                    rdChild = lovResourceDescriptor;
                }
            }
            
            if (rdChild != null)
            {
                if (rd.getChildren() == null) rd.setChildren(new java.util.ArrayList());
                rd.getChildren().add(rdChild);
            }

            newResourceDescriptor = getServer().getWSClient().modifyReportUnitResource(getReportUnitUri(), rd, null);
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
        
        //System.out.println("Mandatory: " + descriptor.isMandatory() );
        //.out.println("ReadOnly: " + descriptor.isReadOnly() );
        //System.out.println("Visible: " + descriptor.getResourcePropertyValueAsBoolean(ResourceDescriptor.PROP_INPUTCONTROL_IS_VISIBLE) );

        Boolean b = descriptor.getResourcePropertyValueAsBoolean(ResourceDescriptor.PROP_INPUTCONTROL_IS_VISIBLE);
        jCheckBoxVisible.setSelected(b == null || b.booleanValue());

        jCheckBoxMandatory.setSelected( descriptor.isMandatory());
        jCheckBoxReadOnly.setSelected( descriptor.isReadOnly());
        
        Misc.setComboboxSelectedTagValue(jComboBoxType, ""+ descriptor.getControlType());

        if (descriptor.getQueryValueColumn() != null)
        {
            jTextFieldValueColumn.setText( descriptor.getQueryValueColumn() );
        }
        
        if (descriptor.getQueryVisibleColumns() != null)
        {
            String[] visibleColumns = descriptor.getQueryVisibleColumns();
            DefaultListModel dlm = (DefaultListModel)jListColumns.getModel();
            for (int i=0; i<visibleColumns.length; ++i)
            {
                dlm.addElement( visibleColumns[i] );
            }
        }
        
        if (descriptor.getChildren().size() > 0)
        {
            ResourceDescriptor rd = (ResourceDescriptor)descriptor.getChildren().get(0);

            if (rd.getWsType().equals(ResourceDescriptor.TYPE_REFERENCE))
            {
                // If the control is a query based ic, the reference refers to the query
                if (descriptor.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY ||
                    descriptor.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY_RADIO ||
                    descriptor.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY ||
                    descriptor.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY_CHECKBOX)
                {
                    jTextFieldResource1.setText( rd.getReferenceUri() );
                    jRadioButtonRepo1.setSelected(true);
                }
                else // otherwise to another resource...
                {
                    jTextFieldResource.setText( rd.getReferenceUri() );
                    jRadioButtonRepo.setSelected(true);
                }
            }
            else
            {
                jRadioButtonLocal.setSelected(true);
                if (rd.getWsType().equals(ResourceDescriptor.TYPE_DATA_TYPE))
                {
                    dataTypeResourceDescriptor = rd;
                }
                else if (rd.getWsType().equals(ResourceDescriptor.TYPE_LOV))
                {
                    lovResourceDescriptor = rd;
                } 
                else if (rd.getWsType().equals(ResourceDescriptor.TYPE_QUERY))
                {
                    queryResourceDescriptor = rd;
                } 
            }
            
            if (descriptor.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY ||
                descriptor.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY_RADIO ||
                descriptor.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY ||
                descriptor.getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY_CHECKBOX)
            {
                updateQueryResourceFromType();
            }
            else
            {
                updateResourceFromType();
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButtonAddColumn;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonEditLocalResource;
    private javax.swing.JButton jButtonEditLocalResource1;
    private javax.swing.JButton jButtonModColumn;
    private javax.swing.JButton jButtonPickResource;
    private javax.swing.JButton jButtonPickResource1;
    private javax.swing.JButton jButtonRemColumn;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBoxMandatory;
    private javax.swing.JCheckBox jCheckBoxReadOnly;
    private javax.swing.JCheckBox jCheckBoxVisible;
    private javax.swing.JComboBox jComboBoxType;
    private javax.swing.JEditorPane jEditorPaneDescription;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelLabel;
    private javax.swing.JLabel jLabelLocate;
    private javax.swing.JLabel jLabelLocate1;
    private javax.swing.JLabel jLabelLocate2;
    private javax.swing.JLabel jLabelLocate3;
    private javax.swing.JLabel jLabelMandatory;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelReadOnly;
    private javax.swing.JLabel jLabelType;
    private javax.swing.JLabel jLabelUriString;
    private javax.swing.JLabel jLabelVisible;
    private javax.swing.JList jListColumns;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelFields;
    private javax.swing.JPanel jPanelLocate;
    private javax.swing.JPanel jPanelLocateQuery;
    private javax.swing.JPanel jPanelQuery;
    private javax.swing.JPanel jPanelVisibleColumns;
    private javax.swing.JRadioButton jRadioButtonLocal;
    private javax.swing.JRadioButton jRadioButtonLocal1;
    private javax.swing.JRadioButton jRadioButtonRepo;
    private javax.swing.JRadioButton jRadioButtonRepo1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextFieldLabel;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldResource;
    private javax.swing.JTextField jTextFieldResource1;
    private javax.swing.JTextField jTextFieldUriString;
    private javax.swing.JTextField jTextFieldValueColumn;
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
    
    public void updateResourceFromType()
    {
        jTextFieldResource.setEnabled( jRadioButtonRepo.isSelected() );
        jButtonPickResource.setEnabled( jRadioButtonRepo.isSelected() );
        
        jButtonEditLocalResource.setEnabled( jRadioButtonLocal.isSelected() );
    }
    
    public void updateQueryResourceFromType()
    {
        jTextFieldResource1.setEnabled( jRadioButtonRepo1.isSelected() );
        jButtonPickResource1.setEnabled( jRadioButtonRepo1.isSelected() );
        
        jButtonEditLocalResource1.setEnabled( jRadioButtonLocal1.isSelected() );
    }

}
