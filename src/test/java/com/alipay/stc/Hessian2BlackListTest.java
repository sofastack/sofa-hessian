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
package com.alipay.stc;

import com.alipay.hessian.ClassNameResolver;
import com.alipay.hessian.NameBlackListFilter;
import com.alipay.stc.bl.MockNameBlacklistFilter;
import com.alipay.stc.bl.TestBlackBean;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>自定义对象，数组，List，Map 命中黑名单的case</p>
 * 
 * @author <a href="mailto:zhanggeng.zg@antfin.com">GengZhang</a>
 */
public class Hessian2BlackListTest {

    static SerializerFactory serializerFactory;

    @BeforeClass
    public static void init() {
        NameBlackListFilter filter = new MockNameBlacklistFilter();
        ClassNameResolver resolver = new ClassNameResolver();
        resolver.addFilter(filter);
        serializerFactory = new SerializerFactory();
        serializerFactory.setClassNameResolver(resolver);
    }

    @Test
    public void testBeanSerialize() throws IOException {
        TestBlackBean blackBean = new TestBlackBean().setString("sss");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(output);
        hout.setSerializerFactory(serializerFactory);

        try {
            hout.writeObject(blackBean);
            hout.flush();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testBeanDeserialize() throws IOException {
        byte[] bs = new byte[] { 79, -81, 99, 111, 109, 46, 97, 108, 105, 112, 97, 121, 46, 115, 116, 99, 46, 98
                , 108, 46, 84, 101, 115, 116, 66, 108, 97, 99, 107, 66, 101, 97, 110,
                -111, 6, 115, 116, 114, 105, 110, 103, 111, -112, 3, 115, 115, 115 };

        ByteArrayInputStream input = new ByteArrayInputStream(bs, 0, bs.length);
        Hessian2Input hin = new Hessian2Input(input);
        hin.setSerializerFactory(serializerFactory);

        try {
            hin.readObject();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testListSerialize() throws IOException {
        TestBlackBean blackBean = new TestBlackBean().setString("sss");
        List list = new ArrayList<TestBlackBean>();
        list.add(blackBean);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(output);
        hout.setSerializerFactory(serializerFactory);

        try {
            hout.writeObject(list);
            hout.flush();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testListDeserialize() throws IOException {
        byte[] bs = new byte[] { 86, 110, 1, 79, -81, 99, 111, 109, 46, 97, 108, 105, 112, 97, 121, 46, 115,
                116, 99, 46, 98, 108, 46, 84, 101, 115, 116, 66, 108, 97, 99, 107, 66, 101, 97, 110, -111,
                6, 115, 116, 114, 105, 110, 103, 111, -112, 3, 115, 115, 115, 122 };

        ByteArrayInputStream input = new ByteArrayInputStream(bs, 0, bs.length);
        Hessian2Input hin = new Hessian2Input(input);
        hin.setSerializerFactory(serializerFactory);

        try {
            hin.readObject();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testArraySerialize() throws IOException {
        TestBlackBean blackBean = new TestBlackBean().setString("sss");
        Object[] array = new Object[] { blackBean };

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(output);
        hout.setSerializerFactory(serializerFactory);

        try {
            hout.writeObject(array);
            hout.flush();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testArrayDeserialize() throws IOException {
        byte[] bs = new byte[] { 86, 116, 0, 7, 91, 111, 98, 106, 101, 99, 116, 110, 1, 79, -81, 99, 111, 109, 46,
                97, 108, 105, 112, 97, 121, 46, 115, 116, 99, 46, 98, 108, 46, 84, 101, 115, 116, 66, 108, 97, 99,
                107, 66, 101, 97, 110, -111, 6, 115, 116, 114, 105, 110, 103, 111, -112, 3, 115, 115, 115, 122 };

        ByteArrayInputStream input = new ByteArrayInputStream(bs, 0, bs.length);
        Hessian2Input hin = new Hessian2Input(input);
        hin.setSerializerFactory(serializerFactory);

        try {
            hin.readObject();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testMapSerialize() throws IOException {
        TestBlackBean blackBean = new TestBlackBean().setString("sss");
        Map<TestBlackBean, TestBlackBean> map = new HashMap<TestBlackBean, TestBlackBean>();
        map.put(blackBean, blackBean);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(output);
        hout.setSerializerFactory(serializerFactory);

        try {
            hout.writeObject(map);
            hout.flush();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testMapDeserialize() throws IOException {
        byte[] bs = new byte[] { 77, 79, -81, 99, 111, 109, 46, 97, 108, 105, 112, 97, 121, 46, 115, 116, 99, 46, 98,
                108, 46, 84, 101, 115, 116, 66, 108, 97, 99, 107, 66, 101, 97, 110, -111, 6, 115, 116, 114, 105,
                110, 103, 111, -112, 3, 115, 115, 115, 74, 1, 122 };

        ByteArrayInputStream input = new ByteArrayInputStream(bs, 0, bs.length);
        Hessian2Input hin = new Hessian2Input(input);
        hin.setSerializerFactory(serializerFactory);

        try {
            hin.readObject();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }
}
