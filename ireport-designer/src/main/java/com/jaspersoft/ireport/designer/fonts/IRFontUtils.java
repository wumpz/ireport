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

package com.jaspersoft.ireport.designer.fonts;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ReportClassLoader;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.util.JRFontUtil;
import org.apache.xerces.parsers.DOMParser;
import org.openide.filesystems.FileUtil;
import org.w3c.dom.*;

/**
 *
 * @version $Id: IRFontUtils.java 0 2009-10-26 11:14:37 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IRFontUtils {

    public static List<SimpleFontFamilyEx> loadFonts()
    {
        List<SimpleFontFamilyEx> fontsList = new ArrayList<SimpleFontFamilyEx>();

        try {
            File fontsDir = Misc.getFontsDirectory();
            if (fontsDir == null)
            {
                throw new java.lang.Exception("I'm unable to find the fonts directory of iReport!!");
            }


            File xmlFile = new File(fontsDir,"irfonts.xml");


             ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
             Thread.currentThread().setContextClassLoader(DOMParser.class.getClassLoader());

             DOMParser parser = new DOMParser();
             
             
             
//             System.out.println("Font files:" + xmlFile);
//             java.io.Reader afis = new java.io.FileReader(xmlFile);
//             char[] line = new char[1024];
//             int b = 0;
//             while ((b = afis.read(line)) > 0)
//             {
//                 System.out.println(new String(line, 0, b));
//             }
             
             
             java.io.FileInputStream fis = new FileInputStream(xmlFile);
             org.xml.sax.InputSource input_sss  = new org.xml.sax.InputSource(fis);
             //input_sss.setSystemId(filename);
             parser.parse( input_sss );

             Thread.currentThread().setContextClassLoader(oldClassLoader);


             Document document = parser.getDocument();
             Node node = document.getDocumentElement();


             NodeList list_child = node.getChildNodes(); // The root is fontFamilies
             for (int ck=0; ck< list_child.getLength(); ck++) {
                 Node connectionNode = list_child.item(ck);
                 if (connectionNode.getNodeName() != null && connectionNode.getNodeName().equals("fontFamily"))
                 {
                    // Take the CDATA...
                    SimpleFontFamilyEx family = new SimpleFontFamilyEx();
                     NamedNodeMap familyAttributes = connectionNode.getAttributes();

                     if (familyAttributes.getNamedItem("name") != null)
                     {
                         family.setName(familyAttributes.getNamedItem("name").getNodeValue());
                     }


                    // Get all connections parameters...
                    NodeList list_child2 = connectionNode.getChildNodes();
                    for (int ck2=0; ck2< list_child2.getLength(); ck2++) {
                        Node child_child = list_child2.item(ck2);
                        if (child_child.getNodeType() == Node.ELEMENT_NODE) {

                            String property = child_child.getNodeName();
                            String value = Misc.readPCDATA(child_child);

                            if (property.equals("normal")) family.setNormalFont(value);
                            if (property.equals("bold")) family.setBoldFont(value);
                            if (property.equals("italic")) family.setItalicFont(value);
                            if (property.equals("boldItalic")) family.setBoldItalicFont(value);
                            if (property.equals("pdfEncoding")) family.setPdfEncoding(value);
                            if (property.equals("pdfEmbedded")) family.setPdfEmbedded(value.equals("true") );

                            if (property.equals("locales")) // property[locales]/set/value
                            {
                                family.setLocales(new HashSet(getLocales(child_child)));
                            }

                            if (property.equals("exportFonts")) // property[exportFonts]/map/entry[key]/value
                            {
                                family.setExportFonts(getMappings(child_child));
                            }
                        }
                    }

                    fontsList.add(family);
                    
                }
             }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

         return fontsList;
    }


    /**
     * Extract the items in a set node child of thespecified node;
     * @param node
     * @return
     */
    private static List<String> getLocales(Node node)
    {
        List<String> locales = new ArrayList<String>();

        if (node != null)
        {
            List<Node> valueNodes = getSubNodes(node, "locale");
            for (Node tmpNode : valueNodes)
            {
                locales.add( Misc.readPCDATA(tmpNode, true));
            }
        }
        return locales;

    }

    private static List<Node> getSubNodes(Node node, String subNodeName)
    {
        List<Node> nodes = new ArrayList<Node>();

        NodeList list_child = node.getChildNodes(); // The root is beans
        for (int ck=0; ck< list_child.getLength(); ck++) {
         Node tmpNode = list_child.item(ck);
         if (tmpNode.getNodeName() != null && tmpNode.getNodeName().equals(subNodeName))
         {
             nodes.add(tmpNode);
         }
        }

        return nodes;
    }

    private static Node getSubNode(Node node, String subNodeName)
    {
        List<Node> nodes = getSubNodes(node, subNodeName);

        if (nodes.size() > 0) return nodes.get(0);
        return null;
    }

    /**
     * Extract the items in a set node child of thespecified node;
     * @param node
     * @return
     */
    private static Map<String, String> getMappings(Node node)
    {
        Map<String, String> map = new HashMap<String,String>();
        if (node != null)
        {
            List<Node> entryNodes = getSubNodes(node, "export");
            for (Node tmpNode : entryNodes)
            {
                String key = tmpNode.getAttributes().getNamedItem("key").getNodeValue();
                String value = Misc.readPCDATA(tmpNode, true);
                if (value != null && value.length() > 0 &&
                    key != null && key.length() > 0)
                {
                    map.put(key, value);
                }
            }
        }
        return map;

    }

    /**
     * This method just save the list of SimpleFontFamilyEx.
     * No TTF files operations (like copy or delete) is performed.
     */
    public static void saveFonts(List<SimpleFontFamilyEx> fonts)
    {
        try {
            File fontsDir = Misc.getFontsDirectory();
            if (fontsDir == null)
            {
                throw new java.lang.Exception("I'm unable to find the fonts directory of iReport!!");
            }


            File xmlFile = new File(fontsDir,"irfonts.xml");

            PrintWriter pw = new PrintWriter(xmlFile);

            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

            pw.println("<fontFamilies>");

            for (SimpleFontFamilyEx font : fonts)
            {
                pw.print(dumpBean(font));
            }

            pw.println("</fontFamilies>");

            pw.close();

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public static void reloadAndNotifyFontsListChange()
    {
        SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    ClassLoader oldCL = Thread.currentThread().getContextClassLoader();

                     Thread.currentThread().setContextClassLoader(new ReportClassLoader(IReportManager.getReportClassLoader(true)));

                     JRFontUtil.getFontFamilyNames();
                     //System.out.println("Reloading fonts: " + JRFontUtil.getFontFamilyNames());

                     Thread.currentThread().setContextClassLoader(oldCL);

                    // Fire a preference changed event...
                    IReportManager.getPreferences().put("fontExtensions", "" + (new java.util.Date()).getTime());
                }
            });
    }

    public static String dumpBean(SimpleFontFamilyEx font)
    {
        StringBuffer bean_xml = new StringBuffer("");

        bean_xml.append("   <fontFamily name=\"" + Misc.escapeXMLEntity(font.getName()) +  "\">\n");
        if (font.getNormalFont() != null && font.getNormalFont().length() > 0)
        {
            bean_xml.append("       <normal><![CDATA[" + font.getNormalFont() +  "]]></normal>\n");
        }

        if (font.getBoldFont() != null && font.getBoldFont().length() > 0)
        {
            bean_xml.append("       <bold><![CDATA[" + font.getBoldFont() +  "]]></bold>\n");
        }

        if (font.getItalicFont() != null && font.getItalicFont().length() > 0)
        {
            bean_xml.append("       <italic><![CDATA[" + font.getItalicFont() +  "]]></italic>\n");
        }

        if (font.getBoldItalicFont() != null && font.getBoldItalicFont().length() > 0)
        {
            bean_xml.append("       <boldItalic><![CDATA[" + font.getBoldItalicFont() +  "]]></boldItalic>\n");
        }

        if (font.getPdfEncoding() != null && font.getPdfEncoding().length() > 0)
        {
            bean_xml.append("       <pdfEncoding><![CDATA[" + font.getPdfEncoding() +  "]]></pdfEncoding>\n");
        }

        bean_xml.append("       <pdfEmbedded><![CDATA[" + font.isPdfEmbedded() +  "]]></pdfEmbedded>\n");


        if (font.getExportFonts() != null && font.getExportFonts().size() > 0)
        {

            bean_xml.append("       <exportFonts>\n");

            java.util.Iterator<String> keys = font.getExportFonts().keySet().iterator();
            while (keys.hasNext())
            {
                String key = keys.next();
                bean_xml.append("               <export key=\"" + Misc.escapeXMLEntity(key) +  "\"><![CDATA[" + font.getExportFonts().get(key) +  "]]></export>\n");
            }
            bean_xml.append("       </exportFonts>\n");

        }

        if (font.getLocales() != null && font.getLocales().size() > 0)
        {

            bean_xml.append("       <locales>\n");
            java.util.Iterator<String> keys = font.getLocales().iterator();

            while (keys.hasNext())
            {
                String key = keys.next();
                bean_xml.append("               <locale><![CDATA[" + key +  "]]></locale>\n");
            }

            bean_xml.append("       </locales>\n");
        }

        bean_xml.append("   </fontFamily>\n\n");

        return bean_xml.toString();
    }


    public static void export(Object[] fonts)
    {

        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( IReportManager.getInstance().getCurrentDirectory());

        jfc.setDialogTitle(I18n.getString("IReportPanel.Title.Dialog"));
        jfc.setDialogTitle(I18n.getString("IReportPanel.Title.Dialog"));//"addToClassPath"

        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setFileSelectionMode( JFileChooser.FILES_ONLY  );
        jfc.addChoosableFileFilter( new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File file) {
                String filename = file.getName();
                return (filename.toLowerCase().endsWith(".jar") || file.isDirectory() ||
                        filename.toLowerCase().endsWith(".zip")
                        ) ;
            }
            public String getDescription() {
                return "*.jar, *.zip";
            }
        });

        jfc.setMultiSelectionEnabled(false);

        jfc.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG);
        if  (jfc.showSaveDialog(Misc.getMainFrame()) == javax.swing.JOptionPane.OK_OPTION) {

            java.io.File jarFile = jfc.getSelectedFile();
            File fontsDir = Misc.getFontsDirectory();

            FileOutputStream fos = null;

            try
            {
                fos = new FileOutputStream(jarFile);
                ZipOutputStream zipos = new ZipOutputStream(fos);
                zipos.setMethod(ZipOutputStream.DEFLATED);

                String prefix = "family" + (new Date()).getTime();
                String fontXmlFile = "fonts" + prefix +".xml";

                ZipEntry propsEntry = new ZipEntry("jasperreports_extension.properties");
                zipos.putNextEntry(propsEntry);

                PrintWriter pw = new PrintWriter(zipos);


                pw.println("net.sf.jasperreports.extension.registry.factory.fonts=net.sf.jasperreports.engine.fonts.SimpleFontExtensionsRegistryFactory");
                pw.println("net.sf.jasperreports.extension.simple.font.families.ireport" + prefix +"=fonts/" + fontXmlFile);

                pw.flush();

                ZipEntry fontsXmlEntry = new ZipEntry("fonts/"+fontXmlFile);
                zipos.putNextEntry(fontsXmlEntry);

                pw = new PrintWriter(zipos);

                pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

                pw.println("<fontFamilies>\n");

                List<File> files = new ArrayList<File>();
                for (Object font : fonts)
                {
                    if (font instanceof SimpleFontFamilyEx)
                    {
                        SimpleFontFamilyEx sff = (SimpleFontFamilyEx)font;

                        String[] fontNames = new String[4];
                        fontNames[0] = sff.getNormalFont();
                        fontNames[1] = sff.getBoldFont();
                        fontNames[2] = sff.getItalicFont();
                        fontNames[3] = sff.getBoldItalicFont();

                        for (int i=0; i<fontNames.length; ++i)
                        {
                            if ( fontNames[i] != null && fontNames[i].length() > 0)
                            {
                                File fromFile = new File(fontsDir,fontNames[i] );
                                if (fromFile.exists())
                                {
                                    files.add(fromFile);
                                }
                                else
                                {
                                    if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(Misc.getMainFrame(), "I' unable to find the file: " + fromFile + "\nThe font extension may be corrupted. I'l skip this file.\n\nDo you want to continue anyway?"))
                                    {
                                        return;
                                    }
                                }
                                        
                            }
                        }
                        
                        sff = convertFontFamily(sff,"fonts/");
                        pw.println(dumpBean(sff));
                    }
                }
                
                pw.println("</fontFamilies>");

                pw.flush();

                for (File f : files)
                {
                    ZipEntry ttfZipEntry = new ZipEntry("fonts/" + f.getName());
                    zipos.putNextEntry(ttfZipEntry);

                    FileUtil.copy(new FileInputStream(f),zipos);
                }


                zipos.flush();
                zipos.finish();

                javax.swing.JOptionPane.showMessageDialog(Misc.getMainFrame(),
                    "Extension Jar successfully created",
                    "Done",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex)
            {
                javax.swing.JOptionPane.showMessageDialog(Misc.getMainFrame(),
                    ex.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE );
                return;
            }
            finally
            {
                if (fos != null)
                {
                    try
                    {
                        fos.close();
                    }
                    catch (IOException e)
                    {
                    }
                }
            }
        }
    }


    private static SimpleFontFamilyEx convertFontFamily(SimpleFontFamilyEx font, String fontPath)
    {
        SimpleFontFamilyEx newFont = new SimpleFontFamilyEx();
        newFont.setName( font.getName() );
        newFont.setLocales( font.getLocales() );
        newFont.setExportFonts( font.getExportFonts() );
        newFont.setPdfEmbedded( font.isPdfEmbedded() );
        newFont.setPdfEncoding( font.getPdfEncoding() );

        newFont.setName( font.getName() );

        String fname = font.getNormalFont();
        if (fname != null && fname.length() > 0)
        {
            File f = new File(fname);
            newFont.setNormalFont( fontPath + f.getName()  );
        }

        fname = font.getBoldFont();
        if (fname != null && fname.length() > 0)
        {
            File f = new File(fname);
            newFont.setBoldFont( fontPath +  f.getName()  );
        }

        fname = font.getItalicFont();
        if (fname != null && fname.length() > 0)
        {
            File f = new File(fname);
            newFont.setItalicFont( fontPath +  f.getName()  );
        }

        fname = font.getBoldItalicFont();
        if (fname != null && fname.length() > 0)
        {
            File f = new File(fname);
            newFont.setBoldItalicFont( fontPath +  f.getName()  );
        }

        return newFont;

    }

  
}
