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
public class JmsOutboundFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/jms-outbound-config.xml";
    }


    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "jmsOutboundService");

        assertNotNull(service);
        assertEquals("jmsOutboundModel", service.getModel().getName());
    }

    /* ToDo Fix this test...it fails when run under Maven under some platforms.  Seems to
      happen when using topics or JMS 1.0.2b with the ActiveMQ embedded broker.    
     */
    public void testMessageSent() throws Exception {
        /*
        MuleClient client = new MuleClient(muleContext);
        client.send("http://localhost:9756/backup-reports","test",null);
        MuleMessage message = client.request("jms://topic:backup.reports",15000);
        assertNotNull(message);
        assertEquals("test", message.getPayloadAsString()); */
    }

}
