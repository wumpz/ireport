package com.jaspersoft.ireport.hadoop.hive.designer;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.data.fieldsproviders.SQLFieldsProvider;
import com.jaspersoft.ireport.hadoop.hive.connection.HiveConnection;

/**
 * 
 * @author jneyra
 * 
 */
public class HiveFieldsProvider extends SQLFieldsProvider {
	@Override
	public boolean hasQueryDesigner() {
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JRField[] getFields(IReportConnection iReportConnection, JRDataset reportDataset, Map parameters)
			throws JRException, UnsupportedOperationException {
		if (iReportConnection == null || !(iReportConnection instanceof HiveConnection)) {
			throw new JRException(
					"The active connection is not of type Hive Connection. Activate a Hive Connection first.");
		}
		return com.jaspersoft.hadoop.hive.HiveFieldsProvider.getInstance().getFields(
				(com.jaspersoft.hadoop.hive.connection.HiveConnection) ((HiveConnection) iReportConnection).getConnection(),
				reportDataset, parameters);
	}
}