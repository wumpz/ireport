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
package com.jaspersoft.ireport.components.barcode;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author gtoffoli
 */
public class BarcodeDescriptor {

    private static List<BarcodeDescriptor> barbecueBarcodes = null;
    public static List<BarcodeDescriptor> getBarbecueBarcodes()
    {
        if (barbecueBarcodes == null)
        {
            barbecueBarcodes = new ArrayList<BarcodeDescriptor>();
            barbecueBarcodes.add(new BarcodeDescriptor("2of7",                        "/com/jaspersoft/ireport/components/barcode/icons/2of7.png",            "2of7"));
            barbecueBarcodes.add(new BarcodeDescriptor("3of9",                        "/com/jaspersoft/ireport/components/barcode/icons/3of9.png",                        "3of9"));
            barbecueBarcodes.add(new BarcodeDescriptor("Bookland",                    "/com/jaspersoft/ireport/components/barcode/icons/Bookland.png",                    "Bookland"));
            barbecueBarcodes.add(new BarcodeDescriptor("Codabar",                     "/com/jaspersoft/ireport/components/barcode/icons/Codabar.png",                     "Codabar"));
            barbecueBarcodes.add(new BarcodeDescriptor("Code128",                     "/com/jaspersoft/ireport/components/barcode/icons/Code128.png",                     "Code128"));
            barbecueBarcodes.add(new BarcodeDescriptor("Code128A",                    "/com/jaspersoft/ireport/components/barcode/icons/Code128A.png",                    "Code128A"));
            barbecueBarcodes.add(new BarcodeDescriptor("Code128B",                    "/com/jaspersoft/ireport/components/barcode/icons/Code128B.png",                    "Code128B"));
            barbecueBarcodes.add(new BarcodeDescriptor("Code128C",                    "/com/jaspersoft/ireport/components/barcode/icons/Code128C.png",                    "Code128C"));
            barbecueBarcodes.add(new BarcodeDescriptor("Code39",                      "/com/jaspersoft/ireport/components/barcode/icons/Code39.png",                      "Code39"));
            barbecueBarcodes.add(new BarcodeDescriptor("Code39 (Extended)",            "/com/jaspersoft/ireport/components/barcode/icons/Code39_extended.png",             "Code39 (Extended)"));
            barbecueBarcodes.add(new BarcodeDescriptor("EAN128",                      "/com/jaspersoft/ireport/components/barcode/icons/EAN128.png",                      "EAN128"));
            barbecueBarcodes.add(new BarcodeDescriptor("EAN13",                       "/com/jaspersoft/ireport/components/barcode/icons/EAN13.png",                       "EAN13"));
            barbecueBarcodes.add(new BarcodeDescriptor("GlobalTradeItemNumber",       "/com/jaspersoft/ireport/components/barcode/icons/GlobalTradeItemNumber.png",       "GlobalTradeItemNumber"));
            barbecueBarcodes.add(new BarcodeDescriptor("Int2of5",                     "/com/jaspersoft/ireport/components/barcode/icons/Int2of5.png",                     "Int2of5"));
            barbecueBarcodes.add(new BarcodeDescriptor("Monarch",                     "/com/jaspersoft/ireport/components/barcode/icons/Monarch.png",                     "Monarch"));
            barbecueBarcodes.add(new BarcodeDescriptor("NW7",                         "/com/jaspersoft/ireport/components/barcode/icons/NW7.png",                         "NW7"));
            barbecueBarcodes.add(new BarcodeDescriptor("PDF417",                      "/com/jaspersoft/ireport/components/barcode/icons/PDF417.png",                      "PDF417"));
            barbecueBarcodes.add(new BarcodeDescriptor("PostNet",                     "/com/jaspersoft/ireport/components/barcode/icons/PostNet.png",                     "PostNet"));
            barbecueBarcodes.add(new BarcodeDescriptor("RandomWeightUPCA",            "/com/jaspersoft/ireport/components/barcode/icons/RandomWeightUPCA.png",            "RandomWeightUPCA"));
            barbecueBarcodes.add(new BarcodeDescriptor("SCC14ShippingCode",           "/com/jaspersoft/ireport/components/barcode/icons/SCC14ShippingCode.png",           "SCC14ShippingCode"));
            barbecueBarcodes.add(new BarcodeDescriptor("ShipmentIdentificationNumber","/com/jaspersoft/ireport/components/barcode/icons/ShipmentIdentificationNumber.png","ShipmentIdentificationNumber"));
            barbecueBarcodes.add(new BarcodeDescriptor("SSCC18",                      "/com/jaspersoft/ireport/components/barcode/icons/SSCC18.png",                      "SSCC18"));
            barbecueBarcodes.add(new BarcodeDescriptor("Std2of5",                     "/com/jaspersoft/ireport/components/barcode/icons/Std2of5.png",                     "Std2of5"));
            barbecueBarcodes.add(new BarcodeDescriptor("UCC128",                      "/com/jaspersoft/ireport/components/barcode/icons/UCC128.png",                      "UCC128"));
            barbecueBarcodes.add(new BarcodeDescriptor("UPCA",                        "/com/jaspersoft/ireport/components/barcode/icons/UPCA.png",                        "UPCA"));
            barbecueBarcodes.add(new BarcodeDescriptor("USD3",                        "/com/jaspersoft/ireport/components/barcode/icons/USD3.png",                        "USD3"));
            barbecueBarcodes.add(new BarcodeDescriptor("USD4",                        "/com/jaspersoft/ireport/components/barcode/icons/USD4.png",                        "USD4"));
            barbecueBarcodes.add(new BarcodeDescriptor("USPS",                        "/com/jaspersoft/ireport/components/barcode/icons/USPS.png",                        "USPS"));
        }
        return barbecueBarcodes;
    }

    private static List<BarcodeDescriptor> barcode4jBarcodes = null;
    public static List<BarcodeDescriptor> getBarcode4jBarcodes()
    {
        if (barcode4jBarcodes == null)
        {
            barcode4jBarcodes = new ArrayList<BarcodeDescriptor>();
            barcode4jBarcodes.add(new BarcodeDescriptor("Codabar",                        "/com/jaspersoft/ireport/components/barcode/icons/Codabar.png",            "Codabar"));
            barcode4jBarcodes.add(new BarcodeDescriptor("Code39",                     "/com/jaspersoft/ireport/components/barcode/icons/Code39.png",                     "Code39"));
            barcode4jBarcodes.add(new BarcodeDescriptor("Code128",                        "/com/jaspersoft/ireport/components/barcode/icons/Code128.png",                        "Code128"));
            barcode4jBarcodes.add(new BarcodeDescriptor("DataMatrix",                     "/com/jaspersoft/ireport/components/barcode/icons/DataMatrix.png",                     "DataMatrix"));
            barcode4jBarcodes.add(new BarcodeDescriptor("EAN128",                    "/com/jaspersoft/ireport/components/barcode/icons/EAN128.png",                    "EAN128"));
            barcode4jBarcodes.add(new BarcodeDescriptor("EAN13",                       "/com/jaspersoft/ireport/components/barcode/icons/EAN13.png",                       "EAN13"));
            barcode4jBarcodes.add(new BarcodeDescriptor("EAN8",                       "/com/jaspersoft/ireport/components/barcode/icons/EAN8.png",                       "EAN8"));
            barcode4jBarcodes.add(new BarcodeDescriptor("Royal Mail Customer",                     "/com/jaspersoft/ireport/components/barcode/icons/RoyalMailCustomer.png",                     "Royal Mail Customer"));
            barcode4jBarcodes.add(new BarcodeDescriptor("USPS Intelligent Mail",                     "/com/jaspersoft/ireport/components/barcode/icons/USPSIntelligentMail.png",                     "USPS Intelligent Mail"));
            barcode4jBarcodes.add(new BarcodeDescriptor("Int2of5",                     "/com/jaspersoft/ireport/components/barcode/icons/Int2of5.png",                     "Int2of5"));
            barcode4jBarcodes.add(new BarcodeDescriptor("UPCA",                        "/com/jaspersoft/ireport/components/barcode/icons/UPCA.png",                        "UPCA"));
            barcode4jBarcodes.add(new BarcodeDescriptor("UPCE",                        "/com/jaspersoft/ireport/components/barcode/icons/UPCE.png",                        "UPCE"));
             barcode4jBarcodes.add(new BarcodeDescriptor("PostNet",                        "/com/jaspersoft/ireport/components/barcode/icons/PostNet.png",                        "PostNet"));
            barcode4jBarcodes.add(new BarcodeDescriptor("PDF417",                        "/com/jaspersoft/ireport/components/barcode/icons/PDF417.png",                        "PDF417"));
        }
        return barcode4jBarcodes;
    }

    public BarcodeDescriptor(String name, String iconPath, String description)
    {
        this(name, (Icon)null, description);
        if (iconPath != null)
        {
            try {
                this.icon = new ImageIcon(getClass().getResource(iconPath));
            } catch (Exception ex){}
        }

    }

    public BarcodeDescriptor(String name, Icon icon, String description)
    {
        this.name = name;
        this.icon = icon;
        this.description = description;
    }

    private String name = "";
    private Icon icon = null;
    private String description = "";

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
