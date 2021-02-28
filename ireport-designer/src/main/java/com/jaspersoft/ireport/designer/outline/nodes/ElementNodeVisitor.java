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
package com.jaspersoft.ireport.designer.outline.nodes;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.charts.multiaxis.MultiAxisChartElementNode;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitable;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.design.JRDesignBreak;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignEllipse;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Lookup;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class ElementNodeVisitor implements JRVisitor {

    public static final String ICON_BASE = "com/jaspersoft/ireport/designer/resources/";
    public static final String ICON_BREAK = ICON_BASE + "break-16.png"; 
    public static final String ICON_CROSSTAB = ICON_BASE + "crosstab-16.png"; 
    public static final String ICON_FRAME = ICON_BASE + "frame-16.png"; 
    public static final String ICON_ELLIPSE = ICON_BASE + "ellipse-16.png"; 
    public static final String ICON_LINE = ICON_BASE + "line-16.png"; 
    public static final String ICON_RECTANGLE = ICON_BASE + "rectangle-16.png";
    public static final String ICON_ROUND_RECTANGLE = ICON_BASE + "roundrectangle-16.png";
    public static final String ICON_IMAGE = ICON_BASE + "image-16.png";
    public static final String ICON_SUBREPORT = ICON_BASE + "subreport-16.png";
    public static final String ICON_STATIC_TEXT = ICON_BASE + "statictext-16.png";
    public static final String ICON_TEXT_FIELD = ICON_BASE + "textfield-16.png";
    public static final String ICON_CHART = ICON_BASE + "chart-16.png";
    public static final String ICON_GENERIC_ELEMENT = ICON_BASE + "genericelement-16.png";

    private JasperDesign jasperDesign = null;
    private ElementNameVisitor nameVisitor = null;
    private IRIndexedNode node = null;
    private Lookup doLkp = null;
    
    
    /**
     *
     */
    public ElementNodeVisitor(JasperDesign jasperDesign, Lookup doLkp)
    {
        this.doLkp = doLkp;
        this.jasperDesign = jasperDesign;
        this.nameVisitor = new ElementNameVisitor(jasperDesign);
    }
    
    
    /**
     *
     */
    public IRIndexedNode getNode(JRVisitable visitable)
    {
        visitable.visit(this);
        node.setDisplayName(nameVisitor.getName(visitable));
        return node;
    }
    
    
    /**
     *
     */
    public void visitBreak(JRBreak breakElement)
    {
        node = new ElementNode(jasperDesign, (JRDesignBreak)breakElement,doLkp);
        node.setIconBaseWithExtension(ICON_BREAK);
    }

    /**
     *
     */
    public void visitChart(JRChart chart)
    {
        if (chart.getChartType() == JRChart.CHART_TYPE_MULTI_AXIS)
        {
            node = new MultiAxisChartElementNode(jasperDesign, (JRDesignChart)chart, doLkp);
        }
        else
        {
            node = new ElementNode(jasperDesign, (JRDesignChart)chart,doLkp);
        }
        node.setIconBaseWithExtension(ICON_CHART);

        ((JRBaseChartPlot)((JRDesignChart)chart).getPlot()).getEventSupport()
                .addPropertyChangeListener((PropertyChangeListener)node);
    }

    /**
     *
     */
    public void visitCrosstab(JRCrosstab crosstab)
    {
        node = new CrosstabNode(jasperDesign, (JRDesignCrosstab)crosstab,doLkp);
        node.setIconBaseWithExtension(ICON_CROSSTAB);
    }

    /**
     *
     */
    public void visitElementGroup(JRElementGroup elementGroup)
    {
        node = new ElementGroupNode(jasperDesign, (JRDesignElementGroup)elementGroup,doLkp);
    }

    /**
     *
     */
    public void visitEllipse(JREllipse ellipse)
    {
        node = new ElementNode(jasperDesign, (JRDesignEllipse)ellipse,doLkp);
        node.setIconBaseWithExtension(ICON_ELLIPSE);
    }

    /**
     *
     */
    public void visitFrame(JRFrame frame)
    {
        node = new FrameNode(jasperDesign,(JRDesignFrame)frame,doLkp);
        node.setIconBaseWithExtension(ICON_FRAME);
    }

    /**
     *
     */
    public void visitImage(JRImage image)
    {
        node = new ElementNode(jasperDesign, (JRDesignImage)image,doLkp);
        node.setIconBaseWithExtension(ICON_IMAGE);
    }

    /**
     *
     */
    public void visitLine(JRLine line)
    {
        node = new ElementNode(jasperDesign, (JRDesignLine)line,doLkp);
        node.setIconBaseWithExtension(ICON_LINE);
    }

    /**
     *
     */
    public void visitRectangle(JRRectangle rectangle)
    {
        node = new ElementNode(jasperDesign, (JRDesignRectangle)rectangle,doLkp);
        
        if (rectangle.getRadius() > 0)
            node.setIconBaseWithExtension(ICON_ROUND_RECTANGLE);
        else
            node.setIconBaseWithExtension(ICON_RECTANGLE);
    }

    /**
     *
     */
    public void visitStaticText(JRStaticText staticText)
    {
        node = new ElementNode(jasperDesign, (JRDesignStaticText)staticText,doLkp);
        node.setIconBaseWithExtension(ICON_STATIC_TEXT);
    }

    /**
     *
     */
    public void visitSubreport(JRSubreport subreport)
    {
        node = new ElementNode(jasperDesign, (JRDesignSubreport)subreport,doLkp);
        node.setIconBaseWithExtension(ICON_SUBREPORT);
    }

    /**
     *
     */
    public void visitTextField(JRTextField textField)
    {
        node = new ElementNode(jasperDesign, (JRDesignTextField)textField,doLkp);
        node.setIconBaseWithExtension(ICON_TEXT_FIELD);
    }

    public void visitComponentElement(JRComponentElement componentElement) {

        node = IReportManager.getComponentNode(jasperDesign, (JRDesignComponentElement)componentElement, doLkp );

        if (node == null)
        {
            node = new ElementNode(jasperDesign, (JRDesignComponentElement)componentElement,doLkp);
            node.setIconBaseWithExtension(ICON_RECTANGLE);
        }
    }

    public void visitGenericElement(JRGenericElement genericElement) {
        node = new ElementNode(jasperDesign, (JRDesignGenericElement)genericElement,doLkp);
        node.setIconBaseWithExtension(ICON_GENERIC_ELEMENT);
    }
    
}
