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

package com.jaspersoft.ireport.designer.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

/**
 *
 * @version $Id: DataScriptlet.java 0 2010-05-28 17:22:14 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class DataScriptlet extends JRDefaultScriptlet {

    DefaultTableModel model = null;

    Map fields = null;
    List<String> fieldNames = null;

    @Override
    public void afterReportInit() throws JRScriptletException {
    }




    @Override
    public void afterDetailEval() throws JRScriptletException {
        
        
        if (model == null)
        {
            model = (DefaultTableModel)getParameterValue("ireport.data.tabelmodel");
        }

        if (fieldNames == null)
        {

            fieldNames = (List<String>)getParameterValue("ireport.data.columns");

            for (Object obj : fieldNames)
            {
                final String fName = obj+"";
                //fieldNames.add(fName);
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            model.addColumn(fName);
                        }
                    });
                } catch (Exception ex) {
                }

            }

            /*
            model.setRowCount(0);
            model.setColumnCount(0);
            fieldNames = new ArrayList<String>();
            Iterator fieldsIter = this.fieldsMap.keySet().iterator();

            while (fieldsIter.hasNext())
            {
                final String fName = fieldsIter.next()+"";
                fieldNames.add(fName);
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            model.addColumn(fName);
                        }
                    });
                } catch (Exception ex) {
                }
                
            }
            */
        }

        

        final Object[] row = new Object[fieldNames.size()];

        int i=0;
        for (String fName : fieldNames)
        {
            // Add the value to the table...
            row[i] = getFieldValue(fName);
            i++;
        }
        try {

            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    try {
                        final int record_count = (Integer)getVariableValue("REPORT_COUNT");
                        if (model.getRowCount() < record_count) model.addRow(row);
                    } catch (Exception ex) {}
                }
            });
        } catch (Exception ex) {
        }


        super.afterDetailEval();

    }


}
