/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junyuan
 * @version ReflectionUtil.java, v 0.1 2023年03月30日 21:23 junyuan Exp $
 */
public class ReflectionUtil {
    private static final Logger log = Logger.getLogger(ReflectionUtil.class.getName());

    public static boolean setAccessible(Method m) {
        try {
            m.setAccessible(true);
        } catch (Throwable t) {
            log.log(Level.INFO,
                "failed when setting accessible on method [" + m.toString() + "], error message: " + t.getMessage(), t);
            return false;
        }
        return true;
    }

    public static boolean setAccessible(Constructor c) {
        try {
            c.setAccessible(true);
        } catch (Throwable t) {
            log.log(Level.INFO, "failed when setting accessible on constructor [" + c.toString() +
                "], error message: " + t.getMessage(), t);
            return false;
        }
        return true;
    }

    public static boolean setAccessible(Field f) {
        try {
            f.setAccessible(true);
        } catch (Throwable t) {
            log.log(Level.INFO,
                "failed when setting accessible on field [" + f.toString() + "], error message: " + t.getMessage(), t);
            return false;
        }
        return true;
    }

}