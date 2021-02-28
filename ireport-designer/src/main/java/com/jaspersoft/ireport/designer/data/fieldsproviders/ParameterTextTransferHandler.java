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

package com.jaspersoft.ireport.designer.data.fieldsproviders;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import net.sf.jasperreports.engine.design.JRDesignParameter;

/**
 *
 * @version $Id: ParameterTextTransferHandler.java 0 2010-09-08 18:59:49 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ParameterTextTransferHandler  extends TransferHandler {

    int action = TransferHandler.MOVE;

    @Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        this.action = action;
        super.exportAsDrag(comp, e, action);
    }




    @Override
  protected Transferable createTransferable(JComponent c) {

    String text = "";
    if (c instanceof JList)
    {
        JList jList = (JList)c;
        if (jList.getSelectedValue() instanceof JRDesignParameter)
        {
            JRDesignParameter p = (JRDesignParameter) jList.getSelectedValue();
            text =  ((action == MOVE) ? "$P{" : "$P!{")+ p.getName() + "}";
        }
    }
    
    return new StringSelection(text);
  }

  public int getSourceActions(JComponent c) {
    return COPY_OR_MOVE;
  }

  public boolean importData(JComponent c, Transferable t) {
    return false;
  }

  protected void exportDone(JComponent c, Transferable data, int action) {
  }

  public boolean canImport(JComponent c, DataFlavor[] flavors) {
    return false;
  }
}

