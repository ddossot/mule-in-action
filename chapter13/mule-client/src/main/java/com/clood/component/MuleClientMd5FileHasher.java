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

    private String sourceFolder;

    private String fileConnectorName;

    public void setSourceFolder(final String sourceFolder) {
        this.sourceFolder = sourceFolder.replace('\\', '/');
    }

    public void setFileConnector(final FileConnector fileConnector) {
        this.fileConnectorName = fileConnector.getName();
    }

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        eventContext.setStopFurtherProcessing(true);

        final String fileName = eventContext.getMessageAsString();

        // NB. this is not an efficient implementation of the Md5FileHasher,
        // look at the one in mule-event-context

        // <start id="MuleClient-NonVMTransportCall"/>
        final MuleClient muleClient = new MuleClient(eventContext
                .getMuleContext());

        final MuleMessage requestedFileMessage = muleClient.request("file://"
                + sourceFolder + "/" + fileName + "?connector="
                + fileConnectorName, 0);
        // <end id="MuleClient-NonVMTransportCall"/>

        muleClient.dispose();

        return requestedFileMessage != null ? DigestUtils
                .md5Hex(requestedFileMessage.getPayloadAsBytes()) : null;
    }
}
