package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.context.notification.ServerNotification;
import org.mule.api.context.notification.ComponentMessageNotificationListener;
import org.mule.api.EncryptionStrategy;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.mule.security.MuleCredentials;


import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class JaasPbeFunctionalTestCase extends FunctionalTestCase {

    private CountDownLatch latch = new CountDownLatch(2);

    @Override
    protected String getConfigResources() {
        return "conf/jaas-pbe-jms-config.xml";
    }

    protected void doSetUp() throws Exception {
        super.doSetUp();
         muleContext.registerListener(new ComponentMessageNotificationListener() {
            public void onNotification(final ServerNotification notification) {
                if ("jaasPbeService".equals(notification.getResourceIdentifier())) {
                        latch.countDown();
                }
            }
        });
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "jaasPbeService");
        assertNotNull(service);
        assertEquals("jaasPbeModel", service.getModel().getName());
    }

    @SuppressWarnings({"unchecked"})
    public void testEndpointAuthenticated() throws Exception {
        MuleClient client = new MuleClient(muleContext);

        Map messageProperties = new HashMap();
        
        messageProperties.put("MULE_USER", "PBE pEtDEBiQnNRh+tmO8SttSQ==");

        client.sendAsync("jms://messages", TEST_PAYLOAD, messageProperties);
        assertTrue("Message did not reach component on time", latch.await(15, TimeUnit.SECONDS));        
    }

    private static String TEST_PAYLOAD = "TEST";

}
