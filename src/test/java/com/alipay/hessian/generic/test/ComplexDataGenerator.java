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

import com.alipay.hessian.generic.model.GenericArray;
import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by qiwei.lqw on 2016/7/5.
 */
public class ComplexDataGenerator {

    public Object generatePerson_1() {
        return new Person();
    }

    public Object generateGenericPerson_1() {
        GenericObject gp = new GenericObject(Person.class.getName());
        gp.putField("name", null);
        gp.putField("age", 0);
        gp.putField("gender", null);
        gp.putField("scores", null);
        gp.putField("pet", null);
        gp.putField("friend", null);
        return gp;
    }

    public Object generatePerson_2() {
        Person p = new Person();
        p.setName("xiaoming");
        p.setAge(24);
        p.setGender("male");
        p.setPet(null);
        p.setFriend(null);
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        p.setScores(scores);
        return p;
    }

    public Object generateGenericPerson_2() {
        GenericObject gp = new GenericObject(Person.class.getName());
        gp.putField("name", "xiaoming");
        gp.putField("age", 24);
        gp.putField("gender", "male");
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        gp.putField("scores", scores);
        gp.putField("pet", null);
        gp.putField("friend", null);
        return gp;
    }

    public Object generatePerson_3() {
        Person hua = new Person();
        hua.setName("xiaohua");
        hua.setAge(23);
        hua.setGender("female");
        hua.setScores(null);
        hua.setPet(null);
        hua.setFriend(null);

        Person p = new Person();
        p.setName("xiaoming");
        p.setAge(24);
        p.setGender("male");
        p.setPet(null);
        p.setFriend(hua);
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        p.setScores(scores);
        return p;
    }

    public Object generateGenericPerson_3() {
        GenericObject hua = new GenericObject(Person.class.getName());
        hua.putField("name", "xiaohua");
        hua.putField("age", 23);
        hua.putField("gender", "female");
        hua.putField("scores", null);
        hua.putField("pet", null);
        hua.putField("friend", null);

        GenericObject gp = new GenericObject(Person.class.getName());
        gp.putField("name", "xiaoming");
        gp.putField("age", 24);
        gp.putField("gender", "male");
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        gp.putField("scores", scores);
        gp.putField("pet", null);
        gp.putField("friend", hua);
        return gp;
    }

    public Object generatePerson_4() {
        Person hua = new Person();
        Person p = new Person();

        hua.setName("xiaohua");
        hua.setAge(23);
        hua.setGender("female");
        hua.setScores(null);
        hua.setPet(null);
        hua.setFriend(p);

        p.setName("xiaoming");
        p.setAge(24);
        p.setGender("male");
        p.setPet(null);
        p.setFriend(hua);
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        p.setScores(scores);
        return p;
    }

    public Object generateGenericPerson_4() {
        GenericObject hua = new GenericObject(Person.class.getName());
        GenericObject gp = new GenericObject(Person.class.getName());

        hua.putField("name", "xiaohua");
        hua.putField("age", 23);
        hua.putField("gender", "female");
        hua.putField("scores", null);
        hua.putField("pet", null);
        hua.putField("friend", gp);

        gp.putField("name", "xiaoming");
        gp.putField("age", 24);
        gp.putField("gender", "male");
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        gp.putField("scores", scores);
        gp.putField("pet", null);
        gp.putField("friend", hua);
        return gp;
    }

    public Object generatePerson_5() {
        Person hua = new Person();
        Person p = new Person();
        Pet wang = new Pet();

        hua.setName("xiaohua");
        hua.setAge(23);
        hua.setGender("female");
        hua.setScores(null);
        hua.setPet(wang);
        hua.setFriend(p);

        wang.setName("wangwang");
        wang.setType("dog");
        wang.setOwner(p);

        p.setName("xiaoming");
        p.setAge(24);
        p.setGender("male");
        p.setPet(wang);
        p.setFriend(hua);
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        p.setScores(scores);
        return p;
    }

    public Object generateGenericPerson_5() {
        GenericObject hua = new GenericObject(Person.class.getName());
        GenericObject gp = new GenericObject(Person.class.getName());
        GenericObject wang = new GenericObject(Pet.class.getName());

        hua.putField("name", "xiaohua");
        hua.putField("age", 23);
        hua.putField("gender", "female");
        hua.putField("scores", null);
        hua.putField("pet", wang);
        hua.putField("friend", gp);

        wang.putField("name", "wangwang");
        wang.putField("type", "dog");
        wang.putField("owner", gp);

        gp.putField("name", "xiaoming");
        gp.putField("age", 24);
        gp.putField("gender", "male");
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        gp.putField("scores", scores);
        gp.putField("pet", wang);
        gp.putField("friend", hua);
        return gp;
    }

    public Object generatePerson_6() {
        Person hua = new Person();
        Person p = new Person();
        Pet wang = new Pet();
        Pet miao = new Pet();

        hua.setName("xiaohua");
        hua.setAge(23);
        hua.setGender("female");
        hua.setScores(null);
        hua.setPet(miao);
        hua.setFriend(p);

        miao.setName("miaomiao");
        miao.setType("cat");
        miao.setOwner(p);

        wang.setName("wangwang");
        wang.setType("dog");
        wang.setOwner(hua);

        p.setName("xiaoming");
        p.setAge(24);
        p.setGender("male");
        p.setPet(wang);
        p.setFriend(hua);
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        p.setScores(scores);
        return p;
    }

    public Object generateGenericPerson_6() {
        GenericObject hua = new GenericObject(Person.class.getName());
        GenericObject gp = new GenericObject(Person.class.getName());
        GenericObject wang = new GenericObject(Pet.class.getName());
        GenericObject miao = new GenericObject(Pet.class.getName());

        hua.putField("name", "xiaohua");
        hua.putField("age", 23);
        hua.putField("gender", "female");
        hua.putField("scores", null);
        hua.putField("pet", miao);
        hua.putField("friend", gp);

        miao.putField("name", "miaomiao");
        miao.putField("type", "cat");
        miao.putField("owner", gp);

        wang.putField("name", "wangwang");
        wang.putField("type", "dog");
        wang.putField("owner", hua);

        gp.putField("name", "xiaoming");
        gp.putField("age", 24);
        gp.putField("gender", "male");
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        gp.putField("scores", scores);
        gp.putField("pet", wang);
        gp.putField("friend", hua);
        return gp;
    }

    //map
    public Object generateMapPerson_1() {
        Map<Object, Object> map1 = new HashMap<Object, Object>();
        Map<Object, Object> map2 = new TreeMap<Object, Object>();

        Person hua = new Person();
        Person p = new Person();
        Pet wang = new Pet();
        Pet miao = new Pet();

        hua.setName("xiaohua");
        hua.setAge(23);
        hua.setGender("female");
        hua.setScores(null);
        hua.setPet(miao);
        hua.setFriend(p);

        miao.setName("miaomiao");
        miao.setType("cat");
        miao.setOwner(p);

        wang.setName("wangwang");
        wang.setType("dog");
        wang.setOwner(hua);

        p.setName("xiaoming");
        p.setAge(24);
        p.setGender("male");
        p.setPet(wang);
        p.setFriend(hua);
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        p.setScores(scores);

        map1.put(1, p);
        map1.put(2, hua);
        map1.put("map", map2);
        map2.put("lll", p);
        map2.put("qqq", hua);
        map2.put("www", p);

        return map1;
    }

    public Object generateMapGenericPerson_1() {
        Map<Object, Object> map1 = new HashMap<Object, Object>();
        Map<Object, Object> map2 = new TreeMap<Object, Object>();

        GenericObject hua = new GenericObject(Person.class.getName());
        GenericObject gp = new GenericObject(Person.class.getName());
        GenericObject wang = new GenericObject(Pet.class.getName());
        GenericObject miao = new GenericObject(Pet.class.getName());

        hua.putField("name", "xiaohua");
        hua.putField("age", 23);
        hua.putField("gender", "female");
        hua.putField("scores", null);
        hua.putField("pet", miao);
        hua.putField("friend", gp);

        miao.putField("name", "miaomiao");
        miao.putField("type", "cat");
        miao.putField("owner", gp);

        wang.putField("name", "wangwang");
        wang.putField("type", "dog");
        wang.putField("owner", hua);

        gp.putField("name", "xiaoming");
        gp.putField("age", 24);
        gp.putField("gender", "male");
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        gp.putField("scores", scores);
        gp.putField("pet", wang);
        gp.putField("friend", hua);

        map1.put(1, gp);
        map1.put(2, hua);
        map1.put("map", map2);
        map2.put("lll", gp);
        map2.put("qqq", hua);
        map2.put("www", gp);

        return map1;
    }

    //list
    public Object generateListPerson_1() {
        List<Object> list1 = new ArrayList<Object>();
        List<Object> list2 = new LinkedList<Object>();

        Person hua = new Person();
        Person p = new Person();
        Pet wang = new Pet();
        Pet miao = new Pet();

        hua.setName("xiaohua");
        hua.setAge(23);
        hua.setGender("female");
        hua.setScores(null);
        hua.setPet(miao);
        hua.setFriend(p);

        miao.setName("miaomiao");
        miao.setType("cat");
        miao.setOwner(p);

        wang.setName("wangwang");
        wang.setType("dog");
        wang.setOwner(hua);

        p.setName("xiaoming");
        p.setAge(24);
        p.setGender("male");
        p.setPet(wang);
        p.setFriend(hua);
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        p.setScores(scores);

        list1.add(p);
        list1.add(hua);
        list1.add(list2);
        list2.add(p);
        list2.add(hua);
        list2.add(p);

        return list1;
    }

    public Object generateListGenericPerson_1() {
        List<Object> list1 = new ArrayList<Object>();
        List<Object> list2 = new LinkedList<Object>();

        GenericObject hua = new GenericObject(Person.class.getName());
        GenericObject gp = new GenericObject(Person.class.getName());
        GenericObject wang = new GenericObject(Pet.class.getName());
        GenericObject miao = new GenericObject(Pet.class.getName());

        hua.putField("name", "xiaohua");
        hua.putField("age", 23);
        hua.putField("gender", "female");
        hua.putField("scores", null);
        hua.putField("pet", miao);
        hua.putField("friend", gp);

        miao.putField("name", "miaomiao");
        miao.putField("type", "cat");
        miao.putField("owner", gp);

        wang.putField("name", "wangwang");
        wang.putField("type", "dog");
        wang.putField("owner", hua);

        gp.putField("name", "xiaoming");
        gp.putField("age", 24);
        gp.putField("gender", "male");
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        gp.putField("scores", scores);
        gp.putField("pet", wang);
        gp.putField("friend", hua);

        list1.add(gp);
        list1.add(hua);
        list1.add(list2);
        list2.add(gp);
        list2.add(hua);
        list2.add(gp);

        return list1;
    }

    //array
    public Object generateArrayPerson_1() {
        Object[] arr1 = new Object[3];
        Object[] arr2 = new Object[3];

        Person hua = new Person();
        Person p = new Person();
        Pet wang = new Pet();
        Pet miao = new Pet();

        hua.setName("xiaohua");
        hua.setAge(23);
        hua.setGender("female");
        hua.setScores(null);
        hua.setPet(miao);
        hua.setFriend(p);

        miao.setName("miaomiao");
        miao.setType("cat");
        miao.setOwner(p);

        wang.setName("wangwang");
        wang.setType("dog");
        wang.setOwner(hua);

        p.setName("xiaoming");
        p.setAge(24);
        p.setGender("male");
        p.setPet(wang);
        p.setFriend(hua);
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        p.setScores(scores);

        arr1[0] = p;
        arr1[1] = hua;
        arr1[2] = arr2;
        arr2[0] = p;
        arr2[1] = hua;
        arr2[2] = p;

        return arr1;
    }

    public Object generateArrayGenericPerson_1() {
        Object[] arr1 = new Object[3];
        Object[] arr2 = new Object[3];

        GenericObject hua = new GenericObject(Person.class.getName());
        GenericObject gp = new GenericObject(Person.class.getName());
        GenericObject wang = new GenericObject(Pet.class.getName());
        GenericObject miao = new GenericObject(Pet.class.getName());

        hua.putField("name", "xiaohua");
        hua.putField("age", 23);
        hua.putField("gender", "female");
        hua.putField("scores", null);
        hua.putField("pet", miao);
        hua.putField("friend", gp);

        miao.putField("name", "miaomiao");
        miao.putField("type", "cat");
        miao.putField("owner", gp);

        wang.putField("name", "wangwang");
        wang.putField("type", "dog");
        wang.putField("owner", hua);

        gp.putField("name", "xiaoming");
        gp.putField("age", 24);
        gp.putField("gender", "male");
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        gp.putField("scores", scores);
        gp.putField("pet", wang);
        gp.putField("friend", hua);

        arr1[0] = gp;
        arr1[1] = hua;
        arr1[2] = arr2;
        arr2[0] = gp;
        arr2[1] = hua;
        arr2[2] = gp;

        return arr1;
    }

    //mix
    public Object generateMixObject() {
        Person hua = new Person();
        Person ming = new Person();
        Pet wang = new Pet();
        Pet miao = new Pet();

        hua.setName("xiaohua");
        hua.setAge(23);
        hua.setGender("female");
        hua.setScores(null);
        hua.setPet(miao);
        hua.setFriend(ming);

        miao.setName("miaomiao");
        miao.setType("cat");
        miao.setOwner(ming);

        wang.setName("wangwang");
        wang.setType("dog");
        wang.setOwner(hua);

        ming.setName("xiaoming");
        ming.setAge(24);
        ming.setGender("male");
        ming.setPet(wang);
        ming.setFriend(hua);
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        ming.setScores(scores);

        Object[] arr1 = new Object[3];
        Object[] arr2 = new Object[3];
        Map map1 = new HashMap();
        Map map2 = new TreeMap();
        List list1 = new ArrayList();
        List list2 = new LinkedList();

        arr1[0] = ming;
        arr2[0] = ming;
        map1.put(1, ming);
        map2.put("lll", ming);
        list1.add(ming);
        list2.add(ming);

        arr1[1] = hua;
        arr2[1] = hua;
        map1.put(2, hua);
        map2.put("qqq", hua);
        list1.add(hua);
        list2.add(hua);

        arr1[2] = arr2;
        arr2[2] = map1;
        map1.put(3, map2);
        map2.put("www", list1);
        list1.add(list2);
        list2.add(arr1);

        Object[][] arr = new Object[3][3];
        arr[1][0] = arr1;
        arr[1][1] = map1;
        arr[1][2] = list1;
        arr[2][0] = arr;
        arr[2][1] = ming;
        arr[2][2] = hua;

        return arr;
    }

    public Object generateMixGenericObject() {
        GenericObject hua = new GenericObject(Person.class.getName());
        GenericObject ming = new GenericObject(Person.class.getName());
        GenericObject wang = new GenericObject(Pet.class.getName());
        GenericObject miao = new GenericObject(Pet.class.getName());

        hua.putField("name", "xiaohua");
        hua.putField("age", 23);
        hua.putField("gender", "female");
        hua.putField("scores", null);
        hua.putField("pet", miao);
        hua.putField("friend", ming);

        miao.putField("name", "miaomiao");
        miao.putField("type", "cat");
        miao.putField("owner", ming);

        wang.putField("name", "wangwang");
        wang.putField("type", "dog");
        wang.putField("owner", hua);

        ming.putField("name", "xiaoming");
        ming.putField("age", 24);
        ming.putField("gender", "male");
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        ming.putField("scores", scores);
        ming.putField("pet", wang);
        ming.putField("friend", hua);

        Object[] arr1 = new Object[3];
        Object[] arr2 = new Object[3];
        Map map1 = new HashMap();
        Map map2 = new TreeMap();
        List list1 = new ArrayList();
        List list2 = new LinkedList();

        arr1[0] = ming;
        arr2[0] = ming;
        map1.put(1, ming);
        map2.put("lll", ming);
        list1.add(ming);
        list2.add(ming);

        arr1[1] = hua;
        arr2[1] = hua;
        map1.put(2, hua);
        map2.put("qqq", hua);
        list1.add(hua);
        list2.add(hua);

        //generate circle reference
        arr1[2] = arr2;
        arr2[2] = map1;
        map1.put(3, map2);
        map2.put("www", list1);
        list1.add(list2);
        list2.add(arr1);

        Object[][] arr = new Object[3][3];
        arr[1][0] = arr1;
        arr[1][1] = map1;
        arr[1][2] = list1;
        arr[2][0] = arr;
        arr[2][1] = ming;
        arr[2][2] = hua;

        return arr;
    }

    // SofaRequest
    public SofaRequest generateSofaRequest() {
        SofaRequest request = new SofaRequest();

        request.setTargetServiceUniqueName("liqiwei");
        request.setMethodName("sayHello");
        request.setTargetAppName("HelloService");

        request.setMethodArgSigs(new String[] { "com.lqw.testPerson", "int" });
        request.setMethodArgs(new Object[] { generateGenericPerson_6(), 1992 });
        request.setMethod(null);

        request.addRequestProps("trace", new HashMap<String, String>());

        return request;
    }

    // SofaResponse
    public SofaResponse generateSofaResponse() {
        SofaResponse response = new SofaResponse();

        response.setErrorMsg("just_error");
        response.setAppResponse(generatePerson_6());

        return response;
    }

    // GenericObjectArray
    public GenericObject[] generateGenericObjectArray() {
        GenericObject[] goArr = new GenericObject[3];

        goArr[0] = (GenericObject) generateGenericPerson_4();
        goArr[1] = (GenericObject) generateGenericPerson_5();
        goArr[2] = (GenericObject) generateGenericPerson_6();

        return goArr;
    }

    // GenericArray
    public GenericArray generateGenericArray() {

        Object[] objects = new Object[3];
        objects[0] = generateGenericPerson_4();
        objects[1] = generateGenericPerson_5();
        objects[2] = generateGenericPerson_6();

        GenericArray ga = new GenericArray(Person.class.getName());
        ga.setObjects(objects);
        return ga;
    }

    public Object generatePersonWithCollection() {
        Person hua = new Person();
        hua.setName("xiaohua");
        hua.setAge(23);
        hua.setGender("female");
        hua.setScores(null);
        hua.setPet(null);
        hua.setFriend(null);

        Person p = new Person();
        p.setName("xiaoming");
        p.setAge(24);
        p.setGender("male");
        p.setPet(null);
        p.setFriend(hua);
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        p.setScores(scores);

        Map<String, Object> mapValue = new HashMap<String, Object>();
        mapValue.put("1", generatePerson_2());
        mapValue.put("2", newPet());
        p.setMapValue(mapValue);

        List<Object> listValue = new ArrayList<Object>();
        listValue.add(generatePerson_2());
        listValue.add(newPet());
        p.setListValue(listValue);

        return p;
    }

    public Object generateGenericPersonWithCollection() {
        GenericObject hua = new GenericObject(Person.class.getName());
        hua.putField("name", "xiaohua");
        hua.putField("age", 23);
        hua.putField("gender", "female");
        hua.putField("scores", null);
        hua.putField("pet", null);
        hua.putField("friend", null);

        GenericObject gp = new GenericObject(Person.class.getName());
        gp.putField("name", "xiaoming");
        gp.putField("age", 24);
        gp.putField("gender", "male");
        Map<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("math", 10);
        scores.put("program", 10);
        gp.putField("scores", scores);
        gp.putField("pet", null);
        gp.putField("friend", hua);

        Map<String, Object> mapValue = new HashMap<String, Object>();
        mapValue.put("1", generateGenericPerson_2());
        mapValue.put("2", newGenericPet());
        gp.putField("mapValue", mapValue);

        List<Object> listValue = new ArrayList<Object>();
        listValue.add(generateGenericPerson_2());
        listValue.add(newGenericPet());
        gp.putField("listValue", listValue);

        return gp;
    }

    public Pet newPet() {
        Pet wang = new Pet();
        wang.setName("wangwang");
        wang.setType("dog");
        return wang;
    }

    public GenericObject newGenericPet() {
        GenericObject genericPet = new GenericObject(Pet.class.getName());
        genericPet.putField("name", "wangwang");
        genericPet.putField("type", "dog");
        return genericPet;
    }
}
