package com.muleinaction;

import java.util.List;
import javax.jms.ObjectMessage;

import com.muleinaction.common.*;
import org.mule.api.lifecycle.Callable;
import org.mule.api.MuleEventContext;
import org.mule.module.client.MuleClient;

/**
 * Mock BillingService to be used with the Wiretap functional tests.  Accepts a message and sends it to the
 * "test.success" vm queue.
 */
public class BillingService implements Callable {

    public Object onCall(MuleEventContext muleEventContext) throws Exception {

        MuleClient muleClient = new MuleClient(muleEventContext.getMuleContext());
        muleClient.sendAsync("vm://billing.service.received", "message received", null);
        return muleEventContext.getMessage();
    }


}
