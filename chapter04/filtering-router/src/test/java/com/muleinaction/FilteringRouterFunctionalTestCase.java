package com.muleinaction;

import org.mule.api.MuleMessage;
import org.mule.api.service.Service;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

import com.muleinaction.common.Fare;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class FilteringRouterFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/filtering-router-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "filteringRouterService");

        assertNotNull(service);
        assertEquals("filteringRouterModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {
        final MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://fares", new Fare("UA", "JFK-SJC", 1000.00),
                null);
        muleClient.sendAsync("jms://fares", new Fare("UA", "JFK-SJC", 2000.00),
                null);

        final MuleMessage response = muleClient.request("jms://cheapestFares",
                2000);

        assertNotNull(response);
    }

    /*
     * public void testMessageNotConsumed() throws Exception { MuleClient
     * muleClient = new MuleClient(muleContext);
     * muleClient.sendAsync("jms://messages.in", "STATUS: CRITICAL", null);
     * MuleMessage response = muleClient.request("jms://messages.out", 2000);
     * assertNull(response); response =
     * muleClient.request("jms://messages.errors", 2000);
     * assertNotNull(response); }
     */

}
