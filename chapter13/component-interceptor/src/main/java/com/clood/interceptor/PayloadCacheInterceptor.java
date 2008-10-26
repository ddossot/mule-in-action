package com.clood.interceptor;

import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;

import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.interceptor.Interceptor;
import org.mule.api.interceptor.Invocation;

/**
 * @author David Dossot (david@dossot.net)
 */
public class PayloadCacheInterceptor implements Interceptor {

    private SelfPopulatingCache cache;

    public void setCache(final SelfPopulatingCache cache) {
        this.cache = cache;
    }

    public MuleMessage intercept(final Invocation invocation)
            throws MuleException {

        return (MuleMessage) cache.get(invocation).getValue();
    }

}
