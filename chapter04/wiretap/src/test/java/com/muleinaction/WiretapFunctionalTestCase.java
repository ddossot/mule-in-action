package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class WiretapFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/wiretap-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "wiretapService");

        assertNotNull(service);
        assertEquals("wiretapModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://orders", "Order Data", null);
        MuleMessage response = muleClient.request("vm://billing.service.received", 2000);
        assertNotNull(response);
        response = muleClient.request("vm://order.service.received", 2000);        
        assertNotNull(response);
    }

}
