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

/**
*
 */
public interface DataGenerator {

    //
    // result values
    //

    /**
     * Result of null
     *
     * <code><pre>
     * R N
     * </pre></code>
     */
    public Object generateNull();

    //
    // boolean
    //

    /**
     * Boolean true
     *
     * <code><pre>
     * T
     * </pre></code>
     */
    public Object generateTrue();

    /**
     * Boolean false
     *
     * <code><pre>
     * F
     * </pre></code>
     */
    public Object generateFalse();

    //
    // integers
    //

    /**
     * Result of integer 0
     *
     * <code><pre>
     * R x90
     * </pre></code>
     */
    public int generateInt_0();

    /**
     * Result of integer 1
     *
     * <code><pre>
     * R x91
     * </pre></code>
     */
    public int generateInt_1();

    /**
     * Result of integer 47
     *
     * <code><pre>
     * R xbf
     * </pre></code>
     */
    public int generateInt_47();

    /**
     * Result of integer -16
     *
     * <code><pre>
     * R x80
     * </pre></code>
     */
    public int generateInt_m16();

    // two byte integers

    /**
     * Result of integer 0x30
     *
     * <code><pre>
     * R xc8 x30
     * </pre></code>
     */
    public int generateInt_0x30();

    /**
     * Result of integer x7ff
     *
     * <code><pre>
     * R xcf xff
     * </pre></code>
     */
    public int generateInt_0x7ff();

    /**
     * Result of integer -17
     *
     * <code><pre>
     * R xc7 xef
     * </pre></code>
     */
    public int generateInt_m17();

    /**
     * Result of integer -0x800
     *
     * <code><pre>
     * R xc0 x00
     * </pre></code>
     */
    public int generateInt_m0x800();

    /**
     * Result of integer 0x800
     *
     * <code><pre>
     * R xd4 x08 x00
     * </pre></code>
     */
    public int generateInt_0x800();

    /**
     * Result of integer 0x3ffff
     *
     * <code><pre>
     * R xd7 xff xff
     * </pre></code>
     */
    public int generateInt_0x3ffff();

    /**
     * Result of integer -0x801
     *
     * <code><pre>
     * R xd3 xf8 x00
     * </pre></code>
     */
    public int generateInt_m0x801();

    /**
     * Result of integer m0x40000
     *
     * <code><pre>
     * R xd0 x00 x00
     * </pre></code>
     */
    public int generateInt_m0x40000();

    // 5 byte integers

    /**
     * Result of integer 0x40000
     *
     * <code><pre>
     * R I x00 x04 x00 x00
     * </pre></code>
     */
    public int generateInt_0x40000();

    /**
     * Result of integer 0x7fffffff
     *
     * <code><pre>
     * R I x7f xff xff xff
     * </pre></code>
     */
    public int generateInt_0x7fffffff();

    /**
     * Result of integer m0x40001
     *
     * <code><pre>
     * R I xff xf3 xff xf
     * </pre></code>
     */
    public int generateInt_m0x40001();

    /**
     * Result of integer -0x80000000
     *
     * <code><pre>
     * R I x80 x00 x00 x00
     * </pre></code>
     */
    public int generateInt_m0x80000000();

    //
    // longs
    //

    /**
     * Result of long 0
     *
     * <code><pre>
     * R xe0
     * </pre></code>
     */
    public long generateLong_0();

    /**
     * Result of long 1
     *
     * <code><pre>
     * R xe1
     * </pre></code>
     */
    public long generateLong_1();

    /**
     * Result of long 15
     *
     * <code><pre>
     * R xef
     * </pre></code>
     */
    public long generateLong_15();

    /**
     * Result of long -8
     *
     * <code><pre>
     * R xd8
     * </pre></code>
     */
    public long generateLong_m8();

    // two byte longs

    /**
     * Result of long 0x10
     *
     * <code><pre>
     * R xf8 x10
     * </pre></code>
     */
    public long generateLong_0x10();

    /**
     * Result of long x7ff
     *
     * <code><pre>
     * R xff xff
     * </pre></code>
     */
    public long generateLong_0x7ff();

    /**
     * Result of long -9
     *
     * <code><pre>
     * R xf7 xf7
     * </pre></code>
     */
    public long generateLong_m9();

    /**
     * Result of long -0x800
     *
     * <code><pre>
     * R xf0 x00
     * </pre></code>
     */
    public long generateLong_m0x800();

    /**
     * Result of long 0x800
     *
     * <code><pre>
     * R x3c x08 x00
     * </pre></code>
     */
    public long generateLong_0x800();

    /**
     * Result of long 0x3ffff
     *
     * <code><pre>
     * R x3f xff xff
     * </pre></code>
     */
    public long generateLong_0x3ffff();

    /**
     * Result of long -0x801
     *
     * <code><pre>
     * R x3b xf7 xff
     * </pre></code>
     */
    public long generateLong_m0x801();

    /**
     * Result of long m0x40000
     *
     * <code><pre>
     * R x38 x00 x00
     * </pre></code>
     */
    public long generateLong_m0x40000();

    // 5 byte longs

    /**
     * Result of long 0x40000
     *
     * <code><pre>
     * R x59 x00 x04 x00 x00
     * </pre></code>
     */
    public long generateLong_0x40000();

    /**
     * Result of long 0x7fffffff
     *
     * <code><pre>
     * R x59 x7f xff xff xff
     * </pre></code>
     */
    public long generateLong_0x7fffffff();

    /**
     * Result of long m0x40001
     *
     * <code><pre>
     * R x59 xff xf3 xff xf
     * </pre></code>
     */
    public long generateLong_m0x40001();

    /**
     * Result of long -0x80000000
     *
     * <code><pre>
     * R x59 x80 x00 x00 x00
     * </pre></code>
     */
    public long generateLong_m0x80000000();

    /**
     * Result of long 0x80000000
     *
     * <code><pre>
     * R L x00 x00 x00 x00 x80 x00 x00 x00
     * </pre></code>
     */
    public long generateLong_0x80000000();

    /**
     * Result of long -0x80000001
     *
     * <code><pre>
     * R L xff xff xff xff x7f xff xff xff
     * </pre></code>
     */
    public long generateLong_m0x80000001();

    //
    // doubles
    //

    /**
     * Result of double 0.0
     *
     * <code><pre>
     * R x5b
     * </pre></code>
     */
    public double generateDouble_0_0();

    /**
     * Result of double 1.0
     *
     * <code><pre>
     * R x5c
     * </pre></code>
     */
    public double generateDouble_1_0();

    /**
     * Result of double 2.0
     *
     * <code><pre>
     * R x5d x02
     * </pre></code>
     */
    public double generateDouble_2_0();

    /**
     * Result of double 127.0
     *
     * <code><pre>
     * R x5d x7f
     * </pre></code>
     */
    public double generateDouble_127_0();

    /**
     * Result of double -128.0
     *
     * <code><pre>
     * R x5d x80
     * </pre></code>
     */
    public double generateDouble_m128_0();

    /**
     * Result of double 128.0
     *
     * <code><pre>
     * R x5e x00 x80
     * </pre></code>
     */
    public double generateDouble_128_0();

    /**
     * Result of double -129.0
     *
     * <code><pre>
     * R x5e xff x7f
     * </pre></code>
     */
    public double generateDouble_m129_0();

    /**
     * Result of double 32767.0
     *
     * <code><pre>
     * R x5e x7f xff
     * </pre></code>
     */
    public double generateDouble_32767_0();

    /**
     * Result of double -32768.0
     *
     * <code><pre>
     * R x5e x80 x80
     * </pre></code>
     */
    public double generateDouble_m32768_0();

    /**
     * Result of double 0.001
     *
     * <code><pre>
     * R x5f x00 x00 x00 x01
     * </pre></code>
     */
    public double generateDouble_0_001();

    /**
     * Result of double -0.001
     *
     * <code><pre>
     * R x5f xff xff xff xff
     * </pre></code>
     */
    public double generateDouble_m0_001();

    /**
     * Result of double 65.536
     *
     * <code><pre>
     * R x5f x00 x01 x00 x00
     * </pre></code>
     */
    public double generateDouble_65_536();

    /**
     * Result of double 3.14159
     *
     * <code><pre>
     * D x40 x09 x21 xf9 xf0 x1b x86 x6e
     * </pre></code>
     */
    public double generateDouble_3_14159();

    //
    // date
    //

    /**
     * date 0 (01-01-1970 00:00 GMT)
     *
     * <code><pre>
     * x4a x00 x00 x00 x00
     * </pre></code>
     */
    public Object generateDate_0();

    /**
     * Date by millisecond (05-08-1998 07:51 GMT)
     *
     * <code><pre>
     * x4a x00 x00 x00 xd0 x4b x92 x84 xb8
     * </pre></code>
     */
    public Object generateDate_1();

    /**
     * Date by minute (05-08-1998 07:51 GMT)
     *
     * <code><pre>
     * x4b x00 xe3 x83 x8f
     * </pre></code>
     */
    public Object generateDate_2();

    //
    // string length
    //

    /**
     * A zero-length string
     *
     * <code><pre>
     * x00
     * </pre></code>
     */
    public String generateString_0();

    /**
     * A null string
     *
     * <code><pre>
     * N
     * </pre></code>
     */
    public String generateString_null();

    /**
     * A one-length string
     *
     * <code><pre>
     * x01 a
     * </pre></code>
     */
    public String generateString_1();

    /**
     * A 31-length string
     *
     * <code><pre>
     * x0f 0123456789012345678901234567890
     * </pre></code>
     */
    public String generateString_31();

    /**
     * A 32-length string
     *
     * <code><pre>
     * x30 x02 01234567890123456789012345678901
     * </pre></code>
     */
    public String generateString_32();

    /**
     * A 1023-length string
     *
     * <code><pre>
     * x33 xff 000 01234567890123456789012345678901...
     * </pre></code>
     */
    public String generateString_1023();

    /**
     * A 1024-length string
     *
     * <code><pre>
     * S x04 x00 000 01234567890123456789012345678901...
     * </pre></code>
     */
    public String generateString_1024();

    /**
     * A 65536-length string
     *
     * <code><pre>
     * R x80 x00 000 ...
     * S x04 x00 000 01234567890123456789012345678901...
     * </pre></code>
     */
    public String generateString_65536();

    //
    // binary length
    //

    /**
     * A zero-length binary
     *
     * <code><pre>
     * x20
     * </pre></code>
     */
    public Object generateBinary_0();

    /**
     * A null string
     *
     * <code><pre>
     * N
     * </pre></code>
     */
    public Object generateBinary_null();

    /**
     * A one-length string
     *
     * <code><pre>
     * x01 0
     * </pre></code>
     */
    public Object generateBinary_1();

    /**
     * A 15-length binary
     *
     * <code><pre>
     * x2f 0123456789012345
     * </pre></code>
     */
    public Object generateBinary_15();

    /**
     * A 16-length binary
     *
     * <code><pre>
     * x34 x10 01234567890123456789012345678901
     * </pre></code>
     */
    public Object generateBinary_16();

    /**
     * A 1023-length binary
     *
     * <code><pre>
     * x37 xff 000 01234567890123456789012345678901...
     * </pre></code>
     */
    public Object generateBinary_1023();

    /**
     * A 1024-length binary
     *
     * <code><pre>
     * B x04 x00 000 01234567890123456789012345678901...
     * </pre></code>
     */
    public Object generateBinary_1024();

    /**
     * A 65536-length binary
     *
     * <code><pre>
     * A x80 x00 000 ...
     * B x04 x00 000 01234567890123456789012345678901...
     * </pre></code>
     */
    public Object generateBinary_65536();

    //
    // lists
    //

    /**
     * Zero-length untyped list
     *
     * <code><pre>
     * x78
     * </pre></code>
     */
    public Object generateUntypedFixedList_0();

    /**
     * 1-length untyped list
     *
     * <code><pre>
     * x79 x01 1
     * </pre></code>
     */
    public Object generateUntypedFixedList_1();

    /**
     * 7-length untyped list
     *
     * <code><pre>
     * x7f x01 1 x01 2 x01 3 x01 4 x01 5 x01 6 x01 7
     * </pre></code>
     */
    public Object generateUntypedFixedList_7();

    /**
     * 8-length untyped list
     *
     * <code><pre>
     * X x98 x01 1 x01 2 x01 3 x01 4 x01 5 x01 6 x01 7 x01 8
     * </pre></code>
     */
    public Object generateUntypedFixedList_8();

    /**
     * Zero-length typed list (String array)
     *
     * <code><pre>
     * x70 x07 [string
     * </pre></code>
     */
    public Object generateTypedFixedList_0();

    /**
     * 1-length typed list (String array)
     *
     * <code><pre>
     * x71 x07 [string x01 1
     * </pre></code>
     */
    public Object generateTypedFixedList_1();

    /**
     * 7-length typed list (String array)
     *
     * <code><pre>
     * x77 x07 [string x01 1 x01 2 x01 3 x01 4 x01 5 x01 6 x01 7
     * </pre></code>
     */
    public Object generateTypedFixedList_7();

    /**
     * 8-length typed list (String array)
     *
     * <code><pre>
     * V x07 [stringx98 x01 1 x01 2 x01 3 x01 4 x01 5 x01 6 x01 7 x01 8
     * </pre></code>
     */
    public Object generateTypedFixedList_8();

    //
    // untyped maps
    //

    /**
     * zero-length untyped map
     *
     * <code><pre>
     * H Z
     * </pre></code>
     */
    public Object generateUntypedMap_0();

    /**
     * untyped map with string key
     *
     * <code><pre>
     * H x01 a x90 Z
     * </pre></code>
     */
    public Object generateUntypedMap_1();

    /**
     * untyped map with int key
     *
     * <code><pre>
     * H x90 x01 a x91 x01 b Z
     * </pre></code>
     */
    public Object generateUntypedMap_2();

    /**
     * untyped map with list key
     *
     * <code><pre>
     * H x71 x01 a x90 Z
     * </pre></code>
     */
    public Object generateUntypedMap_3();

    //
    // typed maps
    //

    /**
     * zero-length typed map
     *
     * <code><pre>
     * M x13 java.lang.Hashtable Z
     * </pre></code>
     */
    public Object generateTypedMap_0();

    /**
     * untyped map with string key
     *
     * <code><pre>
     * M x13 java.lang.Hashtable x01 a x90 Z
     * </pre></code>
     */
    public Object generateTypedMap_1();

    /**
     * typed map with int key
     *
     * <code><pre>
     * M x13 java.lang.Hashtable x90 x01 a x91 x01 b Z
     * </pre></code>
     */
    public Object generateTypedMap_2();

    /**
     * typed map with list key
     *
     * <code><pre>
     * M x13 java.lang.Hashtable x71 x01 a x90 Z
     * </pre></code>
     */
    public Object generateTypedMap_3();

    //
    // objects
    //

    /**
     * Returns a single object
     *
     * <code><pre>
     * C x1a A0 x90 x60
     * </pre></code>
     */
    public Object generateObject_0();

    /**
     * Returns 16 object types
     *
     * <code><pre>
     * X xa0
     *  C x1a A0 x90 x60
     *  C x1a A1 x90 x61
     *  C x1a A2 x90 x62
     *  C x1a A3 x90 x63
     *  C x1a A4 x90 x64
     *  C x1a A5 x90 x65
     *  C x1a A6 x90 x66
     *  C x1a A7 x90 x67
     *  C x1a A8 x90 x68
     *  C x1a A9 x90 x69
     *  C x1b A10 x90 x6a
     *  C x1b A11 x90 x6b
     *  C x1b A12 x90 x6c
     *  C x1b A13 x90 x6d
     *  C x1b A14 x90 x6e
     *  C x1b A15 x90 x6f
     *  C x1b A16 x90 O xa0
     */
    public Object generateObject_16();

    /**
     * Simple object with one field
     *
     * <code><pre>
     * C x22 TestObject x91 x06 _value x60 x90
     * </pre></code>
     */
    public Object generateObject_1();

    /**
     * Simple two objects with one field
     *
     * <code><pre>
     * x7a
     *   C x22 TestObject x91 x06 _value
     *   x60 x90
     *   x60 x91
     * </pre></code>
     */
    public Object generateObject_2();

    /**
     * Simple repeated object
     *
     * <code><pre>
     * x7a
     *   C x22 TestObject x91 x06 _value
     *   x60 x90
     *   Q x91
     * </pre></code>
     */
    public Object generateObject_2a();

    /**
     * Two object with equals
     *
     * <code><pre>
     * x7a
     *   C x22 TestObject x91 x06 _value
     *   x60 x90
     *   x60 x90
     * </pre></code>
     */
    public Object generateObject_2b();

    /**
     * Circular object
     *
     * <code><pre>
     * C x20 TestCons x91 x06 _first x05 _rest
     *   x60 x01 a Q \x90x
     * </pre></code>
     */
    public Object generateObject_3();

}
