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
package com.caucho.hessian.test.throwable;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author junyuan
 * @version ThrowableClassTest.java, v 0.1 2023年04月10日 14:42 junyuan Exp $
 */
public class ThrowableClassTest {

    private static SerializerFactory     factory;
    private static ByteArrayOutputStream os;

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        os = new ByteArrayOutputStream();
    }

    @Test
    public void test_stacktrace() throws IOException {
        Throwable t = null;
        try {
            int x = 1 / 0;
        } catch (Exception e) {
            t = e;
        }

        ExceptionWrapper w = new ExceptionWrapper();
        w.setT(t);

        Object result = doEncodeNDecode(w);

        Assert.assertTrue(result instanceof ExceptionWrapper);

        StackTraceElement[] origin = w.getT().getStackTrace();
        StackTraceElement[] target = ((ExceptionWrapper) result).getT().getStackTrace();

        Assert.assertEquals(origin.length, target.length);
        Assert.assertArrayEquals(origin, target);
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
