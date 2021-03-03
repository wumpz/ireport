/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.callouts;



import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.palette.PaletteItemAction;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class CreateCalloutAction extends PaletteItemAction  {

    @Override
    public void drop(DropTargetDropEvent dtde) {

            Point p = getScene().convertViewToScene( dtde.getLocation() );

            if (!(getScene() instanceof ReportObjectScene))
            {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        JOptionPane.showMessageDialog(Misc.getMainFrame(), "Notes can be added only to the main report designer");
                    }
                });

                return;

            }

            CalloutsLayer layer = CalloutsUtility.getCalloutsLayer((AbstractReportObjectScene)getScene(), true);

            Widget w = CalloutsUtility.createCalloutWidget((AbstractReportObjectScene)getScene());
            w.setPreferredLocation(p);
            layer.addChild(w);
            getScene().validate();
    }
}
