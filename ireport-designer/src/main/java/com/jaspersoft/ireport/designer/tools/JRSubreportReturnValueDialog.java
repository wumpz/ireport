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
package com.jaspersoft.ireport.designer.tools;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignSubreportReturnValue;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.type.CalculationEnum;

/**
 * @author Administrator
 */
public class JRSubreportReturnValueDialog
    extends javax.swing.JDialog
{

    /** Creates new form JRParameterDialog */
    private JRDesignSubreportReturnValue subreportReturnValue = null;

    /**
     * Creates a new JRVariableDialog object.
     */
    public JRSubreportReturnValueDialog(Dialog parent) 
    {
         super(parent);
         initAll();
    }

    /** Creates new form ReportQueryFrame */
    public JRSubreportReturnValueDialog(Frame parent) 
    {
         super(parent);
         initAll();
    }
    
    public void initAll(){ 
    
        setModal(true);
        initComponents();
        //applyI18n();
        setTypes();
        updateVariables();
        jComboBoxSubreportVariable.setSelectedItem("");
        this.setSize(380, 260);
        setLocationRelativeTo(null);
        
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);


        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButtonOK);
    }

    /**
     * DOCUMENT ME!
     */
    public void setTypes()
    {
        this.jComboBoxCalculationType.addItem(new Tag( CalculationEnum.NOTHING,I18n.getString("JRSubreportReturnValueDialog.ComboBox.Nothing")));
        this.jComboBoxCalculationType.addItem(new Tag( CalculationEnum.COUNT, I18n.getString("JRSubreportReturnValueDialog.ComboBox.Count")));
        this.jComboBoxCalculationType.addItem(new Tag( CalculationEnum.DISTINCT_COUNT, I18n.getString("JRSubreportReturnValueDialog.ComboBox.DistinctCount")));
        this.jComboBoxCalculationType.addItem(new Tag( CalculationEnum.SUM, I18n.getString("JRSubreportReturnValueDialog.ComboBox.Sum")));
        this.jComboBoxCalculationType.addItem(new Tag( CalculationEnum.AVERAGE, I18n.getString("JRSubreportReturnValueDialog.ComboBox.Average")));
        this.jComboBoxCalculationType.addItem(new Tag( CalculationEnum.LOWEST, I18n.getString("JRSubreportReturnValueDialog.ComboBox.Lowest")));
        this.jComboBoxCalculationType.addItem(new Tag( CalculationEnum.HIGHEST, I18n.getString("JRSubreportReturnValueDialog.ComboBox.Highest")));
        this.jComboBoxCalculationType.addItem(new Tag( CalculationEnum.STANDARD_DEVIATION, I18n.getString("JRSubreportReturnValueDialog.ComboBox.StandardDeviation")));
        this.jComboBoxCalculationType.addItem(new Tag( CalculationEnum.VARIANCE, I18n.getString("JRSubreportReturnValueDialog.ComboBox.Variance")));
        // CALCULATION_SYSTEM Not allowed as calc type for return values... [bug 0004349]
        // this.jComboBoxCalculationType.addItem(new Tag( new Byte( JRDesignVariable.CALCULATION_SYSTEM ), I18n.getString("JRSubreportReturnValueDialog.ComboBox.System")));
        this.jComboBoxCalculationType.addItem(new Tag( CalculationEnum.FIRST, I18n.getString("JRSubreportReturnValueDialog.ComboBox.First")));
    }

    /**
     * Read all available variables from the active report and popute the combobox
     */
    public void updateVariables()
    {
        jComboBoxSubreportVariable.removeAllItems();
        jComboBoxVariable.removeAllItems();
        try {
            List variables = IReportManager.getInstance().getActiveReport().getVariablesList();

            for (int i=0; i<variables.size(); ++i)
            {
                JRDesignVariable var = (JRDesignVariable)variables.get(i);
                if (var.isSystemDefined())
                {
                    jComboBoxSubreportVariable.addItem( var.getName());
                }
                else
                {
                    jComboBoxVariable.addItem( var.getName());
                }
            }
        } catch (Exception ex)
        {
            
        }
    }

    /**
     * This method is called from within the constructor to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBoxCalculationType = new javax.swing.JComboBox();
        jComboBoxVariable = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldNameIncrementerFactoryClass = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jComboBoxSubreportVariable = new javax.swing.JComboBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/jaspersoft/ireport/locale/Bundle"); // NOI18N
        setTitle(bundle.getString("JRSubreportReturnValueDialog.Title.AddModVariable")); // NOI18N
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(bundle.getString("JRSubreportReturnValueDialog.Label.SubreportVariable")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setText(bundle.getString("JRSubreportReturnValueDialog.Label.CalculationType")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel5.setText(bundle.getString("JRSubreportReturnValueDialog.Label.LocalDestinationVariable")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxCalculationType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxVariable, gridBagConstraints);

        jLabel8.setText(bundle.getString("JRSubreportReturnValueDialog.Label.CustomIncrementerFactoryClass")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel8, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jTextFieldNameIncrementerFactoryClass, gridBagConstraints);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonOK.setMnemonic('o');
        jButtonOK.setText(bundle.getString("Global.Button.Ok")); // NOI18N
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonOK);

        jButtonCancel.setMnemonic('c');
        jButtonCancel.setText(bundle.getString("Global.Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonCancel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jPanel1, gridBagConstraints);

        jComboBoxSubreportVariable.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        getContentPane().add(jComboBoxSubreportVariable, gridBagConstraints);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents
    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonCancelActionPerformed
    {
        setVisible(false);
        this.setDialogResult(javax.swing.JOptionPane.CANCEL_OPTION);
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonOKActionPerformed
    {

        if ((this.jComboBoxSubreportVariable.getSelectedItem()+"").trim().length() <= 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
                    I18n.getString("JRSubreportReturnValueDialog.Message.Warning"),
                    I18n.getString("JRSubreportReturnValueDialog.Message.Error"),
                    javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        }

        subreportReturnValue = new JRDesignSubreportReturnValue();
        
        subreportReturnValue.setSubreportVariable( this.jComboBoxSubreportVariable.getSelectedItem()+"" );
        
        if (this.jComboBoxVariable.getSelectedItem() == null)
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
                    I18n.getString("JRSubreportReturnValueDialog.Message.Warning2"),
                    I18n.getString("JRSubreportReturnValueDialog.Message.Error2"),
                    javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        }
        subreportReturnValue.setToVariable( jComboBoxVariable.getSelectedItem()+"");

        Object t = this.jComboBoxCalculationType.getSelectedItem();
        if (t != null && t instanceof Tag) subreportReturnValue.setCalculation(  (CalculationEnum)((Tag)t).getValue() );
        
        if (this.jTextFieldNameIncrementerFactoryClass.getText().length() == 0)
        {
            subreportReturnValue.setIncrementerFactoryClassName(null);
        }
        else
        {
            subreportReturnValue.setIncrementerFactoryClassName( this.jTextFieldNameIncrementerFactoryClass.getText());
        }
        setVisible(false);
        this.setDialogResult(javax.swing.JOptionPane.OK_OPTION);
        dispose();
    }//GEN-LAST:event_jButtonOKActionPerformed

    /**
     * Closes the dialog
     * @param evt DOCUMENT ME!
     */
    private void closeDialog(java.awt.event.WindowEvent evt)//GEN-FIRST:event_closeDialog
    {
        setVisible(false);
        this.setDialogResult(javax.swing.JOptionPane.CLOSED_OPTION);
        dispose();
    }//GEN-LAST:event_closeDialog

    /**
     * Getter for property tmpParameter.
     * 
     * @return Value of property tmpParameter.
     */
    public JRDesignSubreportReturnValue getSubreportReturnValue()
    {

        return subreportReturnValue;
    }

    /**
     * Setter for property tmpParameter.
     * 
     * @param tmpVariable New value of property tmpParameter.
     */
    public void setSubreportReturnValue(JRDesignSubreportReturnValue tmpSubreportReturnValue)
    {
        this.jComboBoxSubreportVariable.setSelectedItem(new String(tmpSubreportReturnValue.getSubreportVariable() ));
        for (int i=0; i<jComboBoxVariable.getItemCount(); ++i)
        {

            Object var = jComboBoxVariable.getItemAt(i);
            if ((var+"").equals( tmpSubreportReturnValue.getToVariable() ))
            {
                jComboBoxVariable.setSelectedIndex(i);
                break;
            }
        }
        
        Misc.setComboboxSelectedTagValue( jComboBoxCalculationType, tmpSubreportReturnValue.getCalculationValue() );
        this.jTextFieldNameIncrementerFactoryClass.setText(tmpSubreportReturnValue.getIncrementerFactoryClassName());
    }

    /**
     * Getter for property dialogResult.
     * 
     * @return Value of property dialogResult.
     */
    public int getDialogResult()
    {

        return dialogResult;
    }

    /**
     * Setter for property dialogResult.
     * 
     * @param dialogResult New value of property dialogResult.
     */
    public void setDialogResult(int dialogResult)
    {
        this.dialogResult = dialogResult;
    }

 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JComboBox jComboBoxCalculationType;
    private javax.swing.JComboBox jComboBoxSubreportVariable;
    private javax.swing.JComboBox jComboBoxVariable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldNameIncrementerFactoryClass;
    // End of variables declaration//GEN-END:variables
    private int dialogResult;

    /*
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonCancel.setText(I18n.getString("jRSubreportReturnValueDialog.buttonCancel","Cancel"));
                jButtonOK.setText(I18n.getString("jRSubreportReturnValueDialog.buttonOK","OK"));
                jLabel1.setText(I18n.getString("jRSubreportReturnValueDialog.label1","Subreport variable"));
                jLabel2.setText(I18n.getString("jRSubreportReturnValueDialog.label2","Calculation type"));
                jLabel5.setText(I18n.getString("jRSubreportReturnValueDialog.label5","Local destination variable"));
                jLabel8.setText(I18n.getString("jRSubreportReturnValueDialog.label8","Custom Incrementer Factory Class"));
                // End autogenerated code ----------------------
                this.setTitle(I18n.getString("jRSubreportReturnValueDialog.title","Add/modify variable"));
                jButtonCancel.setMnemonic(I18n.getString("jRSubreportReturnValueDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOK.setMnemonic(I18n.getString("jRSubreportReturnValueDialog.buttonOKMnemonic","o").charAt(0));
    }
     */
}
