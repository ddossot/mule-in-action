package com.clood;

import org.mule.tck.AbstractMuleTestCase;
import org.mule.RequestContext;

public class MessageEnricherTest extends AbstractMuleTestCase {

    protected void doSetUp() throws Exception {
        RequestContext.setEvent(getTestEvent("TEST_PAYLOAD"));
    }

    protected void doTearDown() throws Exception {
         RequestContext.setEvent(null);
    }

    public void testOnCall() {
        MessageEnricher messageEnricher = new MessageEnricher();
        messageEnricher.onCall(RequestContext.getEventContext());
        assertNotNull(RequestContext.getEvent().getProperty("ORGANIZATION",true));
        assertEquals("CLOOD", RequestContext.getEvent().getProperty("ORGANIZATION",true));
    }
}