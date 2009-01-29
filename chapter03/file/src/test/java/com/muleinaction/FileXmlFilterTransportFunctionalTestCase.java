package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.util.UUID;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class FileXmlFilterTransportFunctionalTestCase extends FunctionalTestCase {

    private static String SOURCE_DIRECTORY = "./data/snapshot";
    private static String DEST_DIRECTORY = "./data/archive";


    @Override
    protected String getConfigResources() {
        return "conf/file-xml-filter-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "fileService");

        assertNotNull(service);
        assertEquals("fileModel", service.getModel().getName());
    }

    public void testFileIsArchived() throws IOException, InterruptedException {
        String filename = "SNAPSHOT-" + UUID.randomUUID().toString() + ".xml";
        BufferedWriter out = new BufferedWriter(new FileWriter(SOURCE_DIRECTORY + "/" + filename));
        out.write("data");
        out.close();
        Thread.sleep(2000);

        assertEquals(1,
                FileUtils.listFiles(new File(DEST_DIRECTORY),
                        new WildcardFileFilter("*.xml"), null).size());

    }

}
