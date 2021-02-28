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

import com.jaspersoft.ireport.designer.dnd.ReportObjectPaletteTransferable;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.jrtx.StyleNode;
import com.jaspersoft.ireport.designer.dnd.DnDUtilities;
import com.jaspersoft.ireport.designer.jrtx.TemplateNode;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
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
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.datatransfer.NewType;
import org.openide.util.datatransfer.PasteType;

/**
 *
 * @author gtoffoli
 */
public class LibraryStyleNode extends StyleNode {


    public LibraryStyleNode(JRSimpleTemplate template, JRBaseStyle style, Lookup doLkp)
    {
        super (template, style, doLkp);
    }

    @Override
    public Action[] getActions(boolean popup) {

        List<Action> actions = new ArrayList<Action>();
        actions.add(SystemAction.get( NewAction.class ));
        actions.add(null);
        actions.add(SystemAction.get( AddStyleToReportAction.class ));
        actions.add(null);

        Action[] originals = super.getActions(popup);

        for (int i=0; i<originals.length; ++i)
        {
            actions.add(originals[i]);
        }

        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Transferable drag() throws IOException {

        ExTransferable tras = ExTransferable.create(clipboardCut());
        tras.put(new ReportObjectPaletteTransferable(
                    "com.jaspersoft.ireport.designer.styles.DragStyleAction",
                    getStyle()));

        return tras;
    }

//    @SuppressWarnings("unchecked")
//    @Override
//    protected void createPasteTypes(Transferable t, List s) {
//        super.createPasteTypes(t, s);
//        PasteType paste = getDropType(t, DnDConstants.ACTION_MOVE, -1);
//        if (null != paste) {
//            s.add(paste);
//        }
//    }

    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        final Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);

        if (null != dropNode) {
            final JRDesignStyle style = dropNode.getLookup().lookup(JRDesignStyle.class);
            final JRTemplateReference reference = dropNode.getLookup().lookup(JRTemplateReference.class);
            final JRSimpleTemplate template = (getParentNode() == null) ? null : getParentNode().getLookup().lookup(JRSimpleTemplate.class);
            if (null != style && template != null) {
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

                        if (currentIndex >= 0) return null;
                        // At this point lastSystemDefinedParameterIndex contains the first valid index
                        // to add a parameter and currentIndex contains the index of the parameter in the list
                        // if present

                            try {

                                // Look for the style in the current template...


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
                                ((StylesLibraryChildren)getParentNode().getChildren()).recalculateKeys();

                            } catch (JRException ex) {
                                Exceptions.printStackTrace(ex);
                            }

                        return null;
                    };
                };
            }
            else if (null != reference && template != null) {
                return new PasteType() {

                    @SuppressWarnings("unchecked")
                    public Transferable paste() throws IOException {

                        JRTemplateReference[] ss = template.getIncludedTemplates();
                        for (int i=0; i<ss.length; ++i)
                        {
                            if (ss[i].getLocation() != null &&
                                ss[i].getLocation().equals(reference.getLocation()))
                            {
                                return null;
                            }
                        }
                        template.addIncludedTemplate(reference.getLocation());
                        ((StylesLibraryChildren)getParentNode().getChildren()).recalculateKeys();


                        return null;
                    };
                };
            }
        }
        return null;
    }

    


    @Override
    public NewType[] getNewTypes()
    {
        if (getParentNode() instanceof TemplateNode)
        {
            return ((TemplateNode)getParentNode()).getNewTypes();
        }
        return super.getNewTypes();
    }

}
