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
package com.jaspersoft.ireport.jasperserver.validation;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.compatibility.JRXmlWriterHelper;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.ireport.jasperserver.ui.ResourceChooser;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 *
 * @author  gtoffoli
 */
public class JrxmlValidationDialog extends javax.swing.JDialog {
    
    private JServer server = null;
    private RepositoryReportUnit reportUnit = null;
    private java.util.List elementVelidationItems = null;
    private JasperDesign report = null;
    private String fileName = null;
    private JrxmlVisualView view = null;
    
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    
    /** Creates new form JrxmlValidationDialog */
    public JrxmlValidationDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        // 450...
        TableColumn dtcm = jTable1.getColumnModel().getColumn(0);
        dtcm.setMinWidth(22);
        dtcm.setMaxWidth(22);
        dtcm.setPreferredWidth(20);
        dtcm.setResizable(false);
        jTable1.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                updateContinue();
            }
        } );
       

        dtcm = jTable1.getColumnModel().getColumn(1);
        dtcm.setMinWidth(100);
        dtcm.setPreferredWidth(100);
        dtcm.setCellRenderer(new AlignedTableCellRenderer(JLabel.LEFT) );
        
        dtcm = jTable1.getColumnModel().getColumn(2);
        dtcm.setMinWidth(100);
        dtcm.setPreferredWidth(300);
        
        dtcm = jTable1.getColumnModel().getColumn(3);
        dtcm.setMinWidth(100);
        dtcm.setPreferredWidth(300);
        
        
        dtcm = jTable1.getColumnModel().getColumn(4);
        dtcm.setMinWidth(50);
        dtcm.setMaxWidth(100);
        dtcm.setPreferredWidth(80);
        dtcm.setCellRenderer(new AlignedTableCellRenderer(JLabel.RIGHT));
        
        applyI18n();
        this.pack();
        setLocationRelativeTo(null);
    }
    
    
    public void applyI18n()
    {
        jButtonCancel.setText( JasperServerManager.getString("jrxmlValidationDialog.buttonCancel","Cancel"));
        jButtonDeselectAll.setText( JasperServerManager.getString("jrxmlValidationDialog.buttonDeselectAll","Deselect all"));
        jButtonOk.setText( JasperServerManager.getString("jrxmlValidationDialog.buttonOk","Continue"));
        jButtonSelectAll.setText( JasperServerManager.getString("jrxmlValidationDialog.buttonSelectAll","Select all"));
        jButtonSkip.setText( JasperServerManager.getString("jrxmlValidationDialog.buttonSkip","Skip this step"));
        jCheckBoxDoNotShowAgain.setText( JasperServerManager.getString("jrxmlValidationDialog.checkDoNotShowAgain","Do not show this window again"));
    
        jLabel1.setText(JasperServerManager.getString("jrxmlValidationDialog.message","<html>JasperServer Plugin has detected some locally referenced resources in your report.<br>\n"+
                            "You can choose to attach these images to the Report Unit importing them into the repository "+
                            "and replace the relative image expressions with an url like \"repo:myImage.jpg\".<br>" +
                            "<b>Please check the images you want attach to this Report Unit</b>.</html>"));
        
        try {
        jTable1.getColumn("Image").setHeaderValue( JasperServerManager.getString("jrxmlValidationDialog.table.image","Image") ); 
        jTable1.getColumn("Local file").setHeaderValue( JasperServerManager.getString("jrxmlValidationDialog.table.localFile","Local file") ); 
        jTable1.getColumn("Proposed exp").setHeaderValue( JasperServerManager.getString("jrxmlValidationDialog.table.proposedExpression","Proposed exp") );
        jTable1.getColumn("File size").setHeaderValue( JasperServerManager.getString("jrxmlValidationDialog.table.fileSize","File size") );
        } catch (Exception ex) {}
        jTable1.updateUI();
    }

    public void setVisualView(JrxmlVisualView view) {
        this.view = view;
    }

    
    
    public void updateContinue()
    {
        DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
        for (int i=0; i< dtm.getRowCount(); ++i)
        {
            if ( ((Boolean)jTable1.getValueAt(i,0)).booleanValue())
            {
                jButtonOk.setEnabled(true);
                return;
            }
        }
        jButtonOk.setEnabled(false);
    }
    
    public void setElementVelidationItems(java.util.List items)
    {
        DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
        dtm.setRowCount(0);
        
        NumberFormat formatter = new DecimalFormat("#,##0");
        for (int i=0; i<items.size(); ++i)
        {
            ElementValidationItem iev = (ElementValidationItem)items.get(i);
            String filesize = "";
            if (iev.getOriginalFileName().length() < 1024)
            {
                filesize = formatter.format(iev.getOriginalFileName().length())+" bytes";
            }
            else
            {
                filesize = formatter.format(iev.getOriginalFileName().length() / 1024)+" KB";
            }
            
            dtm.addRow(new Object[]{ Boolean.TRUE, iev, iev.getOriginalFileName()+"", iev.getProposedExpression(), filesize});
        }
        this.elementVelidationItems = items;
    }

    public java.util.List getElementVelidationItems() {
        return elementVelidationItems;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItemSelectLink = new javax.swing.JMenuItem();
        jMenuItemImportAsLocalResource = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jButtonSelectAll = new javax.swing.JButton();
        jButtonDeselectAll = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxDoNotShowAgain = new javax.swing.JCheckBox();
        jButtonOk = new javax.swing.JButton();
        jButtonSkip = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        jMenuItemSelectLink.setText("Select a resource in the repository");
        jMenuItemSelectLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSelectLinkActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemSelectLink);

        jMenuItemImportAsLocalResource.setText("Import as local resource");
        jMenuItemImportAsLocalResource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemImportAsLocalResourceActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemImportAsLocalResource);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("JRXML validation");
        setModal(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("<html>JasperServer Plugin has detected some locally referenced images in your report.<br>\nYou can choose to attach these images to the Report Unit importing them into the repository and replace the relative image expressions with an url like \"repo:myImage.jpg\".<br>\n<b>Please check the images you want attach to this Report Unit</b>.</html>");
        jLabel1.setMinimumSize(new java.awt.Dimension(64, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jLabel1, gridBagConstraints);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(452, 200));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "Image", "Local file", "Proposed exp", "File size"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setComponentPopupMenu(jPopupMenu1);
        jScrollPane2.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        getContentPane().add(jScrollPane2, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButtonSelectAll.setMnemonic('a');
        jButtonSelectAll.setText("Select all");
        jButtonSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectAllActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSelectAll, new java.awt.GridBagConstraints());

        jButtonDeselectAll.setMnemonic('d');
        jButtonDeselectAll.setText("Deselect all");
        jButtonDeselectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeselectAllActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDeselectAll, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        getContentPane().add(jPanel1, gridBagConstraints);

        jSeparator1.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        getContentPane().add(jSeparator1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jCheckBoxDoNotShowAgain.setMnemonic('n');
        jCheckBoxDoNotShowAgain.setText("Do not show this window again for this report");
        jCheckBoxDoNotShowAgain.setActionCommand("Do not show this window again");
        jCheckBoxDoNotShowAgain.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxDoNotShowAgain.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxDoNotShowAgain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDoNotShowAgainActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jCheckBoxDoNotShowAgain, gridBagConstraints);

        jButtonOk.setMnemonic('O');
        jButtonOk.setText("Continue");
        jButtonOk.setEnabled(false);
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        jPanel2.add(jButtonOk, gridBagConstraints);

        jButtonSkip.setMnemonic('O');
        jButtonSkip.setText("Skip this step");
        jButtonSkip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        jPanel2.add(jButtonSkip, gridBagConstraints);

        jButtonCancel.setMnemonic('C');
        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        jPanel2.add(jButtonCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed1

        this.setDialogResult( JOptionPane.OK_OPTION );
        this.setVisible(false);
        this.dispose();
        
    }//GEN-LAST:event_jButtonOkActionPerformed1

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed

        this.setDialogResult( JOptionPane.CANCEL_OPTION );
        this.setVisible(false);
        this.dispose();
        
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed

        DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
        
        List selectedItems = new ArrayList();
        for (int i=0; i< dtm.getRowCount(); ++i)
        {
            if ( ((Boolean)jTable1.getValueAt(i,0)).booleanValue())
            {
                selectedItems.add(jTable1.getValueAt(i,1));
            }
        }
        
        boolean reportChanged = false;

        if (selectedItems.size() > 0)
        {
             UploadResourcesDialog urd = new UploadResourcesDialog(Misc.getMainFrame(), true);
             urd.setResourceItems( selectedItems );
             urd.setValidationDialog( this );
             urd.setVisible(true);

             // Expressions have been already replaced...
             
             //JasperDesign jd = getReport();
             //for (int i=0; i<selectedItems.size(); ++i)
             //{
//                 System.out.println("Replacing the expressions...");
//                 System.out.flush();
//                 ElementValidationItem iev = (ElementValidationItem)selectedItems.get(i);
//                 JRDesignElement element = iev.getReportElement();
//                 if (element instanceof JRDesignImage)
//                 {
//                     ((JRDesignImage)element).setExpression(Misc.createExpression("java.lang.String", iev.getProposedExpression()));
//                     
//                     reportChanged = true;
//                 }
//                 else if (element instanceof JRDesignSubreport)
//                 {
//                     ((JRDesignSubreport)element).setExpression(Misc.createExpression("java.lang.String", iev.getProposedExpression()));
//                     
//                     reportChanged = true;
//                 }
             //}
             //*/
             
//             return;
             
             saveReport();
             return;
        }
        
        

        // We need to modify the expression requested...
        elaborationFinished(true);
        
    }//GEN-LAST:event_jButtonOkActionPerformed

    
    public void elaborationFinished(boolean result)
    {
        this.dialogResult = (result) ? JOptionPane.OK_OPTION : JOptionPane.CANCEL_OPTION;
        this.setVisible(false);
        this.dispose();
    }
    
    
    private void jCheckBoxDoNotShowAgainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDoNotShowAgainActionPerformed

        if (getReport() != null)
        {
            boolean found = false;
            String[] propNames = getReport().getPropertiesMap().getPropertyNames();
            for (int i=0; i< propNames.length; ++i)
            {
                String propName = propNames[i];
                if (propName != null && propName.equals("com.jaspersoft.ireport.validation"))
                {
                    getReport().getPropertiesMap().setProperty(propName, "0");
                    found = true;
                }
            }
            
            if (!found)
            {
                getReport().getPropertiesMap().setProperty("com.jaspersoft.ireport.validation", "0");
                saveReport();
            }
            
            
        }
    }//GEN-LAST:event_jCheckBoxDoNotShowAgainActionPerformed

    private void jButtonDeselectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeselectAllActionPerformed

        DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
        for (int i=0; i< dtm.getRowCount(); ++i)
        {
            jTable1.setValueAt(Boolean.FALSE,i,0);
        }
        
        jTable1.updateUI();
        
    }//GEN-LAST:event_jButtonDeselectAllActionPerformed

    private void jButtonSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectAllActionPerformed

        DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
        for (int i=0; i< dtm.getRowCount(); ++i)
        {
            jTable1.setValueAt(Boolean.TRUE,i,0);
        }
        
        jTable1.updateUI();
        
    }//GEN-LAST:event_jButtonSelectAllActionPerformed

    private void jMenuItemSelectLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSelectLinkActionPerformed
        if (jTable1.getSelectedRow() >= 0)
        {
            ResourceChooser rc = new ResourceChooser();
            rc.setServer(server);
            if (rc.showDialog(this, null) == JOptionPane.OK_OPTION)
            {
                ResourceDescriptor rd = rc.getSelectedDescriptor();
                if (rd != null)
                {
                    DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
                    ElementValidationItem iev = (ElementValidationItem)dtm.getValueAt(jTable1.getSelectedRow(),1);
                    iev.setProposedExpression("\"repo:" + rd.getUriString()+ "\"");
                    iev.setStoreAsLink(true);
                    iev.setReferenceUri(rd.getUriString());
                    dtm.setValueAt(iev.getProposedExpression(), jTable1.getSelectedRow(),3);
                    jTable1.updateUI();
                }
            }




        }
    }//GEN-LAST:event_jMenuItemSelectLinkActionPerformed

    private void jMenuItemImportAsLocalResourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemImportAsLocalResourceActionPerformed
        if (jTable1.getSelectedRow() >= 0)
        {
            DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
            ElementValidationItem iev = (ElementValidationItem)dtm.getValueAt(jTable1.getSelectedRow(),1);
            iev.setProposedExpression("\"repo:" + iev.getResourceName() + "\"");
            dtm.setValueAt(iev.getProposedExpression(), jTable1.getSelectedRow(),3);
            iev.setStoreAsLink(false);
            jTable1.updateUI();
        }
    }//GEN-LAST:event_jMenuItemImportAsLocalResourceActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JrxmlValidationDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    public JServer getServer() {
        return server;
    }

    public void setServer(JServer server) {
        this.server = server;
    }

    public RepositoryReportUnit getReportUnit() {
        return reportUnit;
    }

    public void setReportUnit(RepositoryReportUnit reportUnit) {
        this.reportUnit = reportUnit;
    }

    public JasperDesign getReport() {
        return report;
    }

    public void setReport(JasperDesign report) {
        this.report = report;
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDeselectAll;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonSelectAll;
    private javax.swing.JButton jButtonSkip;
    private javax.swing.JCheckBox jCheckBoxDoNotShowAgain;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuItem jMenuItemImportAsLocalResource;
    private javax.swing.JMenuItem jMenuItemSelectLink;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public void saveReport()
    {
        try {

            System.out.println("JRS Plugin --> Saving report " + getFileName() + " " + view);
            if (view != null)
            {
                
                // We cannot use the view directly, since we are operating
                view.getEditorSupport().saveDocument();
            }
            else
            {
                JRXmlWriter.writeReport(getReport(),  "UTF-8");
                
                final String compatibility = IReportManager.getPreferences().get("compatibility", "");

                if (compatibility.length() == 0)
                {
                    JRXmlWriter.writeReport(getReport(), new java.io.FileOutputStream(getFileName()), "UTF-8"); // IReportManager.getInstance().getProperty("jrxmlEncoding", System.getProperty("file.encoding") ));
                }
                else
                {
                    String content = JRXmlWriterHelper.writeReport(getReport(), "UTF-8", compatibility);
                    PrintWriter fos = null;
                    try {
                        fos = new PrintWriter(getFileName());
                        fos.write(content);
                    } finally
                    {
                        fos.close();
                    }
                }
                    
                    
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
