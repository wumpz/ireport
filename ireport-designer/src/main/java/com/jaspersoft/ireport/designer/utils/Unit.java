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
package com.jaspersoft.ireport.designer.utils;

import java.util.HashMap;

/**
 *
 * @author gtoffoli
 */
public class Unit {

    public static final double PIXEL = 1.0;
    public static final double INCHES = 72.0;
    public static final double CENTIMETERS = 28.3464;
    public static final double MILLIMETERS = 2.83464;

    /** Holds value of property unitName. */
    private String unitName;
    private String keyName;

    /** Holds value of property conversionValue. */
    private double conversionValue;

    /** Creates a new instance of Unit */
    public Unit(String unitName, double conversionValue) {
        this(unitName, conversionValue, unitName);
    }
    
    public Unit(String unitName, double conversionValue, String keyName) {
        this.unitName = unitName;
        this.conversionValue = conversionValue;
        this.setKeyName(keyName);
    }

    /** Getter for property unitName.
     * @return Value of property unitName.
     *
     */
    public String getUnitName() {
        return this.unitName;
    }

    /** Setter for property unitName.
     * @param unitName New value of property unitName.
     *
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /** Getter for property conversionValue.
     * @return Value of property conversionValue.
     *
     */
    public double getConversionValue() {
        return this.conversionValue;
    }

    /** Setter for property conversionValue.
     * @param conversionValue New value of property conversionValue.
     *
     */
    public void setConversionValue(double conversionValue) {
        this.conversionValue = conversionValue;
    }

    private static Unit[] units = null;
    
    private static HashMap<String,Unit> unitsMap = null;
    
    public static Unit[] getStandardUnits()
    {
        if (units == null)
        {
            unitsMap = new HashMap<String,Unit>();
            units = new Unit[4];
            units[0] = new Unit("pixels",Unit.PIXEL, "pixels");  
            unitsMap.put("pixels", units[0]);
            units[1] = new Unit("inches",Unit.INCHES, "inches");
            unitsMap.put("inches", units[1]);
            units[2] = new Unit("cm",    Unit.CENTIMETERS, "cm");
            unitsMap.put("cm", units[2]);
            units[3] = new Unit("mm",    Unit.MILLIMETERS, "mm");
            unitsMap.put("mm", units[3]);
        }
        return units;
    }

    public static int getUnitIndex(String unitName)
    {
        Unit[] units = getStandardUnits();
        for (int i=0; i<units.length; ++i)
        {
            if (units[i].getUnitName().equalsIgnoreCase(unitName)) return i;
        }
        return -1;
    }

    public String toString()
    {
        return getUnitName();
    }

    /**
     * convert from pixel to the unit represented by this class
     *
     * @param pixel
     * @return
     */
    public double convert(int pixels)
    {
        return ((double)pixels)/conversionValue;
    }

    /**
     * convert from pixel to the unit represented by this class
     *
     * @param pixel
     * @return
     */
    public long toPixel(double val)
    {
        return (long)(val*conversionValue);
    }

    static public double convertPixelsToInches(long pixels)
	{
		return ((double)pixels)/INCHES;
	}

	static public long convertInchesToPixels(double inches)
	{
		return (long)(inches*INCHES);
	}

	static public double convertPixelsToCentimeters(long pixels)
	{
		return ((double)pixels)/CENTIMETERS;
	}

	static public long convertCentimetersToPixels(double centimeters)
	{
		return (long)(centimeters*CENTIMETERS);
	}

	static public double convertPixelsToMillimeters(long pixels)
	{
		return ((double)pixels)/MILLIMETERS;
	}

	static public long convertMillimetersToPixels(double millimeters)
	{
		return (long)(millimeters*CENTIMETERS);
	}

	static public long convertToPixels(double value, double convert)
	{
		return (long)(value*convert);
	}
        
        static public Unit getUnit(String unitName)
	{
            if (unitsMap == null) getStandardUnits();
            return unitsMap.get(unitName);
        }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}
