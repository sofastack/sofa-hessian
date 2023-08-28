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
package com.caucho.hessian.io.java17.base;

import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;

import java.util.Currency;

/**
 *
 * @author junyuan
 * @version SerializeFactoryWithoutCurrency.java, v 0.1 2023年08月09日 11:48 junyuan Exp $
 */
public class SerializeFactoryWithCurrency extends SerializerFactory {

    private Serializer   javaCurrencySerializer   = new JavaCurrencySerializer(Currency.class);
    private Deserializer javaCurrencyDeserializer = new JavaCurrencyDeserializer();

    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        if (Currency.class.equals(cl)) {
            return javaCurrencySerializer;
        }
        return super.getSerializer(cl);
    }

    @Override
    public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
        if (Currency.class.equals(cl)) {
            return javaCurrencyDeserializer;
        }
        return super.getDeserializer(cl);
    }
}