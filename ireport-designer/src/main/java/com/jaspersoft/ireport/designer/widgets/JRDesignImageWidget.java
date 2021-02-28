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
package com.jaspersoft.ireport.designer.widgets;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IRLocalJasperReportsContext;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.utils.ImageExpressionFileResolver;
import com.jaspersoft.ireport.designer.utils.ProxyFileResolver;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.File;
import net.sf.jasperreports.engine.base.JRBaseLine;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.base.JRBaseStaticText;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.export.draw.DrawVisitor;
import net.sf.jasperreports.engine.util.JRResourcesUtil;
import org.openide.filesystems.FileUtil;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class JRDesignImageWidget extends JRDesignElementWidget implements Runnable {

    ImageExpressionFileResolver resolver = null;
    BufferedImage offScreenImage = null;
    boolean needReload = false;
    boolean running = false;

    public JRDesignImageWidget(AbstractReportObjectScene scene, JRDesignImage element) {
        super(scene, element);
    }
    
    
    @Override
    protected void paintWidgetImplementation() {

        if (getElement().getHeight() <= 0 || getElement().getWidth() <= 0)
        {
                return;
        }

        if (offScreenImage == null || needReload)
        {
            needReload = false;

            offScreenImage = new BufferedImage(getElement().getWidth(), getElement().getHeight(), BufferedImage.TYPE_INT_ARGB );

            Component component = ((AbstractReportObjectScene)getScene()).getView();
            JrxmlVisualView visualView = null;
            while (component.getParent() != null) //FIXME: the component could be not installed...
            {
                if (component.getParent() instanceof JrxmlVisualView)
                {
                    visualView = (JrxmlVisualView) component.getParent();
                    break;
                }
                else
                {
                    component = component.getParent();
                }
            }

            File reportFolder = null;
            if (visualView != null)
            {
                File file = FileUtil.toFile(visualView.getEditorSupport().getDataObject().getPrimaryFile());
                if (file.getParentFile() != null)
                {
                    reportFolder = file.getParentFile();
                }
            }

            ProxyFileResolver fileResolver = new ProxyFileResolver(IReportManager.getInstance().getFileResolvers());
            //fileResolver.addResolver(new SimpleFileResolver(reportFolder));//FIXMETD can we keep the parent folder somewhere? in the draw visitor maybe?

            if (visualView != null && visualView.getModel() != null && visualView.getModel().getJasperDesign() != null)
            {
                if (resolver == null)
                {
                    resolver = new ImageExpressionFileResolver((JRDesignImage)getElement(), reportFolder+"",  visualView.getModel().getJasperDesign());
                }
                else
                {
                    // check if something is changed...
                    try {
                        resolver.setImageElement((JRDesignImage)getElement());
                        resolver.setJasperDesign(visualView.getModel().getJasperDesign());
                        resolver.setReportFolder(reportFolder+"");
                    } catch (Exception ex)
                    {
                       ex.printStackTrace();
                    }
                }

                fileResolver.addResolver(resolver);
            }
            
            IRLocalJasperReportsContext.getInstance().setFileResolver(fileResolver);
            //JRResourcesUtil.setThreadFileResolver(fileResolver);

            try
            {
                DrawVisitor dv = ((AbstractReportObjectScene)this.getScene()).getDrawVisitor();
                if (dv == null) return;
                dv.setGraphics2D((Graphics2D)offScreenImage.getGraphics());
                try {
                    getElement().visit( dv );
                } catch (Exception ex){
                    System.err.println("iReport - Element rendering exception " + getElement() + " " + ex.getMessage());
                }
            }
            catch (Exception ex)
            {
                System.err.println("iReport - Error painting image: " + ex.getMessage());
            }
            finally
            {
                JRResourcesUtil.resetThreadFileResolver();
            }
        }
        getGraphics().drawImage(offScreenImage, getBounds().x,getBounds().y, getBounds().width, getBounds().height, null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
        String propertyName = evt.getPropertyName();
        if (propertyName == null) return;

        if (propertyName.equals( JRDesignElement.PROPERTY_HEIGHT) ||
            propertyName.equals( JRDesignElement.PROPERTY_WIDTH) ||
            propertyName.equals( JRBaseStyle.PROPERTY_BACKCOLOR) ||
            propertyName.equals( JRBaseStyle.PROPERTY_FORECOLOR) ||
            propertyName.equals( JRDesignElement.PROPERTY_PARENT_STYLE) ||
            propertyName.equals( JRDesignElement.PROPERTY_PARENT_STYLE_NAME_REFERENCE) ||
            propertyName.equals( JRBaseStyle.PROPERTY_MODE ) ||
            //FIXME propertyName.equals( JRDesignGraphicElement.PROPERTY_PEN) ||
            propertyName.equals( JRBaseStyle.PROPERTY_FILL) ||
            propertyName.equals( JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT) ||
            propertyName.equals( JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT) ||
            propertyName.equals( JRBaseStyle.PROPERTY_SCALE_IMAGE) ||
            propertyName.equals( JRDesignTextField.PROPERTY_EXPRESSION) ||
            propertyName.equals("pen") ||           // Special property fired by the property sheet
            propertyName.equals("linebox") ||       // Special property fired by the property sheet
            propertyName.equals(JRBasePen.PROPERTY_LINE_COLOR) ||
            propertyName.equals(JRBasePen.PROPERTY_LINE_STYLE) ||
            propertyName.equals(JRBasePen.PROPERTY_LINE_WIDTH) ||
            propertyName.equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING) ||
            propertyName.equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING) ||
            propertyName.equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING) ||
            propertyName.equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING)
            )
        {
            // Schedule a repoaint in 3 secs...
            if (running) return;
            Thread t = new Thread(this);
            t.start();
        }

        super.propertyChange(evt);
    }

    public void run() {

        running = true;
        try {
            Thread.sleep(3000);
        } catch (Exception ex)
        {}
        // scheduled a repaint ?
        needReload = true;
        Mutex.EVENT.readAccess(new Runnable() {

            public void run() {
                repaint();
            }
        });

        running = false;
    }


}
