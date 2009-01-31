package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import com.muleinaction.common.Fare;

import java.util.Map;
import java.util.HashMap;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class CollectionAggregatorFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/collection-aggregator-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "collectionAggregatorService");

        assertNotNull(service);
        assertEquals("collectionAggregatorModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {

        Fare fare1 = new Fare();
        fare1.setAirline("OCEANIC");
        fare1.setPrice(100.00);
        fare1.setType("COACH");

        Fare fare2 = new Fare();
        fare2.setAirline("JBLUE");
        fare2.setPrice(150.00);
        fare2.setType("COACH");

        Fare fare3 = new Fare();
        fare3.setAirline("AA");
        fare3.setPrice(170.00);
        fare3.setType("COACH");

        Map properties = new HashMap();
        properties.put("MULE_CORRELATION_ID", "AUS-2009010700");
        properties.put("MULE_CORRELATION_GROUP_SIZE", "3");

        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://fares", fare1, properties);
        muleClient.sendAsync("jms://fares", fare2, properties);
        muleClient.sendAsync("jms://fares", fare3, properties);

        MuleMessage response = muleClient.request("jms://topic:cheapFares", 30000);
        assertNotNull(response);
        Fare fare = (Fare) response.getPayload();
        assertEquals(100.00, fare.getPrice());
        assertEquals("OCEANIC", fare.getAirline());
    }

}
