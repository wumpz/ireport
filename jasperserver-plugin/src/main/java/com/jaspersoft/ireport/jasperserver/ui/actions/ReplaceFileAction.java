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
package com.jaspersoft.ireport.jasperserver.ui.actions;

import com.jaspersoft.ireport.JrxmlDataObject;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.jrtx.JRTXEditorSupport;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.ActiveEditorTopComponentListener;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFile;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ReportUnitNode;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ResourceNode;
import com.jaspersoft.ireport.jasperserver.ui.wizards.ReportUnitWizardDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.DataEditorSupport;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.Mutex;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class ReplaceFileAction extends NodeAction {

    public String getName() {
        return NbBundle.getMessage(ReplaceFileAction.class, "CTL_ReplaceFileAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {
        
        if (!(activatedNodes[0] instanceof ResourceNode)) return;
        
        ResourceNode node = (ResourceNode)activatedNodes[0];
        
        if (((ResourceNode)activatedNodes[0]).getRepositoryObject() instanceof RepositoryFile)
        {
            final RepositoryFile rf = (RepositoryFile)((ResourceNode)activatedNodes[0]).getRepositoryObject();
            ReportUnitNode reportUnitNode = null;

            // Check if we are inside a report unit...
            RepositoryReportUnit reportUnit = null;

            String reportUnitUri;
            if (activatedNodes[0].getParentNode() instanceof ResourceNode &&
                ((ResourceNode)activatedNodes[0].getParentNode()).getResourceDescriptor().getWsType().equals( ResourceDescriptor.TYPE_REPORTUNIT))
            {
                if ( ((ResourceNode)activatedNodes[0].getParentNode()).getRepositoryObject() instanceof RepositoryReportUnit)
                {
                    reportUnit = (RepositoryReportUnit)((ResourceNode)activatedNodes[0].getParentNode()).getRepositoryObject();
                    if (activatedNodes[0].getParentNode() instanceof ReportUnitNode)
                    {
                        reportUnitNode = (ReportUnitNode)activatedNodes[0].getParentNode();
                    }
                    else if (activatedNodes[0].getParentNode().getParentNode() instanceof ReportUnitNode)
                    {
                        reportUnitNode = (ReportUnitNode)activatedNodes[0].getParentNode().getParentNode();
                    }
                }
            }

            final RepositoryReportUnit finalReportUnit = reportUnit;
            final ReportUnitNode finalReportUnitNode = reportUnitNode;

            if (rf.getDescriptor().getWsType() != null &&
                rf.getDescriptor().getWsType().equals(ResourceDescriptor.TYPE_JRXML))
            {
                // Get the current file...
                final JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
                if (view != null && view.getLookup() != null)
                {
                    JrxmlDataObject dobject = view.getLookup().lookup(JrxmlDataObject.class);
                    if (dobject != null)
                    {
                        SaveCookie scookie = view.getLookup().lookup(SaveCookie.class);
                        if (scookie != null)
                        {
                            int answer = MessageDisplayer.displayMessage("The current file has been modified, do you want to save it before send its content to the server?", "Updating report");
                            if (answer == JOptionPane.CANCEL_OPTION)
                            {
                                return;

                            }

                            if (answer == JOptionPane.YES_OPTION)
                            {
                                try {
                                    scookie.save();
                                } catch (IOException ex) {
                                    Exceptions.printStackTrace(ex);
                                }
                            }
                        }

                        final String fileName = FileUtil.toFile(dobject.getPrimaryFile()).getPath();
                        

                        Thread t = new Thread(new Runnable() {

                            public void run() {
                                try {

                                    File file = new File(fileName);

                                    String ruUri = null;

                                    JasperServerManager.getMainInstance().fireResourceReplacing_resourceWillBeUpdated(rf, finalReportUnit, file);

                                    if (finalReportUnit != null)
                                    {
                                        // Add extra resources...
                                        ruUri = finalReportUnit.getDescriptor().getUriString();
                                        ReportUnitWizardDescriptor.addRequiredResources(rf.getServer(), file, view.getEditorSupport().getCurrentModel(), finalReportUnit.getDescriptor(), rf.getDescriptor());
                                    }

                                    rf.getServer().getWSClient().modifyReportUnitResource(ruUri, rf.getDescriptor(),file);

                                    JasperServerManager.getMainInstance().fireResourceReplacing_resourceUpdated(rf, finalReportUnit, file);

                                    Mutex.EVENT.readAccess(new Runnable() {

                                        public void run() {
                                            JOptionPane.showMessageDialog(Misc.getMainFrame(),
                                            JasperServerManager.getString("repositoryExplorer.message.fileUpdated", "File successfully updated."),
                                            JasperServerManager.getString("repositoryExplorer.message.operationResult", "Operation result"), JOptionPane.INFORMATION_MESSAGE);
                                        }
                                    });

                                    if (finalReportUnitNode != null)
                                    {
                                        SwingUtilities.invokeAndWait(new Runnable() {

                                            public void run() {
                                                    finalReportUnitNode.updateDisplayName();
                                                    finalReportUnitNode.refreshChildrens(true);
                                                }
                                            });
                                    }

                                } catch (Exception ex)
                                {
                                    final String msg = ex.getMessage();
                                    Mutex.EVENT.readAccess(new Runnable() {

                                        public void run() {
                                             JOptionPane.showMessageDialog(Misc.getMainFrame(),JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {msg}),"Operation result", JOptionPane.ERROR_MESSAGE);
                                        }
                                    });
                                    ex.printStackTrace();
                                }

                            }
                        });

                        t.start();
                    }
                }
            }
            else
            {
                TopComponent tc = ActiveEditorTopComponentListener.getDefaultInstance().getActiveEditorTopComponent();

                DataObject dobj = tc.getLookup().lookup(DataObject.class);
                if (dobj == null) return;

                final File file = FileUtil.toFile(dobj.getPrimaryFile());
                if (file.exists())
                {
                    Thread t = new Thread(new Runnable() {

                            public void run() {
                                try {

                                    String ruUri = null;

                                    // Avoid to notify this type of resource update...
                                    // JasperServerManager.getMainInstance().fireResourceReplacing_resourceWillBeUpdated(rf, finalReportUnit, jrtxFile);

                                    if (finalReportUnit != null)
                                    {
                                        // Add extra resources...
                                        ruUri = finalReportUnit.getDescriptor().getUriString();
                                    }

                                    rf.getServer().getWSClient().modifyReportUnitResource(ruUri, rf.getDescriptor(),file);

                                    //JasperServerManager.getMainInstance().fireResourceReplacing_resourceUpdated(rf, finalReportUnit, file);

                                    Mutex.EVENT.readAccess(new Runnable() {

                                        public void run() {
                                            JOptionPane.showMessageDialog(Misc.getMainFrame(),
                                            JasperServerManager.getString("repositoryExplorer.message.fileUpdated", "File successfully updated."),
                                            JasperServerManager.getString("repositoryExplorer.message.operationResult", "Operation result"), JOptionPane.INFORMATION_MESSAGE);
                                        }
                                    });

                                    if (finalReportUnitNode != null)
                                    {
                                        SwingUtilities.invokeAndWait(new Runnable() {

                                            public void run() {
                                                    finalReportUnitNode.updateDisplayName();
                                                    finalReportUnitNode.refreshChildrens(true);
                                                }
                                            });
                                    }

                                } catch (Exception ex)
                                {
                                    final String msg = ex.getMessage();
                                    Mutex.EVENT.readAccess(new Runnable() {

                                        public void run() {
                                             JOptionPane.showMessageDialog(Misc.getMainFrame(),JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {msg}),"Operation result", JOptionPane.ERROR_MESSAGE);
                                        }
                                    });
                                    ex.printStackTrace();
                                }

                            }
                        });

                        t.start();
                }



            }
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        if ( activatedNodes[0] instanceof ResourceNode &&
             ((ResourceNode)activatedNodes[0]).getResourceDescriptor().getWsType().equals( ResourceDescriptor.TYPE_JRXML))
        {
            JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
            if (view != null && view.getLookup() != null)
            {
                JrxmlDataObject dobject = view.getLookup().lookup(JrxmlDataObject.class);
                if (dobject != null)
                {
                    return true;
                }
            }
        }
        if ( activatedNodes[0] instanceof ResourceNode &&
             ((ResourceNode)activatedNodes[0]).getRepositoryObject() instanceof RepositoryFile)
        {
            // Get the active document...
            TopComponent tc = ActiveEditorTopComponentListener.getDefaultInstance().getActiveEditorTopComponent();
            if (tc != null) return true;
        }
        return false;
    }

}