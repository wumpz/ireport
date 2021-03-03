/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.background;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.ResizeStrategy;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.actions.SystemAction;

/**
 *
 * @version $Id: BackgroundImageWidget.java 0 2010-01-13 17:04:49 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class BackgroundImageWidget extends Widget {

    private WidgetAction resizeAction = null;
    private WidgetAction moveAction = null;
    private WidgetAction popupMenuAction = null;

    private boolean transforming = false;
    private boolean keepRatio = true;
    private float  transparency = 0.25f;

    private ImageIcon img = null;

    private JPopupMenu popupMenu = null;

    public BackgroundImageWidget(Scene scene, ImageIcon img)
    {
        super(scene);
        setBorder(BorderFactory.createEmptyBorder(8));
        resizeAction = ActionFactory.createResizeAction(new ResizeStrategy () {
            public Rectangle boundsSuggested (Widget widget, Rectangle originalBounds, Rectangle suggestedBounds, ResizeProvider.ControlPoint controlPoint) {
                
                if (isKeepRatio() && getImg() != null)
                {
                    double ratioH = (double)getImg().getIconWidth()/(double)getImg().getIconHeight();
                    double ratioW = (double)getImg().getIconHeight()/(double)getImg().getIconWidth();
                    // Check where we moved...
                    if (originalBounds.width-suggestedBounds.width != 0)
                    {
                        suggestedBounds.height = (int)(suggestedBounds.width*ratioW);
                    }
                    else
                    {
                        suggestedBounds.width = (int)(suggestedBounds.height*ratioH);
                    }
                    
                }
                
                return suggestedBounds;
            }
        }, ActionFactory.createDefaultResizeProvider());
        moveAction = ActionFactory.createMoveAction();
        popupMenuAction = ActionFactory.createPopupMenuAction (new PopupMenuProvider() {
            public JPopupMenu getPopupMenu (Widget widget, Point localLocation) {
                return ((BackgroundImageWidget)widget).getPopupMenu();
            }
        });
        setImg(img);
        setPreferredLocation(new Point(-8,-8));

        loadState();

        this.addDependency(new Widget.Dependency() {

            public void revalidateDependency() {
                saveState();
            }
        });
    }

    private JPopupMenu getPopupMenu()
    {
        if (popupMenu == null)
        {
            popupMenu = new JPopupMenu();
            JMenuItem fitPageWidth = new JMenuItem("Fit page width");
            fitPageWidth.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    int w = ((AbstractReportObjectScene)getScene()).getJasperDesign().getPageWidth();

                    if (w == 0) return;

                    int h = (int)(img.getIconHeight() * ((double)w/(double)img.getIconWidth()));

                    setPreferredBounds(new Rectangle(0,0,w+16,h+16));
                    setPreferredLocation(new Point(-8,-8));
                    revalidate();
                    getScene ().validate ();
                }
            });
            
            popupMenu.add(fitPageWidth);

            JMenu transparencyMenu = new JMenu("Transparency");
            

            createTransparencyMenu(transparencyMenu, new float[]{0.05f, 0.10f,0.15f,0.20f,0.25f,0.30f,0.40f,0.5f,0.75f,1.0f});

            popupMenu.add(transparencyMenu);

            final JCheckBoxMenuItem cmi = new JCheckBoxMenuItem("Keep ratio");
            cmi.setSelected(isKeepRatio());
            cmi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setKeepRatio(cmi.isSelected());
                }
            });


            popupMenu.add(cmi);
            popupMenu.add(new JSeparator());

            JMenuItem endChanges = new JMenuItem("End Transformation");
            endChanges.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    SystemAction.get(TransformBackgroundImageAction.class).getMenu().setSelected(false);
                    SystemAction.get(TransformBackgroundImageAction.class).actionPerformed(null);
                }
            });

            popupMenu.add(endChanges);
        }

        return popupMenu;
    }


    private void createTransparencyMenu(JMenu menu, float[] values)
    {
        final ButtonGroup bg = new ButtonGroup();
        ActionListener alTransparency = new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    float f = Float.valueOf(bg.getSelection().getActionCommand());
                    setTransparency(f);
                }
            };

        for (int i=0; i<values.length; ++i)
        {
            JRadioButtonMenuItem mi = new JRadioButtonMenuItem("" + (int)(values[i]*100)+"%");
            mi.addActionListener(alTransparency);
            mi.setActionCommand(values[i]+"");
            if (values[i] == getTransparency())
            {
                mi.setSelected(true);
            }
            menu.add(mi);

            bg.add(mi);
        }
    }

    /**
     * @return the transforming
     */
    public boolean isTransforming() {
        return transforming;
    }

    /**
     * @param transforming the transforming to set
     */
    public void setTransforming(boolean transforming) {

        if (transforming == this.transforming) return;
        this.transforming = transforming;
        
        if (transforming)
        {
            getActions().addAction(resizeAction);
            getActions().addAction(moveAction);
            getActions().addAction(popupMenuAction);

            setBorder(BorderFactory.createResizeBorder(8, Color.BLACK, true));
            setPreferredSize(new Dimension(img.getIconWidth()+16, img.getIconHeight()+16));
        }
        else
        {
            getActions().removeAction(resizeAction);
            getActions().removeAction(moveAction);
            getActions().removeAction(popupMenuAction);
            setBorder(BorderFactory.createEmptyBorder(8));
            setPreferredSize(new Dimension(img.getIconWidth()+16, img.getIconHeight()+16));
        }

        revalidate();
        getScene ().validate ();
    }

    /**
     * @return the img
     */
    public ImageIcon getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(ImageIcon img) {
        this.img = img;
        setPreferredBounds(new Rectangle(0,0,img.getIconWidth()+16, img.getIconHeight()+16));
    }

    @Override
    protected void paintWidget() {
        if (img == null)
        {
            super.paintWidget();
        }
        else
        {
            Insets is = getBorder().getInsets();
            int x = getBounds().x+is.right;
            int y = getBounds().y+is.top;
            int w = getBounds().width - is.right - is.left;
            int h = getBounds().height - is.top - is.bottom;

            Composite oldComposite = getGraphics().getComposite();
            getGraphics().setComposite(AlphaComposite.getInstance( AlphaComposite.SRC_OVER , getTransparency()) );
            getGraphics().drawImage(img.getImage(), x,y,w,h, null);
            //getGraphics().drawString("" + getLocation(), x+10, y+20);

            getGraphics().setComposite(oldComposite);
        }
    }

    /**
     * @return the transparency
     */
    public float getTransparency() {
        return transparency;
    }

    /**
     * @param transparency the transparency to set
     */
    public void setTransparency(float transparency) {
        this.transparency = transparency;
        repaint();
        saveState();
    }

    /**
     * @return the keepRatio
     */
    public boolean isKeepRatio() {
        return keepRatio;
    }

    /**
     * @param keepRatio the keepRatio to set
     */
    public void setKeepRatio(boolean keepRatio) {
        if (this.keepRatio == keepRatio) return;
        this.keepRatio = keepRatio;

        if (keepRatio == true && getImg() != null)
        {
            double ratioH = (double)getImg().getIconWidth()/(double)getImg().getIconHeight();
            double ratioW = (double)getImg().getIconHeight()/(double)getImg().getIconWidth();

            Rectangle r = getBounds();
            if ((double)getBounds().width/(double)getBounds().height > ratioH )
            {
                r.height = (int)(getBounds().width*ratioW);
            }
            else
            {
                r.width = (int)(getBounds().height*ratioH);
            }
            setPreferredBounds(r);
            getScene().validate();
        }
        saveState();
    }

    public void setImageVisible(boolean b)
    {
        super.setVisible(b);
        if (b == false)
        {
            setTransforming(false);
        }
    }

    public void saveState()
    {
        if (getLocation() == null || getBounds() == null) return;
        String bounds = isVisible() + "," +  isKeepRatio() + "," + getTransparency() + "," + getLocation().x + "," + getLocation().y + "," + getBounds().x + "," + getBounds().y + "," + getBounds().width + "," + getBounds().height;
        ((AbstractReportObjectScene)getScene()).getJasperDesign().setProperty("ireport.background.image.properties", bounds);
    }

    public void loadState()
    {
        String propString = ((AbstractReportObjectScene)getScene()).getJasperDesign().getProperty("ireport.background.image.properties");
        if (propString != null)
        {
            String[] props = propString.split(",");
            int index = 0;
            setVisible(Boolean.valueOf(props[index]));
            setKeepRatio(  Boolean.valueOf(props[++index]) );
            setTransparency( Float.valueOf(props[++index]));
            Point location = new Point( Integer.valueOf(props[++index]), Integer.valueOf(props[++index]));
            Rectangle bounds = new Rectangle( Integer.valueOf(props[++index]), Integer.valueOf(props[++index]),Integer.valueOf(props[++index]), Integer.valueOf(props[++index]));

            setPreferredLocation(location);
            setPreferredBounds(bounds);

            revalidate();
            getScene().validate();
        }


    }

}
