package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import com.muleinaction.common.Order;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class IdempotentReceiverFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/idempotent-receiver-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "idempotentReceiverService");

        assertNotNull(service);
        assertEquals("idempotentReceiverModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        Map<String, String> properties = new HashMap<String, String>();

        String uid = UUID.randomUUID().toString();

        properties.put("orderId", uid);
        muleClient.sendAsync("jms://orders", getOrder(), properties);
        MuleMessage response = muleClient.request("jms://duplicate.orders", 2000);
        assertNull(response);
    }

    public void testMessageConsumedOnce() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        Map<String, String> properties = new HashMap<String, String>();

        String uid = UUID.randomUUID().toString();

        properties.put("orderId", uid);
        muleClient.sendAsync("jms://orders", getOrder(), properties);
        muleClient.sendAsync("jms://orders", getOrder(), properties);

        MuleMessage response = muleClient.request("jms://duplicate.orders", 2000);
        assertNotNull(response);
    }

    Order getOrder() {
        Order order = new Order();
        order.setProductId(1234);
        order.setPurchaserId(1234);
        order.setStatus("PENDING");
        return order;
    }


}
