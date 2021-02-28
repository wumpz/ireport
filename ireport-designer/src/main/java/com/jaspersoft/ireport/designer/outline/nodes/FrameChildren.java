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
package com.jaspersoft.ireport.designer.outline.nodes;

import java.beans.PropertyChangeListener;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class FrameChildren  extends ElementContainerChildren implements PropertyChangeListener {

    private JRDesignFrame frame = null;
    
    @SuppressWarnings("unchecked")
    public FrameChildren(JasperDesign jd, JRDesignFrame frame, Lookup doLkp) {
        super(jd, doLkp);
        //this.jd = jd;
        this.frame = frame;
        this.frame.getEventSupport().addPropertyChangeListener(this);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void recalculateKeys() {
        
        List l = (List)lock();
        l.clear();
        l.addAll(frame.getChildren());
        update();
    }
    

}
