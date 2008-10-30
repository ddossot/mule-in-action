package com.clood.interceptor;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.context.MuleContextAware;
import org.mule.api.context.notification.MuleContextNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.api.interceptor.Interceptor;
import org.mule.api.interceptor.Invocation;
import org.mule.context.notification.MuleContextNotification;
import org.mule.context.notification.NotificationException;

/**
 * @author David Dossot (david@dossot.net)
 */
// <start id="BrokerNotReadyInterceptor"/>
public final class BrokerNotReadyInterceptor implements MuleContextAware,
        MuleContextNotificationListener, Interceptor {

    private volatile boolean brokerReady = false;

    public void setMuleContext(final MuleContext context) {
        try {
            context.registerListener(this);
        } catch (final NotificationException ne) {
            throw new RuntimeException(ne);
        }
    }

    public void onNotification(final ServerNotification notification) {
        if (notification.getAction() == MuleContextNotification.CONTEXT_STARTED) {
            brokerReady = true;
        }
    }

    public MuleMessage intercept(final Invocation invocation)
            throws MuleException {

        if (!brokerReady) {
            return null;
        }

        return invocation.invoke();
    }
}
// <end id="BrokerNotReadyInterceptor"/>