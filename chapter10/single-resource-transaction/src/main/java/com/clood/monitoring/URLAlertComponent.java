package com.clood.monitoring;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;

public class URLAlertComponent {

    public Object process(Object object) throws Exception {
        return getData((String) object);
    }


    Map getData(String email) {

        Map result = new HashMap();

        String[] fields = email.split("\n|\r");

        for (String field : fields) {
            if (field.matches("^Host:.*")) {
                result.put("HOST", splitField(field));
            }

            if (field.matches("^Message:.*")) {
                result.put("MESSAGE", splitField(field));
            }
        }

        result.put("TIMESTAMP", new Date());
        return result;

    }

    String splitField(String field) {
        return field.split(":\\s*", 2)[1];
    }
}
