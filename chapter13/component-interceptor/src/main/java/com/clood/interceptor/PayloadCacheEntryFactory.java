package com.clood.interceptor;

import net.sf.ehcache.constructs.blocking.CacheEntryFactory;

import org.mule.api.MuleMessage;
import org.mule.api.interceptor.Invocation;

/**
 * @author David Dossot (david@dossot.net)
 */
class PayloadCacheEntryFactory implements CacheEntryFactory {

    public Object createEntry(final Object object) throws Exception {

        final Invocation invocation = (Invocation) object;
        final MuleMessage invocationResult = invocation.invoke();
        return invocationResult;
    }

}
