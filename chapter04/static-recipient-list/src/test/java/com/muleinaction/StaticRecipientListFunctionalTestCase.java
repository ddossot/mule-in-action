package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class StaticRecipientListFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/static-recipient-list-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "staticRecipientListService");

        assertNotNull(service);
        assertEquals("staticRecipientListModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("vm://quoteData", "JAVA: 100", null);
        MuleMessage response = muleClient.request("jms://brokerage.house.1", 2000);
        assertNotNull(response);
        response = muleClient.request("jms://brokerage.house.2", 2000);
        assertNotNull(response);
        response = muleClient.request("jms://brokerage.house.3", 2000);
        assertNotNull(response); 
    }

}
