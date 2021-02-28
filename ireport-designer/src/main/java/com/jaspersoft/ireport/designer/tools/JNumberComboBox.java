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
package com.jaspersoft.ireport.designer.tools;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
/**
 *  This class is designed for the zoom combo box.
 *  It stores a vector of predefined values (see the inner class NumberEntry),
 *  added from the program using  addEntry(String name, double value) method.
 *  The user can insert manually a value. It is interpreted using NumberFormat.
 *  (max fraction digits used:  2) 
 *  The value is valuated on focus lost....
 *  
 * @author  Giulio Toffoli
 */
public class JNumberComboBox extends JComboBox implements KeyListener, ItemListener, FocusListener {
    
    private NumberFormat numberFormat = null;
    private Vector entries;    
        
    private double value;
    
    private double minValue;
    
    private double maxValue;
    
    private boolean setting = false;
    
    /** Utility field used by event firing mechanism. */
    private javax.swing.event.EventListenerList listenerList =  null;
    
    private String postfix="";
    
    /** Creates a new instance of JNumberComboBox */
    public JNumberComboBox() {     
      
        super();
        entries = new Vector();
       this.setFocusCycleRoot(false);
       numberFormat = NumberFormat.getNumberInstance();
       numberFormat.setMinimumFractionDigits(0);
       numberFormat.setMaximumFractionDigits(2);
       this.setEditable(true);
       
       // this code solve sun Metal Bug Id  4137675 
       // Attention, this.setEditable(true); must be call before!!!
       for (int i=0; i<this.getComponentCount(); i++) {
           //System.out.println("Register to: "+ this.getComponent(i).getClass());
                this.getComponent(i).addFocusListener(this);
        }

       this.addFocusListener(this);
       this.addKeyListener(this);
       //this.addItemListener(this);
       this.addActionListener(this);      
    }
    
    @SuppressWarnings("unchecked")
    public void addEntry(String name, double value)
    {
        // If this entry name already exists, we change the value...
        Enumeration e = entries.elements();
        while (e.hasMoreElements())
        {
            NumberEntry ne = (NumberEntry)e.nextElement();
            if (ne.name.equals(name))
            {
                ne.value = value;
                return;
            }
        }
        NumberEntry entry = new NumberEntry(name,value);
        this.addItem(entry);
        this.entries.addElement(entry);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (isSetting()) return;
        //System.out.println("actionPerformed Item: "+ this.getSelectedItem() );
        super.actionPerformed(e);  

        if (this.getSelectedItem() != null)
        {
            Object obj = getSelectedItem();
            if (obj instanceof NumberEntry)
            {
                if (((NumberEntry)getSelectedItem()).value != value)
                {

                    double oldValue = this.value;
                    this.value = ((NumberEntry)getSelectedItem()).value;
                    fireValueChangedListenerValueChanged(new ValueChangedEvent((JComponent)this, oldValue, this.value));

                }
                
            }
            else
            {
                String s = ""+getSelectedItem();
                try {
                    Number nb = numberFormat.parse(s);
                    double oldValue = this.value;
                    this.value = nb.doubleValue();
                    fireValueChangedListenerValueChanged(new ValueChangedEvent((JComponent)this, oldValue, this.value));                    
                } catch (Exception ex)
                {
                    
                }
             }
            this.setSetting(true);
            this.setSelectedItem( numberFormat.format( value ) +getPostfix());  
            this.setSetting(false);
        }
    }
    
    /** Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ENTER)
        {
            e.consume();
               FocusManager.getCurrentManager().focusNextComponent();
               
        }
    }
    
    /** Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     */
    public void keyReleased(KeyEvent e) {
    }
    
    /** Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     */
    public void keyTyped(KeyEvent e) {
    }
    
    /** Invoked when a component gains the keyboard focus.
     *
     */
    public void focusGained(FocusEvent e) {
    }
    
    /** Invoked when a component loses the keyboard focus.
     *
     */
    public void focusLost(FocusEvent e) {
       acceptResultNow();
    }
    
    public void acceptResultNow()
    {
         try {
            
                
                Number num = numberFormat.parse( ""+ this.getSelectedItem());
           
                 if (num.doubleValue() != value)
                {
                    double oldValue = this.value;
                    this.value = num.doubleValue(); 
                    fireValueChangedListenerValueChanged(new ValueChangedEvent(this, oldValue, this.value));
                }
            } catch (Exception ex)
            { 
                //System.out.println(ex.getMessage());
                fireValueChangedListenerValueChanged(new ValueChangedEvent(this, this.value, this.value));
            }
            boolean setting = isSetting();
            this.setSetting(true);
            this.setSelectedItem( numberFormat.format( value )+getPostfix() );
            this.setSetting(setting);
    }
    
    /** Getter for property minValue.
     * @return Value of property minValue.
     *
     */
    public double getMinValue() {
        return minValue;
    }
    
    /** Setter for property minValue.
     * @param minValue New value of property minValue.
     *
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }
    
    /** Getter for property maxValue.
     * @return Value of property maxValue.
     *
     */
    public double getMaxValue() {
        return maxValue;
    }
    
    /** Setter for property maxValue.
     * @param maxValue New value of property maxValue.
     *
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
    
    /** Getter for property value.
     * @return Value of property value.
     *
     */
    public double getValue() {
        return value;
    }
    
    /** Setter for property value.
     * @param value New value of property value.
     *
     */
    public void setValue(double value) {    
        if (this.value != value)
        {
            double oldValue = this.value;
            this.value = value;
            fireValueChangedListenerValueChanged(new ValueChangedEvent(this, oldValue, this.value));
        }
        boolean setting = isSetting();
        this.setSetting(true);
        this.setSelectedItem( numberFormat.format( this.value )+getPostfix() );
        this.setSetting(setting);
    }
    
    /** Registers ValueChangedListener to receive events.
     * @param listener The listener to register.
     *
     */
    public synchronized void addValueChangedListener(ValueChangedListener listener) {
        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add(ValueChangedListener.class, listener);
    }
    
    /** Removes ValueChangedListener from the list of listeners.
     * @param listener The listener to remove.
     *
     */
    public synchronized void removeValueChangedListener(ValueChangedListener listener) {
        listenerList.remove(ValueChangedListener.class, listener);
    }
    
    /** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    private void fireValueChangedListenerValueChanged(ValueChangedEvent event) {
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ValueChangedListener.class) {
                ((ValueChangedListener)listeners[i+1]).valueChanged(event);
            }
        }
    }
    
    /** Getter for property postfix.
     * @return Value of property postfix.
     *
     */
    public java.lang.String getPostfix() {
        return postfix;
    }
    
    /** Setter for property postfix.
     * @param postfix New value of property postfix.
     *
     */
    public void setPostfix(java.lang.String postfix) {
        this.postfix = postfix;
    }

    public boolean isSetting() {
        return setting;
    }

    public void setSetting(boolean setting) {
        this.setting = setting;
    }

    public void itemStateChanged(ItemEvent e) {
        
        if (isSetting()) return;
        if (e.getStateChange() == e.SELECTED)
        {
            actionPerformed(null);
        }
    }
    
    public static final class NumberEntry {
        public String name="";
        public double value=0.0;

        @Override
        public String toString()
        {
            return name;
        }

        public NumberEntry( String name, double value)
        {
            this.name  = name;
            this.value = value;
        }
    }
}

