package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.context.notification.EndpointMessageNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.tck.FunctionalTestCase;
import org.mule.context.notification.EndpointMessageNotification;
import org.apache.commons.io.FileUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.io.File;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class SmtpTransportFunctionalTestCase extends AbstractEmailFunctionalTestCase {

    private static String SOURCE_DIRECTORY = "./data/snapshot";

    private CountDownLatch latch = new CountDownLatch(1);

    public SmtpTransportFunctionalTestCase() {
        super(65437, STRING_MESSAGE, "smtp");
    }

    protected void doSetUp() throws Exception {
        super.doSetUp();
        muleContext.registerListener(new EndpointMessageNotificationListener() {
            public void onNotification(final ServerNotification notification) {
                if ("smtpService".equals(notification.getResourceIdentifier())) {
                    final EndpointMessageNotification messageNotification = (EndpointMessageNotification) notification;
                    if (messageNotification.getEndpoint().getName().equals("endpoint.smtp.localhost.65437")) {
                        latch.countDown();
                    }
                }
            }
        });
    }

    @Override
    protected String getConfigResources() {
        return "conf/smtp-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "smtpService");
        assertNotNull(service);
        assertEquals("smtpModel", service.getModel().getName());
    }

    public void testEmailSent() throws Exception {
        assertTrue("Email not sent", latch.await(15, TimeUnit.SECONDS));
    }

}
