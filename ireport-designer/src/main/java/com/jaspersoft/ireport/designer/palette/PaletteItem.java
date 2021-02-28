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
package com.jaspersoft.ireport.designer.palette;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.outline.OutlineTopComponent;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Image;
import java.util.Properties;
import org.openide.util.ImageUtilities;

/**
 *
 * @author gtoffoli
 */
public class PaletteItem {

    private Properties props;
    
    private Image icon16;
    private Image icon32;
    
    private Object data = null;
    
    public static final String PROP_ID = "id";
    public static final String PROP_NAME = "name";
    public static final String PROP_COMMENT = "tooltip";
    public static final String PROP_ICON16 = "icon16";
    public static final String PROP_ICON32 = "icon32";
    public static final String ACTION = "action";
    
    /** Creates a new instance of PaletteItem */
    public PaletteItem( Properties props ) {
        this.props = props;
        loadIcons();
    }
    public String getId() {
        return props.getProperty( PROP_ID );
    }
    public String getDisplayName() {
        if ( props.getProperty( PROP_NAME ) != null)
        {
            try {
                return I18n.getString(props.getProperty( PROP_NAME ));
            } catch (Exception ex)
            {
                return props.getProperty( PROP_NAME );
            }
        }
        return null;
    }
    
    public String getComment() {
        if (props.getProperty( PROP_COMMENT ) != null)
        {
            
            try {
                return I18n.getString(props.getProperty( PROP_COMMENT ));
            } catch (Exception ex)
            {
                return props.getProperty( PROP_COMMENT );
            }
        }
        return null;
    }
    public Image getSmallImage() {
        return icon16;
    }
    public Image getBigImage() {
        return icon32;
    }
    @Override
    public boolean equals(Object obj) {
        if( obj instanceof PaletteItem ) {
            return getId().equals( ((PaletteItem)obj).getId()  );
        }
        return false;
    }
    private void loadIcons() {
        String iconId = props.getProperty( PROP_ICON16 );
        if (iconId == null) return;
        icon16 = ImageUtilities.loadImage( iconId );
        iconId = props.getProperty( PROP_ICON32 );
        icon32 = ImageUtilities.loadImage( iconId );
    }
    
    public void drop(java.awt.dnd.DropTargetDropEvent dtde)
    {
        if (props.getProperty( ACTION ) != null)
        {
            try {
                PaletteItemAction pia = (PaletteItemAction)Class.forName( props.getProperty( ACTION ), true, Thread.currentThread().getContextClassLoader()).newInstance();
                pia.setJasperDesign( IReportManager.getInstance().getActiveReport());
                pia.setPaletteItem(this);
                pia.setScene( OutlineTopComponent.getDefault().getCurrentJrxmlVisualView().getReportDesignerPanel().getActiveScene());
            
                pia.drop(dtde);
                
            } catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
    }
    
    /**
     * This field can be used by special palette item implementations... 
     **/
    public Object getData()
    {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
}
