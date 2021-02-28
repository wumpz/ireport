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
package com.jaspersoft.ireport.examples;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import com.jaspersoft.ireport.examples.beans.*;
import java.util.*;

public class PersonBeansDataSource extends JRAbstractBeanDataSourceProvider {
  
  	public PersonBeansDataSource() {
  		super(PersonBean.class);
                
        }
        
    @Override
        public JRField[] getFields(JasperReport report) throws JRException
        {
            
            return super.getFields(report);
        }
  
        
        
    @SuppressWarnings("unchecked")
  	public JRDataSource create(JasperReport report) throws JRException {
  		
  		ArrayList list = new ArrayList();
  		list.add(new PersonBean("Aldo"));
  		list.add(new PersonBean("Giovanni"));
  		list.add(new PersonBean("Giacomo"));
                
               return new JRBeanCollectionDataSource(list);
  	
  	}
 	
  	public void dispose(JRDataSource dataSource) throws JRException {
 		// nothing to do
  	}
  }
