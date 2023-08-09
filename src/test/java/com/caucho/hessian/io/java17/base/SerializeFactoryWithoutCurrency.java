/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.java17.base;

import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.JavaSerializer;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;

import java.util.Currency;

/**
 *
 * @author junyuan
 * @version SerializeFactoryWithoutCurrency.java, v 0.1 2023年08月09日 11:48 junyuan Exp $
 */
public class SerializeFactoryWithoutCurrency extends SerializerFactory {

    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        if (Currency.class.equals(cl)) {
            return new JavaSerializer(cl);
        }
        return super.getSerializer(cl);
    }

    @Override
    public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
        if (Currency.class.equals(cl)) {
            return new JavaDeserializer(cl);
        }
        return super.getDeserializer(cl);
    }
}