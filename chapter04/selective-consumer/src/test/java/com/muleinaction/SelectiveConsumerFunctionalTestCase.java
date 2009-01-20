package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class SelectiveConsumerFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/selective-consumer-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "selectiveConsumerService");

        assertNotNull(service);
        assertEquals("selectiveConsumerModel", service.getModel().getName());

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
        assertNull(response);
        response = muleClient.request("jms://messages.errors", 2000);
        assertNotNull(response);
    }


}
