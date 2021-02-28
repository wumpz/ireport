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
package com.jaspersoft.ireport.jasper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;

public class JasperDataNode extends DataNode {

    private static final String IMAGE_ICON_BASE = "com/jaspersoft/ireport/designer/resources/jasperreports_jasper.png";

    public JasperDataNode(JasperDataObject obj) {
        super(obj, Children.LEAF);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }
    JasperDataNode(JasperDataObject obj, Lookup lookup) {
        super(obj, Children.LEAF, lookup);
        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

//    /** Creates a property sheet. */
//    @Override
//    protected Sheet createSheet() {
//        Sheet s = super.createSheet();
//        Sheet.Set ss = s.get(Sheet.PROPERTIES);
//        if (ss == null) {
//            ss = Sheet.createPropertiesSet();
//            s.put(ss);
//        }
//        // TODO add some relevant properties: ss.put(...)
//        return s;
//    }

     @Override
    public Action[] getActions(boolean b) {
        Action[] actions = super.getActions(b);

        List<Action> actions_list = new ArrayList<Action>();

        actions_list.add(SystemAction.get(OpenJasperAction.class));
        actions_list.add(null);
        actions_list.addAll( Arrays.asList(actions) );

        return actions_list.toArray(new Action[actions_list.size()]);
    }


}
