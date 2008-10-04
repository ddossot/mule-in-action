package com.muleinaction.muleclient;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.apache.commons.lang.RandomStringUtils;
import org.mule.module.client.MuleClient;
import org.mule.module.client.RemoteDispatcher;
import org.mule.tck.FunctionalTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class RemoteHttpXmlMuleClientFunctionalTestCase extends
        FunctionalTestCase {

    private String queueName;

    private String payload;

    @Override
    protected String getConfigResources() {
        return "conf/remote-http-xml-muleclient-config.xml";
    }

    @Override
    protected void doSetUp() throws Exception {
        super.doSetUp();

        payload = RandomStringUtils.randomAlphanumeric(10);

        queueName = "queue." + RandomStringUtils.randomAlphanumeric(10);

        // load a message in the test queue so we can fetch it via the remote
        // client
        final ConnectionFactory connectionFactory = (ConnectionFactory) muleContext
                .getRegistry().lookupObject("amqConnectionFactory");

        final Connection connection = connectionFactory.createConnection();
        final Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        session.createProducer(session.createQueue(queueName)).send(
                session.createTextMessage(payload));
        connection.close();
    }

    public void testJmsAsynchronousRequest() throws Exception {
        final MuleClient muleClient = new MuleClient(false);

        final RemoteDispatcher remoteDispatcher = muleClient
                .getRemoteDispatcher("http://localhost:8181");

        final Object response = remoteDispatcher.receiveRemote(
                "jms://" + queueName, 1000).getPayloadAsString();

        assertEquals(payload, response);

        muleClient.dispose();
    }
}
