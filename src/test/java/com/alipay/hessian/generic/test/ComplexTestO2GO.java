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
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Created by qiwei.lqw on 2016/7/5.
 * Serialize: Hessian --> Deserialize: GenericHessian
 */
public class ComplexTestO2GO {
    private static ComplexDataGenerator dg = new ComplexDataGenerator();

    @Test
    public void singlePerson() throws IOException {
        Object p1 = dg.generatePerson_1();
        Object p2 = dg.generatePerson_2();
        Object p3 = dg.generatePerson_3();
        Object p4 = dg.generatePerson_4();
        Object p5 = dg.generatePerson_5();
        Object p6 = dg.generatePerson_6();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());

        hout.writeObject(p1);
        hout.writeObject(p2);
        hout.writeObject(p3);
        hout.writeObject(p4);
        hout.writeObject(p5);
        hout.writeObject(p6);

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

        ComplexTestGO2GO.cmpGPersonEqualPerson(dp1, (Person) p1);
        ComplexTestGO2GO.cmpGPersonEqualPerson(dp2, (Person) p2);
        ComplexTestGO2GO.cmpGPersonEqualPerson(dp3, (Person) p3);
        ComplexTestGO2GO.cmpGPersonEqualPerson(dp4, (Person) p4);
        ComplexTestGO2GO.cmpGPersonEqualPerson(dp5, (Person) p5);
        ComplexTestGO2GO.cmpGPersonEqualPerson(dp6, (Person) p6);

    }

    @Test
    public void testMap() throws IOException {
        Map map = (Map) dg.generateMapPerson_1();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());

        hout.writeObject(map);

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Map dmap = (Map) hin.readObject();

        assertEquals(dmap.size(), map.size());
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) dmap.get(1), (Person) map.get(1));
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) dmap.get(2), (Person) map.get(2));

        Map dmap2 = (Map) dmap.get("map");
        Map map2 = (Map) map.get("map");
        assertEquals(dmap2.size(), map2.size());
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) dmap2.get("lll"),
            (Person) map2.get("lll"));
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) dmap2.get("qqq"),
            (Person) map2.get("qqq"));
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) dmap2.get("www"),
            (Person) map2.get("www"));
        assertSame(dmap2.get("lll"), dmap2.get("www"));
    }

    @Test
    public void testList() throws IOException {
        List list = (List) dg.generateListPerson_1();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());

        hout.writeObject(list);

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        List dlist = (List) hin.readObject();

        assertEquals(dlist.size(), list.size());
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) dlist.get(0), (Person) list.get(0));
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) dlist.get(1), (Person) list.get(1));

        List dlist2 = (List) dlist.get(2);
        List list2 = (List) list.get(2);
        assertEquals(dlist2.size(), list2.size());
        ComplexTestGO2GO
            .cmpGPersonEqualPerson((GenericObject) dlist2.get(0), (Person) list2.get(0));
        ComplexTestGO2GO
            .cmpGPersonEqualPerson((GenericObject) dlist2.get(1), (Person) list2.get(1));
        ComplexTestGO2GO
            .cmpGPersonEqualPerson((GenericObject) dlist2.get(2), (Person) list2.get(2));
        assertSame(dlist2.get(0), dlist2.get(2));

    }

    // @Test
    // Not Supported
    public void testArray() throws IOException {
        Object[] arr = (Object[]) dg.generateArrayPerson_1();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());

        hout.writeObject(arr);

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object[] darr = (Object[]) hin.readObject();

        assertEquals(darr.length, arr.length);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) darr[0], (Person) arr[0]);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) darr[1], (Person) arr[1]);

        Object[] darr2 = (Object[]) darr[2];
        Object[] arr2 = (Object[]) arr[2];
        assertEquals(darr2.length, arr2.length);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) darr2[0], (Person) arr2[0]);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) darr2[1], (Person) arr2[1]);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) darr2[2], (Person) arr2[2]);
        assertSame(darr2[0], darr2[2]);

    }

    //@Test
    // Not Supported
    public void testMix() throws IOException {
        Object obj = dg.generateMixObject();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());

        hout.writeObject(obj);

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object[][] darr = (Object[][]) hin.readObject();
        Object[][] arr = (Object[][]) obj;

        assertEquals(darr.length, 3);
        assertEquals(darr[0].length, 3);

        assertSame(darr, darr[2][0]);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) darr[2][1], (Person) arr[2][1]);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) darr[2][2], (Person) arr[2][2]);

        Object[] darr1 = (Object[]) darr[1][0];
        Map dmap1 = (Map) darr[1][1];
        List dlist1 = (List) darr[1][2];

        Object[] darr2 = (Object[]) darr1[2];
        assertSame(darr2[2], dmap1);
        Map dmap2 = (Map) dmap1.get(3);
        assertSame(dmap2.get("www"), dlist1);
        List dlist2 = (List) dlist1.get(2);
        assertSame(dlist2.get(2), darr1);

        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) darr1[0],
            (Person) ((Object[]) arr[1][0])[0]);
        assertSame(darr1[0], darr2[0]);
        assertSame(darr1[0], dmap1.get(1));
        assertSame(darr1[0], dmap2.get("lll"));
        assertSame(darr1[0], dlist1.get(0));
        assertSame(darr1[0], dlist2.get(0));

        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) darr1[1],
            (Person) ((Object[]) arr[1][0])[1]);
        assertSame(darr1[1], darr2[1]);
        assertSame(darr1[1], dmap1.get(2));
        assertSame(darr1[1], dmap2.get("qqq"));
        assertSame(darr1[1], dlist1.get(1));
        assertSame(darr1[1], dlist2.get(1));

        //System.out.println(darr1[1]);

    }
}
