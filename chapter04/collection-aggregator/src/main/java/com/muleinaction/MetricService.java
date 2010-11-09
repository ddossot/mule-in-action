package com.muleinaction;

import java.util.List;

public class MetricService {
    public Double averageMetrics(List metrics) throws Exception {

        double total = 0.0;

        for (Object eachMetric : metrics) {
            ResponseTimeMetric metric = (ResponseTimeMetric) eachMetric;
            total += metric.getResponseTime();
        }

        return total / metrics.size();
    }
}
