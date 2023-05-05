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
package com.caucho.hessian.io.throwable;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 *
 * @author junyuan
 * @version ThrowableClassTest.java, v 0.1 2023年04月10日 14:42 junyuan Exp $
 */
public class ThrowableClassTest {

    private static SerializerFactory     factory;
    private static ByteArrayOutputStream os;
    private Method                       addSuppressed = null;
    private Method                       getSuppressed = null;
    {
        try {
            addSuppressed = Throwable.class.getMethod("addSuppressed", Throwable.class);
            getSuppressed = Throwable.class.getMethod("getSuppressed");
        } catch (Exception e) {

        }
    }

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        os = new ByteArrayOutputStream();
    }

    @Test
    public void test_Throwable() throws IOException {
        Throwable t = null;
        try {
            int x = 1 / 0;
        } catch (Exception e) {
            t = new Throwable(e);
        }

        ExceptionWrapper w = new ExceptionWrapper();
        w.setT(t);
        if (addSuppressed != null) {
            addSuppress(w);
            addSuppress(w);
        }

        Object result = doEncodeNDecode(w);

        Assert.assertTrue(result instanceof ExceptionWrapper);
        Throwable origin = w.getT();
        Throwable target = ((ExceptionWrapper) result).getT();

        // stack trace
        Assert.assertEquals(origin.getStackTrace().length, target.getStackTrace().length);
        Assert.assertArrayEquals(origin.getStackTrace(), target.getStackTrace());

        // detail message
        Assert.assertEquals(origin.getMessage(), target.getMessage());

        // cause, now only assert on cause.detailMessage
        Assert.assertEquals(origin.getCause().getMessage(), target.getCause().getMessage());

        // suppress
        Throwable[] originSuppressed = getSuppress(origin);
        Throwable[] targetSuppressed = getSuppress(target);
        if (originSuppressed == null && targetSuppressed == null) {
            return;
        }

        Assert.assertTrue("one suppress is null while another is not",
            !(originSuppressed == null || targetSuppressed == null));

        Assert.assertTrue(originSuppressed.length == targetSuppressed.length);
        for (int i = 0; i < originSuppressed.length; i++) {
            Assert.assertEquals(originSuppressed[i].getMessage(), targetSuppressed[i].getMessage());
        }
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

    private void mockCause(ExceptionWrapper w) {
    }

    private void addSuppress(ExceptionWrapper w) {
        Throwable suppressT1 = null;
        try {
            String x = null;
            x.equals("");
        } catch (NullPointerException e) {
            suppressT1 = e;
        }

        try {
            addSuppressed.invoke(w.getT(), suppressT1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Throwable[] getSuppress(Throwable t) {
        Throwable[] ts = null;
        try {
            ts = (Throwable[]) getSuppressed.invoke(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }

}
