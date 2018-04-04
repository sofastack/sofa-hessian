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
package com.alipay.hessian.generic.test;

import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by qiwei.lqw on 2016/7/7.
 */
public class TestPerformance {
    private static ComplexDataGenerator dg         = new ComplexDataGenerator();

    private static final int            TEST_TIMES = 100000;

    @Test
    public void testHessian2Serialize() throws IOException {
        Object person = dg.generateMixObject();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < TEST_TIMES; ++i) {
            bout.reset();
            Hessian2Output hout = new Hessian2Output(bout);
            hout.setSerializerFactory(new SerializerFactory());
            hout.writeObject(person);
            hout.close();
        }
        long endTime = System.currentTimeMillis();

        bout.close();

        System.out.println("time of hessian2 serialization: " + (endTime - startTime) / 1000);

    }

    @Test
    public void testHessian2Deserialize() throws IOException {
        Object person = dg.generateMixObject();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());
        hout.writeObject(person);
        hout.close();

        byte[] body = bout.toByteArray();

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < TEST_TIMES; ++i) {
            ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
            Hessian2Input hin = new Hessian2Input(bin);
            hin.setSerializerFactory(new SerializerFactory());

            hin.readObject();

            hin.close();
            bin.close();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("time of hessian2 deserialization: " + (endTime - startTime) / 1000);

    }

    @Test
    public void testGenericHessianSerialize() throws IOException {
        Object person = dg.generateMixGenericObject();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < TEST_TIMES; ++i) {
            bout.reset();
            Hessian2Output hout = new Hessian2Output(bout);
            hout.setSerializerFactory(new GenericSerializerFactory());
            hout.writeObject(person);
            hout.close();
        }
        long endTime = System.currentTimeMillis();

        bout.close();

        System.out.println("time of generic hessian: " + (endTime - startTime) / 1000);

    }

    @Test
    public void testGenericHessianDeserialize() throws IOException {
        Object person = dg.generateMixObject();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(person);
        hout.close();

        byte[] body = bout.toByteArray();

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < TEST_TIMES; ++i) {
            ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
            Hessian2Input hin = new Hessian2Input(bin);
            hin.setSerializerFactory(new GenericSerializerFactory());

            hin.readObject();

            hin.close();
            bin.close();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("time of generic hessian deserialization: " + (endTime - startTime)
            / 1000);

    }
}
