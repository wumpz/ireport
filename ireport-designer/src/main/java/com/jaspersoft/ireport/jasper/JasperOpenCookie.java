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
package com.jaspersoft.ireport.jasper;

import org.openide.cookies.OpenCookie;
import com.jaspersoft.ireport.designer.utils.Misc;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author gtoffoli
 */
public class JasperOpenCookie implements OpenCookie {

    private JasperDataObject dataObject;

    public JasperOpenCookie(JasperDataObject dataObject)
    {
        this.dataObject = dataObject;
    }

    public void open() {

        if (dataObject != null)
        {
            ConvertJasperJrxmlDialog dialog = new ConvertJasperJrxmlDialog(Misc.getMainFrame(), true);
            dialog.setJasperFile( FileUtil.toFile( dataObject.getPrimaryFile() ) + "");
            dialog.setVisible(true);
        }
    }

    /**
     * @return the dataObject
     */
    public JasperDataObject getDataObject() {
        return dataObject;
    }

    /**
     * @param dataObject the dataObject to set
     */
    public void setDataObject(JasperDataObject dataObject) {
        this.dataObject = dataObject;
    }

}
