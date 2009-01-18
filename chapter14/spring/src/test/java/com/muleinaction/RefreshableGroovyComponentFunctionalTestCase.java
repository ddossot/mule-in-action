package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class RefreshableGroovyComponentFunctionalTestCase extends FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "conf/refreshable-groovy-component-config.xml";
	}

	public void testCorrectlyInitialized() throws Exception {
		final Service springMessageEnrichmentService = muleContext.getRegistry().lookupService(
				"springMessageEnrichmentService");
    	final Service inService = muleContext.getRegistry().lookupService(
				"in");

		assertNotNull(springMessageEnrichmentService);
		assertNotNull(inService);
		assertEquals("springMessageEnrichmentModel", springMessageEnrichmentService.getModel().getName());
	}

}
