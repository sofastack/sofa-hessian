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

import java.io.Serializable;
import java.util.*;

/**
 * 此类用于表示泛化类型.
 * 除了表示一般的JavaBean, 此类还能表示Enum与AliEnum.
 * 之所以没有为 Enum 与 AliEnum 单独提供泛化类型, 是因为在反序列化时无法通过类名判断具体类型, 所以不管是普通JavaBean 还是 Enum 类型都会反序列化成此类型.
 * 对Enum类型或者AliEnum类型, fields类型只应该包含一个name属性.
 *
 * @author <a href="mailto:caojie.cj@antfin.com">Jie Cao</a>
 * @since 5.3.0
 */
public final class GenericObject implements Serializable, Comparable<GenericObject> {

    private static final long   serialVersionUID = 3457717009326601317L;

    // present the type of Object
    private String              type;
    // store all fields of Object. note that the value can be reference of any type,
    // including Integer, String, java.*, com.alipay.*, even GenericObject
    private Map<String, Object> fields           = new TreeMap<String, Object>();

    /**
     * 默认构造函数, 供hessian反序列化
     */
    private GenericObject() {
    }

    public GenericObject(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Object putField(String fieldName, Object value) {
        return fields.put(fieldName, value);
    }

    public Object getField(String fieldName) {
        return fields.get(fieldName);
    }

    public boolean hasField(String fieldName) {
        return fields.containsKey(fieldName);
    }

    public Set<String> getFieldNames() {
        return fields.keySet();
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return toString(new HashSet<Object>());
    }

    public String toString(Set<Object> set) {
        if (set.contains(this))
            return "<REF>";
        set.add(this);
        StringBuilder sb = new StringBuilder("{type: " + type + ", fields: {");
        Iterator<Map.Entry<String, Object>> it = fields.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            sb.append(entry.getKey());
            sb.append("=");
            Object value = entry.getValue();
            if (value instanceof GenericObject)
                sb.append(((GenericObject) value).toString(set));
            else
                sb.append(value);
            if (it.hasNext())
                sb.append(", ");
        }
        sb.append("}}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        GenericObject that = (GenericObject) o;

        if (type != null ? !type.equals(that.type) : that.type != null)
            return false;
        return fields != null ? fields.equals(that.fields) : that.fields == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (fields != null ? getFieldsHashcode(fields) : 0);
        return result;
    }

    private int getFieldsHashcode(Map<String, Object> fields) {
        int h = 0;
        Iterator<Map.Entry<String, Object>> i = fields.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, Object> entry = i.next();

            if (entry.getValue() != null && entry.getValue().getClass() == GenericObject.class) {
                /**
                 * fields中可能放入当前对象, 为防止死循环,如果值域为GenericObject,我计算Key的HashCode
                 */
                h += entry.getKey().hashCode();
            } else {

                h += entry.hashCode();
            }
        }
        return h;
    }

    @Override
    public int compareTo(GenericObject genericObject) {
        int result = this.type.compareTo(genericObject.getType());
        if (result != 0) {
            return result;
        }

        result = fields.size() - genericObject.getFields().size();
        if (result != 0) {
            return result;
        }

        Iterator<String> thisIterator = fields.keySet().iterator();
        Iterator<String> thatIterator = genericObject.getFields().keySet().iterator();
        while (thisIterator.hasNext() && thatIterator.hasNext()) {
            String thisKey = thisIterator.next();
            String thatKey = thatIterator.next();

            result = thisKey.compareTo(thatKey);
            if (result != 0) {
                return result;
            }

            Object thisValue = fields.get(thisKey);
            Object thatValue = genericObject.getFields().get(thatKey);
            if (thisValue == null && thatValue == null) {
                continue;
            }
            if (thisValue == null) {
                return -1;
            }
            if (thatValue == null) {
                return 1;
            }
            result = Integer.valueOf(thisValue.hashCode()).compareTo(thatValue.hashCode());
            if (result != 0) {
                return result;
            }
        }

        return 0;
    }
}
