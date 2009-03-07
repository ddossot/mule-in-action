package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class OrderTransformerFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/order-transformer-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "orderTransformerService");
        assertNotNull(service);
        assertEquals("orderTransformerModel", service.getModel().getName());
    }

    public void testMessageTransformed() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        MuleMessage message = client.request("jms://orders", 15000);
        assertNotNull(message);
        assertEquals(ORDER_XML, message.getPayloadAsString());
    }

    private static String ORDER_XML = "<orders>\n" +
            "  <order>\n" +
            "    <subscriberId>409</subscriberId>\n" +
            "    <productId>1234</productId>\n" +
            "    <status>PENDING</status>\n" +
            "  </order>\n" +
            "  <order>\n" +
            "    <subscriberId>410</subscriberId>\n" +
            "    <productId>1234</productId>\n" +
            "    <status>PENDING</status>\n" +
            "  </order>\n" +
            "  <order>\n" +
            "    <subscriberId>411</subscriberId>\n" +
            "    <productId>1235</productId>\n" +
            "    <status>PENDING</status>\n" +
            "  </order>\n" +
            "</orders>";
}
