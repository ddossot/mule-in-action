package com.muleinaction.transformer;

import java.util.Collections;
import java.util.Map;

import org.mule.api.transformer.Transformer;
import org.mule.transformer.AbstractTransformerTestCase;
import org.springframework.ui.velocity.VelocityEngineFactory;

/**
 * @author David Dossot (david@dossot.net)
 */
public class VelocityPayloadTransformerTest extends AbstractTransformerTestCase {

    static final String RESULT_DATA = "***[payloadFoo=payloadBar\u00EB]***";

    static final Map<String, String> TEST_DATA =
            Collections.singletonMap("payloadFoo", "payloadBar\u00EB");

    @Override
    public Object getResultData() {
        return RESULT_DATA;
    }

    @Override
    public Transformer getRoundTripTransformer() throws Exception {
        return null;
    }

    @Override
    public Object getTestData() {
        return TEST_DATA;
    }

    @Override
    public Transformer getTransformer() throws Exception {
        final VelocityEngineFactory velocityEngineFactory =
                new VelocityEngineFactory();
        velocityEngineFactory.setResourceLoaderPath("classpath:templates");

        final VelocityPayloadTransformer velocityTransformer =
                new VelocityPayloadTransformer();
        velocityTransformer.setVelocityEngine(velocityEngineFactory.createVelocityEngine());
        velocityTransformer.setTemplateName("test-payload.vm");
        velocityTransformer.initialise();
        return velocityTransformer;
    }

}
