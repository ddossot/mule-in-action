package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;
import junit.framework.TestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class XmppTransportInboundFunctionalTestCase extends TestCase {

    protected String getConfigResources() {
        return "conf/xmpp-inbound-config.xml";
    }

    /*
    ToDo Re-enable and improve this test...need a mock XMPP server
     */
    public void testCorrectlyInitialized() throws Exception {
        /*
        final Service service = muleContext.getRegistry().lookupService(
                "xmppInboundService");
        assertNotNull(service);
        assertEquals("xmppInboundModel", service.getModel().getName());  */
    }

}
