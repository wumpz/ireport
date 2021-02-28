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
package com.jaspersoft.ireport.designer.widgets;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.borders.ReportBorder;
import com.jaspersoft.ireport.designer.utils.Java2DUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;


/**
 *
 * @author gtoffoli
 */
public class PageWidget extends Widget {
    
    private int gridSize = 13;
    private TexturePaint gridTexture = null;
    private static final BasicStroke GRID_STROKE = new BasicStroke(0, BasicStroke.CAP_SQUARE,
            BasicStroke.JOIN_BEVEL, 1.0f, new float[]{2f,2f}, 0.0f);

    private static TexturePaint restrictedAreaTexture = null; //
    
    public PageWidget(ReportObjectScene scene) {
        super(scene);
        
        
        
        //this.setMaximumSize( new Dimension( scene.getJasperDesign().getPageWidth(), getMaximumDesignHeight()) );
        
        /*
        setBorder( BorderFactory.createImageBorder(
                new Insets(10,10,10,10), 
                new Insets(14,14,14,14), Utilities.loadImage("com/jaspersoft/ireport/designer/widgets/pageborder.png") ));
        */
        setBorder(new ReportBorder(scene));
        
        
        setBackground(Color.WHITE);
        setOpaque(false);
        setCheckClipping(true);
        updateBounds();
    }
    
    public void updateBounds()
    {
        JasperDesign jd = ((ReportObjectScene)getScene()).getJasperDesign();
        this.setPreferredSize(
                new Dimension( jd.getPageWidth()+20, ModelUtils.getDesignHeight(jd)+20) );
    }
    
    /*
    public Rectangle  calculateClientArea()
    {
        return new Rectangle(getJasperDesign().getPageWidth() + 20, getDesignHeight()+20 );
    }
    */
    
    public JasperDesign getJasperDesign()
    {
        return ((ReportObjectScene)this.getScene()).getJasperDesign();
    }
    
    

    @Override
    protected void paintWidget() {
        super.paintWidget();
        
        //Draw the bands...
        Graphics2D g = this.getGraphics();
        
        g.setColor(ReportObjectScene.DESIGN_LINE_COLOR);
        Stroke oldStroke = g.getStroke();
        //g.setStroke(new BasicStroke(0));

        double zoom = getScene().getZoomFactor();
        Stroke bs = Java2DUtils.getInvertedZoomedStroke(oldStroke, getScene().getZoomFactor());
        g.setStroke(bs);
        
        
        JasperDesign jd = getJasperDesign();
        if (jd != null)
        {
            int dh = ModelUtils.getDesignHeight(jd);

            // Detached background...
            // Draw the background band
            if (IReportManager.getInstance().isBackgroundSeparated() &&
                jd.getBackground() != null &&
                jd.getBackground().getHeight() > 0)
            {
                dh -= jd.getBackground().getHeight();
                dh -= jd.getTopMargin();
                dh -= jd.getBottomMargin();
                dh -= 40;
            }

            // Paint the white document...
            g.setColor(Color.WHITE);
            g.fillRect(0,0,jd.getPageWidth(), dh);

            

            if (((ReportObjectScene)getScene()).isGridVisible())
            {
                paintGrid(g, new Rectangle(0,0,jd.getPageWidth(), dh));
            }

            // Paint restricted area...
            paintRestrictedArea(jd, g);

            g.setColor(ReportObjectScene.DESIGN_LINE_COLOR);
            // LEFT MARGINE LINE
            g.drawLine( jd.getLeftMargin(),0,
                        jd.getLeftMargin(),
                        dh);

            // RIGHT MARGIN LINE
            g.drawLine(jd.getPageWidth() - jd.getRightMargin(),0,
                       jd.getPageWidth() - jd.getRightMargin(),dh);

            g.drawLine( 0, jd.getTopMargin(),
                        jd.getPageWidth(), jd.getTopMargin());

            g.drawLine( 0,
                    dh - jd.getBottomMargin(),
                    jd.getPageWidth(),
                    dh - jd.getBottomMargin());
            
            // Draw the columns....
            if (jd.getColumnCount() > 1)
            {
                // TODO: show locked document portion 
                // Column header
                int c_x = jd.getLeftMargin();


//                if (jd.getPrintOrderValue() != null &&
//                    jd.getPrintOrderValue() == PrintOrderEnum.VERTICAL)
//                {
//                    int c_y0 = ModelUtils.getBandLocation(jd.getColumnHeader(), jd);
//                    int c_y1 = ModelUtils.getBandLocation(jd.getColumnFooter(), jd);
//                    for (int i=1; i < jd.getColumnCount(); ++i)
//                    {
//                        c_x += jd.getColumnWidth();
//                        g.drawLine( c_x, c_y0,
//                                    c_x, c_y1);
//
//                        c_x += jd.getColumnSpacing();
//                        g.drawLine( c_x, c_y0,
//                                    c_x, c_y1);
//
//                    }
//                }
//                else
//                {
                    List<JRBand> bands = new ArrayList<JRBand>();
                    if (jd.getColumnHeader() != null) bands.add(jd.getColumnHeader());
                    if (jd.getColumnFooter() != null) bands.add(jd.getColumnFooter());
                    if (jd.getDetailSection()!= null && jd.getDetailSection().getBands() != null
                        && jd.getDetailSection().getBands().length > 0)
                    {
                        bands.addAll( Arrays.asList(jd.getDetailSection().getBands()) );
                    }

                    if (jd.getPrintOrderValue() != null &&
                        jd.getPrintOrderValue() == PrintOrderEnum.VERTICAL)
                    {
                          // Add all the group header and footers as well..
                          List groups = jd.getGroupsList();
                          for (Object grpObj : groups)
                          {
                              JRGroup grp = (JRGroup)grpObj;
                              if (grp.getGroupHeaderSection() != null &&
                                  grp.getGroupHeaderSection().getBands() != null &&
                                  grp.getGroupHeaderSection().getBands().length > 0)
                              {
                                  bands.addAll( Arrays.asList( grp.getGroupHeaderSection().getBands() ) );
                              }
                              if (grp.getGroupFooterSection() != null &&
                                  grp.getGroupFooterSection().getBands() != null &&
                                  grp.getGroupFooterSection().getBands().length > 0)
                              {
                                  bands.addAll( Arrays.asList( grp.getGroupFooterSection().getBands() ) );
                              }
                          }
                    }


                    for (JRBand b : bands)
                    {
                        int c_y0 = ModelUtils.getBandLocation(b, jd);
                        int c_y1 = c_y0 + b.getHeight();
                        c_x = jd.getLeftMargin();
                        for (int i=1; i < jd.getColumnCount(); ++i)
                        {
                            c_x += jd.getColumnWidth();
                            g.drawLine( c_x, c_y0,
                                        c_x, c_y1);

                            c_x += jd.getColumnSpacing();
                            g.drawLine( c_x, c_y0,
                                        c_x, c_y1);

                        }
                    }
//                }

            }
            
            //g.setColor( Color.RED);
            //g.drawRect(0, 0, jd.getPageWidth()-4, dh-4);
            
            g.setFont( new Font( "Arial", Font.PLAIN, 20));
            
            int designHeight = jd.getTopMargin();
         
            g.setStroke(oldStroke);
            
            if (IReportManager.getPreferences().getBoolean( IReportManager.PROPERTY_SHOW_BAND_NAMES, true))
            {
                List<JRBand> bands = ModelUtils.getBands(jd);

                for (JRBand b : bands)
                {
                    // Detached background...
                    if (b instanceof JRDesignBand &&
                        ((JRDesignBand)b).getOrigin().getBandTypeValue() == BandTypeEnum.BACKGROUND &&
                        IReportManager.getInstance().isBackgroundSeparated())
                    {
                        continue;
                    }

                    designHeight += b.getHeight();
                    paintBand(g, jd, ModelUtils.nameOf(b, jd) , b, designHeight);
                }
            }



            oldStroke = g.getStroke();
            g.setStroke(bs);

            // Detached background...
            // Draw the background band
            if (IReportManager.getInstance().isBackgroundSeparated() &&
                jd.getBackground() != null &&
                jd.getBackground().getHeight() > 0)
            {

                designHeight += jd.getBottomMargin();

                Color oldC = g.getColor();
//                g.setColor(Color.RED);
//                g.drawLine( 0,designHeight,
//                            jd.getPageWidth(),
//                            designHeight);

                // new page 0 position
                designHeight += 40; // space before the background...

//                g.drawLine( 0,designHeight,
//                            jd.getPageWidth(),
//                            designHeight);


                
                int bgPageHeight = jd.getBackground().getHeight() + jd.getTopMargin() + jd.getBottomMargin();
                // Paint a new page...
                g.setColor(Color.WHITE);
                g.fillRect(0,designHeight,jd.getPageWidth(), bgPageHeight);

                if (((ReportObjectScene)getScene()).isGridVisible())
                {
                    paintGrid(g, new Rectangle(0,designHeight,jd.getPageWidth(), bgPageHeight));
                }
                
                //g.setColor(Color.GREEN);
                g.setColor(ReportObjectScene.DESIGN_LINE_COLOR);

                // LEFT MARGINE LINE
                g.drawLine( jd.getLeftMargin(),designHeight,
                            jd.getLeftMargin(),
                            designHeight+bgPageHeight);

                // RIGHT MARGIN LINE
                g.drawLine(jd.getPageWidth() - jd.getRightMargin(),designHeight,
                           jd.getPageWidth() - jd.getRightMargin(),designHeight+bgPageHeight);

                g.drawLine( 0, designHeight + jd.getTopMargin(),
                            jd.getPageWidth(), designHeight + jd.getTopMargin());

                g.drawLine( 0,
                        designHeight + bgPageHeight - jd.getBottomMargin(),
                        jd.getPageWidth(),
                        designHeight + bgPageHeight - jd.getBottomMargin());


                designHeight += jd.getTopMargin();
                g.setStroke(oldStroke);
                
                paintBand(g, jd, ModelUtils.nameOf(jd.getBackground(), jd) , jd.getBackground(), designHeight + jd.getBackground().getHeight());
                g.setColor(oldC);
            }
        }
        g.setStroke(oldStroke);

        
    }
    
    private void paintBand(Graphics2D g, JasperDesign jd, String title, JRBand b, int bandBottom)
    {
        if (b== null || b.getHeight() == 0) return;
        
        //g.drawLine( 0, bandBottom, jd.getPageWidth(), bandBottom);
        
        int txt_width = g.getFontMetrics().stringWidth(title)/2;
        int txt_height = g.getFontMetrics().getHeight()/2;
        txt_height -= g.getFontMetrics().getMaxDescent();
        
        
        
        Java2DUtils.setClip(g,
                    jd.getLeftMargin(),
                    bandBottom-b.getHeight(),
                    jd.getPageWidth() - jd.getRightMargin(),
                    b.getHeight() );
        
        Paint oldPaint = g.getPaint();
        g.setPaint(ReportObjectScene.GRID_LINE_COLOR);
        g.drawString( title,
                    (jd.getPageWidth()/2) - txt_width,
                    bandBottom - (b.getHeight()-txt_height)/2 ); 
        Java2DUtils.resetClip(g);
 
        g.setPaint(oldPaint);
    }
    
    
    protected void paintGrid(Graphics2D g, Rectangle area) {
        Paint oldPaint = g.getPaint();

        // The top/left corner of the textture must match where we are...
        AffineTransform backT = g.getTransform();
        g.translate(area.x, area.y);

        g.setPaint( getGridTexture() );
        
        g.fill( new Rectangle(0,0,area.width,area.height) );
        g.setPaint(oldPaint);
        g.setTransform(backT);
    }

    private TexturePaint getGridTexture()
    {
        if ( gridTexture == null || gridTexture.getImage().getWidth() != getGridSize() )
        {
                BufferedImage img = new BufferedImage( getGridSize(), getGridSize(), BufferedImage.TYPE_INT_RGB );
                Graphics2D g2 = img.createGraphics();
                g2.setColor(new Color(255,255,255,255));
                g2.fill( getClientArea() );
                g2.setColor( ReportObjectScene.GRID_LINE_COLOR );
                g2.setStroke( GRID_STROKE );
                g2.drawLine( getGridSize()-1, 0, getGridSize()-1, getGridSize()-1 );
                g2.drawLine( 0, getGridSize()-1, getGridSize()-1, getGridSize()-1 );
                gridTexture = new TexturePaint( img, new Rectangle(0,0, getGridSize(), getGridSize() ) );
        }
        return gridTexture;
    }


    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }


    private  static TexturePaint getRestrictedAreaTexture()
    {
        if ( restrictedAreaTexture == null )
        {
                Image img2 = ImageUtilities.loadImage("com/jaspersoft/ireport/designer/resources/restricted_area.png");
                BufferedImage img = new BufferedImage(14, 14, BufferedImage.TYPE_INT_ARGB);
                img.getGraphics().drawImage(img2, 0, 0, null);
                restrictedAreaTexture = new TexturePaint( img, new Rectangle(0,0, 14, 14) );
        }
        return restrictedAreaTexture;
    }

    private void paintRestrictedArea(JasperDesign jd, Graphics2D g) {

        if (jd.getColumnCount() <= 1) return;
        // Column header
        int c_x = jd.getLeftMargin();
//        if (jd.getPrintOrderValue() != null &&
//            jd.getPrintOrderValue() == PrintOrderEnum.VERTICAL)
//        {
//            int c_y0 = ModelUtils.getBandLocation(jd.getColumnHeader(), jd);
//            int c_y1 = ModelUtils.getBandLocation(jd.getPageFooter(), jd);
//
//            Paint oldPaint = g.getPaint();
//            g.setPaint(getRestrictedAreaTexture());
//            int x0 = jd.getLeftMargin() + jd.getColumnWidth();
//            int width = jd.getPageWidth() - x0 - jd.getRightMargin();
//
//            if (jd.getColumnDirection() == RunDirectionEnum.RTL)
//            {
//                x0 = jd.getLeftMargin();
//                width = (jd.getColumnCount()-1)*jd.getColumnWidth() + (jd.getColumnCount()-1)*jd.getColumnSpacing();
//            }
//
//            g.fillRect(x0, c_y0, width, c_y1-c_y0);
//            g.setPaint(oldPaint);
//        }
//        else
//        {
            List<JRBand> bands = new ArrayList<JRBand>();
            if (jd.getColumnHeader() != null) bands.add(jd.getColumnHeader());
            if (jd.getColumnFooter() != null) bands.add(jd.getColumnFooter());
            if (jd.getDetailSection()!= null && jd.getDetailSection().getBands() != null
                && jd.getDetailSection().getBands().length > 0)
            {
                bands.addAll( Arrays.asList(jd.getDetailSection().getBands()) );
            }

            if (jd.getPrintOrderValue() != null &&
                jd.getPrintOrderValue() == PrintOrderEnum.VERTICAL)
            {
                  // Add all the group header and footers as well..
                  List groups = jd.getGroupsList();
                  for (Object grpObj : groups)
                  {
                      JRGroup grp = (JRGroup)grpObj;
                      if (grp.getGroupHeaderSection() != null &&
                          grp.getGroupHeaderSection().getBands() != null &&
                          grp.getGroupHeaderSection().getBands().length > 0)
                      {
                          bands.addAll( Arrays.asList( grp.getGroupHeaderSection().getBands() ) );
                      }
                      if (grp.getGroupFooterSection() != null &&
                          grp.getGroupFooterSection().getBands() != null &&
                          grp.getGroupFooterSection().getBands().length > 0)
                      {
                          bands.addAll( Arrays.asList( grp.getGroupFooterSection().getBands() ) );
                      }
                  }
            }
            
            int x0 = jd.getLeftMargin() + jd.getColumnWidth();
            int width = jd.getPageWidth() - x0 - jd.getRightMargin();

            if (jd.getColumnDirection() == RunDirectionEnum.RTL)
            {
                x0 = jd.getLeftMargin();
                width = (jd.getColumnCount()-1)*jd.getColumnWidth() + (jd.getColumnCount()-1)*jd.getColumnSpacing();
            }

            Paint oldPaint = g.getPaint();
            g.setPaint(getRestrictedAreaTexture());
            for (JRBand b : bands)
            {
                int c_y0 = ModelUtils.getBandLocation(b, jd);
                g.fillRect(x0, c_y0, width, b.getHeight());
            }

             g.setPaint(oldPaint);
        }

//    }
    
}
