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
package com.alipay.hessian.example;

import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.alipay.hessian.generic.model.GenericArray;
import com.alipay.hessian.generic.model.GenericObject;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by zhanggeng on 2018/4/3.
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public class GenericExample {

    public static void main(String[] args) throws IOException {
        // Initial SerializerFactory
        // It is highly recommended to cache this factory for every serialization and deserialization.
        SerializerFactory serializerFactory = new SerializerFactory();
        GenericSerializerFactory genericSerializerFactory = new GenericSerializerFactory();

        // define generic object
        GenericObject genericObject = new GenericObject("com.alipay.hessian.example.TestObj");
        genericObject.putField("name", "xxx");
        genericObject.putField("age", 25);
        GenericArray nickList = new GenericArray("java.lang.String");
        nickList.setObjects(new String[] { "xa", "xb", "xc" });
        genericObject.putField("nickNames", nickList);

        // Do serializer
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(genericSerializerFactory); // set to genericSerializerFactory
        hout.writeObject(genericObject);
        hout.close();
        byte[] data = bout.toByteArray();

        // deserializer to native TestObj
        ByteArrayInputStream bin = new ByteArrayInputStream(data, 0, data.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(serializerFactory);
        TestObj dst = (TestObj) hin.readObject();
        hin.close();
        System.out.println(dst.getName());
        System.out.println(dst.getAge());
        System.out.println(dst.getNickNames());
    }
}
