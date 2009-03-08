import org.mule.tck.FunctionalTestCase;
import org.mule.api.context.notification.ComponentMessageNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.apache.commons.io.FileUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.io.File;

public class QuartzFileFunctionalTestCase extends FunctionalTestCase {

    private CountDownLatch latch = new CountDownLatch(1);

    protected String getConfigResources() {
        return "conf/quartz-file-config.xml";
    }

    protected void doSetUp() throws Exception {
        super.doSetUp();
        new File("data/in").mkdir();
        File file = new File("data/in/test.csv");
        FileUtils.writeStringToFile(file, "1,2,3");
        muleContext.registerListener(new ComponentMessageNotificationListener() {
            public void onNotification(final ServerNotification notification) {
                if ("quartzPollingFileService".equals(notification.getResourceIdentifier())) {
                    latch.countDown();
                }
            }
        });
    }

    public void testMessageDispatched() throws Exception {
        assertTrue("Message did not reach component on time", latch.await(15, TimeUnit.SECONDS));
    }

}
