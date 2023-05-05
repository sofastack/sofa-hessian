/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.throwable;

import com.caucho.hessian.io.AbstractDeserializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author junyuan
 * @version AbstractFieldSpecificDeserializer.java, v 0.1 2023年05月06日 14:21 junyuan Exp $
 */
public abstract class AbstractFieldSpecificDeserializer extends AbstractDeserializer {

    protected Map<String, Field> _fields;

    public AbstractFieldSpecificDeserializer(Class<?> cl) {
        _fields = getFieldMapForSerialize(cl);
    }

    protected Map<String, Field> getFieldMapForSerialize(Class cl) {
        Map<String, Field> fields = new HashMap<String, Field>();
        for (; cl != null; cl = cl.getSuperclass()) {
            Field[] originFields = cl.getDeclaredFields();
            for (int i = 0; i < originFields.length; i++) {
                Field field = originFields[i];
                if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                } else if (fields.containsKey(field.getName())) {
                    continue;
                }
                fields.put(field.getName(), field);
            }
        }
        return fields;
    }
}