package com.clood.component;

import org.mule.api.MuleMessage;
import org.mule.component.DefaultJavaComponent;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.mule.tck.functional.FunctionalTestComponent;

import com.clood.model.Client;

/**
 * @author David Dossot (david@dossot.net)
 */
public class ClientValidatorServiceFunctionalTestCase extends
        FunctionalTestCase {

    private MuleClient muleClient;

    private FunctionalTestComponent activityReportProcessorComponent;

    private FunctionalTestComponent errorProcessorComponent;

    @Override
    protected String getConfigResources() {
        return "conf/client-validator-service-config.xml";
    }

    @Override
    protected void doSetUp() throws Exception {
        super.doSetUp();

        muleClient = new MuleClient(muleContext);

        final DefaultJavaComponent activityReportProcessorJavaComponent = (DefaultJavaComponent) muleContext
                .getRegistry().lookupService("ActivityReportProcessor")
                .getComponent();

        activityReportProcessorComponent = (FunctionalTestComponent) activityReportProcessorJavaComponent
                .getObjectFactory().getInstance();

        final DefaultJavaComponent errorProcessorJavaComponent = (DefaultJavaComponent) muleContext
                .getRegistry().lookupService("ErrorProcessor").getComponent();

        errorProcessorComponent = (FunctionalTestComponent) errorProcessorJavaComponent
                .getObjectFactory().getInstance();
    }

    @Override
    protected void doTearDown() throws Exception {
        muleClient.dispose();

        super.doTearDown();
    }

    public void testValidClient() throws Exception {

        final MuleMessage result = muleClient.send(
                "vm://ActivityReportService.In", new Client(123, "Mr.", "Doe"),
                null);

        assertEquals("OK", result.getPayloadAsString());

        waitUntilMessageHitsComponent(activityReportProcessorComponent);

        assertEquals(1, activityReportProcessorComponent.getReceivedMessagesCount());
        assertEquals(0, errorProcessorComponent.getReceivedMessagesCount());
    }

    public void testInvalidClient() throws Exception {

        final MuleMessage result = muleClient.send(
                "vm://ActivityReportService.In",
                new Client(-123, "Mr.", "Doe"), null);

        assertEquals("ERROR: Client ID must be a positive long", result
                .getPayloadAsString());

        waitUntilMessageHitsComponent(errorProcessorComponent);

        assertEquals(0, activityReportProcessorComponent.getReceivedMessagesCount());
        assertEquals(1, errorProcessorComponent.getReceivedMessagesCount());
    }

    public void testInvalidObject() throws Exception {

        final MuleMessage result = muleClient.send(
                "vm://ActivityReportService.In", new Object(), null);

        assertEquals(
                "ERROR: Payload is not a: com.clood.model.Client but a: java.lang.Object",
                result.getPayloadAsString());

        waitUntilMessageHitsComponent(errorProcessorComponent);

        assertEquals(0, activityReportProcessorComponent.getReceivedMessagesCount());
        assertEquals(1, errorProcessorComponent.getReceivedMessagesCount());
    }

    private void waitUntilMessageHitsComponent(
            final FunctionalTestComponent expectedTestComponent)
            throws InterruptedException {
        int i = 0;
        while ((expectedTestComponent.getReceivedMessagesCount() == 0)
                && (++i < 100)) {
            // since dispatch is asynchronous, we must wait until we get the
            // message
            Thread.sleep(50);
        }
    }
}
