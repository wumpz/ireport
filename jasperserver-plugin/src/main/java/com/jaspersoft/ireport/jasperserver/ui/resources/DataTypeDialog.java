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
import com.jaspersoft.ireport.designer.utils.ConfigurablePlainDocument;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.math.BigDecimal;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author  gtoffoli
 */
public class DataTypeDialog extends javax.swing.JDialog {
    
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    
    private JServer server = null;
    private String parentFolder = null;
    private RepositoryFolder resource = null;
    
    private ResourceDescriptor newResourceDescriptor = null;
    
    private boolean doNotStore = false;
    
    /**
     * Creates new form DataSourceDialog
     */
    public DataTypeDialog(java.awt.Frame parent, boolean modal) {
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
        
        jComboBoxType.addItem( new Tag("1", JasperServerManager.getString("dataTypeDialog.type.1","Text") ));
        jComboBoxType.addItem( new Tag("2", JasperServerManager.getString("dataTypeDialog.type.2","Number")));
        jComboBoxType.addItem( new Tag("3", JasperServerManager.getString("dataTypeDialog.type.3","Date")));
        jComboBoxType.addItem( new Tag("4", JasperServerManager.getString("dataTypeDialog.type.4","Date/time")));       
        
        jComboBoxType.setSelectedIndex(0);
        
        applyI18n();
        
        this.jTextFieldName.setDocument(new ConfigurablePlainDocument(JasperServerManager.MAX_ID_LENGHT));
        this.jTextFieldLabel.setDocument(new ConfigurablePlainDocument(JasperServerManager.MAX_NAME_LENGHT));
        this.jEditorPaneDescription.setDocument(new ConfigurablePlainDocument(250));
        
        this.jTextFieldLabel.getDocument().addDocumentListener(changesListener);
        this.jTextFieldName.getDocument().addDocumentListener(changesListener);
        jTextFieldName.requestFocusInWindow();
    }
    
     public void applyI18n()
     {
        jButtonClose.setText( JasperServerManager.getString("dataTypeDialog.buttonCancel","Cancel"));
        jButtonSave.setText( JasperServerManager.getString("dataTypeDialog.buttonSave","Save"));
        jLabel1.setText( JasperServerManager.getString("dataTypeDialog.title","Data Type"));
        jLabelDescription.setText( JasperServerManager.getString("dataTypeDialog.labelDescription","Description"));
        jLabelLabel.setText( JasperServerManager.getString("dataTypeDialog.labelLabel","Label"));
        jLabelMaxVal.setText( JasperServerManager.getString("dataTypeDialog.labelMaxVal","Maximum value"));
        jLabelMinValue.setText( JasperServerManager.getString("dataTypeDialog.labelMinValue","Minimum value"));
        jLabelName.setText( JasperServerManager.getString("dataTypeDialog.labelName","Name"));
        jLabelUriString.setText( JasperServerManager.getString("dataTypeDialog.labelParentFolder","Parent folder"));
        jLabelPattern.setText( JasperServerManager.getString("dataTypeDialog.labelPattern","Pattern"));
        jLabelStrictMaxVal.setText( JasperServerManager.getString("dataTypeDialog.labelStrictMaxValue","Is strict maximum"));
        jLabelStrictMinVal.setText( JasperServerManager.getString("dataTypeDialog.labelStrictMinValue","Is strict minimum"));
        jLabelType.setText( JasperServerManager.getString("dataTypeDialog.labelType","Type"));
        
        jTabbedPane1.setTitleAt(0, JasperServerManager.getString("dataTypeDialog.tabGeneral","General") );
        jTabbedPane1.setTitleAt(1, JasperServerManager.getString("dataTypeDialog.tabDetails","Date Type details") );
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
        jLabelPattern = new javax.swing.JLabel();
        jTextFieldPattern = new javax.swing.JTextField();
        jLabelMinValue = new javax.swing.JLabel();
        jCheckBoxMinValue = new javax.swing.JCheckBox();
        jTextFieldMaxValue = new javax.swing.JTextField();
        jLabelMaxVal = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jCheckBoxMaxValue = new javax.swing.JCheckBox();
        jLabelStrictMinVal = new javax.swing.JLabel();
        jLabelStrictMaxVal = new javax.swing.JLabel();
        jTextFieldMinValue = new javax.swing.JTextField();
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
        jLabel1.setText("Data Type");
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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 20);
        jPanel3.add(jComboBoxType, gridBagConstraints);

        jLabelPattern.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelPattern.setText("Pattern");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 16, 0);
        jPanel3.add(jLabelPattern, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 16, 20);
        jPanel3.add(jTextFieldPattern, gridBagConstraints);

        jLabelMinValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelMinValue.setText("Minimum value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 4, 0);
        jPanel3.add(jLabelMinValue, gridBagConstraints);

        jCheckBoxMinValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 16, 20);
        jPanel3.add(jCheckBoxMinValue, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 20);
        jPanel3.add(jTextFieldMaxValue, gridBagConstraints);

        jLabelMaxVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelMaxVal.setText("Maximum value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 4, 0);
        jPanel3.add(jLabelMaxVal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 99;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel5, gridBagConstraints);

        jCheckBoxMaxValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 8, 20);
        jPanel3.add(jCheckBoxMaxValue, gridBagConstraints);

        jLabelStrictMinVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelStrictMinVal.setText("Is strict minimum");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 16, 0);
        jPanel3.add(jLabelStrictMinVal, gridBagConstraints);

        jLabelStrictMaxVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelStrictMaxVal.setText("Is strict maximum");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 8, 0);
        jPanel3.add(jLabelStrictMaxVal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 20);
        jPanel3.add(jTextFieldMinValue, gridBagConstraints);

        jTabbedPane1.addTab("Data Type details", jPanel3);

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
        
        rd.setWsType( ResourceDescriptor.TYPE_DATA_TYPE );

        // Put here resource details
        rd.setDataType(Byte.parseByte( ((Tag)jComboBoxType.getSelectedItem()).getValue()+"") );
        rd.setPattern( jTextFieldPattern.getText());
        rd.setMaxValue( jTextFieldMaxValue.getText() );
        rd.setMinValue( jTextFieldMinValue.getText() );
        rd.setStrictMax( jCheckBoxMaxValue.isSelected());
        rd.setStrictMin( jCheckBoxMinValue.isSelected());
        
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
        this.resource = resource;
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
        
        jTextFieldName.setText( descriptor.getName());
        setTitle( JasperServerManager.getFormattedString("properties.title", "{0} - Properties", new Object[]{descriptor.getName()}));

        
        jTextFieldLabel.setText(descriptor.getLabel());
        jEditorPaneDescription.setText( descriptor.getDescription());
                
        if (descriptor.getWsType().equals( ResourceDescriptor.TYPE_DATA_TYPE))
        {
            this.jCheckBoxMaxValue.setSelected( descriptor.isStrictMax());
            this.jCheckBoxMinValue.setSelected( descriptor.isStrictMin());
            this.jTextFieldMaxValue.setText( descriptor.getMaxValue() == null ? "" : descriptor.getMaxValue());
            this.jTextFieldMinValue.setText( descriptor.getMinValue() == null ? "" : descriptor.getMinValue());
        
            Misc.setComboboxSelectedTagValue(jComboBoxType, 
                    ""+descriptor.getDataType());
            this.jTextFieldPattern.setText( descriptor.getPattern());
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBoxMaxValue;
    private javax.swing.JCheckBox jCheckBoxMinValue;
    private javax.swing.JComboBox jComboBoxType;
    private javax.swing.JEditorPane jEditorPaneDescription;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelLabel;
    private javax.swing.JLabel jLabelMaxVal;
    private javax.swing.JLabel jLabelMinValue;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelPattern;
    private javax.swing.JLabel jLabelStrictMaxVal;
    private javax.swing.JLabel jLabelStrictMinVal;
    private javax.swing.JLabel jLabelType;
    private javax.swing.JLabel jLabelUriString;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextFieldLabel;
    private javax.swing.JTextField jTextFieldMaxValue;
    private javax.swing.JTextField jTextFieldMinValue;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldPattern;
    private javax.swing.JTextField jTextFieldUriString;
    // End of variables declaration//GEN-END:variables
    
    
   
    /**
     * This method valitates data. If something is wrong, an exception si thrown.
     * The validation code was arranged by the web UI
     */
    public void validate(ResourceDescriptor dataType) throws Exception 
    {
		String strMinValue = (String)jTextFieldMinValue.getText();
                String strMaxValue = (String)jTextFieldMaxValue.getText();
        
                ValidationUtils.validateName( dataType.getName() );
                ValidationUtils.validateLabel( dataType.getLabel() );
                ValidationUtils.validateDesc( dataType.getDescription() );
            
		if (strMinValue != null && strMinValue.length() == 0)
			dataType.setMinValue(null);
		if (strMaxValue != null && strMaxValue.length() == 0)
			dataType.setMaxValue(null);

		if (dataType.getDataType() == dataType.DT_TYPE_NUMBER) {
			BigDecimal minValue = null;
			BigDecimal maxValue = null;
			try {

				minValue = new BigDecimal(strMinValue);
			} catch(NumberFormatException e) {
				if (strMinValue.length() > 0)
					throw new Exception( JasperServerManager.getString("dataTypeDialog.message.invalidNumber","Invalid number") );
			}

			try {

				maxValue = new BigDecimal(strMaxValue);
			} catch(NumberFormatException e) {
				if (strMaxValue.length() > 0)
					throw new Exception(  JasperServerManager.getString("dataTypeDialog.message.invalidNumber","Invalid number") );
			}

			if (minValue != null && maxValue != null)
				if (minValue.compareTo(maxValue) >= 0)
					throw new Exception( JasperServerManager.getString("dataTypeDialog.message.invalidMinMax","Minimum value must be smaller than maximum value") );
		}

		if (dataType.getDataType() == dataType.DT_TYPE_TEXT && dataType.getPattern() != null && dataType.getPattern().trim().length() > 0) {
			if (
				strMinValue != null
				&& strMinValue.trim().length() > 0
				&& !Pattern.matches(dataType.getPattern(), strMinValue)
				)
			{
				throw new Exception(JasperServerManager.getString("dataTypeDialog.message.invalidMinPattern","Minimum value does not match pattern"));
			}
			if (
				strMaxValue != null
				&& strMaxValue.trim().length() > 0
				&& !Pattern.matches(dataType.getPattern(), strMaxValue)
				)
			{
				throw new Exception(JasperServerManager.getString("dataTypeDialog.message.invalidMaxPattern","Maximum value does not match pattern"));
			}
		}
	}

    public boolean isDoNotStore() {
        return doNotStore;
    }

    public void setDoNotStore(boolean doNotStore) {
        this.doNotStore = doNotStore;
    }
}
