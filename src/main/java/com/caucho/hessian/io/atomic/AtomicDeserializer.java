/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.atomic;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
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

    private BasicDeserializer intArrayDsr  = new BasicDeserializer(BasicDeserializer.INTEGER_ARRAY);
    private BasicDeserializer longArrayDsr = new BasicDeserializer(BasicDeserializer.LONG_ARRAY);

    public AtomicDeserializer(Class<?> cl) {
        this._type = cl;
    }

    @Override
    public Object readObject(AbstractHessianInput in, String[] fieldNames) throws IOException {

        if (AtomicInteger.class.equals(_type)) {
            AtomicInteger tmp = new AtomicInteger();
            in.addRef(tmp);
            tmp.set(in.readInt());
            return tmp;
        }
        else if (AtomicBoolean.class.equals(_type)) {
            AtomicBoolean tmp = new AtomicBoolean();
            in.addRef(tmp);
            tmp.set(in.readInt() == 1);
            return tmp;
        }
        else if (AtomicLong.class.equals(_type)) {
            AtomicLong tmp = new AtomicLong();
            in.addRef(tmp);
            tmp.set(in.readLong());
            return tmp;
        }
        else if (AtomicReference.class.equals(_type)) {
            AtomicReference tmp = new AtomicReference();
            in.addRef(tmp);
            tmp.set(in.readObject());
            return tmp;
        }
        else if (AtomicIntegerArray.class.equals(_type)) {
            AtomicIntegerArray array = null;
            int ref = in.addRef(array);
            int[] res = (int[]) intArrayDsr.readObject(in);
            int len = res.length;
            array = new AtomicIntegerArray(len);
            for (int i = 0; i < len; i++) {
                array.set(i, res[i]);
            }
            in.setRef(ref, array);
            return array;
        }
        else if (AtomicLongArray.class.equals(_type)) {
            AtomicLongArray array = null;
            int ref = in.addRef(array);
            long[] res = (long[]) longArrayDsr.readObject(in);
            int len = res.length;
            array = new AtomicLongArray(len);
            for (int i = 0; i < len; i++) {
                array.set(i, res[i]);
            }
            in.setRef(ref, array);
            return array;
        }
        else if (AtomicReferenceArray.class.equals(_type)) {
            int ref = in.addRef(null);
            Object[] res = (Object[]) in.readObject((new Object[0]).getClass());
            int len = res.length;
            AtomicReferenceArray array = new AtomicReferenceArray(len);
            for (int i = 0; i < len; i++) {
                array.set(i, res[i]);
            }
            in.setRef(ref, array);
            return array;
        }

        throw new UnsupportedOperationException(String.valueOf(this));
    }
}