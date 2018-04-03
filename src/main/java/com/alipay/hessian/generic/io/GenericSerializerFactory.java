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
package com.alipay.hessian.generic.io;

import com.alipay.hessian.generic.model.*;
import com.alipay.hessian.generic.util.ClassFilter;
import com.caucho.hessian.io.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <a href="mailto:caojie.cj@antfin.com">Jie Cao</a>
 * @since 5.3.0
 */
public class GenericSerializerFactory extends SerializerFactory {

    private static final ConcurrentMap<String, Deserializer> deserializerMap = new ConcurrentHashMap<String, Deserializer>();

    private static final char                                ARRAY_PREFIX    = '[';

    private List<AbstractGenericSerializerFactory>           factories       = new CopyOnWriteArrayList<AbstractGenericSerializerFactory>();

    /**
     * Adds a factory.
     */
    public void addGenericFactory(AbstractGenericSerializerFactory factory) {
        factories.add(factory);
        super.addFactory(factory);
    }

    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        if (GenericObject.class == cl) {
            return GenericObjectSerializer.getInstance();
        }

        if (GenericArray.class == cl || GenericObject[].class == cl) {
            return GenericArraySerializer.getInstance();
        }

        if (GenericCollection.class == cl) {
            return GenericCollectionSerializer.getInstance();
        }

        if (GenericMap.class == cl) {
            return GenericMapSerializer.getInstance();
        }

        if (GenericClass.class == cl) {
            return GenericClassSerializer.getInstance();
        }

        return super.getSerializer(cl);
    }

    /**
     * 重写此方法主要是为了保证类不实现Serializable接口也可序列化
     *
     * @param cl Class
     * @return Serializer
     */
    protected Serializer getDefaultSerializer(Class cl) {
        if (_defaultSerializer != null)
            return _defaultSerializer;

        return new JavaSerializer(cl);
    }

    @Override
    public Deserializer getDeserializer(String type) throws HessianProtocolException {

        // 如果类型在过滤列表, 说明是jdk自带类, 直接委托父类处理
        if (isBlank(type) || ClassFilter.filterExcludeClass(type)) {
            return super.getDeserializer(type);
        }

        // 如果是数组类型, 且在name过滤列表, 说明jdk类, 直接委托父类处理
        if (type.charAt(0) == ARRAY_PREFIX && ClassFilter.arrayFilter(type)) {
            return super.getDeserializer(type);
        }

        // 查看是否已经包含反序列化器
        Deserializer deserializer = deserializerMap.get(type);
        if (deserializer != null) {
            return deserializer;
        }

        // 检查factories列表能否处理此type
        for (AbstractGenericSerializerFactory factory : factories) {
            deserializer = factory.getDeserializer(type);
            if (deserializer != null) {
                deserializerMap.put(type, deserializer);
                return deserializer;
            }
        }

        // 新建反序列化器, 如果是java.lang.Class使用GenericClassDeserializer,否则使用GenericDeserializer
        if (ClassFilter.CLASS_NAME.equals(type)) {
            deserializer = GenericClassDeserializer.getInstance();
        } else {
            deserializer = new GenericDeserializer(type);
        }

        deserializerMap.put(type, deserializer);
        return deserializer;
    }

    private boolean isBlank(String str) {
        return str == null || str.length() == 0;
    }
}
