/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.designer.data.fieldsproviders.olap;

import net.sf.jasperreports.olap.result.JROlapHierarchyLevel;

/**
 *
 * @version $Id: IROlapHierarchyLevel.java 0 2010-03-03 19:06:38 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IROlapHierarchyLevel implements JROlapHierarchyLevel {

    String name = null;
    int depth = 0;

    public IROlapHierarchyLevel(String name, int depth)
    {
        this.name = name;
        this.depth = depth;
    }

    public String getName() {
        return name;
    }

    public int getDepth() {
        return depth;
    }

}
