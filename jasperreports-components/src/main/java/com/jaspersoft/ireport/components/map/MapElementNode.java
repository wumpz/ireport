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
package com.jaspersoft.ireport.components.map;


import com.jaspersoft.ireport.components.map.properties.LatitudeExpressionProperty;
import com.jaspersoft.ireport.components.map.properties.LongitudeExpressionProperty;
import com.jaspersoft.ireport.components.map.properties.ZoomExpressionProperty;
import com.jaspersoft.ireport.components.map.properties.EvaluationGroupProperty;
import com.jaspersoft.ireport.components.map.properties.EvaluationTimeProperty;
import com.jaspersoft.ireport.components.map.properties.LanguageExpressionProperty;
import com.jaspersoft.ireport.components.map.properties.MapImageTypeProperty;
import com.jaspersoft.ireport.components.map.properties.MapScaleProperty;
import com.jaspersoft.ireport.components.map.properties.MapTypeProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.locale.I18n;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.components.map.StandardMapComponent;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author gtoffoli
 */
public class MapElementNode extends ElementNode {

    public MapElementNode(JasperDesign jd, JRDesignElement element, Lookup doLkp)
    {
        super(jd, element,doLkp);
        setIconBaseWithExtension("com/jaspersoft/ireport/components/map/map-16.png");
        
    }

    @Override
    public String getDisplayName() {
        return I18n.getString("MapElementNode.name");
    }




    
    @Override
    public Action[] getActions(boolean popup) {

        List<Action> actions = new ArrayList<Action>();
        Action[] originalActions = super.getActions(popup);

        actions.add( SystemAction.get( EditMarkerDatasetAction.class));
        
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
        propertySet.setName("map");
        StandardMapComponent component = (StandardMapComponent)( (JRDesignComponentElement)getElement()).getComponent();
        propertySet.setDisplayName(I18n.getString("map"));

        JRDesignDataset dataset = ModelUtils.getElementDataset(getElement(), getJasperDesign());

        //propertySet.put(new ImageEvaluationTimeProperty( element,dataset));
        //propertySet.put(new EvaluationGroupProperty( element,dataset));

        propertySet.put(new EvaluationTimeProperty(component,dataset));
        propertySet.put(new EvaluationGroupProperty(component,dataset));

        propertySet.put(new LatitudeExpressionProperty(component,dataset));
        propertySet.put(new LongitudeExpressionProperty(component,dataset));
        
        propertySet.put(new ZoomExpressionProperty(component,dataset));
        
        propertySet.put(new MapTypeProperty(component));
        propertySet.put(new MapScaleProperty(component));
        propertySet.put(new MapImageTypeProperty(component));
        
        propertySet.put(new LanguageExpressionProperty(component,dataset));


        sheet.put( propertySet );

        return sheet;
    }
 

}
