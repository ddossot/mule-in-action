package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.context.notification.ServerNotification;
import org.mule.tck.FunctionalTestCase;
import org.mule.tck.functional.FunctionalTestNotificationListener;
import org.mule.module.client.MuleClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class CustomGroovyFilterFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/custom-groovy-filter-config.xml";
    }

    private CountDownLatch latch = new CountDownLatch(1);

    protected void doSetUp() throws Exception {
        super.doSetUp();
        muleContext.registerListener(new FunctionalTestNotificationListener() {
            public void onNotification(final ServerNotification notification) {
                if ("springFilterService".equals(notification.getResourceIdentifier())) {
                    latch.countDown();
                }
            }
        });
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service springFilterService = muleContext.getRegistry().lookupService(
                "springFilterService");
        final Service rhinoMessageEnrichmentService = muleContext.getRegistry().lookupService(
                "rhinoMessageEnrichmentService");

        assertNotNull(springFilterService);
        assertNotNull(rhinoMessageEnrichmentService);
        assertEquals("springFilterModel", springFilterService.getModel().getName());
    }

    public void testMessageReceived() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        client.send("vm://in", "test", null);
        assertTrue("Message did not reach service on time", latch.await(15, TimeUnit.SECONDS));
    }

}
