package com.muleinaction;

import com.muleinaction.common.FarmStatus;

import java.util.Random;

public class FarmService {

    String farmName;

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public FarmStatus getFarmStatus(String query) {
        Random rand = new Random();
        return new FarmStatus(farmName, rand.nextInt(20));
    }
}
