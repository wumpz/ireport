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
package com.jaspersoft.ireport.examples;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

/**
 *
 * @author gtoffoli
 */
public class TestScriptlet extends JRAbstractScriptlet {

    @Override
    public void beforeReportInit() throws JRScriptletException {
       this.setVariableValue("testVariable", "beforeReportInit");
    }

    @Override
    public void afterReportInit() throws JRScriptletException {
        this.setVariableValue("testVariable", "afterReportInit");
    }

    @Override
    public void beforePageInit() throws JRScriptletException {
        this.setVariableValue("testVariable", "beforePageInit");
    }

    @Override
    public void afterPageInit() throws JRScriptletException {
        this.setVariableValue("testVariable", "afterPageInit");
    }

    @Override
    public void beforeColumnInit() throws JRScriptletException {
        this.setVariableValue("testVariable", "beforeColumnInit");
    }

    @Override
    public void afterColumnInit() throws JRScriptletException {
        this.setVariableValue("testVariable", "afterColumnInit");
    }

    @Override
    public void beforeGroupInit(String arg0) throws JRScriptletException {
        this.setVariableValue("testVariable", "beforeGroupInit");
    }

    @Override
    public void afterGroupInit(String arg0) throws JRScriptletException {
        this.setVariableValue("testVariable", "afterGroupInit");
    }

    @Override
    public void beforeDetailEval() throws JRScriptletException {
        this.setVariableValue("testVariable", "beforeDetailEval");
    }

    @Override
    public void afterDetailEval() throws JRScriptletException {
        this.setVariableValue("testVariable", "afterDetailEval");
    }

}
