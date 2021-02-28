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
package com.jaspersoft.ireport.designer.menu;

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.SaveAsCapable;
import org.openide.loaders.DataObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Action to save document under a different file name and/or extension.
 * The action is enabled for editor windows only.
 *
 * @since 6.3
 * @author S. Aubrecht
 */
final public class SaveAsAction extends AbstractAction implements ContextAwareAction, LookupListener, PropertyChangeListener {

    private Lookup context;
    private Lookup.Result<SaveAsCapable> lkpInfo;
    private boolean isEditorWindowActivated;

    public SaveAsAction() {
        this( Utilities.actionsGlobalContext() );
        TopComponent.getRegistry().addPropertyChangeListener( this );
    }

    public SaveAsAction( Lookup context ) {
        super( NbBundle.getMessage(DataObject.class, "CTL_SaveAsAction") ); //NOI18N
        this.context = context;
        putValue("noIconInMenu", Boolean.TRUE); //NOI18N
    }

    /**
     * Method is called from XML layers to create action instance for the main menu/toolbar.
     * @return Global instance for menu/toolbar
     */
    public static ContextAwareAction create() {
        return new SaveAsAction();
    }

    void init() {
        assert SwingUtilities.isEventDispatchThread()
               : "this shall be called just from AWT thread";

        if (lkpInfo != null) {
            return;
        }

        //The thing we want to listen for the presence or absence of
        //on the global selection
        Lookup.Template<SaveAsCapable> tpl = new Lookup.Template<SaveAsCapable>(SaveAsCapable.class);
        lkpInfo = context.lookup (tpl);
        lkpInfo.addLookupListener(this);
        propertyChange(null);
    }

    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }

    public void actionPerformed(ActionEvent e) {
        init();
        Collection<? extends SaveAsCapable> inst = lkpInfo.allInstances();
        if( inst.size() > 0 ) {
            SaveAsCapable saveAs = inst.iterator().next();
            File newFile = getNewFileName();
            if( null != newFile ) {


                if (newFile.exists())
                {
                    if (JOptionPane.showConfirmDialog(Misc.getMainFrame(),
                            I18n.getString("LBL_File_Exists", newFile.getName()),
                            NbBundle.getMessage(DataObject.class, "CTL_SaveAsAction"),
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE) != JOptionPane.OK_OPTION)
                    {
                        return;
                    }
                }

                //create target folder if necessary
                FileObject newFolder = null;
                try {
                    File targetFolder = newFile.getParentFile();
                    if( null == targetFolder )
                        throw new IOException(newFile.getAbsolutePath());

                    
                    if (!targetFolder.exists())
                    {
                        newFolder = FileUtil.createFolder( targetFolder );
                    }
                    else
                    {
                        newFolder = FileUtil.toFileObject(targetFolder);
                    }
                } catch( IOException ioE ) {

                    ioE.printStackTrace();
                    
                    NotifyDescriptor error = new NotifyDescriptor(
                            NbBundle.getMessage(DataObject.class, "MSG_CannotCreateTargetFolder"), //NOI18N
                            NbBundle.getMessage(DataObject.class, "LBL_SaveAsTitle"), //NOI18N
                            NotifyDescriptor.DEFAULT_OPTION,
                            NotifyDescriptor.ERROR_MESSAGE,
                            new Object[] {NotifyDescriptor.OK_OPTION},
                            NotifyDescriptor.OK_OPTION );
                    DialogDisplayer.getDefault().notify( error );
                    return;
                }

                try {
                    saveAs.saveAs( newFolder, newFile.getName() );
                } catch( IOException ioE ) {
                    Exceptions.attachLocalizedMessage( ioE, NbBundle.getMessage( DataObject.class, "MSG_SaveAsFailed" ) );  //NOI18N
                    Logger.getLogger( getClass().getName() ).log( Level.WARNING, null, ioE );
                }
            }
        }
    }

    public void resultChanged(LookupEvent ev) {
        setEnabled (null != lkpInfo && lkpInfo.allItems().size() != 0 && isEditorWindowActivated );
    }

    /**
     * Show file 'save as' dialog window to ask user for a new file name.
     * @return File selected by the user or null if no file was selected.
     */
    private File getNewFileName() {
        File newFile = null;
        FileObject currentFileObject = getCurrentFileObject();
        if( null != currentFileObject )
            newFile = FileUtil.toFile( currentFileObject );

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle( NbBundle.getMessage(DataObject.class, "LBL_SaveAsTitle" ) ); //NOI18N
        chooser.setMultiSelectionEnabled( false );
        if( null != newFile ) {
            chooser.setSelectedFile( newFile );
            FileUtil.preventFileChooserSymlinkTraversal( chooser, newFile.getParentFile() );
        }
        File origFile = newFile;
        if( JFileChooser.APPROVE_OPTION != chooser.showSaveDialog( WindowManager.getDefault().getMainWindow() ) ) {
            return null;
        }
        newFile = chooser.getSelectedFile();
        if( null == newFile || newFile.equals( origFile ) )
            return null;

        return newFile;
    }

    private FileObject getCurrentFileObject() {
        TopComponent tc = TopComponent.getRegistry().getActivated();
        if( null != tc ) {
            DataObject dob = tc.getLookup().lookup( DataObject.class );
            if( null != dob )
                return dob.getPrimaryFile();
        }
        return null;
    }

    public Action createContextAwareInstance(Lookup actionContext) {
        return new SaveAsAction( actionContext );
    }

    public void propertyChange(PropertyChangeEvent arg0) {
        TopComponent tc = TopComponent.getRegistry().getActivated();

        isEditorWindowActivated = null != tc && WindowManager.getDefault().isEditorTopComponent( tc );

        resultChanged( null );
    }
}

