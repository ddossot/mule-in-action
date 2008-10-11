package com.clood.component;

import org.apache.commons.codec.digest.DigestUtils;
import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.Callable;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.module.client.MuleClient;
import org.mule.transport.file.FileConnector;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecyleMd5FileHasher implements Initialisable, Disposable,
        MuleContextAware, Callable {

    private MuleContext context;

    MuleClient muleClient;

    private String sourceFolder;

    private String fileConnectorName;

    public void setSourceFolder(final String sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public void setFileConnector(final FileConnector fileConnector) {
        this.fileConnectorName = fileConnector.getName();
    }

    public void setMuleContext(final MuleContext context) {
        this.context = context;
    }

    public void initialise() throws InitialisationException {
        try {
            muleClient = new MuleClient(context);
        } catch (final MuleException me) {
            throw new InitialisationException(me, this);
        }
    }

    public void dispose() {
        muleClient.dispose();
        muleClient = null;
    }

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        eventContext.setStopFurtherProcessing(true);

        final String fileName = eventContext.transformMessageToString();

        final MuleMessage requestedFileMessage =
                muleClient.request("file://" + sourceFolder + "/" + fileName
                        + "?connector=" + fileConnectorName, 0);

        return requestedFileMessage != null ? DigestUtils.md5Hex(requestedFileMessage.getPayloadAsBytes())
                : null;
    }

}
