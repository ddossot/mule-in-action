package com.muleinaction.transformer;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.AbstractMessageAwareTransformer;

/**
 * A transformer that uses both the message and its payload as a Velocity
 * context and returns the result of the rendering.
 * 
 * @author David Dossot (david@dossot.net)
 */
// <start id="VelocityMessageTransformer"/>
public final class VelocityMessageTransformer extends
        AbstractMessageAwareTransformer {

    private VelocityEngine velocityEngine;

    private String templateName;

    private Template template;

    public VelocityMessageTransformer() {
        registerSourceType(Object.class);
        setReturnClass(String.class);
    }

    public void setVelocityEngine(final VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    @Override
    public void initialise() throws InitialisationException {
        try {
            template = velocityEngine.getTemplate(templateName);
        } catch (final Exception e) {
            throw new InitialisationException(e, this);
        }
    }

    @Override
    public Object transform(final MuleMessage message, final String outputEncoding)
            throws TransformerException {

        try {
            final StringWriter result = new StringWriter();

            final Map<String, Object> context = new HashMap<String, Object>();
            context.put("message", message);
            context.put("payload", message.getPayload());

            template.merge(new VelocityContext(context), result);

            return result.toString();

        } catch (final Exception e) {
            throw new TransformerException(MessageFactory
                    .createStaticMessage("Can not transform message with template: "
                            + template), e);
        }
    }
}
// <end id="VelocityMessageTransformer"/>
