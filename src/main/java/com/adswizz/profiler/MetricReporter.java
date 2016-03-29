package com.adswizz.profiler;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author artiom.darie.
 */
public class MetricReporter {

    private final static Logger logger = LoggerFactory.getLogger(MetricReporter.class);

    private static MetricRegistry metricRegistry = new MetricRegistry();

    public static void startJmxReporter() {
        logger.info("Init JMX reporter");

        JmxReporter jmxReporter = JmxReporter
                .forRegistry(metricRegistry)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .convertRatesTo(TimeUnit.MINUTES)
                .build();
        jmxReporter.start();
    }

    // called by instrumented methods
    public static void reportTime(String name, long timeInMs) {
        Timer timer = metricRegistry.timer(name);
        timer.update(timeInMs, TimeUnit.MILLISECONDS);
    }

}
