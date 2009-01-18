package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class GlobalTransformerFunctionalTestCase extends FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "conf/global-transformer-config.xml";
	}

	public void testCorrectlyInitialized() throws Exception {
		final Service service = muleContext.getRegistry().lookupService(
				"groovyTransformerService");

		assertNotNull(service);
		assertEquals("groovyTransformerModel", service.getModel().getName());
	}

}
