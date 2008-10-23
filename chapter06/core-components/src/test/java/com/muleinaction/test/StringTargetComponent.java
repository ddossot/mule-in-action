package com.muleinaction.test;

import java.util.concurrent.atomic.AtomicReference;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

/**
 * @author David Dossot (david@dossot.net)
 */
public class StringTargetComponent implements Callable {

    private final AtomicReference<String> value = new AtomicReference<String>();

    public void reset() {
        value.set(null);
    }

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        final String payloadToString = eventContext.getMessage()
                .getPayload().toString();

        if (!value.compareAndSet(null, payloadToString)) {
            throw new IllegalStateException("Can be called only once!");
        }

        return payloadToString;
    }

    public String getValue() {
        return value.get();
    }
}
