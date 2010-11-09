package com.muleinaction;

import com.muleinaction.common.Order;
import org.mule.api.MuleMessage;
import org.mule.api.service.Service;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        muleClient.dispatch("jms://orders", getOrder(), properties);
        MuleMessage response = muleClient.request("vm://duplicate.orders", 2000);
        assertNull(response);
    }

    /*
    ToDo Fix when http://www.mulesoft.org/jira/browse/MULE-5190 is resolved

    public void testMessageConsumedOnce() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        Map<String, String> properties = new HashMap<String, String>();

        String uid = UUID.randomUUID().toString();

        properties.put("orderId", uid);
        muleClient.dispatch("jms://orders", getOrder(), properties);
        muleClient.dispatch("jms://orders", getOrder(), properties);

        MuleMessage response = muleClient.request("vm://duplicate.orders", 5000);
        assertNotNull(response);
    } */

    Order getOrder() {
        Order order = new Order();
        order.setProductId(1234);
        order.setPurchaserId(1234);
        order.setStatus("PENDING");
        return order;
    }


}
