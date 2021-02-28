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
package com.jaspersoft.ireport.designer.welcome;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import org.openide.util.ImageUtilities;

/**
 *
 * @author S. Aubrecht
 */
public abstract class LinkButton extends JButton
        implements MouseListener, ActionListener, FocusListener {

    public static final String BULLET_IMAGE = "com/jaspersoft/ireport/designer/welcome/bullet.png"; // NOI18N
    public static final Color LINK_COLOR = new Color(22,75,123);
    public static final Color LINK_IN_FOCUS_COLOR = new Color(22,75,123);
    public static final Color MOUSE_OVER_LINK_COLOR = new Color(255,142,0);
    public static final Color VISITED_LINK_COLOR = new Color(22,75,123);
    public static final Stroke LINK_IN_FOCUS_STROKE = new BasicStroke(1, BasicStroke.CAP_SQUARE,
        BasicStroke.JOIN_BEVEL, 0, new float[] {0, 2}, 0);

    public static final Font BUTTON_FONT = new Font( null, Font.BOLD, 12 );


    private boolean underline = false;

    final ImageIcon BULLET_ICON = new ImageIcon( ImageUtilities.loadImage( BULLET_IMAGE ) );

    public LinkButton( String label, boolean showBullet ) {
        super( label );
        setForeground( LINK_COLOR );
        setFont( BUTTON_FONT );
        setBorder( new EmptyBorder(1, 1, 1, 1) );
        setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        setHorizontalAlignment( JLabel.LEFT );
        addMouseListener(this);
        setFocusable( true );
        if( showBullet )
            setIcon( BULLET_ICON );

        setMargin( new Insets(0, 0, 0, 0) );
        setBorderPainted( false );
        setFocusPainted( false );
        setRolloverEnabled( true );
        setContentAreaFilled( false );

        addActionListener( this );
        addFocusListener( this );
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        if( isEnabled() ) {
            underline = true;
            setForeground(LINK_IN_FOCUS_COLOR);
            repaint();
            onMouseEntered( e );
            setForeground(MOUSE_OVER_LINK_COLOR);
        }
    }

    public void mouseExited(MouseEvent e) {
        if( isEnabled() ) {
            underline = false;
            setForeground(isVisited() ? VISITED_LINK_COLOR : LINK_COLOR );
            repaint();
            onMouseExited( e );
        }
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Map rhints = (Map)(Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints")); //NOI18N
        if( rhints == null && Boolean.getBoolean("swing.aatext") ) { //NOI18N
             g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
        } else if( rhints != null ) {
            g2.addRenderingHints( rhints );
        }
        super.paintComponent(g2);

        Dimension size = getSize();
        if( hasFocus() && isEnabled() ) {
            g2.setStroke( LINK_IN_FOCUS_STROKE );
            g2.setColor( LINK_IN_FOCUS_COLOR );
            g2.drawRect( 0, 0, size.width - 1, size.height - 1 );
        }
    }

    public void focusLost(FocusEvent e) {
    }

    public void focusGained(FocusEvent e) {
        Rectangle rect = getBounds();
        rect.grow( 0, 12 );
        scrollRectToVisible( rect );
    }

    protected void onMouseExited(MouseEvent e) {
    }

    protected void onMouseEntered(MouseEvent e) {
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if( underline && isEnabled() ) {
            Font f = getFont();
            FontMetrics fm = getFontMetrics(f);
            int iconWidth = 0;
            if( null != getIcon() ) {
                iconWidth = getIcon().getIconWidth()+getIconTextGap();
            }
            int x1 = iconWidth;
            int y1 = fm.getHeight();
            int x2 = fm.stringWidth(getText()) + iconWidth;
            if( getText().length() > 0 )
                g.drawLine(x1, y1, x2, y1);
        }
    }

    protected boolean isVisited() {
        return false;
    }
}
