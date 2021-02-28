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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class AddDatasetUndoableEdit extends AggregatedUndoableEdit {

    private JRDesignDataset dataset = null;
    private JasperDesign jasperDesign = null;
    
    private int index = -1;
    
    public AddDatasetUndoableEdit(JRDesignDataset dataset, JasperDesign jasperDesign)
    {
        this.dataset = dataset;
        this.jasperDesign = jasperDesign;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        
        super.undo();
        index = jasperDesign.getDatasetsList().indexOf(getDataset());
        jasperDesign.removeDataset(getDataset());
        
    }

    @Override
    public void redo() throws CannotRedoException {
        
        super.redo();
        
        // add the dataset again at the correct index...
        if (index > -1)
        {
            jasperDesign.getDatasetsList().add(index,getDataset());
        }
        else
        {
            jasperDesign.getDatasetsList().add(getDataset());
        }
        jasperDesign.getEventSupport().firePropertyChange( JasperDesign.PROPERTY_DATASETS, null, null);
    }
    
    @Override
    public String getPresentationName() {
        return "Add Dataset " + getDataset().getName();
    }

    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }

    public JRDesignDataset getDataset() {
        return dataset;
    }

    public void setDataset(JRDesignDataset dataset) {
        this.dataset = dataset;
    }
}
