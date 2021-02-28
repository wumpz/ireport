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
package com.jaspersoft.ireport.designer.data;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.data.fieldsproviders.BeanInspectorPanel;
import bsh.EvalError;
import com.jaspersoft.ireport.designer.*;
//import com.jaspersoft.ireport.designer.data.BeanInspectorPanel;
//import com.jaspersoft.ireport.designer.data.CincomMDXFieldsProvider;
//import com.jaspersoft.ireport.designer.data.EJBQLFieldsProvider;
//import com.jaspersoft.ireport.designer.data.HQLFieldsProvider;
//import com.jaspersoft.ireport.designer.data.MDXFieldsProvider;
//import com.jaspersoft.ireport.designer.data.SQLFieldsProvider;
//import com.jaspersoft.ireport.designer.data.XMLFieldsProvider;
import com.jaspersoft.ireport.designer.data.queryexecuters.QueryExecuterDef;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.table.*;
import javax.swing.*;
import java.util.*;
import bsh.Interpreter;
import com.jaspersoft.ireport.designer.connection.EJBQLConnection;
import com.jaspersoft.ireport.designer.connection.JDBCConnection;
import com.jaspersoft.ireport.designer.connection.JRCSVDataSourceConnection;
import com.jaspersoft.ireport.designer.connection.JRDataSourceProviderConnection;
import com.jaspersoft.ireport.designer.connection.JRHibernateConnection;
import com.jaspersoft.ireport.designer.connection.JRXlsDataSourceConnection;
import com.jaspersoft.ireport.designer.connection.JRXlsxDataSourceConnection;
import com.jaspersoft.ireport.designer.connection.MondrianConnection;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.editor.ExpressionEditor;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.CustomColumnControlButton;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.event.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.text.MessageFormat;
import javax.persistence.EntityManager;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;
import net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory;
import net.sf.jasperreports.olap.JRMondrianQueryExecuterFactory;
import nickyb.sqleonardo.querybuilder.QueryStyledDocument;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.icon.ColumnControlIcon;

/** 
 * A dialog which allows the user to enter a SQL query and then choose the
 * fields to use in the report.
 *
 * @author <a href="mailto:gt78@users.sourceforge.net">Giulio Toffoli</a>
 * @author <a href="mailto:phenderson@users.sourceforge.net">Peter Henderson</a>
 */
public class ReportQueryDialog extends javax.swing.JDialog implements ClipboardOwner, FieldsContainer {
    
    private BeanInspectorPanel bip1 = null;
    private boolean winMaximized = false;
    private boolean adjustingFrameSize = false;
    private Rectangle normalBounds = null;
    private UndoManager manager = new UndoManager();
    
    private FieldsProvider fieldsProvider = null;
        
    protected static String standard_types[]= new String[]{
        "java.lang.String",
	"java.lang.Object",
	"java.lang.Boolean",
	"java.lang.Byte",
	"java.util.Date",
	"java.sql.Timestamp",
	"java.sql.Time",
	"java.lang.Double",
	"java.lang.Float",
	"java.lang.Integer",
	"java.io.InputStream",
	"java.lang.Long",
	"java.lang.Short",
	"java.math.BigDecimal"    
    };
    
    FieldReader readerThread = null;
    public static int num = 1;

    private boolean showPreview = false;
    int lastPreviewSize = -1;
    
    public JLabel getJLabelStatusSQL()
    {
        return this.jLabelStatusSQL;
    }
    
    /** Creates new form ReportQueryFrame */
    public ReportQueryDialog(Dialog parent, boolean modal) 
    {
         super(parent, modal);
         initAll();
    }

    /** Creates new form ReportQueryFrame */
    public ReportQueryDialog(Frame parent, boolean modal) 
    {
         super(parent, modal);
         initAll();
    }

    
    public void initAll() {
        
        initComponents();
        this.setSize(800, 550);
        setLocationRelativeTo(null);
        
        jTableFields.getColumn(0).setCellRenderer(new JRFieldTableCellRenderer());
        
        stoppedChanging.setRepeats(false);
        jEditorPane1.setDocument(new QueryStyledDocument());
        jEditorPane1.getDocument().addDocumentListener( new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                if(isSettingSQLExpression)return;
                //okButton.setEnabled(false);
                stoppedChanging.restart();
            }
            public void insertUpdate(DocumentEvent e) {
                if(isSettingSQLExpression)return;
                //okButton.setEnabled(false);
                stoppedChanging.restart();
            }
            public void removeUpdate(DocumentEvent e) {
                if(isSettingSQLExpression)return;
                //okButton.setEnabled(false);
                stoppedChanging.restart();
            }
        } );
        
        
        jEditorPane1.getDocument().addUndoableEditListener(new UndoableEditListener() {

            public void undoableEditHappened(UndoableEditEvent e) {
                if(e.getEdit().getPresentationName() != null &&
                        e.getEdit().getPresentationName().equals("style change"))
                {
                    // ignore it
                    return;
                }
                manager.undoableEditHappened(e);
            }
        });

        Action undoAction = new UndoAction(manager);
        Action redoAction = new RedoAction(manager);

        // Assign the actions to keys
        jEditorPane1.registerKeyboardAction(undoAction, KeyStroke.getKeyStroke(
            KeyEvent.VK_Z, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
        jEditorPane1.registerKeyboardAction(redoAction, KeyStroke.getKeyStroke(
            KeyEvent.VK_Y, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);


        setColumnsError( I18n.getString("ReportQueryDialog.Message.Error") );
        
        /*
        if (MainFrame.getMainInstance().getProperties().getProperty("beanClass") != null)
        {
            jTextFieldBeanClass1.setText( MainFrame.getMainInstance().getProperties().getProperty("beanClass") +"");
        }
        */
        
        jComboBoxQueryType.addItem( new Tag("sql","SQL"));
        jComboBoxQueryType.addItem( new Tag("hql","Hibernate Query Language (HQL)"));
        jComboBoxQueryType.addItem( new Tag("xPath","XPath"));
        jComboBoxQueryType.addItem( new Tag("ejbql","EJBQL"));
        jComboBoxQueryType.addItem( new Tag("mdx","MDX"));
        jComboBoxQueryType.addItem( new Tag("xmla-mdx","XMLA-MDX"));

        jComboBoxMaxPreviewData.addItem( new Tag(100,"First 100 records"));
        jComboBoxMaxPreviewData.addItem( new Tag(500,"First 500 records"));
        jComboBoxMaxPreviewData.addItem( new Tag(1000,"First 1000 records"));
        jComboBoxMaxPreviewData.addItem( new Tag(0,"All"));

        jComboBoxMaxPreviewData.setSelectedIndex(0);

        
        java.util.List<QueryExecuterDef> queryExecuters = IReportManager.getInstance().getQueryExecuters();
        
        
        for (QueryExecuterDef qe : queryExecuters)
        {
            if (qe == null || qe.getLanguage() == null)
            {
                System.out.println("Query excuter found which is null!");
                continue;
            }
            String s = qe.getLanguage();
            boolean found = false;
            for (int i=0; i<jComboBoxQueryType.getItemCount(); ++i)
            {
                Tag t = (Tag)jComboBoxQueryType.getItemAt(i);
                if (s.toLowerCase().equals( (t.getValue()+"").toLowerCase() ) )
                {
                    found = true;
                }
            }
            if (!found)
            {
                jComboBoxQueryType.addItem( new Tag(qe,qe.getLanguage()));
            }
        }
        
        okButton.setEnabled(false);
        
        bip1 = new BeanInspectorPanel();
        bip1.setComboVisible(false);
        bip1.setJTableFields( jTableFields );
        bip1.setPathOnDescription(true);
        jPanel11.add(bip1, BorderLayout.CENTER);
                
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelButtonActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);


        javax.swing.KeyStroke F11KeyStroke =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0, false);
        javax.swing.Action maximizeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {

                setAdjustingFrameSize(true);
                if (isWinMaximized())
                {
                    if (getNormalBounds() != null)
                    {
                         ReportQueryDialog.this.setBounds(getNormalBounds());
                    }
                }
                else
                {
                    setNormalBounds(getBounds());
                    ReportQueryDialog.this.setBounds(Misc.getMainFrame().getBounds());
                }
                setWinMaximized(!isWinMaximized());
                setAdjustingFrameSize(false);
            }
        };

        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(F11KeyStroke, "MaximizeWindow");
        getRootPane().getActionMap().put("MaximizeWindow", maximizeAction);

        
        //applyI18n();
        
        //FieldsContainerTransferHandler fcth = new FieldsContainerTransferHandler(this);
        //jTableFields.setTransferHandler( fcth );
        
        //columnsErrorMsgLabel.setTransferHandler( fcth);
        //columnsScrollPane.setTransferHandler(fcth);
        //columnsErrorScrollPane.setTransferHandler( fcth);
        
        jTableFields.setColumnControl(new CustomColumnControlButton(jTableFields, new ColumnControlIcon() ));

        FieldsContainerTransferHandler fcth = new FieldsContainerTransferHandler(this);
        jTableFields.setTransferHandler( fcth );
        columnsErrorMsgLabel.setTransferHandler( fcth);
        columnsScrollPane.setTransferHandler(fcth);
        columnsErrorScrollPane.setTransferHandler( fcth);

        //jTableFields.setDropMode(DropMode.INSERT_ROWS);

        /*jTableFields.setTransferHandler(new TransferHandler() {

          final DataFlavor designFieldDF = new java.awt.datatransfer.DataFlavor(JRDesignField.class, JRDesignField.class.getName());

          public boolean canImport(TransferSupport support) {
            // for the demo, we'll only support drops (not clipboard paste)
            if (!support.isDrop()) {
              return false;
            }


            // we only import Strings
            if (!support.isDataFlavorSupported(designFieldDF)) {
              return false;
            }

            return true;
          }

          public boolean importData(TransferSupport support) {
            // if we can't handle the import, say so
            if (!canImport(support)) {
              return false;
            }

            // fetch the drop location
            JTable.DropLocation dl = (JTable.DropLocation) support
                .getDropLocation();

            int row = dl.getRow();

            // fetch the data and bail if this fails
            JRDesignField field;
            try {
               field= (JRDesignField) support.getTransferable().getTransferData(designFieldDF);
            } catch (Exception e) {
                e.printStackTrace();
              return false;
            }
            Vector rowData = new Vector();
            rowData.add(field);
            rowData.add(field.getValueClassName());
            rowData.add(field.getDescription());
            //String[] rowData = data.split(",");
            ((DefaultTableModel)jTableFields.getModel()).insertRow(row, rowData);

            Rectangle rect = jTableFields.getCellRect(row, 0, false);
            if (rect != null) {
              jTableFields.scrollRectToVisible(rect);
            }
            return true;
          }
        });
        */


        jTableData.setModel(new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }


        });
    } 
    
    /**
     * A timer to detect when the SQL expression area has not been changed, for
     * a short moment. This is to prevent the database being hit with every
     * with every key press.
     */
    javax.swing.Timer stoppedChanging = new javax.swing.Timer( 500, new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if( automaticlyReadFieldsCheckBox.isSelected() ) {
                processQueryChanged( jEditorPane1.getText().trim() );
            }
        }
    } );
    
    
    /**
     * Given a database query string, extract the database columns, then display
     * them. If there was a problem loading the list of columns, show an error
     * panel which contains the reason why.
     *
     * @param query The SQL query string, which can contain JasperReports parameters. 
     */
    private void processQueryChanged( String query ) {
        
        if (isSettingSQLExpression) return;
        
        
        if (dataset == null) {
            setColumnsError( I18n.getString("ReportQueryDialog.Message.Error") );
            return;
        }
        
        if (query.length() == 0) {
            setColumnsError( I18n.getString("ReportQueryDialog.Message.Error2") );
            return;
        }

        IReportConnection conn = IReportManager.getInstance().getDefaultConnection();
        
        Object obj = jComboBoxQueryType.getSelectedItem();
        String queryLanguage = "sql";
        if (obj != null && obj instanceof Tag) 
        {
            queryLanguage = ""+((Tag)obj).getValue();
        }
        else
        {
            queryLanguage = ""+obj;
        }      
        
        try {
            // Run the query in the backgroud as it is not quick.
            if (readerThread != null && readerThread.isAlive())
            {
                readerThread.interrupt();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();

        }

        readerThread = new FieldReader(query, conn, queryLanguage);
        readerThread.start();
    }
    
    public static int elaborationSequence = 0;

    /**
     * @return the adjustingFrameSize
     */
    public boolean isAdjustingFrameSize() {
        return adjustingFrameSize;
    }

    /**
     * @param adjustingFrameSize the adjustingFrameSize to set
     */
    public void setAdjustingFrameSize(boolean adjustingFrameSize) {
        this.adjustingFrameSize = adjustingFrameSize;
    }

    /**
     * @return the winMaximized
     */
    public boolean isWinMaximized() {
        return winMaximized;
    }

    /**
     * @param winMaximized the winMaximized to set
     */
    public void setWinMaximized(boolean winMaximized) {
        this.winMaximized = winMaximized;
    }

    /**
     * @return the normalBounds
     */
    public Rectangle getNormalBounds() {
        return normalBounds;
    }

    /**
     * @param normalBounds the normalBounds to set
     */
    public void setNormalBounds(Rectangle normalBounds) {
        this.normalBounds = normalBounds;
    }

    /**
     * @return the showPreview
     */
    public boolean isShowPreview() {
        return showPreview;
    }

    /**
     * @param showPreview the showPreview to set
     */
    public void setShowPreview(boolean showPreview) {
        this.showPreview = showPreview;
        if (showPreview)
        {
            jButtonPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/arrow_up.png")));
        }
        else
        {
            jButtonPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/arrow_down.png")));
        }
    }
    
    /**
     * A Thread class to extract field names from a SQL query.
     *
     */
    class FieldReader extends Thread {
        String src_query;
        String src_query_language = "sql";
        IReportConnection conn;
       
        int elaborationID = 0;
        
        /**
         * ctor.
         * @param query The query to read the field from
         * @param conn The IRport DB connection to use.
         */
        public FieldReader(String query, IReportConnection conn) {
            this(query, conn, "sql");
        }
        
        /**
         * ctor.
         * @param query The query to read the field from
         * @param conn The IRport DB connection to use.
         */
        public FieldReader(String query, IReportConnection conn, String query_language) {
            src_query=query;
            this.conn=conn;
            this.src_query_language = query_language;
            ReportQueryDialog.elaborationSequence++;
            elaborationID = ReportQueryDialog.elaborationSequence;
        }
        
        
        
        /**
         * Set the fields table data to the supplied data.
         * This is called from a none swing thread, hence all the invoke and
         * wait magic. If the current thread is the AWT Event Dispacher, no
         * invoke and wait is call.
         * The columns are only set if the query string matches the one the 
         * results are for.
         *
         * @param columns The list of columns to set.
         */
        public void setColumnsFromWorker( final List columns ) {
            try {
                
                Runnable r = new Runnable() {
                        public void run() {
                            String str = jEditorPane1.getText().trim();
                            if( str.compareTo(src_query)==0 ) {
                                setColumns( columns );
                            }
                        }
                    };
                    
                if (!SwingUtilities.isEventDispatchThread())
                {
                    SwingUtilities.invokeAndWait( r );
                }
                else
                {
                        r.run();
                }
                    
            } catch(Exception e) {
                // oh well we got interrupted.
            }
        }
        
        /**
         * Set the columns error message.
         * This is called from a none swing thread, hence all the invoke and
         * wait magic.
         * The message is only set if the query string matches the one the 
         * error message is for.
         *
         * @param columns The list of columns to set.
         */
        public void setColumnErrorFromWork( final String error_msg ) {
            try {
                
                Runnable r = new Runnable() {
                    public void run() {
                        String str = jEditorPane1.getText().trim();
                        if( str.compareTo(src_query)==0 ) {
                            setColumnsError( error_msg );
                            jEditorPane1.requestFocusInWindow();
                        }
                        
                    }
                };
                
                if (SwingUtilities.isEventDispatchThread())
                {
                    r.run();
                }
                else
                {
                    SwingUtilities.invokeAndWait( r );
                }
            } catch(Exception e) {
                // oh well we got interrupted.
            }
        }
        
        public String interpretQuery()
        {
            
            String query = this.src_query;
            try {
                Interpreter interpreter = prepareExpressionEvaluator();

                // look for parameters in the query and replace them with default values.
                // parameters look something like 
                // $P{QuoteGroupID}
                // or 
                // $P!{OrderByClause}
                java.util.List queryParams = new ArrayList();
                Iterator iterParams = dataset.getParametersList().iterator();
                while( iterParams.hasNext() ) {
                    JRDesignParameter parameter = (JRDesignParameter)iterParams.next();

                    String p1 = "$P{" + parameter.getName() + "}";
                    String p2 = "$P!{" + parameter.getName() + "}";

                    // evaluate the Default expression value
                    
                    // Integer expID = (Integer)parameterNameToExpressionID.get(parameter.getName());
                    
                    Object defValue;
                    if(  parameter.getDefaultValueExpression() != null &&  !parameter.getDefaultValueExpression().equals("") ) {
                        String expText = "";
                        if (parameter.getDefaultValueExpression() != null)
                        {
                            expText = parameter.getDefaultValueExpression().getText();
                        }
                        defValue = recursiveInterpreter( interpreter, expText , dataset.getParametersList(),0,parameter.getName());
                        // interpreter.eval("bshCalculator.evaluate(" + expID.intValue() + ")");
                    } else {
                        // this param does not have a default value.
                        defValue = null;
                    }


                    int ip1 = query.indexOf(p1);
                    while( ip1!=-1 ) {
                        // String replacement, Altering the SQL statement.
                        if( defValue==null ) {
                            throw new IllegalArgumentException(I18n.getString("ReportQueryDialog.Message.Exception") +
                                I18n.getString("ReportQueryDialog.Message.ExceptionB") 
                                + parameter.getName() + "'" );
                        }

                        String before = query.substring(0, ip1);
                        String after = query.substring(ip1+p1.length());
                        if (parameter.getValueClassName().equals("java.lang.String"))
                        {
                            String stt = defValue.toString();
                            stt = Misc.string_replace("''","'", stt);
                            query = before + "'" + stt + "'" + after;
                        }
                        else query = before + "" + defValue.toString() + "" + after;
                        
                        ip1 = query.indexOf(p1);
                    }

                    int ip2 = query.indexOf(p2);
                    while( ip2!=-1 ) {
                        // String replacement, Altering the SQL statement.
                        if( defValue==null ) {
                            throw new IllegalArgumentException(I18n.getString("ReportQueryDialog.Message.Exception") +
                                I18n.getString("ReportQueryDialog.Message.ExceptionB") 
                                + parameter.getName() + "'" );
                        }

                        String before = query.substring(0, ip2);
                        String after = query.substring(ip2+p2.length());
                        query = before + "" + defValue.toString() + "" + after;
                        ip2 = query.indexOf(p2);
                    }
                }
            
                return query;
            } catch (Exception ex)
            {
                javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
                return "";
            }
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public void run() {
            
            ((JXBusyLabel)ReportQueryDialog.this.getJLabelStatusSQL()).setBusy(true);
            
            try {
                String error_msg = "";
                num++;
                int in = num;

                FieldsProvider fp = getFieldsProvider();
                if (fp == null) return;

                if (fp.hasEditorComponent())
                {
                    FieldsProviderEditor editor = fp.getEditorComponent(ReportQueryDialog.this);
                    editor.queryChanged(this.src_query);
                }

                if (in < num) return; //Abort, new execution requested

                if (fp.supportsGetFieldsOperation())
                {
                    try {

                        // Create the JasperReport object
                        
                        JRDesignDataset ds = new JRDesignDataset( getDataset().isMainDataset() );

                        // Copy parameters and fields...
                        ds.getParametersList().addAll(getDataset().getParametersList());
                        ds.getVariablesList().addAll(getDataset().getVariablesList());
                        ds.getFieldsList().addAll(getDataset().getFieldsList());
                        
                        JRDesignQuery query = new JRDesignQuery();
                        query.setLanguage(src_query_language);
                        query.setText(src_query);

                        ClassLoader origCL = Thread.currentThread().getContextClassLoader();
                        Thread.currentThread().setContextClassLoader( IReportManager.getReportClassLoader());

                        ds.setQuery(query);

                        Thread.currentThread().setContextClassLoader( origCL );

                        
                        // Set query and langauge
                        
                        
                        Interpreter interpreter = prepareExpressionEvaluator();
                        HashMap parametersValues = new HashMap();

                        for (int i=0; i< ds.getParametersList().size(); ++i)
                        {

                            Object defValue;
                            JRDesignParameter param = (JRDesignParameter) ds.getParametersList().get(i);
                            
                            if(  param.getDefaultValueExpression() != null &&  !param.getDefaultValueExpression().getText().equals("") ) {

                                defValue = recursiveInterpreter( interpreter, param.getDefaultValueExpression().getText(), dataset.getParametersList(),0, param.getName());
                                //defValue = interpreter.eval( parameter.getDefaultValueExpression() );
                                // interpreter.eval("bshCalculator.evaluate(" + expID.intValue() + ")");
                            } else {
                                // this param does not have a default value.
                                defValue = null;
                            }

                            parametersValues.put(param.getName(), defValue);
                        }

                        if (in < num) return; //Abort, new execution requested

                        // Create a temporary JasperReports object...
                        net.sf.jasperreports.engine.JRField[] jrFields =  fp.getFields( this.conn, ds , parametersValues);

                        if (in < num) return; //Abort, new execution requested

                        List<JRField> columns = Arrays.asList(jrFields);
                        
                        if (in < num) return; //Abort, new execution requested

                        setColumnsFromWorker(columns);

                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                        setColumnErrorFromWork( I18n.getString("ReportQueryDialog.Message.Error3") +  ex.getMessage() );
                    }
                    getJLabelStatusSQL().setText(I18n.getString("ReportQueryDialog.Label.statusSQL"));
                }

                /////////////////////
            }
            finally
            {
                if (elaborationID == ReportQueryDialog.elaborationSequence)
                {
                    ((JXBusyLabel)ReportQueryDialog.this.getJLabelStatusSQL()).setBusy(false);
                }
            }
        }
    } // Class end
    
    
    
     
    /**
     * Shows the list of columns.
     * If the column error message label is visible remove it first.
     *
     * @param cols A List Object[], for the fields.
     */
    @SuppressWarnings("unchecked")
    public void setColumns( final List cols ) {
        
        columnsErrorMsgLabel.setText( "" );
        jPanel2.remove( columnsErrorScrollPane );
        jPanel2.add( columnsScrollPane, java.awt.BorderLayout.CENTER );
        jPanel2.revalidate();
        
        
        javax.swing.table.DefaultTableModel dtm =  (javax.swing.table.DefaultTableModel)jTableFields.getModel();
        // Collect all the previous fields trying to preserve possible settings...
        HashMap<String,JRPropertiesMap> fieldProperties = new HashMap<String,JRPropertiesMap>();

        // GEt from the document...
        JasperDesign report = IReportManager.getInstance().getActiveReport();
        JRField[] oldFields = report.getFields();
        for (int i=0; i<oldFields.length; ++i)
        {
            JRField field = oldFields[i];
            if (field.getPropertiesMap().hasProperties())
            {
                fieldProperties.put(field.getName(), field.getPropertiesMap().cloneProperties());
            }
        }

        // Get from the current  table...
        for (int i=0; i<dtm.getRowCount(); ++i)
        {
            JRField field = (JRField)dtm.getValueAt(i, 0);
            if (field.getPropertiesMap().hasProperties())
            {
                fieldProperties.put(field.getName(), field.getPropertiesMap().cloneProperties());
            }
        }

        

        dtm.getDataVector().clear();
        for(int i=0; i<cols.size(); i++) {
            Object obj = cols.get(i);
            if (obj instanceof JRField)
            {

                JRField jrfield = (JRField)obj;
                JRDesignField field = new JRDesignField();
                field.setName(jrfield.getName());
                field.setDescription(jrfield.getDescription());
                field.setValueClassName(jrfield.getValueClassName());
                ModelUtils.replacePropertiesMap(jrfield.getPropertiesMap(), field.getPropertiesMap());
               
                if (fieldProperties.containsKey(field.getName()))
                {
                    JRPropertiesMap map = fieldProperties.get(field.getName());
                    String[] names = map.getPropertyNames();
                    for (int k=0; k<names.length; ++k)
                    {
                        if (!field.getPropertiesMap().containsProperty(names[k]))
                        {
                            field.getPropertiesMap().setProperty(names[k], map.getProperty(names[k]));
                        }
                    }
                }
                Vector row = new Vector();
                row.add(field);
                row.add(field.getValueClassName());
                row.add(field.getDescription());
                dtm.addRow( row );
            }
            else
            {
                Object [] row = (Object[])cols.get(i);
                dtm.addRow( row );
            }
        }
        
        // Select all the fields so the new user does not get confused, when
        // they press OK. As only the selected fields are actually saved to the
        // report
        jTableFields.selectAll();
        
        okButton.setEnabled( true );
    }
    
    /**
     * Replace the columns list with a label that contains the reason why
     * columns cannot be loaded.
     *
     * @param msg The error message to display, can be in HTML.
     */
    public void setColumnsError( final String msg ) {
        columnsErrorMsgLabel.setText( msg );
        jPanel2.remove( columnsScrollPane );
        jPanel2.add( columnsErrorScrollPane, java.awt.BorderLayout.CENTER );
        jPanel2.revalidate();
        columnsErrorMsgLabel.repaint();
        
        //okButton.setEnabled(false);
    }

    private FieldsProvider getFieldsProvider() {
        return fieldsProvider;
    }

    private void setFieldsProvider(FieldsProvider fieldsProvider) {
        this.fieldsProvider = fieldsProvider;
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPanePreview = new javax.swing.JSplitPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelSQL = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxQueryType = new javax.swing.JComboBox();
        jPanel16 = new javax.swing.JPanel();
        jButtonLoadQuery = new javax.swing.JButton();
        jButtonSaveQuery = new javax.swing.JButton();
        jPanelQueryArea = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JTextPane();
        jLabelStatusSQL = new org.jdesktop.swingx.JXBusyLabel();
        automaticlyReadFieldsCheckBox = new javax.swing.JCheckBox();
        readFieldsButton = new javax.swing.JButton();
        jButtonOpenDesigner = new javax.swing.JButton();
        exportQueryButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jTextFieldBeanClass1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButtonReadBeanAttributes3 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButtonPreview = new javax.swing.JButton();
        columnsErrorScrollPane = new javax.swing.JScrollPane();
        columnsErrorMsgLabel = new javax.swing.JLabel();
        columnsScrollPane = new javax.swing.JScrollPane();
        jTableFields = new org.jdesktop.swingx.JXTable();
        jPanelPreview = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jButtonRefreshData = new javax.swing.JButton();
        jComboBoxMaxPreviewData = new javax.swing.JComboBox();
        jLabelStatusPreview = new org.jdesktop.swingx.JXBusyLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableData = new javax.swing.JTable();

        setTitle("Report query");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jSplitPanePreview.setDividerSize(0);
        jSplitPanePreview.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPanePreview.setResizeWeight(1.0);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);

        jPanel1.setMinimumSize(new java.awt.Dimension(10, 100));
        jPanel1.setPreferredSize(new java.awt.Dimension(10, 350));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(154, 350));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(154, 350));

        jPanelSQL.setMinimumSize(new java.awt.Dimension(1, 100));
        jPanelSQL.setPreferredSize(new java.awt.Dimension(1, 350));
        jPanelSQL.setLayout(new java.awt.GridBagLayout());

        jPanel14.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Query language");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel14.add(jLabel1, gridBagConstraints);

        jComboBoxQueryType.setEditable(true);
        jComboBoxQueryType.setMinimumSize(new java.awt.Dimension(200, 18));
        jComboBoxQueryType.setPreferredSize(new java.awt.Dimension(200, 22));
        jComboBoxQueryType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxQueryTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel14.add(jComboBoxQueryType, gridBagConstraints);

        jPanel16.setLayout(new java.awt.GridBagLayout());

        jButtonLoadQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/data/folder_database.png"))); // NOI18N
        jButtonLoadQuery.setText("Load query");
        jButtonLoadQuery.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonLoadQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadQueryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        jPanel16.add(jButtonLoadQuery, gridBagConstraints);

        jButtonSaveQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/data/database_save.png"))); // NOI18N
        jButtonSaveQuery.setText("Save query");
        jButtonSaveQuery.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonSaveQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveQueryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanel16.add(jButtonSaveQuery, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel14.add(jPanel16, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelSQL.add(jPanel14, gridBagConstraints);

        jPanelQueryArea.setLayout(new java.awt.BorderLayout());

        jSplitPane2.setBorder(null);
        jSplitPane2.setDividerSize(6);
        jSplitPane2.setResizeWeight(0.5);
        jSplitPane2.setOneTouchExpandable(true);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(661, 340));

        jEditorPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jEditorPane1.setFont(new java.awt.Font("Monospaced", 0, 12));
        jEditorPane1.setMinimumSize(new java.awt.Dimension(50, 200));
        jEditorPane1.setPreferredSize(new java.awt.Dimension(661, 340));
        jScrollPane1.setViewportView(jEditorPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel7.add(jScrollPane1, gridBagConstraints);

        jLabelStatusSQL.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jLabelStatusSQL, gridBagConstraints);

        automaticlyReadFieldsCheckBox.setSelected(true);
        automaticlyReadFieldsCheckBox.setText("Automatically Retrieve Fields");
        automaticlyReadFieldsCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                automaticlyReadFieldsCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel7.add(automaticlyReadFieldsCheckBox, gridBagConstraints);

        readFieldsButton.setText("Read Fields");
        readFieldsButton.setEnabled(false);
        readFieldsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readFieldsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        jPanel7.add(readFieldsButton, gridBagConstraints);

        jButtonOpenDesigner.setText("Query designer");
        jButtonOpenDesigner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenDesignerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        jPanel7.add(jButtonOpenDesigner, gridBagConstraints);

        exportQueryButton.setText("Send to clipboard");
        exportQueryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportQueryButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        jPanel7.add(exportQueryButton, gridBagConstraints);

        jSplitPane2.setLeftComponent(jPanel7);

        jPanelQueryArea.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        jPanelSQL.add(jPanelQueryArea, gridBagConstraints);

        jTabbedPane1.addTab("Report query ", jPanelSQL);

        jPanel8.setMinimumSize(new java.awt.Dimension(235, 30));
        jPanel8.setPreferredSize(new java.awt.Dimension(215, 30));
        jPanel8.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel8.add(jTextFieldBeanClass1, gridBagConstraints);

        jLabel3.setText("Class name");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel8.add(jLabel3, gridBagConstraints);

        jButtonReadBeanAttributes3.setText("Read attributes");
        jButtonReadBeanAttributes3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReadBeanAttributes3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel8.add(jButtonReadBeanAttributes3, gridBagConstraints);

        jPanel11.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        jPanel8.add(jPanel11, gridBagConstraints);

        jSeparator1.setMinimumSize(new java.awt.Dimension(0, 4));
        jSeparator1.setPreferredSize(new java.awt.Dimension(0, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel8.add(jSeparator1, gridBagConstraints);

        jTabbedPane1.addTab("JavaBean Datasource", jPanel8);

        jButton2.setText("Get fields from datasource");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton2);

        jTabbedPane1.addTab("DataSource Provider", jPanel6);

        jButton3.setText("Get fields from datasource");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed1(evt);
            }
        });
        jPanel15.add(jButton3);

        jTabbedPane1.addTab("CSV Datasource", jPanel15);

        jButton5.setText("Get fields from datasource");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5jButton2ActionPerformed1(evt);
            }
        });
        jPanel17.add(jButton5);

        jTabbedPane1.addTab("Excel Datasource", jPanel17);

        jPanel1.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setTopComponent(jPanel1);

        jPanel2.setPreferredSize(new java.awt.Dimension(453, 150));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());

        okButton.setText("OK");
        okButton.setEnabled(false);
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel4.add(okButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel4.add(cancelButton, gridBagConstraints);

        jPanel3.add(jPanel4, java.awt.BorderLayout.EAST);

        jPanel9.setMinimumSize(new java.awt.Dimension(100, 20));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        jButton1.setText("Filter expression...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel9.add(jButton1, gridBagConstraints);

        jButton4.setText("Sort options...");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel9.add(jButton4, gridBagConstraints);

        jButtonPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/arrow_down.png"))); // NOI18N
        jButtonPreview.setText("Preview data");
        jButtonPreview.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButtonPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPreviewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel9.add(jButtonPreview, gridBagConstraints);

        jPanel3.add(jPanel9, java.awt.BorderLayout.WEST);

        jPanel2.add(jPanel3, java.awt.BorderLayout.SOUTH);

        columnsErrorMsgLabel.setText("jLabel1");
        columnsErrorMsgLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        columnsErrorMsgLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        columnsErrorScrollPane.setViewportView(columnsErrorMsgLabel);

        jPanel2.add(columnsErrorScrollPane, java.awt.BorderLayout.CENTER);

        jTableFields.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Field name", "Field type", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableFields.setColumnControlVisible(true);
        jTableFields.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFieldsMouseClicked(evt);
            }
        });
        jTableFields.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableFieldsKeyReleased(evt);
            }
        });
        columnsScrollPane.setViewportView(jTableFields);

        jPanel2.add(columnsScrollPane, java.awt.BorderLayout.WEST);

        jSplitPane1.setBottomComponent(jPanel2);

        jSplitPanePreview.setTopComponent(jSplitPane1);

        jPanelPreview.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelPreview.setPreferredSize(new java.awt.Dimension(0, 0));
        jPanelPreview.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jButtonRefreshData.setText("Refresh Preview Data");
        jButtonRefreshData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshDataActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jButtonRefreshData, gridBagConstraints);
        jPanel5.add(jComboBoxMaxPreviewData, new java.awt.GridBagConstraints());

        jLabelStatusPreview.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelStatusPreview.setText("Ready");
        jLabelStatusPreview.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        jPanel5.add(jLabelStatusPreview, gridBagConstraints);

        jPanelPreview.add(jPanel5, java.awt.BorderLayout.NORTH);

        jTableData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane2.setViewportView(jTableData);

        jPanelPreview.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPanePreview.setBottomComponent(jPanelPreview);

        getContentPane().add(jSplitPanePreview, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jTableFieldsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFieldsMouseClicked
    if (SwingUtilities.isLeftMouseButton(evt) && evt.getClickCount() == 2)
    {
        if (this.jTableFields.getSelectedRow() >= 0)
        {
            JRDesignField field = (JRDesignField)this.jTableFields.getValueAt( this.jTableFields.getSelectedRow(), 0);
            
            /*
            JRFieldDialog jrpd = new JRFieldDialog(MainFrame.getMainInstance(), true);
            jrpd.setDataset( this.getDataset() );
            jrpd.setField(field);
            jrpd.setVisible(true);
            
            
            
            if (jrpd.getDialogResult() == JOptionPane.OK_OPTION)
            {
                field = jrpd.getField();
                DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
                int index = jTableFields.convertRowIndexToModel( this.jTableFields.getSelectedRow() );
                dtm.setValueAt(field,  index, 0);
                dtm.setValueAt(field.getClassType(),  index, 1);
                dtm.setValueAt(field.getDescription(),  index, 2);
                this.jTableFields.updateUI();
            }
            */
        }
    }
}//GEN-LAST:event_jTableFieldsMouseClicked

private void jTableFieldsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableFieldsKeyReleased
    //javax.swing.JOptionPane.showMessageDialog(null,"Key: "+evt.getKeyCode() + " (" + java.awt.event.KeyEvent.VK_DELETE + ")");
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE)
        {
             javax.swing.table.DefaultTableModel dtm =  (javax.swing.table.DefaultTableModel)jTableFields.getModel();
             //int[] selectedRows = jTableFields.getSelectedRows();
             //for (int i= selectedRows.length-1; i>=0; --i) 
             //{
             //    it.businesslogic.ireport.JRField field = (it.businesslogic.ireport.JRField)this.jTableFields.getValueAt( i, 0);
                 //this.subDataset.removeField(field);
                 //this.jTableFields.removeRowSelectionInterval(i,i);
             //}
             while (jTableFields.getSelectedRow() >=0)
             {
                 dtm.removeRow(jTableFields.getSelectedRow());
             }
        }
}//GEN-LAST:event_jTableFieldsKeyReleased

    private void jButtonOpenDesignerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenDesignerActionPerformed

        if (getFieldsProvider() != null && 
            getFieldsProvider().hasQueryDesigner())
        {
            try {
                String newQuery = getFieldsProvider().designQuery( IReportManager.getInstance().getDefaultConnection(), jEditorPane1.getText(), this );
                if (newQuery != null && !jEditorPane1.getText().equals(newQuery))
                {
                    jEditorPane1.setText( newQuery );
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        
        
    }//GEN-LAST:event_jButtonOpenDesignerActionPerformed

    private void jButton1ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed1

        SortFieldsDialog sfd = new SortFieldsDialog( this, true);
        sfd.setDataset( this.getDataset() );
        sfd.setVisible(true);
        
    }//GEN-LAST:event_jButton1ActionPerformed1

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

         openFilterExpressionDialog(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonSaveQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveQueryActionPerformed

       Misc.saveSQLQuery( jEditorPane1.getText(), this );
        
    }//GEN-LAST:event_jButtonSaveQueryActionPerformed

    private void jButtonLoadQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadQueryActionPerformed

        
            String query = Misc.loadSQLQuery(this);
            
            if (query != null)
            {
                jEditorPane1.setText(query);
            }
            
    }//GEN-LAST:event_jButtonLoadQueryActionPerformed

    private void jComboBoxQueryTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxQueryTypeActionPerformed
// TODO add your handling code here:
        boolean autoReadFields = automaticlyReadFieldsCheckBox.isSelected();

        readFieldsButton.setEnabled(false);
        automaticlyReadFieldsCheckBox.setSelected(false);
        readFieldsButton.setEnabled(false);
        
        String language = "sql";
        
        if (jComboBoxQueryType.getSelectedItem() != null &&
            jComboBoxQueryType.getSelectedItem() instanceof Tag)
        {
            language = ""+((Tag)jComboBoxQueryType.getSelectedItem()).getValue();
        }
        else if (jComboBoxQueryType.getSelectedItem() != null)
        {
            language = "" + jComboBoxQueryType.getSelectedItem();
        }

        
        // 1. Look for a special FieldsProvider....
        getJLabelStatusSQL().setText(I18n.getString("ReportQueryDialog.Label.StatusSQL2")  +  language + I18n.getString("ReportQueryDialog.Label.StatusSQL2B"));
        /////////////////////////////
        setFieldsProvider(null);
            
        List<QueryExecuterDef> qexecuters = IReportManager.getInstance().getQueryExecuters();
        for (QueryExecuterDef qed : qexecuters)
        {
            if (qed != null && qed.getLanguage() != null && qed.getLanguage().equals(language) && qed.getFieldsProvider() != null && qed.getFieldsProvider().length() > 0)
            {
                try {

                    // We have to set the query executer class...
                    IReportManager.getInstance().setJRProperty("net.sf.jasperreports.query.executer.factory." + language, qed.getClassName());
                    setFieldsProvider( (FieldsProvider)Class.forName( qed.getFieldsProvider(),true,IReportManager.getInstance().getReportClassLoader()).newInstance() );
                } catch (Throwable t)
                {
                    t.printStackTrace();
                    getJLabelStatusSQL().setText(I18n.getString("ReportQueryDialog.Label.StatusSQL3")  +  t.getMessage());
                }
            }
        }
        
        exportQueryButton.setEnabled(language.equals("sql"));
        
        /*
        if (getFieldsProvider() == null && language.equals("sql"))
        {
            setFieldsProvider( new SQLFieldsProvider());
        }
        else if (getFieldsProvider() == null && (language.toLowerCase().equals("xpath") || language.toLowerCase().equals("xpath2")))
        {
            setFieldsProvider( new XMLFieldsProvider());
        }
        else if (getFieldsProvider() == null && language.equals("hql"))
        {
            setFieldsProvider( new HQLFieldsProvider());
        }
        else if (getFieldsProvider() == null && language.equals("ejbql"))
        {
            setFieldsProvider( new EJBQLFieldsProvider());
        }
        else if (getFieldsProvider() == null && language.equals("mdx"))
        {
            setFieldsProvider( new MDXFieldsProvider());
        }
        else if (getFieldsProvider() == null && language.equals("xmla-mdx"))
        {
            setFieldsProvider( new CincomMDXFieldsProvider());
        }
        */
        boolean isSettingSQLExpressionOldValue = isSettingSQLExpression;
        isSettingSQLExpression = true;
        
        if (getFieldsProvider() == null)
        {
            setSpecialLanguageComponent( null );
            readFieldsButton.setEnabled(false );
            jButtonOpenDesigner.setEnabled( false );
            automaticlyReadFieldsCheckBox.setEnabled(false);
            automaticlyReadFieldsCheckBox.setSelected(autoReadFields);
            getJLabelStatusSQL().setText(I18n.getString("ReportQueryDialog.Label.StatusSQL4", language));
        }
        else
        {
            automaticlyReadFieldsCheckBox.setEnabled( getFieldsProvider().supportsAutomaticQueryExecution() );
            automaticlyReadFieldsCheckBox.setSelected( autoReadFields );
            readFieldsButton.setEnabled( (getFieldsProvider().supportsAutomaticQueryExecution() && !autoReadFields) || 
                                          getFieldsProvider().supportsGetFieldsOperation() );
            jButtonOpenDesigner.setEnabled( getFieldsProvider().hasQueryDesigner());
            if (getFieldsProvider().hasEditorComponent())
            {
                FieldsProviderEditor fpe = getFieldsProvider().getEditorComponent(this);
                if (fpe instanceof Component)
                {
                    setSpecialLanguageComponent( (Component)fpe );
                }
            }
            else
            {
                setSpecialLanguageComponent( null );
            }
            getJLabelStatusSQL().setText(I18n.getString("ReportQueryDialog.Label.StatusSQL5", language));
        }
        isSettingSQLExpression = isSettingSQLExpressionOldValue;
        
    }//GEN-LAST:event_jComboBoxQueryTypeActionPerformed

    
    public void setSpecialLanguageComponent(Component c)
    {
        if (c == null)
        {
            if (jPanelQueryArea.getComponent(0) != jPanel7)
            {
                jPanelQueryArea.removeAll();
                jPanelQueryArea.add(jPanel7, BorderLayout.CENTER);
            }
        }
        else
        {
         
            if (jPanelQueryArea.getComponent(0) != jSplitPane2 ||
                jSplitPane2.getRightComponent() != c)
            {
                jPanelQueryArea.removeAll();
                jSplitPane2.setLeftComponent(jPanel7);
                jSplitPane2.setRightComponent( c );
                jPanelQueryArea.add(jSplitPane2, BorderLayout.CENTER);
            }
        }
        jPanelQueryArea.updateUI();
        jEditorPane1.requestFocusInWindow();
        jEditorPane1.requestFocus();
    }
    
    
    @SuppressWarnings("unchecked")
    private void jButton2ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed1
        IReportConnection conn = IReportManager.getInstance().getDefaultConnection();
        if (conn == null || !(conn instanceof JRCSVDataSourceConnection)) {
            setColumnsError( I18n.getString("ReportQueryDialog.Message.Error4") );
            return;
        }
        else
        {
            JRCSVDataSourceConnection ic = (JRCSVDataSourceConnection)conn;
            try {
                Vector names = ic.getColumnNames();
                DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
                dtm.setRowCount(0);
            
                for (int nd =0; nd < names.size(); ++nd) {
                    String fieldName = ""+names.elementAt(nd);
                    JRDesignField field = new JRDesignField();
                    field.setName(fieldName);
                    field.setValueClassName("java.lang.String");
                    //field.setDescription(null); //Field returned by " +methods[i].getName() + " (real type: "+ returnType +")");
                    
                    Vector row = new Vector();
                    row.addElement(field);
                    row.addElement(field.getValueClassName());
                    row.addElement(field.getDescription());
                    dtm.addRow(row);
                }
                jTableFields.setRowSelectionInterval(0, names.size()-1);
            } catch (Exception ex)
            {
                setColumnsError( "" + ex.getMessage() );
            
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed1

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    @SuppressWarnings("unchecked")
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       
        IReportConnection conn = IReportManager.getInstance().getDefaultConnection();
        if (conn == null || !(conn instanceof JRDataSourceProviderConnection)) {
            setColumnsError( I18n.getString("ReportQueryDialog.Message.Error5") );
            return;
        }
        else
        {
           JRDataSourceProviderConnection ic = (JRDataSourceProviderConnection)conn;
            try {
                //JasperDesign report = IReportManager.getInstance().getActiveReport();
                // Create a temporary JasperReports object...
                //net.sf.jasperreports.engine.JasperReport jr = new net.sf.jasperreports.engine.JasperReport(report,"",null,(JRBaseObjectFactory)null,"");

                InputStream is = getClass().getResourceAsStream("/com/jaspersoft/ireport/designer/data/data.jrxml");
                JasperDesign dataJd = net.sf.jasperreports.engine.xml.JRXmlLoader.load(is);

                // Remove fields...
                dataJd.getFieldsList().clear(); // This would not be legal...
                dataJd.getFieldsMap().clear();

                JasperDesign report = IReportManager.getInstance().getActiveReport();
                ModelUtils.replacePropertiesMap(report.getPropertiesMap(), dataJd.getPropertiesMap());

                JasperReport jr = JasperCompileManager.compileReport(dataJd);

                net.sf.jasperreports.engine.JRField[] jrfields = ic.getDataSourceProvider().getFields( jr );

                DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
                dtm.setRowCount(0);
                
                for (int i=0; i< jrfields.length; ++i)
                {
                    Vector row = new Vector();
                    row.addElement(jrfields[i]);
                    row.addElement(jrfields[i].getValueClassName());
                    row.addElement(jrfields[i].getDescription());
                    dtm.addRow(row);
                }
                } catch (Exception ex)
                {
                    setColumnsError( "" + ex.getMessage() );
                    ex.printStackTrace();
                }
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void exportQueryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportQueryButtonActionPerformed
       
        FieldReader fr = new FieldReader(jEditorPane1.getText(), null);
        String query = fr.interpretQuery();
        
        java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection fieldContent = new StringSelection (query);
        
     	clipboard.setContents (fieldContent, this);

        
        // TODO add your handling code here:
    }//GEN-LAST:event_exportQueryButtonActionPerformed

    private void jButtonReadBeanAttributes3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReadBeanAttributes3ActionPerformed
              
        bip1.setClassName(jTextFieldBeanClass1.getText().trim());
    }//GEN-LAST:event_jButtonReadBeanAttributes3ActionPerformed

    @SuppressWarnings("unchecked")
    protected void getFieldsFromClass(Class clazz, String path) throws Exception
    {
         DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
            
         java.lang.reflect.Method[] methods = clazz.getMethods();
         java.lang.reflect.Field[] fields = clazz.getFields();
         // for any method, looking for get<FieldName> ....
            
            
         for (int i=0; i<methods.length; ++i)
         {
               
                if ( java.lang.reflect.Modifier.isPublic( methods[i].getModifiers() ) &&
                     methods[i].getDeclaringClass().getName().equals(clazz.getName() ) &&
                     !java.lang.reflect.Modifier.isNative( methods[i].getModifiers() )     
                     && methods[i].getName().startsWith("get")
                        && !methods[i].getReturnType().isPrimitive() 
                        && !methods[i].getReturnType().isArray())
                {
                   String fieldName = methods[i].getName().substring(3);
                   // Looking for the field...
                   for (int f=0; f<fields.length; ++f)
                   {
                       if (fields[f].getName().equalsIgnoreCase( fieldName ))
                       {
                           
                           fieldName = fields[f].getName();
                           break;
                       }
                   }
                   
                   String returnType =  methods[i].getReturnType().getName();
                   boolean found = false;
                   for (int cc=0; cc<standard_types.length; ++cc)
                   {
                        if ( returnType.equalsIgnoreCase(standard_types[cc]))
                        {                       
                            JRDesignField field = new JRDesignField();
                            field.setName(fieldName);
                            field.setValueClassName(returnType);
                            field.setDescription(path + "" + fieldName);
                            Vector row = new Vector();
                            row.addElement(field);
                            row.addElement(field.getValueClassName());
                            row.addElement(field.getDescription());
                            dtm.addRow(row);
                            found = true;
                            break;
                        }
                  }
                  if (!found)
                  {
                        JRDesignField field = new JRDesignField();
                        field.setName(fieldName);
                        field.setValueClassName("java.lang.Object");
                        field.setDescription(path + "" + fieldName);
                        
                        Vector row = new Vector();
                        row.addElement(field);
                        row.addElement(field.getValueClassName());
                        row.addElement(field.getDescription());
                        dtm.addRow(row);
                        Class subClazz = Class.forName(returnType,true, IReportManager.getInstance().getReportClassLoader());
                        getFieldsFromClass( subClazz , path + fieldName + ".");
                  }
                }
            }                    
    }
    


    
    private void automaticlyReadFieldsCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_automaticlyReadFieldsCheckBoxActionPerformed
        
        
        if( automaticlyReadFieldsCheckBox.isSelected() ) {
            // Automagically get quiery fields.
            // User has just enabled this so get field list.
            readFieldsButton.setEnabled(false);
            if (!isSettingSQLExpression)
            {
                processQueryChanged( jEditorPane1.getText().trim() );
            }
        } else {
            // Turn off automagic field reading. User will have to press the 
            // Read Fields button
            //okButton.setEnabled(false);
            readFieldsButton.setEnabled(true);
            //setColumnsError( "Enter your query above. Then use the Read " +
            //        "Fields button to retrieve the list of fields." );
            this.jLabelStatusSQL.setText(I18n.getString("ReportQueryDialog.Label.StatusSQL6") +
                    I18n.getString("ReportQueryDialog.Label.StatusSQL6B") );
        }
        
        IReportManager.getPreferences().putBoolean( IReportManager.USE_AUTO_REGISTER_FIELDS, automaticlyReadFieldsCheckBox.isSelected());
        
    }//GEN-LAST:event_automaticlyReadFieldsCheckBoxActionPerformed

    private void readFieldsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readFieldsButtonActionPerformed
        processQueryChanged( jEditorPane1.getText().trim() );
    }//GEN-LAST:event_readFieldsButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // No changes.
        num++;
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        
        try {
            if (stoppedChanging != null) stoppedChanging.stop();

            if ( this.getDataset() != null)
            {       
                num++; // avoid syncronization problems....

                Object obj = jComboBoxQueryType.getSelectedItem();
                //JRDesignQuery query = (JRDesignQuery)dataset.getQuery();
                JRDesignQuery query = new JRDesignQuery();

                if (obj != null && obj instanceof Tag) 
                {
                    query.setLanguage(""+((Tag)obj).getValue());
                }
                else
                {
                    query.setLanguage(""+obj);
                }

                query.setText(jEditorPane1.getText());

                ClassLoader origCL = Thread.currentThread().getContextClassLoader();
                Thread.currentThread().setContextClassLoader( IReportManager.getReportClassLoader());

                this.dataset.setQuery(query);

                Thread.currentThread().setContextClassLoader( origCL );

                
                com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();

                if ( jTableFields.getRowCount() > 0)
                {
                    // Clear all the existing fields.
                    this.dataset.getFieldsList().clear(); // This would not be legal...
                    this.dataset.getFieldsMap().clear();
                    // Add the new fields.
                    for (int i=0; i<jTableFields.getRowCount(); ++i)
                    {
                        JRDesignField field = (JRDesignField)this.jTableFields.getValueAt(i, 0);
                        Iterator iterFields = this.dataset.getFieldsList().iterator();
                        boolean found = false;
                        while (iterFields.hasNext()) {
                            JRDesignField f = (JRDesignField)iterFields.next();
                            if (f.getName().equalsIgnoreCase(field.getName())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            this.dataset.addField(field);
                        }
                    }
                } 
           }
       
       } catch (Throwable ex)
            {
                ex.printStackTrace();
            }
       
       this.setVisible(false);
        
    }//GEN-LAST:event_okButtonActionPerformed
    
    
    
    
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void jButton5jButton2ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5jButton2ActionPerformed1
        IReportConnection conn = IReportManager.getInstance().getDefaultConnection();
        if (conn == null || !((conn instanceof JRXlsDataSourceConnection) || conn instanceof JRXlsxDataSourceConnection)) {
            setColumnsError( I18n.getString("ReportQueryDialog.Message.Error.NoXlsConnection") );
            return;
        }
        else
        {
            if (conn instanceof JRXlsDataSourceConnection)
            {
                JRXlsDataSourceConnection ic = (JRXlsDataSourceConnection)conn;
                try {
                    List<String> names = ic.getColumnNames();
                    DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
                    dtm.setRowCount(0);

                    for (int nd =0; nd < names.size(); ++nd) {
                        String fieldName = ""+names.get(nd);
                        JRDesignField field = new JRDesignField();
                        field.setName(fieldName);
                        field.setValueClassName("java.lang.String");
                        //field.setDescription(""); //Field returned by " +methods[i].getName() + " (real type: "+ returnType +")");

                        Vector row = new Vector();
                        row.addElement(field);
                        row.addElement(field.getValueClassName());
                        row.addElement(field.getDescription());
                        dtm.addRow(row);
                    }
                    jTableFields.setRowSelectionInterval(0, names.size()-1);
                } catch (Exception ex)
                {
                    setColumnsError( "" + ex.getMessage() );

                }
            }
            else if(conn instanceof JRXlsxDataSourceConnection)
            {
                JRXlsxDataSourceConnection ic = (JRXlsxDataSourceConnection)conn;
                try {
                    List<String> names = ic.getColumnNames();
                    DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
                    dtm.setRowCount(0);

                    for (int nd =0; nd < names.size(); ++nd) {
                        String fieldName = ""+names.get(nd);
                        JRDesignField field = new JRDesignField();
                        field.setName(fieldName);
                        field.setValueClassName("java.lang.String");
                        //field.setDescription(""); //Field returned by " +methods[i].getName() + " (real type: "+ returnType +")");

                        Vector row = new Vector();
                        row.addElement(field);
                        row.addElement(field.getValueClassName());
                        row.addElement(field.getDescription());
                        dtm.addRow(row);
                    }
                    jTableFields.setRowSelectionInterval(0, names.size()-1);
                } catch (Exception ex)
                {
                    setColumnsError( "" + ex.getMessage() );

                }
            }
        }
    }//GEN-LAST:event_jButton5jButton2ActionPerformed1

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        
        if (isWinMaximized() == true && !isAdjustingFrameSize())
        {
            setWinMaximized(false);
            setNormalBounds(getBounds());
        }
    }//GEN-LAST:event_formComponentResized

    private void jButtonPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPreviewActionPerformed
        
        
        if (!isShowPreview())
        {
            final int last = jSplitPanePreview.getDividerLocation();
            jSplitPanePreview.setDividerSize(5);
            Dimension d = getSize();
            d.height += (lastPreviewSize > 0) ? lastPreviewSize : 300;
            this.setSize(d);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    jSplitPanePreview.setDividerLocation(last);
                }
            });
            setShowPreview(true);

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    jButtonRefreshDataActionPerformed(null);
                }
            });
        }
        else
        {
            jSplitPanePreview.setDividerSize(0);
            final int last = jSplitPanePreview.getDividerLocation();
            Dimension d = getSize();
            d.height -= jPanelPreview.getSize().height+5;
            lastPreviewSize = jPanelPreview.getSize().height;
            jPanelPreview.setSize(0,0);
            this.setSize(d);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    jSplitPanePreview.setDividerLocation(last);
                }
            });
            setShowPreview(false);
        }
    }//GEN-LAST:event_jButtonPreviewActionPerformed

    private void jButtonRefreshDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshDataActionPerformed

        // 1 . clear the table model...

        DefaultTableModel dtm = (DefaultTableModel) jTableData.getModel();
        dtm.setRowCount(0);
        dtm.setColumnCount(0);

        
        
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    // 2. we need a data source...
                    // to do that, we relay of what JasperReports does by creating the proper Query executer (if necessary...)..
                    // we will load a simple jasperReports with a proper scriptlet...

                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            getJLabelStatusPreview().setBusy(true);
                            getJLabelStatusPreview().setText("Getting data...");
                        }
                    });


                    populateDataPreview();

                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            getJLabelStatusPreview().setBusy(false);
                            getJLabelStatusPreview().setText( MessageFormat.format("Ready ({0} records read)", jTableData.getRowCount()));
                        }
                    });
                } catch (final JRException ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            getJLabelStatusPreview().setBusy(false);
                            getJLabelStatusPreview().setText("Error: " + ex.getMessage());
                        }
                    });
                }
            }
        });
        
        t.start();

    }//GEN-LAST:event_jButtonRefreshDataActionPerformed

    public JXBusyLabel getJLabelStatusPreview()
    {
        return this.jLabelStatusPreview;
    }

    private void populateDataPreview() throws JRException
    {
        System.out.println(" Populating preview... ");
        System.out.flush();
        
        ClassLoader origCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader( IReportManager.getReportClassLoader());
        InputStream is = getClass().getResourceAsStream("/com/jaspersoft/ireport/designer/data/data.jrxml");
        
        JasperDesign dataJd = net.sf.jasperreports.engine.xml.JRXmlLoader.load(is);
        // set language and query...
        Object obj = jComboBoxQueryType.getSelectedItem();
        //JRDesignQuery query = (JRDesignQuery)dataset.getQuery();
        JRDesignQuery query = new JRDesignQuery();

        if (obj != null && obj instanceof Tag)
        {
            query.setLanguage(""+((Tag)obj).getValue());
        }
        else
        {
            query.setLanguage(""+obj);
        }

        query.setText(jEditorPane1.getText());

        
        dataJd.setQuery(query);

        List parameters = getDataset().getParametersList();
        dataJd.setLanguage( IReportManager.getInstance().getActiveReport().getLanguage() );

        // Copy the properties map...
        try {
            JasperDesign report = IReportManager.getInstance().getActiveReport();
            ModelUtils.replacePropertiesMap(report.getPropertiesMap(), dataJd.getPropertiesMap());
        } catch (Exception ex){}
        
        // set the parameters
        for (Object p : parameters)
        {
            JRParameter par = (JRParameter)p;
            if (par.isSystemDefined()) continue;
            if (dataJd.getParametersMap().containsKey(par.getName()))
            {
                dataJd.removeParameter(par.getName());
            }
            dataJd.addParameter(par);
        }

        JRDesignParameter param = new JRDesignParameter();
        param.setName("ireport.data.tabelmodel");
        param.setValueClass(DefaultTableModel.class);

        dataJd.addParameter(param);

        JRDesignParameter paramColumns = new JRDesignParameter();
        paramColumns.setName("ireport.data.columns");
        paramColumns.setValueClass(Collection.class);

        dataJd.addParameter(paramColumns);


        // Add the fields...
        List<String> columnNames = new ArrayList<String>();

        if ( jTableFields.getRowCount() > 0)
        {
            // Clear all the existing fields.
            dataJd.getFieldsList().clear(); // This would not be legal...
            dataJd.getFieldsMap().clear();
            // Add the new fields.
            for (int i=0; i<jTableFields.getRowCount(); ++i)
            {
                JRDesignField field = (JRDesignField)this.jTableFields.getValueAt(i, 0);
                dataJd.addField(field);
                columnNames.add(field.getName());
            }
        }

        JasperReport jasper_report_obj = JasperCompileManager.compileReport(dataJd);
        
        HashMap hm = new HashMap();
        hm.put("ireport.data.tabelmodel", jTableData.getModel());
        
        hm.put("ireport.data.columns", columnNames);

        int max_records = Integer.valueOf( "" + ((Tag)jComboBoxMaxPreviewData.getSelectedItem()).getValue());

        if (max_records > 0)
        {
            hm.put("REPORT_MAX_COUNT", max_records);
        }

        // Now we need to decide how to fill this dataset... this is not a trivial thing actually...
        IReportConnection connection = IReportManager.getInstance().getDefaultConnection();

        try
        {

                if (connection.isJDBCConnection())
                {
                   Connection con = connection.getConnection();
                   try {
                      JasperFillManager.fillReport(jasper_report_obj,hm, con);
                   } catch (Exception ex)
                   {
                       throw ex;
                   } finally
                   {
                       // FIXMEGT This way of closing connection based on the connection class is not very clean...

                       if (connection instanceof JDBCConnection)
                       {
                            if (con != null) try {  con.close(); } catch (Exception ex) { }
                       }
                   }
                }
                else if (connection.isJRDataSource())
                {

                       JRDataSource ds = null;
                       if (connection instanceof JRDataSourceProviderConnection)
                       {
                            ds = ((JRDataSourceProviderConnection) connection).getJRDataSource(jasper_report_obj);

                            if (ds == null)
                            {
                                throw new JRException("Unable to create the datasource using the JRDataSourceProvider");
                            }
                            JasperFillManager.fillReport(jasper_report_obj,hm,ds);

                            try { ((JRDataSourceProviderConnection)connection).disposeDataSource(); } catch (Exception ex) {
                            }
                       }
                       else
                       {
                           ds = connection.getJRDataSource(jasper_report_obj);
                           JasperFillManager.fillReport(jasper_report_obj,hm,ds);
                       }
                }
                else
                {
                   if (connection instanceof JRHibernateConnection)
                   {
                       Session session = null;
                       Transaction transaction = null;

                       try {
                            session = ((JRHibernateConnection)connection).createSession();
                            transaction = session.beginTransaction();
                            hm.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
                            JasperFillManager.fillReport(jasper_report_obj,hm);

                       } catch (Exception ex)
                       {
                           throw ex;
                       } finally
                       {
                            if (transaction != null) try {  transaction.rollback(); } catch (Exception ex) { }
                            if (transaction != null) try {  session.close(); } catch (Exception ex) { }
                       }
                   }
                   else if (connection instanceof EJBQLConnection)
                   {
                       EntityManager em = null;
                       try {
                            em = ((EJBQLConnection)connection).getEntityManager();
                            hm.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, em);
                            //Thread.currentThread().setContextClassLoader( reportClassLoader );
                            JasperFillManager.fillReport(jasper_report_obj,hm);

                       } catch (Exception ex)
                       {
                           throw ex;
                       } finally
                       {
                            ((EJBQLConnection)connection).closeEntityManager();
                       }
                   }
                   else if (connection instanceof MondrianConnection)
                   {
                       mondrian.olap.Connection mCon = null;
                       try {
                            mCon = ((MondrianConnection)connection).getMondrianConnection();
                            hm.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION, mCon);
                            //Thread.currentThread().setContextClassLoader( reportClassLoader );
                            JasperFillManager.fillReport(jasper_report_obj,hm);

                       } catch (Exception ex)
                       {
                           throw ex;
                       } finally
                       {
                            ((MondrianConnection)connection).closeMondrianConnection();
                       }
                   }
                   else // Query Executor mode...
                   {
                       //Thread.currentThread().setContextClassLoader( reportClassLoader );
                       JasperFillManager.fillReport(jasper_report_obj,hm);
                   }
                }

        } catch (final Throwable ex)
        {

            throw new JRException(ex);

        }
        finally
        {
            connection.disposeSpecialParameters(hm);
            if (connection != null && connection instanceof JRDataSourceProviderConnection)
            {
                    try { ((JRDataSourceProviderConnection)connection).disposeDataSource(); } catch (Exception ex) {
                    }
            }

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ReportQueryDialog(new javax.swing.JFrame(), true).setVisible(true);
    }
        
    Map parameterNameToExpressionID = null;
    
    /**
     * Create an expression evaluator for report parameters.
     *
     */
    private Interpreter prepareExpressionEvaluator() throws bsh.EvalError {
        
        Thread.currentThread().setContextClassLoader(IReportManager.getReportClassLoader());
                
        Interpreter interpreter = new Interpreter();
        interpreter.setClassLoader(IReportManager.getReportClassLoader());
        
        // Staring patch from rp4
        
        //interpreter.eval("String tmp;");
        //List<String> paths = IReportManager.getInstance().getClasspath();
        //for (String path : paths)
        //{
        //    interpreter.set("tmp", path);
        //    interpreter.eval("addClassPath(tmp);");
        //}        
       
        // Add report import directives to the bsh interpreter
        interpreter.eval("import net.sf.jasperreports.engine.*;");
        interpreter.eval("import net.sf.jasperreports.engine.fill.*;");
        interpreter.eval("import java.util.*;");
        interpreter.eval("import java.math.*;");
        interpreter.eval("import java.text.*;");
        interpreter.eval("import java.io.*;");
        interpreter.eval("import java.net.*;");
        interpreter.eval("import java.util.*;");
        interpreter.eval("import net.sf.jasperreports.engine.*;");
        interpreter.eval("import net.sf.jasperreports.engine.data.*;");
        
        String[] imports =  IReportManager.getInstance().getActiveReport().getImports();
        for (int i=0; imports != null && i<imports.length; ++i)
        {
            interpreter.eval("import " + imports[i] + ";");
        }
        // End patch from rp4
        /*
        interpreter.eval(new StringReader(bshScript));

        interpreter.eval("bshCalculator = createBshCalculator()");
        */
        return interpreter;
         
        // return null;
    }

    /**
     * Convert a class name string into its class object.
     * There must be a function in JasperReports that does this somewhere.
     *
     *
     */
    private Class classStringToClass(String classType) {
        Class c = null;
        
        
        if ( classType.equals("java.lang.String") ) {
            c = java.lang.String.class;
        } else if ( classType.equals("java.lang.Integer") ) {
            c = java.lang.Integer.class;
        } else if ( classType.equals("java.lang.Boolean") ) {
            c = java.lang.Boolean.class;
        } else if ( classType.equals("java.lang.Byte") ) {
            c = java.lang.Byte.class;
        } else if ( classType.equals("java.util.Date") ) {
            c = java.util.Date.class;
        } else if ( classType.equals("java.sql.Timestamp") ) {
            c = java.sql.Timestamp.class;
        } else if ( classType.equals("java.sql.Time") ) {
            c = java.sql.Time.class;
        } else if ( classType.equals("java.lang.Double") ) {
            c = java.lang.Double.class;
        } else if ( classType.equals("java.lang.Float") ) {
            c = java.lang.Float.class;
        } else if ( classType.equals("java.lang.Long") ) {
            c = java.lang.Long.class;
        } else if ( classType.equals("java.lang.Short") ) {
            c = java.lang.Short.class;
        } else if ( classType.equals("java.math.BigDecimal") ) {
            c = java.math.BigDecimal.class;
        }
        
        return c;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox automaticlyReadFieldsCheckBox;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel columnsErrorMsgLabel;
    private javax.swing.JScrollPane columnsErrorScrollPane;
    private javax.swing.JScrollPane columnsScrollPane;
    private javax.swing.JButton exportQueryButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonLoadQuery;
    private javax.swing.JButton jButtonOpenDesigner;
    private javax.swing.JButton jButtonPreview;
    private javax.swing.JButton jButtonReadBeanAttributes3;
    private javax.swing.JButton jButtonRefreshData;
    private javax.swing.JButton jButtonSaveQuery;
    private javax.swing.JComboBox jComboBoxMaxPreviewData;
    private javax.swing.JComboBox jComboBoxQueryType;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private org.jdesktop.swingx.JXBusyLabel jLabelStatusPreview;
    private org.jdesktop.swingx.JXBusyLabel jLabelStatusSQL;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelPreview;
    private javax.swing.JPanel jPanelQueryArea;
    private javax.swing.JPanel jPanelSQL;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPanePreview;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableData;
    private org.jdesktop.swingx.JXTable jTableFields;
    private javax.swing.JTextField jTextFieldBeanClass1;
    private javax.swing.JButton okButton;
    private javax.swing.JButton readFieldsButton;
    // End of variables declaration//GEN-END:variables
    
    private boolean isSettingSQLExpression = false;
    
    
    
    public void lostOwnership (Clipboard parClipboard, Transferable parTransferable) { }

    public static Object recursiveInterpreter(Interpreter interpreter, String expression, List parameters) throws EvalError
    {
        return recursiveInterpreter(interpreter, expression, parameters, 0);
    }
    
    public static  Object recursiveInterpreter(Interpreter interpreter, String expression, List parameters, int recursion_level) throws EvalError
    {
        return recursiveInterpreter(interpreter, expression, parameters, 0, null);
    }
    
    public static  Object recursiveInterpreter(Interpreter interpreter, String expression, List parameters, int recursion_level, String this_param_name) throws EvalError
    {
        ++recursion_level;
        
        if (expression == null || expression.length() == 0) return null;
        
        //System.out.println("Valuto ["+ recursion_level +"]: " + expression);
        if (recursion_level > 100) return null;
        if (expression != null && expression.trim().length() > 0)
        {
            // for each parameter, we have to calc the real value...
            while (expression.indexOf("$P{") >= 0)
            {
                int start_index = expression.indexOf("$P{")+3;
                String param_name = expression.substring(start_index, expression.indexOf("}", start_index) );
                String param_expression = "";
                for (int i=0; i<parameters.size(); ++i)
                {
                    JRDesignParameter p = (JRDesignParameter)parameters.get(i);
                    if (p.getName().equals( param_name))
                    {
                        param_expression = p.getDefaultValueExpression().getText();
                        break;
                    }
                }
                
                String param_name_literal = "param_" + net.sf.jasperreports.engine.util.JRStringUtil.getJavaIdentifier(param_name);
                
                expression = Misc.string_replace( param_name_literal, "$P{"+param_name+"}", expression);
                //interpreter.set( param_name_literal, recursiveInterpreter(interpreter, param_expression, parameters, recursion_level));
            
                // If the parameter was never evaluated before, that can happen is some cases,
                // evaluate it now!
                if (interpreter.get(param_name_literal) == null)
                {
                    Object paramValue = recursiveInterpreter(interpreter, param_expression, parameters, recursion_level, this_param_name);
                    interpreter.set(param_name_literal, paramValue);
                }
            }
            
            String this_param_name_literal = "param_unknow";
            
            if (this_param_name!= null) 
            {
                this_param_name_literal = "param_" + net.sf.jasperreports.engine.util.JRStringUtil.getJavaIdentifier(this_param_name);
            } 
            //System.out.println("interpreto ["+ recursion_level +"]: " + expression);
            //System.out.flush();
            Object res = interpreter.eval(expression);
            interpreter.set(this_param_name_literal, res);
            //System.out.println("Result: " + res);
            //System.out.flush();
            return res;
        }
        return null;
    }

    private JRDesignDataset dataset;

    public JRDesignDataset getDataset() {
        return dataset;
    }

    @SuppressWarnings("unchecked")
    public void setDataset(JRDesignDataset dataset) {

        System.out.println("Set Dataset: " + dataset);
        System.out.flush();

        isSettingSQLExpression = true;
        try { // Used only to perform a finally op
            this.dataset = dataset;

            DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
            dtm.setRowCount(0); 

            num++;
            jLabelStatusSQL.setText( "" );
            jEditorPane1.setText("");

            try {
                automaticlyReadFieldsCheckBox.setSelected( IReportManager.getPreferences().getBoolean( IReportManager.USE_AUTO_REGISTER_FIELDS , true)  );
                //automaticlyReadFieldsCheckBoxActionPerformed(null);
            } catch (Exception ex)
            {

            }
            // Load query...
            if (dataset == null)
                this.jEditorPane1.setText("");
            else
            {
                // Use query, and use existing field list. ie Dont load from DB
                String query = "";
                String language = "sql";
                if (this.dataset.getQuery() != null && 
                    this.dataset.getQuery().getText() != null)
                {
                    query = this.dataset.getQuery().getText();
                    language = this.dataset.getQuery().getLanguage();
                    if (language == null || language.length() == 0) language = "sql";
                }
                this.jEditorPane1.setText(query);

                List columns = new ArrayList();
                Iterator i = dataset.getFieldsList().iterator();
                while( i.hasNext() ) {
                    JRDesignField field = (JRDesignField)i.next();
                    columns.add( new Object[]{field, field.getValueClassName(), field.getDescription()} );
                }
                setColumns( columns );

                for (int ix=0; ix<jComboBoxQueryType.getItemCount(); ++ix)
                {
                   if (!(jComboBoxQueryType.getItemAt(ix) instanceof Tag))
                   {
                    jComboBoxQueryType.removeItemAt(ix);
                    ix--;
                   }
                }

                boolean found = false;
                for (int ix=0; ix<jComboBoxQueryType.getItemCount(); ++ix)
                {
                   Tag t = (Tag)jComboBoxQueryType.getItemAt(ix);
                   String lang = (""+t.getValue()).toLowerCase();
                   if (lang.equals(language.toLowerCase()))
                   {
                       found = true;
                       jComboBoxQueryType.setSelectedIndex(ix);
                       break;
                   }
                }
                if (!found) // Default is sql...
                {
                    jComboBoxQueryType.setEditable(true);
                    jComboBoxQueryType.setSelectedItem(language);
                }

                jLabelStatusSQL.setText("");
 
            }

            jLabelStatusSQL.setText("setting jComboBoxQueryTypeActionPerformed");
            jComboBoxQueryTypeActionPerformed(null);
        
        } finally {
            
            isSettingSQLExpression = false;
        }
    }
    
    /*
    public void applyI18n(){
                // Start autogenerated code ----------------------
                automaticlyReadFieldsCheckBox.setText(I18n.getString("reportQueryDialog.utomaticlyReadFieldsCheckBox","Automatically Retrieve Fields"));
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                cancelButton.setText(I18n.getString("reportQueryDialog.ancelButton","Cancel"));
                exportQueryButton.setText(I18n.getString("reportQueryDialog.xportQueryButton","Send to clipboard"));
                jButton1.setText(I18n.getString("reportQueryDialog.button1","Filter expression..."));
                jButton2.setText(I18n.getString("reportQueryDialog.button2","Get fields from datasource"));
                jButton3.setText(I18n.getString("reportQueryDialog.button3","Get fields from datasource"));
                jButton4.setText(I18n.getString("reportQueryDialog.button4","Sort options..."));
                jButtonLoadQuery.setText(I18n.getString("reportQueryDialog.buttonLoadQuery","Load query"));
                jButtonReadBeanAttributes3.setText(I18n.getString("reportQueryDialog.buttonReadBeanAttributes3","Read attributes"));
                jButtonSaveQuery.setText(I18n.getString("reportQueryDialog.buttonSaveQuery","Save query"));
                jLabel1.setText(I18n.getString("reportQueryDialog.label1","Query language"));
                jLabel3.setText(I18n.getString("reportQueryDialog.label3","Class name"));
                okButton.setText(I18n.getString("reportQueryDialog.kButton","OK"));
                readFieldsButton.setText(I18n.getString("reportQueryDialog.eadFieldsButton","Read Fields"));
                // End autogenerated code ----------------------
                
                jTableFields.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("reportQueryDialog.tablecolumn.fieldName","Field name") );
                jTableFields.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("reportQueryDialog.tablecolumn.fieldType","Field type") );
                jTableFields.getColumnModel().getColumn(2).setHeaderValue( I18n.getString("reportQueryDialog.tablecolumn.description","Description") );
    
                jTabbedPane1.setTitleAt(0,it.businesslogic.ireport.util.I18n.getString("reportQueryDialog.tab.ReportQuery", "Report query"));
                jTabbedPane1.setTitleAt(1,it.businesslogic.ireport.util.I18n.getString("reportQueryDialog.tab.JavaBeanDatasource", "JavaBean Datasource"));
                jTabbedPane1.setTitleAt(2,it.businesslogic.ireport.util.I18n.getString("reportQueryDialog.tab.DataSourceProvider", "DataSource Provider"));
                jTabbedPane1.setTitleAt(3,it.businesslogic.ireport.util.I18n.getString("reportQueryDialog.tab.CSVDatasource", "CSV Datasource"));
    
                this.setTitle(I18n.getString("reportQueryDialog.title","Report query"));
                cancelButton.setMnemonic(I18n.getString("reportQueryDialog.cancelButtonMnemonic","c").charAt(0));
                okButton.setMnemonic(I18n.getString("reportQueryDialog.okButtonMnemonic","o").charAt(0));
    }
    */
    
    /**
     * Thie method can be useful for a CustomQueryEditor
     */
    public JEditorPane getQueryEditorPane()
    {
        return jEditorPane1;
    }
    
    /**
     * Thie method can be useful for a CustomQueryEditor
     * Return the table containing all the fields.
     */
    public JTable getFieldsTable()
    {
        return jTableFields;
    }

    /**
     * This method is used to open the filter expression in case of errors...
     */
    public void openFilterExpressionDialog(boolean showAsError) {
        if (getDataset() == null) return;
        ExpressionEditor fed = new ExpressionEditor();
        fed.setExpressionContext(new ExpressionContext(this.getDataset()));
        //FilterExpressionDialog fed = new FilterExpressionDialog(this, true);
        String expText = "";
        if (this.getDataset().getFilterExpression() != null)
        {
            expText = this.getDataset().getFilterExpression().getText();
        }
        fed.setExpression( expText);
        
//        if (showAsError)
//        {
//            fed.setFocusedExpression( FilterExpressionDialog.COMPONENT_EXPRESSION);
//        }
        
        if (fed.showDialog(this) == JOptionPane.OK_OPTION)
        {
            if (fed.getExpression().trim().length() == 0)
            {
                 getDataset().setFilterExpression(null);
            }
            else
            {
                JRDesignExpression exp = new JRDesignExpression();
                exp.setText(fed.getExpression());
                exp.setValueClassName("java.lang.Boolean");
                getDataset().setFilterExpression( exp );
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void addField(JRField field) {
        
        // Add the field if there is not already a fiels with the same name...
        if (field == null) return;
        
        if (columnsErrorMsgLabel.isVisible())
        {
            columnsErrorMsgLabel.setText( "" );
            jPanel2.remove( columnsErrorScrollPane );
            jPanel2.add( columnsScrollPane, java.awt.BorderLayout.CENTER );
            jPanel2.revalidate();
        }
        
        DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
        for (int i=0; i<dtm.getRowCount(); ++i)
        {
            JRDesignField tmpField = (JRDesignField)dtm.getValueAt(i, 0);
            if (tmpField.getName().equals(field.getName())) return;
        }
        Vector row = new Vector();
        row.addElement(field);
        row.addElement(field.getValueClassName());
        row.addElement(field.getDescription());
        dtm.addRow(row);
        
        jTableFields.addRowSelectionInterval(jTableFields.getRowCount()-1, jTableFields.getRowCount()-1);
  
        jTableFields.updateUI();                
    }

    public void setVisible(boolean b)
    {
        try {
            setAdjustingFrameSize(true);
            if (b)
            {
                // Check for the last used size...
                int w = IReportManager.getPreferences().getInt("ReportQueryDialog.size.width", getPreferredSize().width);
                int h = IReportManager.getPreferences().getInt("ReportQueryDialog.size.height", getPreferredSize().height);
                int x = IReportManager.getPreferences().getInt("ReportQueryDialog.position.x", getLocation().x);
                int y = IReportManager.getPreferences().getInt("ReportQueryDialog.position.y", getLocation().y);
                boolean preview = IReportManager.getPreferences().getBoolean("ReportQueryDialog.preview.open",false);
                int dividerLocation = IReportManager.getPreferences().getInt("ReportQueryDialog.divider.location", -1);

                setWinMaximized(IReportManager.getPreferences().getBoolean("ReportQueryDialog.position.maximized", false));

                // Multi screen is not handled here...
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

                // Adjust size based on the screen size...
                w = Math.min(w,dim.width);
                h = Math.min(h, dim.height);

                // Adjust position based on screen size and window size...
                x = Math.min( x, dim.width - w);
                y = Math.min( y, dim.height - y);

                setNormalBounds(new Rectangle(x, y, w, h));

                if (!isWinMaximized())
                {
                    this.setBounds(getNormalBounds());
                }
                else
                {
                    this.setBounds(Misc.getMainFrame().getBounds());
                }

                if (preview)
                {
                    final int last = dividerLocation;
                    jSplitPanePreview.setDividerSize(5);
                    //Dimension d = getSize();
                    //d.height += (lastPreviewSize > 0) ? lastPreviewSize : 300;
                    //this.setSize(d);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {

                            System.out.println("Setting divider at " + last);
                            jSplitPanePreview.setDividerLocation(last);
                            setShowPreview(true);
                        }
                    });
                }

            }
            else
            {
                // Store position and location...

                if (getNormalBounds() != null)
                {
                    IReportManager.getPreferences().putInt("ReportQueryDialog.size.width", getSize().width);
                    IReportManager.getPreferences().putInt("ReportQueryDialog.size.height", getSize().height);
                    IReportManager.getPreferences().putInt("ReportQueryDialog.position.x", getLocation().x);
                    IReportManager.getPreferences().putInt("ReportQueryDialog.position.y", getLocation().y);
                }

                IReportManager.getPreferences().putBoolean("ReportQueryDialog.position.maximized", isWinMaximized());
                IReportManager.getPreferences().putBoolean("ReportQueryDialog.preview.open", isShowPreview());
                IReportManager.getPreferences().putInt("ReportQueryDialog.divider.location", jSplitPanePreview.getDividerLocation());
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally {

            setAdjustingFrameSize(false);
            super.setVisible(b);
        }
    }
}



// The Undo action
  class UndoAction extends AbstractAction {
    public UndoAction(UndoManager manager) {
      this.manager = manager;
    }

    public void actionPerformed(ActionEvent evt) {
      try {
        manager.undo();
      } catch (CannotUndoException e) {
        Toolkit.getDefaultToolkit().beep();
      }
    }

    private UndoManager manager;
  }

  // The Redo action
  class RedoAction extends AbstractAction {
    public RedoAction(UndoManager manager) {
      this.manager = manager;
    }

    public void actionPerformed(ActionEvent evt) {
      try {
        manager.redo();
      } catch (CannotRedoException e) {
        Toolkit.getDefaultToolkit().beep();
      }
    }

    private UndoManager manager;
  }
