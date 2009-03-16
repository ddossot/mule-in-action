package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class MD5PropertyEnricherFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/md5-property-enricher-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "rhinoMessageEnrichmentService");
        assertNotNull(service);
        assertEquals("rhinoMessageEnrichmentModel", service.getModel().getName());
    }

}
