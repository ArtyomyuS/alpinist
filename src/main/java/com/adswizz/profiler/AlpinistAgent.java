package com.adswizz.profiler;

import com.adswizz.profiler.transformer.TimedClassTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * The analytics agent.
 *
 * @author artiom.darie.
 */
public class AlpinistAgent {

    public static final Logger logger = LoggerFactory.getLogger(AlpinistAgent.class);

    public static void premain(String agentArguments, Instrumentation instrumentation) {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        logger.info("Runtime: {}: {}", runtimeMxBean.getName(), runtimeMxBean.getInputArguments());
        logger.info("Starting agent with arguments " + agentArguments);

        MetricReporter.startJmxReporter();

        // define the class transformer to use
        instrumentation.addTransformer(new TimedClassTransformer());
    }
}
