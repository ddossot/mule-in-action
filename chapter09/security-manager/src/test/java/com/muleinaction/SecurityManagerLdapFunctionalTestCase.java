package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @@author John D'Emic (john.demic@@gmail.com)
 */
public class SecurityManagerLdapFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/security-manager-ldap-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "securityManagerHttpLdapService");
        assertNotNull(service);
        assertEquals("securityManagerHttpLdapModel", service.getModel().getName());
    }

    public void testEndpointAuthenticated() throws Exception {
           MuleClient client = new MuleClient(muleContext);

           MuleMessage message = client.send("http://john:password@localhost:8081/secure", TEST_PAYLOAD, null);
           assertNotNull(message);
           assertEquals(TEST_PAYLOAD, message.getPayloadAsString());
       }

       private static String TEST_PAYLOAD = "TEST";
    

}
