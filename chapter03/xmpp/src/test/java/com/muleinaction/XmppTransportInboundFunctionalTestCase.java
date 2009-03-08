package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class XmppTransportInboundFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/xmpp-inbound-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "xmppInboundService");
        assertNotNull(service);
        assertEquals("xmppInboundModel", service.getModel().getName());
    }

}
