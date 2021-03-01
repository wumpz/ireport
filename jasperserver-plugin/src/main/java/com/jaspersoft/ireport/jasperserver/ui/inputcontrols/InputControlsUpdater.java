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

package com.jaspersoft.ireport.jasperserver.ui.inputcontrols;

import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.ui.ReportUnitRunDialog;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.BasicInputControl;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Argument;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.openide.util.Exceptions;

/**
 *
 * @version $Id: InputControlsUpdater.java 0 2009-11-04 18:34:15 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class InputControlsUpdater implements Runnable {

    JServer server = null;
    BasicInputControl ic = null;
    Map parameters = null;
    String reportUnitUri = null;
    ReportUnitRunDialog dialog = null;

    public InputControlsUpdater(JServer server, BasicInputControl ic, String reportUnitUri, Map parameters, ReportUnitRunDialog dialog)
    {
        this.server = server;
        this.ic = ic;
        this.parameters = parameters;
        this.reportUnitUri = reportUnitUri;
        this.dialog = dialog;
    }


    public void run() {

        SwingUtilities.invokeLater(new Runnable() {

                public void run() {

                    if (dialog != null)
                    {
                        dialog.setBusy(true);
                    }
                }
            });
            
        java.util.List args = new java.util.ArrayList();
        args.add(new Argument( Argument.IC_GET_QUERY_DATA, ""));
        args.add(new Argument( "RU_REF_URI", reportUnitUri)); // TODO: use the constant....

        ic.getInputControl().getParameters().clear();
        ic.getInputControl().setResourceProperty(ResourceDescriptor.PROP_QUERY_DATA, null);

        String cacheKey = ic.getInputControl().getName();

        for (Iterator i= parameters.keySet().iterator(); i.hasNext() ;)
        {

            String key = ""+i.next();
            Object value = parameters.get( key );


            cacheKey += key + "=" + value + "\n";

            if (value instanceof java.util.Collection)
            {
                Iterator cIter = ((Collection)value).iterator();
                while (cIter.hasNext())
                {
                    String item = ""+cIter.next();
                    ListItem l = new ListItem(key+"",item);
                    l.setIsListItem(true);
                    ic.getInputControl().getParameters().add( l );
                }
            }
            else
            {
                ic.getInputControl().getParameters().add( new ListItem(key+"",parameters.get( key )));
            }
        }

        ResourceDescriptor newic = null;
        if (dialog.getValuesCache().containsKey(cacheKey))
        {
            ic.getInputControl().setQueryData((List)dialog.getValuesCache().get(cacheKey));
            newic = ic.getInputControl();
        }
        else
        {
            try {
                newic = server.getWSClient().get(ic.getInputControl(), null, args);
                if (newic.getQueryData() != null)
                {
                    dialog.getValuesCache().put(cacheKey, newic.getQueryData());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                    newic = null;
                try {

                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            if (dialog != null) {
                                dialog.setBusy(false);
                            }
                        }
                    });
                } catch (Exception ex1) {
                }
            }

        }

        if (newic != null)
        {
            try {
                final ResourceDescriptor newic2 = newic;
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        ic.setInputControl(newic2, newic2.getQueryData());
                        if (dialog != null) {
                            dialog.setBusy(false);
                        }
                    }
                });
            } catch (InterruptedException ex) {
            } catch (InvocationTargetException ex) {
            }
        }
        
        System.out.println("Update of ic " + ic.getInputControl().getName() + " finished!");
        System.out.flush();

    }

}
