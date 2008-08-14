package com.muleinaction.component;

import org.apache.commons.codec.digest.DigestUtils;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.EndpointBuilder;
import org.mule.api.lifecycle.Callable;
import org.mule.transport.file.FileConnector;

/**
 * @author David Dossot (david@dossot.net)
 */
// <start id="LoCoShow-MD5H-java"/>
public class Md5FileHasher implements Callable {

    private String sourceFolderUri;

    private FileConnector fileConnector;

    public void setSourceFolder(final String sourceFolder) {
        this.sourceFolderUri = "file://" + sourceFolder + "/";
    }

    public void setFileConnector(final FileConnector fileConnector) {
        this.fileConnector = fileConnector;
    }

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        eventContext.setStopFurtherProcessing(true);

        final EndpointBuilder endpointBuilder =
                eventContext.getMuleContext().getRegistry().lookupEndpointFactory().getEndpointBuilder(
                        sourceFolderUri
                                + eventContext.transformMessageToString());

        endpointBuilder.setConnector(fileConnector);

        final MuleMessage requestedFileMessage =
                endpointBuilder.buildInboundEndpoint().request(0);

        return requestedFileMessage != null ? DigestUtils.md5Hex(requestedFileMessage.getPayloadAsBytes())
                : null;
    }
}
// <end id="LoCoShow-MD5H-java"/>
