package com.clood.component;

import org.apache.commons.codec.digest.DigestUtils;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.transport.file.FileConnector;

/**
 * @author David Dossot (david@dossot.net)
 */
public class Md5FileHasher implements Callable {

    private String sourceFolder;

    private String fileConnectorName;

    public void setSourceFolder(final String sourceFolder) {
        this.sourceFolder = sourceFolder.replace('\\', '/');
    }

    public void setFileConnector(final FileConnector fileConnector) {
        this.fileConnectorName = fileConnector.getName();
    }

    // <start id="EventContext-Md5FileHasher"/>
    public Object onCall(final MuleEventContext eventContext) throws Exception {

        final String fileName = eventContext.getMessageAsString();

        final MuleMessage requestedFileMessage = eventContext.requestEvent(
                "file://" + sourceFolder + "/" + fileName + "?connector="
                        + fileConnectorName, 0);

        eventContext.setStopFurtherProcessing(true);

        return requestedFileMessage != null ? DigestUtils
                .md5Hex(requestedFileMessage.getPayloadAsBytes()) : null;
    }
    // <end id="EventContext-Md5FileHasher"/>
}
