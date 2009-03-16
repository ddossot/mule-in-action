package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.context.notification.EndpointMessageNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.tck.FunctionalTestCase;
import org.mule.context.notification.EndpointMessageNotification;
import org.mule.module.client.MuleClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.io.File;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class JmsInboundFunctionalTestCase extends FunctionalTestCase {

    private static String DEST_DIRECTORY = "./data/reports";

     private CountDownLatch latch = new CountDownLatch(1);

    protected void doSetUp() throws Exception {
        super.doSetUp();
        FileUtils.cleanDirectory(new File(DEST_DIRECTORY));
        muleContext.registerListener(new EndpointMessageNotificationListener() {
            public void onNotification(final ServerNotification notification) {
                if ("jmsInboundService".equals(notification.getResourceIdentifier())) {
                    final EndpointMessageNotification messageNotification = (EndpointMessageNotification) notification;
                    if (messageNotification.getEndpoint().getName().equals("endpoint.file.data.reports")) {
                        latch.countDown();
                    }
                }
            }
        });
    }

    @Override
    protected String getConfigResources() {
        return "conf/jms-inbound-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "jmsInboundService");
        assertNotNull(service);
        assertEquals("jmsInboundModel", service.getModel().getName());
    }

    public void testBackupReportReceived() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        assertEquals(0, FileUtils.listFiles(new File(DEST_DIRECTORY), new WildcardFileFilter("*.*"), null).size());
        client.dispatch("jms://topic:backup-reports","test",null);
        assertTrue("Message did not reach directory on time", latch.await(15, TimeUnit.SECONDS));
        assertEquals(1, FileUtils.listFiles(new File(DEST_DIRECTORY), new WildcardFileFilter("*.*"), null).size());
    }

}
