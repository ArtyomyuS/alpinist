package com.adswizz.profiler.transformer;

import com.adswizz.profiler.JVMUtils;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.function.Predicate;

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

            if (isClassModifiable().test(ctClass)) return null;

            Arrays.asList(ctClass.getDeclaredMethods()).stream()
                    .filter(isMethodModifiable())
                    .forEach(ctMethod -> {
                        logger.debug("Instrumenting method " + ctMethod.getLongName());
                        try {
                            ctMethod.addLocalVariable("__metricStartTime", CtClass.longType);
                            ctMethod.insertBefore("__metricStartTime = System.currentTimeMillis();");
                            String metricName = ctClass.getName() + "." + ctMethod.getName();
                            ctMethod.insertAfter("com.adswizz.profiler.MetricReporter.reportTime(\"" + metricName + "\", System.currentTimeMillis() - __metricStartTime);");
                        } catch (CannotCompileException e) {
                            logger.debug("Skip class {}: ", className, e.getMessage());
                            throw new RuntimeException(e);
                        }
                    });

            return ctClass.toBytecode();
        } catch (Throwable e) {
            logger.debug("Skip class {}: ", className, e.getMessage());
            return null;
        }
    }

    /**
     * @return true if the class is modifiable.
     */
    private Predicate<CtClass> isClassModifiable() {
        return ctClass -> !ctClass.getPackageName().contains(JVMUtils.getPackageName()) &&
                !ctClass.isFrozen() &&
                !ctClass.isPrimitive() &&
                !ctClass.isArray() &&
                !ctClass.isAnnotation() &&
                !ctClass.isEnum() &&
                !ctClass.isInterface();
    }

    /**
     * @return true if the method is modifiable.
     */
    private Predicate<CtMethod> isMethodModifiable() {
        return ctMethod -> ctMethod.getMethodInfo().getCodeAttribute() != null &&
                !ctMethod.getLongName().contains(JVMUtils.getClassName() + ".main") &&
                !Modifier.isNative(ctMethod.getModifiers()) &&
                !Modifier.isAbstract(ctMethod.getModifiers());
    }
}