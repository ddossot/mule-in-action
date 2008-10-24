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
public class ClientValidatorService implements Initialisable, Callable {

    private volatile boolean initialized = false;

    private EndpointBuilder errorProcessorChannelBuilder;

    private OutboundEndpoint errorProcessorChannel;

    // <start id="Lifecycle-Validator"/>
    public void setErrorProcessorChannel(
            final EndpointBuilder errorProcessorChannelBuilder) {

        this.errorProcessorChannelBuilder = errorProcessorChannelBuilder;
    }

    public void initialise() throws InitialisationException {
        if (initialized) {
            return;
        }

        try {
            errorProcessorChannel = errorProcessorChannelBuilder
                    .buildOutboundEndpoint();

            initialized = true;
        } catch (final EndpointException ee) {
            throw new InitialisationException(ee, this);
        }
    }

    // <end id="Lifecycle-Validator"/>

    // <start id="EventContext-Validator"/>
    public Object onCall(final MuleEventContext eventContext) throws Exception {
        eventContext.setStopFurtherProcessing(true);

        final Object payload = eventContext.transformMessage();

        try {
            validatePayloadIsValidClient(payload);
        } catch (final IllegalArgumentException iae) {

            try {
                eventContext.dispatchEvent(eventContext.getMessage(),
                        errorProcessorChannel);
            } catch (final MuleException me) {
                processMuleException(me);
            }

            return "ERROR: " + iae.getMessage();
        }

        eventContext.dispatchEvent(payload);

        return "OK";
    }

    // <end id="EventContext-Validator"/>

    private void validatePayloadIsValidClient(final Object payload) {
        Validate.notNull(payload, "Payload is null");

        Validate.isTrue(payload instanceof Client, "Payload is not a: "
                + Client.class.getName() + " but a: "
                + payload.getClass().getName());

        Validate.isTrue(((Client) payload).getId() > 0,
                "Client ID must be a positive long");
    }

    private void processMuleException(final MuleException me) {
        // do something smart with the exception
    }
}
