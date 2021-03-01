/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.ireport.jasperserver.ui.actions;

import com.jaspersoft.ireport.designer.utils.Misc;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.openide.util.Exceptions;

/**
 *
 * @version $Id: MessageDisplayer.java 0 2009-11-03 16:47:40 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class MessageDisplayer implements Runnable {

    private String message = "";
    private String title = "";
    private int answer = JOptionPane.NO_OPTION;

    public int getAnswer()
    {
        return answer;
    }

    private MessageDisplayer(String message, String title)
    {
        this.message = message;
        this.title = title;
    }

    public static int displayMessage(String message, String title)
    {
        MessageDisplayer md = new MessageDisplayer(message, title);

        if (!SwingUtilities.isEventDispatchThread())
        {
            try {
                SwingUtilities.invokeAndWait(md);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        else
        {
            md.run();
        }

        return md.getAnswer();
    }

    public void run()
    {
        answer = JOptionPane.showConfirmDialog(Misc.getMainFrame(), message, title, JOptionPane.YES_NO_OPTION);
    }
}