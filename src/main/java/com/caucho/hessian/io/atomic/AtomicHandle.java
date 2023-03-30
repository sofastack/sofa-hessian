/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.atomic;

import com.caucho.hessian.io.HessianHandle;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 *
 * @author junyuan
 * @version AtomicHandle.java, v 0.1 2023年03月30日 19:57 junyuan Exp $
 */
public class AtomicHandle implements HessianHandle, Serializable {
    private static final long serialVersionUID = 5253528013752548947L;

    private Class<?>          type;
    private Object            value;

    public AtomicHandle(Class<?> type, Object value) {
        this.type = type;
        this.value = value;
    }

    protected Object readResolve() {
        if (AtomicInteger.class.equals(type)) {
            return new AtomicInteger((Integer) value);
        }
        else if (AtomicLong.class.equals(type)) {
            return new AtomicLong((Long) value);
        }
        else if (AtomicBoolean.class.equals(type)) {
            return new AtomicBoolean((Boolean) value);
        }
        else if (AtomicReference.class.equals(type)) {
            return new AtomicReference(value);
        }
        else if (AtomicIntegerArray.class.equals(type)) {
            int[] tmp = (int[]) value;
            int len = tmp.length;
            AtomicIntegerArray array = new AtomicIntegerArray(len);
            for (int i = 0; i < len; i++) {
                array.set(i, tmp[i]);
            }
            return array;
        }
        else if (AtomicLongArray.class.equals(type)) {
            long[] tmp = (long[]) value;
            int len = tmp.length;
            AtomicLongArray array = new AtomicLongArray(len);
            for (int i = 0; i < len; i++) {
                array.set(i, tmp[i]);
            }
            return array;
        }
        else if (AtomicReferenceArray.class.equals(type)) {
            Object[] tmp = (Object[]) value;
            int len = tmp.length;
            AtomicReferenceArray array = new AtomicReferenceArray(len);
            for (int i = 0; i < len; i++) {
                array.set(i, tmp[i]);
            }
            return array;
        }

        throw new UnsupportedOperationException(String.valueOf(this));
    }

}