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
package com.jaspersoft.ireport.designer.actions;

import bsh.EvalError;
import bsh.Interpreter;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.sf.jasperreports.charts.design.JRDesignPieDataset;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignChart;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignEllipse;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

public final class TransformElementAction extends NodeAction {

    public static final int TRANSFORMATION_TYPE_TO_TEXTFIELD = 0;
    public static final int TRANSFORMATION_TYPE_TO_STATICTEXT = 1;
    public static final int TRANSFORMATION_TYPE_TO_RECTANGLE = 2;
    public static final int TRANSFORMATION_TYPE_TO_ELLIPSE = 3;
    public static final int TRANSFORMATION_TYPE_TO_FRAME = 4;
    public static final int TRANSFORMATION_TYPE_TO_CHART_PIE = 5;
    public static final int TRANSFORMATION_TYPE_TO_CHART_PIE3D = 6;


    private JMenu menu = null;
    private List<JMenuItem> transformActions = null;

    public String getName() {
        return I18n.getString("TransformElementAction.name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {
    }

    public void transformElements(ActionEvent e)
    {
        int transformationType = -1;
        try {
            transformationType = Integer.valueOf( e.getActionCommand() );
        } catch (Exception ex) { return; }

        // Get selection...
        org.openide.nodes.Node[] nodes = this.getActivatedNodes();

        List<JRDesignElement> selectedElements = new ArrayList<JRDesignElement>();
        List<JRDesignElement> selection = IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel().getActiveScene().getSelectionManager().getSelectedElements();
        JRDesignElement primarySelected = (selection.size() > 0) ? selection.get(0) : null;

        for (int i=0; i<nodes.length; ++i)
        {
            if (!(nodes[i] instanceof ElementNode)) continue;
            ElementNode node = (ElementNode)nodes[i];
            JRDesignElement element = node.getElement();
            JRDesignElement newElement = null;
            JasperDesign jd = node.getJasperDesign();

            switch (transformationType)
            {
                case TRANSFORMATION_TYPE_TO_TEXTFIELD:
                {
                    if (element instanceof JRDesignStaticText)
                    {
                        newElement = new JRDesignTextField(jd);
                        copyBasicProperties(element, newElement);
                        copyTextProperties((JRDesignStaticText)element, (JRDesignTextField)newElement);
                        String s = ((JRDesignStaticText)element).getText();
                        s = Misc.string_replace("\\\\", "\\", s);
                        s = Misc.string_replace("\\\"", "\"", s);
                        s = "\"" + s + "\"";
                        JRDesignExpression exp = Misc.createExpression("java.lang.String",s);
                        ((JRDesignTextField)newElement).setExpression(exp);
                    }
                    break;
                }
                case TRANSFORMATION_TYPE_TO_STATICTEXT:
                {
                    if (element instanceof JRDesignTextField)
                    {
                        newElement = new JRDesignStaticText(jd);
                        copyBasicProperties(element, newElement);
                        copyTextProperties((JRDesignTextField)element, (JRDesignStaticText)newElement);
                        String s = Misc.getExpressionText( ((JRDesignTextField)element).getExpression() );
                        // remove quotes...
                        StringBuffer newString = new StringBuffer();
                        if (s.length() > 0)
                        {
                            for (int index=0; index<s.length(); ++index)
                            {
                                char c = s.charAt(index);
                                switch (c)
                                {
                                    case '"':
                                        break;

                                    case '\\':
                                    {
                                        index++;
                                        if (index<s.length())
                                        {
                                            char c1 = s.charAt(index);
                                            if (c1 == 'n') newString.append('\n');
                                            else if (c1 == 't') newString.append('\t');
                                            else if (c1 == '\\') newString.append('\\');
                                            else if (c1 == '"') newString.append('"');
                                            else if (c1 == 'u')
                                            {
                                                // Unicode character...
                                                if (index+4<s.length() &&
                                                   isOctal(s.charAt(index+1)) &&
                                                   isOctal(s.charAt(index+2)) &&
                                                   isOctal(s.charAt(index+3)) &&
                                                   isOctal(s.charAt(index+4)))
                                                {
                                                    Interpreter interpreter = new Interpreter();
                                                    try {
                                                        newString.append(interpreter.eval("\"" + s.substring(index - 1, index - 1 + 6) + "\""));

                                                    } catch (EvalError ex) {
                                                        ex.printStackTrace();
                                                        newString.append(s.substring(index - 1, index - 1 + 6));
                                                    }
                                                    index+=4;
                                                    
                                                }
                                            }
                                            else
                                            {
                                                newString.append("\\" + c);
                                            }
                                        }
                                        else
                                        {
                                            newString.append(c);
                                        }
                                        break;
                                    }
                                    default:
                                       newString.append(c);
                                }
                            }
                        }


                        //s = Misc.string_replace("\"", "\\\"", s);
                        //s = Misc.string_replace("\\", "\\\\", s);
                        ((JRDesignStaticText)newElement).setText(newString.toString());
                    }
                    break;
                }
                case TRANSFORMATION_TYPE_TO_ELLIPSE:
                {
                    if (element instanceof JRDesignRectangle)
                    {
                        newElement = new JRDesignEllipse(jd);
                        copyBasicProperties(element, newElement);
                    }
                    break;
                }
                case TRANSFORMATION_TYPE_TO_RECTANGLE:
                {
                    if (element instanceof JRDesignEllipse)
                    {
                        newElement = new JRDesignRectangle(jd);
                        copyBasicProperties(element, newElement);
                    }
                    break;
                }
                case TRANSFORMATION_TYPE_TO_FRAME:
                {
                    if (element instanceof JRDesignRectangle)
                    {
                        newElement = new JRDesignFrame(jd);
                        copyBasicProperties(element, newElement);
                    }
                    break;
                }
                case TRANSFORMATION_TYPE_TO_CHART_PIE:
                {
                    if (element instanceof JRDesignChart)
                    {
                        JRDesignChart chartElement = (JRDesignChart)element;
                        if (chartElement.getChartType() == JRDesignChart.CHART_TYPE_PIE3D)
                        {
                            newElement = new JRDesignChart(jd, JRDesignChart.CHART_TYPE_PIE);
                            copyBasicProperties(element, newElement);
                            copyBasicChartProperties((JRDesignChart)element, (JRDesignChart)newElement);
                            ((JRDesignChart)newElement).setDataset((JRDesignPieDataset)chartElement.getDataset().clone() );
                        }
                    }
                    break;
                }
                case TRANSFORMATION_TYPE_TO_CHART_PIE3D:
                {
                    if (element instanceof JRDesignChart)
                    {
                        JRDesignChart chartElement = (JRDesignChart)element;
                        if (chartElement.getChartType() == JRDesignChart.CHART_TYPE_PIE)
                        {
                            newElement = new JRDesignChart(jd, JRDesignChart.CHART_TYPE_PIE3D);
                            copyBasicProperties(element, newElement);
                            copyBasicChartProperties((JRDesignChart)element, (JRDesignChart)newElement);
                            ((JRDesignChart)newElement).setDataset((JRDesignPieDataset)chartElement.getDataset().clone() );
                        }
                    }
                    break;
                }

            }

            // Replace the element...
            if (newElement != null) // Should be always true....
            {
                replaceElement(node, newElement);
                selectedElements.add(newElement);
                if (primarySelected == element)
                {
                    primarySelected = newElement;
                }
            }
            else
            {
                selectedElements.add(element);
            }
        }


        if (selectedElements.size() > 0)
        {
            boolean first = true;

            if (primarySelected != null && selectedElements.contains(primarySelected))
            {
                selectedElements.remove(primarySelected);
                selectedElements.add(0, primarySelected);
            }
            
            for (JRDesignElement element : selectedElements)
            {
                
                if (first) IReportManager.getInstance().setSelectedObject(element);
                else IReportManager.getInstance().addSelectedObject(element);
                first = false;
            }
        }
    }

   public static void replaceElement(ElementNode node, JRDesignElement newElement)
   {
        JRDesignElement element = node.getElement();
        JRElementGroup container = element.getElementGroup();
        if (container instanceof JRDesignElementGroup)
        {
            int index = ((JRDesignElementGroup)container).getChildren().indexOf(element);
            ((JRDesignElementGroup)container).getChildren().add(index, newElement);
            newElement.setElementGroup(container);
            ((JRDesignElementGroup)container).removeElement(element);
        }
        else if (container instanceof JRDesignFrame)
        {
            int index = ((JRDesignFrame)container).getChildren().indexOf(element);
            ((JRDesignFrame)container).getChildren().set(index, newElement);
            newElement.setElementGroup(container);
            ((JRDesignFrame)container).removeElement(element);
            //((JRDesignFrame)container).getEventSupport().firePropertyChange(JRDesignElementGroup.PROPERTY_CHILDREN, null, null);
        }

        // TODO: Add undo op....
   }


   protected boolean enable(org.openide.nodes.Node[] activatedNodes) {


       getMenu().setEnabled(false);
       if (activatedNodes == null || activatedNodes.length < 1) return false;

       List<Integer> commonTransformationTypes = getTransformationTypes(activatedNodes);
       if (commonTransformationTypes == null || commonTransformationTypes.size() == 0) return false;
       
       rebuildMenu(commonTransformationTypes);
       getMenu().setEnabled(true);
       return true;
    }

    private JRExpression cloneExpression(JRExpression expression) {
        if (expression != null) return (JRExpression)expression.clone();
        return null;
    }

    private void copyBasicChartProperties(JRDesignChart element, JRDesignChart newElement) {

      newElement.setAnchorNameExpression( element.getAnchorNameExpression());
      newElement.setBookmarkLevel( element.getBookmarkLevel());
      newElement.setCustomizerClass( element.getCustomizerClass());
      newElement.setEvaluationGroup( element.getEvaluationGroup());
      newElement.setEvaluationTime( element.getEvaluationTimeValue());
      newElement.setHyperlinkAnchorExpression( cloneExpression( element.getHyperlinkAnchorExpression()));
      newElement.setHyperlinkWhenExpression( cloneExpression( element.getHyperlinkWhenExpression()));
      newElement.setHyperlinkPageExpression( cloneExpression(element.getHyperlinkPageExpression()));
      newElement.setHyperlinkReferenceExpression( cloneExpression(element.getHyperlinkReferenceExpression()));
      newElement.setHyperlinkTarget( element.getHyperlinkTarget());
      newElement.setLinkTarget(element.getLinkTarget());
      newElement.setHyperlinkTooltipExpression( cloneExpression(element.getHyperlinkTooltipExpression()));
      newElement.setHyperlinkType( element.getHyperlinkType());
      newElement.setLegendBackgroundColor( element.getOwnLegendBackgroundColor());
      newElement.setLegendColor( element.getOwnLegendColor());
      newElement.setLegendFont( element.getLegendFont());
      newElement.setLegendPosition( element.getLegendPositionValue());
      newElement.setLinkTarget( element.getLinkTarget());
      newElement.setLinkType( element.getLinkType());
      newElement.setRenderType( element.getRenderType());
      newElement.setShowLegend( element.getShowLegend());
      newElement.setSubtitleColor( element.getOwnSubtitleColor());
      newElement.setSubtitleExpression( cloneExpression(element.getSubtitleExpression()));
      newElement.setSubtitleFont( element.getSubtitleFont());
      newElement.setTheme( element.getTheme());
      newElement.setTitleFont( element.getTitleFont());
      newElement.setTitlePosition( element.getTitlePositionValue());
      newElement.setTitleColor( element.getOwnTitleColor());
      newElement.setTitleExpression( cloneExpression(element.getTitleExpression()));

      newElement.getPlot().setBackcolor( element.getPlot().getOwnBackcolor()  );
      newElement.getPlot().setBackgroundAlpha( element.getPlot().getBackgroundAlphaFloat() );
      newElement.getPlot().setForegroundAlpha( element.getPlot().getForegroundAlphaFloat()  );
      newElement.getPlot().setLabelRotation( element.getPlot().getLabelRotationDouble() );
      newElement.getPlot().setOrientation( element.getPlot().getOrientation() );
      newElement.getPlot().setSeriesColors( element.getPlot().getSeriesColors() );
    }

    private void copyBasicProperties(JRDesignElement element, JRDesignElement newElement) {
        newElement.setX( element.getX());
        newElement.setY( element.getY());
        newElement.setWidth( element.getWidth());
        newElement.setHeight( element.getHeight());
        newElement.setBackcolor( element.getOwnBackcolor());
        newElement.setForecolor( element.getOwnForecolor());
        newElement.setKey( element.getKey());
        newElement.setMode( element.getOwnModeValue());
        newElement.setPositionType( element.getPositionTypeValue());
        newElement.setPrintWhenDetailOverflows( element.isPrintWhenDetailOverflows());
        newElement.setPrintWhenExpression(cloneExpression(element.getPrintWhenExpression()));
        newElement.setPrintRepeatedValues( element.isPrintRepeatedValues());
        newElement.setPrintInFirstWholeBand( element.isPrintInFirstWholeBand());
        newElement.setPrintWhenGroupChanges( element.getPrintWhenGroupChanges());
        newElement.setRemoveLineWhenBlank(element.isRemoveLineWhenBlank());
        newElement.setStretchType(element.getStretchTypeValue());
        newElement.setStyle(element.getStyle());
        newElement.setStyleNameReference(element.getStyleNameReference());


    }

    private void copyTextProperties(JRDesignTextElement element, JRDesignTextElement newElement) {
        newElement.setBold( element.isOwnBold() );
        newElement.setItalic( element.isOwnItalic());
        newElement.setFontName( element.getOwnFontName());
        newElement.setFontSize( element.getOwnFontSize());
        newElement.setHorizontalAlignment( element.getOwnHorizontalAlignmentValue());
        newElement.setLineSpacing( element.getOwnLineSpacingValue());
        newElement.setMarkup(element.getMarkup());
        newElement.setPdfEmbedded( element.isOwnPdfEmbedded());
        newElement.setPdfEncoding( element.getOwnPdfEncoding());
        newElement.setPdfFontName( element.getOwnPdfFontName());
        newElement.setRotation( element.getOwnRotationValue());
        newElement.setStrikeThrough( element.isOwnStrikeThrough());
        newElement.setVerticalAlignment( element.getOwnVerticalAlignmentValue());
        newElement.setUnderline(element.isOwnUnderline());
    }

    /**
     * Find the common possible transformation types...
     * @param activatedNodes
     * @return
     */
    private List<Integer> getTransformationTypes(org.openide.nodes.Node[] activatedNodes)
    {
        List<Integer> commonTransformationTypes = null;

        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(activatedNodes[i] instanceof ElementNode)) return commonTransformationTypes;
            
            if (commonTransformationTypes == null)
            {
                commonTransformationTypes = getTransformationTypes(((ElementNode)activatedNodes[i]).getElement());
            }
            else
            {
                List<Integer> thisSet = getTransformationTypes(((ElementNode)activatedNodes[i]).getElement());

                commonTransformationTypes.retainAll(thisSet);
            }
            if (commonTransformationTypes.size() == 0) return commonTransformationTypes;
        }

        return commonTransformationTypes;
    }

    private List<Integer> getTransformationTypes(JRDesignElement element)
    {
        List<Integer> commonTransformationTypes = new ArrayList<Integer>();

        if (element instanceof JRDesignRectangle)
        {
            commonTransformationTypes.add(TRANSFORMATION_TYPE_TO_ELLIPSE);
            commonTransformationTypes.add(TRANSFORMATION_TYPE_TO_FRAME);
        } 
        
        if (element instanceof JRDesignEllipse)
        {
            commonTransformationTypes.add(TRANSFORMATION_TYPE_TO_RECTANGLE);
        }

        if (element instanceof JRDesignTextField)
        {
            commonTransformationTypes.add(TRANSFORMATION_TYPE_TO_STATICTEXT);
        }

        if (element instanceof JRDesignStaticText)
        {
            commonTransformationTypes.add(TRANSFORMATION_TYPE_TO_TEXTFIELD);
        }

        if (element instanceof JRDesignChart)
        {
            JRDesignChart chartElement = (JRDesignChart)element;
            if (chartElement.getChartType() == JRDesignChart.CHART_TYPE_PIE)
            {
                commonTransformationTypes.add(TRANSFORMATION_TYPE_TO_CHART_PIE3D);
            }
            else if (chartElement.getChartType() == JRDesignChart.CHART_TYPE_PIE3D)
            {
                commonTransformationTypes.add(TRANSFORMATION_TYPE_TO_CHART_PIE);
            }
        }

        //if (element instanceof JRDesignTextField ||
        //    element instanceof JRDesignStaticText) return TRANSFORMATION_TYPE_CATEGORY_CHART;


        return commonTransformationTypes;
    }

   @Override
    public JMenuItem getMenuPresenter() {

        return getMenu();
    }

   @Override
    public JMenuItem getPopupPresenter() {
        return getMenu();
    }

    /**
     * @return the menu
     */
    public JMenu getMenu() {
        if (menu == null)
        {
            menu = new JMenu(getName());
        }
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(JMenu menu) {
        this.menu = menu;
    }

    private void rebuildMenu(List<Integer> commonTransformationTypes) {

        getMenu().removeAll();
        

        for (Integer k : commonTransformationTypes)
        {
            getMenu().add(getTransformActions().get(k.intValue()));
        }
    }


    private List<JMenuItem> getTransformActions()
    {
        if (transformActions == null)
        {
            ActionListener listener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    transformElements(e);
                }
            };

            transformActions = new ArrayList<JMenuItem>();

            JMenuItem itemTextfield = new JMenuItem();
            itemTextfield.setText(I18n.getString("TextField_Name"));
            itemTextfield.setIcon(new ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/textfield-16.png")));
            itemTextfield.setActionCommand(TRANSFORMATION_TYPE_TO_TEXTFIELD+"");
            itemTextfield.addActionListener(listener);
            transformActions.add(itemTextfield);


            JMenuItem itemStaticText = new JMenuItem();
            itemStaticText.setText(I18n.getString("StaticText_Name"));
            itemStaticText.setIcon(new ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/statictext-16.png")));
            itemStaticText.setActionCommand(TRANSFORMATION_TYPE_TO_STATICTEXT+"");
            itemStaticText.addActionListener(listener);
            transformActions.add(itemStaticText);

            JMenuItem itemRectangle = new JMenuItem();
            itemRectangle.setText(I18n.getString("Rectangle_Name"));
            itemRectangle.setIcon(new ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/rectangle-16.png")));
            itemRectangle.setActionCommand(TRANSFORMATION_TYPE_TO_RECTANGLE+"");
            itemRectangle.addActionListener(listener);
            transformActions.add(itemRectangle);

            JMenuItem itemEllipse = new JMenuItem();
            itemEllipse.setText(I18n.getString("Ellipse_Name"));
            itemEllipse.setIcon(new ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/ellipse-16.png")));
            itemEllipse.setActionCommand(TRANSFORMATION_TYPE_TO_ELLIPSE+"");
            itemEllipse.addActionListener(listener);
            transformActions.add(itemEllipse);

            JMenuItem itemFrame = new JMenuItem();
            itemFrame.setText(I18n.getString("Frame_Name"));
            itemFrame.setIcon(new ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/frame-16.png")));
            itemFrame.setActionCommand(TRANSFORMATION_TYPE_TO_FRAME+"");
            itemFrame.addActionListener(listener);
            transformActions.add(itemFrame);

            JMenuItem itemChartPie = new JMenuItem();
            itemChartPie.setText(I18n.getString("ChartSelectionJDialog.Chart.Pie"));
            itemChartPie.setToolTipText(I18n.getString("ChartSelectionJDialog.Chart.Pie"));
            itemChartPie.setIcon(new ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/charts/icons/pie.png")));
            itemChartPie.setActionCommand(TRANSFORMATION_TYPE_TO_CHART_PIE+"");
            itemChartPie.addActionListener(listener);
            transformActions.add(itemChartPie);

            JMenuItem itemChartPie3d = new JMenuItem();
            itemChartPie3d.setText(I18n.getString("ChartSelectionJDialog.Chart.Pie3D"));
            itemChartPie3d.setToolTipText(I18n.getString("ChartSelectionJDialog.Chart.Pie3D"));
            itemChartPie3d.setIcon(new ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/charts/icons/pie3d.png")));
            itemChartPie3d.setActionCommand(TRANSFORMATION_TYPE_TO_CHART_PIE3D+"");
            itemChartPie3d.addActionListener(listener);
            transformActions.add(itemChartPie3d);


        }

        return transformActions;
    }

    private boolean isOctal(char c)
    {
        c = Character.toLowerCase(c);
        return (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' ||
                c == '8' || c == '9' || c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f');
    }
}