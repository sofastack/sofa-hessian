/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junyuan
 * @version AbstractStringBuilderDeserializer.java, v 0.1 2023年10月20日 11:31 junyuan Exp $
 */
public class AbstractStringBuilderDeserializer extends JavaDeserializer {
    private static final Logger  log    = Logger.getLogger(AbstractStringBuilderDeserializer.class.getName());

    private static final boolean ENABLE = judgeAvailability();
    /** String 的 value field, 用以判断是否需要用当前 deserializer */
    private static Field         stringValueField;
    /** String 的 coder field, 用以从中间 String 变量中获取 coder */
    private static Field         stringCoderField;

    static {
        try {
            stringCoderField = String.class.getDeclaredField("coder");
            stringCoderField.setAccessible(true);
        } catch (Throwable t) {
            log.log(Level.WARNING,
                "coder field not found or not accessible, will skip coder check, error is " + t.getMessage());
        }
    }

    /**
     * 判断是否要使用该反序列化器, 当 String.value 类型不为 char[] 时需要使用
     * @return
     */
    private static boolean judgeAvailability() {
        try {
            stringValueField = String.class.getDeclaredField("value");
            stringValueField.setAccessible(true);
        } catch (Throwable t) {
            return false;
        }

        if (byte[].class.equals(stringValueField.getType())) {
            return true;
        }

        return false;
    }

    public static boolean isEnable() {
        return ENABLE;
    }

    public AbstractStringBuilderDeserializer(Class<?> cl) {
        super(cl);
    }

    @Override
    protected HashMap getFieldMap(Class cl) {
        HashMap fieldMap = super.getFieldMap(cl);
        Field valueField = null;
        valueField = getAbstractStringBuilderField(cl, "value");
        if (valueField == null) {
            log.log(Level.WARNING, "get value field failed");
            return fieldMap;
        }

        Field coderField = null;
        if (fieldMap.containsKey("coder")) {
            coderField = getAbstractStringBuilderField(cl, "coder");
        }

        fieldMap.put("value", new StringBuilderValueFieldDeserializer(valueField, coderField));
        return fieldMap;
    }

    /**
     * 获取 AbstractStingBuilder 类内的 field
     * @param cl
     * @param fieldName
     * @return
     */
    private Field getAbstractStringBuilderField(Class cl, String fieldName) {
        Field field = null;
        try {
            field = cl.getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (Throwable t) {
            log.log(Level.WARNING, "get " + fieldName + " field failed", t);
            return null;
        }
        return field;
    }

    /**
     * 针对 value field 定制的 field deserializer
     * 读取 value field 时, 根据传入数据进行读取, 读取到值后进行转换
     */
    static class StringBuilderValueFieldDeserializer extends FieldDeserializer {
        /**
         * 这个 _field 会是 AbstractStringBuilder.value
         */
        private final Field _field;

        /**
         * _coderField 会是 AbstractStringBuilder.coder 字段
         */
        private final Field _coderField;

        StringBuilderValueFieldDeserializer(Field value, Field coder) {
            _field = value;
            _coderField = coder;
        }

        @Override
        void deserialize(AbstractHessianInput in, Object obj) throws IOException {
            Object value = null;

            try {
                // hessian 在读取 char 时会用 utf-8 编码
                value = in.readObject();
                if (value == null) {
                    return;
                }

                // 理论上获取到的值有两种情况: String 或者 byte[]
                if (value instanceof String) {
                    dealWithStringValue((String) value, obj);
                } else if (value instanceof byte[]) {
                    _field.set(obj, value);
                } else {
                    throw new UnsupportedEncodingException("未知的编码类型" + value.getClass());
                }
            } catch (Exception e) {
                logDeserializeError(_field, obj, value, e);
            }
        }

        /**
         * 如果读取到的是 String, 需要通过 String.coder 进行编码
         */
        public void dealWithStringValue(String value, Object obj) {
            try {
                byte[] res = (byte[]) stringValueField.get(value);
                _field.set(obj, res);

                if (stringCoderField != null) {
                    byte coder = (byte) stringCoderField.getByte(value);
                    _coderField.set(obj, coder);
                }
            } catch (Throwable t) {

            }
        }
    }

}