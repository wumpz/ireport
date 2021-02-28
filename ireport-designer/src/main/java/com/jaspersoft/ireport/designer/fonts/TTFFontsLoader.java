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

import com.jaspersoft.ireport.designer.*;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gtoffoli
 */
public class TTFFontsLoader {

    public static java.awt.Font loadFont(String file)
    {
        java.awt.Font f = null;
        try {
            f = java.awt.Font.createFont(Font.TRUETYPE_FONT,  new java.io.FileInputStream(file));
        }
        catch (IllegalArgumentException ett)
        {
            System.out.println(ett.getMessage() +" No TrueType font");
        }
        catch (java.awt.FontFormatException ef)
        {
            System.out.println(ef.getMessage() +" FontFormatException");
        }
         catch (java.io.IOException ioex)
        {
            System.out.println(ioex.getMessage() +" IOException");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage() +" General Exception");
        }

        return f;
    }

    public static List<IRFont> loadTTFFonts()
    {
        return loadTTFFonts(null);
    }
    
    
    public static List<IRFont> loadTTFFonts(TTFFontsLoaderMonitor monitor)
    {
        if (monitor != null) monitor.fontsLoadingStarted();
        
        if (monitor != null)
        {
             monitor.fontsLoadingStatusUpdated( "Initializing font loader"); //"fontsLoader.loadingFontsInit"
        }
        
        List<IRFont> fonts = new ArrayList<IRFont>();
        
        List<String> fontsPaths = IReportManager.getInstance().getFontpath();
        
        
        if (fontsPaths == null || fontsPaths.size() == 0)
        {
            fontsPaths = IReportManager.getInstance().getClasspath();
        }
//            String path = System.getProperty("java.class.path");
//            if (path != null && path.length()>0)
//            {
//                StringTokenizer st = new StringTokenizer(path,  System.getProperty("path.separator") );
//
//                while (st.hasMoreTokens())
//                {
//                    String path_str = st.nextToken();
//                            
//                    try { addFontNames(fonts, path_str, monitor); } catch (Exception ex) { 
//                 
//                        System.out.println( 
//                                Misc.formatString("Invalid font found in: {0}", new Object[]{path_str})); //"fontListLoader.invalidFont"
//                    }
//                }
//            }
//        }
//        else
//        {
        for (String path_str : fontsPaths)
            {
                addFontNames(fonts, path_str, monitor); 
            }
//        }
        
        if (monitor != null) monitor.fontsLoadingFinished();
        return fonts;
    }
    
    
    public static void addFontNames(List<IRFont> fonts, String path_str, TTFFontsLoaderMonitor monitor)
    {
                java.text.MessageFormat formatter = new java.text.MessageFormat("Loaded font {0}");
        
                java.io.File file = new java.io.File(path_str);
                if (!file.exists()) {

                    return;
                }
                if (file.isDirectory())
                {
                    String[] files = file.list( new java.io.FilenameFilter(){
                        public boolean accept(java.io.File dir, String filename)
                        {
// modified                          	
//                            return filename.toUpperCase().endsWith(".TTF") ;
                            return filename.toUpperCase().endsWith(".TTF") || filename.toUpperCase().endsWith(".TTC");
                        }
                    });

                    //added begin 
                    com.lowagie.text.pdf.FontMapper fontMapper = new com.lowagie.text.pdf.DefaultFontMapper();
                    
                    if (files == null)
                    {
                         System.out.println( 
                                Misc.formatString("Unable to list files in: {0}", new Object[]{""+file}));
                        return;
                    }
                    for (int i=0; i<files.length; ++i)
                    {
                        try {
                            if (files[i].toUpperCase().endsWith(".TTC")) {
                                    try {
                                            String[] names = com.lowagie.text.pdf.BaseFont.enumerateTTCNames(file.getPath() + File.separator + files[i]);
                                            for (int a = 0; a < names.length; a++) {

                                                    java.awt.Font f = fontMapper.pdfToAwt( com.lowagie.text.FontFactory.getFont( file.getPath() + File.separator + files[i]).getBaseFont(), 10  );
                                                    if (f != null) {
                                                            fonts.add(new IRFont(f, files[i]));
                                                    }
                                                    else
                                                    {
                                                        System.out.println( 
                                                            Misc.formatString("Failed to load font {0}", new Object[]{""+file.getPath() + File.separator +files[i]}));
                                                    }


                                                    if (monitor != null)
                                                    {
                                                        monitor.fontsLoadingStatusUpdated( 
                                                                formatter.format( new Object[]{  file.getPath() + File.separator + files[i]  }));
                                                    }
                                            }
                                    } catch(com.lowagie.text.DocumentException de) {
                                            System.out.println(de);
                                    } catch(java.io.IOException ioe) {
                                            System.out.println(ioe);
                                    }
                            } else {
                            //added end


                                java.awt.Font f = loadFont( file.getPath() + File.separator + files[i]);
                                if (f != null)
                                {
                                    fonts.add(new IRFont(f, files[i]));
                                    //System.out.println(""+ f.getFamily() + " " + f.getFontName() +" ("+file.getPath() + file.separator +files[i]+")");
                                }
                                else
                                {
                                    System.out.println( 
                                        Misc.formatString("Failed to load font {0}", new Object[]{""+file.getPath() + File.separator +files[i]}));
                                }


                                if (monitor != null)
                                {
                                    monitor.fontsLoadingStatusUpdated( 
                                            formatter.format( new Object[]{  file.getPath() + File.separator + files[i]  }));
                                }
                            }
                        
                        } catch (Exception ex) { 

                            System.out.println( 
                                    Misc.formatString("Invalid font found: {0}. Font skipped.", new Object[]{files[i]})); //"fontListLoader.invalidFont"
                        }
                        
                        
                    }
                } else if (path_str.toUpperCase().endsWith(".TTF"))
                {
                    // Try to load this file...
                    //System.out.println(""+ path_str);
                }
                else if (path_str.toUpperCase().endsWith(".TTC"))
                {
                    // Try to load this file...
                    //System.out.println("TTC: "+ path_str);
                }
    }
}
