package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class SimpleRhinoComponentFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/simple-rhino-component-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "rhinoMessageEnrichmentService");
        assertNotNull(service);
        assertEquals("rhinoMessageEnrichmentModel", service.getModel().getName());
    }

    public void testMessageEnriched() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        client.sendAsync("vm://in", "test", null);
        MuleMessage message = client.request("vm://out", 15000L);
        assertNotNull(message);
    }

}
