package com.muleinaction;

import org.mule.api.MuleMessage;
import org.mule.routing.AggregationException;
import org.mule.routing.CollectionCorrelatorCallback;
import org.mule.routing.EventCorrelatorCallback;
import org.mule.routing.inbound.EventGroup;
import org.mule.routing.response.ResponseCorrelationAggregator;

public class FareResponseAggregator extends ResponseCorrelationAggregator
{
    protected EventCorrelatorCallback getCorrelatorCallback()
    {
        return new CollectionCorrelatorCallback()
        {

            public MuleMessage aggregateEvents(EventGroup events) throws AggregationException
            {
                System.out.println("\n\n\n************************   EVENT SIZE: " +  events.size() + "******************* \n\n\n\n");
                
                try
                {
                    FareSelectionService selector = new FareSelectionService();
                    return selector.selectFare(events);
                }
                catch (Exception e)
                {
                    throw new AggregationException(events, null, e);
                }
            }
        };
    }
}
