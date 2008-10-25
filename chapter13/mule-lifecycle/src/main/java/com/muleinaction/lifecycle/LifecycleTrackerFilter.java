package com.muleinaction.lifecycle;

import org.mule.api.MuleMessage;
import org.mule.api.routing.filter.Filter;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecycleTrackerFilter extends AbstractLifecycleTracker implements
        Filter {

    public boolean accept(final MuleMessage message) {
        return true;
    }

}
