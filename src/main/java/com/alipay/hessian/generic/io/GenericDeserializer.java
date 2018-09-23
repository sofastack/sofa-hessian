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

import com.alipay.hessian.generic.model.GenericArray;
import com.alipay.hessian.generic.model.GenericCollection;
import com.alipay.hessian.generic.model.GenericMap;
import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.hessian.generic.util.ClassFilter;
import com.caucho.hessian.io.*;

import java.io.IOException;
import java.util.*;

/**
 * 此类用于反序列化GenericObject/GenericMap/GenericCollection
 * Collection对象或者数组对象反序列化会调用readList/readLengthList方法,根据type类型返回GenericCollection/GenericArray
 * Map对象反序列化会调用readMap方法,返回结果为GenericMap
 * Object对象反序列会调用readObject方法,返回结果为GenericObject
 *
 * @author <a href="mailto:caojie.cj@antfin.com">Jie Cao</a>
 * @since 5.3.0
 */
public class GenericDeserializer extends AbstractDeserializer {

    private final String              type;
    private static final Deserializer DEFAULT_MAP_DESERIALIZER        = new MapDeserializer(
                                                                          HashMap.class);
    private static final Deserializer DEFAULT_COLLECTION_DESERIALIZER = new CollectionDeserializer(
                                                                          ArrayList.class);
    public static final char          ARRAY_PREFIX                    = '[';

    public GenericDeserializer(String type) {
        this.type = type;
    }

    /**
     * 当读取list时会调用此方法
     */
    public Object readList(AbstractHessianInput in, int length) throws IOException {
        List list = (List) DEFAULT_COLLECTION_DESERIALIZER.readList(in, length);
        return convertGeneric(length, list);

    }

    /**
     * 当读取list时会调用此方法
     */
    public Object readLengthList(AbstractHessianInput in, int length) throws IOException {
        List list = (List) DEFAULT_COLLECTION_DESERIALIZER.readLengthList(in, length);
        return convertGeneric(length, list);
    }

    /**
     * 当读取map时会调用此方法
     */
    public Object readMap(AbstractHessianInput in) throws IOException {
        Map map = (Map) DEFAULT_MAP_DESERIALIZER.readMap(in);
        GenericMap genericMap = new GenericMap(type);
        genericMap.setMap(map);
        return genericMap;
    }

    /**
     * 当读取Object时会调用此方法, Object类型包括Enum与AliEnum
     */
    public Object readObject(AbstractHessianInput in, Object[] fieldNames) throws IOException {
        GenericObject obj = new GenericObject(type);
        return readGenericObject(in, obj, (String[]) fieldNames);
    }

    private static GenericObject readGenericObject(AbstractHessianInput in, GenericObject obj,
                                                   String[] fieldNames) throws IOException {
        in.addRef(obj);

        for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            Object value = in.readObject();
            obj.putField(fieldName, value);
        }
        return obj;
    }

    private Object convertGeneric(int length, List list) {

        if (type.charAt(0) == ARRAY_PREFIX) {

            // type以'['开头, 说明反序列化的是数组对象, 反序列化为GenericArray
            Object[] objects = new Object[length];

            for (int i = 0; i < list.size(); i++) {
                objects[i] = list.get(i);
            }

            GenericArray ga = new GenericArray(ClassFilter.decodeObjectAndDate(type.substring(1)));
            ga.setObjects(objects);
            return ga;
        } else {

            // type不以'['开头, 说明反序列化的是Collection对象, 反序列化为GenericCollection
            GenericCollection gc = new GenericCollection(type);
            gc.setCollection(list);
            return gc;
        }
    }
}
