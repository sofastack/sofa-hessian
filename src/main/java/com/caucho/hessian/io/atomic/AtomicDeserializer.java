/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.atomic;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.ArrayDeserializer;
import com.caucho.hessian.io.BasicDeserializer;

import java.io.IOException;
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
 * @version AtomicDeserializer.java, v 0.1 2023年03月31日 17:34 junyuan Exp $
 */
public class AtomicDeserializer extends AbstractDeserializer {

    private Class<?>          _type;

    private BasicDeserializer intArrayDsr    = new BasicDeserializer(BasicDeserializer.INTEGER_ARRAY);
    private BasicDeserializer longArrayDsr   = new BasicDeserializer(BasicDeserializer.LONG_ARRAY);
    private BasicDeserializer objectArrayDsr = new BasicDeserializer(BasicDeserializer.OBJECT_ARRAY);

    public AtomicDeserializer(Class<?> cl) {
        this._type = cl;
    }

    @Override
    public Object readObject(AbstractHessianInput in, String[] fieldNames) throws IOException {

        if (AtomicInteger.class.equals(_type)) {
            return new AtomicInteger(in.readInt());
        }
        else if (AtomicBoolean.class.equals(_type)) {
            return new AtomicBoolean(in.readInt() == 1);
        }
        else if (AtomicLong.class.equals(_type)) {
            return new AtomicLong(in.readLong());
        }
        else if (AtomicReference.class.equals(_type)) {
            return new AtomicReference(in.readObject());
        }
        else if (AtomicIntegerArray.class.equals(_type)) {
            int[] res = (int[]) intArrayDsr.readObject(in);
            int len = res.length;
            AtomicIntegerArray array = new AtomicIntegerArray(len);
            for (int i = 0; i < len; i++) {
                array.set(i, res[i]);
            }
            return array;
        }
        else if (AtomicLongArray.class.equals(_type)) {
            long[] res = (long[]) longArrayDsr.readObject(in);
            int len = res.length;
            AtomicLongArray array = new AtomicLongArray(len);
            for (int i = 0; i < len; i++) {
                array.set(i, res[i]);
            }
            return array;
        }
        else if (AtomicReferenceArray.class.equals(_type)) {
            Object[] res = (Object[]) in.readObject((new Object[0]).getClass());
            int len = res.length;
            AtomicReferenceArray array = new AtomicReferenceArray(len);
            for (int i = 0; i < len; i++) {
                array.set(i, res[i]);
            }
            return array;
        }

        throw new UnsupportedOperationException(String.valueOf(this));
    }
}