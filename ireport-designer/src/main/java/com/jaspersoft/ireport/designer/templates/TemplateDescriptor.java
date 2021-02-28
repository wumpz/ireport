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
package com.jaspersoft.ireport.designer.templates;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JRFont;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author gtoffoli
 */
public class TemplateDescriptor {

    private TemplateCategory category = new TemplateCategory();
    private String   displayName;
    private ImageIcon  image;
    private long timestamp = 0;
    private File     file;
    private Dimension pageSize = null;
    
    /**
     * The image is reloaded if changed on disk.
     * The method looks for an image with the same name of the template
     * and with extension of type png,gif,jpg,jpeg
     * 
     * @return the image
     */
    public ImageIcon getPreviewIcon() {

        String filePath = getFile().getPath();
        
        String[] extensions = new String[]{".png",".gif",".jpg",".jpeg"};
        File f = null;
        for (int i=0; i<extensions.length; ++i)
        {
            String fname = Misc.changeFileExtension(filePath, ".png");
            f = new File(fname);
            if (f.exists())
            {
                break;
            }
            else
            {
                f = null;
            }
        }


        if (f != null && (image == null || timestamp < f.lastModified()))
        {
            this.image = new ImageIcon(f.getPath());
            timestamp = f.lastModified();
        }
        else if (image == null && getPageSize() != null)
        {
            BufferedImage img = new BufferedImage(getPageSize().width, getPageSize().height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gfx = img.createGraphics();
            GradientPaint gradient = new GradientPaint(0,getPageSize().height,  new Color(232,232,232,232), getPageSize().width, 0, Color.WHITE);
            gfx.setPaint(gradient);
            gfx.fillRect(0,0,getPageSize().width, getPageSize().height);
            gfx.setFont(new Font("SandSerif",Font.PLAIN, getPageSize().height/3));
            FontMetrics fm = gfx.getFontMetrics();
            gfx.setColor(Color.DARK_GRAY);
            gfx.drawRect(0,0,getPageSize().width-1, getPageSize().height-1);
            Rectangle2D rect = fm.getStringBounds("?", gfx);
            gfx.drawString("?", (int)((getPageSize().width - rect.getWidth())/2), (int)( (getPageSize().height/3) + (getPageSize().height - rect.getHeight())/2));
            this.image = new ImageIcon(img);
        }
        return image;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    /**
     * @return the category
     */
    public void setCategory(TemplateCategory cat) {
        this.category = cat;
    }

    /**
     * @return the category
     */
    public TemplateCategory getCategory() {
        return category;
    }

    public FileObject getFileObject()
    {
        if (getFile().exists())
        {
            return FileUtil.toFileObject(getFile());
        }
        return null;
    }

    /**
     * @return the pageSize
     */
    public Dimension getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(Dimension pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof TemplateDescriptor)
        {
            return ((TemplateDescriptor)obj).getFile().equals(getFile());
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.displayName != null ? this.displayName.hashCode() : 0);
        hash = 79 * hash + (this.file != null ? this.file.hashCode() : 0);
        return hash;
    }



}
