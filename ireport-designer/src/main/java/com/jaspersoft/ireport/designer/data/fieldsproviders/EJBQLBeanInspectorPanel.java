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
package com.jaspersoft.ireport.designer.data.fieldsproviders;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.data.fieldsproviders.ejbql.EJBQLFieldsReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import net.sf.jasperreports.engine.design.JRDesignField;

/**
 *
 * @author gtoffoli
 */
public class EJBQLBeanInspectorPanel extends BeanInspectorPanel {
    
    /** Creates a new instance of EJBQLBeanInspectorPanel */
    public EJBQLBeanInspectorPanel() {
        super();
        
    }
    
    /**
     * Ad hoc queryChanged method for EJBQL queries....
     */
    @SuppressWarnings("unchecked")
    public void queryChanged(String newQuery) {
    
        lastExecution++;
        int thisExecution = lastExecution;
        // Execute a thread to perform the query change...
        
        String error_msg = "";
        lastExecution++;
            
        int in = lastExecution;
            
        getReportQueryDialog().getJLabelStatusSQL().setText("Executing EJBQL query....");
        /////////////////////////////
            
        try {
        Thread.currentThread().setContextClassLoader( IReportManager.getInstance().getReportClassLoader());
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
            
        if (in < lastExecution) return; //Abort, new execution requested
        
        EJBQLFieldsReader ejbqlFR = new EJBQLFieldsReader(newQuery, getReportQueryDialog().getDataset().getParametersList());
            
            try {
                Vector fields = ejbqlFR.readFields();
                
                List columns = new ArrayList();
                for (int i=0; i<fields.size(); ++i)
                {
                    JRDesignField field = (JRDesignField)fields.elementAt(i);
                    columns.add( new Object[]{field, field.getValueClassName(), field.getDescription()} );
                }
                Vector v = null;
                if (ejbqlFR.getSingleClassName() != null)
                {
                    v = new Vector();
                    v.add( ejbqlFR.getSingleClassName() );
                }
                
                setBeanExplorerFromWorker(v,true,false);
                setColumnsFromWorker(columns);
                
            } catch (Exception ex)
            {
                ex.printStackTrace();
                setBeanExplorerFromWorker(null,true,false);
                setColumnErrorFromWork( "Error: " +  ex.getMessage() );
            }
        
        getReportQueryDialog().getJLabelStatusSQL().setText("Ready");
    }
    
}
