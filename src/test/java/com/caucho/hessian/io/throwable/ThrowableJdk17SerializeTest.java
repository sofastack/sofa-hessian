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
 * can run under JDK 6 - 17
 * under jdk 8, reflection are used to do the serialize work
 * under jdk17, getter and reflection take effects simultaneously
 *
 * @author junyuan
 * @version ThrowableJdk17SerializeTest.java, v 0.1 2023年05月06日 17:53 junyuan Exp $
 */
public class ThrowableJdk17SerializeTest {

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

    Throwable                            t             = null;

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        os = new ByteArrayOutputStream();
    }

    @Test
    public void test_EnumConstantNotPresentException() throws IOException {
        EnumConstantNotPresentException e = new EnumConstantNotPresentException(MeaninglessEnum.class, "CIG");

        try {
            addSuppressed.invoke(e, new NullPointerException());
        } catch (Exception e1) {

        }

        Object result = doEncodeNDecode(e);
        Assert.assertTrue(result instanceof EnumConstantNotPresentException);

        Throwable origin = e;
        Throwable target = (Throwable) result;

        serializeCheck(origin, target);
    }

    @Test
    public void test_NPE() throws IOException {
        Throwable t = null;
        try {
            t.getMessage();
        } catch (NullPointerException e) {
            t = e;
        }

        Object result = doEncodeNDecode(t);
        Assert.assertTrue(result instanceof NullPointerException);

        Throwable origin = t;
        Throwable target = (Throwable) result;

        serializeCheck(origin, target);
    }

    @Test
    public void test_SelfDefined() throws IOException {
        SelfDefinedException e = new SelfDefinedException("0023", "error msg");

        Object result = doEncodeNDecode(e);
        Assert.assertTrue(result instanceof SelfDefinedException);

        Throwable origin = t;
        Throwable target = (Throwable) result;
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

    protected void serializeCheck(Throwable origin, Throwable target) {
        // stack trace
        Assert.assertEquals(origin.getStackTrace().length, target.getStackTrace().length);
        Assert.assertArrayEquals(origin.getStackTrace(), target.getStackTrace());

        // detail message
        Assert.assertEquals(origin.getMessage(), target.getMessage());

        // cause, now only assert on cause.detailMessage
        if (origin.getCause() == null) {
            // getCause为空可能是 cause == this 的情况, 此时只需要保证target也为null即可
            // 实际该字段可能在序列化过程中有损
            Assert.assertNull(target.getCause());
        }

        // suppress
        try {
            Throwable[] originSuppressed = (Throwable[]) getSuppressed.invoke(origin);
            Throwable[] targetSuppressed = (Throwable[]) getSuppressed.invoke(target);
            if (originSuppressed == null && targetSuppressed == null) {
                return;
            }
            Assert.assertFalse("one suppress is null while another is not",
                originSuppressed == null || targetSuppressed == null);

            Assert.assertEquals(originSuppressed.length, targetSuppressed.length);
            for (int i = 0; i < originSuppressed.length; i++) {
                Assert.assertEquals(originSuppressed[i].getMessage(), targetSuppressed[i].getMessage());
            }
        } catch (Exception e2) {

        }
    }

    private static final boolean isLessThanJdk17 = isLessThanJdk17();

    private static boolean isLessThanJdk17() {
        String javaVersion = System.getProperty("java.specification.version");
        return Double.parseDouble(javaVersion) < 17;
    }

    {
        Throwable x = null;
        try {
            t.getStackTrace();
        } catch (NullPointerException e) {
            x = e;
        }

        t = new EnumConstantNotPresentException(MeaninglessEnum.class, "CIG");

        try {
            addSuppressed.invoke(t, x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
