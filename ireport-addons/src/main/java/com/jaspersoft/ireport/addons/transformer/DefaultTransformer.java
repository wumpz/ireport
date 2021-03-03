/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.transformer;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @version $Id: AbstractTransformer.java 0 2010-05-31 18:22:50 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public abstract class DefaultTransformer implements Transformer {

    public JasperDesign transform(String srcFileName) throws TransformException
    {
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(srcFileName);
            return transform(jasperDesign);
        } catch (Exception ex)
        {
            throw new TransformException(ex);
        }
    }

    public JasperDesign transform(JasperDesign jd) throws TransformException
    {
        return jd;
    }
}
