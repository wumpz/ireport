/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.mongodb.connection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.openide.util.NbBundle;

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import java.awt.Color;
import javax.swing.Box;

/**
 *
 * @author Eric Diaz
 *
 */
@SuppressWarnings("serial")
public class MongoDbConnectionEditor extends JPanel implements IReportConnectionEditor {

	private JTextField urlField;

	private JTextField usernameField;

	private JPasswordField passwordField;

	public MongoDbConnectionEditor() {
		initComponents();
	}

	private void initComponents() {
		JPanel propertiesPanel = new JPanel(new GridBagLayout());
		addLabel(propertiesPanel, "uri", 0, 0, 0, 1.0);
		addComponent(propertiesPanel, urlField = new JTextField("mongodb://HOST:27017/DB_NAME"), 1, 0, 0.95, 1.0, 20,
				GridBagConstraints.HORIZONTAL, 2);
		addComponent(propertiesPanel, Box.createHorizontalBox(), 2, 0, 0.05, 1.0, 20,
				GridBagConstraints.HORIZONTAL, 1);
		addLabel(propertiesPanel, "username", 0, 1, 0.0, 0.0);
		addComponent(propertiesPanel, usernameField = new JTextField(10), 1, 1, 0.6, 0.0, 10, GridBagConstraints.HORIZONTAL, 1);
		addComponent(propertiesPanel, Box.createHorizontalBox(), 2, 1, 0.1, 1.0, 20,
				GridBagConstraints.NONE, 1);
		addComponent(propertiesPanel, Box.createHorizontalBox(), 3, 1, 0.3, 1.0, 0,
				GridBagConstraints.HORIZONTAL, 1);
		addLabel(propertiesPanel, "password", 0, 2, 0.0, 0.0);
		addComponent(propertiesPanel, passwordField = new JPasswordField(10), 1, 2, 0.5, 0.0, 0,
				GridBagConstraints.HORIZONTAL, 1);
		addComponent(propertiesPanel, Box.createHorizontalBox(), 2, 2, 0.1, 1.0, 0,
				GridBagConstraints.NONE, 1);
		addComponent(propertiesPanel, Box.createHorizontalBox(), 3, 2, 0.4, 1.0, 0,
				GridBagConstraints.HORIZONTAL, 1);

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
		JLabel urlLabel = new JLabel(NbBundle.getMessage(MongoDbConnection.class, fieldName));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(5, 5, 2, 5);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		panel.add(urlLabel, constraints);
	}

	private void addComponent(JPanel panel, JComponent component, int gridx, int gridy, double weightx, double weighty,
			int ipadx, int fill, int gridwidth) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.fill = fill;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(2, 5, 5, 5);
		constraints.ipadx = ipadx;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.gridwidth = gridwidth;
		panel.add(component, constraints);
	}

	@Override
	public IReportConnection getIReportConnection() {
		MongoDbConnection connection = new MongoDbConnection();
		connection.setMongoURI(urlField.getText().trim());
		connection.setUsername(usernameField.getText().trim());
		connection.setPassword(new String(passwordField.getPassword()).trim());
		return connection;
	}

	@Override
	public void setIReportConnection(IReportConnection connection) {
		if (connection instanceof MongoDbConnection) {
			urlField.setText(((MongoDbConnection) connection).getMongoURI());
			usernameField.setText(((MongoDbConnection) connection).getUsername());
			passwordField.setText(((MongoDbConnection) connection).getPassword());
		}
	}
}