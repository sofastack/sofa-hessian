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

import com.alipay.hessian.generic.bean.ComplexBean;
import com.alipay.hessian.generic.bean.SimplePerson;
import com.alipay.hessian.generic.bean.TestList;
import com.alipay.hessian.generic.bean.TestMap;
import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.alipay.hessian.generic.model.GenericArray;
import com.alipay.hessian.generic.model.GenericCollection;
import com.alipay.hessian.generic.model.GenericMap;
import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.hessian.generic.util.GenericUtils;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author xuanbei
 * @since 2016/12/24
 */
public class CollectionTest {
    @Test
    public void testCollection() throws Exception {

        TestList testList = new TestList();
        testList.add(1);
        testList.add(new SimplePerson("wang", "coder"));

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(testList);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertTrue(o.getClass() == GenericCollection.class);
        GenericCollection genericCollection = (GenericCollection) o;
        assertEquals(genericCollection.getType(), TestList.class.getName());
        assertEquals(genericCollection.getCollection().size(), 2);
        assertEquals(((ArrayList) genericCollection.getCollection()).get(0), 1);
        assertEquals(((ArrayList) genericCollection.getCollection()).get(1).getClass(),
            GenericObject.class);

        GenericObject genericObject = (GenericObject) ((ArrayList) genericCollection
            .getCollection()).get(1);
        assertEquals(genericObject.getType(), SimplePerson.class.getName());
        assertEquals(genericObject.getField("name"), "wang");
        assertEquals(genericObject.getField("job"), "coder");
    }

    @Test
    public void testGenericCollection() throws Exception {

        TestList testList = new TestList();
        testList.add(1);
        testList.add(new SimplePerson("wang", "coder"));

        GenericCollection collection = new GenericCollection(TestList.class.getName());
        collection.setCollection(testList);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(collection);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertTrue(o.getClass() == GenericCollection.class);
        GenericCollection genericCollection = (GenericCollection) o;
        assertEquals(genericCollection.getType(), TestList.class.getName());
        assertEquals(genericCollection.getCollection().size(), 2);
        assertEquals(((ArrayList) genericCollection.getCollection()).get(0), 1);
        assertEquals(((ArrayList) genericCollection.getCollection()).get(1).getClass(),
            GenericObject.class);

        GenericObject genericObject = (GenericObject) ((ArrayList) genericCollection
            .getCollection()).get(1);
        assertEquals(genericObject.getType(), SimplePerson.class.getName());
        assertEquals(genericObject.getField("name"), "wang");
        assertEquals(genericObject.getField("job"), "coder");
    }

    @Test
    public void testMap() throws Exception {

        TestMap testMap = new TestMap();
        testMap.put("1", new SimplePerson("wang", "coder"));

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(testMap);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertTrue(o.getClass() == GenericMap.class);
        GenericMap genericMap = (GenericMap) o;
        assertEquals(genericMap.getType(), TestMap.class.getName());
        assertEquals(genericMap.getMap().size(), 1);
        assertTrue(genericMap.getMap().get("1").getClass() == GenericObject.class);

        GenericObject genericObject = (GenericObject) genericMap.getMap().get("1");
        assertEquals(genericObject.getType(), SimplePerson.class.getName());
        assertEquals(genericObject.getField("name"), "wang");
        assertEquals(genericObject.getField("job"), "coder");
    }

    @Test
    public void testMapGeneric() throws Exception {

        TestMap testMap = new TestMap();
        testMap.put("1", new SimplePerson("wang", "coder"));

        GenericMap map = new GenericMap(TestMap.class.getName());
        map.setMap(testMap);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(map);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertTrue(o.getClass() == GenericMap.class);
        GenericMap genericMap = (GenericMap) o;
        assertEquals(genericMap.getType(), TestMap.class.getName());
        assertEquals(genericMap.getMap().size(), 1);
        assertTrue(genericMap.getMap().get("1").getClass() == GenericObject.class);

        GenericObject genericObject = (GenericObject) genericMap.getMap().get("1");
        assertEquals(genericObject.getType(), SimplePerson.class.getName());
        assertEquals(genericObject.getField("name"), "wang");
        assertEquals(genericObject.getField("job"), "coder");
    }

    @Test
    public void testPersonArray() throws Exception {

        Object[] persons = new Object[2];
        persons[0] = new SimplePerson("zhao", "coder");
        persons[1] = new SimplePerson("li", "coder");

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(persons);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertTrue(o.getClass() == GenericArray.class);
        GenericArray genericArray = (GenericArray) o;
        assertEquals(Object.class.getName(), genericArray.getComponentType());
        assertTrue(2 == genericArray.getLength());
        GenericObject go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "zhao");
        go.putField("job", "coder");
        assertEquals(go, genericArray.getObjects()[0]);
        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "li");
        go.putField("job", "coder");
        assertEquals(go, genericArray.getObjects()[1]);
    }

    @Test
    public void testStringArray() throws Exception {

        Object[] strs = new String[3];
        strs[0] = "11111";
        strs[1] = "22222";

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(strs);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertTrue(o.getClass() == String[].class);
        String[] s = (String[]) o;
        assertTrue(3 == s.length);
        assertEquals("11111", s[0]);
        assertEquals("22222", s[1]);
        assertEquals(null, s[2]);
    }

    @Test
    public void testGenericStringArray() throws Exception {

        Object[] strs = new String[3];
        strs[0] = "11111";
        strs[1] = "22222";

        GenericArray collection = new GenericArray(String.class.getName());
        collection.setObjects(strs);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(collection);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertTrue(o.getClass() == String[].class);
        String[] strings = (String[]) o;
        assertTrue(3 == strings.length);
        assertEquals("11111", strings[0]);
        assertEquals("22222", strings[1]);
        assertEquals(null, strings[2]);
    }

    @Test
    public void testGenericPersonArray() throws Exception {

        Object[] persons = new Object[4];
        GenericObject go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "zhao");
        go.putField("job", "coder");
        persons[0] = go;
        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "li");
        go.putField("job", "coder");
        persons[1] = go;

        GenericArray collection = new GenericArray(Object.class.getName());
        collection.setObjects(persons);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(collection);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertTrue(o.getClass() == GenericArray.class);
        GenericArray genericArray = (GenericArray) o;
        assertTrue(4 == genericArray.getLength());
        assertEquals(Object.class.getName(), genericArray.getComponentType());
        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "zhao");
        go.putField("job", "coder");
        assertEquals(go, genericArray.getObjects()[0]);
        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "li");
        go.putField("job", "coder");
        assertEquals(go, genericArray.getObjects()[1]);
        assertEquals(null, genericArray.getObjects()[2]);
        assertEquals(null, genericArray.getObjects()[3]);
    }

    @Test
    public void test2StringArray() throws Exception {

        Object[][] strs = new String[2][3];
        strs[0][0] = "11111";
        strs[1][1] = "22222";

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(strs);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertTrue(o.getClass() == String[][].class);
        String[][] s = (String[][]) o;
        assertEquals("11111", s[0][0]);
        assertEquals("22222", s[1][1]);

    }

    @Test
    public void test2ObjectArray() throws Exception {

        Object[][] strs = new Object[2][3];
        strs[0][0] = "11111";
        strs[1][1] = "22222";

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(strs);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertTrue(o.getClass() == GenericArray.class);
        GenericArray ga1 = (GenericArray) o;
        assertEquals(Object[].class.getName(), ga1.getComponentType());
        assertEquals(2, ga1.getLength());
        GenericArray ga2 = (GenericArray) ga1.getObjects()[0];
        assertEquals(3, ga2.getLength());
        assertEquals("11111", ga2.getObjects()[0]);
    }

    @Test
    public void test2MixArray() throws Exception {

        Object[][] objs = new Object[3][];
        objs[0] = new Object[] { 1, "22", null };
        objs[1] = new String[] { "33", "44" };
        objs[2] = new SimplePerson[] { new SimplePerson("wang", "coder") };

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(objs);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertTrue(o.getClass() == GenericArray.class);
        GenericArray ga1 = (GenericArray) o;
        doAssertMixGenericArray(ga1);

        Object[][] object = objs;
        GenericArray genericArray = (GenericArray) o;
        Object[][] objectsResult = GenericUtils.convertToObject(genericArray);
        doAssertMixArray(objectsResult);

        GenericArray genericArrayResult = GenericUtils.convertToGenericObject(object);
        doAssertMixGenericArray(genericArrayResult);

        bout = new ByteArrayOutputStream();
        hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(genericArrayResult);
        hout.close();

        body = bout.toByteArray();
        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Object[][] oo = (Object[][]) hin.readObject();
        doAssertMixArray(oo);
    }

    private void doAssertMixArray(Object[][] objectsResult) {
        assertEquals(3, objectsResult.length);
        assertEquals(3, objectsResult[0].length);
        assertEquals(1, objectsResult[0][0]);
        assertEquals("22", objectsResult[0][1]);
        assertEquals(null, objectsResult[0][2]);
        assertEquals("33", objectsResult[1][0]);
        assertEquals("44", objectsResult[1][1]);
        assertEquals(2, objectsResult[1].length);
        assertEquals(new SimplePerson("wang", "coder"), objectsResult[2][0]);
    }

    private void doAssertMixGenericArray(GenericArray ga) {
        assertEquals(Object[].class.getName(), ga.getComponentType());
        assertEquals(3, ga.getLength());
        GenericArray ga2 = (GenericArray) ga.getObjects()[0];
        assertEquals(3, ga2.getLength());
        assertEquals(1, ga2.getObjects()[0]);
        assertEquals("22", ga2.getObjects()[1]);
        assertEquals(null, ga2.getObjects()[2]);

        assertEquals(String[].class.getName(), ga.getObjects()[1].getClass().getName());
        String[] ss = (String[]) ga.getObjects()[1];
        assertEquals("33", ss[0]);
        assertEquals("44", ss[1]);

        assertEquals(GenericArray.class.getName(), ga.getObjects()[2].getClass().getName());
        GenericArray ga3 = (GenericArray) ga.getObjects()[2];
        assertEquals(1, ga3.getLength());
        GenericObject go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "wang");
        go.putField("job", "coder");
        assertEquals(go, ga3.get(0));
    }

    @Test
    public void testComplexBean() throws Exception {

        GenericObject genericObject = new GenericObject(SimplePerson.class.getName());
        genericObject.putField("name", "zhao");
        genericObject.putField("job", "coder");

        ComplexBean complexBean = new ComplexBean(new SimplePerson("zhao", "coder"),
            new SimplePerson("wang", "coder"), new String[] { "11111" }, new String[] { "22222" },
            new TestList(), new TestList(), new int[] { 1, 2 }, new Integer[] { 1, 2 },
            genericObject);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());
        hout.writeObject(complexBean);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertGenericComplexBean(o);
        GenericObject result = (GenericObject) o;

        // 检测GenericUtils工具正确性
        ComplexBean cb = GenericUtils.convertToObject(result);
        assertEquals(complexBean, cb);

        GenericObject g = GenericUtils.convertToGenericObject(complexBean);
        assertGenericComplexBean(g);
    }

    private void assertGenericComplexBean(Object o) {
        assertTrue(o.getClass() == GenericObject.class);
        GenericObject result = (GenericObject) o;
        assertEquals(result.getType(), ComplexBean.class.getName());

        GenericObject go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "zhao");
        go.putField("job", "coder");
        assertEquals(go, result.getField("person1"));

        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "wang");
        go.putField("job", "coder");
        assertEquals(go, result.getField("person2"));

        assertEquals(result.getField("str1").getClass().getName(), String[].class.getName());
        assertEquals(result.getField("str2").getClass().getName(), String[].class.getName());

        assertEquals("11111", ((String[]) result.getField("str1"))[0]);
        assertEquals("22222", ((String[]) result.getField("str2"))[0]);

        assertEquals(result.getField("testList1").getClass(), GenericCollection.class);
        assertEquals(result.getField("testList2").getClass(), GenericCollection.class);

        assertEquals(((GenericCollection) result.getField("testList1")).getType(),
            TestList.class.getName());
        assertEquals(((GenericCollection) result.getField("testList2")).getType(),
            TestList.class.getName());

        assertEquals(int[].class.getName(), result.getField("ints").getClass().getName());
        assertEquals(Integer[].class.getName(), result.getField("integers").getClass().getName());

        assertEquals(GenericObject.class.getName(), result.getField("genericObject").getClass()
            .getName());
        assertEquals(GenericObject.class.getName(),
            ((GenericObject) result.getField("genericObject")).getType());
        assertEquals(SimplePerson.class.getName(),
            ((GenericObject) result.getField("genericObject")).getField("type"));
        assertEquals(TreeMap.class.getName(), ((GenericObject) result.getField("genericObject"))
            .getField("fields").getClass().getName());
        assertEquals("zhao",
            ((TreeMap) ((GenericObject) result.getField("genericObject")).getField("fields"))
                .get("name"));
        assertEquals("coder",
            ((TreeMap) ((GenericObject) result.getField("genericObject")).getField("fields"))
                .get("job"));
    }
}
