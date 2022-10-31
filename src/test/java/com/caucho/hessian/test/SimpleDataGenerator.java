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

import com.alipay.hessian.generic.model.GenericObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * The test service is a Hessian 2.0 protocol test for developers of
 * Hessian 2.0 clients.  For a new client the recommended order is:
 * <p>
 * <ul>
 * <li>methodNull
 * <li>methodHello
 * </ul>
 */
public class SimpleDataGenerator implements DataGenerator {

    //
    // generate tests, testing serialization output
    //

    public Object generateNull() {
        return null;
    }

    //
    // boolean
    //

    public Object generateTrue() {
        return true;
    }

    public Object generateFalse() {
        return false;
    }

    //
    // integers
    //

    // single byte integers

    public int generateInt_0() {
        return 0;
    }

    public int generateInt_1() {
        return 1;
    }

    public int generateInt_47() {
        return 47;
    }

    public int generateInt_m16() {
        return -16;
    }

    // two byte integers

    public int generateInt_0x30() {
        return 0x30;
    }

    public int generateInt_0x7ff() {
        return 0x7ff;
    }

    public int generateInt_m17() {
        return -17;
    }

    public int generateInt_m0x800() {
        return -0x800;
    }

    // three byte integers

    public int generateInt_0x800() {
        return 0x800;
    }

    public int generateInt_0x3ffff() {
        return 0x3ffff;
    }

    public int generateInt_m0x801() {
        return -0x801;
    }

    public int generateInt_m0x40000() {
        return -0x40000;
    }

    // 5 byte integers

    public int generateInt_0x40000() {
        return 0x40000;
    }

    public int generateInt_0x7fffffff() {
        return 0x7fffffff;
    }

    public int generateInt_m0x40001() {
        return -0x40001;
    }

    public int generateInt_m0x80000000() {
        return -0x80000000;
    }

    //
    // longs
    //

    // single byte longs

    public long generateLong_0() {
        return 0;
    }

    public long generateLong_1() {
        return 1;
    }

    public long generateLong_15() {
        return 15;
    }

    public long generateLong_m8() {
        return -8;
    }

    // two byte longs

    public long generateLong_0x10() {
        return 0x10;
    }

    public long generateLong_0x7ff() {
        return 0x7ff;
    }

    public long generateLong_m9() {
        return -9;
    }

    public long generateLong_m0x800() {
        return -0x800;
    }

    // three byte longs

    public long generateLong_0x800() {
        return 0x800;
    }

    public long generateLong_0x3ffff() {
        return 0x3ffff;
    }

    public long generateLong_m0x801() {
        return -0x801;
    }

    public long generateLong_m0x40000() {
        return -0x40000;
    }

    // 5 byte longs

    public long generateLong_0x40000() {
        return 0x40000;
    }

    public long generateLong_0x7fffffff() {
        return 0x7fffffff;
    }

    public long generateLong_m0x40001() {
        return -0x40001;
    }

    public long generateLong_m0x80000000() {
        return -0x80000000;
    }

    public long generateLong_0x80000000() {
        return 0x80000000L;
    }

    public long generateLong_m0x80000001() {
        return -0x80000001L;
    }

    //
    // doubles
    //

    public double generateDouble_0_0() {
        return 0;
    }

    public double generateDouble_1_0() {
        return 1;
    }

    public double generateDouble_2_0() {
        return 2;
    }

    public double generateDouble_127_0() {
        return 127;
    }

    public double generateDouble_m128_0() {
        return -128;
    }

    public double generateDouble_128_0() {
        return 128;
    }

    public double generateDouble_m129_0() {
        return -129;
    }

    public double generateDouble_32767_0() {
        return 32767;
    }

    public double generateDouble_m32768_0() {
        return -32768;
    }

    public double generateDouble_0_001() {
        return 0.001;
    }

    public double generateDouble_m0_001() {
        return -0.001;
    }

    public double generateDouble_65_536() {
        return 65.536;
    }

    public double generateDouble_3_14159() {
        return 3.14159;
    }

    // date

    public Object generateDate_0() {
        return new Date(0);
    }

    public Object generateDate_1() {
        long time = 894621091000L;

        return new Date(time);
    }

    public Object generateDate_2() {
        long time = 894621091000L;

        time -= time % 60000L;

        return new Date(time);
    }

    // strings by length

    public String generateString_0() {
        return "";
    }

    public String generateString_null() {
        return null;
    }

    public String generateString_1() {
        return "0";
    }

    public String generateString_31() {
        return "0123456789012345678901234567890";
    }

    public String generateString_32() {
        return "01234567890123456789012345678901";
    }

    public String generateString_1023() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            sb.append("" + (i / 10) + (i % 10)
                + " 456789012345678901234567890123456789012345678901234567890123\n");
        }

        sb.setLength(1023);

        return sb.toString();
    }

    public String generateString_1024() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            sb.append("" + (i / 10) + (i % 10)
                + " 456789012345678901234567890123456789012345678901234567890123\n");
        }

        sb.setLength(1024);

        return sb.toString();
    }

    public String generateString_65536() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 64 * 16; i++) {
            sb.append("" + (i / 100) + (i / 10 % 10) + (i % 10)
                + " 56789012345678901234567890123456789012345678901234567890123\n");
        }

        sb.setLength(65536);

        return sb.toString();
    }

    // binarys by length

    public Object generateBinary_0() {
        return new byte[0];
    }

    public Object generateBinary_null() {
        return null;
    }

    public Object generateBinary_1() {
        return toBinary("0");
    }

    public Object generateBinary_15() {
        return toBinary("012345678901234");
    }

    public Object generateBinary_16() {
        return toBinary("0123456789012345");
    }

    public Object generateBinary_1023() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            sb.append("" + (i / 10) + (i % 10)
                + " 456789012345678901234567890123456789012345678901234567890123\n");
        }

        sb.setLength(1023);

        return toBinary(sb.toString());
    }

    public Object generateBinary_1024() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            sb.append("" + (i / 10) + (i % 10)
                + " 456789012345678901234567890123456789012345678901234567890123\n");
        }

        sb.setLength(1024);

        return toBinary(sb.toString());
    }

    public Object generateBinary_65536() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 64 * 16; i++) {
            sb.append("" + (i / 100) + (i / 10 % 10) + (i % 10)
                + " 56789012345678901234567890123456789012345678901234567890123\n");
        }

        sb.setLength(65536);

        return toBinary(sb.toString());
    }

    private byte[] toBinary(String s) {
        byte[] buffer = new byte[s.length()];

        for (int i = 0; i < s.length(); i++)
            buffer[i] = (byte) s.charAt(i);

        return buffer;
    }

    //
    // lists
    //

    public Object generateUntypedFixedList_0() {
        ArrayList list = new ArrayList();

        return list;
    }

    public Object generateUntypedFixedList_1() {
        ArrayList list = new ArrayList();

        list.add("1");

        return list;
    }

    public Object generateUntypedFixedList_7() {
        ArrayList list = new ArrayList();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");

        return list;
    }

    public Object generateUntypedFixedList_8() {
        ArrayList list = new ArrayList();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");

        return list;
    }

    public Object generateTypedFixedList_0() {
        return new String[] {};
    }

    public Object generateTypedFixedList_1() {
        return new String[] { "1" };
    }

    public Object generateTypedFixedList_7() {
        return new String[] { "1", "2", "3", "4", "5", "6", "7" };
    }

    public Object generateTypedFixedList_8() {
        return new String[] { "1", "2", "3", "4", "5", "6", "7", "8" };
    }

    // untyped map

    public Object generateUntypedMap_0() {
        return new HashMap();
    }

    public Object generateUntypedMap_1() {
        HashMap map = new HashMap();
        map.put("a", 0);

        return map;
    }

    public Object generateUntypedMap_2() {
        HashMap map = new HashMap();

        map.put(0, "a");
        map.put(1, "b");

        return map;
    }

    public Object generateUntypedMap_3() {
        HashMap map = new HashMap();

        ArrayList list = new ArrayList();
        list.add("a");

        map.put(list, 0);

        return map;
    }

    // typed map

    public Object generateTypedMap_0() {
        Hashtable map = new Hashtable();

        return map;
    }

    public Object generateTypedMap_1() {
        Map map = new Hashtable();

        map.put("a", 0);

        return map;
    }

    public Object generateTypedMap_2() {
        Map map = new Hashtable();

        map.put(0, "a");
        map.put(1, "b");

        return map;
    }

    public Object generateTypedMap_3() {
        Map map = new Hashtable();

        ArrayList list = new ArrayList();
        list.add("a");

        map.put(list, 0);

        return map;
    }

    //
    // objects
    //

    public Object generateObject_0() {
        return new A0();
    }

    public Object generateGenericObject_0() {
        return new GenericObject(A0.class.getName());
    }

    public Object generateObject_16() {
        ArrayList list = new ArrayList();

        list.add(new A0());
        list.add(new A1());
        list.add(new A2());
        list.add(new A3());
        list.add(new A4());
        list.add(new A5());
        list.add(new A6());
        list.add(new A7());
        list.add(new A8());
        list.add(new A9());
        list.add(new A10());
        list.add(new A11());
        list.add(new A12());
        list.add(new A13());
        list.add(new A14());
        list.add(new A15());
        list.add(new A16());

        return list;
    }

    public Object generateGenericObject_16() {
        ArrayList list = new ArrayList();

        list.add(new GenericObject(A0.class.getName()));
        list.add(new GenericObject(A1.class.getName()));
        list.add(new GenericObject(A2.class.getName()));
        list.add(new GenericObject(A3.class.getName()));
        list.add(new GenericObject(A4.class.getName()));
        list.add(new GenericObject(A5.class.getName()));
        list.add(new GenericObject(A6.class.getName()));
        list.add(new GenericObject(A7.class.getName()));
        list.add(new GenericObject(A8.class.getName()));
        list.add(new GenericObject(A9.class.getName()));
        list.add(new GenericObject(A10.class.getName()));
        list.add(new GenericObject(A11.class.getName()));
        list.add(new GenericObject(A12.class.getName()));
        list.add(new GenericObject(A13.class.getName()));
        list.add(new GenericObject(A14.class.getName()));
        list.add(new GenericObject(A15.class.getName()));
        list.add(new GenericObject(A16.class.getName()));

        return list;
    }

    public Object generateObject_1() {
        return new TestObject(0);
    }

    public GenericObject generateGenericObject_1() {
        GenericObject gobj = new GenericObject(TestObject.class.getName());
        gobj.putField("_value", 0);
        return gobj;
    }

    public Object generateGenericObject_1(int value) {
        GenericObject gobj = new GenericObject(TestObject.class.getName());
        gobj.putField("_value", value);
        return gobj;
    }

    public Object generateObject_2() {
        ArrayList list = new ArrayList();

        list.add(new TestObject(2));
        list.add(new TestObject(1));

        return list;
    }

    public Object generateGenericObject_2() {
        ArrayList list = new ArrayList();

        list.add(generateGenericObject_1(2));
        list.add(generateGenericObject_1(1));

        return list;
    }

    public Object generateObject_2a() {
        ArrayList list = new ArrayList();

        TestObject obj = new TestObject(0);

        list.add(obj);
        list.add(obj);

        return list;
    }

    public Object generateGenericObject_2a() {
        ArrayList list = new ArrayList();

        Object gobj = generateGenericObject_1(0);

        list.add(gobj);
        list.add(gobj);

        return list;
    }

    public Object generateObject_2b() {
        ArrayList list = new ArrayList();

        list.add(new TestObject(0));
        list.add(new TestObject(0));

        return list;
    }

    public Object generateGenericObject_2b() {
        ArrayList list = new ArrayList();

        list.add(generateGenericObject_1(0));
        list.add(generateGenericObject_1(0));

        return list;
    }

    public Object generateObject_3() {
        SimpleTestCons cons = new SimpleTestCons();

        cons.setFirst("a");
        cons.setRest(cons);

        return cons;
    }

    public Object generateGenericObject_3() {
        GenericObject gobj = new GenericObject(SimpleTestCons.class.getName());
        gobj.putField("_first", "a");
        gobj.putField("_rest", gobj);
        return gobj;
    }
}
