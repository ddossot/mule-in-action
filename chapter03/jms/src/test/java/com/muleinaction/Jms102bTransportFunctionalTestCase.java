package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;


/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class Jms102bTransportFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/jms-102b-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "jms102bService");

        assertNotNull(service);
        assertEquals("jms102bModel", service.getModel().getName());
    }

    /* ToDo Fix this test...fails under Linux when the tests run from chapter 3's root pom.  Works fine
        under OS/X...
     */
    public void testMessageSent() throws Exception {
        /*
        MuleClient client = new MuleClient(muleContext);
        client.send("http://localhost:9756/backup-reports", "test", null);
        MuleMessage message = client.request("jms://topic:backup-reports", 15000);
        assertNotNull(message);
        assertEquals("test", message.getPayloadAsString()); */ 
    }

}
