package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.context.notification.EndpointMessageNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.context.notification.EndpointMessageNotification;
import org.mule.module.client.MuleClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class JmsRemoteSyncFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/jms-remote-sync-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "jmsRemoteSyncService");
        assertNotNull(service);
        assertEquals("jmsRemoteSyncModel", service.getModel().getName());
    }

    public void testMessageSent() throws Exception {

        MuleClient client = new MuleClient(muleContext);
        MuleMessage response = client.send("http://localhost:9765/orders?id=1",null,null);
        assertNotNull(response);
        assertEquals("SUBMITTED",response.getPayloadAsString()); 
    }

}