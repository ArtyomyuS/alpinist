package com.adswizz.profiler;

import com.adswizz.profiler.transformer.SimpleTransformer;
import com.adswizz.profiler.transformer.TimedClassTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

/**
 * The analytics agent.
 *
 * @author artiom.darie.
 */
public class AlpinistAgent {

    public static final Logger logger = LoggerFactory.getLogger(AlpinistAgent.class);

    public static void premain(String arguments, Instrumentation instrumentation) {
        agentmain(arguments, instrumentation);
    }

    /**
     * Main agent signature.
     *
     * @param arguments       the arguments.
     * @param instrumentation the instrumentation.
     */
    public static void agentmain(String arguments, Instrumentation instrumentation) {
        final RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        final OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();

        logger.info("===========================================================================");
        logger.info("OS: {}", osMxBean.getName());
        logger.info("Runtime: {}: {}", runtimeMxBean.getName(), runtimeMxBean.getInputArguments());
        logger.info("Starting with arguments " + arguments);
        logger.info("===========================================================================");


        MetricReporter.startJmxReporter();

        // add metric reporter -- Socket reporter

        // add class transformer to report metrics.

        instrumentation.addTransformer(new TimedClassTransformer());
    }

}
