/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.background;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.ReportDesignerPanel;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import java.util.List;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.Presenter;

/**
 *
 * @version $Id: BackgroundTransformAction.java 0 2010-01-13 16:32:41 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class TransformBackgroundImageAction extends CallableSystemAction
        implements Presenter.Menu, Presenter.Popup, LookupListener {


    private final Lookup lkp;
    private final Lookup.Result <? extends ReportDesignerPanel> result;
    JCheckBoxMenuItem menu = null;

    public TransformBackgroundImageAction()
    {
        this.lkp = Utilities.actionsGlobalContext();
        result = lkp.lookupResult(ReportDesignerPanel.class);
        result.addLookupListener(this);
        result.allItems();
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return getMenu();
    }

    @Override
    public JMenuItem getPopupPresenter() {
        return getMenu();
    }

    @Override
    public void performAction() {

        SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                        JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();

                        ReportObjectScene scene = view.getReportDesignerPanel().getScene();

                        // Find the backgound image layer...
                        BackgroundImageLayer layer = BackgroundImageUtilities.getBackgroundImageLayer(view, false);


                        JasperDesign jd = view.getModel().getJasperDesign();

                        if (layer != null)
                        {

                            // Check if I'm transforming the background image...
                            List<Widget> widgets = layer.getChildren();
                            for (Widget w : widgets)
                            {
                                if (w instanceof BackgroundImageWidget)
                                {
                                    ((BackgroundImageWidget)w).setTransforming(getMenu().isSelected());
                                    break;
                                }
                            }
                        }
                    }
                });

    }

    protected boolean enable() {

        // Update status...
        getMenu().setSelected(false);
        getMenu().setEnabled(false);

        if (IReportManager.getInstance().getActiveVisualView() != null)
        {
            BackgroundImageLayer layer = BackgroundImageUtilities.getBackgroundImageLayer(IReportManager.getInstance().getActiveVisualView(), false);

            // Check if I'm transforming the background image...
            boolean b = false;
            if (layer != null && layer.getChildren().size() > 0)
            {
               List<Widget> widgets = layer.getChildren();
                for (Widget w : widgets)
                {
                    if (w instanceof BackgroundImageWidget)
                    {
                        getMenu().setEnabled(((BackgroundImageWidget)w).isVisible());
                        b = ((BackgroundImageWidget)w).isTransforming();
                        break;
                    }
                }
            }
            getMenu().setSelected(b);
            getMenu().updateUI();
            return getMenu().isEnabled();
        }
        return false;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(ShowBackgroundImageAction.class, "CTL_TransformBackgroundImageAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected JCheckBoxMenuItem getMenu()
    {
        if (menu == null)
        {
            menu = new JCheckBoxMenuItem(getName());
            menu.addActionListener(this);
            menu.setSelected(false);
            menu.setEnabled(false);
        }

        return menu;
    }

    public void resultChanged(LookupEvent le) {
        enable();
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

}

