package com.clood.component;

import java.io.StringWriter;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.management.stats.printers.XMLPrinter;

/**
 * Returns all the statistics of a Mule instance as a string.
 * 
 * @author David Dossot (david@dossot.net)
 */
public class XmlStatisticsComponent implements Callable {

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        final StringWriter xmlStatiticsWriter = new StringWriter();

        eventContext.getMuleContext().getStatistics().logSummary(
                new XMLPrinter(xmlStatiticsWriter));

        return xmlStatiticsWriter.toString();
    }

}
