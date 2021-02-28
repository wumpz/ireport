
package com.jaspersoft.ireport.designer.sheet.properties;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import org.openide.nodes.PropertySupport;

/**
 *
 * Class to manage the JRDesignGroup.PROPERTY_START_NEW_PAGE property
 * @author gtoffoli
 */
public class StartNewPageProperty extends PropertySupport
{
        private final JRDesignGroup group;

        @SuppressWarnings("unchecked")
        public StartNewPageProperty(JRDesignGroup group)
        {
            super(JRDesignGroup.PROPERTY_START_NEW_PAGE,Boolean.class, I18n.getString("BandNode.Property.NewPage"), I18n.getString("BandNode.Property.NewPagedetail"), true, true);
            this.group = group;
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return group.isStartNewPage();
        }

        @Override
        public boolean isDefaultValue() {
            return group.isStartNewPage() == false;
        }

        @Override
        public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
            setPropertyValue(false);
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }

        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (val instanceof Boolean)
            {
                setPropertyValue((Boolean)val);
            }
        }

        private void setPropertyValue(boolean val)
        {
            Boolean oldValue = group.isStartNewPage();
            Boolean newValue = val;
            group.setStartNewPage(newValue);

            ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            group,
                            "StartNewPage", 
                            Boolean.TYPE,
                            oldValue,newValue);
                // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
}