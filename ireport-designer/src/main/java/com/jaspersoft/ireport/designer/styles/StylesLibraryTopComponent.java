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
package com.jaspersoft.ireport.designer.styles;

import com.jaspersoft.ireport.designer.jrtx.TemplateNode;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.text.DefaultEditorKit;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.xml.JRXmlTemplateLoader;
import net.sf.jasperreports.engine.xml.JRXmlTemplateWriter;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 * Top component which displays something.
 */
final class StylesLibraryTopComponent extends TopComponent implements ExplorerManager.Provider {

    private static StylesLibraryTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "com/jaspersoft/ireport/designer/resources/jasperreports_jrtx.png";
    static final String LIBRARY_PARENT = "ireport";
    static final String LIBRARY_NAME = "style_library.jrtx";
    static final String LIBRARY_PATH = LIBRARY_PARENT + "/" + LIBRARY_NAME;

    private static final String PREFERRED_ID = "StylesLibraryTopComponent";

    //private BeanTreeView view;
    //private StyleJList view;
    private StyleListView view;
    private final ExplorerManager manager = new ExplorerManager();
    private AbstractNode noReportNode = null;
    /** Dynamic Lookup content */
    private final InstanceContent ic;
    /** Lookup instance */
    private final Lookup lookup;
    private JRSimpleTemplate library = null;


    private StylesLibraryTopComponent() {
        initComponents();
        
        Misc.getMainFrame().addWindowListener(new WindowListener(){

            public void windowOpened(WindowEvent e) {}

            public void windowClosing(WindowEvent e) {
                saveLibrary();
            }

            public void windowClosed(WindowEvent e) {}

            public void windowIconified(WindowEvent e) {}

            public void windowDeiconified(WindowEvent e) {}

            public void windowActivated(WindowEvent e) {}

            public void windowDeactivated(WindowEvent e) {}
        });

        setName(NbBundle.getMessage(StylesLibraryTopComponent.class, "CTL_StylesLibraryTopComponent"));
        setToolTipText(NbBundle.getMessage(StylesLibraryTopComponent.class, "HINT_StylesLibraryTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        this.ic = new InstanceContent();
        this.lookup = new AbstractLookup(ic);

        noReportNode = new AbstractNode(Children.LEAF);
        noReportNode.setDisplayName("");

        ActionMap map = getActionMap();
        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(manager));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(manager));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(manager));
        map.put("delete", ExplorerUtils.actionDelete(manager, true));

        associateLookup( new ProxyLookup(lookup, ExplorerUtils.createLookup(manager, map)) );

        setLayout(new BorderLayout());
        //view = new StyleBeanTreeView();
        view = new StyleListView(); //StyleJList();
        //JScrollPane scrollPane1 = new JScrollPane(view);

        //view.setRootVisible(false);

        add(view , BorderLayout.CENTER); //scrollPane1



        getExplorerManager().setRootContext(noReportNode);

        loadLibrary();
    }

    public ExplorerManager getExplorerManager() {
        return manager;
    }


    public void loadLibrary()
    {

        library = new JRSimpleTemplate();

        FileObject libraryObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject(LIBRARY_PATH);

        if (libraryObject != null)
        {
            try {
                library = (JRSimpleTemplate)JRXmlTemplateLoader.load(libraryObject.getInputStream());
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        //view.setRootVisible(false);
        getExplorerManager().setRootContext(new TemplateNode(new StylesLibraryChildren(library, lookup), library, lookup));

    }

    public void saveLibrary()
    {
        FileObject libraryObject = null;
        FileLock lock = null;
        try {
             libraryObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject(LIBRARY_PATH);
            if (libraryObject == null)
            {
                libraryObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject(LIBRARY_PARENT).createData(LIBRARY_NAME);
            }

            if (libraryObject != null)
            {
                    lock = libraryObject.lock();
                    JRXmlTemplateWriter.writeTemplate(library, libraryObject.getOutputStream(lock)); // IReportManager.getInstance().getProperty("jrxmlEncoding", System.getProperty("file.encoding") ));
            }
        } catch (Exception ex)
            {
               ex.printStackTrace();
            }
        finally {

            if (lock != null)
            {
                lock.releaseLock();
            }

        }

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized StylesLibraryTopComponent getDefault() {
        if (instance == null) {
            instance = new StylesLibraryTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the StylesLibraryTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized StylesLibraryTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(StylesLibraryTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof StylesLibraryTopComponent) {
            return (StylesLibraryTopComponent) win;
        }
        Logger.getLogger(StylesLibraryTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return StylesLibraryTopComponent.getDefault();
        }
    }
}
