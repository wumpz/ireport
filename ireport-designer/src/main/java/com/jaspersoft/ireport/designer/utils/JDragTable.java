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
package com.jaspersoft.ireport.designer.utils;
import com.jaspersoft.ireport.designer.dnd.TransferableObject;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import org.jdesktop.swingx.decorator.SortController;
import org.jdesktop.swingx.decorator.SortOrder;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 *
 * @author  Administrator
 */
public class JDragTable extends org.jdesktop.swingx.JXTable implements DragGestureListener, DragSourceListener {    
   
     /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList =  null;
    
   public JDragTable() {
        
       super();
       
        DragSource dragSource = DragSource.getDefaultDragSource();

        // creating the recognizer is all that's necessary - it
        // does not need to be manipulated after creation
        dragSource.createDefaultDragGestureRecognizer(
         this, // component where drag originates
         DnDConstants.ACTION_COPY, // actions
         this); // drag gesture listener

        setColumnControlVisible(true); 
        
      }
      
       public void dragGestureRecognized(DragGestureEvent e) {
         // drag anything ...
         
         TransferableObject  to =   new TransferableObject(this.getValueAt(this.getSelectedRow(), this.getSelectedColumn() ));
         
         try{
         if (to != null)
         e.startDrag(DragSource.DefaultCopyDrop , // cursor
            to); //, // transferable
            //this); // drag source listener
         } catch (Exception ex) {

            ex.printStackTrace();

         }

      }

      public void dragDropEnd(DragSourceDropEvent e) {}
      public void dragEnter(DragSourceDragEvent e) {}
      public void dragExit(DragSourceEvent e) {}
      public void dragOver(DragSourceDragEvent e) {}
      public void dropActionChanged(DragSourceDragEvent e) {}

      
    @Override
    public void toggleSortOrder(int columnIndex) {
        super.toggleSortOrder(columnIndex);
        
        int index = convertColumnIndexToModel(columnIndex);
        SortOrder so = SortOrder.UNSORTED;
        SortController sortController = getSortController();
        if (sortController != null)
        {
            so = sortController.getSortOrder(index);
        }

        fireSortChangedListenerSortChanged(new SortChangedEvent(this, index, so) );
    }

    @Override
    public void toggleSortOrder(Object identifier) {
        super.toggleSortOrder(identifier);
        
        TableColumnExt columnExt = getColumnExt(identifier);
        int index = columnExt.getModelIndex();
        
        SortOrder so = SortOrder.UNSORTED;
        SortController sortController = getSortController();
        if (sortController != null)
        {
            so = sortController.getSortOrder(index);
        }
        
        fireSortChangedListenerSortChanged(new SortChangedEvent(this, index, so ) );
    }

    @Override
    public void setSortOrder(Object identifier, SortOrder arg1) {
        super.setSortOrder(identifier, arg1);
        
        TableColumnExt columnExt = getColumnExt(identifier);
        int index = columnExt.getModelIndex();
        
        SortOrder so = SortOrder.UNSORTED;
        SortController sortController = getSortController();
        if (sortController != null)
        {
            so = sortController.getSortOrder(index);
        }
        
        fireSortChangedListenerSortChanged(new SortChangedEvent(this, index, so ) );
    }
    
    
    @Override
    public void resetSortOrder() {
        super.resetSortOrder();
        
        fireSortChangedListenerSortChanged(new SortChangedEvent(this, -1, SortOrder.UNSORTED ) );
    }

      
     

    /**
     * Registers TabPaneChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addSortChangedListener(SortChangedListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (SortChangedListener.class, listener);
    }

    /**
     * Removes TabPaneChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeSortChangedListener(SortChangedListener listener) {

        listenerList.remove (SortChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    private void fireSortChangedListenerSortChanged(SortChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==SortChangedListener.class) {
                ((SortChangedListener)listeners[i+1]).sortChanged (event);
            }
        }
    }
}
