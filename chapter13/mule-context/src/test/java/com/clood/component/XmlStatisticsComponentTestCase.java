package com.clood.component;

import java.io.StringReader;

import javax.xml.xpath.XPathFactory;

import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.xml.sax.InputSource;

/**
 * @author David Dossot (david@dossot.net)
 */
public class XmlStatisticsComponentTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/xml-statistics-config.xml";
    }

    public void testRendersAllXmlStatistics() throws Exception {
        final MuleClient muleClient = new MuleClient(muleContext);

        final String xmlStatistics =
                muleClient.send("vm://XmlStats.IN", null, null).getPayloadAsString();

        muleClient.dispose();

        assertNotNull(xmlStatistics);

        assertEquals("2", XPathFactory.newInstance().newXPath().evaluate(
                "count(/Components/Service)",
                new InputSource(new StringReader(xmlStatistics))));

    }

    public void testRendersOneServiceXmlStatistics() throws Exception {
        final MuleClient muleClient = new MuleClient(muleContext);

        final String xmlStatistics =
                muleClient.send("vm://XmlStats.IN", "Emailer", null).getPayloadAsString();

        muleClient.dispose();

        assertNotNull(xmlStatistics);

        assertEquals("1", XPathFactory.newInstance().newXPath().evaluate(
                "count(/Components/Service)",
                new InputSource(new StringReader(xmlStatistics))));
    }

}
