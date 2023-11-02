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
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * may only work under jdk17
 * @author junyuan
 * @version Jdk17AbstractStringBuilderTest.java, v 0.1 2023年10月25日 11:41 junyuan Exp $
 */
public class Jdk17AbstractStringBuilderTest {
    private static SerializerFactory     factory;
    private static SerializerFactory     jdk8Factory;
    private static ByteArrayOutputStream os;

    protected final static boolean       isHigherThanJdk17 = isJava17();

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        jdk8Factory = new StringBuilderJDK8SerializeFactory();

        os = new ByteArrayOutputStream();
    }

    @Test
    public void test_Ser8_Des17_short() throws IOException {
        if (!isJava17()) {
            System.out.println("not jdk17, skip test");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("test");
        String testDataInHessianBytes = "79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-110,5,99,111,117,110,116,5,118,97,108,117,101,111,-112,-108,16,116,101,115,116";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);

        Object result = doDecode(input, factory);

        Assert.assertTrue(result instanceof StringBuilder);

        StringBuilder sbResult = (StringBuilder) result;

        Assert.assertEquals(sb.toString(), sbResult.toString());
    }

    @Test
    public void test_Ser8_Des17_long() throws IOException {
        if (!isJava17()) {
            System.out.println("not jdk17, skip test");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc");
        String testDataInHessianBytes = "79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-110,5,99,111,117,110,116,5,118,97,108,117,101,111,-112,-56,78,83,0,78,49,48,48,48,48,48,48,48,48,48,50,48,48,48,48,48,48,48,48,48,51,48,48,48,48,48,48,48,48,48,52,48,48,48,48,48,48,48,48,48,53,48,48,48,48,48,48,48,48,48,54,48,48,48,48,48,48,48,48,48,97,97,97,97,97,97,98,98,98,98,98,98,99,99,99,99,99,99";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);

        Object result = doDecode(input, factory);

        Assert.assertTrue(result instanceof StringBuilder);

        StringBuilder sbResult = (StringBuilder) result;

        Assert.assertEquals(sb.toString(), sbResult.toString());
    }

    @Test
    public void test_wrapper_Ser8_Des17_short() throws IOException {
        if (!isJava17()) {
            System.out.println("not jdk17, skip test");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("test");
        StringBuilderWrapper sbw = new StringBuilderWrapper();
        sbw.setStr(sb.toString());
        sbw.setStringBuilder(sb);

        String testDataInHessianBytes = "79,-56,54,99,111,109,46,99,97,117,99,104,111,46,104,101,115,115,105,97,110,46,105,111,46,106,97,118,97,49,55,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,87,114,97,112,112,101,114,-110,13,115,116,114,105,110,103,66,117,105,108,100,101,114,3,115,116,114,111,-112,79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-110,5,99,111,117,110,116,5,118,97,108,117,101,111,-111,-108,16,116,101,115,116,0,0,0,0,0,0,0,0,0,0,0,0,4,116,101,115,116";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);

        Object result = doDecode(input, factory);

        Assert.assertTrue(result instanceof StringBuilderWrapper);

        StringBuilderWrapper sbwResult = (StringBuilderWrapper) result;

        Assert.assertEquals(sb.toString(), sbwResult.getStringBuilder().toString());
        Assert.assertEquals(sb.toString(), sbwResult.getStr());
    }

    @Test
    public void test_wrapper_Ser8_Des17_long() throws IOException {
        if (!isJava17()) {
            System.out.println("not jdk17, skip test");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc");
        StringBuilderWrapper sbw = new StringBuilderWrapper();
        sbw.setStr(sb.toString());
        sbw.setStringBuilder(sb);

        String testDataInHessianBytes = "79,-56,54,99,111,109,46,99,97,117,99,104,111,46,104,101,115,115,105,97,110,46,105,111,46,106,97,118,97,49,55,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,87,114,97,112,112,101,114,-110,13,115,116,114,105,110,103,66,117,105,108,100,101,114,3,115,116,114,111,-112,79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-110,5,99,111,117,110,116,5,118,97,108,117,101,111,-111,-56,78,83,0,78,49,48,48,48,48,48,48,48,48,48,50,48,48,48,48,48,48,48,48,48,51,48,48,48,48,48,48,48,48,48,52,48,48,48,48,48,48,48,48,48,53,48,48,48,48,48,48,48,48,48,54,48,48,48,48,48,48,48,48,48,97,97,97,97,97,97,98,98,98,98,98,98,99,99,99,99,99,99,83,0,78,49,48,48,48,48,48,48,48,48,48,50,48,48,48,48,48,48,48,48,48,51,48,48,48,48,48,48,48,48,48,52,48,48,48,48,48,48,48,48,48,53,48,48,48,48,48,48,48,48,48,54,48,48,48,48,48,48,48,48,48,97,97,97,97,97,97,98,98,98,98,98,98,99,99,99,99,99,99";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);

        Object result = doDecode(input, factory);

        Assert.assertTrue(result instanceof StringBuilderWrapper);

        StringBuilderWrapper sbwResult = (StringBuilderWrapper) result;

        Assert.assertEquals(sb.toString(), sbwResult.getStringBuilder().toString());
        Assert.assertEquals(sb.toString(), sbwResult.getStr());
    }

    protected Object doDecode(byte[] data, SerializerFactory factory) throws IOException {
        os.reset();
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        Hessian2Input input = new Hessian2Input(is);
        input.setSerializerFactory(factory);
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
}