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
package com.caucho.hessian.test;

import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by qiwei.lqw on 2016/7/5.
 */
public class SimpleTestGO2O {

    private static SimpleDataGenerator dg = new SimpleDataGenerator();

    @org.junit.Test
    public void testNull() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(dg.generateNull());
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        assertNull(hin.readObject());

        hin.close();
    }

    @org.junit.Test
    public void testBoolean() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(dg.generateTrue());
        hout.writeObject(dg.generateFalse());
        hout.writeObject(dg.generateTrue());
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        assertEquals(true, hin.readObject());
        assertEquals(false, hin.readObject());
        assertEquals(true, hin.readObject());

        hin.close();
    }

    @org.junit.Test
    public void testIntegers() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        //single byte
        hout.writeObject(dg.generateInt_0());
        hout.writeObject(dg.generateInt_1());
        hout.writeObject(dg.generateInt_47());
        hout.writeObject(dg.generateInt_m16());

        //two bytes
        hout.writeObject(dg.generateInt_0x30());
        hout.writeObject(dg.generateInt_0x7ff());
        hout.writeObject(dg.generateInt_m17());
        hout.writeObject(dg.generateInt_m0x800());

        //three bytes
        hout.writeObject(dg.generateInt_0x800());
        hout.writeObject(dg.generateInt_0x3ffff());
        hout.writeObject(dg.generateInt_m0x801());
        hout.writeObject(dg.generateInt_m0x40000());

        //five bytes
        hout.writeObject(dg.generateInt_0x40000());
        hout.writeObject(dg.generateInt_0x7fffffff());
        hout.writeObject(dg.generateInt_m0x40001());
        hout.writeObject(dg.generateInt_m0x80000000());

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        //single byte
        assertEquals(dg.generateInt_0(), hin.readObject());
        assertEquals(dg.generateInt_1(), hin.readObject());
        assertEquals(dg.generateInt_47(), hin.readObject());
        assertEquals(dg.generateInt_m16(), hin.readObject());

        //two bytes
        assertEquals(dg.generateInt_0x30(), hin.readObject());
        assertEquals(dg.generateInt_0x7ff(), hin.readObject());
        assertEquals(dg.generateInt_m17(), hin.readObject());
        assertEquals(dg.generateInt_m0x800(), hin.readObject());

        //three bytes
        assertEquals(dg.generateInt_0x800(), hin.readObject());
        assertEquals(dg.generateInt_0x3ffff(), hin.readObject());
        assertEquals(dg.generateInt_m0x801(), hin.readObject());
        assertEquals(dg.generateInt_m0x40000(), hin.readObject());

        //five bytes
        assertEquals(dg.generateInt_0x40000(), hin.readObject());
        assertEquals(dg.generateInt_0x7fffffff(), hin.readObject());
        assertEquals(dg.generateInt_m0x40001(), hin.readObject());
        assertEquals(dg.generateInt_m0x80000000(), hin.readObject());

        hin.close();
    }

    @org.junit.Test
    public void testLong() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        //single byte
        hout.writeObject(dg.generateLong_0());
        hout.writeObject(dg.generateLong_1());
        hout.writeObject(dg.generateLong_15());
        hout.writeObject(dg.generateLong_m8());

        //two bytes
        hout.writeObject(dg.generateLong_0x10());
        hout.writeObject(dg.generateLong_0x7ff());
        hout.writeObject(dg.generateLong_m9());
        hout.writeObject(dg.generateLong_m0x800());

        //three bytes
        hout.writeObject(dg.generateLong_0x800());
        hout.writeObject(dg.generateLong_0x3ffff());
        hout.writeObject(dg.generateLong_m0x801());
        hout.writeObject(dg.generateLong_m0x40000());

        //five bytes
        hout.writeObject(dg.generateLong_0x40000());
        hout.writeObject(dg.generateLong_0x7fffffff());
        hout.writeObject(dg.generateLong_m0x40001());
        hout.writeObject(dg.generateLong_m0x80000000());
        hout.writeObject(dg.generateLong_0x80000000());
        hout.writeObject(dg.generateLong_m0x80000001());

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        //single byte
        assertEquals(dg.generateLong_0(), hin.readObject());
        assertEquals(dg.generateLong_1(), hin.readObject());
        assertEquals(dg.generateLong_15(), hin.readObject());
        assertEquals(dg.generateLong_m8(), hin.readObject());

        //two bytes
        assertEquals(dg.generateLong_0x10(), hin.readObject());
        assertEquals(dg.generateLong_0x7ff(), hin.readObject());
        assertEquals(dg.generateLong_m9(), hin.readObject());
        assertEquals(dg.generateLong_m0x800(), hin.readObject());

        //three bytes
        assertEquals(dg.generateLong_0x800(), hin.readObject());
        assertEquals(dg.generateLong_0x3ffff(), hin.readObject());
        assertEquals(dg.generateLong_m0x801(), hin.readObject());
        assertEquals(dg.generateLong_m0x40000(), hin.readObject());

        //five bytes
        assertEquals(dg.generateLong_0x40000(), hin.readObject());
        assertEquals(dg.generateLong_0x7fffffff(), hin.readObject());
        assertEquals(dg.generateLong_m0x40001(), hin.readObject());
        assertEquals(dg.generateLong_m0x80000000(), hin.readObject());
        assertEquals(dg.generateLong_0x80000000(), hin.readObject());
        assertEquals(dg.generateLong_m0x80000001(), hin.readObject());

        hin.close();
    }

    @org.junit.Test
    public void testDoubles() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(dg.generateDouble_0_0());
        hout.writeObject(dg.generateDouble_1_0());
        hout.writeObject(dg.generateDouble_2_0());
        hout.writeObject(dg.generateDouble_127_0());
        hout.writeObject(dg.generateDouble_m128_0());
        hout.writeObject(dg.generateDouble_128_0());
        hout.writeObject(dg.generateDouble_m129_0());
        hout.writeObject(dg.generateDouble_32767_0());
        hout.writeObject(dg.generateDouble_m32768_0());
        hout.writeObject(dg.generateDouble_0_001());
        hout.writeObject(dg.generateDouble_m0_001());
        hout.writeObject(dg.generateDouble_65_536());
        hout.writeObject(dg.generateDouble_3_14159());

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        assertEquals(dg.generateDouble_0_0(), hin.readObject());
        assertEquals(dg.generateDouble_1_0(), hin.readObject());
        assertEquals(dg.generateDouble_2_0(), hin.readObject());
        assertEquals(dg.generateDouble_127_0(), hin.readObject());
        assertEquals(dg.generateDouble_m128_0(), hin.readObject());
        assertEquals(dg.generateDouble_128_0(), hin.readObject());
        assertEquals(dg.generateDouble_m129_0(), hin.readObject());
        assertEquals(dg.generateDouble_32767_0(), hin.readObject());
        assertEquals(dg.generateDouble_m32768_0(), hin.readObject());
        assertEquals(dg.generateDouble_0_001(), hin.readObject());
        assertEquals(dg.generateDouble_m0_001(), hin.readObject());
        assertEquals(dg.generateDouble_65_536(), hin.readObject());
        assertEquals(dg.generateDouble_3_14159(), hin.readObject());

        hin.close();
    }

    @org.junit.Test
    public void testDate() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(dg.generateDate_0());
        hout.writeObject(dg.generateDate_1());
        hout.writeObject(dg.generateDate_2());

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        assertEquals(dg.generateDate_0(), hin.readObject());
        assertEquals(dg.generateDate_1(), hin.readObject());
        assertEquals(dg.generateDate_2(), hin.readObject());

        hin.close();
    }

    @org.junit.Test
    public void testString() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(dg.generateString_0());
        hout.writeObject(dg.generateString_null());
        hout.writeObject(dg.generateString_1());
        hout.writeObject(dg.generateString_31());
        hout.writeObject(dg.generateString_32());
        hout.writeObject(dg.generateString_1023());
        hout.writeObject(dg.generateString_1024());
        hout.writeObject(dg.generateString_65536());

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        assertEquals(dg.generateString_0(), hin.readObject());
        assertEquals(dg.generateString_null(), hin.readObject());
        assertEquals(dg.generateString_1(), hin.readObject());
        assertEquals(dg.generateString_31(), hin.readObject());
        assertEquals(dg.generateString_32(), hin.readObject());
        assertEquals(dg.generateString_1023(), hin.readObject());
        assertEquals(dg.generateString_1024(), hin.readObject());
        assertEquals(dg.generateString_65536(), hin.readObject());

        hin.close();
    }

    @org.junit.Test
    public void testBinary() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(dg.generateBinary_0());
        hout.writeObject(dg.generateBinary_null());
        hout.writeObject(dg.generateBinary_1());
        hout.writeObject(dg.generateBinary_15());
        hout.writeObject(dg.generateBinary_16());
        hout.writeObject(dg.generateBinary_1023());
        hout.writeObject(dg.generateBinary_1024());
        hout.writeObject(dg.generateBinary_65536());

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        assertTrue(compareByteArray(dg.generateBinary_0(), hin.readObject()));
        assertNull(hin.readObject());
        assertTrue(compareByteArray(dg.generateBinary_1(), hin.readObject()));
        assertTrue(compareByteArray(dg.generateBinary_15(), hin.readObject()));
        assertTrue(compareByteArray(dg.generateBinary_16(), hin.readObject()));
        assertTrue(compareByteArray(dg.generateBinary_1023(), hin.readObject()));
        assertTrue(compareByteArray(dg.generateBinary_1024(), hin.readObject()));
        assertTrue(compareByteArray(dg.generateBinary_65536(), hin.readObject()));

        hin.close();
    }

    @org.junit.Test
    public void testList() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(dg.generateUntypedFixedList_0());
        hout.writeObject(dg.generateUntypedFixedList_1());
        hout.writeObject(dg.generateUntypedFixedList_7());
        hout.writeObject(dg.generateUntypedFixedList_8());

        hout.writeObject(dg.generateTypedFixedList_0());
        hout.writeObject(dg.generateTypedFixedList_1());
        hout.writeObject(dg.generateTypedFixedList_7());
        hout.writeObject(dg.generateTypedFixedList_8());

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        assertEquals(dg.generateUntypedFixedList_0(), hin.readObject());
        assertEquals(dg.generateUntypedFixedList_1(), hin.readObject());
        assertEquals(dg.generateUntypedFixedList_7(), hin.readObject());
        assertEquals(dg.generateUntypedFixedList_8(), hin.readObject());

        assertTrue(Arrays.equals((Object[]) dg.generateTypedFixedList_0(),
            (Object[]) hin.readObject()));
        assertTrue(Arrays.equals((Object[]) dg.generateTypedFixedList_1(),
            (Object[]) hin.readObject()));
        assertTrue(Arrays.equals((Object[]) dg.generateTypedFixedList_7(),
            (Object[]) hin.readObject()));
        assertTrue(Arrays.equals((Object[]) dg.generateTypedFixedList_8(),
            (Object[]) hin.readObject()));

        hin.close();
    }

    @org.junit.Test
    public void testMap() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(dg.generateUntypedMap_0());
        hout.writeObject(dg.generateUntypedMap_1());
        hout.writeObject(dg.generateUntypedMap_2());
        hout.writeObject(dg.generateUntypedMap_3());

        hout.writeObject(dg.generateTypedMap_0());
        hout.writeObject(dg.generateTypedMap_1());
        hout.writeObject(dg.generateTypedMap_2());
        hout.writeObject(dg.generateTypedMap_3());

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        assertEquals(dg.generateUntypedMap_0(), hin.readObject());
        assertEquals(dg.generateUntypedMap_1(), hin.readObject());
        assertEquals(dg.generateUntypedMap_2(), hin.readObject());
        assertEquals(dg.generateUntypedMap_3(), hin.readObject());

        assertEquals(dg.generateTypedMap_0(), hin.readObject());
        assertEquals(dg.generateTypedMap_1(), hin.readObject());
        assertEquals(dg.generateTypedMap_2(), hin.readObject());
        assertEquals(dg.generateTypedMap_3(), hin.readObject());

        hin.close();
    }

    @org.junit.Test
    public void testObject() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(dg.generateGenericObject_0());
        hout.writeObject(dg.generateGenericObject_16());
        hout.writeObject(dg.generateGenericObject_1());
        hout.writeObject(dg.generateGenericObject_2());
        hout.writeObject(dg.generateGenericObject_2a());
        hout.writeObject(dg.generateGenericObject_2b());
        hout.writeObject(dg.generateGenericObject_3());

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Object dobj1 = hin.readObject();
        Object dobj2 = hin.readObject();
        Object dobj3 = hin.readObject();
        Object dobj4 = hin.readObject();
        Object dobj5 = hin.readObject();
        Object dobj6 = hin.readObject();
        Object dobj7 = hin.readObject();

        Object obj1 = dg.generateObject_0();
        Object obj2 = dg.generateObject_16();
        Object obj3 = dg.generateObject_1();
        Object obj4 = dg.generateObject_2();
        Object obj5 = dg.generateObject_2a();
        Object obj6 = dg.generateObject_2b();
        Object obj7 = dg.generateObject_3();

        assertEquals(obj1, dobj1);
        assertEquals(obj2, dobj2);
        assertEquals(obj3, dobj3);
        assertEquals(obj4, dobj4);
        assertEquals(obj5, dobj5);
        assertEquals(obj6, dobj6);
        assertEquals(obj7, dobj7);

        hin.close();

    }

    public static boolean compareByteArray(Object a, Object b) {
        byte[] ba = (byte[]) a;
        byte[] bb = (byte[]) b;

        if (ba.length != bb.length)
            return false;
        for (int i = 0; i < ba.length; ++i)
            if (ba[i] != bb[i])
                return false;

        return true;
    }
}
