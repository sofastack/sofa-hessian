/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.java17.lang;

import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.JavaSerializer;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;

/**
 *
 * @author junyuan
 * @version StringBuilderJDK8SerializeFactory.java, v 0.1 2023年10月24日 16:52 junyuan Exp $
 */
public class StringBuilderJDK8SerializeFactory extends SerializerFactory {
    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        Serializer serializer = super.getSerializer(cl);

        if ( StringBuilder.class.isAssignableFrom(cl) || StringBuffer.class.isAssignableFrom(cl) ) {
            serializer = new JavaSerializer(cl);
        }

        return serializer;
    }

    @Override
    public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
        Deserializer deserializer = super.getDeserializer(cl);

        if ( StringBuilder.class.isAssignableFrom(cl) || StringBuffer.class.isAssignableFrom(cl) ) {
            deserializer = new JavaDeserializer(cl);
        }

        _cachedDeserializerMap.put(cl, deserializer);
        return deserializer;
    }
}