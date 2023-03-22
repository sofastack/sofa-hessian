/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.util;

import sun.reflect.ReflectionFactory;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

/**
 *
 * @author junyuan
 * @version ReflectionUtil.java, v 0.1 2023年03月21日 20:45 junyuan Exp $
 */
public class ReflectionUtil {

    private static final Helper privileged;
    static {
        String version = System.getProperty("java.version");
        if (version != null && version.startsWith("17")) {
            privileged = new Java17();
        } else {
            privileged = new Default();
        }
    }

    public static Object getWriteReplace(Class<?> clazz) {
        return privileged.getWriteReplace(clazz);
    }

    public static Object getReadResolve(Class<?> clazz) {
        return privileged.getReadResolve(clazz);
    }

    private static abstract class Helper {
        abstract Object getWriteReplace(Class<?> clazz);

        abstract Object getReadResolve(Class<?> clazz);
    }

    private static class Java17 extends Helper {
        private static final ReflectionFactory INSTANCE = ReflectionFactory.getReflectionFactory();

        @Override
        MethodHandle getWriteReplace(Class<?> clazz) {
            // will get from super class if NoSuchMethod
            return INSTANCE.writeReplaceForSerialization(clazz);
        }

        @Override
        MethodHandle getReadResolve(Class<?> clazz) {
            return INSTANCE.readResolveForSerialization(clazz);
        }
    }

    private static class Default extends Helper {
        @Override
        Method getWriteReplace(Class<?> clazz) {
            Method m = recursivelyGetMethod(clazz, "writeReplace");
            if (m != null) {
                m.setAccessible(true);
            }
            return m;
        }

        @Override
        Method getReadResolve(Class<?> clazz) {
            Method m = recursivelyGetMethod(clazz, "readResolve");
            if (m != null) {
                m.setAccessible(true);
            }
            return m;
        }

        private Method recursivelyGetMethod(Class<?> clazz, String methodName) {
            for (; clazz != null; clazz = clazz.getSuperclass()) {
                Method[] methods = clazz.getDeclaredMethods();

                for (int i = 0; i < methods.length; i++) {
                    Method method = methods[i];

                    if (method.getName().equals(methodName) &&
                        method.getParameterTypes().length == 0)
                        return method;
                }
            }
            return null;
        }
    }
}