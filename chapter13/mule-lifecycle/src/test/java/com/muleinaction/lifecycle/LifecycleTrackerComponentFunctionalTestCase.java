package com.muleinaction.lifecycle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecycleTrackerComponentFunctionalTestCase {

    private MuleClient muleClient;

    private MuleContext muleContext;

    @Before
    public void bootMule() throws Exception {
        muleClient = new MuleClient("conf/lifecycle-config.xml");
        muleContext = muleClient.getMuleContext();
        muleContext.start();
    }

    @Test
    public void trackSpringBeanServiceLifecycle() throws Exception {
        trackComponentLifecycle(
                "SpringBeanService",
                "[setProperty, setMuleContext, springInitialize, setService, setMuleContext, start, stop, springDestroy]");
    }

    @Test
    public void trackMuleSingletonServiceLifecycle() throws Exception {
        trackComponentLifecycle("MuleSingletonService",
                "[setProperty, setService, setMuleContext, initialise, start, stop, dispose]");
    }

    @Test
    public void trackMulePrototypeServiceLifecycle() throws Exception {
        trackComponentLifecycle("MulePrototypeService",
                "[setProperty, setService, setMuleContext, initialise, start, stop, dispose]");
    }

    @Test
    public void trackMulePooledPrototypeServiceLifecycle() throws Exception {
        trackComponentLifecycle("MulePooledPrototypeService",
                "[setProperty, setService, setMuleContext, initialise, start, stop, dispose]");
    }

    private void trackComponentLifecycle(final String serviceName,
            final String expectedLifeCycle) throws Exception {

        final AbstractLifecycleTracker tracker =
                exerciseComponent(muleClient, serviceName);

        muleContext.dispose();
        muleClient.dispose();

        assertEquals(serviceName, expectedLifeCycle,
                tracker.getTracker().toString());
    }

    private AbstractLifecycleTracker exerciseComponent(
            final MuleClient muleClient, final String componentName)
            throws Exception {

        final AbstractLifecycleTracker ltc =
                (AbstractLifecycleTracker) muleClient.send(
                        "vm://" + componentName + ".In", null, null).getPayload();

        assertNotNull(ltc);

        return ltc;
    }
}
