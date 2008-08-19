package com.muleinaction.deployment;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class ApplicationFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "soap-wrapper-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service =
                muleContext.getRegistry().lookupService("GeoCoderSearch");

        assertNotNull(service);
        assertEquals("ApplicationDeployment", service.getModel().getName());
    }

}
