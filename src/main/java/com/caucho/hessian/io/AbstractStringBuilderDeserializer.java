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
    private static final Logger log = Logger.getLogger(AbstractStringBuilderDeserializer.class.getName());

    private static final boolean ENABLE = judgeAvailability();

    /**
     * 判断是否要使用该反序列化器, 当 String.value 类型不为 char[] 时需要使用
     * @return
     */
    private static boolean judgeAvailability() {
        Field field;
        try {
            field = String.class.getDeclaredField("value");
        } catch (Throwable t) {
            return false;
        }

        if (char[].class.equals(field.getType())) {
            return false;
        }

        return true;
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
        try {
            valueField = cl.getSuperclass().getDeclaredField("value");
        } catch (Exception e) {
            log.log(Level.WARNING, "get value field field", e);
            return fieldMap;
        }

        fieldMap.put("value", new StringBuilderValueFieldDeserializer(valueField));
        return fieldMap;
    }

    /**
     * 针对 value field 定制的 field deserializer
     * 读取 value field 时, 根据传入数据进行读取, 读取到值后进行转换
     */
    static class StringBuilderValueFieldDeserializer extends FieldDeserializer {
        private final Field _field;

        StringBuilderValueFieldDeserializer(Field field) {
            _field = field;
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
                    byte[] res = hessianString2Bytes((String) value);
                    _field.set(obj, res);
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
         * 按照 hessian 序列化的处理方式将 String 还原为 byte[]
         * {@link Hessian2Output#printString(java.lang.String, int, int)}
         * @param str
         * @return
         */
        private byte[] hessianString2Bytes(String str) {
            byte[] buffer = new byte[str.length()];
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);

                if (ch < 0x80)
                    buffer[i] = (byte) (ch);
                else if (ch < 0x800) {
                    buffer[i] = (byte) (0xc0 + ((ch >> 6) & 0x1f));
                    buffer[i] = (byte) (0x80 + (ch & 0x3f));
                } else {
                    buffer[i] = (byte) (0xe0 + ((ch >> 12) & 0xf));
                    buffer[i] = (byte) (0x80 + ((ch >> 6) & 0x3f));
                    buffer[i] = (byte) (0x80 + (ch & 0x3f));
                }
            }
            return buffer;
        }
    }

}