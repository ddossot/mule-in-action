package com.muleinaction.muleclient;

import static org.junit.Assert.assertEquals;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

/**
 * @author David Dossot (david@dossot.net)
 */
public class RawJmsMuleClientTest {

    public static final String BROKER_URL = "tcp://localhost:52525";

    private BrokerService amqBroker;

    private String queueName;

    private String expectedPayload;

    @Before
    public void initializeActiveMQ() throws Exception {
        expectedPayload = RandomStringUtils.randomAlphanumeric(10);

        amqBroker = new BrokerService();
        amqBroker.addConnector(BROKER_URL);
        amqBroker.start();

        queueName = "queue." + RandomStringUtils.randomAlphanumeric(10);

        // load a message in the test queue so we can fetch it via the remote
        // client
        final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                BROKER_URL);

        final Connection connection = connectionFactory.createConnection();

        final Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        session.createProducer(session.createQueue(queueName)).send(
                session.createTextMessage(expectedPayload));

        connection.close();
    }

    @After
    public void disposeActiveMQ() throws Exception {
        amqBroker.stop();
    }

    @Test
    public void tapJmsTransport() throws Exception {
        // <start id="MuleClient-RawJms-Transport"/>
        final MuleClient muleClient = new MuleClient(
                "conf/raw-jms-muleclient-config.xml");

        muleClient.getMuleContext().start();

        final MuleMessage response = muleClient.request("jms://" + queueName
                + "?connector=amqConnector", 1000);

        muleClient.getMuleContext().dispose();
        muleClient.dispose();
        // <end id="MuleClient-RawJms-Transport"/>

        final String actualPayload = response.getPayloadAsString();

        assertEquals(expectedPayload, actualPayload);
    }
}
