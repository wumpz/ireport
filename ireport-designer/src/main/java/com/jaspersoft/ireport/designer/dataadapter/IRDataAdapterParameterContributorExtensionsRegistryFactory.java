package com.jaspersoft.ireport.designer.dataadapter;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.ParameterContributorFactory;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.extensions.SingletonExtensionRegistry;

/**
 *
 * @author gtoffoli
 */


public class IRDataAdapterParameterContributorExtensionsRegistryFactory implements ExtensionsRegistryFactory {
    private static final ExtensionsRegistry extensionsRegistry = 
			new SingletonExtensionRegistry<ParameterContributorFactory>(
					ParameterContributorFactory.class, IRDataAdapterParameterContributorFactory.getInstance());
	
	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties) 
	{
		return extensionsRegistry;
	}
}
