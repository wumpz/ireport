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
package com.jaspersoft.ireport.designer.sheet.editors.box;


import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.sheet.editors.box.BoxBorderSelectionPanel.Side;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.event.ActionEvent;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.type.LineStyleEnum;
/**
 *
 * @author  Administrator
 */
public class BoxPanel extends javax.swing.JPanel implements ActionListener, BorderSelectionListener {
    
    private boolean init = false;
    
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    
    private SpinnerNumberModel spinnedModel = null;
    private DefaultListModel styleListModel = null;
    
    private JRLineBox lineBox = null;
    
    
    public BoxBorderSelectionPanel getSelectionPanel() {
        return selectionPanel;
    }

    public void setSelectionPanel(BoxBorderSelectionPanel selectionPanel) {
        this.selectionPanel = selectionPanel;
    }

    public JRLineBox getLineBox() {
        return lineBox;
    }

    public void setLineBox(JRLineBox lineBox) {
        
        boolean oldInit = isInit();
        setInit(true);
        this.lineBox = lineBox;
        getSelectionPanel().clearSelection();
        getSelectionPanel().setLineBox(lineBox);
        getSelectionPanel().repaint();
        
        if (lineBox != null)
        {
            jSpinnerBottom.setValue( lineBox.getBottomPadding() );
            jSpinnerTop.setValue( lineBox.getTopPadding() );
            jSpinnerLeft.setValue( lineBox.getLeftPadding() );
            jSpinnerRight.setValue(lineBox.getRightPadding() );
        }
        else
        {
            jSpinnerBottom.setValue( 0);
            jSpinnerTop.setValue( 0);
            jSpinnerLeft.setValue( 0);
            jSpinnerRight.setValue( 0);
        }
        setInit(oldInit);
        selectionChanged(getSelectionPanel().getSelectedBorders());
    }
    
    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
    
    /** Creates new form BorderPanel */
    public BoxPanel() {
        initComponents();
             
        jPanelButtons.setVisible(false);
        colorSelector.setColor(null);
        colorSelector.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
                colorChanged();
            }
        });
        
        spinnedModel = new SpinnerNumberModel(0, 0, 100, 0.25); 
        jSpinnerLineWidth.setModel(spinnedModel);
        spinnedModel.addChangeListener(new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
                widthChanged();
            }
        });
        
        jSpinnerLineWidth.setFont( UIManager.getFont("TextField.font"));
        
        jList1.setCellRenderer(new LineStyleListCellRenderer());
        styleListModel = new DefaultListModel();
        jList1.setModel(styleListModel);

        //styleListModel.addElement("");
        styleListModel.addElement( LineStyleEnum.SOLID);
        styleListModel.addElement( LineStyleEnum.DASHED);
        styleListModel.addElement( LineStyleEnum.DOTTED);
        styleListModel.addElement( LineStyleEnum.DOUBLE);
        
        jList1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                lineStyleChanged();
            }
        });
        
        //applyI18n();
        
        
        init = true;
        
        SpinnerNumberModel snmTop = new SpinnerNumberModel(0,0,10000,1);
        jSpinnerTop.setModel(snmTop);
        snmTop.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSpinnerTopStateChanged(evt);
            }
        });

	SpinnerNumberModel snmBottom = new SpinnerNumberModel(0,0,10000,1);
        jSpinnerBottom.setModel(snmBottom);
        snmBottom.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSpinnerBottomStateChanged(evt);
            }
        });
        
        SpinnerNumberModel snmRight = new SpinnerNumberModel(0,0,10000,1);
        jSpinnerRight.setModel(snmRight);
        snmRight.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSpinnerRightStateChanged(evt);
            }
        });
        
        SpinnerNumberModel snmLeft = new SpinnerNumberModel(0,0,10000,1);
        jSpinnerLeft.setModel(snmLeft);
        snmLeft.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSpinnerLeftStateChanged(evt);
            }
        });
        

        init = false;

        selectionPanel.addBorderSelectionListener(this);
    }

    
     
    private void jSpinnerTopStateChanged(ChangeEvent evt)
    {
        getLineBox().setTopPadding( Integer.parseInt(jSpinnerTop.getValue()+""));
    }
    
    private void jSpinnerLeftStateChanged(ChangeEvent evt)
    {
        getLineBox().setLeftPadding( Integer.parseInt(jSpinnerLeft.getValue()+""));
    }
    
    private void jSpinnerRightStateChanged(ChangeEvent evt)
    {
        getLineBox().setRightPadding( Integer.parseInt(jSpinnerRight.getValue()+""));
    }
 
       
    private void jSpinnerBottomStateChanged(ChangeEvent evt)
    {
        getLineBox().setBottomPadding( Integer.parseInt(jSpinnerBottom.getValue()+""));
    }
 
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSpinnerLeft = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerTop = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jSpinnerRight = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jSpinnerBottom = new javax.swing.JSpinner();
        jPanelBorderEditorContainer = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanelPreview = new javax.swing.JPanel();
        selectionPanel = new com.jaspersoft.ireport.designer.sheet.editors.box.BoxBorderSelectionPanel();
        jButtonRestoreDefaults = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSpinnerLineWidth = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jPanelColorSelector = new javax.swing.JPanel();
        colorSelector = new com.jaspersoft.ireport.designer.sheet.editors.box.ColorSelectorPanel();
        jPanelButtons = new javax.swing.JPanel();
        jButtonCancel = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();
        jButtonReset = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(309, 80));
        setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Padding"));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText(I18n.getString("BoxPanel.Label.Left")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel2.add(jLabel3, gridBagConstraints);

        jSpinnerLeft.setMinimumSize(new java.awt.Dimension(50, 20));
        jSpinnerLeft.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerLeft.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSpinnerLeftPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jSpinnerLeft, gridBagConstraints);

        jLabel4.setText(I18n.getString("BoxPanel.Label.Top")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel2.add(jLabel4, gridBagConstraints);

        jSpinnerTop.setMinimumSize(new java.awt.Dimension(50, 20));
        jSpinnerTop.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jSpinnerTop, gridBagConstraints);

        jLabel5.setText(I18n.getString("BoxPanel.Label.Right")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel2.add(jLabel5, gridBagConstraints);

        jSpinnerRight.setMinimumSize(new java.awt.Dimension(50, 20));
        jSpinnerRight.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jSpinnerRight, gridBagConstraints);

        jLabel6.setText(I18n.getString("BoxPanel.Label.Bottom")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel2.add(jLabel6, gridBagConstraints);

        jSpinnerBottom.setMinimumSize(new java.awt.Dimension(50, 20));
        jSpinnerBottom.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jSpinnerBottom, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jPanel2, gridBagConstraints);

        jPanelBorderEditorContainer.setBorder(javax.swing.BorderFactory.createTitledBorder("Borders"));
        jPanelBorderEditorContainer.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanelPreview.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanelPreview.setMinimumSize(new java.awt.Dimension(120, 80));
        jPanelPreview.setPreferredSize(new java.awt.Dimension(120, 80));
        jPanelPreview.setLayout(new java.awt.BorderLayout());
        jPanelPreview.add(selectionPanel, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        jPanel3.add(jPanelPreview, gridBagConstraints);

        jButtonRestoreDefaults.setText(I18n.getString("BoxPanel.Button.RestoreDefaults")); // NOI18N
        jButtonRestoreDefaults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRestoreDefaultsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel3.add(jButtonRestoreDefaults, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        jPanel1.add(jPanel3, gridBagConstraints);

        jPanel4.setMinimumSize(new java.awt.Dimension(200, 150));
        jPanel4.setPreferredSize(new java.awt.Dimension(200, 150));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(I18n.getString("BoxPanel.Label.LineWidth")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        jPanel4.add(jLabel1, gridBagConstraints);

        jSpinnerLineWidth.setMinimumSize(new java.awt.Dimension(120, 20));
        jSpinnerLineWidth.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 8);
        jPanel4.add(jSpinnerLineWidth, gridBagConstraints);

        jLabel7.setText(I18n.getString("BoxPanel.Label.LineStyle")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        jPanel4.add(jLabel7, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(120, 80));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(120, 80));

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 8);
        jPanel4.add(jScrollPane1, gridBagConstraints);

        jLabel2.setText(I18n.getString("BoxPanel.Label.LineColor")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        jPanel4.add(jLabel2, gridBagConstraints);

        jPanelColorSelector.setBackground(new java.awt.Color(255, 255, 255));
        jPanelColorSelector.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.controlShadow));
        jPanelColorSelector.setMinimumSize(new java.awt.Dimension(50, 20));
        jPanelColorSelector.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanelColorSelector.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanelColorSelector.add(colorSelector, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 8);
        jPanel4.add(jPanelColorSelector, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel4, gridBagConstraints);

        jPanelBorderEditorContainer.add(jPanel1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 6, 6);
        add(jPanelBorderEditorContainer, gridBagConstraints);

        jButtonCancel.setText(I18n.getString("Global.Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonOk.setText(I18n.getString("Global.Button.Ok")); // NOI18N
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        jButtonReset.setText(I18n.getString("Global.Button.Reset")); // NOI18N
        jButtonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelButtonsLayout = new org.jdesktop.layout.GroupLayout(jPanelButtons);
        jPanelButtons.setLayout(jPanelButtonsLayout);
        jPanelButtonsLayout.setHorizontalGroup(
            jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelButtonsLayout.createSequentialGroup()
                .addContainerGap(137, Short.MAX_VALUE)
                .add(jButtonOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonCancel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonReset, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(9, 9, 9))
        );
        jPanelButtonsLayout.setVerticalGroup(
            jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelButtonsLayout.createSequentialGroup()
                .add(jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonReset)
                    .add(jButtonCancel)
                    .add(jButtonOk))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(jPanelButtons, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinnerLeftPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSpinnerLeftPropertyChange
         
    }//GEN-LAST:event_jSpinnerLeftPropertyChange

    private void jButtonRestoreDefaultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRestoreDefaultsActionPerformed
        if (lineBox == null) return;
        
        List<Side> borders =  new ArrayList<Side>();
        borders.addAll(selectionPanel.getSelectedBorders());
        
        if (borders.size() == 0) {
            resetPen( getLineBox().getPen() );
            borders.add( Side.TOP);
            borders.add(Side.BOTTOM);
            borders.add(Side.LEFT);
            borders.add(Side.RIGHT);
        }
        for (BoxBorderSelectionPanel.Side s : borders) {
            switch (s) {
                case TOP:
                {
                    resetPen( getLineBox().getTopPen() );
                    break;
                }
                case LEFT:
                {
                    resetPen( getLineBox().getLeftPen() );
                    break;
                }
                case BOTTOM:
                {
                    resetPen( getLineBox().getBottomPen() );
                    break;
                }
                case RIGHT:
                {
                    resetPen( getLineBox().getRightPen() );
                    break;
                }
            }
        }
        
        selectionChanged(borders);
        getSelectionPanel().repaint();
        //fireActionPerformed();
}//GEN-LAST:event_jButtonRestoreDefaultsActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
       
        if (dialog != null)
        {
            setDialogResult(JOptionPane.OK_OPTION);
            dialog.setVisible(false);
            dialog.dispose();
        }
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        if (dialog != null)
        {
            setDialogResult(JOptionPane.CANCEL_OPTION);
            dialog.setVisible(false);
            dialog.dispose();
        }
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetActionPerformed
        
        setLineBox(new JRBaseLineBox(null));
}//GEN-LAST:event_jButtonResetActionPerformed

    
    public void selectionChanged(List<Side> selectedBorders) {
              if (isInit() || lineBox == null) return;  
              
              setInit(true);
              
              boolean sameWidth = true;
              boolean sameStyle = true;
              boolean sameColor = true;
              
              boolean first = true;
              
              // Reset all...
              spinnedModel.setValue(0.0);
              colorSelector.setColor(null);  
              jList1.clearSelection();
              
              if (selectedBorders.size() == 0)
              {
                  
                  JRPen pen = getLineBox().getPen();
                  spinnedModel.setValue(pen.getLineWidth().doubleValue());
                  colorSelector.setColor( pen.getLineColor() ); 
                  if (pen.getLineStyleValue() != null)
                  {
                    jList1.setSelectedValue( pen.getLineStyleValue(), true);
                  }
                 
              }
              
              for (BoxBorderSelectionPanel.Side s : selectedBorders)
              {
                    JRPen pen = null;
                    if (getLineBox() != null)
                    {
                        switch (s)
                        {
                            case TOP: pen =  getLineBox().getTopPen(); break;
                            case BOTTOM: pen =  getLineBox().getBottomPen(); break;
                            case RIGHT: pen =  getLineBox().getRightPen(); break;
                            case LEFT: pen =  getLineBox().getLeftPen(); break;
                        }
                    }
                    
                    if (pen != null)
                    {
                        if (first)
                        {
                            spinnedModel.setValue(pen.getLineWidth().doubleValue());
                            colorSelector.setColor( pen.getLineColor() ); 
                            if (pen.getLineStyleValue() != null)
                            {
                                jList1.setSelectedValue( pen.getLineStyleValue(), true);
                            }
                        }
                        else 
                        {
                            if (sameWidth)
                            {
                                if (pen.getLineWidth() != spinnedModel.getNumber().floatValue())
                                {
                                    sameWidth = false;
                                    // Keep the first value...
                                }
                            }
                            
                            if (sameColor)
                            {
                                
                                if (pen.getLineColor() == null ||
                                    !pen.getLineColor().equals( colorSelector.getColor() ))
                                {
                                    sameColor = false;
                                    colorSelector.setColor( null );
                                }
                            }
                            
                            if (sameStyle)
                            {
                                LineStyleEnum b = (LineStyleEnum)jList1.getSelectedValue();
                                if (b != pen.getLineStyleValue() )
                                {
                                    sameStyle = false;
                                    jList1.clearSelection();
                                }
                            }
                            
                        }
                        first = false;
                    }
              
                    
              }
              
              setInit(false);
              
    }
    
    /**
     * Registers ActionListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addActionListener(java.awt.event.ActionListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (java.awt.event.ActionListener.class, listener);
    }

    /**
     * Removes ActionListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeActionListener(java.awt.event.ActionListener listener) {

        listenerList.remove (java.awt.event.ActionListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    private void fireActionListenerActionPerformed(java.awt.event.ActionEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==java.awt.event.ActionListener.class) {
                ((java.awt.event.ActionListener)listeners[i+1]).actionPerformed (event);
            }
        }
        
    }

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jaspersoft.ireport.designer.sheet.editors.box.ColorSelectorPanel colorSelector;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonReset;
    private javax.swing.JButton jButtonRestoreDefaults;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelBorderEditorContainer;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelColorSelector;
    private javax.swing.JPanel jPanelPreview;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerBottom;
    private javax.swing.JSpinner jSpinnerLeft;
    private javax.swing.JSpinner jSpinnerLineWidth;
    private javax.swing.JSpinner jSpinnerRight;
    private javax.swing.JSpinner jSpinnerTop;
    private com.jaspersoft.ireport.designer.sheet.editors.box.BoxBorderSelectionPanel selectionPanel;
    // End of variables declaration//GEN-END:variables
    
    /*
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jLabel3.setText(I18n.getString("boxPanel.label3","Left"));
                jLabel4.setText(I18n.getString("boxPanel.label4","Top"));
                jLabel5.setText(I18n.getString("boxPanel.label5","Right"));
                jLabel6.setText(I18n.getString("boxPanel.label6","Bottom"));
                // End autogenerated code ----------------------
                
                ((javax.swing.border.TitledBorder)jPanel1.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("boxPanel.panelBorder.Border","Border") );
                ((javax.swing.border.TitledBorder)jPanel2.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("boxPanel.panelBorder.Padding","Padding") );
    }
    */
    public void actionPerformed(ActionEvent e) {
        fireActionListenerActionPerformed(e);
    }
    
    
    public void resetPen(JRPen pen)
    {
        if (pen == null) return;
        pen.setLineColor(null);
        pen.setLineStyle((LineStyleEnum)null);
        pen.setLineWidth(null);
    }
    
    
    
    public void widthChanged() {
        
        if (isInit() || lineBox == null) return;
                
        List<Side> borders =  new ArrayList<Side>();
        borders.addAll(selectionPanel.getSelectedBorders());
       
        if (borders.size() == 0)
        {
            JRPen pen = lineBox.getPen();
            pen.setLineWidth( spinnedModel.getNumber().floatValue() );
            borders.add( Side.TOP);
            borders.add(Side.BOTTOM);
            borders.add(Side.LEFT);
            borders.add(Side.RIGHT);
        }
        
            
        for (BoxBorderSelectionPanel.Side s : borders)
        {
            switch (s)
            {
                case TOP:
                {
                    JRPen pen = lineBox.getTopPen();
                    pen.setLineWidth( spinnedModel.getNumber().floatValue() );
                    break;
                }
                case LEFT:
                {
                    JRPen pen = lineBox.getLeftPen();
                    pen.setLineWidth( spinnedModel.getNumber().floatValue() );
                    break;
                }
                case BOTTOM:
                {
                    JRPen pen = lineBox.getBottomPen();
                    pen.setLineWidth( spinnedModel.getNumber().floatValue() );
                    break;
                }
                case RIGHT:
                {
                    JRPen pen = lineBox.getRightPen();
                    pen.setLineWidth( spinnedModel.getNumber().floatValue() );
                    break;
                }
            }
        }
        
        this.getSelectionPanel().repaint();
        //fireActionPerformed();
    }
    
    public void colorChanged() {
        
        if (isInit() || lineBox == null) return;
                
        Color color = colorSelector.getColor();
        
        List<Side> borders =  new ArrayList<Side>();
        borders.addAll(selectionPanel.getSelectedBorders());
        
        if (borders.size() == 0)
        {
            JRPen pen = lineBox.getPen();
            pen.setLineColor(color);
            borders.add( Side.TOP);
            borders.add(Side.BOTTOM);
            borders.add(Side.LEFT);
            borders.add(Side.RIGHT);
        }
         
        for (BoxBorderSelectionPanel.Side s : borders)
        {
            switch (s)
            {
                case TOP:
                {
                    JRPen pen = lineBox.getTopPen();
                    if (pen != null) pen.setLineColor(color );
                    break;
                }
                case LEFT:
                {
                    JRPen pen = lineBox.getLeftPen();
                    if (pen != null) pen.setLineColor(color );
                    break;
                }
                case BOTTOM:
                {
                    JRPen pen = lineBox.getBottomPen();
                    if (pen != null) pen.setLineColor(color );
                    break;
                }
                case RIGHT:
                {
                    JRPen pen = lineBox.getRightPen();
                    if (pen != null) pen.setLineColor(color );
                    break;
                }
            }
        }
        
        this.getSelectionPanel().repaint();
        //fireActionPerformed();
    }
    
    public void lineStyleChanged() {
        
        if (isInit() || lineBox == null) return;
                
        LineStyleEnum style = null;
        if (jList1.getSelectedIndex() >= 0)
        {
            style = (LineStyleEnum)jList1.getSelectedValue();
        }
        
        List<Side> borders =  new ArrayList<Side>();
        borders.addAll(selectionPanel.getSelectedBorders());
        
        if (borders.isEmpty())
        {
            JRPen pen = lineBox.getPen();
            pen.setLineStyle( style );
            borders.add( Side.TOP);
            borders.add(Side.BOTTOM);
            borders.add(Side.LEFT);
            borders.add(Side.RIGHT);
        }
            
        for (BoxBorderSelectionPanel.Side s : borders)
        {
            switch (s)
            {
                case TOP:
                {
                    JRPen pen = lineBox.getTopPen();
                    pen.setLineStyle( style );
                    break;
                }
                case LEFT:
                {
                    JRPen pen = lineBox.getLeftPen();
                    pen.setLineStyle( style );
                    break;
                }
                case BOTTOM:
                {
                    JRPen pen = lineBox.getBottomPen();
                    pen.setLineStyle( style );
                    break;
                }
                case RIGHT:
                {
                    JRPen pen = lineBox.getRightPen();
                    pen.setLineStyle( style );
                    break;
                }
            }
        }
        
        this.getSelectionPanel().repaint();
        //fireActionPerformed();
    }
    
    
    JDialog dialog = null;
    
    public JRLineBox showDialog(JRLineBox box)
    {
        setLineBox(box);
        getJPanelButtons().setVisible(true);
        dialog = new JDialog(Misc.getMainFrame(), true);
        dialog.getContentPane().add(this);

        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };

        dialog.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        dialog.getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);


        //to make the default button ...
        dialog.getRootPane().setDefaultButton(this.jButtonOk);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        if (getDialogResult() == JOptionPane.OK_OPTION)
        {
            return getLineBox();
        }
        
        return null;
        
    }

    public javax.swing.JPanel getJPanelButtons() {
        return jPanelButtons;
    }

    public void setJPanelButtons(javax.swing.JPanel jPanelButtons) {
        this.jPanelButtons = jPanelButtons;
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
}
