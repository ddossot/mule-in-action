package com.muleinaction.mulecontext;

import java.math.BigInteger;

import org.mule.api.endpoint.EndpointBuilder;
import org.mule.api.registry.MuleRegistry;
import org.mule.config.spring.SpringRegistry;
import org.mule.tck.FunctionalTestCase;
import org.springframework.context.ApplicationContext;

/**
 * @author David Dossot (david@dossot.net)
 */
public class RegistryTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/registry-config.xml";
    }

    public void testGetMuleObject() throws Exception {
        final Object muleObject = muleContext.getRegistry().lookupObject(
                "MuleObject");

        assertTrue(muleObject.toString(), muleObject instanceof EndpointBuilder);
    }

    public void testGetSpringBeanDirect() throws Exception {
        final Object springBean = muleContext.getRegistry().lookupObject(
                "SpringBean");

        assertTrue(springBean instanceof BigInteger);
    }

    public void testGetSpringBeanViaApplicationContext() throws Exception {
        final ApplicationContext ac = (ApplicationContext) muleContext
                .getRegistry().lookupObject(
                        SpringRegistry.SPRING_APPLICATION_CONTEXT);

        final Object springBean = ac.getBean("SpringBean", BigInteger.class);

        assertNotNull(springBean);
    }

    public void testPutInTransientRegistry() throws Exception {
        final MuleRegistry registry = muleContext.getRegistry();

        assertNull(registry.lookupObject("bar"));

        registry.registerObject("bar", 123L);

        assertEquals(123L, registry.lookupObject("bar"));
    }
}
