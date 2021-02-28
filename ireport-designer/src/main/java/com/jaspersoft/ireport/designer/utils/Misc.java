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
package com.jaspersoft.ireport.designer.utils;

import com.jaspersoft.ireport.designer.IRURLClassLoader;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlEditorSupport;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.tools.JNumberComboBox;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Connection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreePath;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Lookup;
import org.openide.util.Mutex;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author gtoffoli
 */
public class Misc {

    public static FileObject createFolders(String path) throws IOException {

        FileObject root = Repository.getDefault().getDefaultFileSystem().getRoot();

        StringTokenizer st = new StringTokenizer(path, "/");

        System.out.println("Creating dir: " + path);
        FileObject nextDir = root;
        while (st.hasMoreTokens())
        {
            String nextDirName = st.nextToken();
            if (nextDirName.length() == 0) continue;

            System.out.println("Creating subir: " + nextDirName);
            if (nextDir != null)
            {
                FileObject newDir = nextDir.getFileObject(nextDirName);
                if (newDir == null)
                {
                    newDir = nextDir.createFolder(nextDirName);
                }
                nextDir = newDir;
            }
        }
        return nextDir;
    }

    public static String getDataFolderPath(DataFolder targetFolder) {
       if (targetFolder == null) return null;
       FileObject file = targetFolder.getPrimaryFile();
       File f = FileUtil.toFile (file);
       String path = f.getPath();
       return path;
    }

    public static String getLogFile() {
        String userDir = System.getProperty("netbeans.user");
        if (userDir == null)
            userDir = "<iReport user directory>";
        // FIXME the same as above
        return "" + new File(userDir + "/var/log/messages.log"); // TEMP
    }
    
    /**
     * If c != null, the ancestor window is return, otherwise this method looks for the main window.
     */
    public static Window getParentWindow(Component c)
    {
        if (c == null)
        {
            return getMainWindow();
        }
        else
        {
            return SwingUtilities.getWindowAncestor(c);
        }
    }
    
    /**
     * Return the NetBeans main window.
     */
    public static Window getMainWindow()
    {
        return getMainFrame();
    }

    private static Frame mainFrame = null;
    /**
     * Return the NetBeans main window.
     */
    public static Frame getMainFrame()
    {
        if (mainFrame == null)
        {
            Runnable run = new Runnable() {

                public void run() {
                     WindowManager w = Lookup.getDefault().lookup( WindowManager.class );
                     mainFrame =  (w == null) ? null : w.getMainWindow();
                }
            };

            Mutex.EVENT.readAccess(run);
        }
        return mainFrame;
    }
    
    public static String formatString(String s, Object[] params)
    {
        return MessageFormat.format(s, params);
    }

    public static java.awt.Image loadImageFromResources(String filename) {
            
        return loadImageFromResources(filename, Misc.class.getClassLoader());
    }
    
    public static java.awt.Image loadImageFromResources(String filename, ClassLoader cl) {
            
            try {
                    if (!filename.startsWith("/")) filename = "/" + filename;
                    return new javax.swing.ImageIcon( cl.getResource(  filename )).getImage();
            } catch (Exception ex) {
                    System.out.println("Exception loading resource: " +filename);
            }
            return null;
    }
    
    public static java.awt.Image loadImageFromFile(String path) {
                java.io.File file = new java.io.File(path);
                if (file.exists()) {
                        java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
                        java.awt.Image img = tk.createImage(path);
                        try {
                                java.awt.MediaTracker mt = new java.awt.MediaTracker( new javax.swing.JPanel() );
                                mt.addImage(img,0);
                                mt.waitForID(0);
                        } catch (Exception ex){
                                return null;
                        }
                        return img;
                }
                return null;
        }

    public static void log(String string) {
        Logger.getLogger( Misc.class.getName() ).log(Level.INFO, string );
        java.util.logging.Handler[] handlres = Logger.getLogger( Misc.class.getName() ).getHandlers();
        for (int i=0; i<handlres.length; ++i) handlres[i].flush();
    }
    
    public static void log(String string, Throwable t) {
       Logger.getLogger( Misc.class.getName() ).log(Level.SEVERE, string, t);
        java.util.logging.Handler[] handlres = Logger.getLogger( Misc.class.getName() ).getHandlers();
        for (int i=0; i<handlres.length; ++i) handlres[i].flush();
    }

    
    static public String readPCDATA(Node textNode) {
        return readPCDATA(textNode,true);
    }

    static public String readPCDATA(Node textNode, boolean trim) {
        NodeList list_child = textNode.getChildNodes();
        for (int ck=0; ck< list_child.getLength(); ck++) {
            Node child_child = list_child.item(ck);

            // --- start solution: if there is another node this should be the PCDATA-node
            Node ns = child_child.getNextSibling();
            if (ns != null)
            child_child = ns;
            // --- end solution

            final short nt = child_child.getNodeType();
            if ((nt == Node.CDATA_SECTION_NODE) || (nt == Node.TEXT_NODE)) {
               if (trim) return (child_child.getNodeValue()).trim();
                return child_child.getNodeValue();
            }
        }
        return "";
    }
    
    static public String nvl(Object obj, String def)
    {
        if (obj == null) return def;
        else return ""+obj;
    }
    
    
    
    /**
         *  Take a string like _it_IT or it_IT or it
         *  and return the right locale
         *  Default return value is Locale.getDefault()
         */
        static public java.util.Locale getLocaleFromString( String localeName )
        {
            return getLocaleFromString(localeName, Locale.getDefault() );
        }

        public static String toHTML(String s) {
                s = Misc.string_replace("&gt;",">",s);
                s = Misc.string_replace("&lt;","<",s);
                s = Misc.string_replace("&nbsp;"," ",s);
                s = Misc.string_replace("&nbsp;&nbsp;&nbsp;&nbsp;","\t",s);
                s = Misc.string_replace("<br>", "\n", s);
                return s;
        }
        
        /**
         *  Take a string like _it_IT or it_IT or it
         *  and return the right locale
         *  
         */
        static public java.util.Locale getLocaleFromString( String localeName, Locale defaultLocale )
	{
		String language = "";
		String country = "";
		String variant = "";
		Locale locale = defaultLocale;

		if (localeName == null || localeName.length() == 0) return locale;
		if (localeName.startsWith("_")) localeName = localeName.substring(1);
		if (localeName.indexOf("_") > 0)
		{
			language = localeName.substring(0,localeName.indexOf("_"));
			localeName = localeName.substring(localeName.indexOf("_")+1);

			if (localeName.indexOf("_") > 0)
			{
				country = localeName.substring(0,localeName.indexOf("_"));
				localeName = localeName.substring(localeName.indexOf("_")+1);

				if (localeName.indexOf("_") > 0)
				{
				    variant = localeName.substring(0,localeName.indexOf("_"));
				    localeName = localeName.substring(localeName.indexOf("_")+1);
				}
				else
				{
				    variant = localeName;
				}
			}
			else
			{
				country = localeName;
			}
		}
		else
		{
			language = localeName;
		}

		locale = new Locale(language,country,variant);

		return locale;
	}
        
        
        /**
         *    Replace s2 with s1 in s3
         **/
        public static String string_replace(String s1, String s2, String s3) {
                String string="";
                string = "";

                if (s2 == null || s3 == null || s2.length() == 0) return s3;

                int pos_i = 0; // posizione corrente.
                int pos_f = 0; // posizione corrente finale

                int len = s2.length();
                while ( (pos_f = s3.indexOf(s2, pos_i)) >= 0) {
                        string += s3.substring(pos_i,pos_f)+s1;
                        //+string.substring(pos+ s2.length());
                        pos_f = pos_i = pos_f + len;

                }

                string += s3.substring(pos_i);
                return string;
        }
        
        
        /**
   * This method select the whole text inside a textarea and set there the focus.
   * It should be used to select a component that contains a wrong expression.
   * In the future other properties of the componenct can be modified
   */
  public static void selectTextAndFocusArea(final JComponent expArea)
  {
      if (expArea == null) return;
      
      if (expArea instanceof JTextComponent)
      {
        ((JTextComponent)expArea).setSelectionStart(0);
        ((JTextComponent)expArea).setSelectionEnd(  ((JTextComponent)expArea).getText().length() );
        ((JTextComponent)expArea).setBorder(new LineBorder(Color.RED.darker(),2));
      }
      else if (expArea instanceof JEditorPane)
      {
        ((JEditorPane)expArea).setSelectionStart(0);
        ((JEditorPane)expArea).setSelectionEnd(  ((JTextComponent)expArea).getText().length() );
        ((JEditorPane)expArea).setBorder(new LineBorder(Color.RED.darker(),2));
      }
      
      expArea.requestFocusInWindow();

  }
  
  
  /**
     * Return the correct field type...
     *
     */
    public static String getJRFieldType(String type)
    {
        if (type == null) return "java.lang.Object";
        if (type.equals("java.lang.Boolean") || type.equals("boolean")) return "java.lang.Boolean";
        if (type.equals("java.lang.Byte") || type.equals("byte")) return "java.lang.Byte";
        if (type.equals("java.lang.Integer") || type.equals("int")) return "java.lang.Integer";
        if (type.equals("java.lang.Long") || type.equals("long")) return "java.lang.Long";
        if (type.equals("java.lang.Double") || type.equals("double")) return "java.lang.Double";
        if (type.equals("java.lang.Float") || type.equals("float")) return "java.lang.Float";
        if (type.equals("java.lang.Short") || type.equals("short")) return "java.lang.Short";
        if (type.startsWith("[")) return "java.lang.Object";
        return type;
    }
    
    
        /**
     * Save the query asking for a file.
     * see saveSQLQuery(String query, Component c)
     */
    public static boolean saveSQLQuery(String query)
    {
         return saveSQLQuery(query, null);
    }
    /**
     * Save the query asking for a file.
     * The optional component is used as parent for the file selection dialog
     * Default is the MainFrame
     */
    public static boolean saveSQLQuery(String query, Component c)
    {
            if (c == null) c = getMainWindow();
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".sql") || filename.endsWith(".txt") ||file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "SQL query (*.sql, *.txt)";
		    }
	    });

	    if (jfc.showSaveDialog( Misc.getMainWindow() ) == JFileChooser.APPROVE_OPTION) {

                try {

                    String fileName = jfc.getSelectedFile().getName();
                    if (fileName.indexOf(".") < 0)
                    {
                        fileName += ".sql";
                    }

                    File f = new File( jfc.getSelectedFile().getParent(), fileName);

                    FileWriter fw = new FileWriter(f);
                    fw.write( query );
                    fw.close();

                    return true;
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(getMainWindow(),"Error saving the query: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
	    }

            return false;
    }

    
    /**
     * Load the query asking for a file.
     * The optional component is used as parent for the file selection dialog
     * Default is the MainFrame
     */
    public static String loadSQLQuery()
    {
        return loadSQLQuery(null);
    }
    /**
     * Load the query asking for a file.
     * The optional component is used as parent for the file selection dialog
     * Default is the MainFrame
     */
    public static String loadSQLQuery(Component c)
    {
            if (c == null) c = getMainWindow(); 
            JFileChooser jfc = new JFileChooser();
            jfc.setMultiSelectionEnabled(false);
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".sql") || filename.endsWith(".txt") ||file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "SQL query (*.sql, *.txt)";
		    }
	    });

	    if (jfc.showOpenDialog( getMainWindow()) == JFileChooser.APPROVE_OPTION) {

                try {

                    FileReader fr = new FileReader(jfc.getSelectedFile());
                    StringBuffer sb = new StringBuffer();
                    char[] cbuf = new char[1024];
                    int i = fr.read(cbuf);
                    while (i > 0)
                    {
                        sb.append( cbuf, 0, i);
                        i = fr.read(cbuf);
                    }
                    fr.close();

                    return sb.toString();
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(getMainWindow(),"Error loading the query: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
	    }

            return null;
    }
    
    
    /**
         * Thanx to Jackie Manning j.m@programmer.net for this method!!
         */
        public static String getJdbcTypeClass(java.sql.ResultSetMetaData rsmd, int t ) {
                String cls = "java.lang.Object";

                try {
                    cls = rsmd.getColumnClassName(t);
                    cls =  getJRFieldType(cls);

                } catch (Exception ex)
                {
                    // if getColumnClassName is not supported...
                    try {
                        int type = rsmd.getColumnType(t);
                        switch( type ) {
                                case java.sql.Types.TINYINT:
                                case java.sql.Types.BIT:
                                        cls = "java.lang.Byte";
                                        break;
                                case java.sql.Types.SMALLINT:
                                        cls = "java.lang.Short";
                                        break;
                                case java.sql.Types.INTEGER:
                                        cls = "java.lang.Integer";
                                        break;
                                case java.sql.Types.FLOAT:
                                case java.sql.Types.REAL:
                                case java.sql.Types.DOUBLE:
                                case java.sql.Types.NUMERIC:
                                case java.sql.Types.DECIMAL:
                                        cls = "java.lang.Double";
                                        break;
                                case java.sql.Types.CHAR:
                                case java.sql.Types.VARCHAR:
                                        cls = "java.lang.String";
                                        break;

                                case java.sql.Types.BIGINT:
                                        cls = "java.lang.Long";
                                        break;
                                case java.sql.Types.DATE:
                                        cls = "java.util.Date";
                                        break;
                                case java.sql.Types.TIME:
                                        cls = "java.sql.Time";
                                        break;
                                case java.sql.Types.TIMESTAMP:
                                        cls = "java.sql.Timestamp";
                                        break;
                        }
                    } catch (Exception ex2){
                        ex2.printStackTrace();
                    }
                }
                return cls;
        }
        
        
  /**
   * If treePath is not in the current jTree selection, set it as selected.
   *
   */
  public static void ensurePathIsSelected(TreePath treePath, JTree jTree)
  {
        if (jTree == null || treePath == null) return;
        
        TreePath[] selectedPaths = jTree.getSelectionPaths();
        for (int i=0; selectedPaths != null && i<selectedPaths.length; ++i)
        {
            if (selectedPaths[i].equals( treePath )) return;
        }
        jTree.setSelectionPath(treePath);
  }

  /**
     * Don't use it. It does not work well in general,
     * it just does the job for the porpuses it has been
     * created for.
     * The function parse \n \r \t \\ characters
     * replacing the sequences with real newline, tabs etc...
     * @param str
     * @return
     */
    public static String removeSlashesString(String str) {

        if (str == null) return str;

        String newStr = "";
        for (int i=0; i<str.length(); ++i)
        {
            char c = str.charAt(i);
            if (c == '\\' && str.length() > i+1)
            {
                i++;
                char c2 = str.charAt(i);
                switch (c2)
                {
                    case 'n': newStr +="\n"; break;
                    case 'r': newStr +="\r"; break;
                    case 't': newStr +="\t"; break;
                    case '\\': newStr +="\\"; break;
                    default:
                    {
                        newStr += c;
                        newStr += c2;
                    }
                }
            }
            else
            {
                newStr += c;
            }
        }

        return newStr;
    }

    /**
     * Don't use it. It does not work well in general,
     * it just does the job for the porpuses it has been
     * created for.
     * The function replaces with \n \r \t \\ sequences
     * newlines, tabs etc...
     * @param str
     * @return
     */
    public static String addSlashesString(String str) {

        if (str == null) return str;

        String newStr = "";
        for (int i=0; i<str.length(); ++i)
        {
            char c = str.charAt(i);
            switch (c)
            {
                case '\n': newStr +="\\n"; break;
                case '\r': newStr +="\\r"; break;
                case '\t': newStr +="\\t"; break;
                case '\\': newStr +="\\\\"; break;
                default: newStr += c;
            }
        }
        return newStr;
    }
   
    public static void updateComboBox(javax.swing.JComboBox comboBox, List newItems) {
            updateComboBox(comboBox,newItems, false);
    }
    
    @SuppressWarnings("unchecked")
    public static void updateComboBox(javax.swing.JComboBox comboBox, List newItems, boolean addNullEntry) {
            Object itemSelected = null;
            if (comboBox.getSelectedIndex() >=0 ) {
                    itemSelected = comboBox.getSelectedItem();
            }

            //comboBox.removeAllItems();

            java.util.Vector items = new java.util.Vector(newItems.size(),1);
            boolean selected = false;
            boolean foundNullItem = false;
            Iterator e = newItems.iterator();
            int selectedIndex = -1;
            int currentelement = 0;
            while (e.hasNext()) {
                    Object item = e.next();
                    items.add(item);
                    if (item == itemSelected) {
                            selectedIndex = currentelement;
                    }
                    if (item.equals("")) {
                            foundNullItem = true;
                    }

                    currentelement++;
            }

            if (addNullEntry) {
                    if (!foundNullItem) items.add(0,""); 
                    if (selectedIndex < 0) selectedIndex = 0;
            }

            comboBox.setModel( new DefaultComboBoxModel(items)  );
            comboBox.setSelectedIndex(selectedIndex);
    }
    
    public static void setComboboxSelectedTagValue(javax.swing.JComboBox comboBox, Object itemValue) {
            for (int i=0; i<comboBox.getItemCount(); ++i)
            {
                Object val = comboBox.getItemAt(i);
                if ( val instanceof Tag)
                {
                    if ( (val == null && itemValue==null) ||
                         ((Tag)val).getValue() == itemValue ||
                         (((Tag)val).getValue() != null && ((Tag)val).getValue().equals(itemValue)))
                    {
                        comboBox.setSelectedIndex( i );
                        return;
                    }
                }
            }

            // Item not found...if the combobox is editable..let's set the value...
            if (comboBox.isEditable() && itemValue != null && itemValue instanceof String)
            {
                comboBox.setSelectedItem(itemValue);
            }
        }
    
    /**
         *  Take a filename, strip out the extension and append the new extension
         *  newExtension =   ".xyz"  or "xyz"
         *  If filename is null, ".xyz" is returned
         */
        public static String changeFileExtension(String filename, String newExtension ) {
                if (!newExtension.startsWith(".")) newExtension = "."+newExtension;
                if (filename == null || filename.length()==0 ) {
                        return newExtension;
                }

                int index = filename.lastIndexOf(".");
                if (index >= 0) {
                        filename = filename.substring(0,index);
                }
                return filename += newExtension;
        }
        
        public static String getClassPath() {
                String cp = System.getProperty("java.class.path");
                if (IReportManager.getInstance() != null)
                {
                    List<String> cp_v = IReportManager.getInstance().getClasspath();
                    for (String s : cp_v)
                    {
                        cp += File.pathSeparator + s;
                    }
                }
                return cp;
        }
        
        
    public static String getExpressionText(JRExpression exp)
    {
        if (exp == null) return "";
        if (exp.getText() == null) return "";
        return exp.getText();
    }
    
    public static JRDesignExpression createExpression(String className, String text)
    {
        if (text == null || text.trim().length() == 0) return null;
        JRDesignExpression exp = new JRDesignExpression();
        exp.setValueClassName(className != null ? className : "java.lang.Object");
        exp.setText(text);
        return exp;
    }
    
    public static  void  showErrorMessage(String errorMsg, String title, Throwable theException)
    {
        
        final JXErrorPane pane = new JXErrorPane();
        //pane.setLocale(I18n.getCurrentLocale());
       
        String[] lines = errorMsg.split("\r\n|\n|\r");

        String shortMessage = errorMsg;
        if (lines.length > 4)
        {
            shortMessage = "";
            for (int i=0; i<4; ++i)
            {
                shortMessage += lines[i]+"\n";
            }
            shortMessage = shortMessage.trim() + "\n...";
        }
      
        final ErrorInfo ei = new ErrorInfo(title,
                 shortMessage,
                 null, //"<html><pre>" + errorMsg + "</pre>"
                 null,
                 theException,
                 null,
                 null);
         
        
        /*
        
        
        final String fErrorMsg = errorMsg;
        */
        Runnable r = new Runnable() {
                public void run() {
                   // JOptionPane.showMessageDialog(MainFrame.getMainInstance(),fErrorMsg,title,JOptionPane.ERROR_MESSAGE);
                
                    pane.setErrorInfo(ei);
                   JXErrorPane.showDialog(Misc.getMainWindow(), pane);
                }
            };

        if (!SwingUtilities.isEventDispatchThread())
        {
            try {
                SwingUtilities.invokeAndWait( r );
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        else
        {
                r.run();
        }
    }
    
    
    /**
	 * Try to find a directory to open the chooser open.
	 * If there is a file among selected nodes (e.g. open editor windows),
	 * use that directory; else just stick to the user's home directory.
	 */
	public static File findStartingDirectory() {

        try {
            org.openide.nodes.Node[] nodes = TopComponent.getRegistry().getActivatedNodes();
            for (int i = 0; i < nodes.length; i++) {
                DataObject d = nodes[i].getCookie(DataObject.class);
                if (d != null) {
                    File f = FileUtil.toFile(d.getPrimaryFile());
                    if (f != null) {
                        if (f.isFile()) {
                            f = f.getParentFile();
                        }
                        return f;
                    }
                }
            }
        } catch (Throwable tr) {};
                
        String dir = IReportManager.getPreferences().get( IReportManager.CURRENT_DIRECTORY, null);
        if (dir != null)
        {
            File f = new File(dir);
            if (f.exists())
            {
                return f;
            }
        }
		// Backup:
        File f = new File(System.getProperty("user.home"));
        if (f.exists()) return f;
        
        f = new File(System.getProperty("user.dir"));
        if (f.exists()) return f;
        
        f = new File(".");
        try {
            f = new File( f.getAbsolutePath() );
        } catch (Throwable tr) {}
    	return f;
	}
    
        public static boolean setComboBoxText( boolean firstTime, String value, javax.swing.JComboBox comboField ) {
        if (( ! firstTime ) && (!( Misc.nvl(comboField.getSelectedItem(),"").equalsIgnoreCase(value)))) {
            comboField.setSelectedIndex(0);
            return false;
        } else {
            try {
                // find the item and select it.
                // we want an ignore case selection of the item...
                boolean found = false;
                for (int i=0; i<comboField.getItemCount(); ++i)
                {
                    String s = (String)comboField.getItemAt(i);
                    if (s != null && s.equalsIgnoreCase(value))
                    {
                        comboField.setSelectedIndex(i);
                        found = true;
                        break;
                    }
                }
                
                if (!found)
                {
                    comboField.addItem(value);
                    comboField.setSelectedItem(value);
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }


    public static boolean setComboBoxTag( boolean firstTime, Object value, javax.swing.JComboBox comboField ) {

        if (firstTime)
        {
            Misc.setComboboxSelectedTagValue(comboField, value);
            return true;
        }

        String selectedFont = null;

        if (comboField.getSelectedItem() != null)
        {
            if (comboField.getSelectedItem() instanceof Tag)
            {
                selectedFont = (String)((Tag)comboField.getSelectedItem()).getValue();
            }
            else
            {
                selectedFont = ""+comboField.getSelectedItem();
            }
        }

        if (selectedFont != null && selectedFont.equals(value))
        {
            return true;
        }

        if (comboField.isEditable())
        {
            comboField.setSelectedItem("");
        }
        
        return false;
    }
        
   public static boolean setElementComboNumber( boolean firstTime, double value, JNumberComboBox numberField ) {
        if (!firstTime && numberField.getValue() != value) {
            numberField.setSetting(true);
            numberField.setSelectedItem("");
            numberField.setSetting(false);
            return false;
        } else {
            try {
                numberField.setValue( value );
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Load the query asking for a file.
     * The optional component is used as parent for the file selection dialog
     * Default is the MainFrame
     */
   public static String loadExpression(Component c)
    {
            JFileChooser jfc = new JFileChooser();
            jfc.setMultiSelectionEnabled(false);
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".txt") ||file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "Text file (*.txt)";
		    }
	    });

	    if (jfc.showOpenDialog(c) == JFileChooser.APPROVE_OPTION) {

                try {

                    FileReader fr = new FileReader(jfc.getSelectedFile());
                    StringBuffer sb = new StringBuffer();
                    char[] cbuf = new char[1024];
                    int i = fr.read(cbuf);
                    while (i > 0)
                    {
                        sb.append( cbuf, 0, i);
                        i = fr.read(cbuf);
                    }
                    fr.close();

                    return sb.toString();
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(c,I18n.getString("Misc.loadingExpression.error.text", new Object[]{ ex.getMessage() }),I18n.getString("Misc.loadingExpression.error.title","Error"),JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
	    }

            return null;
    }

   /**
     * Save the query asking for a file.
     * The optional component is used as parent for the file selection dialog
     * Default is the MainFrame
     */
    public static boolean saveExpression(String expression, Component c)
    {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".txt") ||file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "Text file (*.txt)";
		    }
	    });

	    if (jfc.showSaveDialog(c) == JFileChooser.APPROVE_OPTION) {

                try {

                    String fileName = jfc.getSelectedFile().getName();
                    if (fileName.indexOf(".") < 0)
                    {
                        fileName += ".txt";
                    }

                    File f = new File( jfc.getSelectedFile().getParent(), fileName);

                    FileWriter fw = new FileWriter(f);
                    fw.write( expression );
                    fw.close();

                    return true;
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(c,I18n.getString("Misc.savingExpression.error.text", new Object[]{ ex.getMessage() }),I18n.getString("Misc.savingExpression.error.title","Error"),JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
	    }

            return false;
    }

    public static JRDataSource getDataSource(String name)
    {
        List<IReportConnection> conns = IReportManager.getInstance().getConnections();
        for (IReportConnection con : conns)
        {
            if (con.getName() != null &&
                con.getName().equals(name))
            {
                if (con.isJDBCConnection()) continue;
                return con.getJRDataSource();
            }
        }
        return null;
    }

    public static Connection getConnection(String name)
    {
        List<IReportConnection> conns = IReportManager.getInstance().getConnections();
        for (IReportConnection con : conns)
        {
            if (con.getName() != null &&
                con.getName().equals(name) && con.isJDBCConnection())
            {
                return con.getConnection();
            }
        }
        return null;
    }


    public static boolean isNumeric(String type)
    {
        return type!=null && (type.equals("java.lang.Byte") ||
            type.equals("java.lang.Short") ||
            type.equals("java.lang.Integer") ||
            type.equals("java.lang.Long") ||
            type.equals("java.lang.Float") ||
            type.equals("java.lang.Double") ||
            type.equals("java.lang.Number") ||
            type.equals("java.math.BigDecimal"));
    }


    public static void  showErrorMessage(final String errorMsg, final String title)
    {
        Runnable r = new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),errorMsg,title,JOptionPane.ERROR_MESSAGE);
                }
            };

        if (!SwingUtilities.isEventDispatchThread())
        {
            try {
                SwingUtilities.invokeAndWait( r );
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        else
        {
                r.run();
        }
    }

    
    public static boolean openFile(File f) {

        DataObject obj;

        f = FileUtil.normalizeFile(f);
        FileObject fl = FileUtil.toFileObject(f);
        if (fl == null) return false;
        try {
            obj = DataObject.find(fl);
        } catch (DataObjectNotFoundException ex) {
            return false;
        }

        final OpenCookie ocookie = obj.getCookie(OpenCookie.class);

        if (ocookie != null)
        {
            Mutex.EVENT.readAccess(new Runnable() {

                public void run() {
                    ocookie.open();
                }
            });
            
            return true;
        }

        return false;
    }
   

    public static JrxmlVisualView getViewForFile(File file)
    {
        Set<TopComponent> components = WindowManager.getDefault().getRegistry().getOpened();

        for (TopComponent t : components)
        {
            JrxmlEditorSupport jrxmlEditorSupport = t.getLookup().lookup(JrxmlEditorSupport.class);

            if (jrxmlEditorSupport != null && jrxmlEditorSupport.getDataObject().getPrimaryFile() != null)
            {
                File f = FileUtil.toFile(jrxmlEditorSupport.getDataObject().getPrimaryFile());
                if (f.equals(file))
                {
                    return (JrxmlVisualView)(jrxmlEditorSupport.getDescriptions()[0]);
                }
            }
        }
        return null;
    }

    /**
     * This is the fonts directory...
     * 
     * @return
     */
    public static File getFontsDirectory()
    {
        File fontsDir = InstalledFileLocator.getDefault().locate("fonts", null, false);
        if (fontsDir != null && fontsDir.exists() && fontsDir.isDirectory())
        {
            return fontsDir;
        }

        return null;
    }


    /**
     * convert a single char to its equivalent XML entity. Ordinary chars are not changed. 160 -> &nbsp;  weird chars
     * -> &#nnn; form
     *
     * @param c Char to convert
     *
     * @return equivalent string e.g. &amp;, null means leave char as is.
     */
    public static String escapeXMLEntity( String s )
    {

        StringBuffer buf = new StringBuffer("");
        for (int i=0; i<s.length(); ++i)
        {
            char c = s.charAt(i);
            switch ( c )
            {
            default:
                if ( c < 127 )
                    {
                    // leave alone as equivalent string.
                    buf.append(c);
                    break;
                    // faster than String.valueOf( c ).intern();
                    }
                else
                    {
                    // use the &#nnn; form
                    buf.append("&#" + Integer.toString( c ) + ";");
                    break;
                    }

                // don NOT modify the following code. It is not generated.

            case 34:
                buf.append("&quot;")/* &#x22; quotation mark */;
                break;
            case 38:
                buf.append("&amp;");/* &#x26; ampersand */;
                break;
            //            case 39:    // don't use apos, to make more compatible with HTML
            //                return "&apos;"/* &#x27; apos */
            case 60:
                buf.append("&lt;");/* &#x3c; less-than sign */;
                break;
            case 62:
                buf.append("&gt;");/* &#x3e; greater-than sign */;
                break;
            case 160:
                buf.append("&nbsp;"); /* &#x01; nbsp */
                break;
            }// end switch

        // can't fall out bottom
        }// end charToXMLEntity

        return buf.toString();
    }


    public static void copyFile(File from, File to) throws java.io.IOException
    {
        FileInputStream fis = new FileInputStream(from);
        FileOutputStream fos = new FileOutputStream(to);
        FileUtil.copy(fis,fos);
        fis.close();
        fos.close();
    }

    public static void copyFile(String from, String to) throws java.io.IOException
    {
        copyFile(new File(from), new File(to));
    }

    public static JrxmlVisualView getViewForJasperDesign(JasperDesign jd)
    {
        Set<TopComponent> components = WindowManager.getDefault().getRegistry().getOpened();

        for (TopComponent t : components)
        {
            JrxmlEditorSupport jrxmlEditorSupport = t.getLookup().lookup(JrxmlEditorSupport.class);

            if (jrxmlEditorSupport != null && jrxmlEditorSupport.getCurrentModel() == jd)
            {
                return (JrxmlVisualView)(jrxmlEditorSupport.getDescriptions()[0]);
            }
        }
        return null;
    }

        /**
     * Return the phisical file pointed by the specified expression.
     * All the .jasper extensions are translated in .jrxml
     *
     *
     * If the file is not found and exception is rised with the reason.
     *
     * Where do we look for this file?
     *
     * 1. Absolute path
     * 2. Classpath
     *
     * @param jasperDesign
     * @param dataset - If null, the main dataset from the jasperDesign is used.
     * @param expression
     * @param reportFolder If null, the current edited file location is used.
     * @param extension  If not null, it checks that the file ends with this extension
     * @param expression If classloader is null, the default report classloader is used.
     * @return File the file.
     * @throws Exception
     */
    public static File locateFileFromExpression(JasperDesign jasperDesign, JRDesignDataset dataset, JRDesignExpression expression, File reportFolder, String extension, ClassLoader classLoader) throws Exception
    {
        if (expression == null ||
                expression.getValueClassName() == null ||
                !expression.getValueClassName().equals("java.lang.String"))
        {
           // Return default image...
           // Unable to resolve the subreoport jrxml file...
            throw new Exception("The file expression is empty or it is not of type String.");
        }

        if (dataset == null) dataset =  jasperDesign.getMainDesignDataset();
        if (classLoader == null)
            classLoader = IReportManager.getReportClassLoader();

        File fileToOpen = null;

        String error = null;
        try {

            // Try to process the expression...
            ExpressionInterpreter interpreter = new ExpressionInterpreter(dataset, classLoader);
            interpreter.setConvertNullParams(true);

            Object ret = interpreter.interpretExpression( expression.getText() );

            if (ret != null)
            {
                String resourceName = ret + "";
                if (resourceName.toLowerCase().endsWith(".jasper"))
                {
                    resourceName = resourceName.substring(0, resourceName.length() -  ".jasper".length());
                    resourceName += ".jrxml";
                }

                if (extension != null)
                {
                    if (!extension.toLowerCase().endsWith(extension))
                    {
                        throw new Exception("Unable to resolve the " + extension + " file for this expression");
                    }
                }

                File f = new File(resourceName);
                if (!f.exists())
                {
                    String jrxmlFileName = f.getName();
                    if (reportFolder == null)
                    {
                        JrxmlVisualView visualView = IReportManager.getInstance().getActiveVisualView();
                        if (visualView != null)
                        {
                            File file = FileUtil.toFile(visualView.getEditorSupport().getDataObject().getPrimaryFile());
                            if (file.getParentFile() != null)
                            {
                                reportFolder = file.getParentFile();
                            }
                        }
                    }

                    URL[] urls = new URL[]{};
                    if (reportFolder != null)
                    {
                        urls = new URL[]{ reportFolder.toURI().toURL()};
                    }
                    IRURLClassLoader urlClassLoader = new IRURLClassLoader(urls, classLoader);
                    URL url = urlClassLoader.getResource(resourceName);
                    if (url == null)
                    {
                        // try just the file name...
                        url = urlClassLoader.getResource(jrxmlFileName);

                        if (url == null)
                        {
                            throw new Exception(resourceName + " not found.");
                        }
                    }

                    f = new File(url.toURI());
                    if (f.exists())
                    {
                        fileToOpen = f;
                    }
                    else
                    {
                        throw new Exception(f + " not found.");
                    }
                }
                else
                {
                    fileToOpen = f;
                }

             }
            else
            {
                throw new Exception();
            }
        } catch (Throwable ex) {

            fileToOpen = null;
            error = ex.getMessage();
            //ex.printStackTrace();
        }


        if (fileToOpen == null)
        {
            if (error == null)
            {
                error = "The file expression cannot be interpreted, and I'm unable to locate the file.";
            }
            throw new Exception(error);
        }

        return fileToOpen;
    }

    /**
     * Split a string in tokens. Tokens are divided by the single white character
     * Strings can be kept together with quotes. I.e.: '"This \"is\"" a String' results in [This "is"] [a] [String]
     * @param str
     * @return
     */
    public static String[] getTokens(String str)
    {
        List<String> tokens = new ArrayList<String>();

        boolean escaped = false;
        boolean inQuotes = false;

        StringBuffer token = new StringBuffer();
        for (int i=0; i<str.length(); ++i)
        {
           // case 1: the char is a "...
            char c = str.charAt(i);
            if (c == '\\')
            {
                if (escaped)
                {
                    token.append('\\');
                    token.append('\\');
                }
                escaped= !escaped;  // If we find it for two times in a row, it is autoescaped...
            }
            else if (c == '\"')
            {
                if (escaped)
                {
                    token.append(c);
                    escaped = false;
                }
                else if (!inQuotes && token.length() == 0)
                {
                    inQuotes = true;
                }
                else if (inQuotes)
                {
                    inQuotes = false;
                }
                else
                {
                    token.append(c);
                }

            }
            else if (!inQuotes && c == ' ')
            {
                // close the scring.
                if (escaped) token.append('\\');
                escaped = false;
                String t = token.toString();
                if (t.length() > 0) tokens.add(t);
                token = new StringBuffer();
            }
            else
            {
                if (escaped) token.append('\\');
                token.append(c);
                escaped = false;
            }
        }

        String t = token.toString();
        if (t.length() > 0) tokens.add(t);

        return tokens.toArray(new String[tokens.size()]);

    }
}


