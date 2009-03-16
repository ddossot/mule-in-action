package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class GroovyTransformerFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/groovy-transformer-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "groovyTransformerService");

        assertNotNull(service);
        assertEquals("groovyTransformerModel", service.getModel().getName());
    }

    public void testMessageTransformed() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        client.sendAsync("vm://in","hello, world.",null);
        MuleMessage message = client.request("vm://out",15000);
        assertEquals("HELLO, WORLD.", message.getPayloadAsString());
    }

}
