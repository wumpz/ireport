/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.transformer;

import com.jaspersoft.ireport.designer.ElementDecorator;
import net.sf.jasperreports.engine.design.JRDesignImage;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;

/**
 *
 * @version $Id: ElementDecorator.java 0 2010-04-14 17:56:55 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class BarcodeTransformerElementDecorator implements ElementDecorator {


    public void paintWidget(Widget w) {
    }

    public SystemAction[] getActions(Node node) {
        return new SystemAction[]{ SystemAction.get(TransformToBarcodeComponentAction.class) };
    }

    public boolean appliesTo(Object designElement) {
        return designElement instanceof JRDesignImage;
    }

}
