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

import com.jaspersoft.ireport.designer.GenericCloseOperationHandler;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.xml.JRXmlTemplateWriter;
import org.netbeans.api.queries.FileEncodingQuery;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.cookies.EditCookie;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.loaders.SaveAsCapable;
import org.openide.nodes.Node.Cookie;
import org.openide.text.CloneableEditorSupport;
import org.openide.text.DataEditorSupport;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Task;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author gtoffoli
 */
public class JRTXEditorSupport extends DataEditorSupport implements OpenCookie, EditorCookie, EditCookie, SaveAsCapable  {

    private static Logger LOG = Logger.getLogger(JRTXEditorSupport.class.getName());
    
    private InstanceContent specialNodeLookupIC = null;
    private Lookup specialNodeLookup = null;
    
    private final SaveCookie saveCookie = new SaveCookie() {
        /** Implements <code>SaveCookie</code> interface. */
            public void save() throws IOException {
                saveDocument();
            }
        };

    final MultiViewDescription[] descriptions = {
        new JRTXVisualView(this),
        new JRTXTextView(this)
        //new JrxmlPreviewView(this)
    };
     
    
    private JRSimpleTemplate currentModel = null;

    @Override
    protected Task reloadDocument() {
        
        // Force a document refresh...
        ((JRTXVisualView)descriptions[0]).refreshModel();
        return super.reloadDocument();
    }


    public MultiViewDescription[] getDescriptions()
    {
        return descriptions;
    }

    
    private JRTXEditorSupport(JRTXDataObject obj) {
        super(obj, new JRTXEnv(obj));
        specialNodeLookupIC = new InstanceContent();
        specialNodeLookupIC.add(this);
        specialNodeLookup = new AbstractLookup(specialNodeLookupIC);
    }
    
    public static JRTXEditorSupport create(JRTXDataObject obj) {
         JRTXEditorSupport ed = new JRTXEditorSupport(obj);
         ed.setMIMEType("text/xml");
         return ed;
    }



    protected CloneableEditorSupport.Pane createPane() {
        return (CloneableEditorSupport.Pane)MultiViewFactory.
                createCloneableMultiView(descriptions, descriptions[0], new GenericCloseOperationHandler(this));
    }
    
    protected boolean notifyModified() {
        boolean retValue;
        retValue = super.notifyModified();
        if (retValue) {
            JRTXDataObject obj = (JRTXDataObject)getDataObject();
            if(obj.getCookie(SaveCookie.class) == null) {
                obj.getIc().add( saveCookie );
                specialNodeLookupIC.add(saveCookie);
                obj.setModified(true);
            }
        }
        return retValue;
    }

    public void notifyModelChangeToTheView()
    {
        if (getCurrentModel() != null)
        {
            ((JRTXVisualView)descriptions[0]).modelChanged();
            notifyModified();
        }
    }


    protected void notifyUnmodified() {
        super.notifyUnmodified();
        JRTXDataObject obj = (JRTXDataObject)getDataObject();
        
        Cookie cookie = obj.getCookie(SaveCookie.class);

        if(cookie != null && cookie.equals( saveCookie )) {
            obj.getIc().remove(saveCookie);
            specialNodeLookupIC.remove(saveCookie);
            obj.setModified(false);
        }
    }
    
    public DataEditorSupport.Env  getEnv()
    {
        return (Env)this.env;
    }
    
    public void saveDocument() throws IOException {
            
            if (getCurrentModel() != null)
            {
                //set the document content...
                JRSimpleTemplate jrtx = getCurrentModel();
                String content = null;
                try {
                    content = JRXmlTemplateWriter.writeTemplate(jrtx, "UTF-8"); // IReportManager.getInstance().getProperty("jrxmlEncoding", System.getProperty("file.encoding") ));
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(), "Error saving the JRTX: " + ex.getMessage() + "\nSee the log file for more details.", "Error saving", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    return;
                }

                if (content != null)
                {
                    try {
                        getDocument().remove(0, getDocument().getLength());
                        getDocument().insertString(0, content, null);
                        ((JRTXVisualView) descriptions[0]).setNeedModelRefresh(false);
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }

            Charset cs = FileEncodingQuery.getDefaultEncoding();
            String fileEncoding = System.getProperty("file.encoding", "UTF-8");
            try {

                FileEncodingQuery.setDefaultEncoding(Charset.forName("UTF-8"));
                //System.setProperty("file.encoding", "UTF-8");
            } catch (Exception ex)
            {
                //System.out.println("UTF-8 encoding not supported!");
                //System.out.flush();
            }

            //getDataObject().getPrimaryFile().setAttribute(EDITOR_MODE, this)
            try {
                super.saveDocument();
            } finally
            {
                FileEncodingQuery.setDefaultEncoding(cs);
                System.setProperty("file.encoding", fileEncoding);
            }

        }

    public Lookup getSpecialNodeLookup() {
        return specialNodeLookup;
    }

    public void setSpecialNodeLookup(Lookup specialNodeLookup) {
        this.specialNodeLookup = specialNodeLookup;
    }
    
    public static final class JRTXEnv extends DataEditorSupport.Env {
    
        public JRTXEnv(JRTXDataObject obj) {
            super(obj);
        }
        
        protected FileObject getFile() {
            return super.getDataObject().getPrimaryFile();
        }
        
        protected FileLock takeLock() throws IOException {
            return ((JRTXDataObject)super.getDataObject()).getPrimaryEntry().takeLock();
        }

    }

    public JRSimpleTemplate getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(JRSimpleTemplate currentModel) {
        
        // Update the lookup...
        if (this.currentModel != null)
        {
            ((JRTXDataObject)getDataObject()).getIc().remove(this.currentModel);
        }
        this.currentModel = currentModel;
        if (this.currentModel != null)
        {
            ((JRTXDataObject)getDataObject()).getIc().add(this.currentModel);
        }
    }


}
