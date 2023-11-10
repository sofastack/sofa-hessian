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

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 *  <ol>
 *    <li>确保新老两种序列化方式内容一致(由于类实现有变动, 无法保证 jdk17 和 jdk8 序列化结果完全一致)</li>
 *    <li>确保 jdk8 下的序列化结果能被 jdk8 下的反序列化器正确解析</li>
 *  </ol>
 *
 *
 * @author junyuan
 * @version AbstractStringBuilderEncodeTest.java, v 0.1 2023年10月20日 10:07 junyuan Exp $
 */
public class AbstractStringBuilderEncodeTest {

    private static SerializerFactory     factory;
    private static SerializerFactory     previousVersionFactory;
    private static ByteArrayOutputStream os;

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        previousVersionFactory = new StringBuilderJavaSerializeFactory();

        os = new ByteArrayOutputStream();
    }

    private void compatibleTest(Object inputData, SerializerFactory newFactory, SerializerFactory oldFactory) {
        byte[] resNew = new byte[0];
        byte[] resOld = new byte[0];

        try {
            resNew = doEncodeWithStringBuilder(inputData, newFactory);
            resOld = doEncodeWithStringBuilder(inputData, oldFactory);
        } catch (Throwable e) {
            e.printStackTrace();
            Assert.fail();
        }

        boolean result = Arrays.equals(resNew, resOld);
        Assert.assertTrue(result);
    }

    @Test
    public void compatibleTest_short_no_warpper() {
        StringBuilder sb = new StringBuilder();
        sb.append("test");

        compatibleTest(sb, factory, previousVersionFactory);
    }

    @Test
    public void compatibleTest_long_no_warpper() {
        StringBuilder sb = new StringBuilder();
        sb.append("100000000020000000003000000000400000000050000000006000000000");

        compatibleTest(sb, factory, previousVersionFactory);
    }

    /**
     * short string with length lt 16
     */
    @Test
    public void compatibleTest_short_wrapper() {
        StringBuilder sb = new StringBuilder();
        sb.append("test");
        StringBuilderWrapper sbw = new StringBuilderWrapper();
        sbw.setStringBuilder(sb);
        sbw.setStr(sb.toString());

        compatibleTest(sbw, factory, previousVersionFactory);
    }

    /**
     * long string with length gt 31
     */
    @Test
    public void compatibleTest_long_wrapper() {
        StringBuilder sb = new StringBuilder();
        sb.append("100000000020000000003000000000400000000050000000006000000000");
        StringBuilderWrapper sbw = new StringBuilderWrapper();
        sbw.setStringBuilder(sb);
        sbw.setStr(sb.toString());

        compatibleTest(sbw, factory, previousVersionFactory);
    }

    /**
     * UTF16
     */
    @Test
    public void compatibleTest_wrapper_short_utf16() {
        StringBuilder sb = new StringBuilder();
        sb.append("test测试");
        StringBuilderWrapper sbw = new StringBuilderWrapper();
        sbw.setStringBuilder(sb);
        sbw.setStr(sb.toString());

        compatibleTest(sbw, factory, previousVersionFactory);
    }

    /**
     * UTF16
     */
    @Test
    public void compatibleTest_wrapper_long_utf16() {
        StringBuilder sb = new StringBuilder();
        sb.append("aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeefffffffffftest测试");
        StringBuilderWrapper sbw = new StringBuilderWrapper();
        sbw.setStringBuilder(sb);
        sbw.setStr(sb.toString());

        compatibleTest(sbw, factory, previousVersionFactory);
    }

    protected byte[] doEncodeWithStringBuilder(Object data, SerializerFactory factory)
        throws IOException {
        os.reset();
        Hessian2Output out = new Hessian2Output(os);
        out.setSerializerFactory(factory);
        out.writeObject(data);
        out.flush();

        return os.toByteArray();
    }

    /*
     *打印byte[],以逗号分隔
     */
    private static void printBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            System.out.println("null");
            return;
        }

        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + ",");
        }
        System.out.println();
    }
}