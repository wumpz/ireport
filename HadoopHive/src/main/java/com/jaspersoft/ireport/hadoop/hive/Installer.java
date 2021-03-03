package com.jaspersoft.ireport.hadoop.hive;

import org.openide.modules.ModuleInstall;
import com.jaspersoft.hadoop.hive.HiveDataSource;
import com.jaspersoft.hadoop.hive.connection.HiveConnectionManager;
import com.jaspersoft.hadoop.hive.query.HiveQueryExecuterFactory;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.hadoop.hive.connection.HiveConnectionFactory;
import com.jaspersoft.ireport.hadoop.hive.designer.HiveFieldsProvider;

/**
 *
 * @author ediaz
 *
 */
public class Installer extends ModuleInstall {

	private static HiveConnectionManager connectionManager;

	@Override
	public void restored() {
		IReportManager.getInstance().addConnectionImplementationFactory(new HiveConnectionFactory());
		IReportManager.getInstance().addQueryExecuterDef(
				new com.jaspersoft.ireport.designer.data.queryexecuters.QueryExecuterDef(HiveDataSource.QUERY_LANGUAGE,
				HiveQueryExecuterFactory.class.getName(), HiveFieldsProvider.class.getName()), true);
		System.out.println("Initializing Hive Module");
		connectionManager = new HiveConnectionManager();
	}

	public static HiveConnectionManager getConnectionManager() {
		return connectionManager;
	}

	@Override
	public void close() {
		super.close();
		System.out.println("Closing Hive Module");
		if (connectionManager != null) {
			connectionManager.shutdown();
		}
	}
}
