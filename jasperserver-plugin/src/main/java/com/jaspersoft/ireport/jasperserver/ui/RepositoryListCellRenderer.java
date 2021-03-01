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
package com.jaspersoft.ireport.jasperserver.ui;

import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

/**
 *
 * @author gtoffoli
 */
public class RepositoryListCellRenderer extends DefaultListCellRenderer  {
    
    final static ImageIcon serverIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/server.png"));
    final static ImageIcon folderIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/folder.png"));
    final static ImageIcon reportUnitIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/reportunit.png"));
    final static ImageIcon datasourceJndiIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_jndi.png"));
    final static ImageIcon datasourceMongoDBIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_mongodb.png"));
    final static ImageIcon datasourceHiveIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_hive.png"));
    final static ImageIcon datasourceVirtualIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_virtual.png"));
    
    final static ImageIcon datasourceIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datasource.png"));
    final static ImageIcon datasourceJdbcIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_jdbc.png"));
    final static ImageIcon imageIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/picture.png"));
    final static ImageIcon jrxmlIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/jrxml_file.png"));
    final static ImageIcon refIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/link.png"));
    final static ImageIcon bundleIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/bundle.png"));
    final static ImageIcon fontIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/font.png"));
    final static ImageIcon jarIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/jar.png"));
    final static ImageIcon inputcontrolIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/inputcontrol.png"));
    final static ImageIcon datatypeIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datatype.png"));
    final static ImageIcon lovIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/lov.png"));
    final static ImageIcon datasourceBeanIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_bean.png"));
    final static ImageIcon unknowIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/unknow.png"));
    final static ImageIcon queryIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/query.png"));
    final static ImageIcon waitingIcon = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/waiting.png"));
    final static ImageIcon reportOptionsResourceIcon  = new ImageIcon(RepositoryListCellRenderer.class.getResource("/com/jaspersoft/ireport/jasperserver/res/reportunit_options.png"));
    
    
    boolean comboboxMode = false;
    /* This is the only method defined by ListCellRenderer.  We just
     * reconfigure the Jlabel each time we're called.
     */
    public RepositoryListCellRenderer(boolean mode)
    {
        super();
        this.comboboxMode = mode;
    }
    
    public RepositoryListCellRenderer()
    {
        super();
    }
    
    public Component getListCellRendererComponent(
        JList list,
	Object value,   // value to display
	int index,      // cell index
	boolean iss,    // is the cell selected
	boolean chf)    // the list and the cell have the focus
    {
        /* The DefaultListCellRenderer class will take care of
         * the JLabels text property, it's foreground and background
         * colors, and so on.
         */
        super.getListCellRendererComponent(list, value, index, iss, chf);

        /* We additionally set the JLabels icon property here.
         */
        if (value instanceof ResourceDescriptor)
        {
            ResourceDescriptor rd = (ResourceDescriptor)value;
            
            if (rd.getUriString().equals("/"))
            {
                setIcon(getResourceIcon( null ) );
                
                setText( JasperServerManager.getString("misc.labelRepositoryRoot","Repository root (/)") );
            }
            else
            {
                
                ImageIcon iconImage = getResourceIcon( rd );
                
                if (comboboxMode && index > 0)
                {
                    Image image = new java.awt.image.BufferedImage(iconImage.getIconWidth() + 8*index, iconImage.getIconHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB );
                    
                    Graphics g = image.getGraphics();
                    
                    g.setColor( (iss) ? list.getSelectionBackground() : list.getBackground() );
                    g.fillRect(0,0,image.getWidth(null), image.getHeight(null));
                    g.drawImage(iconImage.getImage(), 8*index,0,null);
                    
                    
                    setIcon(new ImageIcon(image));
                }
                else
                {
                  setIcon(iconImage);  
                }
                setText( rd.getName() );
            }
        }
	return this;
    }
    
    public static ImageIcon getResourceIcon(ResourceDescriptor resource)
    {
        if (resource == null) return serverIcon;
        else if (resource.getWsType() == null) return serverIcon;
        else if (resource.getIsReference()) return refIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_FOLDER)) return folderIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_REPORTUNIT)) return reportUnitIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_JNDI)) return datasourceJndiIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_JDBC)) return datasourceJdbcIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_VIRTUAL)) return datasourceVirtualIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_BEAN)) return datasourceBeanIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION)) return datasourceIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_IMAGE)) return imageIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_JRXML)) return jrxmlIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_FONT)) return fontIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_CLASS_JAR)) return jarIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_RESOURCE_BUNDLE)) return bundleIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_INPUT_CONTROL)) return inputcontrolIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_DATA_TYPE)) return datatypeIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_LOV)) return lovIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_QUERY)) return queryIcon;
        else if (resource.getWsType().equals("ReportOptionsResource")) return reportOptionsResourceIcon;
        else if (resource.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_CUSTOM) && RepositoryFolder.isDataSource(resource))
        {
            String serviceClass = resource.getResourcePropertyValue(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS);
            
            if (serviceClass != null)
            {
                if (serviceClass.equals("com.jaspersoft.hadoop.hive.jasperserver.HiveDataSourceService"))
                {
                    return datasourceHiveIcon;
                }
                else if (serviceClass.equals("com.jaspersoft.mongodb.jasperserver.MongoDbDataSourceService"))
                {
                    return datasourceMongoDBIcon;
                }
            }
                    
            
        }
        else if (RepositoryFolder.isDataSource(resource))
        {
            return datasourceIcon;
        }
        return unknowIcon;
    }
}
