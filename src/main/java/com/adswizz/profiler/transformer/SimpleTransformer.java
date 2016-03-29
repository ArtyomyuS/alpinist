package com.adswizz.profiler.transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author artiom.darie.
 */
public class SimpleTransformer implements ClassFileTransformer {

    public static final Logger logger = LoggerFactory.getLogger(SimpleTransformer.class);

    @Override
    public byte[] transform(ClassLoader loader, String fullyQualifiedClassName, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        String className = fullyQualifiedClassName.replace("/", ".");


        logger.debug(className);

        return classfileBuffer;
    }
}
