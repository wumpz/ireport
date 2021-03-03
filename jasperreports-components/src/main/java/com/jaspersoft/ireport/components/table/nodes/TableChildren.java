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
package com.jaspersoft.ireport.components.table.nodes;

import com.jaspersoft.ireport.components.table.TableCell;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class TableChildren extends Index.KeysChildren {

    private JRDesignComponentElement component = null;
    private JasperDesign jd = null;
    private Lookup doLkp = null;

    
    @SuppressWarnings("unchecked")
    public TableChildren(JasperDesign jd, JRDesignComponentElement component, Lookup doLkp) {
        super(new ArrayList());
        this.jd = jd;
        this.component = component;
        this.doLkp = doLkp;
    }


    @Override
    protected void addNotify() {
        super.addNotify();
        recalculateKeys();
    }

    @SuppressWarnings("unchecked")
    public void recalculateKeys() {
        
        List l = (List)lock();
        l.clear();
        if (component.getComponent() instanceof StandardTable)
        {
            StandardTable table = (StandardTable)component.getComponent();
            l.add(new TableSectionNode(jd, component, TableCell.TABLE_HEADER, null, doLkp));
            l.add(new TableSectionNode(jd, component, TableCell.COLUMN_HEADER, null, doLkp));

            JRDesignDataset dataset = jd.getMainDesignDataset();
            if (table.getDatasetRun() != null && table.getDatasetRun().getDatasetName() != null)
            {
                dataset = (JRDesignDataset) jd.getDatasetMap().get(table.getDatasetRun().getDatasetName());
            }
            JRGroup[] groups = dataset.getGroups();
            for (JRGroup group : groups)
            {
                l.add(new TableSectionNode(jd, component, TableCell.GROUP_HEADER, (JRDesignGroup) group, doLkp));
            }
            l.add(new TableSectionNode(jd, component, TableCell.DETAIL, null, doLkp));
            for (JRGroup group : groups)
            {
                l.add(new TableSectionNode(jd, component, TableCell.GROUP_FOOTER, (JRDesignGroup)group, doLkp));
            }
            l.add(new TableSectionNode(jd, component, TableCell.COLUMN_FOOTER, null, doLkp));
            l.add(new TableSectionNode(jd, component, TableCell.TABLE_FOOTER, null, doLkp));


        }
        update();
    }

    protected Node[] createNodes(Object key)
    {
        Node node = null;
        if (key instanceof Node)
        {
            node = (Node)key;
        }

        if (node != null)
            return new Node[]{node};


        return new Node[]{};
    }


}
