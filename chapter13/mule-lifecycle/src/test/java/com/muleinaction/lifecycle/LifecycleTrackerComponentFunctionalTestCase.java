package com.muleinaction.lifecycle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.registry.MuleRegistry;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecycleTrackerComponentFunctionalTestCase {

    @Test
    public void trackLifecycle() throws Exception {
        final MuleClient muleClient = new MuleClient(
                "conf/lifecycle-config.xml");

        final MuleContext muleContext = muleClient.getMuleContext();
        muleContext.start();

        final MuleRegistry registry = muleContext.getRegistry();

        final AbstractLifecycleTracker springLT = (AbstractLifecycleTracker) registry
                .lookupObject("SpringBeanLifecycleTracker");

        final AbstractLifecycleTracker springLTC = exerciseComponent(
                muleClient, "SpringBeanService");

        final AbstractLifecycleTracker muleSingletonLTC = exerciseComponent(
                muleClient, "MuleSingletonService");

        final AbstractLifecycleTracker mulePrototypeLTC = exerciseComponent(
                muleClient, "MulePrototypeService");

        muleContext.dispose();
        muleClient.dispose();

        assertEquals(
                "SpringBeanLifecycleTracker",
                "[setProperty, setMuleContext, springInitialize, start, stop, springDestroy]",
                springLT.getTracker().toString());

        assertEquals(
                "SpringBeanService",
                "[setProperty, setMuleContext, springInitialize, setService, initialise, start, start, stop, stop, dispose, dispose, springDestroy]",
                springLTC.getTracker().toString());

        assertEquals(
                "MuleSingletonService",
                "[setProperty, setService, initialise, start, stop, dispose, dispose]",
                muleSingletonLTC.getTracker().toString());

        assertEquals("MulePrototypeService",
                "[setProperty, setService, initialise, start, stop, dispose]",
                mulePrototypeLTC.getTracker().toString());
    }

    private AbstractLifecycleTracker exerciseComponent(
            final MuleClient muleClient, final String componentName)
            throws Exception {

        final AbstractLifecycleTracker ltc = (AbstractLifecycleTracker) muleClient
                .sendDirect(componentName, null, null, null).getPayload();

        assertNotNull(ltc);

        return ltc;
    }
}
