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
package com.jaspersoft.ireport.designer.editor;

import bsh.ParseException;
import bsh.Parser;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabParameter;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignDataset;

/**
 *
 * @author  gtoffoli
 */
public class ExpressionEditor extends javax.swing.JPanel {
    
    public static final String USER_DEFINED_EXPRESSIONS = "USER_DEFINED_EXPRESSIONS";
    public static final String PARAMETERS = "PARAMETERS";
    public static final String FIELDS = "FIELDS";
    public static final String VARIABLES = "VARIABLES";
    public static final String RECENT_EXPRESSIONS = "RECENT_EXPRESSIONS";
    public static final String WIZARDS = "WIZARDS";
    
    private ExpressionContext expressionContext = null;
    private JDialog dialog = null;
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    private boolean refreshingContext = false;
    
    public String getExpression() {
        return jEditorPane1.getText();
    }

    public void setExpression(String expression) {
        jEditorPane1.setText(expression);
        checkSyntax();
    }

    public static ArrayList<String> getPredefinedExpressions() {


        ArrayList<String> exps = new ArrayList<String>();
        Preferences pref = IReportManager.getPreferences();
        if (pref.getBoolean("custom_expressions_set", false))
        {
            for (int i=0; pref.get("customexpression."+i, null) != null; ++i)
            {
                exps.add(pref.get("customexpression."+i, ""));
            }
        }
        else
        {
            exps.addAll(getDefaultPredefinedExpressions());
        }
        return exps;
    }

    public static ArrayList<String> getDefaultPredefinedExpressions() {
        return defaultExpressions;
    }

    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    public void setExpressionContext(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
        this.jEditorPane1.setExpressionContext(expressionContext);

         refreshContext();

        
    }
    
    private static java.util.ArrayList<String> recentExpressions = new java.util.ArrayList<String>();
    private static java.util.ArrayList<String> defaultExpressions = null;
    
    static {
        defaultExpressions = new java.util.ArrayList<String>();
        defaultExpressions.add("( <condition> ? exp1 : exp2 )");
        defaultExpressions.add("msg(<pattern>, <arg0>)");
        defaultExpressions.add("msg(<pattern>, <arg0>, <arg1>)");
        defaultExpressions.add("msg(<pattern>, <arg0>, <arg1>, <arg2>)");
        defaultExpressions.add("str(<key>)");
        defaultExpressions.add("((net.sf.jasperreports.engine.data.JRXmlDataSource)$P{REPORT_DATA_SOURCE}).subDataSource(<select expression>)");
        defaultExpressions.add("((net.sf.jasperreports.engine.data.JRXmlDataSource)$P{REPORT_DATA_SOURCE}).dataSource(<select expression>)");
    }

    
    
    /** Creates new form ExpressionEditorPanel */
    public ExpressionEditor() {
        initComponents();
        
        jButtonApply.setVisible(false);
        jButtonCancel.setVisible(false);

        jList1.setCellRenderer(new NamedIconItemCellRenderer());
        jList2.setModel(new DefaultListModel());
        jList3.setModel(new DefaultListModel());
        jList1.setModel(new DefaultListModel());
        
        jList2.setCellRenderer(new ExpObjectCellRenderer(jList2));
        jList3.setCellRenderer(new MethodsListCellRenderer((jList3)));


        jToggleButtonBuiltinParameters.setSelected(
                IReportManager.getPreferences().getBoolean("filter_parameters_in_editor", false));

        jToggleButtonBuiltinVariables.setSelected(
                IReportManager.getPreferences().getBoolean("filter_variables_in_editor", false));
        

        //jList4.setModel(new DefaultListModel());

        /*
        jEditorPane1.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                refreshTokensList();
            }

            public void removeUpdate(DocumentEvent e) {
                refreshTokensList();
            }

            public void changedUpdate(DocumentEvent e) {
                refreshTokensList();
            }
        });
        */

        jEditorPane1.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent e) {
                //refreshTokensList();
                int y = 0;
                int x = 0;
                try {
                    String text = jEditorPane1.getText(0, jEditorPane1.getCaretPosition());
                    String[] lines = text.split("[\\r\\n]+");
                    y = lines.length;
                    x = lines[lines.length-1].length();
                } catch (Exception ex){}
                // Calculate caret position...
                jLabelCaretPosition.setText( "Ln " + y + ", Col " + x);
            }
        });


        jEditorPane1.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                checkSyntax();
            }

            public void removeUpdate(DocumentEvent e) {
                checkSyntax();
            }

            public void changedUpdate(DocumentEvent e) {
                checkSyntax();
            }
        } );

        refreshContext();
    }


    public void checkSyntax()
    {
        jLabelErrors.setForeground(Color.BLACK);
        jLabelErrors.setText(" ");
        String exp = jEditorPane1.getText();

        while (exp.length() > 0 && Character.isWhitespace(  exp.charAt(exp.length()-1)) )
        {
            exp = exp.substring(0,exp.length()-1);
        }
        if (exp.endsWith(";"))
        {
            Point p = findCaretPosition(exp);
            jLabelErrors.setForeground(Color.red.darker());
            jLabelErrors.setText("Invalid character ';' at line: " + p.y + ", column: " + p.x);
        }

        // Replace all the $x{xxx} with a valid variable name one by one
            // to keep the width...
        String[] patterns = new String[]{"$P{", "$V{", "$F{", "$R{"};
        String[] patternNames = new String[]{"Parameter", "Variable", "Field", "Resource"};

        for (int k=0; k<patterns.length; ++k)
        {
            while (exp.indexOf(patterns[k]) >= 0)
            {
                int initialIndex = exp.indexOf(patterns[k]);
                boolean endFound = false;
                for (int index = initialIndex; index >=0 && index < exp.length(); ++index)
                {
                    char c = exp.charAt(index);
                    exp = exp.substring(0,index) + "X" + exp.substring(index+1);

                    if (c == '}')
                    {
                        endFound = true;
                        break;
                    }
                }
                if (!endFound)
                {
                    String text = jEditorPane1.getText();
                    Point p = findCaretPosition(text, initialIndex);
                    jLabelErrors.setForeground(Color.red.darker());
                    jLabelErrors.setText("Report " + patternNames[k] + " reference not closed at line: " + p.y + ", column: " + p.x);
                    return;
                }
           }
        }

        exp += ";";
        Parser parser = new Parser(new StringReader(exp));
        try {
            
//            interpreter.eval(exp);
            parser.Line();

        } catch (ParseException  ex)
        {
            bsh.Token errorToken = null;
            bsh.Token tmp = parser.getToken(0);
            for (int i=1; tmp != null; i++)
            {
                errorToken = tmp;
                tmp = tmp.next;
            }
            String message = ex.getMessage() +  "\n";
            if (errorToken != null)
            {

                Point p = findCaretPosition(exp);
                if (p.y == errorToken.beginLine && p.x == errorToken.beginColumn)
                {
                    message = "Incomplete expression.";
                }
            }
            jLabelErrors.setForeground(Color.red.darker());
            jLabelErrors.setText(message);

        } catch (Throwable  ex) {}
    }

    /**
     * Return column/line
     * @param text
     * @param position
     * @return
     */
    private Point findCaretPosition(String text)
    {
        Point p = new Point(0,1);
        String[] lines = text.split("[\\r\\n]+");
        p.y = lines.length;
        p.x = lines[lines.length-1].length();
        return p;
    }

    /**
     * Return column/line
     * @param text
     * @param position
     * @return
     */
    private Point findCaretPosition(String text, int position)
    {
        return findCaretPosition(text.substring(0,position));
    }

    /*
    public void refreshTokensList()
    {
        DefaultListModel model = (DefaultListModel)jList4.getModel();
        model.removeAllElements();

        AbstractDocument document = (AbstractDocument)jEditorPane1.getDocument();
        document.readLock();
        try {

            TokenHierarchy th = TokenHierarchy.get(document);
            TokenSequence ts = th.tokenSequence();

            int caretPos = jEditorPane1.getCaretPosition();
            model.addElement("Current position: "  + jEditorPane1.getCaretPosition());

            Token tokenAtPosition = null;
            Token previousPositionToken = null;
            String textFromLastValidToked = "";
            try {
                textFromLastValidToked = document.getText(0, caretPos);
            } catch (BadLocationException ex) {
                //Exceptions.printStackTrace(ex);
            }

            ts.moveStart();
            int pos = 0;
            while (ts.moveNext())
            {
                Token t = ts.token();
                if (t.length() <= 0) continue; // skip null tokens...
                int t_start = pos;
                int t_end = pos + t.length();
                pos = t_end;

                if (caretPos > t_start && caretPos <= t_end)
                {
                    tokenAtPosition = t;
                    model.addElement("**" + t.id().name() + " " + t.text() + " {"  + t_start + " - " + t_end + "}");
                }
                else
                {
                    model.addElement(t.id().name() + " " + t.text() + " {"  + t_start + " - " + t_end + "}");
                }
                
                if (caretPos > t_end)
                {
                    previousPositionToken = t;
                    try {
                        textFromLastValidToked = document.getText(t_end, caretPos - t_end);
                    } catch (BadLocationException ex) {
                        //Exceptions.printStackTrace(ex);
                    }
                }
            }

            if (previousPositionToken != null)
            {
                model.insertElementAt("PT: " + previousPositionToken.id().name() + " " + previousPositionToken.text(),1);
            }
            else
            {
                model.insertElementAt( "NO PT",1);
            }
            if (tokenAtPosition != null)
            {
                model.insertElementAt("CT: " + tokenAtPosition.id().name() + " " + tokenAtPosition.text(),1);
            }
            else
            {
                model.insertElementAt("NO CT",1);
            }
            
            model.insertElementAt("Pre text:" + textFromLastValidToked,1);

        } finally {
            document.readUnlock();
        }


    }
    */
    
    /**
     *  Refresh the content of the expression editor based on the current
     *  ExpressionContext.
     */
    public void refreshContext()
    {
        setRefreshingContext(true);
        jList2.removeAll();
        jList3.removeAll();
        jList1.removeAll();
        
        DefaultListModel dlm1 = (DefaultListModel)jList1.getModel();
        dlm1.removeAllElements();
        
        if (getExpressionContext() != null)
        {
            // Aggregate the datasets....
            if (getExpressionContext().getDatasets().size() > 0)
            {
                JRDesignDataset ds = getExpressionContext().getDatasets().get(0);
                dlm1.addElement(new NamedIconItem(PARAMETERS, I18n.getString("ExpressionEditor.IconName.Parameters"), NamedIconItem.ICON_FOLDER_PARAMETERS) );
                dlm1.addElement( new NamedIconItem(FIELDS, I18n.getString("ExpressionEditor.IconName.Fields"), NamedIconItem.ICON_FOLDER_FIELDS) );
                dlm1.addElement( new NamedIconItem(VARIABLES, I18n.getString("ExpressionEditor.IconName.Variables"), NamedIconItem.ICON_FOLDER_VARIABLES) );
            }
            
            int i = 0;
            for (JRDesignCrosstab crosstab : getExpressionContext().getCrosstabs())
            {
                i++;
                String key = crosstab.getKey();
                if (key == null) key = "";
                
                dlm1.addElement(new NamedIconItem(crosstab, I18n.getString("ExpressionEditor.IconName.Crosstab") + i + ") " + key, NamedIconItem.ICON_CROSSTAB) );
            }
        }
        
        dlm1.addElement(new NamedIconItem(USER_DEFINED_EXPRESSIONS, I18n.getString("ExpressionEditor.IconName.UserDefinedExpr"), NamedIconItem.ICON_FOLDER_FORMULAS ) );
        dlm1.addElement( new NamedIconItem(RECENT_EXPRESSIONS, I18n.getString("ExpressionEditor.IconName.RecentExpr"), NamedIconItem.ICON_FOLDER_RECENT_EXPRESSIONS ) );
        dlm1.addElement( new NamedIconItem(WIZARDS, I18n.getString("ExpressionEditor.IconName.ExprWiz"), NamedIconItem.ICON_FOLDER_WIZARDS ) );
        
        jList1.updateUI();

        setRefreshingContext(false);
        // If there are fields, select the fields node by default
        try {
            if (dlm1.getSize() > 0)
            {
                if (((NamedIconItem)(dlm1.getElementAt(1))).getItem().equals(FIELDS))
                {
                    JRDesignDataset ds = getExpressionContext().getDatasets().get(0);
                    if (ds.getFieldsList().size() > 0)
                    {
                        jList1.setSelectedIndex(1);
                    }
                    else
                    {
                        jList1.setSelectedIndex(0);
                    }
                }
                else
                {
                    jList1.setSelectedIndex(0);
                }
            }
        } catch (Exception ex) {}       
        
    }
    
    
    
    public String getPrintableTypeName( String type )
    {
            if (type == null) return "void";

            if (type.endsWith(";")) type = type.substring(0,type.length()-1);
    
            while (type.startsWith("["))
            {
                type = type.substring(1) + "[]";
                if (type.startsWith("[")) continue;
                if (type.startsWith("L")) type = type.substring(1);
                if (type.startsWith("Z")) type = "boolean" + type.substring(1);
                if (type.startsWith("B")) type = "byte" + type.substring(1);
                if (type.startsWith("C")) type = "char" + type.substring(1);
                if (type.startsWith("D")) type = "double" + type.substring(1);
                if (type.startsWith("F")) type = "float" + type.substring(1);
                if (type.startsWith("I")) type = "int" + type.substring(1);
                if (type.startsWith("J")) type = "long" + type.substring(1);
                if (type.startsWith("S")) type = "short" + type.substring(1);
            }
            
            if (type.startsWith("java.lang."))
            {
                type = type.substring("java.lang.".length());
                if (type.indexOf(".") > 0)
                {
                    type = "java.lang." + type;
                }
            }
            return type;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new com.jaspersoft.ireport.designer.editor.ExpressionEditorPane();
        jPanel3 = new javax.swing.JPanel();
        jLabelErrors = new javax.swing.JLabel();
        jLabelCaretPosition = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jToolBar2 = new javax.swing.JToolBar();
        jToggleButtonBuiltinParameters = new javax.swing.JToggleButton();
        jToggleButtonBuiltinVariables = new javax.swing.JToggleButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButtonImport = new javax.swing.JButton();
        jButtonExport = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonApply = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(550, 450));

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.8);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jEditorPane1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jEditorPane1CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        jScrollPane1.setViewportView(jEditorPane1);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabelErrors.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor.class, "ExpressionEditor.jLabelErrors.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jLabelErrors, gridBagConstraints);

        jLabelCaretPosition.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor.class, "ExpressionEditor.jLabelCaretPosition.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(jLabelCaretPosition, gridBagConstraints);

        jPanel2.add(jPanel3, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setLeftComponent(jPanel2);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jSplitPane2.setDividerSize(6);
        jSplitPane2.setResizeWeight(0.3);

        jSplitPane3.setBorder(null);
        jSplitPane3.setDividerSize(6);
        jSplitPane3.setResizeWeight(0.5);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setBorder(null);

        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
        });
        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jList2);

        jPanel4.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jToolBar2.setBorder(null);
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jToggleButtonBuiltinParameters.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/filter-parameters.png"))); // NOI18N
        jToggleButtonBuiltinParameters.setToolTipText(org.openide.util.NbBundle.getMessage(ExpressionEditor.class, "ExpressionEditor.jToggleButtonBuiltinParameters.toolTipText")); // NOI18N
        jToggleButtonBuiltinParameters.setBorderPainted(false);
        jToggleButtonBuiltinParameters.setFocusPainted(false);
        jToggleButtonBuiltinParameters.setFocusable(false);
        jToggleButtonBuiltinParameters.setMargin(new java.awt.Insets(0, 6, 0, 6));
        jToggleButtonBuiltinParameters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonBuiltinParametersActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButtonBuiltinParameters);

        jToggleButtonBuiltinVariables.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/filter-variables.png"))); // NOI18N
        jToggleButtonBuiltinVariables.setToolTipText(org.openide.util.NbBundle.getMessage(ExpressionEditor.class, "ExpressionEditor.jToggleButtonBuiltinVariables.toolTipText")); // NOI18N
        jToggleButtonBuiltinVariables.setBorderPainted(false);
        jToggleButtonBuiltinVariables.setFocusPainted(false);
        jToggleButtonBuiltinVariables.setFocusable(false);
        jToggleButtonBuiltinVariables.setMargin(new java.awt.Insets(0, 6, 0, 6));
        jToggleButtonBuiltinVariables.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonBuiltinVariablesActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButtonBuiltinVariables);

        jPanel4.add(jToolBar2, java.awt.BorderLayout.SOUTH);

        jSplitPane3.setLeftComponent(jPanel4);

        jScrollPane4.setBorder(null);

        jList3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList3MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jList3);

        jSplitPane3.setRightComponent(jScrollPane4);

        jSplitPane2.setRightComponent(jSplitPane3);

        jScrollPane2.setBorder(null);

        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        jSplitPane2.setLeftComponent(jScrollPane2);

        jPanel1.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel1);

        jButtonImport.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor.class, "ExpressionEditor.jButtonImport.text")); // NOI18N
        jButtonImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportActionPerformed(evt);
            }
        });

        jButtonExport.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor.class, "ExpressionEditor.jButtonExport.text")); // NOI18N
        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportActionPerformed(evt);
            }
        });

        jButtonCancel.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor.class, "ExpressionEditor.jButtonCancel.text")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonApply.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor.class, "ExpressionEditor.jButtonApply.text")); // NOI18N
        jButtonApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jButtonImport)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonExport)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 240, Short.MAX_VALUE)
                        .add(jButtonApply)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .add(6, 6, 6)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonImport)
                    .add(jButtonExport)
                    .add(jButtonCancel)
                    .add(jButtonApply))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jList2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList2ValueChanged

        if (isRefreshingContext()) return;
        DefaultListModel dlm = (DefaultListModel)jList3.getModel();
        dlm.removeAllElements();
        
        Class clazz = null; //getSelectedObjectClass();
        
        if (jList2.getSelectedValue() instanceof ExpObject)
        {
            try {
                clazz = IReportManager.getReportClassLoader().loadClass( ((ExpObject)jList2.getSelectedValue()).getClassType());
        
            } 
            catch (Throwable ex3)
            {
                
            }
        }
        
        if (clazz != null)
        {
            java.lang.reflect.Method[] methods = clazz.getMethods();
            for (int i=0; i<methods.length; ++i)
            {
                if ((methods[i].getModifiers() & java.lang.reflect.Modifier.PUBLIC) != 0 )
                {
                    String method_firm = methods[i].getName() + "(";
                    Class[] params = methods[i].getParameterTypes();
                    int j=0;
                    for (j=0; j<params.length; ++j)
                    {
                        
                        if (j > 0) method_firm +=", ";
                        else method_firm +=" ";
                        method_firm +=  getPrintableTypeName( params[j].getName() );
                    }
                    if (j>0) method_firm+=" ";
                    method_firm += ") ";

                    String rname = methods[i].getReturnType().getName();
                    if (rname.equals("void")) continue; // we have to return something always!
                    method_firm += getPrintableTypeName( rname);
                    dlm.addElement( method_firm );
                }
            }
        }
        
        
    }//GEN-LAST:event_jList2ValueChanged

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
       
        if (dialog != null && dialog.isVisible()) 
        {
            dialogResult = JOptionPane.CANCEL_OPTION;
            dialog.setVisible(false);
            dialog.dispose();
        }
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jList3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList3MouseClicked
       if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() == 2)
        {
            if (jList2.getSelectedValue() != null && jList3.getSelectedValue() != null)
            {
                try {
                    
                    String objName = "";
                    if (jList2.getSelectedValue() instanceof ExpObject)
                    {
                        objName = ((ExpObject)jList2.getSelectedValue()).getExpression();
                    }
                    else
                    {
                        objName = ""+jList2.getSelectedValue();
                    }
                    
                    String method = (jList3.getSelectedValue()+"");
                    method = method.substring(0, method.lastIndexOf(")")+1);
                    // Remove selected text...
                    jEditorPane1.replaceSelection(objName+"."+method);
            } catch (Exception ex){}
            }
        }
    }//GEN-LAST:event_jList3MouseClicked

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked
        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() == 2)
        {
            try {
                    String objName = "";
                    if (jList2.getSelectedValue() instanceof ExpObject)
                    {
                        objName = ((ExpObject)jList2.getSelectedValue()).getExpression();
                    }
                    else
                    {
                        objName = ""+jList2.getSelectedValue();
                    }
                    
                    jEditorPane1.replaceSelection(objName+"");
             } catch (Exception ex){}
        }
    }//GEN-LAST:event_jList2MouseClicked

    private void jButtonApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyActionPerformed

        if (dialog != null && dialog.isVisible())
        {
            // Add the expression to the recent expressions list...
            String exp = getExpression();
            recentExpressions.remove(exp);
            recentExpressions.add(0,exp);

            dialogResult = JOptionPane.OK_OPTION;
            dialog.setVisible(false);
            dialog.dispose();
        }
        
    }//GEN-LAST:event_jButtonApplyActionPerformed

    private void jButtonImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportActionPerformed
        String expression = Misc.loadExpression(this);

        if (expression != null) {
            jEditorPane1.setText(expression);
        }
    }//GEN-LAST:event_jButtonImportActionPerformed

    private void jButtonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportActionPerformed
        Misc.saveExpression( jEditorPane1.getText(), this );
    }//GEN-LAST:event_jButtonExportActionPerformed

    private void jEditorPane1CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jEditorPane1CaretPositionChanged

    }//GEN-LAST:event_jEditorPane1CaretPositionChanged

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged

        if (isRefreshingContext()) return;

        setRefreshingContext(true);
        DefaultListModel dlm2 = new DefaultListModel(); //(DefaultListModel)jList2.getModel();
        DefaultListModel dlm3 = (DefaultListModel)jList3.getModel();

        dlm2.removeAllElements();
        dlm3.removeAllElements();

        jToggleButtonBuiltinParameters.setEnabled(false);
        jToggleButtonBuiltinVariables.setEnabled(false);


        if (jList1.getSelectedValue() != null) {
            NamedIconItem item = (NamedIconItem)jList1.getSelectedValue();
            if (item.getItem().equals( USER_DEFINED_EXPRESSIONS)) {
                ArrayList<String> exps = getPredefinedExpressions();
                for (String s : exps) dlm2.addElement(s);
            } else if (item.getItem().equals( RECENT_EXPRESSIONS)) {
                for (String s : recentExpressions) dlm2.addElement(s);
            } else if (item.getItem().equals( PARAMETERS)) {
                jToggleButtonBuiltinParameters.setEnabled(true);
                JRDesignDataset ds = getExpressionContext().getDatasets().get(0);
                Iterator parameters = ds.getParametersList().iterator();
                while (parameters.hasNext()) {
                    JRParameter parameter = (JRParameter) parameters.next();
                    if (parameter.isSystemDefined() && jToggleButtonBuiltinParameters.isSelected()) continue;
                    dlm2.addElement(new ExpObject(parameter));
                }
            } else if (item.getItem().equals( FIELDS)) {
                JRDesignDataset ds = getExpressionContext().getDatasets().get(0);
                Iterator fields = ds.getFieldsList().iterator();
                while (fields.hasNext()) {
                    ExpObject eo = new ExpObject(fields.next());
                    dlm2.addElement(eo);
                }
            } else if (item.getItem().equals( VARIABLES)) {
                jToggleButtonBuiltinVariables.setEnabled(true);
                JRDesignDataset ds = getExpressionContext().getDatasets().get(0);
                Iterator variables = ds.getVariablesList().iterator();
                while (variables.hasNext()) {

                    JRVariable variable = (JRVariable) variables.next();
                    if (variable.isSystemDefined() && jToggleButtonBuiltinParameters.isSelected()) continue;
                    dlm2.addElement(new ExpObject(variable));
                }
            } else if (item.getItem() instanceof JRDesignCrosstab) {
                JRDesignCrosstab crosstab = (JRDesignCrosstab)item.getItem();
                List rowGroups = crosstab.getRowGroupsList();
                List columnGroups = crosstab.getColumnGroupsList();

                Iterator measures = crosstab.getMesuresList().iterator();
                while (measures.hasNext()) {
                    JRDesignCrosstabMeasure measure = (JRDesignCrosstabMeasure)measures.next();
                    dlm2.addElement(new ExpObject(measure.getVariable()));

                    for (int i=0; i<rowGroups.size(); ++i) {
                        JRDesignCrosstabRowGroup rowGroup = (JRDesignCrosstabRowGroup)rowGroups.get(i);
                        dlm2.addElement(new CrosstabTotalVariable(measure, rowGroup, null));


                        for (int j=0; j<columnGroups.size(); ++j) {
                            JRDesignCrosstabColumnGroup columnGroup = (JRDesignCrosstabColumnGroup)columnGroups.get(j);
                            if (j==0) {
                                dlm2.addElement(new CrosstabTotalVariable(measure, null, columnGroup));
                            }

                            dlm2.addElement(new CrosstabTotalVariable(measure, rowGroup, columnGroup));
                        }
                    }
                }

                for (int i=0; i<rowGroups.size(); ++i) {
                    JRDesignCrosstabRowGroup rowGroup = (JRDesignCrosstabRowGroup)rowGroups.get(i);
                    dlm2.addElement(new ExpObject(rowGroup.getVariable()));
                }

                for (int i=0; i<columnGroups.size(); ++i) {
                    JRDesignCrosstabColumnGroup columnGroup = (JRDesignCrosstabColumnGroup)columnGroups.get(i);
                    dlm2.addElement(new ExpObject(columnGroup.getVariable()));
                }

                List crosstabParameters = crosstab.getParametersList();
                for (int i=0; i<crosstabParameters.size(); ++i) {
                    JRDesignCrosstabParameter parameter = (JRDesignCrosstabParameter)crosstabParameters.get(i);
                    dlm2.addElement(new ExpObject(parameter));
                }

            }
            // TODO -> Wizards
            jList2.setModel(dlm2);

            setRefreshingContext(false);

            if (dlm2.size() > 0) {
                jList2.setSelectedIndex(0);
            }

        }

    }//GEN-LAST:event_jList1ValueChanged

    private void jToggleButtonBuiltinParametersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonBuiltinParametersActionPerformed
        IReportManager.getPreferences().putBoolean("filter_parameters_in_editor", jToggleButtonBuiltinParameters.isSelected());
        jList1ValueChanged(null);
}//GEN-LAST:event_jToggleButtonBuiltinParametersActionPerformed

    private void jToggleButtonBuiltinVariablesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonBuiltinVariablesActionPerformed
        IReportManager.getPreferences().putBoolean("filter_variables_in_editor", jToggleButtonBuiltinVariables.isSelected());
        jList1ValueChanged(null);
}//GEN-LAST:event_jToggleButtonBuiltinVariablesActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonApply;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonExport;
    private javax.swing.JButton jButtonImport;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorPane jEditorPane1;
    private javax.swing.JLabel jLabelCaretPosition;
    private javax.swing.JLabel jLabelErrors;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButtonBuiltinParameters;
    private javax.swing.JToggleButton jToggleButtonBuiltinVariables;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Show the dialog to edit the expression.
     * If parent is not null, it is used to find a parent Window.
     */
    public int showDialog(Component parent)
    {
        jButtonApply.setVisible(true);
        jButtonCancel.setVisible(true);
        
        Window pWin = (parent != null) ? SwingUtilities.windowForComponent(parent) : null;
        
        if (pWin instanceof Dialog) dialog = new JDialog((Dialog)pWin);
        else if (pWin instanceof Frame) dialog = new JDialog((Frame)pWin);
        else dialog = new JDialog();
        
        dialog.setModal(true);
        

        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };

        dialog.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        dialog.getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);


        //to make the default button ...
        dialog.getRootPane().setDefaultButton(this.jButtonApply);

        dialog.getContentPane().add(this);
        dialog.pack();
        dialogResult = JOptionPane.CANCEL_OPTION;
        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        dialog.setTitle(I18n.getString("ExpressionEditor.Title.ExpressionEditor"));
        dialog.setVisible(true);

        return dialogResult;
    }
 
    @Override
    public void addNotify () {
        super.addNotify();
        //force focus to the editable area
        if (isEnabled() && isFocusable()) {
            jEditorPane1.requestFocus();
        }
    }

    /**
     * @return the refreshingContext
     */
    public boolean isRefreshingContext() {
        return refreshingContext;
    }

    /**
     * @param refreshingContext the refreshingContext to set
     */
    public void setRefreshingContext(boolean refreshingContext) {
        this.refreshingContext = refreshingContext;
    }
}

