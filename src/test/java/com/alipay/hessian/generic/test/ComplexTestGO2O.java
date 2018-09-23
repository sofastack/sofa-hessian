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
 * Serialize: GenericHessian --> Deserialize: Hessian
 */
public class ComplexTestGO2O {

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
        hin.setSerializerFactory(new SerializerFactory());

        Person dp1 = (Person) hin.readObject();
        Person dp2 = (Person) hin.readObject();
        Person dp3 = (Person) hin.readObject();
        Person dp4 = (Person) hin.readObject();
        Person dp5 = (Person) hin.readObject();
        Person dp6 = (Person) hin.readObject();

        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) gp1, dp1);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) gp2, dp2);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) gp3, dp3);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) gp4, dp4);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) gp5, dp5);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) gp6, dp6);

        //System.out.println(dp6);
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
        hin.setSerializerFactory(new SerializerFactory());

        Map dmap = (Map) hin.readObject();
        Map map = (Map) dg.generateMapPerson_1();

        assertEquals(dmap.size(), map.size());
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) ((Map) go).get(1),
            (Person) dmap.get(1));
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) ((Map) go).get(2),
            (Person) dmap.get(2));

        Map dmap2 = (Map) dmap.get("map");
        Map map2 = (Map) ((Map) go).get("map");

        assertEquals(dmap2.size(), map2.size());
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) map2.get("lll"),
            (Person) dmap2.get("lll"));
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) map2.get("qqq"),
            (Person) dmap2.get("qqq"));
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) map2.get("www"),
            (Person) dmap2.get("www"));
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
        hin.setSerializerFactory(new SerializerFactory());

        List dlist = (List) hin.readObject();

        assertEquals(dlist.size(), ((List) go).size());
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) ((List) go).get(0),
            (Person) dlist.get(0));
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) ((List) go).get(1),
            (Person) dlist.get(1));

        List dlist2 = (List) dlist.get(2);
        List list2 = (List) ((List) go).get(2);

        assertEquals(dlist2.size(), list2.size());
        ComplexTestGO2GO
            .cmpGPersonEqualPerson((GenericObject) list2.get(0), (Person) dlist2.get(0));
        ComplexTestGO2GO
            .cmpGPersonEqualPerson((GenericObject) list2.get(1), (Person) dlist2.get(1));
        ComplexTestGO2GO
            .cmpGPersonEqualPerson((GenericObject) list2.get(2), (Person) dlist2.get(2));
        assertSame(dlist2.get(0), dlist2.get(2));

    }

    @Test
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

        assertEquals(darr.length, ((Object[]) go).length);
        ComplexTestGO2GO
            .cmpGPersonEqualPerson((GenericObject) ((Object[]) go)[0], (Person) darr[0]);
        ComplexTestGO2GO
            .cmpGPersonEqualPerson((GenericObject) ((Object[]) go)[1], (Person) darr[1]);

        Object[] darr2 = (Object[]) darr[2];
        Object[] arr2 = (Object[]) ((Object[]) go)[2];

        assertEquals(darr2.length, arr2.length);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) arr2[0], (Person) darr2[0]);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) arr2[1], (Person) darr2[1]);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) arr2[2], (Person) darr2[2]);
        assertSame(darr2[0], darr2[2]);

    }

    @Test
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
        hin.setSerializerFactory(new SerializerFactory());

        Object[][] darr = (Object[][]) hin.readObject();

        assertEquals(darr.length, 3);
        assertEquals(darr[0].length, 3);

        assertSame(darr, darr[2][0]);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) ((Object[][]) obj)[2][1],
            (Person) darr[2][1]);
        ComplexTestGO2GO.cmpGPersonEqualPerson((GenericObject) ((Object[][]) obj)[2][2],
            (Person) darr[2][2]);

        Object[] darr1 = (Object[]) darr[1][0];
        Map dmap1 = (Map) darr[1][1];
        List dlist1 = (List) darr[1][2];

        Object[] darr2 = (Object[]) darr1[2];
        assertSame(darr2[2], dmap1);
        Map dmap2 = (Map) dmap1.get(3);
        assertSame(dmap2.get("www"), dlist1);
        List dlist2 = (List) dlist1.get(2);
        assertSame(dlist2.get(2), darr1);

        ComplexTestGO2GO.cmpGPersonEqualPerson(
            (GenericObject) ((Object[]) ((Object[][]) obj)[1][0])[0], (Person) darr1[0]);
        assertSame(darr1[0], darr2[0]);
        assertSame(darr1[0], dmap1.get(1));
        assertSame(darr1[0], dmap2.get("lll"));
        assertSame(darr1[0], dlist1.get(0));
        assertSame(darr1[0], dlist2.get(0));

        ComplexTestGO2GO.cmpGPersonEqualPerson(
            (GenericObject) ((Object[]) ((Object[][]) obj)[1][0])[1], (Person) darr1[1]);
        assertSame(darr1[1], darr2[1]);
        assertSame(darr1[1], dmap1.get(2));
        assertSame(darr1[1], dmap2.get("qqq"));
        assertSame(darr1[1], dlist1.get(1));
        assertSame(darr1[1], dlist2.get(1));

        //System.out.println(darr1[1]);

    }
}
