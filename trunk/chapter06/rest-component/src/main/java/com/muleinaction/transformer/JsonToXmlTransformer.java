package com.muleinaction.transformer;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

/**
 * @author David Dossot (david@dossot.net)
 */
public class JsonToXmlTransformer extends AbstractTransformer {

    public JsonToXmlTransformer() {
        super();
        registerSourceType(String.class);
    }

    @Override
    protected Object doTransform(final Object src, final String encoding)
            throws TransformerException {

        return new XMLSerializer().write(JSONObject.fromObject(src));
    }

}
