package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class ChainingRouterFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/chaining-router-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "chainingRouterService");

        assertNotNull(service);
        assertEquals("chainingRouterModel", service.getModel().getName());
    }

      public void testMessageGenerated() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://weather.request", "11209", null);
        MuleMessage response = muleClient.request("jms://weather.report", 15000);
        assertNotNull(response);
    }

}
