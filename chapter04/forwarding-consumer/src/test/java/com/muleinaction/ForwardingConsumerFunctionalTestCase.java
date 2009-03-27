package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

import java.util.Map;
import java.util.HashMap;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class ForwardingConsumerFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/forwarding-consumer-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "forwardingConsumerService");

        assertNotNull(service);
        assertEquals("forwardingConsumerModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://messages.in", "STATUS: OK", null);
        MuleMessage response = muleClient.request("jms://messages.out", 2000);
        assertNotNull(response);
    }

    public void testMessageNotConsumed() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://messages.in", "STATUS: CRITICAL", null);
        MuleMessage response = muleClient.request("jms://messages.out", 2000);
        assertNotNull(response);
        assertEquals(response.getPayloadAsString(),"STATUS: OK");
    }

}
