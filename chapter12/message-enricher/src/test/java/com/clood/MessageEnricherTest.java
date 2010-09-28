
package com.clood;

import org.mule.RequestContext;
import org.mule.tck.AbstractMuleTestCase;

public class MessageEnricherTest extends AbstractMuleTestCase
{

    @Override
    protected void doSetUp() throws Exception
    {
        RequestContext.setEvent(getTestEvent("TEST_PAYLOAD"));
    }

    @Override
    protected void doTearDown() throws Exception
    {
        RequestContext.setEvent(null);
    }

    public void testOnCall()
    {
        final MessageEnricher messageEnricher = new MessageEnricher();
        messageEnricher.onCall(RequestContext.getEventContext());
        final Object organizationProperty = RequestContext.getEvent().getMessage().getOutboundProperty(
            "ORGANIZATION", null);
        assertNotNull(organizationProperty);
        assertEquals("CLOOD", organizationProperty);
    }
}
