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

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

public class MyListSerializer extends AbstractSerializer {

    private Field[]           _fields;
    private FieldSerializer[] _fieldSerializers;

    public MyListSerializer(Class cl) {
        try {
            _fields = new Field[1];
            _fieldSerializers = new FieldSerializer[1];

            _fields[0] = cl.getDeclaredField("name");
            _fields[0].setAccessible(true);
            _fieldSerializers[0] = StringFieldSerializer.SER;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (out.addRef(obj)) {
            return;
        }

        Class cl = obj.getClass();
        int ref = out.writeObjectBegin(cl.getName());

        if (ref < -1) {
            writeObject10(obj, out);
        } else {
            if (ref == -1) {
                writeDefinition20(out);
                out.writeObjectBegin(cl.getName());
            }

            writeInstance(obj, out);
        }
    }

    protected void writeObject10(Object obj, AbstractHessianOutput out) throws IOException {
        for (int i = 0; i < _fields.length; i++) {
            Field field = _fields[i];
            out.writeString(field.getName());
            _fieldSerializers[i].serialize(out, obj, field);
        }

        // writeList 内容
        out.writeString("_list_content");
        ArrayList list = new ArrayList((Collection) obj);
        out.writeObject(list);
    }

    private void writeDefinition20(AbstractHessianOutput out) throws IOException {
        out.writeClassFieldLength(_fields.length + 1);

        for (int i = 0; i < _fields.length; i++) {
            Field field = _fields[i];
            out.writeString(field.getName());
        }
        out.writeString("_list_content");
    }

    public void writeInstance(Object obj, AbstractHessianOutput out) throws IOException {
        for (int i = 0; i < _fields.length; i++) {
            Field field = _fields[i];
            _fieldSerializers[i].serialize(out, obj, field);
        }

        ArrayList list = new ArrayList((Collection) obj);
        out.writeObject(list);
    }

    static class FieldSerializer {
        static final FieldSerializer SER = new FieldSerializer();

        void serialize(AbstractHessianOutput out, Object obj, Field field) throws IOException {
            Object value = null;

            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                log.log(Level.FINE, e.toString(), e);
            }

            out.writeObject(value);
        }
    }

    static class StringFieldSerializer extends FieldSerializer {
        static final FieldSerializer SER = new StringFieldSerializer();

        void serialize(AbstractHessianOutput out, Object obj, Field field) throws IOException {
            String value = null;

            try {
                value = (String) field.get(obj);
            } catch (IllegalAccessException e) {
                log.log(Level.FINE, e.toString(), e);
            }

            out.writeString(value);
        }
    }
}
