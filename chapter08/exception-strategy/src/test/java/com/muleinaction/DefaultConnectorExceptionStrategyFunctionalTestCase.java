package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class DefaultConnectorExceptionStrategyFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/default-connector-exception-strategy-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "defaultConnectorExceptionStrategyService");

        assertNotNull(service);
        assertEquals("defaultConnectorExceptionStrategyModel", service.getModel().getName());
    }

}
