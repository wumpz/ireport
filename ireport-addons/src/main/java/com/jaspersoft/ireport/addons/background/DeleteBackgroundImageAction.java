/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.background;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;
import org.openide.util.actions.SystemAction;

/**
 *
 * @version $Id: ShowBackgroundImageAction.java 0 2010-01-12 16:48:47 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class DeleteBackgroundImageAction extends NodeAction {


    protected void performAction(Node[] activatedNodes) {

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                        JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();

                        ReportObjectScene scene = view.getReportDesignerPanel().getScene();

                        // Find the backgound image layer...
                        BackgroundImageLayer layer = BackgroundImageUtilities.getBackgroundImageLayer(view, false);

                        JasperDesign jd = view.getModel().getJasperDesign();
                        jd.removeProperty("ireport.background.image");
                        jd.removeProperty("ireport.background.image.properties");

                        if (layer != null)
                        {
                            layer.removeChildren();
                            scene.validate();
                            scene.revalidate(true);
                        }
                        (SystemAction.get(ShowBackgroundImageAction.class)).resultChanged(null);
                        firePropertyChange(NodeAction.PROP_ENABLED,true ,false);
                        IReportManager.getInstance().notifyReportChange();

                        BackgroundImageUtilities.notifyBackgroundOptionsChange();
                        setEnabled(false);
                    }
                });
    }

    public String getName() {
        return NbBundle.getMessage(BackgroundImageAction.class, "CTL_DeleteBackgroundImageAction");
    }


    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected boolean enable(Node[] nodes) {

        if (IReportManager.getInstance().getActiveReport() != null &&
                IReportManager.getInstance().getActiveVisualView() != null)
        {

            if (IReportManager.getInstance().getActiveReport().getProperty("ireport.background.image") != null)
            {
                return true;
            }
        }
        return false;
    }


}

