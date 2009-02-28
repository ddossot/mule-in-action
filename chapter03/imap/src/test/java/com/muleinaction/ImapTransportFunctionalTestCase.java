package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.context.notification.*;

import org.mule.context.notification.EndpointMessageNotification;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class ImapTransportFunctionalTestCase extends AbstractEmailFunctionalTestCase {

    private static String DEST_DIRECTORY = "./data/out";

    CountDownLatch latch;

    public ImapTransportFunctionalTestCase() {
        super(65434, STRING_MESSAGE, "imap");
    }

    protected void doSetUp() throws Exception {
        latch = new CountDownLatch(1);
        super.doSetUp();
        FileUtils.cleanDirectory(new File(DEST_DIRECTORY));
        muleContext.registerListener(new EndpointMessageNotificationListener() {
            public void onNotification(final ServerNotification notification) {
                if ("imapService".equals(notification.getResourceIdentifier())) {
                    final EndpointMessageNotification messageNotification = (EndpointMessageNotification) notification;
                    if (messageNotification.getEndpoint().getName().equals("endpoint.file.data.out")) {
                        latch.countDown();
                    }
                }
            }
        });
    }

    @Override
    protected String getConfigResources() {
        return "conf/imap-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "imapService");
        assertNotNull(service);
        assertEquals("imapModel", service.getModel().getName());
    }

    public void testMessageReceived() throws Exception {
        assertEquals(0, FileUtils.listFiles(new File(DEST_DIRECTORY), new WildcardFileFilter("*.*"), null).size());
        assertTrue("Message did not reach directory on time", latch.await(15, TimeUnit.SECONDS));
        assertEquals(1, FileUtils.listFiles(new File(DEST_DIRECTORY), new WildcardFileFilter("*.*"), null).size());
    }
}

