package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;
import junit.framework.TestCase;


/**
 * @author John D'Emic (john.demic@gmail.com)
 * This test is currently disabled, see: 
 *     http://mule.mulesource.org/jira/browse/MULE-3671
 */
public class PGPVmFunctionalTestCase extends TestCase {

	protected String getConfigResources() {
		return "conf/pgp-vm-config.xml";
	}

	public void testCorrectlyInitialized() throws Exception {
        /*
		final Service service = muleContext.getRegistry().lookupService(
				"pgpService");

		assertNotNull(service);
		assertEquals("pgpModel", service.getModel().getName()); */
	}

}
