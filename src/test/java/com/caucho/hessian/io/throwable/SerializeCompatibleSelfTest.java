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

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
//import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * jdk17 下的throwable 序列化必定有损, 这个类仅用于自测, 实际运行必然不通过
 *
 * @author junyuan
 * @version SerializeCompatibleSelfTest.java, v 0.1 2023年04月10日 14:52 junyuan Exp $
 */
public class SerializeCompatibleSelfTest {

    private static SerializerFactory     useJdk17Factory;
    private static SerializerFactory     originFactory;
    private static SerializerFactory     factory;
    private static ByteArrayOutputStream os;

    private Method addSuppressed = null;
    private Method getSuppressed = null;
    {
        try {
            addSuppressed = Throwable.class.getMethod("addSuppressed", Throwable.class);
            getSuppressed = Throwable.class.getMethod("getSuppressed");
        } catch (Exception e) {

        }
    }

    Throwable                            t = null;
    {
        Throwable x = null;
        try {
            t.getStackTrace();
        } catch (NullPointerException e) {
            t = e;
        }

//        t = new EnumConstantNotPresentException(MeaninglessEnum.class, "CIG");

//        try {
//            addSuppressed.invoke(t, x);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    private static final boolean isLessThanJdk17 = isLessThanJdk17();

    private static boolean isLessThanJdk17() {
        String javaVersion = System.getProperty("java.specification.version");
        return Double.parseDouble(javaVersion) < 17;
    }

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        originFactory = new SerializeFactoryWithoutThrowable();
        // 可以在jdk8环境下使用到专门为jdk17准备的 serializer
        useJdk17Factory = new JDK17SerializeFactory();

        os = new ByteArrayOutputStream();
    }

    /**
     * result byte should be in same with former version so as to behaving compatible
     * @throws IOException
     */
//    @Test
    public void test_EnumConstantNotPresentExceptionSerialize() throws IOException {
        if (isLessThanJdk17) {
            // jdk 17 开始不需要执行这个, 反射受限, 执行会失败
            byte[] caseOrigin = serializeEnumConstantNotPresentException(originFactory);
            byte[] caseNew = serializeEnumConstantNotPresentException(factory);
            byte[] caseJdk17 = serializeEnumConstantNotPresentException(useJdk17Factory);
            Assert.assertTrue(bytesEquals(caseOrigin, caseNew));
            Assert.assertTrue(bytesEquals(caseJdk17, caseNew));
        }

    }

//    @Test
    public void test_EnumConstantNotPresentExceptionWrapperSerialize() throws IOException {
        if (isLessThanJdk17) {
            byte[] wrapperCaseOrigin = serializeEnumConstantNotPresentExceptionWrapper(originFactory);
            byte[] wrapperCaseNew = serializeEnumConstantNotPresentExceptionWrapper(factory);
            byte[] wrapperCaseJdk17 = serializeEnumConstantNotPresentExceptionWrapper(useJdk17Factory);
            Assert.assertTrue(bytesEquals(wrapperCaseOrigin, wrapperCaseNew));
            bytePrint(wrapperCaseJdk17);
            bytePrint(wrapperCaseOrigin);
            Assert.assertTrue(bytesEquals(wrapperCaseJdk17, wrapperCaseNew));
        }
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

    private void bytePrint(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + " ");
        }
        System.out.println();
    }

    private byte[] serializeEnumConstantNotPresentExceptionWrapper(SerializerFactory sf) throws IOException {
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper();

        exceptionWrapper.setT(t);

        os.reset();
        Hessian2Output wrapCaseOutput = new Hessian2Output(os);
        wrapCaseOutput.setSerializerFactory(sf);
        wrapCaseOutput.writeObject(exceptionWrapper);
        wrapCaseOutput.flush();
        byte[] wrappedBytes = os.toByteArray();
        return wrappedBytes;
    }

    private byte[] serializeEnumConstantNotPresentException(SerializerFactory sf) throws IOException {
        os.reset();
        Hessian2Output wrapCaseOutput = new Hessian2Output(os);
        wrapCaseOutput.setSerializerFactory(sf);
        wrapCaseOutput.writeObject(t);
        wrapCaseOutput.flush();
        byte[] wrappedBytes = os.toByteArray();
        return wrappedBytes;
    }

}
