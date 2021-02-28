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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @version $Id: CreateDetailTextFieldsForFields.java 0 2010-09-07 09:05:54 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class CreateDetailTextFieldsForFieldsAction extends CreateTextFieldAction {

    public JRDesignElement[] createElements(JasperDesign jd, boolean labels)
    {

        List<JRDesignField> fields = (List<JRDesignField>)getPaletteItem().getData();
        List<JRDesignElement> elements = new ArrayList<JRDesignElement>();

        int w = jd.getPageWidth() - jd.getLeftMargin() - jd.getRightMargin();
        w /= fields.size();

        for (JRDesignField field : fields)
        {
            JRDesignElement element = null;
            if (!labels)
            {
                element = new JRDesignTextField( jd );
                JRDesignExpression exp = new JRDesignExpression();

                exp.setText("$F{"+ field.getName() + "}");
                exp.setValueClassName(field.getValueClassName());

                ((JRDesignTextField)element).setExpression(exp);

                setMatchingClassExpression(
                    ((JRDesignExpression)((JRDesignTextField)element).getExpression()),
                    field.getValueClassName(),
                true
                );

                ((JRDesignTextField)element).setExpression(exp);
            }
            else
            {
                element = new JRDesignStaticText( jd );
                if (field.getDescription() != null &&
                    field.getDescription().trim().length() > 0)
                {
                    ((JRDesignStaticText)element).setText(field.getDescription());
                }
                else
                {
                    ((JRDesignStaticText)element).setText(field.getName());
                }
            }
            element.setWidth(w);
            element.setHeight(20);
            elements.add(element);
        }

        return elements.toArray(new JRDesignElement[elements.size()]);
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {

        JRDesignElement[] elements = createElements(getJasperDesign(), false);

        if (elements == null || elements.length == 0) return;
        // Find location...
        dropElementsAt(getScene(), getJasperDesign(), elements, dtde.getLocation());

        if (getJasperDesign().getColumnHeader() != null &&
                        getJasperDesign().getColumnHeader().getHeight() >= 20 &&
                        IReportManager.getPreferences().getBoolean("createLabelForField", true))
        {
            int y = ModelUtils.getBandLocation(getJasperDesign().getColumnHeader(), getJasperDesign());

            Point labelLocation = getScene().convertSceneToView(new Point(getJasperDesign().getLeftMargin()+1,y+1));
            dropElementsAt(getScene(), getJasperDesign(), createElements(getJasperDesign(), true), labelLocation);
        }
    }

    @Override
    public void adjustElement(JRDesignElement[] elements, int index, Scene theScene, JasperDesign jasperDesign, Object parent, Point dropLocation) {
        if (index == 0 && elements.length == 1) return;
        
        int newx = (index == 0) ? 0 : elements[index-1].getX() + elements[index-1].getWidth();
        elements[index].setX(newx);
        elements[index].setY(0);
        
        super.adjustElement(elements, index, theScene, jasperDesign, parent, dropLocation);
    }




}
