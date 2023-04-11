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
package com.caucho.hessian.test.throwable;

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.io.StackTraceElementSerializer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author junyuan
 * @version SerializeCompatibleTest.java, v 0.1 2023年04月10日 14:52 junyuan Exp $
 */
public class SerializeCompatibleTest {

    private static SerializerFactory     originFactory;
    private static SerializerFactory     factory;
    private static ByteArrayOutputStream os;

    Throwable                            t = null;
    {
        t = new EnumConstantNotPresentException(MeaninglessEnum.class, "CIG");
    }

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        originFactory = new SerializeFactoryWithoutThrowable();

        os = new ByteArrayOutputStream();
    }

    @AfterClass
    public static void tearDown() {
        try {
            Field field = SerializerFactory.class.getDeclaredField("_staticSerializerMap");
            field.setAccessible(true);
            ConcurrentMap map = (ConcurrentMap) field.get(SerializerFactory.class);
            StackTraceElementSerializer serializer = new StackTraceElementSerializer();
            map.put(StackTraceElement.class, serializer);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * result byte should be in same with former version so as to behaving compatible
     * @throws IOException
     */
    @Test
    public void test_EnumConstantNotPresentExceptionSerialize() throws IOException {
        byte[] caseOrigin = serializeEnumConstantNotPresentException(originFactory);
        byte[] caseNew = serializeEnumConstantNotPresentException(factory);
        Assert.assertTrue(bytesEquals(caseOrigin, caseNew));
    }

    @Test
    public void test_EnumConstantNotPresentExceptionWrapperSerialize() throws IOException {
        byte[] wrapperCaseOrigin = serializeEnumConstantNotPresentExceptionWrapper(originFactory);
        byte[] wrapperCaseNew = serializeEnumConstantNotPresentExceptionWrapper(factory);
        Assert.assertTrue(bytesEquals(wrapperCaseOrigin, wrapperCaseNew));
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
