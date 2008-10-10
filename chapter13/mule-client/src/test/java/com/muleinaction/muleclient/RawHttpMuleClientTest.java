package com.muleinaction.muleclient;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
public class RawHttpMuleClientTest {
    @Test
    public void tapHttpTransport() throws Exception {
        // <start id="MuleClient-Raw-Transport"/>
        final MuleClient muleClient = new MuleClient();

        final MuleMessage response = muleClient
                .send(
                        "http://finance.google.com/finance/historical?q=GOOG&histperiod=weekly&output=csv",
                        null, null);

        final String payload = response.getPayloadAsString();

        muleClient.dispose();
        // <end id="MuleClient-Raw-Transport"/>

        assertNotNull(response);
        assertTrue(payload.contains("Date,Open,High,Low,Close,Volume"));

    }
}
