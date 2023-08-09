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
package com.caucho.hessian.io.throwable;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.JavaSerializer;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;

import java.io.IOException;

/**
 *
 * @author junyuan
 * @version SerializeFactoryWithoutThrowable.java, v 0.1 2023年04月27日 19:14 junyuan Exp $
 */
public class SerializeFactoryWithoutThrowable extends SerializerFactory {

    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        Serializer serializer = super.getSerializer(cl);

        if (Throwable.class.isAssignableFrom(cl))
            serializer = new OriginThrowableSerializer(cl);

        if (StackTraceElement.class.isAssignableFrom(cl))
            serializer = new JavaSerializer(cl);

        _cachedSerializerMap.put(cl, serializer);

        return serializer;
    }

    @Override
    public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
        Deserializer deserializer = super.getDeserializer(cl);

        if (Throwable.class.isAssignableFrom(cl))
            deserializer = new JavaDeserializer(cl);

        if (StackTraceElement.class.isAssignableFrom(cl))
            deserializer = new OriginStackTraceElementDeserializer();

        _cachedDeserializerMap.put(cl, deserializer);

        return deserializer;
    }

    private static class OriginThrowableSerializer extends JavaSerializer {

        public OriginThrowableSerializer(Class cl) {
            super(cl);
        }

        @Override
        public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
            Throwable t = (Throwable) obj;
            t.getStackTrace();

            super.writeObject(obj, out);
        }
    }

    private class OriginStackTraceElementDeserializer extends JavaDeserializer {
        public OriginStackTraceElementDeserializer() {
            super(StackTraceElement.class);
        }

        @Override
        protected Object instantiate() throws Exception {
            return new StackTraceElement("", "", "", 0);
        }
    }

}
