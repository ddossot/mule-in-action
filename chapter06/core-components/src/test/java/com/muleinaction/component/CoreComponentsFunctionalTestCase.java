package com.muleinaction.component;

import java.util.Random;

import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.mule.transport.NullPayload;

import com.clood.model.Client;
import com.clood.statistic.ActivityEmailContext;
import com.clood.statistic.ActivityReport;
import com.muleinaction.test.StringTargetComponent;

/**
 * @author David Dossot (david@dossot.net)
 */
public class CoreComponentsFunctionalTestCase extends FunctionalTestCase {

    private StringTargetComponent target;

    private MuleClient muleClient;

    @Override
    protected String getConfigResources() {
        return "conf/core-components-config.xml";
    }

    @Override
    protected void doSetUp() throws Exception {
        setDisposeManagerPerSuite(true);

        super.doSetUp();

        muleClient = new MuleClient(muleContext);

        target = (StringTargetComponent) muleContext.getRegistry()
                .lookupObject("StringTargetComponent");

        target.reset();
    }

    public void testExplicitBridge() throws Exception {
        doTestComponent("vm://ExplicitBridge.In", "Hello world!", true, false);
    }

    public void testImplicitBridge() throws Exception {
        doTestComponent("vm://ImplicitBridge.In", "Hello world!", true, false);
    }

    public void testEcho() throws Exception {
        doTestComponent("vm://Echo.In", "Hello world!", true, false);
    }

    public void testLog() throws Exception {
        doTestComponent("vm://Log.In", "Hello world!",
                true, false);
    }

    public void testNull() throws Exception {
        doTestComponent("vm://Null.In", NullPayload.getInstance().toString(),
                false, true);
    }

    public void testBuilder() throws Exception {
        final long clientId = new Random().nextLong();

        final ActivityEmailContext aec = new ActivityEmailContext(clientId);

        final MuleMessage response = muleClient.send(
                "vm://EmailContextBuilder.In", aec, null);

        assertNotNull(response);
        assertSame(aec, response.getPayload());
        assertEquals(clientId, aec.getClient().getId());
        assertEquals(ActivityReport.class.getSimpleName(), aec
                .getActivityReport().toString());
    }

    public void testGuidedEntryPointResolution() throws Exception {
        final long clientId = new Random().nextLong();

        final ActivityEmailContext aec = new ActivityEmailContext(clientId);

        final MuleMessage response = muleClient.send(
                "vm://ClientLookupService.In", aec, null);

        assertNotNull(response);
        Client client = (Client) response.getPayload();
        assertEquals(clientId, client.getId());
    }

    private void doTestComponent(final String inboundUri,
            final String expectedResponse, final boolean expectedToReachTarget,
            final boolean willGetException) throws Exception {

        final MuleMessage response = muleClient.send(inboundUri, "Hello", null);

        assertNotNull(response);
        assertEquals(expectedResponse, response.getPayloadAsString());

        if (expectedToReachTarget) {
            assertEquals(expectedResponse, target.getValue());
        } else {
            assertNull(target.getValue());
        }

        assertEquals(willGetException, response.getExceptionPayload() != null);
    }

}
