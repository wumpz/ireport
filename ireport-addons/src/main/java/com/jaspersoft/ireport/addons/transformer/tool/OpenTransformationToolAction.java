/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.transformer.tool;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.event.ActionEvent;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class OpenTransformationToolAction extends CallableSystemAction {

    TransformationFrame transformationFrame = null;

    public void actionPerformed(ActionEvent e) {
       if (transformationFrame == null)
       {
           transformationFrame = new TransformationFrame(Misc.getMainFrame());
       }

       transformationFrame.setVisible(true);
    }

    @Override
    public void performAction() {

    }

    @Override
    public String getName() {
         return NbBundle.getMessage(OpenTransformationToolAction.class, "CTL_OpenTransformationToolAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
