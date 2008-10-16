package com.clood.component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.service.Service;

/**
 * A simple HTML dashboard for monitoring service activities.
 * 
 * @author David Dossot (david@dossot.net)
 */
public class HtmlDashboard implements Callable, Initialisable {

    private Set<Service> observedServices;

    private int refreshPeriod = 60;

    private HtmlDashboardRenderer htmlDashboardRenderer;

    private CssProvider cssProvider;

    public void setRefreshPeriod(final int refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }

    public void setObservedServices(final Set<Service> observedServices) {
        Validate.notEmpty(observedServices,
                "The HTML Dashboard needs at least one service to observe!");

        this.observedServices = observedServices;
    }

    public void initialise() throws InitialisationException {
        try {
            htmlDashboardRenderer =
                    new HtmlDashboardRenderer(observedServices, refreshPeriod);

            cssProvider = new CssProvider();

        } catch (final UnknownHostException uhe) {
            throw new InitialisationException(uhe, this);
        } catch (final IOException ioe) {
            throw new InitialisationException(ioe, this);
        }
    }

    public Object onCall(final MuleEventContext eventContext) throws Exception {
        eventContext.getMessage().clearProperties();

        final String content =
                getContentAndSetResponseContentType(eventContext);

        setResponseContentLength(eventContext, content);

        eventContext.setStopFurtherProcessing(true);

        return content;
    }

    private String getContentAndSetResponseContentType(
            final MuleEventContext eventContext) throws Exception {

        if (StringUtils.contains(
                eventContext.getMessage().getPayloadAsString(), "css")) {

            eventContext.getMessage().setStringProperty("Content-Type",
                    "text/css; charset=" + eventContext.getEncoding());

            return cssProvider.getContent();
        }

        eventContext.getMessage().setStringProperty("Content-Type",
                "text/html; charset=" + eventContext.getEncoding());

        return htmlDashboardRenderer.getContent();
    }

    private void setResponseContentLength(final MuleEventContext eventContext,
            final String content) throws UnsupportedEncodingException {

        eventContext.getMessage().setStringProperty(
                "Content-Length",
                Integer.toString(content.getBytes(eventContext.getEncoding()).length));
    }

}
