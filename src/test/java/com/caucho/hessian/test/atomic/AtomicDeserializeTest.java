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

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
 * @version AtomicDeserializeTest.java, v 0.1 2023年03月29日 18:16 junyuan Exp $
 */
public class AtomicDeserializeTest {
    private static SerializerFactory     factory;
    private static ByteArrayOutputStream os;

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();

        os = new ByteArrayOutputStream();
    }

    @Test
    public void test_atomicWrapper() throws IOException {
        os.reset();

        // prepare
        AtomicWrapper atomicWrapper = new AtomicWrapper();
        atomicWrapper.setaInteger(new AtomicInteger(17));
        atomicWrapper.setaBoolean(new AtomicBoolean(true));
        atomicWrapper.setaLong(new AtomicLong(2147483649L));
        atomicWrapper.setaReference(new AtomicReference(new Integer(1)));

        atomicWrapper.setaIntegerArray(new AtomicIntegerArray(2));
        atomicWrapper.getaIntegerArray().set(0, 0);
        atomicWrapper.getaIntegerArray().set(1, 1);

        atomicWrapper.setaLongArray(new AtomicLongArray(2));
        atomicWrapper.getaLongArray().set(0, 0L);
        atomicWrapper.getaLongArray().set(1, 1L);

        atomicWrapper.setaReferenceArray(new AtomicReferenceArray(2));
        atomicWrapper.getaReferenceArray().set(0, new Integer(1));
        atomicWrapper.getaReferenceArray().set(1, new Integer(2));

        // work
        Object actual = doEncodeNDecode(atomicWrapper);

        // check
        Assert.assertTrue(actual instanceof AtomicWrapper);
        Assert.assertEquals(atomicWrapper.getaInteger().get(), ((AtomicWrapper) actual).getaInteger().get());
        Assert.assertEquals(atomicWrapper.getaBoolean().get(), ((AtomicWrapper) actual).getaBoolean().get());
        Assert.assertEquals(atomicWrapper.getaLong().get(), ((AtomicWrapper) actual).getaLong().get());
        Assert.assertEquals(atomicWrapper.getaReference().get(), ((AtomicWrapper) actual).getaReference().get());

        Assert
            .assertEquals(atomicWrapper.getaIntegerArray().get(0), ((AtomicWrapper) actual).getaIntegerArray().get(0));
        Assert
            .assertEquals(atomicWrapper.getaIntegerArray().get(1), ((AtomicWrapper) actual).getaIntegerArray().get(1));

        Assert.assertEquals(atomicWrapper.getaLongArray().get(0), ((AtomicWrapper) actual).getaLongArray().get(0));
        Assert.assertEquals(atomicWrapper.getaLongArray().get(1), ((AtomicWrapper) actual).getaLongArray().get(1));

        Assert.assertEquals(atomicWrapper.getaReferenceArray().get(0), ((AtomicWrapper) actual).getaReferenceArray()
            .get(0));
        Assert.assertEquals(atomicWrapper.getaReferenceArray().get(1), ((AtomicWrapper) actual).getaReferenceArray()
            .get(1));
    }

    @Test
    public void test_ref() throws IOException {
        List<AtomicWrapper> l = new ArrayList<AtomicWrapper>();

        // prepare
        AtomicInteger ai = new AtomicInteger(17);
        AtomicBoolean ab = new AtomicBoolean(true);
        AtomicLong al = new AtomicLong(2147483649L);
        AtomicReference at = new AtomicReference(new Integer(1));
        AtomicIntegerArray aia = new AtomicIntegerArray(2);
        aia.set(0, 0);
        aia.set(1, 1);
        AtomicLongArray ala = new AtomicLongArray(2);
        ala.set(0, 0L);
        ala.set(1, 1L);
        AtomicReferenceArray ara = new AtomicReferenceArray(2);
        ara.set(0, new Integer(1));
        ara.set(1, new Integer(2));

        AtomicWrapper atomicWrapper1 = new AtomicWrapper();
        atomicWrapper1.setaInteger(ai);
        atomicWrapper1.setaBoolean(ab);
        atomicWrapper1.setaLong(al);
        atomicWrapper1.setaReference(at);
        atomicWrapper1.setaIntegerArray(aia);
        atomicWrapper1.setaLongArray(ala);
        atomicWrapper1.setaReferenceArray(ara);

        AtomicWrapper atomicWrapper2 = new AtomicWrapper();
        atomicWrapper2.setaInteger(ai);
        atomicWrapper2.setaBoolean(ab);
        atomicWrapper2.setaLong(al);
        atomicWrapper2.setaReference(at);
        atomicWrapper2.setaIntegerArray(aia);
        atomicWrapper2.setaLongArray(ala);
        atomicWrapper2.setaReferenceArray(ara);
        l.add(atomicWrapper1);
        l.add(atomicWrapper2);

        Object result = doEncodeNDecode(l);
        Assert.assertTrue(result instanceof List);
        List<AtomicWrapper> resultInstance = (List<AtomicWrapper>) result;

        Assert.assertTrue(atomicWrapper1 instanceof AtomicWrapper);
        AtomicWrapper actual = atomicWrapper1;
        Assert.assertEquals(resultInstance.get(0).getaInteger().get(), actual.getaInteger().get());
        Assert.assertEquals(resultInstance.get(0).getaBoolean().get(), actual.getaBoolean().get());
        Assert.assertEquals(resultInstance.get(0).getaLong().get(), actual.getaLong().get());
        Assert.assertEquals(resultInstance.get(0).getaReference().get(), actual.getaReference().get());

        Assert.assertEquals(resultInstance.get(0).getaIntegerArray().get(0), actual.getaIntegerArray().get(0));
        Assert.assertEquals(resultInstance.get(0).getaIntegerArray().get(1), actual.getaIntegerArray().get(1));
        Assert.assertEquals(resultInstance.get(0).getaLongArray().get(0), actual.getaLongArray().get(0));
        Assert.assertEquals(resultInstance.get(0).getaLongArray().get(1), actual.getaLongArray().get(1));
        Assert.assertEquals(resultInstance.get(0).getaReferenceArray().get(0), actual.getaReferenceArray()
            .get(0));
        Assert.assertEquals(resultInstance.get(0).getaReferenceArray().get(1), actual.getaReferenceArray()
            .get(1));

        Assert.assertEquals(resultInstance.get(1).getaInteger().get(), actual.getaInteger().get());
        Assert.assertEquals(resultInstance.get(1).getaBoolean().get(), actual.getaBoolean().get());
        Assert.assertEquals(resultInstance.get(1).getaLong().get(), actual.getaLong().get());
        Assert.assertEquals(resultInstance.get(1).getaReference().get(), actual.getaReference().get());

        Assert.assertEquals(resultInstance.get(1).getaIntegerArray().get(0), actual.getaIntegerArray().get(0));
        Assert.assertEquals(resultInstance.get(1).getaIntegerArray().get(1), actual.getaIntegerArray().get(1));
        Assert.assertEquals(resultInstance.get(1).getaLongArray().get(0), actual.getaLongArray().get(0));
        Assert.assertEquals(resultInstance.get(1).getaLongArray().get(1), actual.getaLongArray().get(1));
        Assert.assertEquals(resultInstance.get(1).getaReferenceArray().get(0), actual.getaReferenceArray()
            .get(0));
        Assert.assertEquals(resultInstance.get(1).getaReferenceArray().get(1), actual.getaReferenceArray()
            .get(1));

    }

    @Test
    public void test_atomic_unwrappedInteger() throws IOException {
        os.reset();
        AtomicInteger i = new AtomicInteger(1);
        Object actual = doEncodeNDecode(i);

        Assert.assertTrue(actual instanceof AtomicInteger);
        TestCase.assertEquals(i.get(), ((AtomicInteger) actual).get());
    }

    @Test
    public void test_atomic_unwrappedLong() throws IOException {
        os.reset();
        AtomicLong i = new AtomicLong(1L);
        Object actual = doEncodeNDecode(i);

        Assert.assertTrue(actual instanceof AtomicLong);
        TestCase.assertEquals(i.get(), ((AtomicLong) actual).get());
    }

    @Test
    public void test_atomic_unwrappedBoolean() throws IOException {
        os.reset();
        AtomicBoolean i = new AtomicBoolean(true);
        Object actual = doEncodeNDecode(i);

        Assert.assertTrue(actual instanceof AtomicBoolean);
        TestCase.assertEquals(i.get(), ((AtomicBoolean) actual).get());
    }

    @Test
    public void test_atomic_unwrappedReference() throws IOException {
        os.reset();
        AtomicReference i = new AtomicReference(new Integer(1));
        Object actual = doEncodeNDecode(i);

        Assert.assertTrue(actual instanceof AtomicReference);
        TestCase.assertEquals(i.get(), ((AtomicReference) actual).get());
    }

    @Test
    public void test_atomic_unwrappedIntegerArray() throws IOException {
        os.reset();
        AtomicIntegerArray i = new AtomicIntegerArray(2);
        i.set(0, 0);
        i.set(1, 1);

        Object actual = doEncodeNDecode(i);

        Assert.assertTrue(actual instanceof AtomicIntegerArray);
        Assert.assertEquals(i.get(0), ((AtomicIntegerArray) actual).get(0));
        Assert.assertEquals(i.get(1), ((AtomicIntegerArray) actual).get(1));
    }

    @Test
    public void test_atomic_unwrappedLongArray() throws IOException {
        os.reset();
        AtomicLongArray i = new AtomicLongArray(2);
        i.set(0, 0L);
        i.set(1, 1L);

        Object actual = doEncodeNDecode(i);

        Assert.assertTrue(actual instanceof AtomicLongArray);
        Assert.assertEquals(i.get(0), ((AtomicLongArray) actual).get(0));
        Assert.assertEquals(i.get(1), ((AtomicLongArray) actual).get(1));
    }

    @Test
    public void test_atomic_unwrappedReferenceArray() throws IOException {
        os.reset();
        AtomicReferenceArray i = new AtomicReferenceArray(2);
        i.set(0, new Integer(0));
        i.set(1, new Integer(1));

        Object actual = doEncodeNDecode(i);

        Assert.assertTrue(actual instanceof AtomicReferenceArray);
        Assert.assertEquals(i.get(0), ((AtomicReferenceArray) actual).get(0));
        Assert.assertEquals(i.get(1), ((AtomicReferenceArray) actual).get(1));
    }

    protected Object doEncodeNDecode(Object origin) throws IOException {
        os.reset();
        Hessian2Output output = new Hessian2Output(os);

        output.setSerializerFactory(factory);
        output.writeObject(origin);
        output.flush();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        Hessian2Input input = new Hessian2Input(is);
        input.setSerializerFactory(factory);
        Object actual = input.readObject();
        return actual;
    }
}