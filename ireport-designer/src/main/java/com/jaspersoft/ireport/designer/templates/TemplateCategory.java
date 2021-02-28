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
package com.jaspersoft.ireport.designer.templates;

/**
 *
 * @author gtoffoli
 */
public class TemplateCategory implements Comparable<TemplateCategory> {
    
    public static final String CATEGORY_ALL_REPORTS = "All_reports";
    public static final String CATEGORY_OTHER_REPORTS = "Other_reports";

    private String category = CATEGORY_OTHER_REPORTS;
    private String subCategory = "";

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category == null ? CATEGORY_OTHER_REPORTS : category;
    }

    /**
     * @return the subCategory
     */
    public String getSubCategory() {
        return subCategory;
    }

    /**
     * @param subCategory the subCategory to set
     */
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory == null ? "" : subCategory;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof TemplateCategory)
        {
            TemplateCategory cat = (TemplateCategory)obj;
            if (cat.getCategory().equals(getCategory()) &&
                cat.getSubCategory().equals(getSubCategory()))
            {
                    return true;
            }
            return false;
        }

        return super.equals(obj);
    }

    public int compareTo(TemplateCategory o) {

        int c = getCategory().compareTo(getCategory());
        if (c == 0) return getSubCategory().compareTo(o.getSubCategory());
        return c;
    }



}
