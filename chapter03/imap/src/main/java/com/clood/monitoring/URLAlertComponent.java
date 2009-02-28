package com.clood.monitoring;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class URLAlertComponent {

    public Object process(Object object) throws Exception {
        return getData((String) object);
    }

    Map getData(String email) throws ParseException {

        Map result = new HashMap();

        String[] fields = email.split("\n|\r");

        for (String field : fields) {
            if (field.matches("^Host:.*")) {
                result.put("HOST", splitField(field));
            }

            if (field.matches("^Message:.*")) {
                result.put("MESSAGE", splitField(field));
            }

            if (field.matches("^Alert Time:.*")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S");
                result.put("TIMESTAMP",new Timestamp(format.parse(splitField(field)).getTime()));
            }
        }

        return result;

    }

    String splitField(String field) {
        return field.split(":\\s*", 2)[1];
    }
}
