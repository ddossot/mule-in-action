package com.muleinaction.lifecycle;

import java.util.ArrayList;
import java.util.List;

import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationException;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.Callable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Lifecycle;
import org.mule.api.service.Service;
import org.mule.api.service.ServiceAware;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecycleTrackerComponent implements Lifecycle, MuleContextAware,
        ServiceAware, Callable {

    private final List<String> tracker = new ArrayList<String>();

    public List<String> getTracker() {
        return tracker;
    }

    public void setProperty(final String value) {
        tracker.add("springSetProperty");
    }

    public void setMuleContext(final MuleContext context) {
        tracker.add("setMuleContext");
    }

    public void setService(final Service service) throws ConfigurationException {
        tracker.add("setService");
    }

    public void springInitialize() {
        tracker.add("springInitialize");
    }

    public void springDestroy() {
        tracker.add("springDestroy");
    }

    public void initialise() throws InitialisationException {
        tracker.add("initialise");
    }

    public void start() throws MuleException {
        tracker.add("start");
    }

    public void stop() throws MuleException {
        tracker.add("stop");
    }

    public void dispose() {
        tracker.add("dispose");
    }

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        return "ACK";
    }

}
