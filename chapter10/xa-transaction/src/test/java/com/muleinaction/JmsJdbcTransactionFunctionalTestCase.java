package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.FutureMessageResult;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.BadSqlGrammarException;
import org.enhydra.jdbc.standard.StandardDataSource;

import java.sql.SQLException;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class JmsJdbcTransactionFunctionalTestCase extends FunctionalTestCase {

    protected void suitePreSetUp() throws Exception {
        createOperationalDatabase();
        createWarehouseDatabase();
        super.suitePreSetUp();
    }

    @Override
    protected String getConfigResources() {
        return "conf/jms-jdbc-transaction-config.xml";
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "BillingService");

        assertNotNull(service);
        assertEquals("BillingModel", service.getModel().getName());

    }

    public void testMessageConsumedTransactionally() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        FutureMessageResult result = muleClient.sendAsync("vm://billing.stat", "STATUS: OK", null);
        result.getMessage(5000);
        assertEquals(1, getTemplate("operationalDB", false).queryForList("SELECT * FROM BILLING_STATS").size());
        assertEquals(1, getTemplate("warehouseDB", false).queryForList("SELECT * FROM BILLING_STATS").size());
    }

    public void testMessageRolledBack() throws Exception {
        createOperationalDatabase();
        createBrokenWarehouseDatabase();
        MuleClient muleClient = new MuleClient(muleContext);
        FutureMessageResult result = muleClient.sendAsync("vm://billing.stat", "STATUS: OK", null);
        result.getMessage(5000);
        assertEquals(0, getTemplate("operationalDB", false).queryForList("SELECT * FROM BILLING_STATS").size());
        assertEquals(0, getTemplate("warehouseDB", false).queryForList("SELECT * FROM BILLING_STATS").size());
    }

    void createOperationalDatabase() throws SQLException {
        JdbcTemplate template = getTemplate("operationalDB", true);
        try {
            template.update("DROP TABLE BILLING_STATS");
        } catch (BadSqlGrammarException ex) {
            logger.info(ex);
        }
        template.update("CREATE TABLE BILLING_STATS (id BIGINT NOT NULL, stat VARCHAR(255))");
    }

    void createWarehouseDatabase() throws SQLException {
        JdbcTemplate template = getTemplate("warehouseDB", true);
        try {
            template.update("DROP TABLE BILLING_STATS");
        } catch (BadSqlGrammarException ex) {
            logger.info(ex);
        }
        template.update("CREATE TABLE BILLING_STATS (id BIGINT NOT NULL, stat VARCHAR(255))");
    }

    void createBrokenWarehouseDatabase() throws SQLException {
        JdbcTemplate template = getTemplate("warehouseDB", true);
        try {
            template.update("DROP TABLE BILLING_STATS");
        } catch (BadSqlGrammarException ex) {
            logger.info(ex);
        }
        template.update("CREATE TABLE BILLING_STATS (id BIGINT NOT NULL)");
    }

    JdbcTemplate getTemplate(String database, boolean create) throws SQLException {
        StandardDataSource dataSource = new org.enhydra.jdbc.standard.StandardDataSource();
        dataSource.setDriverName("org.apache.derby.jdbc.EmbeddedDriver");
        dataSource.setUrl(String.format("jdbc:derby:%s;create=%b", database, create));
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(dataSource);
        return template;
    }


}
