package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.MuleMessage;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.BadSqlGrammarException;

import javax.sql.DataSource;
import java.util.Date;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class JdbcTransportInboundFunctionalTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/jdbc-inbound-config.xml";
    }

    protected void doSetUp() throws Exception {
        super.doSetUp();
        createDatabase();
    }

    public void testCorrectlyInitialized() throws Exception {
        final Service service = muleContext.getRegistry().lookupService(
                "jdbcInboundService");
        assertNotNull(service);
        assertEquals("jdbcInboundModel", service.getModel().getName());
    }

    public void testMessageReceived() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        MuleMessage message = client.request("vm://orders",5000);
        assertNotNull(message);
    }

    private void createDatabase() {
        DataSource dataSource = (DataSource) muleContext.getRegistry().lookupObject("dataSource");
        JdbcTemplate template = new JdbcTemplate(dataSource);
        try {
            template.update("DROP TABLE alerts");
        } catch (BadSqlGrammarException ex) {
            logger.error(ex);
        }
        template.update("CREATE TABLE alerts " +
                "(id BIGINT NOT NULL, host VARCHAR(255), message VARCHAR(4096), timestamp TIMESTAMP)");

        Date now = new Date();
        template.update("INSERT INTO alerts VALUES (0,?,?,?)",
                new Object[]{"esb01", "message1", new Date(now.getTime() - 5000)});
    }

}
