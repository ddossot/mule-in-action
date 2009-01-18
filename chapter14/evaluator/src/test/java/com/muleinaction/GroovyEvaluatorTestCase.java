package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class GroovyEvaluatorTestCase extends FunctionalTestCase {

	@Override
	protected String getConfigResources() {
		return "conf/groovy-evaluator-config.xml";
	}

	public void testCorrectlyInitialized() throws Exception {
		final Service service = muleContext.getRegistry().lookupService(
				"groovyExpressionService");

		assertNotNull(service);
		assertEquals("groovyExpressionModel", service.getModel().getName());
	}

}
