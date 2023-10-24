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
package com.caucho.hessian.io.java17.lang;

import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.io.AbstractStringBuilderSerializer;

/**
 * 可以在 java8 环境下运行专门给 jdk17 使用的序列化器
 * 以便在 java8 下进行兼容测试
 * @author junyuan
 * @version StringBuilderJDK17SerializeFactory.java, v 0.1 2023年05月06日 11:13 junyuan Exp $
 */
public class StringBuilderJDK17SerializeFactory extends SerializerFactory {
    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        Serializer serializer = super.getSerializer(cl);

        if ( StringBuilder.class.isAssignableFrom(cl) || StringBuffer.class.isAssignableFrom(cl) ) {
            serializer = new AbstractStringBuilderSerializer(cl);
        }

        return serializer;
    }

    @Override
    public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
        Deserializer deserializer = super.getDeserializer(cl);

        // 只能在 jdk9 以上环境下测试
//        if ( StringBuilder.class.isAssignableFrom(cl) || StringBuffer.class.isAssignableFrom(cl) ) {
//            deserializer = new AbstractStringBuilderDeserializer(cl);
//        }

        _cachedDeserializerMap.put(cl, deserializer);
        return deserializer;
    }
}
