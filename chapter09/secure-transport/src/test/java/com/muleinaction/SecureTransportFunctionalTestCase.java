package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class SecureTransportFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/https-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "secureTransportService");

        assertNotNull(service);
        assertEquals("secureTransportModel", service.getModel().getName());
    }

    public void testMessageSent() throws Exception {
        MuleClient client = new MuleClient(muleContext);

        MuleMessage message = client.send("https://localhost:8081/secure", "test", null);
        assertNotNull(message);
    }

}
