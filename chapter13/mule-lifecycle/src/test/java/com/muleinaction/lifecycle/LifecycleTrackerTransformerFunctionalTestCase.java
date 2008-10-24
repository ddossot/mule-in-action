package com.muleinaction.lifecycle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecycleTrackerTransformerFunctionalTestCase {

    @Test
    public void trackLifecycle() throws Exception {
        final MuleClient muleClient = new MuleClient(
                "conf/lifecycle-config.xml");

        final MuleContext muleContext = muleClient.getMuleContext();
        muleContext.start();

        final MuleMessage result = muleClient.send("vm://EchoService.In",
                "foo", null);

        final LifecycleTrackerTransformer ltt = (LifecycleTrackerTransformer) result
                .getPayload();

        muleContext.dispose();
        muleClient.dispose();

        assertEquals("[setProperty, setMuleContext, initialise]", ltt
                .getTracker().toString());
    }
}
