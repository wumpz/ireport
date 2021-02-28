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
package com.jaspersoft.ireport.designer.options;

import com.jaspersoft.ireport.designer.IRLocalJasperReportsContext;
import com.jaspersoft.ireport.designer.fonts.SimpleFontFamilyEx;
import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ReportClassLoader;
import com.jaspersoft.ireport.designer.compatibility.JRXmlWriterHelper;
import com.jaspersoft.ireport.designer.data.queryexecuters.QueryExecuterDef;
import com.jaspersoft.ireport.designer.editor.ExpressionEditor;
import com.jaspersoft.ireport.designer.fonts.CheckBoxListEntry;
import com.jaspersoft.ireport.designer.fonts.EditFontPanel;
import com.jaspersoft.ireport.designer.fonts.OptionsFontListCellRenderer;
import com.jaspersoft.ireport.designer.fonts.IRFontUtils;
import com.jaspersoft.ireport.designer.fonts.InstallFontWizardDescriptor;
import com.jaspersoft.ireport.designer.options.export.ExportOptionsPanel;
import com.jaspersoft.ireport.designer.options.jasperreports.JROptionsPanel;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.tools.FieldPatternDialog;
import com.jaspersoft.ireport.designer.tools.LocaleSelectorDialog;
import com.jaspersoft.ireport.designer.tools.QueryExecuterDialog;
import com.jaspersoft.ireport.designer.tools.TimeZoneDialog;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.utils.Unit;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.prefs.Preferences;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.charts.ChartThemeBundle;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.design.JRCompiler;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fonts.SimpleFontFamily;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;

final class IReportPanel extends javax.swing.JPanel {

    private final IReportOptionsPanelController controller;

    private ExportOptionsPanel exportOptionsPanel = null;
    private JROptionsPanel jrOptionsPanel = null;

    private Locale currentReportLocale = null;
    private String currentReportTimeZoneId = null;

    private boolean init = false;

    public boolean setInit(boolean b)
    {
        boolean old = init;
        init =b;
        return old;
    }

    public boolean isInit()
    {
        return init;
    }

    /**
     * Notify a change in the UI.
     */
    public void notifyChange()
    {
        if (this.controller != null && !isInit())
        {
            this.controller.changed();
        }
    }
    
    IReportPanel(IReportOptionsPanelController ctlr) {
        this.controller = ctlr;
        initComponents();

        exportOptionsPanel = new ExportOptionsPanel(controller);
        addTab(I18n.getString("ExportOptionsPanel.title"), exportOptionsPanel);

        jrOptionsPanel = new JROptionsPanel(controller);
        addTab(I18n.getString("JROptionsPanel.title"), jrOptionsPanel);

        jTabbedPane1.setTitleAt(0,I18n.getString("OptionsTabs.general"));
        jTabbedPane1.setTitleAt(1,I18n.getString("OptionsTabs.classpath"));
        jTabbedPane1.setTitleAt(2,I18n.getString("OptionsTabs.fontpath"));
        jTabbedPane1.setTitleAt(3,I18n.getString("OptionsTabs.viewers"));
        jTabbedPane1.setTitleAt(4,I18n.getString("OptionsTabs.templates"));
        jTabbedPane1.setTitleAt(5,I18n.getString("OptionsTabs.compilation"));
        jTabbedPane1.setTitleAt(6,I18n.getString("OptionsTabs.queryexecuters"));


        jCheckBoxKeyInReportInspector.setText(I18n.getString("OptionsTabs.showelementkey"));

        // TODO listen to changes in form fields and call notifyChange()
        Unit[] units = Unit.getStandardUnits();
        for (int i=0; i<units.length; ++i)
        {
            jComboBoxUnits.addItem(new Tag(units[i].getKeyName(), units[i].getUnitName()));
        }
        
        //jListClassPath.setModel(new DefaultListModel());
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(300);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(70);


        jTableQueryExecuters.getColumnModel().getColumn(0).setPreferredWidth(70);
        jTableQueryExecuters.getColumnModel().getColumn(1).setPreferredWidth(250);
        jTableQueryExecuters.getColumnModel().getColumn(1).setPreferredWidth(250);


        jListFontspath.setModel(new DefaultListModel());
        jListTemplates.setModel(new DefaultListModel());
        
        jListFontspath.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
               notifyChange();
            }
         });

         jSpinnerVirtualizerSize.setModel(new javax.swing.SpinnerNumberModel(100,1,100000000,10));
         jSpinnerVirtualizerBlockSize.setModel(new javax.swing.SpinnerNumberModel(100,1,100000000,10));
         jSpinnerVirtualizerGrownCount.setModel(new javax.swing.SpinnerNumberModel(1,1,100000000,10));
         
         
         javax.swing.event.DocumentListener textfieldListener =  new javax.swing.event.DocumentListener()
                {
                    public void changedUpdate(javax.swing.event.DocumentEvent evt)
                    {
                        notifyChange();
                    }
                    public void insertUpdate(javax.swing.event.DocumentEvent evt)
                    {
                        notifyChange();
                    }
                    public void removeUpdate(javax.swing.event.DocumentEvent evt)
                    {
                        notifyChange();
                    }
                };
                
         jTextFieldPDFViewer.getDocument().addDocumentListener(textfieldListener);
         jTextFieldXLSViewer.getDocument().addDocumentListener(textfieldListener);
         jTextFieldTXTViewer.getDocument().addDocumentListener(textfieldListener);
         jTextFieldCSVViewer.getDocument().addDocumentListener(textfieldListener);
         jTextFieldODFViewer.getDocument().addDocumentListener(textfieldListener);
         jTextFieldRTFViewer.getDocument().addDocumentListener(textfieldListener);
         jTextFieldHTMLViewer.getDocument().addDocumentListener(textfieldListener);
         jTextFieldVirtualizerDir.getDocument().addDocumentListener(textfieldListener);
         jTextFieldCompilationDirectory.getDocument().addDocumentListener(textfieldListener);
         

         jComboBoxVirtualizer.addItem( new Tag("JRFileVirtualizer", I18n.getString("virtualizer.file") ));
         jComboBoxVirtualizer.addItem( new Tag("JRSwapFileVirtualizer",I18n.getString("virtualizer.swap")));
         jComboBoxVirtualizer.addItem( new Tag("JRGzipVirtualizer",I18n.getString("virtualizer.gzip")));

         //((TitledBorder)jPanelVirtualizer.getBorder()).setTitle(I18n.getString("optionsPanel.virtualizer.virtualizer"));
         jLabel2.setText(I18n.getString("optionsPanel.virtualizer.label1","Use this virtualizer"));
         jButtonVirtualizerDirBrowse.setText(  I18n.getString("optionsPanel.virtualizer.Browse","Browse"));
         jLabelReportVirtualizerDirectory.setText(  I18n.getString("optionsPanel.virtualizer.ReportVirtualizerDir","Directory where the paged out data is to be stored"));
         jLabelReportVirtualizerSize.setText(  I18n.getString("optionsPanel.virtualizer.ReportVirtualizerSize","Maximum size (in JRVirtualizable objects) of the paged in cache"));

         ((TitledBorder)jPanel23.getBorder()).setTitle(I18n.getString("optionsPanel.virtualizer.SwapFile"));
         jLabelReportVirtualizerSize1.setText(I18n.getString("optionsPanel.virtualizer.blockSize"));
         jLabelReportVirtualizerMinGrowCount.setText(I18n.getString("optionsPanel.virtualizer.growCount"));

         jCheckBoxCrosstabAutoLayout.setText(I18n.getString("optionsPanel.crosstabCellAutoLayout"));
         jCheckBoxUseReportDirectoryToCompile.setText(I18n.getString("optionsPanel.useReportDirToCompile"));
         jLabelCompilationDirectory.setText(I18n.getString("optionsPanel.compilationDirectory"));


         List tags = new java.util.ArrayList();
         tags.add(new Tag("", "<Template Default>"));//FIXMETD confusion between lower case and upper case values
         tags.add(new Tag(JasperDesign.LANGUAGE_JAVA, "Java"));//FIXMETD confusion between lower case and upper case values
         tags.add(new Tag(JasperDesign.LANGUAGE_GROOVY, "Groovy"));
         tags.add(new Tag("javascript", "JavaScript"));

         jComboBoxLanguage.setModel(new DefaultComboBoxModel(tags.toArray()));


         jTableQueryExecuters.getSelectionModel().addListSelectionListener( new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                jTableQueryExecutersSelectionChanged();
            }
        });

        jTableExpressions.getSelectionModel().addListSelectionListener( new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                jTableExpressionsSelectionChanged();
            }
        });

        jCheckBoxShowBackgroundAsSeparatedDocument.setText(I18n.getString("optionsPanel.checkBoxShowBackgroundAsSeparatedDocument"));
        jCheckBoxAskConfirmationOnDelete.setText(I18n.getString("optionsPanel.checkBoxAskConfirmationOnDelete"));
        //jTabbedPane1.remove(6);
        //jTabbedPane1.remove(5);

        jComboBoxCompatibility.addItem(new Tag("", "Last version"));
        
        
        List<String> versions = JRXmlWriterHelper.getJRVersions();
        Collections.sort(versions);
        
        for (String ver : versions)
        {
            jComboBoxCompatibility.addItem(new Tag(ver, "JasperReports" + ver));
        }
        
        
        jListFonts.setCellRenderer(new OptionsFontListCellRenderer());
        jListFonts.setModel(new DefaultListModel());

    }

    public void jTableQueryExecutersSelectionChanged()
    {
        jButtonModifyQueryExecuter.setEnabled( jTableQueryExecuters.getSelectedRowCount() > 0);
        jButtonRemoveQueryExecuter.setEnabled( jTableQueryExecuters.getSelectedRowCount() > 0);
    }

    public void jTableExpressionsSelectionChanged()
    {
        jButtonModifyExpression.setEnabled( jTableExpressions.getSelectedRowCount() > 0);
        jButtonRemoveExpression.setEnabled( jTableExpressions.getSelectedRowCount() > 0);
    }

    public void addTab(String title, JComponent component)
    {
        jTabbedPane1.add(title, component);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxUnits = new javax.swing.JComboBox();
        jCheckBoxMagneticGuideLines = new javax.swing.JCheckBox();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel24 = new javax.swing.JPanel();
        jLabelTimeZone1 = new javax.swing.JLabel();
        jComboBoxLanguage = new javax.swing.JComboBox();
        jComboBoxTheme = new javax.swing.JComboBox();
        jLabelTimeZone2 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jCheckBoxKeyInReportInspector = new javax.swing.JCheckBox();
        jCheckBoxCrosstabAutoLayout = new javax.swing.JCheckBox();
        jCheckBoxShowBackgroundAsSeparatedDocument = new javax.swing.JCheckBox();
        jCheckBoxAskConfirmationOnDelete = new javax.swing.JCheckBox();
        jCheckBoxDebugMode = new javax.swing.JCheckBox();
        jCheckBoxShowPositionErrors = new javax.swing.JCheckBox();
        jCheckBoxSaveZoom = new javax.swing.JCheckBox();
        jCheckBoxLabelForField = new javax.swing.JCheckBox();
        jPanel22 = new javax.swing.JPanel();
        jLabelExpressions = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableExpressions = new javax.swing.JTable();
        jButtonAddExpression = new javax.swing.JButton();
        jButtonModifyExpression = new javax.swing.JButton();
        jButtonRemoveExpression = new javax.swing.JButton();
        jButtonRestoreExpressions = new javax.swing.JButton();
        jCheckBoxUseSyntaxHighlighting = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jSpinnerEditorFontSize = new javax.swing.JSpinner();
        jPanelCompatibility = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxCompatibility = new javax.swing.JComboBox();
        jCheckBoxShowCompatibilityWarning = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabelLanguage = new javax.swing.JLabel();
        jTextFieldLanguage = new javax.swing.JTextField();
        jLabelTerritory = new javax.swing.JLabel();
        jTextFieldTerritory = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabelClasspath = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jButtonAddClasspathItem = new javax.swing.JButton();
        jButtonAddClasspathItem1 = new javax.swing.JButton();
        jButtonRemoveClasspathItem = new javax.swing.JButton();
        jButtonMoveUpClasspathItem = new javax.swing.JButton();
        jButtonMoveDownClasspathItem = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabelFontspath = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListFontspath = new com.jaspersoft.ireport.designer.fonts.CheckBoxList();
        jLabelFontspath1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListFonts = new javax.swing.JList();
        jButtonInstallFont = new javax.swing.JButton();
        jButtonEditFont = new javax.swing.JButton();
        jButtonRemoveFont = new javax.swing.JButton();
        jButtonExportFonts = new javax.swing.JButton();
        jButtonSelectAllFonts = new javax.swing.JButton();
        jButtonDeselectAllFonts = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jTextFieldExternalEditor = new javax.swing.JTextField();
        jButtonPDFViewer1 = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jLabelPDFViewer = new javax.swing.JLabel();
        jTextFieldPDFViewer = new javax.swing.JTextField();
        jButtonPDFViewer = new javax.swing.JButton();
        jLabelHTMLViewer = new javax.swing.JLabel();
        jTextFieldHTMLViewer = new javax.swing.JTextField();
        jButtonHTMLViewer = new javax.swing.JButton();
        jLabelXLSViewer = new javax.swing.JLabel();
        jTextFieldXLSViewer = new javax.swing.JTextField();
        jButtonXLSViewer = new javax.swing.JButton();
        jLabelCSVViewer = new javax.swing.JLabel();
        jTextFieldCSVViewer = new javax.swing.JTextField();
        jButtonCSVViewer = new javax.swing.JButton();
        jLabelTXTViewer = new javax.swing.JLabel();
        jTextFieldTXTViewer = new javax.swing.JTextField();
        jButtonTXTViewer = new javax.swing.JButton();
        jLabelRTFViewer = new javax.swing.JLabel();
        jTextFieldRTFViewer = new javax.swing.JTextField();
        jButtonRTFViewer = new javax.swing.JButton();
        jLabelODFViewer = new javax.swing.JLabel();
        jTextFieldODFViewer = new javax.swing.JTextField();
        jButtonODFViewer = new javax.swing.JButton();
        jLabelODSViewer = new javax.swing.JLabel();
        jTextFieldODSViewer = new javax.swing.JTextField();
        jButtonODSViewer = new javax.swing.JButton();
        jLabelDOCXViewer = new javax.swing.JLabel();
        jTextFieldDOCXViewer = new javax.swing.JTextField();
        jButtonDOCXViewer = new javax.swing.JButton();
        jLabelPPTXViewer = new javax.swing.JLabel();
        jTextFieldPPTXViewer = new javax.swing.JTextField();
        jButtonPPTXViewer = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabelClasspath1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListTemplates = new javax.swing.JList();
        jPanel19 = new javax.swing.JPanel();
        jButtonAddTemplate = new javax.swing.JButton();
        jButtonAddTemplateFolder = new javax.swing.JButton();
        jButtonRemoveTemplate = new javax.swing.JButton();
        jButtonMoveUpTemplate = new javax.swing.JButton();
        jButtonMoveDownTemplate = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabelCompilationDirectory = new javax.swing.JLabel();
        jTextFieldCompilationDirectory = new javax.swing.JTextField();
        jButtonCompilationDirectory = new javax.swing.JButton();
        jCheckBoxUseReportDirectoryToCompile = new javax.swing.JCheckBox();
        jCheckBoxCompileSubreports = new javax.swing.JCheckBox();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxLimitRecordNumber = new javax.swing.JCheckBox();
        jLabelMaxNumber = new javax.swing.JLabel();
        jSpinnerMaxRecordNumber = new javax.swing.JSpinner();
        jTextFieldReportLocale = new javax.swing.JTextField();
        jButtonReportLocale = new javax.swing.JButton();
        jLabelReportLocale = new javax.swing.JLabel();
        jLabelTimeZone = new javax.swing.JLabel();
        jTextFieldTimeZone = new javax.swing.JTextField();
        jButtonTimeZone = new javax.swing.JButton();
        jCheckBoxIgnorePagination = new javax.swing.JCheckBox();
        jCheckBoxVirtualizer = new javax.swing.JCheckBox();
        jPanelParameterPromptOptions = new javax.swing.JPanel();
        jLabelDateFormat = new javax.swing.JLabel();
        jTextPromptFieldDateFormat = new javax.swing.JTextField();
        jButtonDateFormat = new javax.swing.JButton();
        jLabelDateTimeFormat = new javax.swing.JLabel();
        jTextPromptFieldDateTimeFormat = new javax.swing.JTextField();
        jButtonDateTimeFormat = new javax.swing.JButton();
        jPanelVirtualizer = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxVirtualizer = new javax.swing.JComboBox();
        jPanel23 = new javax.swing.JPanel();
        jLabelReportVirtualizerSize1 = new javax.swing.JLabel();
        jSpinnerVirtualizerBlockSize = new javax.swing.JSpinner();
        jLabelReportVirtualizerMinGrowCount = new javax.swing.JLabel();
        jSpinnerVirtualizerGrownCount = new javax.swing.JSpinner();
        jLabelReportVirtualizerDirectory = new javax.swing.JLabel();
        jTextFieldVirtualizerDir = new javax.swing.JTextField();
        jButtonVirtualizerDirBrowse = new javax.swing.JButton();
        jLabelReportVirtualizerSize = new javax.swing.JLabel();
        jSpinnerVirtualizerSize = new javax.swing.JSpinner();
        jPanel25 = new javax.swing.JPanel();
        jLabelQueryExecuters = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableQueryExecuters = new javax.swing.JTable();
        jButtonAddQueryExecuter = new javax.swing.JButton();
        jButtonRemoveQueryExecuter = new javax.swing.JButton();
        jButtonModifyQueryExecuter = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Units"));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "Default unit");

        jComboBoxUnits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxUnitsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxMagneticGuideLines, "Turn off magnetic attraction");
        jCheckBoxMagneticGuideLines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMagneticGuideLinesActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jComboBoxUnits, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jCheckBoxMagneticGuideLines)
                .addContainerGap(270, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jComboBoxUnits, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jCheckBoxMagneticGuideLines))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(jLabelTimeZone1, "Language");

        jComboBoxLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLanguageActionPerformed(evt);
            }
        });

        jComboBoxTheme.setEditable(true);
        jComboBoxTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxThemeActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabelTimeZone2, "Chart theme");

        org.jdesktop.layout.GroupLayout jPanel24Layout = new org.jdesktop.layout.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel24Layout.createSequentialGroup()
                        .add(jLabelTimeZone1)
                        .add(17, 17, 17)
                        .add(jComboBoxLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel24Layout.createSequentialGroup()
                        .add(jLabelTimeZone2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBoxTheme, 0, 514, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelTimeZone1)
                    .add(jComboBoxLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelTimeZone2)
                    .add(jComboBoxTheme, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(266, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Report defaults", jPanel24);

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxKeyInReportInspector, "Show only element key in the report inspector (if the key is available)");
        jCheckBoxKeyInReportInspector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxKeyInReportInspectorActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxCrosstabAutoLayout, "Disable crosstab cell auto-layout");
        jCheckBoxCrosstabAutoLayout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCrosstabAutoLayoutActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxShowBackgroundAsSeparatedDocument, "Show Background as separated document");
        jCheckBoxShowBackgroundAsSeparatedDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxShowBackgroundAsSeparatedDocumentActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxAskConfirmationOnDelete, "Show confirmation dialog when deleting an element");
        jCheckBoxAskConfirmationOnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAskConfirmationOnDeleteActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxDebugMode, "Debug mode (for model changes)");
        jCheckBoxDebugMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDebugModeActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxShowPositionErrors, "Show element position errors in the designer");
        jCheckBoxShowPositionErrors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxShowPositionErrorsActionPerformed(evt);
            }
        });

        jCheckBoxSaveZoom.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxSaveZoom, "Save designer zoom factor and main page location in the Jrxml");
        jCheckBoxSaveZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxSaveZoomActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxLabelForField, "Create a label for fields dropped in the detail band if the column header band is heigher than 20 pixels");
        jCheckBoxLabelForField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxLabelForFieldActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel26Layout = new org.jdesktop.layout.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxKeyInReportInspector)
                    .add(jCheckBoxCrosstabAutoLayout)
                    .add(jCheckBoxShowBackgroundAsSeparatedDocument)
                    .add(jCheckBoxAskConfirmationOnDelete)
                    .add(jCheckBoxDebugMode)
                    .add(jCheckBoxShowPositionErrors)
                    .add(jCheckBoxSaveZoom)
                    .add(jCheckBoxLabelForField))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBoxKeyInReportInspector)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxCrosstabAutoLayout)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxShowBackgroundAsSeparatedDocument)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxAskConfirmationOnDelete)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxDebugMode)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxShowPositionErrors)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxSaveZoom)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxLabelForField)
                .addContainerGap(132, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Designer", jPanel26);

        jLabelExpressions.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelExpressions, "Custom expressions");

        jTableExpressions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Expression"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableExpressions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableExpressionsMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTableExpressions);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddExpression, "Add");
        jButtonAddExpression.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonAddExpression.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonAddExpression.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonAddExpression.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddExpressionActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonModifyExpression, "Modify");
        jButtonModifyExpression.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonModifyExpression.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonModifyExpression.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonModifyExpression.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyExpressionActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonRemoveExpression, "Remove");
        jButtonRemoveExpression.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonRemoveExpression.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonRemoveExpression.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonRemoveExpression.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveExpressionActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonRestoreExpressions, "Restore defaults");
        jButtonRestoreExpressions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRestoreExpressionsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxUseSyntaxHighlighting, "Use Syntax Highlighting");
        jCheckBoxUseSyntaxHighlighting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUseSyntaxHighlightingActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, "Editor font size:");

        jSpinnerEditorFontSize.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(12), Integer.valueOf(6), null, Integer.valueOf(1)));
        jSpinnerEditorFontSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerEditorFontSizeStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel22Layout = new org.jdesktop.layout.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelExpressions, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel22Layout.createSequentialGroup()
                        .add(jScrollPane6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(jButtonAddExpression, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jButtonModifyExpression, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jButtonRemoveExpression, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jButtonRestoreExpressions, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(jPanel22Layout.createSequentialGroup()
                        .add(jCheckBoxUseSyntaxHighlighting)
                        .add(18, 18, 18)
                        .add(jLabel5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinnerEditorFontSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBoxUseSyntaxHighlighting)
                    .add(jLabel5)
                    .add(jSpinnerEditorFontSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabelExpressions)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel22Layout.createSequentialGroup()
                        .add(jButtonAddExpression, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonModifyExpression, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonRemoveExpression, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonRestoreExpressions))
                    .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane3.addTab("Expression editor", jPanel22);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, "<html>Set the compatibility of the produced jrxml source code when the report is saved. If you don't use the last version, you risk to lose part the report content.");

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxShowCompatibilityWarning, "Show compatibility warning dialog");
        jCheckBoxShowCompatibilityWarning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxShowCompatibilityWarningActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelCompatibilityLayout = new org.jdesktop.layout.GroupLayout(jPanelCompatibility);
        jPanelCompatibility.setLayout(jPanelCompatibilityLayout);
        jPanelCompatibilityLayout.setHorizontalGroup(
            jPanelCompatibilityLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelCompatibilityLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelCompatibilityLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jComboBoxCompatibility, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 397, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 418, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jCheckBoxShowCompatibilityWarning))
                .addContainerGap(170, Short.MAX_VALUE))
        );
        jPanelCompatibilityLayout.setVerticalGroup(
            jPanelCompatibilityLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelCompatibilityLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jComboBoxCompatibility, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jCheckBoxShowCompatibilityWarning)
                .addContainerGap(212, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Compatibility", jPanelCompatibility);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Oracle Globalization Options"));

        org.openide.awt.Mnemonics.setLocalizedText(jLabelLanguage, "Language");

        org.openide.awt.Mnemonics.setLocalizedText(jLabelTerritory, "Territory");

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelLanguage)
                    .add(jLabelTerritory))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextFieldTerritory, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                    .add(jTextFieldLanguage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelLanguage)
                    .add(jTextFieldLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelTerritory)
                    .add(jTextFieldTerritory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(228, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Other", jPanel8);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTabbedPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("General", jPanel3);

        jLabelClasspath.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelClasspath, "Classpath");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Path", "Reloadable"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable1);

        jPanel5.setMinimumSize(new java.awt.Dimension(120, 10));
        jPanel5.setPreferredSize(new java.awt.Dimension(120, 10));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddClasspathItem, "Add JAR");
        jButtonAddClasspathItem.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonAddClasspathItem.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonAddClasspathItem.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonAddClasspathItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddClasspathItemActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel5.add(jButtonAddClasspathItem, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddClasspathItem1, "Add Folder");
        jButtonAddClasspathItem1.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonAddClasspathItem1.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonAddClasspathItem1.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonAddClasspathItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddClasspathItem1jButtonAddActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel5.add(jButtonAddClasspathItem1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonRemoveClasspathItem, "Remove");
        jButtonRemoveClasspathItem.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonRemoveClasspathItem.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonRemoveClasspathItem.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonRemoveClasspathItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveClasspathItemActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel5.add(jButtonRemoveClasspathItem, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonMoveUpClasspathItem, "Move up");
        jButtonMoveUpClasspathItem.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonMoveUpClasspathItem.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonMoveUpClasspathItem.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonMoveUpClasspathItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMoveUpClasspathItemActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel5.add(jButtonMoveUpClasspathItem, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonMoveDownClasspathItem, "Move down");
        jButtonMoveDownClasspathItem.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonMoveDownClasspathItem.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonMoveDownClasspathItem.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonMoveDownClasspathItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMoveDownClasspathItemActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel5.add(jButtonMoveDownClasspathItem, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jPanel6, gridBagConstraints);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                        .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabelClasspath, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                        .add(387, 387, 387))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelClasspath)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Classpath", jPanel4);

        jLabelFontspath.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelFontspath, "PDF fonts path (Deprecated, Install True Type fonts instead)");

        jListFontspath.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jListFontspath);

        jLabelFontspath1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelFontspath1, "Fonts");

        jListFonts.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListFonts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListFontsMouseClicked(evt);
            }
        });
        jListFonts.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListFontsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListFonts);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonInstallFont, "Install Font");
        jButtonInstallFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInstallFontActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonEditFont, "Edit Font");
        jButtonEditFont.setEnabled(false);
        jButtonEditFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditFontActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonRemoveFont, "Remove Font");
        jButtonRemoveFont.setEnabled(false);
        jButtonRemoveFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveFontActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonExportFonts, "Export as extension");
        jButtonExportFonts.setEnabled(false);
        jButtonExportFonts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportFontsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonSelectAllFonts, "Select all");
        jButtonSelectAllFonts.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonSelectAllFonts.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonSelectAllFonts.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonSelectAllFonts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectAllFontsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonDeselectAllFonts, "Deselect all");
        jButtonDeselectAllFonts.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonDeselectAllFonts.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonDeselectAllFonts.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonDeselectAllFonts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeselectAllFontsActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(jButtonEditFont)
                                .add(jButtonRemoveFont)
                                .add(jButtonInstallFont))
                            .add(jButtonExportFonts)))
                    .add(jLabelFontspath1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelFontspath, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jButtonDeselectAllFonts, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jButtonSelectAllFonts, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel7Layout.linkSize(new java.awt.Component[] {jButtonEditFont, jButtonExportFonts, jButtonInstallFont, jButtonRemoveFont}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelFontspath1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jButtonInstallFont)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonEditFont)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonRemoveFont)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonExportFonts))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 196, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabelFontspath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, 0, 0, Short.MAX_VALUE)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jButtonSelectAllFonts, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(jButtonDeselectAllFonts, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Fonts", jPanel7);

        jPanel10.setLayout(new java.awt.GridBagLayout());

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder("Jrxml External Editor"));
        jPanel29.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanel29.add(jTextFieldExternalEditor, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonPDFViewer1, "Browse");
        jButtonPDFViewer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPDFViewer1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel29.add(jButtonPDFViewer1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        jPanel10.add(jPanel29, gridBagConstraints);

        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder("Report Viewers"));
        jPanel30.setLayout(new java.awt.GridBagLayout());

        jLabelPDFViewer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelPDFViewer, "PDF Viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jLabelPDFViewer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jTextFieldPDFViewer, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonPDFViewer, "Browse");
        jButtonPDFViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPDFViewerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel30.add(jButtonPDFViewer, gridBagConstraints);

        jLabelHTMLViewer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelHTMLViewer, "HTML Viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jLabelHTMLViewer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jTextFieldHTMLViewer, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonHTMLViewer, "Browse");
        jButtonHTMLViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHTMLViewerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel30.add(jButtonHTMLViewer, gridBagConstraints);

        jLabelXLSViewer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelXLSViewer, "XLS Viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jLabelXLSViewer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jTextFieldXLSViewer, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonXLSViewer, "Browse");
        jButtonXLSViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXLSViewerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel30.add(jButtonXLSViewer, gridBagConstraints);

        jLabelCSVViewer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelCSVViewer, "CSV Viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jLabelCSVViewer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jTextFieldCSVViewer, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonCSVViewer, "Browse");
        jButtonCSVViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCSVViewerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel30.add(jButtonCSVViewer, gridBagConstraints);

        jLabelTXTViewer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelTXTViewer, "TXT Viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jLabelTXTViewer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jTextFieldTXTViewer, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonTXTViewer, "Browse");
        jButtonTXTViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTXTViewerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel30.add(jButtonTXTViewer, gridBagConstraints);

        jLabelRTFViewer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelRTFViewer, "RTF Viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jLabelRTFViewer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jTextFieldRTFViewer, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonRTFViewer, "Browse");
        jButtonRTFViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRTFViewerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel30.add(jButtonRTFViewer, gridBagConstraints);

        jLabelODFViewer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelODFViewer, "OpenDocument (ODF) Viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jLabelODFViewer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jTextFieldODFViewer, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonODFViewer, "Browse");
        jButtonODFViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonODFViewerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel30.add(jButtonODFViewer, gridBagConstraints);

        jLabelODSViewer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelODSViewer, "OpenDocument Spreedsheet (ODS) Viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jLabelODSViewer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jTextFieldODSViewer, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonODSViewer, "Browse");
        jButtonODSViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonODSViewerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel30.add(jButtonODSViewer, gridBagConstraints);

        jLabelDOCXViewer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelDOCXViewer, "Microsoft Word (DOCX) Viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jLabelDOCXViewer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jTextFieldDOCXViewer, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonDOCXViewer, "Browse");
        jButtonDOCXViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDOCXViewerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel30.add(jButtonDOCXViewer, gridBagConstraints);

        jLabelPPTXViewer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelPPTXViewer, "PowerPoint (PPTX) Viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jLabelPPTXViewer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel30.add(jTextFieldPPTXViewer, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonPPTXViewer, "Browse");
        jButtonPPTXViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPPTXViewerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel30.add(jButtonPPTXViewer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel10.add(jPanel30, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(IReportPanel.class, "ReportViewer.jLabel1.text")); // NOI18N
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel10.add(jLabel4, gridBagConstraints);

        jTabbedPane1.addTab("Viewers", jPanel10);

        jLabelClasspath1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelClasspath1, "<html>Wizard templates are used in the report wizard to create a report starting from an existing layout.");

        jScrollPane3.setViewportView(jListTemplates);

        jPanel19.setMinimumSize(new java.awt.Dimension(120, 10));
        jPanel19.setPreferredSize(new java.awt.Dimension(120, 10));
        jPanel19.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddTemplate, "Add Jrxml");
        jButtonAddTemplate.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonAddTemplate.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonAddTemplate.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonAddTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddTemplateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel19.add(jButtonAddTemplate, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddTemplateFolder, "Add Folder");
        jButtonAddTemplateFolder.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonAddTemplateFolder.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonAddTemplateFolder.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonAddTemplateFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddTemplateFolderjButtonAddActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel19.add(jButtonAddTemplateFolder, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonRemoveTemplate, "Remove");
        jButtonRemoveTemplate.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonRemoveTemplate.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonRemoveTemplate.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonRemoveTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveTemplateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel19.add(jButtonRemoveTemplate, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonMoveUpTemplate, "Move up");
        jButtonMoveUpTemplate.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonMoveUpTemplate.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonMoveUpTemplate.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonMoveUpTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMoveUpTemplateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel19.add(jButtonMoveUpTemplate, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonMoveDownTemplate, "Move down");
        jButtonMoveDownTemplate.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonMoveDownTemplate.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonMoveDownTemplate.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonMoveDownTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMoveDownTemplateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel19.add(jButtonMoveDownTemplate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanel19.add(jPanel20, gridBagConstraints);

        org.jdesktop.layout.GroupLayout jPanel18Layout = new org.jdesktop.layout.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelClasspath1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel18Layout.createSequentialGroup()
                        .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelClasspath1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel19, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Wizard templates", jPanel18);

        jPanel21.setPreferredSize(new java.awt.Dimension(612, 309));

        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Default compilation directory"));

        org.openide.awt.Mnemonics.setLocalizedText(jLabelCompilationDirectory, "Compilation directory");

        jTextFieldCompilationDirectory.setText(".");
        jTextFieldCompilationDirectory.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonCompilationDirectory, "Browse");
        jButtonCompilationDirectory.setEnabled(false);
        jButtonCompilationDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCompilationDirectoryActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxUseReportDirectoryToCompile, "Use Report Directory to compile");
        jCheckBoxUseReportDirectoryToCompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUseReportDirectoryToCompileActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxCompileSubreports, "Compile Subreports (if can be found)");
        jCheckBoxCompileSubreports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCompileSubreportsActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel27Layout = new org.jdesktop.layout.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel27Layout.createSequentialGroup()
                        .add(jCheckBoxCompileSubreports)
                        .addContainerGap())
                    .add(jPanel27Layout.createSequentialGroup()
                        .add(jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCheckBoxUseReportDirectoryToCompile, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                            .add(jPanel27Layout.createSequentialGroup()
                                .add(jLabelCompilationDirectory)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jTextFieldCompilationDirectory, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonCompilationDirectory))))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel27Layout.createSequentialGroup()
                .add(jCheckBoxUseReportDirectoryToCompile)
                .add(9, 9, 9)
                .add(jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelCompilationDirectory)
                    .add(jTextFieldCompilationDirectory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonCompilationDirectory))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxCompileSubreports)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxLimitRecordNumber, "Limit the number of records");
        jCheckBoxLimitRecordNumber.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxLimitRecordNumberStateChanged(evt);
            }
        });
        jCheckBoxLimitRecordNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxLimitRecordNumberActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabelMaxNumber, "Max number of reports");
        jLabelMaxNumber.setEnabled(false);

        jSpinnerMaxRecordNumber.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(5000), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinnerMaxRecordNumber.setEnabled(false);
        jSpinnerMaxRecordNumber.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerMaxRecordNumberStateChanged(evt);
            }
        });

        jTextFieldReportLocale.setEditable(false);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonReportLocale, "Select...");
        jButtonReportLocale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportLocaleActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabelReportLocale, "Report Locale");

        org.openide.awt.Mnemonics.setLocalizedText(jLabelTimeZone, "Report Time Zone");

        jTextFieldTimeZone.setEditable(false);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonTimeZone, "Select...");
        jButtonTimeZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTimeZoneActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxIgnorePagination, "Ignore pagination");
        jCheckBoxIgnorePagination.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxIgnorePaginationStateChanged(evt);
            }
        });
        jCheckBoxIgnorePagination.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxIgnorePaginationActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxVirtualizer, "Use virtualizer");
        jCheckBoxVirtualizer.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxVirtualizerStateChanged(evt);
            }
        });
        jCheckBoxVirtualizer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxVirtualizerActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jCheckBoxVirtualizer)
                        .addContainerGap())
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel2Layout.createSequentialGroup()
                            .add(21, 21, 21)
                            .add(jLabelMaxNumber)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jSpinnerMaxRecordNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(370, 370, 370))
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jCheckBoxLimitRecordNumber, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                                .add(311, 311, 311))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jCheckBoxIgnorePagination)
                                    .add(jPanel2Layout.createSequentialGroup()
                                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(jLabelTimeZone)
                                            .add(jLabelReportLocale))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, jTextFieldReportLocale, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                                            .add(org.jdesktop.layout.GroupLayout.LEADING, jTextFieldTimeZone, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(jButtonReportLocale)
                                            .add(jButtonTimeZone))))
                                .addContainerGap())))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(17, 17, 17)
                .add(jCheckBoxLimitRecordNumber)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelMaxNumber)
                    .add(jSpinnerMaxRecordNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(23, 23, 23)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelReportLocale)
                    .add(jTextFieldReportLocale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonReportLocale))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelTimeZone)
                    .add(jTextFieldTimeZone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonTimeZone))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBoxIgnorePagination)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBoxVirtualizer)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Execution options", jPanel2);

        org.openide.awt.Mnemonics.setLocalizedText(jLabelDateFormat, "Date format");

        org.openide.awt.Mnemonics.setLocalizedText(jButtonDateFormat, "Create...");
        jButtonDateFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDateFormatActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabelDateTimeFormat, "Time format");

        org.openide.awt.Mnemonics.setLocalizedText(jButtonDateTimeFormat, "Create...");
        jButtonDateTimeFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDateTimeFormatActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelParameterPromptOptionsLayout = new org.jdesktop.layout.GroupLayout(jPanelParameterPromptOptions);
        jPanelParameterPromptOptions.setLayout(jPanelParameterPromptOptionsLayout);
        jPanelParameterPromptOptionsLayout.setHorizontalGroup(
            jPanelParameterPromptOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelParameterPromptOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelParameterPromptOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelParameterPromptOptionsLayout.createSequentialGroup()
                        .add(jLabelDateFormat)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextPromptFieldDateFormat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonDateFormat))
                    .add(jPanelParameterPromptOptionsLayout.createSequentialGroup()
                        .add(jLabelDateTimeFormat)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextPromptFieldDateTimeFormat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonDateTimeFormat)))
                .addContainerGap(203, Short.MAX_VALUE))
        );
        jPanelParameterPromptOptionsLayout.setVerticalGroup(
            jPanelParameterPromptOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelParameterPromptOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelParameterPromptOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelDateFormat)
                    .add(jTextPromptFieldDateFormat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonDateFormat))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelParameterPromptOptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelDateTimeFormat)
                    .add(jTextPromptFieldDateTimeFormat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonDateTimeFormat))
                .addContainerGap(200, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Parameters prompt", jPanelParameterPromptOptions);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, "Use this virtualizer");

        jComboBoxVirtualizer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxVirtualizerActionPerformed(evt);
            }
        });

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Swap file"));

        org.openide.awt.Mnemonics.setLocalizedText(jLabelReportVirtualizerSize1, "Block size");

        jSpinnerVirtualizerBlockSize.setMinimumSize(new java.awt.Dimension(127, 20));
        jSpinnerVirtualizerBlockSize.setPreferredSize(new java.awt.Dimension(127, 20));
        jSpinnerVirtualizerBlockSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerVirtualizerBlockSizejSpinnerVirtualizerSizeStateChanged2(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabelReportVirtualizerMinGrowCount, "Min. grow count");

        jSpinnerVirtualizerGrownCount.setMinimumSize(new java.awt.Dimension(127, 20));
        jSpinnerVirtualizerGrownCount.setPreferredSize(new java.awt.Dimension(127, 20));
        jSpinnerVirtualizerGrownCount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerVirtualizerGrownCountjSpinnerVirtualizerSize1jSpinnerVirtualizerSizeStateChanged2(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel23Layout = new org.jdesktop.layout.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel23Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelReportVirtualizerSize1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jSpinnerVirtualizerBlockSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabelReportVirtualizerMinGrowCount)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jSpinnerVirtualizerGrownCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(148, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel23Layout.createSequentialGroup()
                .add(3, 3, 3)
                .add(jPanel23Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelReportVirtualizerSize1)
                    .add(jSpinnerVirtualizerBlockSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelReportVirtualizerMinGrowCount)
                    .add(jSpinnerVirtualizerGrownCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(jLabelReportVirtualizerDirectory, "Directory where the paged out data is to be stored");

        jTextFieldVirtualizerDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldVirtualizerDirActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonVirtualizerDirBrowse, "Browse");
        jButtonVirtualizerDirBrowse.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonVirtualizerDirBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVirtualizerDirBrowseActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabelReportVirtualizerSize, "Maximum size (in JRVirtualizable objects) of the paged in cache");

        jSpinnerVirtualizerSize.setMinimumSize(new java.awt.Dimension(127, 20));
        jSpinnerVirtualizerSize.setPreferredSize(new java.awt.Dimension(127, 20));
        jSpinnerVirtualizerSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerVirtualizerSizeStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelVirtualizerLayout = new org.jdesktop.layout.GroupLayout(jPanelVirtualizer);
        jPanelVirtualizer.setLayout(jPanelVirtualizerLayout);
        jPanelVirtualizerLayout.setHorizontalGroup(
            jPanelVirtualizerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelVirtualizerLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelVirtualizerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel23, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelReportVirtualizerDirectory)
                    .add(jPanelVirtualizerLayout.createSequentialGroup()
                        .add(jTextFieldVirtualizerDir, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonVirtualizerDirBrowse))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jComboBoxVirtualizer, 0, 578, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel2)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelReportVirtualizerSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 578, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jSpinnerVirtualizerSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanelVirtualizerLayout.setVerticalGroup(
            jPanelVirtualizerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelVirtualizerLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2)
                .add(0, 0, 0)
                .add(jComboBoxVirtualizer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabelReportVirtualizerDirectory)
                .add(0, 0, 0)
                .add(jPanelVirtualizerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextFieldVirtualizerDir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonVirtualizerDirBrowse))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabelReportVirtualizerSize)
                .add(0, 0, 0)
                .add(jSpinnerVirtualizerSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(jPanel23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Virtualizer", jPanelVirtualizer);

        org.jdesktop.layout.GroupLayout jPanel21Layout = new org.jdesktop.layout.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel27, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTabbedPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.getAccessibleContext().setAccessibleName("");

        jTabbedPane1.addTab("Compilation and execution", jPanel21);

        jLabelQueryExecuters.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelQueryExecuters, "Query Executers");

        jTableQueryExecuters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Language", "Query Executer Factory", "Fields Provider Class"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableQueryExecuters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableQueryExecutersMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTableQueryExecuters);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddQueryExecuter, "Add");
        jButtonAddQueryExecuter.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonAddQueryExecuter.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonAddQueryExecuter.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonAddQueryExecuter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddQueryExecuterActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonRemoveQueryExecuter, "Remove");
        jButtonRemoveQueryExecuter.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonRemoveQueryExecuter.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonRemoveQueryExecuter.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonRemoveQueryExecuter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveQueryExecuterActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonModifyQueryExecuter, "Modify");
        jButtonModifyQueryExecuter.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonModifyQueryExecuter.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonModifyQueryExecuter.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonModifyQueryExecuter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyQueryExecuterActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel25Layout = new org.jdesktop.layout.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelQueryExecuters, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel25Layout.createSequentialGroup()
                        .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jButtonAddQueryExecuter, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jButtonModifyQueryExecuter, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jButtonRemoveQueryExecuter, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelQueryExecuters)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel25Layout.createSequentialGroup()
                        .add(jButtonAddQueryExecuter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonModifyQueryExecuter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonRemoveQueryExecuter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Query Executers", jPanel25);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("General");
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxUnitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxUnitsActionPerformed
        notifyChange();
    }//GEN-LAST:event_jComboBoxUnitsActionPerformed

    private void jCheckBoxLimitRecordNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxLimitRecordNumberActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jCheckBoxLimitRecordNumberActionPerformed

    private void jCheckBoxLimitRecordNumberStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxLimitRecordNumberStateChanged
        jLabelMaxNumber.setEnabled( jCheckBoxLimitRecordNumber.isSelected());
        jSpinnerMaxRecordNumber.setEnabled( jCheckBoxLimitRecordNumber.isSelected() );
        notifyChange();
}//GEN-LAST:event_jCheckBoxLimitRecordNumberStateChanged

    private void jSpinnerMaxRecordNumberStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerMaxRecordNumberStateChanged
        notifyChange();
    }//GEN-LAST:event_jSpinnerMaxRecordNumberStateChanged

    private void jCheckBoxIgnorePaginationStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxIgnorePaginationStateChanged
        // TODO add your handling code here:
}//GEN-LAST:event_jCheckBoxIgnorePaginationStateChanged

    private void jCheckBoxIgnorePaginationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxIgnorePaginationActionPerformed
        notifyChange();
}//GEN-LAST:event_jCheckBoxIgnorePaginationActionPerformed

    private void jButtonReportLocaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReportLocaleActionPerformed
       
        
        LocaleSelectorDialog selectorDialog = null;
        
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof Dialog)
        {
            selectorDialog = new LocaleSelectorDialog((Dialog)w, true);
        }
        else
        {
            selectorDialog = new LocaleSelectorDialog((Frame)w, true);
        }
        
        if (getCurrentReportLocale() != null)
        {
            selectorDialog.setSelectedLocale(getCurrentReportLocale());
        }
        
        selectorDialog.setVisible(true);
        
        if (selectorDialog.getDialogResult() == JOptionPane.OK_OPTION)
        {
            setCurrentReportLocale(selectorDialog.getSelectedLocale());
        }
        
        notifyChange();
    }//GEN-LAST:event_jButtonReportLocaleActionPerformed

    private void jButtonTimeZoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTimeZoneActionPerformed
        
        TimeZoneDialog selectorDialog = null;
        
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof Dialog)
        {
            selectorDialog = new TimeZoneDialog((Dialog)w, true);
        }
        else
        {
            selectorDialog = new TimeZoneDialog((Frame)w, true);
        }
        
        if (getCurrentReportTimeZoneId() != null)
        {
            selectorDialog.setReportTimeZoneId(getCurrentReportTimeZoneId());
        }
        
        selectorDialog.setVisible(true);
        
        if (selectorDialog.getDialogResult() == JOptionPane.OK_OPTION)
        {
            setCurrentReportTimeZoneId(selectorDialog.getReportTimeZoneId());
        }
        
        notifyChange();
        
    }//GEN-LAST:event_jButtonTimeZoneActionPerformed

    private void jButtonAddClasspathItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddClasspathItemActionPerformed
        String fileName = "";
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
        
        jfc.setMultiSelectionEnabled(true);
        
        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
            java.io.File[] files = jfc.getSelectedFiles();

            DefaultTableModel tbm = (DefaultTableModel)jTable1.getModel();
            for (int i=0; i<files.length; ++i) {
                //((DefaultListModel)jListClassPath.getModel()).addElement( files[i] );
                tbm.addRow(new Object[]{files[i], new Boolean(false)});
            }




            IReportManager.getInstance().setCurrentDirectory( jfc.getSelectedFile(), true);
            notifyChange();
            classpathChanged();
        }
}//GEN-LAST:event_jButtonAddClasspathItemActionPerformed

    private void jButtonAddClasspathItem1jButtonAddActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddClasspathItem1jButtonAddActionPerformed1
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( IReportManager.getInstance().getCurrentDirectory());
        
        jfc.setDialogTitle(I18n.getString("IReportPanel.Title.Dialog"));
        jfc.setDialogTitle(I18n.getString("IReportPanel.Title.Dialog")); //"addToClassPath"
        
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY  );
        
        jfc.setMultiSelectionEnabled(true);
        
        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
            java.io.File[] files = jfc.getSelectedFiles();

            DefaultTableModel tbm = (DefaultTableModel)jTable1.getModel();
            for (int i=0; i<files.length; ++i) {
                tbm.addRow(new Object[]{files[i], new Boolean(false)});
            }
//            for (int i=0; i<files.length; ++i) {
//                ((DefaultListModel)jListClassPath.getModel()).addElement( files[i] );
//            }
            IReportManager.getInstance().setCurrentDirectory( jfc.getSelectedFile(), true);
            notifyChange();
            classpathChanged();
        }
}//GEN-LAST:event_jButtonAddClasspathItem1jButtonAddActionPerformed1

    private void jButtonRemoveClasspathItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveClasspathItemActionPerformed
        
        
        if (jTable1.getSelectedRowCount() > 0) {
            int[] rows = jTable1.getSelectedRows();
            Arrays.sort(rows);

            DefaultTableModel tbm = (DefaultTableModel)jTable1.getModel();
            for (int i=rows.length-1; i>=0; --i)
            {
                    tbm.removeRow(rows[i]);
            }
            //Object[] values = jListClassPath.getSelectedValues();
            //for (int i=0; i<values.length; ++i) {
            //    ((DefaultListModel)jListClassPath.getModel()).removeElement(values[i]);
            //}
            notifyChange();
            classpathChanged();
        }
}//GEN-LAST:event_jButtonRemoveClasspathItemActionPerformed

    private void jButtonMoveUpClasspathItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMoveUpClasspathItemActionPerformed

        if (jTable1.getSelectedRowCount() > 0)
        {
            int[] rows = jTable1.getSelectedRows();
            DefaultTableModel tbm = (DefaultTableModel)jTable1.getModel();
            jTable1.clearSelection();
            for (int i=0; i<rows.length; ++i) {
                if (rows[i] == 0) continue;
                Object[] theRow = new Object[]{tbm.getValueAt(rows[i], 0),tbm.getValueAt(rows[i], 1)  };
                tbm.removeRow(rows[i]);
                tbm.insertRow(rows[i]-1, theRow);
                rows[i]--;
                jTable1.addRowSelectionInterval(rows[i],rows[i]);
            }
            notifyChange();
        }
//        if (jListClassPath.getSelectedValues() != null) {
//            int[] indices = jListClassPath.getSelectedIndices();
//            for (int i=0; i<indices.length; ++i) {
//                if (indices[i] == 0) continue;
//                Object val = ((DefaultListModel)jListClassPath.getModel()).remove( indices[i] );
//                ((DefaultListModel)jListClassPath.getModel()).insertElementAt(val, indices[i]-1);
//                indices[i]--;
//            }
//            jListClassPath.setSelectedIndices(indices);
//            notifyChange();
//        }
}//GEN-LAST:event_jButtonMoveUpClasspathItemActionPerformed

    private void jButtonMoveDownClasspathItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMoveDownClasspathItemActionPerformed
      
        if (jTable1.getSelectedRowCount() > 0)
        {
            int[] rows = jTable1.getSelectedRows();
            DefaultTableModel tbm = (DefaultTableModel)jTable1.getModel();
            jTable1.clearSelection();
            for (int i=rows.length-1; i>=0; --i) {
                if (rows[i] == jTable1.getRowCount()-1) continue;
                Object[] theRow = new Object[]{tbm.getValueAt(rows[i], 0),tbm.getValueAt(rows[i], 1)  };
                tbm.removeRow(rows[i]);
                tbm.insertRow(rows[i]+1, theRow);
                rows[i]++;
                jTable1.addRowSelectionInterval(rows[i],rows[i]);
            }
            notifyChange();
        }
            
//      if (jListClassPath.getSelectedValues() != null) {            
            //            int[] indices = jListClassPath.getSelectedIndices();
//            for (int i=indices.length-1; i>=0; --i) {
//                if (indices[i] >= ((DefaultListModel)jListClassPath.getModel()).size() -1 ) continue;
//
//                Object val = ((DefaultListModel)jListClassPath.getModel()).remove( indices[i] );
//                ((DefaultListModel)jListClassPath.getModel()).insertElementAt(val, indices[i]+1);
//                indices[i]++;
//            }
//            jListClassPath.setSelectedIndices(indices);
//            notifyChange();
//        }
}//GEN-LAST:event_jButtonMoveDownClasspathItemActionPerformed

    private void jButtonSelectAllFontsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectAllFontsActionPerformed
        DefaultListModel dlm = (DefaultListModel)jListFontspath.getModel();
        
        for (int i=0; i<dlm.size(); ++i) {
            Object obj = dlm.getElementAt(i);
            if (obj instanceof JCheckBox) {
                JCheckBox checkbox = (JCheckBox)obj;
                checkbox.setSelected(true);
            }
        }
        jListFontspath.updateUI();
}//GEN-LAST:event_jButtonSelectAllFontsActionPerformed

    private void jButtonDeselectAllFontsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeselectAllFontsActionPerformed
        DefaultListModel dlm = (DefaultListModel)jListFontspath.getModel();
        
        for (int i=0; i<dlm.size(); ++i) {
            Object obj = dlm.getElementAt(i);
            if (obj instanceof JCheckBox) {
                JCheckBox checkbox = (JCheckBox)obj;
                checkbox.setSelected(false);
            }
        }
        jListFontspath.updateUI();
}//GEN-LAST:event_jButtonDeselectAllFontsActionPerformed

private void jButtonPDFViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPDFViewerActionPerformed

            javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();
            
            jfc.setDialogTitle("Choose a PDF viewer...");
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldPDFViewer.setText( "\"" + jfc.getSelectedFile().getPath() + "\"");
            }
}//GEN-LAST:event_jButtonPDFViewerActionPerformed

private void jButtonHTMLViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHTMLViewerActionPerformed

            javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();
            
            jfc.setDialogTitle("Choose a HTML viewer...");
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldHTMLViewer.setText( "\"" + jfc.getSelectedFile().getPath() + "\"");
            }
}//GEN-LAST:event_jButtonHTMLViewerActionPerformed

private void jButtonXLSViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXLSViewerActionPerformed

            javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();
            
            jfc.setDialogTitle("Choose a XLS viewer...");
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldXLSViewer.setText( "\"" + jfc.getSelectedFile().getPath() + "\"");
            }
}//GEN-LAST:event_jButtonXLSViewerActionPerformed

private void jButtonCSVViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCSVViewerActionPerformed

            javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();
            
            jfc.setDialogTitle("Choose a CSV viewer...");
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldCSVViewer.setText( "\"" + jfc.getSelectedFile().getPath() + "\"");
            }
}//GEN-LAST:event_jButtonCSVViewerActionPerformed

private void jButtonTXTViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTXTViewerActionPerformed

            javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();
            
            jfc.setDialogTitle("Choose a Text viewer...");
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldTXTViewer.setText( "\"" + jfc.getSelectedFile().getPath() + "\"");
            }
}//GEN-LAST:event_jButtonTXTViewerActionPerformed

private void jButtonRTFViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRTFViewerActionPerformed

            javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();
            
            jfc.setDialogTitle("Choose a RTF viewer...");
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldRTFViewer.setText( "\"" + jfc.getSelectedFile().getPath() + "\"");
            }
}//GEN-LAST:event_jButtonRTFViewerActionPerformed

private void jButtonODFViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonODFViewerActionPerformed

                javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();
            
            jfc.setDialogTitle("Choose an ODF viewer...");
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldODFViewer.setText( "\"" + jfc.getSelectedFile().getPath() + "\"");
            }
}//GEN-LAST:event_jButtonODFViewerActionPerformed

private void jButtonAddTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddTemplateActionPerformed
        String fileName = "";
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( IReportManager.getInstance().getCurrentDirectory());

        jfc.setDialogTitle(I18n.getString("IReportPanel.Title.Dialog"));
        jfc.setDialogTitle(I18n.getString("IReportPanel.Title.Dialog"));//"addToClassPath"

        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setFileSelectionMode( JFileChooser.FILES_ONLY  );
        jfc.addChoosableFileFilter( new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File file) {
                String filename = file.getName();
                return (filename.toLowerCase().endsWith(".jrxml") || file.isDirectory() ||
                        filename.toLowerCase().endsWith("c.xml") ||
                        filename.toLowerCase().endsWith("t.xml")
                        ) ;
            }
            public String getDescription() {
                return "Columnar or Tabular template definition (*<C|T>.jrxml, *<C|T>.xml)";
            }
        });

        jfc.setMultiSelectionEnabled(true);

        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
            java.io.File[] files = jfc.getSelectedFiles();

            for (int i=0; i<files.length; ++i) {
                ((DefaultListModel)jListTemplates.getModel()).addElement( files[i] );
            }
            IReportManager.getInstance().setCurrentDirectory( jfc.getSelectedFile(), true);
            notifyChange();
        }
}//GEN-LAST:event_jButtonAddTemplateActionPerformed

private void jButtonAddTemplateFolderjButtonAddActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddTemplateFolderjButtonAddActionPerformed1
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( IReportManager.getInstance().getCurrentDirectory());

        jfc.setDialogTitle(I18n.getString("IReportPanel.Title.Dialog"));
        jfc.setDialogTitle(I18n.getString("IReportPanel.Title.Dialog")); //"addToClassPath"

        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY  );

        jfc.setMultiSelectionEnabled(true);

        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
            java.io.File[] files = jfc.getSelectedFiles();

            for (int i=0; i<files.length; ++i) {
                ((DefaultListModel)jListTemplates.getModel()).addElement( files[i] );
            }
            IReportManager.getInstance().setCurrentDirectory( jfc.getSelectedFile(), true);
            notifyChange();
        }
}//GEN-LAST:event_jButtonAddTemplateFolderjButtonAddActionPerformed1

private void jButtonRemoveTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveTemplateActionPerformed
     if (jListTemplates.getSelectedValues() != null) {
            Object[] values = jListTemplates.getSelectedValues();
            for (int i=0; i<values.length; ++i) {
                ((DefaultListModel)jListTemplates.getModel()).removeElement(values[i]);
            }
            notifyChange();
        }
}//GEN-LAST:event_jButtonRemoveTemplateActionPerformed

private void jButtonMoveUpTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMoveUpTemplateActionPerformed
    if (jListTemplates.getSelectedValues() != null) {
            int[] indices = jListTemplates.getSelectedIndices();
            for (int i=0; i<indices.length; ++i) {
                if (indices[i] == 0) continue;
                Object val = ((DefaultListModel)jListTemplates.getModel()).remove( indices[i] );
                ((DefaultListModel)jListTemplates.getModel()).insertElementAt(val, indices[i]-1);
                indices[i]--;
            }
            jListTemplates.setSelectedIndices(indices);
            notifyChange();
        }
}//GEN-LAST:event_jButtonMoveUpTemplateActionPerformed

private void jButtonMoveDownTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMoveDownTemplateActionPerformed
    if (jListTemplates.getSelectedValues() != null) {
            int[] indices = jListTemplates.getSelectedIndices();
            for (int i=indices.length-1; i>=0; --i) {
                if (indices[i] >= ((DefaultListModel)jListTemplates.getModel()).size() -1 ) continue;

                Object val = ((DefaultListModel)jListTemplates.getModel()).remove( indices[i] );
                ((DefaultListModel)jListTemplates.getModel()).insertElementAt(val, indices[i]+1);
                indices[i]++;
            }
            jListTemplates.setSelectedIndices(indices);
            notifyChange();
        }
}//GEN-LAST:event_jButtonMoveDownTemplateActionPerformed

private void jComboBoxVirtualizerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxVirtualizerActionPerformed
    notifyChange();
}//GEN-LAST:event_jComboBoxVirtualizerActionPerformed

private void jTextFieldVirtualizerDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldVirtualizerDirActionPerformed
    notifyChange();
}//GEN-LAST:event_jTextFieldVirtualizerDirActionPerformed

private void jButtonVirtualizerDirBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVirtualizerDirBrowseActionPerformed
    JFileChooser jfc = new JFileChooser();
    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        jTextFieldVirtualizerDir.setText( jfc.getSelectedFile().getPath());
    }
    notifyChange();
}//GEN-LAST:event_jButtonVirtualizerDirBrowseActionPerformed

private void jSpinnerVirtualizerSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerVirtualizerSizeStateChanged
    notifyChange();
}//GEN-LAST:event_jSpinnerVirtualizerSizeStateChanged

private void jSpinnerVirtualizerBlockSizejSpinnerVirtualizerSizeStateChanged2(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerVirtualizerBlockSizejSpinnerVirtualizerSizeStateChanged2
    notifyChange();
}//GEN-LAST:event_jSpinnerVirtualizerBlockSizejSpinnerVirtualizerSizeStateChanged2

private void jSpinnerVirtualizerGrownCountjSpinnerVirtualizerSize1jSpinnerVirtualizerSizeStateChanged2(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerVirtualizerGrownCountjSpinnerVirtualizerSize1jSpinnerVirtualizerSizeStateChanged2
    notifyChange();
}//GEN-LAST:event_jSpinnerVirtualizerGrownCountjSpinnerVirtualizerSize1jSpinnerVirtualizerSizeStateChanged2

private void jCheckBoxVirtualizerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxVirtualizerStateChanged
    // TODO add your handling code here:
}//GEN-LAST:event_jCheckBoxVirtualizerStateChanged

private void jCheckBoxVirtualizerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxVirtualizerActionPerformed
    notifyChange();
}//GEN-LAST:event_jCheckBoxVirtualizerActionPerformed

private void jComboBoxLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLanguageActionPerformed
    notifyChange();
}//GEN-LAST:event_jComboBoxLanguageActionPerformed

private void jComboBoxThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxThemeActionPerformed
    notifyChange();
}//GEN-LAST:event_jComboBoxThemeActionPerformed

private void jButtonAddQueryExecuterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddQueryExecuterActionPerformed

    java.awt.Frame parent = Misc.getMainFrame();
        QueryExecuterDialog jrpd = new QueryExecuterDialog( parent, true);
        jrpd.setVisible(true);
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            QueryExecuterDef qe = jrpd.getQueryExecuterDef();
            ((DefaultTableModel)jTableQueryExecuters.getModel()).addRow(new Object[]{qe, qe.getClassName(),qe.getFieldsProvider()});
            notifyChange();
        }

}//GEN-LAST:event_jButtonAddQueryExecuterActionPerformed

private void jButtonRemoveQueryExecuterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveQueryExecuterActionPerformed


    if (jTableQueryExecuters.getSelectedRowCount() > 0)
    {
        DefaultTableModel dtm = (DefaultTableModel)jTableQueryExecuters.getModel();
        int[] indices = jTableQueryExecuters.getSelectedRows();
        for (int i=indices.length-1; i>=0; --i)
        {
            Object val = dtm.getValueAt( indices[i], 0);
            if (!((QueryExecuterDef)val).isBuiltin())
            {
                dtm.removeRow( indices[i]);
                //dtm.insertRow( indices[i]-1, new Object[]{val, ((QueryExecuterDef)val).getClassName(),((QueryExecuterDef)val).getFieldsProvider()});
                //indices[i]--;
                notifyChange();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "You can not modify or delete a built-in query executer.\nJust redefine the language");
            }
        }
    }


}//GEN-LAST:event_jButtonRemoveQueryExecuterActionPerformed

private void jTableQueryExecutersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableQueryExecutersMouseClicked

    if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1 ) {

        jButtonModifyQueryExecuterActionPerformed(null);
        

    }
}//GEN-LAST:event_jTableQueryExecutersMouseClicked

private void jButtonModifyQueryExecuterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyQueryExecuterActionPerformed
    
    if (jTableQueryExecuters.getSelectedRow() >=0)
    {
        DefaultTableModel dtm = (DefaultTableModel)jTableQueryExecuters.getModel();
        int index = jTableQueryExecuters.getSelectedRow();

        QueryExecuterDef qe = (QueryExecuterDef)dtm.getValueAt(index, 0);

        if (qe.isBuiltin())
        {
            JOptionPane.showMessageDialog(this, "You can not modify or delete a built-in query executer.\nJust redefine the language");
            return;
        }

        java.awt.Frame parent = Misc.getMainFrame();
        QueryExecuterDialog jrpd = new QueryExecuterDialog( parent, true);
        jrpd.setQueryExecuterDef( qe );
        jrpd.setVisible(true);

        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            dtm.setValueAt( jrpd.getQueryExecuterDef(),  index, 0);
            dtm.setValueAt( jrpd.getQueryExecuterDef().getClassName(),  index, 1);
            dtm.setValueAt( jrpd.getQueryExecuterDef().getFieldsProvider(),  index, 2);
            notifyChange();
        }
    }

}//GEN-LAST:event_jButtonModifyQueryExecuterActionPerformed

private void jCheckBoxMagneticGuideLinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMagneticGuideLinesActionPerformed
    notifyChange();
}//GEN-LAST:event_jCheckBoxMagneticGuideLinesActionPerformed

private void jButtonCompilationDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCompilationDirectoryActionPerformed
    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

            jfc.setDialogTitle("Choose the compilation directory");

            jfc.setMultiSelectionEnabled(false);
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    jTextFieldCompilationDirectory.setText( jfc.getSelectedFile().getPath());
                    notifyChange();
            }
}//GEN-LAST:event_jButtonCompilationDirectoryActionPerformed

private void jCheckBoxUseReportDirectoryToCompileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxUseReportDirectoryToCompileActionPerformed

    notifyChange();

    jTextFieldCompilationDirectory.setEnabled(!jCheckBoxUseReportDirectoryToCompile.isSelected());
    jButtonCompilationDirectory.setEnabled(!jCheckBoxUseReportDirectoryToCompile.isSelected());

}//GEN-LAST:event_jCheckBoxUseReportDirectoryToCompileActionPerformed

private void jCheckBoxKeyInReportInspectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxKeyInReportInspectorActionPerformed
    notifyChange();
}//GEN-LAST:event_jCheckBoxKeyInReportInspectorActionPerformed

private void jCheckBoxCrosstabAutoLayoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCrosstabAutoLayoutActionPerformed

        notifyChange();
}//GEN-LAST:event_jCheckBoxCrosstabAutoLayoutActionPerformed

private void jTableExpressionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableExpressionsMouseClicked

    if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1 ) {

        jButtonModifyExpressionActionPerformed(null);


    }
}//GEN-LAST:event_jTableExpressionsMouseClicked

private void jButtonAddExpressionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddExpressionActionPerformed
    
    
    ExpressionEditor editor = new ExpressionEditor();

    if (editor.showDialog(this) == JOptionPane.OK_OPTION)
    {
        ((DefaultTableModel)jTableExpressions.getModel()).addRow(new Object[]{editor.getExpression()});
        notifyChange();
    }

}//GEN-LAST:event_jButtonAddExpressionActionPerformed

private void jButtonModifyExpressionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyExpressionActionPerformed
   
    if (jTableExpressions.getSelectedRow() >=0)
    {
        DefaultTableModel dtm = (DefaultTableModel)jTableExpressions.getModel();
        int index = jTableExpressions.getSelectedRow();

        String exp = "" + dtm.getValueAt(index, 0);

        ExpressionEditor editor = new ExpressionEditor();
        editor.setExpression(exp);

        if (editor.showDialog(this) == JOptionPane.OK_OPTION)
        {
            dtm.setValueAt( editor.getExpression(),  index, 0);
            notifyChange();
        }
    }


}//GEN-LAST:event_jButtonModifyExpressionActionPerformed

private void jButtonRemoveExpressionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveExpressionActionPerformed
    if (jTableExpressions.getSelectedRowCount() > 0)
    {
        DefaultTableModel dtm = (DefaultTableModel)jTableExpressions.getModel();
        int[] indices = jTableExpressions.getSelectedRows();
        for (int i=indices.length-1; i>=0; --i)
        {
            Object val = dtm.getValueAt( indices[i], 0);
            dtm.removeRow( indices[i]);
            notifyChange();
        }
    }
}//GEN-LAST:event_jButtonRemoveExpressionActionPerformed

private void jButtonRestoreExpressionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRestoreExpressionsActionPerformed

    ((DefaultTableModel)jTableExpressions.getModel()).setRowCount(0);
    ArrayList<String> exps = ExpressionEditor.getDefaultPredefinedExpressions();
    for (String s: exps)
    {
        ((DefaultTableModel)jTableExpressions.getModel()).addRow(new Object[]{s});
    }
    notifyChange();

}//GEN-LAST:event_jButtonRestoreExpressionsActionPerformed

private void jCheckBoxAskConfirmationOnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAskConfirmationOnDeleteActionPerformed
    notifyChange();
}//GEN-LAST:event_jCheckBoxAskConfirmationOnDeleteActionPerformed

private void jCheckBoxShowBackgroundAsSeparatedDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxShowBackgroundAsSeparatedDocumentActionPerformed
    notifyChange();
}//GEN-LAST:event_jCheckBoxShowBackgroundAsSeparatedDocumentActionPerformed

private void jCheckBoxShowCompatibilityWarningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxShowCompatibilityWarningActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jCheckBoxShowCompatibilityWarningActionPerformed

private void jButtonDOCXViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDOCXViewerActionPerformed

        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

        jfc.setDialogTitle("Choose a DOCX viewer...");
        jfc.setMultiSelectionEnabled(false);
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            jTextFieldDOCXViewer.setText( "\"" + jfc.getSelectedFile().getPath() + "\"");
        }
}//GEN-LAST:event_jButtonDOCXViewerActionPerformed

private void jCheckBoxCompileSubreportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCompileSubreportsActionPerformed
     notifyChange();
}//GEN-LAST:event_jCheckBoxCompileSubreportsActionPerformed

private void jCheckBoxDebugModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDebugModeActionPerformed
    notifyChange();
}//GEN-LAST:event_jCheckBoxDebugModeActionPerformed

private void jButtonPDFViewer1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPDFViewer1ActionPerformed

        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

        jfc.setDialogTitle("Choose a Jrxml External Editor...");
        jfc.setMultiSelectionEnabled(false);
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            jTextFieldExternalEditor.setText( jfc.getSelectedFile().getPath());
        }
}//GEN-LAST:event_jButtonPDFViewer1ActionPerformed

private void jButtonODSViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonODSViewerActionPerformed
     javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

            jfc.setDialogTitle("Choose an ODS viewer...");
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldODSViewer.setText( "\"" + jfc.getSelectedFile().getPath() + "\"");
            }
}//GEN-LAST:event_jButtonODSViewerActionPerformed

private void jButtonInstallFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInstallFontActionPerformed
        InstallFontWizardDescriptor wizardDescriptor = new InstallFontWizardDescriptor();
        if (wizardDescriptor.runWizard())
        {
            IRFontUtils.reloadAndNotifyFontsListChange();
            // update fonts list...
            updateFontsList();
            // Fire a preference changed event...
        }
}//GEN-LAST:event_jButtonInstallFontActionPerformed

private void jListFontsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListFontsValueChanged

    boolean enabled =  jListFonts.getSelectedValue() != null && jListFonts.getSelectedValue() instanceof SimpleFontFamilyEx;
    jButtonEditFont.setEnabled(enabled);
    jButtonRemoveFont.setEnabled(enabled);
    jButtonExportFonts.setEnabled(enabled);

}//GEN-LAST:event_jListFontsValueChanged

private void jButtonRemoveFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveFontActionPerformed

    DefaultListModel model = (DefaultListModel)jListFonts.getModel();
    int[] selectedIndexes = jListFonts.getSelectedIndices();

    for (int i=selectedIndexes.length -1; i>=0; --i)
    {
     int idx = selectedIndexes[i];

     if (model.getElementAt(idx) instanceof SimpleFontFamily)
     {
         model.removeElementAt(idx);
     }
    }

    List<SimpleFontFamilyEx> fonts = new ArrayList<SimpleFontFamilyEx>();

    for (int i=0; i<model.size(); ++i)
    {
        if (model.get(i) instanceof SimpleFontFamilyEx)
        {
            fonts.add((SimpleFontFamilyEx)model.get(i));
        }
    }

    IRFontUtils.saveFonts(fonts);
    IRFontUtils.reloadAndNotifyFontsListChange();
    updateFontsList();

}//GEN-LAST:event_jButtonRemoveFontActionPerformed

private void jButtonEditFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditFontActionPerformed

    if (jListFonts.getSelectedValue() == null) return;

    if (jListFonts.getSelectedValue() instanceof SimpleFontFamilyEx)
    {
        int index = jListFonts.getSelectedIndex();
        DefaultListModel model = (DefaultListModel)jListFonts.getModel();
        SimpleFontFamilyEx font = (SimpleFontFamilyEx)model.getElementAt(index);
        EditFontPanel pan = new EditFontPanel();
        pan.setFontFamily(font);
        if (pan.showDialog(this, true) == JOptionPane.OK_OPTION)
        {
            model.setElementAt(pan.getFontFamily(), index);
            updateUI();

            // Save fonts.xml...
            List<SimpleFontFamilyEx> fonts = new ArrayList<SimpleFontFamilyEx>();

            for (int i=0; i<model.size(); ++i)
            {
                if (model.get(i) instanceof SimpleFontFamilyEx)
                {
                    fonts.add((SimpleFontFamilyEx)model.get(i));
                }
            }

            IRFontUtils.saveFonts(fonts);
            IRFontUtils.reloadAndNotifyFontsListChange();
        }
    }
}//GEN-LAST:event_jButtonEditFontActionPerformed

private void jListFontsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListFontsMouseClicked
    if (SwingUtilities.isLeftMouseButton(evt) && evt.getClickCount() == 2)
    {
        jButtonEditFontActionPerformed(null);
    }
}//GEN-LAST:event_jListFontsMouseClicked

private void jButtonExportFontsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportFontsActionPerformed

        IRFontUtils.export(jListFonts.getSelectedValues());

}//GEN-LAST:event_jButtonExportFontsActionPerformed

private void jCheckBoxShowPositionErrorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxShowPositionErrorsActionPerformed
    notifyChange();
}//GEN-LAST:event_jCheckBoxShowPositionErrorsActionPerformed

private void jButtonDateFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDateFormatActionPerformed
    
    FieldPatternDialog fpd = null;
    Window win = SwingUtilities.getWindowAncestor(this);
    if (win != null && win instanceof JFrame)
    {
        fpd = new FieldPatternDialog( (JFrame)win, true);
    }
    else fpd = new FieldPatternDialog( (JDialog)win, true);

    fpd.setOnlyDate(true);
    if (jTextPromptFieldDateFormat.getText().length() > 0)
    {
        fpd.setPattern(jTextPromptFieldDateFormat.getText());
    }
    fpd.setVisible(true);

    if (fpd.getDialogResult() == JOptionPane.OK_OPTION)
    {
        jTextPromptFieldDateFormat.setText(fpd.getPattern());
        notifyChange();
    }
}//GEN-LAST:event_jButtonDateFormatActionPerformed

private void jButtonDateTimeFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDateTimeFormatActionPerformed
    FieldPatternDialog fpd = null;
    Window win = SwingUtilities.getWindowAncestor(this);
    if (win != null && win instanceof JFrame)
    {
        fpd = new FieldPatternDialog( (JFrame)win, true);
    }
    else fpd = new FieldPatternDialog( (JDialog)win, true);

    fpd.setOnlyDate(true);
    fpd.setSelectedCategory("Global.Label.Time");
    if (jTextPromptFieldDateTimeFormat.getText().length() > 0)
    {
        fpd.setPattern(jTextPromptFieldDateTimeFormat.getText());
    }

    fpd.setVisible(true);

    if (fpd.getDialogResult() == JOptionPane.OK_OPTION)
    {
        jTextPromptFieldDateTimeFormat.setText(fpd.getPattern());
        notifyChange();
    }
}//GEN-LAST:event_jButtonDateTimeFormatActionPerformed

private void jCheckBoxSaveZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxSaveZoomActionPerformed
    notifyChange();
}//GEN-LAST:event_jCheckBoxSaveZoomActionPerformed

private void jButtonPPTXViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPPTXViewerActionPerformed
    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

        jfc.setDialogTitle("Choose a PPTX viewer...");
        jfc.setMultiSelectionEnabled(false);
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            jTextFieldPPTXViewer.setText( "\"" + jfc.getSelectedFile().getPath() + "\"");
        }
}//GEN-LAST:event_jButtonPPTXViewerActionPerformed

private void jCheckBoxLabelForFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxLabelForFieldActionPerformed
   notifyChange();
}//GEN-LAST:event_jCheckBoxLabelForFieldActionPerformed

private void jCheckBoxUseSyntaxHighlightingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxUseSyntaxHighlightingActionPerformed
    notifyChange();
}//GEN-LAST:event_jCheckBoxUseSyntaxHighlightingActionPerformed

private void jSpinnerEditorFontSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerEditorFontSizeStateChanged
    notifyChange();
}//GEN-LAST:event_jSpinnerEditorFontSizeStateChanged
    
            
    void load() {

        setInit(true);
        Preferences pref = IReportManager.getPreferences();
        
        String unit = pref.get("Unit","inches"); // NOI18N
        Misc.setComboboxSelectedTagValue(jComboBoxUnits, unit);
        
        jCheckBoxLimitRecordNumber.setSelected( pref.getBoolean("limitRecordNumber", false)  );  // NOI18N
        ((SpinnerNumberModel)jSpinnerMaxRecordNumber.getModel()).setValue( pref.getInt("maxRecordNumber", 1) );  // NOI18N

        jCheckBoxMagneticGuideLines.setSelected( pref.getBoolean("noMagnetic", false)  );  // NOI18N
        jCheckBoxIgnorePagination.setSelected( pref.getBoolean("isIgnorePagination", false)  );  // NOI18N
        jCheckBoxVirtualizer.setSelected( pref.getBoolean("isUseReportVirtualizer", false)  );  // NOI18N
        jCheckBoxShowBackgroundAsSeparatedDocument.setSelected( pref.getBoolean("ShowBackgroundAsSeparatedDocument", true) ); // NOI18N
        jCheckBoxAskConfirmationOnDelete.setSelected( !pref.getBoolean("noConfirmElementDelete", true) ); // NOI18N
        jCheckBoxShowCompatibilityWarning.setSelected( pref.getBoolean("show_compatibility_warning", true) ); // NOI18N
        jCheckBoxCompileSubreports.setSelected( pref.getBoolean("compile_subreports", true) ); // NOI18N
        jCheckBoxDebugMode.setSelected( pref.getBoolean("designer_debug_mode", false) ); // NOI18N

        jCheckBoxSaveZoom.setSelected( pref.getBoolean("save_zoom_and_location", true) ); // NOI18N
        
        jCheckBoxShowPositionErrors.setSelected( pref.getBoolean("showPositionErrors", true) ); // NOI18N

        jCheckBoxLabelForField.setSelected( pref.getBoolean("createLabelForField", true) ); // NOI18N

        jCheckBoxUseSyntaxHighlighting.setSelected( pref.getBoolean("useSyntaxHighlighting", true) ); // NOI18N

        ((SpinnerNumberModel)jSpinnerEditorFontSize.getModel()).setValue(pref.getInt("editorFontSize", 12) ); // NOI18N


        jCheckBoxKeyInReportInspector.setSelected( pref.getBoolean("showKeyInReportInspector", false)  );  // NOI18N
        jCheckBoxCrosstabAutoLayout.setSelected( pref.getBoolean("disableCrosstabAutoLayout", false)  );  // NOI18N

        jCheckBoxUseReportDirectoryToCompile.setSelected( pref.getBoolean("useReportDirectoryToCompile", true)  );  // NOI18N
        jTextFieldCompilationDirectory.setText( pref.get("reportDirectoryToCompile", ".")  );  // NOI18N


        jTextFieldCompilationDirectory.setEnabled(!jCheckBoxUseReportDirectoryToCompile.isSelected());
        jButtonCompilationDirectory.setEnabled(!jCheckBoxUseReportDirectoryToCompile.isSelected());

        String locName = pref.get("reportLocale", null);  // NOI18N
        if (locName != null)
        {
            setCurrentReportLocale(Misc.getLocaleFromString(locName));
        }
        else
        {
            setCurrentReportLocale(null);
        }
        
        String timeZoneId = pref.get("reportTimeZone", null);  // NOI18N
        if (timeZoneId != null)
        {
            setCurrentReportTimeZoneId(timeZoneId);
        }
        else
        {
            setCurrentReportTimeZoneId(null);
        }   
        
        //((DefaultListModel)jListClassPath.getModel()).clear();
        
        DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
        dtm.setRowCount(0);
        List<String> cp = IReportManager.getInstance().getClasspath();
        List<String> fullCp = new ArrayList<String>();
        fullCp.addAll(cp);
        for (String path : cp)
        {
            if (path != null && path.length() > 0)
            {
                //((DefaultListModel)jListClassPath.getModel()).addElement( path );
                dtm.addRow(new Object[]{path, new Boolean(false)});
            }
        }

        cp = IReportManager.getInstance().getRelodableClasspath();
        fullCp.addAll(cp);
        for (String path : cp)
        {
            if (path != null && path.length() > 0)
            {
                //((DefaultListModel)jListClassPath.getModel()).addElement( path );
                dtm.addRow(new Object[]{path, new Boolean(true)});
            }
        }
        
        setFontspath(IReportManager.getInstance().getFontpath(), fullCp);


        ((DefaultListModel)jListTemplates.getModel()).clear();
        String templatesPath = IReportManager.getPreferences().get(IReportManager.TEMPLATE_PATH, "");
        String[] paths = templatesPath.split("\\n");
        for (int i=0; i<paths.length; ++i)
        {
            String tpath = paths[i];
            if (tpath != null && tpath.length() > 0)
            {
                ((DefaultListModel)jListTemplates.getModel()).addElement( tpath );
            }
        }

        jTextFieldCSVViewer.setText(pref.get("ExternalCSVViewer", ""));
        jTextFieldPDFViewer.setText(pref.get("ExternalPDFViewer", ""));
        jTextFieldXLSViewer.setText(pref.get("ExternalXLSViewer", ""));
        jTextFieldRTFViewer.setText(pref.get("ExternalRTFViewer", ""));
        jTextFieldODFViewer.setText(pref.get("ExternalODFViewer", ""));
        jTextFieldODSViewer.setText(pref.get("ExternalODSViewer", ""));
        jTextFieldTXTViewer.setText(pref.get("ExternalTXTViewer", ""));
        jTextFieldHTMLViewer.setText(pref.get("ExternalHTMLViewer", ""));
        jTextFieldDOCXViewer.setText(pref.get("ExternalDOCXViewer", ""));
        jTextFieldPPTXViewer.setText(pref.get("ExternalPPTXViewer", ""));
        jTextFieldExternalEditor.setText(pref.get("ExternalEditor", ""));

        jTextPromptFieldDateFormat.setText(pref.get("PromptDateFormat", ""));
        jTextPromptFieldDateTimeFormat.setText(pref.get("PromptDateTimeFormat", ""));

        jTextFieldLanguage.setText(pref.get("oracle_language", ""));
        jTextFieldTerritory.setText(pref.get("oracle_territory", ""));

        JRPropertiesUtil jrPropUtils = IRLocalJasperReportsContext.getUtilities();
        
        this.jTextFieldVirtualizerDir.setText( pref.get("ReportVirtualizerDirectory", Misc.nvl(jrPropUtils.getProperty(JRCompiler.COMPILER_TEMP_DIR),"")));
        this.jSpinnerVirtualizerSize.setValue(pref.getInt("ReportVirtualizerSize",100));
        Misc.setComboboxSelectedTagValue(jComboBoxVirtualizer, pref.get("ReportVirtualizer", "JRFileVirtualizer") );
        this.jSpinnerVirtualizerBlockSize.setValue(pref.getInt("ReportVirtualizerBlockSize",100));
        this.jSpinnerVirtualizerGrownCount.setValue(pref.getInt("ReportVirtualizerMinGrownCount",100));

        Misc.setComboboxSelectedTagValue(jComboBoxLanguage, pref.get("DefaultLanguage", "eye.candy.sixties") );


        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(IReportManager.getJRExtensionsClassLoader());
        
        Set<String> themeNamesSet = new HashSet<String>();
        List themeBundles = ExtensionsEnvironment.getExtensionsRegistry().getExtensions(ChartThemeBundle.class);
        for (Iterator it = themeBundles.iterator(); it.hasNext();)
        {
           ChartThemeBundle bundle = (ChartThemeBundle) it.next();
           String[] themeNames = bundle.getChartThemeNames();
           themeNamesSet.addAll(Arrays.asList(themeNames));
        }

        String[] allThemeNames  = themeNamesSet.toArray(new String[themeNamesSet.size()]);
        Arrays.sort(allThemeNames);

        Thread.currentThread().setContextClassLoader(oldCL);

        jComboBoxTheme.setModel(new DefaultComboBoxModel(allThemeNames));
        ((DefaultComboBoxModel)jComboBoxTheme.getModel()).insertElementAt(
                new Tag("", "<JasperReports Default>"), 0);


        Misc.setComboboxSelectedTagValue(jComboBoxTheme, pref.get("DefaultTheme", "") );


        // Load the query executers...
        ArrayList<QueryExecuterDef> queryExecuters = IReportManager.getInstance().getQueryExecuters();

        ((DefaultTableModel)jTableQueryExecuters.getModel()).setRowCount(0);
        for (QueryExecuterDef qeOriginal : queryExecuters)
        {
            if (qeOriginal != null)
            {
                QueryExecuterDef qe = qeOriginal.cloneMe();
                ((DefaultTableModel)jTableQueryExecuters.getModel()).addRow(new Object[]{qe, qe.getClassName(),qe.getFieldsProvider()});
            }
        }

        jTableQueryExecutersSelectionChanged();

        ((DefaultTableModel)jTableExpressions.getModel()).setRowCount(0);

        if (!pref.getBoolean("custom_expressions_set", false))
        {
            ArrayList<String> exps = ExpressionEditor.getDefaultPredefinedExpressions();
            for (String s: exps)
            {
                ((DefaultTableModel)jTableExpressions.getModel()).addRow(new Object[]{s});
            }
        }
        for (int i=0; pref.get("customexpression."+i, null) != null; ++i)
        {
            ((DefaultTableModel)jTableExpressions.getModel()).addRow(new Object[]{pref.get("customexpression."+i, "")});
        }

        Misc.setComboboxSelectedTagValue(jComboBoxCompatibility, pref.get("compatibility", "") );

        jTableExpressionsSelectionChanged();

        exportOptionsPanel.load();
        jrOptionsPanel.load();


        updateFontsList();


        setInit(false);

    }


    public void updateFontsList()
    {

        List<Object> fonts = new ArrayList<Object>();
        List<String> names = new ArrayList<String>();


        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        List<SimpleFontFamilyEx> editableFonts = IRFontUtils.loadFonts();
        for(SimpleFontFamilyEx sff : editableFonts)
        {
            fonts.add(sff);
            String fname = sff.getName();
            names.add(fname);
            
            System.out.println("Dumping editable fonts: " + sff.getName() + " " + sff.getExportFonts());
            System.out.flush();
            
        }


        try {
            ((DefaultListModel)jListFonts.getModel()).removeAllElements();
            Thread.currentThread().setContextClassLoader(new ReportClassLoader(IReportManager.getReportClassLoader()));

            Collection extensionFonts = JRFontUtil.getFontFamilyNames();
            for(Iterator it = extensionFonts.iterator(); it.hasNext();)
            {
                String fname = (String)it.next();
                if (!names.contains(fname))
                {
                    fonts.add(fname);
                }
            }
        } finally {
            Thread.currentThread().setContextClassLoader(oldCL);
        }



        Object[] objs = fonts.toArray();

        Arrays.sort(objs, new Comparator() {

            public int compare(Object o1, Object o2) {

                String s1 = null;
                String s2 = null;

                if (o1 instanceof String) s1 = (String)o1;
                if (o1 instanceof SimpleFontFamilyEx)
                {
                    s1 = " " + ((SimpleFontFamilyEx)o1).getName();
                }

                if (o2 instanceof String) s2 = (String)o2;
                if (o2 instanceof SimpleFontFamilyEx)
                {
                    s2 = " " + ((SimpleFontFamilyEx)o2).getName();
                }

                if (s1 != null && s2 != null)
                {
                    return s1.compareTo(s2);
                }
                return 0;
            }
        });

        for (Object obj : objs)
        {
            ((DefaultListModel)jListFonts.getModel()).addElement(obj);
        }

        

    }

    void store() {
        
        Preferences pref = IReportManager.getPreferences();
        if (jComboBoxUnits.getSelectedIndex() >= 0)
        {
            String unit = ""+((Tag)jComboBoxUnits.getSelectedItem()).getValue();// NOI18N
            pref.put("Unit", unit); // NOI18N
        }
        
        pref.putBoolean("limitRecordNumber"  , jCheckBoxLimitRecordNumber.isSelected() );// NOI18N
        pref.putInt( "maxRecordNumber", ((SpinnerNumberModel)jSpinnerMaxRecordNumber.getModel()).getNumber().intValue() );// NOI18N
        pref.putBoolean("isIgnorePagination"  , jCheckBoxIgnorePagination.isSelected() );// NOI18N
        pref.putBoolean("isUseReportVirtualizer"  , jCheckBoxVirtualizer.isSelected() );// NOI18N
        pref.putBoolean("noMagnetic"  , jCheckBoxMagneticGuideLines.isSelected() );// NOI18N
        pref.putBoolean("ShowBackgroundAsSeparatedDocument"  , jCheckBoxShowBackgroundAsSeparatedDocument.isSelected() );// NOI18N
        pref.putBoolean("noConfirmElementDelete"  , !jCheckBoxAskConfirmationOnDelete.isSelected() );// NOI18N
        pref.putBoolean("show_compatibility_warning"  ,jCheckBoxShowCompatibilityWarning.isSelected() );// NOI18N

        pref.putBoolean("showKeyInReportInspector"  , jCheckBoxKeyInReportInspector.isSelected() );// NOI18N
        pref.putBoolean("disableCrosstabAutoLayout"  , jCheckBoxCrosstabAutoLayout.isSelected() );// NOI18N

        pref.putBoolean("useReportDirectoryToCompile"  , jCheckBoxUseReportDirectoryToCompile.isSelected() );// NOI18N
        pref.put("reportDirectoryToCompile", jTextFieldCompilationDirectory.getText());// NOI18N
        pref.putBoolean("compile_subreports", jCheckBoxCompileSubreports.isSelected());// NOI18N
        pref.putBoolean("designer_debug_mode", jCheckBoxDebugMode.isSelected());// NOI18N

        pref.putBoolean("showPositionErrors", jCheckBoxShowPositionErrors.isSelected());// NOI18N
        pref.putBoolean("save_zoom_and_location", jCheckBoxSaveZoom.isSelected());// NOI18N

        pref.putBoolean("createLabelForField", jCheckBoxLabelForField.isSelected());// NOI18N

        pref.putBoolean("useSyntaxHighlighting", jCheckBoxUseSyntaxHighlighting.isSelected());// NOI18N

        int fontsize = ((Integer)((SpinnerNumberModel)jSpinnerEditorFontSize.getModel()).getValue()).intValue();
        pref.putInt("editorFontSize", fontsize);// NOI18N
                
        if (getCurrentReportLocale() != null) pref.put("reportLocale", getCurrentReportLocale().toString());
        else pref.remove("reportLocale");
        
        if (getCurrentReportTimeZoneId() != null) pref.put("reportTimeZone", getCurrentReportTimeZoneId());
        else pref.remove("reportTimeZone");
        
        IReportManager.getInstance().setClasspath( getClasspath());
        IReportManager.getInstance().setRelodableClasspath( getClasspath(true));
        IReportManager.getInstance().setFontpath( getFontspath());

        String templatesPath = "";
        ListModel model = jListTemplates.getModel();
        for (int i=0; i<model.getSize(); ++i)
        {
            templatesPath += model.getElementAt(i) + "\n";
        }

        pref.put(IReportManager.TEMPLATE_PATH, templatesPath);
     
        if (jTextFieldCSVViewer.getText().length() > 0) pref.put("ExternalCSVViewer", jTextFieldCSVViewer.getText());
        else pref.remove("ExternalCSVViewer");
        
        if (jTextFieldPDFViewer.getText().length() > 0) pref.put("ExternalPDFViewer", jTextFieldPDFViewer.getText());
        else pref.remove("ExternalPDFViewer");

        if (jTextFieldXLSViewer.getText().length() > 0) pref.put("ExternalXLSViewer", jTextFieldXLSViewer.getText());
        else pref.remove("ExternalXLSViewer");

        if (jTextFieldTXTViewer.getText().length() > 0) pref.put("ExternalTXTViewer", jTextFieldTXTViewer.getText());
        else pref.remove("ExternalTXTViewer");

        if (jTextFieldODFViewer.getText().length() > 0) pref.put("ExternalODFViewer", jTextFieldODFViewer.getText());
        else pref.remove("ExternalODFViewer");

        if (jTextFieldODSViewer.getText().length() > 0) pref.put("ExternalODSViewer", jTextFieldODSViewer.getText());
        else pref.remove("ExternalODSViewer");

        if (jTextFieldRTFViewer.getText().length() > 0) pref.put("ExternalRTFViewer", jTextFieldRTFViewer.getText());
        else pref.remove("ExternalRTFViewer");

        if (jTextFieldHTMLViewer.getText().length() > 0) pref.put("ExternalHTMLViewer", jTextFieldHTMLViewer.getText());
        else pref.remove("ExternalHTMLViewer");

        if (jTextFieldDOCXViewer.getText().length() > 0) pref.put("ExternalDOCXViewer", jTextFieldDOCXViewer.getText());
        else pref.remove("ExternalDOCXViewer");

        if (jTextFieldPPTXViewer.getText().length() > 0) pref.put("ExternalPPTXViewer", jTextFieldPPTXViewer.getText());
        else pref.remove("ExternalPPTXViewer");

        if (jTextFieldExternalEditor.getText().length() > 0) pref.put("ExternalEditor", jTextFieldExternalEditor.getText());
        else pref.remove("ExternalEditor");


        if (jTextFieldVirtualizerDir.getText().length() > 0) pref.put("ReportVirtualizerDirectory", this.jTextFieldVirtualizerDir.getText());
        pref.putInt("ReportVirtualizerSize",((SpinnerNumberModel)this.jSpinnerVirtualizerSize.getModel()).getNumber().intValue());
        pref.putInt("ReportVirtualizerBlockSize",((SpinnerNumberModel)this.jSpinnerVirtualizerBlockSize.getModel()).getNumber().intValue());
        pref.putInt("ReportVirtualizerMinGrownCount",((SpinnerNumberModel)this.jSpinnerVirtualizerGrownCount.getModel()).getNumber().intValue());
        pref.put("ReportVirtualizer",  ""+((Tag)jComboBoxVirtualizer.getSelectedItem()).getValue() );

        Object obj = jComboBoxLanguage.getSelectedItem();
        if (obj instanceof Tag) obj = ((Tag)obj).getValue();
        if (obj == null) obj = "";
        pref.put("DefaultLanguage", (obj+"").trim());

        obj = jComboBoxTheme.getSelectedItem();
        if (obj instanceof Tag) obj = ((Tag)obj).getValue();
        if (obj == null) obj = "";
        pref.put("DefaultTheme", (obj+"").trim());


        //IReportManager.getInstance().re
        // Remove old language definitions...
        int k=0;
        while (pref.get("queryExecuter."+k+".language", null) != null)
        {
            pref.remove("queryExecuter."+k+".language");
            k++;
        }

        k=0;
        for (int i=0; i<jTableQueryExecuters.getRowCount(); ++i)
        {
            QueryExecuterDef qe = (QueryExecuterDef) jTableQueryExecuters.getValueAt(i, 0);
            if (!qe.isBuiltin())
            {
                pref.put("queryExecuter."+k+".language", qe.getLanguage());
                pref.put("queryExecuter."+k+".class", qe.getClassName());
                pref.put("queryExecuter."+k+".provider", qe.getFieldsProvider());
                k++;
            }
        }



        k=0;
        while (pref.get("customexpression."+k, null) != null)
        {
            pref.remove("customexpression."+k);
            k++;
        }

        pref.putBoolean("custom_expressions_set", true);
        for (int i=0; i<jTableExpressions.getRowCount(); ++i)
        {
            pref.put("customexpression."+i, ""+jTableExpressions.getValueAt(i, 0));
        }

        exportOptionsPanel.store();
        jrOptionsPanel.store();

        IReportManager.getInstance().reloadQueryExecuters();

        pref.put("compatibility", ((Tag)jComboBoxCompatibility.getSelectedItem()).getValue()+"");
        
        if (jTextPromptFieldDateFormat.getText().length() > 0) pref.put("PromptDateFormat", this.jTextPromptFieldDateFormat.getText());
        else {
            pref.remove("PromptDateFormat");
        }

        if (jTextPromptFieldDateTimeFormat.getText().length() > 0) pref.put("PromptDateTimeFormat", this.jTextPromptFieldDateTimeFormat.getText());
        else {
            pref.remove("PromptDateTimeFormat");
        }


        if (jTextFieldLanguage.getText().length() > 0) pref.put("oracle_language", this.jTextFieldLanguage.getText());
        else {
            pref.remove("oracle_language");
        }

        if (jTextFieldTerritory.getText().length() > 0) pref.put("oracle_territory", this.jTextFieldTerritory.getText());
        else {
            pref.remove("oracle_territory");
        }
    }
    
    private List<String> getClasspath(boolean relodable)
    {
        List<String> cp = new ArrayList<String>();
        DefaultTableModel tbm = (DefaultTableModel)jTable1.getModel();
        for (int i=0; i<jTable1.getRowCount(); ++i)
        {
            Boolean b = (Boolean)jTable1.getValueAt(i, 1);
            if (b.booleanValue() == relodable)
            {
                cp.add( "" + jTable1.getValueAt(i, 0) );
                //System.out.println(relodable + " " + jTable1.getValueAt(i, 0));
            }
        }
        return cp;
    }

    private List<String> getClasspath()
    {
        return getClasspath(false);
    }

    boolean valid() {
        // TODO check whether form is consistent and complete
        return exportOptionsPanel.valid();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddClasspathItem;
    private javax.swing.JButton jButtonAddClasspathItem1;
    private javax.swing.JButton jButtonAddExpression;
    private javax.swing.JButton jButtonAddQueryExecuter;
    private javax.swing.JButton jButtonAddTemplate;
    private javax.swing.JButton jButtonAddTemplateFolder;
    private javax.swing.JButton jButtonCSVViewer;
    private javax.swing.JButton jButtonCompilationDirectory;
    private javax.swing.JButton jButtonDOCXViewer;
    private javax.swing.JButton jButtonDateFormat;
    private javax.swing.JButton jButtonDateTimeFormat;
    private javax.swing.JButton jButtonDeselectAllFonts;
    private javax.swing.JButton jButtonEditFont;
    private javax.swing.JButton jButtonExportFonts;
    private javax.swing.JButton jButtonHTMLViewer;
    private javax.swing.JButton jButtonInstallFont;
    private javax.swing.JButton jButtonModifyExpression;
    private javax.swing.JButton jButtonModifyQueryExecuter;
    private javax.swing.JButton jButtonMoveDownClasspathItem;
    private javax.swing.JButton jButtonMoveDownTemplate;
    private javax.swing.JButton jButtonMoveUpClasspathItem;
    private javax.swing.JButton jButtonMoveUpTemplate;
    private javax.swing.JButton jButtonODFViewer;
    private javax.swing.JButton jButtonODSViewer;
    private javax.swing.JButton jButtonPDFViewer;
    private javax.swing.JButton jButtonPDFViewer1;
    private javax.swing.JButton jButtonPPTXViewer;
    private javax.swing.JButton jButtonRTFViewer;
    private javax.swing.JButton jButtonRemoveClasspathItem;
    private javax.swing.JButton jButtonRemoveExpression;
    private javax.swing.JButton jButtonRemoveFont;
    private javax.swing.JButton jButtonRemoveQueryExecuter;
    private javax.swing.JButton jButtonRemoveTemplate;
    private javax.swing.JButton jButtonReportLocale;
    private javax.swing.JButton jButtonRestoreExpressions;
    private javax.swing.JButton jButtonSelectAllFonts;
    private javax.swing.JButton jButtonTXTViewer;
    private javax.swing.JButton jButtonTimeZone;
    private javax.swing.JButton jButtonVirtualizerDirBrowse;
    private javax.swing.JButton jButtonXLSViewer;
    private javax.swing.JCheckBox jCheckBoxAskConfirmationOnDelete;
    private javax.swing.JCheckBox jCheckBoxCompileSubreports;
    private javax.swing.JCheckBox jCheckBoxCrosstabAutoLayout;
    private javax.swing.JCheckBox jCheckBoxDebugMode;
    private javax.swing.JCheckBox jCheckBoxIgnorePagination;
    private javax.swing.JCheckBox jCheckBoxKeyInReportInspector;
    private javax.swing.JCheckBox jCheckBoxLabelForField;
    private javax.swing.JCheckBox jCheckBoxLimitRecordNumber;
    private javax.swing.JCheckBox jCheckBoxMagneticGuideLines;
    private javax.swing.JCheckBox jCheckBoxSaveZoom;
    private javax.swing.JCheckBox jCheckBoxShowBackgroundAsSeparatedDocument;
    private javax.swing.JCheckBox jCheckBoxShowCompatibilityWarning;
    private javax.swing.JCheckBox jCheckBoxShowPositionErrors;
    private javax.swing.JCheckBox jCheckBoxUseReportDirectoryToCompile;
    private javax.swing.JCheckBox jCheckBoxUseSyntaxHighlighting;
    private javax.swing.JCheckBox jCheckBoxVirtualizer;
    private javax.swing.JComboBox jComboBoxCompatibility;
    private javax.swing.JComboBox jComboBoxLanguage;
    private javax.swing.JComboBox jComboBoxTheme;
    private javax.swing.JComboBox jComboBoxUnits;
    private javax.swing.JComboBox jComboBoxVirtualizer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelCSVViewer;
    private javax.swing.JLabel jLabelClasspath;
    private javax.swing.JLabel jLabelClasspath1;
    private javax.swing.JLabel jLabelCompilationDirectory;
    private javax.swing.JLabel jLabelDOCXViewer;
    private javax.swing.JLabel jLabelDateFormat;
    private javax.swing.JLabel jLabelDateTimeFormat;
    private javax.swing.JLabel jLabelExpressions;
    private javax.swing.JLabel jLabelFontspath;
    private javax.swing.JLabel jLabelFontspath1;
    private javax.swing.JLabel jLabelHTMLViewer;
    private javax.swing.JLabel jLabelLanguage;
    private javax.swing.JLabel jLabelMaxNumber;
    private javax.swing.JLabel jLabelODFViewer;
    private javax.swing.JLabel jLabelODSViewer;
    private javax.swing.JLabel jLabelPDFViewer;
    private javax.swing.JLabel jLabelPPTXViewer;
    private javax.swing.JLabel jLabelQueryExecuters;
    private javax.swing.JLabel jLabelRTFViewer;
    private javax.swing.JLabel jLabelReportLocale;
    private javax.swing.JLabel jLabelReportVirtualizerDirectory;
    private javax.swing.JLabel jLabelReportVirtualizerMinGrowCount;
    private javax.swing.JLabel jLabelReportVirtualizerSize;
    private javax.swing.JLabel jLabelReportVirtualizerSize1;
    private javax.swing.JLabel jLabelTXTViewer;
    private javax.swing.JLabel jLabelTerritory;
    private javax.swing.JLabel jLabelTimeZone;
    private javax.swing.JLabel jLabelTimeZone1;
    private javax.swing.JLabel jLabelTimeZone2;
    private javax.swing.JLabel jLabelXLSViewer;
    private javax.swing.JList jListFonts;
    private com.jaspersoft.ireport.designer.fonts.CheckBoxList jListFontspath;
    private javax.swing.JList jListTemplates;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelCompatibility;
    private javax.swing.JPanel jPanelParameterPromptOptions;
    private javax.swing.JPanel jPanelVirtualizer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSpinner jSpinnerEditorFontSize;
    private javax.swing.JSpinner jSpinnerMaxRecordNumber;
    private javax.swing.JSpinner jSpinnerVirtualizerBlockSize;
    private javax.swing.JSpinner jSpinnerVirtualizerGrownCount;
    private javax.swing.JSpinner jSpinnerVirtualizerSize;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableExpressions;
    private javax.swing.JTable jTableQueryExecuters;
    private javax.swing.JTextField jTextFieldCSVViewer;
    private javax.swing.JTextField jTextFieldCompilationDirectory;
    private javax.swing.JTextField jTextFieldDOCXViewer;
    private javax.swing.JTextField jTextFieldExternalEditor;
    private javax.swing.JTextField jTextFieldHTMLViewer;
    private javax.swing.JTextField jTextFieldLanguage;
    private javax.swing.JTextField jTextFieldODFViewer;
    private javax.swing.JTextField jTextFieldODSViewer;
    private javax.swing.JTextField jTextFieldPDFViewer;
    private javax.swing.JTextField jTextFieldPPTXViewer;
    private javax.swing.JTextField jTextFieldRTFViewer;
    private javax.swing.JTextField jTextFieldReportLocale;
    private javax.swing.JTextField jTextFieldTXTViewer;
    private javax.swing.JTextField jTextFieldTerritory;
    private javax.swing.JTextField jTextFieldTimeZone;
    private javax.swing.JTextField jTextFieldVirtualizerDir;
    private javax.swing.JTextField jTextFieldXLSViewer;
    private javax.swing.JTextField jTextPromptFieldDateFormat;
    private javax.swing.JTextField jTextPromptFieldDateTimeFormat;
    // End of variables declaration//GEN-END:variables

    public Locale getCurrentReportLocale() {
        return currentReportLocale;
    }

    public void setCurrentReportLocale(Locale currentReportLocale) {
        this.currentReportLocale = currentReportLocale;
        if (currentReportLocale == null)
        {
            jTextFieldReportLocale.setText("Default - " + Locale.getDefault().getDisplayName() + "");
        }
        else
        {
            jTextFieldReportLocale.setText(currentReportLocale.getDisplayName());
        }
    }

    public String getCurrentReportTimeZoneId() {
        return currentReportTimeZoneId;
    }

    public void setCurrentReportTimeZoneId(String timeZoneId) {
        this.currentReportTimeZoneId = timeZoneId;
        if (currentReportTimeZoneId == null)
        {
            jTextFieldTimeZone.setText("Default - " + TimeZone.getDefault().getDisplayName() + "");
        }
        else
        {
            jTextFieldTimeZone.setText( TimeZone.getTimeZone(timeZoneId).getDisplayName() );
        }
    }
    
    
    @SuppressWarnings("unchecked")
    private void setFontspath(List<String> fontsPaths, List<String> cp)
    {
        @SuppressWarnings("unchecked")
        List<String> newcp = new ArrayList<String>();
        newcp.addAll(cp);
        
        List<String> cp_old = new ArrayList<String>();
        
        for (String s : fontsPaths) {
              if (!newcp.contains(s))
              {
                  newcp.add(s);
              }
        }

        Object[] allStrings = new Object[newcp.size()];
        allStrings = newcp.toArray(allStrings);

        Arrays.sort(allStrings);

        ((DefaultListModel)jListFontspath.getModel()).clear();
        for (int i=0; i<allStrings.length; ++i)
        {
            String s = ""+allStrings[i];
            if (s.trim().length() == 0) continue;
            CheckBoxListEntry cble = new CheckBoxListEntry(s,fontsPaths.contains(s));

            if (!cp.contains(s) && !cp_old.contains(s))
            {
                cble.setRed(true);
            }

            ((DefaultListModel)jListFontspath.getModel()).addElement( cble);
        }
    }


    @SuppressWarnings("unchecked")
    private List<String> getFontspath()
    {
         List<String> cp = new ArrayList<String>();
         java.util.List list = jListFontspath.getCheckedItems();
         for (int i=0; i<list.size(); ++i )
         {
             CheckBoxListEntry cble = (CheckBoxListEntry)list.get( i );
             cp.add( cble.getValue() + "" );
         }

         return cp;
    }
    
    
    /**
     * When the classpath changes, we need to update the fonts list...
     * 
     */
    private void classpathChanged()
    {
        setFontspath(getFontspath(), getClasspath(true));
        updateFontsList();
    }
}
