
package com.clood;

import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

public class MessageEnricherFunctionalTest extends FunctionalTestCase
{

    @Override
    protected String getConfigResources()
    {
        return "conf/message-enricher-config.xml";
    }

    public void testMessageTransformation() throws Exception
    {
        final MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://in", "TEST_PAYLOAD", null);

        final MuleMessage response = muleClient.request("jms://out", 15000);

        assertEquals("***[MESSAGE=TEST_PAYLOAD]***", response.getPayload());
        final Object organizationProperty = response.getInboundProperty("ORGANIZATION", null);
        assertNotNull(organizationProperty);
        assertEquals("CLOOD", organizationProperty);
    }
}
