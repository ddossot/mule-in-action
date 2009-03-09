package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.context.notification.ComponentMessageNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.api.config.MuleProperties;
import org.mule.api.EncryptionStrategy;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.mule.security.MuleCredentials;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class JaasPlainFunctionalTestCase extends FunctionalTestCase {

    private CountDownLatch latch = new CountDownLatch(2);

    @Override
    protected String getConfigResources() {
        return "conf/jaas-plain-jms-config.xml";
    }

    protected void doSetUp() throws Exception {
        super.doSetUp();
        muleContext.registerListener(new ComponentMessageNotificationListener() {
            public void onNotification(final ServerNotification notification) {
                if ("jaasPlainService".equals(notification.getResourceIdentifier())) {
                    latch.countDown();
                }
            }
        });
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "jaasPlainService");
        assertNotNull(service);
        assertEquals("jaasPlainModel", service.getModel().getName());
    }

    @SuppressWarnings({"unchecked"})
    public void testEndpointAuthenticated() throws Exception {
        MuleClient client = new MuleClient(muleContext);
                         
        Map messageProperties = new HashMap();
        messageProperties.put("MULE_USER", "Plain john::password");

        client.sendAsync("vm://messages", TEST_PAYLOAD, messageProperties);
        assertTrue("Message did not reach component on time", latch.await(15, TimeUnit.SECONDS));
    }

    private static String TEST_PAYLOAD = "TEST";
}
