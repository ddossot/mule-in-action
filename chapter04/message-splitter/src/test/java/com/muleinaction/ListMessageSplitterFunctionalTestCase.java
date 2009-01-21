package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import com.muleinaction.common.Order;

import java.util.List;
import java.util.ArrayList;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class ListMessageSplitterFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/list-message-splitter-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "listMessageSplitterService");

        assertNotNull(service);
        assertEquals("listMessageSplitterModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {

        List<Order> orders = new ArrayList<Order>();
        Order o1 = new Order();
        o1.setStatus("FULFILLED");

        Order o2 = new Order();
        o2.setStatus("FULFILLED");

        Order o3 = new Order();
        o3.setStatus("PENDING");

        Order o4 = new Order();
        o4.setStatus("CANCELLED");

        orders.add(o1);
        orders.add(o2);
        orders.add(o3);
        orders.add(o4);

        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("vm://orders", orders, null);

        MuleMessage response = muleClient.request("vm://orders.fulfilled", 2000);
        assertNotNull(response);
        response = muleClient.request("vm://orders.fulfilled", 2000);
        assertNotNull(response);
        response = muleClient.request("vm://orders.fulfilled", 2000);
        assertNull(response);

        response = muleClient.request("vm://orders.pending", 2000);
        assertNotNull(response);       
        response = muleClient.request("vm://orders.pending", 2000);
        assertNull(response);

        response = muleClient.request("vm://orders.unknown", 2000);
        assertNotNull(response);
        response = muleClient.request("vm://orders.unknown", 2000);
        assertNull(response);
    }

}
