package com.muleinaction.component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LogicComponentsFunctionalTestCase extends FunctionalTestCase {

    private MuleClient muleClient;

    private String expectedHash;

    private String tempFileName;

    @Override
    protected String getConfigResources() {
        return "conf/logic-components-config.xml";
    }

    @Override
    protected void doSetUp() throws Exception {
        setDisposeManagerPerSuite(true);
        super.doSetUp();
        muleClient = new MuleClient(muleContext);

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

    public void testRandomIntegerGenerator() throws Exception {
        doTestAnyRandomIntegerGenerator("vm://RIG.In");
    }

    public void testSeededRandomIntegerGenerator() throws Exception {
        doTestAnyRandomIntegerGenerator("vm://SRIG.In");
    }

    public void testSpringSeededRandomIntegerGenerator() throws Exception {
        doTestAnyRandomIntegerGenerator("vm://SSRIG.In");
    }

    private void doTestAnyRandomIntegerGenerator(final String rngUri)
            throws Exception {

        // we should be able to ask for a few numbers
        for (int i = 0; i < 10; i++) {
            assertTrue(muleClient.send(rngUri, null, null).getPayload() instanceof Integer);
        }
    }

    public void testMd5FileHasherSingleton() throws Exception {
        doTestMd5FileHasher("vm://Md5FileHasher.In");
    }

    public void testMd5FileHasherPrototype() throws Exception {
        doTestMd5FileHasher("vm://Md5FileHashers.In");
    }

    public void testMd5FileHasherServiceClient() throws Exception {
        final Object result = muleClient.send("vm://MSC.In", tempFileName, null)
                .getPayload();

        assertTrue(result instanceof List);
        assertEquals(expectedHash, ((List<?>) result).get(0));
    }

    private void doTestMd5FileHasher(final String md5fhUri) throws Exception {
        assertEquals(expectedHash, muleClient.send(md5fhUri, tempFileName, null)
                .getPayload());
    }
}
