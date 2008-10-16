package com.muleinaction.lifecycle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
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

        final LifecycleTrackerComponent springLTC =
                (LifecycleTrackerComponent) muleContext.getRegistry().lookupObject(
                        "SpringBeanLifecycleTrackerComponent");

        final DefaultJavaComponent muleComponentJavaLTC =
                (DefaultJavaComponent) muleContext.getRegistry().lookupService(
                        "MuleComponentService").getComponent();

        final LifecycleTrackerComponent muleComponentLTC =
                (LifecycleTrackerComponent) muleComponentJavaLTC.getObjectFactory().getInstance();

        final DefaultJavaComponent muleObjectJavaLTC =
                (DefaultJavaComponent) muleContext.getRegistry().lookupService(
                        "MuleObjectService").getComponent();

        final LifecycleTrackerComponent muleObjectLTC =
                (LifecycleTrackerComponent) muleObjectJavaLTC.getObjectFactory().getInstance();

        exerciseComponents(muleClient);

        muleContext.dispose();
        muleClient.dispose();

        assertEquals(
                "[springSetProperty, setMuleContext, springInitialize, setService, initialise, start, start, stop, stop, dispose, dispose, springDestroy]",
                springLTC.getTracker().toString());

        assertEquals("[]", muleComponentLTC.getTracker().toString());

        assertEquals("[setService, initialise, start, stop, dispose, dispose]",
                muleObjectLTC.getTracker().toString());
    }

    private void exerciseComponents(final MuleClient muleClient)
            throws Exception, MuleException {

        assertEquals("ACK", muleClient.sendDirect("SpringBeanService", null,
                null, null).getPayloadAsString());

        assertEquals("ACK", muleClient.sendDirect("MuleComponentService", null,
                null, null).getPayloadAsString());

        assertEquals("ACK", muleClient.sendDirect("MuleObjectService", null,
                null, null).getPayloadAsString());
    }
}
