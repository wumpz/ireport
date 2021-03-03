/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.hadoop.hive.connection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.log4j.Logger;

import org.openide.util.NbBundle;

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.connection.gui.ConnectionDialog;
import java.awt.Color;
import java.awt.Container;
import javax.swing.*;

/**
 *
 * @author Eric Diaz
 *
 */
public class HiveConnectionEditor extends JPanel implements IReportConnectionEditor {

	private JTextField urlField;

	private Logger logger = Logger.getLogger(HiveConnectionEditor.class);

	public HiveConnectionEditor() {
		initComponents();
	}

	private void initComponents() {
		JPanel propertiesPanel = new JPanel(new GridBagLayout());
		addLabel(propertiesPanel, "hiveURL", 0, 0, 0, 1.0);
		addComponent(propertiesPanel, urlField = new JTextField("jdbc:hive://HOST:10000/default"), 1, 0, 0.95, 1.0, 20,
				GridBagConstraints.HORIZONTAL);
		addComponent(propertiesPanel, Box.createHorizontalBox(), 2, 0, 0.05, 1.0, 20,
				GridBagConstraints.HORIZONTAL);

		GridBagConstraints contraints = new GridBagConstraints();
		contraints.gridx = 0;
		contraints.gridy = 0;
		contraints.fill = GridBagConstraints.HORIZONTAL;
		contraints.anchor = GridBagConstraints.WEST;
		setLayout(new GridBagLayout());
		add(propertiesPanel, contraints);

		contraints = new GridBagConstraints();
		contraints.gridx = 0;
		contraints.gridy = 1;
		contraints.fill = GridBagConstraints.NONE;
		contraints.anchor = GridBagConstraints.WEST;
		contraints.weightx = 1.0;
		contraints.weighty = 1.0;
		JPanel panel = new JPanel();
		add(panel, contraints);
	}

	private void addLabel(JPanel panel, String fieldName, int gridx, int gridy, double weightx, double weighty) {
		JLabel urlLabel = new JLabel(NbBundle.getMessage(HiveConnection.class, fieldName));

		GridBagConstraints contraints = new GridBagConstraints();
		contraints.gridx = gridx;
		contraints.gridy = gridy;
		contraints.fill = GridBagConstraints.NONE;
		contraints.insets = new Insets(5, 5, 2, 5);
		contraints.anchor = GridBagConstraints.WEST;
		contraints.weightx = weightx;
		contraints.weighty = weighty;
		panel.add(urlLabel, contraints);
	}

	private void addComponent(JPanel panel, JComponent component, int gridx, int gridy, double weightx, double weighty,
			int ipadx, int fill) {
		GridBagConstraints contraints = new GridBagConstraints();
		contraints.gridx = gridx;
		contraints.gridy = gridy;
		contraints.fill = fill;
		contraints.anchor = GridBagConstraints.WEST;
		contraints.insets = new Insets(2, 5, 5, 5);
		contraints.ipadx = ipadx;
		contraints.weightx = weightx;
		contraints.weighty = weighty;
		panel.add(component, contraints);
	}

	@Override
	public IReportConnection getIReportConnection() {
		HiveConnection connection = new HiveConnection();
		connection.setUrl(urlField.getText().trim());
		return connection;
	}

	@Override
	public void setIReportConnection(IReportConnection connection) {
		if (connection instanceof HiveConnection) {
			HiveConnection hiveConnection = (HiveConnection) connection;
			urlField.setText(hiveConnection.getUrl());
		}
	}
}