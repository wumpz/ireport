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
package com.jaspersoft.ireport.designer.styles;

import com.jaspersoft.ireport.designer.jrtx.*;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class StylesLibraryChildren extends StylesChildren {

    public StylesLibraryChildren(JRSimpleTemplate template, Lookup doLkp) {
        super(template,doLkp);
    }

    /*
    @Override
    protected List<Node> initCollection() {
        return recalculateKeys();
    }
    */
    
    
    @Override
    protected Node[] createNodes(Object key) {
        
        Node node = null;
        if (key instanceof JRDesignStyle)
        {
            node = new LibraryStyleNode(getTemplate(), (JRDesignStyle)key, getDoLkp());
        }
        else if (key instanceof JRTemplateReference)
        {
            node = new LibraryTemplateReferenceNode(getTemplate(), (JRTemplateReference)key, getDoLkp());
        }

        return new Node[]{node};
    }
}
