package com.muleinaction.service;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

/**
 * @author David Dossot (david@dossot.net)
 */
public class DodgyService implements Callable {

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        throw new RuntimeException("Simulated service error");
    }

}
