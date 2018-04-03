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
package com.alipay.hessian.generic.list;

import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.hessian.generic.util.CustomConverter;
import com.alipay.hessian.generic.util.GenericUtils;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;

public class MyListTest {

    @Test
    public void testAll() throws Exception {

        GenericSerializerFactory genericSerializerFactory = new GenericSerializerFactory();
        genericSerializerFactory.addGenericFactory(new MyListSerializeFactory());

        SerializerFactory serializerFactory = new SerializerFactory();
        serializerFactory.addFactory(new MyListSerializeFactory());

        MyList list = new MyList();
        list.setName("aaaaa");
        list.add(123);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(genericSerializerFactory);
        hout.writeObject(list);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(genericSerializerFactory);

        GenericObject o1 = (GenericObject) hin.readObject();
        GenericUtils.addCustomConverter(new CustomConverter() {
            @Override
            public Class interestClass() {
                return MyList.class;
            }

            @Override
            public Object convertToObject(Class clazz, GenericObject genericObject) {
                MyList myList = new MyList();
                myList.setName((String) genericObject.getField("name"));
                myList.addAll((Collection) genericObject.getField("_list_content"));
                return myList;
            }

            @Override
            public GenericObject convertToGenericObject(Class clazz, Object object) {
                GenericObject genericObject = new GenericObject(
                    "com.alipay.hessian.generic.list.MyList");
                MyList myList = (MyList) object;
                genericObject.putField("name", myList.getName());
                genericObject.putField("_list_content", new ArrayList(myList));
                return genericObject;
            }
        });
        MyList myList = (MyList) GenericUtils.convertToObject(o1);
        Assert.assertEquals("aaaaa", myList.getName());
        Assert.assertEquals(1, myList.size());
        Assert.assertEquals(123, myList.get(0));

        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(serializerFactory);

        myList = (MyList) hin.readObject();
        Assert.assertEquals("aaaaa", myList.getName());
        Assert.assertEquals(1, myList.size());
        Assert.assertEquals(123, myList.get(0));

        GenericObject genericObject = GenericUtils.convertToGenericObject(myList);
        Assert.assertEquals("aaaaa", genericObject.getField("name"));
        Assert.assertEquals("com.alipay.hessian.generic.list.MyList", genericObject.getType());
        Assert.assertEquals(new ArrayList(myList), genericObject.getField("_list_content"));
    }

}
