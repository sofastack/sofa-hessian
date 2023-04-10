/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junyuan
 * @version StackTraceElementSerializer.java, v 0.1 2023年04月10日 11:12 junyuan Exp $
 */
public class StackTraceElementSerializer extends AbstractSerializer {
    protected static final Logger                        log          = Logger
                                                                          .getLogger(StackTraceElementSerializer.class
                                                                              .getName());

    private Class<?>                                     _clazz       = StackTraceElement.class;
    private Field[]                                      _fields;
    {
        Field[] originFields = _clazz.getDeclaredFields();
        ArrayList<Field> tmp = new ArrayList();
        for (int i = 0; i < originFields.length; i++) {
            Field field = originFields[i];

            if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if ("declaringClassObject".equals(field.getName()) || "format".equals(field.getName())) {
                continue;
            }
            tmp.add(field);
        }
        _fields = new Field[tmp.size()];
        tmp.toArray(_fields);
    }

    private final static String                          GET_PREFIX   = "get";

    private Map<String/*fieldName*/, Method/*getter*/> _readMethods = new HashMap<String, Method>();
    {
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
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (obj == null) {
            out.writeNull();
            return;
        }

        if (!(obj instanceof StackTraceElement)) {
            throw new UnsupportedOperationException(String.valueOf(this));
        }

        if (out.addRef(obj)) {
            return;
        }
        Class cl = obj.getClass();
        int ref = out.writeObjectBegin(cl.getName());

        if (ref < -1) {
            writeObject10(obj, out);
        }
        else {
            if (ref == -1) {
                writeDefinition20(out);
                out.writeObjectBegin(cl.getName());
            }

            writeInstance(obj, out);
        }
    }

    private void writeObject10(Object obj, AbstractHessianOutput out)
        throws IOException
    {
        for (int i = 0; i < _fields.length; i++) {
            Field field = _fields[i];

            out.writeString(field.getName());

            serializeField(out, obj, field);
        }

        out.writeMapEnd();
    }

    private void writeDefinition20(AbstractHessianOutput out)
        throws IOException
    {
        out.writeClassFieldLength(_fields.length);

        for (int i = 0; i < _fields.length; i++) {
            Field field = _fields[i];

            out.writeString(field.getName());
        }
    }

    public void writeInstance(Object obj, AbstractHessianOutput out)
        throws IOException
    {
        for (int i = 0; i < _fields.length; i++) {
            Field field = _fields[i];

            serializeField(out, obj, field);
        }
    }

    private void serializeField(AbstractHessianOutput out, Object obj, Field field)
        throws IOException {
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
            out.writeNull();
        }

    }
}