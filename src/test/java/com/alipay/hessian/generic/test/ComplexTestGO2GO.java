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

import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.alipay.hessian.generic.model.GenericObject;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by qiwei.lqw on 2016/7/1.
 * Serialize: GenericHessian --> Deserialize: GenericHessian
 */
public class ComplexTestGO2GO {

    private static ComplexDataGenerator dg = new ComplexDataGenerator();

    @Test
    public void singlePerson() throws IOException {
        Object gp1 = dg.generateGenericPerson_1();
        Object gp2 = dg.generateGenericPerson_2();
        Object gp3 = dg.generateGenericPerson_3();
        Object gp4 = dg.generateGenericPerson_4();
        Object gp5 = dg.generateGenericPerson_5();
        Object gp6 = dg.generateGenericPerson_6();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(gp1);
        hout.writeObject(gp2);
        hout.writeObject(gp3);
        hout.writeObject(gp4);
        hout.writeObject(gp5);
        hout.writeObject(gp6);

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        GenericObject dp1 = (GenericObject) hin.readObject();
        GenericObject dp2 = (GenericObject) hin.readObject();
        GenericObject dp3 = (GenericObject) hin.readObject();
        GenericObject dp4 = (GenericObject) hin.readObject();
        GenericObject dp5 = (GenericObject) hin.readObject();
        GenericObject dp6 = (GenericObject) hin.readObject();

        cmpGPersonEqualPerson(dp1, (Person) dg.generatePerson_1());
        cmpGPersonEqualPerson(dp2, (Person) dg.generatePerson_2());
        cmpGPersonEqualPerson(dp3, (Person) dg.generatePerson_3());
        cmpGPersonEqualPerson(dp4, (Person) dg.generatePerson_4());
        cmpGPersonEqualPerson(dp5, (Person) dg.generatePerson_5());
        cmpGPersonEqualPerson(dp6, (Person) dg.generatePerson_6());

    }

    @Test
    public void testMap() throws IOException {
        Object go = dg.generateMapGenericPerson_1();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(go);

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Map dmap = (Map) hin.readObject();
        Map map = (Map) dg.generateMapPerson_1();

        assertEquals(dmap.size(), map.size());
        cmpGPersonEqualPerson((GenericObject) dmap.get(1), (Person) map.get(1));
        cmpGPersonEqualPerson((GenericObject) dmap.get(2), (Person) map.get(2));

        Map dmap2 = (Map) dmap.get("map");
        Map map2 = (Map) map.get("map");
        assertEquals(dmap2.size(), map2.size());
        cmpGPersonEqualPerson((GenericObject) dmap2.get("lll"), (Person) map2.get("lll"));
        cmpGPersonEqualPerson((GenericObject) dmap2.get("qqq"), (Person) map2.get("qqq"));
        cmpGPersonEqualPerson((GenericObject) dmap2.get("www"), (Person) map2.get("www"));
        assertSame(dmap2.get("lll"), dmap2.get("www"));
    }

    @Test
    public void testList() throws IOException {
        Object go = dg.generateListGenericPerson_1();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(go);

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        List dlist = (List) hin.readObject();
        List list = (List) dg.generateListPerson_1();

        assertEquals(dlist.size(), list.size());
        cmpGPersonEqualPerson((GenericObject) dlist.get(0), (Person) list.get(0));
        cmpGPersonEqualPerson((GenericObject) dlist.get(1), (Person) list.get(1));

        List dlist2 = (List) dlist.get(2);
        List list2 = (List) list.get(2);
        assertEquals(dlist2.size(), list2.size());
        cmpGPersonEqualPerson((GenericObject) dlist2.get(0), (Person) list2.get(0));
        cmpGPersonEqualPerson((GenericObject) dlist2.get(1), (Person) list2.get(1));
        cmpGPersonEqualPerson((GenericObject) dlist2.get(2), (Person) list2.get(2));
        assertSame(dlist2.get(0), dlist2.get(2));

    }

    // @Test
    // Not Supported
    public void testArray() throws IOException {
        Object go = dg.generateArrayGenericPerson_1();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(go);

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Object[] darr = (Object[]) hin.readObject();
        Object[] arr = (Object[]) dg.generateArrayPerson_1();

        assertEquals(darr.length, arr.length);
        cmpGPersonEqualPerson((GenericObject) darr[0], (Person) arr[0]);
        cmpGPersonEqualPerson((GenericObject) darr[1], (Person) arr[1]);

        Object[] darr2 = (Object[]) darr[2];
        Object[] arr2 = (Object[]) arr[2];
        assertEquals(darr2.length, arr2.length);
        cmpGPersonEqualPerson((GenericObject) darr2[0], (Person) arr2[0]);
        cmpGPersonEqualPerson((GenericObject) darr2[1], (Person) arr2[1]);
        cmpGPersonEqualPerson((GenericObject) darr2[2], (Person) arr2[2]);
        assertSame(darr2[0], darr2[2]);

    }

    // @Test
    // Not Supported
    public void testMix() throws IOException {
        Object obj = dg.generateMixGenericObject();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(obj);

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object[][] darr = (Object[][]) hin.readObject();
        Object[][] arr = (Object[][]) dg.generateMixObject();

        assertEquals(darr.length, 3);
        assertEquals(darr[0].length, 3);

        assertSame(darr, darr[2][0]);
        cmpGPersonEqualPerson((GenericObject) darr[2][1], (Person) arr[2][1]);
        cmpGPersonEqualPerson((GenericObject) darr[2][2], (Person) arr[2][2]);

        Object[] darr1 = (Object[]) darr[1][0];
        Map dmap1 = (Map) darr[1][1];
        List dlist1 = (List) darr[1][2];

        Object[] darr2 = (Object[]) darr1[2];
        assertSame(darr2[2], dmap1);
        Map dmap2 = (Map) dmap1.get(3);
        assertSame(dmap2.get("www"), dlist1);
        List dlist2 = (List) dlist1.get(2);
        assertSame(dlist2.get(2), darr1);

        cmpGPersonEqualPerson((GenericObject) darr1[0], (Person) ((Object[]) arr[1][0])[0]);
        assertSame(darr1[0], darr2[0]);
        assertSame(darr1[0], dmap1.get(1));
        assertSame(darr1[0], dmap2.get("lll"));
        assertSame(darr1[0], dlist1.get(0));
        assertSame(darr1[0], dlist2.get(0));

        cmpGPersonEqualPerson((GenericObject) darr1[1], (Person) ((Object[]) arr[1][0])[1]);
        assertSame(darr1[1], darr2[1]);
        assertSame(darr1[1], dmap1.get(2));
        assertSame(darr1[1], dmap2.get("qqq"));
        assertSame(darr1[1], dlist1.get(1));
        assertSame(darr1[1], dlist2.get(1));

        //System.out.println(darr1[1]);

    }

    public static void cmpGPersonEqualPerson(GenericObject gp, Person p) {
        assertTrue(cmpGPersonEqualPerson(gp, p, new HashSet<GenericObject>(), new HashSet<Object>()));
    }

    public static boolean cmpGPersonEqualPerson(GenericObject gp, Person p,
                                                Set<GenericObject> set1, Set<Object> set2) {
        if (gp == null && p == null)
            return true;
        if (gp == null || p == null)
            return false;

        if (!gp.getType().equals(Person.class.getName()))
            return false;
        if (set1.contains(gp) && set2.contains(p))
            return true;
        if (set1.contains(gp) || set2.contains(p))
            return false;

        set1.add(gp);
        set2.add(p);
        assertEquals(gp.getField("name"), p.getName());
        assertEquals(gp.getField("age"), p.getAge());
        assertEquals(gp.getField("gender"), p.getGender());
        assertEquals(gp.getField("scores"), p.getScores());

        assertTrue(cmpGPetEqualPet((GenericObject) gp.getField("pet"), p.getPet(), set1, set2));
        assertTrue(cmpGPersonEqualPerson((GenericObject) gp.getField("friend"), p.getFriend(),
            set1, set2));
        assertCollectionValueEqual(gp, p, set1, set2);

        return true;
    }

    private static void assertCollectionValueEqual(GenericObject gp, Person p,
                                                   Set<GenericObject> set1, Set<Object> set2) {

        Map<String, Object> mapValue = p.getMapValue();
        Map mapValueGeneric = (Map) gp.getField("mapValue");
        int times = 0;

        if (mapValue != null && mapValueGeneric != null) {

            assertTrue(mapValue.size() == mapValueGeneric.size());

            for (Map.Entry<String, Object> entry : mapValue.entrySet()) {
                if (entry.getValue() instanceof Person) {
                    cmpGPersonEqualPerson((GenericObject) mapValueGeneric.get(entry.getKey()),
                        (Person) entry.getValue(), set1, set2);
                    times++;
                }
                if (entry.getValue() instanceof Pet) {
                    cmpGPetEqualPet((GenericObject) mapValueGeneric.get(entry.getKey()),
                        (Pet) entry.getValue(), set1, set2);
                    times++;
                }
            }

            assertTrue(times == 2);
            times = 0;
        } else if ((mapValue == null && mapValueGeneric != null)
            || (mapValue != null && mapValueGeneric == null)) {
            fail("not equals!");
        }

        List<Object> listValue = p.getListValue();
        List listValueGeneric = (List) gp.getField("listValue");
        if (listValue != null && listValueGeneric != null) {

            assertTrue(listValue.size() == listValueGeneric.size());

            for (int i = 0; i < listValue.size(); i++) {
                if (listValue.get(i) instanceof Person) {
                    cmpGPersonEqualPerson((GenericObject) listValueGeneric.get(i),
                        (Person) listValue.get(i), set1, set2);
                    times++;
                }
                if (listValue.get(i) instanceof Pet) {
                    cmpGPetEqualPet((GenericObject) listValueGeneric.get(i),
                        (Pet) listValue.get(i), set1, set2);
                    times++;
                }
            }

            assertTrue(times == 2);
        } else if ((listValue != null && listValueGeneric == null)
            || (listValue == null && listValueGeneric != null)) {
            fail("not equals!");
        }

    }

    public static boolean cmpGPetEqualPet(GenericObject gp, Pet p, Set<GenericObject> set1,
                                          Set<Object> set2) {
        if (gp == null && p == null)
            return true;
        if (gp == null || p == null)
            return false;

        if (!gp.getType().equals(Pet.class.getName()))
            return false;
        if (set1.contains(gp) && set2.contains(p))
            return true;
        if (set1.contains(gp) || set2.contains(p))
            return false;

        set1.add(gp);
        set2.add(p);

        assertEquals(gp.getField("name"), p.getName());
        assertEquals(gp.getField("type"), p.getType());
        assertTrue(cmpGPersonEqualPerson((GenericObject) gp.getField("owner"), p.getOwner(), set1,
            set2));
        return true;
    }
}
