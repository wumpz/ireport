/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.spiderchart;

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.tools.HyperlinkPanel;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.components.spiderchart.SpiderChartComponent;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

/**
 *
 * @version $Id: SpiderChartHyperlinkAction.java 0 2010-07-16 16:58:36 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class SpiderChartHyperlinkAction extends NodeAction {

    public String getName() {
        return I18n.getString("HyperlinkAction.Property.Hyperlink");
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

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {

        ElementNode node = (ElementNode)activatedNodes[0];
        if (node.getElement() instanceof JRDesignComponentElement &&
            ((JRDesignComponentElement)node.getElement()).getComponent() instanceof SpiderChartComponent)
        {
            SpiderChartComponent component = (SpiderChartComponent) ((JRDesignComponentElement)node.getElement()).getComponent();

            if (component.getChartSettings() == null)
            {
                component.setChartSettings(new StandardChartSettings());
            }

            JRHyperlink hyperlink = (JRHyperlink)component.getChartSettings();
            JasperDesign design = ((ElementNode)activatedNodes[0]).getJasperDesign();

            HyperlinkPanel pd = new HyperlinkPanel();
            pd.setExpressionContext(new ExpressionContext( ModelUtils.getElementDataset(((ElementNode)activatedNodes[0]).getElement(), design)) );
            pd.setHyperlink(hyperlink);
            pd.showDialog( Misc.getMainFrame() );
        }

    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        if (!(activatedNodes[0] instanceof ElementNode)) return false;
        ElementNode node = (ElementNode)activatedNodes[0];
        if (node.getElement() instanceof JRDesignComponentElement &&
            ((JRDesignComponentElement)node.getElement()).getComponent() instanceof SpiderChartComponent)
        {
            return true;
        }
        return false;
    }
}

