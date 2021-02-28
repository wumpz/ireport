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
package com.jaspersoft.ireport.designer.data;

import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignField;

/**
 *
 * @author gtoffoli
 */
public interface WizardFieldsProvider {

    /**
     * Return the name of the language to use
     * Null if a language is not required.
     */
    public String getQueryLanguage();
    
    /**
     * Return the fields read the specified query
     */
    public List<JRDesignField> readFields(String query) throws Exception;
    
    /**
     * Return the fields read the specified query
     */
    public boolean supportsDesign();
    
    /**
     * Return the fields read the specified query
     */
    public String designQuery(String query);
    
}
