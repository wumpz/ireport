/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.transformer;

import java.awt.Component;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: AbstractTransformer.java 0 2010-04-15 10:37:57 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public interface Transformer {

    public JasperDesign transform(String filename) throws TransformException;
    public String getName();
    public Component getComponent();
}
