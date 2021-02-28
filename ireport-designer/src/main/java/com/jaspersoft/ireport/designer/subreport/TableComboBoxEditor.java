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
package com.jaspersoft.ireport.designer.subreport;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.editor.ExpObject;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.editor.ExpressionEditor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author gtoffoli
 */
public class TableComboBoxEditor extends DefaultCellEditor {
    
    public TableComboBoxEditor(Vector items) {
        super(new JComboBox(items));
//        JComboBox cb = (JComboBox)this.getComponent();
//        cb.addMouseListener(new MouseAdapter() {
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
//            
//                if (SwingUtilities.isRightMouseButton(e) || e.getClickCount() == 2)
//                {
//                    JComboBox cb = (JComboBox)getComponent();
//                    ExpressionEditor editor = new ExpressionEditor();
//                    editor.setExpressionContext( new ExpressionContext( IReportManager.getInstance().getActiveReport().getMainDesignDataset() ) );
//                    if (editor.showDialog(getComponent()) == JOptionPane.OK_OPTION)
//                    {
//                        cb.setSelectedItem(editor.getExpression());
//                    }
//                }
//            };
//        
//        });
    }
    
    

    @Override
    public Object getCellEditorValue() {

        Object obj = super.getCellEditorValue();
        if (obj instanceof String) return obj;
        ExpObject ooo = new ExpObject(obj);
        return ooo.getExpression();
    }
        
}
