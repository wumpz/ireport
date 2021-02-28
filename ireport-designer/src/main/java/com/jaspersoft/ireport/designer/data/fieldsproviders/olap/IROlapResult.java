/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.designer.data.fieldsproviders.olap;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.olap.result.JROlapCell;
import net.sf.jasperreports.olap.result.JROlapResult;
import net.sf.jasperreports.olap.result.JROlapResultAxis;

/**
 *
 * @version $Id: IROlapResult.java 0 2010-03-03 18:48:51 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IROlapResult implements JROlapResult {

    List<JROlapResultAxis> axes = new ArrayList<JROlapResultAxis>();

    public JROlapResultAxis[] getAxes() {
        return axes.toArray(new JROlapResultAxis[axes.size()]);
    }

    public void addAxis(JROlapResultAxis a)
    {
        axes.add(a);
    }


    public JROlapCell getCell(int[] ints) {
        throw new UnsupportedOperationException("Not supported.");
    }

}
