package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @@author John D'Emic (john.demic@@gmail.com)
 */
public class SecurityManagerHttpPbeFunctionalTestCase extends FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "conf/security-manager-http-pbe-config.xml";
	}

	public void testCorrectlyInitialized() throws Exception {
		final Service service = muleContext.getRegistry().lookupService(
				"securityManagerHttpPbeService");

		assertNotNull(service);
		assertEquals("securityManagerHttpPbeModel", service.getModel().getName());
	}

}
