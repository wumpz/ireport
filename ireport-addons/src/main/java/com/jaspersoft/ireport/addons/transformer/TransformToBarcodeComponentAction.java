/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.addons.transformer;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignImage;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class TransformToBarcodeComponentAction extends NodeAction {

    protected void performAction(Node[] activatedNodes) {

        List<JRDesignElement> selectedElements = new ArrayList<JRDesignElement>();
        List<JRDesignElement> selection = IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel().getActiveScene().getSelectionManager().getSelectedElements();
        JRDesignElement primarySelected = (selection.size() > 0) ? selection.get(0) : null;


        for (Node node : activatedNodes)
        {
            JRDesignImage element = (JRDesignImage)((ElementNode)node).getElement();

            JRDesignElement newElement = new BarcodeTransformer().transformImageToBarcode(element);

            JRElementGroup container = element.getElementGroup();
            if (container instanceof JRDesignElementGroup)
            {
                int index = ((JRDesignElementGroup)container).getChildren().indexOf(element);
                ((JRDesignElementGroup)container).getChildren().add(index, newElement);
                newElement.setElementGroup(container);
                ((JRDesignElementGroup)container).removeElement(element);
            }
            else if (container instanceof JRDesignFrame)
            {
                int index = ((JRDesignFrame)container).getChildren().indexOf(element);
                ((JRDesignFrame)container).getChildren().set(index, newElement);
                newElement.setElementGroup(container);
                ((JRDesignFrame)container).removeElement(element);
                //((JRDesignFrame)container).getEventSupport().firePropertyChange(JRDesignElementGroup.PROPERTY_CHILDREN, null, null);
            }

            selectedElements.add(newElement);
            if (primarySelected == element)
            {
                primarySelected = newElement;
            }
            else
            {
                selectedElements.add(element);
            }
        }

        if (selectedElements.size() > 0)
        {
            boolean first = true;

            if (primarySelected != null && selectedElements.contains(primarySelected))
            {
                selectedElements.remove(primarySelected);
                selectedElements.add(0, primarySelected);
            }

            for (JRDesignElement element : selectedElements)
            {

                if (first) IReportManager.getInstance().setSelectedObject(element);
                else IReportManager.getInstance().addSelectedObject(element);
                first = false;
            }
        }
    }


    public String getName() {
        return NbBundle.getMessage(TransformToBarcodeComponentAction.class, "CTL_TransformToBarcodeComponentAction");
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
    protected boolean enable(Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        for (Node node : activatedNodes)
        {
            if (! (node instanceof ElementNode)) return false;
            JRDesignElement element = ((ElementNode)node).getElement();
            if (!(element instanceof JRDesignImage)) return false;
            if (!Misc.getExpressionText( ((JRDesignImage)element).getExpression() ).startsWith("it.businesslogic.ireport.barcode.BcImage.getBarcodeImage(")) return false;
        }

        return true;
    }

    
}

