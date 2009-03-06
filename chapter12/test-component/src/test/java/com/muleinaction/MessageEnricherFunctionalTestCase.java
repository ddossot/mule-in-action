package com.muleinaction;

import org.mule.tck.FunctionalTestCase;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

public class MessageEnricherFunctionalTestCase extends FunctionalTestCase {
    protected String getConfigResources() {
        return "conf/test-component-delay.xml";
    }

    public void testMessageTimedOut() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://in", "TEST_PAYLOAD", null);
        MuleMessage response = muleClient.request("jms://out", 2000);
        assertNull(response);
    }
}