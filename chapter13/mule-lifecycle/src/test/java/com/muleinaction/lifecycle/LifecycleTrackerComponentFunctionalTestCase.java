package com.muleinaction.lifecycle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.registry.MuleRegistry;
import org.mule.component.DefaultJavaComponent;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecycleTrackerComponentFunctionalTestCase {

    @Test
    public void trackLifecycle() throws Exception {
        final MuleClient muleClient =
                new MuleClient("conf/lifecycle-config.xml");

        final MuleContext muleContext = muleClient.getMuleContext();
        muleContext.start();

        final MuleRegistry registry = muleContext.getRegistry();

        final LifecycleTrackerComponent springLT =
                (LifecycleTrackerComponent) registry.lookupObject("SpringBeanLifecycleTracker");

        final LifecycleTrackerComponent springLTC =
                (LifecycleTrackerComponent) registry.lookupObject("SpringBeanLifecycleTrackerComponent");

        final DefaultJavaComponent muleSingletonJavaLTC =
                (DefaultJavaComponent) registry.lookupService(
                        "MuleSingletonService").getComponent();

        final LifecycleTrackerComponent muleSingletonLTC =
                (LifecycleTrackerComponent) muleSingletonJavaLTC.getObjectFactory().getInstance();

        exerciseComponent(muleClient, "SpringBeanService", springLTC);
        exerciseComponent(muleClient, "MuleSingletonService", muleSingletonLTC);

        muleContext.dispose();
        muleClient.dispose();

        assertEquals(
                "[springSetProperty, setMuleContext, springInitialize, start, stop, springDestroy]",
                springLT.getTracker().toString());

        assertEquals(
                "[springSetProperty, setMuleContext, springInitialize, setService, initialise, start, start, stop, stop, dispose, dispose, springDestroy]",
                springLTC.getTracker().toString());

        assertEquals("[setService, initialise, start, stop, dispose, dispose]",
                muleSingletonLTC.getTracker().toString());
    }

    private void exerciseComponent(final MuleClient muleClient,
            final String componentName, final LifecycleTrackerComponent ltc)
            throws Exception {

        assertEquals(
                componentName,
                "ACK" + ltc.hashCode(),
                muleClient.sendDirect(componentName, null, null, null).getPayloadAsString());
    }
}
