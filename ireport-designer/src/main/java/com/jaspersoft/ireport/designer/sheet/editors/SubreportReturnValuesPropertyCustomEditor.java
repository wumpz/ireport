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
package com.jaspersoft.ireport.designer.sheet.editors;

import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.tools.SubreportReturnValuesPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.util.List;
import org.openide.explorer.propertysheet.PropertyEnv;

/**
 *
 * @author gtoffoli
 */
public class SubreportReturnValuesPropertyCustomEditor extends SubreportReturnValuesPanel implements PropertyChangeListener {

    public SubreportReturnValuesPropertyCustomEditor()
    {
        super();
    }

    
    private PropertyEnv env;
    private PropertyEditor editor;


    //enh 29294, provide one line editor on request
    /** Create a StringCustomEditor.
     * @param value the initial value for the string
     * @param editable whether to show the editor in read only or read-write mode (NOT USED)
     * @param oneline whether the text component should be a single-line or multi-line component (NOT USED)
     * @param instructions any instructions that should be displayed (NOT USED)
     */
    SubreportReturnValuesPropertyCustomEditor (List value, PropertyEditor editor, PropertyEnv env) {
        
        super();
        
        this.editor = editor;
        this.env = env;
        this.env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        this.env.addPropertyChangeListener(this);
        
        ExpressionContext ec = (ExpressionContext)this.env.getFeatureDescriptor().getValue("expressionContext");
        if (ec != null) setExpressionContext(ec);

        this.setReturnValues(value);
    }
    
    /**
    * @return Returns the property value that is result of the CustomPropertyEditor.
    * @exception InvalidStateException when the custom property editor does not represent valid property value
    *            (and thus it should not be set)
    */
    private Object getPropertyValue () throws IllegalStateException {
        return getReturnValues();
    }



    public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {
            editor.setValue(getPropertyValue());
        }
    }

}

