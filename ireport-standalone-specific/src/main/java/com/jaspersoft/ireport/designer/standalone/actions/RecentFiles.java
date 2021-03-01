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
package com.jaspersoft.ireport.designer.standalone.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import java.beans.PropertyChangeEvent;
import org.openide.loaders.DataObject;
import org.openide.windows.CloneableTopComponent;
import org.openide.windows.TopComponent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.URLMapper;
import org.openide.util.actions.SystemAction;
import org.openide.windows.WindowManager;

/**
 * Manages prioritized set of recently closed files.
 *
 * @author Dafe Simonek
 */
public final class RecentFiles {

    private static final Object HISTORY_LOCK = new Object();
    private static List<String> history = new ArrayList<String>();

    /** Boundary for items count in history */
    private static final int MAX_HISTORY_ITEMS = 20;
    
    private RecentFiles () {
    }

    private static boolean inited = false;
    /** Starts to listen for recently closed files */
    public synchronized static void init () {
        
        if (!inited)
        {
            WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
                public void run() {
                    
                    synchronized (HISTORY_LOCK) {
                        String s = IReportManager.getPreferences().get("RecentFiles",null); // NOI18N
                        if (s != null)
                        {
                            String[] files = s.split("\n");
                            for (int i=0; i<files.length; ++i)
                            {
                                history.add(files[i]);
                            }
                        }
                        checkHistory();
                    }
                    TopComponent.getRegistry().addPropertyChangeListener(new WindowRegistryL());
                }
            });
        }
    }

    /** Returns read-only list of recently closed files */
    public static List<String> getRecentFiles () {
        return history;
    }

    static void storeHistory()
    {
        String historyString = "";
        int i=0;
        for (String hItem : history) {
            if (i > MAX_HISTORY_ITEMS) break;
            historyString += hItem + "\n"; // NOI18N
        }
        IReportManager.getPreferences().put("RecentFiles", historyString); // NOI18N

    }

    
    /** Adds file represented by given TopComponent to the list,
     * if conditions are met.
     */ 
    private static void addFile (TopComponent tc) {

        if (tc instanceof CloneableTopComponent) {
            URL fileURL = obtainURL(tc);
            if (fileURL != null) {
                String hItem = fileURL.toExternalForm();
                history.remove(hItem);
                history.add(0, hItem);
            }
        }
    }

    
    
    public static URL obtainURL (TopComponent tc) {
        DataObject dObj = tc.getLookup().lookup(DataObject.class);
        if (dObj != null) {
            FileObject fo = dObj.getPrimaryFile();
            if (fo != null) {
                return convertFile2URL(fo);
            }
        }
        return null;
    }
    
    static URL convertFile2URL (FileObject fo) {
        URL url = URLMapper.findURL(fo, URLMapper.EXTERNAL);
        if (url == null) {
            Logger.getLogger(RecentFiles.class.getName()).log(Level.FINE, 
                    "convertFile2URL: URL can't be found for FileObject " + fo); // NOI18N
        }
        return url;
    }
    
    static FileObject convertURL2File (URL url) {
        FileObject fo = URLMapper.findFileObject(url);
        if (fo == null) {
            Logger.getLogger(RecentFiles.class.getName()).log(Level.FINE, 
                    "convertURL2File: File can't be found for URL " + url); // NOI18N
        }
        return fo;
    }
    
    /** Checks recent files history and removes non-valid entries */
    private static void checkHistory () {
        // note, code optimized for the frequent case that there are no invalid entries
        List<String> invalidEntries = new ArrayList<String>();
        FileObject fo = null;
        for (String historyItem : history) {
            try {
                fo = convertURL2File(new URL(historyItem));
                if (fo == null || !fo.isValid()) {
                    invalidEntries.add(historyItem);
                }
            } catch (Exception ex)
            {
                invalidEntries.add(historyItem);
            }
        }
        for (String historyItem : invalidEntries) {
            history.remove(historyItem);
        }
    }

    /** Receives info about opened and closed TopComponents from window system.
     */ 
    private static class WindowRegistryL implements PropertyChangeListener {
        
        public void propertyChange(PropertyChangeEvent evt) {
            
//            if (TopComponent.Registry.PROP_TC_CLOSED.equals(evt.getPropertyName())) {
//                
//            }
            
            if (TopComponent.Registry.PROP_TC_OPENED.equals(evt.getPropertyName())) {
                //removeFile((TopComponent) evt.getNewValue());
                // add it to the beginning of the list ;-)
                // Postpone this in to the end of the awt events...
                // Sometime needs time to the lookup to get ready...



                final TopComponent tc = (TopComponent) evt.getNewValue();

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        addFile(tc);
                        storeHistory();
                        if (RecentFileAction.getInstance() != null)
                        {
                            RecentFileAction.getInstance().updateSubMenu();
                        }

                    }
                });
            }
        }
    
    }
    
}
