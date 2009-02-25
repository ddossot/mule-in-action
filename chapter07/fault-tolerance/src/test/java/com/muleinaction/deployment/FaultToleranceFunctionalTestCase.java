package com.muleinaction.deployment;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleEventContext;
import org.mule.api.service.Service;
import org.mule.component.DefaultJavaComponent;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.mule.tck.functional.EventCallback;
import org.mule.tck.functional.FunctionalTestComponent;

/**
 * @author David Dossot (david@dossot.net)
 */
public class FaultToleranceFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "fault-tolerance-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service =
                muleContext.getRegistry().lookupService("MessageEntryPoint");

        assertNotNull(service);

        assertEquals("ConversationalFaultTolerance",
                service.getModel().getName());
    }

    public void testOriginalMessageSavedInDlq() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        final List<Object> receivedPayloads = new ArrayList<Object>();

        final EventCallback callback = new EventCallback() {
            public void eventReceived(final MuleEventContext context,
                    final Object component) throws Exception {

                receivedPayloads.add(context.getMessage().getPayload());
                countDownLatch.countDown();
            }
        };

        final DefaultJavaComponent defaultComponent =
                (DefaultJavaComponent) muleContext.getRegistry().lookupService(
                        "DlqProcessor").getComponent();

        final FunctionalTestComponent testComponent =
                (FunctionalTestComponent) defaultComponent.getObjectFactory().getInstance();

        testComponent.setEventCallback(callback);

        final Serializable payload = BigInteger.TEN;
        final MuleClient client = new MuleClient();
        client.dispatch("vm://MessageReceiver.In", new DefaultMuleMessage(
                payload, (Map<?, ?>) null));

        countDownLatch.await(15, TimeUnit.SECONDS);

        assertEquals(1, receivedPayloads.size());
        assertEquals(payload, receivedPayloads.toArray()[0]);
    }
}
