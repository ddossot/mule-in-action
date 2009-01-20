package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class MulticastingRouterFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/multicasting-router-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "multicastingRouterService");

        assertNotNull(service);
        assertEquals("multicastingRouterModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://messages.in", "message", null);
        MuleMessage response = muleClient.request("jms://messages.out", 2000);
        assertNotNull(response);
        response = muleClient.request("vm://messages.out", 2000);
        assertNotNull(response);
    }

}
