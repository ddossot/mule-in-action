package com.muleinaction.lifecycle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.registry.MuleRegistry;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecycleTrackerSpringBeanFunctionalTestCase {

    @Test
    public void trackSpringBeanLifecycle() throws Exception {
        final MuleClient muleClient = new MuleClient(
                "conf/lifecycle-config.xml");

        final MuleContext muleContext = muleClient.getMuleContext();
        muleContext.start();

        final MuleRegistry registry = muleContext.getRegistry();

        final AbstractLifecycleTracker springLT = (AbstractLifecycleTracker) registry
                .lookupObject("SpringBeanLifecycleTracker");

        muleContext.dispose();
        muleClient.dispose();

        assertEquals(
                "SpringBeanLifecycleTracker",
                "[setProperty, setMuleContext, springInitialize, start, stop, springDestroy]",
                springLT.getTracker().toString());
    }
}
