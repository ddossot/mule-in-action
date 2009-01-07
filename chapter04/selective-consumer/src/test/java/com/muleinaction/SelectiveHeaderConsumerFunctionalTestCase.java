package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class SelectiveHeaderConsumerFunctionalTestCase extends FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "conf/selective-header-consumer-config.xml";
	}

	public void testCorrectlyInitialized() throws Exception {
		final Service service = muleContext.getRegistry().lookupService(
				"selectiveHeaderConsumerService");

		assertNotNull(service);
		assertEquals("selectiveHeaderConsumerModel", service.getModel().getName());
	}

}
