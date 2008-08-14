package com.muleinaction.transformer;

import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class VelocityPayloadTransformerFunctionalTest extends
        FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/velocity-payload-transformer-conf.xml";
    }

    public void testMessageTransformation() throws Exception {
        final MuleMessage result =
                new MuleClient(muleContext).send("vm://testChannel",
                        VelocityPayloadTransformerTest.TEST_DATA, null);

        assertEquals(VelocityPayloadTransformerTest.RESULT_DATA,
                result.getPayload());
    }

}
