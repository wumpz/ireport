/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.addons.background;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.utils.Misc;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class BackgroundImageAction extends NodeAction {


    protected void performAction(Node[] activatedNodes) {

                javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( IReportManager.getInstance().getCurrentDirectory());

                jfc.setDialogTitle(NbBundle.getMessage(BackgroundImageAction.class, "select.image.file"));

                jfc.addChoosableFileFilter( new javax.swing.filechooser.FileFilter() {
                        public boolean accept(java.io.File file) {
                                String filename = file.getName();
                                return (filename.toLowerCase().endsWith(".jpg") || file.isDirectory() ||
                                        filename.toLowerCase().endsWith(".png") ||
                                        filename.toLowerCase().endsWith(".gif"));
                        }
                        public String getDescription() {
                                return "Image file (.jpg,.png,.gif)";
                        }
                });

	    jfc.setMultiSelectionEnabled(false);
	    jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
	    if  (jfc.showOpenDialog( Misc.getMainFrame()) == javax.swing.JOptionPane.OK_OPTION) {
		    final java.io.File file = jfc.getSelectedFile();
                    try {

                        SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                            JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();

                            ReportObjectScene scene = view.getReportDesignerPanel().getScene();

                            // Find the backgound image layer...
                            BackgroundImageLayer layer = BackgroundImageUtilities.getBackgroundImageLayer(view, true);

                            layer.removeChildren();

                            JasperDesign jd = view.getModel().getJasperDesign();
                            jd.removeProperty("ireport.background.image");
                            jd.removeProperty("ireport.background.image.properties");
                            IReportManager.getInstance().notifyReportChange();


                            try {
                                layer.addChild( BackgroundImageUtilities.createImageWidget(scene, file));
                                jd.setProperty("ireport.background.image", file.getPath());
                                scene.validate();
                                scene.revalidate(true);

                            } catch (Exception ex)
                            {
                                JOptionPane.showMessageDialog(Misc.getMainFrame(), "The image format is not supported.", "Error" ,JOptionPane.ERROR_MESSAGE);
                            }
                            
                            BackgroundImageUtilities.notifyBackgroundOptionsChange();
                        }
                    });
                            

                } catch (Exception ex){}
	    }

    }

    public String getName() {
        return NbBundle.getMessage(BackgroundImageAction.class, "CTL_BackgroundImageAction");
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
        return (IReportManager.getInstance().getActiveReport() != null && IReportManager.getInstance().getActiveVisualView() != null);
    }
}

