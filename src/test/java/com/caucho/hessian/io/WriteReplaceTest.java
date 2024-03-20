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
import java.io.Serializable;

/**
 *
 * @author junyuan
 * @version WriteReplaceTest.java, v 0.1 2024-03-20 10:34 junyuan Exp $
 */
public class WriteReplaceTest {
    private static SerializerFactory     factory;
    private static ByteArrayOutputStream os;

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        os = new ByteArrayOutputStream();
    }

    @Test
    public void TestWriteReplace() throws IOException {
        TestObject origin = new TestObject();
        origin.setName("testWR");

        os.reset();
        Hessian2Output output = new Hessian2Output(os);

        output.setSerializerFactory(factory);
        try {
            output.writeObject(origin);
        } catch (Exception e) {
            Assert.fail("should be no exception");
        }
        output.flush();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        Hessian2Input input = new Hessian2Input(is);
        input.setSerializerFactory(factory);
        TestObject actual = (TestObject) input.readObject();
        Assert.assertEquals(actual.name, origin.name);
    }

    @Test
    public void TestWrappedWriteReplace() throws IOException {
        WrappedTestObject origin = new WrappedTestObject();
        TestObject testObject = new TestObject();
        testObject.setName("testWR");
        origin.setTestObject(testObject);

        TestObjectWriteReplace wrObject = new TestObjectWriteReplace();
        wrObject.name = "testProxy";
        origin.setWrObject(wrObject);

        os.reset();
        Hessian2Output output = new Hessian2Output(os);

        output.setSerializerFactory(factory);
        try {
            output.writeObject(origin);
        } catch (Exception e) {
            Assert.fail("should be no exception");
        }
        output.flush();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        Hessian2Input input = new Hessian2Input(is);
        input.setSerializerFactory(factory);
        WrappedTestObject actual = (WrappedTestObject) input.readObject();
        Assert.assertEquals(actual.testObject.name, origin.testObject.name);
        Assert.assertEquals(actual.wrObject.name, origin.wrObject.name);
    }

    private static class WrappedTestObject implements Serializable {
        private TestObject             testObject;
        private TestObjectWriteReplace wrObject;

        public TestObject getTestObject() {
            return testObject;
        }

        public void setTestObject(TestObject testObject) {
            this.testObject = testObject;
        }

        public TestObjectWriteReplace getWrObject() {
            return wrObject;
        }

        public void setWrObject(TestObjectWriteReplace wrObject) {
            this.wrObject = wrObject;
        }
    }

    // write replace to TestObjectProxy
    private static class TestObjectWriteReplace implements Serializable {
        private static final long serialVersionUID = 462771763706189820L;

        String                    name;

        Object writeReplace() {
            TestObjectProxy o = new TestObjectProxy();
            o.name = this.name;
            return o;
        }
    }

    // read resolve back to TestObjectWriteReplace
    private static class TestObjectProxy implements Serializable {
        private static final long serialVersionUID = 462771763706189820L;

        String                    name;

        Object readResolve() {
            TestObjectWriteReplace o = new TestObjectWriteReplace();
            o.name = this.name;
            return o;
        }
    }

    private static class TestObject implements Serializable {
        private static final long serialVersionUID = -452701306050912437L;

        String                    name;

        Object writeReplace() {
            return this;
        }

        /**
         * Getter method for property <tt>name</tt>.
         *
         * @return property value of name
         */
        public String getName() {
            return name;
        }

        /**
         * Setter method for property <tt>name</tt>.
         *
         * @param name  value to be assigned to property name
         */
        public void setName(String name) {
            this.name = name;
        }
    }
}
