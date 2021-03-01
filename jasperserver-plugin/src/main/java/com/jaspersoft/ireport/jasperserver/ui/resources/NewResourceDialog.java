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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.ConfigurablePlainDocument;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.ui.ValidationUtils;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;

import java.io.File;
import javax.swing.JOptionPane;
/**
 *
 * @author  gtoffoli
 */
public class NewResourceDialog extends javax.swing.JDialog {
    
    private int dialogResult = javax.swing.JOptionPane.CANCEL_OPTION;
    private String  resourceType = ResourceDescriptor.TYPE_IMAGE;
    private JServer server = null;
    private String reportUnitUri = null;
    private ResourceDescriptor newResourceDescriptor = null;
    
    
    private String parentUri;
    
    /** Creates new form ObjectPropertiesDialog */
    public NewResourceDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        
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

        //jComboBoxResourceType.addItem( new Tag(ResourceDescriptor.TYPE_FOLDER, "Folder"));
        jComboBoxResourceType.addItem( new Tag(ResourceDescriptor.TYPE_IMAGE, "Image"));
        jComboBoxResourceType.addItem( new Tag(ResourceDescriptor.TYPE_RESOURCE_BUNDLE, "Resource bundle"));
        jComboBoxResourceType.addItem( new Tag(ResourceDescriptor.TYPE_JRXML, "Jrxml"));
        jComboBoxResourceType.addItem( new Tag(ResourceDescriptor.TYPE_CLASS_JAR, "Jar"));
        jComboBoxResourceType.addItem( new Tag(ResourceDescriptor.TYPE_FONT, "Font"));
        jComboBoxResourceType.addItem( new Tag(ResourceDescriptor.TYPE_STYLE_TEMPLATE, "Style Template"));
        jComboBoxResourceType.addItem( new Tag(ResourceDescriptor.TYPE_XML_FILE, "xml"));
        
        applyI18n();
        jTextFieldName.requestFocusInWindow();
    }
    
    public void applyI18n()
    {
        jButtonClose.setText( JasperServerManager.getString("newResourceDialog.buttonCancel","Cancel"));
        jButtonSave.setText( JasperServerManager.getString("newResourceDialog.buttonSave","Save"));
        
        jLabel1.setText( JasperServerManager.getString("newResourceDialog.title","New folder/resource"));
        jLabelDescription.setText( JasperServerManager.getString("newResourceDialog.labelDescription","Description"));
        jLabelLabel.setText( JasperServerManager.getString("newResourceDialog.labelLabel","Label"));
        jLabelName.setText( JasperServerManager.getString("newResourceDialog.labelName","Name"));
        jLabelUriString.setText( JasperServerManager.getString("newResourceDialog.labelParentFolder","Parent folder"));
        
        jButton1.setText( JasperServerManager.getString("newResourceDialog.browse","Browse"));
        jLabelResourceFile.setText( JasperServerManager.getString("newResourceDialog.labelResourceFile","Resource file"));
        jLabelResourceType.setText( JasperServerManager.getString("newResourceDialog.labelResourceType","Resource type"));
        
    }
    
    public void updateSaveButton()
    {
        if (jTextFieldLabel.getText().length() > 0 &&
            jTextFieldName.getText().length() > 0 &&
            (!jComboBoxResourceType.isVisible() || jTextFieldFile.getText().length() > 0))
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

        jLabel1 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jLabelUriString = new javax.swing.JLabel();
        jTextFieldUriString = new javax.swing.JTextField();
        jLabelResourceType = new javax.swing.JLabel();
        jComboBoxResourceType = new javax.swing.JComboBox();
        jLabelName = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelLabel = new javax.swing.JLabel();
        jTextFieldLabel = new javax.swing.JTextField();
        jLabelDescription = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPaneDescription = new javax.swing.JEditorPane();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelResourceFile = new javax.swing.JLabel();
        jPanelFile = new javax.swing.JPanel();
        jTextFieldFile = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Import resource");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_new.png"))); // NOI18N
        jLabel1.setText("New folder/resource");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setMaximumSize(new java.awt.Dimension(126, 50));
        jLabel1.setOpaque(true);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jLabel1, gridBagConstraints);

        jSeparator4.setMinimumSize(new java.awt.Dimension(1, 2));
        jSeparator4.setPreferredSize(new java.awt.Dimension(1, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jSeparator4, gridBagConstraints);

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 200));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelUriString.setText("Parent Location");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jLabelUriString, gridBagConstraints);

        jTextFieldUriString.setEditable(false);
        jTextFieldUriString.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextFieldUriString.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jTextFieldUriString, gridBagConstraints);

        jLabelResourceType.setText("Resource type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel1.add(jLabelResourceType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel1.add(jComboBoxResourceType, gridBagConstraints);

        jLabelName.setText("ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel1.add(jLabelName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel1.add(jTextFieldName, gridBagConstraints);

        jSeparator1.setMinimumSize(new java.awt.Dimension(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        jPanel1.add(jSeparator1, gridBagConstraints);

        jLabelLabel.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel1.add(jLabelLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jTextFieldLabel, gridBagConstraints);

        jLabelDescription.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel1.add(jLabelDescription, gridBagConstraints);

        jScrollPane1.setViewportView(jEditorPaneDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jSeparator2.setMinimumSize(new java.awt.Dimension(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        jPanel1.add(jSeparator2, gridBagConstraints);

        jLabelResourceFile.setText("Resource file");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel1.add(jLabelResourceFile, gridBagConstraints);

        jPanelFile.setMinimumSize(new java.awt.Dimension(11, 19));
        jPanelFile.setPreferredSize(new java.awt.Dimension(11, 19));
        jPanelFile.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelFile.add(jTextFieldFile, gridBagConstraints);

        jButton1.setText("Browse");
        jButton1.setMinimumSize(new java.awt.Dimension(73, 19));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanelFile.add(jButton1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel1.add(jPanelFile, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jSeparator3.setMinimumSize(new java.awt.Dimension(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        getContentPane().add(jSeparator3, gridBagConstraints);

        jPanel2.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel2.setPreferredSize(new java.awt.Dimension(10, 30));
        jPanel2.setLayout(new java.awt.GridBagLayout());

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
        jPanel2.add(jButtonSave, gridBagConstraints);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonClose, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        getContentPane().add(jPanel2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String fileName = "";
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser(IReportManager.getInstance().getCurrentDirectory());

        jfc.setDialogTitle("Pick a file....");

        String resType = ""+((Tag)jComboBoxResourceType.getSelectedItem()).getValue();

        if (resType.equals( ResourceDescriptor.TYPE_JRXML))
        {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".xml") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "JasperReports XML *.xml, *.jrxml";
                }
            });
        }
        else if (resType.equals( ResourceDescriptor.TYPE_FONT))
        {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".ttf") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "TrueType font *.TTF";
                }
            });
        }
        else if (resType.equals( ResourceDescriptor.TYPE_RESOURCE_BUNDLE))
        {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".properties") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "ResourceBundle *.properties";
                }
            });
        }
        else if (resType.equals( ResourceDescriptor.TYPE_CLASS_JAR))
        {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".jar") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "Java Archive *.jar";
                }
            });
        }
        else if (resType.equals( ResourceDescriptor.TYPE_STYLE_TEMPLATE))
        {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".jrtx") || file.isDirectory() || filename.toLowerCase().endsWith(".jrtx")) ;
                }
                public String getDescription() {
                    return "Style Template *.jrtx";
                }
            });
        }
        else if (resType.equals( ResourceDescriptor.TYPE_XML_FILE))
        {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".xml") || file.isDirectory() || filename.toLowerCase().endsWith(".xml")) ;
                }
                public String getDescription() {
                    return "XML *.xml";
                }
            });
        }

        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
            
            jTextFieldFile.setText(  jfc.getSelectedFile()+"");
            updateSaveButton();
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

       jButtonCloseActionPerformed(null);
    }//GEN-LAST:event_formWindowClosing

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed

        if (jButtonSave.isEnabled())
        {
            if (JOptionPane.showConfirmDialog(this, 
                    JasperServerManager.getString("newResourceDialog.message.resourceChanged","The resource was changed.\n\nDo you want save the changes?")) == JOptionPane.OK_OPTION)
            {
                jButtonSaveActionPerformed(null);
                // If the save button is still enabled, the change was not executed properly.
                if (jButtonSave.isEnabled()) return;
            }
        }
        this.setVisible(false);
        this.dispose();        
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed

        
        ResourceDescriptor rd = new ResourceDescriptor();
        
        File resourceFile = null;
        if (!getResourceType().equals(ResourceDescriptor.TYPE_FOLDER))
        {
            rd.setWsType(""+((Tag)jComboBoxResourceType.getSelectedItem()).getValue());
            
            rd.setHasData(true);
            resourceFile = new File( jTextFieldFile.getText());
            
            if (!resourceFile.exists())
            {
                JOptionPane.showMessageDialog(this, 
                        JasperServerManager.getFormattedString("newResourceDialog.message.fileNotFound","{0}\n\nFile not found!",new Object[]{jTextFieldFile.getText()}));
                return;
            }
        
        }
        else
        {
            rd.setWsType(ResourceDescriptor.TYPE_FOLDER);
        }
        
        rd.setName( jTextFieldName.getText()  );
        
        String uri = getParentUri();
        if (!uri.endsWith("/")) uri = uri + "/";
        uri += jTextFieldName.getText();
        
        rd.setUriString( uri );
       
        rd.setDescription( jEditorPaneDescription.getText().trim() ); //getResource().getDescriptor().getDescription() 
        rd.setLabel(jTextFieldLabel.getText().trim() ); //getResource().getDescriptor().getLabel()  );
        rd.setParentFolder( getParentUri() );
        
        if (getReportUnitUri() != null && !getReportUnitUri().equals("/"))
        {
            rd.setParentFolder( getReportUnitUri() + "_files" );
        }
        rd.setIsNew(true);
        
        
        try {
            
            validate(rd);
            newResourceDescriptor = getServer().getWSClient().modifyReportUnitResource(getReportUnitUri(), rd, resourceFile);
            setDialogResult(JOptionPane.OK_OPTION);
            this.setVisible(false);
            this.dispose();
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this,JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {ex.getMessage()}));
            ex.printStackTrace();
            return;
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed
        
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
    
    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult){
        this.dialogResult = dialogResult;
    }
    
    public String getParentUri() {
        return parentUri;
    }

    public void setParentUri(String parentUri) {
        this.parentUri = parentUri;
        
        if (reportUnitUri != null && reportUnitUri.length() > 0)
        this.parentUri = reportUnitUri + "_files";
        
        jTextFieldUriString.setText( parentUri );
    }

    public JServer getServer() {
        return server;
    }

    public void setServer(JServer server) {
          
        this.server = server;
    }
    
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
        
        boolean newFolder = resourceType.equals(ResourceDescriptor.TYPE_FOLDER);
        jLabelResourceType.setVisible(!newFolder);
        jComboBoxResourceType.setVisible(!newFolder);
        jSeparator2.setVisible(!newFolder);
        jLabelResourceFile.setVisible(!newFolder);
        jPanelFile.setVisible(!newFolder);
        
        Misc.setComboboxSelectedTagValue(jComboBoxResourceType,resourceType);
    }

    public String getReportUnitUri() {
        return reportUnitUri;
    }

    public void setReportUnitUri(String reportUnitUri) {
        this.reportUnitUri = reportUnitUri;
        
        if (reportUnitUri != null && reportUnitUri.length() > 0)
        this.setParentUri( reportUnitUri + "_files" );
        
    }

    public ResourceDescriptor getNewResourceDescriptor() {
        return newResourceDescriptor;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JComboBox jComboBoxResourceType;
    private javax.swing.JEditorPane jEditorPaneDescription;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelLabel;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelResourceFile;
    private javax.swing.JLabel jLabelResourceType;
    private javax.swing.JLabel jLabelUriString;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelFile;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField jTextFieldFile;
    private javax.swing.JTextField jTextFieldLabel;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldUriString;
    // End of variables declaration//GEN-END:variables
    
}
