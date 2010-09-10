
package com.muleinaction;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.mule.api.MuleMessage;
import org.mule.api.service.Service;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class RefreshableGroovyComponentFunctionalTestCase extends FunctionalTestCase
{

    @Override
    protected String getConfigResources()
    {
        return "conf/refreshable-groovy-component-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception
    {
        final Service springMessageEnrichmentService = muleContext.getRegistry().lookupService(
            "springMessageEnrichmentService");

        assertNotNull(springMessageEnrichmentService);
        assertEquals("springMessageEnrichmentModel", springMessageEnrichmentService.getModel().getName());
    }

    public void testMessageReceived() throws Exception
    {
        final MuleClient client = new MuleClient(muleContext);

        FileUtils.writeStringToFile(new File("script/messageEnricher.groovy"), MESSAGE_ENRICHER_GROOVY_HIGH);
        Thread.sleep(3000);

        client.send("vm://in", "STATUS: CRITICAL", null);
        MuleMessage message = client.request("vm://out", 15000);
        assertNotNull(message);
        assertEquals("HIGH", message.findPropertyInAnyScope("PRIORITY", null));

        client.send("vm://in", "STATUS: NORMAL", null);
        message = client.request("vm://out", 15000);
        assertNotNull(message);
        assertEquals("LOW", message.findPropertyInAnyScope("PRIORITY", null));
    }

    public void testMessageReceivedWithRefresh() throws Exception
    {
        final MuleClient client = new MuleClient(muleContext);

        FileUtils.writeStringToFile(new File("script/messageEnricher.groovy"), MESSAGE_ENRICHER_GROOVY_NORMAL);
        Thread.sleep(3000);

        client.send("vm://in", "STATUS: CRITICAL", null);
        MuleMessage message = client.request("vm://out", 15000);
        assertNotNull(message);
        assertEquals("NORMAL", message.findPropertyInAnyScope("PRIORITY", null));

        client.send("vm://in", "STATUS: NORMAL", null);
        message = client.request("vm://out", 15000);
        assertNotNull(message);
        assertEquals("LOW", message.findPropertyInAnyScope("PRIORITY", null));
    }

    private static String MESSAGE_ENRICHER_GROOVY_HIGH = "import org.mule.api.MuleEventContext\n"
                                                         + "import org.mule.api.lifecycle.Callable\n"
                                                         + "\n"
                                                         + "class MessageEnricher implements Callable {\n"
                                                         + "\n"
                                                         + "    public Object onCall(MuleEventContext muleEventContext) {\n"
                                                         + "        def message = muleEventContext.getMessage()\n"
                                                         + "        if (message.payload =~ \"STATUS: CRITICAL\") {\n"
                                                         + "            message.setProperty(\"PRIORITY\", 'HIGH')\n"
                                                         + "        } else {\n"
                                                         + "            message.setProperty(\"PRIORITY\", 'LOW')\n"
                                                         + "\n" + "        }\n" + "        return message\n"
                                                         + "    }\n" + "}";

    private static String MESSAGE_ENRICHER_GROOVY_NORMAL = "import org.mule.api.MuleEventContext\n"
                                                           + "import org.mule.api.lifecycle.Callable\n"
                                                           + "\n"
                                                           + "class MessageEnricher implements Callable {\n"
                                                           + "\n"
                                                           + "    public Object onCall(MuleEventContext muleEventContext) {\n"
                                                           + "        def message = muleEventContext.getMessage()\n"
                                                           + "        if (message.payload =~ \"STATUS: CRITICAL\") {\n"
                                                           + "            message.setProperty(\"PRIORITY\", 'NORMAL')\n"
                                                           + "        } else {\n"
                                                           + "            message.setProperty(\"PRIORITY\", 'LOW')\n"
                                                           + "\n" + "        }\n"
                                                           + "        return message\n" + "    }\n" + "}";

}
