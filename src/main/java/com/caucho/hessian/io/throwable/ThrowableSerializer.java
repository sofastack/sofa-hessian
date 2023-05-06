/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.throwable;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractFieldSpecificSerializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author junyuan
 * @version ThrowableSerializer.java, v 0.1 2023年04月10日 19:30 junyuan Exp $
 */
public class ThrowableSerializer extends AbstractFieldSpecificSerializer {

    protected Method getSuppressed = null;

    public ThrowableSerializer(Class<?> clazz) {
        super(clazz);

        try {
            getSuppressed = clazz.getMethod("getSuppressed");
        } catch (NoSuchMethodException e) {

        }
    }

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        // 如果需要反射操作获取 stack trace, 这里需要先 get 一下
        ((Throwable) obj).getStackTrace();

        super.writeObject(obj, out);
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
                // 旧版通过反射会获取到这个类型
                throwableList = Collections.unmodifiableList(new ArrayList<Throwable>());
            } else {
                throwableList = new ArrayList<Throwable>(Arrays.asList(throwableArray));
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
