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
package com.jaspersoft.ireport.designer.actions;


import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.designer.widgets.SelectionWidget;
import org.netbeans.api.visual.action.InplaceEditorProvider;
import org.netbeans.api.visual.action.InplaceEditorProvider.EditorController;
import org.netbeans.api.visual.action.InplaceEditorProvider.ExpansionDirection;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.EnumSet;
import javax.swing.border.LineBorder;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignTextElement;

/**
 * This class derives from the David Kaspar's implemenation. Unfortunately that class is final.
 * The new implementation is used to modify the textfield for based on the widget...
 * @author Giulio Toffoli
 */
public class ReportTextElementInplaceEditorProvider implements InplaceEditorProvider<JTextArea> {

    private TextFieldInplaceEditor editor;
    private EnumSet<InplaceEditorProvider.ExpansionDirection> expansionDirections;

    private KeyListener keyListener;
    private FocusListener focusListener;
    private DocumentListener documentListener;

    public ReportTextElementInplaceEditorProvider (TextFieldInplaceEditor editor) {
        this.editor = editor;
        this.expansionDirections = null;
    }

    public JTextArea createEditorComponent (EditorController controller, Widget widget) {
        if (! editor.isEnabled (widget))
            return null;
        JTextArea field = new JTextArea (editor.getText (widget));
        field.setBorder(new LineBorder(Color.GRAY, 1));
        field.selectAll ();
        
        Font font = getElementFont(widget);
        if (font != null)
        {
            field.setFont(font);
        }
        
        field.setAlignmentY(  getElementHorizontalAlignment(widget)  );
        field.setAlignmentX(  getElementVerticalAlignment(widget)  );
        return field;
    }

    private Font getElementFont(Widget widget)
    {
        
        JRDesignTextElement element = getTextElement(widget);
        Scene scene = widget.getScene();
        if (element != null)
        {
           int style = Font.PLAIN;
           if (element.isBold()) style |= Font.BOLD;
           if (element.isItalic()) style |= Font.ITALIC;
           
           float size = ((float) element.getFontSize() * (float) scene.getZoomFactor());
           if (size < 5f) size = 5f;
           
           Font textFont = new Font(element.getFontName(), style, (int)size);
           textFont = textFont.deriveFont(size);
           return textFont;
        }
        
        if (scene.getZoomFactor() > 1.0)
        {
            Font font = scene.getDefaultFont();
            font = font.deriveFont((float) (font.getSize2D() * scene.getZoomFactor()));
        }
        return null;
    }
    
    private float getElementHorizontalAlignment(Widget widget)
    {
        JRDesignTextElement element = getTextElement(widget);
        if (element != null)
        {
            switch (element.getHorizontalAlignmentValue())
            {
                    case CENTER:
                        return JTextArea.CENTER_ALIGNMENT;
                    case LEFT:
                        return JTextArea.LEFT_ALIGNMENT;
                    case RIGHT:
                        return JTextArea.RIGHT_ALIGNMENT;
                    //case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED:
                    //    return JTextArea.;
            }
        }
        return JTextArea.LEFT_ALIGNMENT;
    }
    
    private float getElementVerticalAlignment(Widget widget)
    {
        JRDesignTextElement element = getTextElement(widget);
        if (element != null)
        {
            switch (element.getVerticalAlignmentValue())
            {
                    case MIDDLE:
                        return JTextArea.CENTER_ALIGNMENT;
                    case TOP:
                        return JTextArea.TOP_ALIGNMENT;
                    case BOTTOM:
                        return JTextArea.BOTTOM_ALIGNMENT;
                    //case JRAlignment.VERTICAL_ALIGN_JUSTIFIED:
                    //    return JTextArea.;
            }
        }
        return JTextArea.TOP_ALIGNMENT;
    }
    
    private JRDesignTextElement getTextElement(Widget widget) {
        
        if (widget instanceof JRDesignElementWidget)
        {
            JRDesignElement element = ((JRDesignElementWidget)widget).getElement();
            if (element instanceof JRDesignTextElement) return (JRDesignTextElement) element;
        }
        else if (widget instanceof SelectionWidget)
        {
            return getTextElement( ((SelectionWidget)widget).getRealWidget() );
        }
        return null;
    }
    
    

    public void notifyOpened (final EditorController controller, Widget widget, JTextArea editor) {
        editor.setMinimumSize (new Dimension (64, 19));
        keyListener = new KeyAdapter() {
            public void keyPressed (KeyEvent e) {
                switch (e.getKeyChar ()) {
                    case KeyEvent.VK_ESCAPE:
                        e.consume ();
                        controller.closeEditor (false);
                        break;
                    case KeyEvent.VK_ENTER:
                        if (e.isMetaDown() || e.isAltDown())
                        {
                            e.setModifiers(0);
                        }
                        else
                        {
                            e.consume ();
                            controller.closeEditor (true);
                        }
                        break;
                }
            }
        };
        focusListener = new FocusAdapter() {
            public void focusLost (FocusEvent e) {
                controller.closeEditor (true);
            }
        };
        documentListener = new DocumentListener () {
            public void insertUpdate (DocumentEvent e) {
                controller.notifyEditorComponentBoundsChanged ();
            }

            public void removeUpdate (DocumentEvent e) {
                controller.notifyEditorComponentBoundsChanged ();
            }

            public void changedUpdate (DocumentEvent e) {
                controller.notifyEditorComponentBoundsChanged ();
            }
        };
        editor.addKeyListener (keyListener);
        editor.addFocusListener (focusListener);
        editor.getDocument ().addDocumentListener (documentListener);
        editor.selectAll ();
    }

    public void notifyClosing (EditorController controller, Widget widget, JTextArea editor, boolean commit) {
        editor.getDocument ().removeDocumentListener (documentListener);
        editor.removeFocusListener (focusListener);
        editor.removeKeyListener (keyListener);
        if (commit) {
            this.editor.setText (widget, editor.getText ());
            if (widget != null)
                widget.getScene ().validate ();
        }
    }

    public Rectangle getInitialEditorComponentBounds(EditorController controller, Widget widget, JTextArea editor, Rectangle viewBounds) {
        return null;
    }

    public EnumSet<ExpansionDirection> getExpansionDirections (EditorController controller, Widget widget, JTextArea editor) {
        return expansionDirections;
    }

}
