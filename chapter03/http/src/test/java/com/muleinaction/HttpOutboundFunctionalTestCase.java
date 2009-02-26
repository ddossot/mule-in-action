package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class HttpOutboundFunctionalTestCase extends FunctionalTestCase {

    private static String DEST_DIRECTORY = "./data/reports";

    @Override
    protected String getConfigResources() {
        return "conf/http-outbound-config.xml";
    }

    protected void doSetUp() throws Exception {
        super.doSetUp();
        FileUtils.cleanDirectory(new File(DEST_DIRECTORY));
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "httpOutboundService");

        assertNotNull(service);
        assertEquals("httpOutboundModel", service.getModel().getName());
    }

    public void testMessageSentAndReceived() throws Exception {
        Thread.sleep(5000);
        assertEquals(1, FileUtils.listFiles(new File(DEST_DIRECTORY), new WildcardFileFilter("*.xml"), null).size());
        File file = (File) FileUtils.listFiles(new File(DEST_DIRECTORY), new WildcardFileFilter("*.xml"), null).toArray()[0];
       
        assertEquals(XML,FileUtils.readFileToString(file));

    }

    private static String XML = "<backup><host>esb01</host></backup>\n";

}
