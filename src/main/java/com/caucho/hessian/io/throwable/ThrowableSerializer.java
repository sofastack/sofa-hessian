/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.throwable;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.NonReflectionSerializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author junyuan
 * @version ThrowableSerializer.java, v 0.1 2023年04月10日 19:30 junyuan Exp $
 */
public class ThrowableSerializer extends NonReflectionSerializer {

    private final Class<Throwable> _clazz        = Throwable.class;
    protected Method               getSuppressed = null;

    public ThrowableSerializer() {
        Field[] fields = _clazz.getDeclaredFields();
        List<Field> tmp = new ArrayList();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            if (Modifier.isTransient(field.getModifiers()) ||
                Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            tmp.add(field);
        }

        _fields = new Field[tmp.size()];
        tmp.toArray(_fields);

        try {
            getSuppressed = _clazz.getDeclaredMethod("getSuppressed");
        } catch (NoSuchMethodException e) {

        }
    }

    @Override
    protected void serializeField(AbstractHessianOutput out, Object obj, Field field)
        throws IOException {
        if (!(obj instanceof Throwable)) {
            throw new UnsupportedOperationException(String.valueOf(this));
        }

        Throwable current = (Throwable) obj;

        if ("detailMessage".equals(field.getName())) {
            out.writeString(current.getMessage());
        }
        else if ("cause".equals(field.getName())) {
            out.writeObject(current.getCause());
        }
        else if ("stackTrace".equals(field.getName())) {
            out.writeObject(current.getStackTrace());
        }
        else if ("suppressedExceptions".equals(field.getName())) {
            if (getSuppressed == null) {
                throw new UnsupportedOperationException(String.valueOf(this));
            }
            Throwable[] throwableArray;
            try {
                throwableArray = (Throwable[]) getSuppressed.invoke(obj);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }

            List<Throwable> throwableList;
            if (throwableArray.length == 0) {
                throwableList = Collections.emptyList();
            } else {
                throwableList = Arrays.asList(throwableArray);
            }

            out.writeObject(throwableList);
        }
        else {
            defaultSerializeField(out, obj, field);
        }

    }

    /**
     * 针对自定义的 field, 尝试以反射方式获取
     * @param out
     * @param obj
     * @param field
     * @throws IOException
     */
    protected void defaultSerializeField(AbstractHessianOutput out, Object obj, Field field)
            throws IOException {
        Object fieldValue = null;
        try {
            fieldValue = field.get(obj);
        } catch (IllegalAccessException e) {
            try {
                field.setAccessible(true);
                fieldValue = field.get(obj);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        out.writeObject(fieldValue);
    }
}