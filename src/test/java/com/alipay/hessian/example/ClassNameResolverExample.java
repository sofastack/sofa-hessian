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

import com.alipay.hessian.ClassNameFilter;
import com.alipay.hessian.ClassNameResolver;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by zhanggeng on 2018/4/3.
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public class ClassNameResolverExample {

    public static void main(String[] args) throws IOException {
        // Initial SerializerFactory
        // It is highly recommended to cache this factory for every serialization and deserialization.
        SerializerFactory serializerFactory = new SerializerFactory();

        // Do serializer
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            Hessian2Output hout = new Hessian2Output(bout);
            hout.setSerializerFactory(serializerFactory);
            hout.writeObject(new Socket()); // will throw an exception because java.net.Socket is in the serialize blacklist
            hout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Do deserializer
            ClassNameResolver resolver = new ClassNameResolver();
            resolver.addFilter(new ClassNameFilter() {
                @Override
                public int order() {
                    return 0;
                }

                @Override
                public String resolve(String className) throws IOException {
                    if (className.equals(Thread.class.getName())) {
                        throw new IOException("Thread is not allowed.");
                    }
                    return className;
                }
            });
            serializerFactory.setClassNameResolver(resolver); // replace default ClassNameResolver

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            Hessian2Output hout = new Hessian2Output(bout);
            hout.setSerializerFactory(serializerFactory);
            hout.writeObject(new Thread()); // will throw an exception because java.net.Thread is not allowed
            hout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
