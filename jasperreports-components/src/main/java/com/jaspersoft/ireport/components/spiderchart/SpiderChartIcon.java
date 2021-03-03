/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.spiderchart;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author gtoffoli
 */
public class SpiderChartIcon {

    private ImageIcon icon = null;
    private ImageIcon cachedImage = null;

    public SpiderChartIcon()
    {

    }

    public ImageIcon getIcon()
    {
        if (icon == null)
        {
            try {

                this.icon = new ImageIcon(this.getClass().getResource("/com/jaspersoft/ireport/components/spiderchart/spiderchart.png"));
            } catch (Exception ex)
            {
            }
        }

        return this.icon;
    }

    /**
     * Get the icon just checking the width. Used when keepeng ratio...
     * @param width
     * @return
     */
    public ImageIcon getIcon(int width)
    {
        //ImageIcon icon = getIcon();
        if (cachedImage != null && cachedImage.getIconWidth() == width)
        {
            return cachedImage;
        }

        // Create an image of size width...
        cachedImage = getFasterScaledInstance(getIcon(), width, -1);
        return cachedImage;
    }



    public ImageIcon getIcon(int width, int height)
    {
        //ImageIcon icon = getIcon();
        if (cachedImage != null && cachedImage.getIconWidth() == width && cachedImage.getIconHeight() == height)
        {
            return cachedImage;
        }

        // Create an image of size width...
        cachedImage = getFasterScaledInstance(getIcon(), width, height);
        return cachedImage;
    }


    /**
     * Convenience method that returns a scaled instance of the
     * provided Image.
     *
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels (or -1 if targetHeight is specified)
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels (or -1 if targetWidth is specified)
     *
     * @return a scaled version of the original BufferedImage
     */
    public static ImageIcon getFasterScaledInstance(ImageIcon img, int targetWidth, int targetHeight)
    {
        if (img == null) return null;

        if (targetWidth == -1 && targetHeight > 0)
        {
            targetWidth = (int)( img.getIconWidth()*targetHeight*1.0 / img.getIconHeight());
        }
        else if (targetHeight == -1 && targetWidth > 0)
        {
            targetHeight = (int)( img.getIconHeight()*targetWidth*1.0 / img.getIconWidth());
        }
        else if (targetWidth <= 0 || targetHeight <= 0)
        {
            return img;
        }

        BufferedImage buf = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        buf.createGraphics().drawImage(img.getImage(), 0,0,null);

        Image newImg = getFasterScaledInstance(buf, targetWidth, targetHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);


        return new ImageIcon(newImg);
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

        if (targetWidth > img.getWidth() ||
            targetHeight > img.getHeight())
        {
            progressiveBilinear = false;
        }


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

        //targetWidth = Math.min(targetWidth, w);
        //targetHeight = Math.min(targetHeight, h);



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
}
