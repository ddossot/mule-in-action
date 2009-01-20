package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class JoinedTransactionFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/jms-joined-transaction-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "staticRecipientListService");

        assertNotNull(service);
        assertEquals("staticRecipientListModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://application-response-times", "DATA-1", null);
        MuleMessage response = muleClient.request("jms://operational-database", 2000);
        assertNotNull(response);
        assertEquals("DATA-1",response.getPayloadAsString());
        response = muleClient.request("jms://data-warehouse", 2000);
        assertNotNull(response);
         assertEquals("DATA-1",response.getPayloadAsString());
    }

}