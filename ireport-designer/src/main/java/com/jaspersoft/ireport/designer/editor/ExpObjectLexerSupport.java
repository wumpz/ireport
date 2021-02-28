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
package com.jaspersoft.ireport.designer.editor;


import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import org.netbeans.api.languages.ASTToken;
import org.netbeans.api.languages.CharInput;
import org.netbeans.api.languages.Language;
import org.netbeans.api.languages.LanguageDefinitionNotFoundException;
import org.netbeans.api.languages.LanguagesManager;

/**
 *
 * @author gtoffoli
 */
public class ExpObjectLexerSupport {

    private static final String MIME_TYPE = "text/jrxml-expression";

    public static Object[] parseExpObject (CharInput input) {
        int start = input.getIndex ();

        try {

        if (input.next() == '$')
        {
            input.read();
            if (input.next() == 'F' ||
                input.next() == 'V' ||
                input.next() == 'P')
            {
                char objType = input.read();
                if (input.next() == '{')
                {
                    input.read();
                    String objName = "";
                    while (!input.eof() && input.next() != '}')
                    {
                        objName += input.read();
                    }
                    if (input.next() == '}')
                    {
                        input.read();
                        Language language;

                            try {
                                language = LanguagesManager.get().getLanguage(MIME_TYPE);
                            } catch (LanguageDefinitionNotFoundException ex) {
                                ex.printStackTrace();
                                return null;
                            }

                        // Check is it is a valid objct
                        if (isValidObject(objName,objType))
                        {
                            return new Object[] {
                                ASTToken.create (language, "object_" + objType, "object_" + objType, start, objName.length() + 4, null),
                                "DEFAULT"
                            };
                        }
                        else
                        {
                            return new Object[] {
                                ASTToken.create (language, "invalid_object", "invalid_object", start, objName.length() + 4, null),
                                "DEFAULT"
                            };
                        }
                    }
                }
            }
        }
        
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        input.setIndex(start);
        return new Object[]{null,null};

    }



    private static boolean isValidObject(String objName, char type) {
        ExpressionContext expressionContext = ExpressionContext.getGlobalContext();
        if (expressionContext != null)
        {
            return expressionContext.findObjectClassName(objName, type) != null;
        }
        return false;
    }

    

}
