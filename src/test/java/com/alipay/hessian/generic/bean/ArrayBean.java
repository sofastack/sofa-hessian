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
package com.alipay.hessian.generic.bean;

import java.io.Serializable;

/**
 * @author xuanbei
 * @since 2016/12/28
 */
public class ArrayBean implements Serializable {
    private String[]           str1;
    private String[][]         str2;
    private String[][][]       str3;

    private Object[]           obj1;
    private Object[][]         obj2;
    private Object[][][]       obj3;

    private int[]              int1;
    private int[][]            int2;
    private int[][][]          int3;

    private SimplePerson[]     simplePerson1;
    private SimplePerson[][]   simplePerson2;
    private SimplePerson[][][] simplePerson3;

    public String[] getStr1() {
        return str1;
    }

    public void setStr1(String[] str1) {
        this.str1 = str1;
    }

    public String[][] getStr2() {
        return str2;
    }

    public void setStr2(String[][] str2) {
        this.str2 = str2;
    }

    public String[][][] getStr3() {
        return str3;
    }

    public void setStr3(String[][][] str3) {
        this.str3 = str3;
    }

    public Object[] getObj1() {
        return obj1;
    }

    public void setObj1(Object[] obj1) {
        this.obj1 = obj1;
    }

    public Object[][] getObj2() {
        return obj2;
    }

    public void setObj2(Object[][] obj2) {
        this.obj2 = obj2;
    }

    public Object[][][] getObj3() {
        return obj3;
    }

    public void setObj3(Object[][][] obj3) {
        this.obj3 = obj3;
    }

    public int[] getInt1() {
        return int1;
    }

    public void setInt1(int[] int1) {
        this.int1 = int1;
    }

    public int[][] getInt2() {
        return int2;
    }

    public void setInt2(int[][] int2) {
        this.int2 = int2;
    }

    public int[][][] getInt3() {
        return int3;
    }

    public void setInt3(int[][][] int3) {
        this.int3 = int3;
    }

    public SimplePerson[] getSimplePerson1() {
        return simplePerson1;
    }

    public void setSimplePerson1(SimplePerson[] simplePerson1) {
        this.simplePerson1 = simplePerson1;
    }

    public SimplePerson[][] getSimplePerson2() {
        return simplePerson2;
    }

    public void setSimplePerson2(SimplePerson[][] simplePerson2) {
        this.simplePerson2 = simplePerson2;
    }

    public SimplePerson[][][] getSimplePerson3() {
        return simplePerson3;
    }

    public void setSimplePerson3(SimplePerson[][][] simplePerson3) {
        this.simplePerson3 = simplePerson3;
    }
}
