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
package com.jaspersoft.ireport.jasperserver.validation;

import java.io.File;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class ElementValidationItem {

    private File originalFileName = null;
            
    private String proposedExpression = null;
    
    private String resourceName = null;
    
    private String parentFolder = null;

    private boolean storeAsLink = false;

    private String referenceUri = null;
    
    private JRDesignElement reportElement = null;
    
    public JRDesignElement getReportElement() {
        return reportElement;
    }

    public void setReportElement(JRDesignElement reportElement) {
        this.reportElement = reportElement;
    }
    
    public File getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(File originalFileName) {
        this.originalFileName = originalFileName;
    }
    
    public String toString()
    {
        if (getResourceName() != null) return this.getReportElement().toString();
        return "";
    }

    public String getProposedExpression() {
        return proposedExpression;
    }

    public void setProposedExpression(String proposedExpression) {
        this.proposedExpression = proposedExpression;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
    }

    /**
     * @return the storeAsLink
     */
    public boolean isStoreAsLink() {
        return storeAsLink;
    }

    /**
     * @param storeAsLink the storeAsLink to set
     */
    public void setStoreAsLink(boolean storeAsLink) {
        this.storeAsLink = storeAsLink;
    }

    /**
     * @return the referenceUri
     */
    public String getReferenceUri() {
        return referenceUri;
    }

    /**
     * @param referenceUri the referenceUri to set
     */
    public void setReferenceUri(String referenceUri) {
        this.referenceUri = referenceUri;
    }
    
}
