package com.muleinaction.lifecycle;

import java.util.ArrayList;
import java.util.List;

import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleException;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.Callable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Lifecycle;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecycleTrackerComponent implements Lifecycle, MuleContextAware,
        Callable {

    private final static List<String> TRACKER = new ArrayList<String>();

    public static List<String> getTracker() {
        return TRACKER;
    }

    public void setProperty(final String value) {
        TRACKER.add("springSetProperty");
    }

    public void setMuleContext(final MuleContext context) {
        TRACKER.add("setMuleContext");
    }

    public void springInitialize() {
        TRACKER.add("springInitialize");
    }

    public void springDestroy() {
        TRACKER.add("springDestroy");
    }

    public void initialise() throws InitialisationException {
        TRACKER.add("initialise");
    }

    public void start() throws MuleException {
        TRACKER.add("start");
    }

    public void stop() throws MuleException {
        TRACKER.add("stop");
    }

    public void dispose() {
        TRACKER.add("dispose");
    }

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        // do nothing in particular
        return null;
    }

}
