/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.caucho.hessian.test.atomic;

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.io.atomic.AtomicSerializer;
import com.caucho.hessian.io.throwable.JDK17SerializeFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * for version 3.5.0
 * assure that the result bytes of Atomic classes remains, whatever as object or field, same as
 * previous version
 *
 * @author junyuan
 * @version SerializeCompatibleTest.java, v 0.1 2023年03月31日 16:40 junyuan Exp $
 */
public class SerializeCompatibleTest {
    private static SerializerFactory     factory;
    private static SerializerFactory     useJdk17Factory;

    private static ByteArrayOutputStream os;

    private static final boolean         isLessThanJdk17 = isLessThanJdk17();

    private static boolean isLessThanJdk17() {
        String javaVersion = System.getProperty("java.specification.version");
        return Double.parseDouble(javaVersion) < 17;
    }

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();

        os = new ByteArrayOutputStream();
    }

    @AfterClass
    public static void tearDown() {
        try {
            Field field = SerializerFactory.class.getDeclaredField("_staticSerializerMap");
            field.setAccessible(true);
            ConcurrentMap map = (ConcurrentMap) field.get(SerializerFactory.class);
            AtomicSerializer atomicSerializer = new AtomicSerializer();
            map.put(AtomicInteger.class, atomicSerializer);
            map.put(AtomicLong.class, atomicSerializer);
            map.put(AtomicBoolean.class, atomicSerializer);
            map.put(AtomicReference.class, atomicSerializer);
            map.put(AtomicLongArray.class, atomicSerializer);
            map.put(AtomicIntegerArray.class, atomicSerializer);
            map.put(AtomicReferenceArray.class, atomicSerializer);
        } catch (Throwable t) {
        }
    }

    /**
     * result byte should be in same with former version so as to behaving compatible
     * @throws IOException
     */
    @Test
    public void test_serialize() throws IOException, NoSuchFieldException, IllegalAccessException {
        if (!isLessThanJdk17) {
            return;
        }

        byte[] wrappedCaseAtomic = serializeAtomicWrapper();
        byte[] unwrappedIntegerAtomic = serializeAtomicInteger();
        byte[] unwrappedBooleanAtomic = serializeAtomicBoolean();
        byte[] unwrappedLongAtomic = serializeAtomicLong();
        byte[] unwrappedReferenceAtomic = serializeAtomicReference();
        byte[] unwrappedIntegerArrayAtomic = serializeAtomicIntegerArray();
        byte[] unwrappedLongArrayAtomic = serializeAtomicLongArray();
        byte[] unwrappedReferenceArrayAtomic = serializeAtomicReferenceArray();

        // use origin java serialize then
        Field field = SerializerFactory.class.getDeclaredField("_staticSerializerMap");
        field.setAccessible(true);
        ConcurrentMap map = (ConcurrentMap) field.get(SerializerFactory.class);
        map.remove(AtomicInteger.class);
        map.remove(AtomicBoolean.class);
        map.remove(AtomicLong.class);
        map.remove(AtomicReference.class);
        map.remove(AtomicIntegerArray.class);
        map.remove(AtomicLongArray.class);
        map.remove(AtomicReferenceArray.class);

        byte[] wrappedCaseJava = serializeAtomicWrapper();
        byte[] unwrappedIntegerJava = serializeAtomicInteger();
        byte[] unwrappedBooleanJava = serializeAtomicBoolean();
        byte[] unwrappedLongJava = serializeAtomicLong();
        byte[] unwrappedReferenceJava = serializeAtomicReference();
        byte[] unwrappedIntegerArrayJava = serializeAtomicIntegerArray();
        byte[] unwrappedLongArrayJava = serializeAtomicLongArray();
        byte[] unwrappedReferenceArrayJava = serializeAtomicReferenceArray();

        Assert.assertTrue(bytesEquals(wrappedCaseAtomic, wrappedCaseJava));

        Assert.assertTrue(bytesEquals(unwrappedIntegerAtomic, unwrappedIntegerJava));
        Assert.assertTrue(bytesEquals(unwrappedBooleanAtomic, unwrappedBooleanJava));
        Assert.assertTrue(bytesEquals(unwrappedLongAtomic, unwrappedLongJava));
        Assert.assertTrue(bytesEquals(unwrappedReferenceAtomic, unwrappedReferenceJava));
        Assert.assertTrue(bytesEquals(unwrappedIntegerArrayAtomic, unwrappedIntegerArrayJava));
        Assert.assertTrue(bytesEquals(unwrappedLongArrayAtomic, unwrappedLongArrayJava));
        Assert.assertTrue(bytesEquals(unwrappedReferenceArrayAtomic, unwrappedReferenceArrayJava));
    }

    protected boolean bytesEquals(byte[] src, byte[] target) {
        if (src == null && target == null) {
            return true;
        }

        if (src == null || target == null) {
            return false;
        }

        if (src.length != target.length) {
            return false;
        }

        for (int i = 0; i < src.length; i++) {
            if (src[i] != target[i]) {
                return false;
            }
        }
        return true;
    }

    private byte[] serializeAtomicWrapper() throws IOException {
        // wrapped
        AtomicWrapper atomicWrapper = new AtomicWrapper();
        atomicWrapper.setaInteger(new AtomicInteger(17));
        atomicWrapper.setaBoolean(new AtomicBoolean(true));
        atomicWrapper.setaLong(new AtomicLong(2147483649L));
        atomicWrapper.setaReference(new AtomicReference());
        atomicWrapper.getaReference().set(new Integer(1));

        atomicWrapper.setaIntegerArray(new AtomicIntegerArray(2));
        atomicWrapper.getaIntegerArray().set(0, 0);
        atomicWrapper.getaIntegerArray().set(1, 1);

        atomicWrapper.setaLongArray(new AtomicLongArray(2));
        atomicWrapper.getaLongArray().set(0, 0L);
        atomicWrapper.getaLongArray().set(1, 1L);

        atomicWrapper.setaReferenceArray(new AtomicReferenceArray(2));
        atomicWrapper.getaReferenceArray().set(0, new Integer(1));
        atomicWrapper.getaReferenceArray().set(1, new Integer(2));

        os.reset();
        Hessian2Output wrapCaseOutput = new Hessian2Output(os);

        wrapCaseOutput.setSerializerFactory(factory);
        wrapCaseOutput.writeObject(atomicWrapper);
        wrapCaseOutput.flush();
        byte[] wrappedBytes = os.toByteArray();
        return wrappedBytes;
    }

    private byte[] serializeAtomicInteger() throws IOException {
        AtomicInteger atomic = new AtomicInteger(1);

        os.reset();
        Hessian2Output output = new Hessian2Output(os);
        output.setSerializerFactory(factory);
        output.writeObject(atomic);
        output.flush();
        byte[] unwrappedBytes = os.toByteArray();
        return unwrappedBytes;
    }

    private byte[] serializeAtomicBoolean() throws IOException {
        AtomicBoolean atomic = new AtomicBoolean(true);

        os.reset();
        Hessian2Output output = new Hessian2Output(os);
        output.setSerializerFactory(factory);
        output.writeObject(atomic);
        output.flush();
        byte[] unwrappedBytes = os.toByteArray();
        return unwrappedBytes;
    }

    private byte[] serializeAtomicLong() throws IOException {
        AtomicLong atomic = new AtomicLong(1L);

        os.reset();
        Hessian2Output output = new Hessian2Output(os);
        output.setSerializerFactory(factory);
        output.writeObject(atomic);
        output.flush();
        byte[] unwrappedBytes = os.toByteArray();
        return unwrappedBytes;
    }

    private byte[] serializeAtomicReference() throws IOException {
        AtomicReference atomic = new AtomicReference(new Integer(1));

        os.reset();
        Hessian2Output output = new Hessian2Output(os);
        output.setSerializerFactory(factory);
        output.writeObject(atomic);
        output.flush();
        byte[] unwrappedBytes = os.toByteArray();
        return unwrappedBytes;
    }

    private byte[] serializeAtomicIntegerArray() throws IOException {
        AtomicIntegerArray array = new AtomicIntegerArray(2);
        array.set(0, 0);
        array.set(1, 1);
        os.reset();
        Hessian2Output output = new Hessian2Output(os);
        output.setSerializerFactory(factory);
        output.writeObject(array);
        output.flush();
        byte[] unwrappedBytes = os.toByteArray();
        return unwrappedBytes;
    }

    private byte[] serializeAtomicLongArray() throws IOException {
        AtomicLongArray array = new AtomicLongArray(2);
        array.set(0, 0L);
        array.set(1, 1L);
        os.reset();
        Hessian2Output output = new Hessian2Output(os);
        output.setSerializerFactory(factory);
        output.writeObject(array);
        output.flush();
        byte[] unwrappedBytes = os.toByteArray();
        return unwrappedBytes;
    }

    private byte[] serializeAtomicReferenceArray() throws IOException {
        AtomicReferenceArray array = new AtomicReferenceArray(2);
        array.set(0, new Integer(0));
        array.set(1, new Integer(1));
        os.reset();
        Hessian2Output output = new Hessian2Output(os);
        output.setSerializerFactory(factory);
        output.writeObject(array);
        output.flush();
        byte[] unwrappedBytes = os.toByteArray();
        return unwrappedBytes;
    }

}