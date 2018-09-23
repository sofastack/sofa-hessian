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

import com.alipay.hessian.generic.bean.CollectionBean;
import com.alipay.hessian.generic.bean.SimplePerson;
import com.alipay.hessian.generic.bean.TestList;
import com.alipay.hessian.generic.bean.TestMap;
import com.alipay.hessian.generic.model.GenericArray;
import com.alipay.hessian.generic.model.GenericCollection;
import com.alipay.hessian.generic.model.GenericMap;
import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.hessian.generic.util.GenericUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alipay.hessian.generic.test.ComplexTestGO2GO.cmpGPersonEqualPerson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author xuanbei
 * @since 2016/11/09
 */
public class GenericUtilsTest {
    private static ComplexDataGenerator dg = new ComplexDataGenerator();

    @Test
    public void singlePerson() throws IOException {

        assertEqualsResult((Person) dg.generatePerson_1(),
            (GenericObject) dg.generateGenericPerson_1());

        assertEqualsResult((Person) dg.generatePerson_2(),
            (GenericObject) dg.generateGenericPerson_2());

        assertEqualsResult((Person) dg.generatePerson_3(),
            (GenericObject) dg.generateGenericPerson_3());

        assertEqualsResult((Person) dg.generatePerson_4(),
            (GenericObject) dg.generateGenericPerson_4());

        assertEqualsResult((Person) dg.generatePerson_5(),
            (GenericObject) dg.generateGenericPerson_5());

        assertEqualsResult((Person) dg.generatePerson_6(),
            (GenericObject) dg.generateGenericPerson_6());
    }

    private void assertEqualsResult(Person person, GenericObject genericObject) {
        GenericObject genericResult = GenericUtils.convertToGenericObject(person);
        cmpGPersonEqualPerson(genericResult, person);

        Person personResult = GenericUtils.convertToObject(genericObject);
        cmpGPersonEqualPerson(genericObject, personResult);
    }

    @Test
    public void singleMapAndCollection() throws IOException {
        assertEqualsResult((Person) dg.generatePersonWithCollection(),
            (GenericObject) dg.generateGenericPersonWithCollection());
    }

    @Test
    public void testCollectionBean() throws IOException {

        CollectionBean collectionBean = makeCollectionBean();
        GenericObject collectionGenericObject = makeCollectionGenericObject();

        GenericObject go = GenericUtils.convertToGenericObject(collectionBean);
        testCollectionGenericResult(go);

        Object obj = GenericUtils.convertToObject(collectionGenericObject);
        testCollectionBeanResult(obj);
    }

    @Test
    public void testList() throws IOException {

        TestList testList = new TestList();
        testList.add(12);
        testList.add("12");
        testList.add(new SimplePerson("zhao", "coder"));

        GenericCollection genericCollection = GenericUtils.convertToGenericObject(testList);
        assertEquals(3, genericCollection.getCollection().size());
        assertEquals(12, ((List) genericCollection.getCollection()).get(0));
        assertEquals("12", ((List) genericCollection.getCollection()).get(1));
        assertEquals(GenericObject.class.getName(),
            ((List) genericCollection.getCollection()).get(2).getClass().getName());

        GenericObject go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "zhao");
        go.putField("job", "coder");
        assertEquals(go, ((List) genericCollection.getCollection()).get(2));

        List list = new ArrayList();
        list.add(12);
        list.add("12");
        list.add(go);
        GenericCollection gc = new GenericCollection(TestList.class.getName());
        gc.setCollection(list);
        Collection collection = GenericUtils.convertToObject(gc);
        assertEquals(TestList.class.getName(), collection.getClass().getName());
        TestList co = (TestList) collection;
        assertEquals(3, co.size());
        assertEquals(12, co.get(0));
        assertEquals("12", co.get(1));
        assertEquals(new SimplePerson("zhao", "coder"), co.get(2));
    }

    @Test
    public void testMap() throws IOException {

        TestMap testMap = new TestMap();
        testMap.put("12", 12);
        testMap.put("15", new SimplePerson("zhao", "coder"));

        GenericMap gm = GenericUtils.convertToGenericObject(testMap);
        assertEquals(2, gm.getMap().size());
        assertEquals(12, gm.getMap().get("12"));
        GenericObject go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "zhao");
        go.putField("job", "coder");
        assertEquals(go, gm.getMap().get("15"));

        Map map = new HashMap();
        map.put("12", 12);
        map.put("15", go);
        GenericMap genericMap = new GenericMap(TestMap.class.getName());
        genericMap.setMap(map);
        Map m = GenericUtils.convertToObject(genericMap);
        assertEquals(TestMap.class.getName(), m.getClass().getName());
        TestMap tm = (TestMap) m;
        assertEquals(2, tm.size());
        assertEquals(12, tm.get("12"));
        assertEquals(new SimplePerson("zhao", "coder"), tm.get("15"));
    }

    @Test
    public void testArray() throws IOException {

        Object[] objects = new Object[5];
        objects[0] = "12";
        objects[1] = 12;
        objects[2] = new SimplePerson("zhao", "coder");

        GenericArray ga = GenericUtils.convertToGenericObject(objects);
        assertTrue(5 == ga.getLength());
        assertEquals(Object.class.getName(), ga.getComponentType());
        assertEquals("12", ga.getObjects()[0]);
        assertEquals(12, ga.getObjects()[1]);
        GenericObject go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "zhao");
        go.putField("job", "coder");
        assertEquals(go, ga.getObjects()[2]);
        assertEquals(null, ga.getObjects()[3]);
        assertEquals(null, ga.getObjects()[4]);

        objects = new Object[5];
        objects[0] = "12";
        objects[1] = 12;
        objects[2] = go;
        ga = new GenericArray(Object.class.getName());
        ga.setObjects(objects);
        Object obj = GenericUtils.convertToObject(ga);
        assertEquals(Object[].class.getName(), obj.getClass().getName());
        Object[] objs = (Object[]) obj;
        assertEquals(5, objs.length);
        assertEquals("12", objs[0]);
        assertEquals(12, objs[1]);
        assertEquals(new SimplePerson("zhao", "coder"), objs[2]);
        assertEquals(null, objs[3]);
        assertEquals(null, objs[4]);
    }

    private CollectionBean makeCollectionBean() {
        CollectionBean collectionBean = new CollectionBean();

        List<Integer> integers = new ArrayList<Integer>();
        integers.add(1);
        integers.add(2);
        collectionBean.setIntegers(integers);

        List<SimplePerson> simplePersons = new ArrayList<SimplePerson>();
        simplePersons.add(new SimplePerson("wang", "coder"));
        collectionBean.setSimplePersons(simplePersons);

        TestList<String> stringTestList = new TestList<String>();
        stringTestList.add("12345");
        collectionBean.setStringTestList(stringTestList);

        TestList<SimplePerson> simplePersonTestList = new TestList<SimplePerson>();
        simplePersonTestList.add(new SimplePerson("li", "teacher"));
        collectionBean.setSimplePersonTestList(simplePersonTestList);

        int[] intArray = new int[] { 1, 2, 3 };
        collectionBean.setIntArray(intArray);

        SimplePerson[] simplePersonArray = new SimplePerson[] { new SimplePerson("cao", "coder") };
        collectionBean.setSimplePersonArray(simplePersonArray);

        TestMap<String, SimplePerson> simplePersonTestMap = new TestMap<String, SimplePerson>();
        simplePersonTestMap.put("123", new SimplePerson("zhao", "coder"));
        collectionBean.setSimplePersonTestMap(simplePersonTestMap);

        return collectionBean;
    }

    private void testCollectionBeanResult(Object obj) {
        assertEquals(obj.getClass().getName(), CollectionBean.class.getName());

        CollectionBean collectionBean = (CollectionBean) obj;
        assertTrue(collectionBean.getIntegers().get(1).equals(2));
        assertEquals(new SimplePerson("wang", "coder"), collectionBean.getSimplePersons().get(0));
        assertEquals("12345", collectionBean.getStringTestList().get(0));
        assertEquals(new SimplePerson("li", "teacher"), collectionBean.getSimplePersonTestList()
            .get(0));
        assertEquals(collectionBean.getIntArray().length, 3);
        assertTrue(collectionBean.getIntArray()[1] == 2);
        assertEquals(new SimplePerson("cao", "coder"), collectionBean.getSimplePersonArray()[0]);
        assertEquals(new SimplePerson("zhao", "coder"), collectionBean.getSimplePersonTestMap()
            .get("123"));

    }

    private GenericObject makeCollectionGenericObject() {
        GenericObject genericObject = new GenericObject(CollectionBean.class.getName());

        List<Integer> integers = new ArrayList<Integer>();
        integers.add(1);
        integers.add(2);
        genericObject.putField("integers", integers);

        List simplePersons = new ArrayList();
        GenericObject go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "wang");
        go.putField("job", "coder");
        simplePersons.add(go);
        genericObject.putField("simplePersons", simplePersons);

        List<String> stringTestList = new ArrayList<String>();
        stringTestList.add("12345");
        GenericCollection gc = new GenericCollection(TestList.class.getName());
        gc.setCollection(stringTestList);
        genericObject.putField("stringTestList", gc);

        List simplePersonTestList = new ArrayList();
        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "li");
        go.putField("job", "teacher");
        simplePersonTestList.add(go);
        gc = new GenericCollection(TestList.class.getName());
        gc.setCollection(simplePersonTestList);
        genericObject.putField("simplePersonTestList", gc);

        int[] intArray = new int[] { 1, 2, 3 };
        genericObject.putField("intArray", intArray);

        GenericObject[] simplePersonArray = new GenericObject[1];
        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "cao");
        go.putField("job", "coder");
        simplePersonArray[0] = go;
        GenericArray ga = new GenericArray(SimplePerson.class.getName());
        ga.setObjects(simplePersonArray);
        genericObject.putField("simplePersonArray", ga);

        Map<String, GenericObject> simplePersonTestMap = new HashMap<String, GenericObject>();
        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "zhao");
        go.putField("job", "coder");
        simplePersonTestMap.put("123", go);
        GenericMap gm = new GenericMap(TestMap.class.getName());
        gm.setMap(simplePersonTestMap);
        genericObject.putField("simplePersonTestMap", gm);

        return genericObject;
    }

    private void testCollectionGenericResult(GenericObject genericObject) {
        assertEquals(CollectionBean.class.getName(), genericObject.getType());
        assertEquals(2, ((List) genericObject.getField("integers")).size());
        assertEquals(1, ((List) genericObject.getField("integers")).get(0));
        assertEquals(2, ((List) genericObject.getField("integers")).get(1));

        GenericObject go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "wang");
        go.putField("job", "coder");
        assertEquals(go, ((List) genericObject.getField("simplePersons")).get(0));

        assertEquals(GenericCollection.class.getName(), genericObject.getField("stringTestList")
            .getClass().getName());
        assertEquals("12345",
            ((ArrayList) ((GenericCollection) genericObject.getField("stringTestList"))
                .getCollection()).get(0));

        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "li");
        go.putField("job", "teacher");
        assertEquals(go,
            ((ArrayList) ((GenericCollection) genericObject.getField("simplePersonTestList"))
                .getCollection()).get(0));

        assertEquals(int[].class.getName(), genericObject.getField("intArray").getClass().getName());
        assertEquals(2, ((int[]) genericObject.getField("intArray"))[1]);

        assertEquals(GenericArray.class.getName(), genericObject.getField("simplePersonArray")
            .getClass().getName());
        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "cao");
        go.putField("job", "coder");
        assertEquals(go,
            ((GenericArray) genericObject.getField("simplePersonArray")).getObjects()[0]);

        assertEquals(GenericMap.class.getName(), genericObject.getField("simplePersonTestMap")
            .getClass().getName());
        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "zhao");
        go.putField("job", "coder");
        assertEquals(go,
            ((GenericMap) genericObject.getField("simplePersonTestMap")).getMap().get("123"));
    }
}
