package com.clood.statistic;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.clood.model.Client;

/**
 * @author David Dossot (david@dossot.net)
 */
public class ActivityEmailContext {
    private final long id;
    private Client client;
    private ActivityReport activityReport;

    public ActivityEmailContext(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(final Client client) {
        this.client = client;
    }

    public ActivityReport getActivityReport() {
        return activityReport;
    }

    public void setActivityReport(final ActivityReport activityReport) {
        this.activityReport = activityReport;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
