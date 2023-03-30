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
package com.caucho.hessian.test.java8;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 *
 * @author junyuan
 * @version AtomicWrapper.java, v 0.1 2023年03月29日 18:15 junyuan Exp $
 */
public class AtomicWrapper implements Serializable {
    private AtomicInteger      aInteger;
    private AtomicBoolean      aBoolean;
    private AtomicLong         aLong;
    private AtomicIntegerArray aIntegerArray;
    private AtomicLongArray    aLongArray;

    /**
     * Getter method for property <tt>ai</tt>.
     *
     * @return property value of ai
     */
    public AtomicInteger getaInteger() {
        return aInteger;
    }

    /**
     * Setter method for property <tt>ai</tt>.
     *
     * @param aInteger  value to be assigned to property ai
     */
    public void setaInteger(AtomicInteger aInteger) {
        this.aInteger = aInteger;
    }

    /**
     * Getter method for property <tt>ab</tt>.
     *
     * @return property value of ab
     */
    public AtomicBoolean getaBoolean() {
        return aBoolean;
    }

    /**
     * Setter method for property <tt>ab</tt>.
     *
     * @param aBoolean  value to be assigned to property ab
     */
    public void setaBoolean(AtomicBoolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    /**
     * Getter method for property <tt>al</tt>.
     *
     * @return property value of al
     */
    public AtomicLong getaLong() {
        return aLong;
    }

    /**
     * Setter method for property <tt>al</tt>.
     *
     * @param aLong  value to be assigned to property al
     */
    public void setaLong(AtomicLong aLong) {
        this.aLong = aLong;
    }

    /**
     * Getter method for property <tt>aia</tt>.
     *
     * @return property value of aia
     */
    public AtomicIntegerArray getaIntegerArray() {
        return aIntegerArray;
    }

    /**
     * Setter method for property <tt>aia</tt>.
     *
     * @param aIntegerArray  value to be assigned to property aia
     */
    public void setaIntegerArray(AtomicIntegerArray aIntegerArray) {
        this.aIntegerArray = aIntegerArray;
    }

    /**
     * Getter method for property <tt>ala</tt>.
     *
     * @return property value of ala
     */
    public AtomicLongArray getaLongArray() {
        return aLongArray;
    }

    /**
     * Setter method for property <tt>ala</tt>.
     *
     * @param aLongArray  value to be assigned to property ala
     */
    public void setaLongArray(AtomicLongArray aLongArray) {
        this.aLongArray = aLongArray;
    }
}