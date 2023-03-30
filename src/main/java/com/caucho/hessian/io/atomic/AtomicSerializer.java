/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.atomic;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author junyuan
 * @version AtomicSerializer.java, v 0.1 2023年03月30日 17:29 junyuan Exp $
 */
public class AtomicSerializer extends AbstractSerializer {
    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (obj == null) {
            out.writeNull();
            return;
        }

        Object value;
        if (obj instanceof AtomicBoolean) {
            value = ((AtomicBoolean) obj).get();
        }
        else if (obj instanceof AtomicInteger) {
            value = ((AtomicInteger) obj).get();
        }
        else if (obj instanceof AtomicLong) {
            value = ((AtomicLong) obj).get();
        }
        else if (obj instanceof AtomicReference) {
            value = ((AtomicReference<?>) obj).get();
        }
        else if (obj instanceof AtomicIntegerArray) {
            AtomicIntegerArray tmp = (AtomicIntegerArray) obj;
            int len = tmp.length();
            int[] ints = new int[len];
            for (int i = 0; i < len; i++) {
                ints[i] = tmp.get(i);
            }
            value = ints;
        }
        else if (obj instanceof AtomicLongArray) {
            AtomicLongArray tmp = (AtomicLongArray) obj;
            int len = tmp.length();
            long[] ints = new long[len];
            for (int i = 0; i < len; i++) {
                ints[i] = tmp.get(i);
            }
            value = ints;
        } else {
            throw new UnsupportedOperationException(String.valueOf(this));
        }

        out.writeObject(new AtomicHandle(obj.getClass(), value));
    }

}