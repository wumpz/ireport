/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SampleDatabaseConnectionHeaderPanel.java
 *
 * Created on 11-gen-2010, 16.52.50
 */

package com.jaspersoft.ireport.samples.db;

import java.awt.BorderLayout;
import java.awt.Component;

/**
 *
 * @author gtoffoli
 */
public class SampleDatabaseConnectionHeaderPanel extends javax.swing.JPanel {

    /** Creates new form SampleDatabaseConnectionHeaderPanel */
    public SampleDatabaseConnectionHeaderPanel() {
        initComponents();
        jXTaskPane1.setAnimated(true);
        jXTaskPane1.setCollapsed(true);
    }

    public void setMainPanel(Component c)
    {
        this.jXTaskPane1.add(c);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jXTaskPaneContainer1 = new org.jdesktop.swingx.JXTaskPaneContainer();
        jXTaskPane1 = new org.jdesktop.swingx.JXTaskPane();

        setLayout(new java.awt.BorderLayout());

        jLabel1.setText(org.openide.util.NbBundle.getMessage(SampleDatabaseConnectionHeaderPanel.class, "SampleDatabaseConnectionHeaderPanel.jLabel1.text")); // NOI18N
        add(jLabel1, java.awt.BorderLayout.NORTH);

        jXTaskPane1.setCollapsed(true);
        jXTaskPane1.setTitle(org.openide.util.NbBundle.getMessage(SampleDatabaseConnectionHeaderPanel.class, "SampleDatabaseConnectionHeaderPanel.jXTaskPane1.title")); // NOI18N
        jXTaskPaneContainer1.add(jXTaskPane1);

        add(jXTaskPaneContainer1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane1;
    private org.jdesktop.swingx.JXTaskPaneContainer jXTaskPaneContainer1;
    // End of variables declaration//GEN-END:variables

}
