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
package com.jaspersoft.ireport.components.barcode;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.util.List;
import javax.swing.DefaultListModel;
import net.sf.jasperreports.components.barbecue.StandardBarbecueComponent;
import net.sf.jasperreports.components.barcode4j.BarcodeComponent;
import net.sf.jasperreports.components.barcode4j.CodabarComponent;
import net.sf.jasperreports.components.barcode4j.Code128Component;
import net.sf.jasperreports.components.barcode4j.Code39Component;
import net.sf.jasperreports.components.barcode4j.DataMatrixComponent;
import net.sf.jasperreports.components.barcode4j.EAN128Component;
import net.sf.jasperreports.components.barcode4j.EAN13Component;
import net.sf.jasperreports.components.barcode4j.EAN8Component;
import net.sf.jasperreports.components.barcode4j.Interleaved2Of5Component;
import net.sf.jasperreports.components.barcode4j.PDF417Component;
import net.sf.jasperreports.components.barcode4j.POSTNETComponent;
import net.sf.jasperreports.components.barcode4j.RoyalMailCustomerComponent;
import net.sf.jasperreports.components.barcode4j.UPCAComponent;
import net.sf.jasperreports.components.barcode4j.UPCEComponent;
import net.sf.jasperreports.components.barcode4j.USPSIntelligentMailComponent;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

/**
 *
 * @author gtoffoli
 */
public class BarcodeChooserDialog extends javax.swing.JDialog {

    private JRDesignComponentElement component = null;

    /** Creates new form BarcodeChooserDialog */
    public BarcodeChooserDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        jList1.setCellRenderer(new BarcodeListRenderer());
        jList2.setCellRenderer(new BarcodeListRenderer());

        DefaultListModel listModel = new DefaultListModel();
        List<BarcodeDescriptor> barbecueBarcodes = BarcodeDescriptor.getBarbecueBarcodes();
        for (BarcodeDescriptor b : barbecueBarcodes)
        {
            listModel.addElement(b);
        }
        jList1.setModel(listModel);


        DefaultListModel listModel2 = new DefaultListModel();
        List<BarcodeDescriptor> barcode4jBarcodes = BarcodeDescriptor.getBarcode4jBarcodes();
        for (BarcodeDescriptor b : barcode4jBarcodes)
        {
            listModel2.addElement(b);
        }
        jList2.setModel(listModel2);
        
        
        jList1.updateUI();
        jList2.updateUI();

        setLocationRelativeTo(null);
    }

    public static  JRDesignComponentElement createBarcodeComponent(int libType, String barcodeName) {

        JRDesignComponentElement component = new JRDesignComponentElement();
        component.setWidth(200);
        component.setHeight(50);

        if (libType == 0)
        {

            StandardBarbecueComponent componentImpl = new StandardBarbecueComponent();

            componentImpl.setType(barcodeName);
            componentImpl.setEvaluationTimeValue( EvaluationTimeEnum.NOW);

            JRDesignExpression exp = Misc.createExpression("java.lang.String", "\"1234\"");

            componentImpl.setCodeExpression(exp);

            component.setComponent(componentImpl);
            component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "barbecue"));
        }
        else
        {
            BarcodeComponent componentImpl = null;
            if (barcodeName.equals("Codabar"))
            {
                componentImpl = new CodabarComponent();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "Codabar"));

            }
            else if (barcodeName.equals("Code128"))
            {
                componentImpl = new Code128Component();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "Code128"));

            }
            else if (barcodeName.equals("DataMatrix"))
            {
                componentImpl = new DataMatrixComponent();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "DataMatrix"));

            }
            else if (barcodeName.equals("EAN128"))
            {
                componentImpl = new EAN128Component();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "EAN128"));

            }
            else if (barcodeName.equals("Royal Mail Customer"))
            {
                componentImpl = new RoyalMailCustomerComponent();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "RoyalMailCustomer"));

            }
            else if (barcodeName.equals("USPS Intelligent Mail"))
            {
                componentImpl = new USPSIntelligentMailComponent();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "USPSIntelligentMail"));

            }
            else if (barcodeName.equals("Int2of5"))
            {
                componentImpl = new Interleaved2Of5Component();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "Interleaved2Of5"));

            }
            else if (barcodeName.equals("Code39"))
            {
                componentImpl = new Code39Component();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "Code39"));

            }

            else if (barcodeName.equals("UPCA"))
            {
                componentImpl = new UPCAComponent();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "UPCA"));

            }
            else if (barcodeName.equals("UPCE"))
            {
                componentImpl = new UPCEComponent();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "UPCE"));

            }
            else if (barcodeName.equals("EAN13"))
            {
                componentImpl = new EAN13Component();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "EAN13"));

            }
            else if (barcodeName.equals("EAN8"))
            {
                componentImpl = new EAN8Component();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "EAN8"));

            }
            else if (barcodeName.equals("PostNet"))
            {
                componentImpl = new POSTNETComponent();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "POSTNET"));

            }
            else if (barcodeName.equals("PDF417"))
            {
                componentImpl = new PDF417Component();
                component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "PDF417"));

            }

            componentImpl.setEvaluationTimeValue( EvaluationTimeEnum.NOW);
            componentImpl.setTextPosition("bottom");
            JRDesignExpression exp = new JRDesignExpression();
            exp.setValueClassName("java.lang.String");

            componentImpl.setCodeExpression(exp);

            component.setComponent(componentImpl);



        }

        return component;

    }

    private void closeWindow(int lib) {
//        if (jCheckBox1.isSelected())
//        {
//            IReportManager.getPreferences().putInt("defaultBarcodeLibrary", lib);
//        }
        this.setVisible(false);
        this.dispose();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/components/barcode/barcode-32.png"))); // NOI18N
        jLabel1.setText(org.openide.util.NbBundle.getMessage(BarcodeChooserDialog.class, "BarcodeChooserDialog.jLabel1.text")); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jButton1.setText(org.openide.util.NbBundle.getMessage(BarcodeChooserDialog.class, "BarcodeChooserDialog.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(org.openide.util.NbBundle.getMessage(BarcodeChooserDialog.class, "BarcodeChooserDialog.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(BarcodeChooserDialog.class, "BarcodeChooserDialog.jLabel2.text")); // NOI18N

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(BarcodeChooserDialog.class, "BarcodeChooserDialog.jLabel3.text")); // NOI18N

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButton2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String barcodeType = "Code39 (Extended)";
        if (jList1.getSelectedIndex() >= 0)
        {
            barcodeType = ((BarcodeDescriptor)jList1.getSelectedValue()).getName();
        }
        setComponent(createBarcodeComponent(0, barcodeType));
        closeWindow(0);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String barcodeType = "Code128";
        if (jList2.getSelectedIndex() >= 0)
        {
            barcodeType = ((BarcodeDescriptor)jList2.getSelectedValue()).getName();
        }
        setComponent(createBarcodeComponent(1, barcodeType));
        closeWindow(1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (evt.getClickCount() == 2)
        {
            jButton1ActionPerformed(null);
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked
        if (evt.getClickCount() == 2)
        {
            jButton2ActionPerformed(null);
        }
    }//GEN-LAST:event_jList2MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the component
     */
    public JRDesignComponentElement getComponent() {
        return component;
    }

    /**
     * @param component the component to set
     */
    public void setComponent(JRDesignComponentElement component) {
        this.component = component;
    }

  

}
