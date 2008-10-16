package com.clood.component;

import java.util.Collections;

import org.mule.tck.AbstractMuleTestCase;

/**
 * @author David Dossot (david@dossot.net)
 */
public class HtmlDashboardTestCase extends AbstractMuleTestCase {

    private HtmlDashboard dashboard;

    @Override
    protected void doSetUp() throws Exception {
        super.doSetUp();

        dashboard = new HtmlDashboard();
        dashboard.setObservedServices(Collections.singleton(getTestService()));
        dashboard.setRefreshPeriod(1234);
        dashboard.initialise();
    }

    public void testHtmlRendering() throws Exception {
        final String dashboardContent =
                (String) dashboard.onCall(getTestEventContext(null));

        assertNotNull(dashboardContent);

        assertTrue("<html>", dashboardContent.contains("<html>"));
        assertTrue("</html>", dashboardContent.contains("</html>"));

        assertTrue("Refresh period",
                dashboardContent.contains("content=\"1234\""));

        assertTrue("Component name",
                dashboardContent.contains(getTestService().getName()));
    }

    public void testCssDelivery() throws Exception {
        final String cssContent =
                (String) dashboard.onCall(getTestEventContext("/foo.css"));

        assertNotNull(cssContent);

        assertTrue("body", cssContent.contains("body"));
        assertTrue(".dead", cssContent.contains(".dead"));
    }

}
