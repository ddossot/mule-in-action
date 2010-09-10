package com.muleinaction.service;

import java.util.List;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.context.MuleContextAware;
import org.mule.api.context.notification.RoutingNotificationListener;
import org.mule.context.notification.RoutingNotification;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
// <start id="CorrelationTimeOutListener"/>
public final class CorrelationTimeOutListener implements
        RoutingNotificationListener<RoutingNotification>, MuleContextAware {
   
    private MuleContext muleContext;
    
    public void setMuleContext(MuleContext muleContext) {
        this.muleContext = muleContext;
    }

    public void onNotification(final RoutingNotification notification) {
        if (notification.getAction() != RoutingNotification.CORRELATION_TIMEOUT) {
            return;
        }

        final MuleMessage uncorrelatedMessage = (MuleMessage) notification.getSource();
        // <end id="CorrelationTimeOutListener"/>

        try {
            // we assume here that we care only about the first message of the
            // aggregation collection
            @SuppressWarnings("unchecked")
            final Object uncorrelatedPayload = ((List<Object>)uncorrelatedMessage.getPayload()).toArray()[0];

            new MuleClient(muleContext).send(dlqAddress,
                                             new DefaultMuleMessage(uncorrelatedPayload, uncorrelatedMessage, muleContext));

        } catch (final MuleException me) {
            // here we should log a serialized form of the message, using a
            // specific file appender that target a DLQ log file
        }
    }

    private String dlqAddress;

    public void setDlqAddress(final String dlqAddress) {
        this.dlqAddress = dlqAddress;
    }
}
