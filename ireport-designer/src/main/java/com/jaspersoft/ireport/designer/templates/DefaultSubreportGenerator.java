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
package com.jaspersoft.ireport.designer.templates;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.File;
import java.io.FileOutputStream;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author gtoffoli
 */
public class DefaultSubreportGenerator extends DefaultReportGenerator {

    public FileObject generateReport(WizardDescriptor wizard) {
        
        try {
            // 1. Load the selected template...
            JasperDesign jasperDesign = generateDesign(wizard);
            
            int newPageWidth = jasperDesign.getPageWidth() - jasperDesign.getLeftMargin() - jasperDesign.getRightMargin();
            int newPageHeight = jasperDesign.getPageHeight() - jasperDesign.getTopMargin() - jasperDesign.getBottomMargin();
            
            jasperDesign.setTopMargin(0);
            jasperDesign.setBottomMargin(0);
            jasperDesign.setLeftMargin(0);
            jasperDesign.setRightMargin(0);
            
            jasperDesign.setPageWidth(newPageWidth);
            jasperDesign.setPageHeight(newPageHeight);
            
            File f = getFile(wizard);
            FileOutputStream os = new FileOutputStream(f);
            JRXmlWriter.writeReport(jasperDesign, os ,"UTF8");
            
            try {
                os.close();
            } catch (Exception ex) {}
            return FileUtil.toFileObject(f);
        
        } catch (Exception ex) {
            ex.printStackTrace();
            Misc.showErrorMessage("An error has occurred generating the subreport:\n" + ex.getMessage(), "Error", ex);
            return null;
        }
    }
}
