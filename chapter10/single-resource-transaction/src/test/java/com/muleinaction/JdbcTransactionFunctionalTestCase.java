package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.BadSqlGrammarException;
import org.enhydra.jdbc.standard.StandardDataSource;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class JdbcTransactionFunctionalTestCase extends FunctionalTestCase {

    protected void suitePreSetUp() throws Exception {
        super.suitePreSetUp();
        StandardDataSource dataSource = new org.enhydra.jdbc.standard.StandardDataSource();
        dataSource.setDriverName("org.apache.derby.jdbc.EmbeddedDriver");
        dataSource.setUrl("jdbc:derby:muleEmbeddedDB;create=true");
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(dataSource);

        try {
            template.update("DROP TABLE PERF_METRICS");
        } catch (BadSqlGrammarException ex) {
            logger.info(ex);
        }

        template.update("CREATE TABLE PERF_METRICS " +
                "(id BIGINT NOT NULL, client_id BIGINT NOT NULL, check_name VARCHAR(255), check_result DECIMAL, ts TIMESTAMP)");
    }

    @Override
    protected String getConfigResources() {
        return "conf/jdbc-transaction-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "URLAlertingService");

        assertNotNull(service);
        assertEquals("URLAlertingModel", service.getModel().getName());
    }

    public void testMessageReceived() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        assertNotNull(muleClient.send("jms://topic:monitoring.performance", "STATUS: OK", null));
    }


}