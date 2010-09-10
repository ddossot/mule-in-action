
package com.clood.interceptor;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.context.MuleContextAware;
import org.mule.api.context.notification.MuleContextNotificationListener;
import org.mule.api.interceptor.Interceptor;
import org.mule.api.processor.MessageProcessor;
import org.mule.context.notification.MuleContextNotification;
import org.mule.context.notification.NotificationException;

/**
 * @author David Dossot (david@dossot.net)
 */
// <start id="BrokerNotReadyInterceptor"/>
public final class BrokerNotReadyInterceptor
    implements MuleContextAware, MuleContextNotificationListener<MuleContextNotification>, Interceptor
{
    private volatile boolean brokerReady = false;
    private MessageProcessor next;

    public void setMuleContext(final MuleContext context)
    {
        try
        {
            context.registerListener(this);
        }
        catch (final NotificationException ne)
        {
            throw new RuntimeException(ne);
        }
    }

    public void setListener(MessageProcessor listener)
    {
        next = listener;
    }

    public void onNotification(final MuleContextNotification notification)
    {
        final int action = notification.getAction();

        if (action == MuleContextNotification.CONTEXT_STARTED)
        {
            brokerReady = true;
        }
        else if (action == MuleContextNotification.CONTEXT_STOPPED)
        {
            brokerReady = false;
        }
    }

    public MuleEvent process(MuleEvent event) throws MuleException
    {
        if (!brokerReady)
        {
            throw new IllegalStateException("Invocation of service " + event.getFlowConstruct().getName()
                                            + " impossible at this time!");
        }

        return next.process(event);
    }

}
// <end id="BrokerNotReadyInterceptor"/>
