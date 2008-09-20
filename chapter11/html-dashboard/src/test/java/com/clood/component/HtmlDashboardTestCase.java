package com.clood.component;

import java.util.Collections;

import org.mule.service.AbstractService;
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
        dashboard.setObservedServices(Collections.singleton((AbstractService) getTestService()));
    }

    public void testHtmlRendering() throws Exception {
        final String dashboardContent =
                (String) dashboard.onCall(getTestEventContext(null));

        assertNotNull(dashboardContent);

        assertTrue("<html>", dashboardContent.contains("<html>"));
        assertTrue("</html>", dashboardContent.contains("</html>"));

        assertTrue("Refresh period", dashboardContent.contains("content=\""
                + HtmlDashboard.DEFAULT_REFRESH_PERIOD + "\""));

        assertTrue("Component name",
                dashboardContent.contains(getTestService().getName()));
    }

}
