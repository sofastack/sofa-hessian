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
 * decode test
 * data encoded with hessian in jdk17
 *
 * 确保 jdk17 下的序列化结果能被 jdk8 以及 jdk17 下的反序列化器正确解析
 *
 * @author junyuan
 * @version Jdk8AbstractStringBuilderDecodeTest.java, v 0.1 2023年11月10日 14:09 junyuan Exp $
 */
public class Jdk8AbstractStringBuilderDecodeTest {

    private static SerializerFactory     factory;
    private static ByteArrayOutputStream os;

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();

        os = new ByteArrayOutputStream();
    }

    @Test
    public void test_Ser17_Des8_short() throws IOException {
        String testData = "c12";
        String testDataInHessianBytes = "79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-109,5,99,111,100,101,114,5,99,111,117,110,116,5,118,97,108,117,101,111,-112,-112,-109,16,99,49,50";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);
        Object result = doDecode(input, factory);

        Assert.assertTrue(result instanceof StringBuilder);

        StringBuilder sbResult = (StringBuilder) result;

        Assert.assertEquals(testData, sbResult.toString());
    }

    /**
     * input byte[] from jdk 17
     * @throws IOException
     */
    @Test
    public void test_Ser17_Des8_Long() throws IOException {
        String testData = "100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc";
        String testDataInHessianBytes = "79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-109,5,99,111,100,101,114,5,99,111,117,110,116,5,118,97,108,117,101,111,-112,-112,-56,78,83,0,78,49,48,48,48,48,48,48,48,48,48,50,48,48,48,48,48,48,48,48,48,51,48,48,48,48,48,48,48,48,48,52,48,48,48,48,48,48,48,48,48,53,48,48,48,48,48,48,48,48,48,54,48,48,48,48,48,48,48,48,48,97,97,97,97,97,97,98,98,98,98,98,98,99,99,99,99,99,99";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);
        Object result = doDecode(input, factory);

        Assert.assertTrue(result instanceof StringBuilder);

        StringBuilder sbResult = (StringBuilder) result;

        Assert.assertEquals(testData, sbResult.toString());
    }

    /**
     * input byte[] from jdk 17
     * complex data structure
     * @throws IOException
     */
    @Test
    public void test_Wrapper_Ser17_Des8_Short() throws IOException {
        String testData = "c12";
        StringBuilderWrapper sbw = buildStringBuilderWrapper(testData);

        String testDataInHessianBytes = "79,-56,54,99,111,109,46,99,97,117,99,104,111,46,104,101,115,115,105,97,110,46,105,111,46,106,97,118,97,49,55,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,87,114,97,112,112,101,114,-110,13,115,116,114,105,110,103,66,117,105,108,100,101,114,3,115,116,114,111,-112,79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-109,5,99,111,100,101,114,5,99,111,117,110,116,5,118,97,108,117,101,111,-111,-112,-109,16,99,49,50,0,0,0,0,0,0,0,0,0,0,0,0,0,3,99,49,50";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);

        Object result = doDecode(input, factory);

        assertForStringBuilderWrapper(result, sbw);
    }

    @Test
    public void test_Wrapper_Ser17_Des8_long() throws IOException {
        String testData = "100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc";
        StringBuilderWrapper sbw = buildStringBuilderWrapper(testData);

        String testDataInHessianBytes = "79,-56,54,99,111,109,46,99,97,117,99,104,111,46,104,101,115,115,105,97,110,46,105,111,46,106,97,118,97,49,55,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,87,114,97,112,112,101,114,-110,13,115,116,114,105,110,103,66,117,105,108,100,101,114,3,115,116,114,111,-112,79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-109,5,99,111,100,101,114,5,99,111,117,110,116,5,118,97,108,117,101,111,-111,-112,-56,78,83,0,78,49,48,48,48,48,48,48,48,48,48,50,48,48,48,48,48,48,48,48,48,51,48,48,48,48,48,48,48,48,48,52,48,48,48,48,48,48,48,48,48,53,48,48,48,48,48,48,48,48,48,54,48,48,48,48,48,48,48,48,48,97,97,97,97,97,97,98,98,98,98,98,98,99,99,99,99,99,99,83,0,78,49,48,48,48,48,48,48,48,48,48,50,48,48,48,48,48,48,48,48,48,51,48,48,48,48,48,48,48,48,48,52,48,48,48,48,48,48,48,48,48,53,48,48,48,48,48,48,48,48,48,54,48,48,48,48,48,48,48,48,48,97,97,97,97,97,97,98,98,98,98,98,98,99,99,99,99,99,99";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);

        Object result = doDecode(input, factory);

        assertForStringBuilderWrapper(result, sbw);
    }

    @Test
    public void test_Ser17_Des8_short_utf16() throws IOException {
        String testData = "c12测试";
        String testDataInHessianBytes = "79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-109,5,99,111,100,101,114,5,99,111,117,110,116,5,118,97,108,117,101,111,-112,-111,-107,16,99,49,50,-26,-75,-117,-24,-81,-107,0,0,0,0,0,0,0,0,0,0,0";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);
        Object result = doDecode(input, factory);

        Assert.assertTrue(result instanceof StringBuilder);

        StringBuilder sbResult = (StringBuilder) result;

        Assert.assertEquals(testData, sbResult.toString());
    }

    /**
     * input byte[] from jdk 17
     * @throws IOException
     */
    @Test
    public void test_Ser17_Des8_Long_utf16() throws IOException {
        String testData = "100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc用来测试的数据";
        String testDataInHessianBytes = "79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-109,5,99,111,100,101,114,5,99,111,117,110,116,5,118,97,108,117,101,111,-112,-111,-56,85,83,0,85,49,48,48,48,48,48,48,48,48,48,50,48,48,48,48,48,48,48,48,48,51,48,48,48,48,48,48,48,48,48,52,48,48,48,48,48,48,48,48,48,53,48,48,48,48,48,48,48,48,48,54,48,48,48,48,48,48,48,48,48,97,97,97,97,97,97,98,98,98,98,98,98,99,99,99,99,99,99,-25,-108,-88,-26,-99,-91,-26,-75,-117,-24,-81,-107,-25,-102,-124,-26,-107,-80,-26,-115,-82";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);
        Object result = doDecode(input, factory);

        Assert.assertTrue(result instanceof StringBuilder);

        StringBuilder sbResult = (StringBuilder) result;

        Assert.assertEquals(testData, sbResult.toString());
    }

    @Test
    public void test_Ser17_Des8_short_wrapper_utf16() throws IOException {
        String testData = "c12测试";
        StringBuilderWrapper sbw = buildStringBuilderWrapper(testData);

        String testDataInHessianBytes = "79,-56,54,99,111,109,46,99,97,117,99,104,111,46,104,101,115,115,105,97,110,46,105,111,46,106,97,118,97,49,55,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,87,114,97,112,112,101,114,-110,13,115,116,114,105,110,103,66,117,105,108,100,101,114,3,115,116,114,111,-112,79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-109,5,99,111,100,101,114,5,99,111,117,110,116,5,118,97,108,117,101,111,-111,-111,-107,16,99,49,50,-26,-75,-117,-24,-81,-107,0,0,0,0,0,0,0,0,0,0,0,5,99,49,50,-26,-75,-117,-24,-81,-107";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);
        Object result = doDecode(input, factory);

        assertForStringBuilderWrapper(result, sbw);
    }

    /**
     * input byte[] from jdk 17
     * @throws IOException
     */
    @Test
    public void test_Ser17_Des8_Long_wrapper_utf16() throws IOException {
        String testData = "100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc用来测试的数据";
        StringBuilderWrapper sbw = buildStringBuilderWrapper(testData);

        String testDataInHessianBytes = "79,-56,54,99,111,109,46,99,97,117,99,104,111,46,104,101,115,115,105,97,110,46,105,111,46,106,97,118,97,49,55,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,87,114,97,112,112,101,114,-110,13,115,116,114,105,110,103,66,117,105,108,100,101,114,3,115,116,114,111,-112,79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-109,5,99,111,100,101,114,5,99,111,117,110,116,5,118,97,108,117,101,111,-111,-111,-56,85,83,0,85,49,48,48,48,48,48,48,48,48,48,50,48,48,48,48,48,48,48,48,48,51,48,48,48,48,48,48,48,48,48,52,48,48,48,48,48,48,48,48,48,53,48,48,48,48,48,48,48,48,48,54,48,48,48,48,48,48,48,48,48,97,97,97,97,97,97,98,98,98,98,98,98,99,99,99,99,99,99,-25,-108,-88,-26,-99,-91,-26,-75,-117,-24,-81,-107,-25,-102,-124,-26,-107,-80,-26,-115,-82,83,0,85,49,48,48,48,48,48,48,48,48,48,50,48,48,48,48,48,48,48,48,48,51,48,48,48,48,48,48,48,48,48,52,48,48,48,48,48,48,48,48,48,53,48,48,48,48,48,48,48,48,48,54,48,48,48,48,48,48,48,48,48,97,97,97,97,97,97,98,98,98,98,98,98,99,99,99,99,99,99,-25,-108,-88,-26,-99,-91,-26,-75,-117,-24,-81,-107,-25,-102,-124,-26,-107,-80,-26,-115,-82";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);
        Object result = doDecode(input, factory);

        assertForStringBuilderWrapper(result, sbw);
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
}