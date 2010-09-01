
package com.muleinaction.transformer;

import java.util.Collections;
import java.util.Map;

import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.RequestContext;
import org.mule.api.MuleContext;
import org.mule.api.transformer.MessageTransformer;
import org.mule.api.transformer.Transformer;
import org.mule.tck.MuleTestUtils;
import org.mule.transformer.AbstractTransformerTestCase;
import org.springframework.ui.velocity.VelocityEngineFactory;

/**
 * @author David Dossot (david@dossot.net)
 */
public class VelocityMessageTransformerTest extends AbstractTransformerTestCase
{

    static final Map<String, Object> TEST_PROPERTIES = Collections.singletonMap("propFoo", (Object) "propBar");

    static final String RESULT_DATA = "***[payloadFoo=payloadBar\u00EB+propFoo=propBar]***";

    static final Map<String, String> TEST_DATA = VelocityPayloadTransformerTest.TEST_DATA;

    @Override
    protected MuleContext createMuleContext() throws Exception
    {
        System.setProperty("mule.transform.autoWrap", "false");

        return super.createMuleContext();
    }

    @Override
    protected void doSetUp() throws Exception
    {
        super.doSetUp();

        RequestContext.setEvent(new DefaultMuleEvent(new DefaultMuleMessage(TEST_DATA, TEST_PROPERTIES,
            muleContext), getTestOutboundEndpoint("test"), MuleTestUtils.getTestSession(muleContext)));
    }

    @Override
    public void testTransform() throws Exception
    {
        final MessageTransformer trans = (MessageTransformer) getTransformer();
        final Object result = trans.transform(getTestData(), RequestContext.getEvent());
        assertNotNull("The result of the transform shouldn't be null", result);

        final Object expectedResult = getResultData();
        assertNotNull("The expected result data must not be null", expectedResult);

        assertTrue("Transformation result does not match expected result, got: " + result + " but wanted: "
                   + expectedResult, this.compareResults(expectedResult, result));
    }

    @Override
    public Object getResultData()
    {
        return RESULT_DATA;
    }

    @Override
    public Transformer getRoundTripTransformer() throws Exception
    {
        return null;
    }

    @Override
    public Object getTestData()
    {
        return TEST_DATA;
    }

    @Override
    public Transformer getTransformer() throws Exception
    {
        final VelocityEngineFactory velocityEngineFactory = new VelocityEngineFactory();
        velocityEngineFactory.setResourceLoaderPath("classpath:templates");

        final VelocityMessageTransformer velocityTransformer = new VelocityMessageTransformer();
        velocityTransformer.setMuleContext(muleContext);

        velocityTransformer.setVelocityEngine(velocityEngineFactory.createVelocityEngine());
        velocityTransformer.setTemplateName("test-message.vm");
        velocityTransformer.initialise();
        return velocityTransformer;
    }

}
