package com.muleinaction;

import java.util.Random;
import com.muleinaction.common.*;

public class FareGenerationService {

    String airline;

    public FareGenerationService(String name) {
        this.airline = name;
    }

    public Fare getFare(String request) {
        Random rand = new Random();
        Fare fare = new Fare();
        fare.setAirline(airline);
        fare.setType("Coach");
        fare.setPrice(rand.nextDouble() * 100.00);
        return fare;
    }
}
