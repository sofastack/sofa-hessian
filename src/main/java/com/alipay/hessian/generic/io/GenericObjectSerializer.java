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

import com.alipay.hessian.generic.model.GenericObject;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;
import com.caucho.hessian.io.Hessian2Output;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:caojie.cj@antfin.com">Jie Cao</a>
 * @since 5.3.0
 */
public class GenericObjectSerializer extends AbstractSerializer {
    private ConcurrentHashMap<ObjectDefinition, ObjectDefinition> genericDefinitionMap = new ConcurrentHashMap<ObjectDefinition, ObjectDefinition>();


    private static final Logger LOGGER = Logger
            .getLogger(GenericObjectSerializer.class
                    .getName());
    private static final GenericObjectSerializer INSTANCE = new GenericObjectSerializer();

    public static GenericObjectSerializer getInstance() {
        return INSTANCE;
    }

    private GenericObjectSerializer() {
    }

    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (!(obj instanceof GenericObject)) {
            throw new RuntimeException("the object must be a generic object");
        }
        doWriteObject((GenericObject) obj, out);
    }

    public void doWriteObject(GenericObject obj, AbstractHessianOutput out) throws IOException {
        if (out.addRef(obj)) {
            return;
        }
        String type = obj.getType();
        ObjectDefinition definition = this.getDefinition(obj);
        boolean newDefine = ((Hessian2Output) out).newDefineOrWriteRef(definition);
        // a new definition
        if (newDefine) {
            ((Hessian2Output) out).writeTypeName(type);
            writeDefinition(definition, out);
            // write ref
            ((Hessian2Output) out).newDefineOrWriteRef(definition);
        }
        // write ref
        writeInstance(obj, definition, out);

    }

    private ObjectDefinition getDefinition(GenericObject obj) {
        String[] fields = new String[obj.getFieldNames().size()];
        ObjectDefinition newDefinition = new ObjectDefinition(obj.getType(), obj.getFieldNames().toArray(fields));
        ObjectDefinition existDefinition = genericDefinitionMap.putIfAbsent(newDefinition, newDefinition);
        if (existDefinition != null) {
            return existDefinition;
        } else {
            return newDefinition;
        }
    }

    private void writeDefinition(ObjectDefinition definition, AbstractHessianOutput out)
            throws IOException {
        String[] _fieldNames = definition.getFieldNames();

        out.writeClassFieldLength(_fieldNames.length);

        for (int i = 0; i < _fieldNames.length; i++) {
            String fieldName = _fieldNames[i];

            out.writeString(fieldName);
        }
    }

    private void writeInstance(GenericObject obj, ObjectDefinition definition,
                               AbstractHessianOutput out) throws IOException {
        String[] _fieldNames = definition.getFieldNames();

        FieldSerializer[] _fieldSerializers = new FieldSerializer[_fieldNames.length];
        for (int i = 0; i < _fieldNames.length; i++) {
            Object field = obj.getField(_fieldNames[i]);
            Class<?> fieldType = field == null ? null : field.getClass();
            _fieldSerializers[i] = getFieldSerializer(fieldType);
        }

        for (int i = 0; i < _fieldNames.length; i++) {
            String fieldName = _fieldNames[i];
            _fieldSerializers[i].serialize(out, obj, fieldName);
        }
    }

    // ObjectDefinition just contains the type and fields' names which are used in Hessian
    static class ObjectDefinition {
        private final String _type;
        private final String[] _fieldNames;

        ObjectDefinition(String type, String[] fields) {
            _type = type;
            _fieldNames = fields;
        }

        String getType() {
            return _type;
        }

        String[] getFieldNames() {
            return _fieldNames;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ObjectDefinition that = (ObjectDefinition) o;

            if (_type != null ? !_type.equals(that._type) : that._type != null) return false;
            return Arrays.equals(_fieldNames, that._fieldNames);
        }

        @Override
        public int hashCode() {
            int result = _type != null ? _type.hashCode() : 0;
            result = 31 * result + Arrays.hashCode(_fieldNames);
            return result;
        }
    }

    private static FieldSerializer getFieldSerializer(Class type) {
        if (type == null)
            return FieldSerializer.SER;

        if (int.class.equals(type) || byte.class.equals(type) || short.class.equals(type)
                || Integer.class.equals(type) || Byte.class.equals(type) || Short.class.equals(type)) {
            return IntFieldSerializer.SER;
        } else if (long.class.equals(type) || Long.class.equals(type)) {
            return LongFieldSerializer.SER;
        } else if (double.class.equals(type) || float.class.equals(type)
                || Double.class.equals(type) || Float.class.equals(type)) {
            return DoubleFieldSerializer.SER;
        } else if (boolean.class.equals(type) || Boolean.class.equals(type)) {
            return BooleanFieldSerializer.SER;
        } else if (String.class.equals(type)) {
            return StringFieldSerializer.SER;
        } else
            return FieldSerializer.SER;
    }

    static class FieldSerializer {
        static final FieldSerializer SER = new FieldSerializer();

        void serialize(AbstractHessianOutput out, GenericObject obj, String field)
                throws IOException {
            Object value = obj.getField(field);
            out.writeObject(value);
        }
    }

    static class BooleanFieldSerializer extends FieldSerializer {
        static final FieldSerializer SER = new BooleanFieldSerializer();

        void serialize(AbstractHessianOutput out, GenericObject obj, String field)
                throws IOException {
            boolean value = (Boolean) obj.getField(field);
            out.writeBoolean(value);
        }
    }

    static class IntFieldSerializer extends FieldSerializer {
        static final FieldSerializer SER = new IntFieldSerializer();

        void serialize(AbstractHessianOutput out, GenericObject obj, String field)
                throws IOException {
            int value = ((Number) obj.getField(field)).intValue();
            out.writeInt(value);
        }
    }

    static class LongFieldSerializer extends FieldSerializer {
        static final FieldSerializer SER = new LongFieldSerializer();

        void serialize(AbstractHessianOutput out, GenericObject obj, String field)
                throws IOException {
            long value = ((Number) obj.getField(field)).longValue();
            out.writeLong(value);
        }
    }

    static class DoubleFieldSerializer extends FieldSerializer {
        static final FieldSerializer SER = new DoubleFieldSerializer();

        void serialize(AbstractHessianOutput out, GenericObject obj, String field)
                throws IOException {
            double value = ((Number) obj.getField(field)).doubleValue();
            out.writeDouble(value);
        }
    }

    static class StringFieldSerializer extends FieldSerializer {
        static final FieldSerializer SER = new StringFieldSerializer();

        void serialize(AbstractHessianOutput out, GenericObject obj, String field)
                throws IOException {
            String value = (String) obj.getField(field);
            out.writeString(value);
        }
    }
}
