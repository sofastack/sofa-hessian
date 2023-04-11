/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.throwable.adapter;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.throwable.ThrowableSerializer;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 *
 * @author junyuan
 * @version EnumConstantNotPresentExceptionSerializer.java, v 0.1 2023年04月27日 17:56 junyuan Exp $
 */
public class EnumConstantNotPresentExceptionSerializer extends ThrowableSerializer {

    @Override
    protected void defaultSerializeField(AbstractHessianOutput out, Object obj, Field field)
            throws IOException {
        if (!(obj instanceof EnumConstantNotPresentException)) {
            throw new UnsupportedOperationException(String.valueOf(this));
        }

        EnumConstantNotPresentException cast = (EnumConstantNotPresentException) obj;

        if (field.getName().equals("enumType")) {
            out.writeObject(cast.enumType());
        } else if (field.getName().equals("constantName")) {
            out.writeString(cast.constantName());
        } else {
            super.defaultSerializeField(out, obj, field);
        }
    }
}