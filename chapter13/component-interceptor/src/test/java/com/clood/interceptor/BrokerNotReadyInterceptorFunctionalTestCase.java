package com.clood.interceptor;

import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class BrokerNotReadyInterceptorFunctionalTestCase extends
        FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/broker-ready-config.xml";
    }

    public void testBrokerReadinessSensitiveComponent() throws Exception {
        final MuleClient muleClient = new MuleClient(muleContext);

        assertEquals("ACK", muleClient.send("vm://BrokerReadinessService.In",
                "foo", null).getPayload());

        muleClient.dispose();
    }

}
