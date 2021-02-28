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
package com.jaspersoft.ireport.designer;

import java.beans.PropertyVetoException;
import org.netbeans.core.spi.multiview.CloseOperationHandler;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.openide.text.DataEditorSupport;

/**
 *
 * @author gtoffoli
 */
public class GenericCloseOperationHandler implements CloseOperationHandler{

    DataEditorSupport support;

    public GenericCloseOperationHandler(DataEditorSupport support)
    {
        this.support = support;
    }

    public boolean resolveCloseOperation(CloseOperationState[] elements) {
        try {
            support.getDataObject().setValid(false);
        } catch (PropertyVetoException ex) {
            return false;
        }
        System.gc();
        return true;
        

        /*
        
        //support.getDataObject().setModified(false);

        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(
                "Save before closing?");
        DialogDisplayer.getDefault().notify(nd);
        if (nd.getValue().equals(NotifyDescriptor.YES_OPTION)) {
            // Let's consider only the first...
            for (CloseOperationState element : elements) {
                System.out.println("Executing action procees on element: " + element.getCloseWarningID());
                System.out.flush();
                element.getProceedAction().actionPerformed(
                        new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "xxx"));
            }
            return true;
        } else if (nd.getValue().equals(NotifyDescriptor.NO_OPTION)) {
            for (CloseOperationState element : elements) {
                System.out.println("Executing action procees on element: " + element.getCloseWarningID());
                System.out.flush();
                element.getDiscardAction().actionPerformed(
                        new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "xxx"));
            }
            return true;
        } else {
            // Cancel
            return false;
        }
        */
    }
}
