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
package com.jaspersoft.ireport.designer.palette;

import java.awt.datatransfer.DataFlavor;
import java.util.Properties;
import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class PaletteUtils {

    public static final DataFlavor PALETTE_ITEM_DATA_FLAVOR = new DataFlavor( PaletteItem.class, "IR_PaletteItem");
    
    public static PaletteController controller = null;
    public static PaletteController createPalette() {
        try {
            if (controller == null)
            { 
                controller = PaletteFactory.createPalette("ireport/palette", // NOI18N
            
                                                new PaletteActions() {
                public Action[] getCustomCategoryActions(Lookup lookup) {
                    return new Action[0];
                }
                public Action[] getCustomItemActions(Lookup lookup) {
                    return new Action[0];
                }
                public Action[] getCustomPaletteActions() {
                    return new Action[0];
                }
                public Action[] getImportActions() {
                    return new Action[0];
                }
                public Action getPreferredAction(Lookup lookup) {
                    return null; //TODO
                }
            });
          }
          return controller;

        }
        catch (Throwable ex) {
            
            ex.printStackTrace();
            //ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, ex);
            return null;
        }
    }
}
