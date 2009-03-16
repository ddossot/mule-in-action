package com.muleinaction.transformer;

import org.mule.api.transformer.Transformer;
import org.springframework.ui.velocity.VelocityEngineFactory;

/**
 * @author David Dossot (david@dossot.net)
 */
public class MarkingVelocityMessageTransformerTest extends
        VelocityMessageTransformerTest {

    @Override
    public Transformer getTransformer() throws Exception {
        final VelocityEngineFactory velocityEngineFactory = new VelocityEngineFactory();
        velocityEngineFactory.setResourceLoaderPath("classpath:templates");

        final MarkingVelocityMessageTransformer velocityTransformer = new MarkingVelocityMessageTransformer();

        velocityTransformer.setVelocityEngine(velocityEngineFactory
                .createVelocityEngine());

        velocityTransformer.setTemplateName("test-message.vm");
        velocityTransformer.setTimeStampPropertyName("transformationTime");
        velocityTransformer.initialise();
        return velocityTransformer;
    }

}
