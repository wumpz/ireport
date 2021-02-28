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
package com.jaspersoft.ireport.designer.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 *
 * @author gtoffoli
 */
public class DnDUtilities {
    
    public static final int getTransferAction(Transferable t)
    {
        String subtype = "x-java-openide-nodednd"; // NOI18N
        String primary = "application"; // NOI18N
        String mask = "mask"; // NOI18N
        DataFlavor[] flavors = t.getTransferDataFlavors();
        for (int i = 0; i <flavors.length; i++) {
            DataFlavor df = flavors[i];

            if (df.getSubType().equals(subtype) && df.getPrimaryType().equals(primary)) {
                try {
                    int m = Integer.valueOf(df.getParameter(mask)).intValue();
                    return m;
                } catch (NumberFormatException nfe) {}
            }
        }
        return -1;
    }
}
