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

import java.util.List;
import java.util.Map;

/**
 * @author xuanbei
 * @since 2016/12/26
 */
public class CollectionBean {

    private List<Integer>                 integers;
    private List<SimplePerson>            simplePersons;

    private TestList<String>              stringTestList;
    private TestList<SimplePerson>        simplePersonTestList;

    private int[]                         intArray;
    private SimplePerson[]                simplePersonArray;

    private Map<String, String>           stringMap;
    private TestMap<String, SimplePerson> simplePersonTestMap;

    public List<Integer> getIntegers() {
        return integers;
    }

    public void setIntegers(List<Integer> integers) {
        this.integers = integers;
    }

    public List<SimplePerson> getSimplePersons() {
        return simplePersons;
    }

    public void setSimplePersons(List<SimplePerson> simplePersons) {
        this.simplePersons = simplePersons;
    }

    public TestList<String> getStringTestList() {
        return stringTestList;
    }

    public void setStringTestList(TestList<String> stringTestList) {
        this.stringTestList = stringTestList;
    }

    public TestList<SimplePerson> getSimplePersonTestList() {
        return simplePersonTestList;
    }

    public void setSimplePersonTestList(TestList<SimplePerson> simplePersonTestList) {
        this.simplePersonTestList = simplePersonTestList;
    }

    public int[] getIntArray() {
        return intArray;
    }

    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }

    public SimplePerson[] getSimplePersonArray() {
        return simplePersonArray;
    }

    public void setSimplePersonArray(SimplePerson[] simplePersonArray) {
        this.simplePersonArray = simplePersonArray;
    }

    public Map<String, String> getStringMap() {
        return stringMap;
    }

    public void setStringMap(Map<String, String> stringMap) {
        this.stringMap = stringMap;
    }

    public TestMap<String, SimplePerson> getSimplePersonTestMap() {
        return simplePersonTestMap;
    }

    public void setSimplePersonTestMap(TestMap<String, SimplePerson> simplePersonTestMap) {
        this.simplePersonTestMap = simplePersonTestMap;
    }
}
