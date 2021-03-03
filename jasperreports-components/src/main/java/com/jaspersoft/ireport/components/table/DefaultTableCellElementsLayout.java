/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRCommonText;
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
 * @version $Id: DefaultTableCellElementsLayout.java 0 2010-03-31 10:49:46 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class DefaultTableCellElementsLayout {

 public static final int VERTICAL_LAYOUT = 0;
 public static final int HORIZONTAL_LAYOUT = 1;

 private static DefaultTableCellElementsLayout instance;

    public static DefaultTableCellElementsLayout getInstance()
    {
        if (instance == null)
        {
            instance = new DefaultTableCellElementsLayout();
        }
        return instance;
    }


    public static void doLayout(final DesignCell cell, final TableObjectScene scene, final int type)
    {
                SwingUtilities.invokeLater(new Runnable() {

                     public void run() {
                        getInstance().organizeCellElements(cell, scene, type);
                     }
                 });
    }

    public static void doLayout(final DesignCell cell, final TableObjectScene scene)
    {
                 doLayout(cell,scene, VERTICAL_LAYOUT);
    }

    private void organizeCellElements(DesignCell cell, TableObjectScene sc, int type) {

        Rectangle cellBounds = sc.getTableMatrix().getCellBounds(cell);

        Insets padding = new Insets(0,0,0,0);
        if (cell.getStyle() != null && cell.getStyle().getLineBox() != null)
        {
            if (cell.getStyle().getLineBox().getLeftPadding() != null)
            {
                padding.left = cell.getStyle().getLineBox().getLeftPadding();
            }

            if (cell.getStyle().getLineBox().getRightPadding() != null)
            {
                padding.right = cell.getStyle().getLineBox().getRightPadding();
            }

            if (cell.getStyle().getLineBox().getTopPadding() != null)
            {
                padding.top = cell.getStyle().getLineBox().getTopPadding();
            }

            if (cell.getStyle().getLineBox().getBottomPadding() != null)
            {
                padding.bottom = cell.getStyle().getLineBox().getBottomPadding();
            }
        }

        if (cellBounds == null) return; // the matrix maybe not ready yet...
        int cW = cellBounds.width - padding.left - padding.right;
        int cH = cellBounds.height - padding.top - padding.bottom;

        List children = cell.getChildren();
        int y = padding.top;
        int x = padding.left;
        if (children.size() == 0) return;
        int h = cH/children.size();
        int wd = cW/children.size();

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
                if (type == VERTICAL_LAYOUT)
                {
                    listener.getNewSizes().put(element, new Rectangle(0, i*h, cW, h));
                }
                else if (type == HORIZONTAL_LAYOUT)
                {
                    listener.getNewSizes().put(element, new Rectangle(i*wd, 0, wd, cH));
                }
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
                p.x += rectangle.x - element.getX();
                p.y += rectangle.y - element.getY();

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
                                Map attributes = JRFontUtil.getAttributes(new HashMap(), dte, Locale.getDefault());

                                JRStyledText styledText =
                                    styledTextParser.getStyledText(
                                    attributes,
                                    text,
                                    JRCommonText.MARKUP_STYLED_TEXT.equals(dte.getMarkup()),//FIXMEMARKUP only static styled text appears on preview. no other markup
                                    Locale.getDefault()
                                    );


                                JRTextMeasurer measurer = JRTextMeasurerUtil.getInstance(DefaultJasperReportsContext.getInstance()).createTextMeasurer(dte);
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
