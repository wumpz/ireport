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
package com.jaspersoft.ireport.designer.styles;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.palette.PaletteItemAction;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class DragTemplateReferenceAction extends PaletteItemAction {

    @Override
    public void drop(DropTargetDropEvent dtde) {

        JasperDesign jd = getJasperDesign();
        Object toActivate = null;

        JRTemplateReference reference = (JRTemplateReference)getPaletteItem().getData();
        if (reference != null)
        {
                // Copy the reference...
                JRDesignReportTemplate reportTemplate = new JRDesignReportTemplate();
                reportTemplate.setSourceExpression(Misc.createExpression("java.lang.String", "\""+ Misc.string_replace("\\\\","\\",reference.getLocation()) +"\""));
                jd.addTemplate(reportTemplate);
                IReportManager.getInstance().notifyReportChange();
                toActivate = reportTemplate;
        }

        final Object obj = toActivate;

        if (obj != null)
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    IReportManager.getInstance().setSelectedObject(obj);
                    IReportManager.getInstance().getActiveVisualView().requestActive();
                    IReportManager.getInstance().getActiveVisualView().requestAttention(true);
                }
            });
        }
    }

}
