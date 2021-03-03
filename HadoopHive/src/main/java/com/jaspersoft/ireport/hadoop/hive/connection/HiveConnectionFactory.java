package com.jaspersoft.ireport.hadoop.hive.connection;

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.connection.IReportConnectionFactory;

/**
 * 
 * @author jneyra
 *
 */
public class HiveConnectionFactory implements IReportConnectionFactory {

	public IReportConnection createConnection() {
		return new HiveConnection();

	}

	public String getConnectionClassName() {
		return HiveConnection.class.getName();
	}

}
