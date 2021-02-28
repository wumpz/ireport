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
package com.jaspersoft.ireport.designer.widgets.visitor;

import com.jaspersoft.ireport.designer.IRLocalJasperReportsContext;
import java.awt.Graphics2D;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.convert.ConvertVisitor;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.export.AwtTextRenderer;
import net.sf.jasperreports.engine.export.draw.DrawVisitor;
import net.sf.jasperreports.engine.export.draw.FrameDrawer;

/**
 *
 * @author gtoffoli
 */
public class ConfigurableDrawVisitor extends DrawVisitor {

    private ConvertVisitor convertVisitor = null;
    private ReportConverter reportConverter = null;
    private FrameDrawer frameDrawer = null;
    private Graphics2D grx = null;

    /**
	 *
	 */
	public ConfigurableDrawVisitor(JRReport report, Graphics2D grx)
	{
		this(new ReportConverter(IRLocalJasperReportsContext.getInstance(), report, true), grx);
	}

	/**
	 *
	 */
	public ConfigurableDrawVisitor(ReportConverter reportConverter, Graphics2D grx)
	{
		super( reportConverter, grx);
        //this.convertVisitor = new ConvertVisitor(reportConverter);
        this.grx = grx;
        this.reportConverter = reportConverter;
    }

    @Override
    public void visitFrame(JRFrame frame) {

       

        try
		{
            if (convertVisitor == null)
            {
                convertVisitor = new ConvertVisitor(reportConverter);
            }

            if (frameDrawer == null)
            {
                frameDrawer = new FrameDrawer(null, new AwtTextRenderer(false, true));
                frameDrawer.setClip(true);
            }
            JRPrintFrame element = (JRPrintFrame)convertVisitor.getVisitPrintElement(frame);
            element.getElements().clear();
			frameDrawer.draw(
				grx,
				element,
				-frame.getX(),
				-frame.getY()
				);
		}
        catch (JRException e)
		{
            throw new JRRuntimeException(e);
		}


    }


    /**
	 *
	 */
    @Override
	public void setGraphics2D(Graphics2D grx)
	{
		this.grx = grx;
        super.setGraphics2D(grx);
	}



}
