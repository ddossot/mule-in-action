package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class PBEVmFunctionalTestCase extends FunctionalTestCase {

    protected String getConfigResources() {
        return "conf/pbe-vm-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service inService = muleContext.getRegistry().lookupService(
                "pbeInService");
        final Service outService = muleContext.getRegistry().lookupService(
                "pbeOutService");

        assertNotNull(inService);
        assertNotNull(outService);
        assertEquals("pbeModel", inService.getModel().getName());
        assertEquals("pbeModel", outService.getModel().getName());        
    }

    public void testMessageSentAndDecrypted() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        client.sendAsync("jms://messages.in","TEST",null);
        MuleMessage message = client.request("jms://messages.out",15000);
        assertNotNull(message);
        assertEquals("TEST",message.getPayloadAsString());
    }

}