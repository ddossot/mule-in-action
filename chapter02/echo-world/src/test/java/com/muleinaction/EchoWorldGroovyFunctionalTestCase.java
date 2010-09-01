package com.muleinaction;

import org.mule.api.config.ConfigurationBuilder;
import org.mule.module.scripting.builders.ScriptConfigurationBuilder;
import org.mule.tck.FunctionalTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class EchoWorldGroovyFunctionalTestCase extends
        FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/echo-config.groovy";
    }

    @Override
    protected ConfigurationBuilder getBuilder() throws Exception {
        return new ScriptConfigurationBuilder("groovy", getConfigResources());
    }
    
    public void testCorrectlyInitialized() throws Exception {
        final Object service = muleContext.getRegistry().lookupObject(
                "echoService");

        assertNotNull(service);
    }

}
