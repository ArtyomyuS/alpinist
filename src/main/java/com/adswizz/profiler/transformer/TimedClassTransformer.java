package com.adswizz.profiler.transformer;

import com.adswizz.profiler.Measured;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class TimedClassTransformer implements ClassFileTransformer {

    private final static Logger logger = LoggerFactory.getLogger(TimedClassTransformer.class);
    private ClassPool classPool;

    public TimedClassTransformer() {
        classPool = new ClassPool();
        classPool.appendSystemPath();
        try {
            classPool.appendPathList(System.getProperty("java.class.path"));

            // make sure that MetricReporter is loaded
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] transform(ClassLoader loader, String fullyQualifiedClassName, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classBytes) throws IllegalClassFormatException {
        final String className = fullyQualifiedClassName.replace("/", ".");

        try {
            classPool.appendClassPath(new ByteArrayClassPath(className, classBytes));
            CtClass ctClass = classPool.get(className);

            if (!ctClass.getPackageName().contains("com.adswizz")) return null;

            if (ctClass.isFrozen()) {
                logger.debug("Skip class {}: is frozen", className);
                return null;
            }

            if (ctClass.isPrimitive() || ctClass.isArray() || ctClass.isAnnotation()
                    || ctClass.isEnum() || ctClass.isInterface()) {
                logger.debug("Skip class {}: not a class", className);
                return null;
            }

            boolean isClassModified = false;
            for (CtMethod method : ctClass.getDeclaredMethods()) {
                // if method is annotated, add the code to measure the time
                if (method.getMethodInfo().getCodeAttribute() == null || method.getName().equals("main")) {
                    logger.warn("Skip method " + method.getLongName());
                    continue;
                }

                if (Modifier.isNative(method.getModifiers()) || Modifier.isAbstract(method.getModifiers())) continue;

                logger.debug("Instrumenting method " + method.getLongName());
                method.addLocalVariable("__metricStartTime", CtClass.longType);
                method.insertBefore("__metricStartTime = System.currentTimeMillis();");
                String metricName = ctClass.getName() + "." + method.getName();
                method.insertAfter("com.adswizz.profiler.MetricReporter.reportTime(\"" + metricName + "\", System.currentTimeMillis() - __metricStartTime);");
                isClassModified = true;
            }
            if (!isClassModified) {
                return null;
            }
            return ctClass.toBytecode();
        } catch (Throwable e) {
            logger.debug("Skip class {}: ", className, e.getMessage());
            throw new RuntimeException(e);
//            return null;
        }
    }
}