/*
 * Copyright (C) 2005 - 2008 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * FileEntry.java
 * 
 * Created on 20 maggio 2004, 8.16
 *
 */

package com.jaspersoft.ireport.addons.transformer.tool;

import java.io.*;
/**
 *
 * @author  Administrator
 */
public class FileEntry {
    
    public static final int STATUS_NOT_TRANSFORMED = 1;
    public static final int STATUS_TRANSFORMED = 2;
    public static final int STATUS_ERROR_TRANSFORMING = 3;
    public static final int STATUS_TRANSFORMING = 5;
    
    private File file = null;
    private int status = 0;
    private String message = "";
    private String shortErrorMessage = "";
    private String jasper_version = "";
    
    /** Creates a new instance of FileEntry */
    public FileEntry() {
    }
    
    
    /** Getter for property message.
     * @return Value of property message.
     *
     */
    public java.lang.String getMessage() {
        return message;
    }
    
    /** Setter for property message.
     * @param message New value of property message.
     *
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public int getStatus() {
        return status;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public String decodeStatus( int status ) {
        // Decode the status...
        switch (status)
        {
            case STATUS_NOT_TRANSFORMED: return "Not transformed";
            case STATUS_TRANSFORMED: return "Transformed";
            case STATUS_ERROR_TRANSFORMING: return (shortErrorMessage == null || shortErrorMessage.trim().length() == 0) ? "Error transforming" : shortErrorMessage;
            case STATUS_TRANSFORMING: return "Transforming...";
        }
        return ""+status;
    }
    
    /** Setter for property status.
     * @param status New value of property status.
     *
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
    /** Getter for property file.
     * @return Value of property file.
     *
     */
    public File getFile() {
        return file;
    }
    
    /** Setter for property file.
     * @param file New value of property file.
     *
     */
    public void setFile(File file) {
        this.file = file;
    }
    
    @Override
    public String toString()
    {
        if (file == null) return "";
        try {
        return file.getCanonicalPath();
        }catch (Exception ex) {}
        return "";
    }
    
    /** Getter for property jasper_version.
     * @return Value of property jasper_version.
     *
     */
    public java.lang.String getJasper_version() {
        return jasper_version;
    }
    
    /** Setter for property jasper_version.
     * @param jasper_version New value of property jasper_version.
     *
     */
    public void setJasper_version(java.lang.String jasper_version) {
        this.jasper_version = jasper_version;
    }

    /**
     * @return the shortErrorMessage
     */
    public String getShortErrorMessage() {
        return shortErrorMessage;
    }

    /**
     * @param shortErrorMessage the shortErrorMessage to set
     */
    public void setShortErrorMessage(String shortErrorMessage) {
        this.shortErrorMessage = shortErrorMessage;
    }


    
}
