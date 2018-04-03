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

import com.alipay.hessian.generic.bean.SimplePerson;
import com.alipay.hessian.generic.util.ClassFilter;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author xuanbei
 * @since 2016/12/28
 */
public class ClassFilterTest {

    @Test
    public void testArrayFilter() throws ClassNotFoundException {
        assertTrue(ClassFilter.arrayFilter(int[].class));
        assertTrue(ClassFilter.arrayFilter(int[][].class));
        assertTrue(ClassFilter.arrayFilter(int[][][].class));
        assertTrue(ClassFilter.arrayFilter(Integer[].class));
        assertTrue(ClassFilter.arrayFilter(Integer[][].class));
        assertTrue(ClassFilter.arrayFilter(Integer[][][].class));

        assertTrue(ClassFilter.arrayFilter(boolean[].class));
        assertTrue(ClassFilter.arrayFilter(boolean[][].class));
        assertTrue(ClassFilter.arrayFilter(boolean[][][].class));
        assertTrue(ClassFilter.arrayFilter(Boolean[].class));
        assertTrue(ClassFilter.arrayFilter(Boolean[][].class));
        assertTrue(ClassFilter.arrayFilter(Boolean[][][].class));

        assertTrue(ClassFilter.arrayFilter(byte[].class));
        assertTrue(ClassFilter.arrayFilter(byte[][].class));
        assertTrue(ClassFilter.arrayFilter(byte[][][].class));
        assertTrue(ClassFilter.arrayFilter(Byte[].class));
        assertTrue(ClassFilter.arrayFilter(Byte[][].class));
        assertTrue(ClassFilter.arrayFilter(Byte[][][].class));

        assertTrue(ClassFilter.arrayFilter(short[].class));
        assertTrue(ClassFilter.arrayFilter(short[][].class));
        assertTrue(ClassFilter.arrayFilter(short[][][].class));
        assertTrue(ClassFilter.arrayFilter(Short[].class));
        assertTrue(ClassFilter.arrayFilter(Short[][].class));
        assertTrue(ClassFilter.arrayFilter(Short[][][].class));

        assertTrue(ClassFilter.arrayFilter(long[].class));
        assertTrue(ClassFilter.arrayFilter(long[][].class));
        assertTrue(ClassFilter.arrayFilter(long[][][].class));
        assertTrue(ClassFilter.arrayFilter(Long[].class));
        assertTrue(ClassFilter.arrayFilter(Long[][].class));
        assertTrue(ClassFilter.arrayFilter(Long[][][].class));

        assertTrue(ClassFilter.arrayFilter(float[].class));
        assertTrue(ClassFilter.arrayFilter(float[][].class));
        assertTrue(ClassFilter.arrayFilter(float[][][].class));
        assertTrue(ClassFilter.arrayFilter(Float[].class));
        assertTrue(ClassFilter.arrayFilter(Float[][].class));
        assertTrue(ClassFilter.arrayFilter(Float[][][].class));

        assertTrue(ClassFilter.arrayFilter(double[].class));
        assertTrue(ClassFilter.arrayFilter(double[][].class));
        assertTrue(ClassFilter.arrayFilter(double[][][].class));
        assertTrue(ClassFilter.arrayFilter(Double[].class));
        assertTrue(ClassFilter.arrayFilter(Double[][].class));
        assertTrue(ClassFilter.arrayFilter(Double[][][].class));

        assertTrue(ClassFilter.arrayFilter(char[].class));
        assertTrue(ClassFilter.arrayFilter(char[][].class));
        assertTrue(ClassFilter.arrayFilter(char[][][].class));
        assertTrue(ClassFilter.arrayFilter(Character[].class));
        assertTrue(ClassFilter.arrayFilter(Character[][].class));
        assertTrue(ClassFilter.arrayFilter(Character[][][].class));

        assertTrue(ClassFilter.arrayFilter(String[].class));
        assertTrue(ClassFilter.arrayFilter(String[][].class));
        assertTrue(ClassFilter.arrayFilter(String[][][].class));

        assertFalse(ClassFilter.arrayFilter(SimplePerson[].class));
        assertFalse(ClassFilter.arrayFilter(SimplePerson[][].class));
        assertFalse(ClassFilter.arrayFilter(SimplePerson[][][].class));

        assertFalse(ClassFilter.arrayFilter(Date[].class));
        assertFalse(ClassFilter.arrayFilter(Date[][].class));
        assertFalse(ClassFilter.arrayFilter(Date[][][].class));

        assertFalse(ClassFilter.arrayFilter(Object[].class));
        assertFalse(ClassFilter.arrayFilter(Object[][].class));
        assertFalse(ClassFilter.arrayFilter(Object[][][].class));

        assertEquals("[Lcom.alipay.hessian.generic.bean.SimplePerson;",
            SimplePerson[].class.getName());
        assertEquals("[[Lcom.alipay.hessian.generic.bean.SimplePerson;",
            SimplePerson[][].class.getName());
        assertEquals("[[[Lcom.alipay.hessian.generic.bean.SimplePerson;",
            SimplePerson[][][].class.getName());

        assertEquals(Class.forName("[Lcom.alipay.hessian.generic.bean.SimplePerson;"),
            SimplePerson[].class);
        assertEquals(Class.forName("[[Lcom.alipay.hessian.generic.bean.SimplePerson;"),
            SimplePerson[][].class);
        assertEquals(Class.forName("[[[Lcom.alipay.hessian.generic.bean.SimplePerson;"),
            SimplePerson[][][].class);
    }

    @Test
    public void ttt() {
        System.out.println(Class.class.getName());
    }
}
