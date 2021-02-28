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
package com.jaspersoft.ireport.designer.welcome;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.Icon;
import org.openide.awt.StatusDisplayer;

/**
 *
 * @author gtoffoli
 */
public class ActionButton extends LinkButton {

    private Action action;
    private String urlString;
    private boolean visited = false;

    public ActionButton( Action a, boolean showBullet, String urlString ) {
        super( a.getValue( Action.NAME ).toString(), showBullet );
        this.action = a;
        this.urlString = urlString;
        Object icon = a.getValue( Action.SMALL_ICON );
        if( null != icon && icon instanceof Icon )
            setIcon( (Icon)icon );
        Object tooltip = a.getValue( Action.SHORT_DESCRIPTION );
        if( null != tooltip )
            setToolTipText( tooltip.toString() );
    }

    public void actionPerformed(ActionEvent e) {
        if( null != action ) {
            action.actionPerformed( e );
        }
        if( null != urlString )
            visited = true;
    }

    protected void onMouseExited(MouseEvent e) {
        if( null != urlString ) {
            StatusDisplayer.getDefault().setStatusText( "" ); //NOI18N
        }
    }

    protected void onMouseEntered(MouseEvent e) {
        if( null != urlString ) {
            StatusDisplayer.getDefault().setStatusText( urlString );
        }
    }

    protected boolean isVisited() {
        return visited;
    }

    private static final long serialVersionUID = 1L;
}

