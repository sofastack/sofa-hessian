/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 *
 * @author junyuan
 * @version NonReflectionSerializer.java, v 0.1 2023年04月10日 19:34 junyuan Exp $
 */
public abstract class NonReflectionSerializer extends AbstractSerializer {

    protected Field[] _fields;

    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (obj == null) {
            out.writeNull();
            return;
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

    protected abstract void serializeField(AbstractHessianOutput out, Object obj, Field field) throws IOException;
}