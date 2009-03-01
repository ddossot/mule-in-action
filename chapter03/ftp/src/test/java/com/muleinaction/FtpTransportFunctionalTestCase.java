package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.context.notification.EndpointMessageNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.tck.FunctionalTestCase;
import org.mule.context.notification.EndpointMessageNotification;
import org.mule.util.concurrent.Latch;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class FtpTransportFunctionalTestCase extends FunctionalTestCase {

    private FakeFtpServer fakeFtpServer;

    private static String DEST_DIRECTORY = "./data/out";

    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    protected String getConfigResources() {
        return "conf/ftp-inbound-config.xml";
    }

    protected void doSetUp() throws Exception {
        super.doSetUp();
        FileUtils.cleanDirectory(new File(DEST_DIRECTORY));
         muleContext.registerListener(new EndpointMessageNotificationListener() {
            public void onNotification(final ServerNotification notification) {
                if ("ftpService".equals(notification.getResourceIdentifier())) {
                    final EndpointMessageNotification messageNotification = (EndpointMessageNotification) notification;
                    if (messageNotification.getEndpoint().getName().equals("endpoint.file.data.out")) {
                        latch.countDown();
                    }
                }
            }
        });
    }

    protected void suitePreSetUp() throws Exception {
        startServer();
    }

    protected void suitePostTearDown() throws Exception {
        stopServer();
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "ftpService");
        assertNotNull(service);
        assertEquals("ftpModel", service.getModel().getName());
        assertTrue("Message did not reach directory on time", latch.await(15, TimeUnit.SECONDS));        
        assertEquals(1, FileUtils.listFiles(new File(DEST_DIRECTORY), new WildcardFileFilter("*.*"), null).size());
    }

    void startServer() {
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.setServerControlPort(9879);
        fakeFtpServer.addUserAccount(new UserAccount("ftp", "ftp", "/"));

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new FileEntry("/files/file1.txt", "MULEINACTION"));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.start();
    }

    void stopServer() {
        fakeFtpServer.stop();
    }

}
