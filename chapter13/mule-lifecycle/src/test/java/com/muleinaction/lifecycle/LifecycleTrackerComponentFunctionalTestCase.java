package com.muleinaction.lifecycle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecycleTrackerComponentFunctionalTestCase {

    @Test
    public void trackLifecycle() throws Exception {
        LifecycleTrackerComponent.getTracker().clear();

        // exercise the target component but do not test anything here
        final MuleClient muleClient =
                new MuleClient("conf/lifecycle-config.xml");
        final MuleContext muleContext = muleClient.getMuleContext();
        muleContext.start();

        muleClient.sendDirect("DummyService", null, null, null);

        muleContext.dispose();
        muleClient.dispose();

        final String tracking =
                LifecycleTrackerComponent.getTracker().toString();

        assertEquals(
                "[springSetProperty, setMuleContext, springInitialize, setService, initialise, start, start, stop, stop, dispose, dispose, springDestroy]",
                tracking);
    }

}
