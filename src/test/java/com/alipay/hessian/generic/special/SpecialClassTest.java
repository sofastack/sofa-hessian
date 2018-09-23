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
package com.alipay.hessian.generic.special;

import com.alipay.hessian.generic.bean.ArrayBean;
import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.alipay.hessian.generic.model.GenericClass;
import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.hessian.generic.util.GenericUtils;
import com.caucho.hessian.io.CalendarHandle;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.test.Color;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author xuanbei
 * @since 2016/12/29
 */
public class SpecialClassTest {

    /**
     * 这个用例说明hessian写入的InputStram,反序列化时是byte[], 不兼容
     * @throws Exception
     */
    @Test
    public void testInputStream() throws Exception {
        InputStream inputStream = new FileInputStream("pom.xml");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());
        hout.writeObject(inputStream);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Object o = hin.readObject();
        assertEquals(byte[].class, o.getClass());
    }

    /**
     * 这个用例说明目前的GenericObject结构是支持Throwable的
     * @throws Exception
     */
    @Test
    public void testThrowable() throws Exception {
        MyException ex = new MyException("hello exception!");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());
        hout.writeObject(ex);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertEquals(GenericObject.class, o.getClass());
        assertEquals(MyException.class.getName(), ((GenericObject) o).getType());
        MyException myException = GenericUtils.convertToObject(o);
        assertEquals(myException.getMessage(), "hello exception!");
    }

    /**
     * 这个用例说明hessian写入的Iterator,反序列化时是ArrayList, 不兼容
     * @throws Exception
     */
    @Test
    public void testIterator() throws Exception {

        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        Iterator<Integer> iterable = list.iterator();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());
        hout.writeObject(iterable);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Object o = hin.readObject();
        assertEquals(ArrayList.class, o.getClass());
    }

    /**
     * 这个用例说明hessian写入的Enumeration,反序列化时时是ArrayList, 不兼容
     * @throws Exception
     */
    @Test
    public void testEnumeration() throws Exception {

        MyEnumerator myEnumerator = new MyEnumerator(0, 5, new Object[] { 1, 2, 3, 4, 5 });
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());
        hout.writeObject(myEnumerator);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Object o = hin.readObject();
        assertEquals(ArrayList.class.getName(), o.getClass().getName());
    }

    @Test
    public void testGregorianCalendar() throws Exception {

        Calendar calendar = new GregorianCalendar();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(calendar);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Object o = hin.readObject();
        assertEquals(GregorianCalendar.class, o.getClass());
        assertEquals(calendar, o);

        Object obj = GenericUtils.convertToGenericObject(calendar);
        assertEquals(GregorianCalendar.class, obj.getClass());
        assertEquals(calendar, obj);
        bout = new ByteArrayOutputStream();
        hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(obj);
        hout.close();

        body = bout.toByteArray();
        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        o = hin.readObject();
        assertEquals(GregorianCalendar.class, o.getClass());
        assertEquals(calendar, o);

        Object object = GenericUtils.convertToObject(obj);
        assertEquals(GregorianCalendar.class, object.getClass());
        assertEquals(calendar, object);
    }

    @Test
    public void testMyCalendar1() throws Exception {

        Calendar calendar = new MyCalendar();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(calendar);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Object o = hin.readObject();
        assertEquals(MyCalendar.class, o.getClass());
        assertEquals(calendar, o);

        Object obj = GenericUtils.convertToGenericObject(calendar);
        assertEquals(GenericObject.class, obj.getClass());
        bout = new ByteArrayOutputStream();
        hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(obj);
        hout.close();

        body = bout.toByteArray();
        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        o = hin.readObject();
        assertEquals(MyCalendar.class, o.getClass());
        assertEquals(calendar, o);

        Object object = GenericUtils.convertToObject(obj);
        assertEquals(MyCalendar.class, object.getClass());
        assertEquals(calendar, object);
    }

    /**
     * 检测Enum的序列化情况
     * @throws Exception
     */
    @Test
    public void testEnum() throws Exception {

        Color color = Color.BLANK;

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());
        hout.writeObject(color);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertEquals(GenericObject.class, o.getClass());
        Object obj = GenericUtils.convertToObject(o);
        assertEquals(obj.getClass(), Color.class);
        assertTrue(obj == Color.BLANK);

        GenericObject col = GenericUtils.convertToGenericObject(obj);
        bout = new ByteArrayOutputStream();
        hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(col);
        hout.close();

        body = bout.toByteArray();
        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());
        o = hin.readObject();
        assertEquals(o.getClass(), Color.class);
        assertTrue(o == Color.BLANK);
    }

    /**
     * 检测Calendar的序列化情况
     * @throws Exception
     */
    @Test
    public void testMyCalendar() throws Exception {

        Calendar calendar = new MyCalendar();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());
        hout.writeObject(calendar);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Object o = hin.readObject();
        assertEquals(MyCalendar.class, o.getClass());
        assertEquals(calendar, o);

        Object obj = GenericUtils.convertToGenericObject(calendar);
        assertEquals(GenericObject.class, obj.getClass());
        bout = new ByteArrayOutputStream();
        hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(obj);
        hout.close();

        body = bout.toByteArray();
        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        o = hin.readObject();
        assertEquals(MyCalendar.class, o.getClass());
        assertEquals(calendar, o);

        Object object = GenericUtils.convertToObject(obj);
        assertEquals(MyCalendar.class, object.getClass());
        assertEquals(calendar, object);

        body = bout.toByteArray();
        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        o = hin.readObject();
        assertEquals(o.getClass(), GenericObject.class);
        assertEquals(((GenericObject) o).getType(), CalendarHandle.class.getName());

        Object ob = GenericUtils.convertToObject(o);
        assertEquals(MyCalendar.class, ob.getClass());
        assertEquals(calendar, ob);
    }

    /**
     * 这个用例说明hessian写入的Enumeration,反序列化时时是ArrayList, 不兼容
     * @throws Exception
     */
    @Test
    public void testEnumerationConvert() throws Exception {

        MyEnumerator myEnumerator = new MyEnumerator(0, 5, new Object[] { 1, 2, 3, 4, 5 });
        Object obj = GenericUtils.convertToGenericObject(myEnumerator);
        assertEquals(ArrayList.class.getName(), obj.getClass().getName());

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());
        hout.writeObject(myEnumerator);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Object o = hin.readObject();
        assertEquals(ArrayList.class.getName(), o.getClass().getName());

        bout = new ByteArrayOutputStream();
        hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(obj);
        hout.close();

        body = bout.toByteArray();
        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        List l1 = (List) hin.readObject();
        List l2 = (List) obj;
        assertEquals(l1.size(), l2.size());
        assertEquals(l1.get(0), l2.get(0));
        assertEquals(l1.get(1), l2.get(1));
        assertEquals(l1.get(2), l2.get(2));
        assertEquals(l1.get(3), l2.get(3));
        assertEquals(l1.get(4), l2.get(4));
    }

    @Test
    public void testSpecialBean() throws Exception {

        SpecialBean specialBean = new SpecialBean(new MyCalendar(), ArrayBean.class);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(specialBean);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o = hin.readObject();
        assertEquals(GenericObject.class, o.getClass());
        assertEquals(GenericClass.class.getName(), ((GenericObject) o).getField("clazz").getClass()
            .getName());

        Object obj = GenericUtils.convertToGenericObject(specialBean);

        Object o1 = GenericUtils.convertToObject(obj);
        Object o2 = GenericUtils.convertToObject(o);

        assertEquals(o1, o2);
        assertEquals(o1, specialBean);
        bout = new ByteArrayOutputStream();
        hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(obj);
        hout.close();

        body = bout.toByteArray();
        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        specialBean = new SpecialBean(new MyCalendar(), Class.class);
        bout = new ByteArrayOutputStream();
        hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(specialBean);
        hout.close();

        body = bout.toByteArray();
        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        o = hin.readObject();
        assertEquals(GenericObject.class, o.getClass());
        assertEquals(Class.class.getName(), ((GenericObject) o).getField("clazz").getClass()
            .getName());
        assertEquals(Class.class, ((GenericObject) o).getField("clazz"));

        obj = GenericUtils.convertToGenericObject(specialBean);

        o1 = GenericUtils.convertToObject(obj);
        o2 = GenericUtils.convertToObject(o);

        assertEquals(o1, o2);
        bout = new ByteArrayOutputStream();
        hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(obj);
        hout.close();

        body = bout.toByteArray();
        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());
    }
}
