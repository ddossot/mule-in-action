package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class RefreshableGroovyTransformerFunctionalTestCase extends FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "conf/refreshable-groovy-transformer-config.xml";
	}

	public void testCorrectlyInitialized() throws Exception {
		final Service springTransformerService = muleContext.getRegistry().lookupService(
				"springTransformerService");

		assertNotNull(springTransformerService);
		assertEquals("springTransformerModel", springTransformerService.getModel().getName());
	}

}
