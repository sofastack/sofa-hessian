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
package com.alipay.hessian.generic.test;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by qiwei.lqw on 2016/7/4.
 */
public class Person implements Serializable {
    private static final long    serialVersionUID = 0L;

    private String               name;
    private int                  age;
    private String               gender;
    private Map<String, Integer> scores;
    private Person               friend;
    private Pet                  pet;

    private Map<String, Object>  mapValue;
    private List<Object>         listValue;

    public Person() {
    }

    public Person(String name, int age, String gender, Map<String, Integer> scores, Person friend,
                  Pet pet) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.scores = scores;
        this.friend = friend;
        this.pet = pet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public Person getFriend() {
        return friend;
    }

    public void setFriend(Person friend) {
        this.friend = friend;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Map<String, Object> getMapValue() {
        return mapValue;
    }

    public void setMapValue(Map<String, Object> mapValue) {
        this.mapValue = mapValue;
    }

    public List<Object> getListValue() {
        return listValue;
    }

    public void setListValue(List<Object> listValue) {
        this.listValue = listValue;
    }

    @Override
    public String toString() {
        return name + " " + age + " " + gender + " " + scores + " " + pet + " friend: "
            + (friend == null ? "none" : friend.name);
    }
}
