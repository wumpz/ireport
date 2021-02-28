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

import com.jaspersoft.ireport.designer.dnd.ReportObjectPaletteTransferable;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class NamedStyleNode extends AbstractNode
{

    private JRBaseStyle style = null;

    public NamedStyleNode(JRBaseStyle style, Lookup doLkp)
    {
        super (Children.LEAF, new ProxyLookup(doLkp, Lookups.singleton(style)));
        this.style = style;
        init();
    }

    @Override
    public String getDisplayName() {
        return getStyle().getName();
    }

    private void init()
    {
        setDisplayName ( style.getName());
        super.setName( style.getName() );
        setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/style-16.png");
     }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public Transferable clipboardCut() throws IOException {
        return NodeTransfer.transferable(this, NodeTransfer.CLIPBOARD_CUT);
    }

    @Override
    public Transferable clipboardCopy() throws IOException {
        return NodeTransfer.transferable(this, NodeTransfer.CLIPBOARD_COPY);
    }

    @Override
    public Transferable drag() throws IOException {
        ExTransferable tras = ExTransferable.create(clipboardCut());
         tras.put(new ReportObjectPaletteTransferable(
                    "com.jaspersoft.ireport.designer.styles.DragNamedStyleAction",
                    getStyle()));
        return tras;
    }


    public JRBaseStyle getStyle() {
        return style;
    }

    public void setStyle(JRBaseStyle style) {
        this.style = style;
    }
}
