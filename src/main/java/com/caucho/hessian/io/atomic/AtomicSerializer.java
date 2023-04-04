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
import java.util.concurrent.atomic.AtomicReferenceArray;

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
            doWrite(obj, "value", out);
        }
        else if (obj instanceof AtomicInteger) {
            doWrite(obj, "value", out);
        }
        else if (obj instanceof AtomicLong) {
            doWrite(obj, "value", out);
        }
        else if (obj instanceof AtomicReference) {
            doWrite(obj, "value", out);
        }
        else if (obj instanceof AtomicIntegerArray) {
            doWrite(obj, "array", out);
        }
        else if (obj instanceof AtomicLongArray) {
            doWrite(obj, "array", out);
        }
        else if (obj instanceof AtomicReferenceArray) {
            doWrite(obj, "array", out);
        }
        else {
            throw new UnsupportedOperationException(String.valueOf(this));
        }
    }

    protected void doWrite(Object obj, String fieldName, AbstractHessianOutput out) throws IOException {
        if (out.addRef(obj)) {
            return;
        }
        Class cl = obj.getClass();
        int ref = out.writeObjectBegin(cl.getName()); // atomicinteger.class
        if (ref < -1) {
            //                writeObject10(obj, out);
            out.writeString(fieldName/*field name*/);
            // field do serialize
            writeFieldValue(obj, out);
        }
        else {
            if (ref == -1) {
                out.writeClassFieldLength(1);
                out.writeString(fieldName/*field name*/);
                out.writeObjectBegin(cl.getName());
            }
            // field foreach do serialize
            writeFieldValue(obj, out);
        }
    }

    private void writeFieldValue(Object obj, AbstractHessianOutput out) throws IOException {
        if (obj instanceof AtomicInteger) {
            out.writeInt(((AtomicInteger) obj).get());
        }
        else if (obj instanceof AtomicLong) {
            out.writeLong(((AtomicLong) obj).get());
        }
        else if (obj instanceof AtomicBoolean) {
            out.writeInt(((AtomicBoolean) obj).get() ? 1 : 0);
        }
        else if (obj instanceof AtomicReference) {
            out.writeObject(((AtomicReference) obj).get());
        }
        else if (obj instanceof AtomicIntegerArray) {
            AtomicIntegerArray array = (AtomicIntegerArray) obj;
            int len = array.length();
            int[] tmp = new int[len];
            for (int i = 0; i < len; i++) {
                tmp[i] = array.get(i);
            }
            out.writeObject(tmp);
        }
        else if (obj instanceof AtomicLongArray) {
            AtomicLongArray array = (AtomicLongArray) obj;
            int len = array.length();
            long[] tmp = new long[len];
            for (int i = 0; i < len; i++) {
                tmp[i] = array.get(i);
            }
            out.writeObject(tmp);
        }
        else if (obj instanceof AtomicReferenceArray) {
            AtomicReferenceArray array = (AtomicReferenceArray) obj;
            int len = array.length();
            Object[] tmp = new Object[len];
            for (int i = 0; i < len; i++) {
                tmp[i] = array.get(i);
            }
            out.writeObject(tmp);
        }
    }

}