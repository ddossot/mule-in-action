package com.muleinaction.muleclient;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class MuleClientShowCase extends FunctionalTestCase {

    private String expectedHash;

    private String tempFileName;

    @Override
    protected String getConfigResources() {
        return "conf/muleclient-config.xml";
    }

    @Override
    protected void doSetUp() throws Exception {
        setDisposeManagerPerSuite(true);
        super.doSetUp();

        // prepare test file for MD5 File Hasher Service
        final String fileData = RandomStringUtils.randomAscii(100);

        final File tempFile = File.createTempFile("mia-", null);
        tempFile.deleteOnExit();

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(tempFile);
            fos.write(fileData.getBytes("US-ASCII"));
            fos.flush();
        } finally {
            fos.close();
        }

        expectedHash = DigestUtils.md5Hex(fileData);
        tempFileName = tempFile.getName();
    }

    public void testInMemoryVmCall() throws Exception {
        // this is prototypical for integration testing
        final MuleClient muleClient = new MuleClient(muleContext);

        assertEquals(expectedHash, muleClient.send("vm://Md5FileHasher.In",
                tempFileName, null).getPayload());
    }
}
