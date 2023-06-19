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
package com.caucho.hessian.test.atomic;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 *
 * @author junyuan
 * @version AtomicWrapper.java, v 0.1 2023年03月29日 18:15 junyuan Exp $
 */
public class AtomicWrapper implements Serializable {
    private AtomicInteger        aInteger;
    private AtomicBoolean        aBoolean;
    private AtomicLong           aLong;
    private AtomicReference      aReference;
    private AtomicIntegerArray   aIntegerArray;
    private AtomicLongArray      aLongArray;
    private AtomicReferenceArray aReferenceArray;

    /**
     * Getter method for property <tt>aInteger</tt>.
     *
     * @return property value of aInteger
     */
    public AtomicInteger getaInteger() {
        return aInteger;
    }

    /**
     * Setter method for property <tt>aInteger</tt>.
     *
     * @param aInteger  value to be assigned to property aInteger
     */
    public void setaInteger(AtomicInteger aInteger) {
        this.aInteger = aInteger;
    }

    /**
     * Getter method for property <tt>aBoolean</tt>.
     *
     * @return property value of aBoolean
     */
    public AtomicBoolean getaBoolean() {
        return aBoolean;
    }

    /**
     * Setter method for property <tt>aBoolean</tt>.
     *
     * @param aBoolean  value to be assigned to property aBoolean
     */
    public void setaBoolean(AtomicBoolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    /**
     * Getter method for property <tt>aLong</tt>.
     *
     * @return property value of aLong
     */
    public AtomicLong getaLong() {
        return aLong;
    }

    /**
     * Setter method for property <tt>aLong</tt>.
     *
     * @param aLong  value to be assigned to property aLong
     */
    public void setaLong(AtomicLong aLong) {
        this.aLong = aLong;
    }

    /**
     * Getter method for property <tt>aReference</tt>.
     *
     * @return property value of aReference
     */
    public AtomicReference getaReference() {
        return aReference;
    }

    /**
     * Setter method for property <tt>aReference</tt>.
     *
     * @param aReference  value to be assigned to property aReference
     */
    public void setaReference(AtomicReference aReference) {
        this.aReference = aReference;
    }

    /**
     * Getter method for property <tt>aIntegerArray</tt>.
     *
     * @return property value of aIntegerArray
     */
    public AtomicIntegerArray getaIntegerArray() {
        return aIntegerArray;
    }

    /**
     * Setter method for property <tt>aIntegerArray</tt>.
     *
     * @param aIntegerArray  value to be assigned to property aIntegerArray
     */
    public void setaIntegerArray(AtomicIntegerArray aIntegerArray) {
        this.aIntegerArray = aIntegerArray;
    }

    /**
     * Getter method for property <tt>aLongArray</tt>.
     *
     * @return property value of aLongArray
     */
    public AtomicLongArray getaLongArray() {
        return aLongArray;
    }

    /**
     * Setter method for property <tt>aLongArray</tt>.
     *
     * @param aLongArray  value to be assigned to property aLongArray
     */
    public void setaLongArray(AtomicLongArray aLongArray) {
        this.aLongArray = aLongArray;
    }

    /**
     * Getter method for property <tt>aReferenceArray</tt>.
     *
     * @return property value of aReferenceArray
     */
    public AtomicReferenceArray getaReferenceArray() {
        return aReferenceArray;
    }

    /**
     * Setter method for property <tt>aReferenceArray</tt>.
     *
     * @param aReferenceArray  value to be assigned to property aReferenceArray
     */
    public void setaReferenceArray(AtomicReferenceArray aReferenceArray) {
        this.aReferenceArray = aReferenceArray;
    }
}