package com.muleinaction.component;

import java.util.List;

import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

/**
 * 
 * @author David Dossot (david@dossot.net)
 */
public class RestComponentFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/rest-wrapper-config.xml";
    }

    public void testGoogleSearch() throws Exception {
        final List<?> results = (List<?>) new MuleClient(muleContext).send(
                "vm://GoogleSearch.In", "Mule ESB", null).getPayload();

        assertNotNull(results);
        assertTrue(results.size() > 0);
        assertTrue(results.get(0) instanceof String);
    }

}
