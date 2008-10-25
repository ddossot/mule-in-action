package com.muleinaction.lifecycle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecycleTrackerFilterFunctionalTestCase {

    @Test
    public void trackLifecycle() throws Exception {
        final MuleClient muleClient = new MuleClient(
                "conf/lifecycle-config.xml");

        final MuleContext muleContext = muleClient.getMuleContext();
        muleContext.start();

        final MuleMessage result = muleClient.send("vm://FilteredService.In",
                "foo", null);

        assertEquals("foo", result.getPayload());

        final LifecycleTrackerFilter ltf = (LifecycleTrackerFilter) muleContext
                .getRegistry().lookupObject("LifecycleTrackerFilter");

        muleContext.dispose();
        muleClient.dispose();

        assertEquals("[setProperty, setMuleContext, initialise]", ltf
                .getTracker().toString());
    }
}
