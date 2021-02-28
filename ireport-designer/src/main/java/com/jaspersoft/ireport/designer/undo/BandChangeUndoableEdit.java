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
package com.jaspersoft.ireport.designer.undo;

import com.jaspersoft.ireport.designer.ModelUtils;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class BandChangeUndoableEdit  extends AggregatedUndoableEdit {

    private JRDesignBand oldBand = null;
    private JRDesignBand newBand = null;
    private JRDesignElement element = null;
    private JasperDesign jasperDesign = null;

    public BandChangeUndoableEdit(JasperDesign jasperDesign,
                                  JRDesignBand oldBand,
                                  JRDesignBand newBand,
                                  JRDesignElement element)
    {
        this.oldBand = oldBand;
        this.newBand = newBand;
        this.element = element;
        this.jasperDesign = jasperDesign;
    }

    @Override
    public void undo() throws CannotUndoException {

        super.undo();

        if (oldBand != null && newBand != oldBand)
        {
            int y1 = ModelUtils.getBandLocation(newBand, getJasperDesign());
            int y0 = ModelUtils.getBandLocation(oldBand, getJasperDesign());

            int deltaBand = y0 - y1;
            // Update element band...
            newBand.getChildren().remove(getElement());
            //oldBand.removeElement(dew.getElement());

            // Update the element coordinates...
            getElement().setElementGroup(oldBand);
            getElement().setY(getElement().getY() - deltaBand);
            oldBand.getChildren().add(getElement());
            newBand.getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
            oldBand.getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
        }

    }

    @Override
    public void redo() throws CannotRedoException {

        super.redo();

        if (newBand != null && newBand != oldBand)
        {
            int y1 = ModelUtils.getBandLocation(newBand, getJasperDesign());
            int y0 = ModelUtils.getBandLocation(oldBand, getJasperDesign());

            int deltaBand = y0 - y1;
            // Update element band...
            oldBand.getChildren().remove(getElement());
            //oldBand.removeElement(dew.getElement());

            // Update the element coordinates...
            getElement().setElementGroup(newBand);
            getElement().setY(getElement().getY() + deltaBand);
            newBand.getChildren().add(getElement());
            newBand.getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
            oldBand.getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
        }

    }

    @Override
    public String getPresentationName() {
        return "Element band change";
    }



    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }

    /**
     * @return the oldBand
     */
    public JRDesignBand getOldBand() {
        return oldBand;
    }

    /**
     * @param oldBand the oldBand to set
     */
    public void setOldBand(JRDesignBand oldBand) {
        this.oldBand = oldBand;
    }

    /**
     * @return the newBand
     */
    public JRDesignBand getNewBand() {
        return newBand;
    }

    /**
     * @param newBand the newBand to set
     */
    public void setNewBand(JRDesignBand newBand) {
        this.newBand = newBand;
    }

    /**
     * @return the element
     */
    public JRDesignElement getElement() {
        return element;
    }

    /**
     * @param element the element to set
     */
    public void setElement(JRDesignElement element) {
        this.element = element;
    }
}
