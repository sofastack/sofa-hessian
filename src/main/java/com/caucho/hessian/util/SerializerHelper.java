/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.util;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

/**
 *
 * @author junyuan
 * @version SerializerHelper.java, v 0.1 2023年03月22日 15:21 junyuan Exp $
 */
public class SerializerHelper {

    public SerializerHelper(Class<?> clazz) {
        this._type = clazz;
    }

    private final Class<?> _type;

    private Object         _writeReplace;

    private Object         _readResolve;

    public void fetchWriteReplace() {
        this._writeReplace = ReflectionUtil.getWriteReplace(_type);
    }

    public boolean hasWriteReplace() {
        return this._writeReplace != null;
    }

    public void fetchReadResolve() {
        this._readResolve = ReflectionUtil.getWriteReplace(_type);
    }

    public boolean hasReadResolve() {
        return this._readResolve != null;
    }

    public Object writeReplace(Object obj, Object... args) throws Throwable {
        if (this._writeReplace instanceof Method) {
            return ((Method) this._writeReplace).invoke(obj, args);
        } else if (this._writeReplace instanceof MethodHandle) {
            return ((MethodHandle) this._writeReplace).bindTo(obj).invokeExact(args);
        }
        return null;
    }

    public Object readResolve(Object obj, Object... args) throws Throwable {
        if (this._readResolve instanceof Method) {
            return ((Method) this._readResolve).invoke(obj, args);
        } else if (this._readResolve instanceof MethodHandle) {
            return ((MethodHandle) this._readResolve).bindTo(obj).invokeExact(args);
        }
        return null;
    }

}