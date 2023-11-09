/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;

/**
 *
 * @author junyuan
 * @version AbstractStringBuilderSerializer.java, v 0.1 2023年10月20日 11:31 junyuan Exp $
 */
public class AbstractStringBuilderSerializer extends AbstractFieldAdaptorSerializer {

    private static final boolean ENABLE = judgeAvailability();

    public static boolean isEnable() {
        return ENABLE;
    }

    public AbstractStringBuilderSerializer(Class cl) {
        super(cl);
        for (Field field : _fields) {
            try {
                field.setAccessible(true);
            } catch (Throwable t) {
                log.log(Level.INFO, "unable to set field {} accessible", field.getName());
            }
        }

    }

    @Override
    protected void serializeField(AbstractHessianOutput out, Object obj, Field field)
        throws IOException {
        if ("value".equals(field.getName())) {
            serializeValueArray(out, obj);
        } else {
            serializeNormalField(out, obj, field);
        }
    }

    /**
     * 将底层 value 数组转为 char数组, 并以 writeString 方式进行序列化
     * 保证序列化结果与普通 JavaSerializer 保持一致
     *
     * @param out
     * @param obj
     * @throws IOException
     */
    protected void serializeValueArray(AbstractHessianOutput out, Object obj)
        throws IOException {
        if (obj instanceof StringBuilder) {
            StringBuilder sb = (StringBuilder) obj;
            // 要用实际底层 value 数据的长度以保持一致
            char[] dst = new char[sb.capacity()];
            sb.getChars(0, sb.length(), dst, 0);

            out.writeString(dst, 0, dst.length);
        } else if (obj instanceof StringBuffer) {
            StringBuffer sb = (StringBuffer) obj;
            char[] dst = new char[sb.capacity()];
            sb.getChars(0, sb.length(), dst, 0);

            out.writeString(dst, 0, dst.length);
        } else {
            throw new UnsupportedOperationException("only support AbstractStringBuilder but got " + obj.getClass());
        }
    }

    /**
     * 常规字段以 object 的方式序列化
     * @param out
     * @param obj
     * @param field
     * @throws IOException
     */
    private void serializeNormalField(AbstractHessianOutput out, Object obj, Field field) throws IOException {
        Object value = null;

        try {
            value = field.get(obj);
        } catch (IllegalAccessException e) {
            log.log(Level.FINE, e.toString(), e);
        }

        out.writeObject(value);
    }

    /**
     * 判断是否要使用该反序列化器, 当 String.value 类型为 byte[] 时需要使用
     * @return
     */
    private static boolean judgeAvailability() {
        Field field = null;
        try {
            field = String.class.getDeclaredField("value");
        } catch (Throwable t) {
            return false;
        }

        if (byte[].class.equals(field.getType())) {
            return true;
        }

        return false;
    }
}