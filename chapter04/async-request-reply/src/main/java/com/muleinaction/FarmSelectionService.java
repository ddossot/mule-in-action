package com.muleinaction;

import com.muleinaction.common.FarmStatus;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.routing.inbound.EventGroup;

public class FarmSelectionService {

    public MuleMessage selectFarmStatistics(EventGroup events) throws Exception {

        List results = new ArrayList();

        MuleEvent event = null;

        for (Iterator iterator = events.iterator(); iterator.hasNext();)
        {
            event = (MuleEvent)iterator.next();
            Object o = event.transformMessage();

            FarmStatus farmStatus;

            if(o instanceof FarmStatus)
            {
                farmStatus = (FarmStatus) o;
                results.add(farmStatus);
            }
            else
            {
                throw new IllegalArgumentException("Object received is not a FarmStatus,");
            }

       }

        FarmStatus bestFarmStatus = null;

        for (int i=0; i < results.size(); i++) {
            FarmStatus currentFarmStatus = (FarmStatus) results.get(i);
            if (bestFarmStatus == null)
                bestFarmStatus = currentFarmStatus;
            else if (currentFarmStatus.getVmCount() < bestFarmStatus.getVmCount())
                bestFarmStatus = currentFarmStatus;
        }
        return new DefaultMuleMessage(bestFarmStatus, event.getMessage());
    }
}