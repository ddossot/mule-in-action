package com.muleinaction.expressions;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.component.DefaultJavaComponent;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.mule.tck.functional.EventCallback;
import org.mule.tck.functional.FunctionalTestComponent;

/**
 * @author David Dossot (david@dossot.net)
 */
public class ExpressionEvaluatorsTestCase extends FunctionalTestCase {

    private CountDownLatch latch;

    private final AtomicReference<MuleMessage> receivedMessage =
            new AtomicReference<MuleMessage>();

    private MuleClient muleClient;

    @Override
    protected String getConfigResources() {
        return "conf/expressions-config.xml";
    }

    @Override
    protected void doSetUp() throws Exception {
        super.doSetUp();

        receivedMessage.set(null);

        latch = new CountDownLatch(1);

        final EventCallback callback = new EventCallback() {
            public void eventReceived(final MuleEventContext context,
                    final Object component) throws Exception {

                receivedMessage.set(context.getMessage());
                latch.countDown();
            }
        };

        final DefaultJavaComponent defaultComponent =
                (DefaultJavaComponent) muleContext.getRegistry().lookupService(
                        "TargetService").getComponent();

        final FunctionalTestComponent testComponent =
                (FunctionalTestComponent) defaultComponent.getObjectFactory().getInstance();

        testComponent.setEventCallback(callback);

        muleClient = new MuleClient(muleContext);
    }

    public void testAckingAsyncDispatcher() throws Exception {

        final String id = UUID.randomUUID().toString();
        final String payload = "fooPayload";

        final MuleMessage message =
                new DefaultMuleMessage(payload, Collections.EMPTY_MAP);

        message.setCorrelationId(id);

        final MuleMessage response =
                muleClient.send("vm://AckingAsyncDispatcher.IN", message);

        assertEquals(id, response.getPayload());
        assertEquals(payload, getMessageReceivedInTarget().getPayload());
    }

    public void testPayloadSizeFiltering() throws Exception {
        // send some noise
        muleClient.dispatch("vm://PayloadSizeFiltering.IN",
                RandomStringUtils.randomAlphanumeric(10), null);
        muleClient.dispatch("vm://PayloadSizeFiltering.IN",
                RandomStringUtils.randomAlphanumeric(100), null);
        muleClient.dispatch("vm://PayloadSizeFiltering.IN",
                RandomStringUtils.randomAlphanumeric(1000), null);

        final String acceptedPayload =
                RandomStringUtils.randomAlphanumeric(1100);

        muleClient.dispatch("vm://PayloadSizeFiltering.IN", acceptedPayload,
                null);

        assertEquals(acceptedPayload, getMessageReceivedInTarget().getPayload());
    }

    public void testExpressionParserComponent() throws Exception {
        final String id = UUID.randomUUID().toString();
        final String payload = "fooPayload";

        final MuleMessage message =
                new DefaultMuleMessage(payload, Collections.EMPTY_MAP);

        message.setCorrelationId(id);

        final MuleMessage response =
                muleClient.send("vm://ExpressionParser.IN", message);

        final String payloadAsString = response.getPayloadAsString();
        assertTrue(payloadAsString.startsWith(id));
        assertTrue(StringUtils.substringAfter(payloadAsString, "@").matches(
                "\\d{4}-\\d{2}-\\d{2}"));
    }

    private MuleMessage getMessageReceivedInTarget()
            throws InterruptedException {

        latch.await(10, TimeUnit.SECONDS);
        return receivedMessage.get();
    }

}
