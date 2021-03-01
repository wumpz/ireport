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
import com.jaspersoft.ireport.JrxmlDataObject;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.fonts.TTFFontsLoader;
import com.jaspersoft.ireport.designer.utils.ConfigurablePlainDocument;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFile;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.Image;
import java.io.*;
import javax.swing.JOptionPane;
/**
 *
 * @author  gtoffoli
 */
public class ObjectPropertiesDialog extends javax.swing.JDialog {
    
    
    private RepositoryFolder resource;
    private int dialogResult = javax.swing.JOptionPane.CANCEL_OPTION;
    private AntialiasedEditorPane jEditorPane1 = null;
    
    /** Creates new form ObjectPropertiesDialog */
    public ObjectPropertiesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        this.jTextFieldName.setDocument(new ConfigurablePlainDocument(JasperServerManager.MAX_ID_LENGHT));
        this.jTextFieldLabel.setDocument(new ConfigurablePlainDocument(JasperServerManager.MAX_NAME_LENGHT));
        this.jEditorPaneDescription.setDocument(new ConfigurablePlainDocument(250));
        
        this.setLocationRelativeTo(null);
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
        
        this.jEditorPaneDescription.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
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
        
        
        jEditorPane1 = new AntialiasedEditorPane();
        jEditorPane1.setContentType("text/html");
        jEditorPane1.setVisible(false);
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        jPanelResourceFile.add(jEditorPane1, gridBagConstraints);
        jEditorPane1.setEditable(false);
        
        applyI18n();
        jPanelSpacer.setVisible(false);
        JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
        jButtonCurrentReport.setEnabled(view != null); //MainFrame.getMainInstance().getActiveReportFrame() != null);
        
    }
    
    public void applyI18n()
    {
        jButtonClose.setText( JasperServerManager.getString("objectPropertiesDialog.buttonCancel","Cancel"));
        jButtonSave.setText( JasperServerManager.getString("objectPropertiesDialog.buttonSave","Save"));
        jButtonCurrentReport.setText( JasperServerManager.getString("reportUnitDialog.buttonGetCurrentReport", "Current Report"));
        
        jLabelDescription.setText( JasperServerManager.getString("objectPropertiesDialog.labelDescription","Description"));
        jLabelLabel.setText( JasperServerManager.getString("objectPropertiesDialog.labelLabel","Label"));
        jLabelName.setText( JasperServerManager.getString("objectPropertiesDialog.labelName","Name"));
        jLabelUriString.setText( JasperServerManager.getString("objectPropertiesDialog.labelParentFolder","Parent folder"));
        jButton1.setText( JasperServerManager.getString("objectPropertiesDialog.buttonBrowse","Browse"));
        jButton2.setText( JasperServerManager.getString("objectPropertiesDialog.buttonExportFile","Export file"));
        jCheckBoxChangeFile.setText( JasperServerManager.getString("objectPropertiesDialog.checkBoxChangeFile","Replace resource with this file:"));
        jTabbedPane1.setTitleAt(0, JasperServerManager.getString("objectPropertiesDialog.tabGeneral","General"));
        jTabbedPane1.setTitleAt(1, JasperServerManager.getString("objectPropertiesDialog.tabResource","Resource"));
        jTabbedPane1.setTitleAt(2, JasperServerManager.getString("objectPropertiesDialog.tabDescriptor","XML descriptor"));
    }
    
    
    public void updateSaveButton()
    {
        if (jTextFieldLabel.getText().length() > 0)
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabelUriString = new javax.swing.JLabel();
        jTextFieldUriString = new javax.swing.JTextField();
        jLabelName = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelLabel = new javax.swing.JLabel();
        jTextFieldLabel = new javax.swing.JTextField();
        jLabelDescription = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPaneDescription = new javax.swing.JEditorPane();
        jPanelResourceFile = new javax.swing.JPanel();
        jLabelPreview = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jCheckBoxChangeFile = new javax.swing.JCheckBox();
        jTextFieldFile = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButtonCurrentReport = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jPanelSpacer = new javax.swing.JPanel();
        jPanelDescriptor = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPaneDescriptor = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(350, 250));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelUriString.setText("Location (URI)"); // NOI18N
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

        jLabelName.setText("ID"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel1.add(jLabelName, gridBagConstraints);

        jTextFieldName.setEditable(false);
        jTextFieldName.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextFieldName.setOpaque(false);
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

        jLabelLabel.setText("Name"); // NOI18N
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

        jLabelDescription.setText("Description"); // NOI18N
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

        jTabbedPane1.addTab("General", jPanel1);

        jPanelResourceFile.setLayout(new java.awt.GridBagLayout());

        jLabelPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPreview.setText("  "); // NOI18N
        jLabelPreview.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabelPreview.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelPreview.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        jPanelResourceFile.add(jLabelPreview, gridBagConstraints);

        jSeparator3.setMinimumSize(new java.awt.Dimension(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        jPanelResourceFile.add(jSeparator3, gridBagConstraints);

        jCheckBoxChangeFile.setText("Replace resource with this file:"); // NOI18N
        jCheckBoxChangeFile.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxChangeFile.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxChangeFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxChangeFileActionPerformed(evt);
            }
        });
        jCheckBoxChangeFile.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxChangeFileStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 0);
        jPanelResourceFile.add(jCheckBoxChangeFile, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 24, 0, 0);
        jPanelResourceFile.add(jTextFieldFile, gridBagConstraints);

        jButton1.setText("Browse"); // NOI18N
        jButton1.setMinimumSize(new java.awt.Dimension(73, 19));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanelResourceFile.add(jButton1, gridBagConstraints);

        jButtonCurrentReport.setText("Current Report"); // NOI18N
        jButtonCurrentReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCurrentReportActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 24, 0, 0);
        jPanelResourceFile.add(jButtonCurrentReport, gridBagConstraints);

        jSeparator4.setMinimumSize(new java.awt.Dimension(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        jPanelResourceFile.add(jSeparator4, gridBagConstraints);

        jButton2.setText("Export file"); // NOI18N
        jButton2.setMinimumSize(new java.awt.Dimension(73, 23));
        jButton2.setPreferredSize(new java.awt.Dimension(100, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelResourceFile.add(jButton2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanelResourceFile.add(jPanelSpacer, gridBagConstraints);

        jTabbedPane1.addTab("Resource", jPanelResourceFile);

        jPanelDescriptor.setLayout(new java.awt.GridBagLayout());

        jTextPaneDescriptor.setEditable(false);
        jScrollPane2.setViewportView(jTextPaneDescriptor);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelDescriptor.add(jScrollPane2, gridBagConstraints);

        jTabbedPane1.addTab("Descriptor", jPanelDescriptor);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        jPanel2.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel2.setPreferredSize(new java.awt.Dimension(10, 30));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButtonSave.setText("Save"); // NOI18N
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

        jButtonClose.setText("Close"); // NOI18N
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

    private void jButton1ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed1

        String fileName = "";
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser(IReportManager.getInstance().getCurrentDirectory());
        
        jfc.setDialogTitle(JasperServerManager.getString("objectPropertiesDialog.pickAFile","Pick a file...."));
        
        String resType = this.getResource().getDescriptor().getWsType(); // Select a resource type...
        
        if (resType.equals( ResourceDescriptor.TYPE_JRXML)) {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".xml") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "JasperReports XML *.xml, *.jrxml";
                }
            });
        } else if (resType.equals( ResourceDescriptor.TYPE_FONT)) {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".ttf") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "TrueType font *.TTF";
                }
            });
        } else if (resType.equals( ResourceDescriptor.TYPE_RESOURCE_BUNDLE)) {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".properties") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "ResourceBundle *.properties";
                }
            });
        } else if (resType.equals( ResourceDescriptor.TYPE_CLASS_JAR)) {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".jar") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "Java Archive *.jar";
                }
            });
        } else if (resType.equals( ResourceDescriptor.TYPE_STYLE_TEMPLATE)) {
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
        
        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG);
        if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
            
            try {
                copyFile( new File( ((RepositoryFile)getResource()).getFile() ),
                      jfc.getSelectedFile() );
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this,JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {ex.getMessage()}));
                ex.printStackTrace();
                return;
            }
        }
        
        
    }//GEN-LAST:event_jButton1ActionPerformed1

    private void jCheckBoxChangeFileStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxChangeFileStateChanged
        
    }//GEN-LAST:event_jCheckBoxChangeFileStateChanged

    private void jCheckBoxChangeFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxChangeFileActionPerformed
        updateSaveButton();
    }//GEN-LAST:event_jCheckBoxChangeFileActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String fileName = "";
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser(IReportManager.getInstance().getCurrentDirectory());
        
        jfc.setDialogTitle("Pick a file....");
        
        String resType = this.getResource().getDescriptor().getWsType(); // Select a resource type...
        
        if (resType.equals( ResourceDescriptor.TYPE_JRXML)) {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".xml") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "JasperReports XML *.xml, *.jrxml";
                }
            });
        } else if (resType.equals( ResourceDescriptor.TYPE_FONT)) {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".ttf") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "TrueType font *.TTF";
                }
            });
        } else if (resType.equals( ResourceDescriptor.TYPE_RESOURCE_BUNDLE)) {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".properties") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "ResourceBundle *.properties";
                }
            });
        } else if (resType.equals( ResourceDescriptor.TYPE_CLASS_JAR)) {
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File file) {
                    String filename = file.getName();
                    return (filename.toLowerCase().endsWith(".jar") || file.isDirectory() || filename.toLowerCase().endsWith(".jrxml")) ;
                }
                public String getDescription() {
                    return "Java Archive *.jar";
                }
            });
        } else if (resType.equals( ResourceDescriptor.TYPE_STYLE_TEMPLATE)) {
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
        
        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
            
            jTextFieldFile.setText(  jfc.getSelectedFile()+"");
            updateSaveButton();
            
            if (getResource().getDescriptor().getWsType().equals(ResourceDescriptor.TYPE_IMAGE))
            {
                try {
                    javax.swing.ImageIcon ii = new javax.swing.ImageIcon(jfc.getSelectedFile()+"");
                    jLabelPreview.setText(ii.getIconWidth() + "x" + ii.getIconHeight());
                    this.doLayout();
                    ii.setImage( resampleImage( ii.getImage(),jLabelPreview.getWidth(), (int)Math.max(1,jLabelPreview.getHeight()-20)));
                    jLabelPreview.setIcon( ii );
                    
                } catch (Exception ex)
                {
                    jLabelPreview.setText(" ");
                    jLabelPreview.setIcon(null);
                }
            }
        }
        
        jCheckBoxChangeFile.setSelected(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

       jButtonCloseActionPerformed(null);
    }//GEN-LAST:event_formWindowClosing

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed

        if (jButtonSave.isEnabled())
        {
            if (JOptionPane.showConfirmDialog(this, JasperServerManager.getString("objectPropertiesDialog.message.resourceChanged","The resource was changed.\n\nDo you want save the changes?")) == JOptionPane.OK_OPTION)
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

        File newFile = null;
        if (jCheckBoxChangeFile.isSelected())
        {
            newFile = new File(jTextFieldFile.getText());
            if (!newFile.exists())
            {
                JOptionPane.showMessageDialog(this,
                        JasperServerManager.getFormattedString("objectPropertiesDialog.message.fileNotFound","The file {0}\ndoes not exist.\n\nPlease provide a correct file name.", new Object[] {jTextFieldFile.getText()}));
                return;
            }
        }
        
        ResourceDescriptor rd = new ResourceDescriptor();
        rd.setWsType( getResource().getDescriptor().getWsType()  );
        rd.setName( getResource().getDescriptor().getName()  );
        rd.setUriString( getResource().getDescriptor().getUriString()  );
        rd.setCreationDate( getResource().getDescriptor().getCreationDate()  );
        rd.setConnectionUrl( getResource().getDescriptor().getConnectionUrl()  );
        rd.setDataSourceType( getResource().getDescriptor().getDataSourceType()  );
        rd.setDescription( jEditorPaneDescription.getText().trim() ); //getResource().getDescriptor().getDescription() 
        rd.setDriverClass( getResource().getDescriptor().getDriverClass()  );
        rd.setHasData( newFile != null); //getResource().getDescriptor().getHasData()  );
        rd.setIsNew(false); // getResource().getDescriptor().getIsNew()  );
        rd.setIsReference( getResource().getDescriptor().getIsReference()  );
        rd.setJndiName( getResource().getDescriptor().getJndiName());
        rd.setLabel(jTextFieldLabel.getText().trim() ); //getResource().getDescriptor().getLabel()  );
        rd.setParentFolder( getResource().getDescriptor().getParentFolder());
        rd.setPassword( getResource().getDescriptor().getPassword());
        rd.setReferenceUri( getResource().getDescriptor().getReferenceUri());
        rd.setResourceType( getResource().getDescriptor().getResourceType()  );
        rd.setUsername( getResource().getDescriptor().getUsername());
        rd.setVersion( getResource().getDescriptor().getVersion());
        
        try {
            
            validate(rd);
            ResourceDescriptor newrd = getResource().getServer().getWSClient().addOrModifyResource(rd, newFile);
            setDialogResult(JOptionPane.OK_OPTION);
            jButtonSave.setEnabled(false);
            getResource().setDescriptor( newrd );
            
            if (jCheckBoxChangeFile.isSelected())
            {
                if (getResource() instanceof RepositoryFile)
                {
                    RepositoryFile fileRep = (RepositoryFile)getResource();
                    if (getResource().getDescriptor().getWsType().equals(ResourceDescriptor.TYPE_IMAGE))
                    {
                        try {

                            String s = fileRep.getFile();
                            fileRep.resetFileCache();
                            copyFile(new File(s), newFile); 
                                    
                            // TODO: change all reports that are referring this image...
                            
                            /*
                            RepositoryExplorer rexplorer = IRPlugin.getMainInstance().getRepositoryExplorer();
                            // 1. Look for report units...
                            List list = rexplorer.getOpenedReportSources(getResource().getServer());
                            for (int i=0; i<list.size(); ++i)
                            {
                                RepositoryJrxmlFile jrxmlRepo = (RepositoryJrxmlFile)list.get(i);
                                
                                JReportFrame jrf = jrxmlRepo.getReportFrame();
                                boolean updateFrame = false;
                                if (jrf != null)
                                {
                                    System.out.println(jrf);
                                    Vector elements = jrf.getReport().getElements();
                                    Enumeration enumElements = elements.elements();
                                    while (enumElements.hasMoreElements())
                                    {
                                        ReportElement re = (ReportElement)enumElements.nextElement();
                                        if (re instanceof ImageReportElement)
                                        {
                                            ImageReportElement ire = (ImageReportElement)re;
                                            String expression =ire.getImageExpression();
                                            expression = Misc.string_replace("\\","\\\\",expression);
                                            expression = Misc.string_replace("","\"",expression);

                                            if ((ire.getImageClass().length() == 0 || ire.getImageClass().equals("java.lang.String")) &&
                                                expression.toLowerCase().startsWith("repo:") && expression.substring(5).equals(newrd.getUriString() ))
                                            {
                                                    ire.setImg(new ImageIcon(s).getImage());
                                                    System.out.println(newrd.getUriString() + " --> " + expression.substring(5)+" " + s);
                                                    
                                                    updateFrame = true;
                                            }
                                        }
                                    }
                                    if (updateFrame)
                                    {
                                        jrf.getReportPanel().repaint();
                                    }
                                }
                            }
                            */
                        } catch (Exception ex)
                        {
                            
                        }
                    }
                    else
                    {
                        fileRep.resetFileCache();
                    }
                }
                
            }
            
       } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this,JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {ex.getMessage()}));
            ex.printStackTrace();
            return;
        }
        
        
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCurrentReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCurrentReportActionPerformed
        
        JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
        if (view != null && view.getLookup() != null)
        {
            JrxmlDataObject dobject = view.getLookup().lookup(JrxmlDataObject.class);
            if (dobject != null)
            {
                jCheckBoxChangeFile.setSelected(true);
                jTextFieldFile.setText( dobject.getPrimaryFile().getPath() );
            }
        }
        updateSaveButton();
    }//GEN-LAST:event_jButtonCurrentReportActionPerformed
        
    public void setResource(RepositoryFolder resource)
    {
        this.resource = resource;
        ResourceDescriptor resourceDescriptor = resource.getDescriptor();

        setTitle( JasperServerManager.getFormattedString("properties.title", "{0} - Properties", new Object[]{resourceDescriptor.getName()}));

        this.jTextFieldName.setText( resourceDescriptor.getName() );
        this.jTextFieldLabel.setText( resourceDescriptor.getLabel() );
        this.jTextFieldUriString.setText( resourceDescriptor.getUriString() );
        this.jEditorPaneDescription.setText( resourceDescriptor.getDescription() );
        
        jCheckBoxChangeFile.setSelected(false);
        
        jTabbedPane1.removeAll();
        
        jTabbedPane1.add("General", jPanel1);
        jTabbedPane1.setTitleAt(0, JasperServerManager.getString("objectPropertiesDialog.tabGeneral","General"));
        
        
        if (resourceDescriptor.getWsType().equals(resourceDescriptor.TYPE_IMAGE) ||
            resourceDescriptor.getWsType().equals(resourceDescriptor.TYPE_JRXML) ||
            resourceDescriptor.getWsType().equals(resourceDescriptor.TYPE_CLASS_JAR) ||
            resourceDescriptor.getWsType().equals(resourceDescriptor.TYPE_FONT) ||
            resourceDescriptor.getWsType().equals(resourceDescriptor.TYPE_RESOURCE_BUNDLE) ||
            resourceDescriptor.getWsType().equals(resourceDescriptor.TYPE_STYLE_TEMPLATE))
        {
            jTabbedPane1.add("Resource", jPanelResourceFile);
            jTabbedPane1.setTitleAt(1, JasperServerManager.getString("objectPropertiesDialog.tabResource","Resource"));
        }

//      Show the XML for debug porpuses.
      jTabbedPane1.add("Descriptor", jPanelDescriptor);
//      jTabbedPane1.setTitleAt(1, IRPlugin.getString("objectPropertiesDialog.tabDescriptor","Descriptor"));
//
      com.jaspersoft.jasperserver.ws.xml.Marshaller marshaller = new com.jaspersoft.jasperserver.ws.xml.Marshaller();
      jTextPaneDescriptor.setText( marshaller.writeResourceDescriptor( resourceDescriptor )  );
        
        if (getResource().getDescriptor().getWsType().equals(ResourceDescriptor.TYPE_IMAGE))
        {
            try {
                if (getResource() instanceof RepositoryFile)
                {
                    RepositoryFile res = (RepositoryFile)getResource();
                    String s = res.getFile();
                    javax.swing.ImageIcon ii = new javax.swing.ImageIcon(s);
                    jLabelPreview.setText(ii.getIconWidth() + "x" + ii.getIconHeight());
                    ii.setImage( resampleImage( ii.getImage(),300, 115));
                    jLabelPreview.setIcon( ii );
                }
            } catch (Exception ex)
            {
                jLabelPreview.setText(" ");
                jLabelPreview.setIcon(null);
            }
        }
        else if (getResource().getDescriptor().getWsType().equals(ResourceDescriptor.TYPE_FONT))
        {
            try {
                if (getResource() instanceof RepositoryFile)
                {
                    RepositoryFile res = (RepositoryFile)getResource();
                    String s = res.getFile();
 
                    java.awt.Font f = TTFFontsLoader.loadFont(s);
                    if (f != null)
                    {
                        jEditorPane1.setVisible(true);
                        jLabelPreview.setVisible(false);
                        //java.awt.Font f2 = new java.awt.Font(f.getName(), 0,20);
                        String text = "";
                        for (int dim=1; dim<=8; dim++)
                        {
                            text += "<font face=\""+ f.getName() + "\" size=\""+ dim + "\">" +
                                    JasperServerManager.getString("objectPropertiesDialog.sampleFontText","Nel mezzo del cammin di nostra vita...") + 
                                    "<br></font>";
                        }
                        
                        jEditorPane1.setText( text );
                        //jLabelPreview.setFont(f2);
                        //jLabelPreview.setText("Nel mezzo del cammin di nostra vita...\n123456");
                        //jLabelPreview.setIcon( null );
                    }
                }
            } catch (Exception ex)
            {
                jLabelPreview.setText(" ");
                jLabelPreview.setIcon(null);
            }
        }
        else
        {
           jEditorPane1.setVisible(false);
           jLabelPreview.setVisible(false);
           jSeparator3.setVisible(false);
           jPanelSpacer.setVisible(true);
        }
    
        
        jButtonSave.setEnabled(false);
    }

    public RepositoryFolder getResource() {
        return resource;
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    public Image resampleImage(Image inImage, int maxX, int maxY)
    {
        double scale1 = (double) maxY / (double) inImage.getHeight(null);
        double scale2 = (double) maxX / (double) inImage.getWidth(null);
        double scale = (scale1 < scale2) ? scale1 : scale2;
        
        int scaledW = (int) (scale * inImage.getWidth(null));
	int scaledH = (int) (scale * inImage.getHeight(null));
        return inImage.getScaledInstance(scaledW , scaledH, Image.SCALE_SMOOTH);

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
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonCurrentReport;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBoxChangeFile;
    private javax.swing.JEditorPane jEditorPaneDescription;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelLabel;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelPreview;
    private javax.swing.JLabel jLabelUriString;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelDescriptor;
    private javax.swing.JPanel jPanelResourceFile;
    private javax.swing.JPanel jPanelSpacer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextFieldFile;
    private javax.swing.JTextField jTextFieldLabel;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldUriString;
    private javax.swing.JTextPane jTextPaneDescriptor;
    // End of variables declaration//GEN-END:variables
    
    
    public void copyFile(File in, File out) throws IOException
    {
        
        FileInputStream is = new FileInputStream(in);
        FileOutputStream os = new FileOutputStream(out);
        byte[] buffer = new byte[1024];
        int bCount = 0;
        while ( (bCount = is.read(buffer)) > 0)
        {
            os.write( buffer, 0, bCount);
        }
        is.close();
        os.close();
    }
}
