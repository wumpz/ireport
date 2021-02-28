/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
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
package com.jaspersoft.ireport.designer.tools;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRAnchor;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.design.JRDesignHyperlinkParameter;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.JREnum;


/**
 *
 * @author  gtoffoli
 */
public class HyperlinkPanel extends javax.swing.JPanel {
    
    private JRHyperlink hyperlink = null;
    private ExpressionContext expressionContext = null;
    
    public static final String PROPERTY_HYPERLINK = "hyperlink";

    private boolean init = false;
    
    
    /** Creates new form SectionItemHyperlink */
    public HyperlinkPanel() {
        initComponents();
        jPanelClose.setVisible(false);
        hyperlink = new JRDesignHyperlink();
        setInit(true);
        jComboBoxLinkType.addItem(new Tag( HyperlinkTypeEnum.NONE.getName()) );
        jComboBoxLinkType.addItem(new Tag( HyperlinkTypeEnum.REFERENCE.getName()) );
        jComboBoxLinkType.addItem(new Tag( HyperlinkTypeEnum.LOCAL_ANCHOR.getName()) );
        jComboBoxLinkType.addItem(new Tag( HyperlinkTypeEnum.LOCAL_PAGE.getName()) );
        jComboBoxLinkType.addItem(new Tag( HyperlinkTypeEnum.REMOTE_ANCHOR.getName()) );
        jComboBoxLinkType.addItem(new Tag( HyperlinkTypeEnum.REMOTE_PAGE.getName()) );

        // Adding extra link types...
        for (Tag t : IReportManager.getInstance().getCustomLinkTypes())
        {
            jComboBoxLinkType.addItem(t);
        }

        JRHyperlinkParametersTableCellRenderer cre = new JRHyperlinkParametersTableCellRenderer();
        jTableLinkParameters.getColumnModel().getColumn(0).setCellRenderer(cre);
        jTableLinkParameters.getColumnModel().getColumn(1).setCellRenderer(cre);
        
        // Barcode Evaluation Time...
        jComboBoxLinkTarget.addItem( new Tag( HyperlinkTargetEnum.SELF, "Self" ));
        jComboBoxLinkTarget.addItem(new Tag( HyperlinkTargetEnum.BLANK, "Blank" ));
        jComboBoxLinkTarget.addItem(new Tag( HyperlinkTargetEnum.TOP, "Top" ));
        jComboBoxLinkTarget.addItem(new Tag( HyperlinkTargetEnum.PARENT, "Parent" ));

        this.jRTextExpressionAreaAnchor.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorTextChanged();
            }
        });
        
        this.jRTextExpressionAreaPage.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaPageTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaPageTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaPageTextChanged();
            }
        });
        
        this.jRTextExpressionAreaReference.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaReferenceTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaReferenceTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaReferenceTextChanged();
            }
        });
        
        this.jRTextExpressionAreaTooltip.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTooltipTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTooltipTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTooltipTextChanged();
            }
        });
        
        this.jRTextExpressionAreaWhenExpression.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaWhenExpressionChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaWhenExpressionChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaWhenExpressionChanged();
            }
        });
        
        this.jRTextExpressionAreaAnchorName.getExpressionEditorPane().getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorNameChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorNameChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorNameChanged();
            }
        });
        
        javax.swing.DefaultListSelectionModel dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableLinkParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableLinkParametersListSelectionValueChanged(e);
            }
        });
        
        this.jPanelAnchorName.setVisible(false);
        jSpinnerBookmarkLevel.setModel( new javax.swing.SpinnerNumberModel(0,0,10000,1));
        //applyI18n();
         setInit(false);
    }

    private void fireHyperlinkChanged()
    {
        firePropertyChange(PROPERTY_HYPERLINK, null, getHyperlink());
    }

    public JRHyperlink getHyperlink() {
        if (hyperlink == null)
        {
            hyperlink = new JRDesignHyperlink();
        }
        return hyperlink;
    }
    
    
    public void setExpressionContext( ExpressionContext ec )
    {
        this.expressionContext = ec;
        jRTextExpressionAreaAnchorName.setExpressionContext(ec);
        jRTextExpressionAreaAnchor.setExpressionContext(ec);
        jRTextExpressionAreaReference.setExpressionContext(ec);
        jRTextExpressionAreaAnchor.setExpressionContext(ec);
        jRTextExpressionAreaPage.setExpressionContext(ec);
        jRTextExpressionAreaTooltip.setExpressionContext(ec);
        jRTextExpressionAreaWhenExpression.setExpressionContext(ec);

    }

    public void setHyperlink(JRHyperlink hlink)
    {
        try {
            setInit(true);
        
            try {
                hyperlink = hlink;
               
            } catch (Exception ex) { }
            
        // Fill the hyperlink panel...
            if (hyperlink != null)
            {
                if (JRHyperlinkHelper.getHyperlinkTypeValue(hyperlink.getLinkType()) != HyperlinkTypeEnum.CUSTOM)
                {
                    Misc.setComboboxSelectedTagValue(jComboBoxLinkType, hyperlink.getLinkType());
                }
                else
                {
                    jComboBoxLinkType.setSelectedItem(hyperlink.getLinkType());
                }

                if ( HyperlinkTargetEnum.getByValue(JRHyperlinkHelper.getHyperlinkTarget( hyperlink)) != HyperlinkTargetEnum.CUSTOM)
                {
                    Misc.setComboboxSelectedTagValue(jComboBoxLinkTarget, hyperlink.getLinkTarget());
                }
                else
                {
                    jComboBoxLinkTarget.setSelectedItem(hyperlink.getLinkTarget());
                }

                jRTextExpressionAreaReference.setText( Misc.getExpressionText( hyperlink.getHyperlinkReferenceExpression()) );
                jRTextExpressionAreaAnchor.setText( Misc.getExpressionText( hyperlink.getHyperlinkAnchorExpression()) );
                jRTextExpressionAreaPage.setText( Misc.getExpressionText( hyperlink.getHyperlinkPageExpression()) );
                jRTextExpressionAreaTooltip.setText( Misc.getExpressionText( hyperlink.getHyperlinkTooltipExpression()) );
                jRTextExpressionAreaWhenExpression.setText( Misc.getExpressionText( hyperlink.getHyperlinkWhenExpression()) );
            }
            else
            {
                hyperlink = getHyperlink();
            }
            
            if (hyperlink instanceof JRAnchor)
            {
                jPanelAnchorName.setVisible(true);
                jSpinnerBookmarkLevel.setValue(  ((JRAnchor)hyperlink).getBookmarkLevel() );
                jRTextExpressionAreaAnchorName.setText( Misc.getExpressionText( ((JRAnchor)hyperlink).getAnchorNameExpression()) );
            }
            else
            {
                jPanelAnchorName.setVisible(false);
            }
            
            jTabbedPane2.removeAll();
            
            String linkType = hyperlink.getLinkType();
            if (linkType == null) linkType = "None";
            
            //Adjusting hyperlink combinations...
            if (linkType.equals("None")) {
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setEnabled(false);
                this.jLabelReference.setEnabled(false);
             } else if (linkType.equals("Reference")) {
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setEnabled(true);
                this.jLabelReference.setEnabled(true);

                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Panel.Reference"),this.jPanelReference);
                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Link"), this.jPanelLinkParams);
            } else if (linkType.equals("LocalAnchor")) {
                this.jRTextExpressionAreaAnchor.setEnabled(true);
                this.jLabelAnchor.setEnabled(true);
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setEnabled(false);
                this.jLabelReference.setEnabled(false);

                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                
                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Anchor"), this.jPanelAnchor);
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.LinkParam"), this.jPanelLinkParams);
            } else if (linkType.equals("LocalPage")) {
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setEnabled(true);
                this.jLabelPage.setEnabled(true);
                this.jRTextExpressionAreaReference.setEnabled(false);
                this.jLabelReference.setEnabled(false);

                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Page"),this.jPanelPage);
                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.LinkParam"), this.jPanelLinkParams);
            }
            else if (linkType.equals("RemoteAnchor")) {
                this.jRTextExpressionAreaAnchor.setEnabled(true);
                this.jLabelAnchor.setEnabled(true);
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setEnabled(true);
                this.jLabelReference.setEnabled(true);

                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Panel.Reference"),this.jPanelReference);
                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Anchor"), this.jPanelAnchor);
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.LinkParam"), this.jPanelLinkParams);
            } else if (linkType.equals("RemotePage")) {
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setEnabled(true);
                this.jLabelPage.setEnabled(true);
                this.jRTextExpressionAreaReference.setEnabled(true);
                this.jLabelReference.setEnabled(true);

                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Panel.Reference"),this.jPanelReference);
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                
                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Page"),this.jPanelPage);
                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.LinkParam"), this.jPanelLinkParams);
            } else {
                this.jRTextExpressionAreaAnchor.setEnabled(true);
                this.jLabelAnchor.setEnabled(true);
                this.jRTextExpressionAreaPage.setEnabled(true);
                this.jLabelPage.setEnabled(true);
                this.jRTextExpressionAreaReference.setEnabled(true);
                this.jLabelReference.setEnabled(true);

                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Panel.Reference"),this.jPanelReference);
                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Anchor"), this.jPanelAnchor);
                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Page"),this.jPanelPage);
                jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.LinkParam"), this.jPanelLinkParams);
            }
            
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Tooltip"), this.jPanelTooltip);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.WhenExpression"), this.jPanelWhenExpression);
            
            // Adding parameters...  
            java.util.Iterator enum_parameters = getParametersList().iterator();
            
            javax.swing.table.DefaultTableModel dtmLinkParams = (javax.swing.table.DefaultTableModel)jTableLinkParameters.getModel();
            dtmLinkParams.setRowCount(0);
            
            while (enum_parameters.hasNext()) {
                JRDesignHyperlinkParameter parameter = (JRDesignHyperlinkParameter)enum_parameters.next();
                Vector row = new Vector();
                row.addElement(parameter);
                row.addElement(Misc.getExpressionText( parameter.getValueExpression()) );
                dtmLinkParams.addRow(row);
            }
        } finally {
            
            setInit(false);
        }
    }

    
    public void jTableLinkParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableLinkParameters.getSelectedRowCount() > 0) {
            this.jButtonModLinkParameter.setEnabled(true);
            this.jButtonRemLinkParameter.setEnabled(true);
        } else {
            this.jButtonModLinkParameter.setEnabled(false);
            this.jButtonRemLinkParameter.setEnabled(false);
        }
    }
    
    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelAnchorName = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jRTextExpressionAreaAnchorName = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSpinnerBookmarkLevel = new javax.swing.JSpinner();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelTarget = new javax.swing.JLabel();
        jComboBoxLinkTarget = new javax.swing.JComboBox();
        jLabel36 = new javax.swing.JLabel();
        jComboBoxLinkType = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanelReference = new javax.swing.JPanel();
        jLabelReference = new javax.swing.JLabel();
        jRTextExpressionAreaReference = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanelAnchor = new javax.swing.JPanel();
        jLabelAnchor = new javax.swing.JLabel();
        jRTextExpressionAreaAnchor = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanelPage = new javax.swing.JPanel();
        jLabelPage = new javax.swing.JLabel();
        jRTextExpressionAreaPage = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanelLinkParams = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableLinkParameters = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButtonAddLinkParameter = new javax.swing.JButton();
        jButtonModLinkParameter = new javax.swing.JButton();
        jButtonRemLinkParameter = new javax.swing.JButton();
        jPanelTooltip = new javax.swing.JPanel();
        jLabelReference1 = new javax.swing.JLabel();
        jRTextExpressionAreaTooltip = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanelWhenExpression = new javax.swing.JPanel();
        jLabelReference2 = new javax.swing.JLabel();
        jRTextExpressionAreaWhenExpression = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jPanelClose = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        setLayout(new java.awt.GridBagLayout());

        jPanelAnchorName.setMinimumSize(new java.awt.Dimension(272, 116));
        jPanelAnchorName.setPreferredSize(new java.awt.Dimension(426, 116));
        jPanelAnchorName.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Anchor Name Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        jPanelAnchorName.add(jLabel1, gridBagConstraints);

        jRTextExpressionAreaAnchorName.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        jRTextExpressionAreaAnchorName.setMinimumSize(new java.awt.Dimension(48, 50));
        jRTextExpressionAreaAnchorName.setPreferredSize(new java.awt.Dimension(133, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelAnchorName.add(jRTextExpressionAreaAnchorName, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Bookmark Level");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel4.add(jLabel2, gridBagConstraints);

        jSpinnerBookmarkLevel.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerBookmarkLevel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerBookmarkLevelStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(jSpinnerBookmarkLevel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelAnchorName.add(jPanel4, gridBagConstraints);

        jSeparator1.setMinimumSize(new java.awt.Dimension(2, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanelAnchorName.add(jSeparator1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanelAnchorName, gridBagConstraints);

        jLabelTarget.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelTarget.setText("Hyperlink target");
        jLabelTarget.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabelTarget.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabelTarget.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 5, 0);
        add(jLabelTarget, gridBagConstraints);

        jComboBoxLinkTarget.setEditable(true);
        jComboBoxLinkTarget.setMinimumSize(new java.awt.Dimension(180, 20));
        jComboBoxLinkTarget.setPreferredSize(new java.awt.Dimension(180, 20));
        jComboBoxLinkTarget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLinkTargetActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 2);
        add(jComboBoxLinkTarget, gridBagConstraints);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Hyperlink type");
        jLabel36.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabel36.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel36.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 5, 0);
        add(jLabel36, gridBagConstraints);

        jComboBoxLinkType.setEditable(true);
        jComboBoxLinkType.setMinimumSize(new java.awt.Dimension(180, 20));
        jComboBoxLinkType.setPreferredSize(new java.awt.Dimension(180, 20));
        jComboBoxLinkType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLinkTypeActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        add(jComboBoxLinkType, gridBagConstraints);

        jPanel1.setMinimumSize(new java.awt.Dimension(309, 150));
        jPanel1.setPreferredSize(new java.awt.Dimension(309, 150));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanelReference.setLayout(new java.awt.GridBagLayout());

        jLabelReference.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelReference.setText("Hyperlink Reference Expression");
        jLabelReference.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelReference.add(jLabelReference, gridBagConstraints);

        jRTextExpressionAreaReference.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaReference.setEnabled(false);
        jRTextExpressionAreaReference.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaReference.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelReference.add(jRTextExpressionAreaReference, gridBagConstraints);

        jTabbedPane2.addTab("Reference", jPanelReference);

        jPanelAnchor.setLayout(new java.awt.GridBagLayout());

        jLabelAnchor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelAnchor.setText("Hyperlink Anchor Expression");
        jLabelAnchor.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelAnchor.add(jLabelAnchor, gridBagConstraints);

        jRTextExpressionAreaAnchor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaAnchor.setEnabled(false);
        jRTextExpressionAreaAnchor.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaAnchor.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelAnchor.add(jRTextExpressionAreaAnchor, gridBagConstraints);

        jTabbedPane2.addTab("Anchor", jPanelAnchor);

        jPanelPage.setLayout(new java.awt.GridBagLayout());

        jLabelPage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelPage.setText("Hyperlink Page Expression");
        jLabelPage.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelPage.add(jLabelPage, gridBagConstraints);

        jRTextExpressionAreaPage.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaPage.setEnabled(false);
        jRTextExpressionAreaPage.setMinimumSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelPage.add(jRTextExpressionAreaPage, gridBagConstraints);

        jTabbedPane2.addTab("Page", jPanelPage);

        jPanelLinkParams.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 50));

        jTableLinkParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parameter name", "Expression"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableLinkParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableLinkParametersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableLinkParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelLinkParams.add(jScrollPane1, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jButtonAddLinkParameter.setText("Add");
        jButtonAddLinkParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddLinkParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 6, 0);
        jPanel3.add(jButtonAddLinkParameter, gridBagConstraints);

        jButtonModLinkParameter.setText("Modify");
        jButtonModLinkParameter.setEnabled(false);
        jButtonModLinkParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModLinkParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel3.add(jButtonModLinkParameter, gridBagConstraints);

        jButtonRemLinkParameter.setText("Remove");
        jButtonRemLinkParameter.setEnabled(false);
        jButtonRemLinkParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemLinkParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel3.add(jButtonRemLinkParameter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanelLinkParams.add(jPanel3, gridBagConstraints);

        jTabbedPane2.addTab("Link parameters", jPanelLinkParams);

        jPanelTooltip.setLayout(new java.awt.GridBagLayout());

        jLabelReference1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelReference1.setText("Tooltip Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTooltip.add(jLabelReference1, gridBagConstraints);

        jRTextExpressionAreaTooltip.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaTooltip.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaTooltip.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTooltip.add(jRTextExpressionAreaTooltip, gridBagConstraints);

        jTabbedPane2.addTab("Tooltip", jPanelTooltip);

        jPanelWhenExpression.setLayout(new java.awt.GridBagLayout());

        jLabelReference2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelReference2.setText("When Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelWhenExpression.add(jLabelReference2, gridBagConstraints);
        jLabelReference2.getAccessibleContext().setAccessibleName("When Expression");

        jRTextExpressionAreaWhenExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaWhenExpression.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaWhenExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelWhenExpression.add(jRTextExpressionAreaWhenExpression, gridBagConstraints);

        jTabbedPane2.addTab("When Exp.", jPanelWhenExpression);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jTabbedPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jPanel1, gridBagConstraints);

        jPanelClose.setLayout(new java.awt.GridBagLayout());

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 8);
        jPanelClose.add(jButtonClose, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanelClose, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown

        
        
        
    }//GEN-LAST:event_formComponentShown

    private void jTableLinkParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableLinkParametersMouseClicked

        
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1 && jTableLinkParameters.getSelectedRowCount() > 0)
        {
            jButtonModLinkParameterActionPerformed(null);
        }
        
    }//GEN-LAST:event_jTableLinkParametersMouseClicked

    private void jButtonRemLinkParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemLinkParameterActionPerformed
        if (this.isInit()) return;
        
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableLinkParameters.getModel();
        int[]  rows= jTableLinkParameters.getSelectedRows();
        for (int i=rows.length-1; i>=0; --i) {
            getParametersList().remove( jTableLinkParameters.getValueAt( rows[i], 0) );
            dtm.removeRow(rows[i]);
        }
        IReportManager.getInstance().notifyReportChange();
    }//GEN-LAST:event_jButtonRemLinkParameterActionPerformed

    private void jButtonModLinkParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModLinkParameterActionPerformed
        if (this.isInit()) return;
        
        if (jTableLinkParameters.getSelectedRowCount() <= 0) return;
        
        JRDesignHyperlinkParameter parameter = (JRDesignHyperlinkParameter)jTableLinkParameters.getValueAt( jTableLinkParameters.getSelectedRow(), 0);
        
        JRLinkParameterDialog jrpd = new JRLinkParameterDialog((javax.swing.JDialog)SwingUtilities.getWindowAncestor(this), true);
        jrpd.setExpressionContext( this.getExpressionContext());
        jrpd.setParameter( parameter );
        if (evt != null && evt.getID() > 0)
        {
            jrpd.setFocusedExpression(evt.getID());
        }
        
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            parameter.setName( jrpd.getParameter().getName() );
            parameter.setValueExpression( jrpd.getParameter().getValueExpression());
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableLinkParameters.getModel();
            
            dtm.setValueAt(parameter,jTableLinkParameters.getSelectedRow(),0);
            dtm.setValueAt(Misc.getExpressionText( parameter.getValueExpression()),jTableLinkParameters.getSelectedRow(),1);
            
            jTableLinkParameters.updateUI();
            IReportManager.getInstance().notifyReportChange();
        }
        fireHyperlinkChanged();
    }//GEN-LAST:event_jButtonModLinkParameterActionPerformed

    private void jButtonAddLinkParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddLinkParameterActionPerformed
        if (this.isInit()) return;
        
        JRLinkParameterDialog jrpd = new JRLinkParameterDialog((javax.swing.JDialog)SwingUtilities.getWindowAncestor(this), true);
        jrpd.setExpressionContext(getExpressionContext());
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            JRDesignHyperlinkParameter parameter = jrpd.getParameter();
            
            getParametersList().add( parameter );
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableLinkParameters.getModel();
            dtm.addRow(new Object[]{parameter, Misc.getExpressionText( parameter.getValueExpression())});
            IReportManager.getInstance().notifyReportChange();
        }
        fireHyperlinkChanged();
    }//GEN-LAST:event_jButtonAddLinkParameterActionPerformed

    private void jComboBoxLinkTypeActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLinkTypeActionPerformed1
        if (this.isInit()) return;
        
        jTabbedPane2.removeAll();
        Object val = this.jComboBoxLinkType.getSelectedItem();
        String linkType = "None";
        if (val != null)
        {
            if (val instanceof Tag)
            {
                val = ((Tag)val).getValue();
                if (val instanceof JREnum)
                {
                    val =((JREnum)val).getName();
                }
                else
                {
                    val = ""+ val;
                }
            }
            
            linkType = "" + val;
        }
        
        if (linkType.equals("None")) {
            // Set to blank all link fields...
            setHyperlinkAttribute("LinkType", String.class, linkType);
            setHyperlinkAttribute("HyperlinkReferenceExpression", JRExpression.class, null);
            setHyperlinkAttribute("HyperlinkAnchorExpression", JRExpression.class, null);
            setHyperlinkAttribute("HyperlinkPageExpression", JRExpression.class, null);
            getParametersList().clear();
            this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(false);
            this.jLabelAnchor.setEnabled(false);
            this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(false);
            this.jLabelPage.setEnabled(false);
            this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(false);
            this.jLabelReference.setEnabled(false);
            //this.jPanelAnchor.setVisible(false);
            //this.jPanelPage.setVisible(false);
            //this.jPanelReference.setVisible(false);
            //jTabbedPane2.addTab("Link parameters", this.jPanelLinkParams);
        } else if (linkType.equals("Reference")) {
            // Set to blank all link fields...
            setHyperlinkAttribute("LinkType", String.class, linkType);
            setHyperlinkAttribute("HyperlinkReferenceExpression", JRExpression.class, null);
            setHyperlinkAttribute("HyperlinkAnchorExpression", JRExpression.class, null);
            setHyperlinkAttribute("HyperlinkPageExpression", JRExpression.class, null);
            this.jRTextExpressionAreaAnchor.setText(null);
            this.jRTextExpressionAreaAnchor.setEnabled(false);
            this.jLabelAnchor.setEnabled(false);
            this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(false);
            this.jLabelPage.setEnabled(false);
            this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(true);
            this.jLabelReference.setEnabled(true);
            
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Panel.Reference"), this.jPanelReference);
            //this.jPanelAnchor.setVisible(false);
            //this.jPanelPage.setVisible(false);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.LinkParam"), this.jPanelLinkParams);
            
            
        } else if (linkType.equals("LocalAnchor")) {
            // Set to blank all link fields...
            setHyperlinkAttribute("LinkType", String.class, linkType);
            setHyperlinkAttribute("HyperlinkReferenceExpression", JRExpression.class, null);
            setHyperlinkAttribute("HyperlinkAnchorExpression", JRExpression.class, null);
            setHyperlinkAttribute("HyperlinkPageExpression", JRExpression.class, null);
            this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(true);
            this.jLabelAnchor.setEnabled(true);
            this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(false);
            this.jLabelPage.setEnabled(false);
            this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(false);
            this.jLabelReference.setEnabled(false);
            
            //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Anchor"), this.jPanelAnchor);
            //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.LinkParam"), this.jPanelLinkParams);
            
        } else if (linkType.equals("LocalPage")) {
            // Set to blank all link fields...
            setHyperlinkAttribute("LinkType", String.class, linkType);
            setHyperlinkAttribute("HyperlinkReferenceExpression", JRExpression.class, null);
            setHyperlinkAttribute("HyperlinkAnchorExpression", JRExpression.class, null);
            setHyperlinkAttribute("HyperlinkPageExpression", JRExpression.class, null);
            this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(false);
            this.jLabelAnchor.setEnabled(false);
            this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(true);
            this.jLabelPage.setEnabled(true);
            this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(false);
            this.jLabelReference.setEnabled(false);
            
            //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
            //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Page"),this.jPanelPage);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.LinkParam"), this.jPanelLinkParams);
        }
        
        else if (linkType.equals("RemoteAnchor")) {
            // Set to blank all link fields...
            setHyperlinkAttribute("LinkType", String.class, linkType);
            setHyperlinkAttribute("HyperlinkReferenceExpression", JRExpression.class, null);
            setHyperlinkAttribute("HyperlinkAnchorExpression", JRExpression.class, null);
            setHyperlinkAttribute("HyperlinkPageExpression", JRExpression.class, null);
            this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(true);
            this.jLabelAnchor.setEnabled(true);
            this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(false);
            this.jLabelPage.setEnabled(false);
            this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(true);
            this.jLabelReference.setEnabled(true);
            
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Panel.Reference"),this.jPanelReference);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Anchor"), this.jPanelAnchor);
            //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.LinkParam"), this.jPanelLinkParams);
        } else if (linkType.equals("RemotePage")) {
            // Set to blank all link fields...
            
            setHyperlinkAttribute("LinkType", String.class, linkType);
            setHyperlinkAttribute("HyperlinkReferenceExpression", JRExpression.class, null);
            setHyperlinkAttribute("HyperlinkAnchorExpression", JRExpression.class, null);
            setHyperlinkAttribute("HyperlinkPageExpression", JRExpression.class, null);
            this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(false);
            this.jLabelAnchor.setEnabled(false);
            this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(true);
            this.jLabelPage.setEnabled(true);
            this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(true);
            this.jLabelReference.setEnabled(true);
            
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Panel.Reference"),this.jPanelReference);
            //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Page"),this.jPanelPage);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.LinkParam"), this.jPanelLinkParams);
        } else {
            // Set to blank all link fields...
            
            setHyperlinkAttribute("LinkType", String.class, linkType);
            //element.setHyperlinkReferenceExpression("");
            //element.setHyperlinkAnchorExpression("");
            //element.setHyperlinkPageExpression("");
            //this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(true);
            this.jLabelAnchor.setEnabled(true);
            //this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(true);
            this.jLabelPage.setEnabled(true);
            //this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(true);
            this.jLabelReference.setEnabled(true);
            
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Panel.Reference"),this.jPanelReference);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Anchor"), this.jPanelAnchor);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Page"),this.jPanelPage);
            jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.LinkParam"),this.jPanelLinkParams);
        }
        
        jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.Tooltip"), this.jPanelTooltip);
        jTabbedPane2.addTab(I18n.getString("HyperlinkPanel.Pane.WhenExpression"), this.jPanelWhenExpression);
        
        fireHyperlinkChanged();
    }//GEN-LAST:event_jComboBoxLinkTypeActionPerformed1

    private void jComboBoxLinkTargetActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLinkTargetActionPerformed1
        if (this.isInit()) return;



        Object target = jComboBoxLinkTarget.getSelectedItem();
        
        if (target != null && target instanceof Tag)
        {
            setHyperlinkAttribute("LinkTarget",String.class, ((HyperlinkTargetEnum) ((Tag)target).getValue() ).getName() );
        }
        else if (target != null)
        {
            //setHyperlinkAttribute("HyperlinkTarget", Byte.TYPE, JRHyperlink.HYPERLINK_TARGET_CUSTOM);
            setHyperlinkAttribute("LinkTarget", String.class, "" + target);
        }
        else
        {
            setHyperlinkAttribute("LinkTarget", String.class,  HyperlinkTargetEnum.SELF.getName());
        }
        
        fireHyperlinkChanged();
    }//GEN-LAST:event_jComboBoxLinkTargetActionPerformed1

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        if (dialog != null && dialog.isVisible()) {
            dialog.setVisible(false);
            dialog.dispose();
        }
}//GEN-LAST:event_jButtonCloseActionPerformed

    private void jSpinnerBookmarkLevelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerBookmarkLevelStateChanged
        
        
        if (this.isInit()) return;
        
        int level = ((Integer)jSpinnerBookmarkLevel.getValue()).intValue();
        Object val = new Integer(level);
        
        setHyperlinkAttribute( "BookmarkLevel" , Integer.TYPE, val);
        fireHyperlinkChanged();
                
    }//GEN-LAST:event_jSpinnerBookmarkLevelStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddLinkParameter;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonModLinkParameter;
    private javax.swing.JButton jButtonRemLinkParameter;
    private javax.swing.JComboBox jComboBoxLinkTarget;
    private javax.swing.JComboBox jComboBoxLinkType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabelAnchor;
    private javax.swing.JLabel jLabelPage;
    private javax.swing.JLabel jLabelReference;
    private javax.swing.JLabel jLabelReference1;
    private javax.swing.JLabel jLabelReference2;
    private javax.swing.JLabel jLabelTarget;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelAnchor;
    private javax.swing.JPanel jPanelAnchorName;
    private javax.swing.JPanel jPanelClose;
    private javax.swing.JPanel jPanelLinkParams;
    private javax.swing.JPanel jPanelPage;
    private javax.swing.JPanel jPanelReference;
    private javax.swing.JPanel jPanelTooltip;
    private javax.swing.JPanel jPanelWhenExpression;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaAnchor;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaAnchorName;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaPage;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaReference;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaTooltip;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaWhenExpression;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner jSpinnerBookmarkLevel;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTableLinkParameters;
    // End of variables declaration//GEN-END:variables
    
    
     public void jRTextExpressionAreaTooltipTextChanged() {
        if (this.isInit()) return;
        setHyperlinkAttribute("HyperlinkTooltipExpression", JRExpression.class, Misc.createExpression("java.lang.String", jRTextExpressionAreaTooltip.getText()) );
        fireHyperlinkChanged();
    }
    
     public void jRTextExpressionAreaAnchorNameChanged() {
        if (this.isInit()) return;
        setHyperlinkAttribute("AnchorNameExpression", JRExpression.class, Misc.createExpression("java.lang.String", jRTextExpressionAreaAnchorName.getText()) );
        fireHyperlinkChanged();
    }
     
    public void jRTextExpressionAreaReferenceTextChanged() {
        if (this.isInit()) return;
        setHyperlinkAttribute("HyperlinkReferenceExpression", JRExpression.class, Misc.createExpression("java.lang.String", jRTextExpressionAreaReference.getText()) );
        fireHyperlinkChanged();
    }
    
    public void jRTextExpressionAreaAnchorTextChanged() {
        if (this.isInit()) return;
        setHyperlinkAttribute("HyperlinkAnchorExpression", JRExpression.class, Misc.createExpression("java.lang.String", jRTextExpressionAreaAnchor.getText()) );
        fireHyperlinkChanged();
    }
    
    public void jRTextExpressionAreaPageTextChanged() {
        if (this.isInit()) return;
            setHyperlinkAttribute("HyperlinkPageExpression", JRExpression.class, Misc.createExpression("java.lang.String", jRTextExpressionAreaPage.getText()) );
            fireHyperlinkChanged();
    }
    
    public void jRTextExpressionAreaWhenExpressionChanged() {
        if (this.isInit()) return;
        setHyperlinkAttribute("HyperlinkWhenExpression", JRExpression.class, Misc.createExpression("java.lang.String", jRTextExpressionAreaWhenExpression.getText()) );
        fireHyperlinkChanged();
    }
    
    /*
    public void applyI18n(){
            
            // Start autogenerated code ----------------------
            jButtonAddLinkParameter.setText(I18n.getString("sectionItemHyperlinkPanel.buttonAddLinkParameter","Add"));
            jButtonModLinkParameter.setText(I18n.getString("sectionItemHyperlinkPanel.buttonModLinkParameter","Modify"));
            jButtonRemLinkParameter.setText(I18n.getString("sectionItemHyperlinkPanel.buttonRemLinkParameter","Remove"));
            jLabel36.setText(I18n.getString("sectionItemHyperlinkPanel.label36","Hyperlink type"));
            jLabelAnchor.setText(I18n.getString("sectionItemHyperlinkPanel.labelAnchor","Hyperlink Anchor Expression"));
            jLabelPage.setText(I18n.getString("sectionItemHyperlinkPanel.labelPage","Hyperlink Page Expression"));
            jLabelReference.setText(I18n.getString("sectionItemHyperlinkPanel.labelReference","Hyperlink Reference Expression"));
            jLabelReference1.setText(I18n.getString("sectionItemHyperlinkPanel.labelReference1","Tooltip Expression"));
            jLabelTarget.setText(I18n.getString("sectionItemHyperlinkPanel.labelTarget","Hyperlink target"));
            // End autogenerated code ----------------------

            jTableLinkParameters.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("sectionItemHyperlinkPanel.tablecolumn.parameterName","Parameter name") );
            jTableLinkParameters.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("sectionItemHyperlinkPanel.tablecolumn.expression","Expression") );

    }
     */
    
    public static final int COMPONENT_NONE=0;
    //public static final int COMPONENT_ANCHORNAME_EXPRESSION=1;
    public static final int COMPONENT_HYPERLINK_REFERENCE_EXPRESSION=2;
    public static final int COMPONENT_HYPERLINK_ANCHOR_EXPRESSION=3;
    public static final int COMPONENT_HYPERLINK_PAGE_EXPRESSION=4;
    public static final int COMPONENT_HYPERLINK_TOOLTIP_EXPRESSION=5;
    public static final int COMPONENT_HYPERLINK_PARAMETERS=6;
        
    
    public int openParameterWithThisExpression = -1;
    /**
     * This method set the focus on a specific component.
     * 
     * For this kind of datasource otherInfo must be in the format suitable for sectionItemHyperlinkPanel1
     * expressionInfo[0] must be an Integer holding the expression ID<br>
     * if (expressionInfo[0] == COMPONENT_HYPERLINK_PARAMETERS) then expressionInfo[1] must contain an Integer
     * with the index of the parameter, and expressionInfo[2] the index of the expression to highlight for
     * that parameter. The window will open as soon this component will become "shown". 
     */
    public void setFocusedExpression(Object[] expressionInfo)
    {
        int expID = ((Integer)expressionInfo[0]).intValue();
        
        try {
            switch (expID)
            {
                case COMPONENT_HYPERLINK_REFERENCE_EXPRESSION:
                    this.jTabbedPane2.setSelectedComponent(  jPanelReference );
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaReference);
                    break;  
                case COMPONENT_HYPERLINK_ANCHOR_EXPRESSION:
                    this.jTabbedPane2.setSelectedComponent(  jPanelAnchor );
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaAnchor);
                    break;  
                case COMPONENT_HYPERLINK_PAGE_EXPRESSION:
                    this.jTabbedPane2.setSelectedComponent(  jPanelPage );
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaPage);
                    break;  
                case COMPONENT_HYPERLINK_TOOLTIP_EXPRESSION:
                    this.jTabbedPane2.setSelectedComponent(  jPanelTooltip );
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaTooltip);
                    break;
                case COMPONENT_HYPERLINK_PARAMETERS:
                    this.jTabbedPane2.setSelectedComponent(  jPanelLinkParams );
                    int paramIdex = ((Integer)expressionInfo[1]).intValue();
                    int paramExpId = ((Integer)expressionInfo[2]).intValue();
                    if (paramIdex >= 0 && jTableLinkParameters.getRowCount() > paramIdex)
                    {
                        jTableLinkParameters.setRowSelectionInterval(paramIdex,paramIdex);
                        openParameterWithThisExpression = paramExpId;
                    }
                    break;
            }
        } catch (Exception ex) { }
    }    
    
    public void openExtraWindows()
    {
        if (openParameterWithThisExpression >= 0)
        {
            jButtonModLinkParameterActionPerformed(new ActionEvent(jButtonModLinkParameter,openParameterWithThisExpression,""));
        }
        openParameterWithThisExpression = -1;
    }

    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }
    
    private JDialog dialog = null;

    
    /**
     * Show the dialog to edit the expression.
     * If parent is not null, it is used to find a parent Window.
     */
    public void showDialog(Component parent)
    {

        jPanelClose.setVisible(true);
        
        this.dialog = new JDialog(Misc.getMainFrame(), true);
        
        dialog.getContentPane().add(this);
        dialog.pack();
        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        dialog.setTitle(I18n.getString("HyperlinkPanel.Dialog.Title"));
        dialog.setVisible(true);
    }
 
    /**
     * We assume the  JRHyperlink has always a way to get the parameters a list...
     * @param hl
     * @return the list of parameters
     */
    private List getParametersList()
    {
        if (hyperlink == null) return null;
        try {
            Method m = hyperlink.getClass().getMethod("getHyperlinkParametersList");
            return (List)m.invoke(hyperlink);
        }
        catch (Throwable t) {
            t.printStackTrace(); 
        }
        return null;
    }
    
    /**
     * We assume the  JRHyperlink has always a way to get the parameters a list...
     * @param hl
     * @return the list of parameters
     */
    private void setHyperlinkAttribute(String attribute, Class clazz, Object value)
    {
        if (hyperlink == null) return;
        try {
            Method m = hyperlink.getClass().getMethod("set" + attribute, clazz);
            m.invoke(hyperlink, value);
            IReportManager.getInstance().notifyReportChange();
        }
        catch (Throwable t) { 
            t.printStackTrace();
        }
    }    
    
    public void setAnchorNameVisible(boolean b)
    {
        this.jPanelAnchorName.setVisible(b);
    }
}
