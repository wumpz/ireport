package com.jaspersoft.ireport.mongodb.designer;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.log4j.Logger;

import com.jaspersoft.ireport.designer.FieldsProvider;
import com.jaspersoft.ireport.designer.FieldsProviderEditor;
import com.jaspersoft.ireport.designer.IRLocalJasperReportsContext;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.data.ReportQueryDialog;
import com.jaspersoft.ireport.mongodb.connection.MongoDbConnection;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class MongoDbFieldsProvider implements FieldsProvider {
	private final static Logger logger = Logger.getLogger(MongoDbFieldsProvider.class);

	public MongoDbFieldsProvider() {
	}

	@Override
	public String designQuery(IReportConnection arg0, String arg1, ReportQueryDialog arg2) throws JRException,
			UnsupportedOperationException {
		logger.warn("NOT IMPLEMENTED: designQuery");
		return null;
	}

	@Override
	public FieldsProviderEditor getEditorComponent(ReportQueryDialog reportQueryDialog) {
		logger.warn("NOT IMPLEMENTED: getEditorComponent");
		return null;
	}

	@Override
	public boolean supportsGetFieldsOperation() {
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JRField[] getFields(IReportConnection ireportConnection, JRDataset dataset, Map parameters)
			throws JRException, UnsupportedOperationException {
		if (ireportConnection == null || !(ireportConnection instanceof MongoDbConnection)) {
			throw new JRException("The active connection is not of type MongoDB. Activate a MongoDB connection first.");
		}
		MongoDbConnection connection = (MongoDbConnection) ireportConnection;
		return com.jaspersoft.mongodb.MongoDbFieldsProvider.getInstance()
				.getFields(IRLocalJasperReportsContext.getInstance(),  dataset, parameters, (com.jaspersoft.mongodb.connection.MongoDbConnection) connection.getConnection())
				.toArray(new JRField[0]);
	}

	@Override
	public boolean hasEditorComponent() {
		return false;
	}

	@Override
	public boolean hasQueryDesigner() {
		return false;
	}

	@Override
	public boolean supportsAutomaticQueryExecution() {
		return true;
	}
}