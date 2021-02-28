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

package com.jaspersoft.ireport;

import com.jaspersoft.ireport.designer.IReportManager;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.sendopts.CommandException;
import org.netbeans.spi.sendopts.Env;
import org.netbeans.spi.sendopts.Option;
import org.netbeans.spi.sendopts.OptionProcessor;

/**
 *
 * @version $Id: IReportOptionProcessor.java 0 2009-10-27 13:22:25 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IReportOptionProcessor extends OptionProcessor {

     private Option nonetworkOption = Option.withoutArgument('N',"noNetework");

    @Override
    protected Set<Option> getOptions() {
        nonetworkOption = Option.shortDescription(nonetworkOption,
                                      "com.jaspersoft.ireport.locale.Bundle",
                                      "nonetwork");
        HashSet set = new HashSet();
        set.add(nonetworkOption);
        return set;
    }

    @Override
    protected void process(Env env, Map args) throws CommandException {
        if (args.containsKey(nonetworkOption))
        {
            IReportManager.getInstance().setNoNetwork(true);
        }
    }

}
