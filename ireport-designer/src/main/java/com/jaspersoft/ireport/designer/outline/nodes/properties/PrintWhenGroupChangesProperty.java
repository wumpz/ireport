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
package com.jaspersoft.ireport.designer.outline.nodes.properties;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import org.openide.nodes.PropertySupport;
import org.openide.util.WeakListeners;

    
/**
 *  Class to manage the JRDesignVariable.PROPERTY_INCREMENT_GROUP property
 */
public final class PrintWhenGroupChangesProperty extends PropertySupport implements PropertyChangeListener
{
        private JRDesignElement element = null;
        private JRDesignDataset dataset = null;
        private ComboBoxPropertyEditor editor;

        @SuppressWarnings("unchecked")
        public PrintWhenGroupChangesProperty(JRDesignElement element, JRDesignDataset dataset)
        {
            // TODO: Replace WhenNoDataType with the right constant
            super( JRDesignElement.PROPERTY_PRINT_WHEN_GROUP_CHANGES,JRGroup.class,
                    "Print When Group Changes",
                    "Print this element only when the specified group changes", true, true);
            this.element = element;
            this.dataset = dataset;
            setValue("suppressCustomEditor", Boolean.TRUE);
            
            dataset.getEventSupport().addPropertyChangeListener(WeakListeners.propertyChange(this, dataset.getEventSupport()));
        }

        @Override
        @SuppressWarnings("unchecked")
        public PropertyEditor getPropertyEditor() {

            if (editor == null)
            {
                editor = new ComboBoxPropertyEditor(false, getListOfTags());
            }
            return editor;
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return element.getPrintWhenGroupChanges() == null ? "" : element.getPrintWhenGroupChanges();
        }

        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (val == null || val instanceof JRGroup)
            {
                JRGroup oldValue = element.getPrintWhenGroupChanges();
                JRGroup newValue = (JRGroup)val;
                element.setPrintWhenGroupChanges(newValue);

                ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            element,
                            "PrintWhenGroupChanges", 
                            JRGroup.class,
                            oldValue,newValue);
                // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
        }
        
        public void propertyChange(PropertyChangeEvent evt) {
            if (editor == null) return;
            if (evt.getPropertyName() == null) return;
            if (evt.getPropertyName().equals( JRDesignDataset.PROPERTY_GROUPS) ||
                evt.getPropertyName().equals( JRDesignGroup.PROPERTY_NAME))
            {
                editor.setTagValues(getListOfTags());
            }
        }

        private java.util.ArrayList getListOfTags()
        {
            java.util.ArrayList l = new java.util.ArrayList();
            List groups = dataset.getGroupsList();
            l.add(new Tag( null , ""));
            for (int i=0; i<groups.size(); ++i)
            {
                JRDesignGroup group = (JRDesignGroup)groups.get(i);
                l.add(new Tag( group , group.getName()));
                group.getEventSupport().addPropertyChangeListener(WeakListeners.propertyChange(this, group.getEventSupport()));
            }
            return l;
        }
}
