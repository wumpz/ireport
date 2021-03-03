/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.layers;

import com.jaspersoft.ireport.designer.ElementDecorator;
import net.sf.jasperreports.engine.design.JRDesignElement;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;

/**
 *
 * @version $Id: LayerElementDecorator.java 0 2010-02-27 10:30:41 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class LayerElementDecorator implements ElementDecorator {

    public void paintWidget(Widget w) {
    }

    public SystemAction[] getActions(Node node) {
        return new SystemAction[]{ SystemAction.get(LayersAction.class) };
    }

    public boolean appliesTo(Object designElement) {
        return designElement instanceof JRDesignElement;
    }

}
