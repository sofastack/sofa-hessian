/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.java17.lang;

import java.io.Serializable;

/**
 *
 * @author junyuan
 * @version StringBuilderWrapper.java, v 0.1 2023年10月23日 16:59 junyuan Exp $
 */
public class StringBuilderWrapper implements Serializable {
    StringBuilder stringBuilder;

    String str;

    /**
     * Getter method for property <tt>stringBuilder</tt>.
     *
     * @return property value of stringBuilder
     */
    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    /**
     * Setter method for property <tt>stringBuilder</tt>.
     *
     * @param stringBuilder  value to be assigned to property stringBuilder
     */
    public void setStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    /**
     * Getter method for property <tt>str</tt>.
     *
     * @return property value of str
     */
    public String getStr() {
        return str;
    }

    /**
     * Setter method for property <tt>str</tt>.
     *
     * @param str  value to be assigned to property str
     */
    public void setStr(String str) {
        this.str = str;
    }
}