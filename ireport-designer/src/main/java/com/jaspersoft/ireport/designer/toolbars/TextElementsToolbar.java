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
package com.jaspersoft.ireport.designer.toolbars;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ReportClassLoader;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.tools.JNumberComboBox;
import com.jaspersoft.ireport.designer.tools.ValueChangedEvent;
import com.jaspersoft.ireport.designer.tools.ValueChangedListener;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JToggleButton;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRFontUtil;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author  gtoffoli
 */
public class TextElementsToolbar extends javax.swing.JPanel implements LookupListener, PropertyChangeListener, PreferenceChangeListener {
    
    private final Lookup.Result <ElementNode> result;
    private List<JRDesignTextElement> selectedTextElements = new ArrayList<JRDesignTextElement>();
    
    /**
     * This is used to prevent action perform when we are setting all the UI elements
     */
    private boolean init = false;

    /**
     * This is used to ignore PropertyChange events when we are setting new values.
     */
   private boolean changing = false;
    
    
    double[] predefinedFontSizes = new double[]{3.0,5.0,8.0,10.0,12.0,14.0,24.0,36.0,48.0};
    
    /** Creates new form TextElementsToolbar */
    public TextElementsToolbar() {
        
        setInit(true);
        initComponents();

        updateFonts();

        ((JNumberComboBox)jComboBoxFontSize).addValueChangedListener(new ValueChangedListener() {

            public void valueChanged(ValueChangedEvent evt) {

                if (isInit()) return;
                fontSizeSelected();
            }
        });

        jComboBoxFontName.setRenderer(new FontListCellRenderer());


        for (double size : predefinedFontSizes)
        {
            ((JNumberComboBox)jComboBoxFontSize).addEntry((int)size + "", size);
        }
        
        setInit(false);

        IReportManager.getPreferences().addPreferenceChangeListener(this);
        
        result = Utilities.actionsGlobalContext().lookup(new Lookup.Template(ElementNode.class));
        result.addLookupListener(this);
        result.allItems();
        resultChanged(null);
    }


    public void updateFonts()
    {
        init = true;
        String selectedFont = null;

        if (jComboBoxFontName.getSelectedItem() != null)
        {
            if (jComboBoxFontName.getSelectedItem() instanceof Tag)
            {
                selectedFont = (String)((Tag)jComboBoxFontName.getSelectedItem()).getValue();
            }
            else
            {
                selectedFont = ""+jComboBoxFontName.getSelectedItem();
            }
        }


        jComboBoxFontName.removeAllItems();


        List<Tag> classes = new ArrayList<Tag>();

        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(new ReportClassLoader(IReportManager.getReportClassLoader()));

        Collection extensionFonts = JRFontUtil.getFontFamilyNames();
        for(Iterator it = extensionFonts.iterator(); it.hasNext();)
        {
            String fname = (String)it.next();
            classes.add(new Tag(fname));
        }

        Thread.currentThread().setContextClassLoader(oldCL);

        classes.add(new Tag(null, "--"));

        if (IReportManager.getPreferences().getBoolean("showSystemFonts", true))
        {

            String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            //classes.add(new Tag("sansserif","SansSerif"));

            for (int i = 0; i < names.length; i++) {
                    String name = names[i];
                    classes.add(new Tag(name));
            }

        }

         jComboBoxFontName.setModel(new DefaultComboBoxModel(classes.toArray()));

         if (selectedFont != null)
         {
             Misc.setComboboxSelectedTagValue(jComboBoxFontName, selectedFont);
         }

         init = false;
    }

    public void preferenceChange(PreferenceChangeEvent evt) {
        if (evt == null || evt.getKey() == null || evt.getKey().equals( IReportManager.IREPORT_CLASSPATH) || evt.getKey().equals("fontExtensions"))
        {
            // Refresh the array...
            updateFonts();
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroupHorizontalAlign = new javax.swing.ButtonGroup();
        buttonGroupVerticalAlign = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        jComboBoxFontName = new WideComboBox();
        jComboBoxFontSize = new JNumberComboBox();
        jButtonIncreaseFont = new javax.swing.JButton();
        jButtonDecreaseFont = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jToggleButtonBold = new javax.swing.JToggleButton();
        jToggleButtonItalic = new javax.swing.JToggleButton();
        jToggleButtonUnderline = new javax.swing.JToggleButton();
        jToggleButtonStriketrought = new javax.swing.JToggleButton();
        jToggleButtonAlignLeft = new javax.swing.JToggleButton();
        jToggleButtonAlignJustify = new javax.swing.JToggleButton();
        jToggleButtonAlignCenter = new javax.swing.JToggleButton();
        jToggleButtonAlignRight = new javax.swing.JToggleButton();
        jToggleButtonAlignTop = new javax.swing.JToggleButton();
        jToggleButtonAlignMiddle = new javax.swing.JToggleButton();
        jToggleButtonAlignBottom = new javax.swing.JToggleButton();
        jPanelFiller = new javax.swing.JPanel();

        setMaximumSize(new java.awt.Dimension(700, 23));
        setMinimumSize(new java.awt.Dimension(610, 23));
        setPreferredSize(new java.awt.Dimension(710, 23));
        setLayout(new java.awt.GridBagLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(700, 32769));
        jToolBar1.setMinimumSize(new java.awt.Dimension(600, 25));
        jToolBar1.setOpaque(false);
        jToolBar1.setPreferredSize(new java.awt.Dimension(700, 25));

        jComboBoxFontName.setEditable(true);
        jComboBoxFontName.setMaximumRowCount(20);
        jComboBoxFontName.setMaximumSize(new java.awt.Dimension(140, 22));
        jComboBoxFontName.setMinimumSize(new java.awt.Dimension(140, 22));
        jComboBoxFontName.setPreferredSize(new java.awt.Dimension(170, 22));
        jComboBoxFontName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFontNameActionPerformed(evt);
            }
        });
        jToolBar1.add(jComboBoxFontName);

        jComboBoxFontSize.setEditable(true);
        jComboBoxFontSize.setMaximumSize(new java.awt.Dimension(50, 22));
        jComboBoxFontSize.setMinimumSize(new java.awt.Dimension(51, 22));
        jComboBoxFontSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFontSizeActionPerformed(evt);
            }
        });
        jToolBar1.add(jComboBoxFontSize);

        jButtonIncreaseFont.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/increase_font_size.png"))); // NOI18N
        jButtonIncreaseFont.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jButtonIncreaseFont.text")); // NOI18N
        jButtonIncreaseFont.setBorderPainted(false);
        jButtonIncreaseFont.setFocusable(false);
        jButtonIncreaseFont.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonIncreaseFont.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonIncreaseFont.setMaximumSize(new java.awt.Dimension(23, 23));
        jButtonIncreaseFont.setMinimumSize(new java.awt.Dimension(23, 23));
        jButtonIncreaseFont.setPreferredSize(new java.awt.Dimension(23, 23));
        jButtonIncreaseFont.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonIncreaseFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIncreaseFontActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonIncreaseFont);

        jButtonDecreaseFont.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/decrease_font_size.png"))); // NOI18N
        jButtonDecreaseFont.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jButtonDecreaseFont.text")); // NOI18N
        jButtonDecreaseFont.setBorderPainted(false);
        jButtonDecreaseFont.setFocusable(false);
        jButtonDecreaseFont.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDecreaseFont.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonDecreaseFont.setMaximumSize(new java.awt.Dimension(23, 23));
        jButtonDecreaseFont.setMinimumSize(new java.awt.Dimension(23, 23));
        jButtonDecreaseFont.setPreferredSize(new java.awt.Dimension(23, 23));
        jButtonDecreaseFont.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDecreaseFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDecreaseFontActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonDecreaseFont);
        jToolBar1.add(jSeparator1);

        jToggleButtonBold.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/bold.png"))); // NOI18N
        jToggleButtonBold.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jToggleButtonBold.text")); // NOI18N
        jToggleButtonBold.setActionCommand(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jToggleButtonBold.actionCommand")); // NOI18N
        jToggleButtonBold.setFocusable(false);
        jToggleButtonBold.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonBold.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonBold.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonBoldItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButtonBold);

        jToggleButtonItalic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/italic.png"))); // NOI18N
        jToggleButtonItalic.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jToggleButtonItalic.text")); // NOI18N
        jToggleButtonItalic.setBorderPainted(false);
        jToggleButtonItalic.setFocusable(false);
        jToggleButtonItalic.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonItalic.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jToggleButtonItalic.setMaximumSize(new java.awt.Dimension(23, 23));
        jToggleButtonItalic.setMinimumSize(new java.awt.Dimension(23, 23));
        jToggleButtonItalic.setPreferredSize(new java.awt.Dimension(23, 23));
        jToggleButtonItalic.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonItalic.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonItalicItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButtonItalic);

        jToggleButtonUnderline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/underline.png"))); // NOI18N
        jToggleButtonUnderline.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jToggleButtonUnderline.text")); // NOI18N
        jToggleButtonUnderline.setBorderPainted(false);
        jToggleButtonUnderline.setFocusable(false);
        jToggleButtonUnderline.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonUnderline.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jToggleButtonUnderline.setMaximumSize(new java.awt.Dimension(23, 23));
        jToggleButtonUnderline.setMinimumSize(new java.awt.Dimension(23, 23));
        jToggleButtonUnderline.setPreferredSize(new java.awt.Dimension(23, 23));
        jToggleButtonUnderline.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonUnderline.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonUnderlineItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButtonUnderline);

        jToggleButtonStriketrought.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/strikethrought.png"))); // NOI18N
        jToggleButtonStriketrought.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jToggleButtonUnderline.text")); // NOI18N
        jToggleButtonStriketrought.setBorderPainted(false);
        jToggleButtonStriketrought.setFocusable(false);
        jToggleButtonStriketrought.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonStriketrought.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jToggleButtonStriketrought.setMaximumSize(new java.awt.Dimension(23, 23));
        jToggleButtonStriketrought.setMinimumSize(new java.awt.Dimension(23, 23));
        jToggleButtonStriketrought.setPreferredSize(new java.awt.Dimension(23, 23));
        jToggleButtonStriketrought.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonStriketrought.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonStriketroughtActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonStriketrought);

        buttonGroupHorizontalAlign.add(jToggleButtonAlignLeft);
        jToggleButtonAlignLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/align_left.png"))); // NOI18N
        jToggleButtonAlignLeft.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jToggleButtonUnderline.text")); // NOI18N
        jToggleButtonAlignLeft.setBorderPainted(false);
        jToggleButtonAlignLeft.setFocusable(false);
        jToggleButtonAlignLeft.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonAlignLeft.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jToggleButtonAlignLeft.setMaximumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignLeft.setMinimumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignLeft.setPreferredSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignLeft.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonAlignLeft.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonAlignLeftItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButtonAlignLeft);

        buttonGroupHorizontalAlign.add(jToggleButtonAlignJustify);
        jToggleButtonAlignJustify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/align_justified.png"))); // NOI18N
        jToggleButtonAlignJustify.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jToggleButtonUnderline.text")); // NOI18N
        jToggleButtonAlignJustify.setBorderPainted(false);
        jToggleButtonAlignJustify.setFocusable(false);
        jToggleButtonAlignJustify.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonAlignJustify.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jToggleButtonAlignJustify.setMaximumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignJustify.setMinimumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignJustify.setPreferredSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignJustify.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonAlignJustify.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonAlignJustifyItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButtonAlignJustify);

        buttonGroupHorizontalAlign.add(jToggleButtonAlignCenter);
        jToggleButtonAlignCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/align_center.png"))); // NOI18N
        jToggleButtonAlignCenter.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jToggleButtonUnderline.text")); // NOI18N
        jToggleButtonAlignCenter.setBorderPainted(false);
        jToggleButtonAlignCenter.setFocusable(false);
        jToggleButtonAlignCenter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonAlignCenter.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jToggleButtonAlignCenter.setMaximumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignCenter.setMinimumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignCenter.setPreferredSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignCenter.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonAlignCenter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonAlignCenterItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButtonAlignCenter);

        buttonGroupHorizontalAlign.add(jToggleButtonAlignRight);
        jToggleButtonAlignRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/align_right.png"))); // NOI18N
        jToggleButtonAlignRight.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jToggleButtonUnderline.text")); // NOI18N
        jToggleButtonAlignRight.setBorderPainted(false);
        jToggleButtonAlignRight.setFocusable(false);
        jToggleButtonAlignRight.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonAlignRight.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jToggleButtonAlignRight.setMaximumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignRight.setMinimumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignRight.setPreferredSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignRight.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonAlignRight.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonAlignRightItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButtonAlignRight);

        buttonGroupVerticalAlign.add(jToggleButtonAlignTop);
        jToggleButtonAlignTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/align_top.png"))); // NOI18N
        jToggleButtonAlignTop.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jToggleButtonUnderline.text")); // NOI18N
        jToggleButtonAlignTop.setBorderPainted(false);
        jToggleButtonAlignTop.setFocusable(false);
        jToggleButtonAlignTop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonAlignTop.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jToggleButtonAlignTop.setMaximumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignTop.setMinimumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignTop.setPreferredSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignTop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonAlignTop.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonAlignTopItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButtonAlignTop);

        buttonGroupVerticalAlign.add(jToggleButtonAlignMiddle);
        jToggleButtonAlignMiddle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/align_middle.png"))); // NOI18N
        jToggleButtonAlignMiddle.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jToggleButtonUnderline.text")); // NOI18N
        jToggleButtonAlignMiddle.setBorderPainted(false);
        jToggleButtonAlignMiddle.setFocusable(false);
        jToggleButtonAlignMiddle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonAlignMiddle.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jToggleButtonAlignMiddle.setMaximumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignMiddle.setMinimumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignMiddle.setPreferredSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignMiddle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonAlignMiddle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonAlignMiddleItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButtonAlignMiddle);

        buttonGroupVerticalAlign.add(jToggleButtonAlignBottom);
        jToggleButtonAlignBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/align_bottom.png"))); // NOI18N
        jToggleButtonAlignBottom.setText(org.openide.util.NbBundle.getMessage(TextElementsToolbar.class, "TextElementsToolbar.jToggleButtonUnderline.text")); // NOI18N
        jToggleButtonAlignBottom.setBorderPainted(false);
        jToggleButtonAlignBottom.setFocusable(false);
        jToggleButtonAlignBottom.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonAlignBottom.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jToggleButtonAlignBottom.setMaximumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignBottom.setMinimumSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignBottom.setPreferredSize(new java.awt.Dimension(23, 23));
        jToggleButtonAlignBottom.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonAlignBottom.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonAlignBottomItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButtonAlignBottom);

        add(jToolBar1, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        add(jPanelFiller, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxFontNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFontNameActionPerformed
        
        if (isInit()) return;
        
        boolean oldChanging = isChanging();
        setChanging(true);
        
        boolean isFirstUndo = true;
        if (getSelectedTextElements().size() > 0)
        {
            String newFontName = null;
            if (jComboBoxFontName.getSelectedItem() instanceof Tag)
            {
                newFontName = (String)((Tag)jComboBoxFontName.getSelectedItem()).getValue();
                if (((Tag)jComboBoxFontName.getSelectedItem()).getName().equals("--"))
                {
                    resultChanged(null);
                    return;
                }
            }
            else
            {
                newFontName = ""+jComboBoxFontName.getSelectedItem();
            }

            if (newFontName != null && newFontName.length() > 0)
            {
                for (JRDesignTextElement element : getSelectedTextElements())
                {
                    String oldFontName = element.getOwnFontName();
                    element.setFontName(newFontName);
                    
                    ObjectPropertyUndoableEdit opUndo = new ObjectPropertyUndoableEdit(element, "FontName",String.class, oldFontName , newFontName);
                    IReportManager.getInstance().addUndoableEdit(opUndo, !isFirstUndo);
                    isFirstUndo = false;
                }
                if (jComboBoxFontName.getItemAt(0).equals("")) jComboBoxFontName.removeItemAt(0);
                
            } 
        }
        
        setChanging(oldChanging);
    }//GEN-LAST:event_jComboBoxFontNameActionPerformed

    private void jComboBoxFontSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFontSizeActionPerformed
        
        
        
    }//GEN-LAST:event_jComboBoxFontSizeActionPerformed

    private void jToggleButtonIncreaseFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonIncreaseFontActionPerformed
        
        
        
        
    }//GEN-LAST:event_jToggleButtonIncreaseFontActionPerformed

    private void jToggleButtonDecreaseFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonDecreaseFontActionPerformed
        
    }//GEN-LAST:event_jToggleButtonDecreaseFontActionPerformed

    private void jButtonIncreaseFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIncreaseFontActionPerformed
        if (getSelectedTextElements().size() > 0)
        {
            boolean isFirstUndo = true;
            for (JRDesignTextElement element : getSelectedTextElements())
            {
                // TODO: add undo operation...
                int newFontSize = element.getFontSize() + 2;
                Integer oldFontSize = element.getOwnFontSize();
                element.setFontSize(newFontSize);
                ObjectPropertyUndoableEdit opUndo = new ObjectPropertyUndoableEdit(element, "FontSize",Integer.class, oldFontSize , new Integer(newFontSize));
                IReportManager.getInstance().addUndoableEdit(opUndo, !isFirstUndo);
                isFirstUndo = false;
            }
        }
    }//GEN-LAST:event_jButtonIncreaseFontActionPerformed

    private void jButtonDecreaseFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDecreaseFontActionPerformed
        if (getSelectedTextElements().size() > 0)
        {
            boolean isFirstUndo = true;
            for (JRDesignTextElement element : getSelectedTextElements())
            {
                // TODO: add undo operation...
                int newFontSize = element.getFontSize() -2;
                if (newFontSize >= 3)
                {
                    Integer oldFontSize = element.getOwnFontSize();
                    element.setFontSize(newFontSize);
                    ObjectPropertyUndoableEdit opUndo = new ObjectPropertyUndoableEdit(element, "FontSize",Integer.class, oldFontSize , new Integer(newFontSize));
                    IReportManager.getInstance().addUndoableEdit(opUndo, !isFirstUndo);
                    isFirstUndo = false;
                }
            }
        }
    }//GEN-LAST:event_jButtonDecreaseFontActionPerformed

    private void jToggleButtonBoldItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonBoldItemStateChanged
        if (isInit()) return;
        boolean oldChanging = isChanging();
        setChanging(true);
        
        if (getSelectedTextElements().size() > 0)
        {
            boolean isFirstUndo = true;
            boolean newValue = jToggleButtonBold.isSelected();
            for (JRDesignTextElement element : getSelectedTextElements())
            {
                // TODO: add undo operation...
                
                
                if (newValue != element.isBold())
                {
                    Boolean oldValue = element.isOwnBold();
                    element.setBold(newValue);
                    ObjectPropertyUndoableEdit opUndo = new ObjectPropertyUndoableEdit(element, "Bold",Boolean.class, oldValue , new Boolean(newValue));
                    IReportManager.getInstance().addUndoableEdit(opUndo, !isFirstUndo);
                    isFirstUndo = false;
                }
            }
        }
        
        setChanging(oldChanging);
    }//GEN-LAST:event_jToggleButtonBoldItemStateChanged

    private void jToggleButtonItalicItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonItalicItemStateChanged
        if (isInit()) return;
        boolean oldChanging = isChanging();
        setChanging(true);
        
        if (getSelectedTextElements().size() > 0)
        {
            boolean isFirstUndo = true;
            boolean newValue = jToggleButtonItalic.isSelected();
            for (JRDesignTextElement element : getSelectedTextElements())
            {
                // TODO: add undo operation...
                
                if (newValue != element.isItalic())
                {
                    Boolean oldValue = element.isOwnItalic();
                    element.setItalic(newValue);
                    ObjectPropertyUndoableEdit opUndo = new ObjectPropertyUndoableEdit(element, "Italic",Boolean.class, oldValue , new Boolean(newValue));
                    IReportManager.getInstance().addUndoableEdit(opUndo, !isFirstUndo);
                    isFirstUndo = false;
                }
            }
        }
        
        setChanging(oldChanging);
    }//GEN-LAST:event_jToggleButtonItalicItemStateChanged

    private void jToggleButtonUnderlineItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonUnderlineItemStateChanged
        if (isInit()) return;
        boolean oldChanging = isChanging();
        setChanging(true);
                
        if (getSelectedTextElements().size() > 0)
        {
            boolean isFirstUndo = true;
            boolean newValue = jToggleButtonUnderline.isSelected();
            for (JRDesignTextElement element : getSelectedTextElements())
            {
                // TODO: add undo operation...
                if (newValue != element.isUnderline())
                {
                    Boolean oldValue = element.isOwnUnderline();
                    element.setUnderline(newValue);
                    ObjectPropertyUndoableEdit opUndo = new ObjectPropertyUndoableEdit(element, "Underline",Boolean.class, oldValue , new Boolean(newValue));
                    IReportManager.getInstance().addUndoableEdit(opUndo, !isFirstUndo);
                    isFirstUndo = false;
                }
            }
        }
        
        setChanging(oldChanging);
    }//GEN-LAST:event_jToggleButtonUnderlineItemStateChanged

    private void jToggleButtonStriketroughtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonStriketroughtActionPerformed
        if (isInit()) return;
        boolean oldChanging = isChanging();
        setChanging(true);
        
        if (getSelectedTextElements().size() > 0)
        {
            boolean isFirstUndo = true;
            boolean newValue = jToggleButtonStriketrought.isSelected();
            for (JRDesignTextElement element : getSelectedTextElements())
            {
                // TODO: add undo operation...
                if (newValue != element.isStrikeThrough())
                {
                    Boolean oldValue = element.isOwnStrikeThrough();
                    element.setStrikeThrough(newValue);
                    ObjectPropertyUndoableEdit opUndo = new ObjectPropertyUndoableEdit(element, "StrikeThrough",Boolean.class, oldValue , new Boolean(newValue));
                    IReportManager.getInstance().addUndoableEdit(opUndo, !isFirstUndo);
                    isFirstUndo = false;
                }
            }
        }
        
        setChanging(oldChanging);
    }//GEN-LAST:event_jToggleButtonStriketroughtActionPerformed

    private void jToggleButtonAlignLeftItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonAlignLeftItemStateChanged
        modifyHorizontalAlignment();
    }//GEN-LAST:event_jToggleButtonAlignLeftItemStateChanged

    private void jToggleButtonAlignJustifyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonAlignJustifyItemStateChanged
        modifyHorizontalAlignment();
    }//GEN-LAST:event_jToggleButtonAlignJustifyItemStateChanged

    private void jToggleButtonAlignCenterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonAlignCenterItemStateChanged
        modifyHorizontalAlignment();
    }//GEN-LAST:event_jToggleButtonAlignCenterItemStateChanged

    private void jToggleButtonAlignRightItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonAlignRightItemStateChanged
        modifyHorizontalAlignment();
    }//GEN-LAST:event_jToggleButtonAlignRightItemStateChanged

    private void jToggleButtonAlignTopItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonAlignTopItemStateChanged
        modifyVerticalAlignment();
    }//GEN-LAST:event_jToggleButtonAlignTopItemStateChanged

    private void jToggleButtonAlignMiddleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonAlignMiddleItemStateChanged
        modifyVerticalAlignment();
    }//GEN-LAST:event_jToggleButtonAlignMiddleItemStateChanged

    private void jToggleButtonAlignBottomItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonAlignBottomItemStateChanged
        modifyVerticalAlignment();
    }//GEN-LAST:event_jToggleButtonAlignBottomItemStateChanged
    
    
    private void modifyHorizontalAlignment() {
        if (isInit()) return;
        boolean oldChanging = isChanging();
        setChanging(true);
        
        if (getSelectedTextElements().size() > 0)
        {
            boolean isFirstUndo = true;
            HorizontalAlignEnum newValue = getSelectedHorizontalAlignment();
            for (JRDesignTextElement element : getSelectedTextElements())
            {
                // TODO: add undo operation...
                if ((newValue == null && element.getOwnHorizontalAlignmentValue() != null) || (newValue != null && newValue != element.getHorizontalAlignmentValue()))
                {
                    HorizontalAlignEnum oldValue = element.getOwnHorizontalAlignmentValue();
                    element.setHorizontalAlignment(newValue);
                    ObjectPropertyUndoableEdit opUndo = new ObjectPropertyUndoableEdit(element, "HorizontalAlignment",HorizontalAlignEnum.class, oldValue , newValue);
                    IReportManager.getInstance().addUndoableEdit(opUndo, !isFirstUndo);
                    isFirstUndo = false;
                }
            }
        }
        
        setChanging(oldChanging);
    }
    
    
    private void modifyVerticalAlignment() {
        if (isInit()) return;
        boolean oldChanging = isChanging();
        setChanging(true);
        
        if (getSelectedTextElements().size() > 0)
        {
            boolean isFirstUndo = true;
            VerticalAlignEnum newValue = getSelectedVerticalAlignment();
            for (JRDesignTextElement element : getSelectedTextElements())
            {
                // if newValue is null does not do anything 
                // since the vertical alignment could be justified for which we do not
                // provide an icon...
                if (newValue != null && newValue != element.getVerticalAlignmentValue())
                {
                    VerticalAlignEnum oldValue = element.getOwnVerticalAlignmentValue();
                    element.setVerticalAlignment(newValue);
                    ObjectPropertyUndoableEdit opUndo = new ObjectPropertyUndoableEdit(element, "VerticalAlignment",VerticalAlignEnum.class, oldValue , newValue);
                    IReportManager.getInstance().addUndoableEdit(opUndo, !isFirstUndo);
                    isFirstUndo = false;
                }
            }
        }
        
        setChanging(oldChanging);
    }
    
    
    private void fontSizeSelected() {
     
        if (isInit()) return;
        boolean oldChanging = isChanging();
        setChanging(true);
        
        boolean isFirstUndo = true;
        if (getSelectedTextElements().size() > 0)
        {
            int newFontSize = (int)((JNumberComboBox)jComboBoxFontSize).getValue();
            
            for (JRDesignTextElement element : getSelectedTextElements())
            {
                // TODO: add undo operation...
                if (element.getFontSize() != newFontSize)
                {
                    Integer oldFontSize = element.getOwnFontSize();
                    element.setFontSize(newFontSize);
                    ObjectPropertyUndoableEdit opUndo = new ObjectPropertyUndoableEdit(element, "FontSize",Integer.class, oldFontSize , new Integer(newFontSize));
                    IReportManager.getInstance().addUndoableEdit(opUndo, !isFirstUndo);
                    isFirstUndo = false;
                }
            }
        }
        setChanging(oldChanging);
    }
    
    public List<JRDesignTextElement> getSelectedTextElements()
    {
        return selectedTextElements;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupHorizontalAlign;
    private javax.swing.ButtonGroup buttonGroupVerticalAlign;
    private javax.swing.JButton jButtonDecreaseFont;
    private javax.swing.JButton jButtonIncreaseFont;
    private javax.swing.JComboBox jComboBoxFontName;
    private javax.swing.JComboBox jComboBoxFontSize;
    private javax.swing.JPanel jPanelFiller;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToggleButton jToggleButtonAlignBottom;
    private javax.swing.JToggleButton jToggleButtonAlignCenter;
    private javax.swing.JToggleButton jToggleButtonAlignJustify;
    private javax.swing.JToggleButton jToggleButtonAlignLeft;
    private javax.swing.JToggleButton jToggleButtonAlignMiddle;
    private javax.swing.JToggleButton jToggleButtonAlignRight;
    private javax.swing.JToggleButton jToggleButtonAlignTop;
    private javax.swing.JToggleButton jToggleButtonBold;
    private javax.swing.JToggleButton jToggleButtonItalic;
    private javax.swing.JToggleButton jToggleButtonStriketrought;
    private javax.swing.JToggleButton jToggleButtonUnderline;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    public void resultChanged(LookupEvent arg0) {
        
        setInit(true);
        // TODO: add/remove element listeners...
        for (JRDesignTextElement element : getSelectedTextElements())
        {
            element.getEventSupport().removePropertyChangeListener(this);
        }
        selectedTextElements.clear();
        
        Collection<? extends ElementNode> nodes = result.allInstances();
        for (ElementNode en : nodes)
        {
            if (en.getElement() instanceof JRDesignTextElement)
            {
                selectedTextElements.add((JRDesignTextElement)en.getElement());
            }
        }

        for (JRDesignTextElement element : getSelectedTextElements())
        {
            element.getEventSupport().addPropertyChangeListener(this);
        }

        updateButtons();
        setInit(false);
    }
    
    public void updateButtons()
    {
        boolean oldInit = isInit();
        setInit(true);
        
        try {
            boolean enabled = getSelectedTextElements().size() > 0;

            jComboBoxFontName.setEnabled(enabled);
            jComboBoxFontSize.setEnabled(enabled);
            jToggleButtonAlignBottom.setEnabled(enabled);
            jToggleButtonAlignTop.setEnabled(enabled);
            jToggleButtonAlignMiddle.setEnabled(enabled);
            jToggleButtonAlignLeft.setEnabled(enabled);
            jToggleButtonAlignRight.setEnabled(enabled);
            jToggleButtonAlignCenter.setEnabled(enabled);
            jToggleButtonAlignJustify.setEnabled(enabled);

            jButtonDecreaseFont.setEnabled(enabled);
            jButtonIncreaseFont.setEnabled(enabled);
            jToggleButtonBold.setEnabled(enabled);
            jToggleButtonItalic.setEnabled(enabled);
            jToggleButtonStriketrought.setEnabled(enabled);
            jToggleButtonUnderline.setEnabled(enabled);

            if (jComboBoxFontName.getItemCount() == 0 || !jComboBoxFontName.getItemAt(0).equals(""))
                    this.jComboBoxFontName.insertItemAt("",0);

            // If there are selected elements, update the font and buttons
            // selections...
            if (enabled)
            {
                boolean sameFontName = true;
                boolean sameFontSize = true;
                boolean sameBold = true;
                boolean sameItalic = true;
                boolean sameStriketrough = true;
                boolean sameUnderline= true;
                boolean sameHorizontalAlignment = true;
                boolean sameVerticalAlignment = true;
                
                boolean isFirstElement = true;
                
                for (JRDesignTextElement element : getSelectedTextElements())
                {
                    if (sameFontName) sameFontName = Misc.setComboBoxTag(isFirstElement, element.getFontName(), jComboBoxFontName);
                    if (sameFontSize) sameFontSize = Misc.setElementComboNumber(isFirstElement, element.getFontSize(), (JNumberComboBox)jComboBoxFontSize);
                    
                    if (sameBold) sameBold = setToggleButton(isFirstElement, element.isBold(), jToggleButtonBold);
                    if (sameItalic) sameItalic = setToggleButton(isFirstElement, element.isItalic(), jToggleButtonItalic);
                    if (sameUnderline) sameUnderline = setToggleButton(isFirstElement, element.isUnderline(), jToggleButtonUnderline);
                    if (sameStriketrough) sameStriketrough = setToggleButton(isFirstElement, element.isStrikeThrough(), jToggleButtonStriketrought);

                    if (sameHorizontalAlignment) sameHorizontalAlignment = setHorizontalAlignment(isFirstElement, element.getHorizontalAlignmentValue());
                    if (sameVerticalAlignment) sameVerticalAlignment = setVerticalAlignment(isFirstElement, element.getVerticalAlignmentValue());
                    
                    
                    isFirstElement = false;
                }
            }
        } finally 
        {
            setInit(oldInit);
        }
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        if (!isChanging() &&
            (evt.getPropertyName().equals( JRBaseStyle.PROPERTY_FONT_NAME) ||
            evt.getPropertyName().equals( JRBaseStyle.PROPERTY_FONT_SIZE) ||
            evt.getPropertyName().equals( JRBaseStyle.PROPERTY_BOLD) ||
            evt.getPropertyName().equals( JRBaseStyle.PROPERTY_ITALIC) ||
            evt.getPropertyName().equals( JRBaseStyle.PROPERTY_STRIKE_THROUGH) ||
            evt.getPropertyName().equals( JRBaseStyle.PROPERTY_UNDERLINE) ||
            evt.getPropertyName().equals( JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT) ||
            evt.getPropertyName().equals( JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT)))
        {
            updateButtons();
        }
    }

    private HorizontalAlignEnum getSelectedHorizontalAlignment()
    {
        if (jToggleButtonAlignLeft.isSelected()) return HorizontalAlignEnum.LEFT;
        if (jToggleButtonAlignRight.isSelected()) return HorizontalAlignEnum.RIGHT;
        if (jToggleButtonAlignJustify.isSelected()) return HorizontalAlignEnum.JUSTIFIED;
        if (jToggleButtonAlignCenter.isSelected()) return HorizontalAlignEnum.CENTER;
        return null;
    }

    
    
    private void setSelectedHorizontalAlignment(HorizontalAlignEnum b)
    {
        jToggleButtonAlignLeft.setSelected(b != null && b == HorizontalAlignEnum.LEFT);
        jToggleButtonAlignRight.setSelected(b != null && b == HorizontalAlignEnum.RIGHT);
        jToggleButtonAlignJustify.setSelected(b != null && b == HorizontalAlignEnum.JUSTIFIED);
        jToggleButtonAlignCenter.setSelected(b != null && b == HorizontalAlignEnum.CENTER);
    }
    
    
    private VerticalAlignEnum getSelectedVerticalAlignment()
    {
        if (jToggleButtonAlignTop.isSelected()) return VerticalAlignEnum.TOP;
        if (jToggleButtonAlignMiddle.isSelected()) return VerticalAlignEnum.MIDDLE;
        if (jToggleButtonAlignBottom.isSelected()) return VerticalAlignEnum.BOTTOM;
        return null;
    }
    
    private void setSelectedVerticalAlignment(VerticalAlignEnum b)
    {
        jToggleButtonAlignTop.setSelected(b != null && b == VerticalAlignEnum.TOP);
        jToggleButtonAlignMiddle.setSelected(b != null && b == VerticalAlignEnum.MIDDLE);
        jToggleButtonAlignBottom.setSelected(b != null && b == VerticalAlignEnum.BOTTOM);
    }
    
    private boolean setHorizontalAlignment(boolean firstElement, HorizontalAlignEnum horizontalAlignment) {
        
        if (!firstElement)
        {
            HorizontalAlignEnum selectedValue = getSelectedHorizontalAlignment();
            if (selectedValue != null && selectedValue != horizontalAlignment)
            {
                setSelectedHorizontalAlignment(null);
            }
        }
        setSelectedHorizontalAlignment(horizontalAlignment);
        return true;
    }
    
    private boolean setVerticalAlignment(boolean firstElement, VerticalAlignEnum verticalAlignment) {
        
        if (!firstElement)
        {
            VerticalAlignEnum selectedValue = getSelectedVerticalAlignment();
            if (selectedValue != null && selectedValue != verticalAlignment)
            {
                setSelectedVerticalAlignment(null);
            }
        }
        setSelectedVerticalAlignment(verticalAlignment);
        return true;
    }

    private boolean setToggleButton(boolean firstElement, boolean val, JToggleButton button) {
        
        if (!firstElement && button.isSelected() != val)
        {
            button.setSelected(false);
            return false;
        }
        button.setSelected(val);
        return true;
    }

    public boolean isChanging() {
        return changing;
    }

    public void setChanging(boolean changing) {
        this.changing = changing;
    }
    
    
    
}
