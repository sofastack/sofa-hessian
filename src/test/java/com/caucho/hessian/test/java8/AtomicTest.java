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
package com.caucho.hessian.test.java8;

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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 *
 * @author junyuan
 * @version AtomicTest.java, v 0.1 2023年03月29日 18:16 junyuan Exp $
 */
public class AtomicTest {
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

        atomicWrapper.setaIntegerArray(new AtomicIntegerArray(2));
        atomicWrapper.getaIntegerArray().set(0, 0);
        atomicWrapper.getaIntegerArray().set(1, 1);

        atomicWrapper.setaLongArray(new AtomicLongArray(2));
        atomicWrapper.getaLongArray().set(0, 0L);
        atomicWrapper.getaLongArray().set(1, 1L);

        // work
        Object actual = doEncodeNDecode(atomicWrapper);

        // check
        Assert.assertTrue(actual instanceof AtomicWrapper);
        Assert.assertEquals(atomicWrapper.getaInteger().get(), ((AtomicWrapper) actual).getaInteger().get());
        Assert.assertEquals(atomicWrapper.getaBoolean().get(), ((AtomicWrapper) actual).getaBoolean().get());
        Assert.assertEquals(atomicWrapper.getaLong().get(), ((AtomicWrapper) actual).getaLong().get());

        Assert
            .assertEquals(atomicWrapper.getaIntegerArray().get(0), ((AtomicWrapper) actual).getaIntegerArray().get(0));
        Assert
            .assertEquals(atomicWrapper.getaIntegerArray().get(1), ((AtomicWrapper) actual).getaIntegerArray().get(1));

        Assert.assertEquals(atomicWrapper.getaLongArray().get(0), ((AtomicWrapper) actual).getaLongArray().get(0));
        Assert.assertEquals(atomicWrapper.getaLongArray().get(1), ((AtomicWrapper) actual).getaLongArray().get(1));
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