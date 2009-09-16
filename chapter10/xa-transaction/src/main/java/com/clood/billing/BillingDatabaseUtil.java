package com.clood.billing;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.BadSqlGrammarException;
import org.enhydra.jdbc.standard.StandardDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;


/**
 * Static methods for manipulating the Derby operational and warehouse databases
 */
public class BillingDatabaseUtil {

    static Log logger = LogFactory.getLog(BillingDatabaseUtil.class);

    public static void createOperationalDatabase() throws SQLException {
        JdbcTemplate template = getTemplate("operationalDB", true);
        try {
            template.update("DROP TABLE BILLING_STATS");
        } catch (BadSqlGrammarException ex) {
            logger.info(ex);
        }
        template.update("CREATE TABLE BILLING_STATS (id BIGINT NOT NULL, stat VARCHAR(255))");
    }

    public static void createWarehouseDatabase() throws SQLException {
        JdbcTemplate template = getTemplate("warehouseDB", true);
        try {
            template.update("DROP TABLE BILLING_STATS");
        } catch (BadSqlGrammarException ex) {
            logger.info(ex);
        }
        template.update("CREATE TABLE BILLING_STATS (id BIGINT NOT NULL, stat VARCHAR(255))");
    }

    public static void createBrokenWarehouseDatabase() throws SQLException {
        JdbcTemplate template = getTemplate("warehouseDB", true);
        try {
            template.update("DROP TABLE BILLING_STATS");
        } catch (BadSqlGrammarException ex) {
            logger.info(ex);
        }
        template.update("CREATE TABLE BILLING_STATS (id BIGINT NOT NULL)");
    }

    public static JdbcTemplate getTemplate(String database, boolean create) throws SQLException {
        StandardDataSource dataSource = new org.enhydra.jdbc.standard.StandardDataSource();
        dataSource.setDriverName("org.apache.derby.jdbc.EmbeddedDriver");
        dataSource.setUrl(String.format("jdbc:derby:%s;create=%b", database, create));
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(dataSource);
        return template;
    }


}
