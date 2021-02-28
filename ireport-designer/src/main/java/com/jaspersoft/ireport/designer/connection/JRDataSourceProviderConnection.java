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

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.connection.gui.JRDataSourceProviderConnectionEditor;
import com.jaspersoft.ireport.designer.data.WizardFieldsProvider;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.*;
import javax.swing.*;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Mutex;


/**
 *
 * @author  Administrator
 */
public class JRDataSourceProviderConnection extends IReportConnection implements WizardFieldsProvider {
    
    
    private net.sf.jasperreports.engine.JRDataSourceProvider dsp;
    private net.sf.jasperreports.engine.JRDataSource ds;
    private java.util.HashMap properties = new java.util.HashMap();
    
    public  net.sf.jasperreports.engine.JRDataSourceProvider getDataSourceProvider() {
        
        if (dsp == null && this.getProperties().get("JRDataSourceProvider") != null)
        {
            try {
                dsp = (JRDataSourceProvider)(Class.forName( (String)this.getProperties().get("JRDataSourceProvider"), true, IReportManager.getInstance().getReportClassLoader()).newInstance());
            } catch (NoClassDefFoundError ex)
	    {
                showErrorMessage(
                                //I18n.getString("messages.JRDataSourceProviderConnection.noClassDefFoundError",
                                "No class definition found error!!\nCheck your classpath!",
                                "Exception"); //I18n.getString("message.title.exception",
            } 
            catch (ClassNotFoundException ex)
            {
            showErrorMessage(
                        //I18n.getString("messages.JRDataSourceProviderConnection.classNotFoundError",
                                "Class not found error!!\nCheck your classpath!",
                        "Exception"); //I18n.getString("message.title.exception",
            } 
            catch (Exception ex)
            {

                showErrorMessage( I18n.getString("unexpected.datasource.error"  ,ex.getMessage(), Misc.getLogFile()),
                        "Exception"); //I18n.getString("message.title.exception",
            }
        }
        
        return dsp;
    }
    
    
     
    
    /** Creates a new instance of JRDataSourceProviderConnection */
    public JRDataSourceProviderConnection() {
    }
    
        /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource() { 
        
        return getJRDataSource(null);
    }
    
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource(JasperReport jasper) { 
        
        if (ds != null)
        {
            JOptionPane.showMessageDialog(Misc.getMainWindow(),"This datasource is already in use by another filling process!!", //I18n.getString("messages.JRDataSourceProviderConnection.datasourceInUse"
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        try {
            ds = getDataSourceProvider().create(jasper);
        } catch (final Exception ex)
        {
            Mutex.EVENT.readAccess(new Runnable() {

                public void run() {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                    Misc.formatString("Problems occurred creating the new datasource!!\n{0}", new Object[]{""+ex.getMessage()}), //"messages.JRDataSourceProviderConnection.problemsCreatingDatasource"
                    "Error",JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        
        return ds;
    }
    
    public void disposeDataSource()
    {
            if (dsp != null)
            {
                try {
                    dsp.dispose(ds);
                } catch (Exception ex) {}
                ds = null;
            }
    }
    
    public java.util.HashMap getProperties()
    {
        return properties;
    }
    
    /** All properties of a IReportConnection are stored in a XML file as Pair key/value
     *  This HashMap contains alla properties found for this IReportConnection in the
     *  XML. You must use this hashMap to initialize all attributes of your IReprotConnection
     */
    public void loadProperties(java.util.HashMap map)
    {
        properties = map;
    }
    
    /** Redefine this method is not useful (and not raccomended)
     *  It just write a portion of XML for save properties a IReportConnection name
     */
    public void save(java.io.PrintWriter pw)
    {
        java.util.HashMap hm = this.getProperties();
        pw.println("\t<iReportConnection name=\""+ this.getName() +"\" connectionClass=\"" + this.getClass().getName() +"\">");
        java.util.Iterator iterator = hm.keySet().iterator();
        
        while (iterator.hasNext())
        {
            String key = (String)iterator.next();
            pw.println("\t\t<connectionParameter name=\""  +  key + "\"><![CDATA[" + hm.get(key) + "]]></connectionParameter>");
        }
        pw.println("\t</iReportConnection>");
    }    
    
    public String getDescription(){ return "JRDataSourceProvider"; } //"connectionType.datasourceProvider"
    
    
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JRDataSourceProviderConnectionEditor();
    }
    
    public void test() throws Exception
    {
        try {
                
                Class c = Class.forName((String)this.getProperties().get("JRDataSourceProvider") , true, IReportManager.getInstance().getReportClassLoader());

                if (!(net.sf.jasperreports.engine.JRDataSourceProvider.class.isAssignableFrom(c)))
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            Misc.formatString("\"{0}\" is not a subclass of\nnet.sf.jasperreports.engine.JRDataSourceProvider.",  //"messages.connectionDialog.invalidJRDataSourceProvider"
                            new Object[]{this.getProperties().get("JRDataSourceProvider")}),
                            "Error",JOptionPane.ERROR_MESSAGE);
                    return;	
                }
                else
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            "Connection test successful!","",JOptionPane.INFORMATION_MESSAGE); //"messages.connectionDialog.connectionTestSuccessful"
                    return;
                }
                
            } catch (NoClassDefFoundError ex)
            {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            Misc.formatString( //"messages.connection.noClassDefFoundError",
                            "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                            new Object[]{"", ""+ex.getMessage()}),
                            "Exception",JOptionPane.ERROR_MESSAGE);
                    return;					
            } 
            catch (ClassNotFoundException ex)
            {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            Misc.formatString( //"messages.connection.classNotFoundError",
                            "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                            new Object[]{"", ""+ex.getMessage(), "" + this.getProperties().get("JRDataSourceProvider")}),
                            "Exception",JOptionPane.ERROR_MESSAGE);
                    return;				
            } 
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(Misc.getMainWindow(),
                    Misc.formatString( //"messages.connection.generalError2",
                    "{0}\nGeneral problem:\n {1}",
                    new Object[]{"", ""+ex.getMessage()}),
                    "Exception",JOptionPane.ERROR_MESSAGE);
                    return;									
            }
    }

    public String getQueryLanguage() {
        return null;
    }

    public List<JRDesignField> readFields(String query) throws Exception {
        List<JRDesignField> fields = new ArrayList<JRDesignField>();

        try {

            InputStream is = getClass().getResourceAsStream("/com/jaspersoft/ireport/designer/data/data.jrxml");
            JasperDesign dataJd = net.sf.jasperreports.engine.xml.JRXmlLoader.load(is);

            // Remove fields...
            dataJd.getFieldsList().clear(); // This would not be legal...
            dataJd.getFieldsMap().clear();

            JasperReport jr = JasperCompileManager.compileReport(dataJd);

            net.sf.jasperreports.engine.JRField[] jrfields = getDataSourceProvider().getFields( jr );

            for (int i=0; i< jrfields.length; ++i)
            {
                JRDesignField field = new JRDesignField();
                field.setName( jrfields[i].getName());
                field.setDescription( jrfields[i].getDescription());
                field.setValueClassName( jrfields[i].getValueClassName());
                field.setValueClass( jrfields[i].getValueClass());
                ModelUtils.replacePropertiesMap( jrfields[i].getPropertiesMap(), field.getPropertiesMap());
                fields.add(field);
            }
        } catch (Exception ex)
        {}

        return fields;
    }

    public boolean supportsDesign() {
        return false;
    }

    public String designQuery(String query) {
        return query;
    }
}
