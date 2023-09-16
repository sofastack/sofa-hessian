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
 * jdk17的兼容测试
 * 由于throwable类必定有损
 * 这里的测试方法为分别使用不同的方式进行序列化和反序列化, 排除允许有损的字段后, 检查其余字段
 *
 * @author junyuan
 * @version SerializeCompatibleTest.java, v 0.1 2023年05月06日 15:52 junyuan Exp $
 */
public class SerializeCompatibleTest {
    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        originFactory = new SerializeFactoryWithoutThrowable();
        // 可以在jdk8环境下使用到专门为jdk17准备的 serializer
        useJdk17Factory = new JDK17SerializeFactory();

        os = new ByteArrayOutputStream();
    }

    /**
     * Wrapper
     * use jdk8 to serialize and jdk17 to deserialize
     * 'cause' is bound to lost here as getCause may return null
     */
    @Test
    public void test_case_1() throws IOException {
        if (isLessThanJdk17) {
            test_EnumConstantNotPresentExceptionWrapper(originFactory, useJdk17Factory);
        }
    }

    /**
     * Exception class directly
     * use jdk17 to serialize and jdk8 to deserialize
     */
    @Test
    public void test_case_2() throws IOException {
        if (isLessThanJdk17) {
            test_EnumConstantNotPresentException(originFactory, useJdk17Factory);
        }
    }

    /**
     * Wrapper
     * use jdk17 to serialize and jdk8 to deserialize
     */
    @Test
    public void test_case_3() throws IOException {
        if (isLessThanJdk17) {
            test_EnumConstantNotPresentExceptionWrapper(useJdk17Factory, originFactory);
        }
    }

    /**
     * Exception class directly
     * use jdk17 to serialize and jdk8 to deserialize
     */
    @Test
    public void test_case_4() throws IOException {
        if (isLessThanJdk17) {
            test_EnumConstantNotPresentException(useJdk17Factory, originFactory);
        }
    }

    /**
     * Wrapper
     * use jdk17 to serialize and deserialize
     */
    @Test
    public void test_case_5() throws IOException {
        test_EnumConstantNotPresentExceptionWrapper(factory, factory);
    }

    /**
     * Exception class directly
     * use jdk17 to serialize and deserialize
     */
    @Test
    public void test_case_6() throws IOException {
        test_EnumConstantNotPresentException(factory, factory);
    }

    /**
     * use jdk 8 reflect to encode and jdk17 non-reflect to decode
     */
    @Test
    public void test_compatible_case_1() throws IOException {
        Throwable t = null;
        try {
            Object x = Character.UnicodeBlock.of(13123123);
        } catch (Throwable e) {
            // t.cause = t
            // t.getCause == null
            t = e;
        }

        Object result = doEncodeNDecode(t, originFactory, useJdk17Factory);

        assert result instanceof Throwable;

        Throwable res = (Throwable) result;

        Assert.assertNull(res.getCause());

    }

    @Test
    public void test_compatible_case_2() throws IOException {
        Throwable t = null;
        try {
            Object x = Character.UnicodeBlock.of(13123123);
        } catch (Throwable e) {
            // t.cause = t
            // t.getCause == null
            t = e;
        }

        Object result = doEncodeNDecode(t, useJdk17Factory, originFactory);

        assert result instanceof Throwable;

        Throwable res = (Throwable) result;

        Assert.assertNull(res.getCause());

    }

    private static SerializerFactory     useJdk17Factory;
    private static SerializerFactory     originFactory;
    /**
     * 和originFactory的差别有一个 stack trance element deserializer不同
     * factory直接使用了新版的, originFactory创建了一个老版本的deserializer
     * factory 可以在jdk8或者17环境下运行, 自适应
     */
    private static SerializerFactory     factory;
    private static ByteArrayOutputStream os;

    private Method                       addSuppressed   = null;
    private Method                       getSuppressed   = null;
    {
        try {
            addSuppressed = Throwable.class.getMethod("addSuppressed", Throwable.class);
            getSuppressed = Throwable.class.getMethod("getSuppressed");
        } catch (Exception e) {

        }
    }

    Throwable                            t               = null;
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

    private static final boolean         isLessThanJdk17 = isLessThanJdk17();

    private static boolean isLessThanJdk17() {
        String javaVersion = System.getProperty("java.specification.version");
        return Double.parseDouble(javaVersion) < 17;
    }

    protected Object doEncodeNDecode(Object origin, SerializerFactory serializerFactory,
                                     SerializerFactory deserializerFactory) throws IOException {
        os.reset();
        Hessian2Output output = new Hessian2Output(os);

        output.setSerializerFactory(serializerFactory);
        output.writeObject(origin);
        output.flush();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        Hessian2Input input = new Hessian2Input(is);
        input.setSerializerFactory(deserializerFactory);
        Object actual = input.readObject();
        return actual;
    }

    /**
     * EnumConstantNotPresentException 作为成员变量
     * @throws IOException
     */
    private void test_EnumConstantNotPresentExceptionWrapper(SerializerFactory serialize, SerializerFactory deserialize)
        throws IOException {
        if (isLessThanJdk17) {
            // jdk 17 开始不需要执行这个, 反射受限, 执行会失败

            ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
            exceptionWrapper.setT(t);
            if (addSuppressed != null) {
                addSuppress(exceptionWrapper.getT());
                addSuppress(exceptionWrapper.getT());
            }

            Object result = doEncodeNDecode(exceptionWrapper, serialize, deserialize);
            Assert.assertTrue(result instanceof ExceptionWrapper);

            Throwable origin = exceptionWrapper.getT();
            Throwable target = ((ExceptionWrapper) result).getT();

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
    }

    /**
     * EnumConstantNotPresentException 直接进行序列化
     * @throws IOException
     */
    private void test_EnumConstantNotPresentException(SerializerFactory serialize, SerializerFactory deserialize)
        throws IOException {
        if (isLessThanJdk17) {
            // jdk 17 开始不需要执行这个, 反射受限, 执行会失败

            if (addSuppressed != null) {
                addSuppress(t);
                addSuppress(t);
            }

            Object result = doEncodeNDecode(t, serialize, deserialize);
            Assert.assertTrue(result instanceof Throwable);

            Throwable origin = t;
            Throwable target = (Throwable) result;

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
    }

    private void addSuppress(Throwable t) {
        Throwable suppressT1 = null;
        try {
            String x = null;
            x.equals("");
        } catch (NullPointerException e) {
            suppressT1 = e;
        }

        try {
            addSuppressed.invoke(t, suppressT1);
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
