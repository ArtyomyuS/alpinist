package com.adswizz.profiler.transformer;

import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author artiom.darie.
 */
public class SimpleTransformer implements ClassFileTransformer {

    public static final Logger logger = LoggerFactory.getLogger(SimpleTransformer.class);
    private final ClassPool classPool;

    public SimpleTransformer() {
        classPool = new ClassPool();
        classPool.appendSystemPath();
        try {
            classPool.appendPathList(System.getProperty("java.class.path"));

            // make sure that MetricReporter is loaded
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String fullyQualifiedClassName, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        final String className = fullyQualifiedClassName.replace("/", ".");

        CtClass ctClass;

        try {
            logger.debug(className);

            ctClass = classPool.get(className);

            for (CtMethod method : ctClass.getDeclaredMethods()) {
                logger.debug(method.getLongName());
            }

            return ctClass.toBytecode();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

}
