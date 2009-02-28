package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.context.notification.EndpointMessageNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.context.notification.EndpointMessageNotification;
import org.apache.commons.io.FileUtils;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.io.IOException;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class JdbcTransportOutboundFunctionalTestCase extends AbstractEmailFunctionalTestCase {

    private JdbcTemplate template;
    private CountDownLatch latch = new CountDownLatch(1);

    public JdbcTransportOutboundFunctionalTestCase() throws IOException {
        super(65433, STRING_MESSAGE, "imap", "imap" + CONFIG_BASE, DEFAULT_EMAIL,
                DEFAULT_USER, FileUtils.readFileToString(new File("./src/test/resources/alert.email.txt")), DEFAULT_PASSWORD);
    }

    protected void doSetUp() throws Exception {
        super.doSetUp();
        template = (JdbcTemplate) muleContext.getRegistry().lookupObject("jdbcTemplate");
        createDatabase();
        latch = new CountDownLatch(1);
        muleContext.registerListener(new EndpointMessageNotificationListener() {
            public void onNotification(final ServerNotification notification) {
                if ("jdbcOutboundService".equals(notification.getResourceIdentifier())) {
                    final EndpointMessageNotification messageNotification = (EndpointMessageNotification) notification;
                    if (messageNotification.getEndpoint().getName().equals("endpoint.jdbc.alertInsert")) {
                        latch.countDown();
                    }
                }
            }
        });
    }

    @Override
    protected String getConfigResources() {
        return "conf/jdbc-outbound-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "jdbcOutboundService");
        assertNotNull(service);
        assertEquals("jdbcOutboundModel", service.getModel().getName());
    }

    public void testRowInserted() throws Exception {
        assertTrue("Message did not reach directory on time", latch.await(15, TimeUnit.SECONDS));

        template.query("SELECT count(*) from alerts", new RowCallbackHandler() {
            public void processRow(ResultSet resultSet) throws SQLException {
                assertEquals(1, resultSet.getInt(1));
            }
        });

        final Map<String, String> results = new HashMap<String, String>();
        template.query("SELECT * from alerts where host = ?", new Object[]{"esb01"},
                new RowCallbackHandler() {
                    public void processRow(ResultSet resultSet) throws SQLException {
                        results.put("message", resultSet.getString("message"));
                        results.put("host", resultSet.getString("host"));
                        results.put("timestamp", resultSet.getString("timestamp"));
                    }
                });

        assertNotNull(results.get("message"));
    }

    void createDatabase() {
        try {
            template.update("DROP TABLE alerts");
        } catch (BadSqlGrammarException ex) {
            logger.error(ex);
        }
        template.update("CREATE TABLE alerts " +
                "(id BIGINT NOT NULL, host VARCHAR(255), message VARCHAR(4096), timestamp TIMESTAMP)");
    }


}
