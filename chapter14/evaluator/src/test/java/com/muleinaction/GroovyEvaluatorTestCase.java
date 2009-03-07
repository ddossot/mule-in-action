package com.muleinaction;

import org.mule.api.context.notification.ServerNotification;
import org.mule.api.service.Service;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.mule.tck.functional.FunctionalTestNotificationListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class GroovyEvaluatorTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/groovy-evaluator-config.xml";
    }

    private CountDownLatch latch = new CountDownLatch(1);

    protected void doSetUp() throws Exception {
        super.doSetUp();
        muleContext.registerListener(new FunctionalTestNotificationListener() {
            public void onNotification(final ServerNotification notification) {
                if ("groovyExpressionService".equals(notification.getResourceIdentifier())) {
                    latch.countDown();
                }
            }
        });
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "groovyExpressionService");
        assertNotNull(service);
        assertEquals("groovyExpressionModel", service.getModel().getName());
    }

    public void testMessageAcceptedByComponent() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        client.sendAsync("vm://in", "This is ASCII printable", null);
        assertTrue("Message did not reach service on time", latch.await(15, TimeUnit.SECONDS));
    }

    public void testMessageNotAcceptedByComponent() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        client.sendAsync("vm://in", "\u00ea", null);
        assertFalse(latch.await(5, TimeUnit.SECONDS));
    }

}
