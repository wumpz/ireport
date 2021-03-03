/*
 * Copyright (C) 2005 - 2008 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * FindThread.java
 * 
 * Created on 19 maggio 2004, 19.52
 *
 */

package com.jaspersoft.ireport.addons.transformer.tool;

import javax.swing.table.*;
import java.io.*;
/**
 *
 * @author  Administrator
 */
public class FindThread implements Runnable {
    
    private TransformationFrame massiveCompilerFrame = null;
    private boolean stop = false;
    private Thread thread = null;
    
    public FindThread(TransformationFrame mcf)
    {
        this.massiveCompilerFrame = mcf;
        thread = new Thread(this);
    }
    
    public void stop()
    {
        stop = true;
    }
    
    public void start()
    {
        thread.start();
    }
    
    public void run() {
        if (massiveCompilerFrame == null)
        {
            return;
        }
        
        // Prepare the file search....
        
        DefaultTableModel dtm = (DefaultTableModel)massiveCompilerFrame.getFileTable().getModel();
        
        dtm.setRowCount(0);
        massiveCompilerFrame.getFileTable().updateUI();
        
        // Path
        File path = new File(massiveCompilerFrame.getFindDirectory());
        
        if (path == null || !path.exists() || path.isFile())
        {
            // Invalid conditions to search... 
            return;
        }
        
        if (!stop)  findFiles(path, massiveCompilerFrame.isSearchSubDirectory(),dtm);
        
        massiveCompilerFrame.finishedFind();
        return;
    }
    
    private int findFiles(File path, boolean recursive, DefaultTableModel tmodel)
    {
        if (stop) return 0;
        int count = 0;
        File[] files = path.listFiles();
        for (int i=0; i<files.length; ++i)
        {
            if (stop) return 0;
            if (files[i].isDirectory() && recursive)
            {
                count += findFiles( files[i], recursive,tmodel);
            }
            else
            {
                // Is the file a JasperReports source?
                if (files[i].getName().toLowerCase().endsWith(".xml") ||
                    files[i].getName().toLowerCase().endsWith(".jrxml"))
                {
                    // Ok, for me is a good file, get it !
                    FileEntry fe = new FileEntry();
                    fe.setFile( files[i] );
                    
                    // Looking for compiled and compilation version...
                    
                    
                    // ....
                    fe.setStatus( fe.STATUS_NOT_TRANSFORMED );
                    tmodel.addRow( new Object[]{fe,fe,fe.decodeStatus(fe.getStatus())});
                    
                }
            }
        }
        
        return count;
    }
    
}
