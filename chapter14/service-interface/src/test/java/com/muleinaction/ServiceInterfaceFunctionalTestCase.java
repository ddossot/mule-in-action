package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class ServiceInterfaceFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/service-interface-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "javaInterfaceBindingService");

        assertNotNull(service);
        assertEquals("javaInterfaceBindingModel", service.getModel().getName());
    }

    public void testHashGenerated() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        File inputFile = File.createTempFile("tmp", ".txt");
        inputFile.deleteOnExit();
        FileUtils.writeStringToFile(inputFile, "test");

        System.out.println(inputFile.getAbsolutePath());
        MuleMessage message = client.send("vm://MSC.In", inputFile.getName(), null);
        assertEquals("098f6bcd4621d373cade4e832627b4f6", message.getPayloadAsString());        
    }

}
