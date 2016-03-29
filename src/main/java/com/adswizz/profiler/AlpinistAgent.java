package com.adswizz.profiler;

import com.adswizz.profiler.transformer.SimpleTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
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

        // add metric reporter -- Socket reporter

        // add class transformer to report metrics.

        instrumentation.addTransformer(new SimpleTransformer());
    }

}
