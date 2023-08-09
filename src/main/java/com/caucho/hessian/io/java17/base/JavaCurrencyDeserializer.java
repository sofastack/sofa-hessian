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
        String currencyCode = in.readString();
        return Currency.getInstance(currencyCode);
    }
}