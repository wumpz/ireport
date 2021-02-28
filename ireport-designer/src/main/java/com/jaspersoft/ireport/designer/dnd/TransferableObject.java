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

/**
 *
 * @author  Administrator
 */
public class TransferableObject implements java.awt.datatransfer.Transferable {
    
    Object obj;
    java.awt.datatransfer.DataFlavor thisFlavor;
    /** Creates a new instance of TransferableObject */
    public TransferableObject(Object obj) {
        this.obj = obj;
        thisFlavor = new java.awt.datatransfer.DataFlavor(obj.getClass(), obj.getClass().getName());
    }
    
    public Object getTransferData(java.awt.datatransfer.DataFlavor flavor) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        if (flavor.equals( thisFlavor ))
        {
            return obj;
       }
       // GDN new code start
       else
       if (flavor.equals( java.awt.datatransfer.DataFlavor.stringFlavor )) {
            return new String();    // anything non-null
       }
       else
       // GDN new code end
            return null;
    }
    
    public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
        // GDN new code start
        java.awt.datatransfer.DataFlavor stringFlavor = java.awt.datatransfer.DataFlavor.stringFlavor;
        return new java.awt.datatransfer.DataFlavor[] { thisFlavor,
                                                        stringFlavor };
        // GDN new code end
        
        // GDN comment out-->return new java.awt.datatransfer.DataFlavor[]{thisFlavor};
    }
    
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
// GDN comment out
//        if (flavor != null && flavor.equals( thisFlavor ))
//        {
//            return true;
//        }
//        
//        return false;
// GDN comment out
        // GDN new code begin
        if (flavor == null)
            return false;
        else
        if (flavor.equals( thisFlavor ))
            return true;
        else
        if (flavor.equals( java.awt.datatransfer.DataFlavor.stringFlavor ))
            return true;
        else
            return false;
        // GDN new code end
    }
    
}
