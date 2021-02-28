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
package com.jaspersoft.ireport.designer.actions;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.outline.nodes.ReportNode;
import com.jaspersoft.ireport.designer.undo.AddDatasetUndoableEdit;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.wizards.DatasetWizardIterator;
import java.awt.Dialog;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public final class AddDatasetAction extends NodeAction {

    private static AddDatasetAction instance = null;
    
    public static synchronized AddDatasetAction getInstance()
    {
        if (instance == null)
        {
            instance = new AddDatasetAction();
        }
        
        return instance;
    }
    
    private AddDatasetAction()
    {
        super();
    }
    
    
    public String getName() {
        return I18n.getString("AddDatasetAction.Name.CTL_AddDatasetAction");
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
        
        if (activatedNodes.length > 0 &&
            activatedNodes[0] instanceof ReportNode)
        {
            try {
                ReportNode node = (ReportNode) activatedNodes[0];



                DatasetWizardIterator iterator = new DatasetWizardIterator();
                WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);
                iterator.initialize(wizardDescriptor, node.getJasperDesign() );
                // {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
                // {1} will be replaced by WizardDescriptor.Iterator.name()
                wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
                wizardDescriptor.setTitle("New Dataset");
                Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
                dialog.setVisible(true);
                dialog.toFront();
                boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
                if (!cancelled) {

                    JRDesignDataset newDataset = new JRDesignDataset(false);
                    newDataset.setName( (String) wizardDescriptor.getProperty("dataset_name"));

                    List<JRDesignField> selectedFields = (List<JRDesignField>) wizardDescriptor.getProperty("selectedFields");
                    List<JRDesignField> groupFields = (List<JRDesignField>) wizardDescriptor.getProperty("groupFields");
                    String query = (String) wizardDescriptor.getProperty("query");
                    String queryLanguage = (String) wizardDescriptor.getProperty("queryLanguage");

                    if (selectedFields == null) selectedFields = new ArrayList<JRDesignField>();
                    if (groupFields == null) groupFields = new ArrayList<JRDesignField>();

                    // Adding fields
                    for (JRDesignField f : selectedFields)
                    {
                        newDataset.addField(f);
                    }

                    // Query...
                    if (query != null)
                    {
                        JRDesignQuery designQuery = new JRDesignQuery();
                        designQuery.setText(query);
                        if (queryLanguage != null)
                        {
                            designQuery.setLanguage(queryLanguage);
                        }

                        newDataset.setQuery(designQuery);
                    }

                    // Adjusting groups
                    for (int i=0; i<groupFields.size(); ++i)
                    {
                          JRDesignGroup g =new JRDesignGroup();
                          g.setName(groupFields.get(i).getName());
                          g.setExpression(Misc.createExpression(groupFields.get(i).getValueClassName(), "$F{" + groupFields.get(i).getName() + "}"));
                          newDataset.addGroup(g);
                    }


                    node.getJasperDesign().addDataset(newDataset);
                    AddDatasetUndoableEdit edit = new AddDatasetUndoableEdit(newDataset, node.getJasperDesign());
                    IReportManager.getInstance().addUndoableEdit(edit);

                }
                
            } catch (JRException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        return (activatedNodes.length > 0 && activatedNodes[0] instanceof ReportNode);
    }
}