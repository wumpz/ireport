/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.designer.data.fieldsproviders.olap;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.olap.result.JROlapHierarchy;
import net.sf.jasperreports.olap.result.JROlapMemberTuple;
import net.sf.jasperreports.olap.result.JROlapResultAxis;

/**
 *
 * @version $Id: IROlapAxis.java 0 2010-03-03 18:50:51 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IROlapResultAxis implements JROlapResultAxis {

    List<JROlapHierarchy> hierachies = new ArrayList<JROlapHierarchy>();
    List<JROlapMemberTuple> tuples = new ArrayList<JROlapMemberTuple>();


    public JROlapHierarchy[] getHierarchiesOnAxis() {
        return hierachies.toArray(new JROlapHierarchy[hierachies.size()]);
    }

    public void addHierarchy(JROlapHierarchy a)
    {
        hierachies.add(a);
    }

    public int getTupleCount() {
        return tuples.size();
    }

    public JROlapMemberTuple getTuple(int i) {
        return tuples.get(i);
    }

    public void addTuple(JROlapMemberTuple a)
    {
        tuples.add(a);
    }

}
