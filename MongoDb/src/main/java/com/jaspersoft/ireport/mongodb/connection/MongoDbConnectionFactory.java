package com.jaspersoft.ireport.mongodb.connection;

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.connection.IReportConnectionFactory;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class MongoDbConnectionFactory implements IReportConnectionFactory {
	public IReportConnection createConnection() {
		return new MongoDbConnection();
	}

	public String getConnectionClassName() {
		return MongoDbConnection.class.getName();
	}
}
