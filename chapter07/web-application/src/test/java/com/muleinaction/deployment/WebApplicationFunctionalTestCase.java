package com.muleinaction.deployment;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class WebApplicationFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "mule-webapp-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "RandomIntegerGenerator");

        assertNotNull(service);
        assertEquals("WebApplication", service.getModel().getName());
    }

}
