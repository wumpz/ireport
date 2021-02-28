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
package com.jaspersoft.ireport.designer.formatting.actions;

import com.jaspersoft.ireport.locale.I18n;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;

/**
 *
 * @author gtoffoli
 */
public class EqualsHSpaceAction extends AbstractFormattingToolAction {

    
    public EqualsHSpaceAction()
    {
        super();
        putValue(Action.NAME, getName());
    }
    
    @Override
    protected String iconResource() {
        return "com/jaspersoft/ireport/designer/resources/formatting/elem_add_hspace.png";
    }

    
    protected void performAction(Node[] nodes) {
        
        if (nodes.length == 0) return;
        JasperDesign jd = nodes[0].getLookup().lookup(JasperDesign.class);
        if (jd == null) return;
        
        List<JRDesignElement> elements = getSelectedElements(nodes);
    
        boolean aggregate = false;
        
        List<JRDesignElement> sortedElements = sortXY( elements );
        
        
        int gap = 0;
        int usedSpace = 0;
        int minX = sortedElements.get(0).getX();
        int maxX = minX + sortedElements.get(0).getWidth();
        for (JRDesignElement element : sortedElements)
        {
            if (minX > element.getX()) minX = element.getX();
            if (maxX < element.getX()+element.getWidth()) maxX = element.getX()+element.getWidth();
            usedSpace += element.getWidth();
        }
        
        gap = (maxX - minX - usedSpace)/(elements.size()-1);
        
        int actualX = minX;
        
        for (int i=0; i<sortedElements.size(); ++i)
        {
            JRDesignElement element = sortedElements.get(i);
            if (i == 0) {
                actualX = element.getX() + element.getWidth() + gap;
                continue;
            }
            
            Rectangle oldBounds = getElementBounds(element);
            if (i == sortedElements.size() - 1)
            {
                // Trick to avoid calculations errors.
                element.setX( maxX - element.getWidth() );
            }
            else
            {
                element.setX( actualX );
            }
            actualX = element.getX() + element.getWidth() + gap;
            aggregate = addTransformationUndo( element, oldBounds, aggregate);
        }
    }

    @Override
    public String getName() {
        return I18n.getString("formatting.tools.equalHSpace");
    }

    @Override
    public boolean requiresMultipleObjects() {
        return true;
    }


}
