package com.jaspersoft.ireport.designer.actions;

import java.util.ArrayList;
import javax.swing.JMenuItem;
import org.openide.util.actions.NodeAction;




/**
 *
 * @author gtoffoli
 */
public abstract class AbstractOrderChangeAction extends NodeAction {

    JMenuItem item = null;

    static ArrayList<AbstractOrderChangeAction> listeners = new ArrayList<AbstractOrderChangeAction>();
    
    public static void fireChangeOrder()
    {
        for (AbstractOrderChangeAction listener : listeners)
        {
            listener.orderChanged();
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        listeners.add(this);
    }

    @Override
    public JMenuItem getPopupPresenter() {
        item = super.getPopupPresenter();
        item.setIcon(getIcon());
        item.setEnabled( enable(getActivatedNodes()));
        setEnabled(enable(getActivatedNodes()));

        return item;
    }

    public void orderChanged() {

        boolean enabled = enable(getActivatedNodes());
        item.setEnabled(enabled);
        setEnabled(enabled);

    }
}
