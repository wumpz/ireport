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
package com.jaspersoft.ireport.designer.outline.nodes.properties.charts;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import org.openide.nodes.PropertySupport;

    
/**
 *  Class to manage the JRDesignChart.PROPERTY_EVALUATION_TIME property
 */
public final class EvaluationGroupProperty extends PropertySupport
{
        private final JRDesignDataset dataset;
        private final JRDesignChart element;
        private ComboBoxPropertyEditor editor;

        @SuppressWarnings("unchecked")
        public EvaluationGroupProperty(JRDesignChart element, JRDesignDataset dataset)
        {
            // TODO: Replace WhenNoDataType with the right constant
            super( JRDesignChart.PROPERTY_EVALUATION_GROUP,JRGroup.class, "Evaluation group", "Evaluate the image expression when the specified group changes", true, true);
            this.element = element;
            this.dataset = dataset;
            setValue("suppressCustomEditor", Boolean.TRUE);
        }

        @Override
        public boolean canWrite() {
            return element.getEvaluationTimeValue() == EvaluationTimeEnum.GROUP;
        }


        @Override
        @SuppressWarnings("unchecked")
        public PropertyEditor getPropertyEditor() {

            if (editor == null)
            {
                java.util.ArrayList l = new java.util.ArrayList();

                List groups = dataset.getGroupsList();
                for (int i=0; i<groups.size(); ++i)
                {
                    JRGroup group = (JRGroup)groups.get(i);
                    l.add(new Tag( group , group.getName()));
                }
                editor = new ComboBoxPropertyEditor(false, l);
            }
            return editor;
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return element.getEvaluationGroup() == null ? "" :  element.getEvaluationGroup();
        }

        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (val instanceof JRGroup)
            {
                JRGroup oldValue = element.getEvaluationGroup();
                JRGroup newValue = (JRGroup)val;
                element.setEvaluationGroup(newValue);

                ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            element,
                            "EvaluationGroup", 
                            JRGroup.class,
                            oldValue,newValue);
                // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
        }
}
