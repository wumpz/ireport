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

import com.jaspersoft.ireport.designer.borders.ReportBorder;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author gtoffoli
 */
public class TemplateRenderer extends JComponent implements ListCellRenderer {
    private ImageIcon originalImage = null;
    private boolean selected = false;
    private String text = "";
    private Color selectionColor  = new Color(71,138,229);
    private int horizontalGap  = 20;
    private int verticalGap  = 10;
    private int reflectionGap  = 0;
    private int iconSize = 120;
    private int iconWidth = 80;
    private int iconHeight = 80;
    private ImageIcon defaultIcon = null;

    private ReportBorder reportBorder = new ReportBorder(null);


    public TemplateRenderer()
    {
        setFont(new Font("SansSerif", Font.BOLD, 12));
        defaultIcon = new ImageIcon(this.getClass().getResource("/com/jaspersoft/ireport/designer/templates/no_preview.png"));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension( getIconSize() + 2*getHorizontalGap(), (int)(getIconSize()*1.5) + 10);
    }
    //ow/80 = oh/x    x = oh*80/ow



    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        setForeground( list.getForeground() == null ? Color.DARK_GRAY : list.getForeground());
        setBackground( list.getBackground() == null ? Color.WHITE : list.getBackground());
        // Check the minimum size...

        setOriginalImage(null);
        //if (getOriginalImage() == null)
        if (value instanceof TemplateDescriptor)
        {
            TemplateDescriptor td = (TemplateDescriptor)value;
            ImageIcon img = td.getPreviewIcon();
            setOriginalImage((img != null) ? img : defaultIcon);
        }
        else
        {
            setOriginalImage(defaultIcon);
        }

        if (originalImage.getIconHeight() > originalImage.getIconWidth())
        {
            setIconHeight( Math.min( getIconSize(), originalImage.getIconHeight()));
            setIconWidth( (int) ((1.0 * originalImage.getIconWidth() * getIconHeight())/originalImage.getIconHeight()) );
        }
        else
        {
            setIconWidth( Math.min( getIconSize(), originalImage.getIconWidth()));
            setIconHeight( (int) ((1.0 * originalImage.getIconHeight() * getIconWidth())/originalImage.getIconWidth()) );
            
        }
        
        setPreferredSize( new Dimension( getIconSize() + 2*getHorizontalGap(), (int)(getIconSize()*1.5) + 10));

        if (value instanceof TemplateDescriptor)
        {
            setText(((TemplateDescriptor)value).getDisplayName());
        }

        setSelected(isSelected);

        return this;
    }

    @Override
    public void paint(Graphics g) {
        //g.setColor(new Color(37,37,37));
        //Dimension pageSize = new Dimension(80, 120);

        Graphics2D g2d = (Graphics2D)g;


        g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();

        
        //g.drawRect(0,0, getWidth()-1, getHeight()-1);
        //g.drawImage(image, 5,5, pageSize.width, pageSize.height, this);

        int width = getWidth();
        int height = getHeight();
        int imageWidth = getIconWidth();
        int imageHeight = getIconHeight();
        float opacity = 0.3f;
        float fadeHeight = 0.8f;

        AffineTransform tras =  g2d.getTransform();

        if (getOriginalImage().getIconWidth() > 0 &&
            getOriginalImage().getIconHeight() > 0)
        {
            g2d.translate( (width-imageWidth)/2, getVerticalGap() + getIconSize() - getIconHeight() );

            if (isSelected())
            {
                //g.setColor(Color.YELLOW.darker());
                g.setColor(getSelectionColor());
                //int sHeight = getHeight()/3*2 + fm.getHeight() + 10;
                g2d.fillRoundRect( - 3,-3,imageWidth + 6, imageHeight+6,5,5);
                //g.setColor(Color.GRAY);
                //Stroke oldStroke = g2d.getStroke();
                //g2d.setStroke(new BasicStroke(2f));
                //g2d.drawRoundRect( 2,2,getWidth()-3,sHeight-3,5,5);
                //g2d.setStroke(oldStroke);
            }

            reportBorder.paintShadowBorder( g2d,-6,-6,imageWidth+12, imageHeight+12);

            BufferedImage resizedImage = new BufferedImage(getOriginalImage().getIconWidth(), getOriginalImage().getIconHeight(), BufferedImage.TYPE_INT_ARGB );
            Graphics2D rgd = resizedImage.createGraphics();
            rgd.drawImage( getOriginalImage().getImage(), 0,0, null);
            resizedImage = getFasterScaledInstance(resizedImage, imageWidth, imageHeight,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

            g2d.drawImage( resizedImage, 0,0, this);
            g2d.translate( 0, 2*getIconHeight()+3 );
            g2d.scale( 1, -1 );

            BufferedImage reflection = new BufferedImage( imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB );
            Graphics2D rg = reflection.createGraphics();
            rg.setColor(Color.WHITE);
            //rg.fillRect(0,0, imageWidth, imageHeight);
            rg.drawImage( resizedImage, 0,0, null);
            //rg.setComposite( AlphaComposite.getInstance( AlphaComposite.DST_IN ) );
            int fadeGap = imageHeight - getIconSize();
            rg.setPaint(
                new GradientPaint(
                    0, getIconSize()*fadeHeight + fadeGap, getBackground(),
                    0, imageHeight, new Color( getBackground().getRed(),
                                               getBackground().getGreen(),
                                               getBackground().getBlue(),
                                               (int)(255*opacity) )
                )
            );
            rg.fillRect( 0, 0, imageWidth, getIconSize());
            rg.dispose();
            g2d.drawRenderedImage( reflection, null );

        }
        g2d.setColor(getForeground());
        //g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        String text = getText();
        Rectangle2D rect = fm.getStringBounds(text, g);
        g2d.setTransform(tras);
        
        g2d.drawString(text, (int)(getWidth()/2 - rect.getWidth()/2),
                    getHeight()/3*2 + fm.getHeight() + 5
                );

        

    }



    /**
     * Convenience method that returns a scaled instance of the
     * provided BufferedImage.
     *
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    RenderingHints.KEY_INTERPOLATION (e.g.
     *    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR,
     *    RenderingHints.VALUE_INTERPOLATION_BILINEAR,
     *    RenderingHints.VALUE_INTERPOLATION_BICUBIC)
     * @param progressiveBilinear if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in down-scaling cases, where
     *    targetWidth or targetHeight is
     *    smaller than the original dimensions)
     * @return a scaled version of the original BufferedImage
     */
    public static BufferedImage getFasterScaledInstance(BufferedImage img,
            int targetWidth, int targetHeight, Object hint,
            boolean progressiveBilinear)
    {
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
            BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;
        int w, h;
        int prevW = ret.getWidth();
        int prevH = ret.getHeight();
        boolean isTranslucent = img.getTransparency() !=  Transparency.OPAQUE;

        if (progressiveBilinear) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (progressiveBilinear && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (progressiveBilinear && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            if (scratchImage == null || isTranslucent) {
                // Use a single scratch buffer for all iterations
                // and then copy to the final, correctly-sized image
                // before returning
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);
            prevW = w;
            prevH = h;

            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);

        if (g2 != null) {
            g2.dispose();
        }

        // If we used a scratch buffer that is larger than our target size,
        // create an image of the right size and copy the results into it
        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }

        return ret;
    }
    

    /**
     * @return the originalImage
     */
    public ImageIcon getOriginalImage() {
        return originalImage;
    }

    /**
     * @param originalImage the originalImage to set
     */
    public void setOriginalImage(ImageIcon originalImage) {
        this.originalImage = originalImage;
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the selectionColor
     */
    public Color getSelectionColor() {
        return selectionColor;
    }

    /**
     * @param selectionColor the selectionColor to set
     */
    public void setSelectionColor(Color selectionColor) {
        this.selectionColor = selectionColor;
    }

    /**
     * @return the horizontalGap
     */
    public int getHorizontalGap() {
        return horizontalGap;
    }

    /**
     * @param horizontalGap the horizontalGap to set
     */
    public void setHorizontalGap(int horizontalGap) {
        this.horizontalGap = horizontalGap;
    }

    /**
     * @return the varticalGap
     */
    public int getVerticalGap() {
        return verticalGap;
    }

    /**
     * @param varticalGap the varticalGap to set
     */
    public void setVerticalGap(int varticalGap) {
        this.verticalGap = verticalGap;
    }

    /**
     * @return the reflectionGap
     */
    public int getReflectionGap() {
        return reflectionGap;
    }

    /**
     * @param reflectionGap the reflectionGap to set
     */
    public void setReflectionGap(int reflectionGap) {
        this.reflectionGap = reflectionGap;
    }

    /**
     * @return the iconWidth
     */
    public int getIconWidth() {
        return iconWidth;
    }

    /**
     * @param iconWidth the iconWidth to set
     */
    public void setIconWidth(int iconWidth) {
        this.iconWidth = iconWidth;
    }

    /**
     * @return the iconHeight
     */
    public int getIconHeight() {
        return iconHeight;
    }

    /**
     * @param iconHeight the iconHeight to set
     */
    public void setIconHeight(int iconHeight) {
        this.iconHeight = iconHeight;
    }

    /**
     * @return the iconSize
     */
    public int getIconSize() {
        return iconSize;
    }

    /**
     * @param iconSize the iconSize to set
     */
    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }



}
