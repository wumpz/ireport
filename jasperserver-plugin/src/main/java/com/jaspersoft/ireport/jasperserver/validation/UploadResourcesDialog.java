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

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepoImageCache;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.util.JRExpressionUtil;

/**
 *
 * @author  gtoffoli
 */
public class UploadResourcesDialog extends javax.swing.JDialog implements Runnable {
    
    private JrxmlValidationDialog validationDialog = null;
    private java.util.List resourceItems = null;
        
    /** Creates new form UploadResourcesDialog */
    public UploadResourcesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        jProgressBar1.setMinimum(0);
        jProgressBar1.setMaximum(100);
        jProgressBar1.setValue(0);
        
        setLocationRelativeTo(null);
    }
    
    public void setLabel(String label)
    {
        jLabel.setText( label );
    }
    
    public void setCompletation(int d)
    {
        jProgressBar1.setValue(d);
    }
    
    
    public void startProcessing()
    {
        Thread t = new Thread(this);
        t.start();
    }
    
    public void run()
    {
        jProgressBar1.setValue(0);
        
        jLabel.setText(JasperServerManager.getString("uploadResourcesDialog.adaptingJRXML","Adapting JRXML source...") );
        JrxmlValidationDialog vd = getValidationDialog();
        
        jProgressBar1.setValue(5);
        
         try {
            
            for (int i=0; i< resourceItems.size(); ++i)
            {
                jProgressBar1.setValue(15);
                ElementValidationItem evi = (ElementValidationItem)resourceItems.get(i);

                ResourceDescriptor newDescriptor = new ResourceDescriptor();
                newDescriptor.setUriString( evi.getParentFolder() + "/" + evi.getResourceName());
                newDescriptor.setParentFolder( evi.getParentFolder());
                newDescriptor.setName( evi.getResourceName() );
                newDescriptor.setLabel( evi.getResourceName() );
                newDescriptor.setIsNew(true);

                if (evi.isStoreAsLink())
                {
                     newDescriptor.setHasData(true);
                     newDescriptor.setIsReference(true);
                     newDescriptor.setReferenceUri( evi.getReferenceUri()  );
                     newDescriptor.setWsType(  ResourceDescriptor.TYPE_REFERENCE);
                }
                else
                {
                    newDescriptor.setHasData(true);

                    if (evi instanceof ImageElementValidationItem)
                    {
                        ImageElementValidationItem iev = (ImageElementValidationItem)evi;
                        //Image img = ((ImageReportElement)iev.getReportElement()).getImg();
                        JRDesignExpression exp = (JRDesignExpression)((JRDesignImage)iev.getReportElement()).getExpression();
                        exp.setText(iev.getProposedExpression());

                        // Add the image to the FileResolver so it can be found by the visual
                        // components at design time...
                        RepoImageCache.getInstance().put(JRExpressionUtil.getSimpleExpressionText(exp), iev.getOriginalFileName());

                        jProgressBar1.setValue(20);
                        newDescriptor.setWsType(  ResourceDescriptor.TYPE_IMAGE );
                        //newDescriptor.setParentFolder( (vd.getReportUnit() != null) ? vd.getReportUnit().getDescriptor().getUriString() : "/");
                    }
                    else if (evi instanceof SubReportElementValidationItem)
                    {
                        SubReportElementValidationItem iev = (SubReportElementValidationItem)evi;
                        JRDesignExpression exp = (JRDesignExpression)((JRDesignSubreport)iev.getReportElement()).getExpression();
                        exp.setText(iev.getProposedExpression());
                        newDescriptor.setWsType(  ResourceDescriptor.TYPE_JRXML );
                    }
                    else if (evi instanceof TemplateElementValidationItem)
                    {
                        TemplateElementValidationItem iev = (TemplateElementValidationItem)evi;
                        JRDesignExpression exp = (JRDesignExpression)(iev.getTemplate().getSourceExpression());
                        exp.setText(iev.getProposedExpression());
                        newDescriptor.setWsType(  ResourceDescriptor.TYPE_STYLE_TEMPLATE );
                    }
                }
                jLabel.setText(
                        JasperServerManager.getFormattedString("uploadResourcesDialog.uploadingResource","Uploading {0}",new Object[]{evi.getOriginalFileName().getName()}));
                
                //System.out.println("Modifing resource with RU " + vd.getReportUnit());
                //System.out.flush();
                // Check if a resource with this name already exists...
                List existingResources = vd.getServer().getWSClient().list(vd.getReportUnit().getDescriptor());
                boolean found = false;
                for (int k=0; k<existingResources.size(); ++k)
                {
                   ResourceDescriptor rd = (ResourceDescriptor) existingResources.get(k);
                   if (rd.getName() != null &&
                       rd.getName().equals(newDescriptor.getName()))
                   {
                       found = true;
                       break;
                   }
                }

                if (!found)
                {
                    vd.getServer().getWSClient().modifyReportUnitResource(
                                (vd.getReportUnit() != null) ? vd.getReportUnit().getDescriptor().getUriString() : null,
                                newDescriptor, (evi.isStoreAsLink()) ? null : evi.getOriginalFileName());
                }
                jProgressBar1.setValue( (int)(100.0/(double)i));
            }
            
            vd.saveReport();
            
            //IRPlugin.getMainInstance().getRepositoryExplorer().refreshContentNodeObject(vd.getReportUnit());
            
        } catch (Exception ex)
        {
                final Exception ex2 = ex;
                ex.printStackTrace();
                try {
                    SwingUtilities.invokeAndWait( new Runnable(){

                    public void run()
                    {
                        setVisible(false);
                        dispose();

                        JOptionPane.showMessageDialog(Misc.getMainFrame(), 
                                JasperServerManager.getFormattedString("uploadResourcesDialog.uploadingResourceError","An error occurred during resource upload:\n{0}",new Object[]{ex2.getMessage()}),
                                "Error",
                        JOptionPane.ERROR_MESSAGE);
                    }
                });

                } catch (Exception ex3){
                }
            
            ex.printStackTrace();
        }
        finally {
          
            try {
            SwingUtilities.invokeAndWait( new Runnable(){
            public void run()
                {
                    setVisible(false);
                    dispose();
                }
            });
            } catch (Exception ex3){
                }
        }
        getValidationDialog().elaborationFinished(true);
    }
    
    public void setVisible(boolean b)
    {
        if (b) startProcessing();
        super.setVisible(b);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        jLabel.setText("Uploading resources...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 8);
        getContentPane().add(jLabel, gridBagConstraints);

        jProgressBar1.setPreferredSize(new java.awt.Dimension(250, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
        getContentPane().add(jProgressBar1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    

    public JrxmlValidationDialog getValidationDialog() {
        return validationDialog;
    }

    public void setValidationDialog(JrxmlValidationDialog validationDialog) {
        this.validationDialog = validationDialog;
    }

    public java.util.List getResourceItems() {
        return resourceItems;
    }

    public void setResourceItems(java.util.List resourceItems) {
        this.resourceItems = resourceItems;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
    
}
