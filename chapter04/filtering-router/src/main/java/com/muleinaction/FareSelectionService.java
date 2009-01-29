package com.muleinaction;

import java.util.List;

import javax.jms.ObjectMessage;

import com.muleinaction.common.Fare;

public class FareSelectionService {

    public Fare selectFares(final Object object) throws Exception {

        final List fares = (List) object;

        Fare cheapestFare = null;

        for (int i = 0; i < fares.size(); i++) {
            final ObjectMessage message = (ObjectMessage) fares.get(i);
            final Fare currentFare = (Fare) message.getObject();

            if (cheapestFare == null)
                cheapestFare = currentFare;
            else if (currentFare.getPrice() < cheapestFare.getPrice())
                cheapestFare = currentFare;
        }

        return cheapestFare;
    }
}
