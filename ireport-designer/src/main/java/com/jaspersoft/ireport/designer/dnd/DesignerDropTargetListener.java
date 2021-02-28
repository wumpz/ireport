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

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.outline.OutlineTopComponent;
import com.jaspersoft.ireport.designer.palette.PaletteItem;
import com.jaspersoft.ireport.designer.palette.PaletteUtils;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.palette.actions.CreateDetailTextFieldsForFieldsAction;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import org.netbeans.api.visual.widget.Scene;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.datatransfer.MultiTransferObject;

/**
 *
 * @author gtoffoli
 */
class DesignerDropTargetListener implements DropTargetListener {

    public void dragEnter(DropTargetDragEvent dtde) {
        if (!acceptDataFlavor( dtde ))
        {
            dtde.rejectDrag();
            return;
        }
        
        dtde.acceptDrag( DnDConstants.ACTION_COPY_OR_MOVE );
        
    }

    public void dragOver(DropTargetDragEvent dtde) {
        // Check if we can accept that stuff...
        dragEnter(dtde);
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void drop(DropTargetDropEvent dtde) {


        if (dtde.isDataFlavorSupported(PaletteUtils.PALETTE_ITEM_DATA_FLAVOR))
        {
                PaletteItem paletteItem = null;
                try {
                        paletteItem = (PaletteItem) dtde.getTransferable().getTransferData(PaletteUtils.PALETTE_ITEM_DATA_FLAVOR);
                        paletteItem.drop(dtde);

                } catch (IOException ex) {
                        ex.printStackTrace();
                } catch (UnsupportedFlavorException ex) {
                        ex.printStackTrace();
                }

        }
        else if (dtde.isDataFlavorSupported( ExTransferable.multiFlavor))
        {
            try {

                    // In this case we are dragging a set of fields in the detail band...
                    // so we may use a simplified action...
                    MultiTransferObject mto = (MultiTransferObject) dtde.getTransferable().getTransferData(ExTransferable.multiFlavor);

                    List<JRDesignField> fields = new ArrayList<JRDesignField>();

                    int count = mto.getCount();

                    for (int i=0; i<count; ++i)
                    {
                            PaletteItem paletteItem = null;
                            paletteItem = (PaletteItem)mto.getTransferData(i, PaletteUtils.PALETTE_ITEM_DATA_FLAVOR);
                            fields.add( (JRDesignField)paletteItem.getData() );
                    }

                    if (fields.size() > 0)
                    {
                        Properties properties = new Properties();
                        properties.setProperty( PaletteItem.ACTION, CreateDetailTextFieldsForFieldsAction.class.getName());
                        PaletteItem paletteItem = new PaletteItem(properties);
                        paletteItem.setData(fields);
                        paletteItem.drop(dtde);
                    }

                } catch (Exception e) {
                    // shouldn't occure
                }
        }
    }

    private boolean acceptDataFlavor(DropTargetDragEvent dtde)
    {
        if (!isInDocument(dtde.getLocation())){
            return false;
        }

        DataFlavor[] flavors = dtde.getCurrentDataFlavors();

        for (int i = 0; i < flavors.length; i++) {
            DataFlavor dataFlavor = flavors[i];
            if (dataFlavor.match( PaletteUtils.PALETTE_ITEM_DATA_FLAVOR ))
            {
                return true;
            }
            else if (dataFlavor.match( ExTransferable.multiFlavor ))
            {
                // This is an hack. We want allow the user to drag multiple fields in the DETAIL band.
                // ONLY multiple fields. ONLY in the detail band.
                try {
                    MultiTransferObject mto = (MultiTransferObject)dtde.getTransferable().getTransferData(ExTransferable.multiFlavor);

                    if (mto.areDataFlavorsSupported(new DataFlavor[]{PaletteUtils.PALETTE_ITEM_DATA_FLAVOR}))
                    {
                        // Let's see if:
                        // 1. the drop is performed in the detail band:
                        if (!isInDetailBand(dtde.getLocation())) return false;

                        // 2. check that we are transferring Fields....
                        int count = mto.getCount();

                        for (int k=0; k<count; ++k)
                        {
                            try {
                                PaletteItem paletteItem = null;
                                paletteItem = (PaletteItem)mto.getTransferData(k, PaletteUtils.PALETTE_ITEM_DATA_FLAVOR);
                                if (!(paletteItem.getData() instanceof JRDesignField))
                                {
                                    return false;
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (UnsupportedFlavorException ex) {
                                ex.printStackTrace();
                            }
                        }

                        return true;
                    }
                } catch (Exception e) {
                    // shouldn't occure
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    
    private boolean isInDocument(Point location)
    {
        Scene scene = OutlineTopComponent.getDefault().getCurrentJrxmlVisualView().getReportDesignerPanel().getActiveScene();
        if (scene != null && scene instanceof AbstractReportObjectScene)
        {
            return ((AbstractReportObjectScene)scene).acceptDropAt(location);
        }
        return false;
    }

    private boolean isInDetailBand(Point location)
    {
        Scene scene = OutlineTopComponent.getDefault().getCurrentJrxmlVisualView().getReportDesignerPanel().getActiveScene();
        if (scene != null && scene instanceof ReportObjectScene)
        {
            JasperDesign jd = ((ReportObjectScene)scene).getJasperDesign();
            Point p = scene.convertViewToScene(location);
            
            JRDesignBand band = ModelUtils.getBandAt(jd, p);

            if (band != null && band.getOrigin().getBandTypeValue().equals(BandTypeEnum.DETAIL))
            {
                return true;
            }
        }
        return false;
    }
}
