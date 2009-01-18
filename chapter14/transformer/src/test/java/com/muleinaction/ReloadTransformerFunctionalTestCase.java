package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class ReloadTransformerFunctionalTestCase extends FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "conf/reload-transformer-groovy-config.xml";
	}

	public void testCorrectlyInitialized() throws Exception {
		final Service service = muleContext.getRegistry().lookupService(
				"reloadTransformerService");

		assertNotNull(service);
		assertEquals("reloadTransformerModel", service.getModel().getName());
	}

}
