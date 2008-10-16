package com.clood.component;

import org.apache.commons.lang.Validate;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleException;
import org.mule.api.endpoint.EndpointBuilder;
import org.mule.api.endpoint.EndpointException;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.api.lifecycle.Callable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;

import com.clood.model.Client;

/**
 * @author David Dossot (david@dossot.net)
 */
public class ActivityReportService implements Initialisable, Callable {

    private EndpointBuilder errorProcessorChannelBuilder;

    private OutboundEndpoint errorProcessorChannel;

    public void setErrorProcessorChannel(
            final EndpointBuilder errorProcessorChannelBuilder) {

        this.errorProcessorChannelBuilder = errorProcessorChannelBuilder;
    }

    public void initialise() throws InitialisationException {
        try {
            errorProcessorChannel = errorProcessorChannelBuilder
                    .buildOutboundEndpoint();
        } catch (final EndpointException ee) {
            throw new InitialisationException(ee, this);
        }
    }

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        eventContext.setStopFurtherProcessing(true);

        final Object payload = eventContext.transformMessage();

        try {
            validatePayloadIsValidClient(payload);
        } catch (final RuntimeException re) {

            try {
                eventContext.dispatchEvent(eventContext.getMessage(),
                        errorProcessorChannel);
            } catch (final MuleException me) {
                return "ERROR: " + me.getMessage();
            }

            return "ERROR: " + re.getMessage();
        }

        eventContext.dispatchEvent(payload);

        return "OK";
    }

    private void validatePayloadIsValidClient(final Object payload) {
        Validate.notNull(payload, "Payload is null");

        Validate.isTrue(payload instanceof Client, "Payload is not a: "
                + Client.class.getName() + " but a: "
                + payload.getClass().getName());

        Validate.isTrue(((Client) payload).getId() > 0,
                "Client ID must be a positive long");
    }

}
