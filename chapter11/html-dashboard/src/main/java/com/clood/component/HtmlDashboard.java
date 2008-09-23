package com.clood.component;

import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.api.service.Service;
import org.mule.management.stats.ServiceStatistics;
import org.mule.service.AbstractService;
import org.springframework.util.FileCopyUtils;

/**
 * A simple HTML dashboard for monitoring service activities.
 * 
 * FIXME refactor: extract HTML and CSS providers
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
        eventContext.getMessage().clearProperties();

        String content;

        if (StringUtils.contains(
                eventContext.getMessage().getPayloadAsString(), "css")) {

            // TODO cache
            content =
                    FileCopyUtils.copyToString(new InputStreamReader(
                            Thread.currentThread().getContextClassLoader().getResourceAsStream(
                                    "dashboard.css"), Charset.defaultCharset()));

            eventContext.getMessage().setStringProperty("Content-Type",
                    "text/css; charset=" + eventContext.getEncoding());

        } else {
            content = renderHtmlDashboard();

            eventContext.getMessage().setStringProperty("Content-Type",
                    "text/html; charset=" + eventContext.getEncoding());
        }

        eventContext.getMessage().setStringProperty(
                "Content-Length",
                Integer.toString(content.getBytes(eventContext.getEncoding()).length));

        eventContext.setStopFurtherProcessing(true);

        return content;
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
        htmlBuilder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"dashboard/css\" />");
        htmlBuilder.append("<meta http-equiv=\"refresh\" content=\"");
        htmlBuilder.append(refreshPeriod);
        htmlBuilder.append("\" />\n");
        htmlBuilder.append("</head><body>\n");
        htmlBuilder.append("<table>\n<thead>\n<tr><td colspan=\"2\" class=\"faded\">");
        htmlBuilder.append(hostName);
        htmlBuilder.append("</td></tr>\n</thead>\n<tbody>\n");
    }

    private void renderServices(final StringBuilder htmlBuilder) {
        final Map<Service, String> serviceStates = refreshServiceStates();

        for (final Service observedService : observedServices) {
            final String serviceState = serviceStates.get(observedService);

            htmlBuilder.append("<tr><td>");
            htmlBuilder.append(observedService.getName());
            htmlBuilder.append("</td><td class=\"state ");
            htmlBuilder.append(serviceState != null ? serviceState : "dead");
            htmlBuilder.append("\">");
            htmlBuilder.append(getServiceSymbol(observedService));
            htmlBuilder.append("</td></tr>\n");
        }
    }

    private void renderFooter(final StringBuilder htmlBuilder) {
        htmlBuilder.append("<tr><td colspan=\"2\" class=\"faded\">");
        htmlBuilder.append(new Date().toString());
        htmlBuilder.append("</td></tr>\n</tbody>\n</table>\n</body></html>");
    }

    private String getServiceSymbol(final Service service) {
        if (service == null) {
            return "?";
        } else if (!service.isStarted()) {
            return "X";
        } else if (service.isPaused()) {
            return "=";
        } else {
            return StringUtils.EMPTY;
        }
    }

    private Map<Service, String> refreshServiceStates() {
        final Map<Service, String> serviceStates =
                new HashMap<Service, String>();

        for (final AbstractService observedService : observedServices) {

            final ServiceStatistics serviceStatistics =
                    observedService.getStatistics();

            serviceStates.put(observedService, getStateForStatistics(
                    previousStatistics.get(observedService), serviceStatistics));

            previousStatistics.put(observedService,
                    new PreviouServiceStatistics(serviceStatistics));
        }

        return serviceStates;
    }

    private String getStateForStatistics(
            final PreviouServiceStatistics previousStatistics,
            final ServiceStatistics statistics) {

        if (previousStatistics != null) {

            if ((statistics.getExecutionErrors() > previousStatistics.getExecutionErrors())
                    || (statistics.getFatalErrors() > previousStatistics.getFatalErrors())) {
                return "error";
            }

            if (statistics.getAverageQueueSize() > previousStatistics.getAverageQueueSize()) {
                return "increased";
            }

            if (statistics.getExecutedEvents() > previousStatistics.getExecutedEvents()) {
                return "active";
            }

            return "idle";
        } else {
            return "reset";
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
