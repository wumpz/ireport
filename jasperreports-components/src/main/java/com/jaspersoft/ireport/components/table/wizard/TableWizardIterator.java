/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.components.table.wizard;

import com.jaspersoft.ireport.designer.wizards.ConnectionSelectionWizardPanel;
import com.jaspersoft.ireport.designer.wizards.DatasetWizardNewDataset;
import com.jaspersoft.ireport.designer.wizards.FieldsSelectionWizardPanel;
import com.jaspersoft.ireport.designer.wizards.GroupingWizardPanel;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.WizardDescriptor;

public final class TableWizardIterator implements WizardDescriptor.Iterator {

    // To invoke this wizard, copy-paste and run the following code, e.g. from
    // SomeAction.performAction():
    /*
    WizardDescriptor.Iterator iterator = new TableWizardIterator();
    WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);
    // {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
    // {1} will be replaced by WizardDescriptor.Iterator.name()
    wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
    wizardDescriptor.setTitle("Your wizard dialog title here");
    Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
    dialog.setVisible(true);
    dialog.toFront();
    boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
    if (!cancelled) {
    // do something
    }
     */
    private int index;
    WizardDescriptor wizard = null;
    private JasperDesign jasperDesign = null;
    List<WizardDescriptor.Panel> panels = null;

    public void initialize(WizardDescriptor wizard, JasperDesign jd)
    {
        this.wizard = wizard;
        this.jasperDesign = jd;

        this.wizard.putProperty("jasperdesign", jd);
    }

    public void appendWizardPanel(WizardDescriptor.Panel panel)
    {
        List<WizardDescriptor.Panel> l = new ArrayList<WizardDescriptor.Panel>();
        l.add(panel);
        appendWizardPanels(l);
    }

    public void appendWizardPanels(List<WizardDescriptor.Panel> newPanels)
    {

        getPanels().addAll(newPanels);

        for (WizardDescriptor.Panel panel : getPanels())
        {
            Component c = panel.getComponent();
            // Default step name to component name of panel.
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                // Turn on subtitle creation on each step
                jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                // Show steps on the left side with the image on the background
                jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                // Turn on numbering of all steps
                jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
            }
        }
    }

    private List<WizardDescriptor.Panel> tableFromDatasetPanelSet = null;
    private List<WizardDescriptor.Panel>  emptyTablePanelSet = null;
    private List<WizardDescriptor.Panel> getPanels() {

        if (panels == null) {
            panels = new ArrayList<WizardDescriptor.Panel>();
            appendWizardPanel(new TableWizardPanel1(wizard, this));
            tableFromDatasetPanelSet = new ArrayList<WizardDescriptor.Panel>();
            tableFromDatasetPanelSet.add(new FieldsSelectionWizardPanel(wizard));
            tableFromDatasetPanelSet.add(new ElementConnectionWizardPanel(wizard));
            tableFromDatasetPanelSet.add(new TableWizardPanelStyle(wizard));
            // Dataset connection
            // Table style
            appendWizardPanels(tableFromDatasetPanelSet);
            updateStepLabels();
            wizard.putProperty("WizardPanel_contentData", ( (JComponent)(tableFromDatasetPanelSet.get(0).getComponent()) ).getClientProperty("WizardPanel_contentData"));
        }
        return panels;
    }

    private int lastSelectedType = 0;

    public void updatePanels()
    {
        Integer datasetType = (Integer)wizard.getProperty("table_type");
        if (datasetType == null) return;
        if (lastSelectedType == datasetType.intValue()) return;

        lastSelectedType = datasetType.intValue();

        //  Based on the selected panel, modify the set of panels...
        // 1. Remove the set of panels after the first one...

        WizardDescriptor.Panel panel = getPanels().get(0);
        getPanels().clear();
        appendWizardPanel(panel);

        if (lastSelectedType == 0) // Connection set...
        {
            appendWizardPanels(tableFromDatasetPanelSet);
        }
        else if (lastSelectedType == 1)
        {
            // Nothing to add...
            if (emptyTablePanelSet == null)
            {
                emptyTablePanelSet = new ArrayList<WizardDescriptor.Panel>();
                emptyTablePanelSet.add(new TableWizardPanelStyle(wizard));
            }

            appendWizardPanels(emptyTablePanelSet);
        }

        updateStepLabels();
        wizard.putProperty("WizardPanel_contentData", ( (JComponent)(getPanels().get(0).getComponent()) ).getClientProperty("WizardPanel_contentData"));
    }

    private void updateStepLabels()
    {
        if (panels == null) return;
        // Adjust the step name for all the current available panels...
        String[] steps = new String[panels.size()];
        int i=0;
        for (WizardDescriptor.Panel pan : panels)
        {
            steps[i] = pan.getComponent().getName();
            i++;
        }

        i=0;
        for (WizardDescriptor.Panel pan : panels)
        {
            if (pan.getComponent() instanceof JComponent)
            {
                ((JComponent)pan.getComponent()).putClientProperty("WizardPanel_contentData", steps);
                ((JComponent)pan.getComponent()).putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
            }
            i++;
        }
    }

    public WizardDescriptor.Panel current() {
        return getPanels().get(index);
    }

    public String name() {
        return (index + 1) + " of " + getPanels().size();
    }

    public boolean hasNext() {
        // After the first one there will be always something even if we don't know what yet...
        return index < getPanels().size() - 1;
    }

    public boolean hasPrevious() {
        return index > 0;
    }

    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }


    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then uncomment
    // the following and call when needed: fireChangeEvent();

    private Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
    public final void addChangeListener(ChangeListener l) {
    synchronized (listeners) {
    listeners.add(l);
    }
    }
    public final void removeChangeListener(ChangeListener l) {
    synchronized (listeners) {
    listeners.remove(l);
    }
    }
    protected final void fireChangeEvent() {
    Iterator<ChangeListener> it;
    synchronized (listeners) {
    it = new HashSet<ChangeListener>(listeners).iterator();
    }
    ChangeEvent ev = new ChangeEvent(this);
    while (it.hasNext()) {
    it.next().stateChanged(ev);
    }
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
    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }
}
