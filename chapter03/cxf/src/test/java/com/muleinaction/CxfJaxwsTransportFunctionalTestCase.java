package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class CxfJaxwsTransportFunctionalTestCase extends FunctionalTestCase {

    
    @Override
    protected String getConfigResources() {
        return "conf/cxf-jaxws-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "cxfJaxwsService");
        assertNotNull(service);
        assertEquals("cxfJaxwsModel", service.getModel().getName());
    }

    public void testMessageSentAndReceived() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        MuleMessage response = muleClient.send("http://localhost:9781/greeting",
                SOAP_REQUEST, null);
        assertNotNull(response);
        assertEquals(SOAP_RESPONSE, response.getPayloadAsString());
    }

    private static String SOAP_REQUEST = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:jax=\"http://jaxws.cxf.muleinaction.com/\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <jax:sayGreeting>\n" +
            "         <arg0>John</arg0>\n" +
            "      </jax:sayGreeting>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";
    
    private static String SOAP_RESPONSE = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
            "<soap:Body><ns2:sayGreetingResponse xmlns:ns2=\"http://jaxws.cxf.muleinaction.com/\">" +
            "<return>Hello, John</return></ns2:sayGreetingResponse></soap:Body></soap:Envelope>";

}
