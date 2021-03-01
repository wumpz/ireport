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
package com.jaspersoft.ireport.jasperserver.ui;

import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author  gtoffoli
 */
public class ResourceChooser extends javax.swing.JPanel {
    
    private JServer server = null;
    private JDialog dialog = null;
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    private boolean multipleSelection = false;
    
    private boolean foldersOnly = false;
    
    private ResourceDescriptor root = null;
    private ResourceDescriptor currentParent = null;
    private List<ResourceDescriptor> selectedDescriptors = new ArrayList<ResourceDescriptor>();
    
    /** Creates new form ResourceChooser */
    public ResourceChooser() {
        initComponents();
           
        
        jList1.setModel(new DefaultListModel());
       
        jScrollPane2.getViewport().setBackground(Color.WHITE);
        adjustListView();
        
        setRoot(new ResourceDescriptor());
        getRoot().setUriString("/");
        getRoot().setChildren(null); // This will force a reload...
        getRoot().setWsType( ResourceDescriptor.TYPE_FOLDER);
        
        jList1.setCellRenderer(new RepositoryListCellRenderer());
        TableColumn col = jTable1.getColumnModel().getColumn(0);
        col.setCellRenderer(new RepositoryTableCellRenderer());
        jList1.getSelectionModel().setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        jTable1.setSelectionMode( DefaultListSelectionModel.SINGLE_SELECTION );
        jTable1.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                        jTable1ValueChanged(evt);
                }
            });

        jComboBoxLookIn.removeAllItems();
        jComboBoxLookIn.setRenderer( new RepositoryListCellRenderer(true) );
        
        jButtonDetails.setIcon(UIManager.getIcon("FileChooser.detailsViewIcon"));
        jButtonDetails.setText("");
        
        jButtonList.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
        jButtonList.setText("");
        
        jButtonUp.setIcon(UIManager.getIcon("FileChooser.upFolderIcon"));
        jButtonUp.setText("");
        
        jTextField1.requestFocusInWindow();
        
        jTextField1.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                updateSelectedDescriptor();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                updateSelectedDescriptor();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                updateSelectedDescriptor();
            }
        });
        
        applyI18n();
    }
    
    public void applyI18n()
    {
        
        jButtonClose.setText( JasperServerManager.getString("resourceChooser.buttonCancel","Cancel"));
        jButtonDetails.setToolTipText( JasperServerManager.getString("resourceChooser.buttonDetails","Details"));
        jButtonList.setToolTipText( JasperServerManager.getString("resourceChooser.buttonList","List"));
        jButtonOpen.setText( JasperServerManager.getString("resourceChooser.buttonOpenResource","Open resource"));
        jButtonUp.setToolTipText( JasperServerManager.getString("resourceChooser.buttonUp","Up"));
        jLabel1.setText( JasperServerManager.getString("resourceChooser.labelLookIn","Look in:"));
        jLabel2.setText( JasperServerManager.getString("resourceChooser.labelResourceName","Resource name:"));
        jLabel3.setText( JasperServerManager.getString("resourceChooser.labelResourceType","Type:"));
    
        jTable1.getColumn("Name").setHeaderValue( JasperServerManager.getString("resourceChooser.table.label","Name")); 
        jTable1.getColumn("Label").setHeaderValue( JasperServerManager.getString("resourceChooser.table.value","Label")); 
        jTable1.getColumn("Type").setHeaderValue( JasperServerManager.getString("resourceChooser.table.type","Type")); 
        jTable1.updateUI();
    }
    
    

    public JServer getServer() {
        return server;
    }

    public void setServer(JServer server) {
        this.server = server;
    }
    
    public void updateSelectedDescriptor()
    {
        if (syncSelection == true) return;
        // Check if the text rappresents a valid descriptor...
        System.out.println("updateSelectedDescriptor");
        selectedDescriptors.clear();
    }
    
    
    /**
     * Return a set of ResourceDescriptors....
     *
     */
    public List list(ResourceDescriptor rd) throws Exception
    {
        java.util.List list = new java.util.ArrayList();
        
        if (getServer() != null && rd.getWsType().equals(ResourceDescriptor.TYPE_FOLDER) 
            && rd.getChildren() == null)
        {
            list = getServer().getWSClient().list(rd);
            
            if (isFoldersOnly())
            {
                for (int i=0; i<list.size(); ++i)
                {
                    ResourceDescriptor res = (ResourceDescriptor)list.get(i);
                    if (!res.getWsType().equals(ResourceDescriptor.TYPE_FOLDER))
                    {
                        list.remove(i);
                        i--;
                    }
                }
            }
        
        }
        
        return list;
    }
    
    /**
     * Pops a custom resource chooser dialog with a custom approve button. For example, the following code pops up a
     * file chooser with a "Open image resource" button (instead of the normal "Open resource" button): 
     * 
     * resourcechooser.showDialog(parentFrame, "Open image resource");
     *
     * @Parameters:
     * parent - the parent component of the dialog, can be null; see showDialog for details 
     * approveButtonText - optional text (default  "Open resource")
     *
     * @Returns:
     *  the return state of the file chooser on popdown: 
     *  JOptionPane.CANCEL_OPTION 
     *  JOptionPane.OK_OPTION 
     *
     */
    public int showDialog(Component parent, String approveButtonText)
    {
        dialogResult = JOptionPane.CANCEL_OPTION;
        if (approveButtonText == null) approveButtonText = JasperServerManager.getString("resourceChooser.buttonOpenResource","Open resource");
        jButtonOpen.setText(approveButtonText);
        
        Window w = SwingUtilities.getWindowAncestor(parent);
        if (w instanceof Frame) dialog = new JDialog((Frame)w, true);
        else if (w instanceof Dialog) dialog = new JDialog((Dialog)w, true);
        else dialog = new JDialog((Frame)null, true);
        dialog.getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        dialog.getContentPane().add(this, gridBagConstraints);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        
        jComboBoxLookIn.removeAllItems();
        setCurrentParent( getRoot() );
        
        dialog.setVisible(true);
        return getDialogResult();
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
        jPanelTop = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxLookIn = new javax.swing.JComboBox();
        jButtonUp = new javax.swing.JButton();
        jButtonList = new javax.swing.JToggleButton();
        jButtonDetails = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox();
        jButtonOpen = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jPanelTop.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanelTop.setPreferredSize(new java.awt.Dimension(10, 30));
        jPanelTop.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Look in:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelTop.add(jLabel1, gridBagConstraints);

        jComboBoxLookIn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxLookIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLookInActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanelTop.add(jComboBoxLookIn, gridBagConstraints);

        jButtonUp.setText("Up");
        jButtonUp.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        jPanelTop.add(jButtonUp, gridBagConstraints);

        buttonGroup1.add(jButtonList);
        jButtonList.setSelected(true);
        jButtonList.setText("List");
        jButtonList.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        jPanelTop.add(jButtonList, gridBagConstraints);

        buttonGroup1.add(jButtonDetails);
        jButtonDetails.setLabel("Details");
        jButtonDetails.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanelTop.add(jButtonDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jPanelTop, gridBagConstraints);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 200));

        jList1.setLayoutOrientation(javax.swing.JList.VERTICAL_WRAP);
        jList1.setVisibleRowCount(-1);
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setOpaque(false);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(400, 200));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Label", "Type"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setIntercellSpacing(new java.awt.Dimension(0, 0));
        jTable1.setRequestFocusEnabled(false);
        jTable1.setShowHorizontalLines(false);
        jTable1.setShowVerticalLines(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jScrollPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        add(jPanel1, gridBagConstraints);

        jPanel2.setMinimumSize(new java.awt.Dimension(10, 60));
        jPanel2.setPreferredSize(new java.awt.Dimension(10, 60));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Resource name:");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jLabel2, gridBagConstraints);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Type:");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jLabel3, gridBagConstraints);

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel2.add(jTextField1, gridBagConstraints);

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(jComboBox2, gridBagConstraints);

        jButtonOpen.setText("Open resource");
        jButtonOpen.setMargin(new java.awt.Insets(2, 12, 2, 12));
        jButtonOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 4);
        jPanel2.add(jButtonOpen, gridBagConstraints);

        jButtonClose.setText("Cancel");
        jButtonClose.setMargin(new java.awt.Insets(2, 12, 2, 12));
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 4);
        jPanel2.add(jButtonClose, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed

        if (evt.getKeyCode() == evt.VK_ENTER)
        {
            jButtonOpenActionPerformed(null);
        }
        
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed

        selectedDescriptors.clear();
        this.setDialogResult( JOptionPane.CANCEL_OPTION);
        dialog.setVisible(false);
        dialog.dispose();
        
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenActionPerformed

        if (jTextField1.getText().trim().length() == 0) return;
        
        if (getSelectedDescriptors().size() == 0)
        {   
            // Check if the selected descriptor can be a valid uri...
            // 1. Check if the name contains /...
            String uri = jTextField1.getText().trim();
            while (uri.endsWith("/")) uri = uri.substring(0,uri.length()-1);
            if (uri.length() == 0) return;
            if (uri.indexOf("/") >= 0)
            {
                ResourceDescriptor nrd = new ResourceDescriptor();
                nrd.setUriString(uri);
                nrd.setWsType("-"); //ResourceDescriptor.TYPE_FOLDER);
                try {
                    nrd = getServer().getWSClient().get(nrd, null);
                    
                    if (!nrd.getWsType().equals("-")) // Filled by the WS!!!!
                    {
                        if (nrd.getWsType().equals(ResourceDescriptor.TYPE_FOLDER))
                        {
                            openFolder(nrd);
                            return;
                        }
                        else
                        {
                            selectedDescriptors.add(nrd);
                        }
                    }
                    
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                
            }
            else
            {
                ResourceDescriptor nrd = new ResourceDescriptor();
                uri = ((ResourceDescriptor)jComboBoxLookIn.getSelectedItem()).getUriString() + "/" + uri;
                nrd.setUriString(uri);
                nrd.setWsType(ResourceDescriptor.TYPE_FOLDER);
                try {
                    nrd = getServer().getWSClient().get(nrd, null);
                    if (nrd.getWsType() != null) // Filled by the WS!!!!
                    {
                        if (nrd.getWsType().equals(ResourceDescriptor.TYPE_FOLDER))
                        {
                            openFolder(nrd);
                            return;
                        }
                        else
                        {
                            // get the full selection...
                            selectedDescriptors.add(nrd);
                        }
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                
            }
            
            if (getSelectedDescriptor() == null)
            {
                JOptionPane.showMessageDialog(this,
                        JasperServerManager.getFormattedString("resourceChooser.message.fileNotFound","{0}\nResource not found or not valid.\nPlease verify the correct file name was given.",new Object[]{uri}));

                return;
            }
        }
        
        this.setDialogResult( JOptionPane.OK_OPTION);
        dialog.setVisible(false);
        dialog.dispose();
        
    }//GEN-LAST:event_jButtonOpenActionPerformed

    
    /**
     * Must be used to open never opened folders.
     *
     */
    public void openFolder(ResourceDescriptor nrd)
    {
        try {
            
        nrd.setChildren(null);
                
        // Create the chain of descriptors....
        jComboBoxLookIn.setSelectedIndex(0);
        String path = nrd.getUriString();
        
        String complete_path = "";
        while (path.startsWith("/")) path =path.substring(1);
        while (path.endsWith("/")) path = path.substring(0,path.length() -1);
        while (path.indexOf("/") >= 0)
        {
            String folderName = path.substring(0, path.indexOf("/"));
            
            path = path.substring(path.indexOf("/")+1);
            
            complete_path += "/" + folderName;
            ResourceDescriptor nr = new ResourceDescriptor();
            nr.setUriString(complete_path);
            nr.setName(folderName);
            nr.setWsType(ResourceDescriptor.TYPE_FOLDER);
            nr.setChildren(null);
            jComboBoxLookIn.addItem(nr);
        }
        
        jComboBoxLookIn.addItem(nrd);
        jComboBoxLookIn.setSelectedItem(nrd);
        
        jTextField1.setText(nrd.getUriString());
        jTextField1.setSelectionStart(0);
        jTextField1.setSelectionEnd(nrd.getUriString().length());
        jTextField1.requestFocusInWindow();
        
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    boolean syncSelection = false;

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged

        if (syncSelection) return;
        syncSelection = true;
        if (jList1.getSelectedIndex() < 0) jTable1.getSelectionModel().clearSelection();
        else
        {
            jTable1.getSelectionModel().setSelectionInterval(jList1.getSelectedIndex(),jList1.getSelectedIndex());
        }
        
        selectionChanged();
        syncSelection = false;
    }//GEN-LAST:event_jList1ValueChanged

    
    
     private void jTable1ValueChanged(javax.swing.event.ListSelectionEvent evt) {

        if (syncSelection) return;
        syncSelection = true;
        if (jTable1.getSelectedRowCount() == 0) jList1.getSelectionModel().clearSelection();
        else
        {
            jList1.getSelectionModel().setSelectionInterval(jTable1.getSelectedRow(),jTable1.getSelectedRow());
        }
        
        selectionChanged();
        syncSelection = false;
    }
     
     
     private void selectionChanged()
     {
         selectedDescriptors.clear();
         if (jList1.getSelectedIndex() >= 0)
         {
            Object[] selectedObjects = jList1.getSelectedValues();

            for (Object selectedObject : selectedObjects)
            {
                ResourceDescriptor rd = (ResourceDescriptor)selectedObject;
                if (rd.getWsType() != null)
                {

                    if ( (rd.getWsType().equals(ResourceDescriptor.TYPE_FOLDER) && isFoldersOnly()) ||
                         (!isFoldersOnly() && !rd.getWsType().equals(ResourceDescriptor.TYPE_FOLDER)))
                    {
                        selectedDescriptors.add(rd);

                        if (selectedDescriptors.size() == 1)
                        {
                            jTextField1.setText( rd.getName());
                        }
                        else
                        {
                            String s = "";
                            for (ResourceDescriptor rdx : selectedDescriptors)
                            {
                                if (s.length() > 0) s+=" ";
                                s += "\"" + rdx.getName() + "\"";
                            }
                            jTextField1.setText(s);
                        }
                    }
                }
            }

            Iterator<ResourceSelectedListener> it;
            synchronized (listeners) {
                it = new HashSet<ResourceSelectedListener>(listeners).iterator();
            }

            while (it.hasNext()) {
                it.next().resourceSelected(selectedDescriptors);
            }
         }
     }
    
     private void jButtonUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpActionPerformed

        if (jComboBoxLookIn.getSelectedIndex() > 0)
        {
            jComboBoxLookIn.setSelectedIndex(jComboBoxLookIn.getSelectedIndex()-1);
        }
        
     }//GEN-LAST:event_jButtonUpActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            if (jTable1.getSelectedRow() >= 0)
            {
                ResourceDescriptor rd = (ResourceDescriptor)jTable1.getValueAt(jTable1.getSelectedRow(), 0);
                if (rd.getWsType() != null && rd.getWsType().equals(ResourceDescriptor.TYPE_FOLDER))
                {
                    setCurrentParent(rd);
                }
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked

        if (evt.getClickCount() == 2 &&
            evt.getButton() == evt.BUTTON1)
        {
            ResourceDescriptor rd = (ResourceDescriptor)jList1.getSelectedValue();
            if (rd != null && rd.getWsType() != null && rd.getWsType().equals(ResourceDescriptor.TYPE_FOLDER))
            {
                setCurrentParent(rd);
            }
        }
        
    }//GEN-LAST:event_jList1MouseClicked

    private void jComboBoxLookInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLookInActionPerformed

        setCurrentParent( (ResourceDescriptor)jComboBoxLookIn.getSelectedItem());
        
    }//GEN-LAST:event_jComboBoxLookInActionPerformed

    private void jButtonDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetailsActionPerformed
        adjustListView();
    }//GEN-LAST:event_jButtonDetailsActionPerformed

    private void jButtonListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonListActionPerformed

        adjustListView();
        
    }//GEN-LAST:event_jButtonListActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed
    
    public void adjustListView()
    {
       jScrollPane1.setVisible(jButtonList.isSelected());
       jScrollPane2.setVisible(!jButtonList.isSelected());
       
       jPanel1.doLayout();
       jPanel1.updateUI();
       //if (jScrollPane1.isVisible()) jScrollPane1.updateUI();
       //if (jScrollPane2.isVisible()) jScrollPane2.updateUI();
    }

    public ResourceDescriptor getRoot() {
        return root;
    }

    public void setRoot(ResourceDescriptor root) {
        this.root = root;
    }

    public ResourceDescriptor getCurrentParent() {
        return currentParent;
    }

    public void clearLookInList()
    {
        jComboBoxLookIn.removeAllItems();
    }
    
    public void setCurrentParent(ResourceDescriptor currentParent) {
        
        if (this.currentParent == currentParent &&
            currentParent != null && currentParent.getChildren() != null) 
        {
            return;  // Nothing to do
        }
        
        this.currentParent = currentParent;
        
        // 1. Find the descriptor in the combobox. If it is not present, add it at the end...
        boolean found = false;
        for (int i=0; i<jComboBoxLookIn.getItemCount(); ++i)
        {
            ResourceDescriptor tmprd = (ResourceDescriptor)jComboBoxLookIn.getItemAt(i);
            if (tmprd == currentParent)
            {
               jComboBoxLookIn.setSelectedIndex(i);
               found = true;
               while (jComboBoxLookIn.getItemCount() > i+1)
               {
                   jComboBoxLookIn.removeItemAt(i+1);
               }
               break;
            }
        }
        
        if (!found && currentParent != null)
        {
            jComboBoxLookIn.addItem( currentParent );
            jComboBoxLookIn.setSelectedItem(currentParent);
        }
        
        refreshContent();
    }
    
    /**
     * Replace the content of table and list with the childs of currentParent.
     * If currentParent.getChilds == null, the webservice is called to populate it...
     *
     */
    private void refreshContent()
    {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            DefaultListModel dlm = (DefaultListModel)jList1.getModel();
            DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();

            dlm.removeAllElements();
            dtm.setRowCount(0);

            if (currentParent == null) return;
            if (getServer() == null) return;
            
            if (currentParent != null)
            {       
                    List children = currentParent.getChildren();
                    if (children == null)
                    {
                        try {
                            children = list(currentParent);
                            for (int i=0; i<children.size(); ++i)
                            {
                                ((ResourceDescriptor)children.get(i)).setChildren(null);
                            }
                            currentParent.setChildren( children );
                        } catch (Exception ex)
                        {
                            JOptionPane.showMessageDialog(this,
                                    JasperServerManager.getFormattedString("resourceChooser.message.unableToList","Unable to list {0}\n{1}",new Object[]{currentParent, ex.getMessage()}));

                            ex.printStackTrace();
                        }
                    }
                    
                    if (children != null)
                    {
                        for (int i=0; i<children.size(); ++i)
                        {
                            ResourceDescriptor rd = ((ResourceDescriptor)children.get(i));
                            dlm.addElement(rd);
                            dtm.addRow(new Object[]{rd, rd.getLabel(), rd.getWsType()});
                        }
                    }
            }
        }
        finally
        {
            jList1.updateUI();
            jTable1.updateUI();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    public ResourceDescriptor getSelectedDescriptor() {
        if (selectedDescriptors.size() > 0)
        return selectedDescriptors.get(0);
        return null;
    }
    
    public List<ResourceDescriptor> getSelectedDescriptors() {
        return selectedDescriptors;
    }

    
    public void hideDialogPanel()
    {
        jPanel2.setVisible(false);
    }
    
    
    
    
    private final Set<ResourceSelectedListener> listeners = new HashSet<ResourceSelectedListener>(1); // or can use ChangeSupport in NB 6.0
    public final void addResourceSelectedListener(ResourceSelectedListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }
    public final void removeResourceSelectedListener(ResourceSelectedListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JToggleButton jButtonDetails;
    private javax.swing.JToggleButton jButtonList;
    private javax.swing.JButton jButtonOpen;
    private javax.swing.JButton jButtonUp;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBoxLookIn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelTop;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    public boolean isFoldersOnly() {
        return foldersOnly;
    }

    public void setFoldersOnly(boolean foldersOnly) {
        this.foldersOnly = foldersOnly;
    }

    public void setMultipleSelection(boolean b)
    {
        multipleSelection = b;

        jList1.getSelectionModel().setSelectionMode( (b) ? DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION : DefaultListSelectionModel.SINGLE_SELECTION);
        jTable1.setSelectionMode( (b) ? DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION : DefaultListSelectionModel.SINGLE_SELECTION );

        

    }
    
}
