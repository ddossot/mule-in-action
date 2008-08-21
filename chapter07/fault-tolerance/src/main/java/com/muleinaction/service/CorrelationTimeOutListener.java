package com.muleinaction.service;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleException;
import org.mule.api.MuleMessageCollection;
import org.mule.api.context.notification.RoutingNotificationListener;
import org.mule.api.context.notification.ServerNotification;
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
        final MuleMessageCollection messageCollection =
                (MuleMessageCollection) notification.getSource();

        try {
            // we assume here that we care only about the first message of the
            // aggregation collection
            muleClient.dispatch(dlqAddress, new DefaultMuleMessage(
                    messageCollection.getMessagesAsArray()[0],
                    messageCollection));

        } catch (final MuleException me) {
            // log a serialized form of the message, using a specific file
            // appender that target a DLQ log file
            System.err.println(messageCollection);
        }
    }
}
