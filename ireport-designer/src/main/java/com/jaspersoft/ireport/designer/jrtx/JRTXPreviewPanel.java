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
package com.jaspersoft.ireport.designer.jrtx;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.draw.DrawVisitor;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.openide.nodes.Node;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class JRTXPreviewPanel extends javax.swing.JPanel 
{
    private JRTXVisualView mainVisualView = null;
    
    private  final JasperDesign STYLE_REPORT;
    private  DrawVisitor drawVisitor;
    
    /** Creates new form JRCTXPreviewPanel */
    public JRTXPreviewPanel(JRTXVisualView vv)
    {
        initComponents();

        //JRCTXExtensionsRegistryFactory.addView(vv);

        try
        {
            STYLE_REPORT = JRXmlLoader.load(this.getClass().getResourceAsStream("/com/jaspersoft/ireport/designer/resources/style.jrxml"));
            

        }
        catch(JRException e)
        {
            throw new JRRuntimeException(e);
        }

        this.mainVisualView = vv;


        ((PaintablePanel)jPanelGraphicElements).setPainter(new PaintDelegated() {

                public void paintComponent(JComponent component, Graphics2D g) {
                    paintGraphicsElements(component, g);
                }
            });

        ((PaintablePanel)jPanelTextElements).setPainter(new PaintDelegated() {

                public void paintComponent(JComponent component, Graphics2D g) {
                    paintTextElements(component, g);
                }
            });
    }

    public void paintGraphicsElements(JComponent component, Graphics2D gr) {

            drawVisitor = new DrawVisitor(STYLE_REPORT, null);
            drawVisitor.setGraphics2D(gr);

            Paint p = gr.getPaint();
            Stroke s = gr.getStroke();

            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);


            AffineTransform af = (AffineTransform) gr.getTransform().clone();
            AffineTransform af_new = (AffineTransform) gr.getTransform().clone();
            int gap = 15;
            af_new.translate(0, 10);
            
            try {
                JRDesignElement[] elements = new JRDesignElement[4];
                elements[0] = (JRDesignElement) STYLE_REPORT.getTitle().getElementByKey("rectangle");
                elements[1] = (JRDesignElement) STYLE_REPORT.getTitle().getElementByKey("ellipse");
                elements[2] = (JRDesignElement) STYLE_REPORT.getTitle().getElementByKey("line");
                elements[3] = (JRDesignElement) STYLE_REPORT.getTitle().getElementByKey("image");

                int requiredSpace = 0;
                for (int i=0; i<elements.length; ++i)
                {
                    requiredSpace += elements[i].getWidth();
                }

                gap = Math.max(15, ((component.getWidth() - component.getInsets().left - component.getInsets().right)-requiredSpace)/(1+elements.length) );

                for (int i=0; i<elements.length; ++i)
                {
                    af_new.translate(gap, 0);
                    gr.setTransform(af_new);
                    elements[i].visit( drawVisitor );
                    af_new.translate(elements[i].getWidth(), 0);
                }

            } catch (Exception ex){
                ex.printStackTrace();
            }
            finally
            {
                gr.setPaint(p);
                gr.setStroke(s);
                gr.setTransform(af);
            }
            

    }

    public void paintTextElements(JComponent component, Graphics2D gr) {

            drawVisitor = new DrawVisitor(STYLE_REPORT, null);
            drawVisitor.setGraphics2D(gr);

            Paint p = gr.getPaint();
            Stroke s = gr.getStroke();

            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);


            AffineTransform af = (AffineTransform) gr.getTransform().clone();
            AffineTransform af_new = (AffineTransform) gr.getTransform().clone();
            int gap = 15;
            af_new.translate(15, 10);

            try {
                JRDesignElement[] elements = new JRDesignElement[2];
                elements[0] = (JRDesignElement) STYLE_REPORT.getTitle().getElementByKey("statictext");
                elements[1] = (JRDesignElement) STYLE_REPORT.getTitle().getElementByKey("textfield");

                for (int i=0; i<2; ++i)
                {
                    af_new.translate(0, 15);
                    gr.setTransform(af_new);
                    elements[i].visit( drawVisitor );
                    af_new.translate(0, elements[i].getHeight() );
                }

            } catch (Exception ex){
                ex.printStackTrace();
            }
            finally
            {
                gr.setPaint(p);
                gr.setStroke(s);
                gr.setTransform(af);
            }


    }

    public void modelChanged()
    {
        Mutex.EVENT.readAccess(new Runnable() {

            public void run() {

                if (getMainVisualView() != null)
                {
                    Node[] selectedNodes = getMainVisualView().getExplorerManager().getSelectedNodes();

                    if (selectedNodes.length > 0 &&
                        selectedNodes[0] instanceof StyleNode)
                    {
                        
                        setPreviewStyle(((StyleNode)selectedNodes[0]).getStyle(), ((StyleNode)selectedNodes[0]).getTemplate());
                        
                    }
                    else
                    {
                        setPreviewStyle(null, null);
                    }

                }
            }
        });
        
        //jTextArea1.setText( jTextArea1.getText()+"\nModel changed!" );

        // do what you need with:
        //mainVisualView.getModel().getTemplate();
    }


    public void setPreviewStyle(JRStyle style, JRTemplate template)
    {
        // Remove the styles...
         STYLE_REPORT.setDefaultStyle(null);
         STYLE_REPORT.getStylesList().clear();
         STYLE_REPORT.getStylesMap().clear();

        if (style == null)
        {
            jLabelStyleName.setText("No style selected");
            jPanelGraphicElements.repaint();
            jPanelTextElements.repaint();
            setElementsStyle(null);
        }
        else
        {
            jLabelStyleName.setText(style.getName());
            try {
                JRStyle[] styles = template.getStyles();
                for (int i=0; i<styles.length; ++i)
                {
                    STYLE_REPORT.addStyle(styles[i]);
                    if (styles[i].isDefault()) {
                        STYLE_REPORT.setDefaultStyle(style);
                    }
                }
                
                // The current style has the precedence...
                if (style.isDefault()) {
                    STYLE_REPORT.setDefaultStyle(style);
                }
                setElementsStyle(style);
                jPanelGraphicElements.repaint();
                jPanelTextElements.repaint();
            } catch (JRException ex)
            {

            }

        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelStyleName = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jPanelGraphicElements = new PaintablePanel();
        jLabel2 = new javax.swing.JLabel();
        jPanelTextElements = new PaintablePanel();

        jLabelStyleName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelStyleName.setText(org.openide.util.NbBundle.getMessage(JRTXPreviewPanel.class, "JRTXPreviewPanel.jLabelStyleName.text")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(JRTXPreviewPanel.class, "JRTXPreviewPanel.jLabel1.text")); // NOI18N

        jPanelGraphicElements.setBackground(new java.awt.Color(255, 255, 255));
        jPanelGraphicElements.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText(org.openide.util.NbBundle.getMessage(JRTXPreviewPanel.class, "JRTXPreviewPanel.jLabel2.text")); // NOI18N

        jPanelTextElements.setBackground(new java.awt.Color(255, 255, 255));
        jPanelTextElements.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanelGraphicElements, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanelTextElements, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelStyleName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelStyleName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelGraphicElements, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 124, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelTextElements, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelStyleName;
    private javax.swing.JPanel jPanelGraphicElements;
    private javax.swing.JPanel jPanelTextElements;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the mainVisualView
     */
    public JRTXVisualView getMainVisualView() {
        return mainVisualView;
    }

    /**
     * @param mainVisualView the mainVisualView to set
     */
    public void setMainVisualView(JRTXVisualView mainVisualView) {
        this.mainVisualView = mainVisualView;
    }

    /**
     * @return the drawVisitor
     */
    public DrawVisitor getDrawVisitor() {
        return drawVisitor;
    }

    /**
     * @param drawVisitor the drawVisitor to set
     */
    public void setDrawVisitor(DrawVisitor drawVisitor) {
        this.drawVisitor = drawVisitor;
    }

    private void setElementsStyle(JRStyle style) {

        String[] elementNames = new String[]{"rectangle", "ellipse", "line", "image", "statictext", "textfield"};

        for (int i=0; i<elementNames.length; ++i)
        {
            JRDesignElement element = (JRDesignElement) STYLE_REPORT.getTitle().getElementByKey(elementNames[i]);
            if (element != null)
            {
                element.setStyle(style);
            }
        }
    }

}
