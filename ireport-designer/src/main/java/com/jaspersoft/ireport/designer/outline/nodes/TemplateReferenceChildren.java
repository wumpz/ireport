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
package com.jaspersoft.ireport.designer.outline.nodes;

import com.jaspersoft.ireport.designer.utils.ExpressionFileResolver;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlTemplateLoader;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class TemplateReferenceChildren extends Index.KeysChildren implements PropertyChangeListener {

    JasperDesign jd = null;
    JRDesignReportTemplate template = null;
    private Lookup doLkp = null;
    
    public TemplateReferenceChildren(JasperDesign jd, JRDesignReportTemplate template, Lookup doLkp) {
        this(jd, template, jd.getMainDesignDataset(),doLkp);
        this.template = template;
    }
    @SuppressWarnings("unchecked")
    public TemplateReferenceChildren(JasperDesign jd, JRDesignReportTemplate template,JRDesignDataset dataset, Lookup doLkp) {
        super(new ArrayList());
        this.jd = jd;
        this.doLkp=doLkp;
        this.template = template;
        this.template.getEventSupport().addPropertyChangeListener(this);
    }

    /*
    @Override
    protected List<Node> initCollection() {
        return recalculateKeys();
    }
    */
    
    
    protected Node[] createNodes(Object key) {

        if (key instanceof JRDesignStyle)
        {
            return new Node[]{new NamedStyleNode((JRDesignStyle)key, doLkp)};
        }
        return null;
    }
    
    
    
    @Override
    protected void addNotify() {
        super.addNotify();
        recalculateKeys();
    }
    
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys() {
        
        List l = (List)lock();
        l.clear();
        if (template != null)
        {
            String fileNameExp = Misc.getExpressionText(template.getSourceExpression());
            if (fileNameExp != null && fileNameExp.length() > 0)
            {
                ExpressionFileResolver resolver = new ExpressionFileResolver(
                        (JRDesignExpression)template.getSourceExpression(),jd.getMainDesignDataset(), jd);
                        
                File f = resolver.resolveFile(null);
                if (f!=null && f.exists())
                {
                    try {
                        // try to load this jrtx template...
                        JRSimpleTemplate template = (JRSimpleTemplate) JRXmlTemplateLoader.load(new FileInputStream(f));

                        JRStyle[] styles = template.getStyles();
                        for (int i=0; i<styles.length; ++i)
                        {
                            l.add(styles[i]);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        update();
    }
    
    @SuppressWarnings("unchecked")
    public void reorder() { 
            Mutex.Action action = new Mutex.Action(){ 
                public Object run(){ 
                    Index.Support.showIndexedCustomizer(TemplateReferenceChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignReportTemplate.PROPERTY_SOURCE_EXPRESSION ))
        {
            recalculateKeys();
        }
    }
}
