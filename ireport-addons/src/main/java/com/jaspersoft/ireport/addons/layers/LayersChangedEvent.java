/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.layers;

import java.util.List;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: LayersChangedEvent.java 0 2010-02-27 11:11:18 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class LayersChangedEvent {

    /**
     * A new layer as been added to the list.
     * Check for what layer has been added with getChangedLayers
     */
    public static final int LAYERS_ADDED = 1;
    public static final int LAYERS_REMOVED = 2;

    /**
     * The list of layers currently active is changed
     * The new list of layers can be get with LayersSupport.getInstance().getLayers();
     */
    public static final int LAYERS_LIST_CHANGED = 3;

    private List<Layer> changedLayers = null;
    private int type = LAYERS_LIST_CHANGED;
    private JasperDesign jasperDesign = null;

    public LayersChangedEvent(int type, JasperDesign jd, List<Layer> changedLayers)
    {
        this.type = type;
        this.changedLayers = changedLayers;
        this.jasperDesign = jd;
    }

    public LayersChangedEvent(int type,  JasperDesign jd)
    {
        this(type, jd,  null);
    }


    /**
     * This is a shortcut for:
     * LayersSupport.getInstance().getLayers()
     */
    public List<Layer> getLayers()
    {
        return LayersSupport.getInstance().getLayers();
    }

    /**
     * Valid only for events of type LAYERS_ADDED and LAYERS_REMOVED
     * @return the changedLayer
     */
    public List<Layer> getChangedLayers() {
        return changedLayers;
    }

    /**
     * @param changedLayer the changedLayer to set
     */
    public void setChangedLayers(List<Layer> changedLayers) {
        this.changedLayers = changedLayers;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * The current active JasperDesign or null of no jasperdesign is currently active
     * @return the jasperDesign
     */
    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    /**
     * @param jasperDesign the jasperDesign to set
     */
    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }

}
