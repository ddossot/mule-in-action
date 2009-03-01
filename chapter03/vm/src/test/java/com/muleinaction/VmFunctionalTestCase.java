package com.muleinaction;

import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.mule.api.MuleMessage;

public class VmFunctionalTestCase extends FunctionalTestCase {

    protected String getConfigResources() {
        return "conf/vm-config.xml";
    }

    public void testOrderProcessedByHTTP() throws Exception{
        MuleClient client = new MuleClient(muleContext);
        client.sendAsync("http://localhost:9756/orders","An order",null);
        MuleMessage message1 = client.request("jms://order.submission.ops",15000);
        MuleMessage message2 = client.request("jms://order.submission.sales",15000);

        assertNotNull(message1);
        assertNotNull(message2);
    }

    public void testOrderProcessedByJMS() throws Exception{
        MuleClient client = new MuleClient(muleContext);
        client.sendAsync("jms://orders.in","An order",null);
        MuleMessage message1 = client.request("jms://order.submission.ops",15000);
        MuleMessage message2 = client.request("jms://order.submission.sales",15000);

        assertNotNull(message1);
        assertNotNull(message2);
    }
}