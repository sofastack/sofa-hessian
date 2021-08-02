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
package com.alipay.hessian;

import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.alipay.hessian.generic.model.GenericObject;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author zhaowang
 * @version : GenericObjectSerializerTest.java, v 0.1 2021年08月02日 12:07 下午 zhaowang
 */
public class GenericObjectSerializerTest {
    @Test
    public void testGenericObjectSerializer() throws IOException {
        System.setProperty("generic_hessian_write_definition_everytime", "true");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(outputStream);
        GenericSerializerFactory factory = new GenericSerializerFactory();
        output.setSerializerFactory(factory);

        GenericObject go1 = new GenericObject("com.abc");
        StringBuffer buf = new StringBuffer();
        // refDef + 8140 < 1024*8 - 32 （do not flush at writeString phase, and flush when write def at the second time）
        for (int i = 0; i < 8140; i++) {
            buf.append("V");
        }
        go1.putField("field_256", buf.toString());

        GenericObject go2 = new GenericObject("com.alipay.sofa.generic.object.bug.demo");
        go2.putField("field2", "str");

        go1.putField("go2", go2);

        output.writeObject(go1);
        output.close();

        byte[] bytes = outputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Hessian2Input hessianInput = new Hessian2Input(byteArrayInputStream);
        Object o = hessianInput.readObject();
    }
}