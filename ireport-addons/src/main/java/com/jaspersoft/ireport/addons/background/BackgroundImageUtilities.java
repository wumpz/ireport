/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.background;

import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import java.awt.MediaTracker;
import java.io.File;
import java.util.Iterator;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.actions.SystemAction;

/**
 *
 * @version $Id: BackgroundImageUtilities.java 0 2010-01-12 16:45:51 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class BackgroundImageUtilities {

    public static void notifyBackgroundOptionsChange()
    {
        SystemAction.get(ShowBackgroundImageAction.class).resultChanged(null);
        SystemAction.get(TransformBackgroundImageAction.class).resultChanged(null);
    }


    public static BackgroundImageLayer getBackgroundImageLayer(JrxmlVisualView view, boolean create)
    {
        ReportObjectScene scene = view.getReportDesignerPanel().getScene();

        // Find the backgound image layer...
        BackgroundImageLayer layer = null;

        Iterator<Widget> it = scene.getChildren().iterator();
        while (it.hasNext())
        {
            Widget w = it.next();
            if (w instanceof BackgroundImageLayer)
            {
                layer = (BackgroundImageLayer) w;
                break;
            }
        }

        if (layer == null && create)
        {
            int index = scene.getChildren().indexOf( scene.getPageLayer()  );
            layer = new BackgroundImageLayer(scene);
            scene.addChild(index+1, layer);
            scene.validate();
        }

        return layer;
    }

    public static BackgroundImageWidget getBackgroundImageWidget(JrxmlVisualView view)
    {
        // Find the backgound image layer...
        BackgroundImageLayer layer = getBackgroundImageLayer(view, false);
        if (layer == null) return null;
        Iterator<Widget> it = layer.getChildren().iterator();
        while (it.hasNext())
        {
            Widget w = it.next();
            if (w instanceof BackgroundImageWidget)
            {
                return (BackgroundImageWidget) w;
            }
        }
        return null;
    }


    public static BackgroundImageWidget createImageWidget(Scene scene, File file) throws Exception
    {
        BackgroundImageWidget img = new BackgroundImageWidget(scene, new javax.swing.ImageIcon(file.getPath()) );
        if (img.getImg().getImageLoadStatus() == MediaTracker.ERRORED)
        {
            throw new Exception("Error loading image");
        }
        return img;
    }

    public static void restore(final JrxmlVisualView view) {

        JasperDesign jd = view.getEditorSupport().getCurrentModel();

        if (jd.getProperty("ireport.background.image") != null)
            {
                String s = jd.getProperty("ireport.background.image");
                final File file = new File(s);
                if (file.exists())
                {
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {


                                    try {
                                    ReportObjectScene scene = view.getReportDesignerPanel().getScene();
                                    // Find the backgound image layer...
                                    BackgroundImageLayer layer = BackgroundImageUtilities.getBackgroundImageLayer(view, true);

                                    layer.removeChildren();

                                    layer.addChild( BackgroundImageUtilities.createImageWidget(scene, file));
                                    scene.validate();
                                    scene.revalidate(true);

                                    BackgroundImageUtilities.notifyBackgroundOptionsChange();

                                    } catch (Exception ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                }
                        });
                }
            }
    }
}
