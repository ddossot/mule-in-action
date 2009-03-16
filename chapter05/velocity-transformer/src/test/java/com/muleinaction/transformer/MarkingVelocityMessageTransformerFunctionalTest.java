package com.muleinaction.transformer;

import java.util.HashMap;

import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class MarkingVelocityMessageTransformerFunctionalTest extends
        FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/marking-velocity-message-transformer-conf.xml";
    }

    public void testMessageTransformation() throws Exception {
        final MuleMessage result = new MuleClient(muleContext).send(
                "vm://testChannel", VelocityMessageTransformerTest.TEST_DATA,
                // Why this copy? Because MuleClient expects a
                // modifiable map!
                new HashMap<String, String>(
                        VelocityMessageTransformerTest.TEST_PROPERTIES));

        assertEquals(VelocityMessageTransformerTest.RESULT_DATA, result
                .getPayload());

        assertFalse(result.getLongProperty("transformationTime", Long.MIN_VALUE) == Long.MIN_VALUE);
    }

}
