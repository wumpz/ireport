package com.jaspersoft.ireport.mongodb.connection;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignQuery;

import org.openide.util.NbBundle;

import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.connection.JDBCConnection;
import com.jaspersoft.ireport.designer.data.WizardFieldsProvider;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.mongodb.Installer;
import com.jaspersoft.ireport.mongodb.designer.MongoDbFieldsProvider;
import com.jaspersoft.mongodb.MongoDbDataSource;
import com.jaspersoft.mongodb.connection.MongoDbConnectionManager;

/**
 * 
 * @author Eric Diaz
 * 
 *         It extends from JDBCConnection to allow iReport to close the
 *         connection
 * 
 */
@SuppressWarnings("rawtypes")
public class MongoDbConnection extends JDBCConnection implements WizardFieldsProvider {
	private static final String MONGO_URI_KEY = "MongoDB URI";

	private static final String USERNAME = "username";

	private static final String PASSWORD = "password";

	private String mongoURI;

	private String username;

	private String password;

	private com.jaspersoft.mongodb.connection.MongoDbConnection currentConnection;

	public MongoDbConnection() {
		super();
		setName(NbBundle.getMessage(MongoDbConnection.class, "connectionName"));
	}

	@Override
	public String getDescription() {
		return NbBundle.getMessage(MongoDbConnection.class, "connectionType");
	}

	@Override
	public IReportConnectionEditor getIReportConnectionEditor() {
		return new MongoDbConnectionEditor();
	}

	@Override
	public Connection getConnection() {
		try {
			return createConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private com.jaspersoft.mongodb.connection.MongoDbConnection createConnection() throws Exception {
		MongoDbConnectionManager connectionManager = Installer.getConnectionManager();
		if (currentConnection != null) {
			connectionManager.returnConnection(currentConnection);
		}
		connectionManager.setMongoURI(mongoURI);
		connectionManager.setUsername(username);
		connectionManager.setPassword(password);
		return connectionManager.borrowConnection();
	}

	public String getQueryLanguage() {
		return MongoDbDataSource.QUERY_LANGUAGE;
	}

	@Override
	public JRDataSource getJRDataSource() {
		return new JREmptyDataSource();
	}

	@Override
	public boolean isJDBCConnection() {
		return true;
	}

	@Override
	public boolean isJRDataSource() {
		return false;
	}

	@Override
	public HashMap getProperties() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(MONGO_URI_KEY, getMongoURI());
		map.put(USERNAME, getUsername());
		map.put(PASSWORD, getPassword());
		return map;
	}

	@Override
	public void loadProperties(HashMap map) {
		setMongoURI((String) map.get(MONGO_URI_KEY));
		setUsername((String) map.get(USERNAME));
		setPassword((String) map.get(PASSWORD));
	}

	public String getMongoURI() {
		return mongoURI;
	}

	public void setMongoURI(String mongoURI) {
		this.mongoURI = mongoURI;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public void test() throws Exception {
		com.jaspersoft.mongodb.connection.MongoDbConnection connection = null;
		String errorMessage = null;
		try {
			connection = createConnection();
			if (connection != null) {
				JOptionPane.showMessageDialog(Misc.getMainWindow(), connection.test(), "",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			errorMessage = "A connection could not be created. Please review the IDE log";
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = String.valueOf(e);
		} finally {
			if (connection != null) {
				Installer.getConnectionManager().returnConnection(connection);
			}
		}
		JOptionPane.showMessageDialog(Misc.getMainWindow(), "Error: " + String.valueOf(errorMessage), "",
				JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public String designQuery(String query) {
		return query;
	}

	@Override
	public List<JRDesignField> readFields(String query) throws Exception {
		MongoDbFieldsProvider provider = new MongoDbFieldsProvider();
		List<JRDesignField> result = new ArrayList<JRDesignField>();
		JRDesignDataset dataset = new JRDesignDataset(true);
		JRDesignQuery designQuery = new JRDesignQuery();
		designQuery.setLanguage(MongoDbDataSource.QUERY_LANGUAGE);
		designQuery.setText(query);
		dataset.setQuery(designQuery);
		Map<String, String> parameters = new HashMap<String, String>();
		JRField[] fields = provider.getFields(this, dataset, parameters);
		for (int i = 0; i < fields.length; ++i) {
			result.add((JRDesignField) fields[i]);
		}
		return result;
	}

	@Override
	public boolean supportsDesign() {
		return false;
	}
        
        
}
