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
package com.jaspersoft.ireport.jasperserver.ui;

import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gtoffoli
 */
public class ValidationUtils {

    	public static final int MAX_LENGTH_NAME = 100;
	public static final int MAX_LENGTH_LABEL = 100;
	public static final int MAX_LENGTH_DESC = 250;
        private static final Pattern PATTERN_NAME = Pattern.compile("(\\p{L}|\\p{N}|(\\_)|(\\.)|(\\-)|[;@])+");
        
        
        public static void validateName(String name) throws Exception
        {
            if (name==null || name.length() == 0) 
                throw new Exception(JasperServerManager.getString("resource.name.isEmpty","The name can not be empty"));
            Matcher mat = PATTERN_NAME.matcher(name.trim());
            if (!mat.matches()) 
                    throw new Exception(JasperServerManager.getString("resource.name.invalidCharacters","The name contains invalid characters"));
            if (name.trim().length() > MAX_LENGTH_NAME) throw new Exception(JasperServerManager.getFormattedString("resource.name.tooLong","The name can not be longer than {0,integer} characters",new Object[]{new Integer(MAX_LENGTH_NAME)}));
        }
        
        public static void validateLabel(String name) throws Exception
        {
            if (name==null || name.length() == 0) throw new Exception(JasperServerManager.getString("resource.label.isEmpty","The label can not be empty"));
            if (name.trim().length() > MAX_LENGTH_LABEL) throw new Exception(JasperServerManager.getFormattedString("resource.label.tooLong","The label can not be longer than {0,integer} characters",new Object[]{new Integer(MAX_LENGTH_LABEL)}));
        }
        
        public static void validateDesc(String name) throws Exception
        {
            if (name != null && name.trim().length() > MAX_LENGTH_DESC) throw new Exception(JasperServerManager.getFormattedString("resource.desc.tooLong","The description can not be longer than characters",new Object[]{new Integer(MAX_LENGTH_DESC)}));
        }
        
}
