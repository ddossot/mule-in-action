package com.muleinaction;

import com.muleinaction.common.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.routing.inbound.EventGroup;

public class FareSelectionService {

    public MuleMessage selectFare(EventGroup events) throws Exception {

        List fares = new ArrayList();

        MuleEvent event = null;

        for (Iterator iterator = events.iterator(); iterator.hasNext();)
        {
            event = (MuleEvent)iterator.next();
            Object o = event.transformMessage();

            Fare fare;

            if(o instanceof Fare)
            {
                fare = (Fare) o;
                fares.add(fare);
            }
            else
            {
                throw new IllegalArgumentException("Object received is not a Fare,");
            }

       }

        Fare cheapestFare = null;

        for (int i=0; i < fares.size(); i++) {
            Fare currentFare = (Fare) fares.get(i);
            if (cheapestFare == null)
                cheapestFare = currentFare; 
            else if (currentFare.getPrice() < cheapestFare.getPrice())
                cheapestFare = currentFare; 
        }
        return new DefaultMuleMessage(cheapestFare, event.getMessage());
    }
}
