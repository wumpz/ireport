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
package com.jaspersoft.ireport.designer.ruler;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.utils.Unit;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import org.netbeans.api.visual.widget.Scene.SceneListener;

/**
 *
 * @author  gtoffoli
 */
public class RulerPanel extends javax.swing.JPanel implements MouseListener, MouseMotionListener, SceneListener, ComponentListener {
    
    
    public static final Color BORDER_COLOR=new Color(127,157,185);
    
    private java.util.List<GuideLineChangedListener> guideLineChangedListeners = new java.util.ArrayList<GuideLineChangedListener>();

    public void addGuideLineChangedListener(GuideLineChangedListener listener)
    {
        if (!guideLineChangedListeners.contains(listener))
        {
            guideLineChangedListeners.add( listener );
        }
    }
    
    private double unitPixels = Unit.PIXEL;
    private double zoomFactor = 1.0;
    
    private int lastMousePosition = -1;
    private boolean vertical = false;
    
    private java.awt.image.BufferedImage savedImage = null;
    private java.util.List guideLines = new java.util.ArrayList();
    
    private GuideLine editingGuideLine = null;

    private static final javax.swing.ImageIcon hGuideLineIcon = new javax.swing.ImageIcon(RulerPanel.class.getResource("/com/jaspersoft/ireport/designer/ruler/rulestop_horizontal.png"));
    private static final javax.swing.ImageIcon vGuideLineIcon = new javax.swing.ImageIcon(RulerPanel.class.getResource("/com/jaspersoft/ireport/designer/ruler/rulestop_vertical.png"));
     
    private double zeroPos = 0.0;
    
    public List getGuideLines() {
        return guideLines;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }
    
    static int[] normal_intervals = new int[]{1,2,5,10,25,50,100,200,500,1000,2500,5000,10000};
    
    private AbstractReportObjectScene scene = null;
    
    private JScrollPane sceneScrollPane = null;

    public AbstractReportObjectScene getScene() {
        return scene;
    }

    /**
     *  Set the report panel to which the ruler refers to.
     */
    public void setScene(AbstractReportObjectScene scene) {
        this.scene = scene;
    }
    
    /** Creates new form RulerPanel */
    public RulerPanel(AbstractReportObjectScene scene) {
        initComponents();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.scene = scene;
        
        String newUnitName = IReportManager.getPreferences().get("Unit", "inches");
        unitPixels = Unit.getUnit(newUnitName).getConversionValue();
                    
        IReportManager.getPreferences().addPreferenceChangeListener(new PreferenceChangeListener() {

            public void preferenceChange(PreferenceChangeEvent evt) {
                if (evt.getKey() != null && evt.getKey().equals("Unit"))
                {
                    String newUnitName = IReportManager.getPreferences().get("Unit", "inches");
                    setUnitPixels( Unit.getUnit(newUnitName).getConversionValue() );
                    repaint();
                }
            }
        });
        
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        if (getScene() == null) return;
        getScene().addSceneListener(this);
        JComponent viewComponent = getScene().getView();
        //if (viewComponent == null)
        //    viewComponent = scene.createView ();
        viewComponent.addComponentListener (this);
        refreshRuler();
    }

    @Override
    public void removeNotify() {
        if (getScene() != null)
        {
            getScene().getView().removeComponentListener (this);
            getScene().removeSceneListener (this);
        }
        super.removeNotify();
    }

    @SuppressWarnings("unchecked")
    public void addGuideLine(GuideLine guide) {
        getGuideLines().add(guide);
        fireGuideLineAdded(guide);
    }
    
    public void removeGuideLine(GuideLine guide) {
        getGuideLines().remove(guide);
        fireGuideLineRemoved(guide);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(20, 20));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 221, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public double getUnitPixels() {
               
        return unitPixels;
    }

    public void setUnitPixels(double unitPixels) {
        this.unitPixels = unitPixels;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }
    
    /**
     * Painting function.
     * interval: it is the number of units between two lines. If there is sufficent
     *           space between two lines, the function paintSubunit() will take care of it.
     * interval2: it the space between two labels. Again, it depends by the unit.
     *           if unit are pixels, a special value is used, since we want to see specific
     *           numbers instead of calculated values.
     * 
     * 
     */
    public void paint(Graphics gx)
    {
        super.paint(gx);
        Graphics2D g = (Graphics2D)gx;
        
        Paint oldp = g.getPaint();
        
        double zeroPos = getZeroPos();
        
        JasperDesign jd = null;
        JScrollPane scrollPane = null;
        if (getScene() != null && getScene().getJasperDesign() != null)
        {
            if (getScene() instanceof ReportObjectScene)
            {
                jd = getScene().getJasperDesign();
            }
            scrollPane = findScrollPane(getScene().getView());
        }
        
        if (jd != null)
        {
            g.setPaint(new Color(128,128,128,128));
            
            if (isVertical())
            {
                int scroll = (scrollPane==null || scrollPane.getVerticalScrollBar()==null) ? 0 : scrollPane.getVerticalScrollBar().getValue();
                if (getZeroPos() > 0)
                {
                    g.fillRect(0,0, getWidth(),(int)getZeroPos());
                }
                // Set a different color for each band...
                List<JRBand> bands = ModelUtils.getBands(jd);
                int y0 = (int)getZeroPos();
                int lastY = 10 + jd.getTopMargin();
                boolean gray = false;
                
                Color superLight = new Color(255,200,200,160);
                for (JRBand b :  bands)
                {
                    if (b == null || b.getHeight() == 0) continue;

                    if (b instanceof JRDesignBand &&
                        ((JRDesignBand)b).getOrigin().getBandTypeValue() == BandTypeEnum.BACKGROUND &&
                        IReportManager.getInstance().isBackgroundSeparated())
                    {
                        // Print some extra gray stuff...
                        int y_g1 = (int)Math.round((40+jd.getTopMargin() + jd.getBottomMargin())*getZoomFactor() );
                        Paint oldC = g.getPaint();
                        g.setPaint(new Color(128,128,128,128));
                        g.fillRect(0,y0, getWidth(),y_g1);
                        g.setPaint(oldC);

                        lastY+=40+jd.getTopMargin() + jd.getBottomMargin();
                        y0 += y_g1;
                    }

                    lastY += b.getHeight();
                    int y1 = (int)Math.round(lastY*getZoomFactor() ) - scroll;
                    
                    g.setPaint(gray ? superLight : Color.WHITE);
                    g.fillRect(0,y0, getWidth(),y1-y0);
                    
                    gray = !gray;
                    
                    y0 = y1;
                    if (y0 > getHeight()) break;
                }
                
                g.setPaint(new Color(128,128,128,128));
                
                if (y0 < getHeight())
                {
                    g.fillRect(0,y0, getWidth(),getHeight());
                }
                
            }
            else
            {
                if (getZeroPos() > 0)
                {
                    g.fillRect(0,0,(int)getZeroPos(), this.getHeight());
                }
                
                
                int x1 = (int)Math.round( ((10 + jd.getPageWidth() - jd.getRightMargin())*getZoomFactor()) - ( (scrollPane==null || scrollPane.getHorizontalScrollBar()==null) ? 0 : scrollPane.getHorizontalScrollBar().getValue()) );
                if (x1 < getWidth())
                {
                    g.fillRect(x1,0,getWidth(), this.getHeight());
                }
            }
        }
        
        Paint p = isVertical() ? 
            new GradientPaint((int)(this.getWidth()*0.2),0, new Color(150,150,150,128),this.getWidth(),0, new Color(255,255,255,0)) :
            new GradientPaint(0,(int)(this.getHeight()*0.2), new Color(200,200,200,128),0,this.getHeight(), new Color(255,255,255,0));
        
        g.setPaint(p);
        g.fillRect(0, 0,this.getWidth(), this.getHeight());
        
        p = isVertical() ? 
            new GradientPaint(0,0, new Color(255,255,255,255),(int)(this.getWidth()*0.3),0, new Color(255,255,255,0)) :
            new GradientPaint(0,0, new Color(255,255,255,255),0,(int)(this.getHeight()*0.3), new Color(255,255,255,0));
        
        g.setPaint(p);
        
        if (isVertical()) g.fillRect(1, 0,1+(int)(this.getWidth()*0.3), this.getHeight());
        else  g.fillRect(0, 1,this.getWidth(), 1+(int)(this.getHeight()*0.3));
        
        g.setPaint(oldp);
        
        int interval = 1;
        int interval2 = 1;
        
        while (interval*unitPixels*zoomFactor<=3) interval++;
        // Check if a number is divisible by interval....
        interval = normalizeInterval(interval);
        interval2 = calculateLabelsInterval(interval);
        
        // find the number of units visible in the negative portion of the axis:
        double firstUnit = ((int)(zeroPos/(unitPixels*zoomFactor)))+1;
        double interval_len = interval*unitPixels*zoomFactor;
        
        // Print positive ticks
        double newZeroPos = zeroPos;
        
        if (isVertical() && jd != null)
        {
                int scroll = (scrollPane==null || scrollPane.getVerticalScrollBar()==null) ? 0 : scrollPane.getVerticalScrollBar().getValue();
                // Set a different color for each band...
                List<JRBand> bands = ModelUtils.getBands(jd);
                
                int lastY = 10 + jd.getTopMargin();
                for (JRBand b :  bands)
                {
                    if (b == null || b.getHeight() == 0) continue;

                    int y1 = 0;
                  if (b instanceof JRDesignBand &&
                        ((JRDesignBand)b).getOrigin().getBandTypeValue() == BandTypeEnum.BACKGROUND &&
                        IReportManager.getInstance().isBackgroundSeparated())
                    {
                        // Print some extra gray stuff...
                        lastY +=40+jd.getTopMargin() + jd.getBottomMargin();
                        y1 = (int)Math.round(lastY*getZoomFactor() ) - scroll;
                        for (double unit=0; ; unit += interval)
                        {
                            double pos = unit*unitPixels*zoomFactor + newZeroPos;
                            if (pos > y1) break;
                            paintUnitTicks(g,unit,pos,interval_len, (unit%(interval*interval2)) == 0 && ( unit == 0 || (y1 - pos) > 30), Math.max(1, y1) );
                        }
                        newZeroPos = y1;
                    }

                    lastY += b.getHeight();
                    y1 = (int)Math.round(lastY*getZoomFactor() ) - scroll;
                    
                    for (double unit=0; ; unit += interval)
                    {
                        double pos = unit*unitPixels*zoomFactor + newZeroPos;
                        if (pos > y1) break;
                        paintUnitTicks(g,unit,pos,interval_len, (unit%(interval*interval2)) == 0 && ( unit == 0 || (y1 - pos) > 30), Math.max(1, y1) );
                    }
                    
                    newZeroPos = y1;
                    if (newZeroPos > getHeight()) break;
                }
                
                if (newZeroPos < getHeight())
                {
                    for (double unit=0; ; unit += interval)
                    {
                        double pos = unit*unitPixels*zoomFactor + newZeroPos;
                        if (pos > getHeight()) break;
                        paintUnitTicks(g,unit,pos,interval_len, (unit%(interval*interval2)) == 0, -1);
                    }
                }
        }
        else
        {
            for (double unit=0; ; unit += interval)
            {
                double pos = unit*unitPixels*zoomFactor + newZeroPos;
                if (isVertical() && pos > this.getHeight()) break;
                if (!isVertical() && pos > this.getWidth()) break;
                paintUnitTicks(g,unit,pos,interval_len, (unit%(interval*interval2)) == 0, -1);
            }
        }
        
        // Print negative ticks
        //g.setColor(Color.LIGHT_GRAY);
        for (double unit= -interval; ; unit -= interval)
        {
            double pos = unit*unitPixels*zoomFactor + zeroPos;
            paintUnitTicks(g,unit,pos,interval_len, (unit%(interval*interval2)) == 0, -1);
            if (pos < -interval) break;
        }
        
        g.setColor(BORDER_COLOR);
        if (isVertical())
        {
            g.drawLine(getWidth()-1, 0, getWidth()-1, this.getHeight());
        }
        else
        {
            g.drawLine(0,getHeight()-1, this.getWidth(), getHeight()-1);
        }

        // Paint guidelines...
        for (int i=0; i<getGuideLines().size(); ++i)
        {
            GuideLine pos = (GuideLine)getGuideLines().get(i);
            // Calc posI....
            if (pos != editingGuideLine)
            {
                int posI = (int)(pos.getPosition()*getZoomFactor()  + zeroPos);
                if (isVertical()) g.drawImage(vGuideLineIcon.getImage(),getWidth()-16, posI-4, this);
                else g.drawImage(hGuideLineIcon.getImage(),posI-4, getHeight()-16, this);
            }
        }
        
        lastMousePosition = -1;
    }
    
    /**
     * Print the ticks in a particural interval starting from unit, at position pos.
     * The interval length in pixels is interval_len.
     * If printLabel is true, a label for this unit tick is printed. FontMetrics is used to calculate
     * the label width (passed for performance reasons).
     * 
     * limit defines the last pixel paintable...
     */
    private void paintUnitTicks(Graphics2D g, double unit, double x, double width, boolean printLabel, int limit)
    {
        int lineHeight = 5;
        if (unit == (int)unit) lineHeight = 7;
        
        if (getUnitPixels() == Unit.PIXEL) lineHeight=5;
        
        if (printLabel) //
        {
            String unitStr =  ""+(int)unit;
            printLabel(g,unitStr,x);
            lineHeight=8;
        }
        
        Rectangle2D.Double rect = isVertical() ? 
            new Rectangle2D.Double( getWidth() -1 - lineHeight,x,lineHeight,0) :
            new Rectangle2D.Double( x,getHeight() -1 - lineHeight,0,lineHeight);
        g.draw(rect);
        
        paintSubunit(g, unit, x, width, 6, 1, limit);
    }
    
    /**
     *  Paint all the ticks between x0 and x1.
     *  If there is space, 10 ticks are printed, otherwise, only half
     *  On each single tick, this method is called again...
     *  fraction is the quantity of unit to paint (1,0.5,0.25...) and it is used to calculate
     *  the label to print if requires
     */
    private void paintSubunit(Graphics2D g, 
                              double unit,
                              double x,
                              double width,
                              int tickHeight,
                              int level,
                              int limit)
    {
        Color c = g.getColor();
        if (level > 1) g.setColor(Color.GRAY);
        if (level > 2) g.setColor(Color.LIGHT_GRAY);
        //if (x < getZeroPos()) g.setColor(Color.LIGHT_GRAY);
        if (width >= 30) // Space for 10 ticks.
        {
            double pos = x;
            
            paintSubunit(g, unit, pos, width/10.0, tickHeight-2, level+1, limit);
            
            DecimalFormat dnf = new DecimalFormat("0.0");
            
            for (int i=1; i<10; ++i)
            {
                int realTickHeight = tickHeight-2;
                
                pos = x + (width/10.0) * i;
                
                if (limit > 0 && pos >= limit)
                {
                    g.setColor(c);
                    return;
                }
                
                if (i==5)
                {
                    realTickHeight = tickHeight;
                    // Print a label if there is a lot of space between two unit...
                    if (level == 1 && width > 150 && (limit <= 0 || limit-pos > 30) )
                    {
                        String unitStr = dnf.format( unit + 0.5 );
                        printLabel(g,unitStr,pos);
                    }
                }
                
                if (level == 1 && width > 500 && i != 5 && (limit <= 0 || limit-pos > 30))
                {
                    
                    String unitStr =  dnf.format( (unit + i*0.1) );
                    printLabel(g,unitStr,pos);
                }
                
                
                paintSubunit(g, unit, pos, width/10.0, tickHeight-2, level+1, limit);
                
                Rectangle2D.Double rect = isVertical() ?
                        new Rectangle2D.Double( getWidth() - realTickHeight -1, pos, realTickHeight, 0) :
                        new Rectangle2D.Double( pos,getHeight() - realTickHeight -1,0,realTickHeight);
                
                g.draw(rect);
                
            }
        }
        else if (width >= 10) // Space for half tick
        {
                double pos = x + (width/2.0);
                Rectangle2D.Double rect = isVertical() ?
                    new Rectangle2D.Double( getWidth() - tickHeight -1, pos,tickHeight, 0) :
                    new Rectangle2D.Double( pos,getHeight() - tickHeight -1,0,tickHeight);
                g.draw(rect);
        }
        g.setColor(c);
    }
    
    private void printLabel(Graphics2D g, String unitStr, double x)
    {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(unitStr, g);
        
        if (isVertical())
        {
            writeRotateString(g, getWidth()-10f, x, unitStr); //(float)x - (float)r.getHeight()/2f
        }
        else
        {
            g.drawString(unitStr, (float)x - (float)r.getWidth()/2f, getHeight()-10f);
        }
        
    }
    
    
    private int normalizeInterval(int interval)
    {
        for (int i=0; i<normal_intervals.length-1; ++i)
        {
            if (interval <= normal_intervals[i])
            {
                return normal_intervals[i];
            }
        }
        
        return interval;
    }
    
    
    /**
     *  Return the position of 0 in the component.
     * 
     */
    private double getZeroPos()
    {
        // Return the position of the zero...
        return this.zeroPos;
    }
    
    /**
     *  Return the position of 0 in the component.
     * 
     */
    private double setZeroPos(double d)
    {
        // Return the position of the zero...
        return  this.zeroPos = d;
    }
    
    /**
     * Based on the unit per tick (interval) used to paint the ruler,
     * this method calculates the number of units between two labels.
     * 
     */
    private int calculateLabelsInterval(int interval)
    {
        int interval2 = 1;
        for (int i=0; i<normal_intervals.length-1; ++i)
        {
            if (normal_intervals[i]*interval*unitPixels*zoomFactor >= 30)
            {
                return normal_intervals[i];
            }
        }
        
        while (interval2*interval*unitPixels*zoomFactor>=30) interval2++;
        return interval2;
    }

    public void mouseClicked(MouseEvent e) {
        
    }

    public void mousePressed(MouseEvent evt) {
        
        // Look for an existing gridline....
        updateMousePosition(-1);
        editingGuideLine = null;
        for (int i=0; i<getGuideLines().size(); ++i)
        {
            GuideLine pos = (GuideLine)getGuideLines().get(i);
            int posI = (int)Math.round( pos.getPosition()*zoomFactor + getZeroPos() );
            // Calc posI....
            int mousePos = isVertical() ? evt.getY() : evt.getX();
            
            if (mousePos > posI-3 && mousePos < posI+3)
            {
                editingGuideLine = pos;
                //getGuideLines().remove(i);
                break;
            }
        }
        
        savedImage = ((java.awt.image.BufferedImage)this.createImage(this.getWidth(), this.getHeight()));
        Graphics2D savedGraphics = savedImage.createGraphics();
        this.paint( savedGraphics );
        
        if (editingGuideLine == null)
        {
            editingGuideLine = new GuideLine(getLogicalPosition(isVertical() ? evt.getY() : evt.getX()), isVertical());
            addGuideLine(editingGuideLine);
        }
        else
        {
            this.getGraphics().drawImage(savedImage,0, 0, this);
        }
        
        if (!isVertical()) this.getGraphics().drawImage(hGuideLineIcon.getImage(),evt.getX()-4, getHeight()-16, this);
        else this.getGraphics().drawImage(vGuideLineIcon.getImage(),getWidth()-16, evt.getY()-4, this);
    }

    public void mouseReleased(MouseEvent evt) {
        //this.getGraphics().drawImage(savedImage,0, 0, this);
       
        if (editingGuideLine == null) return;
        if (!isVertical())
        {
            if (evt.getX() > 0 && evt.getX() < this.getWidth())
            {
                editingGuideLine.setPosition(getLogicalPosition(evt.getX()));
                this.repaint();
            }
            else
            {
                removeGuideLine(editingGuideLine);
            }
        }
        else
        {
            if (evt.getY() > 0 && evt.getY() < this.getHeight())
            {
                editingGuideLine.setPosition(getLogicalPosition(evt.getY()));
                this.repaint();
            }
            else
            {
                removeGuideLine(editingGuideLine);
            }
        }
        editingGuideLine = null;
        this.repaint();
    }

    public void mouseEntered(MouseEvent e) {
        updateMousePosition(e.getX());
    }

    public void mouseExited(MouseEvent e) {
        
        //updateMousePosition(-1);
    }

    public void mouseDragged(MouseEvent evt) {
               
        
        if (evt.getSource() == this)
        {
            if (editingGuideLine == null) return;
            
            editingGuideLine.setPosition(getLogicalPosition( isVertical() ? Math.max(0, evt.getY()) : Math.max(0,evt.getX()) ));
            fireGuideLineMoved(editingGuideLine);
            this.getGraphics().drawImage(savedImage,0, 0, this);

            int realPosition = (int)(Math.round( editingGuideLine.getPosition()*zoomFactor ) + zeroPos);
            if (!isVertical()) this.getGraphics().drawImage(hGuideLineIcon.getImage(),realPosition-4, getHeight()-16, this);
            else this.getGraphics().drawImage(vGuideLineIcon.getImage(),getWidth()-16, realPosition-4, this);
        }
        else
        {
            Component c = evt.getComponent();
            Point p = new Point(0,0);
            if (c!= null)
            {
                p.x = evt.getX() + c.getLocationOnScreen().x;
                p.y = evt.getY() + c.getLocationOnScreen().y;
            }
            SwingUtilities.convertPointFromScreen(p,this);
            updateMousePosition(isVertical() ? p.y : p.x);
        }
        
    }

    public void mouseMoved(MouseEvent e) {

        //Point p = e.getLocationOnScreen();
        Component c = e.getComponent();
        Point p = new Point(0,0);
        if (c!= null)
        {
            p.x = e.getX() + c.getLocationOnScreen().x;
            p.y = e.getY() + c.getLocationOnScreen().y;
        }
            
        SwingUtilities.convertPointFromScreen(p,this);
        updateMousePosition(isVertical() ? p.y : p.x);
    }
    
    public void updateMousePosition(int newPosition)
    {
        if (lastMousePosition == newPosition) return;
        Graphics g = this.getGraphics();
        if (g == null) return;
        g.setXORMode(Color.PINK.darker());
        //this.getGraphics()
        if (lastMousePosition >= 0)
        {
            //this.getGraphics().setColor(Color.RED);
            if (isVertical())
            {
                g.drawLine(0,lastMousePosition, this.getWidth(), lastMousePosition);
            }
            else
            {
                g.drawLine(lastMousePosition, 0, lastMousePosition, this.getHeight());
            }
            
        }
        
        lastMousePosition = newPosition;
        if (isVertical())
        {
            g.drawLine(0,lastMousePosition, this.getWidth(), lastMousePosition);
        }
        else
        {
            g.drawLine(lastMousePosition, 0, lastMousePosition, this.getHeight());
        }
        g.setPaintMode();
    }

    
    /**
     * Writes a String at the x position, centered in y.
     * 
     */
    public void writeRotateString(Graphics2D g2, double x, double yCenter, String s)
  {
        java.awt.geom.Rectangle2D sb = g2.getFontMetrics().getStringBounds(s, g2) ;
  	double sw = sb.getWidth();
  	double sh = sb.getHeight();
  	
        Color c = g2.getColor();
        g2.setColor( Color.RED );
        sb.setRect( x - sw/2.0, yCenter - sh, sw, sh);
        //g2.draw(sb);
        
        g2.setColor(c);
        
        AffineTransform oldAr = g2.getTransform();
  	
  	double rotX = x - sw/2.0;
  	double rotY = yCenter - sh;
  	
        AffineTransform at =  g2.getTransform();
        at.rotate(-Math.PI/2.0, x, yCenter);
        //at.translate(-(sw/2),sh);
  	//AffineTransform at = AffineTransform.getRotateInstance(-Math.PI/2.0, x, yCenter);
  	//AffineTransform at2 = AffineTransform.getTranslateInstance(-(sw/2),sh);
  	g2.setTransform(at);
  	
        //g2.draw(sb);
        
  	g2.drawString(s,(float)sb.getX(),(float)(sb.getY() + sb.getHeight()) );//s,x - (sw/2) , yCenter + sh);
  	g2.setTransform(oldAr);
        
  }
    
  public int getLogicalPosition(int p)
  {
      return (int) Math.round((p-getZeroPos())/zoomFactor);
  }
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    public void sceneRepaint() {
        boolean needRefresh = false;
        if (getScene().getZoomFactor() != zoomFactor)
        {
            needRefresh = true;
        }
        
        JasperDesign jd = getScene().getJasperDesign();
        if (jd != null && !needRefresh)
        {
            sceneScrollPane = findScrollPane(getScene().getView());
            double newZeroPos = 0;
            if (isVertical())
            {
                newZeroPos = ((10.0 + jd.getTopMargin()) * zoomFactor) - sceneScrollPane.getVerticalScrollBar().getValue();
            }
            else
            {
                newZeroPos = ((10.0 + jd.getLeftMargin()) * zoomFactor) - sceneScrollPane.getHorizontalScrollBar().getValue();
            }
            
            if (newZeroPos != zeroPos) needRefresh = true;
        }
        if (needRefresh) refreshRuler();    
    }

    public void sceneValidating() {
    }

    public void sceneValidated() {
    }

    public void componentResized(ComponentEvent e) {
        refreshRuler();
    }

    public void componentMoved(ComponentEvent e) {
        refreshRuler();
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }
    
    
    private JScrollPane findScrollPane (JComponent component) {
        for (;;) {
            if (component == null)
                return null;
            if (component instanceof JScrollPane)
                return ((JScrollPane) component);
            Container parent = component.getParent ();
            if (! (parent instanceof JComponent))
                return null;
            component = (JComponent) parent;
        }
    }
    
    //static int kkk = 0;
    public void refreshRuler()
    {
        
        SwingUtilities.invokeLater( new Runnable() {

            public void run() {
                
                
                if (getScene() != null)
                {
                    zoomFactor = getScene().getZoomFactor();
                    JasperDesign jd = getScene().getJasperDesign();
                    if (jd != null)
                    {
                        sceneScrollPane = findScrollPane(getScene().getView());
                        
                        if (isVertical())
                        {
                            zeroPos = ((10.0 + jd.getTopMargin()) * zoomFactor) - sceneScrollPane.getVerticalScrollBar().getValue();
                        }
                        else
                        {
                            zeroPos = ((10.0 + jd.getLeftMargin()) * zoomFactor) - sceneScrollPane.getHorizontalScrollBar().getValue();
                        }
                    }
                }
                repaint();
            }
        });
    }
    
    public void fireGuideLineAdded(GuideLine guideLine)
    {
        for (GuideLineChangedListener listener : guideLineChangedListeners)
        {
            listener.guideLineAdded(guideLine);
        }
    }
    
    public void fireGuideLineRemoved(GuideLine guideLine)
    {
        for (GuideLineChangedListener listener : guideLineChangedListeners)
        {
            listener.guideLineRemoved(guideLine);
        }
    }
    
    public void fireGuideLineMoved(GuideLine guideLine)
    {
        for (GuideLineChangedListener listener : guideLineChangedListeners)
        {
            listener.guideLineMoved(guideLine);
        }
    }
}
