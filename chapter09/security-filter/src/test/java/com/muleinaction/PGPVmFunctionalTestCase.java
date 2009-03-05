package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import junit.framework.TestCase;

import java.io.File;


/**
 * @author John D'Emic (john.demic@gmail.com) 
 */
public class PGPVmFunctionalTestCase extends FunctionalTestCase {

    protected String getConfigResources() {
        return "conf/pgp-vm-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "pgpService");

        assertNotNull(service);
        assertEquals("pgpModel", service.getModel().getName());
    }

    public void testMessageSentAndDecrypted() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        String decryptedMessage = FileUtils.readFileToString(new File("src/test/resources/test.txt"));
        String encryptedMessage = FileUtils.readFileToString(new File("src/test/resources/test.txt.asc"));
        client.send("jms://messages.encrypted", encryptedMessage, null);
        MuleMessage message = client.request("jms://messages.decrypted", 15000);
        assertNotNull(message);
        assertEquals(decryptedMessage, message.getPayloadAsString());
    }

}
