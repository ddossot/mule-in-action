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
public class HeaderFilteringFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/header-filtering-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "headerFilteringService");

        assertNotNull(service);
        assertEquals("headerFilteringModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("STATUS", "OK");
        muleClient.sendAsync("jms://messages.in", "PAYLOAD", properties);
        MuleMessage response = muleClient.request("jms://messages.out", 2000);
        assertNotNull(response);
    }

    public void testMessageNotConsumed() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://messages.in", "STATUS: CRITICAL", null);
        MuleMessage response = muleClient.request("jms://messages.out", 2000);
        assertNull(response);
        response = muleClient.request("jms://errors", 2000);
        assertNotNull(response);
    }


}
