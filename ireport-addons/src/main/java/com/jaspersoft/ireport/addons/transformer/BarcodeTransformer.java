/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.transformer;

import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import java.io.File;
import java.util.List;
import net.sf.jasperreports.components.barbecue.StandardBarbecueComponent;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: BarcodeTransformer.java 0 2010-04-15 10:39:26 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class BarcodeTransformer extends DefaultTransformer {

    private BarcodeTransformerOptions optionsPanel = null;

    @Override
    public JasperDesign transform(String srcFileName) throws TransformException
    {
        JasperDesign jd = super.transform(srcFileName);
        if (((BarcodeTransformerOptions)getComponent()).isBackupOldSource())
        {
            File old_jrxml = new File(srcFileName);
            if (old_jrxml.exists())
            {
                old_jrxml.renameTo(new File( Misc.changeFileExtension(srcFileName,"jrxml.bak")) );
            }
        }
        return jd;
    }

    @Override
    public JasperDesign transform(JasperDesign jasperDesign) throws TransformException {

        List<JRBand> bands = ModelUtils.getBands(jasperDesign);

        for (JRBand band : bands)
        {
            transformElements(band);
        }

        return jasperDesign;
    }

    /**
     * a container can be a Band, a Cell or a Frame. The check is performed recursively.
     * Crosstab are analyzed in depth discovering the cells.
     * Custom components like tables are not checked out.
     * @param container
     */
    public void transformElements(JRElementGroup container)
    {
        List elements = container.getChildren();

        for (Object item : elements)
        {
            if (!(item instanceof JRDesignElement))
            {
                continue;
            }
            JRDesignElement element = (JRDesignElement)item;
            // transform all the elements inside the crosstab...
            if (element instanceof JRDesignCrosstab)
            {
                transformElementsInCrosstab((JRDesignCrosstab)element);
            }
            else if (element instanceof JRElementGroup)
            {
                transformElements( (JRElementGroup)element);
            }

            JRDesignElement newElement = transformElement(element);

            // If the element has been transformed... replace it in the parent...
            if (newElement != element)
            {

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
                }
            }
        }

    }

    /**
     * Transform all the elements inside the crosstab cells
     * @param crosstab
     * @return
     */
    public void transformElementsInCrosstab(JRDesignCrosstab crosstab) {

        List<JRDesignCellContents> cells = ModelUtils.getAllCells(crosstab);

        for (JRDesignCellContents content : cells)
        {
            transformElements(content);
        }
    }


    public JRDesignElement transformElement(JRDesignElement element)
    {
        if (!(element instanceof JRDesignImage)) return element;
        if (!Misc.getExpressionText( ((JRDesignImage)element).getExpression() ).startsWith("it.businesslogic.ireport.barcode.BcImage.getBarcodeImage(")) return element;

        return transformImageToBarcode((JRDesignImage)element);
    }


    public JRDesignElement transformImageToBarcode(JRDesignImage element) {

            StandardBarbecueComponent componentImpl = new StandardBarbecueComponent();
            componentImpl.setEvaluationTimeValue( element.getEvaluationTimeValue() );
            if (element.getEvaluationGroup() != null)
            componentImpl.setEvaluationGroup( element.getEvaluationGroup().getName() );

            String imageExpression = Misc.getExpressionText(element.getExpression());

            int numberOfParams = 7;

            String iE = imageExpression.substring(imageExpression.indexOf("(") + 1, imageExpression.lastIndexOf(")"));
            String[] params = iE.split(",");
            int paramCount = params.length;

            componentImpl.setType( getBarcodeName( new Integer(params[0]).intValue() )); //params[0] will always be type

            String barcodeExpression = "";
            for (int i=0; i <= paramCount - numberOfParams; i++)
            {
                barcodeExpression += params[i+1] + ",";
            }

            componentImpl.setCodeExpression( Misc.createExpression("java.lang.String", barcodeExpression.substring(0, barcodeExpression.length() - 1) ));

            componentImpl.setDrawText( new Boolean(params[2 + (paramCount - numberOfParams)]).booleanValue() );
            componentImpl.setChecksumRequired( new Boolean(params[3 + (paramCount - numberOfParams)]).booleanValue() );

            if (params.length > 4)
            {

                componentImpl.setApplicationIdentifierExpression( Misc.createExpression("java.lang.String", "\"" + params[4 + (paramCount - numberOfParams)] + "\""));
                int bW = Integer.parseInt( params[5 + (paramCount - numberOfParams)]);
                if (bW > 0) componentImpl.setBarWidth(bW);

                int bH = Integer.parseInt( params[6 + (paramCount - numberOfParams)]);
                if (bH > 0) componentImpl.setBarHeight(bH);
            }


            JRDesignComponentElement component = new JRDesignComponentElement();


            component.setX( element.getX());
            component.setY( element.getY());
            component.setWidth( element.getWidth());
            component.setHeight( element.getHeight());
            component.setBackcolor( element.getOwnBackcolor());
            component.setForecolor( element.getOwnForecolor());
            component.setKey( element.getKey());
            component.setMode( element.getOwnModeValue());
            component.setPositionType( element.getPositionTypeValue());
            component.setPrintWhenDetailOverflows( element.isPrintWhenDetailOverflows());
            if (element.getPrintWhenExpression() != null)
            {
                component.setPrintWhenExpression( (JRExpression) ((JRDesignExpression)element.getPrintWhenExpression()).clone() );
            }
            component.setPrintRepeatedValues( element.isPrintRepeatedValues());
            component.setPrintInFirstWholeBand( element.isPrintInFirstWholeBand());
            component.setPrintWhenGroupChanges( element.getPrintWhenGroupChanges());
            component.setRemoveLineWhenBlank(element.isRemoveLineWhenBlank());
            component.setStretchType(element.getStretchTypeValue());
            component.setStyle(element.getStyle());
            component.setStyleNameReference(element.getStyleNameReference());

            component.setComponent(componentImpl);
            component.setComponentKey(new ComponentKey(
                                        "http://jasperreports.sourceforge.net/jasperreports/components",
                                        "jr", "barbecue"));

            return component;
    }


    public String getBarcodeName(int barcodeType)
    {
        switch (barcodeType)
        {
        case 1: return "2of7";
        case 2: return "3of9";
        case 3: return "Bookland";
        case 4: return "Codabar";
        case 5: return "Code128";
        case 6: return "Code128A";
        case 7: return "Code128B";
        case 8: return "Code128C";
        case 9: return "Code39";
        case 10: return "EAN128";
        case 11: return "EAN13";
        case 12: return "GlobalTradeItemNumber";
        case 13: return "Int2of5";
        case 14: return "Monarch";
        case 15: return "NW7";
        case 16: return "PDF417";
        case 17: return "SCC14ShippingCode";
        case 18: return "ShipmentIdentificationNumber";
        case 19: return "SSCC18"; //BarcodeFactory.createSSCC18(text); break;
        case 20: return "Std2of5";
        case 21: return "UCC128"; //BarcodeFactory.createUCC128(applicationIdentifier, text); break;
        case 22: return "UPCA";
        case 23: return "USD3";
        case 24: return "USD4";
        case 25: return "USPS";
        case 26: return "Code39 (Extended)";
        }

        return "3of9";
    }

    public String getName() {
        return "Transform Images to Barcode";
    }

    public Component getComponent() {
        if (optionsPanel == null)
        {
            optionsPanel = new BarcodeTransformerOptions();
        }
        return optionsPanel;
    }


}
