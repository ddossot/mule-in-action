package com.muleinaction.muleclient;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
public class RawMuleClientTest {
    @Test
    public void tapHttpTransport() throws Exception {
        final MuleClient muleClient = new MuleClient();
        final MuleMessage response = muleClient
                .send(
                        "http://finance.google.com/finance/historical?q=GOOG&histperiod=weekly&output=csv",
                        null, null);

        assertNotNull(response);
        assertTrue(response.getPayloadAsString().contains(
                "Date,Open,High,Low,Close,Volume"));

        muleClient.dispose();
    }
}
