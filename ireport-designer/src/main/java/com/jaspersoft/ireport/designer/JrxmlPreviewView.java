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
package com.jaspersoft.ireport.designer;

import com.jaspersoft.ireport.JrxmlDataObject;
import com.jaspersoft.ireport.designer.compiler.CompilationStatusEvent;
import com.jaspersoft.ireport.designer.compiler.CompilationStatusListener;
import com.jaspersoft.ireport.designer.menu.RunReportAction;
import com.jaspersoft.ireport.designer.tools.JrxmlPreviewToolbar;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.BeanInfo;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.swing.JRViewerController;
import net.sf.jasperreports.swing.JRViewerPanel;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;

/**
 *
 * @author gtoffoli
 */
public class JrxmlPreviewView extends TopComponent 
        implements MultiViewDescription, MultiViewElement, CompilationStatusListener {
    
    private boolean needRefresh = true;

    MultiViewElementCallback multiViewCallback = null;
    
    JRViewerController viewerContext = new JRViewerController(IRLocalJasperReportsContext.getInstance(), null, null);
    JrxmlPreviewToolbar viewerToolbar = new JrxmlPreviewToolbar(this, viewerContext);
    
    private JrxmlEditorSupport support;
    private JrxmlVisualView visualView = null;

    public JrxmlPreviewView(JrxmlEditorSupport ed, JrxmlVisualView visualView ) {
        this.support = ed;

        IReportManager.getPreferences().addPreferenceChangeListener(new PreferenceChangeListener() {

            public void preferenceChange(PreferenceChangeEvent evt) {
                if (evt.getKey() != null && evt.getKey().equals("RecentFiles")) return;
                setNeedRefresh(true);
            }
        });
        this.visualView = visualView;
        this.viewerToolbar.setFloatable(false);
    }
    
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }
    
    @Override
    public String getDisplayName() {
        return I18n.getString("view.preview");
    }
    
    @Override
    public Image getIcon() {
        Node nd = ((JrxmlDataObject)support.getDataObject()).getNodeDelegate();
        return nd.getIcon( BeanInfo.ICON_COLOR_16x16);
    }
    
    
    public String preferredID() {
        return "Preview"; // NOI18N
    }
    
    public void componentDeactivated() {
    }
    
    public void componentActivated() {
    }
    
    public void componentHidden() {
    }
    
    public void componentShowing() {
        
        if (isNeedRefresh())
        {
            setJasperPrint(null);
            updateUI();
            RunReportAction.runReport( support );
        }
    }
    
    public void componentClosed() {
    }
    
    public void componentOpened() {
    }
    
    public void setJasperPrint(final JasperPrint print)
    {
        if (print != null)
        {
            setNeedRefresh(false);
        }
        
        ThreadUtils.invokeInAWTThread( new Runnable()
        {
            public void run()
            {
                removeAll();
        
                if (print != null)
                {
                    JRViewerPanel viewerPanel = 
                        new JRViewerPanel(viewerContext)
                        {
                            protected void paintPage(Graphics2D grx) 
                            {
                                ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader(); 
                                try
                                {
                                    //Thread.currentThread().setContextClassLoader(IReportManager.getJRExtensionsClassLoader());
                                    super.paintPage(grx);
                                }
                                finally
                                {
                                    //Thread.currentThread().setContextClassLoader(oldClassLoader);
                                }
                            }
                    	};

                    add(viewerPanel, BorderLayout.CENTER);

                    viewerContext.loadReport(print);
                                        
                    viewerToolbar.init();
                    viewerContext.refreshPage();
                    viewerPanel.updateUI();
                }
                updateUI();
                
            }
        });
        
    }
    
    
    public MultiViewElement createElement() {
        this.setLayout(new BorderLayout());
        try {
            associateLookup( visualView.getLookup() );
        } catch (Throwable ex) {}
        return this;
    }

    public JComponent getVisualRepresentation() {
       return this;
    }

    public JComponent getToolbarRepresentation() {
        return new JrxmlPreviewToolbar(this, viewerContext);
    }

    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.multiViewCallback = callback;
    }

    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }
    
    public void requestActive() {
            if (multiViewCallback != null) {
                multiViewCallback.requestActive();
            } else {
                super.requestActive();
            }
        }

    public

    boolean isNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    public void actionPerformed(ActionEvent e) {
        
        
        
    }

    int currentStatus = CompilationStatusEvent.STATUS_UNDEFINED;
    org.jdesktop.swingx.JXBusyLabel label = new org.jdesktop.swingx.JXBusyLabel();
                
    public void compilationStatus(CompilationStatusEvent e) {
    
        if (e.getStatus() == CompilationStatusEvent.STATUS_RUNNING)
        {
            if (e.getStatus() != currentStatus)
            {
                ThreadUtils.invokeInAWTThread( new Runnable()
                {
                    public void run()
                    {
                        removeAll();
                        JPanel p = new JPanel();
                        p.setLayout(new GridBagLayout());
                        p.add(label);
                        add(p, BorderLayout.CENTER);
                        label.setBusy(true);
                        updateUI();
                    }
                });
            }
        }
        else
        {
            ThreadUtils.invokeInAWTThread( new Runnable()
            {   
                public void run()
                {
                    label.setBusy(false);
                }
            });
            
        }
        
        currentStatus = e.getStatus();
    
    }


    /**
     * @return the visualView
     */
    public JrxmlVisualView getVisualView() {
        return visualView;
    }

    /**
     * @param visualView the visualView to set
     */
    public void setVisualView(JrxmlVisualView visualView) {
        this.visualView = visualView;
    }

    
}
