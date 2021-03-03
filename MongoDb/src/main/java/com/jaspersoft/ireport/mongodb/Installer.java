package com.jaspersoft.ireport.mongodb;

import org.openide.modules.ModuleInstall;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.data.queryexecuters.QueryExecuterDef;
import com.jaspersoft.ireport.mongodb.connection.MongoDbConnectionFactory;
import com.jaspersoft.ireport.mongodb.designer.MongoDbFieldsProvider;
import com.jaspersoft.mongodb.MongoDbDataSource;
import com.jaspersoft.mongodb.query.MongoDbQueryExecuterFactory;
import com.jaspersoft.mongodb.connection.MongoDbConnectionManager;

/**
 *
 * @author Eric Diaz
 *
 */
public class Installer extends ModuleInstall {

	private static MongoDbConnectionManager connectionManager;

	@Override
	public void restored() {
		IReportManager.getInstance().addConnectionImplementationFactory(new MongoDbConnectionFactory());
		IReportManager.getInstance().addQueryExecuterDef(
				new QueryExecuterDef(MongoDbDataSource.QUERY_LANGUAGE, MongoDbQueryExecuterFactory.class.getName(),
				MongoDbFieldsProvider.class.getName()), true);
		System.out.println("Initializing MongoDb Module");
		connectionManager = new MongoDbConnectionManager();
	}

	public static MongoDbConnectionManager getConnectionManager() {
		return connectionManager;
	}

	@Override
	public void close() {
		super.close();
		System.out.println("Closing MongoDb Module");
		if (connectionManager != null) {
			connectionManager.shutdown();
		}
	}
}