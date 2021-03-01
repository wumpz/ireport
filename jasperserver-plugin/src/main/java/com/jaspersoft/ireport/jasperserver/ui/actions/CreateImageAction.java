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

import com.jaspersoft.ireport.designer.palette.actions.CreateReportElementAction;
import com.jaspersoft.ireport.jasperserver.RepoImageCache;
import com.jaspersoft.ireport.jasperserver.RepositoryFile;
import java.io.File;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRExpressionUtil;

/**
 *
 * @author gtoffoli
 */
public class CreateImageAction  extends CreateReportElementAction 
{

    public JRDesignElement createReportElement(JasperDesign jd)
    {
            RepositoryFile file = (RepositoryFile) getPaletteItem().getData();
            
            
            JRDesignElement element = new JRDesignImage(jd);
            element.setWidth(100);
            element.setHeight(50);
            
            JRDesignExpression exp = new JRDesignExpression();
            exp.setValueClassName("java.lang.String");
            exp.setText("\"repo:" + file.getDescriptor().getUriString()+"\"");
            
            // Try to load the image...
            try {
                String fname = file.getFile();
                
                ImageIcon img = new ImageIcon(fname);
                element.setWidth(img.getIconWidth());
                element.setHeight(img.getIconHeight());
                
                RepoImageCache.getInstance().put( JRExpressionUtil.getSimpleExpressionText(exp) , new File(fname));
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            ((JRDesignImage)element).setExpression(exp);
            
            return element;
    }
}
