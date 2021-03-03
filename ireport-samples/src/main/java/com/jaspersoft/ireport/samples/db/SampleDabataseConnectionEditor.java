/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.samples.db;

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.connection.gui.JDBCConnectionEditor;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @version $Id: SampleDabataseConnectionEditor.java 0 2010-01-11 16:34:08 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 * This class does not override the getIReportConnection() which will return a simple JDBC connection!
 *
 */
public class SampleDabataseConnectionEditor extends JDBCConnectionEditor {

    JPanel mainUIcomponent = null;

    public SampleDabataseConnectionEditor()
    {
        super();
        this.setIReportConnection(new SampleDatabaseConnection());
        // Switch to simple mode...
        mainUIcomponent = (JPanel)getComponent(0);
        removeAll();
        SampleDatabaseConnectionHeaderPanel p = new SampleDatabaseConnectionHeaderPanel();
        p.setMainPanel(mainUIcomponent);
        add(p, BorderLayout.CENTER);
    }

    @Override
    public IReportConnection getIReportConnection() {
        IReportConnection conn = super.getIReportConnection();
        SampleDatabaseConnection conn2 = new SampleDatabaseConnection();
        conn2.loadProperties( conn2.getProperties() );
        return conn2;
    }

    


}
