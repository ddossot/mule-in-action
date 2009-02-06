package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.mule.transport.NullPayload;
import com.muleinaction.common.FarmStatus;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class AsyncRequestReplyFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/async-request-reply-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "asyncRequestReplyService");

        assertNotNull(service);
        assertEquals("asyncRequestReplyModel", service.getModel().getName());
    }

    public void testMessageConsumed() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);

        MuleMessage response = muleClient.send("vm://farmRequests","count",null);
        assertFalse(response.getPayload() instanceof NullPayload);
    }


}
