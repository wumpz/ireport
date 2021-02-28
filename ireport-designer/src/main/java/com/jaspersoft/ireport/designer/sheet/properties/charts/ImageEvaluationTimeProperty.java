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
package com.jaspersoft.ireport.designer.sheet.properties.charts;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;

/**
 *  Class to manage the JRDesignChart.PROPERTY_EVALUATION_TIME property
 * @author gtoffoli
 */
public final class ImageEvaluationTimeProperty extends PropertySupport
{
        private final JRDesignDataset dataset;
        private final JRDesignChart element;
        private ComboBoxPropertyEditor editor;

       
        public ImageEvaluationTimeProperty(JRDesignChart element, JRDesignDataset dataset)
        {
            // TODO: Replace WhenNoDataType with the right constant
            super( JRDesignChart.PROPERTY_EVALUATION_TIME,EvaluationTimeEnum.class, I18n.getString("Evaluation_Time"), I18n.getString("When_the_image_expression_should_be_evaluated."), true, true);
            this.element = element;
            this.dataset = dataset;
            setValue(I18n.getString("suppressCustomEditor"), Boolean.TRUE);
        }

        @Override
        public boolean isDefaultValue() {
            return element.getEvaluationTimeValue() == EvaluationTimeEnum.NOW;
        }

        @Override
        public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
            setPropertyValue(EvaluationTimeEnum.NOW);
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }

        @Override
      
        public PropertyEditor getPropertyEditor() {

            if (editor == null)
            {
                java.util.ArrayList l = new java.util.ArrayList();

                l.add(new Tag(EvaluationTimeEnum.NOW, I18n.getString("Now")));
                l.add(new Tag(EvaluationTimeEnum.REPORT, I18n.getString("Report")));
                l.add(new Tag(EvaluationTimeEnum.PAGE, I18n.getString("Page")));
                l.add(new Tag(EvaluationTimeEnum.COLUMN, I18n.getString("Column")));
                l.add(new Tag(EvaluationTimeEnum.GROUP, I18n.getString("Group")));
                l.add(new Tag(EvaluationTimeEnum.BAND, I18n.getString("Band")));
                l.add(new Tag(EvaluationTimeEnum.AUTO, I18n.getString("Auto")));

                editor = new ComboBoxPropertyEditor(false, l);
            }
            return editor;
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return element.getEvaluationTimeValue();
        }

        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (val instanceof EvaluationTimeEnum)
            {
                 setPropertyValue((EvaluationTimeEnum)val);
            }
        }

        private void setPropertyValue(EvaluationTimeEnum val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
        {
                EvaluationTimeEnum oldValue = element.getEvaluationTimeValue();
                EvaluationTimeEnum newValue = val;



                ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            element,
                            I18n.getString("EvaluationTime"), 
                            EvaluationTimeEnum.class,
                            oldValue,newValue);

                JRGroup oldGroupValue = element.getEvaluationGroup();
                JRGroup newGroupValue = null;
                if ( val == EvaluationTimeEnum.GROUP )
                {
                    if (dataset.getGroupsList().size() == 0)
                    {
                        IllegalArgumentException iae = annotateException(I18n.getString("No_groups_are_defined_to_be_used_with_this_element")); 
                        throw iae; 
                    }

                    newGroupValue = (JRGroup)dataset.getGroupsList().get(0);
                }

                element.setEvaluationTime(newValue);

                if (oldGroupValue != newGroupValue)
                {
                    ObjectPropertyUndoableEdit urobGroup =
                            new ObjectPropertyUndoableEdit(
                                element,
                                I18n.getString("EvaluationGroup"), 
                                JRGroup.class,
                                oldGroupValue,newGroupValue);
                    element.setEvaluationGroup(newGroupValue);
                    urob.concatenate(urobGroup);
                }

                // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
        }

        public IllegalArgumentException annotateException(String msg)
        {
            IllegalArgumentException iae = new IllegalArgumentException(msg); 
            ErrorManager.getDefault().annotate(iae, 
                                    ErrorManager.EXCEPTION,
                                    msg,
                                    msg, null, null); 
            return iae;
        }
}
