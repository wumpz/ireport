/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MapPanel.java
 *
 * Created on 26-mag-2009, 17.46.15
 */

package com.jaspersoft.ireport.components.spiderchart;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.charts.datasets.CategorySeriesDialog;
import com.jaspersoft.ireport.designer.charts.datasets.DatasetListsCellRenderer;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.ExpressionInterpreter;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableColumnModel;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.components.spiderchart.SpiderChartComponent;
import net.sf.jasperreports.components.spiderchart.StandardSpiderDataset;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.design.JasperDesign;
import com.jaspersoft.ireport.designer.tools.*;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;



/**
 *
 * @author gtoffoli
 */
public class SpiderChartPanel extends javax.swing.JPanel {

    private SpiderChartComponent currentSelectedChartComponent = null;
    private int dialogResult = javax.swing.JOptionPane.OK_OPTION;
    private JasperDesign jasperDesign = null;
    private ExpressionContext expressionContext = null;
    private boolean init = false;
    private JDialog dialog = null;
    private boolean simplePropertyModeActive = true;
    private ExpressionInterpreter expressionInterpreter = null;
    private JPanel jPanelSimpleProps = null;

    //public static final Color PROPERTY_OVERRIDDEN_COLOR = new Color(153,0,153);

    /** Creates new form MapPanel */
    public SpiderChartPanel() {
        initComponents();
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.NONE,I18n.getString("ChartPropertiesDialog.ComboBoxReset.none")));
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.REPORT,I18n.getString("ChartPropertiesDialog.ComboBoxReset.report")));
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.PAGE,I18n.getString("ChartPropertiesDialog.ComboBoxReset.page")));
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.COLUMN,I18n.getString("ChartPropertiesDialog.ComboBoxReset.column")));
        this.jComboBoxResetType.addItem(new Tag(ResetTypeEnum.GROUP,I18n.getString("ChartPropertiesDialog.ComboBoxReset.group")));

        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.NONE,I18n.getString("ChartPropertiesDialog.ComboBoxIncrementType.none")));
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.REPORT,I18n.getString("ChartPropertiesDialog.ComboBoxIncrementType.report")));
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.PAGE,I18n.getString("ChartPropertiesDialog.ComboBoxIncrementType.page")));
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.COLUMN,I18n.getString("ChartPropertiesDialog.ComboBoxIncrementType.column")));
        this.jComboBoxIncrementType.addItem(new Tag(IncrementTypeEnum.GROUP,I18n.getString("ChartPropertiesDialog.ComboBoxIncrementType.group")));

        jComboBoxDatasetConnectionType.addItem(new Tag(I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.noConnectionNoDatasource"),I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.noConnectionNoDatasource")));
        jComboBoxDatasetConnectionType.addItem(new Tag(I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.connExpression"),I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.connExpression")));
        jComboBoxDatasetConnectionType.addItem(new Tag(I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.datasourceExpr"),I18n.getString("ChartPropertiesDialog.ComboBoxConnectionType.datasourceExpr")));

        this.jRTextExpressionAreaMapExpression.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
        });

        this.jRTextExpressionAreaFilterExpression.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaFilterExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaFilterExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaFilterExpressionTextChanged();
            }
        });

        this.jRTextExpressionAreaTextConnectionExpression.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
        });

        javax.swing.DefaultListSelectionModel dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableDatasetParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableDatasetParametersListSelectionValueChanged(e);
            }
        });

        
        dlsm =  (javax.swing.DefaultListSelectionModel)this.jList1.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jListEntitiesSelectionValueChanged(e);
            }
        });

        DatasetParametersTableCellRenderer dpcr = new DatasetParametersTableCellRenderer();
        ((DefaultTableColumnModel)jTableDatasetParameters.getColumnModel()).getColumn(0).setCellRenderer(dpcr);
        ((DefaultTableColumnModel)jTableDatasetParameters.getColumnModel()).getColumn(1).setCellRenderer(dpcr);

        jList1.setModel(new DefaultListModel());
        jList1.setCellRenderer(new DatasetListsCellRenderer());

    }


    public void jRTextExpressionAreaMapExpressionTextChanged() {
        if (this.isInit()) return;
        if (currentSelectedChartComponent != null)
        {
            JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)currentSelectedChartComponent.getDataset().getDatasetRun();
            if (datasetRun != null)
            {
                JRDesignExpression exp = null;
                if (jRTextExpressionAreaMapExpression.getText().trim().length() > 0)
                {
                    exp = new JRDesignExpression();
                    exp.setValueClassName("java.util.Map");//NOI18N
                    exp.setText(jRTextExpressionAreaMapExpression.getText());
                }

                datasetRun.setParametersMapExpression(exp);
                notifyChange();
            }
        }
    }

    public void jRTextExpressionAreaFilterExpressionTextChanged() {
        if (this.isInit()) return;
        if (currentSelectedChartComponent != null)
        {
            JRDesignExpression exp = null;
            if (jRTextExpressionAreaFilterExpression.getText().trim().length() > 0)
            {
                exp = new JRDesignExpression();
                exp.setValueClassName("java.lang.Boolean");//NOI18N
                exp.setText(jRTextExpressionAreaFilterExpression.getText());
            }

            ((StandardSpiderDataset) currentSelectedChartComponent.getDataset()).setIncrementWhenExpression(exp);
            notifyChange();
        }
    }

    public void jRTextExpressionAreaTextConnectionExpressionTextChanged() {
        if (this.isInit()) return;
        if (currentSelectedChartComponent != null)
        {
            JRDesignExpression exp = null;
            if (jRTextExpressionAreaTextConnectionExpression.getText().trim().length() > 0)
            {
                exp = new JRDesignExpression();
                exp.setText(jRTextExpressionAreaTextConnectionExpression.getText());
            }

            int index = jComboBoxDatasetConnectionType.getSelectedIndex();

            if (index == 1)
            {
                if (exp != null) exp.setValueClassName("java.sql.Connection");//NOI18N
                ((JRDesignDatasetRun) currentSelectedChartComponent.getDataset().getDatasetRun()).setConnectionExpression(exp);
            }
            else if (index == 2)
            {
                if (exp != null) exp.setValueClassName("net.sf.jasperreports.engine.JRDataSource");//NOI18N
                ((JRDesignDatasetRun) currentSelectedChartComponent.getDataset().getDatasetRun()).setDataSourceExpression(exp);
            }
            notifyChange();
         }
    }

    public void jTableDatasetParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableDatasetParameters.getSelectedRowCount() > 0) {
            this.jButtonModParameter.setEnabled(true);
            this.jButtonRemParameter.setEnabled(true);
        }
        else {
            this.jButtonModParameter.setEnabled(false);
            this.jButtonRemParameter.setEnabled(false);
        }
    }

    public void jListEntitiesSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jList1.getSelectedIndex() >= 0) {
            this.jButtonModify.setEnabled(true);
            this.jButtonRemove.setEnabled(true);
        }
        else {
            this.jButtonModify.setEnabled(false);
            this.jButtonRemove.setEnabled(false);
        }
    }



    /**
     * this method notifies a chart dataset panel the subdataset used to fill it
     */
    public void setExpressionContext(ExpressionContext ec)
    {
        expressionContext = ec;
        jRTextExpressionAreaFilterExpression.setExpressionContext(ec);
    }

    public boolean isInit() {
        return init || (currentSelectedChartComponent == null);
    }

    public boolean setInit(boolean init) {
        boolean oldInit = this.init;
        this.init = init;
        return oldInit;
    }

   public void notifyChange()
   {
        IReportManager.getInstance().notifyReportChange();
   }


   public void updateSubDatasets()
    {

        if (getJasperDesign() == null)
	{
            jComboBoxSubDataset.removeAllItems();
            jComboBoxSubDataset.addItem("");//NOI18N
        }
        else
        {
            List<String> datasetNames = new ArrayList<String>();
            for (int i=0; i<getJasperDesign().getDatasetsList().size(); ++i)
            {
                datasetNames.add( ((JRDataset)getJasperDesign().getDatasetsList().get(i)).getName());
            }

            Misc.updateComboBox(jComboBoxSubDataset, datasetNames, true);
        }
    }

   /**
     * This method update the comboboxes where is present the goup list.
     */
    public void updateGroups()
    {

        if (getJasperDesign() == null)
	{
            jComboBoxResetGroup.removeAllItems();
        }
        else
        {
            List<String> groupNames = new ArrayList<String>();

            List groups = getJasperDesign().getGroupsList();

            if (currentSelectedChartComponent != null &&
                currentSelectedChartComponent.getDataset() != null &&
                currentSelectedChartComponent.getDataset().getDatasetRun() != null &&
                currentSelectedChartComponent.getDataset().getDatasetRun().getDatasetName() != null)
            {
                JRDesignDataset ds = getComponentDataset();
                if (ds != null) groups = ds.getGroupsList();
            }

            for (int i=0; i<groups.size(); ++i)
            {
                groupNames.add( ((JRGroup)groups.get(i)).getName());
            }

            Misc.updateComboBox(jComboBoxResetGroup, groupNames);
            Misc.updateComboBox(jComboBoxIncrementGroup, groupNames);
        }

    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPopupMenuSeries = new javax.swing.JPopupMenu();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemPaste = new javax.swing.JMenuItem();
        jButtonClose = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanelDataset = new javax.swing.JPanel();
        jLabelResetType = new javax.swing.JLabel();
        jComboBoxResetType = new javax.swing.JComboBox();
        jLabelResetGroup = new javax.swing.JLabel();
        jComboBoxResetGroup = new javax.swing.JComboBox();
        jLabelIncrementType = new javax.swing.JLabel();
        jComboBoxIncrementType = new javax.swing.JComboBox();
        jLabelIncrementGroup = new javax.swing.JLabel();
        jComboBoxIncrementGroup = new javax.swing.JComboBox();
        jLabelIncrementType2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabelIncrementType1 = new javax.swing.JLabel();
        jComboBoxSubDataset = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPaneSubDataset = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jComboBoxDatasetConnectionType = new javax.swing.JComboBox();
        jRTextExpressionAreaTextConnectionExpression = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanel9 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDatasetParameters = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jButtonAddParameter = new javax.swing.JButton();
        jButtonModParameter = new javax.swing.JButton();
        jButtonRemParameter = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jRTextExpressionAreaMapExpression = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jRTextExpressionAreaFilterExpression = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanelEntities = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jButtonAdd = new javax.swing.JButton();
        jButtonModify = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        jMenuItemCopy.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jMenuItemCopy.text")); // NOI18N
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyActionPerformed(evt);
            }
        });
        jPopupMenuSeries.add(jMenuItemCopy);

        jMenuItemPaste.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jMenuItemPaste.text")); // NOI18N
        jMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteActionPerformed(evt);
            }
        });
        jPopupMenuSeries.add(jMenuItemPaste);

        jButtonClose.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jButtonClose.text")); // NOI18N
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanelDataset.setLayout(new java.awt.GridBagLayout());

        jLabelResetType.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jLabelResetType.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelResetType, gridBagConstraints);

        jComboBoxResetType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxResetTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxResetType, gridBagConstraints);

        jLabelResetGroup.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jLabelResetGroup.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelResetGroup, gridBagConstraints);

        jComboBoxResetGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxResetGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxResetGroup, gridBagConstraints);

        jLabelIncrementType.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jLabelIncrementType.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelIncrementType, gridBagConstraints);

        jComboBoxIncrementType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxIncrementTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxIncrementType, gridBagConstraints);

        jLabelIncrementGroup.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jLabelIncrementGroup.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelIncrementGroup, gridBagConstraints);

        jComboBoxIncrementGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxIncrementGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxIncrementGroup, gridBagConstraints);

        jLabelIncrementType2.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jLabelIncrementType2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelIncrementType2, gridBagConstraints);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jPanel6.border.title"))); // NOI18N
        jPanel6.setPreferredSize(new java.awt.Dimension(329, 192));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabelIncrementType1.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jLabelIncrementType1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel6.add(jLabelIncrementType1, gridBagConstraints);

        jComboBoxSubDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSubDatasetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jComboBoxSubDataset, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLabel41.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jLabel41.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel8.add(jLabel41, gridBagConstraints);

        jComboBoxDatasetConnectionType.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxDatasetConnectionType.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxDatasetConnectionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDatasetConnectionTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanel8.add(jComboBoxDatasetConnectionType, gridBagConstraints);

        jRTextExpressionAreaTextConnectionExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
        jRTextExpressionAreaTextConnectionExpression.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaTextConnectionExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanel8.add(jRTextExpressionAreaTextConnectionExpression, gridBagConstraints);

        jTabbedPaneSubDataset.addTab(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jPanel8.TabConstraints.tabTitle"), jPanel8); // NOI18N

        jPanel9.setLayout(new java.awt.GridBagLayout());

        jPanel16.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setMinimumSize(new java.awt.Dimension(300, 50));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 50));

        jTableDatasetParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parameter", "Expression"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDatasetParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDatasetParametersMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableDatasetParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel16.add(jScrollPane2, gridBagConstraints);

        jPanel10.setMinimumSize(new java.awt.Dimension(100, 10));
        jPanel10.setPreferredSize(new java.awt.Dimension(100, 69));
        jPanel10.setLayout(new java.awt.GridBagLayout());

        jButtonAddParameter.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jButtonAddParameter.text")); // NOI18N
        jButtonAddParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel10.add(jButtonAddParameter, gridBagConstraints);

        jButtonModParameter.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jButtonModParameter.text")); // NOI18N
        jButtonModParameter.setEnabled(false);
        jButtonModParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel10.add(jButtonModParameter, gridBagConstraints);

        jButtonRemParameter.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jButtonRemParameter.text")); // NOI18N
        jButtonRemParameter.setEnabled(false);
        jButtonRemParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel10.add(jButtonRemParameter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel16.add(jPanel10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel9.add(jPanel16, gridBagConstraints);

        jTabbedPaneSubDataset.addTab(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jPanel9.TabConstraints.tabTitle"), jPanel9); // NOI18N

        jPanel11.setLayout(new java.awt.GridBagLayout());

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jLabel26.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 0, 0);
        jPanel11.add(jLabel26, gridBagConstraints);

        jRTextExpressionAreaMapExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaMapExpression.setMinimumSize(new java.awt.Dimension(0, 0));
        jRTextExpressionAreaMapExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanel11.add(jRTextExpressionAreaMapExpression, gridBagConstraints);

        jTabbedPaneSubDataset.addTab(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jPanel11.TabConstraints.tabTitle"), jPanel11); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel7.add(jTabbedPaneSubDataset, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 100;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelDataset.add(jPanel6, gridBagConstraints);

        jRTextExpressionAreaFilterExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaFilterExpression.setMinimumSize(new java.awt.Dimension(400, 50));
        jRTextExpressionAreaFilterExpression.setPreferredSize(new java.awt.Dimension(400, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        jPanelDataset.add(jRTextExpressionAreaFilterExpression, gridBagConstraints);

        jPanel4.add(jPanelDataset, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        jPanelEntities.setLayout(new java.awt.BorderLayout());

        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel1formComponentShown(evt);
            }
        });
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jPanel2.setMinimumSize(new java.awt.Dimension(100, 0));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 0));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButtonAdd.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jButtonAdd.text")); // NOI18N
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 4);
        jPanel2.add(jButtonAdd, gridBagConstraints);

        jButtonModify.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jButtonModify.text")); // NOI18N
        jButtonModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        jPanel2.add(jButtonModify, gridBagConstraints);

        jButtonRemove.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jButtonRemove.text")); // NOI18N
        jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        jPanel2.add(jButtonRemove, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 99;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jPanel2, gridBagConstraints);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel1.add(jLabel1, gridBagConstraints);

        jPanelEntities.add(jPanel1, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab(org.openide.util.NbBundle.getMessage(SpiderChartPanel.class, "SpiderChartPanel.jPanelEntities.TabConstraints.tabTitle"), jPanelEntities); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 463, Short.MAX_VALUE)
            .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 399, Short.MAX_VALUE)
            .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(394, Short.MAX_VALUE)
                .add(jButtonClose)
                .addContainerGap())
            .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonClose)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    private void jComboBoxResetTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxResetTypeActionPerformed


        if (isInit() || getCurrentSelectedChartComponent() == null) return;

        ResetTypeEnum val = (ResetTypeEnum)((Tag)jComboBoxResetType.getSelectedItem()).getValue();

        if (val == ResetTypeEnum.GROUP) {
            List groups = getComponentDataset().getGroupsList();

            

            if (groups.isEmpty()) {
                setInit(true);
                Misc.setComboboxSelectedTagValue(jComboBoxResetType, getCurrentSelectedChartComponent().getDataset().getResetTypeValue());
                SwingUtilities.invokeLater(new Runnable(){
                    public void run() {
                        JOptionPane.showMessageDialog(jComboBoxResetGroup, I18n.getString("ChartPropertiesDialog.MessageDialog.NoGroupsAvail"));
                    }
                });
                setInit(false);
                return;
            } else {
                ((StandardSpiderDataset)getCurrentSelectedChartComponent().getDataset()).setResetType(val);
                jComboBoxResetGroup.setEnabled(true);
                jComboBoxResetGroup.setSelectedIndex(0);
            }
        } else {
            ((StandardSpiderDataset)getCurrentSelectedChartComponent().getDataset()).setResetType(val);
            ((StandardSpiderDataset)getCurrentSelectedChartComponent().getDataset()).setResetGroup(null);
            jComboBoxResetGroup.setEnabled(false);
            jComboBoxResetGroup.setSelectedItem(null);
        }
}//GEN-LAST:event_jComboBoxResetTypeActionPerformed

    private void jComboBoxResetGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxResetGroupActionPerformed

        if (isInit() || getCurrentSelectedChartComponent() == null) return;
        String name = (String)jComboBoxResetGroup.getSelectedItem();

        if (name != null && name.trim().length() != 0) {
            ((StandardSpiderDataset)getCurrentSelectedChartComponent().getDataset()).setResetGroup( (JRGroup)getComponentDataset().getGroupsMap().get(name));
        } else {
            ((StandardSpiderDataset)getCurrentSelectedChartComponent().getDataset()).setResetGroup(null);
        }
}//GEN-LAST:event_jComboBoxResetGroupActionPerformed

    private void jComboBoxIncrementTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIncrementTypeActionPerformed


        if (isInit() || getCurrentSelectedChartComponent() == null) return;

        IncrementTypeEnum val = (IncrementTypeEnum)((Tag)jComboBoxIncrementType.getSelectedItem()).getValue();

        if (val == IncrementTypeEnum.GROUP) {
            // Currently selected dataset...
            List groups = getComponentDataset().getGroupsList();

            
            if (groups.size() == 0) {
                setInit(true);
                Misc.setComboboxSelectedTagValue(jComboBoxIncrementType,getCurrentSelectedChartComponent().getDataset().getIncrementTypeValue());
                SwingUtilities.invokeLater(new Runnable(){
                    public void run() {
                        JOptionPane.showMessageDialog(jComboBoxIncrementGroup, I18n.getString("ChartPropertiesDialog.MessageDialog.NoGroupsAvail"));
                    }
                });
                setInit(false);
                return;
            } else {
                ((StandardSpiderDataset)getCurrentSelectedChartComponent().getDataset()).setIncrementType(val);
                jComboBoxIncrementGroup.setEnabled(true);
                jComboBoxIncrementGroup.setSelectedIndex(0);
            }
        } else {
            ((StandardSpiderDataset)getCurrentSelectedChartComponent().getDataset()).setIncrementType(val);
            ((StandardSpiderDataset)getCurrentSelectedChartComponent().getDataset()).setIncrementGroup(null);
            jComboBoxIncrementGroup.setEnabled(false);
            jComboBoxIncrementGroup.setSelectedItem(null);
        }
    }//GEN-LAST:event_jComboBoxIncrementTypeActionPerformed

    private void jComboBoxIncrementGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIncrementGroupActionPerformed
        if (isInit() || getCurrentSelectedChartComponent() == null) return;
        String name = (String)jComboBoxIncrementGroup.getSelectedItem();

        if (name != null && name.trim().length() != 0) {
            ((StandardSpiderDataset)getCurrentSelectedChartComponent().getDataset()).setIncrementGroup( (JRGroup)getComponentDataset().getGroupsMap().get(name));
        } else {
            ((StandardSpiderDataset)getCurrentSelectedChartComponent().getDataset()).setIncrementGroup(null);
        }
}//GEN-LAST:event_jComboBoxIncrementGroupActionPerformed

    private void jComboBoxSubDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSubDatasetActionPerformed

        if (this.isInit()) return;

        if (getCurrentSelectedChartComponent() != null) {
            // If we selected a goof dataset....
            if (this.jComboBoxSubDataset.getSelectedIndex() > 0) {
                // Check subdataset parameters....
                JRDesignDataset dds = (JRDesignDataset)getJasperDesign().getDatasetMap().get(""+jComboBoxSubDataset.getSelectedItem());
                ExpressionContext ec = new ExpressionContext(dds);
                setExpressionContext( ec );

                if (getCurrentSelectedChartComponent().getDataset().getDatasetRun() == null ||
                        !("" + jComboBoxSubDataset.getSelectedItem()).equals(currentSelectedChartComponent.getDataset().getDatasetRun().getDatasetName()) )//NOI18N
                {

                    JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)getCurrentSelectedChartComponent().getDataset().getDatasetRun();
                    if (datasetRun == null) {
                        datasetRun = new JRDesignDatasetRun();
                        ((StandardSpiderDataset)getCurrentSelectedChartComponent().getDataset()).setDatasetRun(datasetRun);

                        setInit(true);
                        this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                        this.jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
                        jRTextExpressionAreaMapExpression.setText("");//NOI18N
                        setInit(false);

                        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridy = 1;
                        gridBagConstraints.gridwidth = 2;
                        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
                        jPanel7.add(jTabbedPaneSubDataset, gridBagConstraints);

                        //jTabbedPaneSubDataset.setVisible(true);
                        jPanel7.updateUI();
                    }
                    datasetRun.setDatasetName("" + jComboBoxSubDataset.getSelectedItem());//NOI18N
                    // we need to fix the groups...
                    updateGroups();
                }
            } else {
                ExpressionContext ec = new ExpressionContext(getJasperDesign().getMainDesignDataset());
                ((StandardSpiderDataset)getCurrentSelectedChartComponent().getDataset()).setDatasetRun(null);

                setExpressionContext( ec );

                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                dtm.setRowCount(0);
                setInit(true);
                this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                this.jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
                jRTextExpressionAreaMapExpression.setText("");//NOI18N
                setInit(false);

                jPanel7.remove(jTabbedPaneSubDataset);
                //jTabbedPaneSubDataset.setVisible(false);
                //jTabbedPaneSubDataset.updateUI();
                jPanel7.updateUI();
            }

            updateGroups();
            List groups = getJasperDesign().getGroupsList();

            if (getCurrentSelectedChartComponent() != null &&
                    getCurrentSelectedChartComponent().getDataset() != null &&
                    getCurrentSelectedChartComponent().getDataset().getDatasetRun() != null &&
                    getCurrentSelectedChartComponent().getDataset().getDatasetRun().getDatasetName() != null) {
                String dsName = getCurrentSelectedChartComponent().getDataset().getDatasetRun().getDatasetName();
                groups = ((JRDesignDataset)getJasperDesign().getDatasetMap().get(dsName)).getGroupsList();
            }

            if (groups.size() == 0) {
                IncrementTypeEnum val = (IncrementTypeEnum)((Tag)jComboBoxIncrementType.getSelectedItem()).getValue();
                if (val == IncrementTypeEnum.GROUP) {
                    setInit(true);
                    //((StandardSpiderDataset)currentSelectedChartComponent.getDataset()).setIncrementType(JRVariable.RESET_TYPE_REPORT);
                    //((StandardSpiderDataset)currentSelectedChartComponent.getDataset()).setIncrementGroup(null);
                    Misc.setComboboxSelectedTagValue(jComboBoxIncrementType, ResetTypeEnum.NONE);
                    setInit(false);
                }
                ResetTypeEnum resetVal = (ResetTypeEnum)((Tag)jComboBoxResetType.getSelectedItem()).getValue();
                if (resetVal == ResetTypeEnum.GROUP) {
                    setInit(true);
                    //((StandardSpiderDataset)currentSelectedChartComponent.getDataset()).setResetType(JRVariable.RESET_TYPE_REPORT);
                    //((StandardSpiderDataset)currentSelectedChartComponent.getDataset()).setResetGroup(null);
                    Misc.setComboboxSelectedTagValue(jComboBoxResetType, ResetTypeEnum.REPORT);
                    setInit(false);
                }
            }
            jComboBoxIncrementTypeActionPerformed(null);
            jComboBoxResetTypeActionPerformed(null);

            getCurrentSelectedChartComponent().getEventSupport().firePropertyChange(StandardSpiderDataset.PROPERTY_DATASET_RUN, null, null);
        }
        notifyChange();
}//GEN-LAST:event_jComboBoxSubDatasetActionPerformed

    private void jComboBoxDatasetConnectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDatasetConnectionTypeActionPerformed
        if (isInit() || getCurrentSelectedChartComponent() == null) return;

        JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)getCurrentSelectedChartComponent().getDataset().getDatasetRun();
        if (jComboBoxDatasetConnectionType.getSelectedIndex() == 0) {
            jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
            jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
            datasetRun.setConnectionExpression(null);
            datasetRun.setDataSourceExpression(null);
        } else if (jComboBoxDatasetConnectionType.getSelectedIndex() == 1) {

            jRTextExpressionAreaTextConnectionExpression.setText("$P{REPORT_CONNECTION}");//NOI18N
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);

            datasetRun.setDataSourceExpression(null);

            JRDesignExpression exp = new JRDesignExpression();
            exp.setValueClassName("java.sql.Connection");//NOI18N
            exp.setText("$P{REPORT_CONNECTION}");//NOI18N
            datasetRun.setConnectionExpression(exp);

        } else if (jComboBoxDatasetConnectionType.getSelectedIndex() == 2) {

            jRTextExpressionAreaTextConnectionExpression.setText("new net.sf.jasperreports.engine.JREmptyDataSource(1)");//NOI18N
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
            datasetRun.setConnectionExpression(null);

            JRDesignExpression exp = new JRDesignExpression();
            exp.setValueClassName("net.sf.jasperreports.engine.JRDataSource");//NOI18N
            exp.setText("new net.sf.jasperreports.engine.JREmptyDataSource(1)");//NOI18N
            datasetRun.setDataSourceExpression(exp);
        }

        notifyChange();
}//GEN-LAST:event_jComboBoxDatasetConnectionTypeActionPerformed

    private void jTableDatasetParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDatasetParametersMouseClicked

        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            if (jTableDatasetParameters.getSelectedRowCount() > 0) {
                jButtonModParameterActionPerformed(null);
            }
        }
    }//GEN-LAST:event_jTableDatasetParametersMouseClicked

    private void jButtonAddParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddParameterActionPerformed
        if (this.isInit()) return;

        if (getCurrentSelectedChartComponent() == null) return;
        // Set the new value for all selected elements...

        java.util.HashMap map = new java.util.HashMap();
        java.util.List<JRDatasetParameter> params = Arrays.asList(getCurrentSelectedChartComponent().getDataset().getDatasetRun().getParameters());
        for (JRDatasetParameter p : params) {
            map.put(p.getName(), p);
        }

        Object pWin = SwingUtilities.windowForComponent(this);
        JRDatasetParameterDialog jrpd = null;
        if (pWin instanceof Dialog) jrpd = new JRDatasetParameterDialog((Dialog)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(getCurrentSelectedChartComponent().getDataset().getDatasetRun().getDatasetName()));
        else jrpd = new JRDatasetParameterDialog((Frame)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(getCurrentSelectedChartComponent().getDataset().getDatasetRun().getDatasetName()));

        ExpressionContext docEc = new ExpressionContext( getJasperDesign().getMainDesignDataset() );
        jrpd.setExpressionContext(docEc);
        jrpd.setVisible(true);

        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            JRDesignDatasetParameter parameter = jrpd.getParameter();
            try {
                ((JRDesignDatasetRun)getCurrentSelectedChartComponent().getDataset().getDatasetRun()).addParameter( parameter );
                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                dtm.addRow(new Object[]{parameter, parameter.getExpression()});
                notifyChange();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
}//GEN-LAST:event_jButtonAddParameterActionPerformed

    private void jButtonModParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModParameterActionPerformed

        if (this.isInit()) return;

        if (getCurrentSelectedChartComponent() == null) return;
        int rowNumber = jTableDatasetParameters.getSelectedRow();
        JRDesignDatasetParameter parameter = (JRDesignDatasetParameter)jTableDatasetParameters.getValueAt( jTableDatasetParameters.getSelectedRow(), 0);

        java.util.HashMap map = new java.util.HashMap();
        java.util.List<JRDatasetParameter> params = Arrays.asList(getCurrentSelectedChartComponent().getDataset().getDatasetRun().getParameters());
        for (JRDatasetParameter p : params) {
            map.put(p.getName(), p);
        }

        Object pWin = SwingUtilities.windowForComponent(this);
        JRDatasetParameterDialog jrpd = null;
        if (pWin instanceof Dialog) jrpd = new JRDatasetParameterDialog((Dialog)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(getCurrentSelectedChartComponent().getDataset().getDatasetRun().getDatasetName()) );
        else jrpd = new JRDatasetParameterDialog((Frame)pWin,map, (JRDesignDataset)getJasperDesign().getDatasetMap().get(getCurrentSelectedChartComponent().getDataset().getDatasetRun().getDatasetName()));

        ExpressionContext docEc = new ExpressionContext( getJasperDesign().getMainDesignDataset() );
        jrpd.setExpressionContext(docEc);

        jrpd.setParameter( parameter );

        /*
        if (subdatasetParameterHighlightExpression != null)
        {
            jrpd.setFocusedExpression( ((Integer)subdatasetParameterHighlightExpression[0]).intValue() );
        }
         */
        jrpd.setVisible(true);

        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            parameter.setName( jrpd.getParameter().getName() );
            parameter.setExpression( jrpd.getParameter().getExpression());
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
            dtm.setValueAt(parameter, rowNumber, 0);
            dtm.setValueAt(parameter.getExpression(), rowNumber, 1);
            jTableDatasetParameters.updateUI();
            notifyChange();
        }
    }//GEN-LAST:event_jButtonModParameterActionPerformed

    private void jButtonRemParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemParameterActionPerformed
        if (this.isInit()) return;

        if (getCurrentSelectedChartComponent() == null) return;

        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();

        JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)getCurrentSelectedChartComponent().getDataset().getDatasetRun();

        while (jTableDatasetParameters.getSelectedRowCount() > 0) {
            int i=jTableDatasetParameters.getSelectedRow();
            datasetRun.removeParameter( ((JRDatasetParameter)jTableDatasetParameters.getValueAt( i, 0)).getName() );
            dtm.removeRow(i);
        }
        notifyChange();
    }//GEN-LAST:event_jButtonRemParameterActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (evt.getClickCount() == 1 && evt.getButton() == MouseEvent.BUTTON3) {
            jMenuItemCopy.setEnabled(jList1.getSelectedIndex() >= 0);
            jMenuItemPaste.setEnabled( IReportManager.getInstance().getChartSeriesClipBoard() != null &&
                    IReportManager.getInstance().getChartSeriesClipBoard().size() > 0);

            jPopupMenuSeries.show(jList1, evt.getPoint().x, evt.getPoint().y);
        } else if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1) {
            jButtonModifyActionPerformed(null);
        }
}//GEN-LAST:event_jList1MouseClicked

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged

        if (jList1.getSelectedIndex() >= 0) {
            jButtonModify.setEnabled( true );
            jButtonRemove.setEnabled( true );
        } else {
            jButtonModify.setEnabled( false );
            jButtonRemove.setEnabled( false );
        }
}//GEN-LAST:event_jList1ValueChanged

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed

        if (currentSelectedChartComponent == null) return;
        CategorySeriesDialog csd = new CategorySeriesDialog(Misc.getMainFrame() ,true);
        csd.setExpressionContext( this.getExpressionContext() );
        csd.setVisible(true);
        if (csd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            JRDesignCategorySeries cs = new JRDesignCategorySeries();
            cs.setSeriesExpression( csd.getSeriesExpression() );
            cs.setValueExpression( csd.getValueExpression() );
            cs.setLabelExpression( csd.getLabelExpression() );
            cs.setCategoryExpression(csd.getCategoryExpression() );
            cs.setItemHyperlink(csd.getSectionItemHyperlink() );

            ((StandardSpiderDataset)currentSelectedChartComponent.getDataset()).addCategorySeries(cs);
            ((javax.swing.DefaultListModel)jList1.getModel()).addElement(cs);
            notifyChange();
        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyActionPerformed

        if (currentSelectedChartComponent == null) return;
        if (jList1.getSelectedIndex() >= 0) {
            JRDesignCategorySeries cs = (JRDesignCategorySeries)jList1.getSelectedValue();
            CategorySeriesDialog csd = new CategorySeriesDialog(Misc.getMainFrame() ,true);

            csd.setSeriesExpression( (JRDesignExpression)cs.getSeriesExpression() );
            csd.setValueExpression( (JRDesignExpression)cs.getValueExpression() );
            csd.setLabelExpression( (JRDesignExpression)cs.getLabelExpression() );
            csd.setCategoryExpression((JRDesignExpression)cs.getCategoryExpression() );

            JRDesignHyperlink link = new JRDesignHyperlink();
            ModelUtils.copyHyperlink(cs.getItemHyperlink(), link);
            csd.setSectionItemHyperlink( link  );

            csd.setExpressionContext( this.getExpressionContext() );
            csd.setVisible(true);

            if (csd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
                cs.setSeriesExpression( csd.getSeriesExpression() );
                cs.setValueExpression( csd.getValueExpression() );
                cs.setLabelExpression( csd.getLabelExpression() );
                cs.setCategoryExpression(csd.getCategoryExpression() );
                cs.setItemHyperlink( csd.getSectionItemHyperlink() );

                jList1.updateUI();
                notifyChange();
            }

        }
}//GEN-LAST:event_jButtonModifyActionPerformed

    private void jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveActionPerformed

        if (currentSelectedChartComponent == null) return;
        while (jList1.getSelectedIndex() >= 0) {
            ((StandardSpiderDataset)currentSelectedChartComponent.getDataset()).removeCategorySeries((JRCategorySeries)jList1.getSelectedValue() );
            ((javax.swing.DefaultListModel)jList1.getModel()).removeElementAt(jList1.getSelectedIndex());
            notifyChange();
        }
    }//GEN-LAST:event_jButtonRemoveActionPerformed

    private void jPanel1formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1formComponentShown
        
}//GEN-LAST:event_jPanel1formComponentShown

    private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopyActionPerformed
        Object[] values = jList1.getSelectedValues();
        java.util.List copy_c = new ArrayList();
        try {
            for (int i=0; i<values.length; ++i) copy_c.add( cloneChartDatasetItem((JRDesignCategorySeries)values[i]) );
            IReportManager.getInstance().setChartSeriesClipBoard(copy_c);
        } catch (Exception ex) { }
}//GEN-LAST:event_jMenuItemCopyActionPerformed

    private void jMenuItemPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteActionPerformed
        java.util.List series = IReportManager.getInstance().getChartSeriesClipBoard();
        //getChartSeriesClipBoard()
        if (currentSelectedChartComponent == null) return;
        if (series != null && series.size() > 0) {
            for (int i=0; i<series.size(); ++i) {
                if (series.get(i) instanceof JRCategorySeries) {
                    JRDesignCategorySeries cs = (JRDesignCategorySeries)series.get(i);
                    try {
                        cs = cloneChartDatasetItem((JRDesignCategorySeries)cs);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        continue;
                    }
                    ((StandardSpiderDataset)currentSelectedChartComponent.getDataset()).addCategorySeries(cs);
                    ((javax.swing.DefaultListModel)jList1.getModel()).addElement(cs);
                }
            }
            jList1.updateUI();
        }
}//GEN-LAST:event_jMenuItemPasteActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        if (getDialog() != null)
        {
            getDialog().setVisible(false);
            getDialog().dispose();
        }
    }//GEN-LAST:event_jButtonCloseActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonAddParameter;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonModParameter;
    private javax.swing.JButton jButtonModify;
    private javax.swing.JButton jButtonRemParameter;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JComboBox jComboBoxDatasetConnectionType;
    private javax.swing.JComboBox jComboBoxIncrementGroup;
    private javax.swing.JComboBox jComboBoxIncrementType;
    private javax.swing.JComboBox jComboBoxResetGroup;
    private javax.swing.JComboBox jComboBoxResetType;
    private javax.swing.JComboBox jComboBoxSubDataset;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabelIncrementGroup;
    private javax.swing.JLabel jLabelIncrementType;
    private javax.swing.JLabel jLabelIncrementType1;
    private javax.swing.JLabel jLabelIncrementType2;
    private javax.swing.JLabel jLabelResetGroup;
    private javax.swing.JLabel jLabelResetType;
    private javax.swing.JList jList1;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemPaste;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelDataset;
    private javax.swing.JPanel jPanelEntities;
    private javax.swing.JPopupMenu jPopupMenuSeries;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaFilterExpression;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaMapExpression;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaTextConnectionExpression;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPaneSubDataset;
    private javax.swing.JTable jTableDatasetParameters;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the currentSelectedChartComponent
     */
    public SpiderChartComponent getCurrentSelectedChartComponent() {
        return currentSelectedChartComponent;
    }

    /**
     * @param currentSelectedChartComponent the currentSelectedChartComponent to set
     */
    public void setCurrentSelectedChartComponent(SpiderChartComponent chartComponent, JasperDesign jd) {
        setInit(true);
        this.jasperDesign = jd;

        try {

            jPanelSimpleProps = null;

            currentSelectedChartComponent = chartComponent;

            if (currentSelectedChartComponent != null)
            {
                updateGroups();
                updateSubDatasets();

                // Set general dataset data...

                Misc.setComboboxSelectedTagValue(jComboBoxIncrementType, currentSelectedChartComponent.getDataset().getIncrementTypeValue() );
                jComboBoxIncrementGroup.setEnabled(currentSelectedChartComponent.getDataset().getIncrementTypeValue() == IncrementTypeEnum.GROUP);
                if (currentSelectedChartComponent.getDataset().getIncrementTypeValue() == IncrementTypeEnum.GROUP)
                {
                    jComboBoxIncrementGroup.setSelectedItem( currentSelectedChartComponent.getDataset().getIncrementGroup().getName() );
                }

                Misc.setComboboxSelectedTagValue(jComboBoxResetType, currentSelectedChartComponent.getDataset().getResetTypeValue() );
                jComboBoxResetGroup.setEnabled(currentSelectedChartComponent.getDataset().getResetTypeValue() == ResetTypeEnum.GROUP);
                if (currentSelectedChartComponent.getDataset().getResetTypeValue() == ResetTypeEnum.GROUP)
                {
                    jComboBoxResetGroup.setSelectedItem( currentSelectedChartComponent.getDataset().getResetGroup().getName() );
                }

                jRTextExpressionAreaFilterExpression.setText( Misc.getExpressionText( currentSelectedChartComponent.getDataset().getIncrementWhenExpression() ) );

                JRDesignDataset dataset = (JRDesignDataset)getJasperDesign().getMainDataset();

                if (currentSelectedChartComponent.getDataset().getDatasetRun() != null)
                {
                    JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)currentSelectedChartComponent.getDataset().getDatasetRun();

                    jComboBoxSubDataset.setSelectedItem(datasetRun.getDatasetName());
                    //jPanel7.remove(jTabbedPaneSubDataset);
                    //jTabbedPaneSubDataset.setVisible(true);
                    if (jPanel7.getComponentCount() == 0)
                    {
                        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridy = 1;
                        gridBagConstraints.gridwidth = 2;
                        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.weighty = 1.0;
                        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
                        jPanel7.add(jTabbedPaneSubDataset, gridBagConstraints);
                        jPanel7.updateUI();
                    }

                    jRTextExpressionAreaMapExpression.setText( Misc.getExpressionText( datasetRun.getParametersMapExpression() ) );

                    int connectionType = 0;

                    if ( datasetRun.getConnectionExpression() != null)
                    {
                        connectionType = 1;
                    }
                    if ( datasetRun.getDataSourceExpression() != null)
                    {
                        connectionType = 2;
                    }


                    if (connectionType == 0) {
                        this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                        this.jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
                    }
                    else if (connectionType == 1) {
                        this.jComboBoxDatasetConnectionType.setSelectedIndex(1);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
                        this.jRTextExpressionAreaTextConnectionExpression.setText( Misc.getExpressionText( datasetRun.getConnectionExpression() ));
                    }
                    else {
                        this.jComboBoxDatasetConnectionType.setSelectedIndex(2);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
                        this.jRTextExpressionAreaTextConnectionExpression.setText( Misc.getExpressionText( datasetRun.getDataSourceExpression()) );
                    }

                    //Add parameters...
                    javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                    dtm.setRowCount(0);

                    JRDatasetParameter[] params = datasetRun.getParameters();
                    for (int i=0; i<params.length; ++i) {
                        JRDatasetParameter parameter = params[i];
                        Vector row = new Vector();
                        row.addElement(parameter);
                        row.addElement( Misc.getExpressionText( parameter.getExpression() ) );
                        dtm.addRow(row);
                    }

                    dataset = (JRDesignDataset)getJasperDesign().getDatasetMap().get( datasetRun.getDatasetName() );
                }
                else
                {
                    javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                    dtm.setRowCount(0);
                    this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                    this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                    this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                    this.jRTextExpressionAreaTextConnectionExpression.setText("");//NOI18N
                    jRTextExpressionAreaMapExpression.setText("");//NOI18N

                    jComboBoxSubDataset.setSelectedIndex(0);
                    //jTabbedPaneSubDataset.setVisible(false);
                    jPanel7.remove(jTabbedPaneSubDataset);
                }

                // Fill the entities
                JRCategorySeries[] items = ((StandardSpiderDataset)currentSelectedChartComponent.getDataset()).getSeries();
                jList1.setModel(new DefaultListModel());
                for (JRCategorySeries item : items)
                {
                    ((DefaultListModel)jList1.getModel()).addElement(item);
                }
                
                // Fill the properties...
                //this.jRTitleExpression.setText(Misc.getExpressionText( currentSelectedChartComponent.getCaptionExpression() ));//NOI18N

                setExpressionContext( new ExpressionContext( getComponentDataset() ));

            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        ExpressionContext docEc = new ExpressionContext( jd.getMainDesignDataset() );
        jRTextExpressionAreaMapExpression.setExpressionContext(docEc);
        jRTextExpressionAreaTextConnectionExpression.setExpressionContext(docEc);
        //jRTitleExpression.setExpressionContext(docEc);

        if (dialog != null)
        {
            dialog.pack();
        }
        setInit(false);
    }

    /**
     * @return the jasperDesign
     */
    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    /**
     * @param jasperDesign the jasperDesign to set
     */
    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }

    /**
     * @return the expressionContext
     */
    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    public JRDesignCategorySeries cloneChartDatasetItem(JRDesignCategorySeries original)
    {
        JRDesignCategorySeries cs = new JRDesignCategorySeries();
        cs.setSeriesExpression( (JRDesignExpression)original.getSeriesExpression().clone() );
        cs.setValueExpression( (JRDesignExpression)original.getValueExpression().clone() );
        cs.setLabelExpression( (JRDesignExpression)original.getLabelExpression().clone() );
        JRDesignHyperlink link = new JRDesignHyperlink();
        ModelUtils.copyHyperlink(original.getItemHyperlink(), link);
        cs.setItemHyperlink( link  );

        return cs;
    }

    public static void showDialog(Frame parent, SpiderChartComponent chartComponent, JasperDesign jd)
    {
        final SpiderChartPanel panel = new SpiderChartPanel();

        panel.setDialog(new JDialog(parent, true));
        panel.getDialog().getContentPane().setLayout(new BorderLayout());
        
        panel.setCurrentSelectedChartComponent(chartComponent, jd);
        panel.getDialog().setTitle("Chart Properties");

        panel.getDialog().getContentPane().add(panel);
        panel.getDialog().pack();
        panel.getDialog().setLocationRelativeTo(null);

        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                panel.jButtonCloseActionPerformed(e);
            }
        };

        panel.getDialog().getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        panel.getDialog().getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);

        //to make the default button ...
        panel.getDialog().getRootPane().setDefaultButton( panel.jButtonClose);

        panel.getDialog().setVisible(true);
    }

    public JRDesignDataset getComponentDataset()
    {
        if (getJasperDesign() == null || currentSelectedChartComponent == null) return null;
        JRDesignDataset ds = getJasperDesign().getMainDesignDataset();

        if (currentSelectedChartComponent.getDataset().getDatasetRun() != null &&
            currentSelectedChartComponent.getDataset().getDatasetRun().getDatasetName() != null)
        {
            ds = (JRDesignDataset)getJasperDesign().getDatasetMap().get( currentSelectedChartComponent.getDataset().getDatasetRun().getDatasetName() );
        }

        return ds;
    }

    /**
     * @return the dialog
     */
    public JDialog getDialog() {
        return dialog;
    }

    /**
     * @param dialog the dialog to set
     */
    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }

    /**
     * @return the simplePropertyModeActive
     */
    public boolean isSimplePropertyModeActive() {
        return simplePropertyModeActive;
    }

    /**
     * @param simplePropertyModeActive the simplePropertyModeActive to set
     */
    public void setSimplePropertyModeActive(boolean simplePropertyModeActive) {
        this.simplePropertyModeActive = simplePropertyModeActive;
    }

}
