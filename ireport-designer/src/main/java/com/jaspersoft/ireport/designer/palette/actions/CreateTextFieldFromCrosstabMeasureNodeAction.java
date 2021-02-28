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
package com.jaspersoft.ireport.designer.palette.actions;

import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.outline.nodes.CrosstabMeasureNode;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
@SuppressWarnings("unchecked")
public class CreateTextFieldFromCrosstabMeasureNodeAction extends CreateTextFieldAction {

    @Override
    public void drop(DropTargetDropEvent dtde) {
    
        if ( !(getPaletteItem().getData() instanceof CrosstabMeasureNode))
        {
            return;
        }
        
        CrosstabMeasureNode measureNode = (CrosstabMeasureNode)getPaletteItem().getData();
        
        if (!(getScene() instanceof CrosstabObjectScene) ||
            measureNode.getCrosstab() != ((CrosstabObjectScene)getScene()).getDesignCrosstab() )
        {
            Runnable r = new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(Misc.getMainFrame(), "You can only use a measure inside the crosstab\nin which the measure is defined.","Error", JOptionPane.WARNING_MESSAGE);
                }
            };
            
            Mutex.EVENT.readAccess(r); 
            return;
        }
        
        super.drop(dtde);
    }

    @Override
    public JRDesignElement createReportElement(JasperDesign jd)
    {
        JRDesignTextField element = (JRDesignTextField)super.createReportElement( jd );
        
        CrosstabMeasureNode measureNode = (CrosstabMeasureNode)getPaletteItem().getData();
        
        ((JRDesignExpression)element.getExpression()).setText("$V{"+ measureNode.getMeasure().getName() + "}");
        setMatchingClassExpression(
                ((JRDesignExpression)element.getExpression()),
                measureNode.getMeasure().getValueClassName(), 
                true);
        
        return element;
    }

}
