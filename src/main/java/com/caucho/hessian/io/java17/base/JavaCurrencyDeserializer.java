/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.java17.base;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;

import java.io.IOException;
import java.util.Currency;

/**
 *
 * @author junyuan
 * @version JavaCurrencyDeserializer.java, v 0.1 2023年08月09日 10:53 junyuan Exp $
 */
public class JavaCurrencyDeserializer extends AbstractDeserializer {

    @Override
    public Object readObject(AbstractHessianInput in, String[] fieldNames) throws IOException {
        int ref = in.addRef(null);
        String currencyCode = in.readString();
        Currency currency = null;
        try {
            // 如果该数据有问题, 保证至少塞入一个 null 作为 ref
            currency = Currency.getInstance(currencyCode);
        } finally {
            in.setRef(ref, currency);
        }
        return currency;
    }
}