package com.muleinaction;

import org.mule.DefaultMuleEvent;
import org.mule.api.MuleEvent;
import org.mule.routing.AbstractCorrelationAggregator;
import org.mule.routing.AggregationException;
import org.mule.routing.EventGroup;

public class FarmResponseAggregator extends AbstractCorrelationAggregator {

    @Override
    protected MuleEvent aggregateEvents(EventGroup events) throws AggregationException {
        try {
            FarmSelectionService selector = new FarmSelectionService();
            return new DefaultMuleEvent(selector.selectFarmStatistics(events), events.getMessageCollectionEvent());
        }
        catch (Exception e) {
            throw new AggregationException(events, null, e);
        }
    }


}
