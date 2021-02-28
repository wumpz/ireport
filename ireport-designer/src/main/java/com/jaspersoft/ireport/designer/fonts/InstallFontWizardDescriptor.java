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
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;

/**
 *
 * @version $Id: InstallFontWizardDescriptor.java 0 2009-10-22 15:34:12 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class InstallFontWizardDescriptor extends WizardDescriptor {

    private WizardDescriptor.Panel[] panels;

    public InstallFontWizardDescriptor()
    {
        super();
        this.setPanelsAndSettings(new WizardDescriptor.ArrayIterator(getPanels()), this);

        setTitleFormat(new MessageFormat("{0}"));
        setTitle("Font Installation");

    }


    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[]{
                        new InstallFontWizardPanel2TTFSelection(this),
                        new InstallFontWizardPanel2FamilyDetails(this),
                        new InstallFontWizardPanel3Locales(),
                        new InstallFontWizardPanel4FontMapping()
                    };
            String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                // Default step name to component name of panel. Mainly useful
                // for getting the name of the target chooser to appear in the
                // list of steps.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    // Sets steps names for a panel
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }

    /**
     * Run the wizard.
     * If a new font is successfully installed, the return true.
     * otherwise the wizard has been cancelled...
     *
     * @return new font result
     */
    public boolean runWizard()
    {
        Dialog dialog = DialogDisplayer.getDefault().createDialog(this);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = this.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            // do something

            // Collect the data...
            String familyName = (String)this.getProperty("family_name");
            String normal_ttf_file = (String)this.getProperty("normal_ttf_file");
            String bold_ttf_file = (String)this.getProperty("bold_ttf_file");
            String italic_ttf_file = (String)this.getProperty("italic_ttf_file");
            String bolditalic_ttf_file = (String)this.getProperty("bolditalic_ttf_file");
            String pdf_encoding = (String)this.getProperty("pdf_encoding");
            String pdf_embedded = (String)this.getProperty("pdf_embedded");
            List<String>locales = (List<String>)this.getProperty("locales");
            Map<String, String> mappings = (Map<String, String>)this.getProperty("mappings");

            try {
                    // 1. Locate the fonts directory....
                    File fontsDir = Misc.getFontsDirectory();
                    if (fontsDir == null)
                    {
                        throw new java.lang.Exception("I'm unable to find the fonts directory of iReport!!");
                    }

                    String fname = new File(normal_ttf_file).getName();

                    Misc.copyFile(new File(normal_ttf_file), new File(fontsDir,fname));

                    SimpleFontFamilyEx font = new SimpleFontFamilyEx();
                    font.setName(familyName);
                    font.setNormalFont(fname);

                    if (bold_ttf_file != null && bold_ttf_file.length() > 0)
                    {
                        File f = new File(bold_ttf_file);
                        if (f.exists())
                        {
                            fname = f.getName();
                            Misc.copyFile(f, new File(fontsDir,fname));
                            font.setBoldFont(fname);
                        }

                    }

                    if (italic_ttf_file != null && italic_ttf_file.length() > 0)
                    {
                        File f = new File(italic_ttf_file);
                        if (f.exists())
                        {
                            fname = f.getName();
                            Misc.copyFile(f, new File(fontsDir,fname));
                            font.setItalicFont(fname);

                        }
                    }


                    if (bolditalic_ttf_file != null && bolditalic_ttf_file.length() > 0)
                    {
                        File f = new File(bolditalic_ttf_file);
                        if (f.exists())
                        {
                            fname = f.getName();
                            Misc.copyFile(f, new File(fontsDir,fname));
                            font.setBoldItalicFont(fname);
                        }
                    }

                    font.setPdfEncoding(pdf_encoding);
                    font.setPdfEmbedded(pdf_embedded != null && pdf_embedded.equals("true"));

                    if (mappings != null)
                    {
                        font.setExportFonts(mappings);
                        
                    }

                    if (locales != null)
                    {
                        font.setLocales(new HashSet(locales));
                    }

                    String bean_xml = IRFontUtils.dumpBean(font);

                    bean_xml += "</fontFamilies>\n\n";

                    File xmlFile = new File(fontsDir,"irfonts.xml");
                    StringBuffer buf = new StringBuffer("");

                    FileInputStream fis = new FileInputStream(xmlFile);
                    byte[] bufBytes = new byte[1024];
                    int count = 0;
                    while ((count = fis.read(bufBytes)) > 0)
                    {
                        buf.append(new String( bufBytes, 0, count ));
                    }
                    fis.close();

                    String xmlContent = buf.toString();

                    String replaceTag = "</fontFamilies>";
                    if (xmlContent.indexOf(replaceTag) < 0)
                    {
                        replaceTag = "<fontFamilies/>";

                        if (xmlContent.indexOf(replaceTag) >= 0)
                        {

                            bean_xml = "<fontFamilies>\n" + bean_xml;
                        }
                    }

                    xmlContent = Misc.string_replace( bean_xml,replaceTag,  xmlContent);

                    FileWriter fos = new FileWriter(xmlFile);
                    fos.write(xmlContent);
                    fos.flush();
                    fos.close();

                    return true;

            } catch (java.lang.Exception ex)
            {
                JOptionPane.showMessageDialog(Misc.getMainFrame(), ex.getMessage(), "Error" , JOptionPane.ERROR_MESSAGE);
            }

        }

        return cancelled;
    }





    public static void browseForTTFFile(JTextField destinationTextField)
    {
        String fontsDir = IReportManager.getPreferences().get("fontsdir",null);
        if (fontsDir == null)
        {
            fontsDir = IReportManager.getInstance().getCurrentDirectory();
        }
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( fontsDir );

        jfc.setDialogTitle("Select a TrueType Font");

        jfc.addChoosableFileFilter( new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File file) {
                String filename = file.getName();
                return (filename.toLowerCase().endsWith(".ttf") || file.isDirectory()) ;
            }
            public String getDescription() {
                return "TrueType Font *.ttf";
            }
        });

        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( SwingUtilities.getAncestorOfClass(Window.class, destinationTextField)) == javax.swing.JOptionPane.OK_OPTION) {
            java.io.File file = jfc.getSelectedFile();
            try {
                destinationTextField.setText( file.getAbsolutePath() );
                IReportManager.getPreferences().put("fontsdir", file.getParent());
            } catch (Throwable ex){}
        }
    }

}
