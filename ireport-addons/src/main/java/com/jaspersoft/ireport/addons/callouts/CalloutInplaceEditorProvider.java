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
package com.jaspersoft.ireport.addons.callouts;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import org.netbeans.api.visual.action.InplaceEditorProvider;
import org.netbeans.api.visual.action.InplaceEditorProvider.EditorController;
import org.netbeans.api.visual.action.InplaceEditorProvider.ExpansionDirection;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.event.*;
import java.util.EnumSet;
import javax.swing.border.EmptyBorder;

/**
 * This class derives from the David Kaspar's implemenation. Unfortunately that class is final.
 * The new implementation is used to modify the textfield for based on the widget...
 * @author Giulio Toffoli
 */
public class CalloutInplaceEditorProvider implements InplaceEditorProvider<JTextArea> {

    //private static final Border imgBorder = new ImageBorder(new Insets(6, 7, 12, 12), new Insets(16, 16, 22, 21), ImageUtilities.loadImage("/com/jaspersoft/ireport/addons/callouts/borders.png"));
    private TextFieldInplaceEditor editor;
    private EnumSet<InplaceEditorProvider.ExpansionDirection> expansionDirections;

    private KeyListener keyListener;
    private FocusListener focusListener;
    private DocumentListener documentListener;

    public CalloutInplaceEditorProvider (TextFieldInplaceEditor editor) {
        this.editor = editor;
        this.expansionDirections = EnumSet.<InplaceEditorProvider.ExpansionDirection>of (InplaceEditorProvider.ExpansionDirection.BOTTOM);

    }

    public JTextArea createEditorComponent (EditorController controller, Widget widget) {
        if (! editor.isEnabled (widget))
            return null;

        final JTextArea field = new JTextArea (editor.getText (widget));
        final Widget widget2 = widget;
        field.setWrapStyleWord(true);
        field.setLineWrap(true);
        //field.setForeground(Color.red);

        double zoomFactor = widget.getScene().getZoomFactor();
        if (zoomFactor != 1.0 && field.getFont() != null)
        {
            Font font = field.getFont();
            font = font.deriveFont((float) (font.getSize2D() * zoomFactor));
            field.setFont( font );
        }
        
        field.setBorder(new EmptyBorder(0,(int)(2*zoomFactor),0,(int)(2*zoomFactor))); // new LineBorder(new Color(255,216,0), 1));

        field.selectAll();
        //Font font = getFont(widget);
        //field.setFont(font);
        field.setBackground(new Color(253, 255,127));
        field.setOpaque(true);

        field.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                recalculateBounds(field, widget2);
            }

            public void removeUpdate(DocumentEvent e) {
                recalculateBounds(field, widget2);
            }

            public void changedUpdate(DocumentEvent e) {
                recalculateBounds(field, widget2);
            }
        });
        
        return field;
    }

    public void recalculateBounds(JTextArea field, Widget w) {

        ((CalloutWidget)w).setText(field.getText());

        Dimension d = field.getPreferredSize();
        //d.height = Math.max(d.height, w.getBounds().height-w.getBorder().getInsets().top-w.getBorder().getInsets().bottom);
        //w.setPreferredSize(new Dimension(w.getBounds().width, d.height));
        w.getScene().validate();
        w.getScene().repaint();
        Rectangle r = getInitialEditorComponentBounds(null, w, field, null);
        field.setSize(r.width, r.height);

    }

    private Font getFont(Widget widget)
    {
        
        Scene scene = widget.getScene();
        Font font = widget.getFont();
        if (scene.getZoomFactor() != 1.0)
        {
            font = font.deriveFont((float) (font.getSize2D() * scene.getZoomFactor()));
        }
        return font;
    }
    
    public void notifyOpened (final EditorController controller, Widget widget, JTextArea editor) {
        final JTextArea editor2 = editor;
        final Widget widget2 = widget;
        editor.setMinimumSize (new Dimension (64, 19));
        keyListener = new KeyAdapter() {
            public void keyPressed (KeyEvent e) {
                switch (e.getKeyChar ()) {
                    case KeyEvent.VK_ESCAPE:
                        e.consume ();
                        controller.closeEditor (false);
                        break;
                    case KeyEvent.VK_ENTER:
                        /*
                        if (e.isMetaDown() || e.isAltDown())
                        {
                            e.setModifiers(0);
                        }
                        else
                        {
                            e.consume ();
                            controller.closeEditor (true);
                        }
                        */
                        break;
                    case KeyEvent.VK_H:

                        System.out.println( "JTextArea: " + editor2.getFont() );
                        System.out.println( "JTextArea: " +  ((Graphics2D)editor2.getGraphics()).getRenderingHints() );
                        System.out.flush();

                        System.out.println( "Widget  " + widget2.getFont() );
                        //System.out.println( "Widget  " + ((Graphics2D)widget2.getGraphics()).getRenderingHints() );
                        System.out.flush();

                        //((CalloutWidget)widget2).setHints(((Graphics2D)editor2.getGraphics()).getRenderingHints());
                        
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
        return new Rectangle( (int)((widget.getLocation().x+widget.getBorder().getInsets().left+10) * widget.getScene().getZoomFactor()),
                              (int)((widget.getLocation().y+widget.getBorder().getInsets().top+10) * widget.getScene().getZoomFactor()),
                              (int)((widget.getBounds().width-widget.getBorder().getInsets().left-widget.getBorder().getInsets().right) * widget.getScene().getZoomFactor()),
                              (int)((widget.getBounds().height-widget.getBorder().getInsets().top-widget.getBorder().getInsets().bottom) * widget.getScene().getZoomFactor()));
    }

    public EnumSet<ExpansionDirection> getExpansionDirections (EditorController controller, Widget widget, JTextArea editor) {
        return null; //expansionDirections;
    }

}
