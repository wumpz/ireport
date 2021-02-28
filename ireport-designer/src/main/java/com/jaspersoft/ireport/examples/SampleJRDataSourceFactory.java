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
import com.jaspersoft.ireport.examples.beans.PersonBean;
import java.util.Vector;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;

/**
 *
 * @author  Administrator
 */
public class SampleJRDataSourceFactory {
    
    // This is the method to call to get the datasource.
    // The method must be static.....    
    public  JRDataSource createDatasource()
    {
        javax.swing.table.DefaultTableModel tm = new javax.swing.table.DefaultTableModel(4,2);
        
        PersonBean person = new PersonBean();
        person.setFirstName("Giulio");
        person.setLastName("Toffoli");
        person.setEmail("gt@businesslogic.it");
        tm.setValueAt(person, 0, 0);
        tm.setValueAt("Test value row 1 col 1", 0, 1);
        
        person = new PersonBean();
        person.setFirstName("Teodor");
        person.setLastName("Danciu");
        person.setEmail("teodor@hotmail.com");
        tm.setValueAt(person, 1, 0);
        tm.setValueAt("Test value row 2 col 1", 1, 1);
        
        person = new PersonBean();
        person.setFirstName("Mario");
        person.setLastName("Rossi");
        person.setEmail("mario@rossi.org");
        tm.setValueAt(person, 2, 0);
        tm.setValueAt("Test value row 3 col 1", 2, 1);
        
        person = new PersonBean();
        person.setFirstName("Jennifer");
        person.setLastName("Lopez");
        person.setEmail("lopez@jennifer.com");
        tm.setValueAt(person, 3, 0);
        tm.setValueAt("Test value row 4 col 1", 3, 1);
        
        return new JRTableModelDataSource(tm);
    }    
    
     public  JRDataSource createBeanCollectionDatasource()
    {
        return new JRBeanCollectionDataSource(createBeanCollection());
    }    
     
    @SuppressWarnings("unchecked")
     public static  Vector  createBeanCollection()
     {
            java.util.Vector coll = new java.util.Vector();
        
        PersonBean person = new PersonBean();
        person.setFirstName("Giulio");
        person.setLastName("Toffoli");
        person.setEmail("gt@businesslogic.it");
        coll.add(person);
        
        person = new PersonBean();
        person.setFirstName("Teodor");
        person.setLastName("Danciu");
        person.setEmail("teodor@hotmail.com");
        coll.add(person);
        
        person = new PersonBean();
        person.setFirstName("Mario");
        person.setLastName("Rossi");
        person.setEmail("mario@rossi.org");
        coll.add(person);
        
        person = new PersonBean();
        person.setFirstName("Jennifer");
        person.setLastName("Lopez");
        person.setEmail("lopez@jennifer.com");
        coll.add(person);
    
        return coll;
     }
}
