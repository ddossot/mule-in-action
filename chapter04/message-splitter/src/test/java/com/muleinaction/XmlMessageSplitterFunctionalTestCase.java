package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class XmlMessageSplitterFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/xml-message-splitter-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "xmlMessageSplitterService");

        assertNotNull(service);
        assertEquals("xmlMessageSplitterModel", service.getModel().getName());
    }


    public void testMessageConsumed() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("vm://orders", getOrders(), null);

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

    String getOrders() {
        StringBuilder result = new StringBuilder("<orders>");
        result.append(getOrder("1", "1", "FULFILLED"));
        result.append(getOrder("1", "1", "FULFILLED"));
        result.append(getOrder("1", "1", "PENDING"));
        result.append(getOrder("1", "1", "CANCELLED"));
        result.append("</orders>");
        return result.toString();
    }

    String getOrder(String purchaserId, String productId, String status) {
        return String.format("<order>" +
                "<purchaserId>%s</purchaserId>" +
                "<productId>%s</productId>" +
                "<status>%s</status> " +
                "</order>", purchaserId, productId, status);
    }

}
