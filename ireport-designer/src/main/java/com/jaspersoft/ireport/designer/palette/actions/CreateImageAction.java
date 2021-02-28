/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of iReport.
 *
 * iReport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * iReport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with iReport. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.ireport.designer.palette.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Mutex;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class CreateImageAction extends CreateReportElementAction 
{

    public JRDesignElement createReportElement(JasperDesign jd)
    {
        File parent = new File(IReportManager.getInstance().getCurrentDirectory());
        // Try to figure it out the current directory of the report...
        if (IReportManager.getInstance().getActiveVisualView() != null)
        {
            JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
            FileObject obj = view.getEditorSupport().getDataObject().getPrimaryFile();
            File f = FileUtil.toFile(obj);
            if (f != null && f.getParentFile().exists())
            {
                parent = f.getParentFile();
            }
        }
        final javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( parent  );
        jfc.setDialogTitle("Select an image file....");
        jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File file) {
                String filename = file.getName();
                return (filename.endsWith(".jpg") ||
                        filename.endsWith(".jpeg") ||
                        filename.endsWith(".gif") ||
                        file.isDirectory()) ;
            }
            public String getDescription() {
                return "Image *.gif|*.jpg";
            }
        });
        
        jfc.setMultiSelectionEnabled(false);

        final JRDesignImage element = new JRDesignImage(jd);
        element.setWidth(100);
        element.setHeight(50);

        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( null) == javax.swing.JOptionPane.OK_OPTION) {
            element.setExpression( Misc.createExpression("java.lang.String", "\""+ Misc.string_replace("\\\\","\\",jfc.getSelectedFile().getPath() +"\"")));
            IReportManager.getInstance().setCurrentDirectory(jfc.getSelectedFile().getParent(), true);
            // Try to identify the image size...

            SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        try {
                            ImageIcon image = new ImageIcon(jfc.getSelectedFile().getPath());
                            if (image.getIconWidth() > 0)
                            {
                                element.setWidth( image.getIconWidth());
                            }
                            if (image.getIconHeight() > 0)
                            {
                                element.setHeight( image.getIconHeight());
                            }
                            
                        } catch (Exception ex)
                        {

                        }
                    }
                });

        }

        return element;
    }
    
}
