package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class RefreshableGroovyTransformerFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/refreshable-groovy-transformer-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service springTransformerService = muleContext.getRegistry().lookupService(
                "springTransformerService");

        assertNotNull(springTransformerService);
        assertEquals("springTransformerModel", springTransformerService.getModel().getName());
    }

    public void testMessageTransformed() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        FileUtils.writeStringToFile(new File("script/toUpper.groovy"), toUpperGroovyScript);
        Thread.sleep(3000);        
        client.sendAsync("vm://in", "hello, world.", null);
        MuleMessage message = client.request("vm://out", 15000);
        assertEquals("HELLO, WORLD.", message.getPayloadAsString());
    }

    public void testMessageTransformedWithRefresh() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        FileUtils.writeStringToFile(new File("script/toUpper.groovy"), toLowerGroovyScript);
        Thread.sleep(3000);
        client.sendAsync("vm://in", "HELLO, WORLD.", null);
        MuleMessage message = client.request("vm://out", 15000);
        assertEquals("hello, world.", message.getPayloadAsString());
    }

    private String toLowerGroovyScript = "import org.mule.transformer.AbstractTransformer\n" +
            "\n" +
            "class ToUpperTransformer extends AbstractTransformer {\n" +
            "\n" +
            "    protected Object doTransform(Object o, String s) {\n" +
            "        return o.toLowerCase();\n" +
            "    }\n" +
            "\n" +
            "}";

    private String toUpperGroovyScript = "import org.mule.transformer.AbstractTransformer\n" +
            "\n" +
            "class ToUpperTransformer extends AbstractTransformer {\n" +
            "\n" +
            "    protected Object doTransform(Object o, String s) {\n" +
            "        return o.toUpperCase();\n" +
            "    }\n" +
            "\n" +
            "}";

}
