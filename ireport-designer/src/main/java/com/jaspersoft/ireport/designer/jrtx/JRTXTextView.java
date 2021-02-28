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
package com.jaspersoft.ireport.designer.jrtx;

import com.jaspersoft.ireport.locale.I18n;
import java.awt.EventQueue;
import java.awt.Image;
import java.beans.BeanInfo;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;

/**
 *
 * @author gtoffoli
 */
public class JRTXTextView implements MultiViewDescription {

    private JRTXSourceEditor editor;
    private JRTXEditorSupport support;
    
    public JRTXTextView(JRTXEditorSupport ed) {
        this.support = ed;
    }
    
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }
    
    public String getDisplayName() {
        return I18n.getString("view.xml");
    }
    
    public Image getIcon() {
        Node nd = ((JRTXDataObject)support.getDataObject()).getNodeDelegate();
        return nd.getIcon( BeanInfo.ICON_COLOR_16x16);
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    public String preferredID() {
        return "XML"; // NOI18N
    }
    
    public MultiViewElement createElement() {
        return getEd();
    }
    
    public JRTXSourceEditor getEd() {
        assert EventQueue.isDispatchThread();
        if (editor == null) {
            editor = new JRTXSourceEditor(support);
        }
        return editor;
    }

    

}
