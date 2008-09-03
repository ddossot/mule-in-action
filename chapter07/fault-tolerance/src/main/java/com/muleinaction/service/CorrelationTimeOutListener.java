package com.muleinaction.service;

import java.util.HashMap;

import org.mule.api.MuleException;
import org.mule.api.MuleMessageCollection;
import org.mule.api.context.notification.RoutingNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.context.notification.RoutingNotification;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
public class CorrelationTimeOutListener implements RoutingNotificationListener {

    private String dlqAddress;

    private MuleClient muleClient;

    public void setDlqAddress(final String dlqAddress) {
        this.dlqAddress = dlqAddress;
    }

    public void initialize() throws MuleException {
        muleClient = new MuleClient();
    }

    public void onNotification(final ServerNotification notification) {
        if (notification.getAction() != RoutingNotification.CORRELATION_TIMEOUT) {
            return;
        }

        final MuleMessageCollection messageCollection =
                (MuleMessageCollection) notification.getSource();

        try {
            // we assume here that we care only about the first message of the
            // aggregation collection
            muleClient.sendNoReceive(dlqAddress,
                    messageCollection.getMessagesAsArray()[0].getPayload(),
                    new HashMap<Object, Object>());

        } catch (final MuleException me) {
            // here we should log a serialized form of the message, using a
            // specific file appender that target a DLQ log file
            System.err.println(messageCollection);
        }
    }
}
