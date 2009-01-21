package com.clood.billing;

import java.util.Map;
import java.util.HashMap;

public class BillingServiceImpl {

    public Object process(Object object) {
        // do billing stuff..
        Map result = new HashMap();
        result.put("stat", "stats-123");
        return result;
    }
}
