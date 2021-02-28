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

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.dnd.DnDUtilities;
import com.jaspersoft.ireport.designer.outline.nodes.IRIndexedNode;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import org.openide.actions.NewAction;
import org.openide.actions.PasteAction;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.NewType;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;


/**
 *
 * @author gtoffoli
 */
public class TemplateNode extends IRIndexedNode {

    private static final String IMAGE_ICON_BASE = "com/jaspersoft/ireport/designer/resources/jasperreports_jrtx.png";

    private JRSimpleTemplate template = null;

    public TemplateNode(JRSimpleTemplate template, Lookup doLkp)
    {
        this(new StylesChildren(template,doLkp), template, doLkp);
    }

    public TemplateNode(StylesChildren pc, JRSimpleTemplate template, Lookup doLkp)
    {

        super(pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.singleton(template)));


        this.template = template;
        setName("Styles");
        setIconBaseWithExtension(IMAGE_ICON_BASE);

        addNodeListener(new NodeListener() {

            public void childrenAdded(NodeMemberEvent ev) {
                //System.out.println("childrenAdded");
            }

            public void childrenRemoved(NodeMemberEvent ev) {
                //System.out.println("childrenRemoved");
            }

            @SuppressWarnings("unchecked")
            public void childrenReordered(NodeReorderEvent ev) {

                JRStyle[] styles = getTemplate().getStyles();
                ArrayList<JRStyle> newList = new ArrayList<JRStyle>();
                ArrayList<JRTemplateReference> newList2 = new ArrayList<JRTemplateReference>();

                Node[] nodes = getChildren().getNodes();
                for (int i = 0; i < nodes.length; ++i) {
                    if (nodes[i] instanceof StyleNode)
                    {
                        JRBaseStyle s = ((StyleNode) nodes[i]).getStyle();
                        newList.add(s);
                        getTemplate().removeStyle(s);
                    }
                    else if (nodes[i] instanceof TemplateReferenceNode)
                    {
                        JRTemplateReference s = ((TemplateReferenceNode) nodes[i]).getReference();
                        newList2.add(s);
                        getTemplate().removeIncludedTemplate(s);
                    }
                }

               for (JRStyle s : newList)
               {
                    try {
                        getTemplate().addStyle(s);
                    } catch (JRException ex) {
                    }
               }

               for (JRTemplateReference s : newList2)
               {
                        getTemplate().addIncludedTemplate(s);
               }

               JRTXEditorSupport ed = getLookup().lookup(JRTXEditorSupport.class);
               if (ed != null) ed.notifyModelChangeToTheView();

               // Recalculate the childrens...
               ((StylesChildren)getChildren()).recalculateKeys();

            }

            public void nodeDestroyed(NodeEvent ev) {
                //System.out.println("nodeDestroyed");
            }

            public void propertyChange(PropertyChangeEvent evt) {
                //System.out.println("propertyChange " + evt.getPropertyName());
            }
        });

    }


     /*
     * @return false to signal that the customizer should not be used.
     *  Subclasses can override this method to enable customize action
     *  and use customizer provided by this class.
     */
    @Override
    public boolean hasCustomizer() {
        return true;
    }



    @Override
    public NewType[] getNewTypes()
    {
        return new NewType[]{


            new NewType() {

                @Override
                public void create() throws IOException {
                    

                    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( Misc.findStartingDirectory()  );
                    jfc.setDialogTitle("Select a JRTX file....");
                    jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                        public boolean accept(java.io.File file) {
                            String filename = file.getName();
                            return (filename.endsWith(".jrtx") ||
                                    file.isDirectory()) ;
                        }
                        public String getDescription() {
                            return "JasperReports Template (JRTX) *.jrtx";
                        }
                    });

                    jfc.setMultiSelectionEnabled(true);

                    jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
                    if  (jfc.showOpenDialog( Misc.getMainFrame()) == javax.swing.JOptionPane.OK_OPTION)
                    {
                        File[] files = jfc.getSelectedFiles();

                        for (int i=0; i<files.length; ++i)
                        {
                        // Find the first available name...
                            JRTemplateReference reference = new JRTemplateReference();
                            reference.setLocation( files[i].getPath());
                            template.addIncludedTemplate(reference);
                        }
                        ((StylesChildren)getChildren()).recalculateKeys();
                        JRTXEditorSupport ed = getLookup().lookup(JRTXEditorSupport.class);
                        if (ed != null) ed.notifyModelChangeToTheView();
                    }
                }

                @Override
                public String getName() {
                    return "Template Reference";
                }


            },

            new NewType() {

                @Override
                public void create() throws IOException {
                    JRDesignStyle style = new JRDesignStyle();
                    // Find the first available name...


                    List names = new ArrayList();
                    JRStyle[] styles = getTemplate().getStyles();
                    for (int n=0; n<styles.length; ++n)
                    {
                        names.add( styles[n].getName());
                    }

                    int i=1;
                    while (names.contains("Style_"+i))
                    {
                        i++;
                    }

                    style.setName("Style_"+i);
                    try {
                        template.addStyle(style);
                        ((StylesChildren)getChildren()).recalculateKeys();
                    } catch (JRException ex) {
                        //Exceptions.printStackTrace(ex);
                    }

                    JRTXEditorSupport ed = getLookup().lookup(JRTXEditorSupport.class);
                    if (ed != null) ed.notifyModelChangeToTheView();

                }

                @Override
                public String getName() {
                    return "Style";
                }


            }

        };
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            SystemAction.get(NewAction.class),
            SystemAction.get(PasteAction.class)};
    }

    /**
     * @return the template
     */
    public

    JRSimpleTemplate getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(JRSimpleTemplate template) {
        this.template = template;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void createPasteTypes(Transferable t, List s) {
        super.createPasteTypes(t, s);
        PasteType paste = getDropType(t, DnDConstants.ACTION_MOVE, -1);
        if (null != paste) {
            s.add(paste);
        }
    }

    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        final Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        final int dropAction = DnDUtilities.getTransferAction(t);

        if (null != dropNode) {
            final JRDesignStyle style = dropNode.getLookup().lookup(JRDesignStyle.class);
            if (null != style) {
                return new PasteType() {

                    @SuppressWarnings("unchecked")
                    public Transferable paste() throws IOException {

                        
                        List list = new ArrayList();
                        JRStyle[] ss = template.getStyles();
                        for (int i=0; i<ss.length; ++i)
                        {
                            list.add((JRDesignStyle)ss[i]);
                        }


                        int currentIndex = -1; //Current position in the list

                        for (int i = 0; i < list.size(); ++i) {
                            JRDesignStyle s = (JRDesignStyle) list.get(i);
                            if (s == style) {
                                currentIndex = i;
                            }
                        }

                        // At this point lastSystemDefinedParameterIndex contains the first valid index
                        // to add a parameter and currentIndex contains the index of the parameter in the list
                        // if present

                        System.out.println("Duplicating style in " + getDisplayName() + " " + dropAction + " " + (dropAction & NodeTransfer.MOVE));
                            System.out.flush();

                        if( (dropAction & NodeTransfer.MOVE) != 0 ) // Moving parameter...
                        {
                            int newIndex = -1;

                            if (currentIndex != -1) { // Case 1: Moving in the list...
                                // Put the field in a valid position...
                                // Find the position of the node...
                                Node[] nodes = getChildren().getNodes();
                                for (int i = 0; i < nodes.length; ++i) {
                                    if (((StyleNode) nodes[i]).getStyle() == style) {
                                        newIndex = i;
                                        break;
                                    }
                                }

                                list.remove(style);
                                if (newIndex == -1)
                                {
                                    list.add(style);
                                    newIndex = list.indexOf(style);
                                }
                                else
                                {
                                    list.add(newIndex,style );
                                }

                                JRTXEditorSupport ed = getLookup().lookup(JRTXEditorSupport.class);
                                if (ed != null) ed.notifyModelChangeToTheView();

                                //AddStyleUndoableEdit undo = new AddStyleUndoableEdit(style, getJasperDesign()); //newIndex
                                //IReportManager.getInstance().addUndoableEdit(undo);
                            }
                        }
                        else // Duplicating
                        {
                            


                            try {
                                JRDesignStyle newStyle = ModelUtils.cloneStyle(style);

                                // fix the name...
                                List names = new ArrayList();
                                String base = newStyle.getName();
                                JRStyle[] styles = getTemplate().getStyles();
                                for (int n=0; n<styles.length; ++n)
                                {
                                    names.add( styles[n].getName());
                                }

                                int i=1;
                                while (names.contains(base+i))
                                {
                                    i++;
                                }

                                newStyle.setName(base+i);


                                template.addStyle(newStyle);
                                //AddStyleUndoableEdit undo = new AddStyleUndoableEdit(style, getJasperDesign()); //newIndex
                                //IReportManager.getInstance().addUndoableEdit(undo);
                                ((StylesChildren)getChildren()).recalculateKeys();

                                JRTXEditorSupport ed = getLookup().lookup(JRTXEditorSupport.class);
                                if (ed != null) ed.notifyModelChangeToTheView();

                            } catch (JRException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }

                        return null;
                    };
                };
            }
        }
        return null;
    }
}
