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
package com.jaspersoft.ireport.designer.data.fieldsproviders.olap;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.FieldsProviderEditor;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.data.ReportQueryDialog;
import com.jaspersoft.ireport.designer.sheet.Tag;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.olap.result.JROlapHierarchy;
import net.sf.jasperreports.olap.result.JROlapHierarchyLevel;
import net.sf.jasperreports.olap.result.JROlapMember;
import net.sf.jasperreports.olap.result.JROlapMemberTuple;
import net.sf.jasperreports.olap.result.JROlapResult;
import net.sf.jasperreports.olap.result.JROlapResultAxis;
/**
 *
 * @author  gtoffoli
 */
public class OlapBrowser extends javax.swing.JPanel implements FieldsProviderEditor {
    
    private JTable jTableFields = null; 
    private ReportQueryDialog reportQueryDialog = null;
    
    /** Creates new form OlapBrowser */
    public OlapBrowser() {
        initComponents();
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        jTree1.setModel(new DefaultTreeModel(root));
        jTree1.setCellRenderer( new OlapTreeCellRenderer());
        
        jComboBoxType.addItem(new Tag("java.lang.String",I18n.getString("OlapBrowser.ComboBox.Text")));
        jComboBoxType.addItem(new Tag("java.lang.Number", I18n.getString("OlapBrowser.ComboBox.Numeric")));
        jComboBoxType.addItem(new Tag("java.util.Date", I18n.getString("Global.List.Date")));
        jComboBoxType.addItem(new Tag("java.util.Date", I18n.getString("OlapBrowser.ComboBox.Boolean")));
        
        jComboBoxType.setSelectedIndex(0);
        
        //applyI18n();
        
        ((DefaultTreeSelectionModel)jTree1.getSelectionModel()).setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION );
    }
    
    public void setOlapResult(JROlapResult result)
    {
        jTree1.setRootVisible(false);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)jTree1.getModel().getRoot();
        if (result == null)
        {
           jTree1.updateUI();
           return;
        }
        JROlapResultAxis[] axes = result.getAxes();
        for (int i=0; i<axes.length; ++i)
        {
            JROlapResultAxis axis = axes[i];
            OlapElement axisElement = new OlapElement();
            axisElement.setAxis(i);
            axisElement.setType(OlapElement.TYPE_AXIS);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(axisElement);

             DefaultMutableTreeNode measuresNode = null;

            JROlapHierarchy[] hierarchies = axis.getHierarchiesOnAxis();
            for (int k=0; k<hierarchies.length; ++k)
            {
                JROlapHierarchy hierarchy = hierarchies[k];
                OlapElement hierarchyElement = new OlapElement();
                hierarchyElement.setType(OlapElement.TYPE_HIERARCHY);
                hierarchyElement.setDisplayName(hierarchy.getDimensionName());
                DefaultMutableTreeNode hierarchyNode = new DefaultMutableTreeNode(hierarchyElement);
                node.add(hierarchyNode);
                if (hierarchy.getDimensionName().equals("Measures"))
                {
                    measuresNode = hierarchyNode;
                    // Measures have a hierarchy of type 1, so this is enough...
                    // We will populate this node with all the available measures...
                    continue;
                }

                JROlapHierarchyLevel[] levels = hierarchy.getLevels();
                for (int j=0; j<levels.length; ++j)
                {

                    JROlapHierarchyLevel level = levels[j];
                    if (level == null) continue;

                    OlapElement levelElement = new OlapElement();
                    levelElement.setType(OlapElement.TYPE_LEVEL);
                    levelElement.setDisplayName(level.getName());
                    levelElement.setuName(level.getName());
                    levelElement.setHierarchyName(hierarchy.getDimensionName());
                    levelElement.setAxis(i);

                    DefaultMutableTreeNode levelNode = new DefaultMutableTreeNode(levelElement);
                    hierarchyNode.add(levelNode);

                }
            }

            // Add measure..
            if (measuresNode != null)
            {
                for ( int k=0; k<axis.getTupleCount(); ++k)
                {
                    JROlapMemberTuple tuple = axis.getTuple(k);
                    JROlapMember[] members = tuple.getMembers();
                    for (int j=0; j<members.length; ++j)
                    {
                        JROlapMember member = members[j];
                        if (member.getUniqueName().startsWith("[Measures]"))
                        {
                            OlapElement measureElement = new OlapElement();
                            measureElement.setType(OlapElement.TYPE_MEASURE);
                            measureElement.setDisplayName(member.getName());
                            measureElement.setuName(member.getUniqueName());
                            DefaultMutableTreeNode measureNode = new DefaultMutableTreeNode(measureElement);
                            measuresNode.add(measureNode);
                        }
                    }
                }
            }

            root.add(node);

        }

        jTree1.updateUI();
    }
    

    public JTable getJTableFields() {
        return jTableFields;
    }

    public void setJTableFields(JTable jTableFields) {
        this.jTableFields = jTableFields;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldExpression = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(250, 85));
        setPreferredSize(new java.awt.Dimension(405, 384));
        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 322));

        jTree1.setRootVisible(false);
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 4);
        add(jScrollPane1, gridBagConstraints);

        jLabel4.setForeground(new java.awt.Color(0, 51, 255));
        jLabel4.setText(I18n.getString("OlapBrowser.Label.Info")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jLabel4, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(I18n.getString("OlapBrowser.Label.FieldName")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel1.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel1.add(jTextFieldName, gridBagConstraints);

        jLabel3.setText(I18n.getString("OlapBrowser.Label.Type")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        jComboBoxType.setMinimumSize(new java.awt.Dimension(30, 19));
        jComboBoxType.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 4);
        jPanel1.add(jComboBoxType, gridBagConstraints);

        jLabel2.setText(I18n.getString("OlapBrowser.Label.Expr")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel1.add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel1.add(jTextFieldExpression, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButton2.setText(I18n.getString("Global.Button.Clear")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        jPanel2.add(jButton2, gridBagConstraints);

        jButton1.setText(I18n.getString("Global.Button.AddField")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        jPanel2.add(jButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        jTextFieldName.setText("");
        jComboBoxType.setSelectedIndex(0);
        jTextFieldExpression.setText("");
        
    }//GEN-LAST:event_jButton2ActionPerformed

    @SuppressWarnings("unchecked")
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (jTextFieldName.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(this, 
                    //I18n.getString("message.olapBrowser.notValidFieldName",
                    I18n.getString("OlapBrowser.Message.Error"),
                    I18n.getString("OlapBrowser.Message.Error2"), JOptionPane.ERROR_MESSAGE);
            return;
            
        }
        if (jTextFieldExpression.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(this,
                    //I18n.getString("message.olapBrowser.notValidFieldExpression",
                    I18n.getString("OlapBrowser.Message.Error3"),
                    I18n.getString("OlapBrowser.Message.Error2"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JRDesignField field = new JRDesignField();
        field.setName(jTextFieldName.getText().trim() );
        field.setValueClassName(""+((Tag)jComboBoxType.getSelectedItem()).getValue());
        field.setDescription( jTextFieldExpression.getText() );
        
       /*  
        try
        {
                MappingLexer lexer = new MappingLexer(new StringReader(jTextFieldExpression.getText()));
                
                //MappingParser parser = new MappingParser(lexer);
                //parser.setMappingMetadata(this);
                parser.mapping();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Field expression not valid:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        */                
        
        
        if (fieldAlreadyExists(field))
        {
            JOptionPane.showMessageDialog(this,
                    //I18n.getString("message.olapBrowser.fieldDuplicated",
                    I18n.getString("OlapBrowser.Message.Warning"), 
                    I18n.getString("OlapBrowser.Message.Error2"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Vector row = new Vector();
        row.addElement(field);
        row.addElement(field.getValueClassName());
        row.addElement(field.getDescription());
        
        DefaultTableModel dtm = (DefaultTableModel)getJTableFields().getModel();
        dtm.addRow(row);
        getJTableFields().getSelectionModel().addSelectionInterval( getJTableFields().getRowCount()-1, getJTableFields().getRowCount()-1 );
            
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked

        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1)
        {
            TreePath tp = jTree1.getSelectionPath();
            if (tp != null)
            {
                String exp = "";
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent();
                if (node.getUserObject() instanceof OlapElement)
                {
                    OlapElement element = (OlapElement) node.getUserObject();

                    if (element.getType() == OlapElement.TYPE_LEVEL)
                    {
                        String s = "";
                        switch (element.getAxis())
                        {
                            case 0:
                                s = "Columns";
                                break;
                            case 1:
                                s = "Rows";
                                break;
                            case 2:
                                s = "Pages";
                                break;
                            case 3:
                                s = "Chapters";
                                break;
                            case 4:
                                s = "Sections";
                                break;
                            default:
                                s = "Axis(" + element.getAxis() +")";
                        }

                        exp = s+"[" + element.getHierarchyName() + "]"+"["+ element.getuName()+ "]";

                        String newexp = jTextFieldExpression.getText();
                        int ss = ( jTextFieldExpression.getSelectionStart() >= 0 ) ? jTextFieldExpression.getSelectionStart() : jTextFieldExpression.getCaretPosition();
                        int se = ( jTextFieldExpression.getSelectionEnd() >= 0 ) ? jTextFieldExpression.getSelectionEnd() : jTextFieldExpression.getCaretPosition();
                        newexp = newexp.substring(0,ss) + exp + newexp.substring(se);
                        jTextFieldExpression.setText( newexp);
                        jTextFieldExpression.requestFocusInWindow();

                    }
                    else if (element.getType() == OlapElement.TYPE_MEASURE)
                    {
                        int other_axes = ((DefaultMutableTreeNode)jTree1.getModel().getRoot()).getChildCount();
                        exp = "Data(" + element.getuName();

                        for (int i=0; i<other_axes-1; ++i)
                        {
                           exp +=",?";
                        }
                        exp +=")";

                        jTextFieldName.setText( element.getDisplayName());
                        jTextFieldExpression.setText( exp );
                    }
                }

            }

        }
    }//GEN-LAST:event_jTree1MouseClicked
    
    
    private boolean fieldAlreadyExists(JRDesignField field)
    {
        boolean found = false;
            for (int j=0; j<getJTableFields().getRowCount(); ++j)
            {
               Object ff = getJTableFields().getValueAt(j, 0);
               if ( ff instanceof JRDesignField )
               {
                   if ( ((JRDesignField)ff).getName().equals(field.getName()))
                   {
                       found = true;
                       break;
                   }
               }
            }
        
        return found;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBoxType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldExpression;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
    
    /*
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButton1.setText(I18n.getString("olapBrowser.button1","Add field"));
                jButton2.setText(I18n.getString("olapBrowser.button2","Clear"));
                jLabel1.setText(I18n.getString("olapBrowser.label1","Field name"));
                jLabel2.setText(I18n.getString("olapBrowser.label2","Expression"));
                jLabel3.setText(I18n.getString("olapBrowser.label3","Type"));
                jLabel4.setText(I18n.getString("olapBrowser.label4","Double click on a tree element the get it's expression"));
                // End autogenerated code ----------------------
    }
     */

    
    int lastExecution = 0;
    
    public void queryChanged(String newQuery) {
    
        lastExecution++;
        int thisExecution = lastExecution;
        // Execute a thread to perform the query change...
        
        String error_msg = "";
        lastExecution++;
            
        int in = lastExecution;
            
        getReportQueryDialog().getJLabelStatusSQL().setText(I18n.getString("OlapBrowser.Message.SQLStatus"));
        /////////////////////////////
            
        try {
        Thread.currentThread().setContextClassLoader( IReportManager.getInstance().getReportClassLoader());
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
            
        if (in < lastExecution) return; //Abort, new execution requested
        
        OLAPQueryExecuter olapQE = new OLAPQueryExecuter(newQuery, getReportQueryDialog().getDataset().getParametersList());
        
            String lastError = "";
            try {
                
                //final mondrian.olap.Result result = olapQE.executeOlapQuery();
                final JROlapResult result = olapQE.createOlapResult();
                
                if (in < lastExecution) return; //Abort, new execution requested
                SwingUtilities.invokeAndWait( new Runnable() {
                    public void run() {
                        setOlapResult( result );
                    }
                } );
                
            } catch (Exception ex)
            {
                ex.printStackTrace();
                setOlapResult( null );
                lastError = I18n.getString("OlapBrowser.Message.Exception") +  ex.getMessage() +")";
            }
        
        getReportQueryDialog().getJLabelStatusSQL().setText(I18n.getString("OlapBrowser.Message.Ready"));
    }

    public ReportQueryDialog getReportQueryDialog() {
        return reportQueryDialog;
    }

    public void setReportQueryDialog(ReportQueryDialog reportQueryDialog) {
        this.reportQueryDialog = reportQueryDialog;
    }
}
