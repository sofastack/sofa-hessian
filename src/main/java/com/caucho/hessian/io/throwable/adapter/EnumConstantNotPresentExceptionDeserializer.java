/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.throwable.adapter;

import com.caucho.hessian.io.throwable.ThrowableDeserializer;

import java.util.Map;

/**
 *
 * @author junyuan
 * @version EnumConstantNotPresentExceptionDeserializer.java, v 0.1 2023年04月27日 17:55 junyuan Exp $
 */
public class EnumConstantNotPresentExceptionDeserializer extends ThrowableDeserializer {
    public EnumConstantNotPresentExceptionDeserializer(Class cl) {
        super(cl);
    }

    @Override
    protected Throwable instantiate(Class<?> clazz, Map<String, Object> fieldValueMap)
            throws Exception {
        Class enumType = (Class) fieldValueMap.remove("enumType");
        String constantName = (String) fieldValueMap.remove("constantName");
        return new EnumConstantNotPresentException(enumType, constantName);
    }
}