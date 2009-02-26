package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class HttpPollingFunctionalTestCase extends FunctionalTestCase {

    private static String DEST_DIRECTORY = "./data/polling";

    protected void doSetUp() throws Exception {
        super.doSetUp();
        FileUtils.cleanDirectory(new File(DEST_DIRECTORY));
    }

    @Override
    protected String getConfigResources() {
        return "conf/http-polling-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "httpPollingService");

        assertNotNull(service);
        assertEquals("httpPollingModel", service.getModel().getName());
        Thread.sleep(5000);
        assertEquals(1, FileUtils.listFiles(new File(DEST_DIRECTORY), new WildcardFileFilter("*.*"), null).size());
    }

}
