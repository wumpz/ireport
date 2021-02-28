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
package com.jaspersoft.ireport.designer.data.fieldsproviders.xml;

import com.jaspersoft.ireport.designer.dnd.TransferableObject;
import java.awt.datatransfer.Transferable;
import javax.swing.*;
import javax.swing.tree.*;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignField;
/**
 *
 * @author  Administrator
 */
public class XMLTreeTransfertHandler extends javax.swing.TransferHandler  
//iR20 implements DragSourceMotionListener, TimingTarget 
{
    XMLFieldMappingEditor xmlEditor = null;

    public XMLFieldMappingEditor getXmlEditor() {
        return xmlEditor;
    }

    public void setXmlEditor(XMLFieldMappingEditor xmlEditor) {
        this.xmlEditor = xmlEditor;
    }
    
    /** Creates a new instance of TreeTransfertHandler 
     * @param xmlEditor 
     */
    public XMLTreeTransfertHandler(XMLFieldMappingEditor xmlEditor) {
        super();
        this.xmlEditor = xmlEditor;
    }
    
    @Override
    public int getSourceActions(JComponent c) 
    {
        return COPY_OR_MOVE;
        
    }
    
    @Override
    protected Transferable createTransferable(JComponent c) 
    {
        if (c instanceof JTree)
        {
            JTree tree = (JTree)c;
            TreePath path = tree.getLeadSelectionPath();
	    DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
            
            JRField field = getXmlEditor().createField(path, true);
            
            return new TransferableObject(field);           
        }
        
        return new TransferableObject(c);
    }
}
