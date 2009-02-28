package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.context.notification.EndpointMessageNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.context.notification.EndpointMessageNotification;
import org.apache.commons.io.FileUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.BadSqlGrammarException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class ImapJdbcTransportFunctionalTestCase extends AbstractEmailFunctionalTestCase {

    CountDownLatch latch;
    JdbcTemplate template;

    public ImapJdbcTransportFunctionalTestCase() throws IOException {
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
                if ("imapJdbcService".equals(notification.getResourceIdentifier())) {
                    final EndpointMessageNotification messageNotification = (EndpointMessageNotification) notification;
                    if (messageNotification.getEndpoint().getName().equals("endpoint.jdbc.statsInsert")) {
                        latch.countDown();
                    }
                }
            }
        });
    }

    @Override
    protected String getConfigResources() {
        return "conf/imap-jdbc-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "imapJdbcService");

        assertNotNull(service);
        assertEquals("imapJdbcModel", service.getModel().getName());
    }

    public void testRowInserted() throws Exception {
        assertTrue("Message did not reach directory on time", latch.await(15, TimeUnit.SECONDS));

        template.query("SELECT count(*) from alerts", new RowCallbackHandler() {
            public void processRow(ResultSet resultSet) throws SQLException {
                assertEquals(1, resultSet.getInt(1));
            }
        });

        final Map<String, String> results = new HashMap<String, String>();
        template.query("SELECT * from alerts where host = ?", new Object[]{"SJCSYMPHMRPM01P"},
                new RowCallbackHandler() {
                    public void processRow(ResultSet resultSet) throws SQLException {
                        results.put("message", resultSet.getString("message"));
                        results.put("host", resultSet.getString("host"));
                        results.put("timestamp", resultSet.getString("timestamp"));
                    }
                });
        assertEquals(ALERT_MESSAGE, results.get("message"));
        assertEquals(ALERT_HOST, results.get("host"));
        assertEquals(ALERT_TIME, results.get("timestamp"));
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

    private static String ALERT_MESSAGE = "The IBMWAS61Service - RPM561 service was unable to log on as .\\admin with the currently configured";
    private static String ALERT_HOST = "SJCSYMPHMRPM01P";
    private static String ALERT_TIME = "2008-08-08 16:53:22.0";
}
