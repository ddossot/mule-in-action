package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.apache.commons.io.FileUtils;

import java.util.Map;
import java.util.HashMap;
import java.io.File;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class RefreshableGroovyComponentFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/refreshable-groovy-component-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service springMessageEnrichmentService = muleContext.getRegistry().lookupService(
                "springMessageEnrichmentService");

        assertNotNull(springMessageEnrichmentService);
        assertEquals("springMessageEnrichmentModel", springMessageEnrichmentService.getModel().getName());
    }

    public void testMessageReceived() throws Exception {
        MuleClient client = new MuleClient(muleContext);

        FileUtils.writeStringToFile(new File("script/messageEnricher.groovy"), MESSAGE_ENRICHER_GROOVY_HIGH);
        Thread.sleep(3000);

        client.send("vm://in", "STATUS: CRITICAL", null);
        MuleMessage message = client.request("vm://out", 15000);
        assertNotNull(message);
        assertEquals("HIGH", message.getProperty("PRIORITY"));

        client.send("vm://in", "STATUS: NORMAL", null);
        message = client.request("vm://out", 15000);
        assertNotNull(message);
        assertEquals("LOW", message.getProperty("PRIORITY"));
    }

    public void testMessageReceivedWithRefresh() throws Exception {
        MuleClient client = new MuleClient(muleContext);

        FileUtils.writeStringToFile(new File("script/messageEnricher.groovy"), MESSAGE_ENRICHER_GROOVY_NORMAL);
        Thread.sleep(3000);

        client.send("vm://in", "STATUS: CRITICAL", null);
        MuleMessage message = client.request("vm://out", 15000);
        assertNotNull(message);
        assertEquals("NORMAL", message.getProperty("PRIORITY"));

        client.send("vm://in", "STATUS: NORMAL", null);
        message = client.request("vm://out", 15000);
        assertNotNull(message);
        assertEquals("LOW", message.getProperty("PRIORITY"));
    }


    private static String MESSAGE_ENRICHER_GROOVY_HIGH = "import org.mule.api.MuleEventContext\n" +
            "import org.mule.api.lifecycle.Callable\n" +
            "\n" +
            "class MessageEnricher implements Callable {\n" +
            "\n" +
            "    public Object onCall(MuleEventContext muleEventContext) {\n" +
            "        def message = muleEventContext.getMessage()\n" +
            "        if (message.payload =~ \"STATUS: CRITICAL\") {\n" +
            "            message.setProperty(\"PRIORITY\", 'HIGH')\n" +
            "        } else {\n" +
            "            message.setProperty(\"PRIORITY\", 'LOW')\n" +
            "\n" +
            "        }\n" +
            "        return message\n" +
            "    }\n" +
            "}";

    private static String MESSAGE_ENRICHER_GROOVY_NORMAL = "import org.mule.api.MuleEventContext\n" +
            "import org.mule.api.lifecycle.Callable\n" +
            "\n" +
            "class MessageEnricher implements Callable {\n" +
            "\n" +
            "    public Object onCall(MuleEventContext muleEventContext) {\n" +
            "        def message = muleEventContext.getMessage()\n" +
            "        if (message.payload =~ \"STATUS: CRITICAL\") {\n" +
            "            message.setProperty(\"PRIORITY\", 'NORMAL')\n" +
            "        } else {\n" +
            "            message.setProperty(\"PRIORITY\", 'LOW')\n" +
            "\n" +
            "        }\n" +
            "        return message\n" +
            "    }\n" +
            "}";

}
