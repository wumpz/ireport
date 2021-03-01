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

import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.File;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @version $Id: JrxmlNotifier.java 0 2009-12-09 11:48:09 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class JrxmlNotifier implements FileResourceUpdatingListener {

    public void resourceWillBeUpdated(RepositoryFile repositoryFile, RepositoryReportUnit reportUnit, File file) throws Exception {

        try {
            JasperDesign jd = (JasperDesign) JRXmlLoader.load(file);
            if (jd.getQuery() != null &&
                jd.getQuery().getLanguage() != null &&
                jd.getQuery().getLanguage().equalsIgnoreCase("xmla-mdx"))
            {
                JOptionPane.showMessageDialog(Misc.getMainFrame(), "You are trying to upload on JasperServer a report which uses the xmla-mdx query language.\nThis language is not supported by JasperServer and it is outdated.\nYou should use the mdx language instead.", "Unsupported language", JOptionPane.WARNING_MESSAGE);

            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    public void resourceUpdated(RepositoryFile rf, RepositoryReportUnit reportUnit, File file) throws Exception {
    }



}
