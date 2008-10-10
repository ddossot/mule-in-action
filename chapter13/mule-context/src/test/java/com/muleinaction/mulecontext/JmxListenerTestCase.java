package com.muleinaction.mulecontext;

import java.util.List;

import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.ObjectName;

import org.mule.context.notification.MuleContextNotification;
import org.mule.module.management.agent.JmxAgent;
import org.mule.module.management.agent.JmxServerNotificationAgent;
import org.mule.tck.FunctionalTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class JmxListenerTestCase extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "conf/jmx-listener-config.xml";
    }

    public void testRendersXmlStatistics() throws Exception {
        final JmxAgent jmxAgent =
                (JmxAgent) muleContext.getRegistry().lookupAgent("jmx-agent");

        final MBeanServer mBeanServer = jmxAgent.getMBeanServer();

        final ObjectName listenerObjectName =
                ObjectName.getInstance("Mule."
                        + muleContext.getConfiguration().getId() + ":"
                        + JmxServerNotificationAgent.LISTENER_JMX_OBJECT_NAME);

        @SuppressWarnings("unchecked")
        final List<Notification> bootNotifications =
                (List<Notification>) mBeanServer.getAttribute(
                        listenerObjectName, "NotificationsList");

        assertNotNull(bootNotifications);
        assertTrue(bootNotifications.size() > 0);
        assertTrue(bootNotifications.get(0).getSource() instanceof MuleContextNotification);
    }
}
