/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    String        str;

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