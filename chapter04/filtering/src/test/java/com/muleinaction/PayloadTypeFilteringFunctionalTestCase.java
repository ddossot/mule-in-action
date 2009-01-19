package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import com.muleinaction.common.Order;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class PayloadTypeFilteringFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/payload-filtering-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "payloadFilteringService");

        assertNotNull(service);
        assertEquals("payloadFilteringModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {
        Order order = new Order();
        order.setStatus("FULFILLED");
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://messages.in", order, null);
        MuleMessage response = muleClient.request("jms://messages.out", 2000);
        assertNotNull(response);
    }

    public void testMessageNotConsumed() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://messages.in", "order", null);
        MuleMessage response = muleClient.request("jms://messages.out", 2000);
        assertNull(response);
        response = muleClient.request("jms://errors", 2000);
        assertNotNull(response);
    }


}
