package com.muleinaction.lifecycle;

import java.util.ArrayList;
import java.util.List;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationException;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Lifecycle;
import org.mule.api.service.Service;
import org.mule.api.service.ServiceAware;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecycleTrackerTransformer extends AbstractTransformer implements
        Lifecycle, MuleContextAware, ServiceAware {

    private final List<String> tracker = new ArrayList<String>();

    public List<String> getTracker() {
        return tracker;
    }

    public void setProperty(final String value) {
        tracker.add("setProperty");
    }

    public void setMuleContext(final MuleContext context) {
        tracker.add("setMuleContext");
    }

    public void setService(final Service service) throws ConfigurationException {
        tracker.add("setService");
    }

    @Override
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

    @Override
    protected Object doTransform(final Object src, final String encoding)
            throws TransformerException {

        // dirty trick to get the transformer instance that was used for the
        // request
        return this;
    }

}
