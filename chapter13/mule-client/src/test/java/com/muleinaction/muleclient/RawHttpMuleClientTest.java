
package com.muleinaction.muleclient;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
public class RawHttpMuleClientTest
{
    // TODO reactivate when cookie issues will be fixed in Mule 3
    @Ignore
    @Test
    public void tapHttpTransport() throws Exception
    {
        // <start id="MuleClient-RawHttp-Transport"/>
        final MuleClient muleClient = new MuleClient(true);

        final MuleMessage response = muleClient.send(
            "http://www.google.com/finance/historical?q=NASDAQ:GOOG&histperiod=weekly&output=csv", null, null);

        final String payload = response.getPayloadAsString();

        muleClient.dispose();
        // <end id="MuleClient-RawHttp-Transport"/>

        assertNotNull(response);
        assertNotNull(payload);
    }
}
