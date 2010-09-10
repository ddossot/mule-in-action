
package com.muleinaction;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.mule.api.MuleEventContext;
import org.mule.api.service.Service;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.mule.tck.functional.EventCallback;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class CustomGroovyFilterFunctionalTestCase extends FunctionalTestCase
{

    @Override
    protected String getConfigResources()
    {
        return "conf/custom-groovy-filter-config.xml";
    }

    private final CountDownLatch latch = new CountDownLatch(1);

    @Override
    protected void doSetUp() throws Exception
    {
        super.doSetUp();

        getFunctionalTestComponent("springFilterService").setEventCallback(new EventCallback()
        {
            public void eventReceived(MuleEventContext context, Object component) throws Exception
            {
                latch.countDown();
            }
        });
    }

    public void testCorrectlyInitialized() throws Exception
    {
        final Service springFilterService = muleContext.getRegistry().lookupService("springFilterService");
        final Service rhinoMessageEnrichmentService = muleContext.getRegistry().lookupService(
            "rhinoMessageEnrichmentService");

        assertNotNull(springFilterService);
        assertNotNull(rhinoMessageEnrichmentService);
        assertEquals("springFilterModel", springFilterService.getModel().getName());
    }

    public void testMessageReceived() throws Exception
    {
        final MuleClient client = new MuleClient(muleContext);
        client.send("vm://in", "test", null);
        assertTrue("Message did not reach service on time", latch.await(15, TimeUnit.SECONDS));
    }

}
