import org.mule.tck.FunctionalTestCase;
import org.mule.api.context.notification.ComponentMessageNotificationListener;
import org.mule.api.context.notification.ServerNotification;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class QuartzCronFunctionalTestCase extends FunctionalTestCase {

    private CountDownLatch latch = new CountDownLatch(1);

    protected String getConfigResources() {
        return "conf/quartz-cron-config.xml";
    }

    protected void doSetUp() throws Exception {
        super.doSetUp();
        muleContext.registerListener(new ComponentMessageNotificationListener() {
            public void onNotification(final ServerNotification notification) {
                if ("warehouseService".equals(notification.getResourceIdentifier())) {
                    latch.countDown();
                }
            }
        });
    }

    public void testMessageDispatched() throws Exception {
        assertTrue("Message did not reach component on time", latch.await(15, TimeUnit.SECONDS));
    }

}
