package com.clood.billing;

import org.mule.api.context.notification.MuleContextNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.context.notification.MuleContextNotification;

import java.sql.SQLException;

/**
 * <code>NotifiactionListener</code> used to bootstrap the Derby databases when the model has started.
 */
public class ContextStartingListener implements MuleContextNotificationListener {

    public void onNotification(ServerNotification notification) {

        if (notification.getAction() == MuleContextNotification.CONTEXT_STARTING) {
            try {
                BillingDatabaseUtil.createOperationalDatabase();
                BillingDatabaseUtil.createWarehouseDatabase();
            } catch (SQLException e) {
                throw new BillingDatabaseException(e);
            }
        }

    }

}
