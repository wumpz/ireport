/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.designer.data.fieldsproviders.olap;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.olap.result.JROlapMember;
import net.sf.jasperreports.olap.result.JROlapMemberTuple;

/**
 *
 * @version $Id: IROlapMemberTuple.java 0 2010-03-03 18:53:53 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IROlapMemberTuple implements JROlapMemberTuple{

    List<JROlapMember> members = new ArrayList<JROlapMember>();

    public void addMember(JROlapMember a)
    {
        members.add(a);
    }
    
    public JROlapMember[] getMembers() {
        return members.toArray(new JROlapMember[members.size()]);
    }

}
