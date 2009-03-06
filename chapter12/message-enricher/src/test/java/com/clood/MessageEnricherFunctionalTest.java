package com.clood;

import org.mule.tck.FunctionalTestCase;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.RequestContext;

public class MessageEnricherFunctionalTest extends FunctionalTestCase {

    protected String getConfigResources() {
        return "conf/message-enricher-config.xml";
    }

    public void testMessageTransformation() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://in", "TEST_PAYLOAD", null);

        MuleMessage response = muleClient.request("jms://out", 15000);

        assertEquals("***[MESSAGE=TEST_PAYLOAD]***", response.getPayload());
        assertNotNull(response.getProperty("ORGANIZATION", true));
        assertEquals("CLOOD", response.getProperty("ORGANIZATION", true));

    }
}
