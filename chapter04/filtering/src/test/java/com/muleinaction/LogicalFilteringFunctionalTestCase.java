package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import com.muleinaction.common.Order;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class LogicalFilteringFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/logical-filtering-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "logicalFilteringService");

        assertNotNull(service);
        assertEquals("logicalFilteringModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {

        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://messages.in", getOrder("409", "1234", "FULFILLED"), null);
        MuleMessage response = muleClient.request("jms://messages.out", 2000);
        assertNotNull(response);
    }

    public void testMessageNotConsumedBecauseStatusIsNotFulfilled() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://messages.in", getOrder("409", "1234", "CANCELLED"), null);
        MuleMessage response = muleClient.request("jms://messages.out", 2000);
        assertNull(response);
        response = muleClient.request("jms://errors", 2000);
        assertNotNull(response);
    }

    public void testMessageNotConsumedBecausePayloadIsNotAString() throws Exception {
        Order order = new Order();
        order.setStatus("CANCELLED");
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://messages.in", order, null);
        MuleMessage response = muleClient.request("jms://messages.out", 2000);
        assertNull(response);
        response = muleClient.request("jms://errors", 2000);
        assertNotNull(response);
    }

    String getOrder(String purchaserId, String productId, String status) {
        return String.format("<order>" +
                "<purchaserId>%s</purchaserId>" +
                "<productId>%s</productId>" +
                "<status>%s</status> " +
                "</order>", purchaserId, productId, status);
    }

}
