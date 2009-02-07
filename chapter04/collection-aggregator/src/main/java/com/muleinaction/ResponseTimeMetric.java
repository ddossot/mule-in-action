package com.muleinaction;

import java.util.Date;
import java.io.Serializable;

public class ResponseTimeMetric implements Serializable 
{
    String jobId;
    String clientId;
    Double responseTime;
    Date timestamp;

    public ResponseTimeMetric(String jobId, String clientId, Double responseTime, Date timestamp) {
        this.jobId = jobId;
        this.clientId = clientId;
        this.responseTime = responseTime;
        this.timestamp = timestamp;
    }

    public ResponseTimeMetric() {
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Double responseTime) {
        this.responseTime = responseTime;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

