package com.clood.component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.Validate;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.api.service.Service;
import org.mule.management.stats.ServiceStatistics;
import org.mule.service.AbstractService;

/**
 * A simple HTML dashboard for monitoring service activities.
 * 
 * Note: this will be refactored to use CSS.
 * 
 * @author David Dossot (david@dossot.net)
 */
public class HtmlDashboard implements Callable {

    static final int DEFAULT_REFRESH_PERIOD = 60;

    private int refreshPeriod = DEFAULT_REFRESH_PERIOD;

    private Set<AbstractService> observedServices;

    private final ConcurrentMap<Service, PreviouServiceStatistics> previousStatistics;

    private final String hostName;

    public HtmlDashboard() {
        previousStatistics =
                new ConcurrentHashMap<Service, PreviouServiceStatistics>();

        hostName = getHostName();
    }

    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException uoe) {
            uoe.printStackTrace();
        }

        return "N/A";
    }

    public void setRefreshPeriod(final int refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }

    public void setObservedServices(final Set<AbstractService> observedServices) {
        Validate.notEmpty(observedServices,
                "The HTML Dashboard needs at least one service to observe!");

        this.observedServices = observedServices;
    }

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        final String html = renderHtmlDashboard();

        eventContext.getMessage().clearProperties();

        eventContext.getMessage().setStringProperty("Content-Type",
                "text/html; charset=" + eventContext.getEncoding());

        eventContext.getMessage().setStringProperty(
                "Content-Length",
                Integer.toString(html.getBytes(eventContext.getEncoding()).length));

        eventContext.setStopFurtherProcessing(true);

        return html;
    }

    private String renderHtmlDashboard() {
        final StringBuilder htmlBuilder = new StringBuilder();

        renderHeader(htmlBuilder);
        renderServices(htmlBuilder);
        renderFooter(htmlBuilder);

        return htmlBuilder.toString();
    }

    private void renderHeader(final StringBuilder htmlBuilder) {
        htmlBuilder.append("<html><head>\n");
        htmlBuilder.append("<meta http-equiv=\"refresh\" content=\"");
        htmlBuilder.append(refreshPeriod);
        htmlBuilder.append("\" />\n");
        htmlBuilder.append("</head><body><font size=\"2\">\n");
        htmlBuilder.append("<h3>");
        htmlBuilder.append(hostName);
        htmlBuilder.append("</h3><table border=\"1\" cellpadding=\"1\" cellspacing=\"0\">\n");
    }

    private void renderServices(final StringBuilder htmlBuilder) {
        final Map<Service, String> serviceColors = refreshServiceColors();

        for (final Service observedService : observedServices) {
            final String serviceColor = serviceColors.get(observedService);

            htmlBuilder.append("<tr><td><font size=\"2\">");
            htmlBuilder.append(observedService.getName());
            htmlBuilder.append("&nbsp;</font></td><td bgcolor=\"");
            htmlBuilder.append(serviceColor != null ? serviceColor : "black");
            htmlBuilder.append("\">");
            htmlBuilder.append(getServiceSymbol(observedService));
            htmlBuilder.append("</td></tr>\n");
        }
    }

    private void renderFooter(final StringBuilder htmlBuilder) {
        htmlBuilder.append("</table><br/>\n");
        htmlBuilder.append(new Date().toString());
        htmlBuilder.append("\n");
        htmlBuilder.append("</font></body></html>\n");
    }

    private String getServiceSymbol(final Service service) {
        if (service == null) {
            return "&nbsp;?&nbsp;&nbsp;";
        } else if (!service.isStarted()) {
            return "&nbsp;X&nbsp;";
        } else if (service.isPaused()) {
            return "&nbsp;=&nbsp;";
        } else {
            return "&nbsp;&nbsp;&nbsp;&nbsp;";
        }
    }

    private Map<Service, String> refreshServiceColors() {
        final Map<Service, String> serviceColors =
                new HashMap<Service, String>();

        for (final AbstractService observedService : observedServices) {

            final ServiceStatistics serviceStatistics =
                    observedService.getStatistics();

            serviceColors.put(observedService, getColorForStatistics(
                    previousStatistics.get(observedService), serviceStatistics));

            previousStatistics.put(observedService,
                    new PreviouServiceStatistics(serviceStatistics));
        }

        return serviceColors;
    }

    private String getColorForStatistics(
            final PreviouServiceStatistics previousStatistics,
            final ServiceStatistics statistics) {

        if (previousStatistics != null) {

            if ((statistics.getExecutionErrors() > previousStatistics.getExecutionErrors())
                    || (statistics.getFatalErrors() > previousStatistics.getFatalErrors())) {
                return "red";
            }

            if (statistics.getAverageQueueSize() > previousStatistics.getAverageQueueSize()) {
                return "orange";
            }

            if (statistics.getExecutedEvents() > previousStatistics.getExecutedEvents()) {
                return "yellow";
            }

            return "lime";
        } else {
            return "gray";
        }
    }

    private static final class PreviouServiceStatistics {
        private final long averageQueueSize;

        private final long executedEvent;

        private final long executionError;

        private final long fatalError;

        public PreviouServiceStatistics(final ServiceStatistics statistics) {
            averageQueueSize = statistics.getAverageQueueSize();
            executedEvent = statistics.getExecutedEvents();
            executionError = statistics.getExecutionErrors();
            fatalError = statistics.getFatalErrors();
        }

        public long getAverageQueueSize() {
            return averageQueueSize;
        }

        public long getExecutedEvents() {
            return executedEvent;
        }

        public long getExecutionErrors() {
            return executionError;
        }

        public long getFatalErrors() {
            return fatalError;
        }
    }

}
