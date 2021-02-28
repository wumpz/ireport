package com.jaspersoft.ireport.designer.compiler.prompt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultFormatterFactory;
import net.sf.jasperreports.types.date.DateRangeBuilder;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXDatePickerFormatter;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.calendar.DateSpan;
import org.jdesktop.swingx.calendar.JXMonthView;

/**
 *
 * @author gtoffoli
 */


public class DateRangeDatePicker extends JComponent {
    
    public DateRangeDatePicker()
    {
        this(System.currentTimeMillis());

    }
    
    
        /**
     * Replaces the currently installed formatter and factory used by the
     * editor.
     *
     * @param formats The date formats to use.
     */
    public void setFormats(DateFormat[] formats) {
        getEditor().setFormatterFactory(new DefaultFormatterFactory(
                                new DateRangeFormatter(formats)));
    }
    
    
    
    
    
    
    /*
 * $Id: JXDatePicker.java,v 1.7 2005/10/10 18:01:54 rbair Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */



/**
 * A component that combines a button, an editable field and a JXMonthView
 * component.  The user can select a date from the calendar component, which
 * appears when the button is pressed.  The selection from the calendar
 * component will be displayed in editable field.  Values may also be modified
 * manually by entering a date into the editable field using one of the
 * supported date formats.
 *
 * @author Joshua Outwater
 */

    /** The editable date field that displays the date */
    private JFormattedTextField _dateField;

    /**
     * Popup that displays the month view with controls for
     * traversing/selecting dates.
     */
    private JXDatePickerPopup _popup;
    private JPanel _linkPanel;
    private long _linkDate;
    private MessageFormat _linkFormat;
    private JButton _popupButton;
    private int _popupButtonWidth = 20;
    private JXMonthView _monthView;
    private Handler _handler;
    private String _actionCommand = "selectionChanged";

   

    /**
     * Create a new date picker using the specified time as the initial
     * selection and the default abstract formatter
     * <code>JXDatePickerFormatter</code>.
     *
     * @param millis initial time in milliseconds
     */
    public DateRangeDatePicker(long millis) {
        _monthView = new JXMonthView();
        _monthView.setTraversable(true);

        _dateField = new JFormattedTextField(new JXDatePickerFormatter());
        _dateField.setName("dateField");
        _dateField.setBorder(null);
        
        _handler = new Handler();
        _popupButton = new JButton();
        _popupButton.setName("popupButton");
        _popupButton.setRolloverEnabled(false);
        _popupButton.addMouseListener(_handler);
        _popupButton.addMouseMotionListener(_handler);
        _popupButton.setFocusable(false);

        KeyStroke enterKey =
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);

        InputMap inputMap = _dateField.getInputMap(JComponent.WHEN_FOCUSED);
        inputMap.put(enterKey, "COMMIT_EDIT");

        ActionMap actionMap = _dateField.getActionMap();
        actionMap.put("COMMIT_EDIT", new CommitEditAction());

        KeyStroke spaceKey =
            KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false);

        inputMap = _popupButton.getInputMap(JComponent.WHEN_FOCUSED);
        inputMap.put(spaceKey, "TOGGLE_POPUP");

        actionMap = _popupButton.getActionMap();
        actionMap.put("TOGGLE_POPUP", new TogglePopupAction());

        add(_dateField);
        add(_popupButton);

        updateUI();
        
        _linkDate = System.currentTimeMillis();
        _linkPanel = new TodayPanel();
        
        _dateField.setValue(new Date(millis));
        
        
        getEditor().setFormatterFactory(new DefaultFormatterFactory(
                                new DateRangeFormatter()));
    
        
        for (int i=0; i<getComponentCount(); ++i)
        {
            ((JComponent)getComponent(i)).getInputMap().remove(spaceKey);
            ((JComponent)getComponent(i)).getInputMap(JComponent.WHEN_FOCUSED).remove(spaceKey);
            
            //((JComponent)getComponent(i)).getActionMap().remove("TOGGLE_POPUP");
        }
        
        
        getInputMap().remove(spaceKey);
        getInputMap(JComponent.WHEN_FOCUSED).remove(spaceKey);
        //getActionMap().remove("TOGGLE_POPUP");
        
        spaceKey =
            KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, java.awt.event.InputEvent.CTRL_MASK, false);
        
    }

    /**
     * Resets the UI property to a value from the current look and feel.
     */
    public void updateUI() {
        int cols = UIManager.getInt("JXDatePicker.numColumns");
        if (cols == -1) {
            cols = 10;
        }
        _dateField.setColumns(cols);

        String str = UIManager.getString("JXDatePicker.arrowDown.tooltip");
        if (str == null) {
            str = "Show Calendar";
        }
        _popupButton.setToolTipText(str);

        Icon icon = UIManager.getIcon("JXDatePicker.arrowDown.image");
        if (icon == null) {
            icon = (Icon)UIManager.get("Tree.expandedIcon");
        }
        _popupButton.setIcon(icon);

        Border border = UIManager.getBorder("JXDatePicker.border");
        if (border == null) {
            border = BorderFactory.createCompoundBorder(
                    LineBorder.createGrayLineBorder(),
                    BorderFactory.createEmptyBorder(3, 3, 3, 3));
        }
        _dateField.setBorder(border);

        String formatString = UIManager.getString("JXDatePicker.linkFormat");
        if (formatString == null) {
            formatString = "Today is {0,date, dd MMMM yyyy}";
        }
        _linkFormat = new MessageFormat(formatString);
    }

    /**
     * Replaces the currently installed formatter and factory used by the
     * editor.  These string formats are defined by the
     * <code>java.text.SimpleDateFormat</code> class.
     *
     * @param formats The string formats to use.
     * @see java.text.SimpleDateFormat
     */
    public void setFormats(String[] formats) {
        DateFormat[] dateFormats = new DateFormat[formats.length];
        for (int counter = formats.length - 1; counter >= 0; counter--) {
            dateFormats[counter] = new SimpleDateFormat(formats[counter]);
        }
        setFormats(dateFormats);
    }

    /**
     * Returns an array of the formats used by the installed formatter
     * if it is a subclass of <code>JXDatePickerFormatter<code>.
     * <code>javax.swing.JFormattedTextField.AbstractFormatter</code>
     * and <code>javax.swing.text.DefaultFormatter</code> do not have
     * support for accessing the formats used.
     *
     * @return array of formats or null if unavailable.
     */
    public DateFormat[] getFormats() {
        // Dig this out from the factory, if possible, otherwise return null.
        AbstractFormatterFactory factory = _dateField.getFormatterFactory();
        if (factory != null) {
            AbstractFormatter formatter = factory.getFormatter(_dateField);
            if (formatter instanceof JXDatePickerFormatter) {
                return ((JXDatePickerFormatter)formatter).getFormats();
            }
        }
        return null;
    }

    /**
     * Set the currently selected date.
     *
     * @param date date
     */
    public void setDate(Date date) {
        _dateField.setValue(date);
    }
    
    
    /**
     * Set the currently selected date.
     *
     * @param date date
     */
    public void setDateRangeExpression(String value) {
        _dateField.setValue(value);
    }
    
    
    
    /**
     * Set the currently selected date.
     *
     * @param date date
     */
    public String getDateRangeExpression() {
        if (_dateField.getValue() instanceof String)
        {
            return (String)_dateField.getValue();
        }
        return null;
    }

    /**
     * Set the currently selected date.
     *
     * @param millis milliseconds
     */
    public void setDateInMillis(long millis) {
        _dateField.setValue(new Date(millis));
    }

    /**
     * Returns the currently selected date.
     *
     * @return Date
     */
    public Date getDate() {
        return (Date)getFieldDate();
    }

    /**
     * Returns the currently selected date in milliseconds.
     *
     * @return the date in milliseconds
     */
    public long getDateInMillis() {
        return ((Date)getFieldDate()).getTime();
    }

    /**
     * Return the <code>JXMonthView</code> used in the popup to
     * select dates from.
     *
     * @return the month view component
     */
    public JXMonthView getMonthView() {
        return _monthView;
    }

    public void setMonthView(JXMonthView monthView) {
        _monthView = monthView;
        _popup = null;
    }
    
    /**
     * Set the date the link will use and the string defining a MessageFormat
     * to format the link.  If no valid date is in the editor when the popup
     * is displayed the popup will focus on the month the linkDate is in.
     *
     * @param linkDate Date in milliseconds
     * @param linkFormatString String used to format the link
     * @see java.text.MessageFormat
     */
    public void setLinkDate(long linkDate, String linkFormatString) {
        _linkDate = linkDate;
        _linkFormat = new MessageFormat(linkFormatString);
    }
    
    /**
     * Return the panel that is used at the bottom of the popup.  The default
     * implementation shows a link that displays the current month.
     *
     * @return The currently installed link panel
     */
    public JPanel getLinkPanel() {
        return _linkPanel;
    }
    
    /**
     * Set the panel that will be used at the bottom of the popup.
     *
     * @param linkPanel The new panel to install in the popup
     */
    public void setLinkPanel(JPanel linkPanel) {
        // If the popup is null we haven't shown it yet.
        if (_popup != null) {
            _popup.remove(_linkPanel);
            _popup.add(linkPanel, BorderLayout.SOUTH);
        }
        _linkPanel = linkPanel;
    }
    
    /**
     * Returns the formatted text field used to edit the date selection.
     *
     * @return the formatted text field
     */
    public JFormattedTextField getEditor() {
        return _dateField;
    }

    /**
     * Returns true if the current value being edited is valid.
     *
     * @return true if the current value being edited is valid.
     */
    public boolean isEditValid() {
        return _dateField.isEditValid();
    }

    /**
     * Forces the current value to be taken from the AbstractFormatter and
     * set as the current value. This has no effect if there is no current
     * AbstractFormatter installed.
     */
    public void commitEdit() throws ParseException {
        _dateField.commitEdit();
    }

    /**
     * Enables or disables the date picker and all its subcomponents.
     *
     * @param value true to enable, false to disable
     */
    public void setEnabled(boolean value) {
        if (isEnabled() == value) {
            return;
        }

        super.setEnabled(value);
        _dateField.setEnabled(value);
        _popupButton.setEnabled(value);
    }

    /**
     * Returns the string currently used to identiy fired ActionEvents.
     *
     * @return String The string used for identifying ActionEvents.
     */
    public String getActionCommand() {
        return _actionCommand;
    }

    /**
     * Sets the string used to identify fired ActionEvents.
     *
     * @param actionCommand The string used for identifying ActionEvents.
     */
    public void setActionCommand(String actionCommand) {
        _actionCommand = actionCommand;
    }

    /**
     * Adds an ActionListener.
     * <p>
     * The ActionListener will receive an ActionEvent when a selection has
     * been made.
     *
     * @param l The ActionListener that is to be notified
     */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    /**
     * Removes an ActionListener.
     *
     * @param l The action listener to remove.
     */
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    /**
     * Fires an ActionEvent to all listeners.
     */
    protected void fireActionPerformed() {
        Object[] listeners = listenerList.getListenerList();
        ActionEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -=2) {
            if (listeners[i] == ActionListener.class) {
                if (e == null) {
                    e = new ActionEvent(DateRangeDatePicker.this,
                            ActionEvent.ACTION_PERFORMED,
                            _actionCommand);
                }
                ((ActionListener)listeners[i + 1]).actionPerformed(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void doLayout() {
        int width = getWidth();
        int height = getHeight();

        Insets insets = getInsets();
        _dateField.setBounds(insets.left,
                insets.bottom,
                width - _popupButtonWidth,
                height);
        _popupButton.setBounds(width - _popupButtonWidth + insets.left,
                insets.bottom,
                _popupButtonWidth,
                height);
    }

    /**
     * {@inheritDoc}
     */
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * {@inheritDoc}
     */
    public Dimension getPreferredSize() {
        Dimension dim = _dateField.getPreferredSize();
        dim.width += _popupButton.getPreferredSize().width;
        Insets insets = getInsets();
        dim.width += insets.left + insets.right;
        dim.height += insets.top + insets.bottom;
        return dim;
    }

    /**
     * Action used to commit the current value in the JFormattedTextField.
     * This action is used by the keyboard bindings.
     */
    private class TogglePopupAction extends AbstractAction {
        public TogglePopupAction() {
            super("TogglePopup");
        }

        public void actionPerformed(ActionEvent ev) {
            _handler.toggleShowPopup();
        }
    }

    /**
     * Action used to commit the current value in the JFormattedTextField.
     * This action is used by the keyboard bindings.
     */
    private class CommitEditAction extends AbstractAction {
        public CommitEditAction() {
            super("CommitEditPopup");
        }

        public void actionPerformed(ActionEvent ev) {
            try {
                // Commit the current value.
                _dateField.commitEdit();

                // Reformat the value according to the formatter.
                _dateField.setValue(_dateField.getValue());
                fireActionPerformed();
            } catch (java.text.ParseException ex) {
            }
        }
    }

    private class Handler implements MouseListener, MouseMotionListener {
        private boolean _forwardReleaseEvent = false;

        public void mouseClicked(MouseEvent ev) {
        }

        public void mousePressed(MouseEvent ev) {
            if (!isEnabled()) {
                return;
            }

            if (_dateField.isEditValid()) {
                try {
                    _dateField.commitEdit();
                } catch (java.text.ParseException ex) {
                }
            }
            toggleShowPopup();
        }

        public void mouseReleased(MouseEvent ev) {
            // Retarget mouse event to the month view.
            if (_forwardReleaseEvent) {
                ev = SwingUtilities.convertMouseEvent(_popupButton, ev,
                        _monthView);
                _monthView.dispatchEvent(ev);
                _forwardReleaseEvent = false;
            }
        }

        public void mouseEntered(MouseEvent ev) {
        }

        public void mouseExited(MouseEvent ev) {
        }

        public void mouseDragged(MouseEvent ev) {
            _forwardReleaseEvent = true;

            if (!_popup.isShowing()) {
                return;
            }

            // Retarget mouse event to the month view.
            ev = SwingUtilities.convertMouseEvent(_popupButton, ev, _monthView);
            _monthView.dispatchEvent(ev);
        }

        public void mouseMoved(MouseEvent ev) {
        }

        
        public void toggleShowPopup() {
            if (_popup == null) {
                _popup = new JXDatePickerPopup();
            }
            if (!_popup.isVisible()) {
                
                    DateSpan span =
                            new DateSpan((java.util.Date)getFieldDate(),
                                    (java.util.Date)getFieldDate());
                    _monthView.setSelectedDateSpan(span);
                    _monthView.ensureDateVisible(
                            ((Date)getFieldDate()).getTime());
                    _popup.show(DateRangeDatePicker.this,
                            0, DateRangeDatePicker.this.getHeight());
                
            } else {
                _popup.setVisible(false);
            }
        }
    }

    
    protected Date getFieldDate()
    {
        if (_dateField.getValue() == null) {
            _dateField.setValue(new Date(_linkDate));
            return (Date)_dateField.getValue();
        }
        
        if (_dateField.getValue() instanceof Date)
        {
            return (Date) _dateField.getValue();
        }
        
        if (_dateField.getValue() instanceof String)
        {
            DateRangeBuilder b = new DateRangeBuilder(""+_dateField.getValue());
            return b.toDateRange().getStart();
        }
        
        
        return null;
        
    }
    
    /**
     * Popup component that shows a JXMonthView component along with controlling
     * buttons to allow traversal of the months.  Upon selection of a date the
     * popup will automatically hide itself and enter the selection into the
     * editable field of the JXDatePicker.
     */
    protected class JXDatePickerPopup extends JPopupMenu
            implements ActionListener {

        public JXDatePickerPopup() {
            _monthView.addActionListener(this);

            setLayout(new BorderLayout());
            add(_monthView, BorderLayout.CENTER);
            if (_linkPanel != null) {
                add(_linkPanel, BorderLayout.SOUTH);
            }
        }

        public void actionPerformed(ActionEvent ev) {
            String command = ev.getActionCommand();
            if (_monthView.COMMIT_KEY.equals(command)) {
                _dateField.setValue(_monthView.getSelectedDate());
                _popup.setVisible(false);
                fireActionPerformed();
            }
        }
    }
        
    private final class TodayPanel extends JXPanel {
        TodayPanel() {
            super(new FlowLayout());
            //setDrawGradient(true);
            //setGradientPaint(new GradientPaint(0, 0, new Color(238, 238, 238), 0, 1, Color.WHITE));
            JXHyperlink todayLink = new JXHyperlink(new TodayAction());
            Color textColor = new Color(16, 66, 104);
            todayLink.setUnclickedColor(textColor);
            todayLink.setClickedColor(textColor);
            add(todayLink);
        }
        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(new Color(187, 187, 187));
            g.drawLine(0, 0, getWidth(), 0);
            g.setColor(new Color(221, 221, 221));
            g.drawLine(0, 1, getWidth(), 1);
        }
        
        private final class TodayAction extends AbstractAction {
            TodayAction() {
                super(_linkFormat.format(new Object[] { new Date(_linkDate) }));
            }
            
            public void actionPerformed(ActionEvent ae) {
                DateSpan span = new DateSpan(_linkDate, _linkDate);
                _monthView.ensureDateVisible(span.getStart());
            }
        }
    }        
    
    
    
}
