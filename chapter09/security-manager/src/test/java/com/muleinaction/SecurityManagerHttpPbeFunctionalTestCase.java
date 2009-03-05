package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @@author John D'Emic (john.demic@@gmail.com)
 */
public class SecurityManagerHttpPbeFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/security-manager-http-pbe-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "securityManagerHttpPbeService");

        assertNotNull(service);
        assertEquals("securityManagerHttpPbeModel", service.getModel().getName());
    }

    public void testEndpointAuthenticated() throws Exception {
        MuleClient client = new MuleClient(muleContext);

        client.send("http://john:password@localhost:8081/secure", DECRYPTED_MESSAGE, null);
        MuleMessage message = client.request("vm://encrypted", 15000);
        assertNotNull(message);
        assertEquals(ENCRYPTED_MESSAGE, message.getPayloadAsString());
    }

    private static String DECRYPTED_MESSAGE = "test";
    private static String ENCRYPTED_MESSAGE = "PM/9aDkg37o=";


}
