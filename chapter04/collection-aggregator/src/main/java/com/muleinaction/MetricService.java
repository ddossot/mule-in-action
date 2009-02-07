package com.muleinaction;

import com.muleinaction.common.Fare;

import javax.jms.ObjectMessage;
import java.util.List;

public class MetricService {
    public Double averageMetrics(List metrics) throws Exception {

        double total = 0.0;

        for (int i = 0; i < metrics.size(); i++) {
            ObjectMessage message = (ObjectMessage) metrics.get(i);
            ResponseTimeMetric metric = (ResponseTimeMetric) message.getObject();
            total += metric.getResponseTime();

        }

        return total / metrics.size();
    }
}
