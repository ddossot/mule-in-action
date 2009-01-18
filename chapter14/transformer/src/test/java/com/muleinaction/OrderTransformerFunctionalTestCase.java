package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class OrderTransformerFunctionalTestCase extends FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "conf/order-transformer-config.xml";
	}

	public void testCorrectlyInitialized() throws Exception {
		final Service service = muleContext.getRegistry().lookupService(
				"orderTransformerService");

		assertNotNull(service);
		assertEquals("orderTransformerModel", service.getModel().getName());
	}

}
