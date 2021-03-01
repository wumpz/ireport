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

package com.jaspersoft.ireport.jasperserver;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlEditorSupport;
import com.jaspersoft.ireport.designer.SubreportOpenerProvider;
import com.jaspersoft.ireport.designer.utils.ExpressionInterpreter;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @version $Id: JSSubreportOpenerProvider.java 0 2009-10-19 10:36:07 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class JSSubreportOpenerProvider implements SubreportOpenerProvider {

    public File openingSubreport(JrxmlEditorSupport ed, JRDesignSubreport subreportElement, File subreportFile) {

        if (subreportFile != null) return subreportFile;

        if (subreportElement.getExpression() != null &&
            subreportElement.getExpression().getValueClassName() != null &&
            subreportElement.getExpression().getValueClassName().equals("java.lang.String"))
        {
            JasperDesign jasperDesign = ed.getCurrentModel();
            JRDesignDataset dataset =  jasperDesign.getMainDesignDataset();

            ExpressionInterpreter interpreter = new ExpressionInterpreter(dataset, IReportManager.getReportClassLoader());

            Object obj = interpreter.interpretExpression( subreportElement.getExpression().getText() );

            if (obj instanceof String &&
                obj != null)
            {
                String exp_value = (String)obj;
                if (exp_value.startsWith("repo:"))
                {
                    String repo_url = exp_value.substring(5);
                    if (repo_url.length() > 0)
                    {
                        File currentfile = FileUtil.toFile( ed.getDataObject().getPrimaryFile() );
                        RepositoryReportUnit reportUnit = JasperServerManager.getMainInstance().getJrxmlReportUnitMap().get( currentfile.getPath() );

                        JServer server = reportUnit.getServer();

                        if (repo_url.startsWith("/"))
                        {
                            // independent resource...
                        }
                        else
                        {
                            // resource inside the report unit...
                            List<RepositoryFolder> children = reportUnit.getChildren(true);
                            for (RepositoryFolder child : children)
                            {
                                if (child.getDescriptor().getName().equals(repo_url))
                                {
                                    if (child instanceof RepositoryJrxmlFile)
                                    {
                                        try {
                                            return new File(((RepositoryJrxmlFile) child).getFile());
                                        } catch (Exception ex) {
                                            return subreportFile;
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
            // check if the expression starts with repo:....

        }

        return subreportFile;
    }

}
