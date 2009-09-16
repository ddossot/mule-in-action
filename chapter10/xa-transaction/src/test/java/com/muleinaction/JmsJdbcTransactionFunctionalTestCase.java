package com.muleinaction;

import org.mule.api.service.Service;
import org.mule.api.FutureMessageResult;
import org.mule.tck.FunctionalTestCase;
import org.mule.module.client.MuleClient;

import com.clood.billing.BillingDatabaseUtil;

/**
 * @author John D'Emic (john.demic@gmail.com)
 */
public class JmsJdbcTransactionFunctionalTestCase extends FunctionalTestCase {

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
        assertEquals(1,
                BillingDatabaseUtil.getTemplate("operationalDB", false).queryForList("SELECT * FROM BILLING_STATS").size());
        assertEquals(1,
                BillingDatabaseUtil.getTemplate("warehouseDB", false).queryForList("SELECT * FROM BILLING_STATS").size());
    }

    public void testMessageRolledBack() throws Exception {
        BillingDatabaseUtil.createOperationalDatabase();
        BillingDatabaseUtil.createBrokenWarehouseDatabase();
        MuleClient muleClient = new MuleClient(muleContext);
        FutureMessageResult result = muleClient.sendAsync("vm://billing.stat", "STATUS: OK", null);
        result.getMessage(5000);
        assertEquals(0,
                BillingDatabaseUtil.getTemplate("operationalDB", false).queryForList("SELECT * FROM BILLING_STATS").size());
        assertEquals(0,
                BillingDatabaseUtil.getTemplate("warehouseDB", false).queryForList("SELECT * FROM BILLING_STATS").size());
    }


}
