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
package com.jaspersoft.ireport.components.list;

import com.jaspersoft.ireport.designer.outline.nodes.ElementContainerChildren;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import net.sf.jasperreports.components.list.DesignListContents;
import net.sf.jasperreports.components.list.ListComponent;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class ListChildren extends ElementContainerChildren implements PropertyChangeListener {

    private JRDesignComponentElement component = null;
    
    @SuppressWarnings("unchecked")
    public ListChildren(JasperDesign jd, JRDesignComponentElement component, Lookup doLkp) {
        super(jd, doLkp);
        //this.jd = jd;
        this.component = component;
        if (component.getComponent() instanceof ListComponent)
        {
            ListComponent c = (ListComponent) component.getComponent();
            DesignListContents contents = (DesignListContents) c.getContents();
            contents.getEventSupport().addPropertyChangeListener(this);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void recalculateKeys() {
        
        List l = (List)lock();
        l.clear();
        if (component.getComponent() instanceof ListComponent)
        {
            ListComponent c = (ListComponent) component.getComponent();
            DesignListContents contents = (DesignListContents) c.getContents();
            l.addAll(contents.getChildren());
        }
        update();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        super.propertyChange(evt);

    }



}
