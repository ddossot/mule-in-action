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
public class InVmMuleClientFunctionalTestCase extends FunctionalTestCase {

    private String expectedHash;

    private String tempFileName;

    @Override
    protected String getConfigResources() {
        return "conf/in-vm-muleclient-config.xml";
    }

    @Override
    protected void doSetUp() throws Exception {
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
        // <start id="MuleClient-IntegrationTesting"/>
        final MuleClient muleClient = new MuleClient(muleContext);

        assertEquals(expectedHash, muleClient.send("vm://Md5FileHasher.In",
                tempFileName, null).getPayload());
        // <end id="MuleClient-IntegrationTesting"/>
    }

    public void testInMemoryVmCallWithContextDetection() throws Exception {
        // <start id="MuleClient-CreationNoContext"/>
        final MuleClient muleClient = new MuleClient();
        // <end id="MuleClient-CreationNoContext"/>

        assertEquals(expectedHash, muleClient.send("vm://Md5FileHasher.In",
                tempFileName, null).getPayload());
    }
}
