package com.muleinaction.muleclient;

import org.mule.module.client.MuleClient;
import org.mule.module.client.RemoteDispatcher;
import org.mule.tck.FunctionalTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class RemoteTcpMuleClientFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/remote-tcp-muleclient-config.xml";
    }

    public void testInMemoryVmCall() throws Exception {
        final RemoteDispatcher remoteDispatcher =
                new MuleClient(false).getRemoteDispatcher("tcp://localhost:5555");

        final String response =
                remoteDispatcher.sendRemote("TickerLookupChannel", "GOOG", null).getPayloadAsString();

        assertNotNull(response);
        assertTrue(response.contains("Date,Open,High,Low,Close,Volume"));
    }
}
