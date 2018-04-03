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

import com.alipay.hessian.generic.model.GenericObject;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author xuanbei
 * @since 2016/12/27
 */
public class ComplexBean implements Serializable {

    private SimplePerson  person1;
    private SimplePerson  person2;

    private String[]      str1;
    private String[]      str2;

    private TestList      testList1;
    private TestList      testList2;

    private int[]         ints;
    private Integer[]     integers;

    private GenericObject genericObject;

    public ComplexBean() {
    }

    public ComplexBean(SimplePerson person1, SimplePerson person2, String[] str1, String[] str2,
                       TestList testList1, TestList testList2, int[] ints, Integer[] integers,
                       GenericObject genericObject) {
        this.person1 = person1;
        this.person2 = person2;
        this.str1 = str1;
        this.str2 = str2;
        this.testList1 = testList1;
        this.testList2 = testList2;
        this.ints = ints;
        this.integers = integers;
        this.genericObject = genericObject;
    }

    public SimplePerson getPerson1() {
        return person1;
    }

    public void setPerson1(SimplePerson person1) {
        this.person1 = person1;
    }

    public SimplePerson getPerson2() {
        return person2;
    }

    public void setPerson2(SimplePerson person2) {
        this.person2 = person2;
    }

    public String[] getStr1() {
        return str1;
    }

    public void setStr1(String[] str1) {
        this.str1 = str1;
    }

    public String[] getStr2() {
        return str2;
    }

    public void setStr2(String[] str2) {
        this.str2 = str2;
    }

    public TestList getTestList1() {
        return testList1;
    }

    public void setTestList1(TestList testList1) {
        this.testList1 = testList1;
    }

    public TestList getTestList2() {
        return testList2;
    }

    public void setTestList2(TestList testList2) {
        this.testList2 = testList2;
    }

    public int[] getInts() {
        return ints;
    }

    public void setInts(int[] ints) {
        this.ints = ints;
    }

    public Integer[] getIntegers() {
        return integers;
    }

    public void setIntegers(Integer[] integers) {
        this.integers = integers;
    }

    public GenericObject getGenericObject() {
        return genericObject;
    }

    public void setGenericObject(GenericObject genericObject) {
        this.genericObject = genericObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ComplexBean that = (ComplexBean) o;

        if (person1 != null ? !person1.equals(that.person1) : that.person1 != null)
            return false;
        if (person2 != null ? !person2.equals(that.person2) : that.person2 != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(str1, that.str1))
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(str2, that.str2))
            return false;
        if (testList1 != null ? !testList1.equals(that.testList1) : that.testList1 != null)
            return false;
        if (testList2 != null ? !testList2.equals(that.testList2) : that.testList2 != null)
            return false;
        if (!Arrays.equals(ints, that.ints))
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(integers, that.integers))
            return false;
        return genericObject != null ? genericObject.equals(that.genericObject)
            : that.genericObject == null;

    }

    @Override
    public int hashCode() {
        int result = person1 != null ? person1.hashCode() : 0;
        result = 31 * result + (person2 != null ? person2.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(str1);
        result = 31 * result + Arrays.hashCode(str2);
        result = 31 * result + (testList1 != null ? testList1.hashCode() : 0);
        result = 31 * result + (testList2 != null ? testList2.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(ints);
        result = 31 * result + Arrays.hashCode(integers);
        result = 31 * result + (genericObject != null ? genericObject.hashCode() : 0);
        return result;
    }
}
