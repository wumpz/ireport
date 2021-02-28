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
package com.jaspersoft.ireport.designer.undo;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.openide.awt.UndoRedo;
import org.openide.util.Mutex;
import org.openide.util.MutexException;

/**
 *
 * @author gtoffoli
 * UndoRedoManager based on the undo manager of the FormModel in NetBeans
 */
public class UndoRedoManager extends UndoRedo.Manager {
    
        private Mutex.ExceptionAction<Object> runUndo = new Mutex.ExceptionAction<Object>() {
            public Object run() throws Exception {
                superUndo();
                return null;
            }
        };
        private Mutex.ExceptionAction<Object> runRedo = new Mutex.ExceptionAction<Object>() {
            public Object run() throws Exception {
                superRedo();
                return null;
            }
        };

        public void superUndo() throws CannotUndoException {
            super.undo();
        }
        public void superRedo() throws CannotRedoException {
            super.redo();
        }

        @Override
        public void undo() throws CannotUndoException {
            if (java.awt.EventQueue.isDispatchThread()) {
                superUndo();
            }
            else {
                try {
                    Mutex.EVENT.readAccess(runUndo);
                }
                catch (MutexException ex) {
                    Exception e = ex.getException();
                    if (e instanceof CannotUndoException)
                        throw (CannotUndoException) e;
                    else // should not happen, ignore
                        e.printStackTrace();
                }
            }
        }

        @Override
        public void redo() throws CannotRedoException {
            if (java.awt.EventQueue.isDispatchThread()) {
                superRedo();
            }
            else {
                try {
                    Mutex.EVENT.readAccess(runRedo);
                }
                catch (MutexException ex) {
                    Exception e = ex.getException();
                    if (e instanceof CannotRedoException)
                        throw (CannotRedoException) e;
                    else // should not happen, ignore
                        e.printStackTrace();
                }
            }
        }
        
        
    }