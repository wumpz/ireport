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
package com.jaspersoft.ireport.designer.connection;

import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.connection.gui.JRSpringLoadedHibernateConnectionEditor;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import org.hibernate.Query;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * 
 * @author Jeffrey Payne
 *
 */

public class JRSpringLoadedHibernateConnection extends JRHibernateConnection {
	
	private final static String PROP_KEY_SPRING_CONFIG = "spring.loaded.hibernate.spring.config";
	private final static String PROP_KEY_SESSION_FACTORY_ID = "spring.loaded.hibernate.session.factory.id";
	
	private String springConfig = null;
	private String sessionFactoryBeanId = null;
	

	
	public ApplicationContext getApplicationContext() {
		
                StringTokenizer parser = new StringTokenizer(getSpringConfig(), ",");
                String[] configs = new String[parser.countTokens()];
                int iCount = 0;
                while (parser.hasMoreTokens()) {
                        configs[iCount++] = parser.nextToken();
                }
                return new ClassPathXmlApplicationContext(configs);
	}

	public String getSessionFactoryBeanId() {
		return sessionFactoryBeanId;
	}


	public void setSessionFactoryBeanId(String sessionFactoryBeanId) {
		this.sessionFactoryBeanId = sessionFactoryBeanId;
	}


	public String getSpringConfig() {
		return springConfig;
	}


	public void setSpringConfig(String springConfig) {
		this.springConfig = springConfig;
	}
	
	 @Override
        public SessionFactory getSessionFactory() {
		 
		 return (SessionFactory)getApplicationContext().getBean(getSessionFactoryBeanId());
		 
	 }
	 
        /*
         *  This method return all properties used by this connection
         */
        @SuppressWarnings("unchecked")
        @Override
        public java.util.HashMap getProperties()
        {    
            java.util.HashMap map = new java.util.HashMap();
            map.put(PROP_KEY_SESSION_FACTORY_ID, getSessionFactoryBeanId());
            map.put(PROP_KEY_SPRING_CONFIG, getSpringConfig());
            return map;
        }

        @Override
        public void loadProperties(java.util.HashMap map)
        {
            setSessionFactoryBeanId((String)map.get(PROP_KEY_SESSION_FACTORY_ID));
            setSpringConfig((String)map.get(PROP_KEY_SPRING_CONFIG));
        }
        
        @Override
        public String getDescription(){ return "Spring loaded Hibernate connection"; } //"connectionType.hibernateSpring"
	
        
        @Override
        public IReportConnectionEditor getIReportConnectionEditor()
        {
            return new JRSpringLoadedHibernateConnectionEditor();
        }
         
        
        @Override
        public void test() throws Exception
        {
            try {
                    Thread.currentThread().setContextClassLoader( IReportManager.getInstance().getReportClassLoader() );
                    
                    SessionFactory sf = getSessionFactory();
                    if (sf == null) {
                            JOptionPane.showMessageDialog(Misc.getMainWindow(),
                                    //I18n.getString("messages.connectionDialog.noSessionFactoryReturned",
                                    "No session factory returned.  Check your session factory bean id against the spring configuration.",
                                    "Error",JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        
                        
                            Session hb_session = sf.openSession();
                            Transaction  transaction = hb_session.beginTransaction();
                            Query q = hb_session.createQuery("select address as address Address as address");
                        
                            q.setFetchSize(1);
                            java.util.Iterator iterator = q.iterate();
                            // this is a stupid thing: iterator.next();

                            String[] aliases = q.getReturnAliases();
                            Type[] types = q.getReturnTypes();
                
                            
                        JOptionPane.showMessageDialog(Misc.getMainWindow(),
                                //I18n.getString("messages.connectionDialog.hibernateConnectionTestSuccessful",
                                "iReport successfully created a Hibernate session factory from your Spring configuration.",
                                "",JOptionPane.INFORMATION_MESSAGE);
                    }
            } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),e.getMessage(),
                            "Error",JOptionPane.ERROR_MESSAGE);

            }
        }
}
