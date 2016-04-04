package com.adswizz.profiler;

/**
 * @author artiom.darie.
 */
public class JVMUtils {

    /**
     * Return main class name
     *
     * @return
     */
    public static String getClassName() {
        final String command = System.getProperty("sun.java.command");
        final String[] classes = command.split(" ");
        final String classNameWithArguments = classes[classes.length - 1];
        return classNameWithArguments.split(",")[0];
    }

    /**
     * Return main package name.
     *
     * @return
     */
    public static String getPackageName() {
        return getClassName().substring(0, getClassName().lastIndexOf("."));
    }

}
