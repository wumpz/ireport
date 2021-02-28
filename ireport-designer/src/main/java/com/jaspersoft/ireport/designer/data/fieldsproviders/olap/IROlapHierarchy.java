/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.designer.data.fieldsproviders.olap;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.olap.result.JROlapHierarchy;
import net.sf.jasperreports.olap.result.JROlapHierarchyLevel;

/**
 *
 * @version $Id: IROlapHierarchy.java 0 2010-03-03 19:02:26 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IROlapHierarchy implements JROlapHierarchy {
    private String dimensionName = null;
    private String hierarchyUniqueName = null;
    private List<JROlapHierarchyLevel> levels = new ArrayList<JROlapHierarchyLevel>();

    public IROlapHierarchy(String dimensionName)
    {
        this.dimensionName = dimensionName;
    }

    public void addHierarchyLevel(JROlapHierarchyLevel a)
    {
        levels.add(a);
    }


    public JROlapHierarchyLevel[] getLevels() {
        return levels.toArray(new JROlapHierarchyLevel[levels.size()]);
    }

    /**
     * @return the dimensionName
     */
    public String getDimensionName() {
        return dimensionName;
    }

    /**
     * @param dimensionName the dimensionName to set
     */
    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    /**
     * @return the hierarchyUniqueName
     */
    public String getHierarchyUniqueName() {
        return hierarchyUniqueName;
    }

    /**
     * @param hierarchyUniqueName the hierarchyUniqueName to set
     */
    public void setHierarchyUniqueName(String hierarchyUniqueName) {
        this.hierarchyUniqueName = hierarchyUniqueName;
    }

}
