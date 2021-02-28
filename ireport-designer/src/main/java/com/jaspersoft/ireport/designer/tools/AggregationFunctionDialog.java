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

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.type.CalculationEnum;

/**
 *
 * @author gtoffoli
 */
public class AggregationFunctionDialog extends java.awt.Dialog {

    public static int STRING_SET = 0;
    public static int NUMERIC_SET = 1;

    public static int DEFAULT_AS_FUNCTION = 0;
    public static int DEFAULT_AS_VALUE = 1;


    /** Creates new form AggregationFunctionDialog */
    public AggregationFunctionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);

        jLabel1.setText(I18n.getString("AggregationFunctionDialog.jLabel1.text")); // NOI18N
        jRadioButtonValue.setText(I18n.getString("AggregationFunctionDialog.jRadioButtonValue.text")); // NOI18N
        jButtonOk.setText(I18n.getString("AggregationFunctionDialog.jButtonOk.text")); // NOI18N
        jRadioButtonAggregation.setText(I18n.getString("AggregationFunctionDialog.jRadioButtonAggregation.text")); // NOI18N
        setTitle("");
    }

    public void setDefaultSelection(int defaultAs)
    {
        if (defaultAs == DEFAULT_AS_FUNCTION) jRadioButtonAggregation.setSelected(true);
        if (defaultAs == DEFAULT_AS_VALUE) jRadioButtonValue.setSelected(true);

    }

    public void setFunctionSet(int set)
    {
        jComboBox1.removeAllItems();

        jComboBox1.addItem(new Tag(CalculationEnum.COUNT, I18n.getString("VariableNode.Property.Count")));
        jComboBox1.addItem(new Tag(CalculationEnum.DISTINCT_COUNT, I18n.getString("VariableNode.Property.DistinctCount")));

        jComboBox1.setSelectedIndex(0);

        if (set == NUMERIC_SET)
        {
            //jComboBox1.addItem(new Tag(new Byte(JRDesignVariable.CALCULATION_NOTHING), I18n.getString("VariableNode.Property.Nothing")));
            jComboBox1.addItem(new Tag(CalculationEnum.SUM, I18n.getString("VariableNode.Property.Sum")));
            jComboBox1.addItem(new Tag(CalculationEnum.AVERAGE, I18n.getString("VariableNode.Property.Average")));
            jComboBox1.addItem(new Tag(CalculationEnum.LOWEST, I18n.getString("VariableNode.Property.Lowest")));
            jComboBox1.addItem(new Tag(CalculationEnum.HIGHEST, I18n.getString("VariableNode.Property.Highest")));
            jComboBox1.addItem(new Tag(CalculationEnum.STANDARD_DEVIATION, I18n.getString("VariableNode.Property.StandardDeviation")));
            jComboBox1.addItem(new Tag(CalculationEnum.VARIANCE, I18n.getString("VariableNode.Property.Variance")));
            jComboBox1.addItem(new Tag(CalculationEnum.SYSTEM, I18n.getString("VariableNode.Property.System")));
            jComboBox1.addItem(new Tag(CalculationEnum.FIRST, I18n.getString("VariableNode.Property.First")));
            setSelectedFunction(CalculationEnum.SUM);
        }

    }

    public void setSelectedFunction(String function)
    {
        if (function == null) return;
        if (function.equals("Count")) setSelectedFunction( CalculationEnum.COUNT );
        if (function.equals("DistinctCount")) setSelectedFunction(CalculationEnum.DISTINCT_COUNT);
        if (function.equals("Sum")) setSelectedFunction(CalculationEnum.SUM);
        if (function.equals("Average")) setSelectedFunction(CalculationEnum.AVERAGE);
        if (function.equals("Lowest")) setSelectedFunction(CalculationEnum.LOWEST);
        if (function.equals("Highest")) setSelectedFunction(CalculationEnum.HIGHEST);
        if (function.equals("standardDeviation")) setSelectedFunction(CalculationEnum.STANDARD_DEVIATION);
        if (function.equals("variance")) setSelectedFunction(CalculationEnum.VARIANCE);
        if (function.equals("first")) setSelectedFunction(CalculationEnum.FIRST);

        // No function found...
        return;
    }
    public void setSelectedFunction(CalculationEnum function)
    {
        if (function == null) return;
        Misc.setComboboxSelectedTagValue(jComboBox1, function);
    }

    public CalculationEnum getSelectedFunction()
    {
        if (jRadioButtonAggregation.isSelected())
        {
            Object obj = jComboBox1.getSelectedItem();
            if (obj != null)
            {
                Tag t = (Tag)obj;
                return (CalculationEnum)t.getValue();
            }
        }

        return null;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jRadioButtonValue = new javax.swing.JRadioButton();
        jRadioButtonAggregation = new javax.swing.JRadioButton();
        jComboBox1 = new javax.swing.JComboBox();
        jButtonOk = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setTitle(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jLabel1.setText("What...");

        buttonGroup1.add(jRadioButtonValue);
        jRadioButtonValue.setSelected(true);
        jRadioButtonValue.setText("Value");

        buttonGroup1.add(jRadioButtonAggregation);
        jRadioButtonAggregation.setText("Aggregation");

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButtonOk.setText("Ok");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jRadioButtonValue)
                        .addContainerGap())
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                            .add(jRadioButtonAggregation)
                            .addContainerGap())
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                                .addContainerGap())
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                                .addContainerGap())
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                .add(jButtonOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(31, 31, 31)
                .add(jComboBox1, 0, 225, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(5, 5, 5)
                .add(jLabel1)
                .add(8, 8, 8)
                .add(jRadioButtonValue)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jRadioButtonAggregation)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButtonOk)
                .add(21, 21, 21))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

        jRadioButtonAggregation.setSelected(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
       this.setVisible(true);
       this.dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButtonAggregation;
    private javax.swing.JRadioButton jRadioButtonValue;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

}
