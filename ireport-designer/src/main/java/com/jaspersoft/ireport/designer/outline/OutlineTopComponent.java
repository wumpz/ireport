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
package com.jaspersoft.ireport.designer.outline;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultEditorKit;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.awt.UndoRedo;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
final public class OutlineTopComponent extends TopComponent implements ExplorerManager.Provider{

    private static OutlineTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "OutlineTopComponent";
    
    /** Dynamic Lookup content */
    private final InstanceContent ic;
    /** Lookup instance */
    private final Lookup lookup;
            
    private BeanTreeView view;
    private final ExplorerManager manager = new ExplorerManager();
    private JrxmlVisualView currentJrxmlVisualView = null;
    private boolean updatingSelection = false;
    private AbstractNode noReportNode = null;

    public synchronized boolean isUpdatingSelection() {
        return updatingSelection;
    }

    public synchronized void setUpdatingSelection(boolean updatingSelection) {
        this.updatingSelection = updatingSelection;
    }
    
    private OutlineTopComponent() {
        initComponents();

        setName(NbBundle.getMessage(OutlineTopComponent.class, "CTL_OutlineTopComponent"));
        setToolTipText(NbBundle.getMessage(OutlineTopComponent.class, "HINT_OutlineTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));
    
        this.ic = new InstanceContent();
        this.lookup = new AbstractLookup(ic);
        
        noReportNode = new AbstractNode(Children.LEAF);
        noReportNode.setDisplayName("Outline not available");
                
        ActionMap map = getActionMap();
        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(manager));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(manager));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(manager));
        map.put("delete", ExplorerUtils.actionDelete(manager, true));

        associateLookup( new ProxyLookup(lookup, ExplorerUtils.createLookup(manager, map)) );

        //setLayout(new BorderLayout());
        view = new BeanTreeView();
        //view.setRootVisible(false);
        add(view, BorderLayout.CENTER);
        
        manager.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName()))
                {
                    if (isUpdatingSelection()) return;
                    if (getCurrentJrxmlVisualView() != null)
                    {
                        Node[] sn = manager.getSelectedNodes();
                        getCurrentJrxmlVisualView().setSelectedNodes( getExplorerManager().getSelectedNodes());
                    }
                    /*
                    // Remove old selected nodes...
                    Node[] oldSelection = (Node[])evt.getOldValue();
                    Node[] newSelection = (Node[])evt.getNewValue();
                    for (int i=0; i<oldSelection.length; ++i)
                    {
                        ic.remove(oldSelection[i]);
                    }
                    for (int i=0; i<newSelection.length; ++i)
                    {
                        ic.add(newSelection[i]);
                    }
                    */
                }
            }
        });

        getExplorerManager().setRootContext(noReportNode);
        jToggleButton1.setSelected(IReportManager.getPreferences().getBoolean("filter_variables",false));
        jToggleButton2.setSelected(IReportManager.getPreferences().getBoolean("filter_parameters",false));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton1 = new javax.swing.JToggleButton();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/filter-parameters.png"))); // NOI18N
        jToggleButton2.setToolTipText("Hide built-in parameters");
        jToggleButton2.setBorderPainted(false);
        jToggleButton2.setFocusPainted(false);
        jToggleButton2.setFocusable(false);
        jToggleButton2.setMargin(new java.awt.Insets(0, 6, 0, 6));
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton2);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/filter-variables.png"))); // NOI18N
        jToggleButton1.setToolTipText("Hide built-in variables");
        jToggleButton1.setBorderPainted(false);
        jToggleButton1.setFocusPainted(false);
        jToggleButton1.setFocusable(false);
        jToggleButton1.setMargin(new java.awt.Insets(0, 6, 0, 6));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);

        add(jToolBar1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        IReportManager.getPreferences().putBoolean("filter_variables", jToggleButton1.isSelected());
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        IReportManager.getPreferences().putBoolean("filter_parameters", jToggleButton2.isSelected());
    }//GEN-LAST:event_jToggleButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized OutlineTopComponent getDefault() {
        if (instance == null) {
            instance = new OutlineTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the OutlineTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized OutlineTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(OutlineTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof OutlineTopComponent) {
            return (OutlineTopComponent)win;
        }
        Logger.getLogger(OutlineTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    public @Override int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    public @Override void componentOpened() {
        // TODO add custom code on component opening
    }

    public @Override void componentClosed() {
        // TODO add custom code on component closing
    }

    /** replaces this in object stream */
    public @Override Object writeReplace() {
        return new ResolvableHelper();
    }

    protected @Override String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {
        private static final long serialVersionUID = 1L;
        public Object readResolve() {
            return OutlineTopComponent.getDefault();
        }
    }

    public ExplorerManager getExplorerManager() {
        return manager;
    }

    public JrxmlVisualView getCurrentJrxmlVisualView() {
        return currentJrxmlVisualView;
    }

    public void closingJrxmlVisualView(JrxmlVisualView jrxmlVisualView) {
        
        if (jrxmlVisualView != currentJrxmlVisualView) return;
        setCurrentJrxmlVisualView(null);
    }
    
    public void setCurrentJrxmlVisualView(JrxmlVisualView currentJrxmlVisualView) {
        
        if (this.currentJrxmlVisualView != null)
        {
            this.ic.remove( this.currentJrxmlVisualView.getReportDesignerPanel() );
        }
        
        if (currentJrxmlVisualView == null) 
        {
            this.currentJrxmlVisualView = null;
            getExplorerManager().setRootContext(noReportNode);
        }
        if (currentJrxmlVisualView != this.currentJrxmlVisualView)
        {
            try {
                this.currentJrxmlVisualView = currentJrxmlVisualView;
                this.ic.add( this.currentJrxmlVisualView.getReportDesignerPanel() );
                setUpdatingSelection(true);
                if (currentJrxmlVisualView.getModel() != null)
                {
                    getExplorerManager().setRootContext(currentJrxmlVisualView.getModel());
                    getExplorerManager().setSelectedNodes(currentJrxmlVisualView.getExplorerManager().getSelectedNodes());
                }
                else if (currentJrxmlVisualView.isLoading())
                {
                    AbstractNode an = new AbstractNode(Children.LEAF);
                    an.setDisplayName("Loading report...");
                    getExplorerManager().setRootContext(an);
                } else
                {
                    getExplorerManager().setRootContext( currentJrxmlVisualView.getExplorerManager().getRootContext() );
                }
                
            } catch (PropertyVetoException ex) {
                Exceptions.printStackTrace(ex);
            } finally
            {
                setUpdatingSelection(false);
            }
        }
        
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                IReportManager.getInstance().fireJrxmlVisualViewActivatedListenerEvent(getCurrentJrxmlVisualView());
                JasperDesign jd = null;
                
                if (getCurrentJrxmlVisualView() != null &&
                    getCurrentJrxmlVisualView().getModel() != null)
                {
                    jd = getCurrentJrxmlVisualView().getModel().getJasperDesign();
                }
                IReportManager.getInstance().fireJasperDesignActivatedListenerEvent(jd);
            }
        });
        
    }
    
    @Override
    public UndoRedo getUndoRedo(){   
        if (currentJrxmlVisualView != null) return currentJrxmlVisualView.getUndoRedo();
        return super.getUndoRedo();   
    } 
}
