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
package com.jaspersoft.ireport.designer;

import com.jaspersoft.ireport.locale.I18n;
import java.awt.EventQueue;
import java.io.InputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.netbeans.api.queries.FileEncodingQuery;
import org.openide.filesystems.FileObject;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author gtoffoli
 */
public class JrxmlLoader implements ErrorHandler {

   FileObject file = null;

   public JrxmlLoader(FileObject f)
   {
       this.file = f;
   }

   public JrxmlLoader()
   {
       this(null);
   }


   public JasperDesign reloadJasperDesign() throws JRException {

       if (file == null)
       {
           // Unable to load the jrxml file
           throw new JRException(I18n.getString("JrxmlLoader.Error.FileNF"));
       }

       try {
           InputStream  in = file.getInputStream();
           return reloadJasperDesign(in);
       }
       catch (JRException  ex)
       {
           throw ex;
       } catch (Throwable ex) {
           //ex.printStackTrace();
           throw new JRException(ex);
       }
   }

   public JasperDesign reloadJasperDesign(InputStream in) throws JRException {

       if (EventQueue.isDispatchThread())
       {
           throw new IllegalStateException(I18n.getString("JrxmlLoader.Error.Warning"));
       }

       try {

           JasperDesign jd = JRXmlLoader.load(in);

           return jd;

       }
       catch (JRException  ex)
       {
           ex.printStackTrace();

           throw ex;
       }
       catch (Throwable ex)
       {
           ex.printStackTrace();

           throw new JRException(ex);
       }
       finally
       {
            if (in != null)
            {
                try {
                    in.close();
                } catch (Exception ex2)
                {
                }
            }
       }

   }

   public void warning(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

}

