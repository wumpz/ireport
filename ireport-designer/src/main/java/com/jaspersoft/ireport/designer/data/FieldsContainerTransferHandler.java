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

package com.jaspersoft.ireport.designer.data;

import com.jaspersoft.ireport.designer.FieldsContainer;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @version $Id: FieldsContainerTransferHandler.java 0 2010-01-23 10:54:11 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class FieldsContainerTransferHandler  extends TransferHandler  {

    private FieldsContainer fieldsContainer = null;

    public FieldsContainer getFieldsContainer() {
        return fieldsContainer;
    }

    public void setFieldsContainer(FieldsContainer fieldsContainer) {
        this.fieldsContainer = fieldsContainer;
    }

    public FieldsContainerTransferHandler(FieldsContainer fieldsContainer) {
        this.fieldsContainer = fieldsContainer;
    }

    public boolean importData(JComponent c, Transferable t) {
        if (canImport(c, t.getTransferDataFlavors())) {
            try {

                // We assume it is the first flavor...
                JRField field = (JRField)t.getTransferData(t.getTransferDataFlavors()[0]);

                if (fieldsContainer != null)
                {
                    fieldsContainer.addField(field);
                }

                return true;
            } catch (UnsupportedFlavorException ufe) {
            } catch (IOException ioe) {
            }
        }

        return false;
    }

    public boolean canImport(JComponent c, DataFlavor[] flavors) {

        for (int i = 0; i < flavors.length; i++) {

            if (flavors[i].getRepresentationClass().isInstance(JRField.class));
            {
                return true;
            }
        }
        return false;
    }


}
