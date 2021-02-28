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

import java.awt.dnd.DropTargetDropEvent;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author gtoffoli
 */
public abstract class PaletteItemAction {

    private PaletteItem paletteItem = null;
    private Scene scene = null;
    private JasperDesign jasperDesign = null;
    
    public PaletteItem getPaletteItem() {
        return paletteItem;
    }

    public void setPaletteItem(PaletteItem paletteItem) {
        this.paletteItem = paletteItem;
    }
    
    /**
     * It can be a ReportObjectScene or a scene relative to a Crosstab...
     */
    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }
    
    public abstract void drop(java.awt.dnd.DropTargetDropEvent dtde);
    
}
