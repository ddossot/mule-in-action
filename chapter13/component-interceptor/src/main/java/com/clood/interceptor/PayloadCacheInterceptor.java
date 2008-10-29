package com.clood.interceptor;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.interceptor.Interceptor;
import org.mule.api.interceptor.Invocation;

/**
 * @author David Dossot (david@dossot.net)
 */
// <start id="PayloadCacheInterceptor"/>
public final class PayloadCacheInterceptor implements Interceptor {

    private Ehcache cache;

    public void setCache(final Ehcache cache) {
        this.cache = cache;
    }

    public MuleMessage intercept(final Invocation invocation)
            throws MuleException {

        final MuleMessage currentMessage = invocation.getMessage();
        final Object key = currentMessage.getPayload();
        final Element cachedElement = cache.get(key);

        if (cachedElement != null) {
            return new DefaultMuleMessage(cachedElement.getObjectValue(),
                    currentMessage);
        }

        // we don't synchronize so several threads can compete to fill the cache
        // for the same key: this is rare enough to be acceptable
        final MuleMessage result = invocation.invoke();
        cache.put(new Element(key, result.getPayload()));
        return result;
    }
}
// <end id="PayloadCacheInterceptor"/>
