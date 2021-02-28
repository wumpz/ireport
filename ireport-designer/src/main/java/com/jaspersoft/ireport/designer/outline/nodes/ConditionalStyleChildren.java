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

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class ConditionalStyleChildren extends Index.KeysChildren {

    JasperDesign jd = null;
    private JRDesignConditionalStyle style = null;
    private Lookup doLkp = null;
    
    @SuppressWarnings("unchecked")
    public ConditionalStyleChildren(JasperDesign jd, JRDesignConditionalStyle style, Lookup doLkp) {
        super(new ArrayList());
        this.jd = jd;
        this.doLkp=doLkp;
        this.style=style;
    }

    

    /**
     * This method assumes that all the keys are JRDesignConditionalStyle.
     * 
     * @param key
     * @return
     */
    protected Node[] createNodes(Object key) {
        
        return null;
    }
    
    @Override
    protected void addNotify() {
        super.addNotify();
    }
    
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys() {
        List l = (List)lock();
        l.clear();
        update();
    }

  
    public JRDesignConditionalStyle getStyle() {
        return style;
    }

    public void setStyle(JRDesignConditionalStyle style) {
        this.style = style;
    }

}
