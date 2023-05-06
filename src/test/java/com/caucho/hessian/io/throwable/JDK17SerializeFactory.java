/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.throwable;

import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.io.throwable.adapter.EnumConstantNotPresentExceptionDeserializer;
import com.caucho.hessian.io.throwable.adapter.EnumConstantNotPresentExceptionSerializer;

/**
 * 可以在 java8 环境下运行专门给 jdk17 使用的序列化器
 * 以便在 java8 下进行兼容测试
 * @author junyuan
 * @version JDK17SerializeFactory.java, v 0.1 2023年05月06日 11:13 junyuan Exp $
 */
public class JDK17SerializeFactory extends SerializerFactory {
    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        Serializer serializer = super.getSerializer(cl);

        if (Throwable.class.isAssignableFrom(cl)) {
            if (EnumConstantNotPresentException.class.equals(cl)) {
                serializer = new EnumConstantNotPresentExceptionSerializer();
            } else {
                serializer = new ThrowableSerializer(cl);
            }
        }

        if (StackTraceElement.class.isAssignableFrom(cl)) {
            serializer = new StackTraceElementSerializer();
        }
        _cachedSerializerMap.put(cl, serializer);

        return serializer;
    }

    @Override
    public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
        Deserializer deserializer = super.getDeserializer(cl);

        if (Throwable.class.isAssignableFrom(cl)) {
            if (EnumConstantNotPresentException.class.equals(cl)) {
                deserializer = new EnumConstantNotPresentExceptionDeserializer(cl);
            } else {
                deserializer = new ThrowableDeserializer(cl);
            }
        }

        if (StackTraceElement.class.isAssignableFrom(cl))
            deserializer = new StackTraceElementDeserializer();

        _cachedDeserializerMap.put(cl, deserializer);
        return deserializer;
    }
}