/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.util;

import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author junyuan
 * @version ReflectionUtil.java, v 0.1 2023年03月30日 21:23 junyuan Exp $
 */
public class ReflectionUtil {
    private static org.slf4j.Logger LOGGER                     = judgeLogger();

    //do not change this
    public static final String      HESSIAN_SERIALIZE_LOG_NAME = "HessianSerializeLog";
    public static final String      CONFIG_LOG_SPACE_NAME      = "com.alipay.sofa.hessian";

    private static Logger judgeLogger() {

        try {
            ReflectionUtil.class.getClassLoader().loadClass("com.alipay.sofa.common.log.LoggerSpaceManager");
        } catch (Throwable e) {
            //do nothing
            return null;
        }

        return com.alipay.sofa.common.log.LoggerSpaceManager.getLoggerBySpace(HESSIAN_SERIALIZE_LOG_NAME,
            CONFIG_LOG_SPACE_NAME);
    }

    public static boolean setAccessible(Method m) {
        try {
            m.setAccessible(true);
        } catch (Throwable t) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER
                    .debug(
                        "failed when setting accessible on method [" + m.toString() + "], error message: " +
                            t.getMessage(), t);
            }
            return false;
        }
        return true;
    }

    public static boolean setAccessible(Constructor c) {
        try {
            c.setAccessible(true);
        } catch (Throwable t) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER
                    .debug(
                        "failed when setting accessible on method [" + c.toString() + "], error message: " +
                            t.getMessage(), t);
            }
            return false;
        }
        return true;
    }

    public static boolean setAccessible(Field f) {
        try {
            f.setAccessible(true);
        } catch (Throwable t) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER
                    .debug(
                        "failed when setting accessible on method [" + f.toString() + "], error message: " +
                            t.getMessage(), t);
            }
            return false;
        }
        return true;
    }

}