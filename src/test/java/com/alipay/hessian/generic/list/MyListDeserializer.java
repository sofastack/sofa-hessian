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

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.HessianFieldException;
import com.caucho.hessian.io.IOExceptionWrapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;

public class MyListDeserializer extends AbstractDeserializer {

    private Class   _type;
    private HashMap _fieldMap;

    public MyListDeserializer(Class cl) {
        _type = cl;
        _fieldMap = getFieldMap(cl);
    }

    public Object readObject(AbstractHessianInput in, Object[] fieldNames) throws IOException {
        return readObject(in, (String[]) fieldNames);
    }

    public Object readObject(AbstractHessianInput in, String[] fieldNames) throws IOException {
        try {
            Object obj = instantiate();

            return readObject(in, obj, fieldNames);
        } catch (IOException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IOExceptionWrapper(_type.getName() + ":" + e.getMessage(), e);
        }
    }

    public Object readObject(AbstractHessianInput in, Object obj, String[] fieldNames)
        throws IOException {
        try {
            int ref = in.addRef(obj);

            for (int i = 0; i < fieldNames.length; i++) {
                String name = fieldNames[i];

                FieldDeserializer deser = (FieldDeserializer) _fieldMap.get(name);

                if (deser != null) {
                    deser.deserialize(in, obj);
                } else {
                    Object tempObj = in.readObject();
                    if (name.equals("_list_content")) {
                        Collection collection = (Collection) tempObj;
                        Collection re = (Collection) obj;
                        re.addAll(collection);
                    }
                }
            }

            return obj;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOExceptionWrapper(obj.getClass().getName() + ":" + e, e);
        }
    }

    protected Object instantiate() throws Exception {
        return _type.newInstance();
    }

    protected HashMap getFieldMap(Class cl) {
        HashMap fieldMap = new HashMap();
        try {
            Field field = cl.getDeclaredField("name");
            field.setAccessible(true);
            fieldMap.put("name", new StringFieldDeserializer(field));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return fieldMap;
    }

    abstract static class FieldDeserializer {
        abstract void deserialize(AbstractHessianInput in, Object obj) throws IOException;
    }

    static class StringFieldDeserializer extends FieldDeserializer {
        private final Field _field;

        StringFieldDeserializer(Field field) {
            _field = field;
        }

        void deserialize(AbstractHessianInput in, Object obj) throws IOException {
            String value = null;

            try {
                value = in.readString();

                _field.set(obj, value);
            } catch (Exception e) {
                logDeserializeError(_field, obj, value, e);
            }
        }
    }

    static void logDeserializeError(Field field, Object obj, Object value, Throwable e)
        throws IOException {
        String fieldName = (field.getDeclaringClass().getName() + "." + field.getName());

        if (e instanceof HessianFieldException)
            throw (HessianFieldException) e;
        else if (e instanceof IOException)
            throw new HessianFieldException(fieldName + ": " + e.getMessage(), e);

        if (value != null)
            throw new HessianFieldException(fieldName + ": " + value.getClass().getName() + " ("
                + value + ")" + " cannot be assigned to "
                + field.getType().getName());
        else
            throw new HessianFieldException(fieldName + ": " + field.getType().getName()
                + " cannot be assigned from null", e);
    }

}
