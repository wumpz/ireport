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
 * @version $Id: ShowBackgroundImageAction.java 0 2010-01-12 16:48:47 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ShowBackgroundImageAction extends CallableSystemAction
        implements Presenter.Menu, Presenter.Popup, LookupListener {


    private final Lookup lkp;
    private final Lookup.Result <? extends ReportDesignerPanel> result;
    JCheckBoxMenuItem menu = null;

    public ShowBackgroundImageAction()
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
                    BackgroundImageWidget w = BackgroundImageUtilities.getBackgroundImageWidget(view);

                    if (w != null)
                    {
                        w.setImageVisible( getMenu().isSelected());
                    }

                    BackgroundImageUtilities.notifyBackgroundOptionsChange();
                }
            });

    }

    protected boolean enable() {

        // Update status...
        getMenu().setSelected(false);
        getMenu().setEnabled(false);

        if (IReportManager.getInstance().getActiveVisualView() != null)
        {
            BackgroundImageWidget w = BackgroundImageUtilities.getBackgroundImageWidget(IReportManager.getInstance().getActiveVisualView());
            getMenu().setEnabled(w != null);
            getMenu().setSelected(w != null && w.isVisible());
        }
        
        return getMenu().isEnabled();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(ShowBackgroundImageAction.class, "action.showBackgroundImage");
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
            menu.setSelected(true);
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
