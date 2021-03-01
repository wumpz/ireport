/*
 * Copyright (C) 2005 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */

package com.jaspersoft.ireport.jasperserver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.openide.loaders.DataObject;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * This class listens for new documents opened. If the document is a JrxmlDocument
 * and there is a DomainsService view tied to it, listen for document changes.
 *
 * @version $Id: ReportOpenedListener.java 0 2009-10-05 18:17:54 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ActiveEditorTopComponentListener {

    List<String> openedFiles = new ArrayList<String>();
    private TopComponent activeEditorTopComponent = null;


    static private ActiveEditorTopComponentListener mainInstance = null;


    private ActiveEditorTopComponentListener()
    {
        WindowManager.getDefault().getRegistry().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {

                if (evt.getPropertyName().equals(TopComponent.Registry.PROP_ACTIVATED))
                {
                    TopComponent tc = (TopComponent)evt.getNewValue();
                    if (tc != null && tc.getLookup().lookup(DataObject.class) != null)
                    {
                        System.out.println("Active top component: " + tc.getDisplayName() + " " + tc.getLookup().lookup(DataObject.class).getPrimaryFile());
                        System.out.flush();
                        setActiveEditorTopComponent(tc);
                    }

                }
                else if (evt.getPropertyName().equals(TopComponent.Registry.PROP_TC_CLOSED))
                {
                    TopComponent tc = (TopComponent)evt.getNewValue();
                    if (tc == activeEditorTopComponent)
                    {
                        System.out.println("No active editor top component");
                        System.out.flush();
                        setActiveEditorTopComponent(null);
                    }
                }

            }
        });
    }

    public static void startListening() {

        getDefaultInstance();
    }

    public static ActiveEditorTopComponentListener getDefaultInstance() {

        if (mainInstance == null)
        {
            mainInstance = new ActiveEditorTopComponentListener();
        }
        return mainInstance;
    }

    /**
     * @return the activeEditorTopComponent
     */
    public TopComponent getActiveEditorTopComponent() {
        return activeEditorTopComponent;
    }

    /**
     * @param activeEditorTopComponent the activeEditorTopComponent to set
     */
    public void setActiveEditorTopComponent(TopComponent activeEditorTopComponent) {
        this.activeEditorTopComponent = activeEditorTopComponent;
    }

}
