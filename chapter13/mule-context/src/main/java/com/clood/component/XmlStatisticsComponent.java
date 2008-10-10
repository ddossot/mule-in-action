package com.clood.component;

import java.io.StringWriter;

import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.api.service.Service;
import org.mule.management.stats.printers.XMLPrinter;
import org.mule.transport.NullPayload;

/**
 * Returns all the statistics of a Mule instance as a string.
 * 
 * @author David Dossot (david@dossot.net)
 */
public class XmlStatisticsComponent implements Callable {

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        final StringWriter xmlStatisticsWriter = new StringWriter();

        final Object payload = eventContext.getMessage().getPayload();

        final MuleContext muleContext = eventContext.getMuleContext();

        if (payload instanceof NullPayload) {
            muleContext.getStatistics().logSummary(
                    new XMLPrinter(xmlStatisticsWriter));
        } else {
            final Service service =
                    muleContext.getRegistry().lookupService(payload.toString());

            service.getStatistics().logSummary(
                    new XMLPrinter(xmlStatisticsWriter));
        }

        return xmlStatisticsWriter.toString();
    }

}
