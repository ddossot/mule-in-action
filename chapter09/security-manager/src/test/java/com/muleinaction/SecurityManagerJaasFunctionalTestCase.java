package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @@author John D'Emic (john.demic@@gmail.com)
 */
public class SecurityManagerJaasFunctionalTestCase extends FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "conf/security-manager-jaas-config.xml";
	}

	public void testCorrectlyInitialized() throws Exception {
	}

}
