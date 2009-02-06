package com.muleinaction;

import org.mule.api.MuleMessage;
import org.mule.routing.AggregationException;
import org.mule.routing.CollectionCorrelatorCallback;
import org.mule.routing.EventCorrelatorCallback;
import org.mule.routing.inbound.EventGroup;
import org.mule.routing.response.ResponseCorrelationAggregator;

public class FarmResponseAggregator extends ResponseCorrelationAggregator
{
    protected EventCorrelatorCallback getCorrelatorCallback()
    {
        return new CollectionCorrelatorCallback()
        {

            public MuleMessage aggregateEvents(EventGroup events) throws AggregationException
            {
                try
                {
                    FarmSelectionService selector = new FarmSelectionService();
                    return selector.selectFarmStatistics(events);
                }
                catch (Exception e)
                {
                    throw new AggregationException(events, null, e);
                }
            }
        };
    }
}