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
package com.caucho.hessian.io.java17.lang;

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
 * serialize test under same jdk
 * @author junyuan
 * @version Jdk17AbstractStringBuilderDecodeTest.java, v 0.1 2023年10月25日 11:41 junyuan Exp $
 */
public class AbstractStringBuilderTest {
    private static SerializerFactory     factory;
    private static ByteArrayOutputStream os;

    protected final static boolean       isHigherThanJdk17 = isJava17();

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        os = new ByteArrayOutputStream();
    }

    @Test
    public void test_Ser_Des_wrapper() throws IOException {
        StringBuilderWrapper sbw = buildStringBuilderWrapper("c12");

        Object result = doEncodeNDecode(sbw, factory, factory);

        assertForStringBuilderWrapper(result, sbw);
    }

    @Test
    public void test_Ser_Des_wrapper_utf16() throws IOException {
        StringBuilderWrapper sbw = buildStringBuilderWrapper("c12测试");
        Object result = doEncodeNDecode(sbw, factory, factory);

        assertForStringBuilderWrapper(result, sbw);
    }

    @Test
    public void test_Ser_Des() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("c12");

        Object result = doEncodeNDecode(sb, factory, factory);

        assertForStringBuilder(result, sb);
    }

    @Test
    public void test_Ser_Des_utf16() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("c12测试");
        Object result = doEncodeNDecode(sb, factory, factory);
        assertForStringBuilder(result, sb);
    }

    @Test
    public void test_Ser_Des_wrapper_long() throws IOException {
        StringBuilderWrapper sbw = buildStringBuilderWrapper("100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc");

        Object result = doEncodeNDecode(sbw, factory, factory);

        assertForStringBuilderWrapper(result, sbw);
    }

    @Test
    public void test_Ser_Des_wrapper_utf16_long() throws IOException {
        StringBuilderWrapper sbw = buildStringBuilderWrapper("100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc用来测试的数据");
        Object result = doEncodeNDecode(sbw, factory, factory);

        assertForStringBuilderWrapper(result, sbw);
    }

    @Test
    public void test_Ser_Des_long() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc");

        Object result = doEncodeNDecode(sb, factory, factory);

        assertForStringBuilder(result, sb);
    }

    @Test
    public void test_Ser_Des_utf16_long() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc用来测试的数据");
        Object result = doEncodeNDecode(sb, factory, factory);
        assertForStringBuilder(result, sb);
    }

    protected Object doEncodeNDecode(Object data, SerializerFactory ser, SerializerFactory des) throws IOException {
        os.reset();
        Hessian2Output out = new Hessian2Output(os);
        out.setSerializerFactory(ser);
        out.writeObject(data);
        out.flush();

        byte[] res = os.toByteArray();

        os.reset();
        ByteArrayInputStream is = new ByteArrayInputStream(res);
        Hessian2Input input = new Hessian2Input(is);
        input.setSerializerFactory(des);
        return input.readObject();
    }

    private byte[] convertStringToByteArray(String str) {
        String[] strArray = str.split(",");
        byte[] byteArray = new byte[strArray.length];

        for (int i = 0; i < strArray.length; i++) {
            byteArray[i] = Byte.parseByte(strArray[i]);
        }

        return byteArray;
    }

    /**
     * check if the environment is java 17 or beyond
     *
     * @return if on java 17
     */
    private static boolean isJava17() {
        String javaVersion = System.getProperty("java.specification.version");
        return Double.valueOf(javaVersion) >= 17;
    }

    /**
     * StringBuilderWrapper with str and str builder as test data
     * @param testData
     * @return
     */
    private StringBuilderWrapper buildStringBuilderWrapper(String testData) {
        StringBuilder sb = new StringBuilder();
        sb.append(testData);
        StringBuilderWrapper sbw = new StringBuilderWrapper();
        sbw.setStr(sb.toString());
        sbw.setStringBuilder(sb);
        return sbw;
    }

    /**
     *
     * @param result
     * @param expected
     */
    private void assertForStringBuilderWrapper(Object result, StringBuilderWrapper expected) {
        Assert.assertTrue(result instanceof StringBuilderWrapper);

        StringBuilderWrapper sbwResult = (StringBuilderWrapper) result;

        Assert.assertEquals(expected.stringBuilder.toString(), sbwResult.stringBuilder.toString());
        Assert.assertEquals(expected.str, sbwResult.str);
    }

    private void assertForStringBuilder(Object result, StringBuilder expected) {
        Assert.assertTrue(result instanceof StringBuilder);

        StringBuilder sbResult = (StringBuilder) result;

        Assert.assertEquals(expected.toString(), sbResult.toString());
    }
}