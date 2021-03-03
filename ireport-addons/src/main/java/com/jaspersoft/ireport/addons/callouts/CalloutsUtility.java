/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.callouts;

import com.jaspersoft.ireport.addons.ReportOpenedListener;
import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;

/**
 *
 * @version $Id: CalloutsUtility.java 0 2010-01-14 14:45:33 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class CalloutsUtility {

    static public CalloutsLayer getCalloutsLayer(AbstractReportObjectScene scene, boolean create)
    {
        // Find the backgound image layer...
        CalloutsLayer layer = null;

        Iterator<Widget> it = scene.getChildren().iterator();
        while (it.hasNext())
        {
            Widget w = it.next();
            if (w instanceof CalloutsLayer)
            {
                layer = (CalloutsLayer) w;
                break;
            }
        }

        if (layer == null && create)
        {
            layer = new CalloutsLayer(scene);
            scene.addChild(layer);
            scene.validate();
        }

        return layer;
    }

    static public Widget createCalloutWidget(AbstractReportObjectScene abstractReportObjectScene) {

        CalloutWidget w = new CalloutWidget(abstractReportObjectScene);
        return w;
    }

    static public void saveCallouts(AbstractReportObjectScene scene)
    {
        JasperDesign jd = scene.getJasperDesign();

        CalloutsLayer layer = getCalloutsLayer(scene, false);
        if (layer == null) return;

        java.util.Properties props = new java.util.Properties();
        List<Widget> callouts = layer.getChildren();
        int i=0;
        for (Widget w : callouts)
        {
            if (w instanceof CalloutWidget)
            {
                i++;
                CalloutWidget cw = (CalloutWidget)w;
                Rectangle bounds = w.getPreferredBounds();
                if (bounds == null) bounds = w.getPreferredBounds();
                if (bounds != null)
                {
                    props.put("callouts." + i + ".bounds",  w.getLocation().x + "," +w.getLocation().y + "," + bounds.width + "," + bounds.height );
                    props.put("callouts." + i + ".text",  cw.getText());

                    // Find connections of this callouts...
                    String connectionsString = "";
                    List<Widget> connections = layer.getChildren();
                    for (Widget conn : connections)
                    {
                        if (conn instanceof ConnectionWidget)
                        {
                            ConnectionWidget connW = (ConnectionWidget)conn;
                            if (connW.getSourceAnchor().getRelatedWidget() == cw)
                            {
                                Widget pin = connW.getTargetAnchor().getRelatedWidget();
                                if (pin != null)
                                connectionsString += pin.getLocation().x + "," + pin.getLocation().y+";";
                            }
                        }
                    }
                    if (connectionsString.length() > 0)
                    {
                        props.put("callouts." + i + ".pins",  connectionsString);
                    }

                }
            }
        }


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            props.store( out, "");
            String s = out.toString();

            // XML ignores \n in attributes, so we encode \n in our string.
            // a \n before the string callouts. must be reconverted on loading.

            s = Misc.string_replace("", "\r", s);
            s = Misc.string_replace("\\ncallouts.", "\ncallouts.", s);
            s = Misc.string_replace("", "\n", s);
           
            jd.setProperty("ireport.callouts", s);
            ReportOpenedListener.getDefaultInstance().getViewForJasperDesign(jd).getEditorSupport().notifyModified();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    static public void loadCallouts(AbstractReportObjectScene scene)
    {
        JasperDesign jd = scene.getJasperDesign();
        String s = jd.getProperty("ireport.callouts");

        // Restore new lines for properties.
        s = Misc.string_replace("\ncallouts.", "\\ncallouts.", s);
        
        
        CalloutsLayer layer = null;
        if (s != null)
        {
            Properties props = new Properties();
            try {
                props.load(new ByteArrayInputStream(s.getBytes()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            int i=1;

            while ( props.containsKey("callouts."+i+".text"))
            {
                String text = props.getProperty("callouts."+i+".text");
                String bounds = props.getProperty("callouts."+i+".bounds");
                String pins = props.getProperty("callouts."+i+".pins");
                
                i++;
                
                CalloutWidget w = new CalloutWidget(scene);
                w.setText(text);
                String[] vals = bounds.split(",");
                if (vals.length == 4)
                {
                    try {
                        w.setPreferredLocation(new Point(
                                    Integer.valueOf(vals[0].trim()),
                                    Integer.valueOf(vals[1].trim())));
                        Rectangle r = new Rectangle(0,0,
                                    Integer.valueOf(vals[2].trim()),
                                    Integer.valueOf(vals[3].trim())
                                );
                        w.setPreferredBounds(r);
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

                if (layer == null)
                {
                    // Remove all children
                    layer = getCalloutsLayer(scene, true);
                    layer.removeChildren();
                    scene.validate();
                }
                layer.addChild(w);
                scene.validate();
            
                if (pins != null)
                {
                    String[] pinsDefs = pins.split(";");
                    for (String pinDef : pinsDefs)
                    {
                        try {
                            String[] pinCoords = pinDef.split(",");
                            w.createPinConnection(new Point(Integer.valueOf(pinCoords[0].trim()),Integer.valueOf(pinCoords[1].trim())));
                        } catch (Exception ex) { ex.printStackTrace(); }
                    }
                }
                

            }

        }
    }


}
