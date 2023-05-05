/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
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

    public class OriginStackTraceElementDeserializer extends JavaDeserializer {
        public OriginStackTraceElementDeserializer() {
            super(StackTraceElement.class);
        }

        @Override
        protected Object instantiate() throws Exception {
            return new StackTraceElement("", "", "", 0);
        }
    }

}