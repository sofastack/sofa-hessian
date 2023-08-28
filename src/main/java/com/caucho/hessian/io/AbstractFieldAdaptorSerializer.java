/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author junyuan
 * @version AbstractFieldAdaptorSerializer.java, v 0.1 2023年04月10日 19:34 junyuan Exp $
 */
public abstract class AbstractFieldAdaptorSerializer extends AbstractSerializer {

    protected Field[] _fields;

    public AbstractFieldAdaptorSerializer(Class<?> clazz) {
        this._fields = getFieldsForSerialize(clazz);
    }

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

    /**
     * get all fields
     * include super class
     * exclude transient or static
     * @param cl
     * @return
     */
    protected Field[] getFieldsForSerialize(Class cl) {
        List<Field> fields = new ArrayList<Field>();
        for (; cl != null; cl = cl.getSuperclass()) {
            Field[] originFields = cl.getDeclaredFields();
            for (int i = 0; i < originFields.length; i++) {
                Field field = originFields[i];
                if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                fields.add(field);
            }
        }
        return fields.toArray(new Field[0]);
    }
}
