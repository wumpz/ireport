package com.jaspersoft.ireport.designer.dataadapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.DataAdapterServiceUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.ParameterContributor;
import net.sf.jasperreports.engine.ParameterContributorContext;
import net.sf.jasperreports.engine.ParameterContributorFactory;
import net.sf.jasperreports.repo.DataAdapterResource;
import net.sf.jasperreports.repo.RepositoryUtil;

/**
 *
 * @author gtoffoli
 */


public class IRDataAdapterParameterContributorFactory implements ParameterContributorFactory
{
    
        private static final IRDataAdapterParameterContributorFactory INSTANCE = new IRDataAdapterParameterContributorFactory();
	
	private IRDataAdapterParameterContributorFactory()
	{
	}
	
	/**
	 * 
	 */
	public static IRDataAdapterParameterContributorFactory getInstance()
	{
		return INSTANCE;
	}

	/**
	 *
	 */
	public List<ParameterContributor> getContributors(ParameterContributorContext context) throws JRException
	{
		List<ParameterContributor> contributors = new ArrayList<ParameterContributor>();

                try {
                
                    String dataAdapterUri = JRPropertiesUtil.getInstance(context.getJasperReportsContext()).getProperty(context.getDataset(), "net.sf.jasperreports.data.adapter");
                    if (dataAdapterUri != null)
                    {

                            DataAdapterResource dataAdapterResource = null;
                            
                            
                            try {

                                dataAdapterResource = RepositoryUtil.getInstance(context.getJasperReportsContext()).getResourceFromLocation(dataAdapterUri, DataAdapterResource.class);
                                
                                if (dataAdapterResource != null && dataAdapterResource.getValue() != null)
                                {
                                    ParameterContributor dataAdapterService = DataAdapterServiceUtil.getInstance(context.getJasperReportsContext()).getService(dataAdapterResource.getValue());
                                   return Collections.singletonList(dataAdapterService);
                                }
                            } catch (Throwable ex)
                            {
                               System.out.println("This report includes a reference to a data adapter. Data adapters are currently not supported in iReport.");
                                
                                // ex.printStackTrace();
                            }

                            
                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }

		return contributors;
	}
        
       
   
        
}
