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
package com.caucho.hessian.io.java17.base;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.io.throwable.ExceptionWrapper;
import com.caucho.hessian.io.throwable.JDK17SerializeFactory;
import com.caucho.hessian.io.throwable.MeaninglessEnum;
import com.caucho.hessian.io.throwable.SerializeFactoryWithoutThrowable;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Currency;
import java.util.Locale;

/**
 * 在 jdk8 下模拟运行 jdk17 下用的序列化器
 *
 * @author junyuan
 * @version SerializeCompatibleTest.java, v 0.1 2023年08月09日 15:52 junyuan Exp $
 */
public class SerializeCompatibleTest {
    // 把 currency 的去掉了
    private static SerializerFactory     originFactory;
    private static SerializerFactory     factory;
    private static ByteArrayOutputStream os;

    private static final boolean isLessThanJdk17 = isLessThanJdk17();

    private static boolean isLessThanJdk17() {
        String javaVersion = System.getProperty("java.specification.version");
        return Double.parseDouble(javaVersion) < 17;
    }

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        originFactory = new SerializeFactoryWithoutCurrency();

        os = new ByteArrayOutputStream();
    }

    /**
     * Wrapper
     * use currencySerializer to encode and java serializer to decode
     * 'cause' is bound to lost here as getCause may return null
     */
    @Test
    public void test_case_1() throws IOException {
        if (isLessThanJdk17) {
            test_JavaCurrencyWrapper(factory, originFactory);
        }
    }

    /**
     * wrapper
     * use java serializer to encode and currencySerializer to decode
     * @throws IOException
     */
    @Test
    public void test_case_2() throws IOException {
        if (isLessThanJdk17) {
            test_JavaCurrencyWrapper(originFactory, factory);
        }
    }

    /**
     *
     * @throws IOException
     */
    @Test
    public void test_case_3() throws IOException {
        if (isLessThanJdk17) {
            test_JavaCurrencyDirectly(factory, originFactory);
        }
    }

    @Test
    public void test_case_4() throws IOException {
        if (isLessThanJdk17) {
            test_JavaCurrencyDirectly(originFactory, factory);
        }
    }

    /**
     * 确保 CurrencySerializer 和 JavaSerialize 序列化的产物完全一致
     * @throws IOException
     */
    @Test
    public void test_bytes_equals() throws IOException {
        if (isLessThanJdk17()) {
            test_Serialize(originFactory, factory);
        }
    }

    @Test
    public void test_decode_encode_jdk17() throws IOException {
        if (!isLessThanJdk17()) {
            test_JavaCurrencyWrapper(factory, factory);
        }

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

    private void test_JavaCurrencyWrapper(SerializerFactory serialize, SerializerFactory deserialize)
            throws IOException {
        if (isLessThanJdk17()) {
            CurrencyWrapper cw = new CurrencyWrapper();
            cw.setCurrency(Currency.getInstance(Locale.getDefault()));

            Object result = doEncodeNDecode(cw, serialize, deserialize);
            Assert.assertTrue(result instanceof CurrencyWrapper);
            Currency newC = ((CurrencyWrapper) result).getCurrency();
            Assert.assertEquals(cw.getCurrency(), newC);
        }
    }

    private void test_JavaCurrencyDirectly(SerializerFactory serialize, SerializerFactory deserialize)
            throws IOException {
        if (isLessThanJdk17()) {
            Currency origin = Currency.getInstance(Locale.getDefault());

            Object result = doEncodeNDecode(origin, serialize, deserialize);
            Assert.assertTrue(result instanceof Currency);
            Assert.assertEquals(origin, result);
        }
    }

    private void test_Serialize(SerializerFactory originFactory, SerializerFactory newFactory)
            throws IOException {
        CurrencyWrapper cw = new CurrencyWrapper();
        cw.setCurrency(Currency.getInstance(Locale.getDefault()));

        byte[] resultOrigin = doSerialize(cw, originFactory);
        byte[] resultNew = doSerialize(cw, factory);
        Assert.assertTrue(bytesEquals(resultOrigin, resultNew));
    }

    private byte[] doSerialize(Object o, SerializerFactory factory) throws IOException {
        os.reset();
        Hessian2Output output = new Hessian2Output(os);

        output.setSerializerFactory(originFactory);
        output.writeObject(o);
        output.flush();

        return os.toByteArray();
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
}
