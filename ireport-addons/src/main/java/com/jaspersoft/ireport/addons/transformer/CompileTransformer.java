/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.transformer;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import java.io.File;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: CompileTransformer.java 0 2010-05-31 19:47:22 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class CompileTransformer implements Transformer {

    private CompileTransformerOptions optionsPanel = null;

    public JasperDesign transform(String srcFileName) throws TransformException {

        String jasperFileName = Misc.changeFileExtension(srcFileName,"jasper");
        if ( ((CompileTransformerOptions)getComponent()).isDefaultDirectory())
        {
            File f = new File(jasperFileName);
              if (!IReportManager.getPreferences().getBoolean("useReportDirectoryToCompile", true))
              {
                 jasperFileName = IReportManager.getPreferences().get("reportDirectoryToCompile", ".");
                 if (!jasperFileName.endsWith(File.separator))
                 {
                        jasperFileName += File.separator;
                 }
                 jasperFileName += f.getName();
              }
        }
        
        if (((CompileTransformerOptions)getComponent()).isBackupJasper())
        {
            File oldJasper = new File(jasperFileName);
            if (oldJasper.exists())
            {
                oldJasper.renameTo(new File( Misc.changeFileExtension(srcFileName,"jrxml.bak")) );
            }
        }
        try {
            // Compile the report...
            JasperCompileManager.compileReportToFile(srcFileName, jasperFileName);
        } catch (JRException ex) {
            throw new TransformException(ex);
        }

        return null;
    }

    public String getName() {
        return "Compile files";
    }

    public Component getComponent() {
        if (optionsPanel == null)
        {
            optionsPanel = new CompileTransformerOptions();
        }
        return optionsPanel;
    }


}
