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

    public void testRendersXmlStatistics() throws Exception {
        final MuleClient muleClient = new MuleClient(muleContext);

        final String xmlStatistics =
                muleClient.send("vm://XmlStats.IN", null, null).getPayloadAsString();

        assertNotNull(xmlStatistics);

        assertEquals("XmlStatisticsDump",
                XPathFactory.newInstance().newXPath().evaluate(
                        "/Components/Service[@name='XmlStatisticsDump']/@name",
                        new InputSource(new StringReader(xmlStatistics))));

        muleClient.dispose();
    }

}
