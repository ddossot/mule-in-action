package com.muleinaction.muleclient;

import org.mule.api.FutureMessageResult;
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

    public void testTickerLookup() throws Exception {
        // <start id="MuleClient-RDA"/>
        final MuleClient muleClient = new MuleClient(false);

        final RemoteDispatcher remoteDispatcher = muleClient
                .getRemoteDispatcher("tcp://localhost:5555");

        final FutureMessageResult asyncResponse = remoteDispatcher.sendAsyncRemote("TickerLookupChannel", "NASDAQ:GOOG", null);
        // <end id="MuleClient-RDA"/>

        // in a real application we would do something else and check for
        // availability of the response message from time to time
        final String response = asyncResponse.getMessage(10000)
                .getPayloadAsString();

        assertNotNull(response);

        muleClient.dispose();
    }
}
