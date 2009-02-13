package com.muleinaction;

import org.mule.tck.FunctionalTestCase;
import org.mule.api.service.Service;

public class BpmFunctionalTestCase extends FunctionalTestCase {

    protected String getConfigResources() {
        return "conf/provisioning-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
           final Service service = muleContext.getRegistry().lookupService(
                   "bpmSink");

           assertNotNull(service);
           assertEquals("accountProvisioningModel", service.getModel().getName());
       }

}