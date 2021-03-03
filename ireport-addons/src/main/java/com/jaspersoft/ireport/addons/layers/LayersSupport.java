/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.layers;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JasperDesignActivatedListener;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.model.ObjectState;
import org.openide.util.Exceptions;

/**
 * This class holds the layers model and some utilities methods
 * to load and store layers information in the jrxml.
 * Since there is not property change listeners for the jasperdesign properties map,
 * all the operations about layers should be performed using this class.
 * Listeners will be correctly notified about model changes to the layers.
 *
 * An improvement may be done by handlig an hashmap to store the layers list by
 * JrxmlVisualView: each time there is a switch of context we load the list again.
 * But this is good enough since this is a very fast operation when layers are not many.
 *
 * @version $Id: LayersSupport.java 0 2010-02-27 10:53:43 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class LayersSupport implements JasperDesignActivatedListener, ObjectSceneListener {

    private ReportObjectScene scene = null;
    private static LayersSupport singleton = null;
    private List<Layer> layers = new ArrayList<Layer>();

    private final Set<LayersChangedListener>layersChangedListeners = new HashSet<LayersChangedListener>(1);

     public final void addLayersChangedListener(LayersChangedListener l) {
        synchronized (layersChangedListeners) {
            layersChangedListeners.add(l);
        }
    }

    public final void removeLayersChangedListener(LayersChangedListener l) {
        synchronized (layersChangedListeners) {
            layersChangedListeners.remove(l);
        }
    }

    public final void fireLayersChangedListenerEvent(LayersChangedEvent evt) {
        Iterator<LayersChangedListener> it;
        synchronized (layersChangedListeners) {
            it = new HashSet<LayersChangedListener>(layersChangedListeners).iterator();
        }

        while (it.hasNext()) {
            it.next().layersChanged(evt);
        }
    }

    // This listener is used to save the layers when a single layer property changes...
    private PropertyChangeListener layersPropertyCahangeListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {

            if (evt.getPropertyName() != null &&
                evt.getPropertyName().equals( Layer.PROPERTY_VISIBLE))
            {
                if (evt.getSource() instanceof Layer)
                {
                    Layer layer = (Layer) evt.getSource();
                    // update elements....
                    List<JRDesignElementWidget> widgets = getLayerWidgets(layer);
                    for (JRDesignElementWidget w : widgets)
                    {
                        w.setVisible(layer.isVisible());
                        //w.getSelectionWidget().setVisible(layer.isVisible() );
                        w.revalidate(layer.isVisible());
                        w.getScene().validate();
                    }
                }
            }
            else if (evt.getPropertyName() != null &&
                evt.getPropertyName().equals( Layer.PROPERTY_PRINT_WHEN_EXPRESSION))
            {
                if (evt.getSource() instanceof Layer)
                {
                    Layer layer = (Layer) evt.getSource();
                    // update elements....
                    List<JRDesignElement> elements = getLayerElements(layer);
                    for (JRDesignElement element : elements)
                    {
                        // Create a new exp copy for each element!
                        JRDesignExpression exp = Misc.createExpression("java.lang.Boolean", layer.getPrintWhenExpression());
                        element.setPrintWhenExpression(exp);
                    }
                }
            }
            saveLayers();
        }
    };

    public void addElementToLayer(JRDesignElement element, Layer layer)
    {
        element.setPrintWhenExpression(Misc.createExpression("java.lang.Boolean", layer.getPrintWhenExpression()));
        if (!layer.isBackgroundLayer())
        {
            element.getPropertiesMap().setProperty("ireport.layer", ""+layer.getId());
        }
        else
        {
            element.getPropertiesMap().removeProperty("ireport.layer");
        }
    }


    /**
     * Get an instance of the LayersSupport class, which is a singleton
     * and holds the model for the layers.
     *
     * @return
     */
    public static LayersSupport getInstance()
    {
        if (singleton == null)
        {
            singleton = new LayersSupport();
        }
        return singleton;
    }

    /**
     * Private constructor. Use #getInstance() to get this class.
     */
    private LayersSupport()
    {
        IReportManager.getInstance().addJasperDesignActivatedListener(this);
        jasperDesignActivated(IReportManager.getInstance().getActiveReport());
    }

    /**
     * This is the current activa jasper design...
     */
    private JasperDesign jasperDesign = null;

    /**
     *  Saves the layers in the current jasperdesign model
     */
    public void saveLayers()
    {
        if (getJasperDesign() == null) return;

        java.util.Properties props = new java.util.Properties();
        List<Layer> theLayers = getLayers();

        int k=0;
        for (Layer l : theLayers)
        {
           props.put("layer." + k + ".id",  l.getId() +"" );
           props.put("layer." + k + ".name",  l.getName() );
           props.put("layer." + k + ".visible", ""+ l.isVisible() );
           if (l.getPrintWhenExpression() != null && l.getPrintWhenExpression().trim().length() > 0)
           {
            props.put("layer." + k + ".printWhenExpression", l.getPrintWhenExpression());
           }
           k++;
        }


        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            props.store( out, "");
            String s = out.toString();

            // XML ignores \n in attributes, so we encode \n in our string.
            // a \n before the string callouts. must be reconverted on loading.

            s = Misc.string_replace("", "\r", s);
            s = Misc.string_replace("\\nlayer.", "\nlayer.", s);
            s = Misc.string_replace("", "\n", s);

            getJasperDesign().setProperty("ireport.layers", s);

            Misc.getViewForJasperDesign(getJasperDesign()).getEditorSupport().notifyModified();

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Load the layers from the current jasperdesign model
     * @return the list of layers, or an empty list if no model is currently active
     */
    public List<Layer> loadLayers()
    {
        List<Layer> list = new ArrayList<Layer>();
        JasperDesign jd = getJasperDesign();
        if (jd == null) return list;

        String s = jd.getProperty("ireport.layers");

        // Restore new lines for properties.
        s = Misc.string_replace("\nlayer.", "\\nlayer.", s);

        // Add background
        Layer backgroundLayer = new Layer();
        backgroundLayer.setBackgroundLayer(true);
        backgroundLayer.setName("Background");
        list.add(backgroundLayer);

        if (s != null)
        {
            Properties props = new Properties();
            try {
                props.load(new ByteArrayInputStream(s.getBytes()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            int i=0;

            while ( props.containsKey("layer."+i+".id"))
            {
                Layer layer = new Layer();
                layer.setId(Integer.valueOf(props.getProperty("layer."+i+".id")));
                layer.setName(props.getProperty("layer."+i+".name"));
                layer.setVisible(Boolean.valueOf(props.getProperty("layer."+i+".visible")));
                layer.setPrintWhenExpression(props.getProperty("layer."+i+".printWhenExpression"));

                
                if (layer.getId() == 0)
                {
                    backgroundLayer.setVisible(Boolean.valueOf(props.getProperty("layer."+i+".visible")));
                }
                else
                {
                    list.add(layer);
                }
                i++;
            }
        }

        return list;
    }

    public void jasperDesignActivated(JasperDesign jd) {
        setJasperDesign(jd);
    }

    /**
     * @return the jasperDesign
     */
    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    /**
     * @param jasperDesign the jasperDesign to set
     */
    public void setJasperDesign(JasperDesign jd) {

        if (this.jasperDesign == jd) return;

        this.jasperDesign = jd;

        // Remove old listeners..
        if (getScene() != null)
        {
            getScene().removeObjectSceneListener(this, ObjectSceneEventType.OBJECT_ADDED);
        }

        JrxmlVisualView view = Misc.getViewForJasperDesign(jasperDesign);
        if (view != null)
        {
            setScene(view.getReportDesignerPanel().getScene());
            if (getScene() != null)
            {
                getScene().addObjectSceneListener(this, ObjectSceneEventType.OBJECT_ADDED);
            }
        }

        // remove change listener from the current layers...
        List<Layer> currentLayers = getLayers();
        for (Layer layer : currentLayers)
        {
            layer.getEventSupport().removePropertyChangeListener(layersPropertyCahangeListener);
        }

        if (this.getJasperDesign() != null)
        {
            setLayers(loadLayers());
        }
        else
        {
            setLayers(new ArrayList<Layer>());
        }

        // Add change listeners to the layers...
        currentLayers = getLayers();
        for (Layer layer : currentLayers)
        {
            layer.getEventSupport().addPropertyChangeListener(layersPropertyCahangeListener);
        }

        // Update the visibility of all the elements...
        updateAllLayers();

        // Notify layer listeners...
        fireLayersChangedListenerEvent(new LayersChangedEvent(LayersChangedEvent.LAYERS_LIST_CHANGED, getJasperDesign()));
    }

    /**
     * @return the layers
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * @param layers the layers to set
     */
    private void setLayers(List<Layer> layers) {
        this.layers = layers;
    }


    public void addLayer(Layer l) {
        if (getJasperDesign() == null || l == null || l.isBackgroundLayer()) return;

        layers.add(l);

        l.getEventSupport().addPropertyChangeListener(layersPropertyCahangeListener);
        saveLayers();

        List<Layer> toAddLayers = new ArrayList<Layer>();
        toAddLayers.add(l);

        LayersChangedEvent evt = new LayersChangedEvent(LayersChangedEvent.LAYERS_ADDED, getJasperDesign(),toAddLayers);
        fireLayersChangedListenerEvent(evt);
    }


    public void removeLayer(Layer l) {
        if (getJasperDesign() == null || l == null || l.isBackgroundLayer()) return;

        List<Layer> toRemoveLayers = new ArrayList<Layer>();
        toRemoveLayers.add(l);

        removeLayers(toRemoveLayers);
    }

    public void removeLayers(List<Layer> toRemoveLayers) {

        if (getJasperDesign() == null || toRemoveLayers == null) return;

        for (int i=0; i<toRemoveLayers.size(); ++i)
        {
            Layer l = toRemoveLayers.get(i);
            l.getEventSupport().removePropertyChangeListener(layersPropertyCahangeListener);
            if (l.isBackgroundLayer())
            {
                toRemoveLayers.remove(l);
                i--;
            }
        }

        if (toRemoveLayers.size() > 0)
        {
            layers.removeAll(toRemoveLayers);
            saveLayers();

            // update elements....
            for (Layer layer : toRemoveLayers)
            {
                List<JRDesignElementWidget> widgets = getLayerWidgets(layer);
                for (JRDesignElementWidget w : widgets)
                {
                    w.getElement().getPropertiesMap().removeProperty("ireport.layer");
                    w.setVisible(true);
                    w.getSelectionWidget().setVisible(true );
                    w.revalidate(true);
                    w.getScene().validate();
                }
            }

            LayersChangedEvent evt = new LayersChangedEvent(LayersChangedEvent.LAYERS_REMOVED, getJasperDesign(),toRemoveLayers);
            fireLayersChangedListenerEvent(evt);
        }
        /*
         l.getEventSupport().removePropertyChangeListener(layersPropertyChangeListener);
        LayerItemPanel p = getLayerPanelOf(l);
        if (p != null)
        {
            remove(p);
        }
        if (selectedItem == p)
        {
            List<Layer> selectedLayers = getSelectedLayers();
            if (selectedLayers.size() > 0)
            {
                selectedItem = getLayerPanelOf(selectedLayers.get(0));
            }
            else
            {
                selectedItem = null;
            }
        }
        updateUI();

         */

        // Update all the elements...

    }

    public List<JRDesignElementWidget> getLayerWidgets(Layer layer)
    {
        List<JRDesignElementWidget> list = new ArrayList<JRDesignElementWidget>();
        List<JRDesignElement> elements = ModelUtils.getAllElements(getJasperDesign(), false);

        // Get the main scene..
        if (getScene() == null) return list;
        
        for (JRDesignElement element : elements)
        {
            if ( (!element.getPropertiesMap().containsProperty("ireport.layer") && layer.isBackgroundLayer()) ||  // Background
                 (element.getPropertiesMap().getProperty("ireport.layer") != null &&                              // or specified layer
                element.getPropertiesMap().getProperty("ireport.layer").equals(layer.getId()+"")))
            {
                JRDesignElementWidget w = getScene().findElementWidget(element);
                if (w != null)
                list.add(w);
            }
        }

        return list;

    }

    public List<JRDesignElement> getLayerElements(Layer layer)
    {
        List<JRDesignElement> list = new ArrayList<JRDesignElement>();
        List<JRDesignElement> elements = ModelUtils.getAllElements(getJasperDesign(), false);

        for (JRDesignElement element : elements)
        {
            if ( (!element.getPropertiesMap().containsProperty("ireport.layer") && layer.isBackgroundLayer()) ||  // Background
                 (element.getPropertiesMap().getProperty("ireport.layer") != null &&                              // or specified layer
                element.getPropertiesMap().getProperty("ireport.layer").equals(layer.getId()+"")))
            {

                list.add(element);
            }
        }

        return list;

    }

    public Layer getLayer(int id)
    {
        for (Layer l : getLayers())
        {
            if (l.getId() == id) return l;
        }
        return null;
    }

    public void objectAdded(ObjectSceneEvent event, Object addedObject) {
        if (addedObject instanceof JRDesignElement)
        {
            JRDesignElement element = (JRDesignElement)addedObject;
            if (element.getPropertiesMap().getProperty("ireport.layer") != null)
            {
                // Update the visibiliti..
                Layer layer = null;
                try {
                  layer = getLayer(Integer.valueOf(element.getPropertiesMap().getProperty("ireport.layer")));
                } catch (Exception ex) {}
                if (layer != null)
                {
                     JRDesignElementWidget w = ((ReportObjectScene)event.getObjectScene()).findElementWidget(element);
                     if (w != null)
                     {
                         w.setVisible(layer.isVisible());
                         //w.getSelectionWidget().setVisible(layer.isVisible() );
                         w.revalidate(layer.isVisible());
                         w.getScene().validate();
                     }
                }
            }
        }
    }

    public void objectRemoved(ObjectSceneEvent event, Object removedObject) {
    }

    public void objectStateChanged(ObjectSceneEvent event, Object changedObject, ObjectState previousState, ObjectState newState) {
    }

    public void selectionChanged(ObjectSceneEvent event, Set<Object> previousSelection, Set<Object> newSelection) {
    }

    public void highlightingChanged(ObjectSceneEvent event, Set<Object> previousHighlighting, Set<Object> newHighlighting) {
    }

    public void hoverChanged(ObjectSceneEvent event, Object previousHoveredObject, Object newHoveredObject) {
    }

    public void focusChanged(ObjectSceneEvent event, Object previousFocusedObject, Object newFocusedObject) {
    }

    public void updateAllLayers()
    {
        if (getScene() == null || jasperDesign == null) return;
        List<JRDesignElement> elements = ModelUtils.getAllElements(jasperDesign, false);
        for (JRDesignElement element : elements)
        {
            String layerProp = element.getPropertiesMap().getProperty("ireport.layer");
            if (layerProp != null)
            {
                try {
                    Layer layer = getLayer(Integer.valueOf(layerProp));
                    if (layer != null)
                    {
                        // update widget visibility...
                        JRDesignElementWidget w = getScene().findElementWidget(element);
                        if (w.isVisible() != layer.isVisible())
                        {
                            w.setVisible(layer.isVisible());
                            w.revalidate(true);
                        }
                    }
                } catch (Exception ex) {}
            }
        }
        getScene().validate();
    }

    /**
     * @return the scene
     */
    public ReportObjectScene getScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    public void setScene(ReportObjectScene scene) {
        this.scene = scene;
    }

}
