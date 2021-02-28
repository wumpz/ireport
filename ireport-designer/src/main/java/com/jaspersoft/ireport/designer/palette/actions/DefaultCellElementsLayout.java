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
package com.jaspersoft.ireport.designer.palette.actions;

import com.jaspersoft.ireport.designer.IRLocalJasperReportsContext;
import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.fill.JRMeasuredText;
import net.sf.jasperreports.engine.fill.JRTextMeasurer;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRStyledTextParser;
import net.sf.jasperreports.engine.util.JRTextMeasurerUtil;
import org.netbeans.api.visual.animator.AnimatorEvent;
import org.netbeans.api.visual.animator.AnimatorListener;
import org.netbeans.api.visual.animator.SceneAnimator;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class DefaultCellElementsLayout {

    private static DefaultCellElementsLayout instance;
    
    public static DefaultCellElementsLayout getInstance()
    {
        if (instance == null)
        {
            instance = new DefaultCellElementsLayout();
        }
        return instance;
    }
    
    public static void doLayout(final JRDesignCellContents cell, final CrosstabObjectScene scene)
    {
                SwingUtilities.invokeLater(new Runnable() {

                     public void run() {
                        getInstance().organizeCellElements(cell, scene);
                     }
                 });
    }
    
    private void organizeCellElements(JRDesignCellContents cell, CrosstabObjectScene sc) {
        
        int cW = cell.getWidth();
        int cH = cell.getHeight();
        List children = cell.getChildren();
        int y = 0;
        if (children.size() == 0) return;
        int h = cH/children.size();
        
        SceneAnimator animator = new SceneAnimator(sc);
        CellAnimatorListener listener = new CellAnimatorListener();
        animator.getPreferredBoundsAnimator().addAnimatorListener(listener);
        animator.getPreferredLocationAnimator().addAnimatorListener(listener);      
        listener.addAnimation(); // bounds
        listener.addAnimation(); // location
        
        // 1 Calculate new elements size and location..
        for (int i=0; i<children.size(); ++i)
        {
            if (children.get(i) instanceof JRDesignElement)
            {
                JRDesignElement element = (JRDesignElement)children.get(i);
                listener.getNewSizes().put(element, new Rectangle(0, i*h, cW, h));
            }
        }
        
        for (Iterator iter = listener.getNewSizes().keySet().iterator(); iter.hasNext(); )
        {
            JRDesignElement element = (JRDesignElement)iter.next();
            Rectangle rectangle = listener.getNewSizes().get(element);
            Widget w = sc.findWidget(element);
                
            if (w != null)
            {
               Rectangle r = w.getPreferredBounds();
               r.width = rectangle.width;
               r.height = rectangle.height;
               animator.animatePreferredBounds(w, r);
                    
                Point p = new Point( w.getLocation() );
                p.x -= element.getX() + rectangle.x;
                p.y += -(element.getY()) + rectangle.y;
                    
                animator.animatePreferredLocation(w, p)  ;
            }
            
        }
    }
    
    private class CellAnimatorListener implements AnimatorListener
    {

        private HashMap<JRDesignElement, Rectangle> newSizes = new HashMap<JRDesignElement, Rectangle>();
        int animations = 0;
        public void addAnimation()
        {
            animations++;
        }
        
        public void animatorStarted(AnimatorEvent arg0) {
        }

        public void animatorReset(AnimatorEvent arg0) {
        }

        public void animatorFinished(AnimatorEvent arg0) {
            animations--;
            if (animations == 0)
            {
                for (Iterator iter = getNewSizes().keySet().iterator(); iter.hasNext(); )
                {
                    JRDesignElement element = (JRDesignElement)iter.next();
                    Rectangle rectangle = getNewSizes().get(element);
                    element.setX( rectangle.x);
                    element.setY( rectangle.y);
                    element.setWidth( rectangle.width);
                    element.setHeight( rectangle.height);
                    
                    // Calculate the best font height...
                    if (element instanceof JRDesignTextElement)
                    {
                        JRStyledTextParser styledTextParser = JRStyledTextParser.getInstance();
                        JRDesignTextElement dte = (JRDesignTextElement)element;
                        dte.setFontSize((Integer)null);
                        for (int i=dte.getFontSize()-1; i>1 ; --i)
                        {
                                String text = "test";
                                
                                // Convert the element in a print element...
                                Map<Attribute, Object> attributes = JRFontUtil.getAttributes(new HashMap(), dte, Locale.getDefault());
                                
                                JRStyledText styledText = 
                                    styledTextParser.getStyledText(
                                    attributes, 
                                    text, 
                                    JRCommonText.MARKUP_STYLED_TEXT.equals(dte.getMarkup()),//FIXMEMARKUP only static styled text appears on preview. no other markup
                                    Locale.getDefault()
				);

                                JasperReportsContext context = IRLocalJasperReportsContext.getInstance();
                                
                                JRTextMeasurerUtil measurerUtil = JRTextMeasurerUtil.getInstance(context);
                                JRTextMeasurer measurer = measurerUtil.createTextMeasurer(dte);
                                JRMeasuredText measuredText = measurer.measure(  styledText, 0, dte.getHeight(), true);
                                
                                if  (measuredText.getTextHeight() > dte.getHeight())
                                {
                                    if (i>1)
                                    {
                                        dte.setFontSize(i);
                                    }
                                    else
                                    {
                                        break;
                                    }
                                }
                                else
                                {
                                    break;
                                }
                        }
                    }
                }
            }
        }

        public void animatorPreTick(AnimatorEvent arg0) {
        }

        public void animatorPostTick(AnimatorEvent arg0) {
        }

        public HashMap<JRDesignElement, Rectangle> getNewSizes() {
            return newSizes;
        }

        
    }
}
