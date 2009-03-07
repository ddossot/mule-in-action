package com.muleinaction;

import org.mule.tck.FunctionalTestCase;
import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

public class BpmFunctionalTestCase extends FunctionalTestCase {

    private static String PAYLOAD_OPS_XML = "<account>\n" +
            "    <name>John</name>\n" +
            "    <group>Operations</group>\n" +
            "</account>";

    protected String getConfigResources() {
        return "conf/provisioning-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "createAccount");

        assertNotNull(service);
        assertEquals("accountProvisioningModel", service.getModel().getName());

        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.send("jms://it.provisioning.requests", PAYLOAD_OPS_XML, null);
        MuleMessage response = muleClient.request("jms://it.provisioning.completed", 15000);
        assertEquals(PAYLOAD_OPS_XML, response.getPayloadAsString());
    }

}