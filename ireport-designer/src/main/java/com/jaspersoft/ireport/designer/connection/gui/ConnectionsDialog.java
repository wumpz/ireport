/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.designer.connection.gui;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.connection.IReportConnectionFactory;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.*;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import java.util.*;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;

/**
 *
 * @author  Administrator
 */
public class ConnectionsDialog extends javax.swing.JDialog {

    boolean updating_row = false;
    /** Creates new form ValuesDialog */
    public ConnectionsDialog(Dialog parent, boolean modal) 
    {
         super(parent,modal);
         initAll();
         applyI18n();
    }

    /** Creates new form ReportQueryFrame */
    public ConnectionsDialog(Frame parent, boolean modal) 
    {
         super(parent,modal);
         initAll();
         applyI18n();
    }

    
    public void initAll() {
        
        initComponents();
        
        this.setSize(490,500);
        
        DefaultListSelectionModel dlsm =  (DefaultListSelectionModel)this.jTableParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)  {
                jTableParametersListSelectionValueChanged(e);
            }
        });

        DefaultTableModel dtm = (DefaultTableModel)jTableParameters.getModel();
        dtm.addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if (updating_row) return;
                if (e.getType()== TableModelEvent.UPDATE && e.getColumn()==2)
                {
                    if (jTableParameters.getValueAt(e.getFirstRow(), 2).equals(Boolean.TRUE))
                    {
                        jButtonSetDefaultActionPerformed(null);
                    }
                    else
                    {
                        updating_row = true;
                        IReportConnection con = (IReportConnection)jTableParameters.getValueAt(e.getFirstRow(), 0);
                        jTableParameters.setValueAt(con == IReportManager.getInstance().getDefaultConnection(), e.getFirstRow(), 2 );
                        updating_row = false;
                    }
                }

            }
        });
        List<IReportConnection> connections = IReportManager.getInstance().getConnections();
        IReportConnection default_irc = IReportManager.getInstance().getDefaultConnection();

        updating_row = true;
        for (IReportConnection con : connections)
        {
            dtm.addRow( new Object[]{con, con.getDescription(), new Boolean(default_irc == con) });
        }
        updating_row = false;
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                setVisible(false);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);

        this.setLocationRelativeTo(null);

        //to make the default button ...
        //this.getRootPane().setDefaultButton(this.jButtonOK);
    }


    public void jTableParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e)
    {
         if (this.jTableParameters.getSelectedRowCount() > 0) {
            this.jButtonModifyParameter.setEnabled(true);
            this.jButtonDeleteParameter.setEnabled(true);
            this.jButtonDefault.setEnabled(true);
        }
        else {
            this.jButtonModifyParameter.setEnabled(false);
            this.jButtonDeleteParameter.setEnabled(false);
            this.jButtonDefault.setEnabled(false);
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

        jPanelParameters = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableParameters = new javax.swing.JTable();
        jPanelButtons = new javax.swing.JPanel();
        jButtonNewParameter = new javax.swing.JButton();
        jButtonModifyParameter = new javax.swing.JButton();
        jButtonDeleteParameter = new javax.swing.JButton();
        jButtonDefault = new javax.swing.JButton();
        jButtonImport = new javax.swing.JButton();
        jButtonExport = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Connections / Datasources");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanelParameters.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        jTableParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Datasource type", "Default"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableParametersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelParameters.add(jScrollPane1, gridBagConstraints);

        jPanelButtons.setMinimumSize(new java.awt.Dimension(100, 10));
        jPanelButtons.setPreferredSize(new java.awt.Dimension(100, 10));
        jPanelButtons.setLayout(new java.awt.GridBagLayout());

        jButtonNewParameter.setText("New");
        jButtonNewParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelButtons.add(jButtonNewParameter, gridBagConstraints);

        jButtonModifyParameter.setText("Modify");
        jButtonModifyParameter.setEnabled(false);
        jButtonModifyParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanelButtons.add(jButtonModifyParameter, gridBagConstraints);

        jButtonDeleteParameter.setText("Delete");
        jButtonDeleteParameter.setEnabled(false);
        jButtonDeleteParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanelButtons.add(jButtonDeleteParameter, gridBagConstraints);

        jButtonDefault.setText("Set as default");
        jButtonDefault.setActionCommand("Set as active");
        jButtonDefault.setEnabled(false);
        jButtonDefault.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSetDefaultActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 5, 3);
        jPanelButtons.add(jButtonDefault, gridBagConstraints);

        jButtonImport.setText("Import...");
        jButtonImport.setActionCommand("Set as active");
        jButtonImport.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 5, 3);
        jPanelButtons.add(jButtonImport, gridBagConstraints);

        jButtonExport.setText("Export...");
        jButtonExport.setActionCommand("Set as active");
        jButtonExport.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanelButtons.add(jButtonExport, gridBagConstraints);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelButtons.add(jButtonClose, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelParameters.add(jPanelButtons, gridBagConstraints);

        getContentPane().add(jPanelParameters, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportActionPerformed

            if (jTableParameters.getRowCount() == 0)
            {
                JOptionPane.showMessageDialog(this,
                        I18n.getString("ConnectionsDialog.Message.NoExport"),
                        "",JOptionPane.INFORMATION_MESSAGE);
                return;
            }    
        
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".xml") || file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return I18n.getString("ConnectionsDialog.Description.Definition");
		    }
	    });

	    if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

                try {
                    java.io.File f = jfc.getSelectedFile();
                    String fileName = f.getName();
                    if (fileName.indexOf(".") < 0)
                    {
                        fileName += ".xml";
                    }
                    f = new java.io.File(f.getParent(), fileName);
                    
                    if (f.exists())
                    {
                        if (JOptionPane.showConfirmDialog(this,
                                Misc.formatString(I18n.getString("ConnectionsDialog.Message.ConnExists"), new Object[]{""+f}),
                                "",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE) != JOptionPane.OK_OPTION)
                        {
                            return;
                        }
                    }
                    
                    java.io.PrintWriter pw = new java.io.PrintWriter( new java.io.OutputStreamWriter( new java.io.FileOutputStream( f  ), "UTF8" )); //UTF8
                    pw.print("<?xml version=\"1.0\"?>");
                    pw.println("<!-- iReport connections -->");
                    pw.println("<iReportConnectionSet>");

                    List<IReportConnection> connections = IReportManager.getInstance().getConnections();
                    
                    int i = 0;
                    
                    for (IReportConnection con : connections)
                    {
                        i++;
                        con.save(pw);
                    }
                    
                    pw.println("</iReportConnectionSet>");

                    pw.close();
                    
                    JOptionPane.showMessageDialog(this,
                            Misc.formatString(I18n.getString("ConnectionsDialog.Message.Exported"), new Object[]{new Integer(i)}), //"messages.connectionsDialog.connectionsExported" 
                            "",JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(this,
                            Misc.formatString(I18n.getString("ConnectionsDialog.Message.ErrorSave"), new Object[]{ex.getMessage()}) //"messages.connectionsDialog.errorSavingConnections" 
                            );
                    ex.printStackTrace();
                }
                    
	    }
        
        
    }//GEN-LAST:event_jButtonExportActionPerformed

    private void jButtonImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportActionPerformed

        
            JFileChooser jfc = new JFileChooser();
            jfc.setMultiSelectionEnabled(false);
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".xml") || file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return I18n.getString("ConnectionsDialog.Description.Definition");
		    }
	    });

	    if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                    Vector new_cons = loadConnections( jfc.getSelectedFile() );
                    if (new_cons != null)
                    {
                        DefaultTableModel dtm = (DefaultTableModel)jTableParameters.getModel();
                        int i = 0;
                        updating_row = true;
                        for (i=0; i<new_cons.size(); ++i)
                        {
                            IReportConnection con = (IReportConnection)new_cons.elementAt(i);
                            dtm.addRow( new Object[]{con, con.getDescription() });
                            IReportManager.getInstance().addConnection(con);
                        }
                        updating_row = false;
                        IReportManager.getInstance().saveiReportConfiguration();
                        
                        JOptionPane.showMessageDialog(this,
                                Misc.formatString(I18n.getString("ConnectionsDialog.Message.Imported"), new Object[]{new Integer(i)}), //"messages.connectionsDialog.connectionsImported" ,
                                "",JOptionPane.INFORMATION_MESSAGE);
                    }
	    }        
    }//GEN-LAST:event_jButtonImportActionPerformed

    private void jTableParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableParametersMouseClicked

            if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
            {
                jButtonModifyParameterActionPerformed(null);
            }

    }//GEN-LAST:event_jTableParametersMouseClicked

    private void jButtonSetDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSetDefaultActionPerformed

        if (jTableParameters.getSelectedRowCount() > 0)
        {
            updating_row = true;
            IReportConnection irc = null;
            try {
                irc = (IReportConnection)jTableParameters.getModel().getValueAt(jTableParameters.getSelectedRow(), 0);
                IReportManager.getInstance().setDefaultConnection(irc);
                jTableParameters.getModel().setValueAt(new Boolean(true) ,jTableParameters.getSelectedRow(), 2);
                for (int i=0; i<jTableParameters.getRowCount(); ++i)
                {
                    if (i != jTableParameters.getSelectedRow())
                    {
                        jTableParameters.getModel().setValueAt(new Boolean(false) ,i, 2);
                    }
                }
            } catch (Exception ex) { return; }
            finally {
                updating_row = false;
            }
        }
}//GEN-LAST:event_jButtonSetDefaultActionPerformed

    private void jButtonDeleteParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteParameterActionPerformed
        // Get the selected connection...
        if (jTableParameters.getSelectedRowCount() > 0)
        {
            while (jTableParameters.getSelectedRowCount() > 0)
            {
                IReportConnection irc = null;
                try {
                    irc = (IReportConnection)jTableParameters.getModel().getValueAt(jTableParameters.getSelectedRow(), 0);
                } catch (Exception ex) { return; }


                if ( IReportManager.getInstance().getDefaultConnection() == irc)
                {
                    IReportManager.getInstance().setDefaultConnection(null);
                }

                IReportManager.getInstance().removeConnection(irc);
                ((DefaultTableModel)jTableParameters.getModel()).removeRow(jTableParameters.getSelectedRow());
                jTableParameters.updateUI();
            }
            
            IReportManager.getInstance().saveiReportConfiguration();
        }

    }//GEN-LAST:event_jButtonDeleteParameterActionPerformed

    private void jButtonModifyParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyParameterActionPerformed
        
        
        Window pWin = Misc.getParentWindow(this);
        
        ConnectionDialog cd = null;
        if (pWin instanceof Dialog) cd = new ConnectionDialog((Dialog)pWin, true);
        else if (pWin instanceof Frame) cd = new ConnectionDialog((Frame)pWin, false);
        else cd = new ConnectionDialog((Dialog)null, false);
        
        //ConnectionDialog cd = new ConnectionDialog(parent,false);

        // Take the selected connection from the table...
        if (jTableParameters.getSelectedRow() < 0) return;
        IReportConnection irc = null;
        try {
            irc = (IReportConnection)jTableParameters.getModel().getValueAt(jTableParameters.getSelectedRow(), 0);
        } catch (Exception ex) { return; }

        if (irc == null) return;

        cd.setIReportConnection(irc);

        cd.setVisible(true);

        if (cd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            IReportConnection con = cd.getIReportConnection();
            // Now we have an old and a new object.
            // 1. Adjust table...
            try {
                jTableParameters.getModel().setValueAt(con ,jTableParameters.getSelectedRow(), 0);
                jTableParameters.getModel().setValueAt(con.getDescription() ,jTableParameters.getSelectedRow(), 1);
                jTableParameters.getModel().setValueAt(new Boolean(true) ,jTableParameters.getSelectedRow(), 2);
            } catch (Exception ex) { return; }

            updating_row = true;
            for (int i=0; i<jTableParameters.getRowCount(); ++i)
            {
                if (i != jTableParameters.getSelectedRow())
                {
                    jTableParameters.getModel().setValueAt(new Boolean(false) ,i, 2);
                }
            }
            updating_row = false;
            
            IReportManager.getInstance().updateConnection(
                    IReportManager.getInstance().getConnections().indexOf(irc), con);
            IReportManager.getInstance().setDefaultConnection(con);
            IReportManager.getInstance().saveiReportConfiguration();
        }
    }//GEN-LAST:event_jButtonModifyParameterActionPerformed

    private void jButtonNewParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewParameterActionPerformed
        Window parent = Misc.getParentWindow(this);
        
        
        //ConnectionDialog cd = new ConnectionDialog(parent,false);
        ConnectionDialog cd = 
                    new ConnectionDialog(this, true);
        
        cd.setVisible(true);

        if (cd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            
            IReportConnection con = cd.getIReportConnection();
            DefaultTableModel dtm = (DefaultTableModel)jTableParameters.getModel();
            updating_row = true;
            dtm.addRow( new Object[]{con, con.getDescription(), new Boolean(true) });
            
            for (int i=0; i<jTableParameters.getRowCount(); ++i)
            {
                if (i != dtm.getRowCount()-1)
                {
                    jTableParameters.getModel().setValueAt(new Boolean(false) ,i, 2);
                }
            }
            updating_row = false;
            
            IReportManager.getInstance().addConnection(con);
            IReportManager.getInstance().setDefaultConnection(con);
            IReportManager.getInstance().saveiReportConfiguration();
        }
         
    }//GEN-LAST:event_jButtonNewParameterActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
    }//GEN-LAST:event_closeDialog

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButtonCloseActionPerformed

    
    public void updateConnections() {
       /*
        DefaultTableModel dtm = (DefaultTableModel)jTableParameters.getModel();
        dtm.setRowCount(0);

        Enumeration enum = jReportFrame.getReport().getParameters().elements();
        while (enum.hasMoreElements())
        {
            it.businesslogic.ireport.JRParameter parameter = (it.businesslogic.ireport.JRParameter)enum.nextElement();
            Vector row = new Vector();
            row.addElement( parameter);
            row.addElement( parameter.getClassType());
            row.addElement( parameter.isIsForPrompting()+"");

            dtm.addRow(row);
        }
        */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonDefault;
    private javax.swing.JButton jButtonDeleteParameter;
    private javax.swing.JButton jButtonExport;
    private javax.swing.JButton jButtonImport;
    private javax.swing.JButton jButtonModifyParameter;
    private javax.swing.JButton jButtonNewParameter;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelParameters;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableParameters;
    // End of variables declaration//GEN-END:variables

    public void setVisible(boolean visible)
    {

       updateConnections();
        super.setVisible(visible);
    }
       
    /**
     * Load a set of connections from a file. The connection are renamed if already present
     * in the connection list. See getAvailableConnectionName() for details about the new name.
     */
    @SuppressWarnings("unchecked")
    public Vector loadConnections(java.io.File xmlfile)
     {
         Vector v = new Vector();
         try {
             
             ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
             Thread.currentThread().setContextClassLoader(DOMParser.class.getClassLoader());
             
             DOMParser parser = new DOMParser();
             java.io.FileInputStream fis = new java.io.FileInputStream(xmlfile);
             org.xml.sax.InputSource input_sss  = new org.xml.sax.InputSource(fis);
             //input_sss.setSystemId(filename);
             parser.parse( input_sss );

             Thread.currentThread().setContextClassLoader(oldClassLoader);

                          
             Document document = parser.getDocument();
             Node node = document.getDocumentElement();


             NodeList list_child = node.getChildNodes(); // The root is iReportConnections
             for (int ck=0; ck< list_child.getLength(); ck++) {
                 Node connectionNode = list_child.item(ck);
                 if (connectionNode.getNodeName() != null && connectionNode.getNodeName().equals("iReportConnection"))
                 {
                    // Take the CDATA...
                        String connectionName = "";
                        String connectionClass = "";
                        HashMap hm = new HashMap();
                        NamedNodeMap nnm = connectionNode.getAttributes();
                        if ( nnm.getNamedItem("name") != null) connectionName = nnm.getNamedItem("name").getNodeValue();
                        if ( nnm.getNamedItem("connectionClass") != null) connectionClass = nnm.getNamedItem("connectionClass").getNodeValue();

                        // Get all connections parameters...
                        NodeList list_child2 = connectionNode.getChildNodes();
                        for (int ck2=0; ck2< list_child2.getLength(); ck2++) {
                            String parameterName = "";
                            Node child_child = list_child2.item(ck2);
                            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("connectionParameter")) {

                                NamedNodeMap nnm2 = child_child.getAttributes();
                                if ( nnm2.getNamedItem("name") != null)
                                    parameterName = nnm2.getNamedItem("name").getNodeValue();
                                hm.put( parameterName,Misc.readPCDATA(child_child));
                            }
                        }
                        
                        // If the name exists, rename it as "name (2)"
                        try {
                            List<IReportConnectionFactory> types = IReportManager.getInstance().getIReportConnectionFactories(); //ConnectionImplementations();
                            boolean found = false;
                            for (IReportConnectionFactory factory : types) //int i=0; i<types.size(); ++i)
                            {
                                if (factory.getConnectionClassName().equals(connectionClass))
                                {
                                    IReportConnection con = factory.createConnection();
                                    con.loadProperties(hm);
                                    connectionName = getAvailableConnectionName(connectionName);
                                    con.setName(connectionName);
                                    v.add( con );
                                    found = true;
                                    break;
                                }
                            }
                            if (!found)
                            {
                                throw new Exception("Unable to import the connection " + connectionName + "\nNo factory available for connections of type " + connectionClass + "");
                            }
                        } catch (Exception ex) {
                                
                            JOptionPane.showMessageDialog(this,
                                Misc.formatString(I18n.getString("ConnectionsDialog.Message.Error"), new Object[]{connectionName}), //"messages.connectionsDialog.errorLoadingConnection"
                                I18n.getString("ConnectionsDialog.Message.Err"), JOptionPane.ERROR_MESSAGE);
                           ex.printStackTrace();
                        }
                }
             }
         } catch (Exception ex)
         {
             JOptionPane.showMessageDialog(this,
                                Misc.formatString(I18n.getString("ConnectionsDialog.Message.Err2"), new Object[]{ex.getMessage()}), //"messages.connectionsDialog.errorLoadingConnections"
                                I18n.getString("ConnectionsDialog.Message.Err"), JOptionPane.ERROR_MESSAGE);
              ex.printStackTrace();
         }

         return v;
     }
    
     // If the name exists, rename it as "name (2)"
     /**
      * This method take a proposed connection name. Check for duplicates names. If the 
      * proposed name is already present, the name "proposed (2)" is checked and so
      * on up to when a valid name is found....
      */
     public static String getAvailableConnectionName(String proposedConnectionName)
     {
        return getAvailableConnectionName(proposedConnectionName, 0);
     }
     
     private static String getAvailableConnectionName(String proposedConnectionName, int testNumber)
     {
        String name = proposedConnectionName;
        if (testNumber != 0) name += " (" + testNumber + ")";
        
        List<IReportConnection> connections = IReportManager.getInstance().getConnections();
        IReportConnection default_irc = IReportManager.getInstance().getDefaultConnection();

        for (IReportConnection con : connections)
        {
            // toString for an iReportConnection is the getName method...
            String conName = "" + con;
            if (name.equals(conName))
            {
                return getAvailableConnectionName(proposedConnectionName, testNumber+1);
            }
        }
        return name;
     }
     
     
    public void applyI18n(){
                // Start autogenerated code ----------------------
                //jButtonDefault.setText(I18n.getString("connectionsDialog.buttonDefault","Set as default"));
                //jButtonDeleteParameter.setText(I18n.getString("connectionsDialog.buttonDeleteParameter","Delete"));
                //jButtonExport.setText(I18n.getString("connectionsDialog.buttonExport","Export..."));
                //jButtonImport.setText(I18n.getString("connectionsDialog.buttonImport","Import..."));
                //jButtonModifyParameter.setText(I18n.getString("connectionsDialog.buttonModifyParameter","Modify"));
                //jButtonNewParameter.setText(I18n.getString("connectionsDialog.buttonNewParameter","New"));
                // End autogenerated code ----------------------
                //setTitle(I18n.getString("connectionsDialog.title","Connections / Datasources"));
                jTableParameters.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("connectionsDialog.tablecolumn.name") );
                jTableParameters.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("connectionsDialog.tablecolumn.datasourceType") );
                jTableParameters.getColumnModel().getColumn(2).setHeaderValue( I18n.getString("connectionsDialog.tablecolumn.default") );
    }
     
}
