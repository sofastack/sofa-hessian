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
 *  <ol>
 *    <li>确保新老两种序列化方式内容一致(由于类实现有变动, 无法保证 jdk17 和 jdk8 序列化结果完全一致)</li>
 *    <li>确保 jdk8 下的序列化结果能被 jdk8 以及 jdk17 下的反序列化器正确解析</li>
 *    <li>确保 jdk17 下的序列化结果能被 jdk8 以及 jdk17 下的反序列化器正确解析</li>
 *  </ol>
 *
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
    public void compatibleTest_case_no_warpper() {
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
    public void test_Ser8_Des8() throws IOException {
        StringBuilderWrapper sbw = new StringBuilderWrapper();
        StringBuilder d = new StringBuilder();
        d.append("c12");
        sbw.setStr(d.toString());
        sbw.setStringBuilder(d);

        Object result = doEncodeNDecode(sbw, factory, factory);

        Assert.assertTrue(result instanceof StringBuilderWrapper);
        StringBuilderWrapper sbwResult = (StringBuilderWrapper) result;

        Assert.assertEquals(sbw.str, sbwResult.str);
        Assert.assertEquals(sbw.str, sbwResult.stringBuilder.toString());
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
        StringBuilder sb = new StringBuilder();
        sb.append(testData);
        StringBuilderWrapper sbw = new StringBuilderWrapper();
        sbw.setStr(sb.toString());
        sbw.setStringBuilder(sb);

        String testDataInHessianBytes = "79,-56,54,99,111,109,46,99,97,117,99,104,111,46,104,101,115,115,105,97,110,46,105,111,46,106,97,118,97,49,55,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,87,114,97,112,112,101,114,-110,13,115,116,114,105,110,103,66,117,105,108,100,101,114,3,115,116,114,111,-112,79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-109,5,99,111,100,101,114,5,99,111,117,110,116,5,118,97,108,117,101,111,-111,-112,-109,16,99,49,50,0,0,0,0,0,0,0,0,0,0,0,0,0,3,99,49,50";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);

        Object result = doDecode(input, factory);

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

        String testDataInHessianBytes = "79,-56,54,99,111,109,46,99,97,117,99,104,111,46,104,101,115,115,105,97,110,46,105,111,46,106,97,118,97,49,55,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,87,114,97,112,112,101,114,-110,13,115,116,114,105,110,103,66,117,105,108,100,101,114,3,115,116,114,111,-112,79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-109,5,99,111,100,101,114,5,99,111,117,110,116,5,118,97,108,117,101,111,-111,-112,-56,78,83,0,78,49,48,48,48,48,48,48,48,48,48,50,48,48,48,48,48,48,48,48,48,51,48,48,48,48,48,48,48,48,48,52,48,48,48,48,48,48,48,48,48,53,48,48,48,48,48,48,48,48,48,54,48,48,48,48,48,48,48,48,48,97,97,97,97,97,97,98,98,98,98,98,98,99,99,99,99,99,99,83,0,78,49,48,48,48,48,48,48,48,48,48,50,48,48,48,48,48,48,48,48,48,51,48,48,48,48,48,48,48,48,48,52,48,48,48,48,48,48,48,48,48,53,48,48,48,48,48,48,48,48,48,54,48,48,48,48,48,48,48,48,48,97,97,97,97,97,97,98,98,98,98,98,98,99,99,99,99,99,99";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);

        Object result = doDecode(input, factory);

        Assert.assertTrue(result instanceof StringBuilderWrapper);

        StringBuilderWrapper sbwResult = (StringBuilderWrapper) result;

        Assert.assertEquals(testData, sbwResult.stringBuilder.toString());
        Assert.assertEquals(testData, sbwResult.str);
    }

    public void test_Ser8_Des17_short() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("test");
        String testDataInHessianBytes = "79,-89,106,97,118,97,46,108,97,110,103,46,83,116,114,105,110,103,66,117,105,108,100,101,114,-110,5,99,111,117,110,116,5,118,97,108,117,101,111,-112,-108,16,116,101,115,116";
        byte[] input = convertStringToByteArray(testDataInHessianBytes);

        Object result = doDecode(input, factory);

        Assert.assertTrue(result instanceof StringBuilder);

        StringBuilder sbResult = (StringBuilder) result;

        Assert.assertEquals(sb.toString(), sbResult.toString());
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

    /*
     *打印byte[],以逗号分隔
     */
    private static void printBytes(byte[] bytes){
        if(bytes == null || bytes.length==0){
            System.out.println("null");
            return ;
        }

        for(int i=0;i<bytes.length;i++){
            System.out.print(bytes[i]+",");
        }
        System.out.println();
    }
}