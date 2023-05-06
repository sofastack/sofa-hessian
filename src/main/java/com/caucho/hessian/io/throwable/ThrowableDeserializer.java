/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.throwable;

import com.caucho.hessian.io.AbstractFieldSpecificDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.HessianFieldException;
import com.caucho.hessian.io.IOExceptionWrapper;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author junyuan
 * @version ThrowableDeserializer.java, v 0.1 2023年04月10日 20:37 junyuan Exp $
 */
public class ThrowableDeserializer extends AbstractFieldSpecificDeserializer {

    private final Class<?>  _type;
    protected Method        addSuppressed = null;

    private final Throwable selfRef       = new Throwable();

    public ThrowableDeserializer(Class cl) {
        super(cl);
        _type = cl;

        try {
            // since 1.7
            addSuppressed = Throwable.class.getDeclaredMethod("addSuppressed", Throwable.class);
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
            int ref = in.addRef(selfRef);
            Map<String, Object> fieldValueMap = readField(in, fieldNames);
            Throwable obj = instantiate(_type, fieldValueMap);
            fillFields(_type, obj, fieldValueMap);
            in.setRef(ref, obj);
            return obj;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOExceptionWrapper(Throwable.class.getName() + ":" + e, e);
        }
    }

    protected Map<String, Object> readField(AbstractHessianInput in, String[] fieldNames)
        throws IOException {
        Map<String, Object> fieldValueMap = new HashMap<String, Object>();
        for (int i = 0; i < fieldNames.length; i++) {
            String name = fieldNames[i];
            Field field = _fields.get(name);
            if (field == null) {
                continue;
            }

            if (String.class.equals(field.getType())) {
                fieldValueMap.put(name, in.readString());
            } else {
                fieldValueMap.put(name, in.readObject());
            }
        }
        return fieldValueMap;
    }

    protected Throwable instantiate(Class<?> clazz, Map<String, Object> fieldValueMap)
        throws Exception {
        Throwable ex = null;
        try {
            ex = doInstantiate(clazz, fieldValueMap);
        } catch (Exception instantiateException) {
            // todo: unsafe
        } finally {
            if (ex == null) {
                // 兜底返回 Throwable
                ex = new Throwable((String) fieldValueMap.get("detailMessage"), (Throwable) fieldValueMap.get("cause"));
            }
        }
        return ex;
    }

    protected void fillFields(Class<?> clazz, Throwable obj, Map<String, Object> valueMap)
        throws IOException {
        for (String key : valueMap.keySet()) {
            Object value = valueMap.get(key);
            if (value == null)
                continue;

            if (key.equals("cause")) {
                // 如果 cause 还未被写入, init
                if (value.equals(selfRef)) {
                    // 如果 cause 是自己, 跳过不写
                    continue;
                }

                if (obj.getCause() == null) {
                    try {
                        obj.initCause((Throwable) value);
                    } catch (Exception e) {
                        logDeserializeError(_fields.get(key), value, e);
                    }
                }
            }
            else if (key.equals("suppressedExceptions")) {
                // since 1.7
                try {
                    if (!(value instanceof List)) {
                        continue;
                    }
                    List listValue = (List) value;
                    if (listValue.size() == 0) {
                        continue;
                    }
                    if (addSuppressed != null) {
                        for (Object item : listValue) {
                            addSuppressed.invoke(obj, item);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (key.equals("stackTrace")) {
                obj.setStackTrace((StackTraceElement[]) value);
            }
            // 其他所有 field
            else {
                fillOtherFields(clazz, obj, key, value);
            }
        }
    }

    protected void fillOtherFields(Class<?> clazz, Throwable obj, String key, Object value)
        throws IOException {
        Field field = _fields.get(key);
        if (field == null) {
            return;
        }

        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            logDeserializeError(field, value, e);
        }
    }

    /**
     * 实例化, 这里只会返回 targetClass 的实例对象或者 null
     * 根据优先级分别使用构造函数
     * ExceptionClass(String message, Throwable cause)
     * ExceptionClass(String message)
     * ExceptionClass()
     * ExceptionClass(args...) 无法确认参数, 使用默认值进行构造, 优先级最低
     *
     * @param clazz
     * @param fieldValueMap
     * @return
     * @throws Exception
     */
    private Throwable doInstantiate(Class<?> clazz, Map<String, Object> fieldValueMap)
        throws Exception {
        Constructor<?> causeConstructor = null;
        Constructor<?> messageConstructor = null;
        Constructor<?> defaultConstructor = null;
        Constructor<?> constructorByCost = null;

        long bestCost = Long.MAX_VALUE;
        // 只会返回public的构造方法
        for (Constructor<?> c : clazz.getDeclaredConstructors()) {
            Class<?>[] pTypes = c.getParameterTypes();

            if (pTypes.length == 0) {
                defaultConstructor = c;
                continue;
            }

            if (pTypes.length == 1 && pTypes[0].equals(String.class)) {
                // Exception(String detailMessage)
                messageConstructor = c;
                continue;
            }

            if (pTypes.length == 2 && pTypes[0].equals(String.class) && pTypes[1].equals(Throwable.class)) {
                // Exception(String detailMessage, Throwable cause)
                causeConstructor = c;
                continue;
            }

            // 对于不是以上三种的构造方法, 根据JavaDeserializer的cost计算方式获取constructor
            if (calculateCost(pTypes) < bestCost) {
                constructorByCost = c;
            }
        }

        // 根据优先级调用
        String detailMessage = (String) fieldValueMap.get("detailMessage");
        Throwable cause = (Throwable) fieldValueMap.get("cause");
        if (causeConstructor != null) {
            return (Throwable) causeConstructor.newInstance(detailMessage, cause);
        }
        if (messageConstructor != null) {
            return (Throwable) messageConstructor.newInstance(detailMessage);
        }
        if (defaultConstructor != null) {
            return (Throwable) defaultConstructor.newInstance();
        }
        if (constructorByCost != null) {
            Object[] args = getConstructorArgs(constructorByCost);
            return (Throwable) constructorByCost.newInstance(args);
        }

        return null;
    }

    /**
     * get default arg value
     * @param c
     * @return
     */
    protected Object[] getConstructorArgs(Constructor c) {
        Class<?>[] pTypes = c.getParameterTypes();
        Object[] constructorArgs = new Object[pTypes.length];
        for (int i = 0; i < pTypes.length; i++) {
            constructorArgs[i] = getParamArg(pTypes[i]);
        }
        return constructorArgs;
    }

    /**
     * ref to {@link com.caucho.hessian.io.JavaDeserializer#JavaDeserializer(java.lang.Class)}
     * @param pTypes
     * @return
     */
    protected long calculateCost(Class<?>[] pTypes) {
        long cost = 0;

        for (int j = 0; j < pTypes.length; j++) {
            cost = 4 * cost;

            if (Object.class.equals(pTypes[j]))
                cost += 1;
            else if (String.class.equals(pTypes[j]))
                cost += 2;
            else if (int.class.equals(pTypes[j]))
                cost += 3;
            else if (long.class.equals(pTypes[j]))
                cost += 4;
            else if (pTypes[j].isPrimitive())
                cost += 5;
            else
                cost += 6;
        }

        if (cost < 0 || cost > (1 << 48))
            cost = 1 << 48;

        cost += pTypes.length << 48;
        return cost;
    }

    /**
     * ref to {@link com.caucho.hessian.io.JavaDeserializer#JavaDeserializer(java.lang.Class)}
     * @param cl
     * @return
     */
    protected Object getParamArg(Class cl) {
        if (!cl.isPrimitive())
            return null;
        else if (boolean.class.equals(cl))
            return Boolean.FALSE;
        else if (byte.class.equals(cl))
            return new Byte((byte) 0);
        else if (short.class.equals(cl))
            return new Short((short) 0);
        else if (char.class.equals(cl))
            return new Character((char) 0);
        else if (int.class.equals(cl))
            return Integer.valueOf(0);
        else if (long.class.equals(cl))
            return Long.valueOf(0);
        else if (float.class.equals(cl))
            return Float.valueOf(0);
        else if (double.class.equals(cl))
            return Double.valueOf(0);
        else
            throw new UnsupportedOperationException();
    }

    private void logDeserializeError(Field field, Object value, Throwable e) throws IOException {
        String fieldName = (field.getDeclaringClass().getName()
            + "." + field.getName());

        if (e instanceof HessianFieldException)
            throw (HessianFieldException) e;
        else if (e instanceof IOException)
            throw new HessianFieldException(fieldName + ": " + e.getMessage(), e);

        if (value != null)
            throw new HessianFieldException(fieldName + ": " + value.getClass().getName() + " (" + value + ")"
                + " cannot be assigned to " + field.getType().getName());
        else
            throw new HessianFieldException(fieldName + ": " + field.getType().getName() +
                " cannot be assigned from null", e);
    }
}