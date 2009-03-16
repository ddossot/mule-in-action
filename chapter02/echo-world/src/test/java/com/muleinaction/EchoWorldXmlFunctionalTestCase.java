package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class EchoWorldXmlFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/echo-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "echoService");

        assertNotNull(service);
        assertEquals("echoSample", service.getModel().getName());
    }

}
