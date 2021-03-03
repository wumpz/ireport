/*
 * Copyright (C) 2005 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */

package com.jaspersoft.ireport.addons;

import com.jaspersoft.ireport.addons.background.BackgroundImageUtilities;
import com.jaspersoft.ireport.addons.callouts.CalloutsUtility;
import com.jaspersoft.ireport.designer.JrxmlEditorSupport;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.ReportDesignerPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.Mutex;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * This class listens for new documents opened by listening on the WindowsManager registry.
 * When a new JrxmlVisualView is opened, a listener is added to its ReportDesignerPanel for the
 * property JASPERDESIGN_PROPERTY to listen for replacements of the JasperDesign model (so not
 * each change, but just when the model is reloaded).
 * Temporary model changes (catched listening to the JrxmlEditorSupport lookup for the object
 * JasperDesign) are ignored, in other words the model change notifications are the same used
 * to update the visual view in iReport.
 *
 * This class idea can be useful for all the cases when an add-on wants to perform some
 * actions at model load, i.e. check the report properties and setup some design stuff
 * like the background image or callouts.
 * 
 * The actions to take when a new model/view is available is inside propertyChange method
 *
 * @version $Id: ReportOpenedListener.java 0 2009-10-05 18:17:54 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ReportOpenedListener implements PropertyChangeListener {

    List<String> openedFiles = new ArrayList<String>();

    static private ReportOpenedListener mainInstance = null;


    private ReportOpenedListener()
    {
        WindowManager.getDefault().getRegistry().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Mutex.EVENT.readAccess(new Runnable() {

                    public void run() {
                        refreshDocumentsList();
                    }
                });

            }
        });
    }


    /**
     * This methos is invoked by the listener to the windows registry. It checks for closed and opened windows.
     * When there is a new document, a lookup listener is attached to its dataObject.
     */
    public void refreshDocumentsList()
    {

        List<String> currentlyOpenedFiles = new ArrayList<String>();

        Set<TopComponent> components = WindowManager.getDefault().getRegistry().getOpened();

        for (TopComponent t : components)
        {

            JrxmlEditorSupport jrxmlEditorSupport = t.getLookup().lookup(JrxmlEditorSupport.class);

            if (jrxmlEditorSupport != null)
            {
                String fileName = FileUtil.toFile(jrxmlEditorSupport.getDataObject().getPrimaryFile()).getPath();
                JrxmlVisualView view = (JrxmlVisualView)jrxmlEditorSupport.getDescriptions()[0];
                if (view != null && view.getReportDesignerPanel() != null)
                {
                    view.getReportDesignerPanel().removePropertyChangeListener( this);
                    view.getReportDesignerPanel().addPropertyChangeListener( this);
                    currentlyOpenedFiles.add(fileName);
                }
            }
        }

        List<String> closedFiles = new ArrayList<String>();

        for (String s : openedFiles)
        {
            if (!currentlyOpenedFiles.contains(s))
            {
                closedFiles.add(s);
            }
        }

        // Clean up the list of opened files...
        for (String s : closedFiles)
        {
            openedFiles.remove(s);
        }


        
        // Notify new files
        for (String s : currentlyOpenedFiles)
        {
            if (!openedFiles.contains(s))
            {
                // There is a new file here!!!
                JrxmlEditorSupport editorSupport = getJrxmlEditorSupportForFile(new File(s));
                if (editorSupport != null)
                {
                    JrxmlVisualView view = (JrxmlVisualView)editorSupport.getDescriptions()[0];
                    if (view != null)
                    {
                        propertyChange(new PropertyChangeEvent(view.getReportDesignerPanel(), ReportDesignerPanel.JASPERDESIGN_PROPERTY, editorSupport.getCurrentModel(), editorSupport.getCurrentModel()));
                        openedFiles.add(s);
                    }
                }
            }
        }
        
    }

    /**
     * Entry point of the class. It's like to call getDefaultInstance(), since
     * once this class is initialized, it starts to listen to the window manager events.
     */
    public static void startListening() {

        getDefaultInstance();
    }

    public static ReportOpenedListener getDefaultInstance() {

        if (mainInstance == null)
        {
            mainInstance = new ReportOpenedListener();
        }
        return mainInstance;
    }

    public JrxmlEditorSupport getJrxmlEditorSupportForFile(File file)
    {
        Set<TopComponent> components = WindowManager.getDefault().getRegistry().getOpened();

        for (TopComponent t : components)
        {
            JrxmlEditorSupport jrxmlEditorSupport = t.getLookup().lookup(JrxmlEditorSupport.class);

            if (jrxmlEditorSupport != null && jrxmlEditorSupport.getDataObject().getPrimaryFile() != null)
            {
                File f = FileUtil.toFile(jrxmlEditorSupport.getDataObject().getPrimaryFile());
                if (f.equals(file))
                {
                    return jrxmlEditorSupport;
                }
            }
        }
        return null;
    }

    public JrxmlVisualView getViewForJasperDesign(JasperDesign jd)
    {
        Set<TopComponent> components = WindowManager.getDefault().getRegistry().getOpened();

        for (TopComponent t : components)
        {
            JrxmlEditorSupport jrxmlEditorSupport = t.getLookup().lookup(JrxmlEditorSupport.class);

            if (jrxmlEditorSupport != null && jrxmlEditorSupport.getCurrentModel() == jd)
            {
                return (JrxmlVisualView)(jrxmlEditorSupport.getDescriptions()[0]);
            }
        }
        return null;
    }


    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ReportDesignerPanel.JASPERDESIGN_PROPERTY))
        {
            ReportDesignerPanel panel = (ReportDesignerPanel) evt.getSource();
            JasperDesign jd = panel.getJasperDesign();

            if (jd == null) return;

            JrxmlVisualView view = getViewForJasperDesign(jd);

            if (view != null)
            {
                try {
                        BackgroundImageUtilities.restore(view);
                } catch (Throwable t) // you never know...
                {
                    t.printStackTrace();
                }

                try {
                        CalloutsUtility.loadCallouts( view.getReportDesignerPanel().getScene());
                } catch (Throwable t) // you never know...
                {
                    t.printStackTrace();
                }
            }
        }
    }

}
