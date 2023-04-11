/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.throwable;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.IOExceptionWrapper;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author junyuan
 * @version ThrowableDeserializer.java, v 0.1 2023年04月10日 20:37 junyuan Exp $
 */
public class ThrowableDeserializer extends AbstractDeserializer {

    private Class<?>             _type;
    protected Map<String, Field> _fields       = new HashMap<String, Field>();
    protected Method             addSuppressed = null;

    protected Constructor<?>     _constructor  = null;

    public ThrowableDeserializer(Class cl) {
        _type = cl;
        Class<Throwable> clazz = Throwable.class;

        Field[] originFields = clazz.getDeclaredFields();
        for (int i = 0; i < originFields.length; i++) {
            Field field = originFields[i];

            if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            _fields.put(field.getName(), field);
        }

        try {
            _constructor = _type.getDeclaredConstructor(String.class);
        } catch (NoSuchMethodException e) {

        }

        try {
            addSuppressed = clazz.getDeclaredMethod("addSuppressed", Throwable.class);
        } catch (NoSuchMethodException e) {

        }
    }

    @Override
    public Class getType() {
        return _type;
    }

    @Override
    public Object readObject(AbstractHessianInput in, String[] fieldNames) throws IOException {
        try {
            int ref = in.addRef(new Throwable());
            Map<String, Object> fieldValueMap = new HashMap<String, Object>();

            for (int i = 0; i < fieldNames.length; i++) {
                String name = fieldNames[i];
                Field field = _fields.get(name);

                if (String.class.equals(field.getType())) {
                    fieldValueMap.put(name, in.readString());
                } else {
                    fieldValueMap.put(name, in.readObject());
                }
            }
            Throwable obj = null;
            if (_constructor != null) {
                try {
                    obj = (Throwable) _constructor.newInstance(fieldValueMap.get("detailMessage"));
                    Throwable cause = (Throwable) fieldValueMap.get("cause");
                    if (cause != null) {
                        obj.initCause(cause);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    obj = new Throwable((String) fieldValueMap.get("detailMessage"),
                        (Throwable) fieldValueMap.get("cause"));
                }
            } else {
                obj = new Throwable((String) fieldValueMap.get("detailMessage"), (Throwable) fieldValueMap.get("cause"));
            }

            obj.setStackTrace((StackTraceElement[]) fieldValueMap.get("stackTrace"));

            List<Throwable> suppress = (List<Throwable>) fieldValueMap.get("suppressedExceptions");
            for (Throwable t : suppress) {
                addSuppressed.invoke(obj, t);
            }

            in.setRef(ref, obj);

            return obj;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOExceptionWrapper(Throwable.class.getName() + ":" + e, e);
        }
    }
}