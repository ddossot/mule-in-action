package com.clood.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.component.simple.LogService;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transformer.TransformerException;

/**
 * Logs Mule Messages at INFO level for the specified log name. <b>This
 * component honors inbound transformers but does not return any payload, its
 * response should not be routed.</b>
 * 
 * @author David Dossot (david@dossot.net)
 */
public class AuditComponent implements Callable, LogService {

    private Log logger = LogFactory.getLog(AuditComponent.class);

    public void setLogName(final String logName) {
        logger = LogFactory.getLog(logName);
    }

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        log(buildLogEntry(eventContext));
        return null;
    }

    public void log(final String message) {
        logger.info(message);
    }

    String buildLogEntry(final MuleEventContext eventContext)
            throws TransformerException {

        final StringBuffer logEntry = new StringBuffer();

        writeMessageProperties(logEntry, eventContext);
        logEntry.append("\n");

        writePayload(logEntry, eventContext);
        logEntry.append("\n");

        logEntry.append("--------------------------------------------------\n");

        return logEntry.toString();
    }

    private void writePayload(final StringBuffer logEntry,
            final MuleEventContext eventContext) throws TransformerException {

        logEntry.append(eventContext.transformMessageToString());
    }

    private void writeMessageProperties(final StringBuffer logEntry,
            final MuleEventContext eventContext) {

        logEntry.append("{\n");

        final MuleMessage message = eventContext.getMessage();

        for (final Object propertyName : message.getPropertyNames()) {
            logEntry.append("  ");
            logEntry.append(propertyName);
            logEntry.append("=");
            logEntry.append(message.getProperty(propertyName.toString()));
            logEntry.append("\n");
        }

        logEntry.append("}");
    }

}
