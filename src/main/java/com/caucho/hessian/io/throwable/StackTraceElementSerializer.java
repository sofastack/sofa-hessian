/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.throwable;

import com.caucho.hessian.io.AbstractFieldAdaptorSerializer;
import com.caucho.hessian.io.AbstractHessianOutput;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junyuan
 * @version StackTraceElementSerializer.java, v 0.1 2023年04月10日 11:12 junyuan Exp $
 */
public class StackTraceElementSerializer extends AbstractFieldAdaptorSerializer {
    protected static final Logger                        log          = Logger
                                                                          .getLogger(StackTraceElementSerializer.class
                                                                              .getName());

    private final Class<StackTraceElement>               _clazz       = StackTraceElement.class;

    private final static String                          GET_PREFIX   = "get";

    private Map<String/*fieldName*/, Method/*getter*/> _readMethods = new HashMap<String, Method>();

    public StackTraceElementSerializer() {
        super(StackTraceElement.class);

        // get getter
        for (Field field : _fields) {
            String fieldName = field.getName();

            String methodName = GET_PREFIX + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            if ("declaringClass".equals(fieldName)) {
                methodName = GET_PREFIX + "ClassName";
            }

            try {
                Method m = _clazz.getMethod(methodName);
                _readMethods.put(fieldName, m);
            } catch (NoSuchMethodException e) {
                log.log(Level.WARNING, "getter not found: " + methodName, e);
            } catch (Exception e) {
                log.log(Level.WARNING, e.toString(), e);
            }
        }
    }

    @Override
    protected void serializeField(AbstractHessianOutput out, Object obj, Field field)
        throws IOException {
        if (!_readMethods.containsKey(field.getName())) {
            out.writeNull();
            return;
        }

        // only String and int field is required to be serialized
        if (String.class.equals(field.getType())) {
            String value = null;
            try {
                value = (String) _readMethods.get(field.getName()).invoke(obj);
            } catch (Exception e) {
                log.log(Level.FINE, e.toString(), e);
            }
            out.writeString(value);
        } else if (int.class.equals(field.getType())) {
            Integer value = 0;
            try {
                value = (Integer) _readMethods.get(field.getName()).invoke(obj);
            } catch (Exception e) {
                log.log(Level.FINE, e.toString(), e);
            }
            out.writeInt(value);
        } else {
            log.warning("unsupported field " + field.getName() + "(" + field.getType() + "), will write null");
            out.writeNull();
        }
    }

    @Override
    protected Field[] getFieldsForSerialize(Class cl) {
        List<Field> fields = new ArrayList<Field>();
        for (; cl != null; cl = cl.getSuperclass()) {
            Field[] originFields = cl.getDeclaredFields();
            for (int i = 0; i < originFields.length; i++) {
                Field field = originFields[i];
                if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if ("format".equals(field.getName())) {
                    continue;
                }
                fields.add(field);
            }
        }
        return fields.toArray(new Field[0]);
    }
}
