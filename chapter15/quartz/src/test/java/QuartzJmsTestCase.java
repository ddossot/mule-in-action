import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.mule.api.MuleMessage;
import org.mule.api.context.notification.ComponentMessageNotificationListener;
import org.mule.api.context.notification.ServerNotification;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class QuartzJmsTestCase extends FunctionalTestCase {

    private CountDownLatch latch = new CountDownLatch(1);

    protected String getConfigResources() {
        return "conf/quartz-jms-config.xml";
    }
     protected void doSetUp() throws Exception {
        super.doSetUp();
        muleContext.registerListener(new ComponentMessageNotificationListener() {
            public void onNotification(final ServerNotification notification) {
                if ("quartzPollingService".equals(notification.getResourceIdentifier())) {
                    latch.countDown();
                }
            }
        });
    }
        
    public void testMessageReceived() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        client.sendAsync("jms://messages","test",null);
        assertTrue("Message did not reach component on time", latch.await(15, TimeUnit.SECONDS));
    }
}
