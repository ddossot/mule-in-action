
package com.clood.interceptor;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.interceptor.Interceptor;
import org.mule.api.processor.MessageProcessor;

/**
 * @author David Dossot (david@dossot.net)
 */
// <start id="PayloadCacheInterceptor"/>
public final class PayloadCacheInterceptor implements Interceptor
{
    private MessageProcessor next;
    private Ehcache cache;

    public void setListener(MessageProcessor listener)
    {
        next = listener;
    }

    public void setCache(final Ehcache cache)
    {
        this.cache = cache;
    }

    public MuleEvent process(MuleEvent event) throws MuleException
    {
        final MuleMessage currentMessage = event.getMessage();
        final Object key = currentMessage.getPayload();
        final Element cachedElement = cache.get(key);

        if (cachedElement != null)
        {
            return new DefaultMuleEvent(new DefaultMuleMessage(cachedElement.getObjectValue(),
                currentMessage, event.getMuleContext()), event);
        }

        // we don't synchronize so several threads can compete to fill the cache
        // for the same key: this is rare enough to be acceptable
        final MuleEvent result = next.process(event);
        cache.put(new Element(key, result.getMessage().getPayload()));
        return result;
    }
}
// <end id="PayloadCacheInterceptor"/>
