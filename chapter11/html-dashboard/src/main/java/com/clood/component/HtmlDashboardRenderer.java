package com.clood.component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.mule.api.service.Service;
import org.mule.management.stats.ServiceStatistics;

/**
 * @author David Dossot (david@dossot.net)
 */
class HtmlDashboardRenderer implements ContentProvider {

    private final int refreshPeriod;

    private final Set<Service> observedServices;

    private final ConcurrentMap<Service, PreviousServiceStatistics> previousStatistics;

    private final String hostName;

    public HtmlDashboardRenderer(final Set<Service> observedServices,
            final int refreshPeriod) throws UnknownHostException {
        hostName = InetAddress.getLocalHost().getHostName();

        previousStatistics =
                new ConcurrentHashMap<Service, PreviousServiceStatistics>();

        this.observedServices = observedServices;

        this.refreshPeriod = refreshPeriod;
    }

    public String getContent() {
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

        for (final Service observedService : observedServices) {

            final ServiceStatistics serviceStatistics =
                    observedService.getStatistics();

            serviceStates.put(observedService, getStateForStatistics(
                    previousStatistics.get(observedService), serviceStatistics));

            previousStatistics.put(observedService,
                    new PreviousServiceStatistics(serviceStatistics));
        }

        return serviceStates;
    }

    private String getStateForStatistics(
            final PreviousServiceStatistics previousStatistics,
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

    private static final class PreviousServiceStatistics {
        private final long averageQueueSize;

        private final long executedEvent;

        private final long executionError;

        private final long fatalError;

        public PreviousServiceStatistics(final ServiceStatistics statistics) {
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
