package com.muleinaction.service;

import java.util.HashMap;
import java.util.Map;

import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.MuleMessageCollection;
import org.mule.api.context.notification.RoutingNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.context.notification.RoutingNotification;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
// <start id="CorrelationTimeOutListener"/>
public final class CorrelationTimeOutListener implements
        RoutingNotificationListener {

    public void onNotification(final ServerNotification notification) {
        if (notification.getAction() != RoutingNotification.CORRELATION_TIMEOUT) {
            return;
        }

        final MuleMessageCollection messageCollection = (MuleMessageCollection) notification
                .getSource();
        // <end id="CorrelationTimeOutListener"/>

        try {
            // we assume here that we care only about the first message of the
            // aggregation collection
            final MuleMessage uncorrelatedMessage = messageCollection
                    .getMessagesAsArray()[0];

            final Map<String, Object> properties = new HashMap<String, Object>();

            for (final Object propertyName : uncorrelatedMessage
                    .getPropertyNames()) {
                properties.put(propertyName.toString(), uncorrelatedMessage
                        .getProperty(propertyName.toString()));
            }

            new MuleClient().sendNoReceive(dlqAddress, uncorrelatedMessage
                    .getPayload(), properties);

        } catch (final MuleException me) {
            // here we should log a serialized form of the message, using a
            // specific file appender that target a DLQ log file
            System.err.println(messageCollection);
        }
    }

    private String dlqAddress;

    public void setDlqAddress(final String dlqAddress) {
        this.dlqAddress = dlqAddress;
    }
}
