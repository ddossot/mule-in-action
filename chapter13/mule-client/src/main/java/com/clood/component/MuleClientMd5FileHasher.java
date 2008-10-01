package com.clood.component;

import org.apache.commons.codec.digest.DigestUtils;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.module.client.MuleClient;
import org.mule.transport.file.FileConnector;

/**
 * @author David Dossot (david@dossot.net)
 */
public class MuleClientMd5FileHasher implements Callable {

    private String sourceFolderUri;

    private String fileConnectorName;

    public void setSourceFolder(final String sourceFolder) {
        this.sourceFolderUri = "file://" + sourceFolder + "/";
    }

    public void setFileConnector(final FileConnector fileConnector) {
        this.fileConnectorName = fileConnector.getName();
    }

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        eventContext.setStopFurtherProcessing(true);

        final MuleMessage requestedFileMessage =
                new MuleClient(eventContext.getMuleContext()).request(
                        sourceFolderUri
                                + eventContext.transformMessageToString()
                                + "?connector=" + fileConnectorName, 0);

        return requestedFileMessage != null ? DigestUtils.md5Hex(requestedFileMessage.getPayloadAsBytes())
                : null;
    }
}
