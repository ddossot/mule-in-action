package com.muleinaction;

import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.mule.api.MuleMessage;

public class ErrorQueueFunctionalTestCase extends FunctionalTestCase {

    protected String getConfigResources() {
        return "conf/test-component-exception.xml";
    }

    public void testExceptionRouted() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://in", "TEST_PAYLOAD", null);
        MuleMessage response = muleClient.request("jms://errors", 2000);
        assertNotNull(response);
    }
}
