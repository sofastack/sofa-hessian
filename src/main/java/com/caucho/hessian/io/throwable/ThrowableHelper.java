/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.throwable;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractSerializer;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.throwable.adapter.EnumConstantNotPresentExceptionDeserializer;
import com.caucho.hessian.io.throwable.adapter.EnumConstantNotPresentExceptionSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author junyuan
 * @version ThrowableHelper.java, v 0.1 2023年04月27日 16:56 junyuan Exp $
 */
public class ThrowableHelper {

    private static final boolean isLessThanJdk17 = isLessThanJdk17();
    private static final boolean reflectionWorks = isReflectionWorks();

    private static boolean isLessThanJdk17() {
        String javaVersion = System.getProperty("java.specification.version");
        return Double.parseDouble(javaVersion) < 17;
    }

    /**
     * 判断反射在当前环境下是否可用
     * @return
     */
    private static boolean isReflectionWorks() {
        try {
            Throwable.class.getDeclaredField("cause").setAccessible(true);
        } catch (Throwable t) {
            return false;
        }
        return true;
    }

    public static AbstractDeserializer getDeserializer(Class<?> cl) {
        if (isLessThanJdk17 || reflectionWorks) {
            return new JavaDeserializer(cl);
        }
        if (EnumConstantNotPresentException.class.isAssignableFrom(cl)) {
            return new EnumConstantNotPresentExceptionDeserializer(cl);
        }

        return new ThrowableDeserializer(cl);
    }

    public static AbstractSerializer getSerializer(Class<?> cl) {
        if (isLessThanJdk17 || reflectionWorks) {
            return new ReflectThrowableSerializer(cl);
        }

        if (throwableSerializerMap.containsKey(cl.getName())) {
            return throwableSerializerMap.get(cl.getName());
        }

        return new ThrowableSerializer(cl);
    }

    private static final Map<String, AbstractSerializer> throwableSerializerMap = new HashMap<String, AbstractSerializer>();
    static {
        throwableSerializerMap.put(EnumConstantNotPresentException.class.getName(),
            new EnumConstantNotPresentExceptionSerializer());
    }

}
