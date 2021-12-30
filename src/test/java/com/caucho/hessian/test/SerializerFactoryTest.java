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
package com.caucho.hessian.test;

import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author zhaowang
 * @version : SerializerFactoryTest.java, v 0.1 2021年12月30日 2:35 下午 zhaowang
 */
public class SerializerFactoryTest {

    @Test
    public void getDeserializerByType() throws Exception {
        final SerializerFactory serializerFactory = new SerializerFactory();
        Field typeNotFoundMap = serializerFactory.getClass().getDeclaredField("_typeNotFoundMap");
        typeNotFoundMap.setAccessible(true);
        Map<String, Object> map = (Map<String, Object>) typeNotFoundMap.get(serializerFactory);

        final String testClassName = Color.class.getName();
        Deserializer d1 = serializerFactory.getDeserializer(testClassName);
        Assert.assertNotNull("TestClass Deserializer!", d1);

        String notExist = "com.test.NotExistClass";
        Assert.assertNull(map.get(notExist));

        Deserializer d2 = serializerFactory.getDeserializer(notExist);
        Assert.assertNull("NotExistClass Deserializer!", d2);
        Assert.assertNotNull(map.get(notExist));

        Deserializer d3 = serializerFactory.getDeserializer(notExist);
        Assert.assertNull("NotExistClass Deserializer!", d3);

    }
}