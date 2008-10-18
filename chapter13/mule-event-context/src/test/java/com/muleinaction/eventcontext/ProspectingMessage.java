package com.muleinaction.eventcontext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author David Dossot (david@dossot.net)
 */
public class ProspectingMessage {

    private static MuleClient client;

    @BeforeClass
    public static void bootstrapMule() throws Exception {
        client = new MuleClient(true);
    }

    @AfterClass
    public static void disposeMule() throws Exception {
        client.dispose();
    }

    @Test
    public void defaultTransformersExist() {
        assertTrue(client.getMuleContext().getRegistry().getTransformers()
                .size() > 0);
    }

    @Test
    public void stringPayload() throws Exception {
        final String payload = "foo";

        final MuleMessage message = new DefaultMuleMessage(payload);

        assertEquals(payload, message.getPayload());
        assertEquals(payload, message.getPayloadAsString());
        assertTrue(Arrays.equals(payload.getBytes(message.getEncoding()),
                message.getPayloadAsBytes()));
    }

    @Test
    public void serializablePayload() throws Exception {
        final BigInteger payload = BigInteger.valueOf(123L);

        final MuleMessage message = new DefaultMuleMessage(payload);

        assertEquals(payload, message.getPayload());
        assertEquals(payload.toString(), message.getPayloadAsString());
        assertTrue(Arrays.equals(payload.toString().getBytes(
                message.getEncoding()), message.getPayloadAsBytes()));
    }
}
