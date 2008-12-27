package com.clood;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

public class MessageEnricher implements Callable {

    public Object onCall(MuleEventContext muleEventContext) {
        MuleMessage message = muleEventContext.getMessage();
        message.setProperty("ORGANIZATION", "CLOOD");
        return message;
    }
}

