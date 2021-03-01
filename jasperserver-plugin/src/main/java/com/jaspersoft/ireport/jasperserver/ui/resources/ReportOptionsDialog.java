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

import com.jaspersoft.ireport.designer.utils.ConfigurablePlainDocument;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.ResourceUtils;
import com.jaspersoft.ireport.jasperserver.ui.ValidationUtils;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author  gtoffoli
 */
public class ReportOptionsDialog extends javax.swing.JDialog {
    
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    
    private JServer server = null;
    private String parentFolder = null;
    private RepositoryFolder resource = null;
    
    private ResourceDescriptor newResourceDescriptor = null;
    
    private boolean doNotStore = false;
    
    /**
     * Creates new form DataSourceDialog
     */
    public ReportOptionsDialog(java.awt.Frame parent, boolean modal) {
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
        
        DefaultTableModel lm = (DefaultTableModel)jTableLOV.getModel();
        lm.setRowCount(0);
        jTableLOV.updateUI();
        
        this.jTextFieldName.setDocument(new ConfigurablePlainDocument(JasperServerManager.MAX_ID_LENGHT));
        this.jTextFieldLabel.setDocument(new ConfigurablePlainDocument(JasperServerManager.MAX_NAME_LENGHT));
        this.jEditorPaneDescription.setDocument(new ConfigurablePlainDocument(250));
        
        this.jTextFieldLabel.getDocument().addDocumentListener(changesListener);
        this.jTextFieldName.getDocument().addDocumentListener(changesListener);
        jTextFieldName.requestFocusInWindow();

        jTableLOV.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jTableValueChanged(evt);
            }
        });
        
        jTableValueChanged(null);
        applyI18n();
    
    }
    
    public void applyI18n()
    {
        jButtonClose.setText( JasperServerManager.getString("listOfValuesDialog.buttonCancel","Cancel"));
        jButtonSave.setText( JasperServerManager.getString("listOfValuesDialog.buttonSave","Save"));
        jLabel1.setText( JasperServerManager.getString("reportOptionsDialog.title","Report options"));
        jLabelDescription.setText( JasperServerManager.getString("listOfValuesDialog.labelDescription","Description"));
        jLabelLabel.setText( JasperServerManager.getString("listOfValuesDialog.labelLabel","Label"));
        jLabelName.setText( JasperServerManager.getString("listOfValuesDialog.labelName","Name"));
        jLabelUriString.setText( JasperServerManager.getString("listOfValuesDialog.labelParentFolder","Parent folder"));
        
        jLabelReportUnitURI.setText( JasperServerManager.getString("reportOptionsDialog.labelReportUnitURI","Report Unit URI"));
                
        jTabbedPane1.setTitleAt(0, JasperServerManager.getString("listOfValuesDialog.tabGeneral","General") );
        jTabbedPane1.setTitleAt(1, JasperServerManager.getString("listOfValuesDialog.tabReportOptions","Report options") );
        
        jTableLOV.getColumn("Label").setHeaderValue( JasperServerManager.getString("reportOptionsDialog.tableColumn.parameter","Parameter") ); 
        jTableLOV.getColumn("Value").setHeaderValue( JasperServerManager.getString("reportOptionsDialog.tableColumn.value","Value") ); 
        
        jTableLOV.updateUI();
    }
    
    private void jTableValueChanged(javax.swing.event.ListSelectionEvent evt) {                                    
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
        jPanel5 = new javax.swing.JPanel();
        jLabelReportUnitURI = new javax.swing.JLabel();
        jTextFieldReportUnitURI = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableLOV = new javax.swing.JTable();
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
        jLabel1.setText("List of values");
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

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabelReportUnitURI.setText("Report unit URI:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel5.add(jLabelReportUnitURI, gridBagConstraints);

        jTextFieldReportUnitURI.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jTextFieldReportUnitURI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel3.add(jPanel5, gridBagConstraints);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(200, 200));

        jTableLOV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Label", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableLOV);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel3.add(jScrollPane2, gridBagConstraints);

        jTabbedPane1.addTab("Report options", jPanel3);

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
        
        
        // Clone the current referenced descriptor...
        
        
        
        ResourceDescriptor rd = ResourceUtils.cloneResourceDescriptor( resource.getDescriptor());
        
        rd.setDescription( jEditorPaneDescription.getText().trim() ); //getResource().getDescriptor().getDescription()
        rd.setName( jTextFieldName.getText()  );
        rd.setLabel(jTextFieldLabel.getText().trim() ); //getResource().getDescriptor().getLabel()  );
        
        try {
            
            validate(rd);
            newResourceDescriptor = getServer().getWSClient().addOrModifyResource(rd, null);
            
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
                

        // set the report unit uri....
        
        jTextFieldReportUnitURI.setText( descriptor.getResourcePropertyValue("PROP_RU_URI") );
        
        ResourceProperty rp = descriptor.getResourceProperty("PROP_VALUES");
        
        List list = rp.getProperties();
        if (list != null)
        {
            DefaultTableModel dtm = (DefaultTableModel)jTableLOV.getModel();
            for (int i=0; i<list.size(); ++i)
            {
               ResourceProperty li = (ResourceProperty)list.get(i);
               String value = (li.getValue() == null) ? "" : li.getValue();
               if (li.getProperties().size() > 0) // it is a list....
               {
                    for (int k=0; k<li.getProperties().size(); ++k)
                    {
                        ResourceProperty sli = (ResourceProperty)li.getProperties().get(k);
                        dtm.addRow(new Object[]{ li.getName() + " [" + sli.getName() +"]", (sli.getValue() == null) ? "" : sli.getValue()});
                    }
               }
               else
               {
                dtm.addRow(new Object[]{ li.getName(), value});
               }
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JEditorPane jEditorPaneDescription;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelLabel;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelReportUnitURI;
    private javax.swing.JLabel jLabelUriString;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableLOV;
    private javax.swing.JTextField jTextFieldLabel;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldReportUnitURI;
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
}
