/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.java17.base;

import com.caucho.hessian.io.AbstractFieldAdaptorSerializer;
import com.caucho.hessian.io.AbstractHessianOutput;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Currency;

/**
 *
 * @author junyuan
 * @version JavaCurrencySerializer.java, v 0.1 2023年08月09日 10:32 junyuan Exp $
 */
public class JavaCurrencySerializer extends AbstractFieldAdaptorSerializer {

    public JavaCurrencySerializer(Class<?> clazz) {
        super(clazz);
    }

    @Override
    protected void serializeField(AbstractHessianOutput out, Object obj, Field field)
        throws IOException {
        Currency currency = (Currency) obj;
        if ("currencyCode".equals(field.getName())) {
            String currencyCode = currency.getCurrencyCode();
            out.writeString(currencyCode);
        }
    }
}