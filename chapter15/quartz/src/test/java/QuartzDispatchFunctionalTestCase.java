import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.mule.api.MuleMessage;

public class QuartzDispatchFunctionalTestCase extends FunctionalTestCase {

    protected String getConfigResources() {
        return "conf/quartz-dispatch-config.xml";
    }

    public void testMessageReceived() throws Exception{
        MuleClient client = new MuleClient(muleContext);
        client.sendAsync("jms://inbound","test",null);
        MuleMessage message = client.request("jms://outbound",15000);
        assertNotNull(message);
    }

}
