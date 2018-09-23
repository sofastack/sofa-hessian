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

import com.alipay.hessian.generic.model.GenericClass;
import com.alipay.hessian.generic.util.ClassFilter;
import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.IOExceptionWrapper;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author <a href="mailto:caojie.cj@antfin.com">Jie Cao</a>
 * @since 5.3.0
 */
public class GenericClassDeserializer extends AbstractDeserializer {

    private static final GenericClassDeserializer instance     = new GenericClassDeserializer();

    private static final HashMap<String, Class>   _primClasses = new HashMap<String, Class>();

    static {
        _primClasses.put("void", void.class);
        _primClasses.put("boolean", boolean.class);
        _primClasses.put("java.lang.Boolean", Boolean.class);
        _primClasses.put("byte", byte.class);
        _primClasses.put("java.lang.Byte", Byte.class);
        _primClasses.put("char", char.class);
        _primClasses.put("java.lang.Character", Character.class);
        _primClasses.put("short", short.class);
        _primClasses.put("java.lang.Short", Short.class);
        _primClasses.put("int", int.class);
        _primClasses.put("java.lang.Integer", Integer.class);
        _primClasses.put("long", long.class);
        _primClasses.put("java.lang.Long", Long.class);
        _primClasses.put("float", float.class);
        _primClasses.put("java.lang.Float", Float.class);
        _primClasses.put("double", double.class);
        _primClasses.put("java.lang.Double", Double.class);
        _primClasses.put("java.lang.String", String.class);
    }

    public static GenericClassDeserializer getInstance() {
        return instance;
    }

    private GenericClassDeserializer() {
    }

    /**
     * 当读取Object时会调用此方法, Object类型包括Enum与AliEnum
     */
    public Object readObject(AbstractHessianInput in, Object[] fieldNames) throws IOException {
        int ref = in.addRef(null);

        String name = null;

        for (int i = 0; i < fieldNames.length; i++) {
            if ("name".equals(fieldNames[i])) {
                name = in.readString();
            } else {
                in.readObject();
            }
        }

        Object value;
        if (ClassFilter.filter(name)) {
            value = create(name);
        } else {
            value = new GenericClass(name);
        }

        in.setRef(ref, value);

        return value;
    }

    Object create(String name) throws IOException {
        if (name == null) {
            throw new IOException("Serialized Class expects name.");
        }

        Class cl = _primClasses.get(name);

        if (cl != null) {
            return cl;
        }

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            if (loader != null) {
                return Class.forName(name, false, loader);
            } else {
                return Class.forName(name);
            }
        } catch (Exception e) {
            throw new IOExceptionWrapper(e);
        }
    }
}
