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
public class HttpInboundFunctionalTestCase extends FunctionalTestCase {

    private static String DEST_DIRECTORY = "./data/reports";

    @Override
    protected String getConfigResources() {
        return "conf/http-inbound-config.xml";
    }

    protected void doSetUp() throws Exception {
        super.doSetUp();
        FileUtils.cleanDirectory(new File(DEST_DIRECTORY));
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "httpInboundService");
        assertNotNull(service);
        assertEquals("httpInboundModel", service.getModel().getName());
    }

    public void testMessageSentAndReceived() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        MuleMessage response = muleClient.send("http://localhost:9765/backup-reports",
                REPORT_XML, null);
        assertEquals(1, FileUtils.listFiles(new File(DEST_DIRECTORY), new WildcardFileFilter("*.xml"), null).size());
        File file = (File) FileUtils.listFiles(new File(DEST_DIRECTORY), new WildcardFileFilter("*.xml"), null).toArray()[0];
        assertEquals(REPORT_XML, FileUtils.readFileToString(file));
    }

    private static String REPORT_XML = "<backup><host>esb01</host></backup>";

}
