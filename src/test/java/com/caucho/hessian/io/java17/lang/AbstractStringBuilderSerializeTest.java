/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
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
import java.util.Arrays;

/**
 *
 * @author junyuan
 * @version AbstractStringBuilderSerializeTest.java, v 0.1 2023年10月20日 10:07 junyuan Exp $
 */
public class AbstractStringBuilderSerializeTest {

    private static SerializerFactory     factory;
    private static SerializerFactory     jdk17Factory;
    private static ByteArrayOutputStream os;

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        jdk17Factory = new StringBuilderJDK17SerializeFactory();

        os = new ByteArrayOutputStream();
    }

    @Test
    public void compatibleTest_case1_no_warpper() {
        StringBuilder sb = new StringBuilder();
        sb.append("test");

        byte[] resJdk8 = new byte[0];
        byte[] resJdk17 = new byte[0];

        try {
            resJdk8 = doEncodeWithStringBuilder(sb, factory);
            resJdk17 = doEncodeWithStringBuilder(sb, jdk17Factory);
        } catch (Throwable e) {
            e.printStackTrace();
            Assert.fail();
        }

        boolean result = Arrays.equals(resJdk8, resJdk17);
        Assert.assertTrue(result);
    }

    /**
     * short string with length lt 16
     */
    @Test
    public void compatibleTest_case1() {
        StringBuilder sb = new StringBuilder();
        sb.append("test");
        StringBuilderWrapper sbw = new StringBuilderWrapper();
        sbw.setStringBuilder(sb);
        sbw.setStr(sb.toString());

        byte[] resJdk8 = new byte[0];
        byte[] resJdk17 = new byte[0];

        try {
            resJdk8 = doEncodeWithStringBuilder(sbw, factory);
            resJdk17 = doEncodeWithStringBuilder(sbw, jdk17Factory);
        } catch (Throwable e) {
            e.printStackTrace();
            Assert.fail();
        }

        boolean result = Arrays.equals(resJdk8, resJdk17);
        Assert.assertTrue(result);
    }

    /**
     * long string with length gt 31
     */
    @Test
    public void compatibleTest_case2() {
        StringBuilder sb = new StringBuilder();
        sb.append("100000000020000000003000000000400000000050000000006000000000");
        StringBuilderWrapper sbw = new StringBuilderWrapper();
        sbw.setStringBuilder(sb);
        sbw.setStr(sb.toString());

        byte[] resJdk8 = new byte[0];
        byte[] resJdk17 = new byte[0];

        try {
            resJdk8 = doEncodeWithStringBuilder(sbw, factory);
            resJdk17 = doEncodeWithStringBuilder(sbw, jdk17Factory);
        } catch (Throwable e) {
            e.printStackTrace();
            Assert.fail();
        }

        Assert.assertTrue(resJdk8.length > 0);

        boolean result = Arrays.equals(resJdk8, resJdk17);
        Assert.assertTrue(result);
    }

    @Test
    public void test_Ser17_Des8_short() throws IOException {
        String testData = "c12";

        StringBuilder sb = new StringBuilder();
        sb.append(testData);

        Object result = doEncodeNDecode(sb, jdk17Factory, factory);

        Assert.assertTrue(result instanceof StringBuilder);

        StringBuilder sbResult = (StringBuilder) result;

        Assert.assertEquals(testData, sbResult.toString());

    }


    @Test
    public void test_Ser17_Des8_Long() throws IOException {
        String testData = "100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc";

        StringBuilder sb = new StringBuilder();
        sb.append(testData);

        Object result = doEncodeNDecode(sb, jdk17Factory, factory);

        Assert.assertTrue(result instanceof StringBuilder);

        StringBuilder sbResult = (StringBuilder) result;

        Assert.assertEquals(testData, sbResult.toString());

    }

    @Test
    public void test_Wrapper_Ser17_Des8_Short() throws IOException {
        String testData = "100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc";
        StringBuilder sb = new StringBuilder();
        sb.append(testData);

        StringBuilderWrapper sbw = new StringBuilderWrapper();
        sbw.setStr(sb.toString());
        sbw.setStringBuilder(sb);

        Object result = doEncodeNDecode(sbw, jdk17Factory, factory);

        Assert.assertTrue(result instanceof StringBuilderWrapper);

        StringBuilderWrapper sbwResult = (StringBuilderWrapper) result;

        Assert.assertEquals(testData, sbwResult.stringBuilder.toString());
        Assert.assertEquals(testData, sbwResult.str);

    }

    @Test
    public void test_Wrapper_Ser17_Des8_long() throws IOException {
        String testData = "100000000020000000003000000000400000000050000000006000000000aaaaaabbbbbbcccccc";
        StringBuilder sb = new StringBuilder();
        sb.append(testData);

        StringBuilderWrapper sbw = new StringBuilderWrapper();
        sbw.setStr(sb.toString());
        sbw.setStringBuilder(sb);

        Object result = doEncodeNDecode(sbw, jdk17Factory, factory);

        Assert.assertTrue(result instanceof StringBuilderWrapper);

        StringBuilderWrapper sbwResult = (StringBuilderWrapper) result;

        Assert.assertEquals(testData, sbwResult.stringBuilder.toString());
        Assert.assertEquals(testData, sbwResult.str);

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


    protected byte[] doEncodeWithStringBuilder(Object data, SerializerFactory factory)
            throws IOException {
        os.reset();
        Hessian2Output out = new Hessian2Output(os);
        out.setSerializerFactory(factory);
        out.writeObject(data);
        out.flush();

        return os.toByteArray();
    }

    protected Object doDecodeWithStringBuilderWrapper(StringBuilderWrapper sbw, SerializerFactory factory) {
        return null;
    }

}