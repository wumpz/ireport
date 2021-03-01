/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.jasperserver;

import java.io.File;

/**
 * This listener is used when a JRXML is replaced with the current JRXML.
 * The event is reised ONLY when the Replace with current file menu item is used.
 * This allows plugins like domains to change the JRXML or perform other
 * operations before actually save the file on the server.
 *
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 * @version $Id: FileResourceUpdatingListener.java $
 */
public interface FileResourceUpdatingListener {

    /**
     * This method is called before send the file and update the repository.
     * If the file belongs to a ReportUnit, the report unit is provided.
     *
     * @param repositoryFile - The resource that will be updated.
     * @param reportUnit - The report unit to which the resource belongs too. If null the resource does not belong to any report unit.
     * @param file - the file that is going to be updated
     * 
     */
    public void resourceWillBeUpdated(RepositoryFile repositoryFile, RepositoryReportUnit reportUnit, File file) throws Exception;

    /**
     * This method is called when the file has been sent and updated on the server.
     * 
     * @param repositoryFile - The updated resource
     * @param reportUnit - The report unit to which the resource belongs too. If null the resource does not belong to any report unit.
     * @param file - the file that is going to be updated
     */
    public void resourceUpdated(RepositoryFile rf, RepositoryReportUnit reportUnit, File file) throws Exception;

}
