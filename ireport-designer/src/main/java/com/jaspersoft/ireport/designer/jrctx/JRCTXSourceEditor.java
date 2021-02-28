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
package com.jaspersoft.ireport.designer.jrctx;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.beans.BeanInfo;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import net.sf.jasperreports.chartthemes.simple.ChartThemeSettings;
import net.sf.jasperreports.chartthemes.simple.XmlChartTheme;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.nodes.Node;
import org.openide.text.CloneableEditor;
import org.openide.text.NbDocument;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author gtoffoli
 */
public class JRCTXSourceEditor extends CloneableEditor implements MultiViewElement, Runnable {
 
    private JComponent toolbar;
    private MultiViewElementCallback callback;
    
    public JRCTXSourceEditor() {
    }
    
    JRCTXSourceEditor(JRCTXEditorSupport ed) {
        super(ed);
    }
    
    public JComponent getVisualRepresentation() {
        return this;
        //return null;
        
    }
    
    @Override
    public void componentShowing() {
        super.componentShowing();
        ChartThemeSettings template = ((JRCTXEditorSupport)cloneableEditorSupport()).getCurrentModel();
        if (template != null && ((JRCTXEditorSupport)cloneableEditorSupport()).isModified())
        {
            // Update the content...
            try {
                    // Need to save the file to get the content... too bad...

                    //set the document content...
                    String content = null;
                    try {

                        //TODO Save the file and read from it... no good actually...
                        // Save in the primary file...
                        //File tmpFile = File.createTempFile("tmp",".jrctx");
                        //FileUtils.readFully( new FileReader( tmpFile ));
                        content =  XmlChartTheme.saveSettings(template);

                    } catch (Exception ex)
                    {
                        JOptionPane.showMessageDialog(Misc.getMainWindow(), "Error saving the JRCTX: " + ex.getMessage() + "\nSee the log file for more details.", "Error saving", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                        return;
                    }

                    if (content != null)
                    {
                        getEditorPane().setText(content);
                        getEditorPane().setCaretPosition(0);
                    }
                    ((JRCTXVisualView)((JRCTXEditorSupport)cloneableEditorSupport()).descriptions[0]).setNeedModelRefresh(false);
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        ((JRCTXEditorSupport)cloneableEditorSupport()).setCurrentModel(null);
        
    }
    
    public JComponent getToolbarRepresentation() {
        if (toolbar == null) {
          JEditorPane pane = this.pane;
                if (pane != null) {
                    Document doc = pane.getDocument();
                    if (doc instanceof NbDocument.CustomToolbar) {
                        toolbar = ((NbDocument.CustomToolbar)doc).createToolbar(pane);
                    }
                }
            if (toolbar == null) {
                //attempt to create own toolbar?
                toolbar = new JPanel();
            }
        }
        return toolbar;
    }
    
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
        //updateName();
    }
    
    public void componentOpened() {
        super.componentOpened();
    }
    
    public void componentClosed() {
        super.componentClosed();
    }
    
    
    
    public void componentHidden() {
        super.componentHidden();
    }
    
    public void componentActivated() {
        super.componentActivated();
    }
    
    public void componentDeactivated() {
        super.componentDeactivated();
    }
    
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }
    
    public void updateName() {
       // Mutex.EVENT.readAccess(this);
       //IOUtils.runInAWTNoBlocking(
        
        Runnable run = new Runnable() {
            public void run() {
                MultiViewElementCallback c = callback;
                
                if (c == null) {
                    return;
                }
                TopComponent tc = c.getTopComponent();
                if (tc == null) {
                    return;
                }
                Node nd = ((JRCTXDataObject)((JRCTXEditorSupport)cloneableEditorSupport()).getDataObject()).getNodeDelegate();
                tc.setName(nd.getName() );
                tc.setDisplayName(nd.getDisplayName());
                tc.setHtmlDisplayName(nd.getHtmlDisplayName());
                tc.setIcon( nd.getIcon( BeanInfo.ICON_COLOR_16x16));
            }
        };
        
        if (SwingUtilities.isEventDispatchThread ())
            run.run();
        else
            SwingUtilities.invokeLater (run);
    }
    
    public void run() {
        MultiViewElementCallback c = callback;
        if (c == null) {
            return;
        }
        TopComponent tc = c.getTopComponent();
        if (tc == null) {
            return;
        }
        
        super.updateName();
        Node nd = ((JRCTXDataObject)((JRCTXEditorSupport)cloneableEditorSupport()).getDataObject()).getNodeDelegate();
        tc.setName(nd.getName() );
        tc.setDisplayName(nd.getDisplayName());
        tc.setHtmlDisplayName(nd.getHtmlDisplayName());
        //tc.setIcon( nd.getIcon( BeanInfo.ICON_COLOR_16x16));
    }
    
    public Lookup getLookup() {
        return ((JRCTXDataObject)((JRCTXEditorSupport)cloneableEditorSupport()).getDataObject()).getNodeDelegate().getLookup();
    }

}

