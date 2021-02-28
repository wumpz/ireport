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

import com.jaspersoft.ireport.designer.IReportManager;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.EditorKit;
import org.openide.text.CloneableEditorSupport;

/**
 *
 * @author gtoffoli
 */
public class ExpressionEditorPane extends javax.swing.JEditorPane {

    private ExpressionContext expressionContext = null;

    public ExpressionEditorPane()
    {
        this(null);
    }

    public void removeHyperlinkEditorKitListeners()
    {
        KeyListener[] hls = this.getListeners(KeyListener.class);
        for (int i=0; i<hls.length; ++i)
        {
            if (hls[i].getClass().getName().equals("org.netbeans.modules.languages.features.HyperlinkListener"))
            {

                this.removeKeyListener(hls[i]);
            }
        }

        MouseMotionListener[] hls1 = this.getListeners(MouseMotionListener.class);
        for (int i=0; i<hls1.length; ++i)
        {
            if (hls1[i].getClass().getName().equals("org.netbeans.modules.languages.features.HyperlinkListener")) this.removeMouseMotionListener(hls1[i]);
        }

        MouseListener[] hls2 = this.getListeners(MouseListener.class);
        for (int i=0; i<hls2.length; ++i)
        {
            if (hls2[i].getClass().getName().equals("org.netbeans.modules.languages.features.HyperlinkListener")) this.removeMouseListener(hls2[i]);
        }

    }

    public ExpressionEditorPane(ExpressionContext context)
    {
        super();
        this.expressionContext = context;

        if (IReportManager.getPreferences().getBoolean("useSyntaxHighlighting", true))
        {
            EditorKit kit = CloneableEditorSupport.getEditorKit("text/jrxml-expression");
            setEditorKit(kit);
        }

        Font font = new Font("Monospaced", Font.PLAIN, IReportManager.getPreferences().getInt("editorFontSize", 12));
        setFont(font);

        // List all listeners...
        removeHyperlinkEditorKitListeners();


        addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                ExpressionContext.setGlobalContext(getExpressionContext());
                ExpressionContext.activeEditor = ExpressionEditorPane.this;
            }

            public void focusLost(FocusEvent e) {
            }
        });
    }
    
    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    public void setExpressionContext(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
    }
}
