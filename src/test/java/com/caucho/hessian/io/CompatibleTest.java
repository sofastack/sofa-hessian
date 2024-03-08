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
package com.caucho.hessian.io;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author lo1nt
 * @version CompatibleTest.java, v 0.1 2024-03-08 16:58 lo1nt Exp $
 */
public class CompatibleTest {

    Throwable                            t = null;
    private static ByteArrayOutputStream os;
    private static SerializerFactory     factory;

    @BeforeClass
    public static void setUp() {
        factory = new TestSerializeFactory();
        os = new ByteArrayOutputStream();
    }

    {
        Throwable x = null;
        try {
            x.getStackTrace();
        } catch (NullPointerException e) {
            t = e;
        }
    }

    @Test
    public void TestCreateCompatibleClass() {
        try {
            ThrowableSerializer ct = new ThrowableSerializer(RuntimeException.class);
            StackTraceElementDeserializer cs = new StackTraceElementDeserializer();
        } catch (Throwable t) {
            Assert.fail("should be no error");
        }
    }

    @Test
    public void TestSerialize() throws IOException {
        os.reset();
        Hessian2Output output = new Hessian2Output(os);

        output.setSerializerFactory(factory);
        output.writeObject(t);
        output.flush();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        Hessian2Input input = new Hessian2Input(is);
        input.setSerializerFactory(factory);
        Throwable actual = (Throwable) input.readObject();
        Assert.assertEquals(t.getStackTrace().length, actual.getStackTrace().length);
        Assert.assertEquals(t.getMessage(), actual.getMessage());
    }

    private static class TestSerializeFactory extends SerializerFactory {
        @Override
        public Serializer getSerializer(Class cl) throws HessianProtocolException {
            if (Throwable.class.isAssignableFrom(cl)) {
                return new ThrowableSerializer(cl);
            }

            return super.getSerializer(cl);
        }

        @Override
        public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
            if (StackTraceElement.class.isAssignableFrom(cl)) {
                return new StackTraceElementDeserializer();
            }
            return super.getDeserializer(cl);
        }
    }
}
