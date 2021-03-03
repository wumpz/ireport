/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.components.barcode.barbecue;

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.barbecue.StandardBarbecueComponent;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class BarbecueElementNode extends ElementNode {

    public BarbecueElementNode(JasperDesign jd, JRDesignElement element, Lookup doLkp)
    {
        super(jd, element,doLkp);
        setIconBaseWithExtension("com/jaspersoft/ireport/components/barcode/barcode-16.png");
        
    }

    @Override
    public String getDisplayName() {
        return I18n.getString("BarbecueElementNode.name");
    }




    /*
    @Override
    public Action[] getActions(boolean popup) {

        List<Action> actions = new ArrayList<Action>();
        Action[] originalActions = super.getActions(popup);

        actions.add(SystemAction.get(EditDatasetRunAction.class));
        for (int i=0; i<originalActions.length; ++i)
        {
            actions.add(originalActions[i]);
        }
        return actions.toArray(new Action[actions.size()]);
    }
    */

    @Override
    protected Sheet createSheet() {
        
        Sheet sheet = super.createSheet();
        
        // adding common properties...
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Barcode");
        StandardBarbecueComponent component = (StandardBarbecueComponent)( (JRDesignComponentElement)getElement()).getComponent();
        set.setDisplayName(I18n.getString("Barcode"));
        set.put(new BarbecueTypeProperty(component) );
        JRDesignDataset dataset = ModelUtils.getElementDataset(getElement(), getJasperDesign());

        set.put(new BarbecueCodeExpressionProperty(component, dataset));
        set.put(new BarbecueEvaluationTimeProperty(component, dataset));//, dataset));
        set.put(new BarbecueEvaluationGroupProperty(component, dataset));
        set.put(new BarbecueBarWidthProperty(component) );
        set.put(new BarbecueBarHeightProperty(component) );
        set.put(new BarbecueDrawTextProperty(component) );
        set.put(new BarbecueChecksumRequiredProperty(component) );
        set.put(new BarbecueApplicationIdentifierExpressionProperty(component, ModelUtils.getElementDataset(getElement(), getJasperDesign())));
        
        sheet.put( set);
        return sheet;
    }
 

}
