/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.layers;

import java.util.List;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: LayersChangedListener.java 0 2010-02-27 11:09:05 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public interface LayersChangedListener {



    /**
     * This is called when, i.e. the context JasperDesign changes.
     * @param jd
     * @param layers
     */
    public void layersChanged(LayersChangedEvent event);

}
