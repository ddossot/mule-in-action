package com.muleinaction;

import org.mule.api.config.ConfigurationBuilder;
import org.mule.module.scripting.builders.ScriptConfigurationBuilder;

/**
 * @author David Dossot (david@dossot.net)
 */
public class EchoWorldGroovyFunctionalTestCase extends
        EchoWorldXmlFunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/echo-config.groovy";
    }

    @Override
    protected ConfigurationBuilder getBuilder() throws Exception {
        return new ScriptConfigurationBuilder("groovy", getConfigResources());
    }

}
