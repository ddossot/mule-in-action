package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class CxfSimpleTransportFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/cxf-simple-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "cxfSimpleService");
        assertNotNull(service);
        assertEquals("cxfSimpleModel", service.getModel().getName());
    }

    public void testMessageSentAndReceived() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        MuleMessage response = muleClient.send("http://localhost:9781/greeting",
                SOAP_REQUEST, null);
        assertNotNull(response);
        assertEquals(SOAP_RESPONSE, response.getPayloadAsString());
    }

    private static String SOAP_REQUEST = "<soapenv:Envelope " +
            "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:sim=\"http://simple.cxf.muleinaction.com/\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <sim:getGreeting>\n" +
            "         <sim:name>John</sim:name>\n" +
            "      </sim:getGreeting>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    private static String SOAP_RESPONSE = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
            "<soap:Body><ns1:getGreetingResponse xmlns:ns1=\"http://simple.cxf.muleinaction.com/\">" +
            "<ns1:return>Hello, John</ns1:return></ns1:getGreetingResponse></soap:Body></soap:Envelope>";

}
