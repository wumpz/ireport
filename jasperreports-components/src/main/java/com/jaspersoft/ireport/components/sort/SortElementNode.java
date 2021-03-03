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
package com.jaspersoft.ireport.components.sort;

import com.jaspersoft.ireport.components.sort.properties.EvaluationGroupProperty;
import com.jaspersoft.ireport.components.sort.properties.EvaluationTimeProperty;
import com.jaspersoft.ireport.components.sort.properties.ColumnProperty;
import com.jaspersoft.ireport.components.sort.properties.HandlerColorProperty;
import com.jaspersoft.ireport.components.sort.properties.HandlerHorizontalAlignmentProperty;
import com.jaspersoft.ireport.components.sort.properties.HandlerVerticalAlignmentProperty;

import com.jaspersoft.ireport.components.sort.properties.SymbolFontProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.locale.I18n;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.components.sort.SortComponent;
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
public class SortElementNode extends ElementNode {

    public SortElementNode(JasperDesign jd, JRDesignElement element, Lookup doLkp)
    {
        super(jd, element,doLkp);
        setIconBaseWithExtension("com/jaspersoft/ireport/components/sort/sort-16.png");
        
    }

    @Override
    public String getDisplayName() {
        return I18n.getString("SortElementNode.name");
    }




    
    @Override
    public Action[] getActions(boolean popup) {

        List<Action> actions = new ArrayList<Action>();
        Action[] originalActions = super.getActions(popup);

        for (int i=0; i<originalActions.length; ++i)
        {
            actions.add(originalActions[i]);
        }
        return actions.toArray(new Action[actions.size()]);
    }
    

    @Override
    protected Sheet createSheet() {
        
        Sheet sheet = super.createSheet();
        
        // adding common properties...
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("sort");
        SortComponent component = (SortComponent)( (JRDesignComponentElement)getElement()).getComponent();
        propertySet.setDisplayName(I18n.getString("sort"));

        JRDesignDataset dataset = ModelUtils.getElementDataset(getElement(), getJasperDesign());

        //propertySet.put(new ImageEvaluationTimeProperty( element,dataset));
        //propertySet.put(new EvaluationGroupProperty( element,dataset));


        propertySet.put(new ColumnProperty(component,dataset));
        propertySet.put(new EvaluationTimeProperty(component,dataset));
        propertySet.put(new EvaluationGroupProperty(component,dataset));

        sheet.put( propertySet );

        Sheet.Set propertySetHandler = Sheet.createPropertiesSet();
        propertySetHandler.setName("handler");
        propertySetHandler.setDisplayName(I18n.getString("components.sort.handler"));

        propertySetHandler.put(new HandlerColorProperty(component));
        propertySetHandler.put(new HandlerHorizontalAlignmentProperty(component));
        propertySetHandler.put(new HandlerVerticalAlignmentProperty(component));
        propertySetHandler.put(new SymbolFontProperty(component, getJasperDesign()));
        
        sheet.put( propertySetHandler );

        

        return sheet;
    }
 

}
