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
package com.alipay.hessian.generic.model;

import com.alipay.hessian.generic.util.ClassFilter;

import java.io.Serializable;

import static com.alipay.hessian.generic.io.GenericDeserializer.ARRAY_PREFIX;

/**
 * @author <a href="mailto:caojie.cj@antfin.com">Jie Cao</a>
 * @since 5.3.0
 */
public final class GenericArray implements Serializable {

    // actually, we won't serialize this class itself
    private static final long serialVersionUID = 2584912495844320855L;

    private String            type;

    private String            componentType;

    private Object[]          objects;

    /**
     * 默认构造函数, 供hessian反序列化
     */
    private GenericArray() {
    }

    public GenericArray(String componentType) {
        this.componentType = getActualComponentType(componentType);
        this.type = "[" + ClassFilter.encodeObjectAndDate(componentType);
    }

    /**
     * 对于Object[]数组, 传入的componentType为[java.lang.Object, 但是Class.forName 需要的字符串是 [Ljava.lang.Object;
     * 如果不以'['开头, 直接返回, 否则拼接Class.forName需要的字符串格式
     * @return
     */
    private String getActualComponentType(String componentType) {

        if (componentType == null) {
            throw new IllegalArgumentException("参数不能为null.");
        }

        int lastIndex = componentType.lastIndexOf(ARRAY_PREFIX);
        if (lastIndex != -1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lastIndex + 1; i++) {
                sb.append(ARRAY_PREFIX);
            }
            sb.append("L");
            sb.append(componentType.substring(lastIndex + 1));
            sb.append(";");
            return sb.toString();
        }

        return componentType;
    }

    public String getType() {
        return type;
    }

    public String getComponentType() {
        return componentType;
    }

    public int getLength() {
        return objects.length;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }

    public Object get(int idx) {
        return objects[idx];
    }

    public Object[] getObjects() {
        return objects;
    }
}
