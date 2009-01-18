package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class CustomGroovyFilterFunctionalTestCase extends FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "conf/custom-groovy-filter-config.xml";
	}

	public void testCorrectlyInitialized() throws Exception {
		final Service springFilterService = muleContext.getRegistry().lookupService(
				"springFilterService");
    	final Service rhinoMessageEnrichmentService = muleContext.getRegistry().lookupService(
				"rhinoMessageEnrichmentService");

		assertNotNull(springFilterService);
		assertNotNull(rhinoMessageEnrichmentService);
		assertEquals("springFilterModel", springFilterService.getModel().getName());
	}

}
