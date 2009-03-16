package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class DefaultServiceExceptionStrategyFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/default-service-exception-strategy-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "defaultServiceExceptionStrategyService");

        assertNotNull(service);
        assertEquals("defaultServiceExceptionStrategyModel", service.getModel().getName());

    }

    public void testExceptionThrownToRIGAndRouted() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        client.send("vm://RIG.In","test",null);
        MuleMessage message = client.request("jms://error",15000);
        assertNotNull(message);
    }


    public void testExceptionThrownToSRIGAndRouted() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        client.send("vm://SRIG.In","test",null);
        MuleMessage message = client.request("jms://error",15000);
        assertNotNull(message);
    }

}
