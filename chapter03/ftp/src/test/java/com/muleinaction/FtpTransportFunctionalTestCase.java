package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;


/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class FtpTransportFunctionalTestCase extends FunctionalTestCase {

    FakeFtpServer fakeFtpServer;

    private static String DEST_DIRECTORY = "./data/out";

    @Override
    protected String getConfigResources() {
        return "conf/ftp-inbound-config.xml";
    }

    protected void doSetUp() throws Exception {
        super.doSetUp();
        FileUtils.cleanDirectory(new File(DEST_DIRECTORY));
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
        // ToDo replace with a latch
        Thread.sleep(5000);
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
