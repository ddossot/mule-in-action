package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import com.muleinaction.common.Fare;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;

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

        ResponseTimeMetric metric1 = new ResponseTimeMetric("RUN-123456","client1",1.567,new Date());
        ResponseTimeMetric metric2 = new ResponseTimeMetric("RUN-123456","client2",2.345,new Date());
        ResponseTimeMetric metric3 = new ResponseTimeMetric("RUN-123456","client3",0.334,new Date());

        Map properties = new HashMap();
        properties.put("MULE_CORRELATION_ID", metric1.getJobId());
        properties.put("MULE_CORRELATION_GROUP_SIZE", "3");

        MuleClient muleClient = new MuleClient(muleContext);
        muleClient.sendAsync("jms://metrics.responsetimes", metric1, properties);
        muleClient.sendAsync("jms://metrics.responsetimes", metric2, properties);
        muleClient.sendAsync("jms://metrics.responsetimes", metric3, properties);

        MuleMessage response = muleClient.request("jms://topic:metrics.avg.responsetimes", 3000);
        assertNotNull(response);
        Double average = (Double) response.getPayload();
        assertEquals(1.4153333333333336, average, 0.000001);
    }

}
